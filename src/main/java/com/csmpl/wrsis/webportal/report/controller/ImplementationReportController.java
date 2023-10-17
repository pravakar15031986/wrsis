package com.csmpl.wrsis.webportal.report.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.excel.ImplementationReportExcel;
import com.csmpl.wrsis.webportal.excel.ImplementationSummaryReportExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class ImplementationReportController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(ImplementationReportController.class);
	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;
	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@RequestMapping(value = { "/implementationSummaryReport" })
	public String implementationSummaryReport(Model model, HttpSession session) throws Exception {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {

			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			JSONObject jso = new JSONObject(yearSeason);

			// recommendation no
			List<Object[]> recomendations = monitorDetailsRepository.fetchRecomendationNosCurrentSeasonYear();
			Integer recomendationId = -1;
			if (!(recomendations.isEmpty())) {
				recomendationId = (Integer) recomendations.get(0)[0];
			}
			model.addAttribute("Recomendations", recomendations);

			String implementationDetails = monitorDetailsRepository.viewImplementationSummaryReport(-1,
					jso.getInt(WrsisPortalConstant.YEAR), jso.getInt("seasonid"), recomendationId);
			JSONArray jsa = new JSONArray();
			try {
				jsa = new JSONArray(implementationDetails);
			} catch (Exception e) {
				LOG.error("ImplementationReportController::implementationSummerReport():" + e);
			} // handelled the null pointer and json exception
			model.addAttribute(WrsisPortalConstant.REPORT_JSON, new String(Base64.getEncoder().encode(jsa.toString().getBytes())));

			JSONArray years = new JSONArray();
			years = DateUtil.getYearList();
			model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
			model.addAttribute("RegionList", regions);
			List<Object[]> demographList = monitorDetailsRepository.fetchAllActiveDemographs(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			model.addAttribute("RegionId", -1);
			model.addAttribute(WrsisPortalConstant.RECOMENDATION_ID, recomendationId);
			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, jso.getInt(WrsisPortalConstant.YEAR));
			model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, jso.getInt("seasonid"));

		} catch (Exception e) {
			LOG.error("ImplementationReportController::implementationSummerReport():" + e);
		}
		return "implementationSummaryReport";
	}

	@RequestMapping(value = { "/implementationSummaryReportSearch" })
	public String implementationSummaryReportSearch(Model model, HttpSession session,
			@RequestParam("RegionId") Integer regionId, @RequestParam("SeasonId") Integer seasonId,
			@RequestParam("Year") Integer year, @RequestParam(WrsisPortalConstant.RECOMENDATION_ID) Integer recomendationId)
			throws Exception {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			String implementationDetails = monitorDetailsRepository.viewImplementationSummaryReport(regionId, year,
					seasonId, recomendationId);
			JSONArray jsa = new JSONArray();
			try {
				jsa = new JSONArray(implementationDetails);
			} catch (Exception e) {
				LOG.error("ImplementationReportController::implementationSummerReportSearch():" + e);
			} // handelled the null pointer and json exception
			model.addAttribute(WrsisPortalConstant.REPORT_JSON, new String(Base64.getEncoder().encode(jsa.toString().getBytes())));

			JSONArray years = new JSONArray();
			years = DateUtil.getYearList();
			model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
			model.addAttribute("RegionList", regions);
			List<Object[]> demographList = monitorDetailsRepository.fetchAllActiveDemographs(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			model.addAttribute("RegionId", regionId);
			model.addAttribute(WrsisPortalConstant.RECOMENDATION_ID, recomendationId);
			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
			model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, seasonId);
		} catch (Exception e) {
			LOG.error("ImplementationReportController::implementationSummerReportSearch():" + e);
		}
		return "implementationSummaryReport";
	}

	// reomendation no

	@RequestMapping(value = "/fetchRecomendationNosSeasonYear")
	public @ResponseBody String fetchRecomendationNosSeasonYear(Model model, HttpServletRequest request,
			@RequestParam(value = "Year", required = true) int intYear,
			@RequestParam(value = "Season", required = true) int intSeasonid) throws JSONException {

		List<Object[]> reactionList = monitorDetailsRepository.fetchRecomendationNosSeasonYear(intYear, intSeasonid);
		JSONArray jsa = new JSONArray();
		for (int i = 0; i < reactionList.size(); i++) {
			JSONObject json = new JSONObject();
			json.put("RecId", (Integer) reactionList.get(i)[0]);
			json.put("RecName", (String) reactionList.get(i)[1]);
			jsa.put(json);
		}
		return jsa.toString();

	}

	@RequestMapping(value = { "/implementationReport" })
	public String implementationReport(Model model, HttpSession session) throws Exception {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			JSONObject jso = new JSONObject(yearSeason);

			// reomendation no
			List<Object[]> recomendations = monitorDetailsRepository.fetchRecomendationNosCurrentSeasonYear();
			Integer recomendationId = -1;
			if (!recomendations.isEmpty()) {
				recomendationId = (Integer) recomendations.get(0)[0];
			}
			model.addAttribute("Recomendations", recomendations);

			String implementationDetails = monitorDetailsRepository.viewImplementationReport(0, jso.getInt("seasonid"),
					jso.getInt(WrsisPortalConstant.YEAR), recomendationId);

			JSONArray jsa = new JSONArray();
			try {
				jsa = new JSONArray(implementationDetails);
				for (int i = 0; i < jsa.length(); i++)// region
				{
					JSONObject zone = new JSONObject();
					zone = jsa.getJSONObject(i);
					JSONArray zonedetails = zone.getJSONArray("zonedetails");
					for (int j = 0; j < zonedetails.length(); j++) {
						JSONArray finalCounts = zonedetails.getJSONObject(j).getJSONArray("finalcounts");
						JSONObject js = getAllFungicideJson(finalCounts.getJSONObject(0));
						finalCounts.put(0, js);

					}
				}
			} catch (Exception e1) {
				LOG.error("ImplementationReportController::implementationReport():" + e1);
			} // handle the json null exception

			model.addAttribute(WrsisPortalConstant.REPORT_JSON, new String(Base64.getEncoder().encode(jsa.toString().getBytes())));
			List<FungicideMaster> fungicides = fungicideDetailsRepository.fetchAllActiveFungicides();
			model.addAttribute("Fungicides", fungicides);
			JSONArray years = new JSONArray();
			years = DateUtil.getYearList();
			model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
			model.addAttribute("RegionList", regions);
			List<Object[]> demographList = monitorDetailsRepository.fetchAllActiveDemographs(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			model.addAttribute("RegionId", -1);
			model.addAttribute(WrsisPortalConstant.RECOMENDATION_ID, recomendationId);
			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, jso.getInt(WrsisPortalConstant.YEAR));
			model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, jso.getInt("seasonid"));

		} catch (Exception e) {
			LOG.error("ImplementationReportController::implementationReport():" + e);
		}
		return "implementationReport";
	}

	@RequestMapping(value = { "/implementationReportSearch" })
	public String implementationReportSearch(Model model, HttpSession session,
			@RequestParam("RegionId") Integer regionId, @RequestParam("SeasonId") Integer seasonId,
			@RequestParam("Year") Integer year, @RequestParam(WrsisPortalConstant.RECOMENDATION_ID) Integer recomendationId)
			throws Exception {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {

			String implementationDetails = monitorDetailsRepository.viewImplementationReport(regionId, seasonId, year,
					recomendationId);

			JSONArray jsa = new JSONArray();
			try {
				jsa = new JSONArray(implementationDetails);
				for (int i = 0; i < jsa.length(); i++)// region
				{
					JSONObject zone = new JSONObject();
					zone = jsa.getJSONObject(i);
					JSONArray zonedetails = zone.getJSONArray("zonedetails");
					for (int j = 0; j < zonedetails.length(); j++) {
						JSONArray finalCounts = zonedetails.getJSONObject(j).getJSONArray("finalcounts");
						JSONObject js = getAllFungicideJson(finalCounts.getJSONObject(0));
						finalCounts.put(0, js);

					}
				}
			} catch (Exception e1) {
				LOG.error("ImplementationReportController::implementationReportSearch():" + e1);
			} // handle the json null exception

			model.addAttribute(WrsisPortalConstant.REPORT_JSON, new String(Base64.getEncoder().encode(jsa.toString().getBytes())));
			List<FungicideMaster> fungicides = fungicideDetailsRepository.fetchAllActiveFungicides();
			model.addAttribute("Fungicides", fungicides);
			JSONArray years = new JSONArray();
			years = DateUtil.getYearList();
			model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
			model.addAttribute("RegionList", regions);
			List<Object[]> demographList = monitorDetailsRepository.fetchAllActiveDemographs(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			model.addAttribute("RegionId", regionId);
			model.addAttribute(WrsisPortalConstant.RECOMENDATION_ID, recomendationId);
			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
			model.addAttribute(WrsisPortalConstant.SELECTED_SEASON, seasonId);

		} catch (Exception e) {
			LOG.error("ImplementationReportController::implementationReportSearch():" + e);
		}
		return "implementationReport";
	}

	@Autowired
	FungicideMasterRepository fungicideDetailsRepository;

	private JSONObject getAllFungicideJson(JSONObject jsonObj) throws JSONException {

		JSONArray fungiceideused = new JSONArray();
		try {
			fungiceideused = jsonObj.getJSONArray(WrsisPortalConstant.FUNGICEIDE_USED);
			jsonObj.remove(WrsisPortalConstant.FUNGICEIDE_USED);
		} catch (Exception e) {
			jsonObj.put(WrsisPortalConstant.FUNGICEIDE_USED, fungiceideused);
			LOG.error("ImplementationReportController::getAllFungicideJson():" + e);
		}
		List<FungicideMaster> fungicides = fungicideDetailsRepository.fetchAllActiveFungicides();
		JSONArray finaljsa = new JSONArray();
		for (int i = 0; i < fungicides.size(); i++) {
			FungicideMaster fungicide = fungicides.get(i);
			boolean check = false;
			for (int j = 0; j < fungiceideused.length(); j++) {
				JSONObject json = fungiceideused.getJSONObject(j);
				if (json.getInt("ftypeid") == fungicide.getFungicideId()) {
					check = true;
					finaljsa.put(json);
					break;
				}
			}
			if (!check ) {
				JSONObject json = new JSONObject();
				json.put("ftypeid", fungicide.getFungicideId());
				json.put("fused", 0);
				finaljsa.put(json);
			}
		}

		jsonObj.put(WrsisPortalConstant.FUNGICEIDE_USED, finaljsa);

		return jsonObj;
	}

// down load implementation summary Report excel

	@RequestMapping(value = "implementationSummaryReportExcelDownload", method = RequestMethod.POST)
	public void implementationSummaryReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("RegionIdXL") Integer RegionIdXL, @RequestParam("SeasonIdXL") Integer SeasonIdXL,
			@RequestParam("YearXL") Integer YearXL, @RequestParam("recomendationIdXL") Integer recomendationIdXL)
			throws  IOException {
		try {

			String implementationDetails = monitorDetailsRepository.viewImplementationSummaryReport(RegionIdXL, YearXL,
					SeasonIdXL, recomendationIdXL);

			ImplementationSummaryReportExcel reportExcel = new ImplementationSummaryReportExcel();

			HSSFWorkbook excelBook = reportExcel.buildExcelImplementationSummaryReportDetails(implementationDetails);// dataResp
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "implementation_Summary_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {

			LOG.error("ImplementationReportController::implementationSummaryReportExcelDownload():" + e);
		}

	}

// down load implementation summary Report excel

	@RequestMapping(value = "implementationReportExcelDownload", method = RequestMethod.POST)
	public void implementationReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("RegionIdXL") Integer RegionIdXL, @RequestParam("SeasonIdXL") Integer SeasonIdXL,
			@RequestParam("YearXL") Integer YearXL, @RequestParam("recomendationIdXL") Integer recomendationIdXL)
			throws FileNotFoundException, IOException {
		try {

			String implementationDetails = monitorDetailsRepository.viewImplementationReport(RegionIdXL, SeasonIdXL,
					YearXL, recomendationIdXL);

			ImplementationReportExcel reportExcel = new ImplementationReportExcel();

			HSSFWorkbook excelBook = reportExcel.buildExcelImplementationReportDetails(fungicideDetailsRepository,
					implementationDetails);// dataResp
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Implementation_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {

			LOG.error("ImplementationReportController::implementationReportExcelDownload():" + e);
		}

	}

}
