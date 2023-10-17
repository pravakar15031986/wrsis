package com.csmpl.wrsis.prototype.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.RaceAnalysisDataTable;
import com.csmpl.wrsis.datatable.TagSampleDataTable;
import com.csmpl.wrsis.pdf.ReportPDF;
import com.csmpl.wrsis.webportal.bean.RaceAnalysisPendingSamples;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.RaceAnalysis;
import com.csmpl.wrsis.webportal.entity.RaceAnalysisInoculation;
import com.csmpl.wrsis.webportal.entity.RaceAnalysisScore;
import com.csmpl.wrsis.webportal.entity.RaceScoringMaster;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.WheatDifferentialLineEntity;
import com.csmpl.wrsis.webportal.excel.RaceAnalysisScoreRepository;
import com.csmpl.wrsis.webportal.excel.ReportExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisInoculationRepository;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisRepository;
import com.csmpl.wrsis.webportal.repository.RaceScoringMasterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.repository.WheatDifferentialLineRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.RaceAnalysisService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;

@Controller
public class RaceAnalysisController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(RaceAnalysisController.class);

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;

	@Autowired
	TypeOfRustService typeOfRustService;

	@RequestMapping(value = { "/stem-rust-race-analysis" })
	public String raceanalysis(Model model, HttpServletRequest request, HttpSession session) {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, "");
		model.addAttribute(WrsisPortalConstant.SAMPLEID, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, "");
		model.addAttribute(WrsisPortalConstant.REGION_ID, "0");
		model.addAttribute(WrsisPortalConstant.SHOW_ALERT, false);
		return "stem-rust-race-analysis";
	}

	@RequestMapping(value = { "/stemRustRaceAnalysisSearch" })
	public String stemRustRaceAnalysisSearch(Model model, HttpServletRequest request, RaceAnalysisPendingSamples sample,
			HttpSession session) throws ParseException {

		RaceAnalysisPendingSamples backup = sample;

		String startDateB = sample.getAllocationStartDate();
		String endDateB = sample.getAllocationEndDate();

		SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String startDate = "";
		String endDate = "";
		if (!(startDateB.trim().equals("") || endDateB.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDateB);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDateB);
			endDate = smpl.format(date1);
		}

		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);

		model.addAttribute(WrsisPortalConstant.SURVEYNO, backup.getSurveyNo());
		model.addAttribute(WrsisPortalConstant.SAMPLEID, backup.getSampleId());
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, startDateB);
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, endDateB);
		model.addAttribute(WrsisPortalConstant.REGION_ID, backup.getRegionId());
		return "stem-rust-race-analysis";
	}

	@RequestMapping(value = { "/stemRustRaceAnalysisSearchDatatablePagination" })
	public @ResponseBody RaceAnalysisDataTable stemRustRaceAnalysisSearchDatatablePagination(Model model,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.ALLOCATION_START_DATE) String startD,
			@RequestParam(WrsisPortalConstant.ALLOCATION_END_DATE) String endD,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleid,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer region) throws ParseException {

		RaceAnalysisPendingSamples sample = new RaceAnalysisPendingSamples();
		sample.setAllocationStartDate(startD);
		sample.setAllocationEndDate(endD);
		sample.setSampleId(sampleid);
		sample.setRegionId(region);
		sample.setSurveyNo(surveyNo);

		RaceAnalysisPendingSamples backup = sample;

		String startDateB = sample.getAllocationStartDate();
		String endDateB = sample.getAllocationEndDate();

		SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String startDate = "";
		String endDate = "";
		if (!(startDateB.trim().equals("") || endDateB.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDateB);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDateB);
			endDate = smpl.format(date1);
		}

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.fetchActiveSampleTagDetails(
				sample.getSurveyNo().toUpperCase(), sample.getSampleId(), startDate, endDate, sample.getRegionId(),
				userId, Integer.valueOf(start), Integer.valueOf(length));
		Integer count = 0;
		List<RaceAnalysisPendingSamples> samples = new ArrayList<>();
		for (Object[] obj : sampleTagDetails) {
			sample = new RaceAnalysisPendingSamples();
			sample.setsNo((Integer.valueOf(start)) + (++count));
			sample.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
					+ Integer.valueOf(String.valueOf(obj[9])) + "\">" + String.valueOf(obj[0]) + "</a>");
			sample.setSampleId(String.valueOf(obj[1]));
			sample.setAllocationDate(String.valueOf(obj[2]));
			sample.setRustType(String.valueOf(obj[3]));
			sample.setSampleCollectedOn(String.valueOf(obj[4]));
			sample.setRegion(String.valueOf(obj[5]));
			sample.setZone(String.valueOf(obj[6]));
			sample.setWoreda(String.valueOf(obj[7]));
			sample.setKebel(String.valueOf(obj[8]));
			sample.setSurveyId(Integer.valueOf(String.valueOf(obj[9])));
			sample.setSampleTagId(Integer.valueOf(String.valueOf(obj[10])));
			sample.setRustypeId(Integer.valueOf(String.valueOf(obj[11])));
			String action = "<form id=\"myform" + count + "\" method=\"POST\" action=\"raceAnalysisDetails\">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"surveyId\" value=\""
					+ Integer.valueOf(String.valueOf(obj[9])) + "\">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"SampleId\" value=\""
					+ String.valueOf(obj[1]) + "\">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"RustType\" value=\""
					+ String.valueOf(obj[3]) + "\">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"SampleTagId\" value=\""
					+ Integer.valueOf(String.valueOf(obj[10])) + "\">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"RustId\" value=\""
					+ Integer.valueOf(String.valueOf(obj[11])) + " \">\r\n"
					+ "   <input type=\"text\" style=\"display:none;\" name=\"Message\" value=\" \">\r\n"
					+ "   </form>\r\n"
					+ "   <a class=\"btn btn-info btn-sm\" data-toggle=\"tooltip\"  title=\"\" href=\"javascript:void(0)\" onclick=\"document.getElementById('myform"
					+ count + "').submit();\" data-original-title=\"View Details\"><i class=\"icon-edit1\"></i></a>\r\n"
					+

					"   <button class=\"btn btn-danger btn-sm\" onclick=\"dupmRace(this)\" data-title=\"Deactive\" type=\"button\" id=\"btn_"
					+ count + "\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i></button>\r\n"
					+ "		<span style=\"display:none;\" id=\"spanid" + count + "\">"
					+ Integer.valueOf(String.valueOf(obj[10])) + "</span>\r\n"
					+ "		<span style=\"display:none;\" id=\"survey" + count + "\">S"
					+ Integer.valueOf(String.valueOf(obj[9])) + "</span>\r\n"
					+ "		<span style=\"display:none;\" id=\"sampleid" + count + "\">" + String.valueOf(obj[1])
					+ "</span>";
			sample.setAction(action);
			samples.add(sample);
		}

		Integer totalCount = sampleLabTagDetailsRepository.fetchActiveSampleTagDetailsCount(
				backup.getSurveyNo().toUpperCase(), backup.getSampleId(), startDate, endDate, backup.getRegionId(),
				userId, -1, -1);
		RaceAnalysisDataTable sdt = new RaceAnalysisDataTable();
		sdt.setData(samples);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@Autowired
	CommonService commonService;

	@RequestMapping(value = { "/viewSurveyDetailsForTagSample" })
	public String viewsurveyForRaceanalysis(@RequestParam("surveyId") Integer surveyId, HttpServletRequest request,
			Model model) throws JSONException {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

		return "viewsurveyForRaceanalysis";
	}

	@RequestMapping(value = { "/viewsurveyForRaceanalysis" })
	public String viewsurveyForRaceanalysis1(@RequestParam("surveyId") Integer surveyId, HttpServletRequest request,
			Model model) throws JSONException {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

		return "viewsurveyForRaceanalysis";
	}

	@RequestMapping(value = { "/viewSurveyForRaceSample" })
	public String viewSurveyForRaceSample(@RequestParam("surveyId") Integer surveyId, Model model)
			throws JSONException {
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

		return "viewSurveyForRaceSample";
	}

	@RequestMapping(value = { "/raceAnalysisDetails" })
	public String raceAnalysisDetails(HttpSession session, Model model, @RequestParam("surveyId") Integer surveyId,
			@RequestParam("SampleId") String sampleId, @RequestParam("RustType") String rustType,
			@RequestParam(WrsisPortalConstant.MESSAGE_JSON) String message,
			@RequestParam("SampleTagId") String SampleTagId, @RequestParam("RustId") Integer rustId)
			throws JSONException {
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE1, rustType);
		model.addAttribute(WrsisPortalConstant.RUST_ID, rustId);
		List<Object[]> firstWheatLine = raceAnalysisRepository.getFirstWheatLineByRustId(rustId);
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		// get the wheat line from db
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		session.setAttribute("surveyId", surveyId);
		session.setAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		session.setAttribute(WrsisPortalConstant.RUST_TYPE1, rustType);
		session.setAttribute(WrsisPortalConstant.SAMPLE_TAG_ID, SampleTagId);
		session.setAttribute(WrsisPortalConstant.RUST_ID, rustId);
		model.addAttribute(WrsisPortalConstant.MESSAGE_JSON, message);

		// mcnair wheatline

		return "raceAnalysisDetails";
	}

	@Autowired
	RaceAnalysisRepository raceAnalysisRepository;

	@RequestMapping(value = { "/saveInoculationDate" })
	public String saveInoculationDate(HttpSession session, Model model, RedirectAttributes redirect,
			@RequestParam("surveyDate") String surveyDate, @RequestParam("inoculationDate") String inoculationDate,
			@RequestParam("wheatLine") Integer wheatLine) {

		try {
			Date d1 = convertStringToDate(surveyDate);
			Date d2 = convertStringToDate(inoculationDate);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			if (diff < 0) {
				redirect.addFlashAttribute(WrsisPortalConstant.MESSAGE_JSON,
						"Date of Inoculation should not be less than from sample collected date.");
				Integer surveyId = (Integer) session.getAttribute("surveyId");
				String sampleId = (String) session.getAttribute(WrsisPortalConstant.SAMPLE_ID);
				String rustType = (String) session.getAttribute(WrsisPortalConstant.RUST_TYPE1);
				String SampleTagId = (String) session.getAttribute(WrsisPortalConstant.SAMPLE_TAG_ID);
				Integer rustId = (Integer) session.getAttribute(WrsisPortalConstant.RUST_ID);
				return "redirect:/raceAnalysisDetails?surveyId=" + surveyId + "&RustId=" + rustId + "&SampleId="
						+ sampleId + "&RustType=" + rustType + "&SampleTagId=" + SampleTagId + "&Message="
						+ "Date of Inoculation should not be less than from sample collected date.";
			}

			String SampleTagId = (String) session.getAttribute(WrsisPortalConstant.SAMPLE_TAG_ID);

			RaceAnalysis race = new RaceAnalysis();
			race.setBitstatus(false);
			race.setCreatedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
			race.setCreatedOn(new Date());
			race.setIncolutionDate(d2);
			race.setSampleTagId(Integer.valueOf(SampleTagId));
			race.setUpdatedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
			race.setUpdatedOn(new Date());
			race.setRaceStatus(0);
			race.setFirstWheatLine(wheatLine);
			raceAnalysisRepository.save(race);

			Optional<SampleLabTagDetails> sd = sampleLabTagDetailsRepository.findById(Integer.valueOf(SampleTagId));
			if (sd.isPresent()) {
				SampleLabTagDetails tag = sd.get();
				tag.setRaceStatus(1);
				sampleLabTagDetailsRepository.save(tag);
			}
			redirect.addFlashAttribute("ShowMessage", "Data saved successfully.");
			return "redirect:/stem-rust-race-analysis";

		} catch (Exception e) {
			LOG.error("RaceAnalysisController::saveInoculationDate():" + e);
			return "redirect:/stem-rust-race-analysis";
		}

	}

	public static Date convertStringToDate(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("RaceAnalysisController::convertStringToDate():" + ex.getMessage());
		}
		return date;
	}

	@RequestMapping(value = { "/inoculatedsample" })
	public String inoculatedsample(Model model, HttpServletRequest request, HttpSession session) {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, "");
		model.addAttribute(WrsisPortalConstant.SAMPLEID, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, "");
		model.addAttribute(WrsisPortalConstant.REGION_ID, "0");
		model.addAttribute(WrsisPortalConstant.SHOW_ALERT, false);
		return "inoculatedsample";
	}

	@RequestMapping(value = { "/stemRustRaceAnalysisInoculationSearch" })
	public String stemRustRaceAnalysisInoculationSearch(Model model, RaceAnalysisPendingSamples sample,
			HttpSession session) throws ParseException {

		String startDateB = sample.getAllocationStartDate();
		String endDateB = sample.getAllocationEndDate();

		SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String startDate = "";
		String endDate = "";
		if (!(startDateB.trim().equals("") || endDateB.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDateB);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDateB);
			endDate = smpl.format(date1);
		}
		RaceAnalysisPendingSamples backup = sample;

		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, backup.getSurveyNo());
		model.addAttribute(WrsisPortalConstant.SAMPLEID, backup.getSampleId());
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, startDateB);
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, endDateB);
		model.addAttribute(WrsisPortalConstant.REGION_ID, backup.getRegionId());
		return "inoculatedsample";

	}

	// FILTER BY DAT TABLE PAGINATION

	@RequestMapping(value = { "/stemRustRaceAnalysisInoculationSearchDataTablePagination" })
	public @ResponseBody RaceAnalysisDataTable stemRustRaceAnalysisInoculationSearchDataTablePagination(Model model,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.ALLOCATION_START_DATE) String startD,
			@RequestParam(WrsisPortalConstant.ALLOCATION_END_DATE) String endD,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleid,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer region) throws ParseException {

		RaceAnalysisPendingSamples sample = new RaceAnalysisPendingSamples();
		sample.setAllocationStartDate(startD);
		sample.setAllocationEndDate(endD);
		sample.setSampleId(sampleid);
		sample.setRegionId(region);
		sample.setSurveyNo(surveyNo);

		String startDateB = sample.getAllocationStartDate();
		String endDateB = sample.getAllocationEndDate();

		SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String startDate = "";
		String endDate = "";
		if (!(startDateB.trim().equals("") || endDateB.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDateB);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDateB);
			endDate = smpl.format(date1);
		}
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.fetchActiveInoculatedPending(
				sample.getSurveyNo(), sample.getSampleId(), startDate, endDate, sample.getRegionId(), userId,
				Integer.valueOf(start), Integer.valueOf(length));
		RaceAnalysisPendingSamples backup = sample;
		List<RaceAnalysisPendingSamples> samples = new ArrayList<>();
		Integer count = 0;
		for (Object[] obj : sampleTagDetails) {
			sample = new RaceAnalysisPendingSamples();
			sample.setsNo((Integer.valueOf(start)) + (++count));
			sample.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
					+ Integer.valueOf(String.valueOf(obj[9])) + "\">" + String.valueOf(obj[0]) + "</a>");
			sample.setSampleId(String.valueOf(obj[1]));
			sample.setRustType(String.valueOf(obj[2]));
			sample.setSampleCollectedOn(String.valueOf(obj[3]));
			sample.setInoculationDate(String.valueOf(obj[4]));
			sample.setRegion(String.valueOf(obj[5]));
			sample.setZone(String.valueOf(obj[6]));
			sample.setWoreda(String.valueOf(obj[7]));
			sample.setKebel(String.valueOf(obj[8]));
			sample.setSurveyId(Integer.valueOf(String.valueOf(obj[9])));
			sample.setSampleTagId(Integer.valueOf(String.valueOf(obj[10])));
			sample.setAnalysisId(Integer.valueOf(String.valueOf(obj[11])));
			sample.setRepeatation(String.valueOf(obj[12]));
			sample.setRaceStatus(Integer.valueOf(String.valueOf(obj[13])));
			sample.setRustTypeId(Integer.valueOf(String.valueOf(obj[14])));

			String join = "'" + StringUtils.join(String.valueOf(obj[1]), "'");
			String action = "<a class=\"btn btn-info btn-sm\" data-toggle=\"tooltip\"  title=\"\""
					+ "					href=\"javascript:void(0) \" onclick=\"viewDetailss("
					+ Integer.valueOf(String.valueOf(obj[14])) + "," + Integer.valueOf(String.valueOf(obj[9])) + ",'"
					+ String.valueOf(obj[4]) + "'," + Integer.valueOf(String.valueOf(obj[11])) + "," + join
					+ ")\" data-original-title=\"View Details\">" + "<i class=\"icon-edit1\"></i></a>";

			sample.setAction(action);
			samples.add(sample);
		}

		Integer totalCount = sampleLabTagDetailsRepository.fetchActiveInoculatedPendingCount(backup.getSurveyNo(),
				backup.getSampleId(), startDate, endDate, backup.getRegionId(), userId, -1, -1);

		RaceAnalysisDataTable sdt = new RaceAnalysisDataTable();
		sdt.setData(samples);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;

	}

	@RequestMapping(value = { "/raceanalysisresult" })
	public String raceanalysisresult(Model model, HttpServletRequest request, HttpSession session) {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));
		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, "");
		model.addAttribute(WrsisPortalConstant.SAMPLEID, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, "");
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, "");
		model.addAttribute(WrsisPortalConstant.REGION_ID, "0");
		model.addAttribute(WrsisPortalConstant.SHOW_ALERT, false);

		return "raceanalysisresult";
	}

	@RequestMapping(value = { "/raceanalysisresultSearch" })
	public String raceanalysisresultSearch(Model model, HttpServletRequest request, RaceAnalysisPendingSamples sample1,
			HttpSession session) throws ParseException {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		RaceAnalysisPendingSamples backup = sample1;
		String startDateB = sample1.getAllocationStartDate();
		String endDateB = sample1.getAllocationEndDate();

		List<DemographicEntity> list = demographyRepository.fetchAllActiveDemographs1(1);
		model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, list);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, backup.getSurveyNo());
		model.addAttribute(WrsisPortalConstant.SAMPLEID, backup.getSampleId());
		model.addAttribute(WrsisPortalConstant.ALLOCATION_START_DATE, startDateB);
		model.addAttribute(WrsisPortalConstant.ALLOCATION_END_DATE, endDateB);
		model.addAttribute(WrsisPortalConstant.REGION_ID, backup.getRegionId());

		return "raceanalysisresult";
	}

	@RequestMapping(value = { "/raceanalysisresultSearchDatatablePagination" })
	public @ResponseBody RaceAnalysisDataTable raceanalysisresultSearchDatatablePagination(Model model,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.ALLOCATION_START_DATE) String startD,
			@RequestParam(WrsisPortalConstant.ALLOCATION_END_DATE) String endD,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleid,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer region

	) throws ParseException {
		RaceAnalysisPendingSamples sample1 = new RaceAnalysisPendingSamples();
		sample1.setAllocationStartDate(startD);
		sample1.setAllocationEndDate(endD);
		sample1.setSampleId(sampleid);
		sample1.setRegionId(region);
		sample1.setSurveyNo(surveyNo);

		RaceAnalysisPendingSamples backup = sample1;
		String startDateB = sample1.getAllocationStartDate();
		String endDateB = sample1.getAllocationEndDate();

		SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String startDate = "";
		String endDate = "";
		if (!(startDateB.trim().equals("") || endDateB.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDateB);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDateB);
			endDate = smpl.format(date1);
		}
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.fetchActiveRaceResult(
				backup.getSurveyNo().toUpperCase(), backup.getSampleId(), startDate, endDate, backup.getRegionId(),
				userId, Integer.valueOf(start), Integer.valueOf(length));
		int count = 0;
		List<RaceAnalysisPendingSamples> samples = new ArrayList<>();
		for (Object[] obj : sampleTagDetails) {
			RaceAnalysisPendingSamples sample = new RaceAnalysisPendingSamples();
			sample.setsNo((Integer.valueOf(start)) + (++count));
			sample.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
					+ Integer.valueOf(String.valueOf(obj[9])) + "\">" + String.valueOf(obj[0]) + "</a>");
			sample.setSampleId(String.valueOf(obj[1]));
			sample.setRustType(String.valueOf(obj[2]));
			sample.setSampleCollectedOn(String.valueOf(obj[3]));
			sample.setInoculationDate(String.valueOf(obj[4]));
			sample.setRegion(String.valueOf(obj[5]));
			sample.setZone(String.valueOf(obj[6]));
			sample.setWoreda(String.valueOf(obj[7]));
			sample.setKebel(String.valueOf(obj[8]));
			sample.setSurveyId(Integer.valueOf(String.valueOf(obj[9])));
			sample.setSampleTagId(Integer.valueOf(String.valueOf(obj[10])));
			sample.setAnalysisId(Integer.valueOf(String.valueOf(obj[11])));
			sample.setRepeatation(String.valueOf(obj[12]));
			sample.setRaceStatus(Integer.valueOf(String.valueOf(obj[13])));
			sample.setRustTypeId(Integer.valueOf(String.valueOf(obj[14])));
			sample.setRaceResult(((obj[15] != null) ? String.valueOf(obj[15]) : "").trim().length() == 0 ? "--"
					: (obj[15] != null) ? String.valueOf(obj[15]) : "");
			sample.setPublish(Boolean.parseBoolean(String.valueOf(obj[16])));
			sample.setPublicshDate(
					(Boolean.parseBoolean(String.valueOf(obj[16])) == true) ? String.valueOf(obj[17]) : "--");
			String action = "";
			String join = "'" + StringUtils.join(String.valueOf(obj[1]), "'");
			String join1 = ",'" + StringUtils.join(Integer.valueOf(String.valueOf(obj[10])), "'");
			if (Boolean.parseBoolean(String.valueOf(obj[16]))) {

				action = "<a class=\"btn btn-warning btn-sm\" data-toggle=\"tooltip\"  title=\"\""
						+ "					href=\"javascript:void(0)\" onclick=\"viewDetails("
						+ Integer.valueOf(String.valueOf(obj[14])) + "," + Integer.valueOf(String.valueOf(obj[9]))
						+ ",'" + String.valueOf(obj[4]) + "'," + Integer.valueOf(String.valueOf(obj[11])) + "," + join

						+ ")\" data-original-title=\"View Details\">" + "<i class=\"fa fa-eye\"></i></a>";
			} else {

				action = "<a class=\"btn btn-warning btn-sm\" data-toggle=\"tooltip\"  title=\"\""
						+ "					href=\"javascript:void(0)\" onclick=\"viewDetails("
						+ Integer.valueOf(String.valueOf(obj[14])) + "," + Integer.valueOf(String.valueOf(obj[9]))
						+ ",'" + String.valueOf(obj[4]) + "'," + Integer.valueOf(String.valueOf(obj[11])) + "," + join

						+ ")\" data-original-title=\"View Details\">" + "<i class=\"fa fa-eye\"></i></a>"
						+ "<a class=\"btn btn-info btn-sm\" data-toggle=\"tooltip\"  title=\"\""
						+ "					href=\"javascript:void(0)\" onclick=\"publishDetails("
						+ Integer.valueOf(String.valueOf(obj[14])) + "," + Integer.valueOf(String.valueOf(obj[9]))
						+ ",'" + String.valueOf(obj[4]) + "'," + Integer.valueOf(String.valueOf(obj[11])) + "," + join
						+ join1 + ")\" data-original-title=\"Publish\">" + "<i class=\"icon-edit1\"></i></a>";
			}

			sample.setAction(action);

			samples.add(sample);
		}

		Integer totalCount = sampleLabTagDetailsRepository.fetchActiveRaceResultCount(
				backup.getSurveyNo().toUpperCase(), backup.getSampleId(), startDate, endDate, backup.getRegionId(),
				userId, -1, -1);

		RaceAnalysisDataTable sdt = new RaceAnalysisDataTable();
		sdt.setData(samples);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@RequestMapping(value = { "/raceAnalysisFinalResult" })
	public String raceAnalysisFinalResult(HttpSession session, Model model,
			@RequestParam("SampleTagId") Integer sampleTagId, @RequestParam("SampleId") String sampleId,
			@RequestParam("raceAnalysisId") Integer analysisId, @RequestParam(WrsisPortalConstant.RUST_TYPEID) Integer rustTypeId,
			@RequestParam("SurveyId") Integer surveyId, @RequestParam("date") String date) throws JSONException {

		List<RaceAnalysisInoculation> inoculations = raceAnalysisInoculationRepository.fetchByAnalysisId(analysisId);

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		Integer firstWheatLineId = (raceAnalysis.isPresent()) ? raceAnalysis.get().getFirstWheatLine() : 0;
		Optional<WheatDifferentialLineEntity> ent = wheatDifferentialLineRepository.findById(firstWheatLineId);
		String firstWheatLine = (ent.isPresent()) ? ent.get().getDifLine() : "";
		model.addAttribute("Inoculations", inoculations);
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		model.addAttribute(WrsisPortalConstant.DATE, date);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_TAG_ID, sampleTagId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);
		model.addAttribute(WrsisPortalConstant.RUST_NAME, rustMaster.get().getVchRustType());
		model.addAttribute(WrsisPortalConstant.ANALYSIS_ID, analysisId);

		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute("raceAnalysisId", analysisId);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute("date", date);

		return "raceAnalysisFinalResult";
	}

	// save/publish race analysis

	@RequestMapping(value = { "/publishRaceAnalysis" })
	public String raceAnalysisFinalResult(HttpSession session, Model model, RedirectAttributes redirectAttribute,
			@RequestParam("SampleId") Integer sampleId, @RequestParam("document") MultipartFile file,
			@RequestParam(WrsisPortalConstant.ANALYSIS_ID) Integer analysisId, @RequestParam("FinalResult") String finalResult)
			throws JSONException, IOException {
		
		SecureRandom random = new SecureRandom();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		Date datee = new Date();
		String date = dateFormat.format(datee);
		String month = monthFormat.format(datee);
		String year = String.valueOf(datee.getYear());
		int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

		String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
		String fileName = "RACE_DOC_" + randomNo + date + month + year + "." + extensionName;

		String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH
				+ File.separator + fileName; // should be dynamic
		String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH + File.separator;
		File folderCreate = new File(folder);
		if (!folderCreate.exists()) {
			folderCreate.mkdir();
			Path path = Paths.get(folder);
			Files.createDirectories(path);
		}
		File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH);
		if (!file1.exists()) {
			file1.mkdir();
		}
		if (!file1.exists()) {
			Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH);
			Files.createDirectories(path);
		}
		FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath));
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		raceAnalysisService.publishRaceAnalysis(fileName, analysisId, finalResult, sampleId, userId);

		redirectAttribute.addFlashAttribute("RaceMessage", "Race published successfully.");

		return "redirect:/raceanalysisresult";
	}

	// view
	@RequestMapping(value = { "/raceAnalysisNewDetailsView" })
	public String raceAnalysisNewDetailsView(HttpSession session, HttpServletRequest request, Model model,
			@RequestParam("ShowAll") Integer showAll, @RequestParam("SampleId") String sampleId,
			@RequestParam("raceAnalysisId") Integer analysisId, @RequestParam(WrsisPortalConstant.RUST_TYPEID) Integer rustTypeId,
			@RequestParam("SurveyId") Integer surveyId, @RequestParam("date") String date)
			throws JSONException, ParseException {
		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);

		List<Object[]> jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetailsYellow(rustTypeId);
		if (rustMaster.get().getVchRustType().toLowerCase().contains("yellow")) {
			session = request.getSession();
			session.setAttribute(WrsisPortalConstant.RACE_ANALYSIS_ID_NAME, analysisId);
			session.setAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
			session.setAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
			session.setAttribute("date", date);
			session.setAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
			session.setAttribute(WrsisPortalConstant.SHOW_ALL, showAll);
			return "redirect:/raceAnalysisNewDetailsYellowView";
		} else {
			jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetails(rustTypeId);
		}
		// raceAnalysisNewDetailsYellow.jsp
		JSONObject finalJson = new JSONObject();
		List<Object[]> chartsobj = sampleLabTagDetailsRepository.fetchChartDetails();
		JSONObject chartJson = new JSONObject();
		for (int i = 0; i < chartsobj.size(); i++) {
			chartJson.put(((String) (chartsobj.get(i)[0])).toLowerCase(), (String) (chartsobj.get(i)[1]));
		}

		List<String> results = new ArrayList<>();

		// repeatations check and fetch the repeatations details
		List<RaceAnalysisInoculation> inoculations = raceAnalysisInoculationRepository.fetchByAnalysisId(analysisId);
		JSONArray repeatationJsa = new JSONArray();
		for (RaceAnalysisInoculation raceAnalysisInoculation : inoculations) {

			JSONObject json = new JSONObject();
			// keys value
			results.add(raceAnalysisInoculation.getResult());
			Integer repeatationNo = raceAnalysisInoculation.getRepeatationNo();
			Date inoculationDate = raceAnalysisInoculation.getInoculationDate();
			Date recordingDate = raceAnalysisInoculation.getRecordingDate();
			Integer inoculationId = raceAnalysisInoculation.getRaceIncoulationId();

			DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);

			json.put(WrsisPortalConstant.REPEATATION_NUMBER, repeatationNo);
			json.put(WrsisPortalConstant.INOCULATION_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(inoculationDate.toString().split(" ")[0])));
			json.put(WrsisPortalConstant.RECORDING_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(recordingDate.toString().split(" ")[0])));
			json.put("InoculationId", inoculationId);
			json.put(WrsisPortalConstant.INCOULATION_ID, raceAnalysisInoculation.getRaceIncoulationId());

			JSONArray innerJsa = new JSONArray();

			List<RaceAnalysisScore> scores = raceAnalysisScoreRepository.findByInoculationId(inoculationId);

			for (RaceAnalysisScore score : scores) {
				JSONObject jobj = new JSONObject();
				Integer wheatLineId = score.getWheatLineId();
				List<Object[]> obj = raceAnalysisInoculationRepository.fetchWheatLineDetails(wheatLineId);
				String diffLine = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[0] == null) ? "" : obj.get(0)[0]);
				String seedSrc = String
						.valueOf((obj == null && obj.isEmpty() || obj.get(0)[1] == null) ? "" : obj.get(0)[1]);
				String gene = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[2] == null) ? "" : obj.get(0)[2]);
				String lowIt = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[3] == null) ? "" : obj.get(0)[3]);
				String id = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[5] == null) ? "" : obj.get(0)[5]);
				String setNo = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[4] == null) ? "" : obj.get(0)[4]);
				String HL = score.getHlValue();
				String infType = score.getInfType();
				String remark = score.getRemarks();

				jobj.put(WrsisPortalConstant.DIFF_LINE, diffLine);
				jobj.put(WrsisPortalConstant.SEED_SRC, seedSrc);
				jobj.put("Gene", gene);
				jobj.put(WrsisPortalConstant.LOW_IT, lowIt);
				jobj.put("ID", id);
				jobj.put(WrsisPortalConstant.SET_NO, setNo);
				jobj.put("HL", HL);
				jobj.put(WrsisPortalConstant.INFTYPE, infType);
				jobj.put(WrsisPortalConstant.REMARK1, remark);

				innerJsa.put(jobj);
			}

			json.put(WrsisPortalConstant.REPEATATION, innerJsa);

			repeatationJsa.put(json);
		}

		for (Iterator iterator = jsa.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			String key = String.valueOf(objects[4]);
			if (finalJson.has(key)) {
				JSONArray ar = finalJson.getJSONArray(key);
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));

				ar.put(j);
				finalJson.remove(key);
				finalJson.put(key, ar);
			} else {
				JSONArray ar = new JSONArray();
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				ar.put(j);
				finalJson.put(key, ar);
			}

		}
		model.addAttribute("Result", results);
		model.addAttribute(WrsisPortalConstant.CHART, chartJson);
		model.addAttribute(WrsisPortalConstant.DYNAMIC_JSON, new String(Base64.getEncoder().encode(finalJson.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.RJSON, new String(Base64.getEncoder().encode(repeatationJsa.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.REPEATATION, repeatationJsa.length());
		model.addAttribute(WrsisPortalConstant.ANALYSIS_ID, analysisId);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));

		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		Integer firstWheatLineId = (raceAnalysis.isPresent()) ? raceAnalysis.get().getFirstWheatLine() : 0;
		Optional<WheatDifferentialLineEntity> ent = wheatDifferentialLineRepository.findById(firstWheatLineId);
		String firstWheatLine = (ent.isPresent()) ? ent.get().getDifLine() : "";
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		model.addAttribute(WrsisPortalConstant.DATE, date);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.RUST_NAME, rustMaster.get().getVchRustType());
		model.addAttribute(WrsisPortalConstant.SHOW_ALL, showAll);
		// Race scoring master
		List<RaceScoringMaster> raceMaster = raceScoringMasterRepository.findByRustId(rustTypeId);

		// make the race scoring master json
		JSONObject scoreMasterJson = new JSONObject();

		for (RaceScoringMaster scoring : raceMaster) {
			String type = scoring.getInfectionType();
			String hl = scoring.getHlValue();
			if (!scoreMasterJson.has(type)) {
				scoreMasterJson.put(type, hl);
			}
		}

		// file view
		Optional<RaceAnalysis> analysis = raceAnalysisRepository.findById(analysisId);

		if (analysis.isPresent() && analysis.get().isPublishStatus()) {
			model.addAttribute("File", new String(Base64.getEncoder().encode(analysis.get().getRaceDoc().getBytes())));
			model.addAttribute("FinalResult", analysis.get().getRaceResult());
			model.addAttribute("IsPDF", (analysis.get().getRaceDoc()
					.substring(analysis.get().getRaceDoc().lastIndexOf(".") + 1, analysis.get().getRaceDoc().length())
					.trim().toLowerCase().equalsIgnoreCase("pdf")) ? true : false);
		}

		model.addAttribute(WrsisPortalConstant.INFECTION_TYPE, scoreMasterJson);
		return "raceAnalysisNewDetailsView";
	}

	@RequestMapping(value = "/public/showDocument", method = RequestMethod.GET)
	void downloadAdvisoryfile(HttpServletResponse response, @RequestParam(value = "path", required = false) String path)
			throws IOException {
		path = new String(Base64.getDecoder().decode(path));

		Path file = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH, path);
		if (Files.exists(file)) {
			if ((path.substring(path.lastIndexOf(".") + 1, path.length()).trim().toLowerCase()
					.equalsIgnoreCase("pdf"))) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("application/jpeg");
			}
			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + path);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				LOG.error("RaceAnalysisController::downloadAdvisoryfile():" + ex.getMessage());
			}
		}

	}

	@RequestMapping(value = { "/raceAnalysisNewDetailsYellowView" })
	public String raceAnalysisNewDetailsYellowView(HttpSession session, Model model,
			@RequestParam(value = "ShowAll", required = false) Integer showAll,
			@RequestParam(value = "SampleId", required = false) String sampleId,
			@RequestParam(value = "raceAnalysisId", required = false) Integer analysisId,
			@RequestParam(value = WrsisPortalConstant.RUST_TYPEID, required = false) Integer rustTypeId,
			@RequestParam(value = "SurveyId", required = false) Integer surveyId,
			@RequestParam(value = "date", required = false) String date) throws JSONException, ParseException {

		analysisId = (Integer) session.getAttribute(WrsisPortalConstant.RACE_ANALYSIS_ID_NAME);
		rustTypeId = (Integer) session.getAttribute(WrsisPortalConstant.RUST_TYPEID);
		surveyId = (Integer) session.getAttribute(WrsisPortalConstant.SURVEY_ID);
		date = (String) session.getAttribute("date");
		sampleId = (String) session.getAttribute(WrsisPortalConstant.SAMPLE_ID);
		showAll = (Integer) session.getAttribute(WrsisPortalConstant.SHOW_ALL);

		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);

		List<Object[]> jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetailsYellow(rustTypeId);

		// raceAnalysisNewDetailsYellow.jsp
		JSONObject finalJson = new JSONObject();
		List<Object[]> chartsobj = sampleLabTagDetailsRepository.fetchChartDetails();
		JSONObject chartJson = new JSONObject();
		for (int i = 0; i < chartsobj.size(); i++) {
			chartJson.put(((String) (chartsobj.get(i)[0])).toLowerCase(), (String) (chartsobj.get(i)[1]));
		}

		List<String> results = new ArrayList<>();
		List<String> resultsScore = new ArrayList<>();
		// repeatations check and fetch the repeatations details
		List<RaceAnalysisInoculation> inoculations = raceAnalysisInoculationRepository.fetchByAnalysisId(analysisId);
		JSONArray repeatationJsa = new JSONArray();
		for (RaceAnalysisInoculation raceAnalysisInoculation : inoculations) {

			JSONObject json = new JSONObject();
			// keys value
			results.add(raceAnalysisInoculation.getResult());
			resultsScore.add(raceAnalysisInoculation.getVchYellowResult());
			Integer repeatationNo = raceAnalysisInoculation.getRepeatationNo();
			Date inoculationDate = raceAnalysisInoculation.getInoculationDate();
			Date recordingDate = raceAnalysisInoculation.getRecordingDate();
			Integer inoculationId = raceAnalysisInoculation.getRaceIncoulationId();

			DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);

			json.put(WrsisPortalConstant.REPEATATION_NUMBER, repeatationNo);
			json.put(WrsisPortalConstant.INOCULATION_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(inoculationDate.toString().split(" ")[0])));
			json.put(WrsisPortalConstant.RECORDING_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(recordingDate.toString().split(" ")[0])));
			json.put("InoculationId", inoculationId);
			json.put(WrsisPortalConstant.INCOULATION_ID, raceAnalysisInoculation.getRaceIncoulationId());

			JSONArray innerJsa = new JSONArray();

			List<RaceAnalysisScore> scores = raceAnalysisScoreRepository.findByInoculationId(inoculationId);

			for (RaceAnalysisScore score : scores) {
				JSONObject jobj = new JSONObject();
				Integer wheatLineId = score.getWheatLineId();
				List<Object[]> obj = raceAnalysisInoculationRepository.fetchWheatLineDetailsYellow(wheatLineId);
				String diffLine = String.valueOf(obj.get(0)[0]);
				String seedSrc = String.valueOf((obj.get(0)[1] == null) ? "" : obj.get(0)[1]);
				String gene = String.valueOf(obj.get(0)[2]);
				String lowIt = String.valueOf(obj.get(0)[3]);
				String id = String.valueOf(obj.get(0)[5]);
				String setNo = String.valueOf(obj.get(0)[4]);
				String HL = score.getHlValue();
				String infType = score.getInfType();
				String remark = score.getRemarks();

				jobj.put(WrsisPortalConstant.DIFF_LINE, diffLine);
				jobj.put(WrsisPortalConstant.SEED_SRC, seedSrc);
				jobj.put("Gene", gene);
				jobj.put(WrsisPortalConstant.LOW_IT, lowIt);
				jobj.put("ID", id);
				jobj.put(WrsisPortalConstant.SET_NO, setNo);
				jobj.put("HL", HL);
				jobj.put(WrsisPortalConstant.INFTYPE, infType);
				jobj.put(WrsisPortalConstant.REMARK1, remark);

				innerJsa.put(jobj);
			}

			json.put(WrsisPortalConstant.REPEATATION, innerJsa);

			repeatationJsa.put(json);
		}

		model.addAttribute(WrsisPortalConstant.RESULT, results);
		model.addAttribute("ResultsScore", resultsScore);

		for (Iterator iterator = jsa.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			String key = "test";
			if (finalJson.has(key)) {
				JSONArray ar = finalJson.getJSONArray(key);
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				j.put(WrsisPortalConstant.PHENO_TYPE, (String) objects[7]);

				ar.put(j);
				finalJson.remove(key);
				finalJson.put(key, ar);
			} else {
				JSONArray ar = new JSONArray();
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				j.put(WrsisPortalConstant.PHENO_TYPE, (String) objects[7]);
				ar.put(j);
				finalJson.put(key, ar);
			}

		}
		model.addAttribute(WrsisPortalConstant.CHART, chartJson);
		model.addAttribute(WrsisPortalConstant.DYNAMIC_JSON, new String(Base64.getEncoder().encode(finalJson.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.RJSON, new String(Base64.getEncoder().encode(repeatationJsa.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.REPEATATION, repeatationJsa.length());
		model.addAttribute(WrsisPortalConstant.ANALYSIS_ID, analysisId);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));

		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		Integer firstWheatLineId = (raceAnalysis.isPresent()) ? raceAnalysis.get().getFirstWheatLine() : 0;
		Optional<WheatDifferentialLineEntity> ent = wheatDifferentialLineRepository.findById(firstWheatLineId);
		String firstWheatLine = (ent.isPresent()) ? ent.get().getDifLine() : "";
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		model.addAttribute(WrsisPortalConstant.DATE, date);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.RUST_NAME, rustMaster.get().getVchRustType());

		model.addAttribute(WrsisPortalConstant.SHOW_ALL, showAll);

		// Race scoring master
		List<RaceScoringMaster> raceMaster = raceScoringMasterRepository.findByRustId(rustTypeId);

		// make the race scoring master json
		JSONObject scoreMasterJson = new JSONObject();

		for (RaceScoringMaster scoring : raceMaster) {
			String type = scoring.getInfectionType();
			String hl = scoring.getHlValue();
			if (!scoreMasterJson.has(type)) {
				scoreMasterJson.put(type, hl);
			}
		}

		// file view
		Optional<RaceAnalysis> analysis = raceAnalysisRepository.findById(analysisId);

		if (analysis.isPresent() && analysis.get().isPublishStatus()) {
			model.addAttribute("File", new String(Base64.getEncoder().encode(analysis.get().getRaceDoc().getBytes())));
			model.addAttribute("FinalResult", analysis.get().getRaceResult());
			model.addAttribute("IsPDF", (analysis.get().getRaceDoc()
					.substring(analysis.get().getRaceDoc().lastIndexOf(".") + 1, analysis.get().getRaceDoc().length())
					.trim().toLowerCase().equalsIgnoreCase("pdf")) ? true : false);
		}

		model.addAttribute(WrsisPortalConstant.INFECTION_TYPE, scoreMasterJson);
		return "raceAnalysisNewDetailsYellowView";
	}

	@Autowired
	RaceAnalysisInoculationRepository raceAnalysisInoculationRepository;

	@Autowired
	RaceAnalysisScoreRepository raceAnalysisScoreRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@RequestMapping(value = { "/raceAnalysisNewDetailsYellow" })
	public String raceAnalysisNewDetailsYellow(HttpSession session, Model model,
			@RequestParam(value = "SampleId", required = false) String sampleId,
			@RequestParam(value = "raceAnalysisId", required = false) Integer analysisId,
			@RequestParam(value = WrsisPortalConstant.RUST_TYPEID, required = false) Integer rustTypeId,
			@RequestParam(value = "SurveyId", required = false) Integer surveyId,
			@RequestParam(value = "date", required = false) String date) throws JSONException, ParseException {

		analysisId = (Integer) session.getAttribute(WrsisPortalConstant.RACE_ANALYSIS_ID_NAME);
		rustTypeId = (Integer) session.getAttribute(WrsisPortalConstant.RUST_TYPEID);
		surveyId = (Integer) session.getAttribute(WrsisPortalConstant.SURVEY_ID);
		date = (String) session.getAttribute("date");
		sampleId = (String) session.getAttribute(WrsisPortalConstant.SAMPLE_ID);

		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);

		List<Object[]> jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetailsYellow(rustTypeId);

		// raceAnalysisNewDetailsYellow.jsp
		JSONObject finalJson = new JSONObject();
		List<Object[]> chartsobj = sampleLabTagDetailsRepository.fetchChartDetails();
		JSONObject chartJson = new JSONObject();
		for (int i = 0; i < chartsobj.size(); i++) {
			chartJson.put(((String) (chartsobj.get(i)[0])).toLowerCase(), (String) (chartsobj.get(i)[1]));
		}

		List<String> results = new ArrayList<>();
		List<String> resultsScore = new ArrayList<>();
		// repeatations check and fetch the repeatations details
		List<RaceAnalysisInoculation> inoculations = raceAnalysisInoculationRepository.fetchByAnalysisId(analysisId);
		JSONArray repeatationJsa = new JSONArray();
		for (RaceAnalysisInoculation raceAnalysisInoculation : inoculations) {

			JSONObject json = new JSONObject();
			// keys value
			results.add(raceAnalysisInoculation.getResult());
			resultsScore.add(raceAnalysisInoculation.getVchYellowResult());
			Integer repeatationNo = raceAnalysisInoculation.getRepeatationNo();
			Date inoculationDate = raceAnalysisInoculation.getInoculationDate();
			Date recordingDate = raceAnalysisInoculation.getRecordingDate();
			Integer inoculationId = raceAnalysisInoculation.getRaceIncoulationId();

			DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);

			json.put(WrsisPortalConstant.REPEATATION_NUMBER, repeatationNo);
			json.put(WrsisPortalConstant.INOCULATION_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(inoculationDate.toString().split(" ")[0])));
			json.put(WrsisPortalConstant.RECORDING_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(recordingDate.toString().split(" ")[0])));
			json.put("InoculationId", inoculationId);
			json.put(WrsisPortalConstant.INCOULATION_ID, raceAnalysisInoculation.getRaceIncoulationId());

			JSONArray innerJsa = new JSONArray();

			List<RaceAnalysisScore> scores = raceAnalysisScoreRepository.findByInoculationId(inoculationId);

			for (RaceAnalysisScore score : scores) {
				JSONObject jobj = new JSONObject();
				Integer wheatLineId = score.getWheatLineId();
				List<Object[]> obj = raceAnalysisInoculationRepository.fetchWheatLineDetailsYellow(wheatLineId);
				String diffLine = String.valueOf(obj.get(0)[0]);
				String seedSrc = String.valueOf((obj.get(0)[1] == null) ? "" : obj.get(0)[1]);
				String gene = String.valueOf(obj.get(0)[2]);
				String lowIt = String.valueOf(obj.get(0)[3]);
				String id = String.valueOf(obj.get(0)[5]);
				String setNo = String.valueOf(obj.get(0)[4]);
				String HL = score.getHlValue();
				String infType = score.getInfType();
				String remark = score.getRemarks();

				jobj.put(WrsisPortalConstant.DIFF_LINE, diffLine);
				jobj.put(WrsisPortalConstant.SEED_SRC, seedSrc);
				jobj.put("Gene", gene);
				jobj.put(WrsisPortalConstant.LOW_IT, lowIt);
				jobj.put("ID", id);
				jobj.put(WrsisPortalConstant.SET_NO, setNo);
				jobj.put("HL", HL);
				jobj.put(WrsisPortalConstant.INFTYPE, infType);
				jobj.put(WrsisPortalConstant.REMARK1, remark);

				innerJsa.put(jobj);
			}

			json.put(WrsisPortalConstant.REPEATATION, innerJsa);

			repeatationJsa.put(json);
		}

		model.addAttribute(WrsisPortalConstant.RESULT, results);
		model.addAttribute("ResultsScore", resultsScore);

		for (Iterator iterator = jsa.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			String key = "test";
			if (finalJson.has(key)) {
				JSONArray ar = finalJson.getJSONArray(key);
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				j.put(WrsisPortalConstant.PHENO_TYPE, (String) objects[7]);

				ar.put(j);
				finalJson.remove(key);
				finalJson.put(key, ar);
			} else {
				JSONArray ar = new JSONArray();
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				j.put(WrsisPortalConstant.PHENO_TYPE, (String) objects[7]);
				ar.put(j);
				finalJson.put(key, ar);
			}

		}
		model.addAttribute(WrsisPortalConstant.CHART, chartJson);
		model.addAttribute(WrsisPortalConstant.DYNAMIC_JSON, new String(Base64.getEncoder().encode(finalJson.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.RJSON, new String(Base64.getEncoder().encode(repeatationJsa.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.REPEATATION, repeatationJsa.length());
		model.addAttribute(WrsisPortalConstant.ANALYSIS_ID, analysisId);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		Integer firstWheatLineId = (raceAnalysis.isPresent()) ? raceAnalysis.get().getFirstWheatLine() : 0;
		Optional<WheatDifferentialLineEntity> ent = wheatDifferentialLineRepository.findById(firstWheatLineId);
		String firstWheatLine = (ent.isPresent()) ? ent.get().getDifLine() : "";
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		model.addAttribute(WrsisPortalConstant.DATE, date);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.RUST_NAME, rustMaster.get().getVchRustType());

		// Race scoring master
		List<RaceScoringMaster> raceMaster = raceScoringMasterRepository.findByRustId(rustTypeId);

		// make the race scoring master json
		JSONObject scoreMasterJson = new JSONObject();

		for (RaceScoringMaster scoring : raceMaster) {
			String type = scoring.getInfectionType();
			String hl = scoring.getHlValue();
			if (!scoreMasterJson.has(type)) {
				scoreMasterJson.put(type, hl);
			}
		}

		model.addAttribute(WrsisPortalConstant.INFECTION_TYPE, scoreMasterJson);
		return "raceAnalysisNewDetailsYellow";
	}

	@RequestMapping(value = { "/raceAnalysisNewDetails" })
	public String raceAnalysisNewDetails(HttpSession session, HttpServletRequest request, Model model,
			@RequestParam("SampleId") String sampleId, @RequestParam("raceAnalysisId") Integer analysisId,
			@RequestParam(WrsisPortalConstant.RUST_TYPEID) Integer rustTypeId, @RequestParam("SurveyId") Integer surveyId,
			@RequestParam("date") String date) throws JSONException, ParseException {
		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);

		List<Object[]> jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetailsYellow(rustTypeId);
		if (rustMaster.get().getVchRustType().toLowerCase().contains("yellow")) {
			session = request.getSession();
			session.setAttribute(WrsisPortalConstant.RACE_ANALYSIS_ID_NAME, analysisId);
			session.setAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
			session.setAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
			session.setAttribute("date", date);
			session.setAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);

			return "redirect:/raceAnalysisNewDetailsYellow";
		} else {
			jsa = sampleLabTagDetailsRepository.fetchRaceStatusWiseRepeatationDetails(rustTypeId);
		}
		// raceAnalysisNewDetailsYellow.jsp
		JSONObject finalJson = new JSONObject();
		List<Object[]> chartsobj = sampleLabTagDetailsRepository.fetchChartDetails();
		JSONObject chartJson = new JSONObject();
		for (int i = 0; i < chartsobj.size(); i++) {
			chartJson.put(((String) (chartsobj.get(i)[0])).toLowerCase(), (String) (chartsobj.get(i)[1]));
		}

		List<String> results = new ArrayList<>();

		// repeatations check and fetch the repeatations details
		List<RaceAnalysisInoculation> inoculations = raceAnalysisInoculationRepository.fetchByAnalysisId(analysisId);
		JSONArray repeatationJsa = new JSONArray();
		for (RaceAnalysisInoculation raceAnalysisInoculation : inoculations) {

			JSONObject json = new JSONObject();
			// keys value
			results.add(raceAnalysisInoculation.getResult());
			Integer repeatationNo = raceAnalysisInoculation.getRepeatationNo();
			Date inoculationDate = raceAnalysisInoculation.getInoculationDate();
			Date recordingDate = raceAnalysisInoculation.getRecordingDate();
			Integer inoculationId = raceAnalysisInoculation.getRaceIncoulationId();

			DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);

			json.put(WrsisPortalConstant.REPEATATION_NUMBER, repeatationNo);
			json.put(WrsisPortalConstant.INOCULATION_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(inoculationDate.toString().split(" ")[0])));
			json.put(WrsisPortalConstant.RECORDING_DATE, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(df.parse(recordingDate.toString().split(" ")[0])));
			json.put("InoculationId", inoculationId);
			json.put(WrsisPortalConstant.INCOULATION_ID, raceAnalysisInoculation.getRaceIncoulationId());

			JSONArray innerJsa = new JSONArray();

			List<RaceAnalysisScore> scores = raceAnalysisScoreRepository.findByInoculationId(inoculationId);

			for (RaceAnalysisScore score : scores) {
				JSONObject jobj = new JSONObject();
				Integer wheatLineId = score.getWheatLineId();
				List<Object[]> obj = raceAnalysisInoculationRepository.fetchWheatLineDetails(wheatLineId);
				String diffLine = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[0] == null) ? "" : obj.get(0)[0]);
				String seedSrc = String
						.valueOf((obj == null && obj.isEmpty() || obj.get(0)[1] == null) ? "" : obj.get(0)[1]);
				String gene = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[2] == null) ? "" : obj.get(0)[2]);
				String lowIt = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[3] == null) ? "" : obj.get(0)[3]);
				String id = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[5] == null) ? "" : obj.get(0)[5]);
				String setNo = String
						.valueOf((obj == null || obj.isEmpty() || obj.get(0)[4] == null) ? "" : obj.get(0)[4]);
				String HL = score.getHlValue();
				String infType = score.getInfType();
				String remark = score.getRemarks();

				jobj.put(WrsisPortalConstant.DIFF_LINE, diffLine);
				jobj.put(WrsisPortalConstant.SEED_SRC, seedSrc);
				jobj.put("Gene", gene);
				jobj.put(WrsisPortalConstant.LOW_IT, lowIt);
				jobj.put("ID", id);
				jobj.put(WrsisPortalConstant.SET_NO, setNo);
				jobj.put("HL", HL);
				jobj.put(WrsisPortalConstant.INFTYPE, infType);
				jobj.put(WrsisPortalConstant.REMARK1, remark);

				innerJsa.put(jobj);
			}

			json.put(WrsisPortalConstant.REPEATATION, innerJsa);

			repeatationJsa.put(json);
		}

		for (Iterator iterator = jsa.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			String key = String.valueOf(objects[4]);
			if (finalJson.has(key)) {
				JSONArray ar = finalJson.getJSONArray(key);
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));

				ar.put(j);
				finalJson.remove(key);
				finalJson.put(key, ar);
			} else {
				JSONArray ar = new JSONArray();
				JSONObject j = new JSONObject();
				j.put(WrsisPortalConstant.DIFF_LINE, (String) objects[0]);
				j.put(WrsisPortalConstant.SEED_SRC, (String) objects[1]);
				j.put("Gene", (String) objects[2]);
				j.put(WrsisPortalConstant.LOW_IT, (String) objects[3]);
				j.put("ID", Integer.parseInt(String.valueOf(objects[5])));
				j.put(WrsisPortalConstant.SEQ_NO, Integer.parseInt(String.valueOf(objects[6])));
				ar.put(j);
				finalJson.put(key, ar);
			}

		}
		model.addAttribute(WrsisPortalConstant.RESULT, results);
		model.addAttribute(WrsisPortalConstant.CHART, chartJson);
		model.addAttribute(WrsisPortalConstant.DYNAMIC_JSON, new String(Base64.getEncoder().encode(finalJson.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.RJSON, new String(Base64.getEncoder().encode(repeatationJsa.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.REPEATATION, repeatationJsa.length());
		model.addAttribute(WrsisPortalConstant.ANALYSIS_ID, analysisId);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		Integer firstWheatLineId = (raceAnalysis.isPresent()) ? raceAnalysis.get().getFirstWheatLine() : 0;
		Optional<WheatDifferentialLineEntity> ent = wheatDifferentialLineRepository.findById(firstWheatLineId);
		String firstWheatLine = (ent.isPresent()) ? ent.get().getDifLine() : "";
		model.addAttribute(WrsisPortalConstant.USER_NAME, session.getAttribute(WrsisPortalConstant.USER_NAME));
		model.addAttribute(WrsisPortalConstant.WHEAT_LINE, firstWheatLine);
		model.addAttribute(WrsisPortalConstant.DATE, date);
		model.addAttribute(WrsisPortalConstant.RUST_TYPEID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SAMPLE_ID, sampleId);
		model.addAttribute(WrsisPortalConstant.RUST_NAME, rustMaster.get().getVchRustType());
		// Race scoring master
		List<RaceScoringMaster> raceMaster = raceScoringMasterRepository.findByRustId(rustTypeId);

		// make the race scoring master json
		JSONObject scoreMasterJson = new JSONObject();

		for (RaceScoringMaster scoring : raceMaster) {
			String type = scoring.getInfectionType();
			String hl = scoring.getHlValue();
			if (!scoreMasterJson.has(type)) {
				scoreMasterJson.put(type, hl);
			}
		}

		model.addAttribute(WrsisPortalConstant.INFECTION_TYPE, scoreMasterJson);
		return "raceAnalysisNewDetails";
	}

	@Autowired
	RaceAnalysisService raceAnalysisService;

	@Autowired
	RaceScoringMasterRepository raceScoringMasterRepository;

	@Autowired
	WheatDifferentialLineRepository wheatDifferentialLineRepository;

	@RequestMapping(value = { "/saveInoculatedRepeatations" })
	public String saveInoculatedRepeatations(@RequestParam("EncodeJSON") String repeatationEncodeJson,
			@RequestParam(WrsisPortalConstant.RUST_TYPEID) Integer rustTypeId, RedirectAttributes redirectAttribute, Model model,
			HttpSession session) throws JSONException {

		// decrypt the encoded json and store into DB
		// DATA, FinalResult,InoculatedDate , RecordingDate
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		raceAnalysisService.saveIncoulationDetails(repeatationEncodeJson, userId, rustTypeId);
		redirectAttribute.addFlashAttribute("SaveMessage", "Data saved successfully.");

		return "redirect:/inoculatedsample";
	}

	// dump details saved
	@RequestMapping(value = { "/dumpSampleTagDetails" }, method = RequestMethod.POST)
	@ResponseBody
	public String dumpSampleTagDetails(@RequestBody String jsonString, Model model, HttpServletRequest request)
			throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONArray json = new JSONArray(jsonString);
		JSONObject resp = new JSONObject();
		try {
			resp = raceAnalysisService.saveDumpSampleTagDetails(json, userId);
			return json.toString();
		} catch (Exception e) {
			LOG.error("RaceAnalysisController::dumpSampleTagDetails():" + e);
			return resp.toString();

		}

	}

	@RequestMapping(value = { "/raceAnalysisFirstRepetitionCompleted" })
	public String raceAnalysisFirstRepetitionCompleted() {

		return "raceAnalysisFirstRepetitionCompleted";
	}

	@RequestMapping(value = { "/raceAnalysisPendingDetails" })
	public String raceAnalysisPendingDetails() {

		return "raceAnalysisPendingDetails";
	}

	@RequestMapping(value = { "/viewRaceAnalysisFirstRepetition" })
	public String viewRaceAnalysisFirstRepetition() {

		return "viewRaceAnalysisFirstRepetition";
	}

	@RequestMapping(value = { "/viewRaceAnalysisSecondRepetition" })
	public String viewRaceAnalysisSecondRepetition() {

		return "viewRaceAnalysisSecondRepetition";
	}

	@RequestMapping(value = { "/leaf-rust-race-analysis" })
	public String leafraceanalysis() {

		return "leaf-rust-race-analysis";
	}

	@RequestMapping(value = { "/leafRustinoculatedsample" })
	public String leafRustinoculatedsample() {

		return "leafRustinoculatedsample";
	}

	@RequestMapping(value = { "/leafRustraceanalysisresult" })
	public String leafRustraceanalysisresult() {

		return "leafRustraceanalysisresult";
	}

	@RequestMapping(value = { "/leafRustRaceAnalysisDetails" })
	public String leafRustRaceAnalysisDetails() {

		return "leafRustRaceAnalysisDetails";
	}

	@RequestMapping(value = { "/leafRustRaceAnalysisNewDetails" })
	public String leafRustRaceAnalysisNewDetails() {

		return "leafRustRaceAnalysisNewDetails";
	}

	@RequestMapping(value = { "/leafRustRaceAnalysisFirstRepetitionCompleted" })
	public String leafRustRaceAnalysisFirstRepetitionCompleted() {

		return "leafRustRaceAnalysisFirstRepetitionCompleted";
	}

	@RequestMapping(value = { "/leafRustRaceAnalysisPendingDetails" })
	public String leafRustRaceAnalysisPendingDetails() {

		return "leafRustRaceAnalysisPendingDetails";
	}

	@RequestMapping(value = { "/leafRustRaceAnalysisFinalResult" })
	public String leafRustRaceAnalysisFinalResult() {

		return "leafRustRaceAnalysisFinalResult";
	}

	@RequestMapping(value = { "/viewLeafRustRaceAnalysisFirstRepetition" })
	public String viewLeafRustRaceAnalysisFirstRepetition() {

		return "viewLeafRustRaceAnalysisFirstRepetition";
	}

	@RequestMapping(value = { "/viewLeafRustRaceAnalysisSecondRepetition" })
	public String viewLeafRustRaceAnalysisSecondRepetition() {

		return "viewLeafRustRaceAnalysisSecondRepetition";
	}

	@RequestMapping(value = { "/yellow-rust-race-analysis" })
	public String yellowraceanalysis() {

		return "yellow-rust-race-analysis";
	}

	@RequestMapping(value = { "/yellowRustRaceAnalysisDetails" })
	public String yellowRustRaceAnalysisDetails() {

		return "yellowRustRaceAnalysisDetails";
	}

	@RequestMapping(value = { "/yellowRustinoculatedsample" })
	public String yellowRustinoculatedsample() {

		return "yellowRustinoculatedsample";
	}

	@RequestMapping(value = { "/yellowRustRaceAnalysisNewDetails" })
	public String yellowRustRaceAnalysisNewDetails() {

		return "yellowRustRaceAnalysisNewDetails";
	}

	@RequestMapping(value = { "/yellowRustRaceAnalysisFirstRepetitionCompleted" })
	public String yellowRustRaceAnalysisFirstRepetitionCompleted() {

		return "yellowRustRaceAnalysisFirstRepetitionCompleted";
	}

	@RequestMapping(value = { "/yellowRustRaceAnalysisPendingDetails" })
	public String yellowRustRaceAnalysisPendingDetails() {

		return "yellowRustRaceAnalysisPendingDetails";
	}

	@RequestMapping(value = { "/yellowRustraceanalysisresult" })
	public String yellowRustraceanalysisresult() {

		return "yellowRustraceanalysisresult";
	}

	@RequestMapping(value = { "/yellowRustRaceAnalysisFinalResult" })
	public String yellowRustRaceAnalysisFinalResult() {

		return "yellowRustRaceAnalysisFinalResult";
	}

	@RequestMapping(value = { "/viewYellowRustRaceAnalysisFirstRepetition" })
	public String viewYellowRustRaceAnalysisFirstRepetition() {

		return "viewYellowRustRaceAnalysisFirstRepetition";
	}

	@RequestMapping(value = { "/viewYellowRustRaceAnalysisSecondRepetition" })
	public String viewYellowRustRaceAnalysisSecondRepetition() {

		return "viewYellowRustRaceAnalysisSecondRepetition";
	}

	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserResearchCenterMappingRepository userResearchCenterMappingRepository;

	@Autowired
	ManageDemographicService manageDemographicService;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@RequestMapping(value = { "/viewRaceAnalysisStatus" })
	public String viewRaceAnalysisStatus(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute("rust", typeOfRustRepository.getRustTypes());
		} catch (Exception e) {
			LOG.error("RaceAnalysisController::viewRaceAnalysisStatus():" + e);
		}
		return "viewRaceAnalysisStatus";
	}

	@RequestMapping(value = "downloadRaceAnalysisStatusExcel", method = RequestMethod.POST)
	public void downloadRaceAnalysisStatusExcel(HttpServletResponse response, HttpServletRequest request, Model model,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("surveyDateFrom") String startDate, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId,
			@RequestParam("surveyDateTo") String endDate, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("rustId") String rustTypeId) throws IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		List<Object[]> roleList = userRepository.getRoleByUserId(userId);

		Integer researchCenterId = 0;
		Integer currentUserId = 0;

		if (!roleList.isEmpty()) {
			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.SURVEYOR)) {
					currentUserId = userId;
				}
			}

			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.PATHOLOGIST)) {
					currentUserId = 0;

					// rc id

					UserResearchCenterMapping rcmapping = userResearchCenterMappingRepository.findByUserId(userId);
					Integer rcid = rcmapping.getRecearchCenterId();
					researchCenterId = rcid;

				}

			}

		}
		ReportExcel reportExcel = new ReportExcel();
		List<SampleTagBean> beans = raceAnalysisService.downloadRaceAnalysisStatusExcel(surveyNo, sampleId, startDate,
				endDate, regionId, rustTypeId, currentUserId, researchCenterId);
		HSSFWorkbook myExcelBook = reportExcel.buildRaceAnalysisStatusExcel(beans);

		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=RaceAnalysisStatus_" + formattedDate + ".xls");

		OutputStream out = response.getOutputStream();
		myExcelBook.write(out);

		out.flush();
		out.close();
		myExcelBook.close();
	}

	@RequestMapping(value = { "/downloadRaceAnalysisStatusPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadRaceAnalysisStatusPdf(HttpServletResponse response,
			HttpServletRequest request, Model model, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("surveyDateFrom") String startDate, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId,
			@RequestParam("surveyDateTo") String endDate, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("rustId") String rustTypeId) throws IOException {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		List<Object[]> roleList = userRepository.getRoleByUserId(userId);

		Integer researchCenterId = 0;
		Integer currentUserId = 0;

		if (!roleList.isEmpty()) {
			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.SURVEYOR)) {
					currentUserId = userId;
				}
			}

			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.PATHOLOGIST)) {
					currentUserId = 0;

					// rc id

					UserResearchCenterMapping rcmapping = userResearchCenterMappingRepository.findByUserId(userId);
					Integer rcid = rcmapping.getRecearchCenterId();
					researchCenterId = rcid;

				}

			}

		}
		List<SampleTagBean> beans = raceAnalysisService.downloadRaceAnalysisStatusExcel(surveyNo, sampleId, startDate,
				endDate, regionId, rustTypeId, currentUserId, researchCenterId);

		ReportPDF psPdf = new ReportPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.generateRaceAnalysisStatusPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "RaceAnalysisStatus_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	// viewRaceAnalysisStatusSurveyNo

	@RequestMapping(value = { "/viewRaceAnalysisStatusSurveyNo" })
	public String viewRaceAnalysisStatusSurveyNo(@RequestParam("surveyId") Integer surveyId, Model model)
			throws JSONException {
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

		return "viewRaceAnalysisStatusSurveyNo";
	}

	// dynamic pagination view race analysis status

	@RequestMapping(value = { "/viewRaceAnalysisStatusData" })
	public @ResponseBody TagSampleDataTable viewRaceAnalysisStatusData(Model modelHttp, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo, @RequestParam("startDate") String startDate,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam("rustTypeId") String rustTypeId,
			RedirectAttributes redirect) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		List<Object[]> roleList = userRepository.getRoleByUserId(userId);

		Integer researchCenterId = 0;
		Integer currentUserId = 0;
		List<SampleTagBean> samples = null;
		Integer count = 0;
		if (!roleList.isEmpty()) {
			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.SURVEYOR)) {

					currentUserId = userId;
				}
			}

			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.PATHOLOGIST)) {

					currentUserId = 0;

					// rc id

					UserResearchCenterMapping rcmapping = userResearchCenterMappingRepository.findByUserId(userId);
					Integer rcid = rcmapping.getRecearchCenterId();

					researchCenterId = rcid;

				}

			}

		}

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.viewRaceAnalysis(surveyNo.toUpperCase(),
				sampleId, startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), currentUserId,
				researchCenterId, Integer.valueOf(start), Integer.valueOf(length));

		samples = new ArrayList<>();
		for (Object[] obj : sampleTagDetails) {
			SampleTagBean sample = new SampleTagBean();
			sample.setsNo((Integer.valueOf(start)) + (++count));
			sample.setSurveyId((Integer.parseInt(String.valueOf(obj[9]))));
			sample.setSurveyNo((obj[0] != null) ? (String) obj[0] : "");
			sample.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
					+ sample.getSurveyId() + "\">" + sample.getSurveyNo() + "</a>");
			sample.setSampleValue((obj[1] != null) ? (String) obj[1] : "");
			sample.setRustType((obj[2] != null) ? (String) obj[2] : "");
			sample.setSurveyDate((obj[3] != null) ? (String) obj[3] : "");
			sample.setInoculationDate((obj[4] != null) ? (String) obj[4] : "");
			sample.setRegion((obj[5] != null) ? (String) obj[5] : "");
			sample.setZone((obj[6] != null) ? (String) obj[6] : "");
			sample.setWoreda((obj[7] != null) ? (String) obj[7] : "");
			sample.setKebel((obj[8] != null) ? (String) obj[8] : "");
			if (obj[13] != null) {
				sample.setResearchCenter(obj[13].toString());
			} else {
				sample.setResearchCenter("External");
			}
			sample.setStatus((obj[11] != null) ? (String) obj[11] : "");
			if (obj[14] != null) {
				sample.setRaceResult(obj[14].toString());
			} else {
				sample.setRaceResult("--");
			}
			if (obj[15].toString().equals("false")) {
				sample.setRacePublishDate("--");
			} else {
				sample.setRacePublishDate(obj[16].toString());
			}

			samples.add(sample);
		}

		Integer totalCount = sampleLabTagDetailsRepository.viewRaceAnalysisCount(surveyNo.toUpperCase(), sampleId,
				startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), currentUserId,
				researchCenterId, -1, -1);

		TagSampleDataTable sdt = new TagSampleDataTable();
		sdt.setData(samples);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));

		return sdt;
	}

}
