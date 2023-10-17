package com.csmpl.wrsis.webportal.control;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.RecommendationDataTable;
import com.csmpl.wrsis.datatable.ViewImplementationDataTable;
import com.csmpl.wrsis.prototype.webportal.control.RecomendationController;
import com.csmpl.wrsis.webportal.bean.RecommendationBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.repository.AdvisoryRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.repository.NotificationRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationLocationRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationRepository;
import com.csmpl.wrsis.webportal.service.AdvisoryService;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.MonitorService;
import com.csmpl.wrsis.webportal.service.RecomendationService;
import com.csmpl.wrsis.webportal.util.DateUtil;
import com.csmpl.wrsis.webportal.util.EmailUtil;
import com.csmpl.wrsis.webportal.util.PushNotification;
import com.csmpl.wrsis.webportal.util.SMSUtil;
import com.csmpl.wrsis.webportal.util.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RecomendationControllerWeb extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(RecomendationController.class);

	@Autowired
	SMSUtil smsUtil;

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@Autowired
	AdvisoryService advisoryService;

	@Autowired
	PushNotification mobileNotification;

	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	RecomendationService recomendationService;
	@Autowired
	MonitorService monitorService;
	@Autowired
	ManageDemographicService manageDemographicService;
	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;
	@Autowired
	MonitorFungicideRepository monitorFungicideRepositor;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	RecommendationLocationRepository recommendationLocationRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	AdvisoryRepository advisoryRepository;

	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;
	
	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Value("${notification.indicator}")
	private String notificationFlag;
	
	@GetMapping("/recommendation")
	public String recommendation(Model model, SearchVo searchVo) {

		try {
			List<AdvisoryEntiry> advisoryPublishedList = advisoryService.viewAllPublishedAdvisory();
			model.addAttribute("advisoryList", advisoryPublishedList);
			model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::recommendation():" + e);
		}

		return "recommendation";
	}

	@RequestMapping("/recommendationSearch")
	public String searchRecomendation(Model model, SearchVo searchVo) {
		try {
			Date startDate = null;
			Date endDate = null;
			if (searchVo.getStartDate() == "")
				searchVo.setStartDate(null);
			if (searchVo.getEndDate() == "")
				searchVo.setEndDate(null);
			startDate = DateUtil.StringMMMToDate(searchVo.getStartDate());
			endDate = DateUtil.StringMMMToDate(searchVo.getEndDate());
			List<AdvisoryEntiry> advisoryPublishedList = advisoryService.viewAllPublishedAdvisory(startDate, endDate,
					searchVo.getAdvNo());
			model.addAttribute("advisoryList", advisoryPublishedList);
			model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::searchRecomendation():" + e);
		}
		return "recommendation";
	}

	@ResponseBody
	@GetMapping("/advisory-file-exist")
	public String irvFileExits(Model model, @RequestParam("fileId") Integer fileId) {
		return advisoryService.advisoryFileExits(fileId);

	}

	@PostMapping("/advisory-file-recom-download")
	public void advDownloadExcelByFileName(Model m1, ModelMap model, @RequestParam("fileId") Integer fileId,
			HttpServletResponse response) throws ParseException {
		advisoryService.downloadAdvisoryFile(response, fileId);
	}

	@PostMapping("/addrecommendation")
	public String addrecommendation(Model model, @RequestParam("adviosryId") Integer adviosryId,
			@RequestParam("adviosryNo") String adviosryNo, @ModelAttribute("recommend") RecommendationBean recommend) {
		model.addAttribute("advisoryId", adviosryId);
		model.addAttribute("adviosryNo", adviosryNo);

		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		return "addrecommendation";
	}

	@PostMapping("/add-recommendation")
	public String addRecommendation(Model model, RedirectAttributes atts,
			@RequestParam("advisoryId") Integer advisoryId, @RequestParam("recommendType") Integer recommendType,
			@RequestParam(value = "recommendFileName", required = false) MultipartFile recommendFileName,
			@RequestParam(WrsisPortalConstant.RECOMMEND_REMARK) String recommendRemark,
			@RequestParam(value = "WoredaNameId_1", required = false) String[] WoredaNameId,
			@RequestParam(value = "kebeleNameId_1", required = false) String[] kebeleNameId,
			/* @RequestParam("statusBean") Boolean statusBean, */
			@RequestParam(value = "recomendId", required = false) Integer recomendId,
			@RequestParam("sendType") String requiredSMS, @RequestParam(WrsisPortalConstant.SMS_REMARK) String smsContent,
			HttpServletRequest request) {

		String returnPage = "addrecommendation";
		try {

			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

			RecommendationBean recoBean = new RecommendationBean();
			recoBean.setAdvisoryIdBean(advisoryId);
			recoBean.setRecomendFileNameBean(recommendFileName);
			recoBean.setRecomendSummaryBean(recommendRemark);
			recoBean.setWoredaNameIdArray(WoredaNameId);
			recoBean.setKebeleNameIdArray(kebeleNameId);
			recoBean.setRecomendTypeBean(recommendType);
			recoBean.setStatusBean(false);
			recoBean.setCreatedByBean(userId);
			recoBean.setSmsContent(smsContent);
			if (recoBean.getSmsContent().length() > 0) {
				recoBean.setRequiredSms(true);
			}
			if (recomendId != null) {
				recoBean.setRecomendId(recomendId);
				returnPage = "editRecommendation";
			}

			if (recoBean.getRecomendFileNameBean() != null && recoBean.getRecomendFileNameBean().getSize() > 0) {
				if (!Validation.validateAdvisoryUpload(recoBean.getRecomendFileNameBean().getOriginalFilename())) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload .pdf or .doc or .docx file");
					return returnPage;
				}

				long fileSize = recoBean.getRecomendFileNameBean().getSize();

				if (fileSize > 5242880) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "File size should be less than 5MB");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "recommendFileName");
					return returnPage;
				}
			}

			if (!recoBean.getRecomendSummaryBean().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(recoBean.getRecomendSummaryBean())
						|| !Validation.validateLastBlankSpace(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Recommendation summary");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Recommendation summary should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Recommendation summary");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateUserAddress(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Recommendation summary accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
			}

			// validation For SMS content

			if (!recoBean.getSmsContent().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(recoBean.getSmsContent())
						|| !Validation.validateLastBlankSpace(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in SMS");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"SMS should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in SMS");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateUserAddress(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"SMS accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
			}

			// end validation

			String msg = "";
			msg = recomendationService.addRecommmendation(recoBean);

			if (msg.equals(WrsisPortalConstant.SAVE)) {

				atts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Save Successfully");
				return "redirect:viewrecommendation";
			}
			if (msg.equals(WrsisPortalConstant.UPDATE)) {

				atts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				return "redirect:viewrecommendation";
			}
			if (msg.equals("fail")) {
				atts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
				return returnPage;
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::addRecomendation():" + e);
		}
		return returnPage;

	}

	@GetMapping("/viewrecommendation")
	public String viewrecommendation(Model model) {
		try {
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 0);
			model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewrecomendation():" + e);
		}

		return "viewrecommendation";
	}

	@RequestMapping(value = { "/viewallrecommendationPage" })
	public @ResponseBody RecommendationDataTable viewRecommendationDataPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {

		RecommendationDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			List<RecommendationBean> recommendationList = recomendationService.viewAllRecommendationPage(recomType,
					recoMendNo, startDate, endDate, Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = recommendationRepository.viewCountRecommendationPage(recomType, recoMendNo, startDate,
					endDate, -1, -1);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewRecomendationDataPagewise():" + e);
		}
		return sdt;
	}

	@ResponseBody
	@GetMapping("/recommend-file-exist")
	public String recommendFileExits(Model model, @RequestParam("fileId") Integer fileId) {
		return recomendationService.recommendFileExits(fileId);

	}

	@PostMapping("/recommend-file-download")
	public void recommendFileDownload(Model m1, ModelMap model, @RequestParam("fileId") Integer fileId,
			HttpServletResponse response) throws ParseException {
		recomendationService.downloadRecommendFile(response, fileId);
	}
	
	
	
	/*
	 * This Ajax method is design for publishing recommendation and after successful publication  Mail sms and desktop notification will send 
	 * from MoA to BoA(both in general and region specific case)
	 * Ajax method
	 * 
	 * @author Shaktimaan Panda
	 * 
	 * @version 1.1
	 * 
	 * @since 23-10-2020
	 */

	@ResponseBody
	@PostMapping("/publishRecommend")
	public String publishRecommend(Model m1, ModelMap model, @RequestParam("recommendId") Integer recommendId,
			HttpServletRequest request)  {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		try {
			String recommendationNo = recomendationService.getRecommendNoById(recommendId);

			String emailTxt = "";
			String smsTxt = "";
		

			// send mail and sms & desktop notification

			JSONObject obj=new JSONObject(recomendationService.publishRecommendtion(recommendId, userId));
			
			//if recommendation will publish then notification,mail and sms will be send .
			
			if(WrsisPortalConstant.YES.equalsIgnoreCase(obj.getString("msg"))) {                  

			List<UserBean> boaDetails = recomendationService.getBoaDetails(recommendId);
			MailSmsContentEntity mailSmsNtfc = mailSmsContentRepository.findByMscontentid(11);

			
			for (UserBean userBean : boaDetails) {

				// For sending sms
				LOG.info("RecommendationControllerWeb::publishRecommend():smsFlag-" + smsFlag);
				if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
					
					LOG.info("RecommendationControllerWeb::publishRecommend(): Now sms going to send to this no.-"
							+ userBean.getMobile());

					if (userBean.getMobile() != null && !"".equalsIgnoreCase(userBean.getMobile())) {
						try {
							smsTxt = mailSmsNtfc.getSmscontent();
							final String newSMSTxt = smsTxt.replace(WrsisPortalConstant.AT_RCMN, recommendationNo);
							Thread t = new Thread() {
								@Override
								public void run() {
									LOG.info(
											"RecommendationControllerWebController::publishRecommend(): Now SMSUtil->sendSms() call");
									smsUtil.sendSms(userBean.getMobile(), newSMSTxt);
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("RecommendationControllerWebController::publishRecommend():error on sending sms"
									+ e);
						}
					}
				}

				// For Sending Email
				if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
					if (userBean.getEmail() != null && !"".equalsIgnoreCase(userBean.getEmail())) {
						try {

							emailTxt = mailSmsNtfc.getMailcontent();
							final String newEmailTxt = emailTxt.replace("@username", userBean.getFullName())
									.replace(WrsisPortalConstant.AT_RCMN, recommendationNo);

							Thread t = new Thread() {
								@Override
								public void run() {

									EmailUtil.sendAppcMail(userBean.getEmail(), newEmailTxt, mailSmsNtfc.getMailsubject());
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::publishRecommend():error on sending mail" + e);
						}
					}
				}

				// Desktop notification

				

				if (WrsisPortalConstant.YES.equalsIgnoreCase(notificationFlag)) {

					String rawNotification = mailSmsNtfc.getNotification();
					final String desktopNotification = rawNotification.replace(WrsisPortalConstant.AT_RCMN, recommendationNo);
					DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
			        
					
					dashBoard.setToUserId((short)userBean.getUserId());   // It will return the value of this Integer as a short type
					dashBoard.setNotificationMsg(desktopNotification);
					dashBoard.setReadStatus(false);
					dashBoard.setStatus(false);
					dashBoard.setCreatedBy((short)userId);
					dashBoard.setCreatedOn(new Date());
					dashboardNotificationRepository.saveAndFlush(dashBoard);

					

				}
			}
		}

		} catch (Exception e) {
			LOG.error("RecommendationControllerWebController::publishRecommend():error on sending  notification" + e);
		}

		return recomendationService.publishRecommendtion(recommendId, userId);
	}

	// @ResponseBody
	@PostMapping("/deleteRecommend")
	public String deleteRecommend(Model m1, ModelMap model, @RequestParam("recommendId") Integer recommendId,
			HttpServletRequest request, RedirectAttributes redirect) throws ParseException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String result = recomendationService.deleteRecommendtion(recommendId, userId);
		if (result.equalsIgnoreCase(WrsisPortalConstant.SUCCESS))
			redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Recommendation deleted successfully");
		else
			redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
		return "redirect:viewrecommendation";
	}

	@PostMapping("/recommend-search-download")
	public String searchRecommendation(Model model,
			@RequestParam(value = "startDateId", required = false) String startDateId,
			@RequestParam(value = "endDateId", required = false) String endDateId,
			@RequestParam(value = "regionIdSer", required = false) Integer regionId,
			@RequestParam(value = "zoneIdSer", required = false) Integer zoneId,
			@RequestParam(value = "woredaIdSer", required = false) Integer woredaId,
			@RequestParam(value = "kebeleIdSer", required = false) Integer kebeleId,
			@RequestParam(value = "recoMendNoSer", required = false) String recoMendNo,
			@RequestParam(value = "recomType", required = false) Integer recomType) {

		model.addAttribute("sDate", startDateId);
		model.addAttribute("eDate", endDateId);
		model.addAttribute("recoMendNo", recoMendNo);
		model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
		model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
		model.addAttribute("kebeleId", kebeleId);

		if (recomType == null)
			recomType = 0;

		if (recomType == 0) {
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 0);
		}
		if (recomType == 1) {
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 1);
		}
		if (recomType == 2) {
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 2);
		}
		List<RecommendationBean> recommendationList = null;
		model.addAttribute("recoList", recommendationList);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
		return "viewrecommendation";
	}

	@PostMapping("editRecommendation")
	public String editRecommendation(Model model, @RequestParam("recomendId") Integer recomendId) {
		RecommendationBean bean = recomendationService.findByRecommendId(recomendId);

		if (bean.getRegiLi() != null && bean.getRegiLi().size() > 0) {
			for (Integer region : bean.getRegiLi()) {
				model.addAttribute("regionEditList", region);
			}
		}
		if (bean.getZonLi() != null && bean.getZonLi().size() > 0) {
			for (Integer zone : bean.getZonLi()) {
				model.addAttribute("zoneEditList", zone);
			}
		}
		if (bean.getWoreLis() != null && bean.getWoreLis().size() > 0) {
			for (Integer wore : bean.getWoreLis()) {
				model.addAttribute("woredaEditList", wore);
			}
		}
		if (bean.getKebeLis() != null && bean.getKebeLis().size() > 0) {
			for (Integer kebel : bean.getKebeLis()) {
				model.addAttribute("kebeleEditList", kebel);
			}
		}

		model.addAttribute("beanEdit", bean);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		return "editRecommendation";
	}

	@RequestMapping("/viewRecommendationByBoa")
	private String viewRecommendationByBoa(Model model, SearchVo searchVo) {
		model.addAttribute("regionlist", manageDemographicService.viewRegionList());
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewRecommendationByBoa";
	}

	@RequestMapping("/viewRecommendationByBoaPageWise")
	public @ResponseBody RecommendationDataTable viewRecommendationByBoaPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		RecommendationDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);

			List<RecommendationBean> recommendationList = recomendationService.viewRecommendationByBOA(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, Integer.valueOf(start),
					Integer.valueOf(length), userId);

			Integer totalCount = recomendationService.viewRecommendationByBOACount(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, -1, -1, userId);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewRecommendationByBoaPageWise():" + e);
		}
		return sdt;
	}

	@RequestMapping("/viewRecommendationByBoaSearch")
	private String viewRecommendationByBoaSearch(Model model, SearchVo searchVo) {
		model.addAttribute("regionlist", manageDemographicService.viewRegionList());
		model.addAttribute(WrsisPortalConstant.REGION_ID, searchVo.getRegionId());
		model.addAttribute(WrsisPortalConstant.ZONE_ID, searchVo.getZoneId());
		model.addAttribute(WrsisPortalConstant.WOREDA_ID, searchVo.getWoredaId());
		model.addAttribute("kebeleId", searchVo.getKebeleId());
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "viewRecommendationByBoa";
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRecLocDetailsForBoa")
	public String getRecLocDetailsForBoa(HttpServletRequest request,HttpSession session) throws JSONException {
		int userId= (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		int recId = Integer.parseInt(request.getParameter(WrsisPortalConstant.RECOMMENDATION_ID));
		return recomendationService.getRecLocDetailsByUserIdAndRecId(userId,recId);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRecLocDetails")
	public String getRecLocDetailsForMoa(HttpServletRequest request) throws JSONException {
		int recId = Integer.parseInt(request.getParameter(WrsisPortalConstant.RECOMMENDATION_ID));
		return recomendationService.getRecLocDetailsByRecId(recId);
	}
	
	
	@ResponseBody // Ajax method
	@RequestMapping(value = "/getRecLocDetailsForwardedByBoa")
	public String getRecLocDetailsForwardedByBoa(HttpServletRequest request) throws JSONException {
		int recId = Integer.parseInt(request.getParameter(WrsisPortalConstant.RECOMMENDATION_ID));
		return recomendationService.getForwardedRecLocDetailsByRecId(recId);
	}
	/*
	 * This Ajax method is design for showing summary of Recommendation Ajax method
	 * Ajax method
	 * 
	 * @author Shaktimaan Panda
	 * 
	 * @version 1.0
	 * 
	 * @since 23-10-2020
	 */

	@ResponseBody
	@RequestMapping(value = "/getRecSummaryDetails")
	public String getRecSummaryDetails(HttpServletRequest request) throws JSONException, JsonProcessingException {
		int recId = Integer.parseInt(request.getParameter(WrsisPortalConstant.RECOMMENDATION_ID));
		RecommendationBean bean = recomendationService.findByRecommendId(recId);
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		return mapper.writeValueAsString(bean);
	}

	@ResponseBody
	@GetMapping("/recommendation-file-exists")
	public String recommendationFileExists(Model model, @RequestParam("downId") Integer downId) {
		return recomendationService.recommendationFileExists(downId);
	}

	@PostMapping("/recommendation-file-download")
	public void recommendationFileDownload(HttpServletResponse response, @RequestParam("downId") Integer downId) {
		recomendationService.downloadRecommendationFile(response, downId);
	}

	@RequestMapping(value = { "/recommendationForwardByBoa" }, method = RequestMethod.POST)
	public String forwardRecommendation(Model model, @RequestParam("recomendId") int recomendId,HttpSession session) {

		try {
			int userId=(int)session.getAttribute(WrsisPortalConstant.USER_ID);
			model.addAttribute("locDetails", recomendationService.getRecLocationDetailsByRecId(recomendId,userId));
			model.addAttribute("recDetails", recomendationService.recDetails(recomendId));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::forwardRecommendation():" + e);
		}
		return "recommendationForwardByBoa";
	}

	
	/*
	 * This  method is design for forward recommendation and after successful forward  Mail sms and desktop notification will send 
	 * from BoA to WE
	 * 
	 * 
	 * @author Shaktimaan Panda
	 * 
	 * @version 1.1
	 * 
	 * @since 02-11-2020
	 */
	
	@RequestMapping(value = "updateRecommendationForwardByBoa")
	public String updateRecommendationForwardByBoa(Model model,
			@ModelAttribute("recDetails") RecommendationBean recDetails, HttpServletRequest request,
			RedirectAttributes redirect) {
		String sts = "";
		HttpSession session = request.getSession();
		recDetails.setUpdatedByBean((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		String returnPage = "viewRecommendationByBoa";
		try {
			int boaId=(Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			recDetails.setLoginUserId(boaId);
			if (recDetails.getForwardRemarkBean().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Remark");
				return returnPage;
			}
			sts = recomendationService.updateRecForwardByBoa(recDetails);
			if (sts.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Recommendation forwarded successfully");
				returnPage = "redirect:viewRecommendationByBoa";
				
				
				
				String recommendationNo = recomendationService.getRecommendNoById(recDetails.getRecomendId()); //recommendation No 
			
				
				
				
				
				
				
				   //Send SMS mail and Desktop Notification
				List<UserBean> woredaExpertsDetails = recomendationService.getWoredaDetailsByRecommendationId(recDetails.getRecomendId());
				String smsContent=recomendationService.getSmsContentByById(recDetails.getRecomendId());
				
				for (UserBean userBean : woredaExpertsDetails) {
				
				
				// For sending sms
					
					if(smsContent != null && !smsContent.isEmpty()) {               //smsContent.length()>0
						smsContent=smsContent.toString();
					}else {
						smsContent="A Recommendation is forwarded having Recommendation Number "+recommendationNo;
					}
					
				LOG.info("RecommendationControllerWeb::updateRecommendationForwardByBoa():smsFlag-" + smsFlag);
				if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
					
					LOG.info("RecommendationControllerWeb::updateRecommendationForwardByBoa(): Now sms going to send to this no.-"
							+ userBean.getMobile());

					if (userBean.getMobile() != null && !"".equalsIgnoreCase(userBean.getMobile())) {
						try {
						
							final String newSMSTxt = smsContent;
							Thread t = new Thread() {
								@Override
								public void run() {
									LOG.info(
											"RecommendationControllerWebController::updateRecommendationForwardByBoa(): Now SMSUtil->sendSms() call");
									smsUtil.sendSms(userBean.getMobile(), newSMSTxt);
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("RecommendationControllerWebController::updateRecommendationForwardByBoa():error on sending sms"
									+ e);
						}
					}
				}
			

				// For Sending Email
				if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
					if (userBean.getEmail() != null && !"".equalsIgnoreCase(userBean.getEmail())) {
						try {

							
							final String newEmailTxt = smsContent;

							Thread t = new Thread() {
								@Override
								public void run() {

									EmailUtil.sendAppcMail(userBean.getEmail(), newEmailTxt, "New Recommendation Forwarded");
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::publishRecommend():error on sending mail" + e);
						}
					}
				}

                              // Desktop notification

				

				if (WrsisPortalConstant.YES.equalsIgnoreCase(notificationFlag)) {

					DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
			        
					
					dashBoard.setToUserId((short)userBean.getUserId());   // It will return the value of this Integer as a short type
					dashBoard.setNotificationMsg(smsContent);
					dashBoard.setReadStatus(false);
					dashBoard.setStatus(false);
					dashBoard.setCreatedBy((short)userId);
					dashBoard.setCreatedOn(new Date());
					dashboardNotificationRepository.saveAndFlush(dashBoard);

			     	}
				

				}
				
				
			}
			if (sts.equalsIgnoreCase("fail")) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::updateRecommendationForwardByBoa():" + e);
		}
		return returnPage;
	}

	@RequestMapping("/viewRecommendationsForwardedByBoa")
	private String viewRecommendationsForwardedByBoa(Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewRecommendationsForwardedByBoa";
	}

	@RequestMapping("/viewRecommendationsForwardedByBoaPageWise")
	public @ResponseBody RecommendationDataTable viewRecommendationsForwardedByBoaPageWise(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {

		RecommendationDataTable sdt = null;
		try {
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			List<RecommendationBean> recommendationList = recomendationService.viewForwardedByRecommendationByBOA(
					recomType, recoMendNo.trim().toUpperCase(), startDate, endDate, Integer.valueOf(start),
					Integer.valueOf(length), userId);

			Integer totalCount = recomendationService.viewForwardedByRecommendationByBOACount(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, -1, -1, userId);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewRecommendationsForwardedByBoaPageWise():" + e);
		}
		return sdt;
	}

	@RequestMapping("/viewRecommendationsForwardedByBoaSearch")
	private String viewRecommendationsForwardedByBoaSearch(Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "viewRecommendationsForwardedByBoa";
	}

	@RequestMapping(value = { "/moarecommendation" })
	public String moarecommendation(Model model, SearchVo searchVo) {

		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "moarecommendation";
	}

	@RequestMapping(value = { "/moarecommendationPage" })
	public @ResponseBody RecommendationDataTable moarecommendationPage(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam("recoMendNo") String recoMendNo) {

		RecommendationDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			List<RecommendationBean> recommendationList = recomendationService.viewMOARecommendationPage(startDate,
					endDate, recoMendNo, Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = recomendationService.viewMOARecommendationPageCount(startDate, endDate, recoMendNo, -1,
					-1);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::moarecommendationPage():" + e);

		}
		return sdt;
	}

	@RequestMapping(value = { "/moarecommendationsSearch" })
	public String moarecommendationSearch(Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "moarecommendation";
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getFungicideDetails")
	public String getFungicideDetails(HttpServletRequest request) throws JSONException {
		return recomendationService.getFungicideDetails();
	}

	@RequestMapping(value = { "/woredaExpertRecommendation" })
	public String woredaExpertRecommendation(Model model, SearchVo searchVo) {
		model.addAttribute("recommendation", recomendationService.getMoaRecommendationsforWoredaExperts(searchVo));
		return "woredaExpertRecommendation";
	}

	@RequestMapping(value = { "viewRecommendationResult" })
	public String viewRecommendationResult(Model model, @ModelAttribute("searchVo") SearchVo searchVo,
			HttpServletRequest request) {
		try {
			model.addAttribute("regionlist", manageDemographicService.viewAllRegionByStatus());
			if (searchVo.getRegionId() != 0) {
				model.addAttribute("zonelist",
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute("woredalist",
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
			if (searchVo.getRegionId() == 0 && searchVo.getZoneId() == 0 && searchVo.getWoredaId() == 0
					&& "".equals(searchVo.getMonitorRefNumber()) && "".equals(searchVo.getStartDate())
					&& "".equals(searchVo.getEndDate())) {
				model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
			} else {
				model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewRecomendationResult():" + e);

		}
		return "viewRecommendationResult";
	}

	@RequestMapping("/viewRecommendationResultDataPageWise")
	public @ResponseBody ViewImplementationDataTable viewRecommendationResultDataPageWise(Model modelHttp,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("recomNo") String monitorRefNumber, @RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate,
			@RequestParam(WrsisPortalConstant.TO_DATE) String toDate, RedirectAttributes redirect) {
		ViewImplementationDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			SearchVo searchVo = new SearchVo();

			searchVo.setRegionId(regionId);
			searchVo.setZoneId(zoneId);
			searchVo.setWoredaId(woredaId);
			searchVo.setMonitorRefNumber(monitorRefNumber);
			searchVo.setStartDate(fromDate);
			searchVo.setEndDate(toDate);
			searchVo.setPageSize(Integer.parseInt(start));
			searchVo.setPageLength(Integer.parseInt(length));
			List<SurveyImplementationBean> beans = monitorService.viewAllPublishedByPage(searchVo);

			searchVo.setPageSize(-1);
			searchVo.setPageLength(-1);
			Integer totalCount = monitorService.viewCountAllPublishedByPage(searchVo);

			sdt = new ViewImplementationDataTable();
			sdt.setData(beans);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {

			LOG.error("RecomendationControllerWeb::viewRecommendationResultDataPageWise():" + e);

		}
		return sdt;
	}

	@RequestMapping(value = { "viewRecommendationMoniterImpl" })
	public String viewRecommendationMoniterImpl(Model model, HttpServletRequest request,
			@RequestParam(value = "monitorId", required = true) int monitorId) {
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
					List<Object[]> mfungicideList = monitorFungicideRepositor
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
			LOG.error("RecomendationControllerWeb::viewRecommendationMonitorImpl():" + e);
		}
		return "viewRecommendationMoniterImpl";
	}

	@RequestMapping(value = { "viewPublishedRecommendationSurveyData" })
	public String viewPublishedRecommendationSurveyData(Model model, @ModelAttribute("searchVo") SearchVo searchVo,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			searchVo.setUserId(userId);
			List<DemographicEntity> demographList = recomendationService.fetchAllUserWiseImplementionRegion(userId);
			model.addAttribute("regionlist", demographList);
			if (searchVo.getRegionId() != 0) {
				model.addAttribute("zonelist",
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute("woredalist",
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewPublishedRecommendationSurveyData():" + e);
		}
		return "viewPublishedRecommendationSurveyData";
	}

	// Add Pagination view publish Recommendation by userId

	@RequestMapping(value = { "/viewMonitorPublishedDataByUserId" })
	public @ResponseBody ViewImplementationDataTable viewMonitorPublishedDataByUserId(Model modelHttp,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("monitorrefNo") String monitorrefNo, @RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate,
			@RequestParam(WrsisPortalConstant.TO_DATE) String toDate, RedirectAttributes redirect) {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 1, userId, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.viewAllPublishedDiscardedByUserIdCount(regionId, zoneId, woredaId,
				monitorrefNo, fromDate, toDate, 1, userId, -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@RequestMapping(value = { "viewDiscardedRecommendationSurveyData" })
	public String viewDiscardedRecommendationSurveyData(Model model, @ModelAttribute("searchVo") SearchVo searchVo,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			searchVo.setUserId(userId);
			List<DemographicEntity> demographList = recomendationService.fetchAllUserWiseImplementionRegion(userId);
			model.addAttribute("regionlist", demographList);
			if (searchVo.getRegionId() != 0) {
				model.addAttribute("zonelist",
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute("woredalist",
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::viewDiscardedRecommendationSurveyData():" + e);
		}
		return "viewDiscardedRecommendationSurveyData";
	}

	// Add Pagination view publish Recommendation by userId

	@RequestMapping(value = { "/viewMonitorDiscardDataByUserId" })
	public @ResponseBody ViewImplementationDataTable viewMonitorDiscardDataByUserId(Model modelHttp,
			HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) Integer zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) Integer woredaId,
			@RequestParam("monitorrefNo") String monitorrefNo, @RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate,
			@RequestParam(WrsisPortalConstant.TO_DATE) String toDate, RedirectAttributes redirect) {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 2, userId, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = monitorService.viewAllPublishedDiscardedByUserIdCount(regionId, zoneId, woredaId,
				monitorrefNo, fromDate, toDate, 2, userId, -1, -1);

		ViewImplementationDataTable sdt = new ViewImplementationDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@GetMapping("/pendingrecommendation")
	public String pendingrecommendation(Model model) {
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 0);
		return "pendingrecommendation";
	}

	@Autowired
	RecommendationRepository recommendationRepository;

	@RequestMapping(value = { "/pendingRecommendationDataPageWise" })
	public @ResponseBody RecommendationDataTable pendingRecommendationDataPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {

		RecommendationDataTable sdt = null;
		try {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID;
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			// Use for view Pending
			Integer actionStartus = 0;
			List<RecommendationBean> recommendationList = recomendationService.viewAllRecommendationByPage(recomType,
					recoMendNo, startDate, endDate, userId, Integer.valueOf(processId), actionStartus,
					Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = recommendationRepository.viewAllCountRecommendationByPage(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, userId, Integer.valueOf(processId),
					actionStartus, -1, -1);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::pendingRecommendationDataPageWise():" + e);
		}
		return sdt;
	}

	@PostMapping("/pendingrecommendationsearch")
	public String pendingrecommendationsearch(@RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, Model model, HttpSession session) {

		try {
			model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, recomType);
			model.addAttribute("recoMendNo", recoMendNo);
			model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::pendingrecommendationsearch():" + e);
		}
		return "pendingrecommendation";
	}

	@GetMapping("/publishingrecommendation")
	public String publishingrecommendation(Model model) {
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, 0);
		return "publishingrecommendation";
	}

	@RequestMapping(value = { "/publishingRecommendationDataPageWise" })
	public @ResponseBody RecommendationDataTable publishedRecommendationDataPageWise(Model modelHttp,
			HttpSession session, HttpServletRequest request, @RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {

		RecommendationDataTable sdt = null;
		try {
			Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID;
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			Integer actionStartus = 1;
			List<RecommendationBean> recommendationList = recomendationService.viewAllRecommendationByPage(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, userId, Integer.valueOf(processId),
					actionStartus, Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = recommendationRepository.viewAllCountRecommendationByPage(recomType,
					recoMendNo.trim().toUpperCase(), startDate, endDate, userId, Integer.valueOf(processId),
					actionStartus, -1, -1);

			sdt = new RecommendationDataTable();
			sdt.setData(recommendationList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::publishedRecommendationDataPageWise():" + e);

		}
		return sdt;
	}

	@PostMapping("/publishingrecommendationsearch")
	public String publishingRecommendationSearch(@RequestParam("recomType") Integer recomType,
			@RequestParam("recoMendNo") String recoMendNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, Model model, HttpSession session) {

		try {
			model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
			model.addAttribute(WrsisPortalConstant.RECOMMEND_TYPE_ID, recomType);
			model.addAttribute("recoMendNo", recoMendNo);
			model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::publishingRecommendationSearch():" + e);
		}
		return "publishingrecommendation";
	}

	@PostMapping("editRecommendationApp")
	public String editRecommendationApp(Model model, @RequestParam("recomendId") Integer recomendId) {
		RecommendationBean bean = recomendationService.findByRecommendId(recomendId);

		if (bean.getRegiLi() != null && bean.getRegiLi().size() > 0) {
			for (Integer region : bean.getRegiLi()) {
				model.addAttribute("regionEditList", region);
			}
		}
		if (bean.getZonLi() != null && bean.getZonLi().size() > 0) {
			for (Integer zone : bean.getZonLi()) {
				model.addAttribute("zoneEditList", zone);
			}
		}
		if (bean.getWoreLis() != null && bean.getWoreLis().size() > 0) {
			for (Integer wore : bean.getWoreLis()) {
				model.addAttribute("woredaEditList", wore);
			}
		}
		if (bean.getKebeLis() != null && bean.getKebeLis().size() > 0) {
			for (Integer kebel : bean.getKebeLis()) {
				model.addAttribute("kebeleEditList", kebel);
			}
		}

		model.addAttribute("beanEdit", bean);
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}

		return "editRecommendationApp";
	}

	@PostMapping("/add-recommendationApp")
	public String addRecommendationByApp(Model model, RedirectAttributes atts,
			@RequestParam("advisoryId") Integer advisoryId, @RequestParam("recommendType") Integer recommendType,
			@RequestParam(value = "recommendFileName", required = false) MultipartFile recommendFileName,
			@RequestParam(WrsisPortalConstant.RECOMMEND_REMARK) String recommendRemark,
			@RequestParam(value = "WoredaNameId_1", required = false) String[] WoredaNameId,
			@RequestParam(value = "kebeleNameId_1", required = false) String[] kebeleNameId,
			@RequestParam(value = "recomendId", required = false) Integer recomendId,
			@RequestParam("sendType") Boolean requiredSMS, @RequestParam(WrsisPortalConstant.SMS_REMARK) String smsContent,
			HttpServletRequest request) {

		String returnPage = "editRecommendationApp";
		try {

			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

			RecommendationBean recoBean = new RecommendationBean();
			recoBean.setAdvisoryIdBean(advisoryId);
			recoBean.setRecomendFileNameBean(recommendFileName);
			recoBean.setRecomendSummaryBean(recommendRemark);
			recoBean.setWoredaNameIdArray(WoredaNameId);
			recoBean.setKebeleNameIdArray(kebeleNameId);
			recoBean.setRecomendTypeBean(recommendType);
			recoBean.setStatusBean(false);
			recoBean.setCreatedByBean(userId);
			recoBean.setRequiredSms(requiredSMS);
			recoBean.setSmsContent(smsContent);

			if (recomendId != null) {
				recoBean.setRecomendId(recomendId);
				returnPage = "editRecommendationApp";
			}

			if (recoBean.getRecomendFileNameBean() != null && recoBean.getRecomendFileNameBean().getSize() > 0) {
				if (!Validation.validateAdvisoryUpload(recoBean.getRecomendFileNameBean().getOriginalFilename())) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload .pdf or .doc or .docx file");
					return returnPage;
				}

				long fileSize = recoBean.getRecomendFileNameBean().getSize();

				if (fileSize > 5242880) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "File size should be less than 5MB");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "recommendFileName");
					return returnPage;
				}
			}

			if (!recoBean.getRecomendSummaryBean().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(recoBean.getRecomendSummaryBean())
						|| !Validation.validateLastBlankSpace(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Recommendation summary");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Recommendation summary should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Recommendation summary");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
				if (!Validation.validateUserAddress(recoBean.getRecomendSummaryBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Recommendation summary accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RECOMMEND_REMARK);
					return returnPage;
				}
			}

			// validation For SMS content

			if (!recoBean.getSmsContent().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(recoBean.getSmsContent())
						|| !Validation.validateLastBlankSpace(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in SMS");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"SMS should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in SMS");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
				if (!Validation.validateUserAddress(recoBean.getSmsContent())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"SMS accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SMS_REMARK);
					return returnPage;
				}
			}

			// end validation

			String msg = "";
			msg = recomendationService.addRecommmendation(recoBean);

			if (msg.equals(WrsisPortalConstant.SAVE)) {

				atts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Save Successfully");
				return "redirect:viewrecommendation";
			}
			if (msg.equals(WrsisPortalConstant.UPDATE)) {

				atts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				return "redirect:pendingrecommendation";
			}
			if (msg.equals("fail")) {
				atts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
				return returnPage;
			}
		} catch (Exception e) {
			LOG.error("RecomendationControllerWeb::addRecommendationByApp():" + e);
		}
		return returnPage;
	}

	// fetch zone list from recommendation
	@RequestMapping(value = "/getDemographicListZoneinRecom", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> getDemographicListZoneInRecom(Model model, HttpServletRequest request) {

		int regionId = Integer.parseInt(request.getParameter(WrsisPortalConstant.REGION_ID));
		int recomId = Integer.parseInt(request.getParameter("recomId"));

		return demographyRepository.fetchAllZoneInRecom(regionId, recomId);
	}

	// fetch woreda list from recommendation
	@RequestMapping(value = "/getDemographicListWoredainRecom", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> getDemographicListWoredainRecom(Model model, HttpServletRequest request,
			@RequestBody String s) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		JSONObject json = new JSONObject(new String(Base64.getDecoder().decode(s.getBytes())));
		int region = Integer.parseInt(json.getString(WrsisPortalConstant.REGION_ID));
		int zone = Integer.parseInt(json.getString(WrsisPortalConstant.ZONE_ID));
		int recomId = Integer.parseInt(json.getString("recomId"));
		// as Eutopia
		// id is 1
		List<Object[]> recWoreda = demographyRepository.fetchAllWoredaInRecom(region, zone, recomId);
		List<Object[]> woredataggedToUser = demographyRepository.fetchAllWoredaTaggedToUser(userId);
		List<Object[]> woredaList = new ArrayList<>();
		for (Object[] object : woredataggedToUser) {
			int va1=(Short)(object[0]);
			for (Object[] object2 : recWoreda) {
				int va2=(Short)(object2[0]);
				if(va1==va2)
				{	woredaList.add(object);
					break;
				}
			}
		}
//		recWoreda.retainAll(woredataggedToUser);
		return woredaList;

	}

	// fetch Kebele from recommendation
	@RequestMapping(value = "/getKebeleListInRecom", method = RequestMethod.POST)
	public @ResponseBody List<Object[]> getDemographicListKebeleinRecom(Model model, HttpServletRequest request,
			@RequestBody String s) throws JSONException {
		JSONObject json = new JSONObject(new String(Base64.getDecoder().decode(s.getBytes())));
		int regionId = Integer.parseInt(json.getString(WrsisPortalConstant.REGION_ID));
		int zoneId = Integer.parseInt(json.getString(WrsisPortalConstant.ZONE_ID));
		int woredaId = Integer.parseInt(json.getString(WrsisPortalConstant.WOREDA_ID));
		int recomId = Integer.parseInt(json.getString("recomId"));
		List<Object[]> kebeleList = new ArrayList<>();
		List<Object[]> kebelIdinRecom = recommendationLocationRepository.fetchKebeleIdinRecom(regionId, zoneId,
				woredaId, recomId);
		if (kebelIdinRecom.size() == 1) {
			kebeleList = demographyRepository.fetchAllActiveKebele(woredaId);
		} else {
			kebeleList = kebelIdinRecom;
		}
		return kebeleList;
	}

}
