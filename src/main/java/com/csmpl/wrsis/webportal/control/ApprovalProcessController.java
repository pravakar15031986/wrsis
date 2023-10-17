package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.ApprovalProcessBean;
import com.csmpl.wrsis.webportal.entity.ApprovalProcessEntity;
import com.csmpl.wrsis.webportal.service.ApprovalProcessService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ApprovalProcessController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(ApprovalProcessController.class);

	@Autowired
	@Qualifier("ApprovalProcessService")
	ApprovalProcessService ApprovalProcessService;

	@RequestMapping(value = "addApprovalMaster", method = RequestMethod.GET)
	public String addApprovalMaster(Model model) {

		model.addAttribute(WrsisPortalConstant.PROCESS_OBJECT, new ApprovalProcessBean());

		return "addApprovalMaster";
	}

	@RequestMapping(value = "addApprovalMaster", method = RequestMethod.POST)
	public String addApprovalProcess(@ModelAttribute ApprovalProcessBean processObj, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {

		if (!processObj.getTxtProcessName().isEmpty()) {
			if (!Validation.validateAlphabatesOnly(processObj.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name accept only alphabets");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "addApprovalMaster";
			}
			if (!Validation.validateFirstBlankSpace(processObj.getTxtProcessName())
					|| !Validation.validateLastBlankSpace(processObj.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"White space is not allowed at initial and last place in Process Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "addApprovalMaster";
			}
			if (!Validation.validateConsecutiveBlankSpacesInString(processObj.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name should not contain consecutive blank spaces");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "addApprovalMaster";
			}
		}
		if (!processObj.getDesc().isEmpty()) {
			if (!Validation.validateFirstBlankSpace(processObj.getDesc())
					|| !Validation.validateLastBlankSpace(processObj.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"White space is not allowed at initial and last place in description");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "addApprovalMaster";
			}
			if (!Validation.validateConsecutiveBlankSpacesInString(processObj.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "addApprovalMaster";
			}
			if (!Validation.validateSpecialCharAtFirstAndLast(processObj.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"!@#$&*() characters are not allowed at initial and last place in Description");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "addApprovalMaster";
			}
			if (!Validation.validateUserAddress(processObj.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "addApprovalMaster";
			}
		}
		try {
			model.addAttribute(WrsisPortalConstant.PROCESS_OBJECT, processObj);
			int userId = (Integer) getAttibuteFromSession(request, WrsisPortalConstant.USER_ID);

			processObj.setUserId(userId);

			String status = ApprovalProcessService.addApprovalProcessServices(processObj);

			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {

				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data added Successfully");

			} else if ("processexist".equalsIgnoreCase(status)) {
				model.addAttribute(WrsisPortalConstant.PROCESS_OBJECT, processObj);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name already exist ");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);

				return "addApprovalMaster";

			} else {

				model.addAttribute(WrsisPortalConstant.PROCESS_OBJECT, processObj);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Data not added");
				return "addApprovalMaster";
			}
		} catch (Exception e) {
			LOG.error("ApprovalProcessController::addApprovalProcess():" + e);
			
		}
		return "addApprovalMaster";
	}

	@RequestMapping(value = "viewApprovalMaster")
	public String getApprovalProcess(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute("statusList", getStatus());
			model.addAttribute("aplist", ApprovalProcessService.viewApprovalProcess(searchVo));

		} catch (Exception e) {
			LOG.error("ApprovalProcessController::getApprovalProcess():" + e);
			// 
		}
		return "viewApprovalMaster";
	}

	public List<SearchVo> getStatus() {
		SearchVo vo = null;
		String[] rNames = { WrsisPortalConstant.ACTIVE, WrsisPortalConstant.IN_ACTIVE };
		List<SearchVo> statusList = new ArrayList<>();
		try {
			for (int i = 0; i < rNames.length; i++) {
				vo = new SearchVo();
				vo.setDataId(i + 1);
				vo.setDataName(rNames[i]);
				statusList.add(vo);
			}

		} catch (Exception e) {
			LOG.error("ApprovalProcessController::getStatus():" + e);
			// 
		}

		return statusList;
	}

	@RequestMapping(value = "editApprovalMaster", method = RequestMethod.POST)
	public String editApprovalProcess(Model model,
			@RequestParam(value = "intProcessId", required = true) int intProcessId,RedirectAttributes redirect) {

		ApprovalProcessBean apobj = new ApprovalProcessBean();

		try {
			ApprovalProcessEntity apDetails = ApprovalProcessService.editApprovalProcess(intProcessId);
			if(apDetails==null) {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "This process can't modify.");
				return "redirect:viewApprovalMaster";
			}
			apobj.setTxtProcessName(apDetails.getVchProcessName());
			apobj.setDesc(apDetails.getVchDescription());
			apobj.setIntDeletedFlag(apDetails.getIntDeletedFlag());
			apobj.setIntProcessId(apDetails.getIntProcessId());
			model.addAttribute(WrsisPortalConstant.APPROVAL_PROCESS, apobj);

		} catch (Exception e) {
			LOG.error("ApprovalProcessController::editApprovalProcess():" + e);
			
		}
		return "editApprovalMaster";
	}

	@RequestMapping(value = "updateApprovalProcess", method = RequestMethod.POST)
	public String updateApprovalProcess(@ModelAttribute(WrsisPortalConstant.APPROVAL_PROCESS) ApprovalProcessBean apvprocess,
			HttpServletRequest request, BindingResult binding, Model model, RedirectAttributes redirect) {
		if (!apvprocess.getTxtProcessName().isEmpty()) {
			if (!Validation.validateAlphabatesOnly(apvprocess.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name accept only alphabets");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "editApprovalMaster";
			}
			if (!Validation.validateFirstBlankSpace(apvprocess.getTxtProcessName())
					|| !Validation.validateLastBlankSpace(apvprocess.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"White space is not allowed at initial and last place in Process Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "editApprovalMaster";
			}
			if (!Validation.validateConsecutiveBlankSpacesInString(apvprocess.getTxtProcessName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name should not contain consecutive blank spaces");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);
				return "editApprovalMaster";
			}
		}
		if (!apvprocess.getDesc().isEmpty()) {
			if (!Validation.validateFirstBlankSpace(apvprocess.getDesc())
					|| !Validation.validateLastBlankSpace(apvprocess.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"White space is not allowed at initial and last place in description");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "editApprovalMaster";
			}
			if (!Validation.validateConsecutiveBlankSpacesInString(apvprocess.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "editApprovalMaster";
			}
			if (!Validation.validateSpecialCharAtFirstAndLast(apvprocess.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"!@#$&*() characters are not allowed at initial and last place in Description");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "editApprovalMaster";
			}
			if (!Validation.validateUserAddress(apvprocess.getDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
				return "editApprovalMaster";
			}
		}
		try {
			int userId = (Integer) getAttibuteFromSession(request, WrsisPortalConstant.USER_ID);
			apvprocess.setUserId(userId);
			model.addAttribute(WrsisPortalConstant.APPROVAL_PROCESS, apvprocess);
			String status = ApprovalProcessService.updateApprovalProcess(apvprocess);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(status)) {

				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");

			} else if ("processexist".equalsIgnoreCase(status)) {
				model.addAttribute(WrsisPortalConstant.APPROVAL_PROCESS, apvprocess);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process Name already exist ");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);

				return "editApprovalMaster";

			} else if ("configureprocess".equalsIgnoreCase(status)) {
				model.addAttribute(WrsisPortalConstant.APPROVAL_PROCESS, apvprocess);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Process configure already another user");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.PROCESS_NAME);

				return "editApprovalMaster";

			} else {

				model.addAttribute(WrsisPortalConstant.APPROVAL_PROCESS, apvprocess);
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Data not added");
				return "editApprovalMaster";
			}

		} catch (Exception e) {
			LOG.error("ApprovalProcessController::updateApprovalProcess():" + e);
		}
		return "redirect:viewApprovalMaster";
	}

}
