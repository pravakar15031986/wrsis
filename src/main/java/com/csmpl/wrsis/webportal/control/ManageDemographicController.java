package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DemographicBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ManageDemographicController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(ManageDemographicController.class);

	@Autowired
	ManageDemographicService manageDemographicService;

	@RequestMapping(value = { "/addregion" }, method = RequestMethod.GET)
	public String addRegion(@ModelAttribute("region") DemographicBean region, Model model) {
		List<DemographicEntity> country = null;
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
		} catch (Exception e) {
			LOG.error("ManageDemographicController::addRegion():" + e);
		}
		return "addRegionMaster";
	}

	@RequestMapping(value = "/add-region", method = RequestMethod.POST)
	public String saveRegion(Model model, @ModelAttribute("region") DemographicBean region, HttpServletRequest request,
			RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		try {

			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			region.setUserId(userId);

			if (region.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "addRegionMaster";
			}
			if (region.getDemographyName() == null || region.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Region Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "addRegionMaster";
			}
			if (!region.getDemographyName().isEmpty()) {
				if (!(Validation.validateDemographyName(region.getDemographyName()) )) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addRegionMaster";
				}
				if (!Validation.validateFirstBlankSpace(region.getDemographyName())
						|| !Validation.validateLastBlankSpace(region.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Region Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addRegionMaster";
				}
			}
			if (region.getAlias() == null || region.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "addRegionMaster";
			}
			if (!region.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(region.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addRegionMaster";
				}
				if (!Validation.validateFirstBlankSpace(region.getAlias())
						|| !Validation.validateLastBlankSpace(region.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addRegionMaster";
				}
			}
			if (!region.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(region.getDescription())
						|| !Validation.validateLastBlankSpace(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addRegionMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addRegionMaster";
				}
				if (!Validation.validateUserAddress(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addRegionMaster";
				}
			}

			String status = manageDemographicService.saveRegionName(region);
			if (status.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name already exists");
				return "addRegionMaster";
			}
			if (status.equalsIgnoreCase("aliasexists")) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name already exists");
				return "addRegionMaster";
			}
			if (status.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				model.addAttribute("region", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Region added successfully.");

			} else {
				model.addAttribute("region", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Region not added.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::saveRegion():" + e);
		}
		
		return "redirect:addregion";
	}



	@RequestMapping(value = { "/viewregion" })
	public String viewRegion(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("countrylist", manageDemographicService.viewCountry());
			model.addAttribute("regionlist", manageDemographicService.viewRegion(searchVo));

		} catch (Exception e) {
			LOG.error("ManageDemographicController::viewRegion():" + e);
		}
		return "viewRegionMaster";
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
			LOG.error("ManageDemographicController::getStatus():" + e);
		}

		return statusList;
	}

	@RequestMapping(value = { "/editRegion" }, method = RequestMethod.POST)
	public String editRegion(@RequestParam(value = "demographyId", required = true) int demographyId, Model model) {

		DemographicBean regionObj = new DemographicBean();
		try {

			DemographicEntity region = manageDemographicService.editRegion(demographyId);
			regionObj.setCountryId(region.getParentId());

			regionObj.setDemographyName(region.getDemographyName());
			regionObj.setDemographyId(region.getDemographyId());
			regionObj.setAlias(region.getAlias());
			regionObj.setDescription(region.getDescription());
			regionObj.setStatus(region.getStatus());

			model.addAttribute("region", regionObj);

			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());

		} catch (Exception e) {
			LOG.error("ManageDemographicController::editRegion():" + e);
		}
		return "editRegionMaster";
	}

	@RequestMapping(value = { "/updateRegion" })
	public String updateRegion(@Valid @ModelAttribute("region") DemographicBean region, HttpServletRequest request,
			BindingResult binding, Model model, RedirectAttributes redircts) {
		String result = WrsisPortalConstant.FAILURE;
		HttpSession session = request.getSession();
		
		try {

			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			region.setUserId(userId);
			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());
			LOG.info("Region binding" + binding.hasErrors());
			model.addAttribute("region", region);
			if (binding.hasErrors()) {
				return "editRegionMaster";
			}

			if (region.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "editRegionMaster";
			}
			if (region.getDemographyName() == null || region.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Region Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "editRegionMaster";
			}
			if (!region.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(region.getDemographyName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editRegionMaster";
				}
				if (!Validation.validateFirstBlankSpace(region.getDemographyName())
						|| !Validation.validateLastBlankSpace(region.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Region Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editRegionMaster";
				}
			}
			if (region.getAlias() == null || region.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "editRegionMaster";
			}
			if (!region.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(region.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editRegionMaster";
				}
				if (!Validation.validateFirstBlankSpace(region.getAlias())
						|| !Validation.validateLastBlankSpace(region.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editRegionMaster";
				}
			}
			if (!region.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(region.getDescription())
						|| !Validation.validateLastBlankSpace(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editRegionMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editRegionMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editRegionMaster";
				}
				if (!Validation.validateUserAddress(region.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editRegionMaster";
				}
			}
			result = manageDemographicService.updateRegion(region);
			if (result.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Region Name already exists");
				return "editRegionMaster";
			}
			else if (result.equalsIgnoreCase("aliasexists")) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name already exists");
				return "editRegionMaster";
			}
			else if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Region updated successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Region not updated.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::updateRegion():" + e);
		}
		return "redirect:viewregion";
	}

	@RequestMapping(value = { "/addzone" }, method = RequestMethod.GET)
	public String addZone(@ModelAttribute("zone") DemographicBean zone, Model model) {
		List<DemographicEntity> count = null;
		
		try {
			count = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, count);
			
			
		} catch (Exception e) {
			LOG.error("ManageDemographicController::addZone():" + e);
		}
		return "addZoneMaster";
	}

	@RequestMapping(value = "/add-zone", method = RequestMethod.POST)
	public String saveZone(Model model, @ModelAttribute("zone") DemographicBean zone, HttpServletRequest request,
			RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		List<DemographicEntity> regionList = null;
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			regionList = manageDemographicService.regionList();
			model.addAttribute("region", regionList);
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			zone.setUserId(userId);

			if (zone.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "addZoneMaster";
			}
			if (zone.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "addZoneMaster";
			}
			if (zone.getDemographyName() == null || zone.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Zone Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "addZoneMaster";
			}
			if (!zone.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(zone.getDemographyName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addZoneMaster";
				}
				if (!Validation.validateFirstBlankSpace(zone.getDemographyName())
						|| !Validation.validateLastBlankSpace(zone.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Zone Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addZoneMaster";
				}
			}
			if (zone.getAlias() == null || zone.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "addZoneMaster";
			}
			if (!zone.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(zone.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addZoneMaster";
				}
				if (!Validation.validateFirstBlankSpace(zone.getAlias())
						|| !Validation.validateLastBlankSpace(zone.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addZoneMaster";
				}
			}
			if (!zone.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(zone.getDescription())
						|| !Validation.validateLastBlankSpace(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addZoneMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addZoneMaster";
				}
				if (!Validation.validateUserAddress(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addZoneMaster";
				}
			}
			String status = manageDemographicService.saveZoneName(zone);
			if (status.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name already exists");
				return "addZoneMaster";
			}
			if (status.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				model.addAttribute("zone", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Zone added Successfully.");

			} else {
				model.addAttribute("zone", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Not added.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::saveZone():" + e);
		}

		return "redirect:addzone";
	}

	@RequestMapping(value = { "/viewzone" })
	public String viewZone(Model model, @ModelAttribute SearchVo searchVo) {

		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute(WrsisPortalConstant.ZONE_LIST, manageDemographicService.viewZone(searchVo));
		} catch (Exception e) {
			LOG.error("ManageDemographicController::viewZone():" + e);
		}

		return "viewZoneMaster";
	}

	@RequestMapping(value = { "/editZone" }, method = RequestMethod.POST)
	public String editZone(@RequestParam(value = "demographyId", required = true) int demographyId, Model model) {
		DemographicBean zoneObj = new DemographicBean();
		try {

			DemographicEntity zone = manageDemographicService.editZone(demographyId);
			zoneObj.setRegionId(zone.getParentId());

			DemographicEntity region = manageDemographicService.editZone(zone.getParentId());
			zoneObj.setCountryId(region.getParentId());

			zoneObj.setDemographyName(zone.getDemographyName());
			zoneObj.setDemographyId(zone.getDemographyId());
			zoneObj.setAlias(zone.getAlias());
			zoneObj.setDescription(zone.getDescription());
			zoneObj.setStatus(zone.getStatus());

			model.addAttribute("zone", zoneObj);

			model.addAttribute("region", manageDemographicService.getDemographyListByParentId(region.getParentId()));

			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());

		} catch (Exception e) {
			LOG.error("ManageDemographicController::editZone():" + e);
		}
		return "editZoneMaster";
	}

	@RequestMapping(value = { "/updateZone" })
	public String updateZone(@Valid @ModelAttribute("zone") DemographicBean zone, HttpServletRequest request,
			BindingResult binding, Model model, RedirectAttributes redirects) {
		String result = WrsisPortalConstant.FAILURE;
		HttpSession session = request.getSession();
		
		try {
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			zone.setUserId(userId);
			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());
			model.addAttribute("region", manageDemographicService.regionList());
			LOG.info("Zone binding" + binding.hasErrors());
			model.addAttribute("zone", zone);

			if (binding.hasErrors()) {
				return "editZoneMaster";
			}

			if (zone.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "editZoneMaster";
			}
			if (zone.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "editZoneMaster";
			}
			if (zone.getDemographyName() == null || zone.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Zone Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "editZoneMaster";
			}
			if (!zone.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(zone.getDemographyName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editZoneMaster";
				}
				if (!Validation.validateFirstBlankSpace(zone.getDemographyName())
						|| !Validation.validateLastBlankSpace(zone.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Zone Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editZoneMaster";
				}
			}
			if (zone.getAlias() == null || zone.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "editZoneMaster";
			}
			if (!zone.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(zone.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editZoneMaster";
				}
				if (!Validation.validateFirstBlankSpace(zone.getAlias())
						|| !Validation.validateLastBlankSpace(zone.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editZoneMaster";
				}
			}
			if (!zone.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(zone.getDescription())
						|| !Validation.validateLastBlankSpace(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editZoneMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editZoneMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editZoneMaster";
				}
				if (!Validation.validateUserAddress(zone.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editZoneMaster";
				}
			}
			result = manageDemographicService.updateZone(zone);
			if (result.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Name already exists");
				return "editZoneMaster";
			}
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redirects.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Zone Updated Successfully.");

			} else {
				redirects.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Zone Not Updated.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::updateZone():" + e);
		}
		return "redirect:viewzone";
	}

	@RequestMapping(value = { "/addworeda" }, method = RequestMethod.GET)
	public String addWoreda(@ModelAttribute(WrsisPortalConstant.WOREDA1) DemographicBean woreda, Model model) {
		List<DemographicEntity> country = null;
		
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			
		} catch (Exception e) {
			LOG.error("ManageDemographicController::addWoreda():" + e);
		}
		return "addWoredaMaster";
	}

	@RequestMapping(value = "/add-woreda", method = RequestMethod.POST)
	public String saveWoreda(Model model, @ModelAttribute(WrsisPortalConstant.WOREDA1) DemographicBean woreda, HttpServletRequest request,
			RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		List<DemographicEntity> regionList = null;
		List<DemographicEntity> zoneList = null;
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			regionList = manageDemographicService.regionList();
			model.addAttribute("region", regionList);
			zoneList = manageDemographicService.zoneList();
			model.addAttribute("zone", zoneList);
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			woreda.setUserId(userId);

			if (woreda.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "addWoredaMaster";
			}
			if (woreda.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "addWoredaMaster";
			}
			if (woreda.getZoneId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_ZONE);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "zone");
				return "addWoredaMaster";
			}
			if (woreda.getDemographyName() == null || woreda.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Woreda Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "addWoredaMaster";
			}
			if (!woreda.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(woreda.getDemographyName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addWoredaMaster";
				}
				if (!Validation.validateFirstBlankSpace(woreda.getDemographyName())
						|| !Validation.validateLastBlankSpace(woreda.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Woreda Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addWoredaMaster";
				}
			}
			if (woreda.getAlias() == null || woreda.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "addWoredaMaster";
			}
			if (!woreda.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(woreda.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addWoredaMaster";
				}
				if (!Validation.validateFirstBlankSpace(woreda.getAlias())
						|| !Validation.validateLastBlankSpace(woreda.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addWoredaMaster";
				}
			}
			if (!woreda.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(woreda.getDescription())
						|| !Validation.validateLastBlankSpace(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addWoredaMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addWoredaMaster";
				}
				if (!Validation.validateUserAddress(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addWoredaMaster";
				}
			}
			String status = manageDemographicService.saveWoredaName(woreda);
			if (status.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				model.addAttribute("zone", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Woreda added Successfully.");

			} else {
				model.addAttribute("zone", new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Name Not added.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::saveWoreda():" + e);
		}

		return "redirect:addworeda";
	}

	@RequestMapping(value = { "/viewworeda" })
	public String viewWoreda(Model model, @ModelAttribute SearchVo searchVo) {

		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			if (searchVo.getRegionId() != 0) {
				model.addAttribute(WrsisPortalConstant.ZONE_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			model.addAttribute("woredalist", manageDemographicService.viewWoreda(searchVo));
		} catch (Exception e) {
			LOG.error("ManageDemographicController::viewWoreda():" + e);
		}

		return "viewWoredaMaster";
	}

	@RequestMapping(value = { "/editWoreda" }, method = RequestMethod.POST)
	public String editWoreda(@RequestParam(value = "demographyId", required = true) int demographyId, Model model) {
		DemographicBean woredaObj = new DemographicBean();
		try {

			DemographicEntity woreda = manageDemographicService.editWoreda(demographyId);
			woredaObj.setZoneId(woreda.getParentId());

			DemographicEntity zone = manageDemographicService.editWoreda(woreda.getParentId());
			woredaObj.setRegionId(zone.getParentId());

			DemographicEntity region = manageDemographicService.editWoreda(zone.getParentId());
			woredaObj.setCountryId(region.getParentId());

			woredaObj.setDemographyName(woreda.getDemographyName());
			woredaObj.setDemographyId(woreda.getDemographyId());
			woredaObj.setAlias(woreda.getAlias());
			woredaObj.setDescription(woreda.getDescription());
			woredaObj.setStatus(woreda.getStatus());

			model.addAttribute(WrsisPortalConstant.WOREDA1, woredaObj);

			model.addAttribute("zone", manageDemographicService.getDemographyListByParentId(zone.getParentId()));

			model.addAttribute("region", manageDemographicService.getDemographyListByParentId(region.getParentId()));

			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());

		} catch (Exception e) {
			LOG.error("ManageDemographicController::editWoreda():" + e);
		}
		return "editWoredaMaster";
	}

	@RequestMapping(value = { "/updateWoreda" })
	public String updateWoreda(@Valid @ModelAttribute(WrsisPortalConstant.WOREDA1) DemographicBean woreda, HttpServletRequest request,
			BindingResult binding, Model model, RedirectAttributes redirects) {
		String result = WrsisPortalConstant.FAILURE;
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		List<DemographicEntity> regions = null;
		List<DemographicEntity> zones = null;
		try {
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			woreda.setUserId(userId);
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			regions = manageDemographicService.regionList();
			model.addAttribute("region", regions);
			zones = manageDemographicService.zoneList();
			model.addAttribute("zone", zones);
			LOG.info("Woreda binding" + binding.hasErrors());
			model.addAttribute(WrsisPortalConstant.WOREDA1, woreda);
			if (binding.hasErrors()) {
				return "editWoredaMaster";
			}
			LOG.info("In update Woreda Id" + woreda.getDemographyId());

			if (woreda.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "editWoredaMaster";
			}
			if (woreda.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "editWoredaMaster";
			}
			if (woreda.getZoneId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_ZONE);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "zone");
				return "editWoredaMaster";
			}
			if (woreda.getDemographyName() == null || woreda.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Woreda Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "editWoredaMaster";
			}
			if (!woreda.getDemographyName().isEmpty()) {
				if (Validation.validateDemographyName(woreda.getDemographyName()) == false) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editWoredaMaster";
				}
				if (!Validation.validateFirstBlankSpace(woreda.getDemographyName())
						|| !Validation.validateLastBlankSpace(woreda.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Woreda Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editWoredaMaster";
				}
			}
			if (woreda.getAlias() == null || woreda.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "editWoredaMaster";
			}
			if (!woreda.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(woreda.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editWoredaMaster";
				}
				if (!Validation.validateFirstBlankSpace(woreda.getAlias())
						|| !Validation.validateLastBlankSpace(woreda.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editWoredaMaster";
				}
			}
			if (!woreda.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(woreda.getDescription())
						|| !Validation.validateLastBlankSpace(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editWoredaMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editWoredaMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editWoredaMaster";
				}
				if (!Validation.validateUserAddress(woreda.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editWoredaMaster";
				}
			}
			result = manageDemographicService.updateWoreda(woreda);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redirects.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Woreda Updated Successfully.");
			} else {
				redirects.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Woreda Not Updated.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::updateWoreda():" + e);
		}
		return "redirect:viewworeda";
	}

	@RequestMapping(value = { "/addkebele" })
	public String addkebele(@ModelAttribute(WrsisPortalConstant.KEBELE1) DemographicBean kebele, Model model) {
		List<DemographicEntity> country = null;
		
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			
			
		} catch (Exception e) {
			LOG.error("ManageDemographicController::addkebele():" + e);
		}
		return "addKebeleMaster";
	}

	@RequestMapping(value = "/add-kebele", method = RequestMethod.POST)
	public String saveKebele(Model model, @ModelAttribute(WrsisPortalConstant.KEBELE1) DemographicBean kebele, HttpServletRequest request,
			RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		List<DemographicEntity> regionList = null;
		List<DemographicEntity> zoneList = null;
		List<DemographicEntity> woredaList = null;
		try {
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			regionList = manageDemographicService.regionList();
			model.addAttribute("region", regionList);
			zoneList = manageDemographicService.zoneList();
			model.addAttribute("zone", zoneList);
			woredaList = manageDemographicService.woredaList();
			model.addAttribute("zone", woredaList);
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			kebele.setUserId(userId);

			if (kebele.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "addKebeleMaster";
			}
			if (kebele.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "addKebeleMaster";
			}
			if (kebele.getZoneId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_ZONE);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "zone");
				return "addKebeleMaster";
			}
			if (kebele.getWoredaId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Woreda");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.WOREDA1);
				return "addKebeleMaster";
			}
			if (kebele.getDemographyName() == null || kebele.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Kebele Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "addKebeleMaster";
			}
			if (!kebele.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(kebele.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Kebele Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addKebeleMaster";
				}
				if (!Validation.validateFirstBlankSpace(kebele.getDemographyName())
						|| !Validation.validateLastBlankSpace(kebele.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Kebele Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Kebele Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "addKebeleMaster";
				}
			}
			if (kebele.getAlias() == null || kebele.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Short Name ");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "addKebeleMaster";
			}
			if (!kebele.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(kebele.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addKebeleMaster";
				}
				if (!Validation.validateFirstBlankSpace(kebele.getAlias())
						|| !Validation.validateLastBlankSpace(kebele.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "addKebeleMaster";
				}
			}
			if (!kebele.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(kebele.getDescription())
						|| !Validation.validateLastBlankSpace(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addKebeleMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addKebeleMaster";
				}
				if (!Validation.validateUserAddress(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "addKebeleMaster";
				}
			}
			String status = manageDemographicService.saveKebeleName(kebele);
			if (status.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				model.addAttribute(WrsisPortalConstant.KEBELE, new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Kebele added successfully.");

			} else {
				model.addAttribute(WrsisPortalConstant.KEBELE, new DemographicBean());
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Kebele not added.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::savekebele():" + e);
		}

		return "redirect:addkebele";
	}

	@RequestMapping(value = { "/viewkebele" })
	public String viewKebele(Model model, @ModelAttribute SearchVo searchVo) {

		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			if (searchVo.getRegionId() != 0) {
				model.addAttribute(WrsisPortalConstant.ZONE_LIST,
						manageDemographicService.getDemographyListByParentId(searchVo.getRegionId()));
			}
			if (searchVo.getRegionId() != 0 && searchVo.getZoneId() != 0) {
				model.addAttribute("woredalist",
						manageDemographicService.getDemographyListByParentId(searchVo.getZoneId()));
			}
			model.addAttribute("kebelelist", manageDemographicService.viewKebele(searchVo));
		} catch (Exception e) {
			LOG.error("ManageDemographicController::viewKebele():" + e);
		}

		return "viewKebeleMaster";
	}

	@RequestMapping(value = { "/editKebele" }, method = RequestMethod.POST)
	public String editKebele(@RequestParam(value = "demographyId", required = true) int demographyId, Model model) {
		DemographicBean kebeleObj = new DemographicBean();
		try {

			DemographicEntity kebele = manageDemographicService.editKebele(demographyId);
			kebeleObj.setWoredaId(kebele.getParentId());

			DemographicEntity woreda = manageDemographicService.editKebele(kebele.getParentId());
			kebeleObj.setZoneId(woreda.getParentId());

			DemographicEntity zone = manageDemographicService.editKebele(woreda.getParentId());
			kebeleObj.setRegionId(zone.getParentId());

			DemographicEntity region = manageDemographicService.editKebele(zone.getParentId());
			kebeleObj.setCountryId(region.getParentId());

			kebeleObj.setDemographyName(kebele.getDemographyName());
			kebeleObj.setDemographyId(kebele.getDemographyId());
			kebeleObj.setAlias(kebele.getAlias());
			kebeleObj.setDescription(kebele.getDescription());
			kebeleObj.setStatus(kebele.getStatus());

			model.addAttribute(WrsisPortalConstant.KEBELE1, kebeleObj);

			model.addAttribute(WrsisPortalConstant.WOREDA1, manageDemographicService.getDemographyListByParentId(woreda.getParentId()));

			model.addAttribute("zone", manageDemographicService.getDemographyListByParentId(zone.getParentId()));

			model.addAttribute("region", manageDemographicService.getDemographyListByParentId(region.getParentId()));

			model.addAttribute(WrsisPortalConstant.COUNTRY, manageDemographicService.viewCountry());

		} catch (Exception e) {
			LOG.error("ManageDemographicController::editKebele():" + e);
		}
		return "editKebeleMaster";
	}

	@RequestMapping(value = { "/updateKebele" })
	public String updateKebele(@Valid @ModelAttribute(WrsisPortalConstant.KEBELE1) DemographicBean kebele, HttpServletRequest request,
			BindingResult binding, Model model, RedirectAttributes redirects) {
		String result = WrsisPortalConstant.FAILURE;
		HttpSession session = request.getSession();
		List<DemographicEntity> country = null;
		List<DemographicEntity> regions = null;
		List<DemographicEntity> zones = null;
		List<DemographicEntity> woredas = null;
		try {
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			kebele.setUserId(userId);
			country = manageDemographicService.viewCountry();
			model.addAttribute(WrsisPortalConstant.COUNTRY, country);
			regions = manageDemographicService.regionList();
			model.addAttribute("region", regions);
			zones = manageDemographicService.zoneList();
			model.addAttribute("zone", zones);
			woredas = manageDemographicService.woredaList();
			model.addAttribute("zone", woredas);

			model.addAttribute(WrsisPortalConstant.KEBELE1, kebele);
			if (binding.hasErrors()) {
				return "editKebeleMaster";
			}

			if (kebele.getCountryId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_COUNTRY);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.COUNTRY);
				return "editKebeleMaster";
			}
			if (kebele.getRegionId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_REGION);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "region");
				return "editKebeleMaster";
			}
			if (kebele.getZoneId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_SELECT_ZONE);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, "zone");
				return "editKebeleMaster";
			}
			if (kebele.getWoredaId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Woreda");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.WOREDA1);
				return "editKebeleMaster";
			}
			if (kebele.getDemographyName() == null || kebele.getDemographyName().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Kebele Name");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
				return "editKebeleMaster";
			}
			if (!kebele.getDemographyName().isEmpty()) {
				if (!Validation.validateDemographyName(kebele.getDemographyName()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Kebele Name accept only alphabets and numbers");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editKebeleMaster";
				}
				if (!Validation.validateFirstBlankSpace(kebele.getDemographyName())
						|| !Validation.validateLastBlankSpace(kebele.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Kebele Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getDemographyName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Kebele Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMOGRAPHY_NAME);
					return "editKebeleMaster";
				}
			}
			if (kebele.getAlias() == null || kebele.getAlias().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.PLEASE_ENTER_SHORT_NAME);
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
				return "editKebeleMaster";
			}
			if (!kebele.getAlias().isEmpty()) {
				if (!Validation.validateDemographyName(kebele.getAlias()) ) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.SHORT_NAME_ACCEPT_ONLY_ALPHABETS_AND_NUMBERS);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editKebeleMaster";
				}
				if (!Validation.validateFirstBlankSpace(kebele.getAlias())
						|| !Validation.validateLastBlankSpace(kebele.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_SHORT_NAME);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getAlias())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Short Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SHORT_NAME);
					return "editKebeleMaster";
				}
			}
			if (!kebele.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(kebele.getDescription())
						|| !Validation.validateLastBlankSpace(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							WrsisPortalConstant.WHITE_SPACE_IS_NOT_ALLOWED_AT_INITIAL_AND_LAST_PLACE_IN_DESCRIPTION);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editKebeleMaster";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.DESCRIPTION_SHOULD_NOT_CONTAIN_CONSECUTIVE_BLANK_SPACES);
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editKebeleMaster";
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editKebeleMaster";
				}
				if (!Validation.validateUserAddress(kebele.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return "editKebeleMaster";
				}
			}
			result = manageDemographicService.updateKebele(kebele);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redirects.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Kebele Updated Successfully.");
			} else {
				redirects.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Kebele Not Updated.");
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicController::updateKebele():" + e);
		}
		return "redirect:viewkebele";
	}

	// Ajax methods

	@RequestMapping(value = "/GetDemographyByParentId/{pId}")
	public @ResponseBody List<DemographicEntity> getDemographyByParentId(@PathVariable("pId") int pId) {
		return manageDemographicService.getDemographyListByParentId(pId);
	}

}
