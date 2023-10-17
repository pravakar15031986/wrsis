package com.csmpl.wrsis.webportal.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
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
import com.csmpl.wrsis.webportal.service.SurveyReportService;

@Service
public class SurveyReportServiceImpl implements SurveyReportService {

	public static final Logger LOG = LoggerFactory.getLogger(SurveyReportServiceImpl.class);

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
			json.put("plantingDate",
					(surveyDetails.get().getPlanningDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getPlanningDate())
							: "");
			json.put("woredaId", surveyDetails.get().getWordeaId());
			json.put("remark", surveyDetails.get().getRemarks());
			json.put("WoredaName", surveyDetails.get().getWoreda().getDemographyName());
			json.put("SeasionName", surveyDetails.get().getSeasion().getVchSeason());
			json.put("contactedFarmerId", surveyDetails.get().isFarmerContacted());
			json.put("ZoneName", surveyDetails.get().getZone().getDemographyName());
			json.put("observationId",
					(surveyDetails.get().getObserveDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getObserveDate())
							: "");
			json.put("latitudeId", (surveyDetails.get().getLatitude().split("\\."))[0]);
			json.put("seasonId", surveyDetails.get().getSeasion().getIntSeasoonId());
			json.put("RegionName", surveyDetails.get().getRegion().getDemographyName());
			json.put("latitude1Id", "");
			json.put("zoneId", surveyDetails.get().getZone().getDemographyId());
			json.put("fungisideId", surveyDetails.get().isFungicideApplied());
			json.put("altitudeId", surveyDetails.get().getAltitude());
			json.put("longitude1Id", (surveyDetails.get().getLongitude().split("\\."))[1]);
			json.put("capturedImageId", surveyDetails.get().isImageCaptured());
			json.put("KebeleName", surveyDetails.get().getKebele().getDemographyName());
			json.put("surveyDateId", new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(surveyDetails.get().getSurveyDate()));
			json.put(WrsisPortalConstant.REGION_ID, surveyDetails.get().getRegionId());
			json.put("sampleCollectedId", surveyDetails.get().isSampleCollected());
			json.put("longitudeId", (surveyDetails.get().getLongitude().split("\\."))[0]);
			json.put("kebeleId", surveyDetails.get().getKebelId());
			json.put("rustAffectedId", surveyDetails.get().isRustAffected());
			json.put("SurveyNo", surveyDetails.get().getSurveyNumber());

			// site info
			try {
				SurveySiteEntity siteEntity = surveySiteRepository.findBySurveyIdId(surveyId);
				JSONObject siteObj = new JSONObject();
				siteObj.put("siteArea", siteEntity.getArea());
				siteObj.put("surveySite", siteEntity.getSiteTypeId());
				siteObj.put("SurveySiteName",
						(siteEntity.getSiteTypeId() != null && siteEntity.getSiteTypeId() != -1)
								? siteTypeRepository.getOne(siteEntity.getSiteTypeId()).getVchSiteName()
								: "NA");
				siteObj.put("growthStage", siteEntity.getGrowthId());
				siteObj.put("WheatTypeName",
						(siteEntity.getWheatTypeId() != null && siteEntity.getWheatTypeId() != -1)
								? wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName()
								: "NA");
				siteObj.put("GrowthStageName",
						(siteEntity.getGrowthId() != null && siteEntity.getGrowthId() != -1)
								? growthRepository.getOne(siteEntity.getGrowthId()).getVchStage()
								: "NA");
				siteObj.put("VarityName",
						(siteEntity.getVarity() != null && siteEntity.getVarity().trim().length() > 0
								&& (!siteEntity.getVarity().trim().equals("null")))
										? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
												.getVchDesc()
										: "-1");
				siteObj.put("wheatType", siteEntity.getWheatTypeId());
				siteObj.put("varity", siteEntity.getVarity());

				json.put("siteInformation", siteObj);
			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

			// other details
			try {
				SurveyOtherEntity surveyOther = surveyOtherRepository.findBySurveyIdOne(surveyId);
				JSONObject surOtherJson = new JSONObject();
				surOtherJson.put("soilColor", surveyOther.getSoilColorId());
				surOtherJson.put("weedcontrol", surveyOther.isWeedControl());
				surOtherJson.put("moisture", surveyOther.getMoistureId());
				surOtherJson.put("irrigated", surveyOther.isIrrigated());
				surOtherJson.put("SoilName",
						(surveyOther.getSoilColorId() != null && surveyOther.getSoilColorId() != -1)
								? soilColorRepository.getOne(surveyOther.getSoilColorId()).getVchSoilColor()
								: "NA");
				surOtherJson.put("MoistureName",
						(surveyOther.getMoistureId() != null && surveyOther.getMoistureId() != -1)
								? moistureRepository.getOne(surveyOther.getMoistureId()).getVchMoisture()
								: "NA");

				json.put("otherDetails", surOtherJson);
			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}

			// surveyorJsa
			JSONArray jsa = new JSONArray();
			try {
				List<SurveyorDetailsEntity> surveyorDetails = surveyorDetailsRepository.findBySurveyId(surveyId);
				for (SurveyorDetailsEntity s : surveyorDetails) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("country", s.getCountryId());
					jsonObj.put("InstitutionName",
							(s.getCenterId() != null && s.getCenterId() != -1)
									? researchCenterRepository.getOne(s.getCenterId()).getResearchCenterName()
									: "NA");
					jsonObj.put("CountryName",
							(s.getCenterId() != null && s.getCenterId() != -1)
									? demographicRepository.getOne(s.getCountryId()).getDemographyName()
									: "NA");
					jsonObj.put("surveyorName", s.getSurveyorName());
					jsonObj.put("researchId",
							(s.getCenterId() != null && s.getCenterId() != -1) ? s.getCenterId() : -1);
					jsonObj.put("userId",
							(s.getSurveyorUserId() != null && s.getSurveyorUserId() != -1) ? s.getSurveyorUserId()
									: -1);
					jsa.put(jsonObj);
				}

			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

				// remove printstack trace after implementing logger
			}
			json.put("surveyorJsa", jsa);

