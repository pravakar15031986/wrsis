package com.csmpl.wrsis.prototype.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.service.RaceReportService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.RustPrevalenceReportPDF;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.excel.RaceByVarietyReportExcel;
import com.csmpl.wrsis.webportal.excel.ReactiontoRustReportExcel;
import com.csmpl.wrsis.webportal.excel.RustPrevalenceReportExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class ReportController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;
	@Autowired
	DashboardService dashboardService;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	DemographicRepository demographicRepository;

	@RequestMapping(value = { "/prevalenceReport" })
	public String prevalenceReport(Model model) throws JSONException {

		String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		JSONObject jso = new JSONObject(yearSeason);
		String dataResp = surveyorDetailsRepository.fetchPrevalenceReport(jso.getString("year"), jso.getInt(WrsisPortalConstant.SEASON_ID1),
				0, 0);
		model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
				new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();
		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(yearSeason.getBytes())));

		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("RegionList", regions);

		return "prevalenceReport";
	}

	@RequestMapping(value = { "/searchPrevelanceeport" })
	public String searchPrevelanceeport(Model model, @RequestParam("Year") String year,
			@RequestParam("SeasonId") Integer seasonId, @RequestParam("RegionId") Integer regionId,
			@RequestParam("ZoneId") Integer zoneId) throws JSONException {

		String dataResp = surveyorDetailsRepository.fetchPrevalenceReport(year, seasonId, regionId, zoneId);
		model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
				new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();
		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		JSONObject json = new JSONObject();
		json.put("year", year);
		json.put(WrsisPortalConstant.SEASON_ID1, seasonId);
		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(json.toString().getBytes())));

		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("RegionList", regions);
		model.addAttribute("RegionId", regionId);
		model.addAttribute("ZoneId", zoneId);

		model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
		model.addAttribute("SelectedSeasonId", seasonId);
		model.addAttribute("SelectedRegionId", regionId);

		return "prevalenceReport";
	}

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@RequestMapping(value = { "/reaction-ToRustReport" })
	public String reactionToRustReport(Model model) throws JSONException {

		String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		JSONObject jso = new JSONObject(yearSeason);
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();

		try {
			String dataResp = surveyorDetailsRepository.fetchReactionReport(jso.getString("year"),
					jso.getInt(WrsisPortalConstant.SEASON_ID1), 0);
			model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
					new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));
		} catch (Exception e) {
			LOG.error("ReportController::reactionToRustReport():" + e);
			model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
					new String(Base64.getEncoder().encode(new JSONArray().toString().getBytes())));
		}
		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(yearSeason.getBytes())));

		List<VarietyTypeMasterEntity> varietys = varietyTypeRepository.fetchActiveVarietyType();
		model.addAttribute("VarietyType", varietys);

		return "reactionToRustReport";
	}

	@RequestMapping(value = { "/searchReactionReport" })
	public String searchReactionReport(Model model, @RequestParam("year") String year,
			@RequestParam("SeasonId") Integer seasonId, @RequestParam("variety") Integer variety) throws JSONException {

		String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();

		String dataResp = surveyorDetailsRepository.fetchReactionReport(year, seasonId, variety);
		model.addAttribute(WrsisPortalConstant.REPORT_ENCODE_JSON,
				new String(Base64.getEncoder().encode((dataResp == null) ? "[]".getBytes() : dataResp.getBytes())));

		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

		model.addAttribute(WrsisPortalConstant.YEAR_SEASON, new String(Base64.getEncoder().encode(yearSeason.getBytes())));

		List<VarietyTypeMasterEntity> varietys = varietyTypeRepository.fetchActiveVarietyType();
		model.addAttribute("VarietyType", varietys);
		model.addAttribute("SelectedVariety", variety);

		model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
		model.addAttribute("SelectedSeasonId", seasonId);

		return "reactionToRustReport";
	}

	@RequestMapping(value = { "/wheatRustReport" })
	public String wheatRustReport() {
		return "wheatRustReport";
	}

	@RequestMapping(value = { "/summary-WheatRust-Report" })
	public String summaryWheatRustReport() {
		return "summaryWheatRustReport";
	}

	@RequestMapping(value = { "/stemRustRaces-DetectedCropping" })
	public String stemRustRacesDetectedCroppingSeasonTable(Model model) throws JSONException {

		List<RustTypeMasterEntity> rusts = rustTypeRepository.fetchAllActiveRustType();
		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("Regions", regions);
		model.addAttribute("Rusts", rusts);
		List<VarietyTypeMasterEntity> varieties = varietyTypeRepository.fetchActiveVarietyType();
		String dbJson = raceReportService.fetchVarietyReportDataSearch(2);
		JSONArray regionjsa = new JSONArray();
		try {
			regionjsa = new JSONArray(dbJson);
		} catch (Exception e) {
			LOG.error("ReportController::stemRustRacesDetectedCroppingSeasonTable():" + e);
		} // handling the 500 error
		for (int i = 0; i < regionjsa.length(); i++) {
			JSONObject regionJson = regionjsa.getJSONObject(i);
			JSONArray zoneJsa = regionJson.getJSONArray("zonedtls");
			for (int j = 0; j < zoneJsa.length(); j++) {
				JSONObject racedetails = zoneJsa.getJSONObject(j);
				JSONArray raceJsa = racedetails.getJSONArray("raceresults");
				for (int k = 0; k < raceJsa.length(); k++) {
					JSONArray raceJsaVariety = new JSONArray();
					try {
						raceJsaVariety = raceJsa.getJSONObject(k).getJSONArray(WrsisPortalConstant.RESULT_WISE_VARIETY);

					} catch (Exception e) {
						LOG.error("ReportController::stemRustRacesDetectedCroppingSeasonTable():" + e);
						raceJsaVariety = new JSONArray();
					}
					raceJsaVariety = getRequiredFormat(varieties, raceJsaVariety);
					raceJsa.getJSONObject(k).remove(WrsisPortalConstant.RESULT_WISE_VARIETY);
					raceJsa.getJSONObject(k).put(WrsisPortalConstant.RESULT_WISE_VARIETY, raceJsaVariety);

				}
			}
		}

		model.addAttribute("ReportJSON", new String(Base64.getEncoder().encode(regionjsa.toString().getBytes())));
		JSONArray jsa = new JSONArray();
		for (int i = 0; i < varieties.size(); i++) {
			VarietyTypeMasterEntity vte = varieties.get(i);
			jsa.put(vte.getVchDesc());

		}
		model.addAttribute("Varieties", varieties);

		model.addAttribute("RustSelected", 2); // default leaf rust
		model.addAttribute("SelectedRegion", -1);
		return "stemRustRacesDetectedCroppingSeasonTable";
	}

	@RequestMapping(value = { "/searcharietyReport" })
	public String searcharietyReport(Model model, @RequestParam("RustId") Integer rustId,
			@RequestParam("RegionId") Integer regionId) throws JSONException {
		List<DemographicEntity> regions = demographicRepository.findByLevelId(2);
		model.addAttribute("Regions", regions);
		List<RustTypeMasterEntity> rusts = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("Rusts", rusts);
		List<VarietyTypeMasterEntity> varieties = varietyTypeRepository.fetchActiveVarietyType();

		String dbJson = "";
		if (regionId == -1) {
			dbJson = raceReportService.fetchVarietyReportDataSearch(rustId);
		} else {
			dbJson = raceReportService.fetchVarietyReportDataSearch(rustId, regionId);
		}

		JSONArray regionjsa = new JSONArray();
		if (dbJson != null) {
			regionjsa = new JSONArray(dbJson);
		}
		for (int i = 0; i < regionjsa.length(); i++) {
			JSONObject regionJson = regionjsa.getJSONObject(i);
			JSONArray zoneJsa = regionJson.getJSONArray("zonedtls");
			for (int j = 0; j < zoneJsa.length(); j++) {
				JSONObject racedetails = zoneJsa.getJSONObject(j);
				JSONArray raceJsa = racedetails.getJSONArray("raceresults");
				for (int k = 0; k < raceJsa.length(); k++) {
					JSONArray raceJsaVariety = new JSONArray();
					try {
						raceJsaVariety = raceJsa.getJSONObject(k).getJSONArray(WrsisPortalConstant.RESULT_WISE_VARIETY);

					} catch (Exception e) {
						LOG.error("ReportController::searcharietyReport():" + e);
						raceJsaVariety = new JSONArray();
					}
					raceJsaVariety = getRequiredFormat(varieties, raceJsaVariety);
					raceJsa.getJSONObject(k).remove(WrsisPortalConstant.RESULT_WISE_VARIETY);
					raceJsa.getJSONObject(k).put(WrsisPortalConstant.RESULT_WISE_VARIETY, raceJsaVariety);

				}
			}
		}

		model.addAttribute("ReportJSON", new String(Base64.getEncoder().encode(regionjsa.toString().getBytes())));
		JSONArray jsa = new JSONArray();
		for (int i = 0; i < varieties.size(); i++) {
			VarietyTypeMasterEntity vte = varieties.get(i);
			jsa.put(vte.getVchDesc());

		}
		model.addAttribute("Varieties", varieties);

		model.addAttribute("RustSelected", rustId);
		model.addAttribute("SelectedRegion", regionId);
		return "stemRustRacesDetectedCroppingSeasonTable";
	}

	private JSONArray getRequiredFormat(List<VarietyTypeMasterEntity> varieties, JSONArray raceJsaVariety)
			throws JSONException {

		JSONArray finalJsa = new JSONArray();
		for (VarietyTypeMasterEntity vte : varieties) {

			int count = 0;
			JSONObject json1 = new JSONObject();
			for (int i = 0; i < raceJsaVariety.length(); i++) {
				JSONObject json = new JSONObject();
				json = raceJsaVariety.getJSONObject(i);
				if (json.getInt("varietyid") == vte.getVarietyTypeId()) {
					count = json.getInt("count");
					break;
				}
			}

			json1 = new JSONObject();
			json1.put("varietyid", vte.getVarietyTypeId());
			json1.put("count", count);

			finalJsa.put(json1);

		}

		return finalJsa;
	}

	@Autowired
	RaceReportService raceReportService;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@RequestMapping(value = { "/virulence-SpectrumPgtRaces" })
	public String virulenceSpectrumPgtRaces(Model model) throws JSONException {
		String object = raceReportService.fetchVirulenceData(1);
		List<RustTypeMasterEntity> rusttypes = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("Rusttypes", rusttypes);
		JSONArray jsa = new JSONArray();
		try {
			jsa = new JSONArray(object);
		} catch (Exception e) {
			LOG.error("ReportController::virulenceSpectrumPgtRaces():" + e);
		} // avoid 500 internal server error
		model.addAttribute("ViewDetails", new String(Base64.getEncoder().encode(jsa.toString().getBytes())));
		model.addAttribute("SelectedRust", 1);
		model.addAttribute("SearchShow", false);
		return "virulenceSpectrumPgtRaces";
	}

	@RequestMapping(value = { "/rustWiseVirulenceSpectrum" })
	public String rustWiseVirulenceSpectrum(Model model, @RequestParam("RustType") Integer rustType)
			throws JSONException {
		String object = raceReportService.fetchVirulenceData(rustType);
		List<RustTypeMasterEntity> rusttypes = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("Rusttypes", rusttypes);
		JSONArray jsa = new JSONArray();
		try {

			jsa = new JSONArray(object);
		} catch (Exception e) {
			LOG.error("ReportController::rustWiseVirulenceSpectrum():" + e);
		} // avoid 500 internal server error
		model.addAttribute("ViewDetails", new String(Base64.getEncoder().encode(jsa.toString().getBytes())));
		model.addAttribute("SelectedRust", rustType);
		model.addAttribute("SearchShow", true);
		return "virulenceSpectrumPgtRaces";
	}

	@RequestMapping("/surveyAndIvrReport")
	public String surveyAndIvrReportChart(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam(name = "yearId", required = false) String yearId,
			@RequestParam(name = "seasionId", required = false) Integer seasion,
			@RequestParam(name = "surveyDateFrom", required = false) String surveyDateFrom,
			@RequestParam(name = "surveyDateTo", required = false) String surveyDateTo) {

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
				seasionId = jso.getInt(WrsisPortalConstant.SEASON_ID1);
			}

			model.addAttribute(WrsisPortalConstant.SELECTED_YEAR, year);
			model.addAttribute("SeasonId", seasionId);

			ArrayList<String> yearList = new ArrayList<>();
			for (int i = 0; i < DateUtil.getYearList().length(); i++) {
				yearList.add(String.valueOf(DateUtil.getYearList().get(i)));
			}
			model.addAttribute("yearList", yearList);

			List<SeasionMasterEntity> seasonList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute("seasonList", seasonList);
			JSONObject response = new JSONObject();
			if (surveyDateFrom != null && !"".equals(surveyDateFrom)) {
				response = dashboardService.getSurveyIncidentReportDateWise(surveyDateFrom, surveyDateTo);
				model.addAttribute(WrsisPortalConstant.FROM_DATE, surveyDateFrom);
				model.addAttribute(WrsisPortalConstant.TO_DATE, surveyDateTo);
				model.addAttribute("dateWiseData", "dateWiseData");
			} else {
				response = dashboardService.getSurveyIncidentReport(year, seasionId);
			}

			model.addAttribute("DashBoardJson", new String(Base64.getEncoder().encode(response.toString().getBytes())));

		} catch (Exception e) {

			LOG.error("ReportController::surveyAndIvrReportChart():" + e);
		}

		return "surveyAndIvrReport";
	}

	@RequestMapping(value = "/regionAndRustTypeByZoneChart", method = RequestMethod.GET)
	public String regionAndRustTypeByZoneChart() {
		return "regionAndRustTypeByZoneChart";
	}

	@GetMapping(value = "/surveyIvrAndIncidentChart")
	public String surveyIvrAndIncidentChart() {
		return "surveyIvrAndIncidentChart";
	}

	@GetMapping(value = "/viewMISReports")
	public String viewMISReports() {
		return "viewMISReports";
	}

	// down load Rust Prevalence Report pdf

	@RequestMapping(value = { "/rustPrevalenceReportPdfDownload" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> rustPrevalenceReportPdfDownload(HttpServletResponse response,
			HttpSession session, Model model, @RequestParam("Year") String year,
			@RequestParam("SeasonId") Integer seasonId, @RequestParam("RegionId") Integer regionId,
			@RequestParam("ZoneId") Integer zoneId) throws IOException {

		HttpHeaders headers = new HttpHeaders();
		String dataResp = surveyorDetailsRepository.fetchPrevalenceReport(year, seasonId, regionId, zoneId);
		RustPrevalenceReportPDF rIPDF = new RustPrevalenceReportPDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateRustPrevalenceReportPdf(dataResp);
		} catch (Exception e) {
			LOG.error("ReportController::rustPrevalenceReportPdfDownload():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Rust_Prevalence_Report_" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// down load Rust Prevalence Report excel

	@RequestMapping(value = "rustPrevalenceReportExcelDownload", method = RequestMethod.POST)
	public void rustPrevalenceReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("YearXL") String year, @RequestParam("SeasonIdXL") Integer seasonId,
			@RequestParam("RegionIdXL") Integer regionId, @RequestParam("ZoneIdXL") Integer zoneId) throws IOException {
		try {

			String dataResp = surveyorDetailsRepository.fetchPrevalenceReport(year, seasonId, regionId, zoneId);

			RustPrevalenceReportExcel reportExcel = new RustPrevalenceReportExcel();

			HSSFWorkbook excelBook = reportExcel.buildExcelRustPrevalenceReportDetails(dataResp);
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Rust_Prevalence_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {

			LOG.error("ReportController::rustPrevalenceReportExcelDownload():" + e);
		}

	}

	// down load Reaction to Rust Report excel

	@RequestMapping(value = "reactiontoRustReportExcelDownload", method = RequestMethod.POST)
	public void reactiontoRustReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("YearXL") String year, @RequestParam("SeasonIdXL") Integer seasonId,
			@RequestParam("VarietyXL") Integer variety) throws IOException {
		try {

			String dataResp = surveyorDetailsRepository.fetchReactionReport(year, seasonId, variety);

			ReactiontoRustReportExcel reportExcel = new ReactiontoRustReportExcel();

			HSSFWorkbook excelBook = reportExcel.buildExcelReactiontoRustReportDetails(dataResp);// dataResp
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Reaction_to_Rust_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {
			LOG.error("ReportController::reactiontoRustReportExcelDownload():" + e);
		}

	}

	// down load Race By Variety Report excel

	@RequestMapping(value = "raceByVarietyReportExcelDownload", method = RequestMethod.POST)
	public void raceByVarietyReportExcelDownload(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("rustXL") Integer rustXL, @RequestParam("regionXL") Integer regionXL) throws IOException {
		try {

			String dbJson = "";
			if (regionXL == -1) {
				dbJson = raceReportService.fetchVarietyReportDataSearch(rustXL);
			} else {
				dbJson = raceReportService.fetchVarietyReportDataSearch(rustXL, regionXL);
			}

			RaceByVarietyReportExcel reportExcel = new RaceByVarietyReportExcel();

			HSSFWorkbook excelBook = reportExcel.buildExcelRaceByVarietyReportDetails(varietyTypeRepository, dbJson);// dataResp
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Race_By_Variety_Report_" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			OutputStream out = response.getOutputStream();
			excelBook.write(out);
			out.flush();
			out.close();
			excelBook.close();
		} catch (Exception e) {

			LOG.error("ReportController::raceByVarietyReportExcelDownload():" + e);
		}

	}

}
