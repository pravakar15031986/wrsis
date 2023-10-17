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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.GrowthStage;
import com.csmpl.wrsis.webportal.service.GrowthStageService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class GrowthStageController extends WrsisPortalAbstractController {
	

	private static final Logger LOG = LoggerFactory.getLogger(GrowthStageController.class);
	@Autowired
	GrowthStageService growthStageService;

	@GetMapping(value = { "/addgrowthstage" })
	public String addgrowthstage(Model model, @ModelAttribute(WrsisPortalConstant.STAGE) GrowthStage growthStage) {
		return "addgrowthstageMaster";
	}
	
         //Method is used for Growth Stage Add And Update
	@PostMapping(value = { "/save-groth-stage" })
	public String saveAndUpdate(Model model, @ModelAttribute(WrsisPortalConstant.STAGE) GrowthStage growthStage,
			HttpServletRequest request, RedirectAttributes redirect) {

		model.addAttribute(WrsisPortalConstant.STAGE, growthStage);
		String returnPage = "addgrowthstageMaster";
		if (growthStage.getStageId() != 0) {
			returnPage = "editGrowthStage";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			if (growthStage.getStageName() == null || growthStage.getStageName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Growth Stage.");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
				return returnPage;
			}
			if (!growthStage.getStageName().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(growthStage.getStageName())
						|| !Validation.validateLastBlankSpace(growthStage.getStageName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Growth Stage");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(growthStage.getStageName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Growth Stage should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateAlphabatesOnly(growthStage.getStageName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Growth Stage accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
			}

			if (growthStage.getDescription()!=null && !growthStage.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(growthStage.getDescription())
						|| !Validation.validateLastBlankSpace(growthStage.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(growthStage.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(growthStage.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateUserAddress(growthStage.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionId");
					return returnPage;
				}
			}
			growthStage.setCreatedBy(userId);
			String msg = growthStageService.saveAndUpdateGrothStage(growthStage);
			if (WrsisPortalConstant.EXIST.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Growth Stage already exists");
			}
			else if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Crop Growth Stage cannot be deactive. Already in use somewhere.");
			}
			else if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.STAGE, new GrowthStage());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved successfully");
			}
			else if (WrsisPortalConstant.UPDATE.equals(msg)) {
				
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
				returnPage = "redirect:viewgrowthstage";
			}
			else if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}
		} catch (Exception e) {
			LOG.error("GrowthStageController::saveAndUpdate():" + e);
		}
		return returnPage;
	}

	@GetMapping("/viewgrowthstage")
	public String viewGrowthStage(Model model) {
		return "viewGrowthStage";
	}

	@PostMapping("/editGrowthStage")
	public String editGrowthStage(@RequestParam(value = "id") Integer stageId, Model model) {
		try {
			GrowthStage groth = growthStageService.getGrothStageById(stageId);
			model.addAttribute(WrsisPortalConstant.STAGE, groth);
		} catch (Exception e) {
			LOG.error("GrowthStageController::editGrowthStage():" + e);
		}
		return "editGrowthStage";
	}

	@ResponseBody
	@GetMapping("/viewAllGrowthStagePage")
	public String viewAllFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return growthStageService.viewAllGrothByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/search-GrowthStage")
	public String searchGrowthStage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort) {
		
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("stageName"));
		return growthStageService.searchGrothStage(name, status, pageSize, pageNumber, pageable);
	}

}
