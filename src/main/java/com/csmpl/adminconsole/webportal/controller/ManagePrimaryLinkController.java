package com.csmpl.adminconsole.webportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.FunctionMasterVo;
import com.csmpl.adminconsole.webportal.bean.GlobalLinkVo;
import com.csmpl.adminconsole.webportal.bean.PrimaryLinkBean;
import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.service.ManagePrimaryLinkService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class ManagePrimaryLinkController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(ManagePrimaryLinkController.class);

	@Autowired
	private ManagePrimaryLinkService primaryLinkservice; // Bean creation for ManagePrimaryLinkService

	@GetMapping(value = { "/addPrimaryLink" }) // To load Primary Link add page
	public String loadPrimaryLink(Model model) {
		List<GlobalLinkVo> globalList = new ArrayList<>(); // To fetch global list
		List<FunctionMasterVo> functionList = new ArrayList<>(); // To fetch function Master list list
		int maxSlNumber = 0;
		try {

			maxSlNumber = primaryLinkservice.getMaxSlNumber();

			model.addAttribute(WrsisPortalConstant.MAX_SL_NO, maxSlNumber);// for serial no.

			globalList = primaryLinkservice.getGlobalLink(); // Retrieve global list
			functionList = primaryLinkservice.getFunctionList(); // Retrieve function Master list

			model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, new PrimaryLinkBean());
			model.addAttribute("globalList", globalList);
			model.addAttribute("functionList", functionList);

		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::loadPrimaryLink():" + e);
		}
		return "addPrimaryLink";
	}

	@PostMapping({ "/registerPrimarylink" })
	public String addPrimaryLink(@Valid @ModelAttribute(WrsisPortalConstant.PRIMARY_LINK_VO) PrimaryLinkBean primarylinkVo,
			BindingResult binding, Model model) {
		List<GlobalLinkVo> globalList = new ArrayList<>(); // To fetch global list
		List<FunctionMasterVo> functionList = new ArrayList<>(); // To fetch function Master list
		String result = "";
		int maxSlNumber = 0;
		try {
			globalList = primaryLinkservice.getGlobalLink(); // Retrieve global list
			functionList = primaryLinkservice.getFunctionList(); // Retrieve function Master list
			model.addAttribute("globalList", globalList);
			model.addAttribute("functionList", functionList);
			model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, primarylinkVo);

			maxSlNumber = primaryLinkservice.getMaxSlNumber();

			model.addAttribute(WrsisPortalConstant.MAX_SL_NO, maxSlNumber); // for serial no.

			LOG.info("ManagePrimaryLinkController::Result has error:" + binding.hasErrors());
			if (binding.hasErrors()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter valid data");
				return "addPrimaryLink";
			}
			if (!primarylinkVo.getPrimaryLinkName().isEmpty()) {
				if (primaryLinkservice.chkDuplicatePrimaryLinkNameByName(primarylinkVo.getPrimaryLinkName())
						.equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link already exists");
					return "addPrimaryLink";
				}
				if (!Validation.validateAlphabatesOnly(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link accept only alphabets");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "addPrimaryLink";
				}
				if (!Validation.validateFirstBlankSpace(primarylinkVo.getPrimaryLinkName())
						|| !Validation.validateLastBlankSpace(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Primary Link");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "addPrimaryLink";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "addPrimaryLink";
				}
			}
			String sl = String.valueOf(primarylinkVo.getSlNo());
			if (!sl.isEmpty()) {
				
				if (!Validation.validateFirstBlankSpace(sl) || !Validation.validateLastBlankSpace(sl)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Serial Number");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "slNo");
					return "addPrimaryLink";
				}
				if (Validation.validateNumberOnly(sl) == false) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Serial Number accept only numbers.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "slNo");
					return "addPrimaryLink";
				}
			}
			result = primaryLinkservice.addPrimaryLink(primarylinkVo); // Save Entered Data of Primary Link to Data Base

			LOG.info("ManagePrimaryLinkController::PrimaryLink link result" + result);

			if (WrsisPortalConstant.SUCCESS.equals(result)) {
				model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, new PrimaryLinkBean());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Primary Link Added Successfully.");

			} else {
				model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, new PrimaryLinkBean());
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link Not Added.");
			}

			maxSlNumber = primaryLinkservice.getMaxSlNumber();

			model.addAttribute(WrsisPortalConstant.MAX_SL_NO, maxSlNumber); // for serial no.

		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::addPrimaryLink():" + e);
		}

		return "addPrimaryLink";
	}

	@RequestMapping({ "/viewPrimaryLink" })
	public String viewPrimaryLinks(@ModelAttribute SearchVo searchVo, Model model) {

		try {
			model.addAttribute("statusList", getStatus());
			model.addAttribute("primaryList", primaryLinkservice.viewPrimaryLinks(searchVo));
		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::viewPrimaryLinks():" + e);
		}

		return "viewPrimaryLink";
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
			LOG.error("ManagePrimaryLinkController::getStatus():" + e);
		}

		return statusList;
	}

	@RequestMapping(value = { "/editPrimaryLink" }) // To load Function Master edit page
	public String editPrimaryLink(@RequestParam(value = "primaryLinkId", required = true) int primaryLinkId,
			Model model) {
		List<GlobalLinkVo> globalList = new ArrayList<>(); // To fetch global list
		List<FunctionMasterVo> functionList = new ArrayList<>(); // To fetch function Master list list
		PrimaryLinkBean primaryObject = new PrimaryLinkBean();
		try {
			LOG.info("ManagePrimaryLinkController::In Primary Link edit" + primaryLinkId);

			globalList = primaryLinkservice.getGlobalLink(); // Retrieve global list
			functionList = primaryLinkservice.getFunctionList();
			model.addAttribute("globalList", globalList);
			model.addAttribute("functionList", functionList);

			primaryObject = primaryLinkservice.editPrimaryLink(primaryLinkId);

			model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, primaryObject);

		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::editPrimaryLink():" + e);
		}

		return "editPrimaryLink";
	}

	@RequestMapping(value = { "/updatePrimarylink" })
	public String updatePrimarylink(@Valid @ModelAttribute(WrsisPortalConstant.PRIMARY_LINK_VO) PrimaryLinkBean primarylinkVo,
			BindingResult binding, Model model, RedirectAttributes redircts) {
		List<GlobalLinkVo> globalList = new ArrayList<>(); // To fetch global list
		List<FunctionMasterVo> functionList = new ArrayList<>(); // To fetch function Master list
		String result = WrsisPortalConstant.FAILURE;
		try {
			globalList = primaryLinkservice.getGlobalLink(); // Retrieve global list
			functionList = primaryLinkservice.getFunctionList(); // Retrieve function Master list
			model.addAttribute("globalList", globalList);
			model.addAttribute("functionList", functionList);

			LOG.info("ManagePrimaryLinkController::Result has error:" + binding.hasErrors());
			model.addAttribute(WrsisPortalConstant.PRIMARY_LINK_VO, primarylinkVo);
			if (binding.hasErrors()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter valid data");
				return "editPrimaryLink";
			}
			if (!primarylinkVo.getPrimaryLinkName().isEmpty()) {
				if (primaryLinkservice.chkDuplicatePrimaryLinkNameByName(primarylinkVo.getPrimaryLinkId(),
						primarylinkVo.getPrimaryLinkName()).equalsIgnoreCase(WrsisPortalConstant.EXIST)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link already exists");
					return "editPrimaryLink";
				}
				if (!Validation.validateAlphabatesOnly(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link accept only alphabets");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "editPrimaryLink";
				}
				if (!Validation.validateFirstBlankSpace(primarylinkVo.getPrimaryLinkName())
						|| !Validation.validateLastBlankSpace(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Primary Link");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "editPrimaryLink";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(primarylinkVo.getPrimaryLinkName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "primaryLinkName");
					return "editPrimaryLink";
				}
			}
			String sl = String.valueOf(primarylinkVo.getSlNo());
			if (!sl.isEmpty()) {
				
				if (!Validation.validateFirstBlankSpace(sl) || !Validation.validateLastBlankSpace(sl)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Serial Number");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "slNo");
					return "editPrimaryLink";
				}
				if (!Validation.validateNumberOnly(sl)) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Serial Number accept only numbers.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "slNo");
					return "editPrimaryLink";
				}
			}

			result = primaryLinkservice.updatePrimarylink(primarylinkVo);
			if (WrsisPortalConstant.SUCCESS.equals(result)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Primary Link Updated Successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Primary Link Not Updated.");
			}
		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::updatePrimaryLink():" + e);
		}

		return "redirect:viewPrimaryLink";
	}

	@RequestMapping(value = { "/deletePrimaryLink" }) // To load Primary Link delete page
	public String deletePrimaryLink(@RequestParam(value = "primaryLinkId", required = true) int primaryLinkId,
			@RequestParam(value = "status", required = true) int status, Model model, RedirectAttributes redircts) {

		String result = WrsisPortalConstant.FAILURE;
		try {
			LOG.info("ManagePrimaryLinkController::In Primary Link delete --->" + primaryLinkId + "<---");
			if (status == 0) {
				result = primaryLinkservice.deletePrimaryLink(primaryLinkId);
			}
			if (WrsisPortalConstant.SUCCESS.equals(result)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Function Master Deleted Successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Function Master Not Deleted.");
			}

		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::deletePrimaryLink():" + e);
		}

		return "redirect:viewPrimaryLink";
	}

	@RequestMapping(value = { "/chkPrimaryLinkName" })
	public @ResponseBody String chkDuplicatePrimaryLinkName(
			@RequestParam(value = "PLname", required = true) String name) {
		String result = WrsisPortalConstant.NOT_EXIST;
		try {
			result = primaryLinkservice.chkDuplicatePrimaryLinkNameByName(name);
			if (WrsisPortalConstant.EXIST.equals(result)) {
				result = WrsisPortalConstant.EXIST;
			}
		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::chkDuplicatePrimaryLinkName():" + e);
		}
		return result;
	}

	

	// deactivePL
	@RequestMapping(value = "/deactivePL/{pl_id}/{status}", method = RequestMethod.GET)
	public String deactivePL(@PathVariable("pl_id") Integer pl_id, @PathVariable("status") Integer status, Model model,
			RedirectAttributes redircts) {
		try {
			
			String isDeactive = "";
			isDeactive = primaryLinkservice.changePrimaryLinkStatus(pl_id, status);
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(isDeactive)) {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "This Primary Link cannot be deactive. Already in use somewhere.");

			} else if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(isDeactive)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, " Data updated successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Data not updated.");
			}

			

		} catch (Exception e) {
			LOG.error("ManagePrimaryLinkController::deactivePL():" + e);
		}
		return "redirect:/viewPrimaryLink";
	}

}
