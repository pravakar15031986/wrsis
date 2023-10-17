package com.csmpl.wrsis.webportal.control;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.RaceScoringMaster;
import com.csmpl.wrsis.webportal.service.RaceScoringMasterService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class RaceScoringMasterController extends WrsisPortalAbstractController {
	public static final Logger LOG = LoggerFactory.getLogger(RaceScoringMasterController.class);
	@Autowired
	TypeOfRustService typeOfRustService;
	@Autowired
	RaceScoringMasterService raceScoringMasterService;

	@GetMapping("/raceScoringMaster")
	public String raceScoring(@ModelAttribute(WrsisPortalConstant.RACE) RaceScoringMaster race, Model model) {
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		return "raceScoringMaster";
	}

	@PostMapping("/insert-race-Scroing")
	public String addAndUpdateraceScoring(@ModelAttribute(WrsisPortalConstant.RACE) RaceScoringMaster race, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		String returnPage = null;
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			race.setCreatedBy(userId);
			boolean res = false;

			if (!Validation.validateInfectionsType(race.getInfectionType())) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter valid infection type.");
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "infectionId");
				returnPage = "raceScoringMaster";
			}

			res = raceScoringMasterService.infectionTypeExits(race.getInfectionType(), race.getRaceScoreId(),
					race.getRustId().getRustId());

			String msg = null;
			if (!res)
				msg = raceScoringMasterService.addAndUpdateRaceScroing(race);
			else {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Infection type already exists");
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
				returnPage = "raceScoringMaster";
			}
			if (msg != null) {
				if (WrsisPortalConstant.SAVE.equalsIgnoreCase(msg)) {
					model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
					model.addAttribute(WrsisPortalConstant.RACE, new RaceScoringMaster());
					model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Saved Successfully");
					returnPage = "raceScoringMaster";
				}
				if (msg.equalsIgnoreCase(WrsisPortalConstant.UPDATE)) {
					redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
					returnPage = "redirect:raceScoringMasterView";
				}
			}
		} catch (Exception e) {

			LOG.error("RaceScoringMasterController::addAndUpdateraceScoring():" + e);
		}

		return returnPage;
	}

	@GetMapping("/raceScoringMasterView")
	public String raceScoringMasterView(Model model, @ModelAttribute("searchVo") SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.RACE, raceScoringMasterService.viewAllScor());
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		return "raceScoringMasterView";
	}

	@PostMapping("/raceScoringMasterEdit")
	public String raceScoringMasterEdit(Model model, @RequestParam("raceScoreId") Integer raceScoreId,
			@ModelAttribute(WrsisPortalConstant.RACE) RaceScoringMaster race) {
		race = raceScoringMasterService.getRaceScorById(raceScoreId);
		LOG.info(WrsisPortalConstant.SUCCESS_MSG+race.getRustId().getRustId());
		model.addAttribute(WrsisPortalConstant.RACE, race);
		model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
		return "raceScoringMasterEdit";
	}

	@PostMapping("/raceScoringMasterViewSearch")
	public String raceScoringMasterViewSearch(Model model, @ModelAttribute("searchVo") SearchVo searchVo)

	{

		try {
			List<RaceScoringMaster> race = raceScoringMasterService.searchViewAllScor(searchVo.getRustId(),
					searchVo.getHlId(), searchVo.getStatus());
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustService.vewAllTypeOFRust());
			model.addAttribute(WrsisPortalConstant.RACE, race);
		} catch (Exception e) {
			LOG.error("RaceScoringMasterController::raceScoringMasterViewSearch():" + e);
		}

		return "raceScoringMasterView";
	}
}
