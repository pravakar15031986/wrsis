package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.GisBean;
import com.csmpl.wrsis.webportal.bean.GisMapBean;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.FungicideDetailsEntity;
import com.csmpl.wrsis.webportal.entity.GisFileLogEntity;
import com.csmpl.wrsis.webportal.entity.GisMapEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;
import com.csmpl.wrsis.webportal.entity.IVRSurveyDetails;
import com.csmpl.wrsis.webportal.entity.ImportIVRFile;
import com.csmpl.wrsis.webportal.entity.MapEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SampleSurveyDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.entity.SurveyOtherDiseaseEntity;
import com.csmpl.wrsis.webportal.entity.SurveyOtherEntity;
import com.csmpl.wrsis.webportal.entity.SurveyRustDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;
import com.csmpl.wrsis.webportal.entity.SurveyorDetailsEntity;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideDetailsRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GisFileLogRepository;
import com.csmpl.wrsis.webportal.repository.GisMapRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.IVRSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.repository.ManageIVRDataRepository;
import com.csmpl.wrsis.webportal.repository.MapRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
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
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WRSurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.repository.WheatMasterRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.SurveyDiscardLogService;
import com.csmpl.wrsis.webportal.util.DateUtil;
@Service("gisService")
public class GisServiceImpl implements com.csmpl.wrsis.webportal.service.GisService {

	private static final Logger LOG = LoggerFactory.getLogger(GisServiceImpl.class);

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

	@Autowired
	GisMapRepository gisMapRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@Autowired
	GisFileLogRepository gisFileLogRepository;

	@Autowired
	MapRepository mapRepository;

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

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionOptionRepository questionOptionRepository;
	
	@Autowired
	IVRSurveyDetailsRepository iVRSurveyDetailsRepository;
	
	@Autowired
	ManageIVRDataRepository manageIVRDataRepository;
	
	@Autowired
	IVRDataServiceImpl iVRDataServiceImpl;
	
	@Autowired
	IVRDataQuestionRepository iVRQuestionRepository;

	@Value("${map.forecasting.cmd}")
	private String giscmd;

	@Value("${gis.map.workspace}")
	private String GIS_WORKSPACE;
	
	@Value("${ivr.question.api}")
	private String ivrqusapi;
	
	@Value("${ivr.transaction.data.api}")
	private String ivrdataapi;
	
	@Value("${ivr.auth.data.api}")
	private String ivrauth;

	@Override
	public JSONObject getGisSurveyData(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {

			loginReqDto
					.setFromDate((loginReqDto.getFromDate() == null || loginReqDto.getFromDate().trim().equals("")) ? ""
							: loginReqDto.getFromDate());

			loginReqDto.setToDate((loginReqDto.getToDate() == null || loginReqDto.getToDate().trim().equals("")) ? ""
					: loginReqDto.getToDate());

			List<Object[]> surveyList = surveyDetailsRepository.viewGisSurveyData(loginReqDto.getFromDate(),
					loginReqDto.getToDate());

			for (Object[] sObj : surveyList) {
				jsonArray.put(getFormmatedJsonBySurveyId(Integer.valueOf(String.valueOf(sObj[0]))));
			}
			response.put("Rust Survey Data", jsonArray);

		} catch (Exception e) {
			LOG.error("GisServiceImpl::getGisSurveyData():" + e);
		}
		return response;
	}
	
	@Override
	public JSONObject getRawGisSurveyData(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {

			loginReqDto
					.setFromDate((loginReqDto.getFromDate() == null || loginReqDto.getFromDate().trim().equals("")) ? ""
							: loginReqDto.getFromDate());

			loginReqDto.setToDate((loginReqDto.getToDate() == null || loginReqDto.getToDate().trim().equals("")) ? ""
					: loginReqDto.getToDate());

			List<Object[]> surveyList = surveyDetailsRepository.viewRawGisSurveyData(loginReqDto.getFromDate(),
					loginReqDto.getToDate());

			for (Object[] sObj : surveyList) {
				jsonArray.put(getFormmatedJsonBySurveyId(Integer.valueOf(String.valueOf(sObj[0]))));
			}
			response.put("Rust Survey Data", jsonArray);

		} catch (Exception e) {
			LOG.error("GisServiceImpl::getGisSurveyData():" + e);
		}
		return response;
	}

