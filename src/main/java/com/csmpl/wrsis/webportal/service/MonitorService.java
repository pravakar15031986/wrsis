package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.MonitorBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;

public interface MonitorService {

	List<MonitorBean> viewAllRecommendForMonitor();

	List<MonitorBean> searchAllRecommendForMonitor(SearchVo search);

	List<SurveyImplementationBean> viewMoniterDataByRecommendNumber(String rcNumber);

	String publishedAndDiscardMonitorData(String monitorId, Integer userId);// Integer status,

	List<SurveyImplementationBean> viewMoniterDataByRecommendNumberSearch(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String startDate, String endDate, String rcNumber);

	List<SurveyImplementationBean> viewAllPublished(SearchVo searchVo);

	List<SurveyImplementationBean> viewImplementationDetailsData(Integer regionId, Integer zoneId, String monitorNo,
			String monitordate, Integer userId, Integer pstart, Integer plength);

	Integer viewImplementationDetailsDataCount(Integer regionId, Integer zoneId, String monitorNo, String monitordate,
			Integer userId, Integer pstart, Integer plength);

	List<SurveyImplementationBean> monitorRecommendationPublishedData(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String recDtFrom, String recDtTo, String rcNumber, Integer userId,
			Integer processId, Integer pstart, Integer plength);

	Integer monitorRecommendationPublishedDataCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String recDtFrom, String recDtTo, String rcNumber, Integer userId, Integer processId,
			Integer pstart, Integer plength);

	List<SurveyImplementationBean> viewMoniterPublishedAndDiscardSearch(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String startDate, String endDate, Integer status, Integer pstart,
			Integer plength);

	Integer viewMoniterPublishedAndDiscardSearchCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String startDate, String endDate, Integer status, Integer pstart, Integer plength);

	List<SurveyImplementationBean> viewAllPublishedByPage(SearchVo searchVo);

	Integer viewCountAllPublishedByPage(SearchVo searchVo);

	List<SurveyImplementationBean> viewAllPublishedDiscardedByUserId(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String fromDate, String toDate, Integer status, Integer userId, Integer pstart,
			Integer plength);

	Integer viewAllPublishedDiscardedByUserIdCount(Integer regionId, Integer zoneId, Integer woredaId, String monitorNo,
			String fromDate, String toDate, Integer status, Integer userId, Integer pstart, Integer plength);

	List<MonitorBean> viewAllForRecommendForMonitorPage(String recommNo, Integer userId, String startDate,
			String endDate, Integer pStart, Integer pLength);

	Integer viewAllForRecommendForMonitorPageCount(String recommNo, Integer userId, String startDate, String endDate,
			Integer pStart, Integer pLength);

	String deleteMonitorDetails(int monitorId);

	JSONObject getFormmatedJsonByMonitorId(Integer monitorId) throws JSONException;

	List<SurveyImplementationBean> viewAllPublishedByPageReport(SearchVo searchVo);

	Integer viewCountAllPublishedByPageReport(SearchVo searchVo);

	List<SurveyImplementationBean> viewAllPublishedDiscardedReportByUserId(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String fromDate, String toDate, Integer status, Integer userId,
			Integer pstart, Integer plength);

	Integer viewAllPublishedDiscardedReportByUserIdCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String fromDate, String toDate, Integer status, Integer userId, Integer pstart,
			Integer plength);

}
