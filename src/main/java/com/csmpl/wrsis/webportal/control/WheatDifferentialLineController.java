package com.csmpl.wrsis.webportal.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.WheatDifferentialLineEntity;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.service.WheatDifferentialLineService;

@Controller
public class WheatDifferentialLineController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(WheatDifferentialLineController.class);

	@Autowired
	WheatDifferentialLineService wheatDifferentialLineService;
	@Autowired
	TypeOfRustService typeOfRustService;

	@GetMapping(value = { "/wheatDifferentialLineMaster" })
	public String wheatDifferentialLineMaster(
			@ModelAttribute("wheatDiffLineDetails") WheatDifferentialLineEntity wheatDiffLineDetails, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.DIFF_SET_NO, WrsisPortalConstant.wrsisPropertiesFileConstants.DIFFERENTIAL_SET_MAX);
		model.addAttribute(WrsisPortalConstant.SEED_SOURCE_LIST, wheatDifferentialLineService.retriveSeedSource());
		return "wheatDifferentialLineMaster";
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getMaxSeqNo")
	public String getMaxSeqNo(HttpServletRequest request, @RequestParam("rustid") int rustid) throws JSONException {
		return wheatDifferentialLineService.getMaxSeqNo(rustid);
	}

	//Add and Edit of Wheat differential line 
	@PostMapping(value = { "/addEditWheatDiffLine" })
	public String addEditWheatDiffLine(
			@ModelAttribute("wheatDiffLineDetails") WheatDifferentialLineEntity wheatDiffLineDetails, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String returnPage = "wheatDifferentialLineMaster";
		if (wheatDiffLineDetails.getWheatDifLineId() != 0) {
			returnPage = "wheatDifferentialLineMasterEdit";
			wheatDiffLineDetails.setUpdatedBy(userId);
		}
		try {
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.DIFF_SET_NO, WrsisPortalConstant.wrsisPropertiesFileConstants.DIFFERENTIAL_SET_MAX);
			model.addAttribute(WrsisPortalConstant.SEED_SOURCE_LIST, wheatDifferentialLineService.retriveSeedSource());
			wheatDiffLineDetails.setCreatedBy(userId);

			if (wheatDiffLineDetails.getRustTypeId() == 0 || wheatDiffLineDetails.getRustTypeId() == null) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Applicable for Rust Type");
				return returnPage;
			}
			if (wheatDiffLineDetails.getFirstLine() < 0 || wheatDiffLineDetails.getFirstLine() > 2) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Is 1st Differential Line ?");
				return returnPage;
			}
			if (wheatDiffLineDetails.getFirstLine() != 1) {
				if ((wheatDiffLineDetails.getRustTypeId() == 1 || wheatDiffLineDetails.getRustTypeId() == 2)
						&& wheatDiffLineDetails.getDiffSetNo() == 0) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Differential Set Number");
					return returnPage;
				}
				if (wheatDiffLineDetails.getRustTypeId() == 3 && wheatDiffLineDetails.getGene().isEmpty()) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Gene");
					return returnPage;
				}
				if (wheatDiffLineDetails.getRustTypeId() == 3 && wheatDiffLineDetails.getRacePhenotype().isEmpty()) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Race Phenotype");
					return returnPage;
				}
			}
			if (wheatDiffLineDetails.getDifLine().isEmpty() || wheatDiffLineDetails.getDifLine() == ""
					|| wheatDiffLineDetails.getDifLine() == null) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Differential Line Name");
				return returnPage;
			}
			String result = wheatDifferentialLineService.addUpdateWheatDiffLine(wheatDiffLineDetails);
			if (WrsisPortalConstant.EXIST.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Differential Line Name already exists");
			}
			if ("seqexists".equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Sequence No. already exists for this Rust Type");
			}
			if ("diffExists".equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Maximum no. of Differential Lines has already added for this Set");
			}
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				model.addAttribute("wheatDiffLineDetails", new WheatDifferentialLineEntity());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data added successfully");
			}
			if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
				returnPage = "redirect:wheatDifferentialLineMasterView";
			}
			if (WrsisPortalConstant.FAILURE.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineController::addEditWheatDiffLine():" + e);
		}
		return returnPage;
	}
//View of wheat differential line
	@RequestMapping(value = { "/wheatDifferentialLineMasterView" })
	public String wheatDifferentialLineMasterView(Model model, SearchVo searchVo, HttpServletRequest request) {
		try {

			if ("GET".equalsIgnoreCase(request.getMethod())) {
				//searchVo.setDeleteFlag(-1);
				searchVo.setDeleteFlag(0);//Modify by debendra date-30-07-2021 view all active status
				searchVo.setIsFirstDiffLine(-1);
			}

			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.DIFF_SET_NO, WrsisPortalConstant.wrsisPropertiesFileConstants.DIFFERENTIAL_SET_MAX);
			model.addAttribute("differentialLine", wheatDifferentialLineService.viewDifferentialLine(searchVo));
			if ("GET".equalsIgnoreCase(request.getMethod())) {
				searchVo.setDeleteFlag(-1);
				//searchVo.setDeleteFlag(0);//Modify by debendra date-30-07-2021 view all active status
				searchVo.setIsFirstDiffLine(-1);
			}
			model.addAttribute("searchVo", searchVo);
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineController::wheatDifferentialLineMasterView():" + e);
		}
		return "wheatDifferentialLineMasterView";
	}

	//wheat differential line edit
	@RequestMapping(value = { "/wheatDifferentialLineMasterEdit" })
	public String wheatDifferentialLineMasterEdit(Model model, Integer wheatDifLineId) {
		try {
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.DIFF_SET_NO, WrsisPortalConstant.wrsisPropertiesFileConstants.DIFFERENTIAL_SET_MAX);
			model.addAttribute(WrsisPortalConstant.SEED_SOURCE_LIST, wheatDifferentialLineService.retriveSeedSource());
			model.addAttribute("wheatDiffLineDetails", wheatDifferentialLineService.getWheatDiffLine(wheatDifLineId));
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineController::wheatDifferentialLineMasterEdit():" + e);
		}
		return "wheatDifferentialLineMasterEdit";
	}
}