	private JSONObject getFormmatedJsonBySurveyId(Integer surveyId) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsa;
		Optional<SurveyDetails> surveyDetails = surveyDetailsRepository.findById(surveyId);
		String otherSurveyor = null;
		if (surveyDetails.get() != null) {

			JSONObject surveyJson = new JSONObject();

			surveyJson.put("Survey Date", new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
					.format(surveyDetails.get().getSurveyDate()));
			surveyJson.put(WrsisPortalConstant.SEASON_C, surveyDetails.get().getSeasion().getVchSeason());
			surveyJson.put("Publish Date",
					(surveyDetails.get().getActionOn() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
									.format(surveyDetails.get().getActionOn())
							: "");

			surveyJson.put("Region Name", surveyDetails.get().getRegion().getDemographyName());
			surveyJson.put("Zone Name", surveyDetails.get().getZone().getDemographyName());
			surveyJson.put("Woreda Name", surveyDetails.get().getWoreda().getDemographyName());
			surveyJson.put("Kebele Name", surveyDetails.get().getKebele().getDemographyName());
			surveyJson.put("Location other details", surveyDetails.get().getLocationDetails());

//			Boolean latLongType = surveyDetails.get().getDecimaldegree();
			
			surveyJson.put(WrsisPortalConstant.LATITUDE,
					(surveyDetails.get().getLatitude() == null) ? "0.0" : surveyDetails.get().getLatitude());
			surveyJson.put(WrsisPortalConstant.LONGITUDE,
					(surveyDetails.get().getLongitude() == null) ? "0.0" : surveyDetails.get().getLongitude());
			
			/*if (Boolean.TRUE.equals(latLongType)) {
				surveyJson.put(WrsisPortalConstant.LATITUDE,
						(surveyDetails.get().getLatitude() == null) ? "0.0" : surveyDetails.get().getLatitude());
				surveyJson.put(WrsisPortalConstant.LONGITUDE,
						(surveyDetails.get().getLongitude() == null) ? "0.0" : surveyDetails.get().getLongitude());

			} else {

				surveyJson.put(WrsisPortalConstant.LATITUDE, (surveyDetails.get().getDegreeLatitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLatitude());
				surveyJson.put(WrsisPortalConstant.LONGITUDE, (surveyDetails.get().getDegreeLongitude() == null) ? "0.0.0"
						: surveyDetails.get().getDegreeLongitude());
			}*/

			surveyJson.put("Planting Date",
					(surveyDetails.get().getPlanningDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
									.format(surveyDetails.get().getPlanningDate())
							: "");
			surveyJson.put("Tillering Date",
					(surveyDetails.get().getTilleringDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
									.format(surveyDetails.get().getTilleringDate())
							: "");
			surveyJson.put("First Rust Observation Date",
					(surveyDetails.get().getObserveDate() != null)
							? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
									.format(surveyDetails.get().getObserveDate())
							: "");

			otherSurveyor = surveyDetails.get().getOtherSurveyor();
			json.put("Survey Details", surveyJson);

			// surveyor details

			try {
				List<SurveyorDetailsEntity> surveyorDetails = surveyorDetailsRepository.findBySurveyId(surveyId);
				String rcName = null;
				String country = null;
				String surveyors = "";

				for (SurveyorDetailsEntity s : surveyorDetails) {
					if (rcName == null)
						rcName = (s.getCenterId() != null && s.getCenterId() != -1)
								? researchCenterRepository.getOne(s.getCenterId()).getResearchCenterName()
								: "NA";
					if (country == null)
						country = (s.getCenterId() != null && s.getCenterId() != -1)
								? demographicRepository.getOne(s.getCountryId()).getDemographyName()
								: "NA";

					surveyors += s.getSurveyorName() + ",";
				}
				if (surveyors.contains(",")) {
					surveyors = surveyors.substring(0, surveyors.length() - 1);
				}

				JSONObject jsonObj = new JSONObject();
				jsonObj.put("Institution Name", rcName);
				jsonObj.put("Country", country);
				jsonObj.put("Surveyors", surveyors);
				if (otherSurveyor != null && !otherSurveyor.isEmpty())
					jsonObj.put("Other Surveyors", otherSurveyor);

				
				json.put("Surveyor Details", jsonObj);

			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
				
			}

			// site info
			try {
				SurveySiteEntity siteEntity = surveySiteRepository.findBySurveyIdId(surveyId);
				JSONObject siteObj = new JSONObject();
				siteObj.put("Survey Site",
						(siteEntity != null && siteEntity.getSiteTypeId() != null && siteEntity.getSiteTypeId() != -1)
								? siteTypeRepository.getOne(siteEntity.getSiteTypeId()).getVchSiteName()
								: "NA");
				siteObj.put("Wheat Type",
						(siteEntity != null && siteEntity.getWheatTypeId() != null && siteEntity.getWheatTypeId() != -1)
								? wheatMasterRepository.getOne(siteEntity.getWheatTypeId()).getWheatName()
								: "NA");
				siteObj.put(WrsisPortalConstant.VARITY_NAME,
						(siteEntity != null && siteEntity.getVarity() != null
								&& siteEntity.getVarity().trim().length() > 0
								&& (!siteEntity.getVarity().trim().equals("null")))
										? varietyTypeRepository.getOne(Integer.valueOf(siteEntity.getVarity()))
												.getVchDesc()
										: "NA");

				if (siteObj.getString("Varity Name").trim().equalsIgnoreCase("other")) {
					siteObj.put("Varity Name", siteEntity.getOtherVariety());
				}
				siteObj.put("Growth Stage",
						(siteEntity != null && siteEntity.getGrowthId() != null && siteEntity.getGrowthId() != -1)
								? growthRepository.getOne(siteEntity.getGrowthId()).getVchStage()
								: "NA");
				siteObj.put("Site Area", (siteEntity != null) ? siteEntity.getArea() : "NA");

				json.put("Site Information", siteObj);

			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

			Boolean fungicide = null;
			try {
				fungicide = surveyDetails.get().isFungicideApplied();
			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			} 

			// other details
			try {
				JSONObject surOtherJson = new JSONObject();
				SurveyOtherEntity surveyOther = surveyOtherRepository.findBySurveyIdOne(surveyId);

				
				surOtherJson.put("Weed Control", surveyOther.isWeedControl() ? "Yes" : "No");
				
				surOtherJson.put("Irrigated", surveyOther.isIrrigated() ? "Yes" : "No");
				surOtherJson.put("Soil colour",
						(surveyOther.getSoilColorId() != null && surveyOther.getSoilColorId() != -1)
								? soilColorRepository.getOne(surveyOther.getSoilColorId()).getVchSoilColor()
								: "NA");
				surOtherJson.put("Moisture",
						(surveyOther.getMoistureId() != null && surveyOther.getMoistureId() != -1)
								? moistureRepository.getOne(surveyOther.getMoistureId()).getVchMoisture()
								: "NA");

				json.put("Survey Other Details", surOtherJson);
			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

			// Rust Details
			try {
				jsa = new JSONArray();
				List<SurveyRustDetailsEntity> rusts = surveyRustDetailsRepository.findBySurveyId(surveyId);
				for (SurveyRustDetailsEntity s : rusts) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put("Reaction", reactionTypeRepository.getOne(s.getRustReactionId()).getVchDesc());
					otherDiseaseJson.put("Incident", s.getIncident());
					otherDiseaseJson.put("Severity", s.getSeverity());
					otherDiseaseJson.put("Rust Type", rustTypeRepository.getOne(s.getRustTypeId()).getVchRustType());
					// severity name for UK met survey
					// otherDiseaseJson.put("Incident Name",
					
					jsa.put(otherDiseaseJson);
				}
				json.put("Rust Details", jsa);
			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

			// sampleDetails
			jsa = new JSONArray();

			List<SampleDetailsEntity> sampleEnt = sampleDetailsRepository.findBySurveyId(surveyId);
			for (SampleDetailsEntity s : sampleEnt) {
				JSONObject jsonObj = new JSONObject();
				
				Integer sampleId = s.getSampleId();
				String sampleIds = "";
				List<SampleSurveyDetailsEntity> ids = sampleSurveyDetailsRepository.findBySurveySampleId(sampleId);
				for (int i = 0; i < ids.size(); i++) {
					sampleIds += ids.get(i).getSampleValue() + ",";
				}
				if (sampleIds.contains(","))
					sampleIds = sampleIds.substring(0, sampleIds.length() - 1);
				

				
				
				
				
				jsonObj.put("Sample Type",
						(s.getSampleTypeId() != null && s.getSampleTypeId() != -1)
								? sampleTypeRepository.getOne(s.getSampleTypeId()).getSampleName()
								: "NA");
				jsonObj.put("Sample ID", sampleIds);

				jsa.put(jsonObj);
			}

			json.put("Sample Details", jsa);

			// fungicide details
			try {
				JSONObject fun = new JSONObject();
				if (fungicide != null && fungicide) {

					FungicideDetailsEntity fungi = fungicideDetailsRepository.findBySurveyId(surveyId);

					fun.put("Fungicide Name",
							(fungi != null && fungi.getFungicideTypeId() != null && fungi.getFungicideTypeId() != -1
									)
											? fungicideMasterRepository.getOne(fungi.getFungicideTypeId())
													.getFungicideName()
											: "NA");

					if (fun.getString("Fungicide Name").trim().equalsIgnoreCase("other")) {
						fun.put("Fungicide Name", fungi.getOtherFungi());
					}
					fun.put("Spray Date",
							(fungi != null && fungi.getSprayDate() != null)
									? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY)
											.format(fungi.getSprayDate())
									: "");
					fun.put("Used Dose",
							(fungi != null && fungi.getDose() != null && fungi.getDose().trim().length() > 0)
									? fungi.getDose()
									: "");
					fun.put("EffectiveNess",
							(fungi != null && fungi.getFungEffectId() != null && fungi.getFungEffectId() != -1)
									? fungiEffectTypeRepository.getOne(fungi.getFungEffectId()).getVchDesc()
									: "NA");
				}
				json.put("Fungicide Details", fun);
			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

			// otherDisease
			try {
				jsa = new JSONArray();
				List<SurveyOtherDiseaseEntity> ent = surveyOtherDiseaseRepository.findBySurveyId(surveyId);

				for (SurveyOtherDiseaseEntity s : ent) {
					JSONObject otherDiseaseJson = new JSONObject();
					otherDiseaseJson.put("Incident Value", s.getIncident());
					otherDiseaseJson.put("Severity Value", s.getSeverity());
					otherDiseaseJson.put("Disease Name",
							(s.getDiseaseId() != null && s.getDiseaseId() != -1)
									? diseaseMasterRepository.getOne(s.getDiseaseId()).getDiseaseName()
									: "NA");
					jsa.put(otherDiseaseJson);
				}

				json.put("Other Disease", jsa);
			} catch (Exception e) {
				LOG.error("GisServiceImpl::getFormattedJsonBySurveyId():" + e);
			}

		}

		return json;
	}

	@Override
	public JSONObject getLatestGISData(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray gisArray = new JSONArray();
		try {
			List<Object[]> gisData = gisMapRepository.getLatestGISData(loginReqDto.getDisease(),
					loginReqDto.getMapTypeId());
			JSONObject gisDataObj = null;
			if (!gisData.isEmpty()) {
				for (Object[] obj : gisData) {
					gisDataObj = new JSONObject();
					gisDataObj.put("id", String.valueOf(obj[0]));
					gisDataObj.put("layerName", String.valueOf(obj[1]));
					gisDataObj.put("createdOn", String.valueOf(obj[2]));

					gisArray.put(gisDataObj);

				}

			}
			response.put("gisLatestData", gisArray);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getLatestGISData():" + e);
		}
		return response;
	}

	@Override
	public JSONObject getGISHistoryData(MobileReqBean loginReqDto) {
		JSONObject response = new JSONObject();
		JSONArray gishisArray = new JSONArray();
		try {
			List<Object[]> gisData = gisMapRepository.getGISHistoryData(loginReqDto.getDisease(),
					loginReqDto.getMapTypeId());
			JSONObject gishisObj = null;
			if (!gisData.isEmpty()) {
				for (Object[] obj : gisData) {
					gishisObj = new JSONObject();
					gishisObj.put("id", String.valueOf(obj[0]));
					gishisObj.put("layerName", String.valueOf(obj[1]));
					gishisObj.put("createdOn", String.valueOf(obj[2]));

					gishisArray.put(gishisObj);

				}

			}
			response.put("gisHistoryData", gishisArray);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getGISHistoryData():" + e);
		}
		return response;
	}

	@Override
	public JSONObject getMasterDataForGISMap() {
		JSONArray rustTypeArray = new JSONArray();
		JSONArray mapTypeArray = new JSONArray();
		JSONObject response = new JSONObject();
		try {
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

			// map type

			List<Object[]> mapTypeList = gisMapRepository.fetchAllMapType();
			JSONObject mapTypeListObj = null;
			if (!mapTypeList.isEmpty()) {
				for (Object[] obj : mapTypeList) {
					mapTypeListObj = new JSONObject();
					mapTypeListObj.put("mapId", String.valueOf(obj[0]));
					mapTypeListObj.put("mapName", String.valueOf(obj[1]));

					mapTypeArray.put(mapTypeListObj);

				}

			}

			response.put("diseaseData", rustTypeArray);
			response.put("mapTypeData", mapTypeArray);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getMasterDataForGISMap():" + e);
		}
		return response;
	}

	@Override
	public String saveLogfileDetails(String fileName, String filePath, Date uploadDate) {

		GisFileLogEntity fileLog = null;
		String saveStatus = "";
		try {
			fileLog = new GisFileLogEntity();
			fileLog.setFileName(fileName);
			fileLog.setFileLoc(filePath);
			fileLog.setUploadedDate(uploadDate);//added by Raman Shrestha
			fileLog.setCreatedon(new Timestamp(new Date().getTime()));
			fileLog.setBitStatus(false);
			gisFileLogRepository.saveAndFlush(fileLog);

			saveStatus = WrsisPortalConstant.SUCCESS;
			LOG.info("GisServiceImpl::saveLogFileDetails(): File saved log updated " );
		} catch (Exception e) {
			saveStatus = WrsisPortalConstant.FAILURE;
			LOG.error("GisServiceImpl::saveLogFileDetails():" + e);
		}
		return saveStatus;
	}

	@Override
	public String createStoreAndPublishStyleAndSaveData(File file,Date uploadDate) {

		String result = "";
		String resultstatus = "";
		String storeStatus = "";
		String styleStatus = "";
		List<RustTypeMasterEntity> rusts = rustTypeRepository.fetchAllActiveRustType();
		List<MapEntity> maps = mapRepository.fetchAllActiveMapType();
		GisMapBean mapbean = null;

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat("ddMMyyyy").format(ts);

		try {

			File[] list = file.listFiles();// deposition,env
			if (list != null)
				for (File fil : list) {

					if (fil.isDirectory()) {
						File[] rlist = fil.listFiles(); // stem,yellow,leaf
						for (File rfile : rlist) {

							if (rfile.isDirectory()) {
								File[] newshpZip = rfile.listFiles();
								for (File newzips : newshpZip) {
									if (newzips.getName().endsWith(".zip")) {

										mapbean = new GisMapBean();

										// create store
										LOG.info(" --Here call this createStoreCmd() method to create store-- ");
										storeStatus = createStoreCmd(newzips.getAbsolutePath(),
												FilenameUtils.removeExtension(newzips.getName().toLowerCase()) + "_"
														+ timeStamp,
												giscmd, GIS_WORKSPACE);

										try {
											ZipFile zipFile = new ZipFile(newzips.getAbsolutePath());
											Enumeration<? extends ZipEntry> entries = zipFile.entries();

											while (entries.hasMoreElements()) {
												ZipEntry zipEntry = entries.nextElement();

												if (zipEntry.getName().contains(".shp")) {
													mapbean.setLayerName(
															FilenameUtils.removeExtension(zipEntry.getName()));
												}
												if (zipEntry.getName().contains(".sld") && (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(storeStatus))) {
													// create style bublish

														LOG.info(
																" --Here call this createStyleCmd() method to publish style-- ");
														styleStatus = createStyleCmd(fil.getName(),
																FilenameUtils.removeExtension(zipEntry.getName()),
																giscmd, GIS_WORKSPACE);

													}


											}
											zipFile.close();
										} catch (Exception e) {
											LOG.error("GisServiceImpl::createStoreAndPublishStyleAndSaveData():" + e);
										}

										for (int i = 0; i < rusts.size(); i++) {
											RustTypeMasterEntity vte = rusts.get(i);
											if (vte.getVchRustType().replaceAll("\\s+", "").toLowerCase()
													.contains(rfile.getName().toLowerCase())) {
												mapbean.setRustTypeId(vte.getIntRustTypeId());
												break;
											}

										}

										for (int i = 0; i < maps.size(); i++) {
											MapEntity map = maps.get(i);
											if (map.getMapName().toLowerCase().contains(fil.getName().toLowerCase())) {
												mapbean.setMapTypeId(map.getMapTypeId());
												break;
											}

										}
										mapbean.setFileName(
												FilenameUtils.removeExtension(newzips.getName().toLowerCase()) + "_"
														+ timeStamp + ".zip");
										mapbean.setStoreName(
												FilenameUtils.removeExtension(newzips.getName().toLowerCase()) + "_"
														+ timeStamp);

										// save data gis table
										if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(storeStatus)
												&& WrsisPortalConstant.SUCCESS.equalsIgnoreCase(styleStatus)) {

											
											LOG.info(" --Here call this saveGisData() method to save data into DB Table-- ");
											mapbean.setUploadOn(uploadDate);
											resultstatus = saveGisData(mapbean);

											if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(resultstatus)) {
												LOG.info(" --Store Data into Data Base Table-- ");
											} else {
												LOG.info(" --Exception found while store data into DB table-- ");
											}

										}
									}

								}

							}

						}

					}
				}
			result = WrsisPortalConstant.SUCCESS;
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;
			LOG.error("GisServiceImpl::createStoreAndPublishStyleAndSaveData():" + e);
		}

		return result;
	}

	public static String createStoreCmd(String filePath, String filenName, String giscmd, String gIS_WORKSPACE2) {

		String result = WrsisPortalConstant.FAILURE;
		// String comdExecute = "curl -u admin:geoserver -H \"Content-type:
		// application/zip\" -T "+filePath+"
		
		// String curlCmd = "curl -u admin:geoserver -H 'Content-type: application/zip'
		// -T
		// /opt/WRSIS-DATA/Geo_Server_Unzip/dynamicgismap_26052020/environment/leafrust/environment_leaf_rust.zip
		
		
		String curlCmd = "curl -u admin:geoserver -H 'Content-type: application/zip' -T " + filePath + " " + giscmd
				+ "/workspaces/" + gIS_WORKSPACE2 + "/datastores/" + filenName + "/file.shp";

		LOG.info("GisServiceImpl::createStore():curl cmd:-" + curlCmd);

		ProcessBuilder processBuilder = new ProcessBuilder();
		if (!System.getProperty(WrsisPortalConstant.OS_NAME).contains(WrsisPortalConstant.WINDOWS))
			processBuilder.command("bash", "-c", curlCmd);
		else
			processBuilder.command(WrsisPortalConstant.CMD_EXE, "/c", curlCmd);

		try {

			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				LOG.info("GisServiceImpl::createStore():Curl command for create store requested ...");
				LOG.info("GisServiceImpl::createStore():curl response:-" + output);

				result = WrsisPortalConstant.SUCCESS;
			} else {
				LOG.info("GisServiceImpl::createStore():Curl command for create store executed with error ...");
				LOG.info("GisServiceImpl::createStore():curl response:-" + output);

			}

		} catch (IOException e) {
			LOG.error("GisServiceImpl::createStoreCmd():" + e);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::createStoreCmd():" + e);
		}

		return result;
	}

	public String createStyleCmd(String mapType, String sldFile, String giscmd, String gIS_WORKSPACE2) {

		String result = WrsisPortalConstant.FAILURE;
		String curlCmd = null;
		if (mapType.equalsIgnoreCase("deposition")) {
			curlCmd = "curl -u admin:geoserver -XPUT -H 'Content-type: text/xml' -d '<layer><defaultStyle><name>depositionstyle</name></defaultStyle><enabled>true</enabled></layer>' "
					+ giscmd + "/layers/" + gIS_WORKSPACE2 + ":" + sldFile + " ";
		} else {
			curlCmd = "curl -u admin:geoserver -XPUT -H 'Content-type: text/xml' -d '<layer><defaultStyle><name>environmentstyle</name></defaultStyle><enabled>true</enabled></layer>' "
					+ giscmd + "/layers/" + gIS_WORKSPACE2 + ":" + sldFile + " ";
		}

		LOG.info("GisServiceImpl::createStyleCmd():curl cmd:-" + curlCmd);

		ProcessBuilder processBuilder = new ProcessBuilder();
		if (!System.getProperty(WrsisPortalConstant.OS_NAME).contains(WrsisPortalConstant.WINDOWS))
			processBuilder.command("bash", "-c", curlCmd);
		else
			processBuilder.command(WrsisPortalConstant.CMD_EXE, "/c", curlCmd);

		try {

			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				LOG.info("GisServiceImpl::createStyleCmd():Curl command for create style requested ...");
				LOG.info("GisServiceImpl::createStyleCmd():curl response:-" + output);

				result = WrsisPortalConstant.SUCCESS;
			} else {
				LOG.info("GisServiceImpl::createStyleCmd():Curl command for create style executed with error ...");
				LOG.info("GisServiceImpl::createStyleCmd():curl response:-" + output);

			}

		} catch (IOException e) {
			LOG.error("GisServiceImpl::createStyleCmd():" + e);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::createStyleCmd():" + e);
		}

		return result;
	}

