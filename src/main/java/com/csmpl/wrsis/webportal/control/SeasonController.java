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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SeasonMonthBean;
import com.csmpl.wrsis.webportal.entity.Month;
import com.csmpl.wrsis.webportal.service.MonthService;
import com.csmpl.wrsis.webportal.service.SeasonMasterService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class SeasonController extends WrsisPortalAbstractController {
	private static final Logger LOG = LoggerFactory.getLogger(SeasonController.class);

	@Autowired
	MonthService monthService;
	@Autowired
	SeasonMasterService seasonMasterService;

	@GetMapping("/seasonMaster")
	public String seasonMaster(Model model, @ModelAttribute("seasonBean") SeasonMonthBean seasonMonthBean) {
		try {
			List<Month> list = monthService.viewAllMonth();
			model.addAttribute(WrsisPortalConstant.MONTH_LIST, list);
		} catch (Exception e) {
			LOG.error("SeasonController::seasonMaster():" + e);
		}

		return "seasonMaster";
	}

	@PostMapping("/save-season-master")
	public String saveAndUpdateSeason(Model model, @RequestParam("seasonName") String seasonName,
			@RequestParam("monthId") String[] monthId, @RequestParam("desc") String desc,
			@RequestParam("status") Boolean status, HttpServletRequest request, RedirectAttributes redirect,
			@RequestParam(value = "seasonIdBean", required = false) Integer seasonIdBean) {
		
		String returnPage = "seasonMaster";
		try {
			SeasonMonthBean seasonMonthBean = new SeasonMonthBean();

			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			seasonMonthBean.setCreatedBy(userId);
			seasonMonthBean.setSeasonName(seasonName);
			seasonMonthBean.setMonthId(monthId);
			seasonMonthBean.setDesc(desc);
			seasonMonthBean.setStatus(status);

			List<Month> list = monthService.viewAllMonth();
			model.addAttribute(WrsisPortalConstant.MONTH_LIST, list);
			if (seasonIdBean != null) {
				seasonMonthBean.setSeasonIdBean(seasonIdBean);
				returnPage = "seasonMasterEdit";
			}
			if (!seasonMonthBean.getSeasonName().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(seasonMonthBean.getSeasonName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Season Name accept only alphabets.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SEASON_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(seasonMonthBean.getSeasonName())
						|| !Validation.validateLastBlankSpace(seasonMonthBean.getSeasonName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in Season Name");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SEASON_TEXT_INPUT);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(seasonMonthBean.getSeasonName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Season Name should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.SEASON_TEXT_INPUT);
					return returnPage;
				}
			}
			if (!seasonMonthBean.getDesc().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(seasonMonthBean.getDesc())
						|| !Validation.validateLastBlankSpace(seasonMonthBean.getDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(seasonMonthBean.getDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(seasonMonthBean.getDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in Description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
				if (!Validation.validateUserAddress(seasonMonthBean.getDesc())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "descriptionID");
					return returnPage;
				}
			}

			String msg = null;
			boolean sm = seasonMasterService.isExistSeasoName(seasonIdBean, seasonMonthBean.getSeasonName());
			if (sm) {

				if (seasonIdBean != null) {

					SeasonMonthBean season = seasonMasterService.getSeasonById(seasonIdBean);
					List<SeasonMonthBean> seasonM = seasonMasterService.getMonthBySeasonId(season.getSeasonIdBean());
					for (SeasonMonthBean seasonMonBean : seasonM) {
						model.addAttribute(WrsisPortalConstant.SEASON_MONTH_ID, seasonMonBean.getMonthBeanId());
					}

					model.addAttribute(WrsisPortalConstant.SEASON, season);
					List<Month> Mlist = monthService.viewAllMonth();
					model.addAttribute(WrsisPortalConstant.MONTH_LIST, Mlist);
					
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Season name already exists");
					return returnPage;
				}
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Season name already exists");
			} else {

				boolean mon = seasonMasterService.duplicateExistSeasoMonth(seasonIdBean, seasonMonthBean.getMonthId());
				if (mon) {
					if (seasonIdBean != null) {

						SeasonMonthBean season = seasonMasterService.getSeasonById(seasonIdBean);
						List<SeasonMonthBean> seasonM = seasonMasterService
								.getMonthBySeasonId(season.getSeasonIdBean());
						for (SeasonMonthBean seasonMonBean : seasonM) {
							model.addAttribute(WrsisPortalConstant.SEASON_MONTH_ID, seasonMonBean.getMonthBeanId());
						}

						model.addAttribute(WrsisPortalConstant.SEASON, season);
						List<Month> Mlist = monthService.viewAllMonth();
						model.addAttribute(WrsisPortalConstant.MONTH_LIST, Mlist);
						
						model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Month already exists in another season");
						return returnPage;
					}
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Month already exists in another season");
				} else {
					msg = seasonMasterService.saveAndUpdateSeasonMaster(seasonMonthBean);
				}
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "This Season cannot be deactive. Already in use somewhere.");
			}
			if (msg.equals(WrsisPortalConstant.SAVE)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Save Successfully");
			}
			if (msg.equals(WrsisPortalConstant.UPDATE)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				returnPage = "redirect:seasonMasterView";
			}
			if (msg.equals(WrsisPortalConstant.FAILURE)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}

		} catch (Exception e) {
			LOG.error("SeasonController::saveAndUpdateSeason():" + e);
		}
		return returnPage;
	}

	@RequestMapping(value = { "/seasonMasterView" }, method = RequestMethod.GET)
	public String seasonMasterView(Model model) {
		List<Month> list = monthService.viewAllMonth();
		model.addAttribute(WrsisPortalConstant.MONTH_LIST, list);
		return "seasonMasterView";
	}

	@PostMapping("/seasonMasterEdit")
	public String seasonMasterEdit(Model model, @RequestParam("seasonIdBean") Integer seasonIdBean) {
		try {
			SeasonMonthBean season = seasonMasterService.getSeasonById(seasonIdBean);
			List<SeasonMonthBean> seasonM = seasonMasterService.getMonthBySeasonId(season.getSeasonIdBean());

			for (SeasonMonthBean seasonMonthBean : seasonM) {
				model.addAttribute(WrsisPortalConstant.SEASON_MONTH_ID, seasonMonthBean.getMonthBeanId());
			}

			model.addAttribute(WrsisPortalConstant.SEASON, season);
			List<Month> list = monthService.viewAllMonth();
			model.addAttribute(WrsisPortalConstant.MONTH_LIST, list);

		} catch (Exception e) {
			LOG.error("SeasonController::seasonMasterEdit():" + e);
		}
		return "seasonMasterEdit";
	}

	@ResponseBody
	@GetMapping("/viewAllSeasonPage")
	public String viewAllFungicide(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);

		return seasonMasterService.viewSeasonByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/search-Season")
	public String searchGrowthStage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "monthId", required = false) String monthId) {
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("stageName"));
		if (!monthId.equals("select")) {
			return seasonMasterService.searchSeasonByMonthIdPage(monthId, pageSize, pageNumber, pageable);
		} else {
			return seasonMasterService.searchseasonPage(name, monthId, status, pageSize, pageNumber, pageable);
		}
	}
}
