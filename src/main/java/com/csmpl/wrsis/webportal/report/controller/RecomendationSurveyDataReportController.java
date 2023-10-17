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

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.pdf.RecomendationSurveyDetailsPDF;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.excel.DownloadSampleExcel;
import com.csmpl.wrsis.webportal.service.MonitorService;

@Controller
public class RecomendationSurveyDataReportController extends WrsisPortalAbstractController {

	@Autowired
	MonitorService monitorService;

	// Excel Report download Recomendation Survey Data
	@RequestMapping("/downloadRecomendationSurveyDataDetailsExcel")
	public void recomendationSurveyDetailsReportDownloadExcel(HttpServletResponse response, HttpServletRequest request,
			Model model, HttpSession session, @RequestParam("regionIdExcel") Integer regionId,
			@RequestParam("zoneIdExcel") Integer zoneId, @RequestParam("woredaIdExcel") Integer woredaId,
			@RequestParam("rNoExcel") String rNoid, @RequestParam("startDateExcel") String startDate,
			@RequestParam("endDateExcel") String endDate) throws IOException {

		SearchVo searchVo = new SearchVo();

		searchVo.setRegionId(regionId);
		searchVo.setZoneId(zoneId);
		searchVo.setWoredaId(woredaId);
		searchVo.setMonitorRefNumber(rNoid);
		searchVo.setStartDate(startDate);
		searchVo.setEndDate(endDate);

		searchVo.setPageSize(-1);
		searchVo.setPageLength(-1);

		DownloadSampleExcel surveyExcel = new DownloadSampleExcel();

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedByPageReport(searchVo);

		HSSFWorkbook excelBook = surveyExcel.recomendationSurveyReportDownload(response, beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");

		response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
				"attachment; filename=Recomendation_Survey_Details_" + formattedDate + ".xls");
		OutputStream out = response.getOutputStream();
		excelBook.write(out);
		out.flush();
		out.close();
		excelBook.close();

	}

	// PDF Report download Recomendation Survey Data
	@RequestMapping(value = {
			"/downloadRecomendationSurveyDataDetailsPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> recomendationSurveyDetailsReportDownloadPDF(HttpServletResponse response,
			HttpServletRequest request, Model model, HttpSession session,
			@RequestParam("regionIdExcel") Integer regionId, @RequestParam("zoneIdExcel") Integer zoneId,
			@RequestParam("woredaIdExcel") Integer woredaId, @RequestParam("rNoExcel") String rNoid,
			@RequestParam("startDateExcel") String startDate, @RequestParam("endDateExcel") String endDate)
			throws IOException {

		SearchVo searchVo = new SearchVo();

		searchVo.setRegionId(regionId);
		searchVo.setZoneId(zoneId);
		searchVo.setWoredaId(woredaId);
		searchVo.setMonitorRefNumber(rNoid);
		searchVo.setStartDate(startDate);
		searchVo.setEndDate(endDate);

		searchVo.setPageSize(-1);
		searchVo.setPageLength(-1);

		List<SurveyImplementationBean> beans = monitorService.viewAllPublishedByPageReport(searchVo);

		RecomendationSurveyDetailsPDF pdf = new RecomendationSurveyDetailsPDF();
		HttpHeaders headers = new HttpHeaders();
		ByteArrayInputStream bar = pdf.generateRecomendationSurveyDataDetailsPDF(beans);

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
		String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
		String filename = "Recomendation_Survey_Data_Details_" + formattedDate + ".pdf";
		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + filename);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

}
