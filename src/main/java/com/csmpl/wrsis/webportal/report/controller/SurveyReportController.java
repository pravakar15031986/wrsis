package com.csmpl.wrsis.webportal.report.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SuveyDetailsSampleReportDataBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.IVRDataService;
import com.csmpl.wrsis.webportal.service.ManageIVRDataService;
import com.csmpl.wrsis.webportal.service.SurveyExcelDetailsService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class SurveyReportController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyReportController.class);

	@Autowired
	RustTypeRepository rustTypeRepository;
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;
	@Autowired
	SeasionMasterRepository seasionMasterRepository;
	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	FungicideMasterRepository fungicideMasterRepository;

	@Autowired
	FungiEffectTypeRepository fungiEffectTypeRepository;

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;

	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	IVRDataService iVRDataService;
	@Autowired
	ManageIVRDataService manageIVRDataService;
	@Autowired
	SurveyExcelDetailsService surveyExcelDetailsService;
	@Autowired
	GrowthRepository growthRepository;
	@Autowired
	SiteTypeRepository siteTypeRepository;
	@Autowired
	WheatTypeRepository wheatTypeRepository;
	@Autowired
	ReactionTypeRepository reactionTypeRepository;
	@Autowired
	SoilColorRepository soilColorRepository;
	@Autowired
	MoistureRepository moistureRepository;
	@Autowired
	CommonService commonService;
	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@GetMapping("/reportSurveyReportByGlobalRust")
	public String reportSurveyReportByGlobalRust(HttpSession session, Model model) {
		String year = null;
		Integer seasonId = null;
		try {

			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			if (yearSeason != null) {
				JSONObject jso = new JSONObject(yearSeason);
				year = jso.getString(WrsisPortalConstant.YEAR);
				seasonId = jso.getInt("seasonid");
			}

			ArrayList<String> yearList = new ArrayList<>();
			for (int i = 0; i < DateUtil.getYearList().length(); i++) {
				yearList.add(String.valueOf(DateUtil.getYearList().get(i)));
			}

			model.addAttribute("yearList", yearList);
			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
			model.addAttribute("SeasonId", seasonId);
			SeasionMasterEntity season = seasionMasterRepository.getOne(seasonId);

			model.addAttribute("seasonName", season);
			model.addAttribute(WrsisPortalConstant.YEAR, year);
			List<Object[]> details = surveyDetailsRepository.getGlobalRustReport(0, "", "", "", 1, seasonId, 0, 0, year,
					0, 10);
			Integer count = surveyDetailsRepository.countTotalGlobalRustReportTable(0, "", "", "", 1, seasonId, 0, 0,
					year, -1, -1);
			model.addAttribute("Count", count);
			model.addAttribute("SelectedPage", 1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			List<SuveyDetailsSampleReportDataBean> myExcelBook = surveyExcelDetailsService.createGRTableSurvey(beans);
			model.addAttribute("ExcelDataList", myExcelBook);

			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();

			model.addAttribute("rustSize", rustTypeList);

			List<SampleTypeMaster> sampleList = sampleTypeRepository.fetchAllActiveSampletypes();
			model.addAttribute("sampleList", sampleList);
			List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
			model.addAttribute(WrsisPortalConstant.OTHER_DISEASE1, diseaseList);
			List<DiseaseMaster> diseaseListAny = diseaseMasterRepository.fetchAllActiveDisease(false);
			model.addAttribute("diseaseListAny", diseaseListAny);

			List<SeasionMasterEntity> seasonList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute("seasonList", seasonList);

			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();

			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);

			List<ResearchCenter> rcList = researchCenterRepository.findActiveResearchCenter();
			model.addAttribute(WrsisPortalConstant.RC_LIST, rcList);
		} catch (Exception e) {
			LOG.error("SurveyReportController::reportSurveyReportByGlobalRust():" + e);
		}

		return "reportSurveyReportByGlobalRust";
	}

	@PostMapping("/reportSurveyReportByGlobalRustSearch")
	public String reportSurveyReportByGlobalRustSearch(HttpSession session, Model model,
			@RequestParam("yearId") String yearId, @RequestParam("seasionId") Integer seasionId,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("rcId") Integer rcId,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam("pageindex") Integer pageindex,
			@RequestParam("status") Integer status) {
		model.addAttribute("status", status);
		try {

			DateUtil.getYearList().length();

			ArrayList<String> yearList = new ArrayList<>();
			for (int i = 0; i < DateUtil.getYearList().length(); i++) {
				yearList.add(String.valueOf(DateUtil.getYearList().get(i)));
			}

			model.addAttribute("yearList", yearList);

			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, yearId);

			model.addAttribute("SeasonId", seasionId);

			model.addAttribute(WrsisPortalConstant.YEAR, yearId);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute("rcId", rcId);
			model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.getGlobalRustReport(regionId, sDate, eDate,
					surveyNo.toUpperCase().trim(), 1, seasionId, rcId, 0, yearId, (pageindex - 1) * 10, 10);
			Integer count = surveyDetailsRepository.countTotalGlobalRustReportTable(regionId, sDate, eDate,
					surveyNo.toUpperCase().trim(), 1, seasionId, rcId, 0, yearId, -1, -1);

			model.addAttribute("Count", count);
			model.addAttribute("SelectedPage", pageindex);
			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			List<SuveyDetailsSampleReportDataBean> myExcelBook = surveyExcelDetailsService.createGRTableSurvey(beans);
			model.addAttribute("ExcelDataList", myExcelBook);

			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();

			model.addAttribute("rustSize", rustTypeList);

			List<SampleTypeMaster> sampleList = sampleTypeRepository.fetchAllActiveSampletypes();
			model.addAttribute("sampleList", sampleList);
			List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
			model.addAttribute(WrsisPortalConstant.OTHER_DISEASE1, diseaseList);
			List<DiseaseMaster> diseaseListAny = diseaseMasterRepository.fetchAllActiveDisease(false);
			model.addAttribute("diseaseListAny", diseaseListAny);

			List<SeasionMasterEntity> seasonList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute("seasonList", seasonList);

			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();

			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);

			List<ResearchCenter> rcList = researchCenterRepository.findActiveResearchCenter();
			model.addAttribute(WrsisPortalConstant.RC_LIST, rcList);
		} catch (Exception e) {

			LOG.error("SurveyReportController::reportSurveyReportByGlobalRustSearch():" + e);

		}

		return "reportSurveyReportByGlobalRust";
	}

	@RequestMapping(value = "reportSurveyReportByGlobalRustExcel", method = RequestMethod.POST)
	public void downloadSurveyExcelFormat(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("yearIdExcel") String yearId, @RequestParam("seasionIdExcel") Integer seasionId,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("rcIdExcel") Integer rcId,
			@RequestParam("surveyNoExcel") String surveyNo) throws IOException {
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.getGlobalRustReport(regionId, sDate, eDate, surveyNo, 1,
					seasionId, rcId, 0, yearId, -1, -1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			List<Object[]> obj = commonService.getInstitutionNam();
			Integer researchCenterId = (obj != null && !(obj.isEmpty()))
					? (Integer) (Integer.valueOf(String.valueOf(((Object[]) obj.get(0))[0])))
					: null;

			HSSFWorkbook myExcelBook = surveyExcel.buildGlobalRustExcelPlotData(session, rustTypeRepository,
					sampleTypeRepository, diseaseMasterRepository, seasionMasterRepository, demographyRepository,
					siteTypeRepository, wheatTypeRepository, varietyTypeRepository, growthRepository,
					reactionTypeRepository, fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository,
					moistureRepository, researchCenterId);
			myExcelBook = surveyExcelDetailsService.createExcelGlobalRust(myExcelBook, beans);
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat("ssmmHHddMMyyyy").format(ts);
			String FILENAME = "Global_Rust_Details_" + timeStamp + ".xls";
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			myExcelBook.write(out);
			out.flush();
			out.close();
			myExcelBook.close();
		} catch (Exception e) {
			LOG.error("SurveyReportController::downloadSurveyExcelFormat():" + e);

		}

	}

	@RequestMapping(value = "reportSurveyReportByGlobalRustCsv", method = RequestMethod.POST)
	public void downloadReportByGlobalRustCsv(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("yearIdExcel") String yearId, @RequestParam("seasionIdExcel") Integer seasionId,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("rcIdExcel") Integer rcId,
			@RequestParam("surveyNoExcel") String surveyNo) throws IOException {
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.getGlobalRustReport(regionId, sDate, eDate, surveyNo, 1,
					seasionId, rcId, 0, yearId, -1, -1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			List<Object[]> obj = commonService.getInstitutionNam();
			Integer researchCenterId = (obj != null && !(obj.isEmpty()))
					? (Integer) (Integer.valueOf(String.valueOf(((Object[]) obj.get(0))[0])))
					: null;

			HSSFWorkbook myExcelBook = surveyExcel.buildGlobalRusCSVHeaderPlot(rustTypeRepository,
					sampleTypeRepository, diseaseMasterRepository, seasionMasterRepository, demographyRepository,
					siteTypeRepository, wheatTypeRepository, varietyTypeRepository, growthRepository,
					reactionTypeRepository, fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository,
					moistureRepository, researchCenterId);
			myExcelBook = surveyExcelDetailsService.createCsvGlobalRustSurvey(myExcelBook, beans);

			HSSFSheet selSheet = myExcelBook.getSheetAt(0);
			StringBuffer sb = new StringBuffer();
			for (int i = 3; i <= selSheet.getLastRowNum(); i++) {
				if (i == 1 || i == 2) {
					continue;
				}
				Row hrow = selSheet.getRow(0);
				for (int j = 0; j < hrow.getLastCellNum(); j++) {
					try {
						Cell cell = selSheet.getRow(i).getCell(j);
						switch (cell.getCellTypeEnum()) {
						case STRING:
							sb.append(cell.getStringCellValue().replaceAll(",", "#"));
							break;
						case NUMERIC:
							sb.append(cell.getNumericCellValue());
							break;
						case BOOLEAN:
							sb.append(cell.getBooleanCellValue());
							break;
						default:

						}

						sb.append(",");
					} catch (Exception e) {
						sb.append("");
						sb.append(",");
					}
				}
				sb.append("\r\n");
			}

			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);

			String FILENAME = "Global Rust Details_" + timeStamp + ".csv";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);

			OutputStream out = response.getOutputStream();
			out.write(sb.toString().getBytes());

		} catch (Exception e) {

			LOG.error("SurveyReportController::downloadReportByGlobalRustCsv():" + e);

		}

	}
}
