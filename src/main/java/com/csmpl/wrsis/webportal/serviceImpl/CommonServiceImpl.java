package com.csmpl.wrsis.webportal.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.FungicideDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SampleDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SampleSurveyDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.SurveyCapturedImageEntity;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.entity.SurveyOtherDiseaseEntity;
import com.csmpl.wrsis.webportal.entity.SurveyOtherEntity;
import com.csmpl.wrsis.webportal.entity.SurveyRustDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;
import com.csmpl.wrsis.webportal.entity.SurveyorDetailsEntity;
import com.csmpl.wrsis.webportal.entity.WRSurveyOtherDiseaseEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideDetailsRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeverityRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SurveyCapturedImageRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.repository.SurveyOtherRepository;
import com.csmpl.wrsis.webportal.repository.SurveyRustDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WRSurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.repository.WheatMasterRepository;
import com.csmpl.wrsis.webportal.service.SurveyDiscardLogService;

@Service("commonService")
public class CommonServiceImpl implements com.csmpl.wrsis.webportal.service.CommonService {

	private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);
	@Autowired
	LevelDetailsRepository levelDetailsRepository;

	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	SurveySiteRepository surveySiteRepository;

	@Autowired
	SurveyOtherRepository surveyOtherRepository;

	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;

	@Autowired
	SurveyOtherDiseaseRepository surveyOtherDiseaseRepository;

	@Autowired
	WRSurveyOtherDiseaseRepository WRSurveyOtherDiseaseRepository;

	@Autowired
	SurveyRustDetailsRepository surveyRustDetailsRepository;

	@Autowired
	FungicideDetailsRepository fungicideDetailsRepository;

	@Autowired
	SampleDetailsRepository sampleDetailsRepository;

	@Autowired
	SampleSurveyDetailsRepository sampleSurveyDetailsRepository;

	@Autowired
	SurveyCapturedImageRepository surveyCapturedImageRepository;

	@Autowired
	SeverityRepository severityRepository;
	@Autowired
	SurveyDiscardLogService surveyDiscardLogService;

	@Override
	public List<Object[]> getSelectedInstitutionNam(Integer userId) {
		return levelDetailsRepository.getInstituonName(userId);
	}

	@Override
	public List<Object[]> getAllSurveyor(Integer researchCenterId) {
		return levelDetailsRepository.getAllSurveyor(researchCenterId);
	}

	@Transactional
	@Override
	public String saveSurveyDetails(String encodeJSON, Integer surveyIdd, Integer userId, Integer collectMode,
			Integer fileSurveyId, Integer updatedBy) throws JSONException {

		JSONObject response = new JSONObject();
		String decodeJSON = new String(Base64.getDecoder().decode(encodeJSON.getBytes()));
		try {
			JSONObject surveyJson = new JSONObject(decodeJSON);

			// if surveyIdd is not 0, means update else insert.
			// fecth the survey details by surveyId first

			// survey information
			boolean insert = false;
			SurveyDetails surveyDetails = new SurveyDetails();
			if (surveyIdd != null && surveyIdd != 0) {
				insert = true;
				surveyDetails = surveyDetailsRepository.findBySurveyId(surveyIdd);
				// delete the respective records i respective table using surveyId

				surveySiteRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				surveyOtherRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				surveyorDetailsRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				surveyOtherDiseaseRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				WRSurveyOtherDiseaseRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				surveyRustDetailsRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				fungicideDetailsRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				sampleDetailsRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				sampleSurveyDetailsRepository.deleteBySurveyId(surveyDetails.getSurveyId());
				surveyCapturedImageRepository.deleteBySurveyId(surveyDetails.getSurveyId());

			}

			surveyDetails.setActionBy(0);
			surveyDetails.setActionRemark("");
			surveyDetails.setAltitude(surveyJson.getString(WrsisPortalConstant.ALTITUDE_ID));
			surveyDetails.setBitstatus(false);
			if (surveyIdd == null || surveyIdd == 0) {
				surveyDetails.setCollecMode(collectMode);
			}
			if (!(surveyIdd != null && surveyIdd != 0)) {

				surveyDetails.setCreatedBy(userId);
				surveyDetails.setCreatedOn(new Date());
			}
			surveyDetails.setFarmerContacted(
					(surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID)) ? surveyJson.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID) : false);
			Boolean b = null;
			try {
				b = surveyJson.getBoolean(WrsisPortalConstant.FUNGISIDE_ID);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::saveSurveyDetails():" + e);
			} // handled cast and null exception
			if (surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID)) {
				surveyDetails.setFungicideApplied(b);
			} else {
				surveyDetails.setFungicideApplied(false);
			}
			surveyDetails.setImageCaptured(
					(surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID)) ? surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID) : false);
			surveyDetails.setKebelId(Integer.valueOf(surveyJson.getString(WrsisPortalConstant.KEBELE_ID)));

			surveyDetails.setDecimaldegree(surveyJson.getBoolean(WrsisPortalConstant.DECIMAL_DEGREE));
			if (surveyJson.getBoolean(WrsisPortalConstant.DECIMAL_DEGREE)) {
				surveyDetails.setLatitude(surveyJson.getString(WrsisPortalConstant.LATITUDE_ID));
				surveyDetails.setLongitude(surveyJson.getString(WrsisPortalConstant.LONGITUDE_ID));
				surveyDetails.setDegreeLatitude("");
				surveyDetails.setDegreeLongitude("");
			} else {
				String longitude = surveyJson.getString(WrsisPortalConstant.LONGITUDE_ID);
				String latitude = surveyJson.getString(WrsisPortalConstant.LATITUDE_ID);

				String[] longs = longitude.trim().split("\\.");
				String[] lats = latitude.trim().split("\\.");

				surveyDetails.setLatitude(convertToDecimal(lats));
				surveyDetails.setLongitude(convertToDecimal(longs));
				surveyDetails.setDegreeLatitude(latitude);
				surveyDetails.setDegreeLongitude(longitude);

			}

			
			surveyDetails.setRemarks(surveyJson.getString("remark"));
			surveyDetails.setLocationDetails(surveyJson.getString(WrsisPortalConstant.KEBEL_WOREDA_ADDR));
			// othersurveyor
			surveyDetails.setOtherSurveyor(surveyJson.getString(WrsisPortalConstant.OTHER_SURVEYOR));
			
			surveyDetails.setRustAffected(surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID));
			surveyDetails.setSampleCollected(
					(surveyJson.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID)) ? surveyJson.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID) : false);
			surveyDetails.setSeasonId(Integer.valueOf(surveyJson.getString(WrsisPortalConstant.SEASON_ID)));
			surveyDetails.setSurveyDate(convertStringToDate(surveyJson.getString(WrsisPortalConstant.SURVEY_DATE_ID)));
			surveyDetails.setSurveyStatus(0);
			surveyDetails.setUpdatedBy(updatedBy);
			surveyDetails.setUpdatedOn(new Date());

			surveyDetails.setWordeaId(Integer.valueOf(surveyJson.getString(WrsisPortalConstant.WOREDA_ID)));
			surveyDetails.setZoneId(Integer.valueOf(surveyJson.getString(WrsisPortalConstant.ZONE_ID)));
			surveyDetails.setRegionId(Integer.valueOf(surveyJson.getString(WrsisPortalConstant.REGION_ID)));
			if (fileSurveyId != null) {
				surveyDetails.setFileId(fileSurveyId);
			}
			surveyDetails.setFarmerContacted(
					(surveyJson.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID)) ? surveyJson.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID) : false);
			if(surveyJson.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID))
			{
				if (surveyJson.getString(WrsisPortalConstant.OBSERVATION_ID).trim().length() > 0) {
					surveyDetails.setObserveDate(convertStringToDate(surveyJson.getString(WrsisPortalConstant.OBSERVATION_ID)));
				}
				if (surveyJson.getString(WrsisPortalConstant.PLANTING_DATE).trim().length() > 0) {
					surveyDetails.setPlanningDate(convertStringToDate(surveyJson.getString(WrsisPortalConstant.PLANTING_DATE)));
				}
				surveyDetails.setTilleringDate(convertStringToDate(surveyJson.getString("tilleringdate")));
			}
			surveyDetails = surveyDetailsRepository.save(surveyDetails);
			Integer surveyId = surveyDetails.getSurveyId();
			surveyDetailsRepository.updateGeoLocation(surveyId);
			surveyDetails.setSurveyNumber("S" + surveyDetails.getSurveyId());

			// site infomation

			// siteInformation

			JSONObject siteInformation = surveyJson.getJSONObject(WrsisPortalConstant.SITE_INFORMATION1);
			SurveySiteEntity serveySite = new SurveySiteEntity();
			serveySite.setArea((siteInformation.has(WrsisPortalConstant.SITE_AREA)) ? siteInformation.getString(WrsisPortalConstant.SITE_AREA) : "0");
			serveySite.setBitstatus(false);
			serveySite.setCreatedBy(0);
			serveySite.setCreatedOn(new Date());
			serveySite.setSiteTypeId(
					(siteInformation.has("surveySite")) ? Integer.valueOf(siteInformation.getString("surveySite"))
							: -1);
			serveySite.setSurveyId(surveyId);
			serveySite.setSurveyNumber(surveyDetails.getSurveyNumber());
			serveySite.setUpdatedBy(0);
			serveySite.setUpdatedOn(new Date());
			serveySite.setWheatTypeId(
					(siteInformation.has("wheatType")) ? Integer.valueOf(siteInformation.getString("wheatType")) : -1);
			serveySite.setGrowthId(
					(siteInformation.has("growthStage")) ? Integer.valueOf(siteInformation.getString("growthStage"))
							: -1);
			serveySite.setVarity((siteInformation.has(WrsisPortalConstant.VARITY)) ? siteInformation.getString(WrsisPortalConstant.VARITY) : "-1");
			serveySite.setOtherVariety(
					(siteInformation.has(WrsisPortalConstant.OTHER_VARIETY)) ? siteInformation.getString(WrsisPortalConstant.OTHER_VARIETY) : "");
			serveySite.setOtherWheat(
					(siteInformation.has("OtherWheat") ? siteInformation.getString("OtherWheat") : ""));
		
			
			surveySiteRepository.save(serveySite);

			// survey other details

			SurveyOtherEntity surveyOther = new SurveyOtherEntity();
			JSONObject otherDetails = surveyJson.getJSONObject("otherDetails");
			surveyOther.setBitstatus(false);
			surveyOther.setCreatedBy(userId);
			surveyOther.setCreatedOn(new Date());
			surveyOther.setIrrigated(otherDetails.getBoolean("irrigated"));
			surveyOther.setMoistureId(
					(otherDetails.has("moisture")) ? Integer.valueOf(otherDetails.getString("moisture")) : -1);
			surveyOther.setSoilColorId(
					(otherDetails.has("soilColor")) ? Integer.valueOf(otherDetails.getString("soilColor")) : -1);
			surveyOther.setSurveyId(surveyId);
			surveyOther.setSurveyNo(surveyDetails.getSurveyNumber());
			surveyOther.setUpdatedBy(userId);
			surveyOther.setUpdatedOn(new Date());
			surveyOther.setWeedControl(otherDetails.getBoolean("weedcontrol"));

			surveyOtherRepository.save(surveyOther);

			// Surveyor Information

			// surveyorDetailsRepository

			JSONArray surveyorJson = surveyJson.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
			for (int i = 0; i < surveyorJson.length(); i++) {

				SurveyorDetailsEntity surveyorDetails = new SurveyorDetailsEntity();
				JSONObject json = surveyorJson.getJSONObject(i);
				surveyorDetails.setBitstatus(false);
				surveyorDetails.setUpdatedOn(new Date());
				surveyorDetails.setCreatedBy(userId);
				surveyorDetails.setUpdatedBy(userId);
				surveyorDetails.setCreatedOn(new Date());
				surveyorDetails.setCenterId(Integer.valueOf(json.getString("researchId")));
				surveyorDetails.setCountryId(Integer.valueOf(json.getString(WrsisPortalConstant.COUNTRY)));
				surveyorDetails.setSurveyId(surveyId);
				surveyorDetails.setSurveyNumber(surveyDetails.getSurveyNumber());
				surveyorDetails.setSurveyorName(json.getString("surveyorName"));
				surveyorDetails.setSurveyorUserId(Integer.valueOf(json.getString("userId")));

				surveyorDetailsRepository.save(surveyorDetails);

			}

			// Other Disease
			// surveyOtherDiseaseRepository

			if (surveyJson.has("otherDisease")) {
				JSONArray otherDiseaseJsa = surveyJson.getJSONArray("otherDisease");
				for (int i = 0; i < otherDiseaseJsa.length(); i++) {

					SurveyOtherDiseaseEntity otherDisease = new SurveyOtherDiseaseEntity();
					JSONObject json = otherDiseaseJsa.getJSONObject(i);
					otherDisease.setBitstatus(false);
					otherDisease.setCreatedBy(userId);
					otherDisease.setCreatedOn(new Date());
					otherDisease.setDiseaseId(Integer.valueOf(json.getString(WrsisPortalConstant.DISEASE_TYPE_ID)));
					otherDisease.setIncident(json.getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
					otherDisease.setSeverity((json.getString(WrsisPortalConstant.OTH_SEVERITY_VAL).trim().length() == 1)
							? "0" + json.getString(WrsisPortalConstant.OTH_SEVERITY_VAL)
							: json.getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					otherDisease.setSurveyId(surveyId);
					otherDisease.setSurveyNo(surveyDetails.getSurveyNumber());
					otherDisease.setUpdatedBy(userId);
					otherDisease.setUpdatedOn(new Date());

					surveyOtherDiseaseRepository.save(otherDisease);
				}
			}

			// Any other disease
			// "anyOtherDiseaseJsa"
			// "diseaseId"
			if (surveyJson.has(WrsisPortalConstant.ANY_OTHER_DISEASE_JSA)) {
				JSONArray anyOtherDiseaseJsa = surveyJson.getJSONArray(WrsisPortalConstant.ANY_OTHER_DISEASE_JSA);
				for (int i = 0; i < anyOtherDiseaseJsa.length(); i++) {
					JSONObject json = anyOtherDiseaseJsa.getJSONObject(i);

					WRSurveyOtherDiseaseEntity wr = new WRSurveyOtherDiseaseEntity();
					wr.setBitstatus(false);
					wr.setCreatedBy(userId);
					wr.setCreatedOn(new Date());
					wr.setDiseaseId(Integer.valueOf(json.getString(WrsisPortalConstant.DISEASE_ID)));
					wr.setSurveyId(surveyId);
					wr.setSurveyNo(surveyDetails.getSurveyNumber());
					wr.setUpdatedBy(userId);
					wr.setUpdatedOn(new Date());

					WRSurveyOtherDiseaseRepository.save(wr);
				}
			}

			// rustDetails
			// SurveyRustDetailsEntity
			// surveyRustDetailsRepository

			JSONArray rustDetails = surveyJson.getJSONArray("rustDetails");
			for (int i = 0; i < rustDetails.length(); i++) {
				JSONObject json = rustDetails.getJSONObject(i);

				SurveyRustDetailsEntity ent = new SurveyRustDetailsEntity();
				ent.setBitstatus(false);
				ent.setCreatedBy(userId);
				ent.setCreatedOn(new Date());
				ent.setIncident(json.getString(WrsisPortalConstant.INCIDENT_ID));
				ent.setRustTypeId(Integer.valueOf(json.getString(WrsisPortalConstant.RUST_TYPE_ID)));
				ent.setRustReactionId(Integer.valueOf(json.getString("reaction")));
				ent.setSeverity(json.getString(WrsisPortalConstant.SEVERITY_ID));
				ent.setSurveyId(surveyId);
				ent.setSurveyNo(surveyDetails.getSurveyNumber());
				ent.setUpdatedBy(userId);
				ent.setUpdatedOn(new Date());
				ent.setHeadIncident(json.getString("headIncidentId"));
				ent.setHeadSeverity(json.getString("headSeverityId"));
			
				surveyRustDetailsRepository.save(ent);
			}

			if (surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID) && surveyJson.has(WrsisPortalConstant.FUNGICIDE_JSON)) {
				// fungicide
				// fungicideJson
				JSONObject fungicideJson = surveyJson.getJSONObject(WrsisPortalConstant.FUNGICIDE_JSON);
				// fungicideDetailsRepository

				FungicideDetailsEntity ent = new FungicideDetailsEntity();
				ent.setBitstatus(false);
				ent.setCreatedBy(userId);
				ent.setCreatedOn(new Date());
				ent.setUpdatedBy(userId);
				ent.setUpdatedOn(new Date());
				
				ent.setDose((fungicideJson.has(WrsisPortalConstant.DOSE) 
						&& !"".equals(fungicideJson.getString(WrsisPortalConstant.DOSE))) 
						? fungicideJson.getString(WrsisPortalConstant.DOSE) : "0");
				
				ent.setFungEffectId((fungicideJson.has(WrsisPortalConstant.EFFECTIVE_NESS) 
						&& fungicideJson.getString(WrsisPortalConstant.EFFECTIVE_NESS)!= null 
						&& !"".equals(fungicideJson.getString(WrsisPortalConstant.EFFECTIVE_NESS))  )  
						? Integer.valueOf(fungicideJson.getString(WrsisPortalConstant.EFFECTIVE_NESS))
							: -1);
				
				ent.setFungicideTypeId( (fungicideJson.has(WrsisPortalConstant.FUNGI_USED) 
						&& fungicideJson.getString(WrsisPortalConstant.FUNGI_USED)!= null 
						&& !"".equals(fungicideJson.getString(WrsisPortalConstant.FUNGI_USED) ) )
						? Integer.valueOf(fungicideJson.getString(WrsisPortalConstant.FUNGI_USED)) : -1);
				
				ent.setOtherFungi((fungicideJson.has(WrsisPortalConstant.OTHER_FUNGI)) 
						? fungicideJson.getString(WrsisPortalConstant.OTHER_FUNGI) : "");
				
				ent.setSurveyId(surveyId);
				ent.setSurveyNo(surveyDetails.getSurveyNumber());
				
				ent.setSprayDate(
						(fungicideJson.has(WrsisPortalConstant.SPRAY_DATE) && !fungicideJson.getString(WrsisPortalConstant.SPRAY_DATE).trim().equals(""))
								? convertStringToDate(fungicideJson.getString(WrsisPortalConstant.SPRAY_DATE))
								: null);
				//System.err.println("ab"+ent);
				fungicideDetailsRepository.save(ent);
			}
			// "sampleDetails"
			// sampleDetailsRepository sampleSurveyDetailsRepository
			// SampleDetailsEntity
			// SampleSurveyDetailsEntity

			JSONArray sampleDetails = surveyJson.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);
			for (int i = 0; i < sampleDetails.length(); i++) {
				JSONObject json = sampleDetails.getJSONObject(i);

				SampleDetailsEntity sampleEnt = new SampleDetailsEntity();
				sampleEnt.setBitstatus(false);
				sampleEnt.setCreatedBy(userId);
				sampleEnt.setCreatedOn(new Date());
				sampleEnt.setRemarks(json.getString(WrsisPortalConstant.SAMPLE_REMARKS));
				sampleEnt.setSampleCount(Integer.valueOf(json.getString(WrsisPortalConstant.SAMPLE_COUNT_ID)));
				sampleEnt.setSampleTypeId(Integer.valueOf(json.getString(WrsisPortalConstant.SAMPLE_TYPE_ID)));
				sampleEnt.setSurveyId(surveyId);
				sampleEnt.setSurveyNo(surveyDetails.getSurveyNumber());
				sampleEnt.setUpdatedBy(userId);
				sampleEnt.setUpdatedOn(new Date());

				sampleEnt = sampleDetailsRepository.save(sampleEnt);

				String ids[] = json.getString(WrsisPortalConstant.SAMPLE_IDS).split(",");
				for (String id : ids) {
					if (id.isEmpty() || id.trim().equalsIgnoreCase("")) {
						continue;
					}
					SampleSurveyDetailsEntity sampleSurvey = new SampleSurveyDetailsEntity();
					sampleSurvey.setBitstatus(false);
					sampleSurvey.setCreatedBy(userId);
					sampleSurvey.setCreatedOn(new Date());
					sampleSurvey.setSampleId(sampleEnt.getSampleId());
					sampleSurvey.setSurveyId(surveyId);
					sampleSurvey.setSurveyNo(surveyDetails.getSurveyNumber());
					sampleSurvey.setUpdatedBy(userId);
					sampleSurvey.setUpdatedOn(new Date());
					sampleSurvey.setSampleValue(id);

					sampleSurveyDetailsRepository.save(sampleSurvey);
				}

			}

			// capture image
			if (surveyJson.getBoolean(WrsisPortalConstant.RUST_AFFECTED_ID) && surveyJson.getBoolean(WrsisPortalConstant.CAPTURED_IMAGE_ID) ) {
				JSONArray jsa = surveyJson.getJSONArray(WrsisPortalConstant.IMAGES);
				for (int i = 0; i < jsa.length(); i++) {

					List<SurveyCapturedImageEntity> bean1 = surveyCapturedImageRepository
							.checkImageName(jsa.getString(i));
					if (bean1 == null || bean1.size() == 0) {
						SurveyCapturedImageEntity bean = new SurveyCapturedImageEntity();
						bean.setBitstatus(false);
						bean.setCreatedBy(userId);
						bean.setCreatedOn(new Date());
						bean.setImageName(jsa.getString(i));
						bean.setSurveyId(surveyId);
						bean.setSurveyNo(surveyDetails.getSurveyNumber());
						bean.setUpdatedBy(userId);
						bean.setUpdatedOn(new Date());

						surveyCapturedImageRepository.save(bean);
					}
				}

			}

			if (!insert) {

				ApprovalAuthorityEntity ae = new ApprovalAuthorityEntity();
				ApprovalAuthorityHistoryEntity ahe = new ApprovalAuthorityHistoryEntity();
				List<Object[]> obj = approvalAuthorityEntityRepository.fetchApproveDetails(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID));
				ae.setAprovalPId(Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID));
				ae.setPendingAt((Integer) obj.get(0)[2]);
				ae.setSentFrom(userId);
				ae.setStageNo((Integer) obj.get(0)[1]);
				ae.setSurveyId(surveyId);
				ae.setStatus(false);
				approvalAuthorityEntityRepository.save(ae);

				ahe.setAprovalPId(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.APPROVAL_PROCESS_ID));
				ahe.setPendingAt((Integer) obj.get(0)[2]);
				ahe.setSentFrom(userId);
				ahe.setStageNo((Integer) obj.get(0)[1]);
				ahe.setSurveyId(surveyId);
				ahe.setStatus(false);
				approvalAuthorityHistoryEntityRepository.save(ahe);

			}

			response.put("Status", "Survey details saved Successfully.");
			response.put("StatusCode", 200);
			response.put("sNo", surveyDetails.getSurveyNumber());
			return response.toString();
		} catch (Exception e) {
			LOG.error("CommonServiceImpl::saveSurveyDetails():" + e);
			response.put("Status", "Unable to save the details.");
			response.put("StatusCode", 400);
			return response.toString();
		}

	}

	private String convertToDecimal(String[] longs) {

		int degree = Integer.parseInt(longs[0]);
		int minutes = Integer.parseInt(longs[1]);
		int secs = Integer.parseInt(longs[2]);

		double decimal = (minutes / 60.0) + (secs / 3600.0);
		double answer = degree + decimal;

		return String.valueOf(answer);
	}

	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;

	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;

	public static Date convertStringToDate(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("CommonServiceImpl::convertStringToDate():" + ex);
		}
		return date;
	}

	@Override
	public String deleteSurveyDetails(int surveyId) {

		try {
			surveyDetailsRepository.deleteSurveyDetails(surveyId);
			return "Deleted successfully.";
		} catch (Exception e) {
			LOG.error("CommonServiceImpl::deleteSurveyDetails():" + e);

			return "Unable to delete due to some internal error.";
		}
	}

	@Autowired
	SiteTypeRepository siteTypeRepository;

	@Autowired
	GrowthRepository growthRepository;

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@Autowired
	WheatMasterRepository wheatMasterRepository;

	@Autowired
	SoilColorRepository soilColorRepository;

	@Autowired
	MoistureRepository moistureRepository;

	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@Autowired
	SampleTypeRepository sampleTypeRepository;

	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;

	@Autowired
	FungicideMasterRepository fungicideMasterRepository;

	@Autowired
	FungiEffectTypeRepository fungiEffectTypeRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@Autowired
	ReactionTypeRepository reactionTypeRepository;

	@Override
	public JSONObject getFormmatedJsonBySurveyId(Integer surveyId) throws JSONException {
		JSONObject json = new JSONObject();

		Optional<SurveyDetails> surveyDetails = surveyDetailsRepository.findById(surveyId);

		if (surveyDetails.get() != null) {
			json.put(WrsisPortalConstant.PLANTING_DATE,
					(surveyDetails.get().getPlanningDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getPlanningDate())
							: "");
			json.put(WrsisPortalConstant.WOREDA_ID, surveyDetails.get().getWordeaId());
			json.put("remark", surveyDetails.get().getRemarks());
			json.put(WrsisPortalConstant.WOREDA_NAME1, surveyDetails.get().getWoreda().getDemographyName());
			json.put(WrsisPortalConstant.SEASION_NAME, surveyDetails.get().getSeasion().getVchSeason());
			json.put(WrsisPortalConstant.CONTACTED_FARMER_ID, surveyDetails.get().isFarmerContacted());
			json.put(WrsisPortalConstant.ZONE_NAME1, surveyDetails.get().getZone().getDemographyName());
			json.put(WrsisPortalConstant.OBSERVATION_ID,
					(surveyDetails.get().getObserveDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getObserveDate())
							: "");
			json.put(WrsisPortalConstant.SEASON_ID, surveyDetails.get().getSeasion().getIntSeasoonId());
			json.put(WrsisPortalConstant.REGION_NAME1, surveyDetails.get().getRegion().getDemographyName());
			json.put(WrsisPortalConstant.ZONE_ID, surveyDetails.get().getZone().getDemographyId());
			// publish date for UK met global
			json.put("publishdate",
					(surveyDetails.get().getActionOn() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getActionOn())
							: "");
			Boolean fungicide = null;
			try {
				fungicide = surveyDetails.get().isFungicideApplied();
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			} // handle the nullpointer exception fungicide
			json.put(WrsisPortalConstant.FUNGISIDE_ID, fungicide);
			json.put(WrsisPortalConstant.ALTITUDE_ID, surveyDetails.get().getAltitude());
			json.put(WrsisPortalConstant.CAPTURED_IMAGE_ID, surveyDetails.get().isImageCaptured());
			json.put(WrsisPortalConstant.KEBELE_NAME, surveyDetails.get().getKebele().getDemographyName());
			json.put(WrsisPortalConstant.SURVEY_DATE_ID, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getSurveyDate()));
			json.put("tilleringdate",
					(surveyDetails.get().getTilleringDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getTilleringDate())
							: "");

			json.put(WrsisPortalConstant.REGION_ID, surveyDetails.get().getRegionId());
			json.put(WrsisPortalConstant.SAMPLE_COLLECTED_ID, surveyDetails.get().isSampleCollected());
			json.put(WrsisPortalConstant.KEBELE_ID, surveyDetails.get().getKebelId());
			json.put(WrsisPortalConstant.RUST_AFFECTED_ID, surveyDetails.get().isRustAffected());
			json.put(WrsisPortalConstant.SURVEY_NO, surveyDetails.get().getSurveyNumber());
			json.put(WrsisPortalConstant.KEBEL_WOREDA_ADDR, surveyDetails.get().getLocationDetails());
			json.put(WrsisPortalConstant.OTHER_SURVEYOR, surveyDetails.get().getOtherSurveyor());
			json.put(WrsisPortalConstant.LATITUDE_1ID, "");
			json.put(WrsisPortalConstant.LONGITUDE_1ID, "");

			Boolean latLongType = surveyDetails.get().getDecimaldegree();
			json.put(WrsisPortalConstant.DECIMAL_DEGREE, latLongType);
			if (latLongType) {
				json.put(WrsisPortalConstant.LATITUDE_ID,
						(surveyDetails.get().getLatitude() == null) ? "0.0" : surveyDetails.get().getLatitude());
				json.put(WrsisPortalConstant.LONGITUDE_ID,
						(surveyDetails.get().getLongitude() == null) ? "0.0" : surveyDetails.get().getLongitude());
			} else {

				json.put(WrsisPortalConstant.LATITUDE_ID, (surveyDetails.get().getDegreeLatitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLatitude());
				json.put(WrsisPortalConstant.LONGITUDE_ID, (surveyDetails.get().getDegreeLongitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLongitude());
			}

			// site info
			try {
				SurveySiteEntity siteEntity = surveySiteRepository.findBySurveyIdId(surveyId);
				JSONObject siteObj = new JSONObject();
				siteObj.put(WrsisPortalConstant.SITE_AREA, (siteEntity != null) ? siteEntity.getArea() : "");
				siteObj.put("surveySite", (siteEntity != null) ? siteEntity.getSiteTypeId() : -1);
				siteObj.put(WrsisPortalConstant.SURVEY_SITE_NAME,
						(siteEntity != null && siteEntity.getSiteTypeId() != null && siteEntity.getSiteTypeId() != -1)
								? siteTypeRepository.getOne(siteEntity.getSiteTypeId()).getVchSiteName()
								: WrsisPortalConstant.NA);
				siteObj.put("growthStage", (siteEntity != null) ? siteEntity.getGrowthId() : -1);
				siteObj.put(WrsisPortalConstant.WHEAT_TYPE_NAME,
						(siteEntity != null && siteEntity.getWheatTypeId() != null && siteEntity.getWheatTypeId() != -1)
								? wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName()
								: WrsisPortalConstant.NA);
				siteObj.put(WrsisPortalConstant.GROWTH_STAGE_NAME,
						(siteEntity != null && siteEntity.getGrowthId() != null && siteEntity.getGrowthId() != -1)
								? growthRepository.getOne(siteEntity.getGrowthId()).getVchStage()
								: WrsisPortalConstant.NA);
				siteObj.put(WrsisPortalConstant.VARITY_NAME1,
						(siteEntity != null && siteEntity.getVarity() != null
								&& siteEntity.getVarity().trim().length() > 0
								&& (!siteEntity.getVarity().trim().equals("null")))
										? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
												.getVchDesc()
										: "-1");
				siteObj.put("wheatType", (siteEntity != null) ? siteEntity.getWheatTypeId() : -1);
				siteObj.put(WrsisPortalConstant.VARITY, (siteEntity != null) ? siteEntity.getVarity() : WrsisPortalConstant.NA);
				siteObj.put(WrsisPortalConstant.OTHER_VARIETY, (siteEntity != null) ? siteEntity.getOtherVariety() : WrsisPortalConstant.NA);
				/*
				 * siteObj.put("WheatName", (siteEntity != null && siteEntity.getWheatTypeId()
				 * != null && siteEntity.getWheatTypeId() != -1) ?
				 * wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName() :
				 * WrsisPortalConstant.NA);
				 */
				siteObj.put("OtherWheat", (siteEntity != null) ? siteEntity.getOtherWheat() : WrsisPortalConstant.NA);;
				
				// varite name for mobile
				siteObj.put("varityNameMobile",
						(siteEntity != null && siteEntity.getVarity() != null
								&& siteEntity.getVarity().trim().length() > 0
								&& (!siteEntity.getVarity().trim().equals("null")))
										? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
												.getVchDesc()
										: "-1");
				if (siteObj.getString(WrsisPortalConstant.VARITY_NAME1).trim().toLowerCase().equals("other")) {
					siteObj.put(WrsisPortalConstant.VARITY_NAME1, siteEntity.getOtherVariety());
				}
				if (siteObj.getString("WheatTypeName").trim().toLowerCase().equals("other")) {
					siteObj.put("WheatTypeName", siteEntity.getOtherWheat());
				}

				json.put(WrsisPortalConstant.SITE_INFORMATION1, siteObj);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

				 
			}

			// other details
			try {
				SurveyOtherEntity surveyOther = surveyOtherRepository.findBySurveyIdOne(surveyId);
				JSONObject surOtherJson = new JSONObject();
				surOtherJson.put("soilColor", surveyOther.getSoilColorId());
				surOtherJson.put("weedcontrol", surveyOther.isWeedControl());
				surOtherJson.put("moisture", surveyOther.getMoistureId());
				surOtherJson.put("irrigated", surveyOther.isIrrigated());
				surOtherJson.put(WrsisPortalConstant.SOIL_NAME,
						(surveyOther.getSoilColorId() != null && surveyOther.getSoilColorId() != -1)
								? soilColorRepository.getOne(surveyOther.getSoilColorId()).getVchSoilColor()
								: WrsisPortalConstant.NA);
				surOtherJson.put(WrsisPortalConstant.MOISTURE_NAME,
						(surveyOther.getMoistureId() != null && surveyOther.getMoistureId() != -1)
								? moistureRepository.getOne(surveyOther.getMoistureId()).getVchMoisture()
								: WrsisPortalConstant.NA);

				json.put("otherDetails", surOtherJson);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			}

			// surveyorJsa
			JSONArray jsa = new JSONArray();
			try {
				List<SurveyorDetailsEntity> surveyorDetails = surveyorDetailsRepository.findBySurveyId(surveyId);
				for (SurveyorDetailsEntity s : surveyorDetails) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put(WrsisPortalConstant.COUNTRY, s.getCountryId());
					jsonObj.put(WrsisPortalConstant.INSTITUTION_NAME1,
							(s.getCenterId() != null && s.getCenterId() != -1)
									? researchCenterRepository.getOne(s.getCenterId()).getResearchCenterName()
									: WrsisPortalConstant.NA);
					jsonObj.put(WrsisPortalConstant.COUNTRY_NAME,
							(s.getCenterId() != null && s.getCenterId() != -1)
									? demographicRepository.getOne(s.getCountryId()).getDemographyName()
									: WrsisPortalConstant.NA);
					jsonObj.put("surveyorName", s.getSurveyorName());
					jsonObj.put("researchId",
							(s.getCenterId() != null && s.getCenterId() != -1) ? s.getCenterId() : -1);
					jsonObj.put("userId",
							(s.getSurveyorUserId() != null && s.getSurveyorUserId() != -1) ? s.getSurveyorUserId()
									: -1);
					jsa.put(jsonObj);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			}
			json.put(WrsisPortalConstant.SURVEYOR_JSA, jsa);

			// sampleDetails
			List<SampleDetailsEntity> sampleEnt = sampleDetailsRepository.findBySurveyId(surveyId);
			jsa = new JSONArray();
			for (SampleDetailsEntity s : sampleEnt) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(WrsisPortalConstant.SAMPLE_REMARKS, s.getRemarks());
				jsonObj.put(WrsisPortalConstant.SAMPLE_COUNT_ID, s.getSampleCount());
				Integer sampleId = s.getSampleId();
				String sampleIds = "";
				List<SampleSurveyDetailsEntity> ids = sampleSurveyDetailsRepository.findBySurveySampleId(sampleId);
				for (int i = 0; i < ids.size(); i++) {
					sampleIds += ids.get(i).getSampleValue() + " , ";
				}
				sampleIds = sampleIds.substring(0, sampleIds.length() - 2);
				jsonObj.put(WrsisPortalConstant.SAMPLE_IDS, sampleIds);

				jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_ID, s.getSampleTypeId());
				SampleTypeMaster sampl = sampleTypeRepository.findBySampleId(s.getSampleTypeId());
				jsonObj.put(WrsisPortalConstant.RUST_ID, sampl.getRustTypeId());
				jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_NAME,
						(s.getSampleTypeId() != null && s.getSampleTypeId() != -1)
								? sampleTypeRepository.getOne(s.getSampleTypeId()).getSampleName()
								: WrsisPortalConstant.NA);

				jsa.put(jsonObj);
			}

			json.put(WrsisPortalConstant.SAMPLE_DETAILS, jsa);

			// otherDisease
			try {
				List<SurveyOtherDiseaseEntity> ent = surveyOtherDiseaseRepository.findBySurveyId(surveyId);

				jsa = new JSONArray();
				for (SurveyOtherDiseaseEntity s : ent) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_TYPE_ID, s.getDiseaseId());
					otherDiseaseJson.put(WrsisPortalConstant.OTH_INCIDENT_VAL, s.getIncident());
					otherDiseaseJson.put(WrsisPortalConstant.OTH_SEVERITY_VAL, s.getSeverity());
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
							(s.getDiseaseId() != null && s.getDiseaseId() != -1)
									? diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName()
									: WrsisPortalConstant.NA);
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			}
			json.put("otherDisease", jsa);

			// anyOtherDiseaseJsa
			jsa = new JSONArray();
			try {
				List<WRSurveyOtherDiseaseEntity> anyOther = WRSurveyOtherDiseaseRepository.findBySurveyId(surveyId);

				for (WRSurveyOtherDiseaseEntity s : anyOther) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_ID, s.getDiseaseId());
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
							diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName());
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			}
			json.put(WrsisPortalConstant.ANY_OTHER_DISEASE_JSA, jsa);

			jsa = new JSONArray();
			// fungicideJson
			try {
				FungicideDetailsEntity fungi = fungicideDetailsRepository.findBySurveyId(surveyId);
				JSONObject fun = new JSONObject();
				fun.put(WrsisPortalConstant.FUNGI_USED,
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
								? fungi.getFungicideTypeId()
								: -1);
				fun.put("effectiveness",
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungi.getFungEffectId()
								: -1);
				fun.put(WrsisPortalConstant.FUNGICIDE_NAME,
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
										? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
												.getFungicideName()
										: WrsisPortalConstant.NA);
				fun.put("dose",
						(fungi != null && fungi.getDose() != null && fungi.getDose().trim().length() > 0)
								? fungi.getDose()
								: "");
				fun.put(WrsisPortalConstant.EFFECTIVENESS_NAME,
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungiEffectTypeRepository.getOne(fungi.getFungEffectId()).getVchDesc()
								: WrsisPortalConstant.NA);
				fun.put(WrsisPortalConstant.SPRAY_DATE,
						(fungi != null && fungi.getSprayDate() != null)
								? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(fungi.getSprayDate())
								: "");
				// fungicide name for mobile
				fun.put("fungicideNameMobile",
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
										? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
												.getFungicideName()
										: WrsisPortalConstant.NA);
				json.put(WrsisPortalConstant.FUNGICIDE_JSON, fun);
				String otherFungi = "";
				try {
					otherFungi = fungi.getOtherFungi();
				} catch (Exception e) {
					LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

				}
				fun.put(WrsisPortalConstant.OTHER_FUNGI, otherFungi);
				if (fun.getString(WrsisPortalConstant.FUNGICIDE_NAME).trim().equalsIgnoreCase("other")) {
					fun.put(WrsisPortalConstant.FUNGICIDE_NAME, fungi.getOtherFungi());
				}
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

			}

			try {
				List<SurveyRustDetailsEntity> rusts = surveyRustDetailsRepository.findBySurveyId(surveyId);
				for (SurveyRustDetailsEntity s : rusts) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_ID, s.getRustTypeId());
					otherDiseaseJson.put("reaction", s.getRustReactionId());
					otherDiseaseJson.put(WrsisPortalConstant.REACTION_NAME,
							reactionTypeRepository.getOne(s.getRustReactionId()).getVchDesc());
					otherDiseaseJson.put(WrsisPortalConstant.INCIDENT_ID, s.getIncident());
					otherDiseaseJson.put(WrsisPortalConstant.SEVERITY_ID, s.getSeverity());
					otherDiseaseJson.put("headIncidentId", s.getHeadIncident());
					otherDiseaseJson.put("headSeverityId", s.getHeadSeverity());
					otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_NAME, rustTypeRepository.getOne(s.getRustTypeId()).getVchRustType());
					// severity name for UK met survey
					otherDiseaseJson.put("incidentName", severityRepository.getIncidentName(s.getSeverity()));
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

				 
			}
			json.put("rustDetails", jsa);

			jsa = new JSONArray();
			try {
				List<SurveyCapturedImageEntity> images = surveyCapturedImageRepository.findBySurveyId(surveyId);
				for (SurveyCapturedImageEntity s : images) {
					jsa.put(s.getImageName());
				}
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyId():" + e);

				 
			}
			json.put(WrsisPortalConstant.IMAGES, jsa);

			

		}

		return json;
	}
	

	@Override
	public Integer getCurrntSeasonId() {

		return surveyDetailsRepository.getCurrentSeasonId();
	}

	@Override
	public List<Object[]> getInstitutionNam() {
		return levelDetailsRepository.getInstituonName();
	}

	@Override
	public JSONObject getFormmatedJsonBySurveyIdForOtherDisease(Integer surveyId) throws JSONException {

		JSONObject json = new JSONObject();

		Optional<SurveyDetails> surveyDetails = surveyDetailsRepository.findById(surveyId);

		if (surveyDetails.get() != null) {
			json.put(WrsisPortalConstant.PLANTING_DATE,
					(surveyDetails.get().getPlanningDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getPlanningDate())
							: "");
			json.put(WrsisPortalConstant.WOREDA_ID, surveyDetails.get().getWordeaId());
			json.put("remark", surveyDetails.get().getRemarks());
			json.put(WrsisPortalConstant.WOREDA_NAME1, surveyDetails.get().getWoreda().getDemographyName());
			json.put(WrsisPortalConstant.SEASION_NAME, surveyDetails.get().getSeasion().getVchSeason());
			json.put(WrsisPortalConstant.CONTACTED_FARMER_ID, surveyDetails.get().isFarmerContacted());
			json.put(WrsisPortalConstant.ZONE_NAME1, surveyDetails.get().getZone().getDemographyName());
			json.put(WrsisPortalConstant.OBSERVATION_ID,
					(surveyDetails.get().getObserveDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getObserveDate())
							: "");

			json.put(WrsisPortalConstant.SEASON_ID, surveyDetails.get().getSeasion().getIntSeasoonId());
			json.put(WrsisPortalConstant.REGION_NAME1, surveyDetails.get().getRegion().getDemographyName());
			json.put(WrsisPortalConstant.LATITUDE_1ID, (surveyDetails.get().getLatitude().split("\\."))[1]);
			json.put(WrsisPortalConstant.ZONE_ID, surveyDetails.get().getZone().getDemographyId());
			json.put(WrsisPortalConstant.FUNGISIDE_ID, surveyDetails.get().isFungicideApplied());
			json.put(WrsisPortalConstant.ALTITUDE_ID, surveyDetails.get().getAltitude());
			json.put(WrsisPortalConstant.LONGITUDE_1ID, (surveyDetails.get().getLongitude().split("\\."))[1]);
			json.put(WrsisPortalConstant.CAPTURED_IMAGE_ID, surveyDetails.get().isImageCaptured());
			json.put(WrsisPortalConstant.KEBELE_NAME, surveyDetails.get().getKebele().getDemographyName());
			json.put(WrsisPortalConstant.SURVEY_DATE_ID, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getSurveyDate()));
			json.put(WrsisPortalConstant.REGION_ID, surveyDetails.get().getRegionId());
			json.put(WrsisPortalConstant.SAMPLE_COLLECTED_ID, surveyDetails.get().isSampleCollected());

			json.put(WrsisPortalConstant.LATITUDE_1ID, "");
			json.put(WrsisPortalConstant.LONGITUDE_1ID, "");

			
			

			Boolean latLongType = surveyDetails.get().getDecimaldegree();
			json.put(WrsisPortalConstant.DECIMAL_DEGREE, latLongType);
			if (latLongType) {
				json.put(WrsisPortalConstant.LATITUDE_ID,
						(surveyDetails.get().getLatitude() == null) ? "0.0" : surveyDetails.get().getLatitude());
				json.put(WrsisPortalConstant.LONGITUDE_ID,
						(surveyDetails.get().getLongitude() == null) ? "0.0" : surveyDetails.get().getLongitude());
			} else {

				json.put(WrsisPortalConstant.LATITUDE_ID, (surveyDetails.get().getDegreeLatitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLatitude());
				json.put(WrsisPortalConstant.LONGITUDE_ID, (surveyDetails.get().getDegreeLongitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLongitude());
			}
			json.put(WrsisPortalConstant.KEBELE_ID, surveyDetails.get().getKebelId());
			json.put(WrsisPortalConstant.RUST_AFFECTED_ID, surveyDetails.get().isRustAffected());
			json.put(WrsisPortalConstant.SURVEY_NO, surveyDetails.get().getSurveyNumber());

			// site info
			try {
				SurveySiteEntity siteEntity = surveySiteRepository.findBySurveyIdId(surveyId);
				JSONObject siteObj = new JSONObject();
				if (siteEntity != null) {
					siteObj.put(WrsisPortalConstant.SITE_AREA, siteEntity.getArea());
					siteObj.put("surveySite", siteEntity.getSiteTypeId());
					siteObj.put(WrsisPortalConstant.SURVEY_SITE_NAME,
							(siteEntity.getSiteTypeId() != null && siteEntity.getSiteTypeId() != -1)
									? siteTypeRepository.getOne(siteEntity.getSiteTypeId()).getVchSiteName()
									: WrsisPortalConstant.NA);
					siteObj.put("growthStage", siteEntity.getGrowthId());
					siteObj.put(WrsisPortalConstant.WHEAT_TYPE_NAME,
							(siteEntity.getWheatTypeId() != null && siteEntity.getWheatTypeId() != -1)
									? wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName()
									: WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.GROWTH_STAGE_NAME,
							(siteEntity.getGrowthId() != null && siteEntity.getGrowthId() != -1)
									? growthRepository.getOne(siteEntity.getGrowthId()).getVchStage()
									: WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.VARITY_NAME1,
							(siteEntity.getVarity() != null && siteEntity.getVarity().trim().length() > 0
									&& (!siteEntity.getVarity().trim().equals("null")))
											? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
													.getVchDesc()
											: "-1");
					siteObj.put("wheatType", siteEntity.getWheatTypeId());
					siteObj.put(WrsisPortalConstant.VARITY, siteEntity.getVarity());
				} else {
					siteObj.put(WrsisPortalConstant.SITE_AREA, "");
					siteObj.put("surveySite", "");
					siteObj.put(WrsisPortalConstant.SURVEY_SITE_NAME, WrsisPortalConstant.NA);
					siteObj.put("growthStage", "");
					siteObj.put(WrsisPortalConstant.WHEAT_TYPE_NAME, WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.GROWTH_STAGE_NAME, WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.VARITY_NAME1, "");
					siteObj.put("wheatType", "");
					siteObj.put(WrsisPortalConstant.VARITY, "");
				}
				json.put(WrsisPortalConstant.SITE_INFORMATION1, siteObj);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}

			// other details
			try {
				SurveyOtherEntity surveyOther = surveyOtherRepository.findBySurveyIdOne(surveyId);
				JSONObject surOtherJson = new JSONObject();
				surOtherJson.put("soilColor", surveyOther.getSoilColorId());
				surOtherJson.put("weedcontrol", surveyOther.isWeedControl());
				surOtherJson.put("moisture", surveyOther.getMoistureId());
				surOtherJson.put("irrigated", surveyOther.isIrrigated());
				surOtherJson.put(WrsisPortalConstant.SOIL_NAME,
						(surveyOther.getSoilColorId() != null && surveyOther.getSoilColorId() != -1)
								? soilColorRepository.getOne(surveyOther.getSoilColorId()).getVchSoilColor()
								: WrsisPortalConstant.NA);
				surOtherJson.put(WrsisPortalConstant.MOISTURE_NAME,
						(surveyOther.getMoistureId() != null && surveyOther.getMoistureId() != -1)
								? moistureRepository.getOne(surveyOther.getMoistureId()).getVchMoisture()
								: WrsisPortalConstant.NA);

				json.put("otherDetails", surOtherJson);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}

			// surveyorJsa
			JSONArray jsa = new JSONArray();
			try {
				List<SurveyorDetailsEntity> surveyorDetails = surveyorDetailsRepository.findBySurveyId(surveyId);
				for (SurveyorDetailsEntity s : surveyorDetails) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put(WrsisPortalConstant.COUNTRY, s.getCountryId());
					jsonObj.put(WrsisPortalConstant.INSTITUTION_NAME1,
							(s.getCenterId() != null && s.getCenterId() != -1)
									? researchCenterRepository.getOne(s.getCenterId()).getResearchCenterName()
									: WrsisPortalConstant.NA);
					jsonObj.put(WrsisPortalConstant.COUNTRY_NAME,
							(s.getCenterId() != null && s.getCenterId() != -1)
									? demographicRepository.getOne(s.getCountryId()).getDemographyName()
									: WrsisPortalConstant.NA);
					jsonObj.put("surveyorName", s.getSurveyorName());
					jsonObj.put("researchId",
							(s.getCenterId() != null && s.getCenterId() != -1) ? s.getCenterId() : -1);
					jsonObj.put("userId",
							(s.getSurveyorUserId() != null && s.getSurveyorUserId() != -1) ? s.getSurveyorUserId()
									: -1);
					jsa.put(jsonObj);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}
			json.put(WrsisPortalConstant.SURVEYOR_JSA, jsa);

			// sampleDetails
			List<SampleDetailsEntity> sampleEnt = sampleDetailsRepository.findBySurveyId(surveyId);
			jsa = new JSONArray();
			for (SampleDetailsEntity s : sampleEnt) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(WrsisPortalConstant.SAMPLE_REMARKS, s.getRemarks());
				jsonObj.put(WrsisPortalConstant.SAMPLE_COUNT_ID, s.getSampleCount());
				Integer sampleId = s.getSampleId();
				String sampleIds = "";
				List<SampleSurveyDetailsEntity> ids = sampleSurveyDetailsRepository.findBySurveySampleId(sampleId);
				for (int i = 0; i < ids.size(); i++) {
					sampleIds += ids.get(i).getSampleValue() + " , ";
				}
				sampleIds = sampleIds.substring(0, sampleIds.length() - 2);
				jsonObj.put(WrsisPortalConstant.SAMPLE_IDS, sampleIds);

				jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_ID, s.getSampleTypeId());
				SampleTypeMaster sampl = sampleTypeRepository.findBySampleId(s.getSampleTypeId());
				jsonObj.put(WrsisPortalConstant.RUST_ID, sampl.getRustTypeId());
				jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_NAME,
						(s.getSampleTypeId() != null && s.getSampleTypeId() != -1)
								? sampleTypeRepository.getOne(s.getSampleTypeId()).getSampleName()
								: WrsisPortalConstant.NA);

				jsa.put(jsonObj);
			}

			json.put(WrsisPortalConstant.SAMPLE_DETAILS, jsa);

			// otherDisease
			try {
				List<SurveyOtherDiseaseEntity> ent = surveyOtherDiseaseRepository.findBySurveyId(surveyId);

				jsa = new JSONArray();
				for (SurveyOtherDiseaseEntity s : ent) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_TYPE_ID, s.getDiseaseId());
					otherDiseaseJson.put(WrsisPortalConstant.OTH_INCIDENT_VAL, s.getIncident());
					otherDiseaseJson.put(WrsisPortalConstant.OTH_SEVERITY_VAL, s.getSeverity());
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
							(s.getDiseaseId() != null && s.getDiseaseId() != -1)
									? diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName()
									: WrsisPortalConstant.NA);
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
				 
			}
			json.put("otherDisease", jsa);

			// anyOtherDiseaseJsa
			jsa = new JSONArray();
			try {
				List<WRSurveyOtherDiseaseEntity> anyOther = WRSurveyOtherDiseaseRepository.findBySurveyId(surveyId);

				for (WRSurveyOtherDiseaseEntity s : anyOther) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_ID, s.getDiseaseId());
					otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
							diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName());
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
				 
			}
			json.put(WrsisPortalConstant.ANY_OTHER_DISEASE_JSA, jsa);

			jsa = new JSONArray();
			// fungicideJson
			try {
				FungicideDetailsEntity fungi = fungicideDetailsRepository.findBySurveyId(surveyId);
				JSONObject fun = new JSONObject();
				fun.put(WrsisPortalConstant.FUNGI_USED,
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
								? fungi.getFungicideTypeId()
								: -1);
				fun.put("effectiveness",
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungi.getFungEffectId()
								: -1);
				fun.put(WrsisPortalConstant.FUNGICIDE_NAME,
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
										? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
												.getFungicideName()
										: WrsisPortalConstant.NA);
				fun.put("dose",
						(fungi != null && fungi.getDose() != null && fungi.getDose().trim().length() > 0)
								? fungi.getDose()
								: "");
				fun.put(WrsisPortalConstant.EFFECTIVENESS_NAME,
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungiEffectTypeRepository.getOne(fungi.getFungEffectId()).getVchDesc()
								: WrsisPortalConstant.NA);
				fun.put(WrsisPortalConstant.SPRAY_DATE,
						(fungi != null && fungi.getSprayDate() != null)
								? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(fungi.getSprayDate())
								: "");
				json.put(WrsisPortalConstant.FUNGICIDE_JSON, fun);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}

			try {
				List<SurveyRustDetailsEntity> rusts = surveyRustDetailsRepository.findBySurveyId(surveyId);
				for (SurveyRustDetailsEntity s : rusts) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_ID, s.getRustTypeId());
					otherDiseaseJson.put("reaction", s.getRustReactionId());
					otherDiseaseJson.put(WrsisPortalConstant.REACTION_NAME,
							reactionTypeRepository.getOne(s.getRustReactionId()).getVchDesc());
					otherDiseaseJson.put(WrsisPortalConstant.INCIDENT_ID, s.getIncident());
					otherDiseaseJson.put(WrsisPortalConstant.SEVERITY_ID, s.getSeverity());
					otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_NAME, rustTypeRepository.getOne(s.getRustTypeId()).getVchRustType());
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}
			json.put("rustDetails", jsa);

			jsa = new JSONArray();
			try {
				List<SurveyCapturedImageEntity> images = surveyCapturedImageRepository.findBySurveyId(surveyId);
				for (SurveyCapturedImageEntity s : images) {
					jsa.put(s.getImageName());
				}
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getInstitutionNam():" + e);
			}
			json.put(WrsisPortalConstant.IMAGES, jsa);

			

		}

		return json;

	}

	@Override
	public JSONObject getFormmatedJsonBySurveyIdForGlobalRust(Integer surveyId) throws JSONException {

		JSONObject json = new JSONObject();

		Optional<SurveyDetails> surveyDetails = surveyDetailsRepository.findById(surveyId);

		if (surveyDetails.get() != null) {
			json.put(WrsisPortalConstant.PLANTING_DATE,
					(surveyDetails.get().getPlanningDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getPlanningDate())
							: "");
			json.put(WrsisPortalConstant.WOREDA_ID, surveyDetails.get().getWordeaId());
			json.put("remark", surveyDetails.get().getRemarks());
			json.put(WrsisPortalConstant.WOREDA_NAME1, surveyDetails.get().getWoreda().getDemographyName());
			json.put(WrsisPortalConstant.SEASION_NAME, surveyDetails.get().getSeasion().getVchSeason());
			json.put(WrsisPortalConstant.CONTACTED_FARMER_ID, surveyDetails.get().isFarmerContacted());
			json.put(WrsisPortalConstant.ZONE_NAME1, surveyDetails.get().getZone().getDemographyName());
			json.put(WrsisPortalConstant.OBSERVATION_ID,
					(surveyDetails.get().getObserveDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getObserveDate())
							: "");
			json.put(WrsisPortalConstant.LATITUDE_ID, (surveyDetails.get().getLatitude().split("\\."))[0]);
			json.put(WrsisPortalConstant.SEASON_ID, surveyDetails.get().getSeasion().getIntSeasoonId());
			json.put(WrsisPortalConstant.REGION_NAME1, surveyDetails.get().getRegion().getDemographyName());
			json.put(WrsisPortalConstant.LATITUDE_1ID, (surveyDetails.get().getLatitude().split("\\."))[1]);
			json.put(WrsisPortalConstant.ZONE_ID, surveyDetails.get().getZone().getDemographyId());
			json.put(WrsisPortalConstant.FUNGISIDE_ID, surveyDetails.get().isFungicideApplied());
			json.put(WrsisPortalConstant.ALTITUDE_ID, surveyDetails.get().getAltitude());
			json.put(WrsisPortalConstant.LONGITUDE_1ID, (surveyDetails.get().getLongitude().split("\\."))[1]);
			json.put(WrsisPortalConstant.CAPTURED_IMAGE_ID, surveyDetails.get().isImageCaptured());
			json.put(WrsisPortalConstant.KEBELE_NAME, surveyDetails.get().getKebele().getDemographyName());
			json.put(WrsisPortalConstant.SURVEY_DATE_ID, new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getSurveyDate()));
			json.put(WrsisPortalConstant.REGION_ID, surveyDetails.get().getRegionId());
			json.put(WrsisPortalConstant.SAMPLE_COLLECTED_ID, surveyDetails.get().isSampleCollected());
			json.put(WrsisPortalConstant.LONGITUDE_ID, (surveyDetails.get().getLongitude().split("\\."))[0]);
			json.put(WrsisPortalConstant.KEBELE_ID, surveyDetails.get().getKebelId());
			json.put(WrsisPortalConstant.RUST_AFFECTED_ID, surveyDetails.get().isRustAffected());
			json.put(WrsisPortalConstant.SURVEY_NO, surveyDetails.get().getSurveyNumber());
			json.put(WrsisPortalConstant.KEBEL_WOREDA_ADDR, surveyDetails.get().getLocationDetails());
			json.put(WrsisPortalConstant.OTHER_SURVEYOR, surveyDetails.get().getOtherSurveyor());

			// site info
			try {
				SurveySiteEntity siteEntity = surveySiteRepository.findBySurveyIdId(surveyId);
				JSONObject siteObj = new JSONObject();
				if (siteEntity != null) {
					siteObj.put(WrsisPortalConstant.SITE_AREA, siteEntity.getArea());
					siteObj.put("surveySite", siteEntity.getSiteTypeId());
					siteObj.put(WrsisPortalConstant.SURVEY_SITE_NAME,
							(siteEntity.getSiteTypeId() != null && siteEntity.getSiteTypeId() != -1)
									? siteTypeRepository.getOne(siteEntity.getSiteTypeId()).getVchSiteName()
									: WrsisPortalConstant.NA);
					siteObj.put("growthStage", siteEntity.getGrowthId());
					siteObj.put(WrsisPortalConstant.WHEAT_TYPE_NAME,
							(siteEntity.getWheatTypeId() != null && siteEntity.getWheatTypeId() != -1)
									? wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName()
									: WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.GROWTH_STAGE_NAME,
							(siteEntity.getGrowthId() != null && siteEntity.getGrowthId() != -1)
									? growthRepository.getOne(siteEntity.getGrowthId()).getVchStage()
									: WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.VARITY_NAME1,
							(siteEntity.getVarity() != null && siteEntity.getVarity().trim().length() > 0
									&& (!siteEntity.getVarity().trim().equals("null")))
											? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
													.getVchDesc()
											: "-1");
					siteObj.put("wheatType", siteEntity.getWheatTypeId());
					siteObj.put(WrsisPortalConstant.VARITY, siteEntity.getVarity());
				} else {
					siteObj.put(WrsisPortalConstant.SITE_AREA, "");
					siteObj.put("surveySite", "");
					siteObj.put(WrsisPortalConstant.SURVEY_SITE_NAME, WrsisPortalConstant.NA);
					siteObj.put("growthStage", "");
					siteObj.put(WrsisPortalConstant.WHEAT_TYPE_NAME, WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.GROWTH_STAGE_NAME, WrsisPortalConstant.NA);
					siteObj.put(WrsisPortalConstant.VARITY_NAME1, "");
					siteObj.put("wheatType", "");
					siteObj.put(WrsisPortalConstant.VARITY, "");
				}
				json.put(WrsisPortalConstant.SITE_INFORMATION1, siteObj);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

				 
			}

			// other details
			try {
				SurveyOtherEntity surveyOther = surveyOtherRepository.findBySurveyIdOne(surveyId);
				JSONObject surOtherJson = new JSONObject();
				if (surveyOther != null) {
					surOtherJson.put("soilColor", surveyOther.getSoilColorId());
					surOtherJson.put("weedcontrol", surveyOther.isWeedControl());
					surOtherJson.put("moisture", surveyOther.getMoistureId());
					surOtherJson.put("irrigated", surveyOther.isIrrigated());
					surOtherJson.put(WrsisPortalConstant.SOIL_NAME,
							(surveyOther.getSoilColorId() != null && surveyOther.getSoilColorId() != -1)
									? soilColorRepository.getOne(surveyOther.getSoilColorId()).getVchSoilColor()
									: WrsisPortalConstant.NA);
					surOtherJson.put(WrsisPortalConstant.MOISTURE_NAME,
							(surveyOther.getMoistureId() != null && surveyOther.getMoistureId() != -1)
									? moistureRepository.getOne(surveyOther.getMoistureId()).getVchMoisture()
									: WrsisPortalConstant.NA);
				} else {
					surOtherJson.put("soilColor", "");
					surOtherJson.put("weedcontrol", "");
					surOtherJson.put("moisture", "");
					surOtherJson.put("irrigated", "");
					surOtherJson.put(WrsisPortalConstant.SOIL_NAME, WrsisPortalConstant.NA);
					surOtherJson.put(WrsisPortalConstant.MOISTURE_NAME, WrsisPortalConstant.NA);

				}
				json.put("otherDetails", surOtherJson);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}

			// surveyorJsa
			JSONArray jsa = new JSONArray();
			try {
				List<SurveyorDetailsEntity> surveyorDetails = surveyorDetailsRepository.findBySurveyId(surveyId);
				for (SurveyorDetailsEntity s : surveyorDetails) {
					JSONObject jsonObj = new JSONObject();
					if (s != null) {
						jsonObj.put(WrsisPortalConstant.COUNTRY, s.getCountryId());
						jsonObj.put(WrsisPortalConstant.INSTITUTION_NAME1,
								(s.getCenterId() != null && s.getCenterId() != -1)
										? researchCenterRepository.getOne(s.getCenterId()).getResearchCenterName()
										: WrsisPortalConstant.NA);
						jsonObj.put(WrsisPortalConstant.COUNTRY_NAME,
								(s.getCenterId() != null && s.getCenterId() != -1)
										? demographicRepository.getOne(s.getCountryId()).getDemographyName()
										: WrsisPortalConstant.NA);
						jsonObj.put("surveyorName", s.getSurveyorName());
						jsonObj.put("researchId",
								(s.getCenterId() != null && s.getCenterId() != -1) ? s.getCenterId() : -1);
						jsonObj.put("userId",
								(s.getSurveyorUserId() != null && s.getSurveyorUserId() != -1) ? s.getSurveyorUserId()
										: -1);

					} else {
						jsonObj.put(WrsisPortalConstant.COUNTRY, "");
						jsonObj.put(WrsisPortalConstant.INSTITUTION_NAME1, WrsisPortalConstant.NA);
						jsonObj.put(WrsisPortalConstant.COUNTRY_NAME, WrsisPortalConstant.NA);
						jsonObj.put("surveyorName", "");
						jsonObj.put("researchId", "");
						jsonObj.put("userId", "");

					}

					jsa.put(jsonObj);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}
			json.put(WrsisPortalConstant.SURVEYOR_JSA, jsa);

			// sampleDetails
			List<SampleDetailsEntity> sampleEnt = sampleDetailsRepository.findBySurveyId(surveyId);
			jsa = new JSONArray();
			for (SampleDetailsEntity s : sampleEnt) {

				JSONObject jsonObj = new JSONObject();
				if (s != null) {
					jsonObj.put(WrsisPortalConstant.SAMPLE_REMARKS, s.getRemarks());
					jsonObj.put(WrsisPortalConstant.SAMPLE_COUNT_ID, s.getSampleCount());
					Integer sampleId = s.getSampleId();
					String sampleIds = "";
					List<SampleSurveyDetailsEntity> ids = sampleSurveyDetailsRepository.findBySurveySampleId(sampleId);
					for (int i = 0; i < ids.size(); i++) {
						sampleIds += ids.get(i).getSampleValue() + " , ";
					}
					sampleIds = sampleIds.substring(0, sampleIds.length() - 2);
					jsonObj.put(WrsisPortalConstant.SAMPLE_IDS, sampleIds);

					jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_ID, s.getSampleTypeId());
					SampleTypeMaster sampl = sampleTypeRepository.findBySampleId(s.getSampleTypeId());
					jsonObj.put(WrsisPortalConstant.RUST_ID, sampl.getRustTypeId());
					jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_NAME,
							(s.getSampleTypeId() != null && s.getSampleTypeId() != -1)
									? sampleTypeRepository.getOne(s.getSampleTypeId()).getSampleName()
									: WrsisPortalConstant.NA);
				} else {
					jsonObj.put(WrsisPortalConstant.SAMPLE_REMARKS, "");
					jsonObj.put(WrsisPortalConstant.SAMPLE_COUNT_ID, "");
					
					jsonObj.put(WrsisPortalConstant.SAMPLE_IDS, "");

					jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_ID, "");
					
					jsonObj.put(WrsisPortalConstant.RUST_ID, "");
					jsonObj.put(WrsisPortalConstant.SAMPLE_TYPE_NAME, WrsisPortalConstant.NA);

				}
				jsa.put(jsonObj);
			}

			json.put(WrsisPortalConstant.SAMPLE_DETAILS, jsa);

			// otherDisease
			try {
				List<SurveyOtherDiseaseEntity> ent = surveyOtherDiseaseRepository.findBySurveyId(surveyId);

				jsa = new JSONArray();
				for (SurveyOtherDiseaseEntity s : ent) {
					JSONObject otherDiseaseJson = new JSONObject();
					if (s != null) {
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_TYPE_ID, s.getDiseaseId());
						otherDiseaseJson.put(WrsisPortalConstant.OTH_INCIDENT_VAL, s.getIncident());
						otherDiseaseJson.put(WrsisPortalConstant.OTH_SEVERITY_VAL, s.getSeverity());
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
								(s.getDiseaseId() != null && s.getDiseaseId() != -1)
										? diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName()
										: WrsisPortalConstant.NA);
					} else {
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_TYPE_ID, "");
						otherDiseaseJson.put(WrsisPortalConstant.OTH_INCIDENT_VAL, "");
						otherDiseaseJson.put(WrsisPortalConstant.OTH_SEVERITY_VAL, "");
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME, WrsisPortalConstant.NA);

					}
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}
			json.put("otherDisease", jsa);

			// anyOtherDiseaseJsa
			jsa = new JSONArray();
			try {
				List<WRSurveyOtherDiseaseEntity> anyOther = WRSurveyOtherDiseaseRepository.findBySurveyId(surveyId);

				for (WRSurveyOtherDiseaseEntity s : anyOther) {
					JSONObject otherDiseaseJson = new JSONObject();
					if (s != null) {
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_ID, s.getDiseaseId());
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME,
								diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName());
					} else {
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_ID, "");
						otherDiseaseJson.put(WrsisPortalConstant.DISEASE_NAME, "");

					}
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}
			json.put(WrsisPortalConstant.ANY_OTHER_DISEASE_JSA, jsa);

			jsa = new JSONArray();
			// fungicideJson
			try {
				FungicideDetailsEntity fungi = fungicideDetailsRepository.findBySurveyId(surveyId);
				JSONObject fun = new JSONObject();
				if (fungi != null) {
					fun.put(WrsisPortalConstant.FUNGI_USED,
							(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
									? fungi.getFungicideTypeId()
									: -1);
					fun.put("effectiveness",
							(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
									? fungi.getFungEffectId()
									: -1);
					fun.put(WrsisPortalConstant.FUNGICIDE_NAME,
							(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
											? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
													.getFungicideName()
											: WrsisPortalConstant.NA);
					fun.put("dose",
							(fungi != null && fungi.getDose() != null && fungi.getDose().trim().length() > 0)
									? fungi.getDose()
									: "");
					fun.put(WrsisPortalConstant.EFFECTIVENESS_NAME,
							(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
									? fungiEffectTypeRepository.getOne(fungi.getFungEffectId()).getVchDesc()
									: WrsisPortalConstant.NA);
					fun.put(WrsisPortalConstant.SPRAY_DATE,
							(fungi != null && fungi.getSprayDate() != null)
									? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(fungi.getSprayDate())
									: "");
				} else {
					fun.put(WrsisPortalConstant.FUNGI_USED, "");
					fun.put("effectiveness", "");
					fun.put(WrsisPortalConstant.FUNGICIDE_NAME, WrsisPortalConstant.NA);
					fun.put("dose", "");
					fun.put(WrsisPortalConstant.EFFECTIVENESS_NAME, WrsisPortalConstant.NA);
					fun.put(WrsisPortalConstant.SPRAY_DATE, "");

				}
				json.put(WrsisPortalConstant.FUNGICIDE_JSON, fun);
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}

			try {
				List<SurveyRustDetailsEntity> rusts = surveyRustDetailsRepository.findBySurveyId(surveyId);
				for (SurveyRustDetailsEntity s : rusts) {
					JSONObject otherDiseaseJson = new JSONObject();
					if (s != null) {
						otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_ID, s.getRustTypeId());
						otherDiseaseJson.put("reaction", s.getRustReactionId());
						otherDiseaseJson.put(WrsisPortalConstant.REACTION_NAME,
								reactionTypeRepository.getOne(s.getRustReactionId()).getVchDesc());
						otherDiseaseJson.put(WrsisPortalConstant.INCIDENT_ID, s.getIncident());
						otherDiseaseJson.put(WrsisPortalConstant.SEVERITY_ID, s.getSeverity());
						otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_NAME,
								rustTypeRepository.getOne(s.getRustTypeId()).getVchRustType());
					} else {
						otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_ID, "");
						otherDiseaseJson.put("reaction", "");
						otherDiseaseJson.put(WrsisPortalConstant.REACTION_NAME, "");
						otherDiseaseJson.put(WrsisPortalConstant.INCIDENT_ID, "");
						otherDiseaseJson.put(WrsisPortalConstant.SEVERITY_ID, "");
						otherDiseaseJson.put(WrsisPortalConstant.RUST_TYPE_NAME, "");

					}
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}
			json.put("rustDetails", jsa);

			jsa = new JSONArray();
			try {
				List<SurveyCapturedImageEntity> images = surveyCapturedImageRepository.findBySurveyId(surveyId);
				for (SurveyCapturedImageEntity s : images) {
					jsa.put(s.getImageName());
				}
			} catch (Exception e) {
				LOG.error("CommonServiceImpl::getFormmatedJsonBySurveyIdForGlobalRust():" + e);

			}
			json.put(WrsisPortalConstant.IMAGES, jsa);

			

		}

		return json;

	}

}
