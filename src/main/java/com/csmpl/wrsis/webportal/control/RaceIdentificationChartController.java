package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.RaceChartHelperBean;
import com.csmpl.wrsis.webportal.entity.RaceIdentificationChart;
import com.csmpl.wrsis.webportal.service.RaceIdentificationChartService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class RaceIdentificationChartController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(RaceIdentificationChartController.class);
	@Autowired
	RaceIdentificationChartService raceChartService;

	@RequestMapping(value = { "/raceIdentificationChart" }, method = RequestMethod.GET)
	public String raceIdentificationChart(Model model, @ModelAttribute(WrsisPortalConstant.RACE) RaceIdentificationChart raceChart) {
		return "raceIdentificationChart";
	}

	@PostMapping("insert-race-chart")
	public String saveAndUpdateRaceChart(@ModelAttribute(WrsisPortalConstant.RACE) RaceIdentificationChart raceChart, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {
		
		String returnpage = "raceIdentificationChart";
		if (raceChart.getRaceChartId() != 0) {
			returnpage = "raceIdentificationChartEdit";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			model.addAttribute(WrsisPortalConstant.RACE, raceChart);
			if (!raceChart.getNameResult().isEmpty()) {

				if (!Validation.validateRaceName(raceChart.getNameResult())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.NAME_ID);
					return returnpage;
				}
				if (!Validation.validateFirstBlankSpace(raceChart.getNameResult())
						|| !Validation.validateLastBlankSpace(raceChart.getNameResult())) {
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.NAME_ID);
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "White space is not allowed at initial and last place in Name");
					return returnpage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(raceChart.getNameResult())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.NAME_ID);
					return returnpage;
				}
			}
			if (!raceChart.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(raceChart.getDescription())
						|| !Validation.validateLastBlankSpace(raceChart.getDescription())) {
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					return returnpage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(raceChart.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnpage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(raceChart.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnpage;
				}
				if (!Validation.validateUserAddress(raceChart.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnpage;
				}
			}
			raceChart.setCreatedBy(userId);
			ArrayList<RaceChartHelperBean> res = raceChartService.sequenceISExit(raceChart);
			
			if (res != null && res.size() > 0) {
				for (RaceChartHelperBean status : res) {
					
					if (status.getResult().equals(WrsisPortalConstant.EXIST)) {

						

						if (raceChart.getRaceChartId() == 0) {
							/* This if use if the sequence is deactive or status is ture Start */
							if (status.getRaceStatusBean().equals(true)) {

								String msg = raceChartService.saveAndUpdateRaceChart(raceChart);
								if (msg.equals(WrsisPortalConstant.SAVE)) {
									model.addAttribute(WrsisPortalConstant.RACE, new RaceIdentificationChart());
									model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.DATA_SAVED_SUCCESSFULLY);
								}
								if (msg.equals(WrsisPortalConstant.UPDATE)) {
									model.addAttribute(WrsisPortalConstant.RACE, new RaceIdentificationChart());
									redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
									returnpage = "redirect:raceIdentificationChartView";
								}
								if (msg.equals(WrsisPortalConstant.FAILURE)) {
									model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.TRY_AGAIN);
								}
								break;
							}
							/* This if use if the sequence is deactive or status is ture End */
							else {
								
								model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This sequence is alredy exist");
								break;
							}
						} else {
							/* This else block use for Edit Case Start */

							if (status.getRaceHelperId() == raceChart.getRaceChartId()) {

								String msg = raceChartService.saveAndUpdateRaceChart(raceChart);
								if (msg.equals(WrsisPortalConstant.SAVE)) {
									model.addAttribute(WrsisPortalConstant.RACE, new RaceIdentificationChart());
									model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.DATA_SAVED_SUCCESSFULLY);
								}
								if (msg.equals(WrsisPortalConstant.UPDATE)) {
									model.addAttribute(WrsisPortalConstant.RACE, new RaceIdentificationChart());
									redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
									returnpage = "redirect:raceIdentificationChartView";
								}
								if (msg.equals(WrsisPortalConstant.FAILURE)) {
									model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.TRY_AGAIN);
								}

								break;

							} else {
								model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This sequence is alredy exist");

							}
						}
					}
				}
			} else {
				boolean nameStatus = raceChartService.chartNameISExit(raceChart.getNameResult());
				if (!nameStatus ) {
					raceChart.setNameResult(raceChart.getNameResult().toUpperCase().trim());
					String msg = raceChartService.saveAndUpdateRaceChart(raceChart);
					if (msg.equals(WrsisPortalConstant.SAVE)) {
						model.addAttribute(WrsisPortalConstant.RACE, new RaceIdentificationChart());
						model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.DATA_SAVED_SUCCESSFULLY);
					}
					if (msg.equals(WrsisPortalConstant.FAILURE)) {
						model.addAttribute(WrsisPortalConstant.ERROR_MSG, WrsisPortalConstant.TRY_AGAIN);
					}
				} else {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Name is alredy exist");
				}
			}
		} catch (Exception e) {

			LOG.error("RaceIdentificationChartController::saveAndUpdateRaceChart():" + e);
		}

		return returnpage;
	}

	@RequestMapping(value = { "/raceIdentificationChartView" })
	public String raceIdentificationChartView(Model model) {
		return "raceIdentificationChartView";
	}

	@RequestMapping(value = { "/raceIdentificationChartEdit" }, method = RequestMethod.POST)
	public String raceIdentificationChartEdit(Model model, @RequestParam("raceChartId") Integer raceChartId) {

		RaceIdentificationChart raceChart = raceChartService.getByRaceChartId(raceChartId);
		model.addAttribute(WrsisPortalConstant.RACE, raceChart);
		return "raceIdentificationChartEdit";
	}

	@ResponseBody
	@GetMapping("/viewAllraceIdentificationChartPage")
	public String viewAllFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return raceChartService.viewRaceIdentificationChartByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/search-raceIdentificationChart")
	public String searchGrowthStage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("stageName"));
		return raceChartService.searchviewRaceIdentificationChart(name, status, pageSize, pageNumber, pageable);
	}
}
