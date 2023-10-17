package com.csmpl.wrsis.webportal.control;

import java.util.Date;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class TypeOfRustController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(TypeOfRustController.class);

	@Autowired
	TypeOfRustService typeOfRustService;

	@GetMapping("/addTypeofRust")
	public String typeOfRust(Model model, @ModelAttribute(WrsisPortalConstant.TYPE_OF_RUSTT) TypeOfRust typeOfRust) {

		return "addTypeofRust";
	}

	@PostMapping("/add-type-of-rust")
	public String addTypeOfRust(@ModelAttribute(WrsisPortalConstant.TYPE_OF_RUSTT) TypeOfRust typeOfRust, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String returnPage = "addTypeofRust";
		if (typeOfRust.getRustId() != 0) {
			returnPage = "editTypeofRust";
		}
		try {
			model.addAttribute(WrsisPortalConstant.TYPE_OF_RUSTT, typeOfRust);
			if (typeOfRust.getTypeOfRust().isEmpty() || typeOfRust.getTypeOfRust() == "") {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Type of Rust");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
				return returnPage;
			}
			if (!typeOfRust.getTypeOfRust().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(typeOfRust.getTypeOfRust())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Rust accept only alphabets");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(typeOfRust.getTypeOfRust())
						|| !Validation.validateLastBlankSpace(typeOfRust.getTypeOfRust())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Type of Rust");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(typeOfRust.getTypeOfRust())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Rust should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
			}
			if (typeOfRust.getShortName().isEmpty() || typeOfRust.getShortName() == "") {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Short Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT2);
				return returnPage;
			}
			if (!typeOfRust.getShortName().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(typeOfRust.getShortName())
						|| !Validation.validateLastBlankSpace(typeOfRust.getShortName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Short Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT2);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(typeOfRust.getShortName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT2);
					return returnPage;
				}
				if (!Validation.validateAlphabatesOnly(typeOfRust.getShortName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT2);
					return returnPage;
				}
			}
			if (!typeOfRust.getRustDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(typeOfRust.getRustDescription())
						|| !Validation.validateLastBlankSpace(typeOfRust.getRustDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(typeOfRust.getRustDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(typeOfRust.getRustDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
				if (!Validation.validateUserAddress(typeOfRust.getRustDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION_ID);
					return returnPage;
				}
			}
			String tr = "";
			if (typeOfRust.getRustId() == 0) {
				typeOfRust.setCreadtedBy(userId);
				typeOfRust.setCreatedOn(new Date());
				tr = typeOfRustService.saveAndUpdateTypeOfRust(typeOfRust);
				if (tr.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Rust already exists");
				}
				if (tr.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
					model.addAttribute(WrsisPortalConstant.TYPE_OF_RUSTT, new TypeOfRust());
					model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved successfully");

				}
				if (tr.equalsIgnoreCase(WrsisPortalConstant.FAILURE)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
				}
			} else {
				typeOfRust.setUpdatedBy(userId);
				typeOfRust.setUpdatedOn(new Date());
				TypeOfRust t = typeOfRustService.getTypeOfRustById(typeOfRust.getRustId());
				typeOfRust.setCreadtedBy(t.getCreadtedBy());
				typeOfRust.setCreatedOn(t.getCreatedOn());
				typeOfRust.setRustId(t.getRustId());

				tr = typeOfRustService.saveAndUpdateTypeOfRust(typeOfRust);
				if (tr.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Type of Rust already exists");
				}
				if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(tr)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Type of Rust cannot be deactive. Already in use somewhere.");
				}
				if (tr.equalsIgnoreCase(WrsisPortalConstant.UPDATE)) {
					
					redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
					returnPage = "redirect:viewTypeofRust";
				}
				if (tr.equalsIgnoreCase(WrsisPortalConstant.FAILURE)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
				}

			}
		} catch (Exception e) {

			LOG.error("TypeOfRustController::addTypeOfRust():" + e);
		}
		return returnPage;
	}

	@GetMapping(value = { "/viewTypeofRust" })
	public String viewTypeofRust() {
		return "viewTypeofRust";
	}

	@ResponseBody
	@RequestMapping(value = "/viewTypeofRustAjax", method = RequestMethod.GET)
	public String viewEmpPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return typeOfRustService.TypeOfRustByPage(pageSize, pageNumber, pageable);

	}

	@PostMapping(value = { "/type-Of-rust-edit" })
	public String editTypeofRust(@Param(WrsisPortalConstant.RUSTID) Integer rustId, Model mode) {
		try {
			TypeOfRust typeOfRust = null;
			typeOfRust = typeOfRustService.getTypeOfRustById(rustId);
			mode.addAttribute(WrsisPortalConstant.TYPE_OF_RUSTT, typeOfRust);
		} catch (Exception e) {
			LOG.error("TypeOfRustController::editTypeofRust():" + e);
		}

		return "editTypeofRust";
	}

	@ResponseBody
	@RequestMapping(value = "/viewTypeofRustSearch", method = RequestMethod.GET)
	public String searchRustType(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "fName", required = false) String fName,
			@RequestParam(value = "sName", required = false) String sName,
			@RequestParam(value = "status", required = false) String status) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by(WrsisPortalConstant.RUSTID));

		return typeOfRustService.searchTypeOfRustByPage(pageSize, pageNumber, pageable, fName, sName, status);

	}
}
