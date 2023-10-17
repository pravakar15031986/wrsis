package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;



@Service
public interface CommonService {
	
	List<Object[]> getSelectedInstitutionNam(Integer userId);
	List<Object[]> getAllSurveyor(Integer researchCenterId);
	String saveSurveyDetails(String encodeJSON,Integer surveyId,Integer userId,Integer collectMode,Integer fileSurveyId,Integer updatedBy) throws Exception;
	String deleteSurveyDetails(int surveyId);
	JSONObject getFormmatedJsonBySurveyId(Integer surveyId) throws JSONException;
	
	Integer getCurrntSeasonId(); 
	
	List<Object[]> getInstitutionNam();
	
	JSONObject getFormmatedJsonBySurveyIdForOtherDisease(Integer surveyId) throws JSONException;
	JSONObject getFormmatedJsonBySurveyIdForGlobalRust(Integer surveyId) throws JSONException;
}