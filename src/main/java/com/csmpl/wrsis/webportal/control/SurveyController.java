package com.csmpl.wrsis.webportal.control;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.IvrDataTable;
import com.csmpl.wrsis.datatable.SurveyDataTable;
import com.csmpl.wrsis.datatable.SurveyExcelFilesDataTable;
import com.csmpl.wrsis.pdf.IvrPDF;
import com.csmpl.wrsis.pdf.SurveyPDF;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyExcelFilesBean;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.FungicideEffectTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;
import com.csmpl.wrsis.webportal.entity.MoistureMasterEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.ReactionMasterEntity;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SiteTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SoilColorMasterEntity;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.entity.SurveyExcelFiles;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.WheatTypeMasterEntity;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.excel.PublishSurveyExcel;
import com.csmpl.wrsis.webportal.excel.SurveyExcelParser;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyExcelFilesRepository;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.IVRDataService;
import com.csmpl.wrsis.webportal.service.ManageIVRDataService;
import com.csmpl.wrsis.webportal.service.QuestionService;
import com.csmpl.wrsis.webportal.service.SurveyExcelDetailsService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class SurveyController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(SurveyController.class);

	@Autowired
	SurveyExcelFilesRepository surveyExcelFilesRepository;

	@Autowired
	SoilColorRepository soilColorRepository;

	@Autowired
	MoistureRepository moistureRepository;

	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;

	@Autowired
	SiteTypeRepository siteTypeRepository;

	@Autowired
	WheatTypeRepository wheatTypeRepository;

	@Autowired
	GrowthRepository growthRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@Autowired
	ReactionTypeRepository reactionTypeRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	SampleTypeRepository sampleTypeRepository;

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
	QuestionService questionService;

	@Autowired
	IVRDataQuestionRepository iVRDataQuestionRepository;

	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@Autowired
	UserRepository userRepository;
	@Autowired
	LevelDetailsRepository levelDetailsRepository;
	
	@RequestMapping(value = { "/addSurveyDetails" })
	public String addSurveyDetails(Model model, HttpSession session) {
		try {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			session.removeAttribute(WrsisPortalConstant.SURVEY_JSON);
			model = assignModelData(model, userId);
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			String strDate = formatter.format(date);
			model.addAttribute("CurrentDate", strDate);
			model.addAttribute("Update", false);
			model.addAttribute(WrsisPortalConstant.SURVEY_ID, 0);
		} catch (Exception e) {
			LOG.error("SurveyController::addSurveyDetails():" + e);
		}
		return "addSurveyDetails";
	}

	private Model assignModelData(Model model, Integer userId) {

		try {
			// fetch all the soil color master data
			List<SoilColorMasterEntity> soilColorList = soilColorRepository.fetchAllSoilActiveColors();
			model.addAttribute("SoilColors", soilColorList);

			// fetch all active moistures
			List<MoistureMasterEntity> moistureList = moistureRepository.fetchAllActiveMoistures();
			model.addAttribute("MoistureList", moistureList);

			// fetch all disease master repository
			List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
			model.addAttribute("DiseaseList", diseaseList);

			List<DiseaseMaster> diseaseListAny = diseaseMasterRepository.fetchAllActiveDisease(false);
			model.addAttribute("DiseaseListAny", diseaseListAny);

			// fetch all active site types
			List<SiteTypeMasterEntity> siteTypeList = siteTypeRepository.fetchAllActiveSitetypes();
			model.addAttribute("SiteTypeList", siteTypeList);

			// fetch all active site types
			List<WheatTypeMasterEntity> wheatTypeList = wheatTypeRepository.fetchAllActiveWheattypes();
			model.addAttribute("WheatTypeList", wheatTypeList);

			// fetch all active growth
			List<GrowthMasterEntity> growthList = growthRepository.fetchAllActiveGrowth();
			model.addAttribute("GrowthList", growthList);

			// fetch all active Rust types
			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
			model.addAttribute("RustTypeList", rustTypeList);

			// fetch the login user instituion

			List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
			model.addAttribute("InstitutionSelected", (obj != null && !obj.isEmpty()) ? obj.get(0) : null); // new
																											// ArrayList<Object[]>()
																											// changed
																											// to null
																											// by
																											// prasanta
																											// on
																											// 28/07/2020
			model.addAttribute("UserId", userId);

			// current season
			Integer seasonId = commonService.getCurrntSeasonId();
			model.addAttribute("SeasonId", seasonId);

			// fetch all active seasions

			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);

			// set the months accorrding to season id
			for (int j = 0; j < seasionList.size(); j++) {
				SeasionMasterEntity sme = seasionList.get(j);
				Integer season = sme.getIntSeasoonId();
				List<Object> mon = seasionMasterRepository.getSeasonMonths(season);
				String months = "[";
				for (int i = 0; i < mon.size(); i++) {
					months += String.valueOf(mon.get(i)) + ",";
				}
				if (!mon.isEmpty()) {
					months = months.substring(0, months.length() - 1);
				}
				months += "]";
				sme.setMonths(new String(Base64.getEncoder().encode(months.getBytes())));
				seasionList.set(j, sme);
			}

			// fetch all active Sample types
			List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();
			model.addAttribute("SampleTypeList", sampleTypeList);

			// fetch all active Fungis types
			List<FungicideMaster> fungiList = fungicideMasterRepository.fetchAllActiveFungicides();
			model.addAttribute("FungiList", fungiList);

			// fetch all active Fungis types
			List<FungicideEffectTypeMasterEntity> fungiEffectList = fungiEffectTypeRepository
					.fetchAllActiveFungiEffectTypes();
			model.addAttribute("FungiEffectList", fungiEffectList);

			// fetch all the variety type

			List<VarietyTypeMasterEntity> varietyList = varietyTypeRepository.fetchActiveVarietyType();
			model.addAttribute("VarietyList", varietyList);

			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf(obj.get(0)[0])))
					: null;
			if (researchCenterId != null) {
				List<Object[]> surveyors = commonService.getAllSurveyor(researchCenterId);
				model.addAttribute("Surveyors", surveyors);

				List<Object[]> demographList = demographyRepository.fetchAllActiveDemographs(researchCenterId);
				model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			} else {

				List<Object[]> demographList = demographyRepository.fetchAllActiveDemographsByParentId(1); // 1 used for
																											// ethopia
				model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);

				model.addAttribute("Institutions", researchCenterRepository.findActiveResearchCenter());

				model.addAttribute("Surveyors", userRepository.getUserDetailsByUserId(userId));

			}

		} catch (Exception e) {
			LOG.error("SurveyController::assignModelData():" + e);
		}

		return model;
	}

	// fetch the reaction type
	@RequestMapping(value = "/getReactionList", method = RequestMethod.POST)
	public @ResponseBody List<ReactionMasterEntity> getCountyList(Model model, HttpServletRequest request,
			@RequestParam(value = "rustTypeId", required = true) int intRustTypeId) {

//		List<com.csmpl.wrsis.webportal.entity.ReactionMasterEntity> reactionList = reactionTypeRepository
//				.fetchAllActiveReactionTypes();
		return reactionTypeRepository
				.fetchAllActiveReactionTypes();
	}

	@RequestMapping(value = "/getDemographicList", method = RequestMethod.POST)
	public @ResponseBody List<DemographicEntity> getDemographicList1(Model model, HttpServletRequest request,
			@RequestParam(value = "parentId", required = true) int intRustTypeId) {

//		List<DemographicEntity> reactionList = demographyRepository.fetchAllActiveDemographs1(intRustTypeId);
		return demographyRepository.fetchAllActiveDemographs1(intRustTypeId);

	}

	// fetch all demographic data
	@RequestMapping(value = "/getDemographicListZone", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> getDemographicList(Model model, HttpServletRequest request,
			@RequestParam(value = "parentId", required = true) int intZoneId) {

//		List<Object[]> reactionList = demographyRepository.fetchAllActiveDemographsZone(intZoneId); // as Eutopia id is
																									// 1
		return demographyRepository.fetchAllActiveDemographsZone(intZoneId);

	}

	@RequestMapping(value = "/getDemographicListWoreda", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> getDemographicListWoreda(Model model, HttpServletRequest request,
			@RequestBody String s) throws JSONException {
		JSONObject json = new JSONObject(new String(Base64.getDecoder().decode(s.getBytes())));
		int region = Integer.parseInt(json.getString(WrsisPortalConstant.REGION_ID));
		int zone = Integer.parseInt(json.getString(WrsisPortalConstant.ZONE_ID));
//		List<Object[]> reactionList = demographyRepository.fetchAllActiveDemographsWoreda(region, zone); // as Eutopia
																											// id is 1
		return demographyRepository.fetchAllActiveDemographsWoreda(region, zone);

	}

	@RequestMapping(value = "/getDemographicListKebele", method = RequestMethod.POST)
	public @ResponseBody List<DemographicEntity> getDemographicListKebele(Model model, HttpServletRequest request,
			@RequestBody String s) throws JSONException {
		JSONObject json = new JSONObject(new String(Base64.getDecoder().decode(s.getBytes())));
		int woredaId = Integer.valueOf(json.getString(WrsisPortalConstant.WOREDA_ID));
		List<DemographicEntity> reactionList = demographyRepository.fetchAllActiveDemographsKebele(woredaId); // as
																												// Eutopia
																												// id is
																												// 1
		return reactionList;

	}

	// delete survey details
	@RequestMapping(value = "/deleteSurveyDetails", method = RequestMethod.POST)
	public @ResponseBody String deleteSurveyDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyId", required = true) int surveyId) {
		return commonService.deleteSurveyDetails(surveyId);

	}

	// save survey details
	@RequestMapping(value = "/saveSurveyDetails", method = RequestMethod.POST)
	public String saveSurveyDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON, RedirectAttributes atrs,
			@RequestParam("surveyId") Integer surveyId, HttpSession session) throws JSONException {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		try {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String response = commonService.saveSurveyDetails(encodeJSON, surveyId, userId, 1, null, userId);
			List<SurveyDetails> details = surveyDetailsRepository.findActiveSurveyDetailsAll();
			model.addAttribute(WrsisPortalConstant.DETAILS, details);
			if (surveyId == 0) {
				atrs.addFlashAttribute("sNo",
						"Data saved successfully.Survey No is : " + new JSONObject(response).getString("sNo"));
			} else {
				atrs.addFlashAttribute("sNo", WrsisPortalConstant.DATA_UPDATED_SUCCESSFULLY);
			}
			return "redirect:/viewsurveydetails";
		} catch (Exception e) {
			LOG.error("SurveyController::saveSurveyDetails():" + e);
			JSONObject json = new JSONObject();
			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetails";
		}

	}

	@RequestMapping(value = "/saveSurveyDetailsEiar", method = RequestMethod.POST)
	public String saveSurveyDetailsEiar(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON, RedirectAttributes atrs,
			@RequestParam("surveyId") Integer surveyId, HttpSession session) throws JSONException {

		try {
			Integer userId = surveyDetailsRepository.findById(surveyId).get().getCreatedBy();
																								

			String response = commonService.saveSurveyDetails(encodeJSON, surveyId, userId, 1, null,
					(Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
			List<SurveyDetails> details = surveyDetailsRepository.findActiveSurveyDetailsAll();
			model.addAttribute(WrsisPortalConstant.DETAILS, details);
			if (surveyId == 0) {
				atrs.addFlashAttribute("sNo",
						"Data saved successfully.Survey No is : " + new JSONObject(response).getString("sNo"));
			} else {
				atrs.addFlashAttribute("sNo", WrsisPortalConstant.DATA_UPDATED_SUCCESSFULLY);
			}
			return "redirect:/pendingsurvey";
		} catch (Exception e) {
			LOG.error("SurveyController::saveSurveyDetailsEiar():" + e);
			JSONObject json = new JSONObject();
			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetailsEiar";
		}

	}

	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;
	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;

	@RequestMapping(value = "/saveSurveyDetailsDiscardSurveyor", method = RequestMethod.POST)
	public String saveSurveyDetailsDiscardSurveyor(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON, RedirectAttributes atrs,
			@RequestParam("surveyId") Integer surveyId, HttpSession session) throws JSONException {

		try {
			Integer userId = surveyDetailsRepository.findById(surveyId).get().getCreatedBy();
																								

			String response = commonService.saveSurveyDetails(encodeJSON, surveyId, userId, 1, null,
					(Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
			List<SurveyDetails> details = surveyDetailsRepository.findActiveSurveyDetailsAll();
			model.addAttribute(WrsisPortalConstant.DETAILS, details);
			if (surveyId == 0) {
				atrs.addFlashAttribute("sNo",
						"Data saved successfully.Survey No is : " + new JSONObject(response).getString("sNo"));
			} else {
				Optional<SurveyDetails> sdtls = surveyDetailsRepository.findById(surveyId);
				sdtls.get().setSurveyStatus(0);
				surveyDetailsRepository.save(sdtls.get());
				atrs.addFlashAttribute("sNo", WrsisPortalConstant.DATA_UPDATED_SUCCESSFULLY);


				List<Object[]> obj = approvalAuthorityEntityRepository.fetchApproveDetails(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID));

				ApprovalAuthorityEntity ae = approvalAuthorityEntityRepository.getDataByAppIdAndProcessId(surveyId,
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID));

				
				ae.setPendingAt((Integer) obj.get(0)[2]);
				ae.setSentFrom(userId);
				ae.setStageNo((Integer) obj.get(0)[1]);

				ae.setUpdatedBy(userId);
				ae.setUpdatedOn(new Date());
				approvalAuthorityEntityRepository.save(ae);

				// Save ApprovalAuthorityHistory
				ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
				history.setAprovalPId(ae.getAprovalPId());
				history.setStageNo(ae.getStageNo());
				history.setSurveyId(ae.getSurveyId());
				history.setPendingAt(ae.getPendingAt());
				history.setSentFrom(ae.getSentFrom());
				history.setStatus(ae.isStatus());
				history.setCreadtedBy(userId);
				history.setCreatedOn(new Date());

				approvalAuthorityHistoryEntityRepository.saveAndFlush(history);

			}
			return "redirect:/surveyDiscardedReport";
		} catch (Exception e) {
			LOG.error("SurveyController::saveSurveyDetailsDiscardSurveyor():" + e);
			JSONObject json = new JSONObject();
			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetailsDiscardSurveyor";
		}

	}

	// View survey details
	@RequestMapping(value = "/viewSurveyDetailsById", method = RequestMethod.POST)
	public String saveSurveyDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyId", required = true) Integer surveyId, HttpSession session)
			throws JSONException {

		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if (response.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < response.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute("Update", true);
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		model = assignModelData(model, userId);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		return "addSurveyDetails";
	}

	// eiar admin edit
	@RequestMapping(value = "/viewSurveyDetailsByIdEiar", method = RequestMethod.POST)
	public String viewSurveyDetailsByIdEiar(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyId", required = true) Integer surveyId, HttpSession session)
			throws JSONException {

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if (response.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < response.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute("Update", true);


		model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		return "addSurveyDetailsEiar";
	}

	@RequestMapping(value = "/viewSurveyDetailsByIdDiscardSurveyor", method = RequestMethod.POST)
	public String viewSurveyDetailsByIdDiscardSurveyor(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyId", required = true) Integer surveyId, HttpSession session)
			throws JSONException {

		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if (response.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < response.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute("Update", true);


		model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		return "addSurveyDetailsDiscardSurveyor";
	}

	// save survey details
	@RequestMapping(value = "/validateSurveyDetails", method = RequestMethod.POST)
	public /* @ResponseBody */ String saveSurveyDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON,
			@RequestParam("files") MultipartFile[] files, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		try {

			String decodeJSON = new String(Base64.getDecoder().decode(encodeJSON.getBytes()));
			decodeJSON = decodeJSON.replaceAll(WrsisPortalConstant.__SELECT__, WrsisPortalConstant.NA);
			decodeJSON = decodeJSON.replaceAll("--select--", WrsisPortalConstant.NA);

			String validationstr = "{\r\n" + "   \r\n"
					+ "   \"remark\":{\"Message\":\"Invalid Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"kebelworedaaddr\":{\"Message\":\"Invalid Address.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"othersurveyor\":{\"Message\":\"Invalid Other Surveyor's.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+

					"	\"sampleCountId\":{\"Message\":\"Invalid No. of Samples.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n"
					+
					// " \"sampleIds\":{\"Message\":\"Invalid sample ids.\",\"Regex\":\"^[
					// A-Za-z0-9\\\\,]*$\"},\r\n" +
					// " \"latitudeId\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"latitude1Id\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"othIncidentVal\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					// " \"othSeverityVal\":{\"Message\":\"Invalid
					// Severity.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					"	\"siteArea\":{\"Message\":\"Invalid Site Area.\",\"Regex\":\"^(([1-9]*)|(([0-9]*)\\.([0-9]*)))$\"},\r\n"
					+
					// " \"altitudeId\":{\"Message\":\"Invalid
					// Altitude.\",\"Regex\":\"^[0-9]+([\\\\,\\\\.][0-9]+)?$\"},\r\n" +
					// " \"longitude1Id\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"dose\":{\"Message\":\"Invalid Dose.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"longitudeId\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"incidentId\":{\"Message\":\"Invalid Incident.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"severityId\":{\"Message\":\"Invalid Severirty.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"sampleRemarks\":{\"Message\":\"Invalid Sample Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\\\. ]+$\"}"
					+ "}";

			JSONObject validationJson = new JSONObject(validationstr);
			JSONObject validatedJson = validateIncomingJson(new JSONObject(decodeJSON), validationJson);
			boolean check = false;
			for (MultipartFile f : files) {
				if (f.getOriginalFilename().trim().length() > 0
						&& !Validation.validateFileUpload(f.getOriginalFilename())) {
					check = true;
					break;
				}
			}

			// check the sample ids duplication
			if (!(validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check )) {

				
				validatedJson = checkSampleIdsDuplication(validatedJson);

			}

			if (validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check) {

				if (check) {
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR, WrsisPortalConstant.INVALID_FILE_TYPE);

				}
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
				JSONObject oldJson = validatedJson;
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
					model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
				}
				model.addAttribute("Update", (surveyId != 0) ? true : false);
				Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model.addAttribute("Update", (surveyId != null && surveyId != 0) ? true : false);
				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model = assignModelData(model, userId);
				return "addSurveyDetails";
			}

			JSONObject oldJson = new JSONObject();
			try {
				oldJson = new JSONObject(new String(Base64.getDecoder()
						.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));
			} catch (Exception e) {
				LOG.error("SurveyController::saveSurveyDetails():" + e);
			}
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}

			JSONObject surveyJson = new JSONObject(decodeJSON);
			JSONArray jsa = new JSONArray();
			try {
				jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			} catch (Exception e) {// handle for images key
				LOG.error("SurveyController::saveSurveyDetails():" + e);

			}
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				String surveyDate = validatedJson.getString(WrsisPortalConstant.SURVEY_DATE_ID);
				Date sDate = convertStringToDate(surveyDate);
				int counter = (jsa.length() == 0) ? 1 : jsa.length();
				for (MultipartFile file : files) {
					if (file.isEmpty()) {
						continue;
					}
					
					SecureRandom random = new SecureRandom();
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
					SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
					SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
					Date datee = new Date();
					String date = dateFormat.format(datee);
					String month = monthFormat.format(datee);
//					String year = String.valueOf(datee.getYear());
					String year = yearFormat.format(datee);
					int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

					String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
					String fileName = "SURVEY_IMAGE_" + randomNo + date + month + year + "_" + counter + "."
							+ extensionName;
					counter++;

					Calendar cal = Calendar.getInstance();
					cal.setTime(sDate); // don't forget this if date is arbitrary
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
					String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate)
							+ File.separator + fileName; // should be dynamic
					String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate);
					File folderCreate = new File(folder);
					if (!folderCreate.exists()) {
						folderCreate.mkdir();
						Path path = Paths.get(folder);
						Files.createDirectories(path);
					}
					File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
					if (!file1.exists()) {
						file1.mkdir();
					}
					if (!file1.exists()) {
						Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath));

					jsa.put(cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate) + File.separator
							+ fileName);
				}
			}

			surveyJson.put(WrsisPortalConstant.IMAGES, jsa);
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				surveyJson.put(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}
			session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));
			model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));

			List<String> images = new ArrayList<>();
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				for (int i = 0; i < surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
					images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
							.encode((surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
				}
			}
			model.addAttribute(WrsisPortalConstant.IMAGES, images);

			model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

			return "confirmSurveyData";
		} catch (Exception e) {
			LOG.error("SurveyController::saveSurveyDetails():" + e);
			JSONObject json = new JSONObject();

			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetails";
		}

	}

	@RequestMapping(value = "/validateSurveyDetailsEiar", method = RequestMethod.POST)
	public /* @ResponseBody */ String validateSurveyDetailsEiar(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON,
			@RequestParam("files") MultipartFile[] files, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		try {

			String decodeJSON = new String(Base64.getDecoder().decode(encodeJSON.getBytes()));
			decodeJSON = decodeJSON.replaceAll(WrsisPortalConstant.__SELECT__, WrsisPortalConstant.NA);
			decodeJSON = decodeJSON.replaceAll("--select--", WrsisPortalConstant.NA);

			String validationstr = "{\r\n" + "   \r\n"
					+ "   \"remark\":{\"Message\":\"Invalid Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"kebelworedaaddr\":{\"Message\":\"Invalid Address.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"othersurveyor\":{\"Message\":\"Invalid Other Surveyor's.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+

					"	\"sampleCountId\":{\"Message\":\"Invalid No. of Samples.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n"
					+
					// " \"sampleIds\":{\"Message\":\"Invalid sample ids.\",\"Regex\":\"^[
					// A-Za-z0-9\\\\,]*$\"},\r\n" +
					// " \"latitudeId\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"latitude1Id\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"othIncidentVal\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					// " \"othSeverityVal\":{\"Message\":\"Invalid
					// Severity.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					"	\"siteArea\":{\"Message\":\"Invalid Site Area.\",\"Regex\":\"^(([1-9]*)|(([1-9]*)\\.([0-9]*)))$\"},\r\n"
					+
					// " \"altitudeId\":{\"Message\":\"Invalid
					// Altitude.\",\"Regex\":\"^[0-9]+([\\\\,\\\\.][0-9]+)?$\"},\r\n" +
					// " \"longitude1Id\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"dose\":{\"Message\":\"Invalid Dose.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"longitudeId\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"incidentId\":{\"Message\":\"Invalid Incident.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"severityId\":{\"Message\":\"Invalid Severirty.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"sampleRemarks\":{\"Message\":\"Invalid Sample Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\\\. ]+$\"}"
					+ "}";

			JSONObject validationJson = new JSONObject(validationstr);
			JSONObject validatedJson = validateIncomingJson(new JSONObject(decodeJSON), validationJson);
			boolean check = false;
			for (MultipartFile f : files) {
				if (f.getOriginalFilename().trim().length() > 0
						&& !Validation.validateFileUpload(f.getOriginalFilename())) {
					check = true;
					break;
				}
			}

			// check the sample ids duplication
			if (!(validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check)) {

				
				validatedJson = checkSampleIdsDuplication(validatedJson);

			}

			if (validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check) {

				if (check) {
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR, WrsisPortalConstant.INVALID_FILE_TYPE);

				}
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
				JSONObject oldJson = validatedJson;
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
					model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
				}
				model.addAttribute("Update", (surveyId != 0) ? true : false);

				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model.addAttribute("Update", (surveyId != null && surveyId != 0) ? true : false);
				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
				return "addSurveyDetailsEiar";
			}

			JSONObject oldJson = new JSONObject();
			try {
				oldJson = new JSONObject(new String(Base64.getDecoder()
						.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));
			} catch (Exception e) {
				LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);
				// handle for null pointer to get the session value
			}
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}

			JSONObject surveyJson = new JSONObject(decodeJSON);
			JSONArray jsa = new JSONArray();
			try {
				jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			} catch (Exception e) {// handle for images key
				LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);

			}
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				String surveyDate = validatedJson.getString(WrsisPortalConstant.SURVEY_DATE_ID);
				Date sDate = convertStringToDate(surveyDate);
				int counter = (jsa.length() == 0) ? 1 : jsa.length();
				for (MultipartFile file : files) {
					if (file.isEmpty()) {
						continue;
					}
					
					SecureRandom random = new SecureRandom();
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
					SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
					SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
					Date datee = new Date();
					String date = dateFormat.format(datee);
					String month = monthFormat.format(datee);
//					String year = String.valueOf(datee.getYear());
					String year = yearFormat.format(datee);
					int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

					String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
					String fileName = "SURVEY_IMAGE_" + randomNo + date + month + year + "_" + counter + "."
							+ extensionName;
					counter++;

					Calendar cal = Calendar.getInstance();
					cal.setTime(sDate); // don't forget this if date is arbitrary
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
					String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate)
							+ File.separator + fileName; // should be dynamic
					String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate);
					File folderCreate = new File(folder);
					if (!folderCreate.exists()) {
						folderCreate.mkdir();
						Path path = Paths.get(folder);
						Files.createDirectories(path);
					}
					File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
					if (!file1.exists()) {
						file1.mkdir();
					}
					if (!file1.exists()) {
						Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath));

					jsa.put(cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate) + File.separator
							+ fileName);
				}
			}

			surveyJson.put(WrsisPortalConstant.IMAGES, jsa);
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				surveyJson.put(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}
			session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));
			model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));

			List<String> images = new ArrayList<>();
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				for (int i = 0; i < surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
					images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
							.encode((surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
				}
			}
			model.addAttribute(WrsisPortalConstant.IMAGES, images);

			model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

			return "confirmSurveyDataEiar";
		} catch (Exception e) {
			LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);
			JSONObject json = new JSONObject();

			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetailsEiar";
		}

	}

	@RequestMapping(value = "/validateSurveyDetailsDiscardSurveyor", method = RequestMethod.POST)
	public /* @ResponseBody */ String validateSurveyDetailsDiscardSurveyor(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyJSON", required = true) String encodeJSON,
			@RequestParam("files") MultipartFile[] files, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		try {
			
			
			String decodeJSON = new String(Base64.getDecoder().decode(encodeJSON.getBytes()));
			decodeJSON = decodeJSON.replaceAll(WrsisPortalConstant.__SELECT__, WrsisPortalConstant.NA);
			decodeJSON = decodeJSON.replaceAll("--select--", WrsisPortalConstant.NA);

			String validationstr = "{\r\n" + "   \r\n"
					+ "   \"remark\":{\"Message\":\"Invalid Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"kebelworedaaddr\":{\"Message\":\"Invalid Address.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+ "   \"othersurveyor\":{\"Message\":\"Invalid Other Surveyor's.\",\"Regex\":\"^[0-9a-zA-Z,\\. ]+$\"},"
					+

					"	\"sampleCountId\":{\"Message\":\"Invalid No. of Samples.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n"
					+
					// " \"sampleIds\":{\"Message\":\"Invalid sample ids.\",\"Regex\":\"^[
					// A-Za-z0-9\\\\,]*$\"},\r\n" +
					// " \"latitudeId\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"latitude1Id\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"othIncidentVal\":{\"Message\":\"Invalid
					// Latitude.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					// " \"othSeverityVal\":{\"Message\":\"Invalid
					// Severity.\",\"Regex\":\"^([1-9]|[1-9][0-9])$\"},\r\n" +
					"	\"siteArea\":{\"Message\":\"Invalid Site Area.\",\"Regex\":\"^(([1-9]*)|(([1-9]*)\\.([0-9]*)))$\"},\r\n"
					+
					// " \"altitudeId\":{\"Message\":\"Invalid
					// Altitude.\",\"Regex\":\"^[0-9]+([\\\\,\\\\.][0-9]+)?$\"},\r\n" +
					// " \"longitude1Id\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"dose\":{\"Message\":\"Invalid Dose.\",\"Regex\":\"[0-9]+\"},\r\n" +
					// " \"longitudeId\":{\"Message\":\"Invalid
					// Longitude.\",\"Regex\":\"[0-9]+\"},\r\n" +
					"	\"incidentId\":{\"Message\":\"Invalid Incident.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"severityId\":{\"Message\":\"Invalid Severirty.\",\"Regex\":\"[0-9]+\"},\r\n"
					+ "	\"sampleRemarks\":{\"Message\":\"Invalid Sample Remarks.\",\"Regex\":\"^[0-9a-zA-Z,\\\\. ]+$\"}"
					+ "}";

			JSONObject validationJson = new JSONObject(validationstr);
			JSONObject validatedJson = validateIncomingJson(new JSONObject(decodeJSON), validationJson);
			boolean check = false;
			for (MultipartFile f : files) {
				if (f.getOriginalFilename().trim().length() > 0
						&& !Validation.validateFileUpload(f.getOriginalFilename())) {
					check = true;
					break;
				}
			}

			// check the sample ids duplication
			if (!(validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check)) {

				
				validatedJson = checkSampleIdsDuplication(validatedJson);

			}

			if (validatedJson.has(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND) || check) {

				if (check) {
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
					validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR, WrsisPortalConstant.INVALID_FILE_TYPE);

				}
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
				JSONObject oldJson = validatedJson;
				model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
						new String(Base64.getEncoder().encode(oldJson.toString().getBytes())));
				if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
					model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
				}
				model.addAttribute("Update", (surveyId != 0) ? true : false);
				
				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model.addAttribute("Update", (surveyId != null && surveyId != 0) ? true : false);
				model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
				model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
				return "addSurveyDetailsDiscardSurveyor";
			}

			JSONObject oldJson = new JSONObject();
			try {
				oldJson = new JSONObject(new String(Base64.getDecoder()
						.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));
			} catch (Exception e) {
				LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);
				// handle for null pointer to get the session value
			}
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}

			JSONObject surveyJson = new JSONObject(decodeJSON);
			JSONArray jsa = new JSONArray();
			try {
				jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			} catch (Exception e) {// handle for images key
				LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);

			}
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				String surveyDate = validatedJson.getString(WrsisPortalConstant.SURVEY_DATE_ID);
				Date sDate = convertStringToDate(surveyDate);
				int counter = (jsa.length() == 0) ? 1 : jsa.length();
				for (MultipartFile file : files) {
					if (file.isEmpty()) {
						continue;
					}
					SecureRandom random=new SecureRandom();
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
					SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
					Date datee = new Date();
					String date = dateFormat.format(datee);
					String month = monthFormat.format(datee);
					String year = String.valueOf(datee.getYear());
					int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

					String extensionName = FilenameUtils.getExtension(file.getOriginalFilename());
					String fileName = "SURVEY_IMAGE_" + randomNo + date + month + year + "_" + counter + "."
							+ extensionName;
					counter++;

					Calendar cal = Calendar.getInstance();
					cal.setTime(sDate); // don't forget this if date is arbitrary
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
					String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate)
							+ File.separator + fileName; // should be dynamic
					String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate);
					File folderCreate = new File(folder);
					if (!folderCreate.exists()) {
						folderCreate.mkdir();
						Path path = Paths.get(folder);
						Files.createDirectories(path);
					}
					File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
					if (!file1.exists()) {
						file1.mkdir();
					}
					if (!file1.exists()) {
						Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(file.getBytes(), new File(fileUploadPath));

					jsa.put(cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate) + File.separator
							+ fileName);
				}
			}

			surveyJson.put(WrsisPortalConstant.IMAGES, jsa);
			if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
				surveyJson.put(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
			}
			session.setAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));
			model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
					new String(Base64.getEncoder().encode(surveyJson.toString().getBytes())));

			List<String> images = new ArrayList<>();
			if (surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
				for (int i = 0; i < surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
					images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
							.encode((surveyJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
				}
			}
			model.addAttribute(WrsisPortalConstant.IMAGES, images);

			model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);

			return "confirmSurveyDataDiscardSurveyor";
		} catch (Exception e) {
			LOG.error("SurveyController::validateSurveyDetailsEiar():" + e);
			JSONObject json = new JSONObject();

			json.put("Status", WrsisPortalConstant.UNABLE_TO_SAVE_THE_DETAILS);
			json.put(WrsisPortalConstant.STATUS_CODE, 400);
			return "addSurveyDetailsEiar";
		}

	}

	private JSONObject checkSampleIdsDuplication(JSONObject validatedJson) throws JSONException {

		if (validatedJson.has("sampleCollectedId") && validatedJson.getBoolean("sampleCollectedId")) {
			JSONArray sampleCollected = validatedJson.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);
			Set<String> sampleIdsSet = new HashSet<>();
			main: for (int i = 0; i < sampleCollected.length(); i++) {
				JSONObject sampleJson = new JSONObject();
				sampleJson = sampleCollected.getJSONObject(i);
				String sampleIds = sampleJson.getString("sampleIds");
				String spl[] = sampleIds.trim().split(",");

				for (int j = 0; j < spl.length; j++) {
					String sampleId = spl[j];
					if (!sampleIdsSet.add(sampleId)) {
						validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR, "Sample ids cant be duplicate.");
						validatedJson.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);

						JSONArray jsa = new JSONArray();
						for (int l = 0; l < sampleCollected.length(); l++) {
							if (l != i) {
								jsa.put(sampleCollected.getJSONObject(l));
							}
						}

						validatedJson.remove(WrsisPortalConstant.SAMPLE_DETAILS);
						validatedJson.put(WrsisPortalConstant.SAMPLE_DETAILS, jsa);
						break main;
					}
				}

			}
		}

		return validatedJson;
	}

	private JSONObject validateIncomingJson(JSONObject decodeJSON, JSONObject validationJson) throws JSONException {

		Iterator keys = decodeJSON.keys();
		main: while (keys.hasNext()) {
			String currentDynamicKey = (String) keys.next();
			Object obj = decodeJSON.get(currentDynamicKey);

			// if string
			if (obj instanceof String) {
				if (validationJson.has(currentDynamicKey)) {
					Pattern pattern = Pattern
							.compile(validationJson.getJSONObject(currentDynamicKey).getString(WrsisPortalConstant.REGEX));
					Matcher matcher = pattern.matcher(decodeJSON.getString(currentDynamicKey));
					if (!matcher.matches() && (decodeJSON.getString(currentDynamicKey)).trim().length() != 0) {
						decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR,
								validationJson.getJSONObject(currentDynamicKey).getString(WrsisPortalConstant.MESSAGE_JSON));
						decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
						decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FIELD, currentDynamicKey);
						break main;
					}
				}
			}
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = decodeJSON.getJSONObject(currentDynamicKey);
				Iterator keys1 = jsonObj.keys();
				while (keys1.hasNext()) {
					String currentDynamicKey1 = (String) keys1.next();
					if (validationJson.has(currentDynamicKey1)) {
						Pattern pattern = Pattern
								.compile(validationJson.getJSONObject(currentDynamicKey1).getString(WrsisPortalConstant.REGEX));
						Matcher matcher = pattern.matcher(jsonObj.getString(currentDynamicKey1));
						if (!matcher.matches() && jsonObj.getString(currentDynamicKey1).trim().length() != 0) {
							decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR,
									validationJson.getJSONObject(currentDynamicKey1).getString(WrsisPortalConstant.MESSAGE_JSON));
							decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
							decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FIELD, currentDynamicKey1);
							break main;
						}
					}

				}

			}
			if (obj instanceof JSONArray) {

				JSONArray jsa = decodeJSON.getJSONArray(currentDynamicKey);
				for (int i = 0; i < jsa.length(); i++) {

					JSONObject jsonObj = jsa.getJSONObject(i);
					Iterator keys1 = jsonObj.keys();
					while (keys1.hasNext()) {
						String currentDynamicKey1 = (String) keys1.next();
						if (validationJson.has(currentDynamicKey1)) {
							Pattern pattern = Pattern
									.compile(validationJson.getJSONObject(currentDynamicKey1).getString(WrsisPortalConstant.REGEX));
							Matcher matcher = pattern.matcher(jsonObj.getString(currentDynamicKey1));
							if (!matcher.matches()
									&& jsonObj.getString(currentDynamicKey1).trim().length() != 0) {
								decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR,
										validationJson.getJSONObject(currentDynamicKey1).getString(WrsisPortalConstant.MESSAGE_JSON));
								decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FOUND, true);
								decodeJSON.put(WrsisPortalConstant.SERVER_SIDE_ERROR_FIELD, currentDynamicKey1);
								break main;
							}
						}

					}

				}

			}

		}

		return decodeJSON;
	}

	// SurveyJSON

	@RequestMapping(value = "/modifySurvey")
	public String modifySurvey(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
		JSONObject oldJson = new JSONObject(new String(Base64.getDecoder()
				.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

		if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
			model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
		}
		model.addAttribute("Update", (surveyId != 0) ? true : false);
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model = assignModelData(model, userId);
		List<String> images = new ArrayList<>();
		if (oldJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < oldJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((oldJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		return "addSurveyDetails";
	}

	@RequestMapping(value = "/modifySurveyEiar")
	public String modifySurveyEiar(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
		JSONObject oldJson = new JSONObject(new String(Base64.getDecoder()
				.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

		if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
			model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
		}
		model.addAttribute("Update", (surveyId != 0) ? true : false);

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
		List<String> images = new ArrayList<>();
		if (oldJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < oldJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((oldJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		return "addSurveyDetailsEiar";
	}

	@RequestMapping(value = "/modifySurveyDiscardSurveyor")
	public String modifySurveyDiscardSurveyor(Model model, HttpServletRequest request, HttpSession session,
			@RequestParam("surveyId") Integer surveyId) throws JSONException {

		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				(String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON));
		JSONObject oldJson = new JSONObject(new String(Base64.getDecoder()
				.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

		if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {
			model.addAttribute(WrsisPortalConstant.SURVEY_NO, oldJson.getString(WrsisPortalConstant.SURVEY_NO));
		}
		model.addAttribute("Update", (surveyId != 0) ? true : false);

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		List<String> images = new ArrayList<>();
		if (oldJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < oldJson.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((oldJson.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model = assignModelData(model, surveyDetailsRepository.findById(surveyId).get().getCreatedBy());
		return "addSurveyDetailsDiscardSurveyor";
	}

	@RequestMapping(value = "public/load_image", method = RequestMethod.GET)
	void loadImageInformationLicenseeForUpgradationPage(HttpServletResponse response,
			@RequestParam(value = "imagePath", required = false) String path) throws IOException {
		path = new String(Base64.getDecoder().decode(path));
		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(new File(
					WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH + File.separator + path)));
			OutputStream outputStream = response.getOutputStream();
			byte[] bytes = new byte[1024 * 2];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, bytesRead);
			}
			outputStream.flush();

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@RequestMapping(value = { "/confirmSurveyData" })
	public String confirmSurveyData() {
		return "confirmSurveyData";
	}

	@RequestMapping(value = { "/cancelSurveyDetails" })
	public String cancelSurveyDetails(HttpSession session) {
		JSONObject oldJson = new JSONObject();
		try {
			oldJson = new JSONObject(new String(Base64.getDecoder()
					.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

			JSONArray jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			for (int i = 0; i < jsa.length(); i++) {
				File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
						+ File.separator + (String) jsa.getString(i));
				if (file.exists()) 
					if (!file.delete()) {
						LOG.info("LoginServiceImpl::cancelSurveyDetails(): + FILE CAN'T DELETED");
	
						  }
			}
		} catch (Exception e) {
			// handle for null pointer to get the session value
			LOG.error("SurveyController::cancelSurveyDetails():" + e);
		}

		if (oldJson.has(WrsisPortalConstant.SURVEY_NO)) {

			return "redirect:/viewsurveydetails";
		} else {
			return "redirect:/addSurveyDetails";
		}
	}

	@RequestMapping(value = { "/cancelSurveyDetailsEiar" })
	public String cancelSurveyDetailsEiar(HttpSession session) {
		JSONObject oldJson = new JSONObject();
		try {
			oldJson = new JSONObject(new String(Base64.getDecoder()
					.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

			JSONArray jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			for (int i = 0; i < jsa.length(); i++) {
				File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
						+ File.separator + (String) jsa.getString(i));
				if (file.exists()) 
					if (!file.delete()) {
						LOG.info("LoginServiceImpl::cancelSurveyDetails(): + FILE CAN'T DELETED");
	 
						  }
			}
		} catch (Exception e) {
			// handle for null pointer to get the session value
			LOG.error("SurveyController::cancelSurveyDetailsEiar():" + e);
		}

		return "redirect:/pendingsurvey";

	}

	@RequestMapping(value = { "/cancelSurveyDetailsDiscardSurveyor" })
	public String cancelSurveyDetailsDiscardSurveyor(HttpSession session) {
		JSONObject oldJson = new JSONObject();
		try {
			oldJson = new JSONObject(new String(Base64.getDecoder()
					.decode(((String) session.getAttribute(WrsisPortalConstant.SURVEY_JSON)).getBytes())));

			JSONArray jsa = oldJson.getJSONArray(WrsisPortalConstant.IMAGES);
			for (int i = 0; i < jsa.length(); i++) {
				File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
						+ File.separator + (String) jsa.getString(i));
				if (file.exists()) 
					if (!file.delete()) {
						LOG.info("LoginServiceImpl::cancelSurveyDetailsDiscardSurveyor(): + FILE CAN'T DELETED");
	
						  }
			}
		} catch (Exception e) {
			// handle for null pointer to get the session value
			LOG.error("SurveyController::cancelSurveyDetailsDiscardSurveyor():" + e);
		}

		return "redirect:/surveyDiscardedReport";

	}

	@RequestMapping(value = { "/modifySurveyData" })
	public String modifySurveyData() {
		return "modifySurveyData";
	}

	public static Date convertStringToDate(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("SurveyController::modifySurveyData():" + ex);

		}
		return date;
	}

	@Autowired
	SurveySiteRepository surveySiteRepository;

	@RequestMapping(value = { "/viewsurveydetails" })
	public String viewsurveydetails(Model model, HttpSession session) {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		

		
		model = assignModelData(model, userId);

		
		return "viewsurveydetails";
	}

	@RequestMapping(value = { "/searchSurveyDetails" })
	public String searchSurveyDetails(Model model, HttpSession session, @RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam("kebeleId") String kebeleId, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("rustTypeId") String rustTypeId) throws ParseException {
		String startDateB = startDate;
		String endDateB = endDate;
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		if (!(startDate.trim().equals("") || endDate.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
			SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
			endDate = smpl.format(date1);
		}
		List<Object[]> details = surveyDetailsRepository.searchSurveyDetails(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
				Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, -1, -1);
		List<SurveyDetailsBean> beans = new ArrayList<>();
		for (Object[] ar : details) {
			SurveyDetailsBean sdb = new SurveyDetailsBean();
			sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
			sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
			sdb.setRust((ar[6] != null) ? (String) ar[6] : "");
			sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
			sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
			sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
			sdb.setStatus((ar[7] != null) ? (String) ar[7] : "");
			sdb.setWheatType((ar[3] != null) ? (String) ar[3] : "");
			sdb.setMode((ar[8] != null) ? (String) ar[8] : "");
			beans.add(sdb);
		}

		model.addAttribute(WrsisPortalConstant.DETAILS, beans);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute("startDate", startDateB);
		model.addAttribute("endDate", endDateB);
		model.addAttribute("kebeleId", kebeleId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute("rustTypeId", rustTypeId);

		model = assignModelData(model, userId);
		
		return "viewsurveydetails";
	}

	@RequestMapping(value = { "/uploadSurveyDataXcel" })
	public String uploadSurveyDataXcel() {
		return "uploadSurveyDataXcel";
	}

	@RequestMapping(value = { "/wrSurveySummaryReports" })
	public String wrSurveySummaryReports(Model model) {
		try {
			ArrayList<String> s = new ArrayList<>();
			List<Question> qsList = questionService.viewAllWebQuestion();
			for (Question qust : qsList) {
				s.add(qust.getQustion());
			}
			model.addAttribute("qlength", s);
			List<IvrDataReportBean> ivrList = iVRDataService.viewIvrSurveyReportData("", 0, 0, 0, "", "", -1, -1);

			model.addAttribute("ivrDataList", ivrList);
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}
			model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			model.addAttribute(WrsisPortalConstant.ZONE_ID, 0);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, 0);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} catch (Exception e) {
			LOG.error("SurveyController::wrSurveySummaryReports():" + e);
		}
		return "wrSurveySummaryReports";
	}

	@RequestMapping(value = { "/viewAllIVRDataPageWise" })
	public @ResponseBody IvrDataTable publishedSurveyDataPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId, @RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("mobile") String mobile) {

		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<IvrDataReportBean> ivrList = null;

		Integer totalCount = iVRDataService.viewIvrSurveyReportDataCount(mobile.trim(), regionId, zoneId, woredaId, -1,
				-1);
		IvrDataTable sdt = new IvrDataTable();
		sdt.setData(ivrList);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));

		return sdt;
	}

	@Value("${map.forecasting.url}")
	private String GIS_URL;

	@Value("${gis.map.workspace}")
	private String GIS_WORKSPACE;

	@RequestMapping(value = { "/mapdetails" })
	public String mapdetails(Model model) {
		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);
		return "mapdetails";
	}

	

	@RequestMapping(value = { "/uploadSurveyDataXcelView" })
	public String uploadSurveyDataXcelView(Model model, HttpSession session) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		
		
		
		
		
		return "uploadSurveyDataXcelView";
	}

	@GetMapping(value = { "/uploadSurveyDataXcelViewPageWise" })
	@ResponseBody
	public SurveyExcelFilesDataTable uploadSurveyDataXcelViewPageWise(Model model, HttpServletRequest request,
			HttpSession session, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		String start = request.getParameter(WrsisPortalConstant.START);
		String lenght = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		List<SurveyExcelFilesBean> files = surveyExcelDetailsService.viewAllSurveyFileDetails(userId, startDate,
				endDate, Integer.parseInt(start), Integer.parseInt(lenght));
		Integer recordsTotal = surveyExcelDetailsService.viewAllSurveyFileDetailsCount(userId, startDate, endDate, -1,
				-1);

		SurveyExcelFilesDataTable adt = new SurveyExcelFilesDataTable();
		adt.setData(files);
		adt.setRecordsTotal(recordsTotal);
		adt.setRecordsFiltered(recordsTotal);
		adt.setDraw(Integer.parseInt(draw));
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return adt;
	}

	@RequestMapping(value = { "/fetchAllActiveExcelFilesSearch" })
	public String fetchAllActiveExcelFilesSearch(Model model, @RequestParam("startDate") String startDateIN,
			@RequestParam("endDate") String endDateIN, HttpSession session) throws ParseException {

		
		model.addAttribute("StartDate", startDateIN);
		model.addAttribute("EndDate", endDateIN);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "uploadSurveyDataXcelView";
	}

	

	@RequestMapping(value = { "/uploadSurveyDataExcelFileDetailsView" })
	public String uploadSurveyDataExcelFileDetailsView() {
		return "uploadSurveyDataExcelFileDetailsView";
	}

	@RequestMapping(value = { "/viewsurvey" })
	public String viewsurvey(@RequestParam("surveyId") Integer surveyId, Model model) throws JSONException {
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if (response.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID)) {
			for (int i = 0; i < response.getJSONArray(WrsisPortalConstant.IMAGES).length(); i++) {
				images.add("public/load_image?imagePath=" + new String(Base64.getEncoder()
						.encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES, images);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON,
				new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		

		return "viewsurvey";
	}

	@RequestMapping(value = { "/ivrSummaryReport" })
	public String ivrSummaryReport() {
		return "ivrSummaryReport";
	}

	@RequestMapping(value = { "/IVRMapDetails" })
	public String IVRmapdetails() {
		return "IVRMapDetails";
	}

	@RequestMapping(value = { "/IncidentMapDetails" })
	public String IncidentMapDetails(Model model) {
		model.addAttribute(WrsisPortalConstant.GIS_URL, GIS_URL);
		model.addAttribute(WrsisPortalConstant.WORKSPACE, GIS_WORKSPACE);
		return "IncidentMapDetails";
	}
	// ivrSummaryReport

	// Surveyor Excel sample download

	@RequestMapping(value = "downloadSurveyorSampleExcel", method = RequestMethod.GET)
	public void downloadSurveyorSampleExcel(HttpServletResponse response, HttpSession session, Model model)
			throws IOException {
		HSSFWorkbook myExcelBook = null;
		OutputStream out = null;
		try {
			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			model = assignModelData(model, userId);
			List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf((obj.get(0))[0])))
					: null;
//			obj.clear();
			if(obj!=null && obj.isEmpty()) { //obj holding research center names
				obj = researchCenterRepository.findActiveResearchCenters();
			}
			myExcelBook = surveyExcel.buildSuveyDetailsSampleExcel(rustTypeRepository, sampleTypeRepository,
					diseaseMasterRepository, seasionMasterRepository, demographyRepository, siteTypeRepository,
					wheatTypeRepository, varietyTypeRepository, growthRepository, reactionTypeRepository,
					fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository, moistureRepository
					,researchCenterId,obj);//

			
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Survey_Details.xls");

			out = response.getOutputStream();
		} catch (Exception e) {
			LOG.error("SurveyController::downloadSurveyorSampleExcel():" + e);
		} finally {
			if(myExcelBook!=null && out!=null) {
				myExcelBook.write(out);
				out.flush();
				out.close();
				myExcelBook.close();
			}
		}
	}

	// suvey excel upload

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/surveyExcelUpload")
	// If not @RestController, uncomment this
	// @ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile, HttpSession session) {

		Connection connection = null;
		Workbook workbook = null;
		Workbook workbook1 = null;
		if (uploadfile.isEmpty()) {
			return new ResponseEntity("please select a file!", HttpStatus.OK);
		}
		JSONArray finalArray = new JSONArray();

		try {

			String fileName = uploadfile.getOriginalFilename();
			String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));

			if (uploadfile.getSize() > 0) {
				long siz = uploadfile.getSize();
				double d = siz / (Math.pow(1024, 2));
				if (Math.round(d) >= 5) {
					JSONObject json = new JSONObject();
					json.put("IsFileSizeExceed", true);
					finalArray.put(json);
				}
			}
			if (fileExtensionName.equals(".xlsx")) {
				workbook = new XSSFWorkbook(uploadfile.getInputStream());
				workbook1 = new XSSFWorkbook(uploadfile.getInputStream());
			} else if (fileExtensionName.equals(".xls")) {
				workbook = new HSSFWorkbook(uploadfile.getInputStream());
				workbook1 = new HSSFWorkbook(uploadfile.getInputStream());
			} else {
				JSONObject json = new JSONObject();
				json.put("IsFileExtValid", false);
				finalArray.put(json);
			}
			SecureRandom random=new SecureRandom();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
			SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
			Date datee = new Date();
			String date = dateFormat.format(datee);
			String month = monthFormat.format(datee);
//			String year = String.valueOf(datee.getYear());
			String year = yearFormat.format(datee);
			int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // don't forget this if date is arbitrary
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");

			String extensionName = FilenameUtils.getExtension(uploadfile.getOriginalFilename());
			String fileName1 = "SURVEY_EXCEL_ERROR" + randomNo + date + month + year + "." + extensionName;
			String fileNameS = "SURVEY_EXCEL_SUCCESS" + randomNo + date + month + year + "." + extensionName;
			String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH
					+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(new Date())
					+ File.separator + fileName1; // should be dynamic
			String fileUploadPathSuccess = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH
					+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(new Date())
					+ File.separator + fileNameS; // should be dynamic

			File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH + File.separator
					+ cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(new Date()));
			if (!file1.exists()) {
				file1.mkdirs();
			}
			if (file1.exists()) {
				Path path = Paths
						.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH + File.separator
								+ cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(new Date()));
				Files.createDirectories(path);
			}

			

			String fName = new File(fileUploadPath).getName();

			Sheet sheet1 = workbook1.getSheet("Actual");
			Sheet sheet = workbook.getSheet("Actual");
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			connection = jdbcTemplate.getDataSource().getConnection();
			SurveyExcelParser parser = new SurveyExcelParser(commonService, userId, demographyRepository,
					seasionMasterRepository, siteTypeRepository, wheatTypeRepository, varietyTypeRepository,
					growthRepository, reactionTypeRepository, rustTypeRepository, sampleTypeRepository,
					fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository, moistureRepository,
					diseaseMasterRepository,researchCenterRepository, connection);
			JSONObject locations = parser.getAllFieldLocations(sheet.getRow(0));

			if (rowCount < 3) {
				JSONObject json = new JSONObject();
				json.put(WrsisPortalConstant.IS_EMPTY, true);
				finalArray.put(json);
			}

			for (int i = 3; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				JSONObject json = new JSONObject();

				if (i == 203)// if record exceed 200 records ignore
				{
					break;
				}
				json.put(WrsisPortalConstant.IS_EMPTY, false);
				json = parser.getRowWiseData(sheet, row, locations);
				if (json.has("IsRowEnd") && json.getBoolean("IsRowEnd")) {
					break;
				}
				if (json.has(WrsisPortalConstant.ERR) && json.getBoolean(WrsisPortalConstant.ERR)) {
					json.put("FileName",
							new String(Base64.getEncoder()
									.encode((cal.get(Calendar.YEAR) + File.separator
											+ simpleDateFormat.format(new Date()) + File.separator + fName.trim())
													.getBytes())));
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, json.getString(WrsisPortalConstant.MESSAGE_JSON));
					json.put("RowNo", json.getInt("RowNo"));
					finalArray.put(json);
					continue;
				}
				

				finalArray.put(json);
			}

			if (finalArray.length() == 0) {
				JSONObject json = new JSONObject();
				json.put(WrsisPortalConstant.IS_EMPTY, true);
				finalArray.put(json);
				return new ResponseEntity("" + finalArray, new HttpHeaders(), HttpStatus.OK);
			}
			int success = 0;
			JSONArray suc = new JSONArray();
			JSONArray err = new JSONArray();

			for (int i = 3; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				JSONObject json = new JSONObject();
				try {
					json = finalArray.getJSONObject(i - 3);
					if (json == null || json.length() == 0) {
						continue;
					}
				} catch (Exception e) {
					LOG.error("SurveyController::uploadFile():" + e);
					continue;
				}
				if (!json.has(WrsisPortalConstant.ERR)) {
					success++;
					suc.put(row.getRowNum());

				} else {
					Row row1 = sheet1.getRow(i);
					err.put(row1.getRowNum());
				}

			}
			int sucCoun = 0;
			int errCoun = 0;
			for (int i = 0; i < suc.length(); i++) {
				Row row = sheet1.getRow(suc.getInt(i) - sucCoun);
				removeRow(sheet, row.getRowNum());
				sucCoun++;
			}
			for (int i = 0; i < err.length(); i++) {
				Row row = sheet1.getRow(err.getInt(i) - errCoun);
				removeRow(sheet1, row.getRowNum());
				errCoun++;
			}
			session.setAttribute("SuccessCount", success);
			session.setAttribute("ExcelFileName",
					new String(
							Base64.getEncoder()
									.encode((cal.get(Calendar.YEAR) + File.separator
											+ simpleDateFormat.format(new Date()) + File.separator + fileNameS.trim())
													.getBytes())));

			
			try (FileOutputStream outputStream = new FileOutputStream(fileUploadPath)) {
				workbook.write(outputStream);
			}
			try (FileOutputStream outputStream = new FileOutputStream(fileUploadPathSuccess)) {
				workbook1.write(outputStream);
			}

			

		} catch (Exception e) {
			LOG.error("SurveyController::uploadFile():" + e);
			return new ResponseEntity("" + finalArray, new HttpHeaders(), HttpStatus.OK);
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (Exception e) {
				LOG.error("SurveyController::uploadFile():" + e);
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					LOG.error("SurveyController::uploadFile():" + e);

				}
			}
			if (workbook1 != null) {
				try {
					workbook1.close();
				} catch (IOException e) {
					LOG.error("SurveyController::uploadFile():" + e);

				}
			}
		}

		return new ResponseEntity(" " + finalArray, new HttpHeaders(), HttpStatus.OK);

	}

	private static void removeRow(Sheet sheet, int rowIndex) {
		if (rowIndex >= 0) {
			sheet.removeRow(sheet.getRow(rowIndex));
			if (rowIndex < sheet.getLastRowNum()) {
				sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
			}
		}
	}

	// save the excel data
	@RequestMapping(value = "/saveSurveyDetailsSingle", method = RequestMethod.POST)
	public @ResponseBody String saveSurveyDetailsSingle(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyData", required = true) String surveyData, HttpSession session)
			throws Exception {

		String encodeJsa = new String(Base64.getDecoder().decode(surveyData.getBytes()));
		JSONArray jsa = new JSONArray(encodeJsa);
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		int success = (int) session.getAttribute("SuccessCount");
		String fileName = (String) session.getAttribute("ExcelFileName");
		SurveyExcelFiles files = new SurveyExcelFiles();
		files.setCount(success);
		files.setCreatedBy(userId);
		files.setCreatedOn(new Date());
		files.setFileName(fileName);
		files.setStatus(false);
		files.setUpdatedBy(userId);
		files.setUpdatedOn(new Date());
		files = surveyExcelFilesRepository.save(files);
		for (int i = 0; i < jsa.length(); i++) {
			String encodeJSON = jsa.getString(i);
			commonService.saveSurveyDetails(encodeJSON, null, userId, 3, files.getFileSurveyId(), userId);
		}
		return "Data Saved Successfully !";

	}

	@RequestMapping(value = "/mobile/saveSurveyDetailsSingleMobile", method = RequestMethod.POST)
	public @ResponseBody String saveSurveyDetailsSingleMobile(Model model, HttpServletRequest request,
			@RequestParam(value = "surveyData", required = true) String surveyData, HttpSession session)
			throws Exception {
		commonService.saveSurveyDetails(surveyData, null, 10, 2, null, 10);
		return "Data Saved Successfully !";

	}

	@RequestMapping(value = "/public/downloadExcel", method = RequestMethod.GET)
	void downloadExcel(HttpServletResponse response, @RequestParam(value = "path", required = false) String path)
			throws IOException {
		HSSFWorkbook wb = null;
		OutputStream out = null;
		try {
			path = new String(Base64.getDecoder().decode(path));
			

			
			FileInputStream fis = new FileInputStream(new File(
					WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH + File.separator + path));
			wb = new HSSFWorkbook(fis);

			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + path + "");
		} catch (Exception e) {
			LOG.error("SurveyController::downloadExcel():" + e);
		} finally {
			out = response.getOutputStream();
			if(wb!=null)
				wb.write(out);

			out.flush();
			out.close();
			wb.close();
		}

	}

	@RequestMapping(value = { "/showViewByExcelFileId" })
	public String showViewByExcelFileId(Model model, HttpSession session, @RequestParam("path") Integer id) {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		List<Object[]> details = surveyDetailsRepository.searchSurveyDetails(0, 0, 0, 0, "", "", 0, "", id, userId, -1,
				-1);
		List<SurveyDetailsBean> beans = new ArrayList<>();
		for (Object[] ar : details) {
			SurveyDetailsBean sdb = new SurveyDetailsBean();
			sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
			sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
			sdb.setRust((ar[6] != null) ? (String) ar[6] : "");
			sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
			sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
			sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
			sdb.setStatus((ar[7] != null) ? (String) ar[7] : "");
			sdb.setWheatType((ar[3] != null) ? (String) ar[3] : "");
			sdb.setMode((ar[8] != null) ? (String) ar[8] : "");
			beans.add(sdb);
		}

		List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
		if(obj == null || obj.isEmpty()) {
			model.addAttribute("researchCenterTagged", "no");
		}else {
			model.addAttribute("researchCenterTagged", "yes");}
		model.addAttribute(WrsisPortalConstant.DETAILS, beans);
		model = assignModelData(model, userId);
		model.addAttribute("ShowBack", true);

		return "viewsurveydetailsForExcel";
	}

	@PostMapping("/ivr-survey-data-excel-download")
	public void ivrDownloadExcelByFileName(Model model, HttpServletResponse response,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "zone", required = false) Integer zone,
			@RequestParam(value = WrsisPortalConstant.WOREDA1, required = false) Integer woreda,
			@RequestParam(value = "mob", required = false) String mob,
			@RequestParam(value = "startDate", required = false) String startdate,
			@RequestParam(value = "endDate", required = false) String endDate) throws ParseException {

		
		HSSFWorkbook myExcelBook = null;
		OutputStream out = null;
		try {

			PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

			
			List<IvrDataReportBean> list = iVRDataService.viewIvrSurveyReportData(mob, region, zone, woreda, startdate,
					endDate, -1, -1);
			List<Question> qsList = questionService.viewAllWebQuestion();
			myExcelBook = publishSurveyExcel.buildIVRSurveyExcel(list, qsList);

			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "IVR_Survey_Data " + timeStamp + ".xls";
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);

			out = response.getOutputStream();

		} catch (IOException e) {
			LOG.error("SurveyController::ivrDownloadExcelByFileName():" + e);
		} finally {
			try {
				if(myExcelBook!=null && out!=null)
				{
					myExcelBook.write(out);
					out.flush();
					out.close();
					myExcelBook.close();
				}
				
			} catch (Exception e) {
				LOG.error("SurveyController::ivrDownloadExcelByFileName():" + e);
			}
		}

	}

	@PostMapping(value = { "/wrSurveySummaryReportsSearch" })
	public String wrSurveySummaryReportsSearch(Model model,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "zone", required = false) Integer zone,
			@RequestParam(value = WrsisPortalConstant.WOREDA1, required = false) Integer woreda,
			@RequestParam(value = "mob", required = false) String mob,
			@RequestParam(value = "startdate", required = false) String startdate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		List<IvrDataReportBean> list = iVRDataService.viewIvrSurveyReportData(mob, region, zone, woreda, startdate,
				endDate, -1, -1);
		ArrayList<String> s = new ArrayList<>();
		List<Question> qsList = questionService.viewAllWebQuestion();
		for (Question qust : qsList) {
			s.add(qust.getQustion());
		}

		model.addAttribute("qlength", s);
		model.addAttribute("ivrDataList", list);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		model.addAttribute("region", region);
		model.addAttribute("zone", zone);
		model.addAttribute(WrsisPortalConstant.WOREDA1, woreda);
		model.addAttribute("mob", mob);
		model.addAttribute("startDate", startdate);
		model.addAttribute("endDate", endDate);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "wrSurveySummaryReports";
	}

	// common function for mobile and survey form .
	// validation for Duplication of Sample IDS
	@Autowired
	SampleSurveyDetailsRepository sampleSurveyDetailsRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/api/checkDuplicateSampleIds", method = RequestMethod.POST)
	public @ResponseBody String checkDuplicateSampleIds(@RequestBody String ids, Model model,
			HttpServletRequest request) throws JSONException, UnsupportedEncodingException, SQLException {

		ids = URLDecoder.decode(ids, "UTF-8");
		JSONArray response = new JSONArray();
		JSONArray jsa = new JSONArray(ids);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			LOG.info("SurveyController::checkDuplicateSampleIds():ids value :-"+ids);
			LOG.info("SurveyController::checkDuplicateSampleIds():jsa value :-"+jsa);
			
			con = jdbcTemplate.getDataSource().getConnection();
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject json = new JSONObject();
				json = jsa.getJSONObject(i);

				String sampleId = json.getString("SampleId");
				LOG.info("SurveyController::checkDuplicateSampleIds():sampleId value :-"+sampleId);
				try {
					String query = "select * from wrsis.t_wr_survey_sample_sampleids where vch_sampleid_value=?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, sampleId);
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);

					if (rs.next()) {
						json.put("IsDuplicate", true);
					} else {
						json.put("IsDuplicate", false);
					}
				} catch (NullPointerException nex) {
					LOG.error("SurveyController::checkDuplicateSampleIds():" + nex);
					json.put("IsDuplicate", false);
				}

				response.put(json);

			}

		} catch (SQLException e) {
			LOG.error("SurveyController::checkDuplicateSampleIds():" + e);
		} finally {

			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (Exception e) {
				LOG.error("SurveyController::checkDuplicateSampleIds():" + e);
			}
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
			} catch (Exception e) {
				LOG.error("SurveyController::checkDuplicateSampleIds():" + e);
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				LOG.error("SurveyController::checkDuplicateSampleIds():" + e);
			}
		}

		return response.toString();

	}

	// Author : Raman
	@RequestMapping(value = "/api/checkDuplicateSampleId", method = RequestMethod.POST)
	public @ResponseBody String checkDuplicateSampleId(Model model, @RequestParam("sampleId") String sampleId)
			throws JSONException, UnsupportedEncodingException, SQLException {

		JSONArray response = new JSONArray();
		JSONObject json = new JSONObject();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs=null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			try {
				String sql = "select * from wrsis.t_wr_survey_sample_sampleids where vch_sampleid_value=?";
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, sampleId);
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					json.put("IsDuplicate", true);
				} else {
					json.put("IsDuplicate", false);
				}
			} catch (NullPointerException nex) {
				LOG.error("SurveyController::checkDuplicateSampleId():" + nex);
				json.put("IsDuplicate", false);
			} finally {

				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (Exception e) {
					LOG.error("SurveyController::checkDuplicateSampleIds():" + e);
				}
				try {
					if (stmt != null && !stmt.isClosed()) {
						stmt.close();
					}
				} catch (Exception e) {
					LOG.error("SurveyController::checkDuplicateSampleId():" + e);
				}
				try {
					if (!con.isClosed()) {
						con.close();
					}
				} catch (Exception e) {
					LOG.error("SurveyController::checkDuplicateSampleId():" + e);
				}
			}

			response.put(json);

		} catch (SQLException e) {
			LOG.error("SurveyController::checkDuplicateSampleId():" + e);
		}

		return response.toString();

	}

	// Sample Collected

	@RequestMapping(value = "/api/fetchSampleCollected", method = RequestMethod.POST)
	public @ResponseBody String fetchSampleCollected(@RequestBody String rustTypeId, Model model,
			HttpServletRequest request) throws JSONException, UnsupportedEncodingException {
		rustTypeId = URLDecoder.decode(rustTypeId, "UTF-8");
		JSONArray response = new JSONArray();
		JSONObject json;
		String[] spl = rustTypeId.trim().replace("=", "").split(",");
		for (int j = 0; j < spl.length; j++) {
			List<SampleTypeMaster> det = sampleTypeRepository.findByRustTypeId(Integer.valueOf(spl[j]));
			for (SampleTypeMaster m : det) {
				json = new JSONObject();
				json.put("SampleName", m.getSampleName());
				json.put("SampleId", m.getSampleId());
				json.put("RustId", m.getRustTypeId());

				response.put(json);
			}
		}
		return response.toString();

	}

	

	// filterSurveyViewDatatable

	@RequestMapping(value = { "/filterSurveyViewDatatable" })
	public @ResponseBody SurveyDataTable filterSurveyViewDatatable(Model model, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam("kebeleId") String kebeleId, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("rustTypeId") String rustTypeId) throws ParseException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		if (!(startDate.trim().equals("") || endDate.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
			SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
			endDate = smpl.format(date1);
		}

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<Object[]> details = surveyDetailsRepository.searchSurveyDetailsDataTable(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
				Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, Integer.valueOf(start),
				Integer.valueOf(length));
		Integer totalCount = surveyDetailsRepository.searchSurveyDetailsDataTableCount(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
				Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, -1, -1);
		List<SurveyDetailsBean> beans = new ArrayList<>();
		Integer count = 0;
		for (Object[] ar : details) {
			SurveyDetailsBean sdb = new SurveyDetailsBean();
			sdb.setsNo((Integer.valueOf(start)) + (++count));
			sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
			sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
			sdb.setRust((ar[6] != null) ? (String) ar[6] : "");
			sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
			sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
			sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
			sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\"" + sdb.getSurveyId()
					+ "\">" + sdb.getSurveyNo() + "</a>");
			sdb.setStatus((ar[7] != null) ? (String) ar[7] : "");
			sdb.setWheatType((ar[3] != null) ? (String) ar[3] : "");
			sdb.setMode((ar[8] != null) ? (String) ar[8] : "");
			sdb.setAction((sdb.getStatus().equals("0"))
					? " <a class=\"btn btn-info btn-sm editClass\" data-toggle=\"tooltip editClass\" title=\"\" id=\" \" data-original-title=\"Edit\" survey-id=\""
							+ sdb.getSurveyId() + "\"><i class=\"icon-edit1\"></i></a>\r\n"
							+ "                                          <a class=\"btn btn-danger btn-sm deleteClass\" data-toggle=\"tooltip\" title=\"\" id=\"btnDeleteId\" data-original-title=\"Delete\" survey-id=\""
							+ sdb.getSurveyId() + "\"><i class=\"icon-trash-21\"></i></a>\r\n"
							+ "                                          "
					: "");
			beans.add(sdb);
		}

		

		SurveyDataTable sdt = new SurveyDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@RequestMapping(value = { "/downloadSurveyPDF" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadSurveyPDF(Model model, HttpSession session,
			HttpServletRequest request, @RequestParam("regionIdPDF") String regionId,
			@RequestParam("zoneIdPDF") String zoneId, @RequestParam("woredaIdPDF") String woredaId,
			@RequestParam("kebeleIdPDF") String kebeleId, @RequestParam("startDatePDF") String startDate,
			@RequestParam("endDatePDF") String endDate, @RequestParam("surveyNoPDF") String surveyNo,
			@RequestParam("rustTypeIdPDF") String rustTypeId) throws ParseException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		if (!(startDate.trim().equals("") || endDate.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
			SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
			endDate = smpl.format(date1);
		}

		Integer totalCount = surveyDetailsRepository.searchSurveyDetailsDataTableCount(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
				Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, -1, -1);
		List<Object[]> details = surveyDetailsRepository.searchSurveyDetailsDataTable(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
				Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, 0, totalCount);
		List<SurveyDetailsBean> beans = new ArrayList<>();
		
		for (Object[] ar : details) {
			SurveyDetailsBean sdb = new SurveyDetailsBean();

			sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
			sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
			sdb.setRust((ar[6] != null) ? (String) ar[6] : "");
			sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
			sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
			sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
			sdb.setStatus((ar[7] != null) ? (String) ar[7] : "");
			sdb.setWheatType((ar[3] != null) ? (String) ar[3] : "");
			sdb.setMode((ar[8] != null) ? (String) ar[8] : "");
			beans.add(sdb);
		}

		SurveyPDF spdf = new SurveyPDF();
		ByteArrayInputStream bar = spdf.generateSurveyDetailsPdf(beans);
		HttpHeaders headers = new HttpHeaders();
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat("yyyy-MMM-dd HH_mm_ss").format(ts);
		String FILENAME = "Survey_Details_" + timeStamp + ".pdf";
		headers.clear();
		headers.add("Content-type", "application/pdf");
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));

	}

