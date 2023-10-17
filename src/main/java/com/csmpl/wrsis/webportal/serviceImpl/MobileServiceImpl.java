package com.csmpl.wrsis.webportal.serviceImpl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.bean.QueAnsDetails;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.MobileAppVersionEntity;
import com.csmpl.wrsis.webportal.entity.MobileLoginTracking;
import com.csmpl.wrsis.webportal.entity.MobileTokenEntity;
import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;
import com.csmpl.wrsis.webportal.entity.MonitorFungicideEntity;
import com.csmpl.wrsis.webportal.entity.MonitorGrowthStageEntity;
import com.csmpl.wrsis.webportal.entity.MonitorLocationEntity;
import com.csmpl.wrsis.webportal.entity.MonitorRustEntity;
import com.csmpl.wrsis.webportal.entity.MonitorVarietyEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.RustIncidentDetailsEntity;
import com.csmpl.wrsis.webportal.entity.RustIncidentEntity;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthStageRepository;
import com.csmpl.wrsis.webportal.repository.MobileAppVersionRepository;
import com.csmpl.wrsis.webportal.repository.MobileLoginTrackingRepository;
import com.csmpl.wrsis.webportal.repository.MobileTokenRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.repository.MonitorGrowthStageRepository;
import com.csmpl.wrsis.webportal.repository.MonitorLocationRepository;
import com.csmpl.wrsis.webportal.repository.MonitorRustRepository;
import com.csmpl.wrsis.webportal.repository.MonitorVarietyRepository;
import com.csmpl.wrsis.webportal.repository.MultilevelApprovalRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentDetailsRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeverityRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatMasterRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service("mobileService")
public class MobileServiceImpl implements com.csmpl.wrsis.webportal.service.MobileService {

	public static final Logger LOG = LoggerFactory.getLogger(MobileServiceImpl.class);

	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@Autowired
	SiteTypeRepository siteTypeRepository;

	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	GrowthStageRepository growthStageRepository;

	@Autowired
	FungicideMasterRepository fungicideMasterRepository;

	@Autowired
	FungiEffectTypeRepository fungiEffectTypeRepository;

	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;

	@Autowired
	MoistureRepository moistureRepository;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@Autowired
	SoilColorRepository soilColorRepository;

	@Autowired
	WheatMasterRepository wheatMasterRepository;

	@Autowired
	SampleTypeRepository sampleTypeRepository;

	@Autowired
	ReactionTypeRepository reactionTypeRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	RustIncidentRepository rustIncidentRepository;

	@Autowired
	RustIncidentDetailsRepository rustIncidentDetailsRepository;

	@Autowired
	SeverityRepository severityRepository;

	@Autowired
	MobileTokenRepository mtRepo;

	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;
	
	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;

	@Autowired
	MonitorLocationRepository monitorLocationRepository;

	@Autowired
	MonitorVarietyRepository monitorVarietyRepository;

	@Autowired
	MonitorRustRepository monitorRustRepository;

	@Autowired
	MonitorGrowthStageRepository monitorGrowthStageRepository;

	@Autowired
	MonitorFungicideRepository monitorFungicideRepository;

	@Autowired
	@Qualifier("userRepository")
	UserRepository userRepository;

	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;

	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;

	@Autowired
	MobileLoginTrackingRepository mobileLoginTrackingRepository;

	@Autowired
	MultilevelApprovalRepository multilevelApprovalRepository;

	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;

	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;
	
	@Autowired
	MobileAppVersionRepository mAppRepo ;
	

	@Override
	public JSONObject getSurveyMasterData(int userId) {
		JSONObject response = new JSONObject();
		JSONArray surveySiteArray = new JSONArray();
		JSONArray grStgArray = new JSONArray();
		JSONArray fungicideArray = new JSONArray();
		JSONArray whTypeArray = new JSONArray();
		JSONArray fungicideEffectArray = new JSONArray();
		JSONArray otherDiseasesArray = new JSONArray();
		JSONArray anyOtherDiseasesArray = new JSONArray();
		JSONArray moisturesArray = new JSONArray();
		JSONArray rustTypeArray = new JSONArray();
		JSONArray soilColorArray = new JSONArray();
		JSONArray sampleTypeArray = new JSONArray();
		JSONArray reactionTypeArray = new JSONArray();
		JSONArray researchCenterArray = new JSONArray();
		JSONArray surveyorArray = new JSONArray();
		JSONArray varietyArray = new JSONArray();
		JSONArray questionArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		JSONArray seasonArray = new JSONArray();
		JSONArray regionArray = new JSONArray();
		JSONArray zoneArray = new JSONArray();
		JSONArray woredaArray = new JSONArray();
		JSONArray kebelArray = new JSONArray();
		JSONArray monthArray = new JSONArray();
		try {

			// researchCenter

			List<Object[]> researchCenter = researchCenterRepository.fetchResearchCenterByUserId(userId);
			JSONObject researchCenterObj = null;
			if (!researchCenter.isEmpty()) {
				for (Object[] obj : researchCenter) {
					researchCenterObj = new JSONObject();
					researchCenterObj.put("id", String.valueOf(obj[0]));
					researchCenterObj.put("name", String.valueOf(obj[1]));

					researchCenterArray.put(researchCenterObj);
				}
			}else{
				
				List<ResearchCenter> rcList=researchCenterRepository.findActiveResearchCenter();
				for (ResearchCenter obj : rcList) {
					researchCenterObj = new JSONObject();
					researchCenterObj.put("id", obj.getResearchCenterId());
					researchCenterObj.put("name", obj.getResearchCenterName());

					researchCenterArray.put(researchCenterObj);
				}
		    }
			
			response.put("institution", researchCenterArray);
			
			// siteTypes

			List<Object[]> siteTypes = siteTypeRepository.fetchAllSitetypes();
			JSONObject surveySiteObj = null;
			if (!siteTypes.isEmpty()) {
				for (Object[] obj : siteTypes) {
					surveySiteObj = new JSONObject();
					surveySiteObj.put(WrsisPortalConstant.SUR_ID, String.valueOf(obj[0]));
					surveySiteObj.put(WrsisPortalConstant.SUR_NAME, String.valueOf(obj[1]));

					surveySiteArray.put(surveySiteObj);

				}

			}
			
			response.put("surveySite", surveySiteArray);
			// whTypes

			List<Object[]> whTypeList = wheatMasterRepository.fetchAllWheattypes();
			JSONObject whTypeListObj = null;
			if (!whTypeList.isEmpty()) {
				for (Object[] obj : whTypeList) {
					whTypeListObj = new JSONObject();
					whTypeListObj.put("wtId", String.valueOf(obj[0]));
					whTypeListObj.put("wtName", String.valueOf(obj[1]));

					whTypeArray.put(whTypeListObj);

				}

			}
			
			response.put("wheatType", whTypeArray);
			// grStg

			List<Object[]> grStgList = growthStageRepository.fetchAllGrowth();
			JSONObject grStgListObj = null;
			if (!grStgList.isEmpty()) {
				for (Object[] obj : grStgList) {
					grStgListObj = new JSONObject();
					grStgListObj.put("gsId", String.valueOf(obj[0]));
					grStgListObj.put(WrsisPortalConstant.GS_NAME, String.valueOf(obj[1]));

					grStgArray.put(grStgListObj);

				}

			}
			
			response.put("growthStage", grStgArray);
			// fungicide

			List<Object[]> fungicideList = fungicideMasterRepository.fetchAllFungicides();
			JSONObject fungicideObj = null;
			if (!fungicideList.isEmpty()) {
				for (Object[] obj : fungicideList) {
					fungicideObj = new JSONObject();
					fungicideObj.put("fngId", String.valueOf(obj[0]));
					fungicideObj.put("fngName", String.valueOf(obj[1]));

					fungicideArray.put(fungicideObj);

				}

			}
			
			response.put("fongicideUsed", fungicideArray);

			// fungicide effect

			List<Object[]> fungicideEffect = fungiEffectTypeRepository.fetchAllFungiEffectTypes();
			JSONObject fungicideEffectObj = null;
			if (!fungicideEffect.isEmpty()) {
				for (Object[] obj : fungicideEffect) {
					fungicideEffectObj = new JSONObject();
					fungicideEffectObj.put("fngEffId", String.valueOf(obj[0]));
					fungicideEffectObj.put("fngEffName", String.valueOf(obj[1]));

					fungicideEffectArray.put(fungicideEffectObj);

				}

			}
			
			response.put("fongicideEffect", fungicideEffectArray);
			// other disease

			List<Object[]> diseaseList = diseaseMasterRepository.fetchAllDisease();
			JSONObject otherDiseases = null;
			if (!diseaseList.isEmpty()) {
				for (Object[] obj : diseaseList) {
					otherDiseases = new JSONObject();
					otherDiseases.put("disId", String.valueOf(obj[0]));
					otherDiseases.put("disName", String.valueOf(obj[1]));
					otherDiseases.put("severitymin", String.valueOf(obj[2]));
					otherDiseases.put("severitymax", String.valueOf(obj[3]));
					otherDiseases.put("severityRequired", obj[4]);
					otherDiseasesArray.put(otherDiseases);

				}

			}
			
			response.put("otherDiseases", otherDiseasesArray);
			// any other disease

			
			response.put("anyOtherDiseases", anyOtherDiseasesArray);
			// moisture

			List<Object[]> moisturesList = moistureRepository.fetchAllMoistures();
			JSONObject moisturesListObj = null;
			if (!moisturesList.isEmpty()) {
				for (Object[] obj : moisturesList) {
					moisturesListObj = new JSONObject();
					moisturesListObj.put("mostId", String.valueOf(obj[0]));
					moisturesListObj.put("mostName", String.valueOf(obj[1]));

					moisturesArray.put(moisturesListObj);

				}

			}
			
			response.put("moistures", moisturesArray);
			
			// rust types

			List<Object[]> rustTypeList = typeOfRustRepository.fetchAllRustType();
			JSONObject rustTypeListObj = null;
			if (!rustTypeList.isEmpty()) {
				for (Object[] obj : rustTypeList) {
					rustTypeListObj = new JSONObject();
					rustTypeListObj.put(WrsisPortalConstant.RUSTID, String.valueOf(obj[0]));
					rustTypeListObj.put(WrsisPortalConstant.RUSTNAME, String.valueOf(obj[1]));

					rustTypeArray.put(rustTypeListObj);

				}

			}
			
			response.put("rustTypes", rustTypeArray);
			
			// soil colors

			List<Object[]> soilColorList = soilColorRepository.fetchAllSoilColors();
			JSONObject soilColorObj = null;
			if (!soilColorList.isEmpty()) {
				for (Object[] obj : soilColorList) {
					soilColorObj = new JSONObject();
					soilColorObj.put("colId", String.valueOf(obj[0]));
					soilColorObj.put("colName", String.valueOf(obj[1]));

					soilColorArray.put(soilColorObj);

				}

			}
			
			response.put("soilColour", soilColorArray);
			

			// sample types

			List<Object[]> sampleTypeList = sampleTypeRepository.fetchAllSampletypes();
			JSONObject sampleTypeObj = null;
			if (!sampleTypeList.isEmpty()) {
				for (Object[] obj : sampleTypeList) {
					sampleTypeObj = new JSONObject();
					sampleTypeObj.put("scId", String.valueOf(obj[0]));
					sampleTypeObj.put("scName", String.valueOf(obj[1]));
					sampleTypeObj.put(WrsisPortalConstant.RUSTID, String.valueOf(obj[2]));

					sampleTypeArray.put(sampleTypeObj);

				}

			}

			
			response.put("sampleCollected", sampleTypeArray);
			
			// reaction types

			List<Object[]> reactionTypeList = reactionTypeRepository.fetchAllReactionTypes();
			JSONObject reactionTypeObj = null;
			if (!reactionTypeList.isEmpty()) {
				for (Object[] obj : reactionTypeList) {
					reactionTypeObj = new JSONObject();
					reactionTypeObj.put("prId", String.valueOf(obj[0]));
					reactionTypeObj.put("prName", String.valueOf(obj[1]));

					reactionTypeArray.put(reactionTypeObj);

				}

			}
			
			response.put("plantReaction", reactionTypeArray);
			

			// Surveyor Details

			List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
			Integer researchCenterId = (obj != null && !obj.isEmpty())
					? (Integer) (Integer.valueOf(String.valueOf(((Object[]) obj.get(0))[0])))
					: null;
			if (researchCenterId != null) {
				List<Object[]> surveyors = commonService.getAllSurveyor(researchCenterId);
				JSONObject surveyorsObj = null;
				if (!surveyors.isEmpty()) {
					for (Object[] surobj : surveyors) {
						surveyorsObj = new JSONObject();
						surveyorsObj.put(WrsisPortalConstant.SUR_ID, String.valueOf(surobj[0]));
						surveyorsObj.put(WrsisPortalConstant.SUR_NAME, String.valueOf(surobj[1]));

						surveyorArray.put(surveyorsObj);

					}

				}
			}else {
				//If user not tagged to any rc
				List<Object[]> user =userRepository.getUserDetailsByUserId(userId);
				JSONObject surveyorsObj = null;
				for (Object[] uobj : user) {
					surveyorsObj = new JSONObject();
					surveyorsObj.put(WrsisPortalConstant.SUR_ID, String.valueOf(uobj[0]));
					surveyorsObj.put(WrsisPortalConstant.SUR_NAME, String.valueOf(uobj[1]));

					surveyorArray.put(surveyorsObj);

				}
	
			}
			
			response.put("surveyorDetails", surveyorArray);
			
			// variety

			List<Object[]> varietyList = varietyTypeRepository.fetchAllVarietyType();
			JSONObject varietyObj = null;
			if (!varietyList.isEmpty()) {
				for (Object[] variobj : varietyList) {
					varietyObj = new JSONObject();
					varietyObj.put("varId", String.valueOf(variobj[0]));
					varietyObj.put("varName", String.valueOf(variobj[1]));
					varietyArray.put(varietyObj);

				}

			}
			
			response.put(WrsisPortalConstant.VARIETY_DETAILS, varietyArray);
			

			// season
		List<Object[]> seasionList = seasionMasterRepository.fetchAllSeasionsWiseMonth();
			JSONObject seasionObj = null;
			if (!seasionList.isEmpty()) {
				for (Object[] seaobj : seasionList) {
					seasionObj = new JSONObject();
					seasionObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(seaobj[0]));
					seasionObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(seaobj[1]));
					seasonArray.put(seasionObj);

				}
			}
			
