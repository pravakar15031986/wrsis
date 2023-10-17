package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.Comparator;
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

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.service.ManageAdminRoleService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.CommitteeConfigurationBean;
import com.csmpl.wrsis.webportal.service.CommitteeConfigurationService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class CommitteeConfigurationController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(CommitteeConfigurationController.class);

	@Autowired
	CommitteeConfigurationService committeeConfigurationService;

	@Autowired
	ManageAdminRoleService manageAdminRoleService;

	@Autowired
	UserService userService;

	@RequestMapping(value = { "/addCommittee" }, method = RequestMethod.GET)
	public String addCommittee(Model model,
			@ModelAttribute(WrsisPortalConstant.COMMITTEE_DETAILS) CommitteeConfigurationBean committeeBean, HttpServletRequest request) {

		try {
			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);

			model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, committeeBean);

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationController::addCommittee():" + e);

			LOG.info("CommitteeConfigurationController::addCommittee():" + e);
		}

		return "addCommittee";
	}

	@RequestMapping(value = { "/add-committee" }, method = RequestMethod.POST)
	public String saveCommittee(Model model,
			@ModelAttribute(WrsisPortalConstant.COMMITTEE_DETAILS) CommitteeConfigurationBean committeeBean, HttpServletRequest request,
			RedirectAttributes redirect) {

		try {
			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
			model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			committeeBean.setUserId(userId);
			if (!committeeBean.getCommitteeName().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(committeeBean.getCommitteeName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee Name accept only alphabets");
					return "addCommittee";
				}
				if (!Validation.validateFirstBlankSpace(committeeBean.getCommitteeName())
						|| !Validation.validateLastBlankSpace(committeeBean.getCommitteeName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Committee Name");
					return "addCommittee";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(committeeBean.getCommitteeName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee Name should not contain consecutive blank spaces");
					return "addCommittee";
				}
			}
			if (!committeeBean.getCommitteeDesc().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(committeeBean.getCommitteeDesc())
						|| !Validation.validateLastBlankSpace(committeeBean.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					return "addCommittee";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(committeeBean.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					return "addCommittee";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(committeeBean.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					return "addCommittee";
				}
				if (!Validation.validateUserAddress(committeeBean.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					return "addCommittee";
				}
			}
			String sts = committeeConfigurationService.addCommittee(committeeBean);
			if (sts.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Committee added successfully.");

			} else if (sts.equalsIgnoreCase("nousers")) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Please add members.");
			} else {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Committee not added.");
			}

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationController::saveCommittee():" + e);

		}
		return "redirect:addCommittee";
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getUserByRoleAndOrgId")
	public String getUserByRoleAndOrgId(HttpServletRequest request) throws JSONException {
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		int intLevelDetailId = Integer.parseInt(request.getParameter("levelDetailId"));
		return userService.getUserByRoleAndOrgId(roleId, intLevelDetailId);
	}

	@RequestMapping(value = { "/viewCommittee" }, method = RequestMethod.GET)
	public String viewCommittee(Model model) {

		try {
			List<CommitteeConfigurationBean> commList = committeeConfigurationService.getCommitteeDetails();
			model.addAttribute("committeeList", commList);
		} catch (Exception e) {

			LOG.error("CommitteeConfigurationController::viewCommittee():" + e);

		}
		return "viewCommittee";
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getMemberDetails")
	public String getMemberDetails(HttpServletRequest request) throws JSONException {
		int committeeId = Integer.parseInt(request.getParameter("committeeId"));
		return committeeConfigurationService.getMemberDetailsByComId(committeeId);
	}

	@RequestMapping(value = { "/editCommittee" })
	public String editCommittee(Model model, @RequestParam(value = "committeeId") int committeeId,
			HttpServletRequest request, RedirectAttributes redirect) {
		CommitteeConfigurationBean cdto = new CommitteeConfigurationBean();
		cdto.setCommitteeId(committeeId);
		try {
			CommitteeConfigurationBean com = committeeConfigurationService.editCommittee(committeeId);

			HttpSession session = request.getSession();
			int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
			model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS, committeeConfigurationService.getAllMemberIds(cdto));
			model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, com);
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationController::editCommittee():" + e);
		}
		return WrsisPortalConstant.EDIT_COMMITTEE;
	}

	@RequestMapping(value = { "/updateCommittee" }, method = RequestMethod.POST)
	public String updateCommittee(@ModelAttribute(WrsisPortalConstant.COMMITTEE_DETAILS) CommitteeConfigurationBean committeeDetails,
			HttpServletRequest request, Model model, RedirectAttributes redirect) {
		CommitteeConfigurationBean cdto = new CommitteeConfigurationBean();
		

		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			committeeDetails.setUserId(userId);
			model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, committeeDetails);

			if (!committeeDetails.getCommitteeName().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(committeeDetails.getCommitteeName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee Name accept only alphabets");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
				if (!Validation.validateFirstBlankSpace(committeeDetails.getCommitteeName())
						|| !Validation.validateLastBlankSpace(committeeDetails.getCommitteeName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Committee Name");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(committeeDetails.getCommitteeName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee Name should not contain consecutive blank spaces");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
			}
			if (!committeeDetails.getCommitteeDesc().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(committeeDetails.getCommitteeDesc())
						|| !Validation.validateLastBlankSpace(committeeDetails.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(committeeDetails.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(committeeDetails.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
				if (!Validation.validateUserAddress(committeeDetails.getCommitteeDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					return WrsisPortalConstant.EDIT_COMMITTEE;
				}
			}
			String status = committeeConfigurationService.updateCommitteeDetails(committeeDetails);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Committee Updated Successfully");
			} else if ("nousers".equalsIgnoreCase(status)) {
				int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
				model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, committeeDetails);
				model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS, committeeConfigurationService.getAllMemberIds(cdto));
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Members should not be blank");
				return WrsisPortalConstant.EDIT_COMMITTEE;
			} else if ("committeeexists".equalsIgnoreCase(status)) {
				int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
				model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, committeeDetails);
				model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS, committeeConfigurationService.getAllMemberIds(cdto));
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee Name exist");
				return WrsisPortalConstant.EDIT_COMMITTEE;
			} else {
				int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
				model.addAttribute(WrsisPortalConstant.COMMITTEE_ROLE, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.COMMITTEE_DETAILS, committeeDetails);
				model.addAttribute(WrsisPortalConstant.SELECTED_MEMBERS, committeeConfigurationService.getAllMemberIds(cdto));
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Committee not updated");
				return WrsisPortalConstant.EDIT_COMMITTEE;
			}
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationController::updateCommittee():" + e);
		}
		return "redirect:viewCommittee";

	}

	private List<AdminRole> getRolsByUserRole(int roleId, HttpServletRequest request) {
		List<AdminRole> roles = manageAdminRoleService.viewAllRoles();
		roles.sort(Comparator.comparing(AdminRole::getRoleName));
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
			LOG.info("CommitteeConfigurationController::getRolsByUserRole():" + retList);

		}

		return retList;
	}
	

}
