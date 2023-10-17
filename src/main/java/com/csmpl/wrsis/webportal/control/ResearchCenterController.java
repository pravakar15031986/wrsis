package com.csmpl.wrsis.webportal.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.ResearchCenterHelperBean;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ResearchCenterController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(ResearchCenterController.class);

	@Autowired
	ManageDemographicService manageDemographicService;
	@Autowired
	TypeOfRustService typeOfRustService;
	@Autowired
	ResearchCenterService researchCenterService;

	@RequestMapping(value = "/configureResearchCenterMaster", method = RequestMethod.GET)
	public String configureResearchCenterMaster(Model model) {

		model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

		return "configureResearchCenterMaster";
	}

	@ResponseBody
	@RequestMapping(value = "/find-zone-by-region-id")
	public String findZoneByRegionId(@RequestParam("levelId") Integer levelId,
			@RequestParam("parentId") Integer parentId) throws JSONException {

		return manageDemographicService.viewAllZoneByRegionIdAndStatus(levelId, parentId);
	}

	@ResponseBody
	@RequestMapping(value = "/find-woreda-by-zone-id")
	public String findWoredaByZoneId(@RequestParam("levelId") Integer levelId,
			@RequestParam("parentId") Integer parentId) throws JSONException {

		return manageDemographicService.viewAllWoredaByZoneIdAndStatus(levelId, parentId);
	}

	@PostMapping("/insert-research-center")
	public String saveAndUpdate(Model model, HttpServletRequest request, RedirectAttributes redirect,
			@RequestParam(value = "researchCenterId", required = false) String researchCenterId,
			@RequestParam("name") String name, @RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) String regionId,
			@RequestParam(value = "zoneId", required = false) String zoneId,
			@RequestParam("f_woredaId") String[] woridaResultId, @RequestParam("labStaus") boolean labStaus,
			@RequestParam(value = "rustId", required = false) String[] rustId, @RequestParam("status") boolean status,
			@RequestParam(value = "description", required = false) String description) {

		String returnPage = "configureResearchCenterMaster";
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

			ResearchCenterHelperBean center = new ResearchCenterHelperBean();
			if (researchCenterId != null) {
				center.setResearchCenterBeanId(Integer.parseInt(researchCenterId));
				returnPage = "configureResearchCenterMasterEdit";
			}

			center.setResearchCenterBeanName(name);
			center.setWoredaBeanId(woridaResultId);
			center.setRustBeanId(rustId);
			center.setLabStatusBean(labStaus);
			center.setStatusBean(status);
			center.setDescriptionBean(description);

			center.setCreatedByBean(userId);

			model.addAttribute("researchCenter", center);
			if (!center.getResearchCenterBeanName().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(center.getResearchCenterBeanName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Research Center accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_NAME);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(center.getResearchCenterBeanName())
						|| !Validation.validateLastBlankSpace(center.getResearchCenterBeanName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Research Center");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_NAME);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(center.getResearchCenterBeanName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Research Center should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.CENTER_NAME);
					return returnPage;
				}
			}
			if (!center.getDescriptionBean().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(center.getDescriptionBean())
						|| !Validation.validateLastBlankSpace(center.getDescriptionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(center.getDescriptionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(center.getDescriptionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateUserAddress(center.getDescriptionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
			}
			String msg = null;
			boolean res = researchCenterService.checkDuplicateRCName(center.getResearchCenterBeanId(),
					center.getResearchCenterBeanName());
			if (res) {

				if (center.getResearchCenterBeanId() != 0) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Research Center already exists ");
					ResearchCenterHelperBean cent = researchCenterService
							.getByResearchCenterId(center.getResearchCenterBeanId());
					model.addAttribute("center", cent);
					model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
					model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
					returnPage = "configureResearchCenterMasterEdit";
				} else {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Research Center already exists");
				}

			} else {
				msg = researchCenterService.saveAndUpdate(center);
			}
			if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Saved Successfully");
			}
			if (WrsisPortalConstant.UPDATE.equals(msg)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				returnPage = "redirect:configureResearchCenterMasterView";
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"This Research Center cannot be deactive. Already in use somewhere.");
			}
			if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}

		} catch (Exception e) {
			LOG.error("ResearchCenterController::saveAndUpdate():" + e);

		}
		return returnPage;
	}

	@PostMapping("/configureResearchCenterMasterEdit")
	public String configureResearchCenterMasterEdit(@RequestParam("centerId") Integer centerId, Model model) {

		ResearchCenterHelperBean center = researchCenterService.getByResearchCenterId(centerId);

		model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
		model.addAttribute("center", center);

		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());

		return "configureResearchCenterMasterEdit";
	}

	@RequestMapping(value = "/configureResearchCenterMasterView", method = RequestMethod.GET)
	public String configureResearchCenterMasterView(Model model) {

		model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());

		return "configureResearchCenterMasterView";
	}

	@ResponseBody
	@GetMapping("/viewAllResearchCenterPage")
	public String viewAllFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return researchCenterService.viewResearchCenterByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/search-ResearchCenter")
	public String searchGrowthStage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = "zoneId", required = false) Integer zoneId,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("researchCenterId"));

		return researchCenterService.searchResearchCenterByPage(pageSize, pageNumber, pageable, name, status, regionId,
				zoneId);

	}
}
