package com.csmpl.wrsis.webportal.report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.RaceReportService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.ReportPDF;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.excel.CommonExcelFormatDownloadObjectArray;
import com.csmpl.wrsis.webportal.excel.RaceBySample;
import com.csmpl.wrsis.webportal.excel.ReportExcel;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;

@Controller
public class RaceReportControler extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(RaceReportControler.class);
	@Autowired
	RaceReportService raceReportService;
	@Autowired
	TypeOfRustService typeOfRustService;
	@Autowired
	ResearchCenterService researchCenterService;
	@Autowired
	RustTypeRepository rustTypeRepository;

	@RequestMapping(value = { "/stemRustRaces-IdentifiedCropping" })
	public String stemRustRacesIdentifiedCroppingSeasonTable(Model model, HttpSession session) throws Exception {
		JSONObject response = new JSONObject();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			response = raceReportService.fetchRaceReportResultWise(userId, 1);
		} catch (Exception e) {
			LOG.error("RaceReportControler::stemRustRacesIdentifiedCroppingSeasonTable():" + e);
		} // avoid 500 internal server error
		model.addAttribute("TableData", new String(Base64.getEncoder().encode(response.toString().getBytes())));
		List<RustTypeMasterEntity> rusttypes = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("Rusttypes", rusttypes);
		model.addAttribute("RustType", 1);
		model.addAttribute("SearchShow", true);
		return "stemRustRacesIdentifiedCroppingSeasonTable";
	}

	@RequestMapping(value = { "/stemRustRaces-IdentifiedCroppingSearch" })
	public String IdentifiedCroppingSearch(Model model, HttpSession session, @RequestParam("RustType") Integer rustType)
			throws Exception {
		JSONObject response = new JSONObject();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			response = raceReportService.fetchRaceReportResultWise(userId, rustType);
		} catch (Exception e) {
			LOG.error("RaceReportControler::IdentifiedCroppingSearch():" + e);

		} // avoid 500 internal server error
		model.addAttribute("TableData", new String(Base64.getEncoder().encode(response.toString().getBytes())));
		List<RustTypeMasterEntity> rusttypes = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("Rusttypes", rusttypes);
		model.addAttribute("RustType", rustType);
		model.addAttribute("SearchShow", true);
		return "stemRustRacesIdentifiedCroppingSeasonTable";
	}

	@RequestMapping(value = { "/viewReportDetails" })
	public String viewReportDetails(Model model, SearchVo searchVo) {
		model.addAttribute("rustTypeList", typeOfRustService.vewAllTypeOFRust());
		model.addAttribute("laboratories", researchCenterService.getResearchCenterListOrder());
		model.addAttribute("RustType", searchVo.getRustId());
		model.addAttribute("viewracelist", raceReportService.getDetails(searchVo));
		model.addAttribute("SearchVo", searchVo);

		return "viewReportDetails";
	}

	@RequestMapping(value = "api/download-Broker Tea Stock excel-Summary", method = RequestMethod.GET)
	public ModelAndView downloadexcelWareTeaTracking(HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<>();

		// TeaOverView requestData =

		List<Object[]> viewDataList = new ArrayList<>();
		Object[] obj = { "Leaf Rust", "S102", "Ambo", "JGJJ", "12-20-2012" };
		viewDataList.add(obj);
		Object[] obj1 = { "Yellow Rust", "S103", "Ambo1", "JGJJdfsdf", "12-20-2018" };
		viewDataList.add(obj1);
		String[] columns = { "Rust type", "Survey No", "Lab", "Race", "Publish Date" };
		map.put("ExcelContent", viewDataList);
		map.put("Columns", columns);
		map.put("ReportName", "Testing Report");

		response.setContentType("application/ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");

		return new ModelAndView(new CommonExcelFormatDownloadObjectArray(), "VIEW_DATA_LIST", map);

	}

	// Report Excel Download
	@RequestMapping(value = "downloadReportExcel", method = RequestMethod.POST)
	public void downloadReportExcel(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam(WrsisPortalConstant.RUSTID) Integer rustId,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("raceResult") String raceResult,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam("surveyDateFrom") String surveyDateFrom,
			@RequestParam("surveyDateTo") String surveyDateTo, @RequestParam("labId") Integer labId)
			throws IOException {
		ReportExcel reportExcel = new ReportExcel();
		List<SampleTagBean> beans = raceReportService.reportExcel(surveyNo, sampleId, surveyDateFrom, surveyDateTo,
				raceResult, regionId, rustId, labId);

		HSSFWorkbook myExcelBook = reportExcel.buildReportExcel(beans);

		
		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Report_" + formattedDate + ".xls");

		OutputStream out = response.getOutputStream();
		myExcelBook.write(out);

		out.flush();
		out.close();
		myExcelBook.close();
	}

	// Report PDF
	@RequestMapping(value = { "/downloadReportPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadReportPdf(HttpServletResponse response, HttpSession session,
			Model model, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam(WrsisPortalConstant.RUSTID) Integer rustId,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("raceResult") String raceResult,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam("surveyDateFrom") String surveyDateFrom,
			@RequestParam("surveyDateTo") String surveyDateTo, @RequestParam("labId") Integer labId)
			throws IOException {

		List<SampleTagBean> beans = raceReportService.reportExcel(surveyNo, sampleId, surveyDateFrom, surveyDateTo,
				raceResult, regionId, rustId, labId);

		ReportPDF psPdf = new ReportPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.generateReportPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "Report_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// down load Race By Sample Report excel

	@RequestMapping(value = "raceBySampleReportExcelDownload", method = RequestMethod.POST)
	public void raceBySampleReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("RustTypeXL") Integer RustTypeXL) throws IOException {

		try {

			JSONObject responsedata = new JSONObject();

			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			try {
				responsedata = raceReportService.fetchRaceReportResultWise(userId, RustTypeXL);
			} catch (Exception e) {
				LOG.error("RaceReportControler::raceBySampleReportExcelDownload():" + e);
			}

			RaceBySample reportExcel = new RaceBySample();

			HSSFWorkbook excelBook = reportExcel.buildExcelRaceBySampleReportDetails(responsedata);// dataResp
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Race_By_Sample_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {
			LOG.error("RaceReportControler::raceBySampleReportExcelDownload File():" + e);

		}

	}

}