package com.csmpl.wrsis.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.MonitorReportDashboardDataTable;
import com.csmpl.wrsis.datatable.RaceAnalysisDashboardReportDataTable;
import com.csmpl.wrsis.datatable.RustReportDashboardDatatable;
import com.csmpl.wrsis.pdf.ImplementationMonitorPDF;
import com.csmpl.wrsis.pdf.RaceAnalysisDetailsPDF;
import com.csmpl.wrsis.pdf.RecomendationSurveyDetailsPDF;
import com.csmpl.wrsis.pdf.RustIncidentPDF;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.DataCollectModeEntity;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SeasonMaster;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.excel.PublishSurveyExcel;
import com.csmpl.wrsis.webportal.repository.DataCollectModeRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMasterRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.SubReportService;
import com.csmpl.wrsis.webportal.service.SurveyPublishOrDiscardService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class SubReportController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(SubReportController.class);

	@Autowired
	SubReportService subReportService;

	@Autowired
	SurveyPublishOrDiscardService surveyPublishOrDiscardService;

	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	TypeOfRustService typeOfRustService;

	@Autowired
	SeasonMasterRepository seasonMasterRepository;

	@Autowired
	DataCollectModeRepository dataCollectModeRepository;

	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@Autowired
	ManageDemographicService manageDemographicService;

	@Autowired
	DashboardService dashboardService;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	// Dashboard Rust Report
	@RequestMapping(value = "/rustReport")
	public String viewRustReport(HttpServletRequest request, Model model, SearchVo searchVo) {

		String year1 = null;
		Integer seasonId1 = null;
		try {

			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			if (yearSeason != null) {
				JSONObject jso = new JSONObject(yearSeason);
				year1 = jso.getString("year");
				seasonId1 = jso.getInt("seasonid");
			}
		} catch (Exception e) {
			LOG.error("SubReportController::viewRustReport():" + e);
		}
		searchVo.setYearId(Integer.parseInt(year1));
		searchVo.setSeasonId(seasonId1);

		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<DataCollectModeEntity> dataModeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();
		try {

			dataModeList = dataCollectModeRepository.viewAllDataCollectMode();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();
			rustTypeList = typeOfRustService.vewAllTypeOFRust();
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			if (searchVo.getRegionId() != 0) {
				model.addAttribute(WrsisPortalConstant.ZONE_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute(WrsisPortalConstant.WOREDA_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0 && searchVo.getWoredaId() != 0) {
				model.addAttribute(WrsisPortalConstant.KEBEL_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getWoredaId()));
			}
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);

			List<Integer> yearlist = DateUtil.fetchYearListInteger();
			model.addAttribute("year", yearlist);
			model.addAttribute("seasons", dashboardService.getActiveSeasonList());
			model.addAttribute("dataModeList", dataModeList);
			model.addAttribute(WrsisPortalConstant.RESEARCH_CENTER_LIST, researchCenterList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			model.addAttribute(WrsisPortalConstant.SEASON_WISE_DATA, 0);

		} catch (Exception ex) {
			LOG.error("SubReportController::viewRustReport():" + ex);
		}
		return "rustReport";

	}

	@RequestMapping(value = "/rustReportDataSearch")
	public String rustReportDataSearch(Model model, HttpSession session, SearchVo searchVo) throws ParseException {

		if (searchVo.getSearchBySeason() == 0) {
			model.addAttribute(WrsisPortalConstant.SEASON_WISE_DATA, 0);
		} else {
			model.addAttribute("dateWiseData", 1);
		}

		model.addAttribute("regionlist", manageDemographicService.viewRegionList());
		if (searchVo.getRegionId() != 0) {
			model.addAttribute(WrsisPortalConstant.ZONE_LIST,
					manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
		}
		if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
			model.addAttribute(WrsisPortalConstant.WOREDA_LIST,
					manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
		}
		if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0 && searchVo.getWoredaId() != 0) {
			model.addAttribute(WrsisPortalConstant.KEBEL_LIST,
					manageDemographicService.getDemographyListByParentId(searchVo.getWoredaId()));
		}
		List<String> yearlist = DateUtil.fetchYearList();
		model.addAttribute("year", yearlist);
		model.addAttribute("seasons", dashboardService.getActiveSeasonList());
		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<SeasonMaster> seasonList = new ArrayList<>();
		List<DataCollectModeEntity> dataModeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();

		try {
			seasonList = seasonMasterRepository.viewAllSeasonByStatus();
			dataModeList = dataCollectModeRepository.viewAllDataCollectMode();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();

			rustTypeList = typeOfRustService.vewAllTypeOFRust();

			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);
			model.addAttribute("seasonList", seasonList);
			model.addAttribute("dataModeList", dataModeList);
			model.addAttribute(WrsisPortalConstant.RESEARCH_CENTER_LIST, researchCenterList);

			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

		} catch (Exception ex) {
			LOG.error("SubReportController::rustReportDataSearch():" + ex);
		}
		return "rustReport";

	}

	// Author : Raman Shrestha, Date : 14-09-2020
	@RequestMapping(value = { "/rustReportDashboardPagination" })
	public @ResponseBody RustReportDashboardDatatable rustReportDashboardPagination(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("year") Integer year, @RequestParam("seasonId") Integer seasonId,
			@RequestParam("surveyFromDate") String surveyDateFrom, @RequestParam("surveyToDate") String surveyDateTo,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("zoneId") Integer zoneId,
			@RequestParam("woredaId") Integer woredaId, @RequestParam("kebeleId") Integer kebeleId,
			@RequestParam("rustId") Integer rustId, @RequestParam("rcId") Integer rcId,
			@RequestParam("dataCollectModeId") Integer dataCollectModeId) {
		RustReportDashboardDatatable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter("draw");

			List<SurveyDetailsBean> rustReportList = subReportService.retriveSearchRustList(regionId, zoneId, woredaId,
					kebeleId, surveyDateFrom, surveyDateTo, seasonId, rcId, rustId, surveyNo, dataCollectModeId, year,
					Integer.valueOf(start), Integer.valueOf(length));
			Integer totalCount = subReportService.retriveRustReportListCount(regionId, zoneId, woredaId, kebeleId,
					surveyDateFrom, surveyDateTo, seasonId, rcId, rustId, surveyNo, dataCollectModeId, year, -1, -1);

			sdt = new RustReportDashboardDatatable();
			sdt.setData(rustReportList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SubReportController::rustReportDashboardPagination():" + e);
		}
		return sdt;
	}

	// for Rust report Excel
	@PostMapping("/rust-report-excel-download")
	public void rustReportExcel(Model model, HttpServletResponse response, @RequestParam("yearIdXl") Integer yearIdXl,
			@RequestParam("seasonTypeIdXl") Integer seasonTypeIdXl, @RequestParam("regionXl") Integer regionXl,
			@RequestParam("zoneXl") Integer zoneXl, @RequestParam("woredaXl") Integer woredaXl,
			@RequestParam("kebeleXl") Integer kebeleXl, @RequestParam("startDateXl") String startDateXl,
			@RequestParam("endDateXl") String endDateXl, @RequestParam("researchIdXl") Integer researchIdXl,
			@RequestParam("rustTypeIdXl") Integer rustTypeIdXl, @RequestParam("surveyNoXl") String surveyNoXl,
			@RequestParam("dataModeIdXl") Integer dataModeIdXl) {
		subReportService.rustReportExcel(response, yearIdXl, seasonTypeIdXl, regionXl, zoneXl, woredaXl, kebeleXl,
				startDateXl, endDateXl, researchIdXl, rustTypeIdXl, surveyNoXl, dataModeIdXl);
	}

	// for Rust report pdf

	@RequestMapping(value = { "/rust-report-pdf-download" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> rustReportDownloadPDF(Model model, HttpServletResponse response,
			@RequestParam("yearIdXl") Integer yearId, @RequestParam("seasonTypeIdXl") Integer seasonTypeId,
			@RequestParam("regionXl") Integer regionId, @RequestParam("zoneXl") Integer zoneId,
			@RequestParam("woredaXl") Integer woredaId, @RequestParam("kebeleXl") Integer kebeleId,
			@RequestParam("startDateXl") String startDate, @RequestParam("endDateXl") String endDate,
			@RequestParam("researchIdXl") Integer researchId, @RequestParam("rustTypeIdXl") Integer rustTypeId,
			@RequestParam("surveyNoXl") String surveyNo, @RequestParam("dataModeIdXl") Integer dataModeId)
			throws IOException {

		{
			List<SurveyDetailsBean> beans = subReportService.retriveSearchRustList(regionId, zoneId, woredaId, kebeleId,
					startDate, endDate, seasonTypeId, researchId, rustTypeId, surveyNo, dataModeId, yearId, -1, -1);

			RecomendationSurveyDetailsPDF pdf = new RecomendationSurveyDetailsPDF();
			HttpHeaders headers = new HttpHeaders();
			ByteArrayInputStream bar = pdf.generaterustReportPDF(beans);

			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
			String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
			String filename = "Rust_Report_" + formattedDate + ".pdf";
			headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bar));
		}

	}

	// Dashboard Race Analysts Report

	@RequestMapping(value = "/raceAnalysisReport")
	public String viewRaceAnalysisReport(Model model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("SearchVo") SearchVo searchVo) {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		List<DemographicEntity> regionList = new ArrayList<>();
		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();
		List<ResearchCenter> laboratoryList = new ArrayList<>();

		try {
			rustTypeList = typeOfRustRepository.vewAllTypeOFRustByStatus();
			regionList = demographicRepository.retriveRegions();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();
			laboratoryList = researchCenterRepository.findByLabStatusAndStatus(true, false);

			model.addAttribute(WrsisPortalConstant.SEARCH_VO, searchVo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);
			model.addAttribute(WrsisPortalConstant.RESEARCH_CENTER_LIST, researchCenterList);
			model.addAttribute("laboratoryList", laboratoryList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} catch (Exception ex) {
			LOG.error("SubReportController::viewRaceAnalysisReport():" + ex);
		}
		return "raceAnalysisReport";
	}

	@RequestMapping(value = "/raceAnalysisReportSearch")
	public String raceAnalysisReportSearch(Model model, @ModelAttribute("SearchVo") SearchVo searchVo) {
		List<DemographicEntity> regionList = new ArrayList<>();
		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();
		List<ResearchCenter> laboratoryList = new ArrayList<>();
		try {
			rustTypeList = typeOfRustRepository.vewAllTypeOFRustByStatus();
			regionList = demographicRepository.retriveRegions();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();
			laboratoryList = researchCenterRepository.findByLabStatusAndStatus(true, false);

			model.addAttribute(WrsisPortalConstant.SEARCH_VO, searchVo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);
			model.addAttribute(WrsisPortalConstant.RESEARCH_CENTER_LIST, researchCenterList);
			model.addAttribute("laboratoryList", laboratoryList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception ex) {
			LOG.error("SubReportController::raceAnalysisReportSearch():" + ex);
		}
		return "raceAnalysisReport";
	}

	// Author : Raman Shrestha, Date : 08-09-2020
	@RequestMapping(value = { "/raceAnalysisReportPagination" })
	public @ResponseBody RaceAnalysisDashboardReportDataTable raceAnalysisReportPagination(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("surveyId") String surveyId,
			@RequestParam("sampId") String sampleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String surveyDateFrom,
			@RequestParam(WrsisPortalConstant.END_DATE) String surveyDateTo,
			@RequestParam("recDtFrom") String recDtFrom, @RequestParam("recDtTo") String recDtTo,
			@RequestParam("demographyId") Integer regionId, @RequestParam("rustId") Integer rustId,
			@RequestParam("rcId") Integer rcId, @RequestParam("labId") Integer labId) {
		RaceAnalysisDashboardReportDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter("draw");
			List<SampleTagBean> raceReportList = subReportService.retriveRaceReportList(surveyId, sampleId,
					surveyDateFrom, surveyDateTo, recDtFrom, recDtTo, regionId, rustId, rcId, labId,
					Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = subReportService.retriveRaceReportListCount(surveyId, sampleId, surveyDateFrom,
					surveyDateTo, recDtFrom, recDtTo, regionId, rustId, rcId, labId, -1, -1);

			sdt = new RaceAnalysisDashboardReportDataTable();
			sdt.setData(raceReportList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SubReportController::raceAnalysisReportPagination():" + e);
		}
		return sdt;
	}

	// for Race report Excel
	@PostMapping("/raceReport-excel-download")
	public void raceReportExcel(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = WrsisPortalConstant.SURVEYNO, required = false) String surveyNo,
			@RequestParam(value = WrsisPortalConstant.SAMPLEID, required = false) String sampleId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "demography", required = false) int demography,
			@RequestParam(value = "rustType", required = false) int rustType,
			@RequestParam(value = "center", required = false) int center,
			@RequestParam(value = "labCenter", required = false) int labCenter,
			@RequestParam(value = "recDtFrom", required = false) String recDtFrom,
			@RequestParam(value = "recDtTo", required = false) String recDtTo) throws ParseException {

		subReportService.raceReportExcel(response, surveyNo, sampleId, startDate, endDate, demography, rustType, center,
				labCenter, recDtFrom, recDtTo, -1, -1);
	}

	// Dashboard Monitoring Report

	@RequestMapping("/monitorDataReport")
	public String viewMonitoringReport(Model model,
			@ModelAttribute("surveyImplementationBean") SurveyImplementationBean searchVo, HttpServletRequest request) {
		List<DemographicEntity> regionList = new ArrayList<>();

		try {
			// curent year season
			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			JSONObject jso = new JSONObject(yearSeason);

			JSONArray years = new JSONArray();
			years = DateUtil.getYearList();
			model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			regionList = demographicRepository.retriveRegions();
			if (searchVo.getYear() == null && searchVo.getSeasonId() == null) {
				searchVo.setYear(jso.getInt("year"));
				searchVo.setSeasonId(jso.getInt("seasonid"));
			}

			model.addAttribute("surveyImplementationBean", searchVo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			if (searchVo.getYear() == 0) {
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, searchVo.getYear());
			} else if (searchVo.getYear() == jso.getInt("year")) {
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, jso.getInt("year"));
			} else {
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, searchVo.getYear());
			}

			if (searchVo.getRegionId() != 0) {
				model.addAttribute("zoneList",
						manageDemographicService.viewAllZoneByRegionIdAndStatus(3, searchVo.getRegionId()));

			}

			if (searchVo.getSeasonId() == 0) {
				model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, searchVo.getYear());
			} else if (searchVo.getSeasonId() == jso.getInt("seasonid")) {
				model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, jso.getInt("seasonid"));
			} else {
				model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, searchVo.getSeasonId());
			}
			model.addAttribute("selectedZone", searchVo.getZoneId());
			if (searchVo.getSearchBySeason() == null) {
				searchVo.setSearchBySeason(0);
			}
			if (searchVo.getSearchBySeason() == 0) {
				model.addAttribute(WrsisPortalConstant.SEASON_WISE_DATA, 0);
			} else {
				model.addAttribute("dateWiseData", 1);
			}
		} catch (Exception ex) {
			LOG.error("SubReportController::viewMonitoringReport():" + ex);
		}

		return "monitorDataReport";
	}

	// Author : Raman Shrestha, Date : 14-09-2020
	@RequestMapping(value = { "/monitorReportDashboardPagination" })
	public @ResponseBody MonitorReportDashboardDataTable monitorReportDashboardPagination(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("year") Integer year,
			@RequestParam("seasonId") Integer seasonId, @RequestParam("monitorFromDate") String monitorFromDate,
			@RequestParam("monitorToDate") String monitorToDate, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam("zoneId") Integer zoneId, @RequestParam("monitorNo") String monitorNo,
			@RequestParam("recommendationId") String recommendationId) {
		MonitorReportDashboardDataTable sdt = null;
		Integer userId = 0;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter("draw");
			List<SurveyImplementationBean> raceReportList = subReportService.retriveMonitorData(regionId, zoneId,
					monitorNo, recommendationId, userId, monitorFromDate, monitorToDate, year, seasonId,
					Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = subReportService.retriveMonitorDataCount(regionId, zoneId, monitorNo, recommendationId,
					userId, monitorFromDate, monitorToDate, year, seasonId, -1, -1);

			sdt = new MonitorReportDashboardDataTable();
			sdt.setData(raceReportList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SubReportController::monitorReportDashboardPagination():" + e);
		}
		return sdt;
	}

	// excel for monitor Data

	@PostMapping("/monitorData-excel-download")
	public void monitorDataReport(Model model, HttpServletResponse response,
			@RequestParam(value = "region", required = false) int region,
			@RequestParam(value = "zone", required = false) int zone,
			@RequestParam(value = "monitor", required = false) String monitor,
			@RequestParam(value = "recNo", required = false) String recNo,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "season", required = false) Integer season) throws ParseException {

		Integer userId = 0;

		subReportService.excelMonitorData(response, region, zone, monitor, recNo, userId, fromdate, todate, year,
				season);
	}

	// Dashboard Rust Incident Report

	@RequestMapping("/dashboardRustIncident")
	public String viewRustIncidentReport(Model model, @ModelAttribute SearchVo searchVo) {
		List<DemographicEntity> regionList = new ArrayList<>();
		List<SeasonMaster> seasonList = new ArrayList<>();
		List<RustIncidentEntityBean> rustIncidendList = new ArrayList<>();

		try {
			regionList = manageDemographicService.regionList();
			seasonList = seasonMasterRepository.viewAllSeasonByStatus();
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			if (searchVo.getRegionId() != 0) {
				model.addAttribute(WrsisPortalConstant.ZONE_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute(WrsisPortalConstant.WOREDA_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0 && searchVo.getWoredaId() != 0) {
				model.addAttribute(WrsisPortalConstant.KEBEL_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getWoredaId()));
			}
			model.addAttribute("seasonList", seasonList);
			List<Integer> yearlist = DateUtil.fetchYearListInteger();
			model.addAttribute("year", yearlist);
			rustIncidendList = subReportService.retriveIncidendReport(searchVo);

			model.addAttribute("rustIncidendList", rustIncidendList);

			List<String> questionJsa = new ArrayList<>();
			for (int i = 0; i < rustIncidendList.size(); i++) {
				RustIncidentEntityBean rbean = rustIncidendList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::viewRustIncidentReport():" + e);
					continue;
				}
				for (int j = 0; j < jsa.length(); j++) {
					if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

						questionJsa.add(jsa.getJSONObject(j).getString("question"));
					}
				}

			}

			for (int i = 0; i < rustIncidendList.size(); i++) {
				RustIncidentEntityBean rbean = rustIncidendList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::viewRustIncidentReport():" + e);
					continue;
				}
				org.json.JSONObject jso = new org.json.JSONObject();

				for (int j = 0; j < jsa.length(); j++) {
					jso.put(jsa.getJSONObject(j).getString("question"), jsa.getJSONObject(j).getString(WrsisPortalConstant.OPTIONS));
				}

				List<String> questionVals = new ArrayList<>();
				for (int j = 0; j < questionJsa.size(); j++) {
					String qJsa = questionJsa.get(j);
					if (jso.has(qJsa)) {
						questionVals.add(jso.getString(qJsa));
					} else {
						questionVals.add("--");

					}

				}
				rbean.setQuestionsData(questionVals);
				rustIncidendList.set(i, rbean);
			}

			model.addAttribute("Questions", questionJsa);

			model.addAttribute(WrsisPortalConstant.SEARCH_VO, searchVo);
			model.addAttribute("rustList", rustIncidendList.size());

		} catch (Exception ex) {
			LOG.error("SubReportController::viewRustIncidentReport():" + ex);
		}
		return "dashboardRustIncident";
	}

	// Excel For DashBoard Rust Incident
	@RequestMapping(value = "downloadDashBoardRustIncidentExcel", method = RequestMethod.POST)
	public void downloadDashBoardRustIncidentExcel(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("kebeleIdExcel") Integer kebeleId,
			@RequestParam("yearIdExcel") Integer yearId, @RequestParam("seasonTypeIdExcel") Integer seasonId)
			throws IOException {
		HSSFWorkbook myExcelBook = null;
		OutputStream out = null;
		try {
			PublishSurveyExcel publishedSurveyExcel = new PublishSurveyExcel();

			List<RustIncidentEntityBean> beans = surveyPublishOrDiscardService.getDashBoardRustIncidentExcel(regionId,
					zoneId, woredaId, kebeleId, yearId, seasonId);

			List<String> questionJsa = new ArrayList<>();
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::downloadDashBoardRustIncidentExcel():" + e);
					continue;
				}
				for (int j = 0; j < jsa.length(); j++) {
					if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

						questionJsa.add(jsa.getJSONObject(j).getString("question"));
					}
				}
			}
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::downloadDashBoardRustIncidentExcel():" + e);
					continue;
				}
				org.json.JSONObject jso = new org.json.JSONObject();

				for (int j = 0; j < jsa.length(); j++) {
					jso.put(jsa.getJSONObject(j).getString("question"), jsa.getJSONObject(j).getString(WrsisPortalConstant.OPTIONS));
				}

				List<String> questionVals = new ArrayList<>();
				for (int j = 0; j < questionJsa.size(); j++) {
					String qJsa = questionJsa.get(j);
					if (jso.has(qJsa)) {
						questionVals.add(jso.getString(qJsa));
					} else {
						questionVals.add("--");

					}

				}
				rbean.setQuestionsData(questionVals);
				beans.set(i, rbean);
			}

			myExcelBook = publishedSurveyExcel.buildDashBoardRustIncidentExcel(beans);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Rust_Incident_Report" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			out = response.getOutputStream();

		} catch (Exception e) {
			LOG.error("SubReportController::downloadDashBoardRustIncidentExcel():" + e);
		} finally {
			try {
				myExcelBook.write(out);
				out.flush();
				out.close();
				myExcelBook.close();
			} catch (Exception e) {
				LOG.error("SubReportController::downloadDashBoardRustIncidentExcel():" + e);
			}
		}
	}

	// PDF Download For Rust Incident
	@RequestMapping(value = { "/downloadRustIncidentPDF" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadDashBoardRustIncidentPDF(HttpServletResponse response,
			HttpSession session, Model model, @RequestParam("regionIdExcel") Integer regionId,
			@RequestParam("zoneIdExcel") Integer zoneId, @RequestParam("woredaIdExcel") Integer woredaId,
			@RequestParam("kebeleIdExcel") Integer kebeleId, @RequestParam("yearIdExcel") Integer yearId,
			@RequestParam("seasonTypeIdExcel") Integer seasonId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		List<RustIncidentEntityBean> beans = surveyPublishOrDiscardService.getDashBoardRustIncidentExcel(regionId,
				zoneId, woredaId, kebeleId, yearId, seasonId);
		try {
			List<String> questionJsa = new ArrayList<>();
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {

					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::downloadDashBoardRustIncidentPDF():" + e);
					continue;
				}

				for (int j = 0; j < jsa.length(); j++) {
					if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

						questionJsa.add(jsa.getJSONObject(j).getString("question"));
					}
				}
			}
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("SubReportController::downloadDashBoardRustIncidentPDF():" + e);
					continue;
				}

				org.json.JSONObject jso = new org.json.JSONObject();

				for (int j = 0; j < jsa.length(); j++) {
					jso.put(jsa.getJSONObject(j).getString("question"), jsa.getJSONObject(j).getString(WrsisPortalConstant.OPTIONS));
				}

				List<String> questionVals = new ArrayList<>();
				for (int j = 0; j < questionJsa.size(); j++) {
					String qJsa = questionJsa.get(j);
					if (jso.has(qJsa)) {
						questionVals.add(jso.getString(qJsa));
					} else {
						questionVals.add("--");

					}

				}
				rbean.setQuestionsData(questionVals);
				beans.set(i, rbean);
			}
		} catch (Exception e) {
			LOG.error("SubReportController::downloadDashBoardRustIncidentPDF():" + e);

		}
		RustIncidentPDF rIPDF = new RustIncidentPDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateRustIncidentPdf(beans);
		} catch (Exception e) {
			LOG.error("SubReportController::downloadDashBoardRustIncidentPDF():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Rust_Incident_Report" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// pdf download for monitor data

	@RequestMapping(value = { "/downloadMonitorDataPDF" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadMonitorDataPDF(HttpServletResponse response, HttpSession session,
			Model model, @RequestParam("regionPDF") Integer regionPDF, @RequestParam("zonePDF") Integer zonePDF,
			@RequestParam("monitorPDF") String monitorPDF, @RequestParam("recNoPDF") String recNoPDF,
			@RequestParam("fromdatePDF") String fromdatePDF, @RequestParam("todatePDF") String todatePDF,
			@RequestParam("yearPDF") Integer yearPDF, @RequestParam("seasonPDF") Integer seasonPDF) throws IOException {
		Integer userId = 0;
		HttpHeaders headers = new HttpHeaders();
		List<SurveyImplementationBean> beans = subReportService.getMonitorDataforPdf(regionPDF, zonePDF, monitorPDF,
				recNoPDF, userId, fromdatePDF, todatePDF, yearPDF, seasonPDF);
		ImplementationMonitorPDF rIPDF = new ImplementationMonitorPDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateImplementationMonitorPdf(beans);
		} catch (Exception e) {
			LOG.error("SubReportController::downloadMonitorDataPDF():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Monitor_Implementation_Report" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// down load race analysis pdf

	@RequestMapping(value = { "/downloadRaceAnalysisPDF" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadRaceAnalysisPDF(HttpServletResponse response,
			HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("surveyidPDF") String surveyidPDF, @RequestParam("sampidPDF") String sampidPDF,
			@RequestParam("startDatePDF") String startDatePDF, @RequestParam("endDatePDF") String endDatePDF,
			@RequestParam("demographyPDF") int demographyPDF, @RequestParam("rustidPDF") int rustidPDF,
			@RequestParam("centeridPDF") int centeridPDF, @RequestParam("labCenteridPDF") int labCenteridPDF,
			@RequestParam("recDtFromPDF") String recDtFromPDF, @RequestParam("recDtToPDF") String recDtToPDF)
			throws IOException {

		HttpHeaders headers = new HttpHeaders();
		List<SampleTagBean> beans = subReportService.getRaceAnalysisPDFData(surveyidPDF, sampidPDF, startDatePDF,
				endDatePDF, demographyPDF, rustidPDF, centeridPDF, labCenteridPDF, recDtFromPDF, recDtToPDF, -1, -1);
		RaceAnalysisDetailsPDF rIPDF = new RaceAnalysisDetailsPDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateRaceAnalysisPdf(beans);
		} catch (Exception e) {
			LOG.error("SubReportController::downloadRaceAnalysisPDF():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Race_Analysis_Report" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	@ResponseBody
	@GetMapping("/race-download-file-exist")
	public String checkRaceFilePath(Model model, @RequestParam("fileId") Integer fileId) {
		return subReportService.checkRaceFilePath(fileId);
	}

	@PostMapping("/downloadRaceFile")
	public void downloadRaceFile(Model m1, ModelMap model, @RequestParam("fileId") Integer fileId,
			HttpServletResponse response) throws ParseException {

		subReportService.downloadRaceFile(response, fileId, model);

	}

	@Autowired
	CommonService commonService;

	@RequestMapping(value = { "/viewRustDetailsOnDashboard" })
	public String viewSurveyDetailsOnPending(HttpSession session, @RequestParam("surveyId") Integer surveyId,
			Model model, HttpServletRequest request) throws JSONException {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if (response.getBoolean("capturedImageId")) {
			for (int i = 0; i < response.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + String.valueOf(Base64.getEncoder()
						.encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model.addAttribute("SurveyJSON", new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute("SurveyId", surveyId);
		model.addAttribute("SurveyNo", response.getString("SurveyNo"));
		return "viewRustDetailsOnDashboard";
	}

}