	public String saveGisData(GisMapBean mapbean) {
		String result = "";
		GisMapEntity gisMap = null;
		try {
			gisMap = new GisMapEntity();
			gisMap.setMapTypeId(mapbean.getMapTypeId());
			gisMap.setRustTypeId(mapbean.getRustTypeId());
			gisMap.setFileName(mapbean.getFileName());
			gisMap.setStoreName(mapbean.getStoreName());
			gisMap.setLayerName(mapbean.getLayerName());
			gisMap.setBitStatus(false);
			gisMap.setCreatedon(new Timestamp(new Date().getTime()));
			gisMap.setUploadOn(new Timestamp(mapbean.getUploadOn().getTime()));
			gisMapRepository.saveAndFlush(gisMap);

			result = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;
			LOG.error("GisServiceImpl::saveGisData():" + e);
		}
		return result;
	}

	// for map type search
	@Override
	public List<MapEntity> viewAllActiveMap() {
		List<MapEntity> mapTypeArray = new ArrayList<>();

		List<Object[]> mapTypeList = gisMapRepository.fetchAllMapType();
		
		if (!mapTypeList.isEmpty()) {
			for (Object[] obj : mapTypeList) {
				MapEntity mapTypeListObj = new MapEntity();
				mapTypeListObj.setMapTypeId(Integer.parseInt(String.valueOf(obj[0])));
				mapTypeListObj.setMapName(String.valueOf(obj[1]));
				mapTypeArray.add(mapTypeListObj);
			}
		}
		return mapTypeArray;
	}

