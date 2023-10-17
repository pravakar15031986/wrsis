package com.csmpl.wrsis.webportal.control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.repository.IPTrackingRepository;
import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.service.ManageAdminRoleService;
import com.csmpl.adminconsole.webportal.service.ManageDesignationMasterService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.SucessWebLoginDetailsDataTable;
import com.csmpl.wrsis.webportal.service.LoginAuditTrailService;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;

@Controller
public class LoginAuditTrailController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(LoginAuditTrailController.class);

	@Autowired
	IPTrackingRepository ipTrackingRepository;

	@Autowired
	ManageAdminRoleService manageAdminRoleService;

	@Autowired
	UserService userService;

	@Autowired
	ManageDesignationMasterService manageDesignationMasterService;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	ResearchCenterService researchCenterService;

	@Autowired
	LoginAuditTrailService loginAuditTrailService;

	@RequestMapping(value = { "/viewSucessWebLoginDetails" })
	public String viewSucessWebLoginDetails(Model model, @ModelAttribute("trackbean") IPTrackBean trackbean,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);

		if (trackbean.getOrganizationId() == null && trackbean.getRoleId() == null && trackbean.getIntdesigid() == null
				&& trackbean.getStartDate() == null && trackbean.getEndDate() == null
				&& trackbean.getUserName() == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now);
			trackbean.setStartDate(tdate);
			trackbean.setEndDate(tdate);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} else {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}
		try {
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());

		} catch (Exception e) {
			LOG.error("LoginAuditTrailController::viewSucessWebLoginDetails():" + e);
		}
		return "viewSucessWebLoginDetails";
	}

	@RequestMapping(value = { "/viewSucessWebLoginDetailsData" })
	public @ResponseBody SucessWebLoginDetailsDataTable viewLoginDetails(Model model, HttpServletRequest request,
			@RequestParam("organizationId") Integer organizationId, @RequestParam("roleId") String roleId,
			@RequestParam("researchCenterId") String researchCenterId, @RequestParam("intdesigid") String intdesigid,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("fullName") String fullName, @RequestParam("logStatus") String logStatus,
			RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessWebLoginDetails(organizationId,
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, Integer.valueOf(start),
				Integer.valueOf(length));

		Integer totalCount = loginAuditTrailService.viewSucessWebLoginDetailsCount(organizationId,
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, -1, -1);

		SucessWebLoginDetailsDataTable sldt = new SucessWebLoginDetailsDataTable();
		sldt.setData(beans);
		sldt.setRecordsFiltered(totalCount);
		sldt.setRecordsTotal(totalCount);
		sldt.setDraw(Integer.parseInt(draw));

		return sldt;
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

			

		}

		return retList;
	}

	@RequestMapping(value = { "/viewSucessMobLoginDetails" })
	public String viewSucessMobLoginDetails(Model model, @ModelAttribute("trackbean") IPTrackBean trackbean,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);

		if (trackbean.getOrganizationId() == null && trackbean.getRoleId() == null && trackbean.getIntdesigid() == null
				&& trackbean.getStartDate() == null && trackbean.getEndDate() == null
				&& trackbean.getUserName() == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now);
			trackbean.setStartDate(tdate);
			trackbean.setEndDate(tdate);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} else {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}
		try {
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());

		} catch (Exception e) {
			LOG.error("LoginAuditTrailController::viewSucessMobLoginDetails():" + e);
		}
		return "viewSucessMobLoginDetails";
	}

	@RequestMapping(value = { "/viewSucessMobLoginDetailsData" })
	public @ResponseBody SucessWebLoginDetailsDataTable viewSucessMobLoginDetailsData(Model model,
			HttpServletRequest request, @RequestParam("organizationId") Integer organizationId,
			@RequestParam("roleId") String roleId, @RequestParam("researchCenterId") String researchCenterId,
			@RequestParam("intdesigid") String intdesigid, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("fullName") String fullName,
			@RequestParam("logStatus") String logStatus, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessMobLoginDetails(organizationId,
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, Integer.valueOf(start),
				Integer.valueOf(length));

		Integer totalCount = loginAuditTrailService.viewSucessMobLoginDetailsCount(organizationId,
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, -1, -1);

		SucessWebLoginDetailsDataTable sldt = new SucessWebLoginDetailsDataTable();
		sldt.setData(beans);
		sldt.setRecordsFiltered(totalCount);
		sldt.setRecordsTotal(totalCount);
		sldt.setDraw(Integer.parseInt(draw));

		return sldt;
	}
}
