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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.service.ManageDesignationMasterService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.SurveyorDashboardReportDataTable;
import com.csmpl.wrsis.pdf.ReportPDF;
import com.csmpl.wrsis.pdf.SurveyorDetailsPDF;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.excel.ReportExcel;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class DashboardReportController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(DashboardReportController.class);

	@Autowired
	DashboardService dashboardService;
	@Autowired
	ManageDesignationMasterService manageDesignationMasterService;
	@Autowired
	ResearchCenterService researchCenterService;

	@RequestMapping(value = "/surveyorReport")
	public String surveyorDetails(Model model, SearchVo searchVo) {
		try {

			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
		} catch (Exception e) {
			LOG.error("DashboardReportController::surveyorDetails():" + e);
		}
		return "surveyorReport";
	}

	// Author : Raman Shrestha, Date : 17-09-2020
	@RequestMapping(value = { "/surveyorReportDashboardPagination" })
	public @ResponseBody SurveyorDashboardReportDataTable surveyorReportDashboardPagination(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("email") String email,
			@RequestParam("mobile") String mobile, @RequestParam("fullName") String name,
			@RequestParam("rcId") String rcId, @RequestParam("desigId") String desigId) {
		SurveyorDashboardReportDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			List<UserBean> rustReportList = dashboardService.fetchSurveyorList(mobile, email, name, rcId, desigId,
					Integer.valueOf(start), Integer.valueOf(length));
			Integer totalCount = dashboardService.fetchSurveyorListCount(mobile, email, name, rcId, desigId, -1, -1);

			sdt = new SurveyorDashboardReportDataTable();
			sdt.setData(rustReportList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SubReportController::surveyorReportDashboardPagination():" + e);
		}
		return sdt;
	}

	@RequestMapping(value = "/pathologistReport")
	public String pathologistDetails(Model model, SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute("pathologistDetails", dashboardService.fetchPathologistList(searchVo));
		} catch (Exception e) {
			LOG.error("DashboardReportController::pathologistDetails():" + e);
		}
		return "pathologistReport";
	}

	@RequestMapping(value = "/woredaExpertsReport")
	public String woredaExpertsReport(Model model, SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute("woredaExpertDetails", dashboardService.fetchWoredaExpertList(searchVo));
		} catch (Exception e) {
			LOG.error("DashboardReportController::woredaExpertsReport():" + e);
		}
		return "woredaExpertsReport";
	}

	// Excel Download
	@RequestMapping(value = "downloadWoredaExpertReportExcel", method = RequestMethod.POST)
	public void downloadWoredaExpertReportExcel(HttpServletResponse response, HttpServletRequest request, Model model,
			@RequestParam("desigId") String desigId, @RequestParam("mobileNo") String mobileNo,
			@RequestParam("email") String email, @RequestParam("name") String name) throws IOException {

		ReportExcel reportExcel = new ReportExcel();
		List<UserBean> beans = dashboardService.downloadWoredaExpertReportExcel(desigId, mobileNo, email, name);
		HSSFWorkbook myExcelBook = reportExcel.buildWoredaExpertReportExcel(beans);

		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=WoredaExpertReportExcel_" + formattedDate + ".xls");

		OutputStream out = response.getOutputStream();
		myExcelBook.write(out);

		out.flush();
		out.close();
		myExcelBook.close();
	}

	@RequestMapping(value = { "/downloadWoredaExpertReportPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadWoredaExpertReportPdf(HttpServletResponse response,
			HttpServletRequest request, Model model, @RequestParam("desigId") String desigId,
			@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email,
			@RequestParam("name") String name) throws IOException {
		List<UserBean> beans = dashboardService.downloadWoredaExpertReportExcel(desigId, mobileNo, email, name);

		ReportPDF psPdf = new ReportPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.buildWoredaExpertReportPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "WoredaExpertReport_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	@RequestMapping(value = "/devAgentReport")
	public String devAgentReport(Model model, SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute("devAgentDetails", dashboardService.fetchDevAgentList(searchVo));
		} catch (Exception e) {
			LOG.error("DashboardReportController::devAgentReport():" + e);
		}
		return "devAgentReport";
	}

	// Excel Download
	@RequestMapping(value = "downloadDevAgentReportExcel", method = RequestMethod.POST)
	public void downloadDevAgentReportExcel(HttpServletResponse response, HttpServletRequest request, Model model,
			@RequestParam("desigId") String desigId, @RequestParam("mobileNo") String mobileNo,
			@RequestParam("email") String email, @RequestParam("name") String name) throws IOException {

		ReportExcel reportExcel = new ReportExcel();
		List<UserBean> beans = dashboardService.downloadDevAgentReportExcel(desigId, mobileNo, email, name);
		HSSFWorkbook myExcelBook = reportExcel.buildDevAgentReportExcel(beans);

		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=DevelopmentAgentReportExcel_" + formattedDate + ".xls");

		OutputStream out = response.getOutputStream();
		myExcelBook.write(out);

		out.flush();
		out.close();
		myExcelBook.close();
	}

	@RequestMapping(value = { "/downloadDevAgentReportPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> DevelopmentAgentReportPdf(HttpServletResponse response,
			HttpServletRequest request, Model model, @RequestParam("desigId") String desigId,
			@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email,
			@RequestParam("name") String name) throws IOException {
		List<UserBean> beans = dashboardService.downloadDevAgentReportExcel(desigId, mobileNo, email, name);

		ReportPDF psPdf = new ReportPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.buildDevelopmentAgentReportPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "DevelopmentAgentReportReport_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// Excel Download
	@RequestMapping(value = "downloadPathologistReportExcel", method = RequestMethod.POST)
	public void downloadPathologistReportExcel(HttpServletResponse response, HttpServletRequest request, Model model,
			@RequestParam("rcId") String rcId, @RequestParam("desigId") String desigId,
			@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email,
			@RequestParam("name") String name) throws IOException {

		ReportExcel reportExcel = new ReportExcel();
		List<UserBean> beans = dashboardService.downloadPathologistReportExcel(rcId, desigId, mobileNo, email, name);
		HSSFWorkbook myExcelBook = reportExcel.buildPathologistReportExcel(beans);

		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=PathologistReport_" + formattedDate + ".xls");

		OutputStream out = response.getOutputStream();
		myExcelBook.write(out);

		out.flush();
		out.close();
		myExcelBook.close();
	}

	@RequestMapping(value = { "/downloadPathologistReportPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadPathologistReportPdf(HttpServletResponse response,
			HttpServletRequest request, Model model, @RequestParam("rcId") String rcId,
			@RequestParam("desigId") String desigId, @RequestParam("mobileNo") String mobileNo,
			@RequestParam("email") String email, @RequestParam("name") String name) throws IOException {
		List<UserBean> beans = dashboardService.downloadPathologistReportExcel(rcId, desigId, mobileNo, email, name);

		ReportPDF psPdf = new ReportPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.buildPathologistReportPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "PathologistReport_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@RequestMapping(value = { "/rustTypeChartReport" })
	public String rustTypeChartReport(Model model,
			@RequestParam(value = "currentYear", required = false) String currentYear,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = WrsisPortalConstant.SEASON_ID, required = false) Integer seasonId,
			@RequestParam(value = "surveyDateFrom", required = false) String surveyDateFrom,
			@RequestParam(value = "surveyDateTo", required = false) String surveyDateTo) {

		String year1 = null;
		Integer seasonId1 = null;
		try {

			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			if (yearSeason != null) {
				JSONObject jso = new JSONObject(yearSeason);
				year1 = jso.getString("year");
				seasonId1 = jso.getInt("seasonid");
			}

			ArrayList<String> yearList = new ArrayList<>();
			for (int i = 0; i < DateUtil.getYearList().length(); i++) {
				yearList.add(String.valueOf(DateUtil.getYearList().get(i)));
			}

			model.addAttribute("yearList", yearList);

			

			String finalSeasion = null;
			String selectedYear = null;
			JSONArray response = new JSONArray();
			if (surveyDateFrom != null && !"".equals(surveyDateFrom)) {
				SearchVo searchVo = new SearchVo();
				model.addAttribute(WrsisPortalConstant.DATE_WISE_DATA, WrsisPortalConstant.DATE_WISE_DATA);
				searchVo.setSurveyDateFrom(surveyDateFrom);
				searchVo.setSurveyDateTo(surveyDateTo);
				response = dashboardService.getRustReportDateWise(searchVo);
				model.addAttribute("SeasonId", seasonId1);
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year1);
			} else {
				SearchVo searchVo = new SearchVo();

				if (year != null && !"".equals(year)) {
					searchVo.setYear(year);
					selectedYear = year;
				} else {
					searchVo.setYear(year1);
					selectedYear = year1;
				}
				if (seasonId != null) {
					searchVo.setSeasonId(seasonId);
					finalSeasion = String.valueOf(seasonId);
				} else {
					searchVo.setSeasonId(seasonId1);
					finalSeasion = String.valueOf(seasonId1);
				}
				searchVo.setCurrentYear(year1);
				response = dashboardService.getRustReport(searchVo);
				model.addAttribute("SeasonId", finalSeasion);
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, selectedYear);
			}
			List<String> yearlist = DateUtil.fetchYearList();
			model.addAttribute("year", yearlist);
			model.addAttribute(WrsisPortalConstant.SEASONS, dashboardService.getActiveSeasonList());

			model.addAttribute(WrsisPortalConstant.RUST_REPORT_JSON,
					new String(Base64.getEncoder().encode(response.toString().getBytes())));

			model.addAttribute(WrsisPortalConstant.FROM_DATE, surveyDateFrom);

			model.addAttribute(WrsisPortalConstant.TO_DATE, surveyDateTo);
		} catch (Exception e) {
			LOG.error("DashboardReportController::rustTypeChartReport():" + e);
		}
		return "rustTypeChartReport";
	}

	@RequestMapping(value = { "/regionAndRustTypeReport" })
	public String regionAndRustTypeReportChart(Model model, @RequestParam(value = "year", required = false) String year,
			@RequestParam(value = WrsisPortalConstant.SEASON_ID, required = false) Integer seasonId,
			@RequestParam(value = "surveyDateFrom", required = false) String surveyDateFrom,
			@RequestParam(value = "surveyDateTo", required = false) String surveyDateTo) {
		try {
			JSONArray response = new JSONArray();
			if (surveyDateFrom != null && !"".equals(surveyDateFrom)) {
				SearchVo searchVo = new SearchVo();
				model.addAttribute(WrsisPortalConstant.DATE_WISE_DATA, WrsisPortalConstant.DATE_WISE_DATA);
				searchVo.setSurveyDateFrom(surveyDateFrom);
				searchVo.setSurveyDateTo(surveyDateTo);

				response = dashboardService.getRustReportByRegionDateWise(searchVo);
				model.addAttribute(WrsisPortalConstant.FROM_DATE, surveyDateFrom);
				model.addAttribute(WrsisPortalConstant.TO_DATE, surveyDateTo);

			} else {
				SearchVo searchVo = new SearchVo();
				if (year != null) {
					searchVo.setCurrentYear("notcurrent");
				}
				searchVo.setYear(year);
				if (seasonId != null) {
					searchVo.setSeasonId(seasonId);
				} else {
					searchVo.setSeasonId(0);
				}

				response = dashboardService.getRustReportByRegion(searchVo);
				LOG.info("DashboardReportController::regionAndRustTypeReportChart():" + response);

			}
			List<String> yearlist = DateUtil.fetchYearList();
			model.addAttribute("year", yearlist);
			model.addAttribute(WrsisPortalConstant.SEASONS, dashboardService.getActiveSeasonList());
			model.addAttribute(WrsisPortalConstant.RUST_REPORT_JSON,
					new String(Base64.getEncoder().encode(response.toString().getBytes())));
			if (year != null) {
				model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
				model.addAttribute("SeasonId", seasonId);
			} else {
				String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
				if (yearSeason != null) {
					JSONObject jso = new JSONObject(yearSeason);
					model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, jso.getString("year"));
					model.addAttribute("SeasonId", jso.getInt("seasonid"));
				}

			}

		} catch (Exception e) {
			LOG.error("DashboardReportController::regionAndRustTypeReportChart():" + e);
		}
		return "regionAndRustTypeReport";
	}

	@RequestMapping(value = { "/rustSurveyAndRecommentationSurveyChart" })
	public String rustSurveyAndRecommendationSurveyChart(Model model,
			@RequestParam(value = "year", required = false) String yearId,
			@RequestParam(value = WrsisPortalConstant.SEASON_ID, required = false) Integer seasion,
			@RequestParam(value = "surveyDateFrom", required = false) String surveyDateFrom,
			@RequestParam(value = "surveyDateTo", required = false) String surveyDateTo) {
		try {

			String year = null;
			Integer seasionId = null;
			if (yearId != null) {
				year = yearId;
				seasionId = seasion;

			} else {
				String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
				JSONObject jso = new JSONObject(yearSeason);
				year = jso.getString("year");
				seasionId = jso.getInt("seasonid");
			}

			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
			model.addAttribute("SeasonId", seasionId);

			JSONObject response = new JSONObject();
			if (surveyDateFrom != null && !"".equals(surveyDateFrom)) {
				SearchVo searchVo = new SearchVo();
				model.addAttribute(WrsisPortalConstant.DATE_WISE_DATA, WrsisPortalConstant.DATE_WISE_DATA);
				searchVo.setSurveyDateFrom(surveyDateFrom);
				searchVo.setSurveyDateTo(surveyDateTo);

				response = dashboardService.getRustSurveyAndRecommendSurveyDate(surveyDateFrom, surveyDateTo);
				model.addAttribute(WrsisPortalConstant.FROM_DATE, surveyDateFrom);
				model.addAttribute(WrsisPortalConstant.TO_DATE, surveyDateTo);

			} else {

				response = dashboardService.getRustSurveyAndRecommendSurvey(year, seasionId);
				LOG.info("DashboardReportController::rustSurveyAndRecommendationSurveyChart():" + response);

			}
			List<String> yearlist = DateUtil.fetchYearList();
			model.addAttribute("year", yearlist);
			model.addAttribute(WrsisPortalConstant.SEASONS, dashboardService.getActiveSeasonList());
			model.addAttribute(WrsisPortalConstant.RUST_REPORT_JSON,
					new String(Base64.getEncoder().encode(response.toString().getBytes())));

		} catch (Exception e) {
			LOG.error("DashboardReportController::rustSurveyAndRecommendationSurveyChart():" + e);
		}
		return "rustSurveyAndRecommentationSurveyChart";
	}

	// excel download for surveyor

	@PostMapping("/surveyorExcelDownload")
	public void raceReportExcel(Model model, HttpServletResponse response,
			@RequestParam(value = "researchCenterIdE", required = false) String researchCenterIdE,
			@RequestParam(value = "intdesigidE", required = false) String intdesigidE,
			@RequestParam(value = "mobileE", required = false) String mobileE,
			@RequestParam(value = "emailE", required = false) String emailE,
			@RequestParam(value = "fullNameE", required = false) String fullNameE) throws ParseException {
		try {
			dashboardService.surveyorExcelDownload(response, mobileE, emailE, fullNameE, researchCenterIdE,
					intdesigidE);
		} catch (Exception e) {
			LOG.error("DashboardReportController::raceReportExcel():" + e);
		}

	}

//down load race analysis pdf

	@RequestMapping(value = { "/surveyorPdfDownload" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> surveyorPdfDownload(HttpServletResponse response, HttpSession session,
			Model model, @RequestParam("researchCenterIdPDF") String researchCenterIdPDF,
			@RequestParam("intdesigidPDF") String intdesigidPDF, @RequestParam("mobilePDF") String mobilePDF,
			@RequestParam("emailPDF") String emailPDF, @RequestParam("fullNamePDF") String fullNamePDF)
			throws IOException {

		HttpHeaders headers = new HttpHeaders();
		List<UserBean> beans = dashboardService.surveyorPdfDownload(researchCenterIdPDF, intdesigidPDF, mobilePDF,
				emailPDF, fullNamePDF);
		SurveyorDetailsPDF rIPDF = new SurveyorDetailsPDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateSurveyorDetailsPdf(beans);
		} catch (Exception e) {
			LOG.error("DashboardReportController::surveyorPdfDownload():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Surveyor_Details_Report" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// Author : Raman Shrestha
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getUsersCountByAjax", method = RequestMethod.POST)
	public String getUsersCountByAjax(HttpServletRequest request, HttpSession session) throws JSONException {
		String response = "";
		try {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			JSONObject usersCountResponse = dashboardService.getDashboardUsersContent(userId);// Dashboard Users Data
			response = new String(Base64.getEncoder().encode(usersCountResponse.toString().getBytes()));
		} catch (Exception e) {
			LOG.error("DashboardReportController::getUsersCountByAjax():" + e);
		}
		return response;
	}

	// Author : Raman Shrestha
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getTransactionCountByAjax", method = RequestMethod.POST)
	public String getTransactionCountByAjax(HttpServletRequest request, HttpSession session) throws JSONException {
		String response = "";
		try {

			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			JSONObject transCountResponse = dashboardService.getDashboardTransContent(userId);// Dashboard Transaction
																								// Data
			response = new String(Base64.getEncoder().encode(transCountResponse.toString().getBytes()));
		} catch (Exception e) {
			LOG.error("DashboardReportController::getTransactionCountByAjax():" + e);
		}
		return response;
	}

	// Author : Raman Shrestha
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRustTypeChartReportAjax")
	public String getRustTypeChartReportAjax(HttpServletRequest request) throws JSONException {

		String year = request.getParameter("year");
		String seasonId = request.getParameter(WrsisPortalConstant.SEASON_ID);
		String dateFrom = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_FROM));
		String dateTo = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_TO));
		String year1 = null;
		Integer seasonId1 = null;
		JSONArray response = new JSONArray();
		String data = null;
		try {
			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			if (yearSeason != null) {
				JSONObject jso = new JSONObject(yearSeason);
				year1 = jso.getString("year");
				seasonId1 = jso.getInt("seasonid");
			}

			String finalSeasion = null;
			String selectedYear = null;

			if (dateFrom != null && !"".equals(dateFrom)) {
				SearchVo searchVo = new SearchVo();
				searchVo.setSurveyDateFrom(dateFrom);
				searchVo.setSurveyDateTo(dateTo);
				response = dashboardService.getRustReportDateWise(searchVo);
			} else {
				SearchVo searchVo = new SearchVo();

				if (year != null && !"".equals(year)) {
					searchVo.setYear(year);
					selectedYear = year;
				} else {
					searchVo.setYear(year1);
					selectedYear = year1;
				}
				if (seasonId != null && !"".equals(seasonId)) {
					searchVo.setSeasonId(Integer.parseInt(seasonId));
					finalSeasion = seasonId;
				} else {
					searchVo.setSeasonId(seasonId1);
					finalSeasion = String.valueOf(seasonId1);
				}
				searchVo.setCurrentYear(year1);
				response = dashboardService.getRustReport(searchVo);
			}
			data = new String(Base64.getEncoder().encode(response.toString().getBytes()));
		} catch (Exception e) {
			LOG.error("DashboardReportController::getRustTypeChartReportAjax():" + e);
		}
		return data;
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getCurrentYearSeason")
	public String getCurrentYearSeason() throws JSONException {
		String yearSeason = null;
		try {
			yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		} catch (Exception e) {
			LOG.error("DashboardReportController::getCurrentYearSeason():" + e);

		}
		return yearSeason;
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getSurveyIvrIncidentChartReportAjax")
	public String getSurveyIvrIncidentChartReportAjax(HttpServletRequest request) throws JSONException {

		String year = request.getParameter("year");
		String seasonId = request.getParameter(WrsisPortalConstant.SEASON_ID);
		String dateFrom = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_FROM));
		String dateTo = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_TO));
		String data = null;
		try {
			String year1 = null;
			Integer seasionId = null;
			if (year != null && !"".equals(year)) {
				year1 = year;
				seasionId = Integer.parseInt(seasonId);

			} else {
				String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
				JSONObject jso = new JSONObject(yearSeason);
				year1 = jso.getString("year");
				seasionId = jso.getInt("seasonid");
			}
			JSONObject response = new JSONObject();
			if (dateFrom != null && !"".equals(dateFrom)) {
				response = dashboardService.getSurveyIncidentReportDateWise(dateFrom, dateTo);
			} else {
				response = dashboardService.getSurveyIncidentReport(year1, seasionId);
			}
			data = new String(Base64.getEncoder().encode(response.toString().getBytes()));

		} catch (Exception e) {
			LOG.error("DashboardReportController::getSurveyIvrIncidentChartReportAjax():" + e);

		}
		return data;
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRecomChartReportAjax")
	public String getRecomChartReportAjax(HttpServletRequest request) throws JSONException {

		String year = request.getParameter("year");
		String seasonId = request.getParameter(WrsisPortalConstant.SEASON_ID);
		String dateFrom = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_FROM));
		String dateTo = String.valueOf(request.getParameter(WrsisPortalConstant.DATE_TO));

		String data = null;
		try {
			String year1 = null;
			Integer seasionId = null;
			if (year != null && !"".equals(year) && !year.isEmpty()) {
				year1 = year;
				seasionId = Integer.parseInt(seasonId);
			} else {
				String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
				JSONObject jso = new JSONObject(yearSeason);
				year1 = jso.getString("year");
				seasionId = jso.getInt("seasonid");
			}

			JSONObject response = new JSONObject();
			if (dateFrom != null && !"".equals(dateFrom)) {
				SearchVo searchVo = new SearchVo();
				searchVo.setSurveyDateFrom(dateFrom);
				searchVo.setSurveyDateTo(dateTo);
				response = dashboardService.getRustSurveyAndRecommendSurveyDate(dateFrom, dateTo);
			} else {
				response = dashboardService.getRustSurveyAndRecommendSurvey(year1, seasionId);
				LOG.info("DashboardReportController::rustSurveyAndRecommendationSurveyChart():" + response);
			}
			data = new String(Base64.getEncoder().encode(response.toString().getBytes()));

		} catch (Exception e) {
			LOG.error("DashboardReportController::getRecomChartReportAjax():" + e);

		}
		return data;
	}
	
	
	// Author : Debendra Nayak
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getAdvisoryLatestRecord", method = RequestMethod.POST)
	public String getAdvisoryLatestRecord(HttpServletRequest request, HttpSession session) throws JSONException {
		String response = "";
		try {

			//Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			JSONObject transCountResponse = dashboardService.getAdvisoryLatestRecord();
			response = new String(Base64.getEncoder().encode(transCountResponse.toString().getBytes()));
		} catch (Exception e) {
			LOG.error("DashboardReportController::getAdvisoryLatestRecord():" + e);
		}
		return response;
	}
	
	// Author : Debendra Nayak
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRecomLatestRecord", method = RequestMethod.POST)
	public String getRecomLatestRecord(HttpServletRequest request, HttpSession session) throws JSONException {
		String response = "";
		try {

			//Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			JSONObject transCountResponse = dashboardService.getRecomLatestRecord();
			response = new String(Base64.getEncoder().encode(transCountResponse.toString().getBytes()));
		} catch (Exception e) {
			LOG.error("DashboardReportController::getRecomLatestRecord():" + e);
		}
		return response;
	}

}
