package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.MonitorRecommendationDataTable;
import com.csmpl.wrsis.datatable.ViewImplementationDataTable;
import com.csmpl.wrsis.webportal.bean.RecommendationBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;
import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.UserLocationDetails;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographicTaggingRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationLocationRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.UserLocationDetailsRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.service.MobileService;
import com.csmpl.wrsis.webportal.service.MonitorService;
import com.csmpl.wrsis.webportal.service.RecomendationService;

@Controller
public class ImplementationController {

	public static final Logger LOG = LoggerFactory.getLogger(ImplementationController.class);

	@Autowired
	RecomendationService recomendationService;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@Autowired
	GrowthRepository growthRepository;

	@Autowired
	FungicideMasterRepository fungicideMasterRepository;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	MonitorFungicideRepository monitorFungicideRepository;

	@Autowired
	MonitorService monitorService;

	@RequestMapping(value = { "/woredaExpertImplementation" })
	public String woredaExpertRecommendation(Model model, SearchVo searchVo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		searchVo.setUserId(userId);

		

		return "woredaExpertImplementation";
	}

	@RequestMapping(value = { "/viewAddMonitorRecommendation" })
	public @ResponseBody MonitorRecommendationDataTable viewAddMonitorRecommendation(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("recDtFrom") String recDtFrom,
			@RequestParam("recDtTo") String recDtTo, @RequestParam("recNo") String recNo, RedirectAttributes redirect) {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<RecommendationBean> beans = recomendationService.viewAddMonitorRecommendation(recDtFrom, recDtTo, recNo,
				userId, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = recomendationService.viewAddMonitorRecommendationCount(recDtFrom, recDtTo, recNo,
				userId, -1, -1);

		MonitorRecommendationDataTable sdt = new MonitorRecommendationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		
		return sdt;
	}

	@Autowired
	RecommendationLocationRepository recommendationLocationRepository;
	@Autowired
	RecommendationRepository recommendationRepository;
	@Autowired
	DemographicTaggingRepository demographicTaggingRepository;
	@Autowired
	UserLocationDetailsRepository userLocationDetailsRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@RequestMapping(value = { "/woredamonitorrecommendations" })
	public String woredamonitorrecommendationsmod(Model model, HttpServletRequest request,
			@RequestParam(value = "recomId", required = false) int recomId,
			@RequestParam(value = "recomno", required = false) String recomno) {
		try {
			RecommendationBean recommendation = new RecommendationBean();
			recommendation.setRecomendId(recomId);
			recommendation.setRecomendNoBean(recomno);

			HttpSession session = request.getSession();
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			recommendation.setLoginUserId(userId);
			List<DemographicEntity> regionList = new ArrayList<>();
			DemographicEntity obj = null;
			String demographyName = null;
			
			// fetch region,zone,woreda,kebele
			Integer recomType = recommendationRepository.getRecomendTypeByRecomId(recomId);
			model.addAttribute("recomType", recomType);
			if (recomType == 1) {
				List<Object[]> demographList = seasionMasterRepository.fetchAllUserWiseImplementionRegion(userId);
				model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			} else {
				List<Short> regionIdList = recommendationLocationRepository.getRegionIdByRecomendIdAndStatus(recomId);
				List<Short> regionsTaggedToUser = new ArrayList<>();
				List<Object[]> tagLocations = demographicTaggingRepository.getRegionTaggedByUserId(userId);
				for (Object[] tagLocation : tagLocations) {
					if (tagLocation != null) {

						String[] userLocationArr = tagLocation[4].toString().split(",");
						for (int k = 0; k < userLocationArr.length; k++) {

							UserLocationDetails locationuser = userLocationDetailsRepository
									.getOne(Integer.parseInt(userLocationArr[k]));
							if (!locationuser.isStatus() && Integer.parseInt(tagLocation[2].toString()) == 4) {
								
									DemographicEntity woredaId = demographicRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());
									DemographicEntity zoneId = demographicRepository
											.findByDemographyId(woredaId.getParentId());
									DemographicEntity regionId = demographicRepository
											.findByDemographyId(zoneId.getParentId());
									regionsTaggedToUser.add((short) regionId.getDemographyId());
								
							}
						}
					}
				}
					
				regionIdList.retainAll(regionsTaggedToUser);
				
				for (Short regionId : regionIdList) {
					demographyName = demographyRepository.getDemographyNameByDemographyId(regionId.intValue());
					obj = new DemographicEntity();
					obj.setDemographyId(regionId);
					obj.setDemographyName(demographyName);
					regionList.add(obj);
				}
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}

			// fetch all the variety type

			List<VarietyTypeMasterEntity> varietyList = varietyTypeRepository.fetchActiveVarietyType();
			model.addAttribute("VarietyList", varietyList);

			// fetch all active Rust types
			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
			model.addAttribute("RustTypeList", rustTypeList);

			// fetch all active growth
			List<GrowthMasterEntity> growthList = growthRepository.fetchAllActiveGrowth();
			model.addAttribute("GrowthList", growthList);

			// fetch all active Fungis types
			List<FungicideMaster> fungiList = fungicideMasterRepository.fetchAllActiveFungicides();
			model.addAttribute("FungiList", fungiList);
			model.addAttribute("recommendation", recommendation);
			model.addAttribute("Update", false);
			model.addAttribute("monitorId", 0);
			session.removeAttribute(WrsisPortalConstant.MONITOR_JSON);
		} catch (Exception e) {
			LOG.error("ImplementationController::woredamonitorrecommendationsmod():" + e);
		}

		return "woredamonitorrecommendations";
	}

