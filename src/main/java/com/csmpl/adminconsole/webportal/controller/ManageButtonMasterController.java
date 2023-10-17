package com.csmpl.adminconsole.webportal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.ButtonMasterVo;
import com.csmpl.adminconsole.webportal.bean.FunctionMasterVo;
import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.service.ManageButtonMasterService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;

@Controller
public class ManageButtonMasterController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(ManageButtonMasterController.class);

	@Autowired
	private ManageButtonMasterService buttonMasterService; /* Bean creation for ManageTabMasterService */

	@GetMapping(value = { "/addButtonMaster" }) /* To load Button Master add page */
	public String loadFunctionMaster(Model model) {
		List<FunctionMasterVo> FunctionList = new ArrayList<>(); // to fetch function Master list list
		int maxSlNo = 0;
		try {

			maxSlNo = buttonMasterService.getMaxSlNo();

			model.addAttribute("maxSlNo", maxSlNo);
			FunctionList = buttonMasterService.getFunctionList();
			model.addAttribute(WrsisPortalConstant.BUTTON_VO, new ButtonMasterVo());
			model.addAttribute(WrsisPortalConstant.FUNCTION_LIST, FunctionList);
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::loadFunctionMaster():" + e);
		}

		return "addButtonMaster";
	}

	@PostMapping(value = { "/registerButtonMaster" })
	public String addButtonMaster(@Valid @ModelAttribute(WrsisPortalConstant.BUTTON_VO) ButtonMasterVo buttonVo, BindingResult binding,
			Model model) {
		List<FunctionMasterVo> FunctionList = new ArrayList<>(); // to fetch function Master list list
		String result = WrsisPortalConstant.FAILURE;
		try {
			FunctionList = buttonMasterService.getFunctionList();
			model.addAttribute(WrsisPortalConstant.FUNCTION_LIST, FunctionList);

			model.addAttribute(WrsisPortalConstant.BUTTON_VO, buttonVo);
			LOG.info("Result has error:" + binding.hasErrors());
			if (binding.hasErrors()) {
				return "addButtonMaster";
			}

			result = buttonMasterService
					.addButtonMaster(buttonVo); /* Save Entered Data of Button Master to Data Base */

			if (WrsisPortalConstant.SUCCESS.equals(result)) {

				model.addAttribute(WrsisPortalConstant.BUTTON_VO, new ButtonMasterVo());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Button Master Added Successfully.");

			} else {
				model.addAttribute(WrsisPortalConstant.BUTTON_VO, new ButtonMasterVo());
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Button Master Not Added.");
			}

		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::addButtonMaster():" + e);
		}

		return "addButtonMaster";
	}

	@RequestMapping({ "/viewButtonMaster" })
	public String viewButtonMasterList(@ModelAttribute SearchVo searchVo, Model model) {

		try {
			model.addAttribute("statusList", getStatus());
			model.addAttribute("buttonList", buttonMasterService.viewButtonMasterList(searchVo));
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::viewButtonMasterList():" + e);
		}

		return "viewButtonMaster";
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
			LOG.error("ManageButtonMasterController::getStatus():" + e);
		}

		return statusList;
	}

	@RequestMapping(value = { "/editButtonMaster" }) /* To load Function Master edit page */
	public String editButtonMaster(@RequestParam(value = "buttonId", required = true) int buttonId, Model model) {
		List<FunctionMasterVo> FunctionList = new ArrayList<>(); // to fetch function Master list list
		ButtonMasterVo buttonList = new ButtonMasterVo();
		try {
			LOG.info("In Function edit" + buttonId);
			FunctionList = buttonMasterService.getFunctionList();
			model.addAttribute(WrsisPortalConstant.BUTTON_VO, new ButtonMasterVo());
			buttonList = buttonMasterService.editButtonMaster(buttonId);
			model.addAttribute(WrsisPortalConstant.BUTTON_VO, buttonList);
			model.addAttribute(WrsisPortalConstant.FUNCTION_LIST, FunctionList);
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::editButtonMaster():" + e);
		}

		return "editButtonMaster";
	}

	@RequestMapping(value = { "/updateButtonMaster" })
	public String updateButtonMaster(@Valid @ModelAttribute(WrsisPortalConstant.BUTTON_VO) ButtonMasterVo buttonVo, BindingResult binding,
			Model model, RedirectAttributes redircts) {
		List<FunctionMasterVo> FunctionList = new ArrayList<>(); // to fetch function Master list list
		String result = WrsisPortalConstant.FAILURE;
		try {
			FunctionList = buttonMasterService.getFunctionList();
			model.addAttribute(WrsisPortalConstant.FUNCTION_LIST, FunctionList);

			model.addAttribute(WrsisPortalConstant.BUTTON_VO, buttonVo);
			LOG.info("ManageButtonMasterController::Result has error:" + binding.hasErrors());
			if (binding.hasErrors()) {
				return "addButtonMaster";
			}

			result = buttonMasterService.updateButtonMaster(buttonVo);
			if (WrsisPortalConstant.SUCCESS.equals(result)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Button Master Updated Successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Button Master Not Updated.");
			}
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::updateButtonMaster():" + e);
		}

		return "redirect:viewButtonMaster";
	}

	@RequestMapping(value = { "/deleteButtonMaster" }) /* To load Button Master delete page */
	public String deleteButtonMaster(@RequestParam(value = "buttonId", required = true) int buttonId,
			@RequestParam(value = "status", required = true) int status, Model model, RedirectAttributes redircts) {

		String result = WrsisPortalConstant.FAILURE;
		try {
			LOG.info("In Button Master delete --->" + buttonId + "<---");
			if (status == 0) {
				result = buttonMasterService.deleteButtonMaster(buttonId);
			}

			if (WrsisPortalConstant.SUCCESS.equals(result)) {
				redircts.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Button Master Deleted Successfully.");

			} else {
				redircts.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Button Master Not Deleted.");
			}

		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::deleteButtonMaster():" + e);
		}

		return "redirect:viewButtonMaster";
	}

	@RequestMapping(value = { "/chkButtonName" })
	public @ResponseBody String chkButtonName(@RequestParam(value = "but_name", required = true) String buttonName) {
		String result = WrsisPortalConstant.NOT_EXIST;
		try {
			result = buttonMasterService.chkDuplicateButtonNameByName(buttonName);
			if (WrsisPortalConstant.EXIST.equals(result)) {
				result = WrsisPortalConstant.EXIST;
			}
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::chkButtonName():" + e);
		}
		return result;
	}

	@RequestMapping(value = { "/chkButtonMasterFileName" })
	public @ResponseBody String chkButtonMasterFileName(
			@RequestParam(value = "file_name", required = true) String fileName) {
		String result = WrsisPortalConstant.NOT_EXIST;
		try {
			result = buttonMasterService.chkButtonMasterFileNameByName(fileName);
			if (WrsisPortalConstant.EXIST.equals(result)) {
				result = WrsisPortalConstant.EXIST;
			}
		} catch (Exception e) {
			LOG.error("ManageButtonMasterController::chkButtonMasterFileName():" + e);
		}
		return result;
	}

}
