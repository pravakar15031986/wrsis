package com.csmpl.wrsis.webportal.control;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.service.ManageAdminRoleService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.NotificationBeanDataTable;
import com.csmpl.wrsis.webportal.bean.NotificationBean;
import com.csmpl.wrsis.webportal.service.NotificationService;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.util.Validation;
import com.csmpl.wrsis.webportal.util.WrsisSchedulerService;

@Controller
public class NotificationController extends WrsisPortalConstant {

	private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	WrsisSchedulerService wrsisSchedulerServicesched;
	@Autowired
	NotificationService notificationService;
	@Autowired
	UserService userService;
	@Autowired
	ResearchCenterService researchCenterService;
	@Autowired
	ManageAdminRoleService manageAdminRoleService;

	@RequestMapping("addNotification")
	public String addNotification(Model model,
			@ModelAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS) NotificationBean notificationDetails, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
		} catch (Exception e) {
			LOG.error("NotificationController::addNotification():" + e);
		}
		return "addNotification";
	}

	private List<AdminRole> getRolsByUserRole(int roleId, HttpServletRequest request) {
		List<AdminRole> roles = manageAdminRoleService.viewAllRoles();
		List<AdminRole> retList = new ArrayList<>();
		if (roles != null && !roles.isEmpty()) {
			if (roleId == 1) {
				for (AdminRole role : roles) {
					if (role.getRoleId() != 1) {
						retList.add(role);
					}
				}
			} else if (roleId == 2) {
				for (AdminRole role : roles) {
					if (role.getRoleId() != 1 && role.getRoleId() != 2) {
						retList.add(role);
					}
				}
			} else { // Add roles with corresponding user
				for (AdminRole role : roles) {
					if (role.getRoleId() != 1 && role.getRoleId() != 2) {
						retList.add(role);
					}
				}
			}
			LOG.info("NotificationController::getRolsByUserRole():" + retList);
		}
		return retList;
	}

	@RequestMapping(value = { "/saveNotification" }, method = RequestMethod.POST)
	public String saveUpdateNotification(Model model,
			@ModelAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS) NotificationBean notificationDetails, HttpServletRequest request,
			RedirectAttributes redirect) {
		try {
			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			notificationDetails.setUserId((short) userId);
			if (!notificationDetails.getNotificationMsg().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(notificationDetails.getNotificationMsg())
						|| !Validation.validateLastBlankSpace(notificationDetails.getNotificationMsg())) {
					model.addAttribute("ErrorMessage",
							"White space is not allowed at initial and last place in Message");
					return "addNotification";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(notificationDetails.getNotificationMsg())) {
					model.addAttribute("ErrorMessage", "Message should not contain consecutive blank spaces");
					return "addNotification";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(notificationDetails.getNotificationMsg())) {
					model.addAttribute("ErrorMessage",
							"!@#$&*() characters are not allowed at initial and last place in Notification");
					return "addNotification";
				}
				if (!Validation.validateUserAddress(notificationDetails.getNotificationMsg())) {
					model.addAttribute("ErrorMessage",
							"Notification accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					return "addNotification";
				}
			}
			String sts = null;

			if (!Boolean.valueOf(notificationDetails.getSendStatus()))
				sts = notificationService.addNotification(notificationDetails);
			else
				sts = notificationService.sendNotification(notificationDetails);

			if (sts.equalsIgnoreCase("success")) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Notification added successfully.");
			} else if (sts.equalsIgnoreCase("sendsuccess")) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Notification sent successfully.");
			} else if (sts.equalsIgnoreCase("nousers")) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Please add members.");
			} else {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Notification not added.");
			}

		} catch (Exception e) {
			LOG.error("NotificationController::saveUpdateNotification():" + e);
		}
		return "redirect:addNotification";
	}

	@RequestMapping(value = { "/viewNotification" })
	public String viewNotification(Model model, @RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		try {
			if (startDate != null)
				model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
			else
				model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
		} catch (Exception e) {
			LOG.error("NotificationController::viewNotification():" + e);
		}
		return "viewNotification";
	}

	@RequestMapping(value = { "/viewNotificationPageWise" })
	@ResponseBody
	public NotificationBeanDataTable viewNotificationPage(Model model, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate, @RequestParam(WrsisPortalConstant.TO_DATE) String toDate) {
		NotificationBeanDataTable ndt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);

			List<NotificationBean> notificationList = notificationService.getNotificationDetailsByPage(fromDate, toDate,
					Integer.parseInt(start), Integer.parseInt(length));

			Integer recordsTotal = notificationService.getNotificationDetailsByPageCount(fromDate, toDate, -1, -1);

			ndt = new NotificationBeanDataTable();
			ndt.setData(notificationList);
			ndt.setRecordsTotal(recordsTotal);
			ndt.setRecordsFiltered(recordsTotal);
			ndt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("NotificationController::viewNotificationPage():" + e);
		}
		return ndt;
	}

	@RequestMapping(value = { "/archiveNotification" }, method = RequestMethod.GET)
	public String viewArchiveNotification(Model model, SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception e) {
			LOG.error("NotificationController::viewArchiveNotification():" + e);
		}
		return "archiveNotification";
	}

	@RequestMapping(value = { "/viewArchiveNotificationPageWise" })
	@ResponseBody
	public NotificationBeanDataTable viewarchiveNotificationPageWise(Model model, HttpServletRequest request,
			@RequestParam("sendFromDate") String sendFromDate, @RequestParam("sendToDate") String sendToDate,
			@RequestParam(WrsisPortalConstant.FROM_DATE) String fromDate, @RequestParam(WrsisPortalConstant.TO_DATE) String toDate) {
		NotificationBeanDataTable ndt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);

			List<NotificationBean> notificationList = notificationService.getNotificationArchiveDetailsByPage(
					sendFromDate, sendToDate, fromDate, toDate, Integer.parseInt(start), Integer.parseInt(length));

			Integer recordsTotal = notificationService.getNotificationArchiveDetailsCount(sendFromDate, sendToDate,
					fromDate, toDate, -1, -1);

			ndt = new NotificationBeanDataTable();
			ndt.setData(notificationList);
			ndt.setRecordsTotal(recordsTotal);
			ndt.setRecordsFiltered(recordsTotal);
			ndt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("NotificationController::viewarchiveNotificationPageWise():" + e);
		}
		return ndt;
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getNotificationMemberDetails")
	public String getMemberDetails(HttpServletRequest request) throws JSONException {
		int notifyId = Integer.parseInt(request.getParameter("notifyId"));
		return notificationService.getMemberDetailsByNotifyId(notifyId);
	}

	@RequestMapping(value = { "/editNotification" })
	public String editNotification(Model model, @RequestParam(value = "notifyId") BigInteger notifyId,
			HttpServletRequest request, RedirectAttributes redirect) {
		try {
			NotificationBean notification = notificationService.editNotification(notifyId);
			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS, notificationService.getAllMemberIds(notifyId));
			model.addAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS, notification);
		} catch (Exception e) {
			LOG.error("NotificationController::editNotification():" + e);

		}
		return "editNotification";
	}

	@RequestMapping(value = { "/updateNotification" }, method = RequestMethod.POST)
	public String updateNotification(@ModelAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS) NotificationBean notificationDetails,
			HttpServletRequest request, Model model, RedirectAttributes redirect) {

		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			notificationDetails.setUserId((short) userId);
			model.addAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS, notificationDetails);

			String status = null;
			if (!Boolean.valueOf(notificationDetails.getSendStatus()) )
				status = notificationService.updateNotificationDetails(notificationDetails);
			else
				status = notificationService.updateAndSendNotificationDetails(notificationDetails);
			if ("success".equalsIgnoreCase(status)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Notification Updated Successfully");
			}
			if ("sendsuccess".equalsIgnoreCase(status)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Notification Sent Successfully");
			} else if ("nousers".equalsIgnoreCase(status)) {
				int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
				model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
				model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS, notificationDetails);
				model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS,
						notificationService.getAllMemberIds(notificationDetails.getNotifyId()));
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Members should not be blank");

				return "editNotification";
			} else if ("failure".equalsIgnoreCase(status)) {
				int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
				model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
				model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.NOTIFICATION_DETAILS, notificationDetails);
				model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS,
						notificationService.getAllMemberIds(notificationDetails.getNotifyId()));
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Notification not updated");
				return "editNotification";
			}
		} catch (Exception e) {
			LOG.error("NotificationController::updateNotification():" + e);
		}
		return "redirect:viewNotification";

	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getNotificationUserByRoleAndOrgId")
	public String getUserByRoleAndOrgId(HttpServletRequest request) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		int intLevelDetailId = Integer.parseInt(request.getParameter("levelDetailId"));
		int rcId = Integer.parseInt(request.getParameter("rcId"));
		return userService.getNotificationUserByRoleAndOrgId(roleId, intLevelDetailId, rcId, userId);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getDesktopNotificationsByUserId")
	public String getDesktopNotificationsByUserId(Model model, HttpServletRequest request) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.getDesktopNotificationsByUserId(userId);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getDesktopNotificationsByUserIdLoadMore")
	public String getDesktopNotificationsByUserId(Model model, HttpServletRequest request,
			@RequestParam("offset") Integer offset) throws JSONException {
		HttpSession session = request.getSession();
		offset = (offset * 10) + 1;
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.getDesktopNotificationsByUserId(userId, offset);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/loadMoreDesktopNotificationsByUserId")
	public String loadMoreDesktopNotificationsByUserId(HttpServletRequest request,
			@RequestParam(value = "limit", required = false) Integer limit) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.loadDesktopNotificationsByUserId(userId, limit);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getTotalNtf")
	public String getTotalNtf(HttpServletRequest request) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.getTotalNtfOfUserId(userId);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getUnreadNtfsByUserId")
	public String getUnreadNtfsByUserId(HttpServletRequest request) throws JSONException {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.getUnreadDesktopNotificationsByUserId(userId);
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/updateNtfReadStatus")
	public String updateNtfReadStatus(HttpServletRequest request) throws JSONException {
		HttpSession session = request.getSession();
		int notifyId = Integer.parseInt(request.getParameter("notifyId"));
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return notificationService.updateNtfReadStatusByNotifyId(userId, notifyId);
	}

	@RequestMapping(value = { "/searchNotification" })
	public String searchNotification(Model model, SearchVo searchVo) {
		try {
			List<NotificationBean> notificationList = notificationService.searchNotificationDetails(searchVo);
			model.addAttribute("notificationList", notificationList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception e) {
			LOG.error("NotificationController::searchNotification():" + e);
		}
		return "viewNotification";
	}

	@RequestMapping(value = { "/searchArchiveNotification" })
	public String searchArchiveNotification(Model model, SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} catch (Exception e) {
			LOG.error("NotificationController::searchArchiveNotification():" + e);
		}
		return "archiveNotification";
	}
}
