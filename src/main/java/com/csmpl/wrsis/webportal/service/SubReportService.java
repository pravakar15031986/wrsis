package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.Question;

public interface SubReportService {

	List<SurveyDetailsBean> retriveSearchRustList(int regionId, int zoneId, int woredaId, int kebeleId,
			String startDate, String endDate, int seasonTypeId, int researchId, int rustTypeId, String surveyNo,
			int dataModeId, int yearId, Integer start, Integer length);

	List<SampleTagBean> retriveRaceReportList(String surveyId, String sampleId, String surveyDateFrom,
			String surveyDateTo, String recDtFrom, String recDtTo, Integer regionId, Integer rustId, Integer rcId,
			Integer labId, Integer start, Integer length);

	void raceReportExcel(HttpServletResponse response, String surveyNo, String sampleId, String startDate,
			String endDate, int demography, int rustType, int center, int labCenter, String recDtFrom, String recDtTo, Integer start, Integer length);

	void rustReportExcel(HttpServletResponse response, Integer yearIdXl, Integer seasonTypeIdXl, Integer regionXl,
			Integer zoneXl, Integer woredaXl, Integer kebeleXl, String startDateXl, String endDateXl,
			Integer researchIdXl, Integer rustTypeIdXl, String surveyNoXl, Integer dataModeIdX);

	List<SurveyImplementationBean> retriveMonitorData(Integer regionId, Integer zoneId, String monitorNo,
			String recommendationId, Integer userId, String monitorFromDate, String monitorToDate, Integer year,
			Integer seasonId, Integer start, Integer length);

	void excelMonitorData(HttpServletResponse response, int region, int zone, String monitor, String recNo,
			Integer userId, String fromdate, String todate, Integer year, Integer seasonid);

	List<RustIncidentEntityBean> retriveRustIncidendReport();

	List<RustIncidentEntityBean> retriveIncidendReport(SearchVo searchVo);

	List<Question> retriveQuesnList();

	List<SurveyImplementationBean> getMonitorDataforPdf(Integer regionPDF, Integer zonePDF, String monitorPDF,
			String recNoPDF, Integer userId, String fromdatePDF, String todatePDF, Integer yearPDF, Integer seasonPDF);

	List<SampleTagBean> getRaceAnalysisPDFData(String surveyidPDF, String sampidPDF, String startDatePDF,
			String endDatePDF, int demographyPDF, int rustidPDF, int centeridPDF, int labCenteridPDF,
			String recDtFromPDF, String recDtToPDF, Integer start, Integer length);

	void downloadRaceFile(HttpServletResponse response, Integer fileId, ModelMap model);

	String checkRaceFilePath(Integer fileId);

	Integer retriveRaceReportListCount(String surveyId, String sampleId, String surveyDateFrom, String surveyDateTo,
			String recDtFrom, String recDtTo, Integer regionId, Integer rustId, Integer rcId, Integer labId, int i,
			int j);

	Integer retriveRustReportListCount(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			String surveyDateFrom, String surveyDateTo, Integer seasonId, Integer rcId, Integer rustId, String surveyNo,
			Integer dataCollectModeId, Integer year, int i, int j);

	Integer retriveMonitorDataCount(Integer regionId, Integer zoneId, String monitorNo, String recommendationId,
			Integer userId, String monitorFromDate, String monitorToDate, Integer year, Integer seasonId, int start, int length);

}
