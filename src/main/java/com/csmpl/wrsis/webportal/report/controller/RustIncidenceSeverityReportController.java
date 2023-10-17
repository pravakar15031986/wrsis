package com.csmpl.wrsis.webportal.report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
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

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.RustIncidenceSeverityReportPdf;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.excel.RustIncidenceSeverityReportExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class RustIncidenceSeverityReportController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(RustIncidenceSeverityReportController.class);
	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;
	@Autowired
	DashboardService dashboardService;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	DemographicRepository demographicRepository;

	@RequestMapping(value = { "/incidence-Severity-RustReport" })
	public String incidenceSeverityRustReport(Model model) throws JSONException {

		String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		JSONObject jso = new JSONObject(yearSeason);
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();

		String dataResp = surveyorDetailsRepository.fetchRustSeverityReport(jso.getString("year"),
				jso.getInt("seasonid"), 0, 0);
		model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
				new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));

		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(yearSeason.getBytes())));

		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("RegionList", regions);
		return "incidenceSeverityRustReport";
	}

	@RequestMapping(value = { "/searchIncidence-Severity-RustReport" })
	public String searchIncidenceSeverityRustReport(Model model, @RequestParam("Year") String year,
			@RequestParam("SeasonId") Integer seasonId, @RequestParam("RegionId") Integer regionId)
			throws JSONException {
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();
		String dataResp = surveyorDetailsRepository.fetchRustSeverityReport(year, seasonId, regionId, 0);
		model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
				new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));

		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(yearSeason.getBytes())));

		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("RegionList", regions);
		model.addAttribute("SelectedRegion", regionId);
		model.addAttribute("SelectedYear", year);
		model.addAttribute("SelectedSeasonId", seasonId);
		return "incidenceSeverityRustReport";

	}

	// Download Rust Prevalence Report excel

	@RequestMapping(value = "incidence-Severity-RustReportExcel", method = RequestMethod.POST)
	public void rustPrevalenceReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("YearXL") String year, @RequestParam("SeasonIdXL") Integer seasonId,
			@RequestParam("RegionIdXL") Integer regionId) throws IOException {
		try {

			String dataResp = surveyorDetailsRepository.fetchRustSeverityReport(year, seasonId, regionId, 0);

			RustIncidenceSeverityReportExcel reportExcel = new RustIncidenceSeverityReportExcel();

			HSSFWorkbook excelBook = reportExcel.generateRustIncidentSeverityExcel(dataResp);
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Rust_Incidence_Severity_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {

			LOG.error("RustIncidenceSeverityReportController::rustPrevalenceReportExcelDownload():" + e);
		}

	}

	// down load Rust Prevalence Report excel

	@RequestMapping(value = "incidence-Severity-RustReportPdf", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> rustPrevalenceReportPDFDownload(HttpServletResponse response,
			HttpSession session, Model model, @RequestParam("Year") String year,
			@RequestParam("SeasonId") Integer seasonId, @RequestParam("RegionId") Integer regionId)
			throws IOException, JSONException {

		String dataResp = surveyorDetailsRepository.fetchRustSeverityReport(year, seasonId, regionId, 0);

		RustIncidenceSeverityReportPdf spdf = new RustIncidenceSeverityReportPdf();
		ByteArrayInputStream bar = spdf.generateRustIncidenceSeverityReportPdf(dataResp);
		HttpHeaders headers = new HttpHeaders();
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat("yyyy-MMM-dd HH_mm_ss").format(ts);
		String FILENAME = "Survey_Details_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));

	}

}
