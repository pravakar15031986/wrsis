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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.VarietyBean;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.service.VarietyService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class VarietyController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(VarietyController.class);

	@Autowired
	VarietyService varietyService;

	@RequestMapping(value = "/addVariety", method = RequestMethod.GET)
	public String addVariety(Model model) {

		model.addAttribute(WrsisPortalConstant.VARIETY, new VarietyBean());
		return "addVariety";
	}

	@RequestMapping(value = "/saveVariety", method = RequestMethod.POST)
	public String saveVariety(@ModelAttribute(WrsisPortalConstant.VARIETY) VarietyBean variety, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		String returnPage = "addVariety";
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		if (variety.getVarietyTypeId() != 0) {
			returnPage = "editVariety";
			variety.setUpdatedBy(userId);
		} else
			variety.setCreatedBy(userId);
		try {

			// Server Side Validation Start

			if (variety.getVchDesc() == null || variety.getVchDesc().isEmpty()
					|| "".equalsIgnoreCase(variety.getVchDesc())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Variety Name Should Not Be Blank");
				return returnPage;
			}
			if (!variety.getVchDesc().isEmpty()) {
				if (!Validation.validateDemographyName(variety.getVchDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Variety Name accept only alphabets and numbers.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.VARIETY_NAME_ID);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(variety.getVchDesc())
						|| !Validation.validateLastBlankSpace(variety.getVchDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Variety Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.VARIETY_NAME_ID);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(variety.getVchDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Variety Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.VARIETY_NAME_ID);
					return returnPage;
				}
				
				
			}

			String result = varietyService.addvariety(variety);
			if (WrsisPortalConstant.EXIST.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Variety already exists");
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Variety cannot be deactive. Already in use somewhere.");
			}
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.VARIETY, new VarietyBean());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Variety added successfully");
			}
			if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Variety updated successfully");
				return "redirect:viewVariety";
			}
			if (WrsisPortalConstant.FAILURE.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("VarietyController::saveVariety():" + e);
		}
		return returnPage;
	}

	@RequestMapping(value = "/viewVariety")
	public String viewVariety(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute("statusList", getStatus());
			model.addAttribute("varietyList", varietyService.viewVariety(searchVo));

		} catch (Exception e) {
			LOG.error("VarietyController::viewVariety():" + e);
		}
		return "viewVariety";
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
			LOG.error("VarietyController::getStatus():" + e);
		}

		return statusList;
	}

	@RequestMapping(value = "/editVariety", method = RequestMethod.POST)
	public String editVariety(@RequestParam("varietyTypeId") Integer id, Model model) {
		try {

			VarietyTypeMasterEntity vObj = null;
			vObj = varietyService.getVarietyById(id);

			model.addAttribute(WrsisPortalConstant.VARIETY, vObj);

		} catch (Exception e) {
			LOG.error("VarietyController::editVariety():" + e);
		}
		return "editVariety";
	}

}