	@Override
	public List<RustTypeMasterEntity> viewAllActiveRust() {
		
		return Collections.emptyList();
	}

	//After search
	
	@Override
	public List<GisBean> gisForcastingFile(String uploadstartdate, String uploadenddate, String rustTypeId,
			String mapTypeId, String status) {
		List<GisBean> list = new ArrayList<>();
		GisBean vo = null;
		Integer rustId = Integer.valueOf(rustTypeId);
		Integer mapTypId = Integer.valueOf(mapTypeId);

		Date startDate = DateUtil.StringMMMToDate(uploadstartdate);
		Date endDate = DateUtil.StringMMMToDate(uploadenddate);
		List<Object[]> objList = null;
		try {
			if (startDate != null && endDate != null && rustId == 0 && mapTypId == 0 && "0".equals(status)) {
				objList = gisFileLogRepository.getGisForcastingFileByDate(startDate, endDate);
			}
			if (startDate != null && endDate != null && rustId != 0 && mapTypId == 0 && "0".equals(status)) {
				objList = gisFileLogRepository.getGisForcastingFileByDateAndRustType(startDate, endDate, rustId);
			}
			if (startDate != null && endDate != null && rustId == 0 && mapTypId != 0 && "0".equals(status)) {
				objList = gisFileLogRepository.getGisForcastingFileByDateAndMapType(startDate, endDate, mapTypId);
			}
			if (startDate != null && endDate != null && rustId != 0 && mapTypId != 0 && "0".equals(status)) {
				objList = gisFileLogRepository.getGisForcastingFileByDateRustTypeAndMapType(startDate, endDate, rustId,
						mapTypId);
			}

			if (startDate != null && endDate != null && rustId == 0 && mapTypId == 0 && !("0".equals(status))) {
				objList = gisFileLogRepository.getGisForcastingFileByDateAndStatus(startDate, endDate,
						Boolean.valueOf(status));
			}

			if (startDate != null && endDate != null && rustId != 0 && mapTypId == 0 && !("0".equals(status))) {
				objList = gisFileLogRepository.getGisForcastingFileByDateRustTypeAndStatus(startDate, endDate, rustId,
						Boolean.valueOf(status));
			}
			if (startDate != null && endDate != null && rustId == 0 && mapTypId != 0 && !("0".equals(status))) {
				objList = gisFileLogRepository.getGisForcastingFileByDateAndMapTypeAndStatus(startDate, endDate,
						mapTypId, Boolean.valueOf(status));
			}
			if (startDate != null && endDate != null && rustId != 0 && mapTypId != 0 && !("0".equals(status))) {
				objList = gisFileLogRepository.getGisForcastingFileByDateAndRustTypeMapTypeAndStatus(startDate, endDate,
						rustId, mapTypId, Boolean.valueOf(status));
			}

			if (objList!=null && !objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new GisBean();
					vo.setFileName(String.valueOf(obj[1]));
					vo.setCreatedOn(DateUtil.DateToString((Date) (obj[4]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					vo.setMapType(String.valueOf(obj[0]));
					vo.setLayerName(String.valueOf(obj[3]));
					vo.setRustType(String.valueOf(obj[5]));
					vo.setStoreName(String.valueOf(obj[2]));
					vo.setBitStatus((boolean) obj[7]);
					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("GisServiceImpl::gisForcastingFile():" + e);
		}
		return list;
	}

	@Override
	public List<GisBean> gisForcastingFile(String tdate,String edate) {
		List<GisBean> list = new ArrayList<>();
		GisBean vo = null;
		
		
		
		try {
			
			Date startDate = DateUtil.StringMMMToDate(tdate);
			Date endDate = DateUtil.StringMMMToDate(edate);
			

			List<Object[]> objList = gisFileLogRepository.getGisForcastingFileByDate(startDate, endDate);

			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new GisBean();
					vo.setFileName(String.valueOf(obj[1]));//
					vo.setCreatedOn(String.valueOf(obj[4]));//
					vo.setMapType(String.valueOf(obj[0]));//
					vo.setLayerName(String.valueOf(obj[3]));//
					vo.setRustType(String.valueOf(obj[5]));//
					vo.setStoreName(String.valueOf(obj[2]));//

					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("GisServiceImpl::gisForcastingFile():" + e);
		}
		return list;
	}

	@Override
	public JSONObject getIVRQuesMasterData() {
		JSONObject response = new JSONObject();
		JSONArray questionArray = new JSONArray();
		JSONArray optionArray = new JSONArray();
		try {

			List<Object[]> questionList = questionRepository.fetchAllIVRQuestion();
			JSONObject questioObj = null;
			JSONObject optionObj = null;
			if (!questionList.isEmpty()) {
				for (Object[] questobj : questionList) {
					questioObj = new JSONObject();
					
					questioObj.put("queName", String.valueOf(questobj[1]));
					questioObj.put("queOrder", String.valueOf(questobj[2]));
					int questionId = Integer.parseInt(String.valueOf(questobj[0]));
					List<Object[]> optionList = questionOptionRepository.findOptionByQustionId(questionId);
					if (!optionList.isEmpty()) {
						optionArray = new JSONArray();
						for (Object[] optobj : optionList) {
							optionObj = new JSONObject();
							optionObj.put("optValue", String.valueOf(optobj[1]));
							optionObj.put("optId", String.valueOf(optobj[0]));
							optionArray.put(optionObj);

						}
						questioObj.put("optionDetails", optionArray);
					}

					questionArray.put(questioObj);

				}

			}
			response.put("questionDetails", questionArray);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getIVRQuesMasterData():" + e);
		}
		return response;
	}

	// Author : Raman Shrestha
	// Date : 02-Sep-2020
	@Transactional
	@Override
	public int deleteForcastingFile(Date today) {

		int count = 0;
		try {
			Date beforeDate = DateUtil.getBeforeDate(today, -15);
			LOG.info("Today Date : " + today + ", Before 15 days Date : " + beforeDate);
			List<GisMapEntity> storeList = gisMapRepository.getGISMapRecordBeforeDate(beforeDate);
			if (!storeList.isEmpty()) {
				LOG.info("No. of stores returned from DB : " + storeList.size());
				for (GisMapEntity file : storeList) {
					String status = deleteGISStore(file.getStoreName(), giscmd, GIS_WORKSPACE);
					if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {
						file.setBitStatus(true);
						file.setUpdatedon(new Timestamp(new Date().getTime()));
						gisMapRepository.saveAndFlush(file);
						count++;
					}
				}
			}
			LOG.info("No. of stores deleted : " + count);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getfilesbeforedate() : " + e);
		}
		return count;
	}

	public static String deleteGISStore(String storeName, String giscmd, String gISWorkspace) {

		String result = WrsisPortalConstant.FAILURE;

		// curl -v -u admin:geoserver -X DELETE
		// http://localhost:8080/geoserver/rest/workspaces/workspace_name/datastores/store_name?recurse=true

		String curlCmd = "curl -v -u admin:geoserver -X DELETE  " + giscmd + "/workspaces/" + gISWorkspace
				+ "/datastores/" + storeName + "?recurse=true";

		LOG.info("GisServiceImpl::deleteGISStore():curl cmd:-" + curlCmd);

		ProcessBuilder processBuilder = new ProcessBuilder();
		if (!System.getProperty(WrsisPortalConstant.OS_NAME).contains(WrsisPortalConstant.WINDOWS))
			processBuilder.command("bash", "-c", curlCmd);
		else
			processBuilder.command(WrsisPortalConstant.CMD_EXE, "/c", curlCmd);

		try {

			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				LOG.info("GisServiceImpl::deleteGISStore():Curl command for delete store requested ...");
				LOG.info("GisServiceImpl::deleteGISStore():curl response:-" + output);

				result = WrsisPortalConstant.SUCCESS;
			} else {
				LOG.info("GisServiceImpl::deleteGISStore():Curl command for delete store executed with error ...");
				LOG.info("GisServiceImpl::deleteGISStore():curl response:-" + output);

			}

		} catch (Exception e) {
			LOG.error("GisServiceImpl::deleteGISStore():" + e);

		}

		return result;
	}

	@Override
	public String inactivePreviousFilesByDate(Date uploadDate) {
		String result = "";
		try {
			List<GisFileLogEntity> gisFileLogs = gisFileLogRepository.getFileLogsByDate(uploadDate);
			for (GisFileLogEntity gisFileLogEntity : gisFileLogs) {
				gisFileLogEntity.setBitStatus(true);
				gisFileLogRepository.save(gisFileLogEntity);
			}
			result = "success";
		} catch (Exception e) {
			LOG.error("GisServiceImpl::inactivePreviousFilesByDate():" + e);
			result = "fail";
		}
		return result;
	}
	
	@Transactional
	@Override
	public String updateQuestionOptionData() {
		
		LOG.info("GisServiceImpl::updateQuestionOptionData():To get the survey ID from t_wr_ivr_survey_details table for IVR Questions");
		
		IVRSurveyDetails ivrDataDetails =iVRSurveyDetailsRepository.findLatestRecord();
		
		LOG.info("GisServiceImpl::updateQuestionOptionData():Survey ID value to call the API -" + (ivrDataDetails!=null? ivrDataDetails.getSurveyId():0));
		
		
		String returnType ="";
		//String url = "http://8028.et:3000/gisapi/getIVRQuestions?surveyId=7";
		String url = ivrqusapi+"?surveyId="+ivrDataDetails.getSurveyId();
		LOG.info("GisServiceImpl::updateQuestionOptionData():API URL for to get the IVR questions :" +url);
		
		try {
	
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", ivrauth);
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<String> response = restTemplate.exchange(
		        url,
		        HttpMethod.GET,
		        request,
		        String.class
		);
		
		LOG.info("GisServiceImpl::updateQuestionOptionData():IVR Questions API response :" + response.getBody());
		
		String questionResponse =saveIVRQuestionAndOptions(response.getBody(),ivrDataDetails.getSurveyId());

		returnType=questionResponse;
		LOG.info("GisServiceImpl::updateQuestionOptionData():IVR Question master updated");
		} catch (Exception e) {
			LOG.error("GisServiceImpl::updateQuestionOptionData():error in IVR Question master data update",e.toString());
			returnType="Fail";
		}
		
		return returnType;
	}
	
	@Transactional
	@Override
	public String updateIVRData(String queResList,Date date) {
		
		LOG.info("GisServiceImpl::updateIVRData():To get the survey ID from t_wr_ivr_survey_details table for IVR Data");
		
		IVRSurveyDetails ivrDataDetails =iVRSurveyDetailsRepository.findLatestRecord();
		
		LOG.info("GisServiceImpl::updateIVRData():findLatestRecord():Survey ID value to call the API -" + (ivrDataDetails!=null? ivrDataDetails.getSurveyId():0));
		
		
		String returnResponse ="";
		//String url = "http://8028.et:3000/gisapi/getIvrDataAPI?ivrSurveyId=7&ivrDateBySurvey=2017-08-26";
		String url = ivrdataapi+"?ivrSurveyId="+ivrDataDetails.getSurveyId()+"&ivrDateBySurvey="+DateUtil.DateToString(date, WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		//String url = ivrdataapi+"?ivrSurveyId="+ivrDataDetails.getSurveyId()+"&ivrDateBySurvey=2017-08-26";
		
		LOG.info("GisServiceImpl::updateIVRData():API URL for IVR Data -" + url);
		try {
			
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", ivrauth);
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<String> response = restTemplate.exchange(
		        url,
		        HttpMethod.GET,
		        request,
		        String.class
		);
		
		LOG.info("GisServiceImpl::updateIVRData():IVR API Data response :-" + response.getBody());
		//pass list and compare 
		String status =updateIVRDataList(response.getBody(),queResList,date);
		
		
		
		
		returnResponse=status;
		LOG.info("GisServiceImpl::updateIVRData():IVR data updated");
		} catch (Exception e) {
			LOG.error("GisServiceImpl::updateIVRData():Error in IVR data update",e);
			returnResponse="Fail";
		}
		
		return returnResponse;
	}
	
	
	
	@Transactional
	public String saveIVRQuestionAndOptions(String jsonResponse, int surveyId) throws JSONException {
		   JSONObject ivrQueResponse = new JSONObject();
		   
		   LOG.info("GisServiceImpl::saveIVRQuestionAndOptions():Question master api  data to be save/update into database -" + new Date());
		  
		  try {
			  
		  JSONObject json = new JSONObject(jsonResponse);
		  JSONObject responJson =json.getJSONObject("response");
		  JSONArray jsaQus = responJson.getJSONArray("questionDetails");
		  JSONArray responseList =  new JSONArray();
		  Date createdDate = new Date();
		  Question qst =null;
		  Question qusEntity=null;
		  
		  LOG.info("GisServiceImpl::saveIVRQuestionAndOptions():getting the api data in json formate -" + new Date());
			if (jsaQus.length() > 0) {
				
				 LOG.info("GisServiceImpl::saveIVRQuestionAndOptions():Question count from api -" + jsaQus.length());
				
				for (int i = 0; i < jsaQus.length(); i++) {
					
					JSONObject questData = null;
					
					JSONObject qusObj = jsaQus.getJSONObject(i);

					String queName = qusObj.getString("queName");
					int queOrder = qusObj.getInt("queOrder");
					
					//LOG.info("GisServiceImpl::findByIvrSurveyIdAndQustionOrder():service started -" + new Date());
					
					qusEntity =questionRepository.findByIvrSurveyIdAndQustionOrder(surveyId,queOrder);
					
					//LOG.info("GisServiceImpl::findByIvrSurveyIdAndQustionOrder():service closed -" + new Date());
					
					JSONArray jsaOption = qusObj.getJSONArray("optionDetails");
					
					if(qusEntity==null) {
						
					Question q = new Question();
					q.setQustion(queName);
					q.setQustionType(3);
					q.setQustionOrder(queOrder);
					q.setQustionOptionCount(jsaOption.length());
					q.setCreatedBy(1);
					q.setStatus(false);
					q.setCreatedOn(createdDate);
					q.setIvrSurveyId(surveyId);
					
					qst = questionRepository.save(q);
					
					
					}else {
						
						qusEntity =questionRepository.findByQustionId(qusEntity.getQustionId());
						qusEntity.setQustion(queName);
						qusEntity.setQustionType(3);
						qusEntity.setQustionOrder(queOrder);
						qusEntity.setQustionOptionCount(jsaOption.length());
						qusEntity.setUpdatedBy(1);
						qusEntity.setStatus(false);
						qusEntity.setUpdatedOn(createdDate);
						qusEntity.setIvrSurveyId(surveyId);
						
						qst = questionRepository.save(qusEntity);
						
						//LOG.info("GisServiceImpl:: questionRepository.save(qusEntity):update questions -" + new Date());
					}
					if(qst!=null) {
						
						questData = new JSONObject();
						questData.put("qusId", qst.getQustionId());
						questData.put("ordId", qst.getQustionOrder());
						responseList.put(questData);
						
					}
					ivrQueResponse.put("questiArrList", responseList);
					
					//if(qst.getQustionOptionCount()!=jsaOption.length()) {
					
					if(qusEntity==null) {  //if question new add that time add options into table if question update that time not add options into table
						
					if (jsaOption.length() > 0) {
						QuestionOption opObject = null;
						for (int j = 0; j < jsaOption.length(); j++) {
							
							JSONObject optionObj = jsaOption.getJSONObject(j);
							String optValue = optionObj.getString("optValue");
							int optId = optionObj.getInt("optId");
							
							
							
							opObject = new QuestionOption();
							opObject.setOptionNumber(j+1);
							opObject.setOptionValue(String.valueOf(optId));
							Question q1 = new Question();
							q1.setQustionId(qst.getQustionId());
							opObject.setQustionId(q1);

							opObject.setOptionInfo(optValue);
							opObject.setCreatedBy(1);
							opObject.setCreatedOn(createdDate);
							questionOptionRepository.saveAndFlush(opObject);
							
							//LOG.info("GisServiceImpl:: questionOptionRepository.saveAndFlush(opObject);:save options question against -" + new Date());
						}
						
					}
					
				   }else if(qst.getQustionOptionCount()!=jsaOption.length()) {  //if question option count and getting json options count not equal than add options into table
					   
						if (jsaOption.length() > 0) {
							
							//delete the existing options of the question
							questionOptionRepository.deleteExistingOptionsByQuestionId(qst.getQustionId()); //Add by Prasanta on 09-Sep-2021
							
							QuestionOption opObject = null;
							for (int j = 0; j < jsaOption.length(); j++) {
								
								JSONObject optionObj = jsaOption.getJSONObject(j);
								String optValue = optionObj.getString("optValue");
								int optId = optionObj.getInt("optId");
								
								opObject = new QuestionOption();
								opObject.setOptionNumber(j+1);
								opObject.setOptionValue(String.valueOf(optId));
								Question q1 = new Question();
								q1.setQustionId(qst.getQustionId());
								opObject.setQustionId(q1);

								opObject.setOptionInfo(optValue);
								opObject.setCreatedBy(1);
								opObject.setCreatedOn(createdDate);
								questionOptionRepository.saveAndFlush(opObject);
								
								//LOG.info("GisServiceImpl:: questionOptionRepository.saveAndFlush(opObject);:save options question against -" + new Date());
							}
							
						}
					   
				   }
				//}
					  
				
				}
				
				 LOG.info("GisServiceImpl::saveIVRQuestionAndOptions():Question master api  data  saved or updated into database -" + new Date());
				
			}
			ivrQueResponse.put("Status", WrsisPortalConstant.SUCCESS);
			  
		} catch (Exception e) {
			ivrQueResponse.put("Status", WrsisPortalConstant.FAILURE);
			LOG.error("GisServiceImpl::saveIVRQuestionAndOptions() !! Error in api question master data save/update",e);
		
	}
		  LOG.info("GisServiceImpl::saveIVRQuestionAndOptions():After save questions/options and return questiArrList  service closed-" + new Date());
		  return ivrQueResponse.toString();
	}
	
	
	
	@Transactional
	public String updateIVRDataList(String jsonResponse,String queResList, Date surveyDate) throws JSONException {
		   JSONObject ivrUpdatedRes = new JSONObject();
		   
		   LOG.info("GisServiceImpl::updateIVRDataList():IVR data going to save in table");
		  
		  try {
		  JSONArray jsonarr= new JSONArray(jsonResponse);
		  Date createdDate = new Date();
		  
		
			//if (jsonarr.length() > 0) {
				
				LOG.info("GisServiceImpl::updateIVRDataList():transction ivr json count " + jsonarr.length());
					
					ImportIVRFile ivEnt = new ImportIVRFile();
					ivEnt.setCreatedBy(1);
					ivEnt.setCreatedOn(createdDate);
					//ivEnt.setRecordCount(0);
					ivEnt.setFileName("API");
					ivEnt.setStatus(false);
					ivEnt.setApiResponse(jsonarr.toString());
					ImportIVRFile ivrFileObj = manageIVRDataRepository.saveAndFlush(ivEnt);
					
					LOG.info("GisServiceImpl::updateIVRDataList():data save into t_wr_import_ivr_files table-" + new Date());
					
					Integer fileId = ivrFileObj.getImprtIVRId();
					
					IVRDataEntity ivrDataObj = null;
					int count=0;
					if (jsonarr.length() > 0) {
						
						
						
						for (int j = 0; j < jsonarr.length(); j++) {
							count=count+1;
							JSONObject ivrObj = jsonarr.getJSONObject(j);
							String phoneNumber = ivrObj.getString("phoneNumber");
							String surveyTitle = ivrObj.getString("surveyTitle");
							String region = ivrObj.getString("region");
							String clientType = ivrObj.getString("ClientType");
							String language = ivrObj.getString("language");
							int surveyId = ivrObj.getInt("surveyId");
							String gender = ivrObj.getString("gender");
							String responded = ivrObj.getString("responded");
							String zone = ivrObj.getString("zone");
							String woreda = ivrObj.getString("woreda");
							
							ivrDataObj = new IVRDataEntity();
							ivrDataObj.setCreatedBy(1);
							ivrDataObj.setMobile(phoneNumber);
							ivrDataObj.setLanguage(language);
							ivrDataObj.setSurveyTitle(surveyTitle);
							ivrDataObj.setRegionName(region);
							ivrDataObj.setZoneName(zone);
							ivrDataObj.setWoredaName(woreda);
							ivrDataObj.setGender(gender);
							ivrDataObj.setResponded(responded);
							ivrDataObj.setSurveyId(surveyId);
							ivrDataObj.setClientType(clientType);
							ivrDataObj.setCallDataTime(surveyDate);
							ivrDataObj.setCreatedOn(createdDate);

							ImportIVRFile iv = new ImportIVRFile();

							iv.setImprtIVRId(fileId);
							ivrDataObj.setIvrFileId(iv);

							int regionId = 0;
							//DemographicEntity zoneId = demographicRepository.findByDemographyNameIgnoreCase(zone);
							List<DemographicEntity> zoneList = demographicRepository.findByLevelIdAndDemographyNameIgnoreCaseAndStatus(3,zone,false);
							//LOG.info("GisServiceImpl::updateIVRDataList():findByLevelIdAndDemographyNameIgnoreCaseAndStatus():get demographydata from table pass levelid 3 -" + new Date());
							if (!zoneList.isEmpty()) {
								
								if(zoneList.size()==1) {
								   regionId = zoneList.get(0).getParentId();
								   ivrDataObj.setZoneId(zoneList.get(0).getDemographyId());
								
							    } else if (zoneList.size() > 1) {		
									for (int k = 0; k < zoneList.size(); k++) {
		
										Integer masterParentId = zoneList.get(k).getParentId();//regionId
										DemographicEntity regionObj = demographicRepository.findByDemographyId(masterParentId);
										
										if(regionObj.getDemographyName().equalsIgnoreCase(region)) {
											regionId = masterParentId;
											ivrDataObj.setZoneId(zoneList.get(k).getDemographyId());
											break;
										}
									}
								}
							} else {
								ivrDataObj.setZoneId(0);
							}

							List<DemographicEntity> woredaList = demographicRepository.findByLevelIdAndDemographyNameIgnoreCaseAndStatus(4,woreda,false);
							
							if (!woredaList.isEmpty()) {
								
                                 if(woredaList.size()==1) {
                                	 
								     ivrDataObj.setWoredaId(woredaList.get(0).getDemographyId());//
								 
                                 }else if (woredaList.size() > 1) {
 
                                	 for (int i = 0; i < woredaList.size(); i++) {
                                		 
                                	 DemographicEntity zoneObj = demographicRepository.findByDemographyId(woredaList.get(i).getParentId());

     									      if(ivrDataObj.getZoneId()!=0 && zoneObj.getDemographyId()==ivrDataObj.getZoneId()) { //
     									    	
     									    	 ivrDataObj.setWoredaId(woredaList.get(i).getDemographyId());
     									    	 break;

     									      }else {
     									    	  
     									    	 if(zoneObj.getDemographyName().equalsIgnoreCase(zone)) {
     									    		ivrDataObj.setWoredaId(woredaList.get(i).getDemographyId());
     												break;
     											}  
     									   }
                                	 }

     							}
							} else {
								ivrDataObj.setWoredaId(0);
							}

							ivrDataObj.setRegionId(regionId);

							IVRDataEntity ivrDEnt = iVRDataServiceImpl.saveAndUpdateIvrData(ivrDataObj);
							
							
							JSONArray ivrQueOptArr = ivrObj.getJSONArray("pageQuestions");
							if (ivrQueOptArr.length() > 0) {
								IVRDataQuestionEntity qustion = null;
								for (int k = 0; k < ivrQueOptArr.length(); k++) {
									qustion = new IVRDataQuestionEntity();
									JSONObject ivrDataOrdObj = ivrQueOptArr.getJSONObject(k);
									
									int ivrOrdId = ivrDataOrdObj.getInt("q_order");
									int qusVal = ivrDataOrdObj.getInt("q_val");
									JSONObject questionObj = new JSONObject(queResList);
									JSONArray questionArr = questionObj.getJSONArray("questiArrList");
                                     if (questionArr.length() > 0) {
										
										for (int l = 0; l < questionArr.length(); l++) {
											
											JSONObject qusOrd = questionArr.getJSONObject(l);
											
											int queOrderId = qusOrd.getInt("ordId");
											int queId = qusOrd.getInt("qusId");
											
											if(ivrOrdId == queOrderId) {
												
												qustion.setQustionId(queId);
	
											}
									}
									
								    }
									qustion.setStatus(false);
									qustion.setQustionResponse(Integer.toString(qusVal) );
									IVRDataEntity d = new IVRDataEntity();
									d.setIvrDataId(ivrDEnt.getIvrDataId());
									qustion.setIvrDataId(d);
									qustion.setCreatedBy(1);
									qustion.setCreatedOn(createdDate);
									iVRQuestionRepository.saveAndFlush(qustion);
								}
								
							}
							
						}
						
			}else {
				
				 LOG.info("GisServiceImpl::updateIVRDataList():no data found in json " + new Date());
			}
			ivEnt.setRecordCount(count);
			manageIVRDataRepository.saveAndFlush(ivEnt);
			LOG.info("GisServiceImpl::updateIVRDataList():transction ivr data saved in database (count)" + count);
			ivrUpdatedRes.put("Status", WrsisPortalConstant.SUCCESS);
			  
		} catch (Exception e) {
			ivrUpdatedRes.put("Status", WrsisPortalConstant.FAILURE);
			LOG.error("GisServiceImpl::updateIVRDataList() Error in save ivr transction data!!", e);
		
	}
		  LOG.info("GisServiceImpl::updateIVRDataList():After save IVR data and return status service closed-" + new Date());
		  return ivrUpdatedRes.toString();
	}

	@Override
	public JSONObject getStyleDetails() {
		JSONArray depositionArray = new JSONArray();
		JSONArray environmentArray = new JSONArray();
		JSONObject response = new JSONObject();
		try {
		

			List<Object[]> styleList = gisMapRepository.getStyleDetails();
			JSONObject styleObj = null;
			if (!styleList.isEmpty()) {
				for (Object[] obj : styleList) {
					styleObj = new JSONObject();
					styleObj.put("styleId", String.valueOf(obj[0]));
					styleObj.put("styleName", String.valueOf(obj[1]));
					styleObj.put("styleRange", String.valueOf(obj[2]));
					styleObj.put("mapTypeId", String.valueOf(obj[3]));
					styleObj.put("rustTypeId", String.valueOf(obj[4]));
                    String mapId = String.valueOf(obj[3]);
                    if(Integer.parseInt(mapId) ==1) {		
                    depositionArray.put(styleObj);
                    }else {
                    environmentArray.put(styleObj);	
                    }

				}

			}

			response.put("depositionArr", depositionArray);
			response.put("environmentArr", environmentArray);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getStyleDetails():" + e);
		}
		return response;
	}

	@Override
	public boolean isGisLayerExistDeleteByDate(Date uploadDate) {
		////List<GisMapEntity> storeList =	gisMapRepository.getGISMapRecordByDate(uploadDate);
		int count = 0;
		try {
			Date formatedDate = DateUtil.getFromatedDate(uploadDate);
			
			List<GisMapEntity> storeList = gisMapRepository.getGISMapRecordByDate(formatedDate);
			if (!storeList.isEmpty()) {
				LOG.info("No. of stores returned from DB : " + storeList.size());
				for (GisMapEntity file : storeList) {
					String status = deleteGISStore(file.getStoreName(), giscmd, GIS_WORKSPACE);
					if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {
						file.setBitStatus(true);
						file.setUpdatedon(new Timestamp(new Date().getTime()));
						gisMapRepository.saveAndFlush(file);
						count++;
					}
				}
			}
			LOG.info("No. of stores deleted : " + count);
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getfilesbeforedate() : " + e);
			return false;
		}
		//return count;
		return true;
	}

	
	@Override
	public  JSONObject getGISIVRdata(String startDate, String endDate) {
			JSONArray ivrApiArray = new JSONArray();
			JSONObject response = new JSONObject();
			
		   try {

			List<Object[]> gisIvrList = gisMapRepository.getGISIVRdata(startDate,endDate);
			
			JSONObject ivrApiObj = null;
			
			if (!gisIvrList.isEmpty()) {
				
				for (Object[] obj : gisIvrList) {
					
					ivrApiObj = new JSONObject();
					
					ivrApiObj.put("region",String.valueOf(obj[0]));
					ivrApiObj.put("zone", String.valueOf(obj[1]));
					ivrApiObj.put("woreda", String.valueOf(obj[2]));
					ivrApiObj.put("count",  String.valueOf(obj[3]));
					ivrApiObj.put("lat", String.valueOf(obj[4]));
					ivrApiObj.put("long", String.valueOf(obj[5]));
					
					ivrApiArray.put(ivrApiObj);
				}  
              
			}

			response.put("ivrApiArr", ivrApiArray);
			response.put("startDate", startDate);
			response.put("endDate", endDate);
			//response.put("startDate", new java.sql.Date( new SimpleDateFormat("dd-MMM-yyyy").parse(startDate).getTime()));
			//response.put("endDate", new java.sql.Date( new SimpleDateFormat("dd-MMM-yyyy").parse(endDate).getTime()));
		} catch (Exception e) {
			LOG.error("GisServiceImpl::getStyleDetails():" + e);
		}
		return response;
	}

	
	@Override
	public JSONObject getIVRCountDetails(String startDate, String endDate,String regionName,String zoneName,String woredaName) {
		   JSONArray ivrCountArray = new JSONArray();
		   JSONArray quansArr = new JSONArray();
		   JSONObject response = new JSONObject();
		
	   try {

		List<Object[]> countDetails = gisMapRepository.getIVRCountDetails(startDate,endDate,regionName,zoneName,woredaName);
		JSONObject ivrApiObj = null;
		JSONObject q_and_aObj = null;
		
		if (!countDetails.isEmpty()) {
			
			for (Object[] obj : countDetails) {
				
				ivrApiObj = new JSONObject();
				
				
				
					
				ivrApiObj.put("vch_mobileno", String.valueOf(obj[2]));
				ivrApiObj.put("vch_language", String.valueOf(obj[10]));
				ivrApiObj.put("vch_client_type", String.valueOf(obj[11]));
				ivrApiObj.put("vch_gender",String.valueOf(obj[12]));
				ivrApiObj.put("vch_responded",String.valueOf(obj[9]));
				ivrApiObj.put("vch_survey_title",String.valueOf(obj[13]));
				ivrApiObj.put("vch_region", String.valueOf(obj[3]));
				ivrApiObj.put("vch_zone", String.valueOf(obj[4]));
				ivrApiObj.put("vch_woreda",String.valueOf(obj[5]));
				
				 JSONArray jsonarr= new JSONArray(String.valueOf(obj[8]));
				 
				    
					if (jsonarr.length() > 0) {
					
						for (int j = 0; j < jsonarr.length(); j++) {
							q_and_aObj = new JSONObject();
							JSONObject ivrObj = jsonarr.getJSONObject(j);
						
							q_and_aObj.put("slno", j+1);
							q_and_aObj.put("que", ivrObj.getString("vch_question"));
							q_and_aObj.put("ans", ivrObj.getString("incdoption"));
							quansArr.put(q_and_aObj);
						}
						
						ivrApiObj.put("q_and_a", quansArr);
						quansArr = new JSONArray();
					}
					
				
				ivrCountArray.put(ivrApiObj);
              
			}

		}

		response.put("ivrCountArray", ivrCountArray);
	} catch (Exception e) {
		LOG.error("GisServiceImpl::getIVRCountDetails():" + e);
	}
	return response;
	}

}
