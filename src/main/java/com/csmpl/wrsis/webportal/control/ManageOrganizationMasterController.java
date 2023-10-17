package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.OrganizationMasterBean;
import com.csmpl.wrsis.webportal.entity.OrganizationMasterEntity;
import com.csmpl.wrsis.webportal.repository.OrganizationMasterRepository;
import com.csmpl.wrsis.webportal.service.OrganizationMasterService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ManageOrganizationMasterController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(ManageOrganizationMasterController.class);

	@Autowired
	OrganizationMasterService organizationService;

	@Autowired
	OrganizationMasterRepository organizationMasterRepository;

	@GetMapping("/addOrganization")
	public String addOrganization(@ModelAttribute(WrsisPortalConstant.ORG_DETAILS) OrganizationMasterEntity orgDetails, Model model) {
		List<OrganizationMasterEntity> countryList = organizationMasterRepository.fetchAllCountry();
		model.addAttribute(WrsisPortalConstant.COUNTRY_LIST, countryList);
		model.addAttribute(WrsisPortalConstant.ORG_DETAILS, new OrganizationMasterBean());

		return "addOrganization";
	}

	// fetch ministry

	@ResponseBody
	@RequestMapping(value = "/getMinistryList", method = RequestMethod.POST)
	public List<OrganizationMasterEntity> getUsersByLevel(
			@RequestParam(value = "countryId", required = false) Integer countryId, HttpServletRequest request) {

		
	
		


		return organizationMasterRepository.getCountryByMinistry(countryId);

	}

	@PostMapping("/save-update-Organization")
	public String saveUpdateOrganization(@ModelAttribute(WrsisPortalConstant.ORG_DETAILS) OrganizationMasterBean orgDetails, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String returnPage = "addOrganization";
		if (orgDetails.getIntleveldetailid() != 0) {
			returnPage = "editOrganization";
		}
		List<OrganizationMasterEntity> countryList = organizationMasterRepository.fetchAllCountry();
		model.addAttribute(WrsisPortalConstant.COUNTRY_LIST, countryList);
		try {

			if (orgDetails.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select a Country!");
				return returnPage;
			}
			if (orgDetails.getMinistryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select a Ministry!");
				return returnPage;
			}
			if (orgDetails.getNvchlevelname() == null || orgDetails.getNvchlevelname().isEmpty()
					|| "".equalsIgnoreCase(orgDetails.getNvchlevelname())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Organization Name");
				return returnPage;
			}

			if (orgDetails.getNvchlevelname().length() != 0) {
				boolean isValidate = Validation.validateFirstBlankSpace(orgDetails.getNvchlevelname());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Organization Name should not start with a blank space");
					return returnPage;
				}
			}

			if (orgDetails.getNvchlevelname().length() != 0) {
				boolean isValidate = Validation.validateLastBlankSpace(orgDetails.getNvchlevelname());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Organization Name should not end with a blank space");
					return returnPage;
				}
			}
			if (orgDetails.getNvchlevelname().length() != 0) {
				boolean isValidate = Validation.validateConsecutiveBlankSpacesInString(orgDetails.getNvchlevelname());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Organization Name should not contain consecutive blank space");
					return returnPage;
				}
			}

			if (orgDetails.getNvchlevelname().length() != 0) {
				boolean isValidate = Validation.validateOrgName(orgDetails.getNvchlevelname());
				if (!isValidate) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Organization Name accept only alphabets");
					return returnPage;
				}
			}
			if (!orgDetails.getVchalias().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(orgDetails.getVchalias())
						|| !Validation.validateLastBlankSpace(orgDetails.getVchalias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "White space is not allowed at initial and last place in description");
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(orgDetails.getVchalias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(orgDetails.getVchalias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					return returnPage;
				}
				if (!Validation.validateUserAddress(orgDetails.getVchalias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					return returnPage;
				}
			}

			orgDetails.setIntcreatedby(userId);
			String msg = organizationService.saveAndUpdate(orgDetails);
			if (msg.equals(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, " Organization already exists");
			}
			if (msg != null && msg.equals(WrsisPortalConstant.SAVE)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Organization saved successfully");
				model.addAttribute(WrsisPortalConstant.ORG_DETAILS, new OrganizationMasterBean());
			}
			if (msg != null && msg.equals(WrsisPortalConstant.UPDATE)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Organization updated successfully");
				returnPage = "redirect:viewOrganization";
			}
			if (msg.equals(WrsisPortalConstant.FAILURE)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, " Try again");
			}

		} catch (Exception e) {
			LOG.error("ManageOrganizationMasterController::saveUpdateOrganization():" + e);

		}
		return returnPage;
	}

	@RequestMapping(value = "viewOrganization")
	public String viewOrganization(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute("statusList", getStatus());

			List<OrganizationMasterBean> orgaList = organizationService.viewAllOrganisation();
			model.addAttribute("orgaList", orgaList);

		} catch (Exception e) {
			LOG.error("ManageOrganizationMasterController::viewOrganization():" + e);

		}
		return "viewOrganization";
	}

	@RequestMapping(value = "/editOrganization", method = RequestMethod.POST)
	public String editJobDelegation(@ModelAttribute("organization") OrganizationMasterBean organization, Model model,
			HttpServletRequest request) {


		try {
			OrganizationMasterBean editOrgaList = organizationService.editOrganization(organization);

			model.addAttribute(WrsisPortalConstant.ORG_DETAILS, editOrgaList);
			List<OrganizationMasterEntity> countryList = organizationMasterRepository.fetchAllCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY_LIST, countryList);
		} catch (Exception e) {
			LOG.error("ManageOrganizationMasterController::editJobDelegation():" + e);

		}

		return "editOrganization";

	}

	public List<SearchVo> getStatus() {
		SearchVo vo = null;
		String rNames[] = { "Active", "Inactive" };
		List<SearchVo> statusList = new ArrayList<>();
		try {
			for (int i = 0; i < rNames.length; i++) {
				vo = new SearchVo();
				vo.setDataId(i + 1);
				vo.setDataName(rNames[i]);
				statusList.add(vo);
			}

		} catch (Exception e) {
			LOG.error("ManageOrganizationMasterController::getStatus():" + e);

		}

		return statusList;
	}

}
