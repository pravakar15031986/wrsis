package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;

public interface SurveyPublishOrDiscardService {

	List<SurveyDetailsBean> pendingSurveyView();

	List<SurveyDetailsBean> searchPendingSurvey(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId);

	List<SurveyDetailsBean> publishedSurveyView();

	List<SurveyDetailsBean> searchPublishedSurvey(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId);

	List<SurveyDetailsBean> discardSurveyView();

	List<SurveyDetailsBean> searchDiscardSurvey(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId);

	List<SurveyDetailsBean> surveyPublishedReport(Integer userId);

	List<SurveyDetailsBean> searchSurveyPublisheReport(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId, Integer userId);

	List<SurveyDetailsBean> surveyDiscardedReport(Integer userId);

	List<SurveyDetailsBean> searchSurveyDiscardedReport(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, String surveyNo, String rustTypeId, Integer userId);

	List<SurveyDetailsBean> rustSurveyDataReport();

	List<SurveyDetailsBean> rustSurveyDataSearchReport(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo);

	void surveyPublishedExcel(HttpServletResponse response, Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo);

	List<SurveyDetailsBean> rustSurveyDataReportByRCTagging(Integer userId);

	List<SurveyDetailsBean> rustSurveyDataSearchReportByRCTagging(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer userId);

	void surveyPublishedExcelByRCTagging(HttpServletResponse response, Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo,
			Integer userId);

	UserResearchCenterMapping checkUsertaggingResearchCenter(Integer userId);

	List<SurveyDetailsBean> discardSurveyViewPage(String regionId, String zoneId, String woredaId, String kebeleId,
			String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId, Integer userId,
			Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> publishedSurveyViewPage(String regionId, String zoneId, String woredaId, String kebeleId,
			String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId, Integer userId,
			Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> pendingSurveyViewPage(String regionId, String zoneId, String woredaId, String kebeleId,
			String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId, Integer userId,
			Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> pendingSurveyViewPageByUser(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer processId, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> pendingSurveyViewPageExcel(String regionId, String zoneId, String woredaId, String kebeleId,
			String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId, Integer userId,
			Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> publishedSurveyViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> discardSurveyViewPageExcel(String regionId, String zoneId, String woredaId, String kebeleId,
			String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId, Integer userId,
			Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> rustSurveyPublishedDataViewPage(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength);

	List<SurveyDetailsBean> rustSurveyPublishedDataViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength);

	List<RustIncidentEntityBean> getDashBoardRustIncidentExcel(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, Integer yearId, Integer seasonId);

	List<SurveyDetailsBean> surveyPublishedExcelByRCTaggingData(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer userId);

	List<SurveyDetailsBean> pendingSurveyViewPageExcelByUser(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer processId, Integer pstart, Integer pLength);

	String publishOrDiscardSurveyDataSave(String[] surveyFinalArrayId, String surveyFinalOptionId,
			String surveyFinalReasonId, Integer userId, String processId, String sendMailStatus, String sendSmsStatus);

	List<SurveyDetailsBean> viewSurveyPublishedAndDiscardReport(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength);
}
