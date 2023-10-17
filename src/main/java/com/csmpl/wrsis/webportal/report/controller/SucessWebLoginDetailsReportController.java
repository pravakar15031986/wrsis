package com.csmpl.wrsis.webportal.report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.SucessMobLoginDetailsPDF;
import com.csmpl.wrsis.pdf.SucessWebloginDetailsPDF;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.excel.MobLoginDetailsExcel;
import com.csmpl.wrsis.webportal.service.LoginAuditTrailService;

@Controller
public class SucessWebLoginDetailsReportController extends WrsisPortalAbstractController {

	@Autowired
	LoginAuditTrailService loginAuditTrailService;

	// Excel ReportDownload for SucessWebLoginDetailsReport

	@RequestMapping("/downloadSucessWebLoginDetailsExcel")
	public void sucessWebLoginDetailsReportDownloadExcel(HttpServletResponse response, HttpServletRequest request,
			Model model, HttpSession session, @RequestParam("organizationIdExcel") String organizationId,
			@RequestParam(WrsisPortalConstant.ROLE_ID_EXCEL) String roleId,
			@RequestParam(WrsisPortalConstant.RESEARCH_CENTER_ID_EXCEL) String researchCenterId,
			@RequestParam(WrsisPortalConstant.INT_DESIG_ID_EXCEL) String intdesigid,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate,
			@RequestParam("fullNameExcel") String fullName, @RequestParam("statusExcel") String logStatus)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		DownloadSampleExcel surveyExcel = new DownloadSampleExcel();

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessWebLoginDetails(Integer.valueOf(organizationId),
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, -1, -1);

		HSSFWorkbook excelBook = surveyExcel.sucessWebloginReportDownload(beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Weblogin_Details_" + formattedDate + ".xls");
		OutputStream out = response.getOutputStream();
		excelBook.write(out);
		out.flush();
		out.close();
		excelBook.close();

	}

	// PDF ReportDownload for SucessWebLoginDetailsReport

	@RequestMapping(value = { "/downloadSucessWebLoginDetailsPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> sucessWebLoginDetailsReportDownloadPdf(HttpServletResponse response,
			HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("organizationIdExcel") String organizationId,
			@RequestParam(WrsisPortalConstant.ROLE_ID_EXCEL) String roleId,
			@RequestParam(WrsisPortalConstant.RESEARCH_CENTER_ID_EXCEL) String researchCenterId,
			@RequestParam(WrsisPortalConstant.INT_DESIG_ID_EXCEL) String intdesigid,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate,
			@RequestParam("fullNameExcel") String fullName, @RequestParam("statusExcel") String logStatus)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessWebLoginDetails(Integer.valueOf(organizationId),
				Integer.valueOf(roleId), Integer.valueOf(researchCenterId), Integer.valueOf(intdesigid), startDate,
				endDate, fullName.trim().toUpperCase(), userId, logStatus, -1, -1);

		SucessWebloginDetailsPDF pdf = new SucessWebloginDetailsPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = pdf.generateSucessWebloginDetailsPDF(beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
		String filename = "Weblogin_Details_" + formattedDate + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	@RequestMapping("/mobileLoginExcel")
	public void downloadSucessMobLoginDetailsExcel(@RequestParam("organizationIdExcel") Integer organizationIdExcel,
			@RequestParam(WrsisPortalConstant.ROLE_ID_EXCEL) Integer roleIdExcel,
			@RequestParam(WrsisPortalConstant.RESEARCH_CENTER_ID_EXCEL) Integer researchCenterIdExcel,
			@RequestParam(WrsisPortalConstant.INT_DESIG_ID_EXCEL) Integer intdesigidExcel,
			@RequestParam("startDateExcel") String startDateExcel, @RequestParam("endDateExcel") String endDateExcel,
			@RequestParam("fullNameExcel") String fullNameExcel, @RequestParam("statusIdExcel") String statusIdExcel,
			HttpServletResponse response, HttpSession session) throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		MobLoginDetailsExcel mobileExcel = new MobLoginDetailsExcel();

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessMobLoginDetails(organizationIdExcel, roleIdExcel,
				researchCenterIdExcel, intdesigidExcel, startDateExcel, endDateExcel,
				fullNameExcel.trim().toUpperCase(), userId, statusIdExcel, -1, -1);

		HSSFWorkbook excelBook = mobileExcel.buildingMobLogindDetails(beans);

		response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);

		Date date = new Date();
		long time = date.getTime();

		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=Mobile_Login_Details_" + timeStamp + ".xls");

		OutputStream out = response.getOutputStream();
		excelBook.write(out);

		out.flush();
		out.close();
		excelBook.close();

	}

	@RequestMapping(value = { "/mobileLoginPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> mobileLoginPdf(HttpServletResponse response, HttpServletRequest request,
			Model model, HttpSession session, @RequestParam("organizationIdExcel") Integer organizationIdExcel,
			@RequestParam(WrsisPortalConstant.ROLE_ID_EXCEL) Integer roleIdExcel,
			@RequestParam(WrsisPortalConstant.RESEARCH_CENTER_ID_EXCEL) Integer researchCenterIdExcel,
			@RequestParam(WrsisPortalConstant.INT_DESIG_ID_EXCEL) Integer intdesigidExcel,
			@RequestParam("startDateExcel") String startDateExcel, @RequestParam("endDateExcel") String endDateExcel,
			@RequestParam("fullNameExcel") String fullNameExcel, @RequestParam("statusIdExcel") String statusIdExcel)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		List<IPTrackBean> beans = loginAuditTrailService.viewSucessMobLoginDetails(organizationIdExcel, roleIdExcel,
				researchCenterIdExcel, intdesigidExcel, startDateExcel, endDateExcel,
				fullNameExcel.trim().toUpperCase(), userId, statusIdExcel, -1, -1);

		SucessMobLoginDetailsPDF pdf = new SucessMobLoginDetailsPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = pdf.generateMobLoginDetails(beans);

		Date date = new Date();
		long time = date.getTime();

		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String filename = "Mobilelogin_Details_" + timeStamp + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}
}
