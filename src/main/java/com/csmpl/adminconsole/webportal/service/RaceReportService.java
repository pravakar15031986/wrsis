package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;

public interface RaceReportService {

	JSONObject fetchRaceReportResultWise(Integer userId, Integer rustTypeId) throws Exception;

	List<SampleTagBean> getDetails(SearchVo searchVo);

	String fetchVirulenceData(Integer rustType);

	String fetchVarietyReportDataSearch(Integer rustId);

	String fetchVarietyReportDataSearch(Integer rustId, Integer regionId);

	List<SampleTagBean> reportExcel(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			String raceResult, Integer regionId, Integer rustId, Integer labId);

}
