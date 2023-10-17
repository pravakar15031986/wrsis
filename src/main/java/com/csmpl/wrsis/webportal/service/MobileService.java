package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.entity.MobileLoginTracking;
import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;

public interface MobileService {

	JSONObject getSurveyMasterData(int userId);

	JSONObject getQuestionMasterData();
 
	JSONObject getforGISMasterData();

	String saveRustIncident(MobileReqBean loginReqDto);

	JSONObject getSurveyDataforGIS(MobileReqBean loginReqDto);

	JSONObject viewWheatRustSurvey(MobileReqBean loginReqDto);

	JSONObject viewRaceAnalysis(MobileReqBean loginReqDto);

	JSONObject viewRecommendationAndAdvisory(MobileReqBean loginReqDto);

	JSONObject getMasterforRustIncident(MobileReqBean loginReqDto);

	String saveSurveyImplementation(String encodeJSON,Integer collectMode,Integer monitorId);

	JSONObject viewImplementation(MobileReqBean loginReqDto);
	
	JSONObject viewRustIncident(MobileReqBean loginReqDto);

	JSONObject getMasterforSurveyImplementation(MobileReqBean loginReqDto);

	JSONObject viewRecommendationData(MobileReqBean loginReqDto);

	JSONObject mUserProfileDetails(Integer userId);
	
	JSONObject viewDetailsImplementation(MobileReqBean loginReqDto);

	JSONObject viewRaceAnalysisDetails(MobileReqBean loginReqDto);

	JSONObject getMasterforRustIncidentMap();

	JSONObject viewRustIncidentForGis(MobileReqBean loginReqDto);

	JSONObject viewRustIncidentByUser(MobileReqBean loginReqDto);

	JSONObject viewRustIncidentByRustIncident(MobileReqBean loginReqDto);

	JSONObject viewNotificationByUser(MobileReqBean loginReqDto);

	String updateNotificationStatus(MobileReqBean loginReqDto);

	MobileLoginTracking saveMobileTrackInfo(MobileLoginTracking ipinfo);

	String mobileUserLogout(MobileReqBean loginReqDto) throws JSONException;

	String generateMobileToken(int userId);
	
	boolean authenticateToken(int userId,String token);

	String updateAppVersion(MobileReqBean loginReqDto);

	JSONObject getAppVersionByOstype(Integer osTypeId);

	boolean updateAuthTokenAfterUse(Integer userId, String token);

	List<MonitorDetailsEntity> getMonitorDetailsByRecIdAndWoredaId(Integer recId, Integer woredaId);

	String saveRustIncident1(MobileReqBean loginReqDto);

	Boolean checkSeasonLatLongDuplicate(Integer valueOf, String latitude, String longitude);
}
