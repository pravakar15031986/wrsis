package com.csmpl.wrsis.webportal.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.service.ManageAdminRoleService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.AuthorityBeans;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.ApprovalActionEntity;
import com.csmpl.wrsis.webportal.service.MultilevelApprovalService;

@Controller
public class MultilevelApprovalController {

	public static final Logger LOG = LoggerFactory.getLogger(MultilevelApprovalController.class);

	@Autowired
	MultilevelApprovalService multilevelApprovalService;

	@Autowired
	ManageAdminRoleService manageAdminRoleService;

	@Autowired
	UserService userService;

	@RequestMapping(value = { "/addApprovalAuthority" }, method = RequestMethod.GET)
	public String addMultileveApprovalAuthority(Model model) {

		try {
			model.addAttribute("processList", multilevelApprovalService.findAllProcess());
			List<AdminRole> adminRole = manageAdminRoleService.viewAllRoles();
			model.addAttribute("adminRole", adminRole);
			List<User> userName = userService.viewUserList();
			model.addAttribute("userName", userName);
			List<ApprovalActionEntity> actionList = multilevelApprovalService.viewActionList();
			model.addAttribute("actionList", actionList);
			model.addAttribute("processObj", new AuthorityBeans());

		} catch (Exception e) {
			LOG.error("MultilevelApprovalController::addMultileveApprovalAuthority():" + e);
		}

		return "addApprovalAuthority";
	}

	@RequestMapping(value = { "/addApprovalAuthorityStoring" }, method = RequestMethod.POST)
	public String storeMultileveApprovalAuthority(@ModelAttribute("processObj") AuthorityBeans approvalObj,
			@RequestBody String jsonString, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		approvalObj.setUserId(userId);
		try {

			String result = multilevelApprovalService.storeMultileveApprovalAuthority(approvalObj, jsonString);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved Successfully");
			}
			model.addAttribute("processList", multilevelApprovalService.findAllProcess());
			List<AdminRole> adminRole = manageAdminRoleService.viewAllRoles();
			model.addAttribute("adminRole", adminRole);
			List<User> userName = userService.viewUserList();
			model.addAttribute("userName", userName);
			List<ApprovalActionEntity> actionList = multilevelApprovalService.viewActionList();
			model.addAttribute("actionList", actionList);
			model.addAttribute("processObj", new AuthorityBeans());

		} catch (Exception e) {
			LOG.error("MultilevelApprovalController::storeMultileveApprovalAuthority():" + e);

		}

		return "addApprovalAuthority";
	}

	@ResponseBody
	@RequestMapping(value = "/getAuthorityData", method = RequestMethod.POST)
	public String viewApprovalAuthorityDetails(@RequestParam(value = "processId", required = false) Integer processId) {

		return multilevelApprovalService.viewApprovalAuthorityDetails(processId);

	}

	@ResponseBody
	@RequestMapping(value = "/getUsersByRoleId", method = RequestMethod.POST)
	public List<UserBean> getUsersByLevel(@RequestParam(value = "roleId", required = false) Integer roleId,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		LOG.info("User id" + userId);

		List<UserBean> list = multilevelApprovalService.getUsersByRoleId(roleId);

		return list;

	}

}