	@Autowired
	MobileService mobileService;

	@RequestMapping(value = { "/saveImplementationData" })
	public String saveImplementationData(Model model, HttpServletRequest request,
			@RequestParam(value = "jsonencode", required = true) String jsonencode, RedirectAttributes redirectAttrs,
			@RequestParam("monitorId") Integer monitorId) {
		String retuenPage = "";
		JSONArray kebeleDetailsjsa = null;
		Integer recId = null;
		Integer woredaId = null;

		try {
			String decodeJSON = new String(Base64.getDecoder().decode(jsonencode));
			JSONObject implementationJson = new JSONObject(decodeJSON);
			if (implementationJson.has("kebelejsa")) {
				kebeleDetailsjsa = implementationJson.getJSONArray("kebelejsa");
				JSONObject jsonObj = kebeleDetailsjsa.getJSONObject(0);
				recId = Integer.valueOf(jsonObj.getString("recomId"));
				woredaId = Integer.valueOf(jsonObj.getString("woredaId"));
				List<MonitorDetailsEntity> monitorDetails = mobileService.getMonitorDetailsByRecIdAndWoredaId(recId,
						woredaId);

				// Check if recommendation Survey is present in table or not
				if (monitorDetails != null && !monitorDetails.isEmpty()) {
					// Check if recommendation Survey is published or discared or pending
					// respectively
					if (monitorDetails.get(0).getMonitorStatus() == 1) {
						redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG,
								"This recommendation survey is already published.");
						return "redirect:woredaExpertImplementation";
					}
					if (monitorDetails.get(0).getMonitorStatus() == 2) {
						redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
								"This recommendation survey is already discarded.");
						return "redirect:woredaExpertImplementation";
					}
					if (monitorDetails.get(0).getMonitorStatus() == 0) {
						monitorId = monitorDetails.get(0).getMonitorid();
					}
				}
				String implementationStatus = mobileService.saveSurveyImplementation(jsonencode, 1, monitorId);
				if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(implementationStatus)) {
					redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG,
							"Implementation has been saved successfully ");
					retuenPage = "redirect:woredaExpertImplementation";
				} else if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(implementationStatus)) {
					redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG,
							"Implementation has been updated successfully ");
					retuenPage = "redirect:viewImplementation";
				} else {
					redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Unable to save the details.");
					retuenPage = "redirect:woredaExpertImplementation";
				}
			}

		} catch (Exception e) {
			LOG.error("ImplementationController::saveImplementationData():" + e);
			redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Unable to save the details.");
			retuenPage = "redirect:woredaExpertImplementation";
		}
		return retuenPage;
	}

	@RequestMapping(value = { "/viewImplementation" })
	public String viewimplementation(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			List<Object[]> demographList = seasionMasterRepository.fetchAllUserWiseImplementionRegion(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
		} catch (Exception e) {
			LOG.error("ImplementationController::viewimplementation():" + e);
		}

		return "viewImplementation";

	}

	@RequestMapping(value = "searchImplementation", method = RequestMethod.POST)
	public String viewimplementation(Model model, HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("zoneId") String zoneId, @RequestParam("monitorNo") String monitorNo,
			@RequestParam("monitordate") String monitordate, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			List<Object[]> demographList = seasionMasterRepository.fetchAllUserWiseImplementionRegion(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute("zoneId", zoneId);
			model.addAttribute("monitorNo", monitorNo);
			model.addAttribute("monitordate", monitordate);
			
		} catch (Exception e) {
			LOG.error("ImplementationController::viewimplementation():" + e);
		}

		return "viewImplementation";

	}

	

	// dynamic pagination for view implementation

	@RequestMapping(value = { "/viewImplementationDetailsData" })
	public @ResponseBody ViewImplementationDataTable viewImplementationDetailsData(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("zoneId") String zoneId, @RequestParam("monitorNo") String monitorNo,
			@RequestParam("monitordate") String monitordate, RedirectAttributes redirect) {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.viewImplementationDetailsData(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), monitorNo.toUpperCase(), monitordate, userId, Integer.valueOf(start),
				Integer.valueOf(length));

		Integer totalCount = monitorService.viewImplementationDetailsDataCount(Integer.valueOf(regionId),
				Integer.valueOf(zoneId), monitorNo.toUpperCase(), monitordate, userId, -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
	
		return sdt;
	}

	@RequestMapping(value = { "/viewImplementationDetails" })
	public String viewImplementationDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "monitorId", required = false) int monitorId) {
		JSONObject response = new JSONObject();
		JSONArray implementationDetailsArray = new JSONArray();
		JSONArray mfungicideArray = new JSONArray();
		JSONObject mfungicideObj = new JSONObject();

		try {
			List<Object[]> recommendationDetails = monitorDetailsRepository.viewDetailsImplementation(monitorId);
			JSONObject impleListObj = null;
			if (!recommendationDetails.isEmpty()) {
				for (Object[] obj : recommendationDetails) {
					impleListObj = new JSONObject();
					impleListObj.put("montNo", String.valueOf((obj[0] != null) ? (String) obj[0] : ""));
					impleListObj.put("montDate", String.valueOf((obj[1] != null) ? (String) obj[1] : ""));
					impleListObj.put("recomNo", String.valueOf((obj[2] != null) ? (String) obj[2] : ""));
					impleListObj.put("region", String.valueOf((obj[3] != null) ? (String) obj[3] : ""));
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
					
					impleListObj.put("severity", String.valueOf((obj[17] != null) ? (String) obj[17] : ""));
					impleListObj.put("incedences", String.valueOf((obj[18] != null) ? (String) obj[18] : ""));
					impleListObj.put("sowingland", String.valueOf((obj[19] != null) ? (String) obj[19] : ""));
					impleListObj.put("pasaffeted", String.valueOf((obj[21] != null) ? (String) obj[21] : ""));
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
				model.addAttribute("implementationdata",
						new String(Base64.getEncoder().encode(implementationDetailsArray.toString().getBytes())));
			}
		} catch (Exception e) {
			LOG.error("ImplementationController::viewImplementationDetails():" + e);
		}
		return "viewImplementationDetails";
	}

	// delete monitor details
	@RequestMapping(value = "/deleteMonitorDetails", method = RequestMethod.POST)
	public @ResponseBody String deleteMonitorDetails(Model model, HttpServletRequest request,
			@RequestParam(value = "monitorId", required = true) int monitorId) {
		return monitorService.deleteMonitorDetails(monitorId);

	}

	// View monitor details
	@RequestMapping(value = "/viewMonitorDetailsById", method = RequestMethod.POST)
	public String viewMonitorDetailsById(Model model, HttpServletRequest request,
			@RequestParam(value = "monitorId", required = true) Integer monitorId, HttpSession session)
			throws JSONException {

		JSONObject response = monitorService.getFormmatedJsonByMonitorId(monitorId);
		model.addAttribute("MonitorNo", response.getString("monitorNo"));
		model.addAttribute(WrsisPortalConstant.MONITOR_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		session.setAttribute(WrsisPortalConstant.MONITOR_JSON, new String(Base64.getEncoder().encode(response.toString().getBytes())));
		model.addAttribute("Update", true);
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		
		// fetch region according to recommdeation
		List<DemographicEntity> regionList = new ArrayList<>();
		DemographicEntity obj = null;
		String demographyName = null;

		int recomId = Integer.parseInt(response.getString("recoId"));
		Integer recomType = recommendationRepository.getRecomendTypeByRecomId(recomId);
		model.addAttribute("recomType", recomType);
		if (recomType == 2) {
			List<Short> regionIdList = recommendationLocationRepository.getRegionIdByRecomendIdAndStatus(recomId);
			for (Short regionId : regionIdList) {
				demographyName = demographyRepository.getDemographyNameByDemographyId(regionId.intValue());
				obj = new DemographicEntity();
				obj.setDemographyId(regionId);
				obj.setDemographyName(demographyName);
				regionList.add(obj);
			}
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		} else if (recomType == 1) {
			List<Object[]> demographList = seasionMasterRepository.fetchAllUserWiseImplementionRegion(userId);
			model.addAttribute(WrsisPortalConstant.DEMOGRAPH_LIST, demographList);
		}

		// fetch all the variety type

		List<VarietyTypeMasterEntity> varietyList = varietyTypeRepository.fetchActiveVarietyType();
		model.addAttribute("VarietyList", varietyList);

		// fetch all active Rust types
		List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
		model.addAttribute("RustTypeList", rustTypeList);

		// fetch all active growth
		List<GrowthMasterEntity> growthList = growthRepository.fetchAllActiveGrowth();
		model.addAttribute("GrowthList", growthList);

		// fetch all active Fungis types
		List<FungicideMaster> fungiList = fungicideMasterRepository.fetchAllActiveFungicides();
		model.addAttribute("FungiList", fungiList);
		model.addAttribute("monitorId", monitorId);

		RecommendationBean recommendation = new RecommendationBean();
		recommendation.setRecomendId(Integer.valueOf(response.getString("recoId")));
		recommendation.setRecomendNoBean(response.getString("recoNo"));
		recommendation.setLoginUserId(userId);
		model.addAttribute("recommendation", recommendation);

		return "woredamonitorrecommendations";
	}

}