			// sampleDetails
			List<SampleDetailsEntity> sampleEnt = sampleDetailsRepository.findBySurveyId(surveyId);
			jsa = new JSONArray();
			for (SampleDetailsEntity s : sampleEnt) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("sampleRemarks", s.getRemarks());
				jsonObj.put("sampleCountId", s.getSampleCount());
				Integer sampleId = s.getSampleId();
				String sampleIds = "";
				List<SampleSurveyDetailsEntity> ids = sampleSurveyDetailsRepository.findBySurveySampleId(sampleId);
				for (int i = 0; i < ids.size(); i++) {
					sampleIds += ids.get(i).getSampleValue() + " , ";
				}
				sampleIds = sampleIds.substring(0, sampleIds.length() - 2);
				jsonObj.put("sampleIds", sampleIds);

				jsonObj.put("sampleTypeId", s.getSampleTypeId());
				SampleTypeMaster sampl = sampleTypeRepository.findBySampleId(s.getSampleTypeId());
				jsonObj.put("RustId", sampl.getRustTypeId());
				jsonObj.put("SampleTypeName",
						(s.getSampleTypeId() != null && s.getSampleTypeId() != -1)
								? sampleTypeRepository.getOne(s.getSampleTypeId()).getSampleName()
								: "NA");

				jsa.put(jsonObj);
			}

			json.put("sampleDetails", jsa);

			// otherDisease
			try {
				List<SurveyOtherDiseaseEntity> ent = surveyOtherDiseaseRepository.findBySurveyId(surveyId);

				jsa = new JSONArray();
				for (SurveyOtherDiseaseEntity s : ent) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put("diseaseTypeId", s.getDiseaseId());
					otherDiseaseJson.put("othIncidentVal", s.getIncident());
					otherDiseaseJson.put("othSeverityVal", s.getSeverity());
					otherDiseaseJson.put("DiseaseName",
							(s.getDiseaseId() != null && s.getDiseaseId() != -1)
									? diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName()
									: "NA");
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}
			json.put(WrsisPortalConstant.OTHER_DISEASE1, jsa);

			// anyOtherDiseaseJsa
			jsa = new JSONArray();
			try {
				List<WRSurveyOtherDiseaseEntity> anyOther = WRSurveyOtherDiseaseRepository.findBySurveyId(surveyId);

				for (WRSurveyOtherDiseaseEntity s : anyOther) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put("diseaseId", s.getDiseaseId());
					otherDiseaseJson.put("DiseaseName",
							diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName());
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}
			json.put("anyOtherDiseaseJsa", jsa);

			jsa = new JSONArray();
			// fungicideJson
			try {
				FungicideDetailsEntity fungi = fungicideDetailsRepository.findBySurveyId(surveyId);
				JSONObject fun = new JSONObject();
				fun.put("fungiUsed",
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
								? fungi.getFungicideTypeId()
								: -1);
				fun.put("effectiveness",
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungi.getFungEffectId()
								: -1);
				fun.put("FungicideName",
						(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1)
										? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
												.getFungicideName()
										: "NA");
				fun.put("dose",
						(fungi != null && fungi.getDose() != null && fungi.getDose().trim().length() > 0)
								? fungi.getDose()
								: "");
				fun.put(WrsisPortalConstant.EFFECTIVENESS_NAME,
						(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
								? fungiEffectTypeRepository.getOne(fungi.getFungEffectId()).getVchDesc()
								: "NA");
				fun.put("sprayDate",
						(fungi != null && fungi.getSprayDate() != null)
								? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(fungi.getSprayDate())
								: "");
				json.put("fungicideJson", fun);
			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}

			try {
				List<SurveyRustDetailsEntity> rusts = surveyRustDetailsRepository.findBySurveyId(surveyId);
				for (SurveyRustDetailsEntity s : rusts) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put("rustTypeId", s.getRustTypeId());
					otherDiseaseJson.put("reaction", s.getRustReactionId());
					otherDiseaseJson.put("ReactionName",
							reactionTypeRepository.getOne(s.getRustReactionId()).getVchDesc());
					otherDiseaseJson.put("incidentId", s.getIncident());
					otherDiseaseJson.put("severityId", s.getSeverity());
					otherDiseaseJson.put("RustTypeName", rustTypeRepository.getOne(s.getRustTypeId()).getVchRustType());
					jsa.put(otherDiseaseJson);
				}

			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}
			json.put("rustDetails", jsa);

			jsa = new JSONArray();
			try {
				List<SurveyCapturedImageEntity> images = surveyCapturedImageRepository.findBySurveyId(surveyId);
				for (SurveyCapturedImageEntity s : images) {
					jsa.put(s.getImageName());
				}
			} catch (Exception e) {
				LOG.error("SurveyReportServiceImpl::getFormattedJsonBySurveyId():" + e);

			}
			json.put(WrsisPortalConstant.IMAGES, jsa);

			

		}

		return json;
	}

}
