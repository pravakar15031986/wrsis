package com.csmpl.wrsis.webportal.report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.RecomendationPublishedWeReportPdf;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.service.MonitorService;

@Controller
public class RecomendationSurveyDataWeReportController extends WrsisPortalAbstractController {

	@Autowired
	MonitorService monitorService;

	/*-------------------Published Data(Start)-----------------*/

	// Excel Report download Recomendation Survey Data
	@RequestMapping("/downloadRecomendationSurveyDataDetailsWePublishedExcel")
	public void recomendationSurveyDetailsWeReportDownloadExcel(HttpServletResponse response,
			HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("rNoExcel") String monitorrefNo,
			@RequestParam("startDateExcel") String fromDate, @RequestParam("endDateExcel") String toDate)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		// String processId =

		// int regionId, int zoneId, int woredaId, String monitorRefNumber,String
		// startDate, String endDate, int i, int userId

		DownloadSampleExcel surveyExcel = new DownloadSampleExcel();

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedReportByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 1, userId, -1, -1);

		HSSFWorkbook excelBook = surveyExcel.recomendationPublishedSurveyReportDownload(response, beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=Recomendation_Published_Survey_Details_" + formattedDate + ".xls");
		OutputStream out = response.getOutputStream();
		excelBook.write(out);
		out.flush();
		out.close();
		excelBook.close();

	}

	// PDF Report download published Recomendation Survey Data
	@RequestMapping(value = {
			"/downloadRecomendationSurveyDataDetailsWePublishedPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> recomendationSurveyDetailsReportDownloadPDF(HttpServletResponse response,
			HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("rNoExcel") String monitorrefNo,
			@RequestParam("startDateExcel") String fromDate, @RequestParam("endDateExcel") String toDate)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		// String processId =

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedReportByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 1, userId, -1, -1);

		RecomendationPublishedWeReportPdf pdf = new RecomendationPublishedWeReportPdf();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = pdf.generateRecomendationPublishedSurveyDataDetailsPDF(beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
		String filename = "Recomendation_Published_Data_Details_" + formattedDate + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	/*-------------------Published Data(End)-----------------*/

	/*-------------------Discardrd Data(Start)-----------------*/

	// Excel Report download Recomendation Survey Data
	@RequestMapping("/downloadRecomendationSurveyDataDetailsWeDiscardExcel")
	public void recomendationSurveyDiscardedDetailsWeReportDownloadExcel(HttpServletResponse response,
			HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("rNoExcel") String monitorrefNo,
			@RequestParam("startDateExcel") String fromDate, @RequestParam("endDateExcel") String toDate)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		// String processId =

		// int regionId, int zoneId, int woredaId, String monitorRefNumber,String
		// startDate, String endDate, int i, int userId

		DownloadSampleExcel surveyExcel = new DownloadSampleExcel();

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedReportByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 2, userId, -1, -1);

		HSSFWorkbook excelBook = surveyExcel.recomendationPublishedSurveyReportDownload(response, beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=Recomendation_Discardrd_Survey_Details_" + formattedDate + ".xls");
		OutputStream out = response.getOutputStream();
		excelBook.write(out);
		out.flush();
		out.close();
		excelBook.close();

	}

	// PDF Report download published Recomendation Survey Data
	@RequestMapping(value = {
			"/downloadRecomendationSurveyDataDetailsWeDiscardPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> recomendationSurveyDiscardedDetailsReportDownloadPDF(
			HttpServletResponse response, HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("rNoExcel") String monitorrefNo,
			@RequestParam("startDateExcel") String fromDate, @RequestParam("endDateExcel") String toDate)
			throws IOException {

		Integer userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		// String processId =

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedDiscardedReportByUserId(regionId, zoneId,
				woredaId, monitorrefNo, fromDate, toDate, 2, userId, -1, -1);

		RecomendationPublishedWeReportPdf pdf = new RecomendationPublishedWeReportPdf();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = pdf.generateRecomendationPublishedSurveyDataDetailsPDF(beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
		String filename = "Recomendation_Discarded_Data_Details_" + formattedDate + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

	/*-------------------Discardrd Data(End)-----------------*/

}