//Excel Download
	@GetMapping("/surveyExceFormat")
	public String surveyExcelFormatDownLoad(Model model) {
		return "surveyExceFormat";
	}

	@RequestMapping(value = "downloadSurveyExcelFormat", method = RequestMethod.POST)
	public void downloadSurveyExcelFormat(HttpServletResponse response, HttpSession session, Model model,
			@RequestParam("regionIdExcel") String regionId, @RequestParam("zoneIdExcel") String zoneId,
			@RequestParam("woredaIdExcel") String woredaId, @RequestParam("kebeleIdExcel") String kebeleId,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate,
			@RequestParam("surveyNoExcel") String surveyNo, @RequestParam("rustTypeIdExcel") String rustTypeId)
			throws IOException {
		OutputStream out = null;
		HSSFWorkbook myExcelBook = null;
		try {
			
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			if (!(startDate.trim().equals("") || endDate.trim().equals(""))) {
				Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
				SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = smpl.format(date1);
				date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
				endDate = smpl.format(date1);
			}
			List<Object[]> details = surveyDetailsRepository.searchSurveyDetails(Integer.valueOf(regionId),
					Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), startDate, endDate,
					Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, userId, -1, -1);
			List<SurveyDetailsBean> beans = new ArrayList<>();
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setRust((ar[6] != null) ? (String) ar[6] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setStatus((ar[7] != null) ? (String) ar[7] : "");
				sdb.setWheatType((ar[3] != null) ? (String) ar[3] : "");
				sdb.setMode((ar[8] != null) ? (String) ar[8] : "");
				beans.add(sdb);
			}

			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf((obj.get(0))[0])))
					: null;
			myExcelBook = surveyExcel.buildSuveyDetailsSampleExcelPlotData(session, rustTypeRepository,
					sampleTypeRepository, diseaseMasterRepository, seasionMasterRepository, demographyRepository,
					siteTypeRepository, wheatTypeRepository, varietyTypeRepository, growthRepository,
					reactionTypeRepository, fungicideMasterRepository, fungiEffectTypeRepository, soilColorRepository,
					moistureRepository, researchCenterId);
			myExcelBook = surveyExcelDetailsService.createExcelSurvey(myExcelBook, beans);
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat("ssmmHHddMMyyyy").format(ts);
			String FILENAME = "Survey_Details_" + timeStamp + ".xls";
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			out = response.getOutputStream();

		} catch (Exception e) {
			LOG.error("SurveyController::downloadSurveyExcelFormat():" + e);
		} finally {
			if(myExcelBook!=null && out!=null) {
				myExcelBook.write(out);
				out.flush();
				out.close();
				myExcelBook.close();
			}
		}

	}

	@RequestMapping(value = { "/ivr-survey-data-pdf-download" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> ivrDownloadPDFByFileName(Model model, HttpServletResponse response,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "zone", required = false) Integer zone,
			@RequestParam(value = WrsisPortalConstant.WOREDA1, required = false) Integer woreda,
			@RequestParam(value = "mob", required = false) String mob,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws ParseException {
		
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = null;
		try {

			

			
			
			List<IvrDataReportBean> list = iVRDataService.viewIvrSurveyReportData(mob, region, zone, woreda, startDate,
					endDate, -1, -1);
			List<Question> qsList = questionService.viewAllWebQuestion();
			IvrPDF psPdf = new IvrPDF();

			bar = psPdf.generateIVRPdf(list, qsList);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;
			FILENAME = "IVR_Survey_Details_" + timeStamp + ".pdf";
			headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		} catch (Exception e) {
			
			LOG.error("SurveyController::ivrDownloadPDFByFileName():" + e);
		}
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}
	
	
	@RequestMapping(value = { "/ivrSurveyAPIData" })
	public String ivrSurveyAPIData(Model model) {
		try {
			ArrayList<String> s = new ArrayList<>();
			List<Question> qsList = questionService.viewAllAPIQuestion();
			for (Question qust : qsList) {
				s.add(qust.getQustion());
			}
			model.addAttribute("qlength", s);
			List<IvrDataReportBean> ivrList = iVRDataService.ivrSurveyAPIData("", 0, 0, 0, "", "", -1, -1);

			model.addAttribute("ivrDataList", ivrList);
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}
			model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			model.addAttribute(WrsisPortalConstant.ZONE_ID, 0);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, 0);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} catch (Exception e) {
			LOG.error("SurveyController::ivrSurveyAPIData():" + e);
		}
		return "ivrSurveyAPIDataReports";
	}
	
	
	
	@PostMapping(value = { "/ivrSurveyAPIDataSearch" })
	public String ivrSurveyAPIData(Model model,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "zone", required = false) Integer zone,
			@RequestParam(value = WrsisPortalConstant.WOREDA1, required = false) Integer woreda,
			@RequestParam(value = "mob", required = false) String mob,
			@RequestParam(value = "startdate", required = false) String startdate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		List<IvrDataReportBean> list = iVRDataService.ivrSurveyAPIData(mob, region, zone, woreda, startdate,
				endDate, -1, -1);
		ArrayList<String> s = new ArrayList<>();
		List<Question> qsList = questionService.viewAllAPIQuestion();
		for (Question qust : qsList) {
			s.add(qust.getQustion());
		}

		model.addAttribute("qlength", s);
		model.addAttribute("ivrDataList", list);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		model.addAttribute("region", region);
		model.addAttribute("zone", zone);
		model.addAttribute(WrsisPortalConstant.WOREDA1, woreda);
		model.addAttribute("mob", mob);
		model.addAttribute("startDate", startdate);
		model.addAttribute("endDate", endDate);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "ivrSurveyAPIDataReports";
	}
	
	
	@PostMapping("/ivr-survey-api-data-excel-download")
	public void ivrAPIDownloadExcelByFileName(Model model, HttpServletResponse response,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "zone", required = false) Integer zone,
			@RequestParam(value = WrsisPortalConstant.WOREDA1, required = false) Integer woreda,
			@RequestParam(value = "mob", required = false) String mob,
			@RequestParam(value = "startDate", required = false) String startdate,
			@RequestParam(value = "endDate", required = false) String endDate) throws ParseException {

		
		HSSFWorkbook myExcelBook = null;
		OutputStream out = null;
		try {

			PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

			
			List<IvrDataReportBean> list = iVRDataService.ivrSurveyAPIData(mob, region, zone, woreda, startdate,
					endDate, -1, -1);
			List<Question> qsList = questionService.viewAllAPIQuestion();
			myExcelBook = publishSurveyExcel.buildIVRSurveyExcel(list, qsList);

			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "IVR_Survey_API_Data " + timeStamp + ".xls";
			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);

			out = response.getOutputStream();

		} catch (IOException e) {
			LOG.error("SurveyController::ivrAPIDownloadExcelByFileName():" + e);
		} finally {
			try {
				if(myExcelBook!=null && out!=null)
				{
					myExcelBook.write(out);
					out.flush();
					out.close();
					myExcelBook.close();
				}
				
			} catch (Exception e) {
				LOG.error("SurveyController::ivrAPIDownloadExcelByFileName():" + e);
			}
		}

	}

}
