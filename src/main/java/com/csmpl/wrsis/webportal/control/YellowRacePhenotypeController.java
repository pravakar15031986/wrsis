package com.csmpl.wrsis.webportal.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.YellowRacePhenotypeEntity;
import com.csmpl.wrsis.webportal.service.YellowRacePhenotypeService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class YellowRacePhenotypeController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(YellowRacePhenotypeController.class);

	@Autowired
	YellowRacePhenotypeService yellowRacePhenotypeService;

	@RequestMapping(value = { "/addYellowRacePhenotype" })
	public String addYellowRacePhenotype(Model model,
			@ModelAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE) YellowRacePhenotypeEntity yellowRacePhenotype) {
		{
			model.addAttribute(WrsisPortalConstant.GROUP, yellowRacePhenotypeService.getGroupList());
		}
		return "addYellowRacePhenotype";
	}

	@RequestMapping(value = { "/saveYellowRacePhenotype" })
	public String saveYellowRacePhenotype(Model model,
			@ModelAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE) YellowRacePhenotypeEntity yellowRacePhenotype,
			HttpServletRequest request, RedirectAttributes redirect) {
		String rtn = "addYellowRacePhenotype";
		model.addAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE, yellowRacePhenotype);
		model.addAttribute(WrsisPortalConstant.GROUP, yellowRacePhenotypeService.getGroupList());
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
		try {
			if (yellowRacePhenotype.getRacePhenotypeId() != null) {
				rtn = "editYellowRacePhenotype";
				yellowRacePhenotype.setUpdatedBy(userId);
			} else {
				yellowRacePhenotype.setCreatedBy(userId);
			}

			if (yellowRacePhenotype.getRaceName().isEmpty() || yellowRacePhenotype.getRaceName() == "") {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Race Name");
				return rtn;
			}
			if (yellowRacePhenotype.getVirulencePhenotype().isEmpty()
					|| yellowRacePhenotype.getVirulencePhenotype() == "") {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Genetic Group");
				return rtn;
			}
			if (yellowRacePhenotype.getStatus() != false && yellowRacePhenotype.getStatus() != true) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Status");
				return rtn;
			}
			if (!Validation.validateRace(yellowRacePhenotype.getRaceName())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Race Name accept alphabets,numbers and (),_ characters");
				return rtn;
			}
			if (!Validation.validateVirulencePhenotype(yellowRacePhenotype.getVirulencePhenotype())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"Virulence Phenotype accept only alphabets,numbers and (),- characters");
				return rtn;
			}
			String sts = yellowRacePhenotypeService.saveYellowracePhenotype(yellowRacePhenotype);
			if ("raceexist".equals(sts)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Race Name already exists");
				return rtn;
			}
			if ("vphenotypeexists".equals(sts)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Virulence Phenotype already exists");
				return rtn;
			}
			if (WrsisPortalConstant.SUCCESS.equals(sts)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Yellow Race Phenotype submitted successfully");
				model.addAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE, new YellowRacePhenotypeEntity());
				return rtn;
			}
			if (WrsisPortalConstant.UPDATE.equals(sts)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Yellow Race Phenotype updated successfully");
				rtn = "redirect:viewYellowRacePhenotype";
			}
			if (WrsisPortalConstant.FAILURE.equals(sts)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
				return rtn;
			}
		} catch (Exception e) {
			LOG.error("YellowRacePhenotypeController::saveYellowRacePhenotype():" + e);
		}

		return rtn;
	}

	@RequestMapping(value = { "/viewYellowRacePhenotype" })
	public String viewYellowRacePhenotype(Model model, HttpServletRequest request, RedirectAttributes redirect,
			SearchVo searchvo) {
		model.addAttribute(WrsisPortalConstant.GROUP, yellowRacePhenotypeService.getGroupList());
		model.addAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE, yellowRacePhenotypeService.viewYellowRacePhenotype(searchvo));
		return "viewYellowRacePhenotype";
	}

	@RequestMapping(value = { "/editYellowRacePhenotype" })
	public String editYellowRacePhenotype(Model model,
			@RequestParam(value = "racePhenotypeId") Integer racePhenotypeId) {
		{
			YellowRacePhenotypeEntity yellowRacePhenotype = yellowRacePhenotypeService
					.getRacePhenotypeId(racePhenotypeId);
			model.addAttribute(WrsisPortalConstant.GROUP, yellowRacePhenotypeService.getGroupList());
			model.addAttribute(WrsisPortalConstant.YELLOW_RACE_PHENOTYPE, yellowRacePhenotype);
		}
		return "editYellowRacePhenotype";
	}
}
