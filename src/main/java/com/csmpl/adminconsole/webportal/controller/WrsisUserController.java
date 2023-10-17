package com.csmpl.adminconsole.webportal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.UserRoleDto;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.service.ManageAdminRoleService;
import com.csmpl.adminconsole.webportal.service.ManageDesignationMasterService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.adminconsole.webportal.util.WrsisUtillity;
import com.csmpl.wrsis.datatable.UserDataTable;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.datatable.UserDetailsDatatable;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.util.EmailUtil;
import com.csmpl.wrsis.webportal.util.SMSUtil;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class WrsisUserController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(WrsisUserController.class);

	@Value("${default.login.password}")
	private String defaultLoginPassword;

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
	SMSUtil smsUtil;

	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@GetMapping("/addUserUserMaster")
	public String addUserMaster(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
		try {

			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());

		} catch (Exception e) {

			LOG.error("WrsisUserController::addUserMaster():" + e);
		}
		return "addUserUserMaster";
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

			LOG.info("WrsisUserController::getRolsByUserRole():" + retList);

		}

		return retList;
	}

	@Autowired
	UserRepository userRepository;

	@PostMapping("/add-user-master")
	public String saveUserMaster(Model model, @ModelAttribute(WrsisPortalConstant.USER_BEAN) UserBean userBean,
			RedirectAttributes redirectAttrs, HttpServletRequest request) {
		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);
		String password = null;
		String emailTxt = "";
		String smsTxt = "";
		String returnPage = "addUserUserMaster";
		if (userBean.getUserId() != 0) {
			returnPage = "addUserUserMasterEdit";
		}
		try {
			List<AdminRole> roles = getRolsByUserRole(roleId, request);
			model.addAttribute(WrsisPortalConstant.ROLES, roles);
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
			model.addAttribute(WrsisPortalConstant.USER_BEAN, userBean);

			if (userBean.getSelectedRoleList() != null && !userBean.getSelectedRoleList().isEmpty()) {
				roles.removeIf(p -> {
					return userBean.getSelectedRoleList().stream().anyMatch(x -> (p.getRoleId() == x));
				});
				model.addAttribute(WrsisPortalConstant.ROLES, roles);

				List<AdminRole> roleIds = getRolsByUserRole(roleId, request);
				roleIds.removeIf(p -> {
					return roles.stream().anyMatch(x -> (p.getRoleId() == x.getRoleId()));
				});
				model.addAttribute("selectedRoles", roleIds);
			}
			// Organization Validation Start here

			if (userBean.getIntLevelDetailId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select an Organization Name");
				return returnPage;
			}

			if (userBean.getMapStatus()) {
				if (userBean.getResearchCenterId() == 0) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select a Research Center Name");
					return returnPage;
				}
			}

			if (userBean.getSelectedRoleList() == null || userBean.getSelectedRoleList().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select at least one User Role");
				return returnPage;
			}

			if (userBean.getUserId() == 0) {
				// User Name validations

				if (userBean.getUserName() == null || userBean.getUserName().isEmpty()
						|| "".equalsIgnoreCase(userBean.getUserName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the User Id");
					return returnPage;
				}

				Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
				Matcher matcher = pattern.matcher(userBean.getUserName());
				if (!matcher.matches()) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User Id accept only alphabets and numbers ");
					return returnPage;
				}

				if (userBean.getUserName().length() < 6) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User Id should be minimum 6 characters");
					return returnPage;
				}

				if (userBean.getUserName().length() > 52) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User Id should not exceed 52 characters");
					return returnPage;
				}

				// duplicate user check

				User existingCheck = userRepository.findByUserName(userBean.getUserName().toLowerCase());
				if (existingCheck != null) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "User Id already exists.Please choose another User Id");
					return returnPage;
				}

			}

			// Name Validation Start here
			if (userBean.getFullName() == null || userBean.getFullName().isEmpty()
					|| "".equalsIgnoreCase(userBean.getFullName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Name");
				return returnPage;
			}

			if (userBean.getFullName().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(userBean.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not start with a blank space");
					return returnPage;
				}
			}

			if (userBean.getFullName().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(userBean.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not end with a blank space");
					return returnPage;
				}
			}
			if (userBean.getFullName().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(userBean.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not contain consecutive blank space");
					return returnPage;
				}
			}

			if (userBean.getFullName().length() != 0) {
				boolean isValidate = Validation.validateUserAddress(userBean.getFullName());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Invalid Name");
					return returnPage;
				}
			}

			// Mobile Number validation Start here

			if (userBean.getMobile() == null || userBean.getMobile().isEmpty()
					|| "".equalsIgnoreCase(userBean.getMobile())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Mobile No.");
				return returnPage;
			}

			if (userBean.getMobile().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(userBean.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Mobile No. should not start with a blank space");
					return returnPage;
				}
			}

			if (userBean.getMobile().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(userBean.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Mobile No. should not end with a blank space");
					return returnPage;
				}
			}
			

			if (userBean.getMobile().length() != 0) {
				boolean isValidate = Validation.validateMobile(userBean.getMobile());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Mobile No. accept only numbers and it should not be less than 6 digits");
					return returnPage;
				}
			}

			// Email Id validation Start here
			if (userBean.getEmail() == null || userBean.getEmail().isEmpty()
					|| "".equalsIgnoreCase(userBean.getEmail())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Email");
				return returnPage;
			}

			if (userBean.getEmail().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(userBean.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not start with a blank space");
					return returnPage;
				}
			}

			if (userBean.getEmail().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(userBean.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not end with a blank space");
					return returnPage;
				}
			}
			if (userBean.getEmail().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(userBean.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Email should not contain consecutive blank space");
					return returnPage;
				}
			}

			if (userBean.getEmail().length() != 0) {
				boolean isValidate = Validation.validateEmail(userBean.getEmail());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Invalid Email Id");
					return returnPage;
				}
			}

			if (userBean.getUserId() == 0) {

				password = WrsisUtillity.generatePassword();
				userBean.setPassword(new MD5PasswordEncoder().encode(password));
			} else {
				int updateUserId = 0;
				try {
					updateUserId = (Integer) session.getAttribute("TO_BE_UPDATE_USERID");
				} catch (Exception e) {
				}
				if (updateUserId != userBean.getUserId()) {
					redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Due to security reason, data has not updated");
					session.removeAttribute("TO_BE_UPDATE_USERID");
					return "redirect:addUserUserViewMaster";
				} else {
					session.removeAttribute("TO_BE_UPDATE_USERID");
				}
			}

			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			userBean.setCreatedBy(userId);

			String result = userService.saveUserMaster(userBean);
			if (result != null && "successAdd".equalsIgnoreCase(result)) {
				model.addAttribute("msg", "User has been created successfully ");
				model.addAttribute("msg1", "USERNAME :" + userBean.getUserName() + " & PASSWORD :" + password + "");
				model.addAttribute("selectedRoles", null);
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());

				// For sending sms
				LOG.info("WrsisUserController::saveUserMaster():smsFlag-" + smsFlag);
				if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
					LOG.info("WrsisUserController::saveUserMaster(): Now sms going to send to this no.-"
							+ userBean.getMobile());

					if (userBean.getMobile() != null && !"".equalsIgnoreCase(userBean.getMobile())) {
						try {
							smsTxt = "Your wrsis account has been created.Login Username is " + userBean.getUserName()
									+ " and Password is " + password + "";
							final String newSMSTxt = smsTxt;
							Thread t = new Thread() {
								@Override
								public void run() {
									LOG.info("WrsisUserController::saveUserMaster(): Now SMSUtil->sendSms() call");
									smsUtil.sendSms(userBean.getMobile(), newSMSTxt);
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::saveUserMaster():error on sending sms" + e);
						}
					}
				}

				// For Sending Email
				if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
					MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(2);
					if (userBean.getEmail() != null && !"".equalsIgnoreCase(userBean.getEmail())) {
						try {

							emailTxt = mailsms.getMailcontent();
							final String newEmailTxt = emailTxt.replaceAll("@username", userBean.getUserName())
									.replaceAll("@password", password);

							Thread t = new Thread() {
								@Override
								public void run() {

									EmailUtil.sendAppcMail(userBean.getEmail(), newEmailTxt, mailsms.getMailsubject());
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::saveUserMaster():error on sending mail" + e);
						}
					}
				}

				return returnPage;
			} else if (result != null && "successUpdate".equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute("msg", "User has been updated successfully");
				return "redirect:addUserUserViewMaster";
			} else if (userBean.getUserId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
				model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
				model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());

				return returnPage;
			} else {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Data has not updated");

				return returnPage;
			}

		} catch (Exception e) {
			LOG.error("WrsisUserController::saveUserMaster():" + e);
			model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Error occured");

			model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
			model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());
		}
		return returnPage;
	}


	@RequestMapping(value = { "/addUserUserViewMaster" })
	public String viewUserDetails(Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);

		model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
		model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
		model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
		model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());

		model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());
		String msg1 = (String) session.getAttribute("msg1");
		if (msg1 != null && !msg1.equals("")) {
			model.addAttribute("msg1", msg1);
			session.removeAttribute("msg1");

		}

		return "addUserUserViewMaster";
	}

	// view user pagination

	@RequestMapping(value = { "/viewUserDataPagination" })
	public @ResponseBody UserDataTable viewUserDataPagination(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam("organizationId") String organizationId,
			@RequestParam("roleId") String roleId, @RequestParam("researchCenterId") String researchCenterId,
			@RequestParam("intdesigid") String intdesigid, @RequestParam("mobile") String mobile,
			@RequestParam("email") String email, @RequestParam("fullName") String fullName,
			@RequestParam("status") String status, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<UserBean> beans = userService.viewUserDataPagination(mobile, email, fullName, organizationId, roleId,
				researchCenterId, intdesigid, status, Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = userService.viewUserDataPaginationCount(mobile, email, fullName, organizationId, roleId,
				researchCenterId, intdesigid, status, -1, -1);

		UserDataTable sdt = new UserDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	@RequestMapping(value = { "/searchViewUsers" })
	public String searchViewUsers(@RequestParam("mobileNo") String mobileNo, @RequestParam("email") String email,
			@RequestParam("name") String name, @RequestParam("oname") String oname, @RequestParam("role") String role,
			@RequestParam("center") String center, @RequestParam("desig") String desig,
			@RequestParam("status") String status,

			Model model, HttpServletRequest request) {

		model.addAttribute("mobileNo", mobileNo);
		model.addAttribute("email", email);
		model.addAttribute("name", name);
		model.addAttribute("oname", oname);
		model.addAttribute("role", role);
		model.addAttribute("center", center);
		model.addAttribute("desig", desig);
		model.addAttribute("status", status);

		HttpSession session = request.getSession();
		int roleId = (Integer) session.getAttribute(WrsisPortalConstant.ROLE_ID);

		model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
		model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());
		model.addAttribute(WrsisPortalConstant.ROLES, getRolsByUserRole(roleId, request));
		model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());

		Integer totalCount = userService.viewUserDataPaginationCount(mobileNo, email, name, oname, role, center, desig,
				status, -1, -1);
		model.addAttribute("RecordFound", (totalCount > 0) ? true : false);
		model.addAttribute(WrsisPortalConstant.USER_BEAN, new UserBean());

		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

		return "addUserUserViewMaster";
	}

	@PostMapping("/addUserUserMasterEdit")
	public String editUserUserMaster(Model model, @RequestParam("userId") Integer userId, HttpServletRequest request) {
		UserRoleDto udto = new UserRoleDto();
		udto.setUserId(userId);
		try {
			User u = userService.getUserById(userId);
			if (u != null) {
				if (u.getMemUserTypeId() == 1) {
					UserResearchCenterMapping rcMap = userService.getUserResearchCenterMapping(userId);
					if (rcMap != null) {
						u.setResearchCenterId(rcMap.getRecearchCenterId());
					}
				}
				model.addAttribute(WrsisPortalConstant.USER_BEAN, u);
			}

			List<UserRoleDto> roleIds = userService.getAllRoleIds(udto);
			model.addAttribute("selectedRoles", roleIds);
			List<AdminRole> adminRole = manageAdminRoleService.viewAllRoles();
			if (adminRole != null) {
				LOG.info("User Role===========In User Controller========>" + adminRole);
				adminRole.removeIf(p -> {
					return roleIds.stream().anyMatch(x -> (p.getRoleId() == x.getRoleId()));
				});
				model.addAttribute(WrsisPortalConstant.ROLES, adminRole);
			}
			model.addAttribute(WrsisPortalConstant.DESIGNATION_LIST, manageDesignationMasterService.viewAllActiveDesignations());
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.RC_LIST, researchCenterService.getResearchCenterList());

			request.getSession().setAttribute("TO_BE_UPDATE_USERID", userId);

		} catch (Exception e) {
			LOG.error("WrsisUserController::editUserUserMaster():" + e);
		}
		return "addUserUserMasterEdit";
	}

	@ResponseBody
	@RequestMapping(value = "/searchUserPageAjax", method = RequestMethod.GET)
	public String searchUserPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "orgName", required = false) Integer orgName,
			// @RequestParam(value="roleVal",required = false)Integer roleVal,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "status", required = false) Boolean status,
			@RequestParam(value = "intdesigid", required = false) Integer intdesigid,
			@RequestParam(value = "sort", required = false) Sort sort) {

		// @SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("userId"));

		return userService.searchUserByAjax(pageSize, pageNumber, pageable, name, orgName, mobile, email, status,
				intdesigid);// ,roleVal

	}

	// data table user details

	@ResponseBody
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public UserDetailsDatatable getUserDetails(HttpServletRequest request) {
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);

			List<UserBean> ub = userService.viewUserDetails(Integer.valueOf(start), Integer.valueOf(length));

			for (int i = 0; i < ub.size(); i++) {
				UserBean userBean = ub.get(i);
				if (userBean.getPasswordUnlock().equals("Y")) {
					String s1 = "<a class=\"btn btn-danger\" data-toggle=\"tooltip\" title=\"Unlock\" onclick=\"unlock('"
							+ userBean.getUserId()
							+ "')\"> <i id=\"actionId\" class=\"fa fa-share\" aria-hidden=\"true\"></i></a>";
					userBean.setPasswordUnlock(s1);
				} else {
					String s1 = "";
					userBean.setPasswordUnlock(s1);
				}
				String s = "<a class=\"btn btn-primary\" data-toggle=\"tooltip\" title=\"Edit\" onclick=\"edit('"
						+ userBean.getUserId()
						+ "')\"> <i id=\"actionId\" class=\"fa fa-edit\" aria-hidden=\"true\"></i></a>";
				userBean.setEdit(s);
				userBean.setSl((i + 1));
				ub.set(i, userBean);

			}

			Integer count = userService.viewUserDetailsCount();

			UserDetailsDatatable u = new UserDetailsDatatable();
			u.setData(ub);
			u.setRecordsFiltered(count);
			u.setRecordsTotal(count);

			return u;
		} catch (Exception e) {
			LOG.error("WrsisUserController::getUserDetails():" + e);
			return null;
		}
	}

	// user search details

	@ResponseBody
	@RequestMapping(value = "/getUserSearch", method = RequestMethod.GET)
	public UserDetailsDatatable getUserSearch(HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email) {
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);

			List<UserBean> ub = userService.viewUserSearch(Integer.valueOf(start), Integer.valueOf(length), email);

			for (int i = 0; i < ub.size(); i++) {
				UserBean userBean = ub.get(i);
				String s = "<a class=\"btn btn-primary\" data-toggle=\"tooltip\" title=\"\"  data-original-title=\"Edit\" onclick=\"edit('"
						+ userBean.getUserId()
						+ "')\"> <i id=\"actionId\" class=\"fa fa-edit\" aria-hidden=\"true\"></i></a>";
				userBean.setEdit(s);
				userBean.setSl((i + 1));
				ub.set(i, userBean);

			}

			Integer count = userService.viewUserDetailsCount();

			UserDetailsDatatable u = new UserDetailsDatatable();
			u.setData(ub);
			u.setRecordsFiltered(count);
			u.setRecordsTotal(count);

			return u;
		} catch (Exception e) {
			LOG.error("WrsisUserController::getUserSearch():" + e);
			return null;
		}
	}

	@RequestMapping(value = "/unlockUserMaster", method = RequestMethod.POST)
	public String unlockUserMaster(@ModelAttribute(WrsisPortalConstant.USER_BEAN) UserBean userBean, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		Integer unlockId = userBean.getUnlockUserId();
		userBean.setUserId(userId);
		userBean.setUnlockUserId(unlockId);
		try {
			String status = userService.unlockUserMaster(userBean);
			if (status != null && "sucess".equalsIgnoreCase(status)) {
				redirectAttrs.addAttribute("msg1", "User unblocked.");
				return "redirect:addUserUserViewMaster";
			}
		} catch (Exception e) {
			LOG.error("WrsisUserController::unlockUserMaster():" + e);
		}
		session.setAttribute("msg1", "User unblocked.");
		return "redirect:addUserUserViewMaster";
	}

	/**
	 * //This method will execute when Password will be reset by Adminstrator .
	 *
	 * @author Shaktimaan Panda
	 * @version 1.0
	 * @since 24-08-2020
	 */
	@RequestMapping(value = "/userResetPassword", method = RequestMethod.POST)
	public String userResetPassword(@ModelAttribute(WrsisPortalConstant.USER_BEAN) UserBean userBean, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		userBean.setUserId(userId);
		try {
			String status = userService.resetPassword(userBean);
			if (status != null && WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {
				// mail n msg
				User user = userService.getUserById(userBean.getUnlockUserId());
				MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(10);

				// For sending sms
				LOG.info("WrsisUserController::userResetPassword():User-" + user);
				if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
					LOG.info("WrsisUserController::userResetPassword(): Now sms going to send to this no.-"
							+ userBean.getMobile());

					if (user.getMobile() != null && !"".equalsIgnoreCase(user.getMobile())) {
						try {
							String smsTxt = mailsms.getSmscontent();
							final String newSMSTxt = smsTxt.replaceAll("@username", user.getUserName())
									.replaceAll("@password", defaultLoginPassword);

							Thread t = new Thread() {
								@Override
								public void run() {
									LOG.info("WrsisUserController::userResetPassword(): Now SMSUtil->sendSms() call");
									smsUtil.sendSms(user.getMobile(), newSMSTxt);
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::userResetPassword():error on sending sms" + e);
						}
					}
				}

				// For Sending Email
				if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
					if (user.getEmail() != null && !"".equalsIgnoreCase(user.getEmail())) {
						try {

							String emailTxt = mailsms.getMailcontent();
							final String newEmailTxt = emailTxt.replaceAll("@username", user.getUserName())
									.replaceAll("@password", defaultLoginPassword);

							Thread t = new Thread() {
								@Override
								public void run() {

									EmailUtil.sendAppcMail(user.getEmail(), newEmailTxt, mailsms.getMailsubject());
								}
							};
							t.start();
						} catch (Exception e) {
							LOG.error("WrsisUserController::userResetPassword():error on sending mail" + e);
						}
					}
				}
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "User password reset successfully");
				return "redirect:addUserUserViewMaster";
			}
			redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "User password has not reset");

		} catch (Exception e) {
			LOG.error("WrsisUserController::userResetPassword():" + e);
			redirectAttrs.addFlashAttribute(WrsisPortalConstant.ERROR_MSG,
					"Exception occured !! Try again after sometime");

		}

		return "redirect:addUserUserViewMaster";

	}

}
