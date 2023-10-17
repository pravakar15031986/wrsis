package com.csmpl.wrsis.webportal.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.service.FungicideMasterService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class FungicideController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(FungicideController.class);
	@Autowired
	FungicideMasterService fungicideService;

	@GetMapping("/addtypeoffungicide")
	public String addTypeOfFungicide(Model model, @ModelAttribute(WrsisPortalConstant.FUNGICIDE) FungicideMaster fungicideMaster) {
		return "addTypeOfFungicideMaster";
	}

	@PostMapping("/save-typeof-fungicide")
	public String addAndSaveFungicide(Model model, @ModelAttribute(WrsisPortalConstant.FUNGICIDE) FungicideMaster fungicideMaster,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		model.addAttribute(WrsisPortalConstant.FUNGICIDE, fungicideMaster);
		String returnPage = "addTypeOfFungicideMaster";
		if (fungicideMaster.getFungicideId() != 0) {
			returnPage = "editTypeOfFungicideMaster";
		}

		try {
			if (fungicideMaster.getFungicideName() == null || fungicideMaster.getFungicideName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Fungicide Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID,WrsisPortalConstant.NAME_TEXT_INPUT1 );
				return returnPage;
			}
			if (!fungicideMaster.getFungicideName().isEmpty()) {
				if (!Validation.validateFungicideName(fungicideMaster.getFungicideName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Fungicide Name accept alphabets,numbers and -");
					model.addAttribute(WrsisPortalConstant.FIRST_LOGIN_FAIL, WrsisPortalConstant.NAME_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(fungicideMaster.getFungicideName())
						|| !Validation.validateLastBlankSpace(fungicideMaster.getFungicideName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Fungicide Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.NAME_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(fungicideMaster.getFungicideName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Fungicide Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.NAME_TEXT_INPUT1);
					return returnPage;
				}
			}
			if (fungicideMaster.getFungicideAliasName() == null || fungicideMaster.getFungicideAliasName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Short Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID,WrsisPortalConstant.SHORT_TEXT_INPUT );
				return returnPage;
			}
			if (!fungicideMaster.getFungicideAliasName().isEmpty()) {
				if (!Validation.validateFungicideName(fungicideMaster.getFungicideAliasName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name accept alphabets,numbers and -");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(fungicideMaster.getFungicideAliasName())
						|| !Validation.validateLastBlankSpace(fungicideMaster.getFungicideAliasName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Short Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(fungicideMaster.getFungicideAliasName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_TEXT_INPUT);
					return returnPage;
				}
			}
			if (fungicideMaster.getQuantity().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Quantity");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUANTITY);
				return returnPage;
			}
			if (!fungicideMaster.getQuantity().isEmpty()) {
				if (!Validation.validateFungicideQuantity(fungicideMaster.getQuantity())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Quantity accept only decimal numbers with upto 2 precision");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUANTITY);
					return returnPage;
				}
				float quantity = Float.parseFloat(fungicideMaster.getQuantity());
				if (quantity > 99999999) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Quantity should have less than 8 integrals");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUANTITY);
					return returnPage;
				}
			}
			if (!fungicideMaster.getDescription().isEmpty()) {
				if (!Validation.validateUserAddress(fungicideMaster.getDescription()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(fungicideMaster.getDescription())
						|| !Validation.validateLastBlankSpace(fungicideMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(fungicideMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(fungicideMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
			}
			fungicideMaster.setCreatedBy(userId);
			String msg = fungicideService.saveAndUpdate(fungicideMaster);
			if (WrsisPortalConstant.EXIST.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Fungicide Name already exists");
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Type of Fungicide cannot be deactive. Already in use somewhere.");
			}
			if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.FUNGICIDE, new FungicideMaster());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved successfully");
			}
			if (WrsisPortalConstant.UPDATE.equals(msg)) {

				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
				returnPage = "redirect:viewtypeoffungicide";
			}
			if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}
		} catch (Exception e) {
			LOG.error("FungicideController::addAndSaveFungicide():" + e);
		}

		return returnPage;
	}

	@GetMapping("/viewtypeoffungicide")
	public String viewTypeOfFungicide(Model model, @ModelAttribute(WrsisPortalConstant.FUNGICIDE) FungicideMaster fungicideMaster) {

		
		return "viewTypeOfFungicideMaster";
	}

	
	@PostMapping("/edittypeoffungicide")
	public String editTypeOfFungicide(Model model, @Param("fungiId") Integer fungiId) {
		FungicideMaster fMaster = null;
		fMaster = fungicideService.getFungicideById(fungiId);
		model.addAttribute(WrsisPortalConstant.FUNGICIDE, fMaster);
		return "editTypeOfFungicideMaster";
	}

	@ResponseBody
	@GetMapping("/viewAllFungicidePage")
	public String viewAllFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return fungicideService.viewFungicideByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/search-fungicide")
	public String searchViewFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort) {
		LOG.info("name==" + name);
		LOG.info("status==" + status);
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("fungicideId"));
		return fungicideService.searchFungicide(name, status, pageSize, pageNumber, pageable);
	}
}
