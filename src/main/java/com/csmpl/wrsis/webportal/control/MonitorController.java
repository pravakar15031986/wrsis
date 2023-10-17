package com.csmpl.wrsis.webportal.control;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.MonitorBeanDataTable;
import com.csmpl.wrsis.datatable.ViewImplementationDataTable;
import com.csmpl.wrsis.webportal.bean.MonitorBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.MonitorService;

@Controller
public class MonitorController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(MonitorController.class);

	@Autowired
	MonitorService monitorService;
	@Autowired
	ManageDemographicService manageDemographicService;

	@Autowired
	MonitorFungicideRepository monitorFungicideRepository;
	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;

	@GetMapping("/viewMonitorImplementation")
	public String viewMonotorImp(Model model, @ModelAttribute("searchVo") SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewMonitorImplementation";
	}

	@RequestMapping("/viewMonotorImpPageWise")
	public @ResponseBody MonitorBeanDataTable viewMonotorImpPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam("recoMendNo") String recoMendNo) {
		MonitorBeanDataTable mbdt = null;

		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			int userId=(int) session.getAttribute(WrsisPortalConstant.USER_ID);
			List<MonitorBean> beans = monitorService.viewAllForRecommendForMonitorPage(recoMendNo.trim().toUpperCase(),userId, 
					startDate, endDate, Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = monitorService.viewAllForRecommendForMonitorPageCount(recoMendNo.trim().toUpperCase(),userId,
					startDate, endDate, -1, -1);

			mbdt = new MonitorBeanDataTable();
			mbdt.setData(beans);
			mbdt.setRecordsFiltered(totalCount);
			mbdt.setRecordsTotal(totalCount);
			mbdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("MonitorController::viewMonotorImpPageWise():" + e);
		}
		return mbdt;
	}

	@PostMapping("/viewMonitorImplementationSearch")
	public String viewMonotorImpSearch(Model model, @ModelAttribute("searchVo") SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "viewMonitorImplementation";
	}

	@PostMapping("/monitorRecommendationPublished")
	public String monitorPublished(Model model, @RequestParam("rcId") String rcNumber, HttpSession session) {
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;
		try {
			Integer totalCount = monitorService.monitorRecommendationPublishedDataCount(0, 0, 0, "", "", "", rcNumber,
					userId, Integer.valueOf(processId), -1, -1);
			model.addAttribute("Show", totalCount > 0);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			model.addAttribute(WrsisPortalConstant.RC_NUMBER, rcNumber);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} catch (Exception e) {
			LOG.error("MonitorController::monitorPublished():" + e);
		}

		return "monitorRecommendationPublished";
	}

	@RequestMapping("/monitorRecommendationPublishedData")
	public @ResponseBody ViewImplementationDataTable monitorRecommendationPublishedData(Model modelHttp,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate, @RequestParam(WrsisPortalConstant.TO_DATE) String toDate,
			@RequestParam(WrsisPortalConstant.RC_NUMBER) String rcNumber, RedirectAttributes redirect) {

		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.monitorRecommendationPublishedData(
				regionId, zoneId, woredaId,
				monitorRefNumber.toUpperCase(), fromDate, toDate, rcNumber, userId, Integer.valueOf(processId),
				Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.monitorRecommendationPublishedDataCount(regionId,
				zoneId,woredaId, monitorRefNumber.toUpperCase(), fromDate, toDate,
				rcNumber, userId, Integer.valueOf(processId), -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));

		return sdt;
	}

	@PostMapping("/monitorRecommendationPublishedSearch")
	public String monitorRecommendationPublishedSearch(Model model, HttpSession session,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.REC_DT_FROM) String recDtFrom,
			@RequestParam(WrsisPortalConstant.REC_DT_TO) String recDtTo,
			@RequestParam("recommendNumber") String recommendNumber, @RequestParam(WrsisPortalConstant.RC_NUMBER) String rcNumber) {

		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.MONITOR_REF_NUMBER, monitorRefNumber);
		model.addAttribute(WrsisPortalConstant.REC_DT_FROM, recDtFrom);
		model.addAttribute(WrsisPortalConstant.REC_DT_TO, recDtTo);
		model.addAttribute("recommendNumber", recommendNumber);
		model.addAttribute(WrsisPortalConstant.RC_NUMBER, rcNumber);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;

		try {
			Integer totalCount = monitorService.monitorRecommendationPublishedDataCount(regionId,
					zoneId, woredaId, monitorRefNumber.toUpperCase(), recDtFrom,
					recDtTo, rcNumber, userId, Integer.valueOf(processId), -1, -1);

			model.addAttribute("Show", totalCount > 0);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		} catch (Exception e) {
			LOG.error("MonitorController::monitorRecommendationPublishedSearch():" + e);
		}
		return "monitorRecommendationPublished";
	}

	@PostMapping("/monitorRecommendationPublishedView")
	public String monitorRecommendationPublishedView(Model model, @RequestParam("rcId") String rcNumber) {

		try {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			model.addAttribute(WrsisPortalConstant.RC_NUMBER, rcNumber);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception e) {
			LOG.error("MonitorController::monitorRecommendationPublishedView():" + e);

		}
		return "monitorRecommendationPublishedView";
	}

	@RequestMapping("/monitorRecommendationPublishedViewData")
	public @ResponseBody ViewImplementationDataTable monitorRecommendationPublishedViewData(Model modelHttp,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate, @RequestParam(WrsisPortalConstant.TO_DATE) String toDate,
			@RequestParam(WrsisPortalConstant.RC_NUMBER) String rcNumber, RedirectAttributes redirect) {

		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.monitorRecommendationPublishedData(
				regionId, zoneId,woredaId,
				monitorRefNumber.toUpperCase(), fromDate, toDate, rcNumber, userId, Integer.valueOf(processId),
				Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.monitorRecommendationPublishedDataCount(regionId,
				zoneId, woredaId, monitorRefNumber.toUpperCase(), fromDate, toDate,
				rcNumber, userId, Integer.valueOf(processId), -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));

		return sdt;
	}

	@PostMapping("/monitorRecommendationPublishedViewSearch")
	public String monitorRecommendationPublishedViewSearch(Model model, HttpSession session,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.REC_DT_FROM) String recDtFrom,
			@RequestParam(WrsisPortalConstant.REC_DT_TO) String recDtTo,
			@RequestParam("recommendNumber") String recommendNumber, @RequestParam(WrsisPortalConstant.RC_NUMBER) String rcNumber) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);

		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute(WrsisPortalConstant.MONITOR_REF_NUMBER, monitorRefNumber);
		model.addAttribute(WrsisPortalConstant.REC_DT_FROM, recDtFrom);
		model.addAttribute(WrsisPortalConstant.REC_DT_TO, recDtTo);
		model.addAttribute("recommendNumber", recommendNumber);
		model.addAttribute(WrsisPortalConstant.RC_NUMBER, rcNumber);

		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;

		try {
			Integer totalCount = monitorService.monitorRecommendationPublishedDataCount(regionId,
					zoneId, woredaId, monitorRefNumber.toUpperCase(), recDtFrom,
					recDtTo, rcNumber, userId, Integer.valueOf(processId), -1, -1);

			model.addAttribute("Show", totalCount > 0);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		} catch (Exception e) {
			LOG.error("MonitorController::monitorRecommendationPublishedViewSearch():" + e);
		}
		return "monitorRecommendationPublishedView";
	}

	@RequestMapping(value = { "/viewRecommendationsDetailsOnMonitor" })
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
			LOG.error("MonitorController::viewImplementationDetails():" + e);
		}
		return "viewRecommendationsDetailsOnMonitor";
	}

	@PostMapping("/published-discard-monitor")
	public String publishedDiscardMonitor(RedirectAttributes model, @RequestBody String jsonString,
			HttpServletRequest request)// @RequestParam("optionId")Integer optionId,
	{
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String msg = monitorService.publishedAndDiscardMonitorData(jsonString, userId);// optionId,
		model.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, msg);
		return "redirect:/viewMonitorImplementation";

	}

	@GetMapping("/publishedMonitorData")
	public String publishedMonitorData(Model model) {
		try {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		} catch (Exception e) {
			LOG.error("MonitorController::publishedMonitorData():" + e);
		}

		return "publishedMonitorData";
	}

	// Add pagination code
	@RequestMapping(value = { "/viewMonitorPublishedData" })
	public @ResponseBody ViewImplementationDataTable viewMonitorPublishedData(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("monitorrefNo") String monitorrefNo, @RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate,
			@RequestParam(WrsisPortalConstant.TO_DATE) String toDate, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.viewMoniterPublishedAndDiscardSearch(
				regionId, zoneId, woredaId, monitorrefNo, fromDate,
				toDate, 1, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.viewMoniterPublishedAndDiscardSearchCount(regionId,
				zoneId, woredaId, monitorrefNo, fromDate, toDate, 1, -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@PostMapping("/monitorPublishedSearch")
	public String monitorPublishedSearch(Model model, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.REC_DT_FROM) String recDtFrom,
			@RequestParam(WrsisPortalConstant.REC_DT_TO) String recDtTo) {

		try {
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
			model.addAttribute(WrsisPortalConstant.MONITOR_REF_NUMBER, monitorRefNumber);
			model.addAttribute(WrsisPortalConstant.REC_DT_FROM, recDtFrom);
			model.addAttribute(WrsisPortalConstant.REC_DT_TO, recDtTo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			if (regionId != 0) {
				model.addAttribute("zonelist", manageDemographicService.getDemographyListByParentId(regionId));
			}
			if (regionId != 0 && zoneId != 0) {
				model.addAttribute("woredalist", manageDemographicService.getDemographyListByParentId(zoneId));
			}

			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		} catch (Exception e) {

			LOG.error("MonitorController::monitorPublishedSearch():" + e);
		}

		return "publishedMonitorData";
	}

	@GetMapping("/discardedMonitorData")
	public String discardedMonitorData(Model model) {
		try {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		} catch (Exception e) {
			LOG.error("MonitorController::discardedMonitorData():" + e);
		}

		return "discardedMonitorData";
	}

	// Add pagination code
	@RequestMapping(value = { "/viewMonitorDiscardData" })
	public @ResponseBody ViewImplementationDataTable viewMonitorDiscardData(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("monitorrefNo") String monitorrefNo, @RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate,
			@RequestParam(WrsisPortalConstant.TO_DATE) String toDate, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.viewMoniterPublishedAndDiscardSearch(
				regionId,zoneId, woredaId, monitorrefNo, fromDate,
				toDate, 2, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.viewMoniterPublishedAndDiscardSearchCount(regionId,
				zoneId, woredaId, monitorrefNo, fromDate, toDate, 2, -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@PostMapping("/monitorDiscardedSearch")
	public String monitorDiscardedSearch(Model model, @RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam(WrsisPortalConstant.MONITOR_REF_NUMBER) String monitorRefNumber,
			@RequestParam(WrsisPortalConstant.REC_DT_FROM) String recDtFrom,
			@RequestParam(WrsisPortalConstant.REC_DT_TO) String recDtTo) {

		try {
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
			model.addAttribute(WrsisPortalConstant.MONITOR_REF_NUMBER, monitorRefNumber);
			model.addAttribute(WrsisPortalConstant.REC_DT_FROM, recDtFrom);
			model.addAttribute(WrsisPortalConstant.REC_DT_TO, recDtTo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			if (regionId != 0) {
				model.addAttribute("zonelist", manageDemographicService.getDemographyListByParentId(regionId));
			}
			if (regionId != 0 && zoneId != 0) {
				model.addAttribute("woredalist", manageDemographicService.getDemographyListByParentId(zoneId));
			}
		} catch (Exception e) {
			LOG.error("MonitorController::monitorDiscardedSearch():" + e);

		}

		return "discardedMonitorData";
	}

}