			response.put("seasonDetails", seasonArray);
			

			// month
			List<Object[]> monthList = seasionMasterRepository.getMonthByseasonId();
			JSONObject monthObj = null;
			if (!monthList.isEmpty()) {
				for (Object[] mnthobj : monthList) {
					monthObj = new JSONObject();
					monthObj.put("monthId", String.valueOf(mnthobj[0]));
					monthObj.put("monthName", String.valueOf(mnthobj[1]));
					monthObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(mnthobj[2]));
					monthArray.put(monthObj);

				}
				
				
			}
			
			response.put("monthData", monthArray);

			// Question
			List<Object[]> questionList = questionRepository.fetchAllIncidentQuestion();
			JSONObject questioObj = null;
			JSONObject optionObj = null;
			if (!questionList.isEmpty()) {
				for (Object[] questobj : questionList) {
					questioObj = new JSONObject();
					questioObj.put(WrsisPortalConstant.QUE_ID, String.valueOf(questobj[0]));
					questioObj.put(WrsisPortalConstant.QUE_NAME, String.valueOf(questobj[1]));
					questioObj.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(questobj[2]));
					int questionId = Integer.valueOf(String.valueOf(questobj[0]));
					List<Object[]> optionList = questionOptionRepository.findOptionByQustionId(questionId);
					if (!optionList.isEmpty()) {
						for (Object[] optobj : optionList) {
							optionObj = new JSONObject();
							optionObj.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj[0]));
							optionObj.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj[1]));
							optionArray.put(optionObj);
							
						}
						questioObj.put(WrsisPortalConstant.OPTION_DETAILS, optionArray);
						optionArray = new JSONArray();// modify due to option value duplicate
					}

					questionArray.put(questioObj);

				}

			}
			
			response.put(WrsisPortalConstant.QUESTION_DETAILS, questionArray);
			
			// Region
			JSONObject regionObj = null;
			if(researchCenterId!=null) {
				List<Object[]> regionList = demographyRepository.fetchAllRegions(researchCenterId);
				if (!regionList.isEmpty()) {
					for (Object[] regobj : regionList) {
						regionObj = new JSONObject();
						regionObj.put("rId", String.valueOf(regobj[0]));
						regionObj.put("rName", String.valueOf(regobj[1]));
						regionObj.put("cId", String.valueOf(regobj[2]));
						regionArray.put(regionObj);

					}
				}
			}else {
				
				List<DemographicEntity> allRegions = demographyRepository.findByLevelId(2);
				for (DemographicEntity regobj : allRegions) {
					regionObj = new JSONObject();
					regionObj.put("rId", regobj.getDemographyId());
					regionObj.put("rName", regobj.getDemographyName());
					regionObj.put("cId", regobj.getParentId());
					regionArray.put(regionObj);

				}
				
			}

			
			response.put("regionDetails", regionArray);
			
			// Zone
			JSONObject zoneObj = null;
			if(researchCenterId!=null) {
				List<Object[]> zoneList = demographyRepository.fetchAllZones(researchCenterId);
				
				if (!zoneList.isEmpty()) {
					for (Object[] zonobj : zoneList) {
						zoneObj = new JSONObject();
						zoneObj.put("zId", String.valueOf(zonobj[0]));
						zoneObj.put("zName", String.valueOf(zonobj[1]));
						zoneObj.put("rId", String.valueOf(zonobj[2]));
						zoneArray.put(zoneObj);

					}

				}
			}
			else {
				
				List<DemographicEntity> allZones = demographyRepository.findByLevelId(3);
				for (DemographicEntity zonobj : allZones) {
					zoneObj = new JSONObject();
					zoneObj.put("zId", zonobj.getDemographyId());
					zoneObj.put("zName", zonobj.getDemographyName());
					zoneObj.put("rId", zonobj.getParentId());
					zoneArray.put(zoneObj);

				}
			}
			
			response.put("zoneDetails", zoneArray);
			

			// Woreda
			JSONObject woredaObj = null;
			if(researchCenterId!=null) {
				List<Object[]> woredaList = demographyRepository.fetchAllWoredas(researchCenterId);
				
				if (!woredaList.isEmpty()) {
					for (Object[] worobj : woredaList) {
						woredaObj = new JSONObject();
						woredaObj.put("wId", String.valueOf(worobj[0]));
						woredaObj.put("wName", String.valueOf(worobj[1]));
						woredaObj.put("zId", String.valueOf(worobj[2]));
						woredaArray.put(woredaObj);

					}

				}
				
			}
			else {
				
				List<DemographicEntity> allWoredas = demographyRepository.findByLevelId(4);
				for (DemographicEntity worobj : allWoredas) {
					woredaObj = new JSONObject();
					woredaObj.put("wId", worobj.getDemographyId());
					woredaObj.put("wName", worobj.getDemographyName());
					woredaObj.put("zId", worobj.getParentId());
					woredaArray.put(woredaObj);

				}
			}
			
			response.put("woredaDetails", woredaArray);
			

			// Kebel
			JSONObject kebelObj = null;
			if(researchCenterId!=null) {
				List<Object[]> kebelList = demographyRepository.fetchAllKebels(researchCenterId);
				
				if (!kebelList.isEmpty()) {
					for (Object[] kelobj : kebelList) {
						kebelObj = new JSONObject();
						kebelObj.put("kId", String.valueOf(kelobj[0]));
						kebelObj.put("kName", String.valueOf(kelobj[1]));
						kebelObj.put("wId", String.valueOf(kelobj[2]));
						kebelArray.put(kebelObj);

					}

				}
			}else {
				
				List<DemographicEntity> allKebels = demographyRepository.findByLevelId(5);
				for (DemographicEntity kelobj : allKebels) {
					kebelObj = new JSONObject();
					kebelObj.put("kId", kelobj.getDemographyId());
					kebelObj.put("kName", kelobj.getDemographyName());
					kebelObj.put("wId", kelobj.getParentId());
					kebelArray.put(kebelObj);

				}
			}

			
			response.put("kebelDetails", kebelArray);
			
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getSurveyMasterData():" + e);
		}
		return response;
	}

	@Override
	public JSONObject getQuestionMasterData() {
		JSONObject response = new JSONObject();
		JSONArray questionArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		String incidtlid = "";
		try {
			List<Object[]> questionList = questionRepository.fetchAllIncidentQuestion();
			JSONObject questioObj = null;
			JSONObject optionObj = null;
			if (!questionList.isEmpty()) {
				for (Object[] questobj : questionList) {
					questioObj = new JSONObject();
					questioObj.put(WrsisPortalConstant.QUE_ID, String.valueOf(questobj[0]));
					questioObj.put(WrsisPortalConstant.QUE_NAME, String.valueOf(questobj[1]));
					questioObj.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(questobj[2]));
					questioObj.put(WrsisPortalConstant.INCIDTL_ID, incidtlid);
					int questionId = Integer.parseInt(String.valueOf(questobj[0]));
					List<Object[]> optionList = questionOptionRepository.findOptionByQustionId(questionId);
					if (!optionList.isEmpty()) {
						optionArray = new JSONArray();
						for (Object[] optobj : optionList) {
							optionObj = new JSONObject();
							optionObj.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj[1]));
							optionObj.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj[0]));
							optionArray.put(optionObj);

						}
						questioObj.put(WrsisPortalConstant.OPTION_DETAILS, optionArray);
					}

					questionArray.put(questioObj);

				}

			}
			response.put(WrsisPortalConstant.QUESTION_DETAILS, questionArray);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getQuestionMasterData():" + e);
		}
		return response;
	}

	@Override
	public JSONObject getforGISMasterData() {
		JSONObject response = new JSONObject();
		JSONArray rustTypeArray = new JSONArray();
		JSONArray grStgArray = new JSONArray();
		JSONArray severityArray = new JSONArray();
		JSONArray seasonArray = new JSONArray();
		JSONArray yearSeasonArray = new JSONArray();
		try {

			// year

			JSONArray yearJsa = DateUtil.getYearList();
			response.put("yearList", yearJsa);
			// rust type

			List<Object[]> rustTypeList = typeOfRustRepository.fetchAllRustType();
			JSONObject rustTypeListObj = null;
			if (!rustTypeList.isEmpty()) {
				for (Object[] obj : rustTypeList) {
					rustTypeListObj = new JSONObject();
					rustTypeListObj.put(WrsisPortalConstant.RUSTID, String.valueOf(obj[0]));
					rustTypeListObj.put(WrsisPortalConstant.RUSTNAME, String.valueOf(obj[1]));

					rustTypeArray.put(rustTypeListObj);

				}

			}

			// grStg

			List<Object[]> grStgList = growthStageRepository.fetchAllGrowth();
			JSONObject grStgListObj = null;
			if (!grStgList.isEmpty()) {
				for (Object[] obj : grStgList) {
					grStgListObj = new JSONObject();
					grStgListObj.put("gsId", String.valueOf(obj[0]));
					grStgListObj.put(WrsisPortalConstant.GS_NAME, String.valueOf(obj[1]));

					grStgArray.put(grStgListObj);

				}

			}

			// severity

			List<Object[]> severityList = severityRepository.fetchAllSeverity();
			JSONObject severityListObj = null;
			if (!severityList.isEmpty()) {
				for (Object[] obj : severityList) {
					severityListObj = new JSONObject();
					severityListObj.put("seveId", String.valueOf(obj[0]));
					severityListObj.put("seveName", String.valueOf(obj[1]));

					severityArray.put(severityListObj);

				}

			}

			// season

			List<Object[]> seasionList = seasionMasterRepository.fetchAllSeasionsWiseMonth();
			JSONObject seasionObj = null;

			if (!seasionList.isEmpty()) {
				for (Object[] seaobj : seasionList) {
					seasionObj = new JSONObject();
					seasionObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(seaobj[0]));
					seasionObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(seaobj[1]));
					seasonArray.put(seasionObj);

				}

			}

			List<Object[]> yearseasondata = surveyDetailsRepository.fetchCurrentYearWiseSeason();
			JSONObject seasionYObj = null;

			if (!yearseasondata.isEmpty()) {
				for (Object[] yseaobj : yearseasondata) {
					seasionYObj = new JSONObject();
					
					seasionYObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(yseaobj[0]));
					seasionYObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(yseaobj[1]));
					yearSeasonArray.put(seasionYObj);

				}

			}
			response.put("yearSeasonData", yearSeasonArray);

			response.put(WrsisPortalConstant.SEASON_DATA, seasonArray);
			response.put("diseaseData", rustTypeArray);
			response.put("growthData", grStgArray);
			response.put("severityData", severityArray);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getforGisMasterData():" + e);
		}
		return response;
	}

	@Transactional
	@Override
	public String saveRustIncident(MobileReqBean loginReqDto) {
		
		String result = "";
		RustIncidentEntity incident = null;
		RustIncidentDetailsEntity incidentDtl = null;
		Integer count = 0;
		try {

			if (loginReqDto.getIncidentId() == null ) {
				count = rustIncidentRepository.getRustIncidentCount(loginReqDto.getUserId(),
						loginReqDto.getDemographyId(), loginReqDto.getSeasonId(),
						getYearFromDate(loginReqDto.getDate()));
				if (count == 0) {
					incident = new RustIncidentEntity();
					incident.setUserId(loginReqDto.getUserId());
					incident.setRustDate(DateUtil.StringToDate(loginReqDto.getDate(), WrsisPortalConstant.DATE_FORMAT_DDMMYYYY));

					DemographicEntity d = new DemographicEntity();
					d.setDemographyId(loginReqDto.getDemographyId());
					incident.setDemographyId(d);
					SeasionMasterEntity s = new SeasionMasterEntity();
					s.setIntSeasoonId(loginReqDto.getSeasonId());
					incident.setSeasonId(s);
					incident.setYear(getYearFromDate(loginReqDto.getDate()));
					incident.setLongitude(loginReqDto.getLongitude());
					incident.setLatitude(loginReqDto.getLatitude());
					incident.setCreatedOn(new Timestamp(new Date().getTime()));
					incident.setCreatedBy(loginReqDto.getUserId());
					incident.setBitStatus(false);

					incident = rustIncidentRepository.saveAndFlush(incident);
					// store gometric value in data table
					Integer incidentId = incident.getIncidentId();
					rustIncidentRepository.updateGeoLocation(incidentId);

					for (QueAnsDetails queDto : loginReqDto.getQueAnsDetails()) {

						incidentDtl = new RustIncidentDetailsEntity();
						incidentDtl.setIncidentId(incident.getIncidentId());
						Question que = new Question();
						que.setQustionId(queDto.getQueId());
						incidentDtl.setQuestionId(que);
						incidentDtl.setOptionValue(queDto.getOptId());
						incidentDtl.setBitStatus(false);

						rustIncidentDetailsRepository.saveAndFlush(incidentDtl);
					}
					result = WrsisPortalConstant.SUCCESS;
				} else {
					result = "exit";
				}
			} else {
				incident = rustIncidentRepository.findByIncidentId(loginReqDto.getIncidentId());
				if (incident != null) {
					incident.setUserId(loginReqDto.getUserId());
					incident.setRustDate(DateUtil.StringToDate(loginReqDto.getDate(), WrsisPortalConstant.DATE_FORMAT_DDMMYYYY));

					DemographicEntity d = new DemographicEntity();
					d.setDemographyId(loginReqDto.getDemographyId());
					incident.setDemographyId(d);
					SeasionMasterEntity s = new SeasionMasterEntity();
					s.setIntSeasoonId(loginReqDto.getSeasonId());
					incident.setSeasonId(s);
					incident.setYear(getYearFromDate(loginReqDto.getDate()));
					incident.setLongitude(loginReqDto.getLongitude());
					incident.setLatitude(loginReqDto.getLatitude());
					incident.setUpdatedOn(new Timestamp(new Date().getTime()));
					incident.setUpdatedBy(loginReqDto.getUserId());
					incident.setBitStatus(false);

					rustIncidentRepository.saveAndFlush(incident);
					// store gometric value in data table
					rustIncidentRepository.updateGeoLocation(loginReqDto.getIncidentId());

					for (QueAnsDetails queDto : loginReqDto.getQueAnsDetails()) {
						if (queDto.getIncidtlid() != 0) {
							incidentDtl = rustIncidentDetailsRepository.findByIncidentDtlId(queDto.getIncidtlid());
							if (incidentDtl != null) {
								incidentDtl.setIncidentId(incident.getIncidentId());
								Question que = new Question();
								que.setQustionId(queDto.getQueId());
								incidentDtl.setQuestionId(que);
								incidentDtl.setOptionValue(queDto.getOptId());
								incidentDtl.setBitStatus(false);

								rustIncidentDetailsRepository.saveAndFlush(incidentDtl);
							}
						} else {
							incidentDtl = new RustIncidentDetailsEntity();
							incidentDtl.setIncidentId(incident.getIncidentId());
							Question que = new Question();
							que.setQustionId(queDto.getQueId());
							incidentDtl.setQuestionId(que);
							incidentDtl.setOptionValue(queDto.getOptId());
							incidentDtl.setBitStatus(false);

							rustIncidentDetailsRepository.saveAndFlush(incidentDtl);
						}
					}
					result = WrsisPortalConstant.SUCCESS;
				}
			}
		} catch (Exception e) {
			result = "fail";
			LOG.error("MobileServiceImpl::saverustIncident():" + e);
		}
		return result;
	}

	public String getYearFromDate(String date) {
		String year = "";
		try {

			String[] dateParts = date.split("-");
			
			
			year = dateParts[2];

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getYearFromDate():" + e);
		}
		return year;
	}

	@Override
	public JSONObject getSurveyDataforGIS(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray surDataforGISArray = new JSONArray();
		
		try {

			loginReqDto.setDisease((loginReqDto.getDisease() == null) ? 0 : loginReqDto.getDisease());
			loginReqDto.setYear((loginReqDto.getYear() == null || loginReqDto.getYear().trim().equals("")) ? ""
					: loginReqDto.getYear());
			loginReqDto.setGrowth((loginReqDto.getGrowth() == null) ? 0 : loginReqDto.getGrowth());
			loginReqDto
					.setSeverity((loginReqDto.getSeverity() == null || loginReqDto.getSeverity().trim().equals("")) ? ""
							: loginReqDto.getSeverity());
			loginReqDto.setSeasonId((loginReqDto.getSeasonId() == null) ? 0 : loginReqDto.getSeasonId());

			List<Object[]> surveyGISList = surveyDetailsRepository.getSurveyDataforGIS(loginReqDto.getYear(),
					loginReqDto.getDisease(), loginReqDto.getGrowth(), loginReqDto.getSeverity(),
					loginReqDto.getSeasonId());
			
			JSONObject surveyGISObj = null;
			if (!surveyGISList.isEmpty()) {
				for (Object[] obj : surveyGISList) {
					surveyGISObj = new JSONObject();
					surveyGISObj.put("lat", String.valueOf(obj[0]));
					surveyGISObj.put("long", String.valueOf(obj[1]));
					surveyGISObj.put("disease", String.valueOf(obj[2]));
					surveyGISObj.put("growth", String.valueOf(obj[3]));
					surveyGISObj.put("rush_per", String.valueOf(obj[4]));
					surveyGISObj.put(WrsisPortalConstant.SEVERITY_C, String.valueOf(obj[5]));

					surDataforGISArray.put(surveyGISObj);

				}

			}

			response.put("responseData", surDataforGISArray);

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getSurveyDataforGis():" + e);
		}

		return response;
	}

	@Override
	public JSONObject viewWheatRustSurvey(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray viewSurveyArray = new JSONArray();
		try {

			// view survey data

			List<Object[]> surveyDetails = surveyDetailsRepository.searchSurveyDataforMobile(0, 0, 0, 0, "", "", 0, "",
					0, loginReqDto.getUserId());
			JSONObject surveyListObj = null;
			if (!surveyDetails.isEmpty()) {
				for (Object[] obj : surveyDetails) {
					surveyListObj = new JSONObject();
					surveyListObj.put("zone", String.valueOf((obj[5] != null) ? (String) obj[5] : ""));
					surveyListObj.put(WrsisPortalConstant.REGION1, String.valueOf((obj[4] != null) ? (String) obj[4] : ""));
					surveyListObj.put(WrsisPortalConstant.WOREDA1, String.valueOf((obj[6] != null) ? (String) obj[6] : ""));
					surveyListObj.put(WrsisPortalConstant.KEBELE1, String.valueOf((obj[7] != null) ? (String) obj[7] : ""));
					surveyListObj.put("rustType", String.valueOf((obj[8] != null) ? (String) obj[8] : ""));
					surveyListObj.put("surveyDate", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
					surveyListObj.put("surveyId",
							String.valueOf((obj[0] != null) ? Integer.valueOf(String.valueOf(obj[0])) : 0));
					surveyListObj.put("surveyNumber", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
					
					
					surveyListObj.put("wheatType", String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
					
					
					surveyListObj.put("latLong", String.valueOf((obj[9] != null) ? (String) obj[9] : ""));
					surveyListObj.put("remarks", String.valueOf((obj[10] != null) ? (String) obj[10] : ""));
					surveyListObj.put("lat", String.valueOf((obj[11] != null) ? (String) obj[11] : ""));
					surveyListObj.put("long", String.valueOf((obj[12] != null) ? (String) obj[12] : ""));
					
					

					viewSurveyArray.put(surveyListObj);

				}

			}

			response.put("surveyDataDetails", viewSurveyArray);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewWheatRustSurvey():" + e);
		}
		return response;
	}

	@Autowired
	UserResearchCenterMappingRepository userResearchCenterMappingRepository;

	@Override
	public JSONObject viewRaceAnalysis(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray viewRaceAnalysisArray = new JSONArray();
		Integer userId = loginReqDto.getUserId();
		List<Object[]> roleList = userRepository.getRoleByUserId(loginReqDto.getUserId());
		loginReqDto.setResearchCenterId(0);
		
		if (!roleList.isEmpty()) {
			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.SURVEYOR)) {
					loginReqDto.setUserId(userId);
				}
			}

			for (Object[] optobj : roleList) {
				if (String.valueOf(optobj[1]).equals(WrsisPortalConstant.PATHOLOGIST)) {
					loginReqDto.setUserId(0);

					// rc id

					UserResearchCenterMapping rcmapping = userResearchCenterMappingRepository.findByUserId(userId);
					Integer rcid = rcmapping.getRecearchCenterId();
					loginReqDto.setResearchCenterId(rcid);

				}

			}

		}

		try {

			List<Object[]> surveyDetails = sampleLabTagDetailsRepository.viewRaceAnalysisMolile(loginReqDto.getUserId(),
					loginReqDto.getResearchCenterId());
			JSONObject raceListObj = null;
			if (!surveyDetails.isEmpty()) {
				for (Object[] obj : surveyDetails) {
					raceListObj = new JSONObject();
					raceListObj.put(WrsisPortalConstant.SURVEYNO, String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
					raceListObj.put("sampleNo", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
					raceListObj.put("rustType", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
					raceListObj.put("surveyDate", String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
					raceListObj.put("incolutationdate", String.valueOf((obj[4] != null) ? (String) obj[4] : ""));
					raceListObj.put(WrsisPortalConstant.REGION1, String.valueOf((obj[5] != null) ? (String) obj[5] : ""));
					raceListObj.put("zone", String.valueOf((obj[6] != null) ? (String) obj[6] : ""));
					raceListObj.put(WrsisPortalConstant.WOREDA1, String.valueOf((obj[7] != null) ? (String) obj[7] : ""));
					raceListObj.put(WrsisPortalConstant.KEBELE1, String.valueOf((obj[8] != null) ? (String) obj[8] : ""));
					raceListObj.put(WrsisPortalConstant.SEVERITY_C, String.valueOf((obj[10] != null) ? (String) obj[10] : ""));
					raceListObj.put(WrsisPortalConstant.STATUS, String.valueOf((obj[11] != null) ? (String) obj[11] : ""));
					raceListObj.put("remark", String.valueOf((obj[12] != null) ? (String) obj[12] : ""));
					raceListObj.put("raceAnalysisId", String.valueOf((obj[17] != null) ? (String) obj[17] : ""));
					raceListObj.put("longitude", String.valueOf((obj[18] != null) ? (String) obj[18] : ""));
					raceListObj.put("latitude", String.valueOf((obj[19] != null) ? (String) obj[19] : ""));

					viewRaceAnalysisArray.put(raceListObj);

				}

			}

			response.put("raceAnalysisDetails", viewRaceAnalysisArray);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRaceAnalysis():" + e);
		}
		return response;
	}

	@Override
	public JSONObject viewRecommendationAndAdvisory(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray recommendationArray = new JSONArray();
		JSONArray recommendationArrayFinal = new JSONArray();
		JSONArray advisoryArray = new JSONArray();
		try {
			if ("VR".equalsIgnoreCase(loginReqDto.getAlertType())
					|| loginReqDto.getAlertType().equalsIgnoreCase("VR")) {
				List<Object[]> recommendationDetails = sampleLabTagDetailsRepository.viewRecommendation("", "", "");
				JSONObject recommListObj = null;
				if (!recommendationDetails.isEmpty()) {
					for (Object[] obj : recommendationDetails) {
						recommListObj = new JSONObject();
						recommListObj.put(WrsisPortalConstant.RECOM_NO, String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
						recommListObj.put("recomDate", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
						recommListObj.put("recomFile", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
						recommListObj.put(WrsisPortalConstant.REGION1, String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
						recommListObj.put("zone", String.valueOf((obj[4] != null) ? (String) obj[4] : ""));
						recommListObj.put(WrsisPortalConstant.WOREDA1, String.valueOf((obj[5] != null) ? (String) obj[5] : ""));
						recommListObj.put(WrsisPortalConstant.KEBELE1, String.valueOf((obj[6] != null) ? (String) obj[6] : ""));

						recommendationArray.put(recommListObj);

					}

					for (int i = recommendationArray.length() - 1; i >= 0; i--) {
						recommendationArrayFinal.put(recommendationArray.getJSONObject(i));
					}

				}

				response.put("recommendationDetails", recommendationArrayFinal);
			}
			if ("VA".equalsIgnoreCase(loginReqDto.getAlertType())
					|| loginReqDto.getAlertType().equalsIgnoreCase("VA")) {
				List<Object[]> recommendationDetails = sampleLabTagDetailsRepository.viewAdvisory("", "", "");
				JSONObject advisoryListObj = null;
				if (!recommendationDetails.isEmpty()) {
					for (Object[] obj : recommendationDetails) {
						advisoryListObj = new JSONObject();
						advisoryListObj.put("advisoryNo", String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
						advisoryListObj.put("advisoryDate", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
						advisoryListObj.put("advisoryFile", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));

						advisoryArray.put(advisoryListObj);

					}

				}

				response.put("advisoryDetails", advisoryArray);
			}

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRecomendationAndAdvisory():" + e);
		}
		return response;
	}

	@Override
	public JSONObject getMasterforRustIncident(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray seasonArray = new JSONArray();
	
		JSONArray monthArray = new JSONArray();
		JSONArray questionArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		try {

			// season

			List<Object[]> seasionList = seasionMasterRepository.fetchAllSeasionsWiseMonth();
			JSONObject seasionObj = null;

			if (!seasionList.isEmpty()) {
				for (Object[] seaobj : seasionList) {
					seasionObj = new JSONObject();
					seasionObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(seaobj[0]));
					seasionObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(seaobj[1]));
					seasonArray.put(seasionObj);

				}

			}

			// month
			List<Object[]> monthList = seasionMasterRepository.getMonthByseasonId();
			JSONObject monthObj = null;
			if (!monthList.isEmpty()) {
				for (Object[] mnthobj : monthList) {
					monthObj = new JSONObject();
					monthObj.put("monthId", String.valueOf(mnthobj[0]));
					monthObj.put("monthName", String.valueOf(mnthobj[1]));
					monthObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(mnthobj[2]));
					monthArray.put(monthObj);

				}
			}

			// user wise demography
			

			List<Short> demoList = seasionMasterRepository.fetchAllUserWiseDemography(loginReqDto.getUserId());
			JSONObject demoObj = null;
			if (!demoList.isEmpty()) {
				for (Short seaobj : demoList) {
					demoObj = new JSONObject();
					demoObj.put("levelId", String.valueOf(seaobj));

				}

			}

			

			String jsonStr = seasionMasterRepository.getdemographydata(loginReqDto.getUserId());
			JSONArray json = new JSONArray(jsonStr);
			JSONArray region = new JSONArray();
			JSONArray zone = new JSONArray();
			JSONArray woreda = new JSONArray();
			JSONArray kebel = new JSONArray();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(WrsisPortalConstant.REGION_ID, json.getJSONObject(i).getString("regionid"));
				jsonObj.put("regionName", json.getJSONObject(i).getString("regionname"));
				region.put(jsonObj);
				JSONArray zoneA = json.getJSONObject(i).getJSONArray("zone");
				zone: for (int j = 0; j < zoneA.length(); j++) {
					JSONObject jsonZo = new JSONObject();
					jsonZo.put(WrsisPortalConstant.REGION_ID, json.getJSONObject(i).getString("regionid"));
					jsonZo.put("zoneId", zoneA.getJSONObject(j).getString("zoneid"));
					jsonZo.put("zoneName", zoneA.getJSONObject(j).getString("zonename"));
					zone.put(jsonZo);

					JSONArray woredaA = new JSONArray();
					try {
						woredaA = zoneA.getJSONObject(j).getJSONArray(WrsisPortalConstant.WOREDA1);
					} catch (Exception e) {
						LOG.error("MobileServiceImpl::getMasterForrustIncident():" + e);
						continue zone;
					}
					woreda: for (int k = 0; k < woredaA.length(); k++) {
						JSONObject jsonWo = new JSONObject();
						jsonWo.put("zoneId", zoneA.getJSONObject(j).getString("zoneid"));
						jsonWo.put("woredaId", woredaA.getJSONObject(k).getString("woredaid"));
						jsonWo.put("woredaName", woredaA.getJSONObject(k).getString("woredaname"));
						woreda.put(jsonWo);

						JSONArray kebeleA = new JSONArray();
						try {
							kebeleA = woredaA.getJSONObject(k).getJSONArray(WrsisPortalConstant.KEBELE1);
						} catch (Exception e) {
							LOG.error("MobileServiceImpl::getMasterForrustIncident():" + e);

							continue woreda;
						}
						for (int l = 0; l < kebeleA.length(); l++) {
							JSONObject jsonKe = new JSONObject();
							jsonKe.put("woredaId", woredaA.getJSONObject(k).getString("woredaid"));
							jsonKe.put("kebelId", kebeleA.getJSONObject(l).getString("kebelid"));
							jsonKe.put("kebelName", kebeleA.getJSONObject(l).getString("kebelaname"));
							kebel.put(jsonKe);

						}

					}

				}

			}
			
			
			// Question
			List<Object[]> questionList = questionRepository.fetchAllIncidentQuestion();
			JSONObject questioObj = null;
			JSONObject optionObj = null;
			if (!questionList.isEmpty()) {
				for (Object[] questobj : questionList) {
					questioObj = new JSONObject();
					questioObj.put(WrsisPortalConstant.QUE_ID, String.valueOf(questobj[0]));
					questioObj.put(WrsisPortalConstant.QUE_NAME, String.valueOf(questobj[1]));
					questioObj.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(questobj[2]));
					int questionId = Integer.valueOf(String.valueOf(questobj[0]));
					List<Object[]> optionList = questionOptionRepository.findOptionByQustionId(questionId);
					if (!optionList.isEmpty()) {
						for (Object[] optobj : optionList) {
							optionObj = new JSONObject();
							optionObj.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj[1]));
							optionObj.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj[0]));
							optionArray.put(optionObj);
							
						}
						questioObj.put(WrsisPortalConstant.OPTION_DETAILS, optionArray);
						optionArray = new JSONArray();// modify due to option value duplicate
					}

					questionArray.put(questioObj);

				}

			}
						
			response.put(WrsisPortalConstant.QUESTION_DETAILS, questionArray);
			response.put(WrsisPortalConstant.SEASON_DATA, seasonArray);
			response.put("incidentData", demoObj);
			response.put("kebeleData", kebel);
			response.put("woredaData", woreda);
			response.put("zoneData", zone);
			response.put("regionData", region);
			response.put("monthData", monthArray);

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getMasterForrustIncident():" + e);

		}
		return response;
	}

	@Transactional
	@Override
	public String saveSurveyImplementation(String encodeJSON, Integer collectMode, Integer monitorId) {
		String result = null;
		String moniRefNo = "";
		Integer count = 0;
		boolean insert = true;
		MonitorLocationEntity locationDetails = null;
		MonitorVarietyEntity varietyDetails = null;
		MonitorRustEntity rustDetails = null;
		MonitorGrowthStageEntity growthDetails = null;
		MonitorFungicideEntity fungicideDetails = null;
		MonitorDetailsEntity monitorDetails = null;
		try {
			
			Integer processId = Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID);
			
			String decodeJSON = new String(Base64.getDecoder().decode(encodeJSON.getBytes()));
			JSONObject implementationJson = new JSONObject(decodeJSON);
			Integer userid = implementationJson.getInt("loginUserId");
			JSONArray kebeleDetailsjsa = implementationJson.getJSONArray("kebelejsa");
			
			if(monitorId != null && monitorId != 0) { //update monitor data
				monitorDetails = monitorDetailsRepository.findByMonitorid(monitorId);
				if (monitorDetails == null) {
					return WrsisPortalConstant.ERROR;
				}
				
				//delete existing records in child tables
				monitorLocationRepository.deleteByMonitorid(monitorDetails.getMonitorid());
				monitorVarietyRepository.deleteByMonitorid(monitorDetails.getMonitorid());
				monitorRustRepository.deleteByMonitorid(monitorDetails.getMonitorid());
				monitorGrowthStageRepository.deleteByMonitorid(monitorDetails.getMonitorid());
				monitorFungicideRepository.deleteByMonitorid(monitorDetails.getMonitorid());
					
				for (int i = 0; i < kebeleDetailsjsa.length(); i++) {
						
						JSONObject json = kebeleDetailsjsa.getJSONObject(i);

						monitorDetails.setMonitorno(monitorDetails.getMonitorno());
						monitorDetails.setRecomid(Integer.valueOf(json.getString("recomId")));
						monitorDetails.setRecomno(json.getString(WrsisPortalConstant.RECOM_NO));
						monitorDetails.setWoredaid(Integer.valueOf(json.getString("woredaId")));
						monitorDetails.setSeveritypc(json.getString(WrsisPortalConstant.SEVERITY_C));
						monitorDetails.setIncidencespc(json.getString("incidences"));
						monitorDetails.setSowingland(json.getString("sowingLand"));
						monitorDetails.setInfectedland(json.getString("infectedLand"));
						monitorDetails.setControlledland(json.getString("controlledArea"));
						monitorDetails.setMalefarmer(Integer.valueOf(json.getString("malef")));
						monitorDetails.setFemalefarmer(Integer.valueOf(json.getString("femalef")));
						monitorDetails.setTotalfarmer(Integer.valueOf(json.getString("totalf")));
						monitorDetails.setBitStatus(false);
						monitorDetails.setUpdatedBy(userid);
						monitorDetails.setUpdatedOn(new Timestamp(new Date().getTime()));
						
						monitorDetails.setCollectMode(collectMode);

						monitorDetails = monitorDetailsRepository.save(monitorDetails);

					}
					result = WrsisPortalConstant.UPDATE;
					insert = false;
				}else {
					//insert monitor data
					for (int i = 0; i < kebeleDetailsjsa.length(); i++) {
						monitorDetails = new MonitorDetailsEntity();
						JSONObject json = kebeleDetailsjsa.getJSONObject(i);
						String maxSlNo = monitorDetailsRepository.getMaxSlNo();
						if (maxSlNo == null) {
							int s1 = count + 1;
							moniRefNo = "MRN-01-" + Integer.toString(s1);
						} else {
							int s2 = Integer.parseInt(maxSlNo) + 1;
							moniRefNo = "MRN-01-" + Integer.toString(s2);
						}
						
						monitorDetails.setMonitorno(moniRefNo);
						
						monitorDetails.setRecomid(Integer.valueOf(json.getString("recomId")));
						monitorDetails.setRecomno(json.getString(WrsisPortalConstant.RECOM_NO));
						monitorDetails.setWoredaid(Integer.valueOf(json.getString("woredaId")));
						monitorDetails.setSeveritypc(json.getString(WrsisPortalConstant.SEVERITY_C));
						monitorDetails.setIncidencespc(json.getString("incidences"));
						monitorDetails.setSowingland(json.getString("sowingLand"));
						monitorDetails.setInfectedland(json.getString("infectedLand"));
						monitorDetails.setControlledland(json.getString("controlledArea"));
						monitorDetails.setMalefarmer(Integer.valueOf(json.getString("malef")));
						monitorDetails.setFemalefarmer(Integer.valueOf(json.getString("femalef")));
						monitorDetails.setTotalfarmer(Integer.valueOf(json.getString("totalf")));
						monitorDetails.setBitStatus(false);
						monitorDetails.setCreatedBy(userid);
						monitorDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorDetails.setMonitorStatus(0);
						monitorDetails.setCollectMode(collectMode);

						monitorDetails = monitorDetailsRepository.save(monitorDetails);
					}
					result = WrsisPortalConstant.SUCCESS;
				}//end of insert else block
				if (implementationJson.has("locationDetails")) {
					JSONArray locationDetailsjsa = implementationJson.getJSONArray("locationDetails");
					for (int j = 0; j < locationDetailsjsa.length(); j++) {
						locationDetails = new MonitorLocationEntity();
						JSONObject json1 = locationDetailsjsa.getJSONObject(j);
						locationDetails.setMonitorid(monitorDetails.getMonitorid());
						locationDetails.setMonitorno(monitorDetails.getMonitorno());
						locationDetails.setRegionid(Integer.valueOf(json1.getString(WrsisPortalConstant.REGION_ID)));
						locationDetails.setZoneid(Integer.valueOf(json1.getString("zoneId")));
						locationDetails.setWoredaid(Integer.valueOf(json1.getString("woredaId")));
						locationDetails.setKebeleid(Integer.valueOf(json1.getString("kebeleId")));
						locationDetails.setBitStatus(false);
						locationDetails.setCreatedBy(userid);
						locationDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorLocationRepository.save(locationDetails);
					}
				}

				if (implementationJson.has(WrsisPortalConstant.VARIETY_DETAILS)) {
					JSONArray varietyjsa = implementationJson.getJSONArray(WrsisPortalConstant.VARIETY_DETAILS);
					for (int k = 0; k < varietyjsa.length(); k++) {
						varietyDetails = new MonitorVarietyEntity();
						JSONObject json2 = varietyjsa.getJSONObject(k);
						varietyDetails.setMonitorid(monitorDetails.getMonitorid());
						varietyDetails.setMonitorno(monitorDetails.getMonitorno());
						varietyDetails.setVarietyid(Integer.valueOf(json2.getString("varietyId")));
						varietyDetails.setVarietyname(json2.getString("varietyName"));
						varietyDetails.setBitStatus(false);
						varietyDetails.setCreatedBy(userid);
						varietyDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorVarietyRepository.save(varietyDetails);
					}
				}

				if (implementationJson.has("rustDetails")) {
					JSONArray rustjsa = implementationJson.getJSONArray("rustDetails");
					for (int l = 0; l < rustjsa.length(); l++) {
						rustDetails = new MonitorRustEntity();
						JSONObject json3 = rustjsa.getJSONObject(l);
						rustDetails.setMonitorid(monitorDetails.getMonitorid());
						rustDetails.setMonitorno(monitorDetails.getMonitorno());
						rustDetails.setRusttypeid(Integer.valueOf(json3.getString(WrsisPortalConstant.RUSTID)));
						rustDetails.setBitStatus(false);
						rustDetails.setCreatedBy(userid);
						rustDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorRustRepository.save(rustDetails);
					}
				}

				if (implementationJson.has("growthDetails")) {
					JSONArray growthjsa = implementationJson.getJSONArray("growthDetails");
					for (int m = 0; m < growthjsa.length(); m++) {
						growthDetails = new MonitorGrowthStageEntity();
						JSONObject json4 = growthjsa.getJSONObject(m);
						growthDetails.setMonitorid(monitorDetails.getMonitorid());
						growthDetails.setMonitorno(monitorDetails.getMonitorno());
						growthDetails.setGrowthstageid(Integer.valueOf(json4.getString("growthId")));
						growthDetails.setBitStatus(false);
						growthDetails.setCreatedBy(userid);
						growthDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorGrowthStageRepository.save(growthDetails);
					}
				}

				if (implementationJson.has("fungicideDetails")) {
					JSONArray fungicidejsa = implementationJson.getJSONArray("fungicideDetails");
					for (int n = 0; n < fungicidejsa.length(); n++) {
						fungicideDetails = new MonitorFungicideEntity();
						JSONObject json5 = fungicidejsa.getJSONObject(n);
						fungicideDetails.setMonitorid(monitorDetails.getMonitorid());
						fungicideDetails.setMonitorno(monitorDetails.getMonitorno());
						fungicideDetails.setFungicideid(Integer.valueOf(json5.getString("fungicideId")));
						fungicideDetails.setFungicideused(json5.getString("fungicideUsed"));
						fungicideDetails.setBitStatus(false);
						fungicideDetails.setCreatedBy(userid);
						fungicideDetails.setCreatedOn(new Timestamp(new Date().getTime()));
						monitorFungicideRepository.save(fungicideDetails);
					}
				}

				if (insert) {
					List<Object[]> authObj = approvalAuthorityEntityRepository.fetchApproveDetails(processId);
					if (authObj != null) {
						// Save ApprovalAuthority
						ApprovalAuthorityEntity authorityEntity = new ApprovalAuthorityEntity();
						authorityEntity.setAprovalPId(processId);
						authorityEntity.setStageNo((Integer) authObj.get(0)[1]);
						authorityEntity.setSurveyId(monitorDetails.getMonitorid());
						authorityEntity.setPendingAt((Integer) authObj.get(0)[2]);
						authorityEntity.setSentFrom(userid);
						authorityEntity.setStatus(false);
						authorityEntity.setCreadtedBy(userid);
						authorityEntity.setCreatedOn(new Date());
						approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

						// Save ApprovalAuthorityHistory
						ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
						history.setAprovalPId(authorityEntity.getAprovalPId());
						history.setStageNo(authorityEntity.getStageNo());
						history.setSurveyId(authorityEntity.getSurveyId());
						history.setPendingAt(authorityEntity.getPendingAt());
						history.setSentFrom(authorityEntity.getSentFrom());
						history.setStatus(authorityEntity.isStatus());
						history.setCreadtedBy(userid);
						history.setCreatedOn(new Date());

						approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
					}
				}
		}catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;
			LOG.error("MobileServiceImpl::saveSurveyImplementation():" + e);

		}
		return result;
	}

	@Override
	public JSONObject viewImplementation(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray implementationArray = new JSONArray();
		try {
			List<Object[]> recommendationDetails = monitorDetailsRepository.viewSurveyImplementation(0, 0, "", "",
					loginReqDto.getUserId());
			JSONObject impleListObj = null;
			if (!recommendationDetails.isEmpty()) {
				for (Object[] obj : recommendationDetails) {
					impleListObj = new JSONObject();
					impleListObj.put("montId", String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
					impleListObj.put("montNo", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
					impleListObj.put("montDate", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
					impleListObj.put(WrsisPortalConstant.RECOM_NO, String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
					impleListObj.put(WrsisPortalConstant.REGION1, String.valueOf((obj[4] != null) ? (String) obj[4] : ""));
					impleListObj.put("zone", String.valueOf((obj[5] != null) ? (String) obj[5] : ""));
					impleListObj.put(WrsisPortalConstant.WOREDA1, String.valueOf((obj[6] != null) ? (String) obj[6] : ""));
					impleListObj.put("pasaffeted", String.valueOf((obj[7] != null) ? (String) obj[7] : ""));
					
					impleListObj.put("infected", String.valueOf((obj[8] != null) ? (String) obj[8] : ""));
					impleListObj.put("control", String.valueOf((obj[9] != null) ? (String) obj[9] : ""));
					impleListObj.put("controlpercet", String.valueOf((obj[10] != null) ? (String) obj[10] : ""));
					
					
					impleListObj.put("malefar", String.valueOf((obj[11] != null) ? (String) obj[11] : ""));
					impleListObj.put("femalefar", String.valueOf((obj[12] != null) ? (String) obj[12] : ""));
					impleListObj.put("totalfar", String.valueOf((obj[13] != null) ? (String) obj[13] : ""));
					if (obj[13] != null)
						impleListObj.put(WrsisPortalConstant.STATUS,
								getStatus(String.valueOf((obj[14] != null) ? (String) obj[14] : "")));
					impleListObj.put("fungiused", String.valueOf((obj[15] != null) ? (String) obj[15] : ""));
					implementationArray.put(impleListObj);

				}
				response.put("viewImplementationData", implementationArray);

			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewImplementation():" + e);

		}

		return response;
	}

	@Override
	public JSONObject viewDetailsImplementation(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray implementationDetailsArray = new JSONArray();
		JSONArray mfungicideArray = new JSONArray();
		JSONObject mfungicideObj = new JSONObject();
		try {
			List<Object[]> recommendationDetails = monitorDetailsRepository
					.viewDetailsImplementation(loginReqDto.getMonitorId());
			JSONObject impleListObj = null;
			if (!recommendationDetails.isEmpty()) {
				for (Object[] obj : recommendationDetails) {
					impleListObj = new JSONObject();
					impleListObj.put("montNo", String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
					impleListObj.put("montDate", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
					impleListObj.put(WrsisPortalConstant.RECOM_NO, String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
					impleListObj.put(WrsisPortalConstant.REGION1, String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
					impleListObj.put("zone", String.valueOf((obj[4] != null) ? (String) obj[4] : ""));
					impleListObj.put(WrsisPortalConstant.WOREDA1, String.valueOf((obj[5] != null) ? (String) obj[5] : ""));
					impleListObj.put(WrsisPortalConstant.KEBELE1, String.valueOf((obj[6] != null) ? (String) obj[6] : ""));
					impleListObj.put("variety", String.valueOf((obj[7] != null) ? (String) obj[7] : ""));
					impleListObj.put("rust", String.valueOf((obj[8] != null) ? (String) obj[8] : ""));
					impleListObj.put("gstage", String.valueOf((obj[9] != null) ? (String) obj[9] : ""));
					impleListObj.put("fungitotal", String.valueOf((obj[10] != null) ? (String) obj[10] : ""));
					impleListObj.put("infectedland", String.valueOf((obj[11] != null) ? (String) obj[11] : ""));
					impleListObj.put("controlland", String.valueOf((obj[12] != null) ? (String) obj[12] : ""));
					
					
					impleListObj.put("malefar", String.valueOf((obj[13] != null) ? (String) obj[13] : ""));
					impleListObj.put("femalefar", String.valueOf((obj[14] != null) ? (String) obj[14] : ""));
					impleListObj.put("totalfar", String.valueOf((obj[15] != null) ? (String) obj[15] : ""));
					if (obj[16] != null)
						impleListObj.put(WrsisPortalConstant.STATUS,
								getStatus(String.valueOf((obj[16] != null) ? (String) obj[16] : "")));
					impleListObj.put(WrsisPortalConstant.SEVERITY_C, String.valueOf((obj[17] != null) ? (String) obj[17] : ""));
					impleListObj.put("incedences", String.valueOf((obj[18] != null) ? (String) obj[18] : ""));
					impleListObj.put("sowingland", String.valueOf((obj[19] != null) ? (String) obj[19] : ""));
					impleListObj.put("pasaffeted", String.valueOf((obj[6] != null) ? (String) obj[6] : ""));
					impleListObj.put("controlpercet", String.valueOf((obj[22] != null) ? (String) obj[22] : ""));
					Integer monitordtlid = Integer.valueOf(String.valueOf(obj[20]));
					List<Object[]> mfungicideList = monitorFungicideRepository
							.findFungicideDetailsByMonitorid(monitordtlid);
					if (!mfungicideList.isEmpty()) {
						for (Object[] optobj : mfungicideList) {
							mfungicideObj = new JSONObject();
							mfungicideObj.put("funginame", String.valueOf(optobj[0]));
							mfungicideObj.put("fungiused", String.valueOf(optobj[1]));
							mfungicideArray.put(mfungicideObj);

						}
						impleListObj.put("mfungicideDetails", mfungicideArray);
						mfungicideArray = new JSONArray();
					}
					implementationDetailsArray.put(impleListObj);
				}
				response.put("viewImplementationDetails", implementationDetailsArray);

			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewDetailsImplementation():" + e);

		}

		return response;
	}

	private String getStatus(String getStatus) {
		String status = "";
		if (getStatus.equals("0")) {
			return status = "Pending";
		} else {
			status = "Publish";
		}
		return status;
	}

	@Override
	public JSONObject viewRustIncident(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray questArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		JSONArray rustincidentArray = new JSONArray();
		JSONArray optionArray1 = new JSONArray();
		try {
			loginReqDto.setSeasonId((loginReqDto.getSeasonId() == null) ? 0 : loginReqDto.getSeasonId());
			loginReqDto.setUserId(((Integer) loginReqDto.getUserId() == null) ? 0 : loginReqDto.getUserId());
			loginReqDto.setDemographyId((loginReqDto.getDemographyId() == null) ? 0 : loginReqDto.getDemographyId());
			;
			loginReqDto.setYear((loginReqDto.getYear() == null || loginReqDto.getYear().trim().equals("")) ? ""
					: loginReqDto.getYear());
			List<Object[]> rustincidentList = rustIncidentDetailsRepository.viewRustIncident(loginReqDto.getSeasonId(),
					loginReqDto.getUserId(), loginReqDto.getDemographyId(), loginReqDto.getYear());
			JSONObject rustincObj = null;
			JSONObject questvalObj = null;
			JSONObject optionObj = null;
			JSONObject questvalObj1 = null;
			JSONObject optionObj1 = null;
			
			if (!rustincidentList.isEmpty()) {
				for (Object[] rustobj : rustincidentList) {
					rustincObj = new JSONObject();
				
					rustincObj.put("incidid", String.valueOf(rustobj[0]));
					rustincObj.put(WrsisPortalConstant.INCDT, String.valueOf(rustobj[1]));
					rustincObj.put(WrsisPortalConstant.SEASON, String.valueOf(rustobj[2]));
					rustincObj.put(WrsisPortalConstant.LONGI, String.valueOf(rustobj[3]));
					rustincObj.put("lati", String.valueOf(rustobj[4]));
					Integer incidentId = Integer.valueOf(String.valueOf(rustobj[0]));
					List<Object[]> questionList = rustIncidentDetailsRepository.findOptionByIncidentId(incidentId);
					if (!questionList.isEmpty()) {
						for (Object[] queobj : questionList) {
							questvalObj = new JSONObject();
							questvalObj.put(WrsisPortalConstant.INCIDTL_ID, String.valueOf(queobj[0]));
							questvalObj.put(WrsisPortalConstant.QUE_ID, String.valueOf(queobj[1]));
							questvalObj.put(WrsisPortalConstant.QUE_VALUE, String.valueOf(queobj[2]));
							questvalObj.put(WrsisPortalConstant.QUE_NAME, String.valueOf(queobj[3]));
							questvalObj.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(queobj[4]));
							Integer questionId = Integer.valueOf(String.valueOf(queobj[1]));
							List<Object[]> optionList = rustIncidentDetailsRepository
									.findOptionByQuestionId(questionId);
							if (!optionList.isEmpty()) {
								for (Object[] optobj : optionList) {
									optionObj = new JSONObject();
									
									optionObj.put(WrsisPortalConstant.OPT_ORDER, String.valueOf(optobj[0]));
									optionObj.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj[1]));
									optionObj.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj[2]));
									optionArray.put(optionObj);

								}
								questvalObj.put(WrsisPortalConstant.OPTION_DETAILS, optionArray);
								optionArray = new JSONArray();
							}
							questArray.put(questvalObj);

						}

						// modify quevalu ""

						List<Object[]> questionList1 = rustIncidentDetailsRepository.findOptionByNotIncidentId();
						if (!questionList1.isEmpty()) {
							for (Object[] queobj1 : questionList1) {
								questvalObj1 = new JSONObject();
								questvalObj1.put(WrsisPortalConstant.QUE_ID, String.valueOf(queobj1[0]));
								questvalObj1.put(WrsisPortalConstant.QUE_NAME, String.valueOf(queobj1[1]));
								questvalObj1.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(queobj1[2]));
								questvalObj1.put(WrsisPortalConstant.INCIDTL_ID, "");
								questvalObj1.put(WrsisPortalConstant.QUE_VALUE, "");
								Integer questionId = Integer.valueOf(String.valueOf(queobj1[0]));
								List<Object[]> optionList1 = rustIncidentDetailsRepository
										.findOptionByQuestionId(questionId);
								if (!optionList1.isEmpty()) {
									for (Object[] optobj1 : optionList1) {
										optionObj1 = new JSONObject();
										
										optionObj1.put(WrsisPortalConstant.OPT_ORDER, String.valueOf(optobj1[0]));
										optionObj1.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj1[1]));
										optionObj1.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj1[2]));
										optionArray1.put(optionObj1);

									}
									questvalObj1.put(WrsisPortalConstant.OPTION_DETAILS, optionArray1);
									optionArray1 = new JSONArray();
								}
								questArray.put(questvalObj1);

							}

						}
						rustincObj.put(WrsisPortalConstant.QUESTION_DETAILS, questArray);

					}

					rustincidentArray.put(rustincObj);

				}
				response.put(WrsisPortalConstant.RUST_INCIDENT_DETAILS, rustincidentArray);
			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRustIncident():" + e);

		}
		return response;
	}

	@Override
	public JSONObject getMasterforSurveyImplementation(MobileReqBean loginReqDto) {

		JSONObject response = new JSONObject();
		JSONArray grStgArray = new JSONArray();
		JSONArray fungicideArray = new JSONArray();
		
		
		JSONArray varietyArray = new JSONArray();
		JSONArray rustTypeArray = new JSONArray();
		try {

			// grStg

			List<Object[]> grStgList = growthStageRepository.fetchAllGrowth();
			JSONObject grStgListObj = null;
			if (!grStgList.isEmpty()) {
				for (Object[] obj : grStgList) {
					grStgListObj = new JSONObject();
					grStgListObj.put("gsId", String.valueOf(obj[0]));
					grStgListObj.put(WrsisPortalConstant.GS_NAME, String.valueOf(obj[1]));

					grStgArray.put(grStgListObj);

				}

			}

			// fungicide

			List<Object[]> fungicideList = fungicideMasterRepository.fetchAllFungicides();
			JSONObject fungicideObj = null;
			if (!fungicideList.isEmpty()) {
				for (Object[] obj : fungicideList) {
					fungicideObj = new JSONObject();
					fungicideObj.put("fngId", String.valueOf(obj[0]));
					fungicideObj.put("fngName", String.valueOf(obj[1]));

					fungicideArray.put(fungicideObj);

				}

			}

			

			// variety

			List<Object[]> varietyList = varietyTypeRepository.fetchAllVarietyType();
			JSONObject varietyObj = null;
			if (!varietyList.isEmpty()) {
				for (Object[] variobj : varietyList) {
					varietyObj = new JSONObject();
					varietyObj.put("varId", String.valueOf(variobj[0]));
					varietyObj.put("varName", String.valueOf(variobj[1]));
					varietyArray.put(varietyObj);

				}

			}

			// rust types

			List<Object[]> rustTypeList = typeOfRustRepository.fetchAllRustType();
			JSONObject rustTypeListObj = null;
			if (!rustTypeList.isEmpty()) {
				for (Object[] obj : rustTypeList) {
					rustTypeListObj = new JSONObject();
					rustTypeListObj.put(WrsisPortalConstant.RUSTID, String.valueOf(obj[0]));
					rustTypeListObj.put(WrsisPortalConstant.RUSTNAME, String.valueOf(obj[1]));

					rustTypeArray.put(rustTypeListObj);

				}

			}

			String jsonStr = seasionMasterRepository.getdemographydata(loginReqDto.getUserId());
			JSONArray json = new JSONArray(jsonStr);
			JSONArray region = new JSONArray();
			JSONArray zone = new JSONArray();
			JSONArray woreda = new JSONArray();
			JSONArray kebel = new JSONArray();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(WrsisPortalConstant.REGION_ID, json.getJSONObject(i).getString("regionid"));
				jsonObj.put("regionName", json.getJSONObject(i).getString("regionname"));
				region.put(jsonObj);
				JSONArray zoneA = json.getJSONObject(i).getJSONArray("zone");
				zone: for (int j = 0; j < zoneA.length(); j++) {
					JSONObject jsonZo = new JSONObject();
					jsonZo.put(WrsisPortalConstant.REGION_ID, json.getJSONObject(i).getString("regionid"));
					jsonZo.put("zoneId", zoneA.getJSONObject(j).getString("zoneid"));
					jsonZo.put("zoneName", zoneA.getJSONObject(j).getString("zonename"));
					zone.put(jsonZo);

					JSONArray woredaA = new JSONArray();
					try {
						woredaA = zoneA.getJSONObject(j).getJSONArray(WrsisPortalConstant.WOREDA1);
					} catch (Exception e) {
						LOG.error("MobileServiceImpl::getMasterForSurveyImplementation():" + e);

						continue zone;
					}
					woreda: for (int k = 0; k < woredaA.length(); k++) {
						JSONObject jsonWo = new JSONObject();
						jsonWo.put("zoneId", zoneA.getJSONObject(j).getString("zoneid"));
						jsonWo.put("woredaId", woredaA.getJSONObject(k).getString("woredaid"));
						jsonWo.put("woredaName", woredaA.getJSONObject(k).getString("woredaname"));
						woreda.put(jsonWo);

						JSONArray kebeleA = new JSONArray();
						try {
							kebeleA = woredaA.getJSONObject(k).getJSONArray(WrsisPortalConstant.KEBELE1);
						} catch (Exception e) {
							LOG.error("MobileServiceImpl::getMasterForSurveyImplementation():" + e);

							continue woreda;
						}
						for (int l = 0; l < kebeleA.length(); l++) {
							JSONObject jsonKe = new JSONObject();
							jsonKe.put("woredaId", woredaA.getJSONObject(k).getString("woredaid"));
							jsonKe.put("kebelId", kebeleA.getJSONObject(l).getString("kebelid"));
							jsonKe.put("kebelaName", kebeleA.getJSONObject(l).getString("kebelaname"));
							kebel.put(jsonKe);

						}

					}

				}

			}

			response.put("growthStageData", grStgArray);
			response.put("fungicideData", fungicideArray);
			response.put("kebeleData", kebel);
			response.put("woredaData", woreda);
			response.put("zoneData", zone);
			response.put("regionData", region);
			response.put("varietyData", varietyArray);
			response.put("rustTypeData", rustTypeArray);

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getMasterForSurveyImplementation():" + e);

		}
		return response;
	}

	@Autowired
	RecommendationRepository recommendationRepository;

	@Override
	public JSONObject viewRecommendationData(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray recommendationArry = new JSONArray();
		try {
			List<Object[]> recommendationList = recommendationRepository
					.viewRecommendationData(loginReqDto.getUserId());
			JSONObject recommendationObj = null;
			if (!recommendationList.isEmpty()) {
				for (Object[] obj : recommendationList) {
					recommendationObj = new JSONObject();
					recommendationObj.put("recoId", String.valueOf(obj[0]));
					recommendationObj.put("recoNo", String.valueOf(obj[1]));
					//recommendationObj.put("recoDt", DateUtil.DateToString((Date) (obj[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					recommendationObj.put("recoDt", String.valueOf(obj[2]));
					recommendationObj.put("recoFile", String.valueOf(obj[3]));

					recommendationArry.put(recommendationObj);

				}

			}
			response.put("recommendationData", recommendationArry);

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRecommendationData():" + e);

		}
		return response;
	}

	@Override
	public JSONObject mUserProfileDetails(Integer userId) {
		JSONObject rowsData = new JSONObject();
	
		try {
			List<Object[]> objList = userRepository.mUserProfileDetails(userId);
			JSONObject userProfileData = null;
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					userProfileData = new JSONObject();
					userProfileData.put("userName", String.valueOf(obj[0]));
					userProfileData.put("designation", String.valueOf(obj[1]));
					userProfileData.put("mobileNumber", String.valueOf(obj[2]));
					userProfileData.put("userId", String.valueOf(Integer.valueOf((Short) (obj[3]))));
					userProfileData.put("mailid", String.valueOf(obj[4]));
					if (obj[5] != null)
						userProfileData.put("gender", getGender(String.valueOf(Integer.valueOf((Short) (obj[5])))));
					userProfileData.put("image", String.valueOf(obj[6]));
				}
			}
			rowsData.put("userProfileData", userProfileData);
			
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::mUserProfileDetails():" + e);

		}
		return rowsData;
	}

	private String getGender(String gendervalue) {
		String gender = "";
		if (gendervalue.equals("1")) {
			return gender = WrsisPortalConstant.MALE;
		} else {
			gender = WrsisPortalConstant.FEMALE;
		}
		return gender;
	}

	@Override
	public JSONObject viewRaceAnalysisDetails(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray raceArry = new JSONArray();
		JSONArray incolutionArry = new JSONArray();
		try {
			List<Object[]> objList = sampleLabTagDetailsRepository
					.getSurveyDetatailsBySurveyId(loginReqDto.getRaceAnalysisId());
			JSONObject surveyData = null;
			JSONObject incolutionObj = null;
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					surveyData = new JSONObject();
					surveyData.put(WrsisPortalConstant.SURVEYNO, String.valueOf(obj[0]));
					surveyData.put("surveydt", DateUtil.DateToString((Date) (obj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					surveyData.put(WrsisPortalConstant.REGION1, String.valueOf(obj[2]));
					surveyData.put("zone", String.valueOf(obj[3]));
					surveyData.put(WrsisPortalConstant.WOREDA1, String.valueOf(obj[4]));
					surveyData.put(WrsisPortalConstant.KEBELE1, String.valueOf(obj[5]));
					surveyData.put("latitude", String.valueOf(obj[6]));
					surveyData.put("longitude", String.valueOf(obj[7]));
					surveyData.put("altitude", String.valueOf(obj[8]));
					surveyData.put("sampleval", String.valueOf(obj[9]));
					surveyData.put("rusttype", String.valueOf(obj[10]));
					surveyData.put("reciveby", String.valueOf(obj[11]));
					surveyData.put("centername", String.valueOf(obj[12]));
					surveyData.put("variety", String.valueOf(obj[13]));
					surveyData.put("surveyor",
							surveyorDetailsRepository.getSurveyorName(Integer.valueOf(String.valueOf(obj[20]))));
					surveyData.put("incolutationdate", DateUtil.DateToString((Date) (obj[15]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					surveyData.put("racefile", String.valueOf(obj[16]));
					surveyData.put("raceresult", String.valueOf(obj[17]));
					surveyData.put("wheatline", String.valueOf(obj[18]));
					surveyData.put("samplreceived", DateUtil.DateToString((Date) (obj[19]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));

				}

				List<Object[]> incolutionList = sampleLabTagDetailsRepository
						.getIncolutionDataByRaceAnalysisId(loginReqDto.getRaceAnalysisId());
				if (!incolutionList.isEmpty()) {
					for (Object[] optobj : incolutionList) {
						incolutionObj = new JSONObject();
						incolutionObj.put("incolutiondt", DateUtil.DateToString((Date) (optobj[0]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						incolutionObj.put("recordingdt", DateUtil.DateToString((Date) (optobj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						incolutionObj.put("incoresult", String.valueOf(optobj[2]));
						incolutionObj.put("repetition", String.valueOf(optobj[3]));
						incolutionArry.put(incolutionObj);

					}
					surveyData.put("incolutionDetails", incolutionArry);
					incolutionArry = new JSONArray();
				} else {
					surveyData.put("incolutionDetails", new JSONArray());
				}
				raceArry.put(surveyData);

			}
			response.put("raceAnalysisDetails", raceArry);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRaceAnalysisDetails():" + e);

		}
		return response;
	}

	@Override
	public JSONObject getMasterforRustIncidentMap() {
		JSONObject response = new JSONObject();
		JSONArray seasonArray = new JSONArray();
		JSONArray yearSeasonArray = new JSONArray();
		try {

			// year
			JSONArray yearJsa = DateUtil.getYearList();
			response.put("yearList", yearJsa);

			// season
			List<Object[]> seasionList = seasionMasterRepository.fetchAllSeasionsWiseMonth();
			JSONObject seasionObj = null;

			if (!seasionList.isEmpty()) {
				for (Object[] seaobj : seasionList) {
					seasionObj = new JSONObject();
					seasionObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(seaobj[0]));
					seasionObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(seaobj[1]));
					seasonArray.put(seasionObj);

				}

			}
			response.put(WrsisPortalConstant.SEASON_DATA, seasonArray);

			// Current year and season
			List<Object[]> yearseasondata = seasionMasterRepository.fetchCurrentYearWiseSeason();
			JSONObject seasionYObj = null;

			if (!yearseasondata.isEmpty()) {
				for (Object[] yseaobj : yearseasondata) {
					seasionYObj = new JSONObject();
					try {
						seasionYObj.put("year", ((Double) yseaobj[0]).intValue());
					} catch (Exception e) {
					}
					seasionYObj.put(WrsisPortalConstant.SEA_ID, String.valueOf(yseaobj[1]));
					seasionYObj.put(WrsisPortalConstant.SEA_NAME, String.valueOf(yseaobj[2]));
					yearSeasonArray.put(seasionYObj);

				}

			}

			response.put("yearSeasonData", yearSeasonArray);

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getMasterForRustIncidentMap():" + e);

		}

		return response;
	}

	@Override
	public JSONObject viewRustIncidentForGis(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray rustincidentgisArray = new JSONArray();
		try {
			loginReqDto.setSeasonId((loginReqDto.getSeasonId() == null) ? 0 : loginReqDto.getSeasonId());
			loginReqDto.setYear((loginReqDto.getYear() == null || loginReqDto.getYear().trim().equals("")) ? ""
					: loginReqDto.getYear());
			List<Object[]> rustincidentList = rustIncidentDetailsRepository
					.viewRustIncidentForGis(loginReqDto.getSeasonId(), loginReqDto.getYear());
			JSONObject rustincObj = null;
			JSONArray questions = new JSONArray();
			JSONObject questionsIObj = new JSONObject();
			if (!rustincidentList.isEmpty()) {
				for (Object[] rustobj : rustincidentList) {
					rustincObj = new JSONObject();
					rustincObj.put("incidentid", String.valueOf(rustobj[0]));
					rustincObj.put(WrsisPortalConstant.LONGI, String.valueOf(rustobj[1]));
					rustincObj.put("lati", String.valueOf(rustobj[2]));

					String[] ques = String.valueOf(rustobj[3]).split(",");
					String[] ans = String.valueOf(rustobj[4]).split(",");
					questions = new JSONArray();
					for (int i = 0; i < ques.length; i++) {
						questionsIObj = new JSONObject();

						questionsIObj.put("qname", ques[i]);
						questionsIObj.put("qvalue", ans[i]);

						questions.put(questionsIObj);

					}
					rustincObj.put("QuestionArray", questions);

					rustincidentgisArray.put(rustincObj);
				}
				

				response.put("rustIncidentgisData", rustincidentgisArray);

			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRustIncidentForGis():" + e);

		}
		return response;
	}

	@Override
	public JSONObject viewRustIncidentByUser(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray rustincidentArray = new JSONArray();
		try {
			loginReqDto.setSeasonId((loginReqDto.getSeasonId() == null) ? 0 : loginReqDto.getSeasonId());
			loginReqDto.setUserId(((Integer) loginReqDto.getUserId() == null) ? 0 : loginReqDto.getUserId());
			loginReqDto.setDemographyId((loginReqDto.getDemographyId() == null) ? 0 : loginReqDto.getDemographyId());
			
			loginReqDto.setYear((loginReqDto.getYear() == null || loginReqDto.getYear().trim().equals("")) ? ""
					: loginReqDto.getYear());
			List<Object[]> rustincidentList = rustIncidentDetailsRepository.viewRustIncident(loginReqDto.getSeasonId(),
					loginReqDto.getUserId(), loginReqDto.getDemographyId(), loginReqDto.getYear());
			JSONObject rustincObj = null;
			if (!rustincidentList.isEmpty()) {
				for (Object[] rustobj : rustincidentList) {
					rustincObj = new JSONObject();
					rustincObj.put("incidid", String.valueOf(rustobj[0]));
					rustincObj.put(WrsisPortalConstant.INCDT, String.valueOf(rustobj[1]));
					rustincObj.put(WrsisPortalConstant.SEASON, String.valueOf(rustobj[2]));
					rustincObj.put(WrsisPortalConstant.LONGI, String.valueOf(rustobj[3]));
					rustincObj.put("lati", String.valueOf(rustobj[4]));
					rustincObj.put(WrsisPortalConstant.KEBELE1, String.valueOf(rustobj[5]));
					rustincidentArray.put(rustincObj);

				}
				response.put(WrsisPortalConstant.RUST_INCIDENT_DETAILS, rustincidentArray);
			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRustIncidentByUser():" + e);

		}
		return response;
	}

	@Override
	public JSONObject viewRustIncidentByRustIncident(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray rustincidentArray = new JSONArray();
		JSONArray questArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		try {
			loginReqDto
					.setRustincidentId((loginReqDto.getRustincidentId() == null) ? 0 : loginReqDto.getRustincidentId());

			List<Object[]> rustincidentList = rustIncidentDetailsRepository
					.viewRustIncidentByRustIncident(loginReqDto.getRustincidentId());
			JSONObject rustincObj = null;
			JSONObject questvalObj = null;
			JSONObject optionObj = null;
			if (!rustincidentList.isEmpty()) {
				for (Object[] rustobj : rustincidentList) {
					rustincObj = new JSONObject();
					
					rustincObj.put(WrsisPortalConstant.INCDT, String.valueOf(rustobj[1]));
					rustincObj.put(WrsisPortalConstant.SEASON, String.valueOf(rustobj[2]));
					rustincObj.put(WrsisPortalConstant.LONGI, String.valueOf(rustobj[3]));
					rustincObj.put("lati", String.valueOf(rustobj[4]));
					rustincObj.put(WrsisPortalConstant.KEBELE1, String.valueOf(rustobj[5]));

					
					List<Object[]> questionList = rustIncidentDetailsRepository
							.findOptionByIncidentId(loginReqDto.getRustincidentId());
					if (!questionList.isEmpty()) {
						for (Object[] queobj : questionList) {
							questvalObj = new JSONObject();
							
							questvalObj.put(WrsisPortalConstant.QUE_ID, String.valueOf(queobj[1]));
							questvalObj.put(WrsisPortalConstant.QUE_VALUE, String.valueOf(queobj[2]));
							questvalObj.put(WrsisPortalConstant.QUE_NAME, String.valueOf(queobj[3]));
							questvalObj.put(WrsisPortalConstant.QUE_ORDER, String.valueOf(queobj[4]));
							Integer questionId = Integer.valueOf(String.valueOf(queobj[1]));
							
							List<Object[]> optionList = rustIncidentDetailsRepository
									.findOptionByQuestionId(questionId);
							if (!optionList.isEmpty()) {
								for (Object[] optobj : optionList) {
									optionObj = new JSONObject();
									
									optionObj.put(WrsisPortalConstant.OPT_ORDER, String.valueOf(optobj[0]));
									optionObj.put(WrsisPortalConstant.OPT_ID, String.valueOf(optobj[1]));
									optionObj.put(WrsisPortalConstant.OPT_VALUE, String.valueOf(optobj[2]));
									optionArray.put(optionObj);

								}
								questvalObj.put(WrsisPortalConstant.OPTION_DETAILS, optionArray);
								optionArray = new JSONArray();
							}
							questArray.put(questvalObj);
							rustincObj.put(WrsisPortalConstant.QUESTION_DETAILS, questArray);
							
						}
					}
					rustincidentArray.put(rustincObj);

				}
				response.put(WrsisPortalConstant.RUST_INCIDENT_DETAILS, rustincidentArray);
			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewRustIncidentByRustIncident():" + e);

		}
		return response;
	}

	@Override
	public JSONObject viewNotificationByUser(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray notificationArray = new JSONArray();
		try {

			List<Object[]> notificationdata = userRepository.viewNotificationByUser(loginReqDto.getUserId());
			JSONObject notificationObj = null;

			if (!notificationdata.isEmpty()) {
				for (Object[] notiobj : notificationdata) {
					notificationObj = new JSONObject();
					notificationObj.put("notimsg", String.valueOf(notiobj[0]));
					notificationObj.put("notistatus", String.valueOf(notiobj[1]));
					notificationObj.put("notidate", DateUtil.DateToString((Date) (notiobj[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					notificationObj.put("notifrom", String.valueOf(notiobj[3]));
					notificationObj.put("notiId", String.valueOf(notiobj[4]));
					notificationArray.put(notificationObj);

				}

			}
			response.put("notificationData", notificationArray);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::viewNotificationByUser():" + e);
		}

		return response;
	}

	@Override
	public String updateNotificationStatus(MobileReqBean loginReqDto) {
		String result = "";
		DashboardNotificationEntity dnotification = null;

		try {
			dnotification = dashboardNotificationRepository.findByDashboardNotifyIdAndToUserId(
					BigInteger.valueOf(loginReqDto.getNotificationId()),
					Short.valueOf((short) loginReqDto.getUserId()));
			if (dnotification != null) {
				dnotification.setToUserId(Short.valueOf((short) loginReqDto.getUserId()));
				dnotification.setUpdatedOn(new Timestamp(new Date().getTime()));
				dnotification.setUpdatedBy(Short.valueOf((short) loginReqDto.getUserId()));
				dnotification.setReadStatus(true);
				dnotification.setStatus(false);

				dashboardNotificationRepository.saveAndFlush(dnotification);

				result = WrsisPortalConstant.SUCCESS;
			} else {

				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			LOG.error("MobileServiceImpl::updateNotificationStatus():" + e);
		}
		return result;
	}

	@Override
	public MobileLoginTracking saveMobileTrackInfo(MobileLoginTracking ipinfo) {
		MobileLoginTracking info = null;
		try {
			LOG.info("ipTrack info ::: >>>>>>" + ipinfo);
			info = mobileLoginTrackingRepository.save(ipinfo);
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::saveMobileTrackInfo():" + e);
		}
		return info;
	}

	@Override
	public String mobileUserLogout(MobileReqBean loginReqDto) throws JSONException {
		JSONObject response = new JSONObject();
		List<Object[]> dataList = null;
		MobileLoginTracking logoutTrack = null;
		try {

			dataList = mobileLoginTrackingRepository.mobileUserLogout(loginReqDto.getUserId(), loginReqDto.getFcmId());

			if (!dataList.isEmpty()) {
				for (Object[] objs : dataList) {
					Integer trackId = Integer.valueOf(String.valueOf(objs[0]));
					logoutTrack = mobileLoginTrackingRepository.findByTrackId(trackId);
					if (logoutTrack != null) {
						logoutTrack.setTrackId(logoutTrack.getTrackId());
						
						logoutTrack.setUpdatedOn(new Timestamp(new Date().getTime()));
						logoutTrack.setBitStatus(true);
						mobileLoginTrackingRepository.saveAndFlush(logoutTrack);
					}
				}
				response.put("Status", "Logout Successfully.");
				response.put("StatusCode", 200);
				
			} else {
				response.put("Status", "User already logout ...");
				response.put("StatusCode", 200);
			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::mobileUserLogout():" + e);
			response.put("Status", "Unable to logout.");
			response.put("StatusCode", 400);
			
		}
		return response.toString();
	}

	@Override
	public String generateMobileToken(int userId) {

		try {

			Optional<MobileTokenEntity> mTokenById = mtRepo.findByUserIdAndBitStatus(userId, false);

			if (mTokenById.isPresent()) {

				return "token_exist";
			}

			else {

				MobileTokenEntity mtEntity = new MobileTokenEntity();
				
				String mToken = UUID.randomUUID().toString();
				try {
					mtEntity.setTokenGeneratedTime(new Date());
					mtEntity.setToken(mToken);
					mtEntity.setUserId(userId);
					mtRepo.save(mtEntity);
					
				} catch (Exception e) {
					LOG.error("MobileServiceImpl::generateMobileToken():" + e);
					mToken = null;
				}

				return mToken;
			}

		} catch (Exception e) {
			LOG.error("MobileServiceImpl::generateMobileToken():" + e);

			return null;
		}
	}

	@Override
	public boolean authenticateToken(int userId,String token) {
		Optional<MobileTokenEntity> obj= mtRepo.findByUserIdAndTokenAndBitStatus(userId,token, false);
	if (obj.isPresent()) {
		return true ;
	}	

		return false;
	}

	@Override
	public String updateAppVersion(MobileReqBean loginReqDto) {
		String updateStatus="Failure";
		try {
			
			
			
			Optional<MobileAppVersionEntity> mAppVer = mAppRepo.findByOsTypeIdAndBitstatus(loginReqDto.getOsTypeId(),false);
			if(mAppVer.isPresent()) {
				MobileAppVersionEntity mAppEnt=mAppVer.get();
				mAppEnt.setVersionNo(loginReqDto.getVersionval());
				mAppEnt.setMandatorybitstatus(WrsisPortalConstant.YES.equalsIgnoreCase(loginReqDto.getIsMandatory())?true:false);
				mAppEnt.setCreateOn(new Timestamp(new Date().getTime()));
				
				mAppRepo.save(mAppEnt);
			
				updateStatus=WrsisPortalConstant.SUCCESS;		
			}
			
			
			
			
		
		
		}catch (Exception ex) {
		
			LOG.error("MobileServiceImpl::updateAppVersion():" + ex);
		
		
}
		return updateStatus;
	}

	@Override
	public JSONObject getAppVersionByOstype(Integer osTypeId) {
		JSONObject response = new JSONObject();
		
		try {
			Optional<MobileAppVersionEntity> obj = mAppRepo.findByOsTypeIdAndBitstatus(osTypeId,false);
			if(obj.isPresent()) {
				MobileAppVersionEntity tObj=obj.get();
				response.put("versionVal", tObj.getVersionNo());
				response.put("osType", tObj.getOstype());
				response.put("IsMandatory", tObj.isMandatorybitstatus()?WrsisPortalConstant.YES:WrsisPortalConstant.NO);
				
			}
			
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::getAppVersionByOstype():" + e);
		}

		return response;
	}

	@Override
	public boolean updateAuthTokenAfterUse(Integer userId, String token) {
		boolean b=false;
		try{
			Optional<MobileTokenEntity> obj= mtRepo.findByUserIdAndTokenAndBitStatus(userId,token, false);
			
			if (obj.isPresent()) {
				MobileTokenEntity mAppEnt=obj.get();
				mAppEnt.setBitStatus(true);
				mAppEnt.setTokenUpdatedTime(new Timestamp(new Date().getTime()));
				
				mtRepo.save(mAppEnt);
				b=true ;
			}	

		}catch (Exception e) {
			LOG.error("MobileServiceImpl::updateAuthTokenAfterUse():" + e);
		}
			return b;
	}

	@Override
	public List<MonitorDetailsEntity> getMonitorDetailsByRecIdAndWoredaId(Integer recId, Integer woredaId) {
		List<MonitorDetailsEntity> monitorDetails=null;
		try {
			monitorDetails=monitorDetailsRepository.findByRecomidAndWoredaidAndBitStatus(recId,woredaId,false);
		}catch(Exception e){
			LOG.error("MobileServiceImpl::getMonitorDetailsByRecIdAndWoredaId():" + e);
		}
		return monitorDetails;
	}

	@Transactional
	@Override
	public String saveRustIncident1(MobileReqBean loginReqDto) {
		String result = null;
		RustIncidentEntity incident = null;
		RustIncidentDetailsEntity incidentDtl = null;
		try {

			DemographicEntity d = new DemographicEntity();
			d.setDemographyId(loginReqDto.getDemographyId());
			
			SeasionMasterEntity s = new SeasionMasterEntity();
			s.setIntSeasoonId(loginReqDto.getSeasonId());
			
			
			List<RustIncidentEntity> list=rustIncidentRepository.findByYearAndSeasonIdAndDemographyIdAndUserId(getYearFromDate(loginReqDto.getDate()),s,d,loginReqDto.getUserId());
			
			
			//New rust incident to add
			if(list==null || list.isEmpty()) {
				incident = new RustIncidentEntity();
				incident.setUserId(loginReqDto.getUserId());
				incident.setRustDate(DateUtil.StringToDate(loginReqDto.getDate(), WrsisPortalConstant.DATE_FORMAT_DDMMYYYY));

				incident.setDemographyId(d);
				incident.setSeasonId(s);
				
				incident.setYear(getYearFromDate(loginReqDto.getDate()));
				incident.setLongitude(loginReqDto.getLongitude());
				incident.setLatitude(loginReqDto.getLatitude());
				incident.setCreatedOn(new Timestamp(new Date().getTime()));
				incident.setCreatedBy(loginReqDto.getUserId());
				incident.setBitStatus(false);

				incident = rustIncidentRepository.saveAndFlush(incident);
				// store geometric value in data table
				rustIncidentRepository.updateGeoLocation(incident.getIncidentId());

				for (QueAnsDetails queDto : loginReqDto.getQueAnsDetails()) {

					incidentDtl = new RustIncidentDetailsEntity();
					incidentDtl.setIncidentId(incident.getIncidentId());
					
					Question que = new Question();
					que.setQustionId(queDto.getQueId());
					incidentDtl.setQuestionId(que);
					
					incidentDtl.setOptionValue(queDto.getOptId());
					incidentDtl.setBitStatus(false);

					rustIncidentDetailsRepository.saveAndFlush(incidentDtl);
				}
				result = WrsisPortalConstant.SAVE;
			
			}else{//Rust incident to update
				
				incident= list.get(0);
				
				incident.setLongitude(loginReqDto.getLongitude());
				incident.setLatitude(loginReqDto.getLatitude());
				incident.setRustDate(DateUtil.StringToDate(loginReqDto.getDate(), WrsisPortalConstant.DATE_FORMAT_DDMMYYYY));
				incident.setBitStatus(false);
				
				rustIncidentRepository.saveAndFlush(incident);
				// store geometric value in data table
				rustIncidentRepository.updateGeoLocation(incident.getIncidentId());
				
				if(loginReqDto.getQueAnsDetails()!=null && !loginReqDto.getQueAnsDetails().isEmpty()) {
					// delete exist question answers
					List<RustIncidentDetailsEntity> qList=rustIncidentDetailsRepository.findByIncidentId(incident.getIncidentId());
					for(RustIncidentDetailsEntity qObj:qList) {
						qObj.setBitStatus(true);
						rustIncidentDetailsRepository.saveAndFlush(qObj);
					}
					boolean newFlag=true;
					//Incoming questions
					for (QueAnsDetails queDto : loginReqDto.getQueAnsDetails()) {
						
						newFlag=true;
						for(RustIncidentDetailsEntity qObj:qList) {
							
							if(queDto.getQueId()==qObj.getQuestionId().getQustionId()) {
								qObj.setOptionValue(queDto.getOptId());
								qObj.setBitStatus(false);
								rustIncidentDetailsRepository.saveAndFlush(qObj);
								newFlag=false;
								break;
							}
							
						}
						
						if(newFlag){ //If new question comes, will add here
							incidentDtl = new RustIncidentDetailsEntity();
							incidentDtl.setIncidentId(incident.getIncidentId());
							
							Question que = new Question();
							que.setQustionId(queDto.getQueId());
							incidentDtl.setQuestionId(que);
							
							incidentDtl.setOptionValue(queDto.getOptId());
							incidentDtl.setBitStatus(false);

							rustIncidentDetailsRepository.saveAndFlush(incidentDtl);
						}
					}// end of incoming questions
				
				}
				
				result = WrsisPortalConstant.UPDATE;
			}//end of rust incident update else block
			
			
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;
			LOG.error("MobileServiceImpl::saverustIncident():" + e);
		}
		return result;
	}

	@Override
	public Boolean checkSeasonLatLongDuplicate(Integer seasonId, String latitude, String longitude) {
		Boolean result = false;
		try {
			List<SurveyDetails> list = surveyDetailsRepository.checkSeasonLatLongDuplicate(seasonId, latitude,
					longitude);
			if (!list.isEmpty()) {
				result = true;
			}
		} catch (Exception e) {
			LOG.error("MobileServiceImpl::checkSeasonLatLongDuplicate():" + e);
		}
		return result;
	}
}
