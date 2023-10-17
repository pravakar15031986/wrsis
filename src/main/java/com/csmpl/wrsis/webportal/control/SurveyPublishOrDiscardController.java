package com.csmpl.wrsis.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.SurveyDataTable;
import com.csmpl.wrsis.pdf.DiscardedSurveyDetailsPDF;
import com.csmpl.wrsis.pdf.PendingSurveyDetailsPDF;
import com.csmpl.wrsis.pdf.PublishedSurveyDetailsPDF;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.FungicideEffectTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;
import com.csmpl.wrsis.webportal.entity.MoistureMasterEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenterLocation;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SiteTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SoilColorMasterEntity;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.WheatTypeMasterEntity;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.excel.PublishSurveyExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterLocationRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyExcelFilesRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.SurveyExcelDetailsService;
import com.csmpl.wrsis.webportal.service.SurveyPublishOrDiscardService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class SurveyPublishOrDiscardController extends WrsisPortalAbstractController{
	private static final Logger LOG = LoggerFactory.getLogger(SurveyPublishOrDiscardController.class);
	@Autowired
	SurveyPublishOrDiscardService surveyPublishOrDiscardService;
	
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
	TypeOfRustService typeOfRustService;
	@Autowired
	SurveyExcelDetailsService surveyExcelDetailsService;
	@Autowired
	ResearchCenterLocationRepository researchCenterLocationRepository;
	@Value("${mail.indicator}")
	private String mailFlag;
	
	@Value("${sms.indicator}")
	private String smsFlag;
	
	@Autowired
	MailSmsContentRepository mailSmsContentRepository;
	
	private Model assignModelData(Model model, Integer userId) {

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
		model.addAttribute("InstitutionSelected",
				(obj != null && !obj.isEmpty()) ? obj.get(0) : new ArrayList<Object[]>());

		// fetch all active seasions

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		model.addAttribute(WrsisPortalConstant.SEASON_LIST, seasionList);



		try {
			List<Object[]> demographList = demographyRepository
					.fetchAllActiveDemographs(Integer.valueOf(String.valueOf(obj.get(0)[0]))); // as Eutopia id is 1
			model.addAttribute("DemographList", demographList);
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardController::assignModelData():" + e);
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
				? (Integer) (Integer.valueOf(String.valueOf(((Object[]) obj.get(0))[0])))
				: null;
		if (researchCenterId != null) {
			List<Object[]> surveyors = commonService.getAllSurveyor(researchCenterId);
			model.addAttribute("Surveyors", surveyors);
		}

		return model;
	}
	
	
	@RequestMapping(value={ "/pendingsurvey"} )
	public String pendingsurvey(Model model,HttpServletRequest request,HttpSession session) {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));
		
		
		
		//here User Id is default as zero because Its Admin so its can see all records of Survey Details with survey status zero  
		//total count
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
		Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusByUserDataCount(-1,-1,-1,-1,"", "",-1, "", 0, userId, 0,Integer.valueOf(processId),-1,-1);
				
		model.addAttribute("Show", totalCount>0);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}

		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH,true);

		 
		return "pendingsurvey";
	}
	
	
	@RequestMapping(value={ "/pendingSurveyDataPageWise"} )
	 public @ResponseBody  SurveyDataTable pendingSurveyDataPageWise(Model modelHttp,HttpSession session,  HttpServletRequest request, 
				@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, 
				@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,@RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
				@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
				@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
				@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId,
				RedirectAttributes redirect){

			
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
        String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
			
			
			
			
				
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.pendingSurveyViewPageByUser(regionId, zoneId, woredaId, 
					kebeleId, startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, userId, 0, 
					Integer.valueOf(processId) , Integer.valueOf(start), Integer.valueOf(length));
			
			
			Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusByUserDataCount(
					Integer.valueOf(regionId),Integer.valueOf(zoneId),Integer.valueOf(woredaId),Integer.valueOf(kebeleId),
					startDate, endDate, Integer.valueOf(rustTypeId), surveyNo.trim().toUpperCase(), 0, userId, 0,
					Integer.valueOf(processId),-1,-1);
			

			modelHttp.addAttribute(zoneId);
			modelHttp.addAttribute(woredaId);
			modelHttp.addAttribute(kebeleId);
			SurveyDataTable sdt = new SurveyDataTable();
			sdt.setData(beans);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));

			return sdt;
		}
	
	
	@PostMapping("/publishOrDiscardSurveyData")
	public String publishOrDiscardSurveyData(Model model,
			@RequestParam("surveyFinalArrayId") String[] surveyFinalArrayId,
			@RequestParam("surveyFinalOptionId") String surveyFinalOptionId,
			@RequestParam("surveyFinalReasonId") String surveyFinalReasonId, HttpServletRequest request,
			RedirectAttributes redirect) 
	{
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
			String sendMailStatus=mailFlag; 
			String sendSmsStatus=smsFlag;
			if (!surveyFinalReasonId.isEmpty()) 
			{
				if (!Validation.validateFirstBlankSpace(surveyFinalReasonId)
						|| !Validation.validateLastBlankSpace(surveyFinalReasonId)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in reason");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return "redirect:/pendingsurvey";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(surveyFinalReasonId)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Reason should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
					return "redirect:/pendingsurvey";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(surveyFinalReasonId)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in reason");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return "redirect:/pendingsurvey";
				}
				if (!Validation.validateUserAddress(surveyFinalReasonId)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Reason accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return "redirect:/pendingsurvey";
				}
			}
			
			String msg=surveyPublishOrDiscardService.publishOrDiscardSurveyDataSave(surveyFinalArrayId, surveyFinalOptionId,surveyFinalReasonId, userId, processId,sendMailStatus ,sendSmsStatus);
			if (WrsisPortalConstant.FAILURE.equalsIgnoreCase(msg)) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
			else if("config".equalsIgnoreCase(msg)) 
			{
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Authority Not Configured");
				
			}
			else
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, msg);
			 
		} catch (Exception e)
		{
			
			LOG.error("SurveyPublishOrDiscardController::publishOrDiscardSurveyData():" + e);
		}
		return "redirect:/pendingsurvey";
	}

	@RequestMapping(value = { "/searchSurveyDetailsByStatus" })
	public String searchSurveyDetailsByStatus(Model model, HttpSession session, @RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId,RedirectAttributes redirect) 
	{
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
		
		
			
		
		Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusByUserDataCount(
				Integer.valueOf(regionId),Integer.valueOf(zoneId),Integer.valueOf(woredaId),Integer.valueOf(kebeleId),
				startDate, endDate, Integer.valueOf(rustTypeId), surveyNo.trim().toUpperCase(), 0, userId, 0,
				Integer.valueOf(processId),-1,-1);
		model.addAttribute("Show", totalCount>0);
		
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		List<DemographicEntity> zoneList = demographicRepository.viewAllZoneByLevelAndStatus();
		model.addAttribute("zoneList", zoneList);
	
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		
		return "pendingsurvey";
	}

	@RequestMapping(value = { "/viewSurveyDetailsByRC" })
	public String viewSurveyDetailsByRC(HttpSession session,HttpServletRequest request, @RequestParam("surveyId") Integer surveyId,
			Model model) throws JSONException {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));
		
		try {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));

		model = assignModelData(model, userId);
		}catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardController::viewSurveyDetailsByRC():" + e);
		}
		return "viewSurveyDetailsByRC"; 
	}

	
	@RequestMapping(value = { "/viewSurveyDetailsByRCDiscard" })
	public String viewSurveyDetailsByRCDiscard(HttpSession session, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyId,
			Model model) throws JSONException {
		try {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(Integer.parseInt(surveyId));
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));

		model = assignModelData(model, userId);
		}catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardController::viewSurveyDetailsByRCDiscard():" + e);
		}
		return "viewSurveyDetailsByRC"; 
	}

	
	@RequestMapping(value = { "/viewsurveyDetailsOnPending" })
	public String viewSurveyDetailsOnPending(HttpSession session, @RequestParam("surveyId") Integer surveyId,
			Model model,HttpServletRequest request) throws JSONException {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));
		

		

		
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if(response.getBoolean("capturedImageId"))
		{
			for(int i=0;i<response.getJSONArray(WrsisPortalConstant.IMAGES).length();i++)
			{
				images.add("public/load_image?imagePath="+new String(Base64.getEncoder().encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES,images);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		return "viewsurveyDetailsOnPending"; 

	}
	
	
	
	
	@RequestMapping(value = { "/viewSurveyDetailsOnDashboard" })
	public String viewSurveyDetailsOnDashboard(HttpSession session,HttpServletRequest request, @RequestParam("surveyId") Integer surveyId,
			Model model) throws JSONException {
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));

		
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));

		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		List<String> images = new ArrayList<>();
		if(response.getBoolean("capturedImageId"))
		{
			for(int i=0;i<response.getJSONArray(WrsisPortalConstant.IMAGES).length();i++)
			{
				images.add("public/load_image?imagePath="+new String(Base64.getEncoder().encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES,images);

		model = assignModelData(model, userId);
		
		return "viewSurveyDetailsOnDashboard";
	}
	
	@RequestMapping(value = { "/viewSurveyDetailsOnExternalSample" })
	public String viewSurveyDetailsOnExternalSample(HttpSession session, @RequestParam("surveyId") Integer surveyId,
			Model model) throws JSONException {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
        model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model = assignModelData(model, userId);
		return "viewSurveyDetailsOnExternalSample"; 
	}
	@RequestMapping(value = { "/viewSurveyDetailsOnDumpedSample" })
	public String viewSurveyDetailsOnDumpedSample(HttpSession session,HttpServletRequest request, @RequestParam("surveyId") Integer surveyId,
			Model model) throws JSONException {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));

		
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
        model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		model = assignModelData(model, userId);
		return "viewSurveyDetailsOnDumpedSample"; 
	} 
	@RequestMapping(value = { "/viewSurveyDetailsOnPublished" })
	public String viewsurveyDetailsOnPublished(HttpSession session,
			HttpServletRequest request,
			@RequestParam("surveyId") Integer surveyId,
			@RequestParam(value=WrsisPortalConstant.R_URL,required = false) String r_url,
			Model model) throws JSONException {

		
		model.addAttribute(WrsisPortalConstant.R_URL, r_url);
		JSONObject response = commonService.getFormmatedJsonBySurveyId(surveyId);
		List<String> images = new ArrayList<>();
		if(response.getBoolean("capturedImageId"))
		{
			for(int i=0;i<response.getJSONArray(WrsisPortalConstant.IMAGES).length();i++)
			{
				images.add("public/load_image?imagePath="+new String(Base64.getEncoder().encode((response.getJSONArray(WrsisPortalConstant.IMAGES).getString(i)).getBytes())));
			}
		}
		model.addAttribute(WrsisPortalConstant.IMAGES,images);
		model.addAttribute(WrsisPortalConstant.SURVEY_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		
		model.addAttribute(WrsisPortalConstant.SURVEY_ID, surveyId);
		model.addAttribute(WrsisPortalConstant.SURVEY_NO, response.getString(WrsisPortalConstant.SURVEY_NO));
		return "viewSurveyDetailsOnPublished"; 
	}
	
	@RequestMapping(value = { "/publishedsurvey" })
	public String publishedsurvey(Model model,HttpServletRequest request) {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));

		// here User Id is default as zero because Its Admin so its can see all records
				// of Survey Details with survey status one
		
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "publishedsurvey";
	}

	@RequestMapping(value = { "/publishedSurveyDataPageWise" })
	 public @ResponseBody  SurveyDataTable publishedSurveyDataPageWise(Model modelHttp,HttpSession session,  HttpServletRequest request, 
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, 
			@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,@RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId){

		// here User Id is default as zero because Its Admin so its can see all records
				// of Survey Details with survey status one
        String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		
		
		
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.publishedSurveyViewPage(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 1,Integer.valueOf(start),Integer.valueOf(length));
		
		Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusDataCount(Integer.valueOf(regionId),Integer.valueOf(zoneId),Integer.valueOf(woredaId),Integer.valueOf(kebeleId),startDate, endDate, Integer.valueOf(rustTypeId), surveyNo.trim().toUpperCase(), 0, 0, 1,-1,-1);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		SurveyDataTable sdt = new SurveyDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		
		LOG.info(WrsisPortalConstant.REGION_LIST+regionList);
		
		return sdt;
	}
	
	
	@RequestMapping(value = { "/searchSurveyDetailsPublishedByStatus" })
	public String searchSurveyDetailsPublishedByStatus(Model model, HttpSession session, @RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId) {
	

		
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.publishedSurveyViewPage(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 1,-1,-1);
		// here User Id is default as zero because Its Admin so its can see all records
		// of Survey Details with survey status zero
		

		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.DETAILS, beans);
		
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId);
		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "publishedsurvey";
	}
	
	@RequestMapping(value = { "/discardedsurvey" })
	public String discardedsurvey(HttpServletRequest request,Model model) {

		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));

		
		
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus(); 
		if (regionList !=null)
		{ model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList); }
		  
		  model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		  model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "discardedsurvey";
	}
	
	
	@RequestMapping(value = { "/discardedSurveyDataPageWise" })
	 public @ResponseBody SurveyDataTable discardedsurveyPageWise(Model model,
			HttpSession session,  HttpServletRequest request, 
			@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId){

		
        String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		
		
		
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.discardSurveyViewPage(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 2,Integer.valueOf(start),Integer.valueOf(length));
		
		Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusDataCount(Integer.valueOf(regionId),Integer.valueOf(zoneId),Integer.valueOf(woredaId),Integer.valueOf(kebeleId),startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 2,-1,-1);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		SurveyDataTable sdt = new SurveyDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		
		LOG.info(WrsisPortalConstant.REGION_LIST+regionList);
		
		return sdt;
		
		
		
	}
	
	@RequestMapping(value = { "/searchSurveyDetailsDiscardByStatus" })
	public String searchSurveyDetailsDiscardByStatus(Model model,	HttpServletRequest request, HttpSession session, @RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId) {
		
		model.addAttribute(WrsisPortalConstant.R_URL,request.getParameter(WrsisPortalConstant.R_URL));

	
		

		
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.discardSurveyViewPage(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 2,-1,-1);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		 
		
		model.addAttribute(WrsisPortalConstant.DETAILS,beans);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId);
		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		model.addAttribute("searchRegionId", regionId);

		return "discardedsurvey";
	}
	//Show published data by User Id 
	@GetMapping("/surveyPublishedReport")
	public String surveyPublishedReport(Model model,HttpSession session)
	{
		
		
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW,false);
		
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());


		return "surveyPublishedReport";
	}
	
	
	@RequestMapping("/surveyPublishedReportPageWise")
	public @ResponseBody SurveyDataTable surveyPublishedReportPageWise(Model model, HttpSession session,
			HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) Integer kebeleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) Integer rustTypeId) {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

        String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);



		
		//Its for Published Data
		Integer actionStatus=1;
	
		List<SurveyDetailsBean> surveyList = surveyPublishOrDiscardService.viewSurveyPublishedAndDiscardReport(regionId, zoneId, woredaId, kebeleId, startDate, endDate, rustTypeId, surveyNo, 0, userId, actionStatus, Integer.valueOf(start), Integer.valueOf(length));
		Integer rowCount=surveyDetailsRepository.viewSurveyPendingPublishedDiscardDetailsByUserCount(regionId, zoneId, woredaId, kebeleId, startDate, endDate, rustTypeId, surveyNo, 0, userId, actionStatus, -1, -1);
		SurveyDataTable sdt = new SurveyDataTable();
		sdt.setData(surveyList);
		sdt.setRecordsFiltered(rowCount);
		sdt.setRecordsTotal(rowCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}
	 
	
	//Search published data by User Id 
	@RequestMapping(value = { "/searchSurveyDetailsPublishedByUser" })
	public String searchSurveyDetailsPublishedRecearchCenterByStatus(Model model, HttpSession session,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) Integer kebeleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) Integer rustTypeId) 
	{
	
 
		
		


		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId);
		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW,true);
		return "surveyPublishedReport";
	}
	
	//Show Discarded data by User Id 
	@GetMapping("/surveyDiscardedReport")
	public String surveyDiscardedReport(Model model,HttpSession session)
	{
		
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW,false);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

		return "surveyDiscardedReport";
	}
	
	
	@RequestMapping("/surveyDiscardedReportPageWise")
	public @ResponseBody SurveyDataTable surveyDiscardedReportPageWise(Model model, HttpSession session,
			HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) Integer kebeleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate,
			@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) Integer rustTypeId) {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

        String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		
		//Its for Discarded Data
		Integer actionStatus=2;
	
		List<SurveyDetailsBean> surveyList = surveyPublishOrDiscardService.viewSurveyPublishedAndDiscardReport(regionId, zoneId, woredaId, kebeleId, startDate, endDate, rustTypeId, surveyNo, 0, userId, actionStatus, Integer.valueOf(start), Integer.valueOf(length));
		Integer rowCount=surveyDetailsRepository.viewSurveyPendingPublishedDiscardDetailsByUserCount(regionId, zoneId, woredaId, kebeleId, startDate, endDate, rustTypeId, surveyNo, 0, userId, actionStatus, -1, -1);
		SurveyDataTable sdt = new SurveyDataTable();
		sdt.setData(surveyList);
		sdt.setRecordsFiltered(rowCount);
		sdt.setRecordsTotal(rowCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}
	//Search Discarded data by User Id 
	@PostMapping("/searchSurveyDetailsDiscardByUser")
	public String searchSurveyDetailsDiscardRecearchCenterByStatus(Model model, HttpSession session,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			 @RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId) {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		//This is used for the View All record by the EIR Admin  
		// here User Id is default as zero because Its Admin so its can see all records
		// of Survey Details with survey status zero
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.searchSurveyDiscardedReport(regionId,zoneId, woredaId, kebeleId, startDate, endDate, surveyNo, rustTypeId, userId);
		
		
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null)
		{
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.DETAILS, beans);
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId);
		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW,true);
		return "surveyDiscardedReport";
	}
	
	//discarded survey
	
	@RequestMapping("/surveyDiscardedReportDownload")
	public void surveyDiscardedReportDownload(HttpServletResponse response,Model model, HttpSession session,
			@RequestParam("regionIdExcel") String regionId,
			@RequestParam("zoneIdExcel") String zoneId,
			 @RequestParam("woredaIdExcel") String woredaId,
			@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startDIdExcel") String startDate,
			@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
			@RequestParam("rustIdExcel") String rustTypeId) throws IOException,FileNotFoundException {
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.searchSurveyDiscardedReport(regionId,zoneId, woredaId, kebeleId, startDate, endDate, surveyNo, rustTypeId, userId);
		
	    HSSFWorkbook excelBook =surveyExcel.surveyDiscardedReportDownload(response,beans);
	    		
	    
	    
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
	    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
	    
	    
	    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Discarded_Survey_Details_"+formattedDate+".xls");
	   OutputStream out = response.getOutputStream();
		 excelBook.write(out); 
	     out.flush();
	     out.close();
	    excelBook.close();
		 
	}
	
	
	@GetMapping(value={"/rustSurveyData"})
	public String rustSurveyData(Model model,HttpSession session,@ModelAttribute("searchVo")SearchVo searchVo )
	{
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
		if(rcmapping!=null) 
		{
			//This use for Tagging  with Research Center 
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyDataReportByRCTagging(userId);
			List<DemographicEntity> regionL=new ArrayList<>();
			List<ResearchCenterLocation> rCLocationList = researchCenterLocationRepository.getAllResearchCenterLocationByRegion();
			if (rCLocationList != null)
			{
				for (ResearchCenterLocation rCLocation : rCLocationList)
				{
					DemographicEntity region=demographicRepository.getOne(rCLocation.getRegionId());
					regionL.add(region);
				}
				
			}
			List<DemographicEntity> regionList=regionL.stream().distinct().collect(Collectors.toList());
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.DETAILS, beans);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
			return "rustSurveyData";
		}else {
			//This use for not tagging with RC
			//List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyDataReport(); commeted by me today
			
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null)
			{
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

			return "rustSurveyData2";
		}
		
		
			
	}
	
	@PostMapping("/rustSurveyDataSearch")
	public String rustSurveyDataSearch(Model model,HttpServletRequest request, HttpSession session, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId, @RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.KEBELE_ID) Integer kebeleId, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam("servRustId") Integer servRustId) throws ParseException {
	




		
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute(WrsisPortalConstant.ZONE_ID,zoneId);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
			model.addAttribute(WrsisPortalConstant.KEBELE_ID, kebeleId); 
			model.addAttribute(WrsisPortalConstant.START_DATE,startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
			model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo); 
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID,servRustId);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			
			UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
			if(rcmapping!=null) 
			{
				//This use for Tagging  with Research Center
				
				//Rc  Tagging
				List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyDataSearchReportByRCTagging(regionId, zoneId, woredaId, kebeleId, startDate, endDate, servRustId,surveyNo,userId);
				List<DemographicEntity> regionL=new ArrayList<>();
				List<ResearchCenterLocation> rCLocationList = researchCenterLocationRepository.getAllResearchCenterLocationByRegion();
				if (rCLocationList != null)
				{
					for (ResearchCenterLocation rCLocation : rCLocationList)
					{
						DemographicEntity region=demographicRepository.getOne(rCLocation.getRegionId());
						regionL.add(region);
					}
					
				}
				List<DemographicEntity> regionList=regionL.stream().distinct().collect(Collectors.toList());
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
				model.addAttribute(WrsisPortalConstant.DETAILS, beans);
				return "rustSurveyDataOtherUser";
			}else {
				//Rc Not Tagging
				
				// here User Id is default as zero because Its Admin so its can see all records
				// of Survey Details with survey status zero
				
				
	
																							   
				
				
				List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPage(Integer.toString(regionId),Integer.toString(zoneId),
					Integer.toString(woredaId),Integer.toString(kebeleId),startDate, endDate, servRustId, surveyNo, 0, 0, 1,-1,-1);
				List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
				if (regionList != null)
				{
					model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
				}
				model.addAttribute(WrsisPortalConstant.DETAILS, beans);
				return "rustSurveyData2";
			}
			
		
		
	}
	@PostMapping("/survey-publish-excel-download")
	public void surveyPublishedExcel(Model model,HttpServletResponse response,HttpSession session,
			@RequestParam(value="region",required=false)Integer region,
			@RequestParam(value="zone",required=false)Integer zone,
			@RequestParam(value="woreda",required=false)Integer woreda,
			@RequestParam(value="kebele",required=false)Integer kebele,
			@RequestParam(value=WrsisPortalConstant.START_DATE,required=false)String startDate,
			@RequestParam(value=WrsisPortalConstant.END_DATE,required=false)String endDate,
			@RequestParam(value="surveyNumber",required=false)String surveyNumber,
			@RequestParam(value="rustId",required=false)Integer rustId)throws ParseException, IOException
	{
		
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
		if(rcmapping!=null) 
		{
			//RC Tagging
			
			
			PublishSurveyExcel publishedSurveyExcel = new PublishSurveyExcel();
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.surveyPublishedExcelByRCTaggingData(region, zone, woreda, kebele, startDate, endDate,rustId,surveyNumber,userId);
			 HSSFWorkbook myExcelBook =publishedSurveyExcel.rustPublishedSurveyExcelByRCTagging(beans) ;
			 
			 
			 response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			 Date date = new Date();
				long time = date.getTime();
				Timestamp ts = new Timestamp(time);
				String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
				String FILENAME = null;
				
				FILENAME = "Published_Survey_Details_" + timeStamp +".xls";
			    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename="+FILENAME);

			    OutputStream out = response.getOutputStream();
			    myExcelBook.write(out); 

			    out.flush();
			    out.close();
			    myExcelBook.close();
			
		}else {
			//RC Not Tagging
			
			
		
			//Worked on 24-Mar-2020
			
			
			
			PublishSurveyExcel publishedSurveyExcel = new PublishSurveyExcel();
			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPageExcel(Integer.toString(region),Integer.toString(zone),
					Integer.toString(woreda),Integer.toString(kebele),startDate, endDate, rustId, surveyNumber, 0, 0, 1,-1,-1);
			 HSSFWorkbook myExcelBook =publishedSurveyExcel.rustPublishedSurveyExcel(beans) ;
			 

			    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			    
			    LocalDateTime myDateObj = LocalDateTime.now();
			    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
			    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
			    
			    
			    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Published_Survey_Details_"+formattedDate+".xls");

			    OutputStream out = response.getOutputStream();
			    myExcelBook.write(out); 

			    out.flush();
			    out.close();
			    myExcelBook.close();
		}
		
		
	}
	
	
	// download pending survey excel 
	
	
	
	
	@RequestMapping(value="downloadPendingSurveyExcel" , method=RequestMethod.POST)
	public void downloadPublishSurveyExcel(HttpServletResponse response, 
			HttpSession session,Model model,
			@RequestParam("regionIdExcel") String regionId,
			@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
			@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
			@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
			@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException
	{
	PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();
	
	int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
	String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
	
	
	
	
	List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.pendingSurveyViewPageExcelByUser(regionId, zoneId, woredaId, kebeleId, startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, userId, 0, Integer.valueOf(processId), -1, -1);
	
	
	
	 HSSFWorkbook myExcelBook =publishSurveyExcel.buildPendingSurveyExcel(beans) ;
	 

	    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
	    
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
	    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
	    
	    
	    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Pending_Survey_Details_"+formattedDate+".xls");

	    OutputStream out = response.getOutputStream();
	    myExcelBook.write(out); 

	    out.flush();
	    out.close();
	    myExcelBook.close();
	}	
	
	//download publish data excel
	
	
	@RequestMapping(value="downloadPublishedSurveyExcel" , method=RequestMethod.POST)
	public void downloadPublishedSurveyExcel(HttpServletResponse response, 
			HttpSession session,Model model,
			@RequestParam("regionIdExcel") String regionId,
			@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
			@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
			@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
			@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException
	{
	PublishSurveyExcel publishedSurveyExcel = new PublishSurveyExcel();
	
	List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.publishedSurveyViewPageExcel(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 1,-1,-1);
	
	 HSSFWorkbook myExcelBook =publishedSurveyExcel.buildPublishedSurveyExcel(beans) ;
	 

	    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
	    
	    LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
	    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
	    
	    
	    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Published_Survey_Details_"+formattedDate+".xls");

	    OutputStream out = response.getOutputStream();
	    myExcelBook.write(out); 

	    out.flush();
	    out.close();
	    myExcelBook.close();
	}	
	
	// download discarded survey excel 
	
		@RequestMapping(value="downloadDiscardedSurveyExcel" , method=RequestMethod.GET)
		public void downloadDiscardedSurveyExcel(HttpServletResponse response, HttpSession session,Model model) throws FileNotFoundException, IOException
		{
		PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.discardSurveyView();
		 HSSFWorkbook myExcelBook =publishSurveyExcel.buildDiscardedSurveyExcel(beans) ;
		 

		    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
		    
		    LocalDateTime myDateObj = LocalDateTime.now();
		    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
		    
		    
		    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Discarded_Survey_Details_"+formattedDate+".xls");

		    OutputStream out = response.getOutputStream();
		    myExcelBook.write(out); 

		    out.flush(); 
		    out.close();
		    myExcelBook.close();
		}	
		
		
		// download discarded survey excel 
		
		@RequestMapping(value="downloadDiscardSurveyExcel" , method=RequestMethod.POST)
		public void downloadDiscardSurveyExcel(HttpServletResponse response, 
				HttpSession session,Model model,
				@RequestParam("regionIdExcel") String regionId,
				@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
				@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
				@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
				@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException
		{
		PublishSurveyExcel discardSurveyExcel = new PublishSurveyExcel();
		
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.discardSurveyViewPageExcel(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 2,-1,-1);
		
		HSSFWorkbook myExcelBook =discardSurveyExcel.buildDiscardedSurveyExcel(beans);

		    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
		    
		    LocalDateTime myDateObj = LocalDateTime.now();
		    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
		    
		    
		    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Discard_Survey_Details_"+formattedDate+".xls");

		    OutputStream out = response.getOutputStream();
		    myExcelBook.write(out); 

		    out.flush();
		    out.close();
		    myExcelBook.close();
		}
		
		//Survey Data by EIR Admin
		@RequestMapping(value = { "/rustSurveyDataPageWise" })
		 public @ResponseBody  SurveyDataTable rustSurveyDataPageWise(Model modelHttp,HttpSession session,  HttpServletRequest request, 
				@RequestParam(WrsisPortalConstant.REGION_ID) String regionId, 
				@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
				@RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId,
				@RequestParam(WrsisPortalConstant.KEBELE_ID) String kebeleId, 
				@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
				@RequestParam(WrsisPortalConstant.END_DATE) String endDate, 
				@RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
				@RequestParam(WrsisPortalConstant.RUST_TYPE_ID) String rustTypeId){

			// here User Id is default as zero because Its Admin so its can see all records
					// of Survey Details with survey status one
	        String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			
			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPage(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 1,Integer.valueOf(start),Integer.valueOf(length));
			
			Integer totalCount =surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatusDataCount(Integer.valueOf(regionId),Integer.valueOf(zoneId),Integer.valueOf(woredaId),Integer.valueOf(kebeleId),startDate, endDate, Integer.valueOf(rustTypeId), surveyNo.trim().toUpperCase(), 0, 0, 1,-1,-1);
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			SurveyDataTable sdt = new SurveyDataTable();
			sdt.setData(beans);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
			
			LOG.info(WrsisPortalConstant.REGION_LIST+regionList);
			
			return sdt;
		}
		
		
		//Published Survey PDF
		@RequestMapping(value={ "/downloadsurveypublishpdf"} ,produces = MediaType.APPLICATION_PDF_VALUE)
		public ResponseEntity<InputStreamResource> downloadSurveyPublishedPDF(Model model,HttpServletResponse response,HttpSession session,
				@RequestParam(value="region",required=false)Integer region,
				@RequestParam(value="zone",required=false)Integer zone,
				@RequestParam(value="woreda",required=false)Integer woreda,
				@RequestParam(value="kebele",required=false)Integer kebele,
				@RequestParam(value=WrsisPortalConstant.START_DATE,required=false)String startDate,
				@RequestParam(value=WrsisPortalConstant.END_DATE,required=false)String endDate,
				@RequestParam(value="surveyNumber",required=false)String surveyNumber,
				@RequestParam(value="rustId",required=false)Integer rustId)throws ParseException, IOException
		{
			
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			HttpHeaders headers = new HttpHeaders();
			UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
			if(rcmapping!=null) 
			{
				//RC Tagging
				
				
				
				List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.surveyPublishedExcelByRCTaggingData(region, zone, woreda, kebele, startDate, endDate,rustId,surveyNumber,userId);
				PublishedSurveyDetailsPDF pSDPDF=new PublishedSurveyDetailsPDF();
				ByteArrayInputStream bar = null;
				try {
				bar=pSDPDF.generatePublishedSurveyPdf(beans);
				}catch (Exception e) {
					LOG.error("SurveyPublishOrDiscardController::downloadSurveyPublishedPDF():" + e);

				}
				Date date = new Date();
				long time = date.getTime();
				Timestamp ts = new Timestamp(time);
				String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
				String FILENAME = null;
				
				FILENAME = "Published_Survey_Details_" + timeStamp + ".pdf";
				 
			    
				 headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename="+FILENAME);

			    
				
			  		
			  		return ResponseEntity
			  	            .ok()
			  	            .headers(headers)
			  	            .contentType(MediaType.APPLICATION_PDF)
			  	            .body(new InputStreamResource(bar));
				
			}else {
				//RC Not Tagging
				
				
			
				//Worked on 24-Mar-2020
				
				List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPageExcel(Integer.toString(region),Integer.toString(zone),
						Integer.toString(woreda),Integer.toString(kebele),startDate, endDate, rustId, surveyNumber, 0, 0, 1,-1,-1);
				PublishedSurveyDetailsPDF pSDPDF=new PublishedSurveyDetailsPDF();
				ByteArrayInputStream bar = null;
				try {
				bar=pSDPDF.generatePublishedSurveyPdf(beans);
				}catch (Exception e) {
					LOG.error("SurveyPublishOrDiscardController::downloadSurveyPublishedPDF():" + e);
				}
				Date date = new Date();
				long time = date.getTime();
				Timestamp ts = new Timestamp(time);
				String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
				String FILENAME = null;
				
				FILENAME = "Published_Survey_Details_" + timeStamp + ".pdf";
				 
			    
				 headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename="+FILENAME);

			    
				
			  		
			  		return ResponseEntity
			  	            .ok()
			  	            .headers(headers)
			  	            .contentType(MediaType.APPLICATION_PDF)
			  	            .body(new InputStreamResource(bar));
			}
			
			
		}
		
		//Discard Survey PDF
		@RequestMapping(value={ "/downloadsurveydiscardpdf"} ,produces = MediaType.APPLICATION_PDF_VALUE)
		public ResponseEntity<InputStreamResource>  surveyDiscardedReportPDFDownload(HttpServletResponse response,Model model, HttpSession session,
				@RequestParam("regionIdExcel") String regionId,
				@RequestParam("zoneIdExcel") String zoneId,
				 @RequestParam("woredaIdExcel") String woredaId,
				@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startDIdExcel") String startDate,
				@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
				@RequestParam("rustIdExcel") String rustTypeId) throws IOException,FileNotFoundException {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			
			HttpHeaders headers = new HttpHeaders();
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.searchSurveyDiscardedReport(regionId,zoneId, woredaId, kebeleId, startDate, endDate, surveyNo, rustTypeId, userId);
			DiscardedSurveyDetailsPDF pSDPDF=new DiscardedSurveyDetailsPDF();
			ByteArrayInputStream bar = null;
			try {
			bar=pSDPDF.generateDiscardedSurveyPdf(beans);
			}catch (Exception e) {
				LOG.error("SurveyPublishOrDiscardController::surveyDiscardedReportPDFDownload():" + e);
			}
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;
			
			FILENAME = "Discarded_Survey_Details_" + timeStamp + ".pdf";
			 
		    
			 headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename="+FILENAME);

		    
			
		  		
		  		return ResponseEntity
		  	            .ok()
		  	            .headers(headers)
		  	            .contentType(MediaType.APPLICATION_PDF)
		  	            .body(new InputStreamResource(bar));
			 
		}
		//Pending Survey PDF
		@RequestMapping(value={ "/downloadPendingSurveyPdf"} ,produces = MediaType.APPLICATION_PDF_VALUE)
		public ResponseEntity<InputStreamResource>  downloadPublishSurveyPdf(HttpServletResponse response, 
				HttpSession session,Model model,
				@RequestParam("regionIdExcel") String regionId,
				@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
				@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
				@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
				@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException
		{
		
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID;
			
		
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.pendingSurveyViewPageExcelByUser(regionId, zoneId, woredaId, kebeleId, startDate, endDate, Integer.valueOf(rustTypeId), surveyNo.trim(), 0, userId, 0,Integer.valueOf(processId) , -1, -1);
			
			
			PendingSurveyDetailsPDF psPdf=new PendingSurveyDetailsPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.generatePendingSurveyPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "Pending_Survey_Details_" + timeStamp + ".pdf";
		 headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename="+FILENAME);
  		return ResponseEntity
	  	            .ok()
	  	            .headers(headers)
	  	            .contentType(MediaType.APPLICATION_PDF)
	  	            .body(new InputStreamResource(bar));
		}	
		
		//download publish Survey data PDF
			
		@RequestMapping(value={ "/downloadPublishedSurveyPdf"} ,produces = MediaType.APPLICATION_PDF_VALUE)
		public ResponseEntity<InputStreamResource> downloadPublishedSurveyPDF(HttpServletResponse response, 
				HttpSession session,Model model,
				@RequestParam("regionIdExcel") String regionId,
				@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
				@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
				@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
				@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException
		{
		List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.publishedSurveyViewPageExcel(regionId,zoneId,woredaId,kebeleId,startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 1,-1,-1);
		PublishedSurveyDetailsPDF psPdf=new PublishedSurveyDetailsPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = psPdf.generatePublishedSurveyPdf(beans);
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;
		FILENAME = "Published_Survey_Details_" + timeStamp + ".pdf";
		 headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename="+FILENAME);
  		return ResponseEntity
	  	            .ok()
	  	            .headers(headers)
	  	            .contentType(MediaType.APPLICATION_PDF)
	  	            .body(new InputStreamResource(bar)); 
		    
		}
		
		// download discarded survey PDF 
		
	@RequestMapping(value = { "/downloadDiscardSurveyPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadDiscardSurveyPDF(HttpServletResponse response,
			HttpSession session, Model model,
			@RequestParam("regionIdExcel") String regionId,
			@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
			@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
			@RequestParam("endDIdExcel") String endDate, @RequestParam("surveyNoIdExcel") String surveyNo,
			@RequestParam("rustIdExcel") String rustTypeId) throws FileNotFoundException, IOException {
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = null;
		try {
			List<SurveyDetailsBean> beans = surveyPublishOrDiscardService.discardSurveyViewPageExcel(regionId, zoneId,
					woredaId, kebeleId, startDate, endDate, Integer.valueOf(rustTypeId), surveyNo, 0, 0, 2, -1, -1);

			DiscardedSurveyDetailsPDF psPdf = new DiscardedSurveyDetailsPDF();

			bar = psPdf.generateDiscardedSurveyPdf(beans);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;
			FILENAME = "Discard_Survey_Details_" + timeStamp + ".pdf";
			headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardController::downloadDiscardSurveyPDF():" + e);
		}
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));

	}
	 
	
	
	//Survey Data PDF
	@RequestMapping(value = { "/survey-publish-pdf-download" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource>  surveyPublishedPdfWDownload(Model model,HttpServletResponse response,HttpSession session,
			@RequestParam(value="region",required=false)Integer region,
			@RequestParam(value="zone",required=false)Integer zone,
			@RequestParam(value="woreda",required=false)Integer woreda,
			@RequestParam(value="kebele",required=false)Integer kebele,
			@RequestParam(value=WrsisPortalConstant.START_DATE,required=false)String startDate,
			@RequestParam(value=WrsisPortalConstant.END_DATE,required=false)String endDate,
			@RequestParam(value="surveyNumber",required=false)String surveyNumber,
			@RequestParam(value="rustId",required=false)Integer rustId)throws ParseException, IOException
	{
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = null;
		try {
			
		
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
		if(rcmapping!=null) 
		{
			//RC Tagging
			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.surveyPublishedExcelByRCTaggingData(region, zone, woreda, kebele, startDate, endDate,rustId,surveyNumber,userId);
			PublishedSurveyDetailsPDF psPdf = new PublishedSurveyDetailsPDF();

			bar = psPdf.generatePublishedSurveyPdf(beans);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;
			FILENAME = "Published_Survey_Details_" + timeStamp + ".pdf";
			headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
			
			
		}else {
			//RC Not Tagging
			
			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPageExcel(Integer.toString(region),Integer.toString(zone),
					Integer.toString(woreda),Integer.toString(kebele),startDate, endDate, rustId, surveyNumber, 0, 0, 1,-1,-1);
			PublishedSurveyDetailsPDF psPdf = new PublishedSurveyDetailsPDF();

			bar = psPdf.generatePublishedSurveyPdf(beans);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;
			FILENAME = "Published_Survey_Details_" + timeStamp + ".pdf";
			headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);
			
			
			   
		}
		}catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardController::surveyPublishedPdfWDownload():" + e);
		}

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}
	
	
	@PostMapping("/survey-publish-data-excel-generate")
	public void surveyPublishDataExcelGenerate(Model model,HttpServletResponse response,HttpSession session,
			@RequestParam(value="region",required=false)Integer region,
			@RequestParam(value="zone",required=false)Integer zone,
			@RequestParam(value="woreda",required=false)Integer woreda,
			@RequestParam(value="kebele",required=false)Integer kebele,
			@RequestParam(value=WrsisPortalConstant.START_DATE,required=false)String startDate,
			@RequestParam(value=WrsisPortalConstant.END_DATE,required=false)String endDate,
			@RequestParam(value="surveyNumber",required=false)String surveyNumber,
			@RequestParam(value="rustId",required=false)Integer rustId)throws ParseException, IOException
	{
		
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		 
		UserResearchCenterMapping  rcmapping=surveyPublishOrDiscardService.checkUsertaggingResearchCenter(userId);
		if(rcmapping!=null) 
		{
			//RC Tagging
			
			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.surveyPublishedExcelByRCTaggingData(region, zone, woreda, kebele, startDate, endDate,rustId,surveyNumber,userId);
			 HSSFWorkbook myExcelBook =null;
			 
			 DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
				
			 myExcelBook =surveyExcel.buildSuveyDetailsSampleExcelPlotData(session,rustTypeRepository,sampleTypeRepository,diseaseMasterRepository,seasionMasterRepository,demographyRepository,siteTypeRepository,wheatTypeRepository,varietyTypeRepository,growthRepository,reactionTypeRepository,fungicideMasterRepository,fungiEffectTypeRepository,soilColorRepository,moistureRepository,0) ;
			
			 myExcelBook =surveyExcelDetailsService.createExcelSurvey(myExcelBook, beans);
			 
			 
			 response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			 Date date = new Date();
				long time = date.getTime();
				Timestamp ts = new Timestamp(time);
				String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
				String FILENAME = null;
				
				FILENAME = "Published_Survey_Details_" + timeStamp +".xls";
			    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename="+FILENAME);

			    OutputStream out = response.getOutputStream();
			    myExcelBook.write(out); 

			    out.flush();
			    out.close();
			    myExcelBook.close();
			
		}else {
			//RC Not Tagging
			
			
		
			//Worked on 24-Mar-2020
			
			
			

			
			
			List<SurveyDetailsBean> beans =surveyPublishOrDiscardService.rustSurveyPublishedDataViewPageExcel(Integer.toString(region),Integer.toString(zone),
					Integer.toString(woreda),Integer.toString(kebele),startDate, endDate, rustId, surveyNumber, 0, 0, 1,-1,-1);
			 
			HSSFWorkbook myExcelBook =null;
			 
			DownloadSampleExcel surveyExcel = new DownloadSampleExcel();
			
			 myExcelBook =surveyExcel.buildSuveyDetailsSampleExcelPlotData(session,rustTypeRepository,sampleTypeRepository,diseaseMasterRepository,seasionMasterRepository,demographyRepository,siteTypeRepository,wheatTypeRepository,varietyTypeRepository,growthRepository,reactionTypeRepository,fungicideMasterRepository,fungiEffectTypeRepository,soilColorRepository,moistureRepository,0) ;
			
			 myExcelBook =surveyExcelDetailsService.createExcelSurvey(myExcelBook, beans);
			    
			    response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			    
			    LocalDateTime myDateObj = LocalDateTime.now();
			    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
			    String formattedDate = myDateObj.format(myFormatObj).replaceAll(" ", "").replaceAll(":", "");
			    
			    
			    response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Published_Survey_Details_"+formattedDate+".xls");

			    OutputStream out = response.getOutputStream();
			    myExcelBook.write(out); 

			    out.flush();
			    out.close();
			    myExcelBook.close();
		}
		
		
	}
}
