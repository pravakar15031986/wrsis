package com.csmpl.wrsis.webportal.control;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.AdvisoryDataTable;
import com.csmpl.wrsis.webportal.bean.AdvisoryBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.repository.AdvisoryRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.service.AdvisoryService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class AdvisoryController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(AdvisoryController.class);

	@Autowired
	AdvisoryService advisoryService;
	
	@Autowired
	MailSmsContentRepository mailSmsContentRepository;
	
	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;
	
	@Value("${notification.indicator}")
	private String notificationFlag;

	@RequestMapping(value = { "/addadvisory" })
	public String addadvisory(Model model) {

		return "addadvisory";
	}

	@RequestMapping(value = { "/saveadvisory" })
	public String saveAdvisory(AdvisoryBean advDetails, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		String returnPage = "addadvisory";
		if (advDetails.getAdvisoryId() != 0) {
			returnPage = "editadvisory";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			advDetails.setCreatedBy(userId);
			advDetails.setUpdatedBy(userId);
			if (advDetails.getSummaryFromDate() == null || advDetails.getSummaryFromDate().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please choose Summary From Date");
				return returnPage;
			}
			if (advDetails.getSummaryToDate() == null || advDetails.getSummaryToDate().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please choose Summary To Date");
				return returnPage;
			}
			if (advDetails.getAdvisoryId() == 0 && advDetails.getAdvisory().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please Upload Advisory");
				return returnPage;
			}
			if (!advDetails.getAdvisory().isEmpty()) {
				if (!Validation.validateAdvisoryUpload(advDetails.getAdvisory().getOriginalFilename())) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload pdf, doc or docx file ");
					return returnPage;
				}

				long fileSize = advDetails.getAdvisory().getSize();

				if (fileSize > 5242880) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Advisory File size should be less than 5MB");
					return returnPage;
				}
			}
			String result = advisoryService.addAdvisoryDetails(advDetails);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory uploaded successfully");
				returnPage = "redirect:addadvisory";
			}
			if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory updated successfully");
				returnPage = "redirect:viewadvisory";
			}
			if (WrsisPortalConstant.FAILURE.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("AdvisoryController::saveAdvisory():" + e);

		}
		return returnPage;
	}

	@RequestMapping(value = { "/viewadvisory" })
	public String viewadvisory(Model model) {
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return "viewadvisory";
	}

	@Autowired
	AdvisoryRepository advisoryRepository;

	@GetMapping(value = { "/viewAdvisoryPageWise" })
	@ResponseBody
	public AdvisoryDataTable viewAdvisoryPageWise(Model model, HttpServletRequest request,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {
		String start = request.getParameter(WrsisPortalConstant.START);
		String lenght = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<AdvisoryBean> advList = advisoryService.viewAdvisoryPageWise(startDate, endDate, advisoryNo,
				Integer.valueOf(start), Integer.valueOf(lenght));
		Integer recordsTotal = advisoryRepository.viewAdvisoryPageCount(startDate, endDate, advisoryNo, -1, -1);
		AdvisoryDataTable adt = new AdvisoryDataTable();
		adt.setData(advList);
		adt.setRecordsTotal(recordsTotal);
		adt.setRecordsFiltered(recordsTotal);
		adt.setDraw(Integer.parseInt(draw));
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return adt;
	}

	@RequestMapping(value = { "/viewadvisorySearch" })
	public String viewadvisorySearch(Model model, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {

		model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
		model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
		model.addAttribute(WrsisPortalConstant.ADVISORY_NO, advisoryNo);
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
		return "viewadvisory";
	}

	@RequestMapping(value = { "/editadvisory" })
	public String editAdvisory(Model model, @RequestParam("advisoryId") int advisoryId) {
		try {
			model.addAttribute("advDetails", advisoryService.advDetails(advisoryId));
		} catch (Exception e) {
			LOG.error("AdvisoryController::editAdvisory():" + e);

		}
		return "editadvisory";
	}

	@RequestMapping(value = { "/deleteAdvisory" }, method = RequestMethod.POST)
	public String deleteAdvisory(Model model, @RequestParam(value = "advisoryId") int advisoryId,
			RedirectAttributes redirect, HttpServletRequest request) {
		String result = advisoryService.deleteAdvisory(request, advisoryId);
		if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result))
			redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory deleted successfully");
		else
			redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
		return "redirect:viewadvisory";
	}

	@RequestMapping(value = { "/publishAdvisory" }, method = RequestMethod.POST)
	public String publishAdvisory(Model model, @RequestParam(value = "advisoryId") int advisoryId,
			RedirectAttributes redirect, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		
		AdvisoryEntiry advisoryDtls = advisoryService.findByAdvisoryId(advisoryId);
		
		String result = advisoryService.publishAdvisory(request, advisoryId);
		
		//Added Debendra for notification date-30-0702021
		
		//if Advisory will publish then notification be send .
		
		MailSmsContentEntity mailSmsNtfc = mailSmsContentRepository.findByMscontentid(12);
		
		if(WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
			
			List<UserBean> userDetails = advisoryService.getUserDetails(userId);
			
			for (UserBean userBean : userDetails) {
			
			// For sending notification
			LOG.info("AdvisoryController::publishAdvisory():notificationFlag-" + notificationFlag);
		
				
				if (WrsisPortalConstant.YES.equalsIgnoreCase(notificationFlag)) {

					String rawNotification = mailSmsNtfc.getNotification();
					final String desktopNotification = rawNotification.replace(WrsisPortalConstant.AT_RCMN, advisoryDtls.getAdvisoryNo());
					DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
			        
					
					dashBoard.setToUserId((short)userBean.getUserId());   // It will return the value of this Integer as a short type
					dashBoard.setNotificationMsg(desktopNotification);
					dashBoard.setReadStatus(false);
					dashBoard.setStatus(false);
					dashBoard.setCreatedBy((short)userId);
					dashBoard.setCreatedOn(new Date());
					dashboardNotificationRepository.saveAndFlush(dashBoard);

					

				}
				
			}
			
			
		}
		
		
		if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result))
			redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory published successfully");
		else
			redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
		return "redirect:advisorypublished";
	}

	@ResponseBody
	@GetMapping("/advisory-file-exists")
	public String advisoryFileExists(Model model, @RequestParam("downId") Integer downId) {
		return advisoryService.advisoryFileExists(downId);
	}

	@PostMapping("/advisory-file-download")
	public void advisoryFileDownload(HttpServletResponse response, @RequestParam("downId") Integer downId) {
		advisoryService.downloadAdvisoryFile(response, downId);
	}

	@RequestMapping(value = { "/viewPublishedAdvisory" })
	public String viewPublishedAdvisory(Model model, SearchVo searchVo) {
		if ("".equalsIgnoreCase(searchVo.getAdvDtFrom()) || "".equalsIgnoreCase(searchVo.getAdvDtTo())
				|| "".equalsIgnoreCase(searchVo.getAdvNo()))
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		else
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewPublishedAdvisory";
	}

	@GetMapping(value = { "/viewPublishedAdvisoryPageWise" })
	@ResponseBody
	public AdvisoryDataTable viewPublishedAdvisoryPageWise(Model model, HttpServletRequest request,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {
		String start = request.getParameter(WrsisPortalConstant.START);
		String lenght = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		List<AdvisoryBean> advList = advisoryService.viewPublishedAdvisoryPage(startDate, endDate, advisoryNo,
				Integer.valueOf(start), Integer.valueOf(lenght));
		Integer recordsTotal = advisoryService.viewPublishedAdvisoryPageCount(startDate, endDate, advisoryNo, -1, -1);

		AdvisoryDataTable adt = new AdvisoryDataTable();
		adt.setData(advList);
		adt.setRecordsTotal(recordsTotal);
		adt.setRecordsFiltered(recordsTotal);
		adt.setDraw(Integer.parseInt(draw));
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return adt;
	}

	@RequestMapping(value = { "/advisorypending" })
	public String viewadvisoryApp(Model model) {
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return "advisorypending";
	}

	@GetMapping(value = { "/viewAdvisoryPageWiseApp" })
	@ResponseBody
	public AdvisoryDataTable viewAdvisoryPageWiseApp(Model model, HttpServletRequest request,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {
		String start = request.getParameter(WrsisPortalConstant.START);
		String lenght = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		// Use For Pending Advisor
		Integer actionStatus = 0;
		List<AdvisoryBean> advList = advisoryService.viewAdvisoryPageWiseApp(startDate, endDate, advisoryNo,
				actionStatus, Integer.valueOf(start), Integer.valueOf(lenght));
		Integer recordsTotal = advisoryRepository.viewAdvisoryPageCountApp(startDate, endDate,
				advisoryNo.trim().toUpperCase(), actionStatus, -1, -1);
		AdvisoryDataTable adt = new AdvisoryDataTable();
		adt.setData(advList);
		adt.setRecordsTotal(recordsTotal);
		adt.setRecordsFiltered(recordsTotal);
		adt.setDraw(Integer.parseInt(draw));
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return adt;
	}

	@RequestMapping(value = { "/advisorypendingSearch" })
	public String viewadvisorySearchApp(Model model, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {

		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute(WrsisPortalConstant.ADVISORY_NO, advisoryNo);
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
		return "advisorypending";
	}

	@RequestMapping(value = { "/advisorypublished" })
	public String advisoryPublished(Model model) {
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return "advisorypublished";
	}

	@GetMapping(value = { "/advisorypublishedPageWise" })
	@ResponseBody
	public AdvisoryDataTable advisoryPublished(Model model, HttpServletRequest request,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {
		String start = request.getParameter(WrsisPortalConstant.START);
		String lenght = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		// For Published
		Integer actionStatus = 1;
		List<AdvisoryBean> advList = advisoryService.viewAdvisoryPageWiseApp(startDate, endDate, advisoryNo,
				actionStatus, Integer.valueOf(start), Integer.valueOf(lenght));
		Integer recordsTotal = advisoryRepository.viewAdvisoryPageCountApp(startDate, endDate, advisoryNo, actionStatus,
				-1, -1);

		AdvisoryDataTable adt = new AdvisoryDataTable();
		adt.setData(advList);
		adt.setRecordsTotal(recordsTotal);
		adt.setRecordsFiltered(recordsTotal);
		adt.setDraw(Integer.parseInt(draw));
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, false);
		return adt;
	}

	@RequestMapping(value = { "/advisorypublishedSearch" })
	public String advisoryPublishedSearch(Model model, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam(WrsisPortalConstant.ADVISORY_NO) String advisoryNo) {

		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute(WrsisPortalConstant.ADVISORY_NO, advisoryNo);
		model.addAttribute(WrsisPortalConstant.SEARCH_SHOW, true);
		return "advisorypublished";
	}

	@RequestMapping(value = { "/editadvisoryApp" })
	public String editadvisoryApp(Model model, @RequestParam("advisoryId") int advisoryId) {
		try {
			model.addAttribute("advDetails", advisoryService.advDetails(advisoryId));
		} catch (Exception e) {
			LOG.error("AdvisoryController::editadvisoryApp():" + e);

		}
		return "editadvisoryApp";
	}

	@RequestMapping(value = { "/saveadvisoryApp" }, method = RequestMethod.POST)
	public String saveAdvisoryApp(AdvisoryBean advDetails, Model model, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {
		String returnPage = "editadvisoryApp";
		if (advDetails.getAdvisoryId() != 0) {
			returnPage = "editadvisoryApp";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			advDetails.setCreatedBy(userId);
			advDetails.setUpdatedBy(userId);
			if (advDetails.getSummaryFromDate() == null || advDetails.getSummaryFromDate().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please choose Summary From Date");
				return returnPage;
			}
			if (advDetails.getSummaryToDate() == null || advDetails.getSummaryToDate().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please choose Summary To Date");
				return returnPage;
			}
			if (advDetails.getAdvisoryId() == 0 && advDetails.getAdvisory().isEmpty()) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please Upload Advisory");
				return returnPage;
			}
			if (!advDetails.getAdvisory().isEmpty()) {
				if (!Validation.validateAdvisoryUpload(advDetails.getAdvisory().getOriginalFilename())) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload pdf, doc or docx file ");
					return returnPage;
				}

				long fileSize = advDetails.getAdvisory().getSize();

				if (fileSize > 5242880) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Advisory File size should be less than 5MB");
					return returnPage;
				}
			}
			String result = advisoryService.addAdvisoryDetails(advDetails);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory uploaded successfully");
				returnPage = "redirect:addadvisory";
			}
			if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(result)) {
				redirectAttrs.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Advisory updated successfully");
				returnPage = "redirect:advisorypending";
			}
			if (WrsisPortalConstant.FAILURE.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("AdvisoryController::saveAdvisoryApp():" + e);

		}
		return returnPage;
	}
}
