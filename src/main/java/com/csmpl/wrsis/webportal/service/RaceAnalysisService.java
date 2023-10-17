package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;

public interface RaceAnalysisService {

	void saveIncoulationDetails(String encodedJson, Integer userId, Integer rustTypeId) throws JSONException;

	String updateLab(SampleLabTagBean sampleDetails);

	void publishRaceAnalysis(String fileName, Integer analysisId, String finalResult, Integer sampleId, Integer userId);

	JSONObject saveDumpSampleTagDetails(JSONArray json, int userId) throws JSONException;

	List<SampleTagBean> viewRaceAnalysisReport(SearchVo searchVo);

	List<SampleTagBean> downloadRaceAnalysisStatusExcel(String surveyNo, String sampleId, String startDate,
			String endDate, String regionId, String rustTypeId, Integer currentUserId, Integer researchCenterId);

}
