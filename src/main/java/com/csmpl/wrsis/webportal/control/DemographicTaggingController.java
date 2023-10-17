package com.csmpl.wrsis.webportal.control;

import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.AdmLevelDetails;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.repository.AdminLevelRepository;
import com.csmpl.adminconsole.webportal.repository.AdminRoleRepository;
import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DemographicLocationTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.service.DemographicTaggingService;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;

@Controller
public class DemographicTaggingController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(DemographicTaggingController.class);
	@Autowired
	AdminRoleRepository adminRoleRepository;
	@Autowired
	UserService userService;
	@Autowired
	DemographicTaggingService demographicTaggingService;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	ManageDemographicService manageDemographicService;
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	AdminLevelRepository adminLevelRepository;

	@GetMapping("/demographicTagging")
	public String demographicTagging(Model model) {
		try {
			List<AdmLevelDetails> orgList = userService.getOrganizationList();
			model.addAttribute(WrsisPortalConstant.ORG_LIST, orgList);
			List<AdminRole> roleList = adminRoleRepository.getRoleName();
			if (roleList != null) {
				model.addAttribute(WrsisPortalConstant.ROLE_NAME, roleList);
			}
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}

		} catch (Exception e) {
			LOG.error("DemographicTaggingController::demographicTagging():" + e);

		}
		return "demographicTagging";
	}

	@ResponseBody
	@GetMapping("/findUserNameByOrgIDAndRoleID")
	public String getUserName(@RequestParam(value = "orgId", required = false) Integer orgId,
			@RequestParam(value = "roleId", required = false) Integer roleId) {
		return demographicTaggingService.getUserNameByRoleIdAndOrganizationId(roleId, orgId);
	}

	@ResponseBody
	@GetMapping("/find-multiple-zone-by-region-id")
	public String findMulultipleZoneByRegionId(@RequestParam("levelId") Integer levelId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId) {
		return manageDemographicService.findMulultipleZoneByRegionId(levelId, regionId);
	}

	@ResponseBody
	@GetMapping("/find-multiple-woreda-by-zone-id")
	public String findMulultipleWoredaByZoneId(@RequestParam("levelId") Integer levelId,
			@RequestParam("zoneId") String zoneId) {
		return manageDemographicService.findMulultipleWoredaByZoneId(levelId, zoneId);
	}

	@ResponseBody
	@GetMapping("/find-multiple-kebele-by-woreda-id")
	public String findMulultipleKebeleByWoredaId(@RequestParam("levelId") Integer levelId,
			@RequestParam("woredaId") String woredaId) {
		return manageDemographicService.findMulultipleKebeleByWoredaId(levelId, woredaId);
	}

	@ResponseBody
	@GetMapping("/serchLocationDetails-by-userId")
	public String serchLocationDetailsByUserId(@RequestParam("userId") Integer userId, Model model) {

		return demographicTaggingService.serchLocationDetailsByUserId(userId);
	}

	@PostMapping("/insert-demography-tagging")
	public String saveAndUpdateDemographyLocationTagging(Model model, @RequestParam("userNameId") Integer userNameId,
			@RequestParam(WrsisPortalConstant.REGION_ID) String[] regionId, @RequestParam(value="ZoneNameId", required = false) String[] ZoneNameId,
			@RequestParam(value = "f_woredaId", required = false) String[] f_woredaId,
			@RequestParam(value = "f_kebeleId", required = false) String[] f_kebeleId,
			@RequestParam(value="status" , required = false) boolean status, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			DemographicLocationTagBean tag = new DemographicLocationTagBean();
			User user = new User();
			user.setUserId(userNameId);
			tag.setUserIdBean(user);
			tag.setRegionIdBean(regionId);
			tag.setZoneIdBean(ZoneNameId);
			tag.setWoredaIdBean(f_woredaId);
			tag.setKebeleIdBean(f_kebeleId);
			tag.setCreadtedByBean(userId);
			tag.setStatusBean(status);
			String msg = demographicTaggingService.saveAndUpdateTaggingLocationWithLevelId(tag);

			if (msg.equals("Save")) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Saved Successfully");
			}
			if (msg.equals("fail")) {
				model.addAttribute("msg_1", "Try Again");
			}

			List<AdmLevelDetails> orgList = userService.getOrganizationList();
			model.addAttribute(WrsisPortalConstant.ORG_LIST, orgList);
			List<AdminRole> roleList = adminRoleRepository.getRoleName();
			if (roleList != null) {
				model.addAttribute(WrsisPortalConstant.ROLE_NAME, roleList);
			}
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}

		} catch (Exception e) {
			LOG.error("DemographicTaggingController::saveAndUpdateDemographyLocationTagging():" + e);
		}

		return "demographicTagging";
	}

	@GetMapping(value = { "/demographicTaggingView" })
	public String demographicTaggingView(Model model) {
		List<AdmLevelDetails> orgList = userService.getOrganizationList();
		model.addAttribute(WrsisPortalConstant.ORG_LIST, orgList);
		List<AdminRole> roleList = adminRoleRepository.getRoleName();
		if (roleList != null) {
			model.addAttribute(WrsisPortalConstant.ROLE_NAME, roleList);
		}

		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}

		return "demographicTaggingView";
	}

	@ResponseBody
	@GetMapping("/viewAllDemogrphyLocationTagByPage")
	public String viewDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return demographicTaggingService.viewDemographyLocationTagByPage(pageSize, pageNumber, pageable);
	}

	@RequestMapping(value = { "/demographicTaggingEdit" })
	public String demographicTaggingEdit(Model model, @RequestParam("locationtagId") Integer locationTagId) {
		DemographicLocationTagBean tagLocation = demographicTaggingService.getTaggingDetailsById(locationTagId);
		model.addAttribute("tagging", tagLocation);

		

		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}

		List<Object[]> roleList = userRoleRepository.findUserRoleNameByUserId(tagLocation.getUserIdBean().getUserId());
		for (Object[] rName : roleList) {
			model.addAttribute(WrsisPortalConstant.ROLE_NAME, rName[1]);
			model.addAttribute("roleNameId", rName[0]);
		}
		AdmLevelDetails levelDetails = adminLevelRepository
				.findLevelDetailsByUserId(tagLocation.getUserIdBean().getIntLevelDetailId());
		if (levelDetails != null) {
			model.addAttribute(WrsisPortalConstant.ORG_LIST, levelDetails.getLevelName());
			model.addAttribute("orgListId", levelDetails.getLevelDetailId());
		} else {
			model.addAttribute(WrsisPortalConstant.ORG_LIST, "-NA-");
		}
	

		return "demographicTaggingEdit";
	}

	@PostMapping("/update-demography-tagging")
	public String updateDemographyLocationTagging(Model model, @RequestParam("tagId") Integer locationTagId,
			@RequestParam(value = "userNameId", required = false) Integer userNameId,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) String[] regionId,
			@RequestParam(value = "ZoneNameId", required = false) String[] ZoneNameId,
			@RequestParam(value = "f_woredaId", required = false) String[] f_woredaId,
			@RequestParam("status") boolean status,
			@RequestParam(value = "f_kebeleId", required = false) String[] f_kebeleId, HttpServletRequest request) {
		String returnPage = "demographicTaggingView";
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			DemographicLocationTagBean tag = new DemographicLocationTagBean();
			User user = new User();
			user.setUserId(userNameId);
			tag.setUserIdBean(user);
			tag.setRegionIdBean(regionId);
			tag.setZoneIdBean(ZoneNameId);
			tag.setWoredaIdBean(f_woredaId);
			tag.setKebeleIdBean(f_kebeleId);
			tag.setCreadtedByBean(userId);
			tag.setLocationTagIdBean(locationTagId);
			tag.setStatusBean(status);

			String msg = demographicTaggingService.saveAndUpdateTaggingLocationWithLevelId(tag);
			
			if (msg.equals("update")) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
			}
			if (msg.equals("fail")) {
				model.addAttribute("msg_1", "Try Again");
			}

			List<AdmLevelDetails> orgList = userService.getOrganizationList();
			model.addAttribute(WrsisPortalConstant.ORG_LIST, orgList);
			List<AdminRole> roleList = adminRoleRepository.getRoleName();
			if (roleList != null) {
				model.addAttribute(WrsisPortalConstant.ROLE_NAME, roleList);
			}
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}

		} catch (Exception e) {
			LOG.error("DemographicTaggingController::updateDemographyLocationTagging():" + e);

		}

		return returnPage;
	}

	@ResponseBody
	@GetMapping("/searchDemogrphyLocationTagByPage")
	public String searchDemogrphyLocationTagByPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "orgName", required = false) Integer orgName,
			@RequestParam(value = "Status", required = false) String Status,
			@RequestParam(value = "roleId", required = false) Integer roleId,
			@RequestParam(value = "userId", required = false) Integer userId,

			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return demographicTaggingService.searchDemographyLocationTagByPage(pageSize, pageNumber, pageable, userId,
				Status);
	}

	@ResponseBody
	@GetMapping("/serchLocationDetails-by-userId-RegionId")
	public String serchLocationDetailsByUserIdAndRegionId(@RequestParam("userId") Integer userId,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, Model model) {

		return demographicTaggingService.serchLocationDetailsByUserId(userId);
	}

	@ResponseBody
	@GetMapping("/findUserNameByOrgIDAndRoleIDSearch")
	public String findUserNameByOrgIDAndRoleIDSearch(@RequestParam(value = "orgId", required = false) Integer orgId,
			@RequestParam(value = "roleId", required = false) Integer roleId) {
		return demographicTaggingService.getUserNameByRoleIdAndOrganizationIdSearch(roleId, orgId);
	}
}
