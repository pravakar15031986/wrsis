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
public class RustSurveyOtherDiseaseController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(RustSurveyOtherDiseaseController.class);

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

	@GetMapping("/rustSurveyOtherDisease")
	public String rustSurveyOtherDiseaseReport(HttpSession session, Model model) {
		try {

			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			JSONObject jso = new JSONObject(yearSeason);
			String year = jso.getString(WrsisPortalConstant.YEAR);
			Integer seasonId = jso.getInt("seasonid");

			DateUtil.getYearList().length();

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

			List<Object[]> details = surveyDetailsRepository.getRustSurveyOtherDiseaseReport(0, "", "", "", 1, seasonId,
					0, 0, year, 0, 10);

			Integer count = surveyDetailsRepository.countTotalRustSurveyOtherDiseaseReportTable(0, "", "", "", 1,
					seasonId, 0, 0, year, -1, -1);

			model.addAttribute("Count", count);
			model.addAttribute("SelectedPage", 1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			List<SuveyDetailsSampleReportDataBean> myExcelBook = surveyExcelDetailsService
					.createRustSurveyOtherDiseaseTable(beans);
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
			model.addAttribute("seasonWiseData", 0);
			model.addAttribute("dataLength", myExcelBook.size());
		} catch (Exception e) {

			LOG.error("RustSurveyOtherDiseaseController::rustSurveyOtherDiseaseReport():" + e);
		}

		return "rustSurveyOtherDisease";
	}

	@PostMapping("/rustSurveyOtherDiseaseSearch")
	public String reportSurveyOtherDiseaseSearch(HttpSession session, Model model,
			@RequestParam("yearId") String yearId, @RequestParam("seasionId") Integer seasionId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("rcId") Integer rcId,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam("pageindex") Integer pageindex,
			@RequestParam("searchBySeason") Integer searchBySeason) {
		try {
			if ("0".equals(yearId) && seasionId == 0) {
				String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
				JSONObject jso = new JSONObject(yearSeason);
				yearId = jso.getString(WrsisPortalConstant.YEAR);
				seasionId = jso.getInt("seasonid");
			}

			DateUtil.getYearList().length();

			ArrayList<String> yearList = new ArrayList<>();
			for (int i = 0; i < DateUtil.getYearList().length(); i++) {
				yearList.add(String.valueOf(DateUtil.getYearList().get(i)));
			}

			model.addAttribute("yearList", yearList);

			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, yearId);

			model.addAttribute("SeasonId", seasionId);

			model.addAttribute(WrsisPortalConstant.YEAR, yearId);
			model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
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

			List<Object[]> details = surveyDetailsRepository.getRustSurveyOtherDiseaseReport(regionId, sDate, eDate,
					surveyNo.toUpperCase().trim(), 1, seasionId, rcId, 0, yearId, (pageindex - 1) * 10, 10);
			Integer count = surveyDetailsRepository.countTotalRustSurveyOtherDiseaseReportTable(regionId, sDate, eDate,
					surveyNo.toUpperCase().trim(), 1, seasionId, rcId, 0, yearId, -1, -1);

			model.addAttribute("SelectedPage", pageindex);
			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			List<SuveyDetailsSampleReportDataBean> myExcelBook = surveyExcelDetailsService
					.createRustSurveyOtherDiseaseTable(beans);
			model.addAttribute("ExcelDataList", myExcelBook);
			if (myExcelBook.size() > 0)
				model.addAttribute("Count", count);
			else
				model.addAttribute("Count", myExcelBook.size());
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
			model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
			if (searchBySeason == 0) {
				model.addAttribute("seasonWiseData", 0);
			} else {
				model.addAttribute("dateWiseData", 1);
			}

			model.addAttribute("dataLength", myExcelBook.size());
		} catch (Exception e) {

			LOG.error("RustSurveyOtherDiseaseController::reportSurveyOtherDiseaseSearch():" + e);

		}

		return "rustSurveyOtherDisease";
	}

	@RequestMapping(value = "reportSurveyRustOtherDiseaseExcel", method = RequestMethod.POST)
	public void downloadSurveyRustOtherDiseaseExcel(HttpServletResponse response, HttpSession session, Model model,
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

			List<Object[]> details = surveyDetailsRepository.getRustSurveyOtherDiseaseReport(regionId, sDate, eDate,
					surveyNo.toUpperCase().trim(), 1, seasionId, rcId, 0, yearId, -1, -1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			List<Object[]> obj = commonService.getInstitutionNam();
			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf((obj.get(0))[0])))
					: null;

			HSSFWorkbook myExcelBook = surveyExcel.buildOtherDiseaseExcelPlotData(rustTypeRepository,
					sampleTypeRepository, diseaseMasterRepository, seasionMasterRepository, demographyRepository,
					siteTypeRepository, wheatTypeRepository, varietyTypeRepository, growthRepository,
					reactionTypeRepository, fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository,
					moistureRepository, researchCenterId);

			myExcelBook = surveyExcelDetailsService.createExcelRustSurveyOtherDisease(myExcelBook, beans);
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Other_Disease_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			myExcelBook.write(out);
			out.flush();
			out.close();
			myExcelBook.close();
		} catch (Exception e) {

			LOG.error("RustSurveyOtherDiseaseController::downloadSurveyRustOtherDiseaseExcel():" + e);

		}

	}

	@RequestMapping(value = "reportSurveyRustOtherDiseaseCSV", method = RequestMethod.POST)
	public void downloadSurveyRustOtherDiseaseCSVFormat(HttpServletResponse response, HttpSession session, Model model,
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

			List<Object[]> details = surveyDetailsRepository.getRustSurveyOtherDiseaseReport(regionId, sDate, eDate,
					surveyNo.trim().toUpperCase(), 1, seasionId, rcId, 0, yearId, -1, -1);

			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				beans.add(sdb);
			}

			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			List<Object[]> obj = commonService.getInstitutionNam();
			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf((obj.get(0))[0])))
					: null;

			HSSFWorkbook myExcelBook = surveyExcel.buildOtherDiseaseCSVHeader(rustTypeRepository, sampleTypeRepository,
					diseaseMasterRepository, seasionMasterRepository, demographyRepository, siteTypeRepository,
					wheatTypeRepository, varietyTypeRepository, growthRepository, reactionTypeRepository,
					fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository, moistureRepository,
					researchCenterId);

			myExcelBook = surveyExcelDetailsService.createCsvRustSurveyOtherDisease(myExcelBook, beans);

			HSSFSheet selSheet = myExcelBook.getSheetAt(0);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i <= selSheet.getLastRowNum(); i++) {
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
						LOG.error("RustSurveyOtherDiseaseController::downloadSurveyRustOtherDiseaseCSVFormat():" + e);

					}
				}
				sb.append("\r\n");
			}

			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);

			String FILENAME = "Other Disease_" + timeStamp + ".csv";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);

			OutputStream out = response.getOutputStream();
			out.write(sb.toString().getBytes());

		} catch (Exception e) {

			LOG.error("RustSurveyOtherDiseaseController::downloadSurveyRustOtherDiseaseCSVFormat():" + e);

		}

	}
}
