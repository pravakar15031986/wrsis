package com.csmpl.wrsis.webportal.service;

import org.json.JSONException;
import org.json.JSONObject;

public interface SurveyReportService {

	JSONObject getFormmatedJsonBySurveyId(Integer surveyId) throws JSONException;
}
