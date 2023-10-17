package com.csmpl.wrsis.webportal.control;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.MobileLogDataTable;
import com.csmpl.wrsis.datatable.QuerylogDataTable;
import com.csmpl.wrsis.webportal.bean.MobileLogViewBean;
import com.csmpl.wrsis.webportal.bean.QueryLogViewBean;
import com.csmpl.wrsis.webportal.repository.MobileLogRepository;
import com.csmpl.wrsis.webportal.service.MobileLogService;
import com.csmpl.wrsis.webportal.service.QueryBuilderLogService;

@Controller
public class MobileLogController {

	public static final Logger LOG = LoggerFactory.getLogger(MobileLogController.class);

	@Autowired
	MobileLogRepository mobileLogRepository;
	@Autowired
	MobileLogService mobileLogService;
	@Autowired
	QueryBuilderLogService queryLogService;

	@RequestMapping(value = { "/viewMobileLogDetails" })
	public String viewMobileLogDetails(Model model, HttpServletRequest request, @ModelAttribute SearchVo searchVo) {

		if (searchVo.getStartDate() == null && searchVo.getEndDate() == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now);
			searchVo.setStartDate(tdate);
			searchVo.setEndDate(tdate);
		}


		

		return "mobileLogView";
	}

	/*----------------For Dynamic Pagination(Start)---------------*/

	@RequestMapping(value = { "/viewSucessMobilelogData" })
	public @ResponseBody MobileLogDataTable viewMobileDetails(Model model, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<MobileLogViewBean> beans = mobileLogService.getDataList(startDate, endDate, Integer.valueOf(start),
				Integer.valueOf(length));

		Integer totalCount = mobileLogService.getDataListCount(startDate, endDate, -1, -1);

		MobileLogDataTable mldt = new MobileLogDataTable();
		mldt.setData(beans);
		mldt.setRecordsFiltered(totalCount);
		mldt.setRecordsTotal(totalCount);
		mldt.setDraw(Integer.parseInt(draw));

		return mldt;
	}

	/*----------------For Dynamic Pagination(End)---------------*/
	@RequestMapping("/modalrequestdetails")
	public ResponseEntity<List<SearchVo>> modalrequestDetails() {
		return null;

	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getUserReqInfo")
	public String getUserReqInfo(HttpServletRequest request) throws JSONException {
		int logId = Integer.parseInt(request.getParameter(WrsisPortalConstant.LOG_ID));
		return mobileLogService.getRequestResponse(logId);
	}

	@ResponseBody // Ajax method
	@PostMapping(value = "/getUserResInfo")
	public String getUserResInfo(HttpServletRequest request) throws JSONException {

		int logId = Integer.parseInt(request.getParameter(WrsisPortalConstant.LOG_ID));
		return mobileLogService.getResponse(logId);
	}

	/* ----------------------Query builder Log ----------------------- */

	@RequestMapping(value = { "/viewQueryBuilderLog" })
	public String viewQueryBuilderLog(Model model, HttpServletRequest request, @ModelAttribute SearchVo searchVo) {

		if (searchVo.getStartDate() == null && searchVo.getEndDate() == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now);
			searchVo.setStartDate(tdate);
			searchVo.setEndDate(tdate);
		}
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		

		return "viewQueryBuilderLog";
	}

	/*----------------For Dynamic Pagination(Start)---------------*/

	@RequestMapping(value = { "/viewQueryLogDataPage" })
	public @ResponseBody QuerylogDataTable viewQueryLogDetails(Model model, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<QueryLogViewBean> beans = queryLogService.getQueryList(startDate, endDate, Integer.valueOf(start),
				Integer.valueOf(length));

		Integer totalCount = queryLogService.getQueryListCount(startDate, endDate, -1, -1);

		QuerylogDataTable qldt = new QuerylogDataTable();
		qldt.setData(beans);
		qldt.setRecordsFiltered(totalCount);
		qldt.setRecordsTotal(totalCount);
		qldt.setDraw(Integer.parseInt(draw));

		return qldt;
	}

	@ResponseBody // Ajax method
	@RequestMapping(value = "/getUserQueryInfo")
	public String getQueryInfo(HttpServletRequest request) throws JSONException {
		int logId = Integer.parseInt(request.getParameter(WrsisPortalConstant.LOG_ID));
		return queryLogService.getQueryResponse(logId);
	}
	// before search today data show
	

	@RequestMapping(value = "/viewUKMetAndGlobalRustAPILog")
	public String viewGisFileLog(@ModelAttribute("searchVo") SearchVo searchVo, Model model,
			HttpServletRequest request) {


		List<MobileLogViewBean> datalist1 = null;

		try {

			if (searchVo.getStartDate() == null && searchVo.getEndDate() == null) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				LocalDateTime now = LocalDateTime.now();
				searchVo.setStartDate(dtf.format(now));
				searchVo.setEndDate(searchVo.getStartDate());

				
			}

			model.addAttribute("searchVo", searchVo);
			
			datalist1 = mobileLogService.getUKMetAndGlobalRustAPILog(searchVo.getStartDate(), searchVo.getEndDate());
			model.addAttribute("dataList", datalist1);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

		} catch (Exception e) {
			LOG.error("MobileLogController::viewGisFileLog():" + e);
		}

		return "viewUKMetAndSurvey";
	}

	// after search

	@PostMapping("/searchviewUKMetAndSurvey")
	public String searchGisLog(@RequestParam(WrsisPortalConstant.START_DATE) Date uploadstartdate,
			@RequestParam(WrsisPortalConstant.END_DATE) Date uploadenddate, Model model) {
		
		try {

			SimpleDateFormat st1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			String sdate = st1.format(uploadstartdate);

			String edate = st1.format(uploadenddate);
			

			model.addAttribute(WrsisPortalConstant.START_DATE, sdate);
			model.addAttribute(WrsisPortalConstant.END_DATE, edate);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception e) {
			LOG.error("MobileLogController::searchGisLog():" + e);
		}
		return "viewUKMetAndSurvey";

	}

	/* ----------------------/Query builder Log ----------------------- */
}
