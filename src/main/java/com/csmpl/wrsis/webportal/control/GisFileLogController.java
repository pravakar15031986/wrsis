package com.csmpl.wrsis.webportal.control;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.GisBean;
import com.csmpl.wrsis.webportal.bean.GisFileLogBean;
import com.csmpl.wrsis.webportal.bean.LogBean;
import com.csmpl.wrsis.webportal.entity.MapEntity;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.GisFileLogRepository;
import com.csmpl.wrsis.webportal.service.GisFileLogService;
import com.csmpl.wrsis.webportal.service.GisService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class GisFileLogController {

	public static final Logger LOG = LoggerFactory.getLogger(GisFileLogController.class);

	@Autowired
	GisFileLogRepository gisFileLogRepository;

	@Autowired
	GisFileLogService gisFileLogService;

	@Autowired
	GisService gisService;

	@Autowired
	TypeOfRustService typeOfRustService;

	@GetMapping(value = "/viewGisFileLog")
	public String viewGisFileLog(Model model, HttpServletRequest request) {

		List<GisFileLogBean> datalist1 = null;

		try {

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now).toString();

			model.addAttribute(WrsisPortalConstant.START_DATE, tdate);
			model.addAttribute(WrsisPortalConstant.END_DATE, tdate);
			datalist1 = gisFileLogService.gisFileLogMethod();
			model.addAttribute(WrsisPortalConstant.DATA_LIST, datalist1);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

		} catch (Exception e) {
			LOG.error("GisFileLogController::viewGisFileLog():" + e);
		}

		return "viewGisFileLogg";
	}

	@PostMapping("/searchViewGisFileLog")
	public String searchGisLog(@RequestParam("uploadstartdate") Date uploadstartdate,
			@RequestParam("uploadenddate") Date uploadenddate, Model model) {

		try {
			
			SimpleDateFormat st1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			String sdate = st1.format(uploadstartdate);

			String edate = st1.format(uploadenddate);
			model.addAttribute(WrsisPortalConstant.DATA_LIST, gisFileLogService.gisFileLogMethod(uploadstartdate, uploadenddate));

			model.addAttribute(WrsisPortalConstant.START_DATE, sdate);
			model.addAttribute(WrsisPortalConstant.END_DATE, edate);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		} catch (Exception e) {
			LOG.error("GisFileLogController::searchGisLog():" + e);
		}
		return "viewGisFileLogg";

	}

	@ResponseBody
	@GetMapping("/gis-file-exist")
	public String irvFileExits(Model model, @RequestParam("fileId") Integer fileId) {
		return gisFileLogService.gisFileExits(fileId);
	}

	@PostMapping("/downloadGisFile")
	public void gisDownloadByFileName(Model m1, ModelMap model, @RequestParam("fileId") Integer fileId,
			HttpServletResponse response) throws ParseException {

		gisFileLogService.downloadGisFile(response, fileId, model);

	}

	// before search
	@GetMapping(value = "/gisForcastingFile")
	public String viewGisFileLogLOP(Model model, HttpServletRequest request) {// ,@ModelAttribute , @ModelAttribute
																				// SearchVo searchVo

		List<GisBean> datalist1 = null;

		try {

			// before search.by default today date show by fetching from system.

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			LocalDateTime now = LocalDateTime.now();
			String tdate = dtf.format(now);
			
			String edate = dtf.format(now);
			
			
			model.addAttribute(WrsisPortalConstant.START_DATE, tdate);
			model.addAttribute(WrsisPortalConstant.END_DATE, tdate);
			datalist1 = gisService.gisForcastingFile(tdate,edate);
			model.addAttribute(WrsisPortalConstant.DATA_LIST, datalist1);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

			List<TypeOfRust> rustType = typeOfRustService.vewAllTypeOFRust();
			model.addAttribute("rustList", rustType);
			List<MapEntity> mapList = gisService.viewAllActiveMap();
			model.addAttribute("mapList", mapList);

		} catch (Exception e) {
			LOG.error("GisFileLogController::viewGisFileLogLOP():" + e);
		}

		return "gisForcastingFile";
	}

	// after search
	@PostMapping(value = "/searchGisForcastingFile")
	public String searchGisForcastingFile(Model model, HttpServletRequest request,
			@RequestParam("uploadstartdate") String uploadstartdate,
			@RequestParam("uploadenddate") String uploadenddate, @RequestParam("rustTypeId") String rustTypeId,
			@RequestParam("mapTypeId") String mapTypeId, @RequestParam("status") String status) {

		List<GisBean> datalist1 = null;

		try {
			model.addAttribute(WrsisPortalConstant.START_DATE, uploadstartdate);
			model.addAttribute(WrsisPortalConstant.END_DATE, uploadenddate);

			datalist1 = gisService.gisForcastingFile(uploadstartdate, uploadenddate, rustTypeId, mapTypeId, status);

			model.addAttribute(WrsisPortalConstant.DATA_LIST, datalist1);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

			List<TypeOfRust> rustType = typeOfRustService.vewAllTypeOFRust();
			model.addAttribute("rustList", rustType);
			List<MapEntity> mapList = gisService.viewAllActiveMap();
			model.addAttribute("mapList", mapList);

			model.addAttribute("rustTypeId", rustTypeId);
			model.addAttribute("mapTypeId", mapTypeId);
			model.addAttribute("statusId", status);

		} catch (Exception e) {
			LOG.error("GisFileLogController::searchGisForcastingFile():" + e);
		}

		return "gisForcastingFile";

	}

	// View Log File

	@RequestMapping(value = "/viewLogFile")
	public String viewLogFile(Model model, HttpServletRequest request) {
		JSONArray years = new JSONArray();
		years = DateUtil.getYearList();
		model.addAttribute(WrsisPortalConstant.YEAR_LIST, new String(Base64.getEncoder().encode(years.toString().getBytes())));
		List<String> monthList=DateUtil.getMonthList();
		model.addAttribute("monthList",monthList);
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		
		Calendar cal = Calendar.getInstance(); 
		Date d = new Date(cal.getTimeInMillis());

		if(year==null)
		{
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		if(month==null)
		{
			month=new SimpleDateFormat("MMM").format(d);
		}
		String startDate,endDate;
		String lastDate;
	      
	      String DATE_PATTERN = "MMM/yyyy";
	      DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_PATTERN);
	      YearMonth yearMonth = YearMonth.parse(month+"/"+year, pattern);
	      LocalDate date = yearMonth.atEndOfMonth();
	      
	      
		List<LogBean> datalist1 = null;

		try {
			lastDate= String.valueOf(date.lengthOfMonth());
		      startDate="01-"+month+"-"+year;
		      endDate=lastDate+"-"+month+"-"+year;
		      
			datalist1 = gisFileLogService.viewLogFile(logFileDirectory, DateUtil.StringMMMToDate(startDate),
					DateUtil.StringMMMToDate(endDate));
			model.addAttribute(WrsisPortalConstant.DATA_LIST, datalist1);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			model.addAttribute("selectedYear",year);
			model.addAttribute("selectedMonth",month);
		} catch (Exception e) {
			LOG.error("GisFileLogController::viewLogFile():" + e);
		}

		return "viewLogFile";

	}

	@Value("${logfile.directory}")
	private String logFileDirectory;

	@RequestMapping(value = "/public/downloadLogFile", method = RequestMethod.GET)
	public ResponseEntity<?> downloadLogFile(HttpServletResponse response,
			@RequestParam(value = WrsisPortalConstant.FILE_NAME, required = true) String fileName) throws IOException {
		Path path = Paths.get(logFileDirectory + fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			LOG.error("GisFileLogController::downloadLogFile():" + e);		}
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

}
