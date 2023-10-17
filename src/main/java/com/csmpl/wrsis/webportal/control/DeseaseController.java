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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.service.DiseaseMasterService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class DeseaseController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(DeseaseController.class);
	@Autowired
	DiseaseMasterService diseaseService;

	@GetMapping("/deseaseMaster")
	public String deseaseMaster(@ModelAttribute(WrsisPortalConstant.DESEASE) DiseaseMaster deseaseMaster, Model model) {
		return "deseaseMaster";
	}

	@PostMapping("/add-desease-master")
	public String saveDeseasMaster(@ModelAttribute(WrsisPortalConstant.DESEASE) DiseaseMaster diseaseMaster, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String returnPage = "deseaseMaster";
		if (diseaseMaster.getDiseaseId() != 0) {
			returnPage = "disease-edit";
		}
		try {
			model.addAttribute(WrsisPortalConstant.DESEASE, diseaseMaster);
			if (diseaseMaster.getDiseaseName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Disease Name.");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_INPUT);
				return returnPage;
			}
			if (!diseaseMaster.getDiseaseName().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(diseaseMaster.getDiseaseName())
						|| !Validation.validateLastBlankSpace(diseaseMaster.getDiseaseName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Disease Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(diseaseMaster.getDiseaseName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Disease Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_INPUT);
					return returnPage;
				}
				if (!(Validation.validateAlphabatesOnly(diseaseMaster.getDiseaseName()) )) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Disease Name accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_INPUT);
					return returnPage;
				}
			}
			if (!diseaseMaster.getDiseaseAliasName().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(diseaseMaster.getDiseaseAliasName())
						|| !Validation.validateLastBlankSpace(diseaseMaster.getDiseaseAliasName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Alise Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID,WrsisPortalConstant.DISEASE_TEXT_ALIAS );
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(diseaseMaster.getDiseaseAliasName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Alise Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_ALIAS);
					return returnPage;
				}
				if (!Validation.validateAlphabatesOnly(diseaseMaster.getDiseaseAliasName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Alise Name accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DISEASE_TEXT_ALIAS);
					return returnPage;
				}
			}
			if (!diseaseMaster.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(diseaseMaster.getDescription())
						|| !Validation.validateLastBlankSpace(diseaseMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID,WrsisPortalConstant.DEMO_TESTAREA_INPUT );
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(diseaseMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(diseaseMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return returnPage;
				}
				if (!Validation.validateUserAddress(diseaseMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TESTAREA_INPUT);
					return returnPage;
				}
			}
			if (diseaseMaster.getDiseaseId() == 0) {
				diseaseMaster.setCreatedBy(userId);
				diseaseMaster.setCreatedOn(new Date());
				String dis = diseaseService.saveAndUpdate(diseaseMaster);
				if (dis.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Disease Name already exists");
				}
				if (dis.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
					model.addAttribute(WrsisPortalConstant.DESEASE, new DiseaseMaster());
					model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Disease added successfully");
				}
				if (dis.equalsIgnoreCase(WrsisPortalConstant.FAILURE)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
				}
			} else {
				if (!(diseaseMaster.getSeverityRequired() )) {
					diseaseMaster.setSeverityMin(Short.valueOf("0"));
					diseaseMaster.setSeverityMax(Short.valueOf("0"));
				}
				diseaseMaster.setUpdatedBy(userId);
				DiseaseMaster dmaster = diseaseService.getDiseaseMasterById(diseaseMaster.getDiseaseId());
				if (dmaster != null) {
					diseaseMaster.setCreatedBy(dmaster.getCreatedBy());
					diseaseMaster.setCreatedOn(dmaster.getCreatedOn());
					diseaseMaster.setUpdateOn(new Date());
					String dis = diseaseService.saveAndUpdate(diseaseMaster);
					if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(dis)) {
						model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Disease cannot be deactive. Already in use somewhere.");
					}
					if (dis.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
						model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Disease Name already exists");
					}
					if (dis.equalsIgnoreCase(WrsisPortalConstant.UPDATE)) {
						
						redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Diseases updated successfully");
						returnPage = "redirect:deseaseMasterView";
					}
					if (dis.equalsIgnoreCase(WrsisPortalConstant.FAILURE)) {
						model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
					}
				}

			}

		} catch (Exception e) {
			LOG.error("DeseaseController::saveDeseasMaster():" + e.getMessage());
		}
		return returnPage;
	}

	@GetMapping(value = { "/deseaseMasterView" })
	public String deseaseMasterView() {

		return "deseaseMasterView";
	}

	@ResponseBody
	@GetMapping("/viewAllDiseaseMasterPage")
	public String viewDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return diseaseService.viewDiseaseMasterByPage(pageSize, pageNumber, pageable);
	}

	@PostMapping(value = { "/disease-edit" })
	public String deseaseMasterEdit(Model model, @Param("diseaseId") Integer diseaseId) {
		try {
			DiseaseMaster diseaseMaster = diseaseService.getDiseaseMasterById(diseaseId);
			model.addAttribute(WrsisPortalConstant.DESEASE, diseaseMaster);
		} catch (Exception e) {
			LOG.error("DeseaseController::deseaseMasterEdit():" + e.getMessage());
		}
		return "deseaseMasterEdit";
	}

	@ResponseBody
	@GetMapping("/searchAllDiseaseMasterPage")
	public String searchDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "fName", required = false) String fname,
			@RequestParam(value = "sName", required = false) String sName,
			@RequestParam(value = "status", required = false) String status) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("diseaseId"));
		return diseaseService.searchDiseaseMasterByPage(fname, sName, status, pageSize, pageNumber, pageable);
	}
}
