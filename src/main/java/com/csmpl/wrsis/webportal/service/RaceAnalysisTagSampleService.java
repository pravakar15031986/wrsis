package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;

public interface RaceAnalysisTagSampleService {

	String saveTagSample(SampleTagBean tagBean, String jsonString);

	void tagsampleExcelDownload(HttpServletResponse response, String sampleIdE, String surveyNoE, String fromDateE,
			String toDateE, int regionIdE, int filterE);

	List<SampleLabTagBean> tagsamplePdfDownload(String sampleIdPDF, String surveyNoPDF, String fromDatePDF,
			String toDatePDF, int regionIdPDF, int filterPDF);

	List<SampleTagBean> fetchActiveTagSamples(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer start, Integer length);

	Integer searchTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer start, Integer length);

	List<SampleLabTagBean> viewTaggedSamples(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer start, Integer length);

	Integer viewTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer start, Integer length);

}
