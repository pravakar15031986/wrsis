package com.csmpl.adminconsole.webportal.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;

public interface DashboardService {

	JSONObject getDashboardContent(Integer userId) throws SQLException;

	List<UserBean> fetchSurveyorList(String mobile, String email, String name, String rcId, String desigId,
			Integer start, Integer length);

	List<UserBean> fetchPathologistList(SearchVo searchVo);

	List<UserBean> fetchWoredaExpertList(SearchVo searchVo);

	List<UserBean> fetchDevAgentList(SearchVo searchVo);

	List<SeasionMasterEntity> getActiveSeasonList();

	JSONArray getRustReport(SearchVo searchVo) throws SQLException;

	JSONArray getRustReportByRegion(SearchVo searchVo) throws SQLException;

	JSONObject getSurveyIncidentReport(String yearId, Integer seasonId) throws SQLException;

	JSONArray getRustReportDateWise(SearchVo searchVo) throws SQLException;

	JSONArray getRustReportByRegionDateWise(SearchVo searchVo) throws SQLException;

	JSONObject getSurveyIncidentReportDateWise(String fromDate, String toDate) throws SQLException;

	JSONObject getRustSurveyAndRecommendSurvey(String yearId, Integer seasonId) throws SQLException;

	JSONObject getRustSurveyAndRecommendSurveyDate(String fromDate, String toDate) throws SQLException;

	void surveyorExcelDownload(HttpServletResponse response, String mobileE, String emailE, String fullNameE,
			String researchCenterIdE, String intdesigidE);

	List<UserBean> surveyorPdfDownload(String researchCenterIdPDF, String intdesigidPDF, String mobilePDF,
			String emailPDF, String fullNamePDF);

	List<UserBean> downloadPathologistReportExcel(String rcId, String desigId, String mobileNo, String email,
			String name);

	List<UserBean> downloadWoredaExpertReportExcel(String desigId, String mobileNo, String email, String name);

	List<UserBean> downloadDevAgentReportExcel(String desigId, String mobileNo, String email, String name);

	JSONObject getDashboardUsersContent(Integer userId);

	JSONObject getDashboardTransContent(Integer userId);

	Integer fetchSurveyorListCount(String mobile, String email, String name, String rcId, String desigId, int start,
			int length);

	JSONObject getAdvisoryLatestRecord();

	JSONObject getRecomLatestRecord();
}