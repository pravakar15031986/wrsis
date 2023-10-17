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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DelegationBean;
import com.csmpl.wrsis.webportal.bean.MultilevelApprovalBean;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.service.JobDelegationService;
import com.csmpl.wrsis.webportal.service.MultilevelApprovalService;

@Controller
public class JobDelegationController {

	public static final Logger LOG = LoggerFactory.getLogger(JobDelegationController.class);

	@Autowired
	JobDelegationService jobDelegationService;

	@Autowired
	LevelDetailsRepository levelDetailsRepository;

	@Autowired
	UserService userService;

	@Autowired
	MultilevelApprovalService multilevelApprovalService;

	@RequestMapping(value = { "/addJobDeligation" }, method = RequestMethod.GET)
	public String viewDeligationPage(Model model, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			
			model.addAttribute(WrsisPortalConstant.PROCESS_LIST, jobDelegationService.findProcessByUserId(userId));

			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			List<User> userList = jobDelegationService.viewUserList(userId);
			model.addAttribute(WrsisPortalConstant.USER_LIST, userList);
			model.addAttribute(WrsisPortalConstant.DELEGATION_BEAN, new DelegationBean());

		} catch (Exception e) {
			LOG.error("JobDelegationController::viewDeligationPage():" + e);
		}
		return "addJobDeligation";
	}

	@RequestMapping(value = { "/saveDelegation" }, method = RequestMethod.POST)
	public String saveDelegation(@ModelAttribute(WrsisPortalConstant.DELEGATION_BEAN) DelegationBean delegationBean, Model model,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		delegationBean.setUserId(userId);
		String returnVal = "addJobDeligation";
		try {

			// Server Side Validation Start here

			if (delegationBean.getApprovalTo() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Delegation Approval To should not blank");
				return "addJobDeligation";
			}

			// Server Side Validation End here

			String result = jobDelegationService.saveDelegation(delegationBean);

			if (result != null && WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Job delegation has been added successfully");
				delegationBean = new DelegationBean();
			} else if (result != null && WrsisPortalConstant.UPDATE.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Job delegation has been updated successfully");
				delegationBean = new DelegationBean();
			} else if (result != null && "update_exist".equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Job delegation can't add again");
				returnVal = "editJobDeligation";
			} else {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again!");
				delegationBean = new DelegationBean();
			}

			model.addAttribute(WrsisPortalConstant.PROCESS_LIST, jobDelegationService.findProcessByUserId(userId));
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			model.addAttribute(WrsisPortalConstant.USER_LIST, jobDelegationService.viewUserList(userId));
			model.addAttribute(WrsisPortalConstant.DELEGATION_BEAN, delegationBean);

		} catch (Exception e) {
			LOG.error("JobDelegationController::saveDelegation():" + e);
		}

		return returnVal;
	}

	@ResponseBody
	@RequestMapping(value = "/getAuthorityStatus", method = RequestMethod.POST)
	public List<MultilevelApprovalBean> getAuthorityStatus(
			@RequestParam(value = "processId", required = false) Integer processId, HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return jobDelegationService.getAuthorityStatus(processId, userId);

	}

	@ResponseBody
	@RequestMapping(value = "/getAuthorityStatusEdit", method = RequestMethod.POST)
	public List<MultilevelApprovalBean> getAuthorityStatusEdi(
			@RequestParam(value = "processId", required = false) Integer processId, HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return jobDelegationService.getAuthorityStatusEdit(processId, userId);

	}

	// getUsersByLevel
	@ResponseBody
	@RequestMapping(value = "/getUsersByLevel", method = RequestMethod.POST)
	public List<User> getUsersByLevel(@RequestParam(value = "orgId", required = false) Integer orgId,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);



		return userService.getUsersByOrganizationId(userId, orgId);

	}

	@RequestMapping(value = { "/viewJobDeligation" })
	public String viewJobDeligation(@ModelAttribute(WrsisPortalConstant.DELEGATION_BEAN) DelegationBean delegationBean, Model model,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		delegationBean.setUserId(userId);
		try {

			List<DelegationBean> delegationList = jobDelegationService.viewJobDeligation(delegationBean);

			model.addAttribute("delegationList", delegationList);
			model.addAttribute(WrsisPortalConstant.PROCESS_LIST, multilevelApprovalService.findAllProcess());

		} catch (Exception e) {
			LOG.error("JobDelegationController::viewJobDeligation():" + e);
		}

		return "viewJobDeligation";
	}

	@RequestMapping(value = "/deleteJobDelegation", method = RequestMethod.POST)
	public String deleteJobDelegation(@ModelAttribute(WrsisPortalConstant.DELEGATION_BEAN) DelegationBean delegationBean, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		delegationBean.setUserId(userId);
		Integer delegationId = delegationBean.getDelegationId();
		try {
			String status = jobDelegationService.deleteJobDeligation(delegationId);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Record Deleted successfully");
				return "redirect:viewJobDeligation";
			}
		} catch (Exception e) {
			LOG.error("JobDelegationController::deleteJobDelegation():" + e);
		}
		return "redirect:viewJobDeligation";
	}

	@RequestMapping(value = "/editJobDelegation", method = RequestMethod.POST)
	public String editJobDelegation(@ModelAttribute(WrsisPortalConstant.DELEGATION_BEAN) DelegationBean delegationBean, Model model,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		delegationBean.setUserId(userId);
		try {

			DelegationBean editDelegationList = jobDelegationService.editJobDeligation(delegationBean);
			
			model.addAttribute(WrsisPortalConstant.DELEGATION_BEAN, editDelegationList);
			model.addAttribute(WrsisPortalConstant.PROCESS_LIST, jobDelegationService.findProcessByUserId(userId));
			model.addAttribute(WrsisPortalConstant.ORG_LIST, userService.getOrganizationList());
			List<User> userList = jobDelegationService.viewUserListByOrgId(editDelegationList.getOrganisationId());
			model.addAttribute(WrsisPortalConstant.USER_LIST, userList);

			

		} catch (Exception e) {
			LOG.error("JobDelegationController::editJobDelegation():" + e);
		}

		return "editJobDeligation";

	}

}
