package com.csmpl.wrsis.webportal.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.WheatMaster;
import com.csmpl.wrsis.webportal.service.WheatMasterService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class WheatController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(WheatController.class);

	@Autowired
	WheatMasterService wheatService;

	@RequestMapping(value = "/wheatMaster", method = RequestMethod.GET)
	public String wheatMaster(@ModelAttribute(WrsisPortalConstant.WHEAT) WheatMaster WheatMaster) {
		return "wheatMaster";
	}

	@PostMapping("/insert-wheat")
	public String saveAndUpdateWheat(Model model, @ModelAttribute(WrsisPortalConstant.WHEAT) WheatMaster wheatMaster,
			HttpServletRequest request, RedirectAttributes redirect) {
		model.addAttribute(WrsisPortalConstant.WHEAT, wheatMaster);
		String returnPage = "wheatMaster";
		if (wheatMaster.getWheatTypeId() != 0) {
			returnPage = "wheatMasterEdit";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			if (wheatMaster.getWheatName() == "" || wheatMaster.getWheatName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Type of Wheat");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_TEXT_INPUT);
				return returnPage;
			}
			if (!wheatMaster.getWheatName().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(wheatMaster.getWheatName())
						|| !Validation.validateLastBlankSpace(wheatMaster.getWheatName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Type of Wheat");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(wheatMaster.getWheatName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Wheat should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateAlphabatesOnly(wheatMaster.getWheatName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Wheat accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_TEXT_INPUT);
					return returnPage;
				}
			}
			if (!wheatMaster.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(wheatMaster.getDescription())
						|| !Validation.validateLastBlankSpace(wheatMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(wheatMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(wheatMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateUserAddress(wheatMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
			}
			wheatMaster.setCreatedBy(userId);
			String msg = wheatService.saveAndUpdate(wheatMaster);
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Type of Wheat cannot be deactive. Already in use somewhere.");
			}
			if (WrsisPortalConstant.EXIST.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Wheat already exists");
			}
			if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.WHEAT, new WheatMaster());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved successfully");
			}
			if (WrsisPortalConstant.UPDATE.equals(msg)) {

				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
				returnPage = "redirect:wheatMasterView";
			}
			if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}

		} catch (Exception e) {

			LOG.error("WheatController::saveAndUpdateWheat():" + e);
		}

		return returnPage;
	}

	@RequestMapping(value = "/wheatMasterView", method = RequestMethod.GET)
	public String wheatMasterView(Model model) {
		return "wheatMasterView";
	}

	@RequestMapping(value = "/edit-type-wheat", method = RequestMethod.POST)
	public String wheatMasterEdit(Model model, @RequestParam("wheatTypeId") Integer wheatTypeId) {

		try {
			WheatMaster wM = wheatService.getWheatById(wheatTypeId);
			model.addAttribute(WrsisPortalConstant.WHEAT, wM);
		} catch (Exception e) {
			LOG.error("WheatController::wheatMasterEdit():" + e);
		}
		return "wheatMasterEdit";
	}

	@ResponseBody
	@GetMapping("/viewAllWheatPage")
	public String viewDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return wheatService.viewWheatMasterByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/searchAllWheatPage")
	public String searchWheatMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("wheatTypeId"));
		return wheatService.searchWheatByPage(pageSize, pageNumber, pageable, name, status);
	}
}
