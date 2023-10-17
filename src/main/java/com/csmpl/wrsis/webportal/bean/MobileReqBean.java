package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MobileReqBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private String userName;
	private String password;
	private String email;
	private String mobile;
	private List<QueAnsDetails> queAnsDetails;
	private String date;
	private Integer disease;
	private String year;
	private Integer growth;
	private String surveyData;
	private Integer surveyId;
	private Integer rowid;
	private Integer pagecount;
	private String alertType;
	private Integer demographyId;
	private Integer seasonId;
	private String longitude;
	private String latitude;

	private String implementationData;
	private Integer incidentId;
	private Integer monitorId;
	private Integer raceAnalysisId;
	private List<Integer> surveyYears;
	private String severity;
	private Integer rustincidentId;
	private Integer researchCenterId;
	private String toDate;
	private String fromDate;
	private Integer notificationId;
	private Integer mapTypeId;
	private String fcmId;

	//for Mobile App version API
	
	private String versionval;
	private Integer osTypeId;
	private String isMandatory;
	
	private String authToken;

	public List<Integer> getSurveyYears() {
		return surveyYears;
	}

	public void setSurveyYears(List<Integer> surveyYears) {
		this.surveyYears = surveyYears;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<QueAnsDetails> getQueAnsDetails() {
		return queAnsDetails;
	}

	public void setQueAnsDetails(List<QueAnsDetails> queAnsDetails) {
		this.queAnsDetails = queAnsDetails;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getYear() {
		return year;
	}

	public Integer getDisease() {
		return disease;
	}

	public void setDisease(Integer disease) {
		this.disease = disease;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSurveyData() {
		return surveyData;
	}

	public Integer getGrowth() {
		return growth;
	}

	public void setGrowth(Integer growth) {
		this.growth = growth;
	}

	public void setSurveyData(String surveyData) {
		this.surveyData = surveyData;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public Integer getRowid() {
		return rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public Integer getPagecount() {
		return pagecount;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setPagecount(Integer pagecount) {
		this.pagecount = pagecount;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public Integer getDemographyId() {
		return demographyId;
	}

	public void setDemographyId(Integer demographyId) {
		this.demographyId = demographyId;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getImplementationData() {
		return implementationData;
	}

	public void setImplementationData(String implementationData) {
		this.implementationData = implementationData;
	}

	public Integer getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(Integer incidentId) {
		this.incidentId = incidentId;
	}

	public Integer getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(Integer monitorId) {
		this.monitorId = monitorId;
	}

	public Integer getRaceAnalysisId() {
		return raceAnalysisId;
	}

	public void setRaceAnalysisId(Integer raceAnalysisId) {
		this.raceAnalysisId = raceAnalysisId;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Integer getRustincidentId() {
		return rustincidentId;
	}

	public void setRustincidentId(Integer rustincidentId) {
		this.rustincidentId = rustincidentId;
	}

	public Integer getResearchCenterId() {
		return researchCenterId;
	}

	public void setResearchCenterId(Integer researchCenterId) {
		this.researchCenterId = researchCenterId;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Integer getMapTypeId() {
		return mapTypeId;
	}

	public void setMapTypeId(Integer mapTypeId) {
		this.mapTypeId = mapTypeId;
	}

	public String getFcmId() {
		return fcmId;
	}

	public void setFcmId(String fcmId) {
		this.fcmId = fcmId;
	}

	public String getVersionval() {
		return versionval;
	}

	public void setVersionval(String versionval) {
		this.versionval = versionval;
	}

	public Integer getOsTypeId() {
		return osTypeId;
	}

	public void setOsTypeId(Integer osTypeId) {
		this.osTypeId = osTypeId;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	
	//for adding survey Details
	private String otherSurveyorName;
	private String kebeleName;
	private String regionName;
	private String zoneName;
	private String woredaName;
	private String seasionName;
	private String locationName;
	
	private Boolean isDecimalOrDegree;
	
	private Boolean isFarmerContacted;
	
	//site information
	private String surveySiteName;
	private String wheatTypeName;
	private String varityName;
	private String otherVarietyName;
	private String otherWheatName;
	private String growthStageName;
	private String siteArea;
	
	private JSONArray[] SiteInformation ;
	
	//rust affected details
	private Boolean isRustAffected;
	
	
	
	//Capture Image
	
	private Boolean isImageCaptured;
	
	
	//sample details
	private Boolean isSampleCollected;
	
	
	private Boolean isFungisideApplied;
	
	private String effectiveNessName;
	private String fungicideName;
	private String otherFungiName;
	private String sprayDate;
	private Integer dose;
 
	
	
	//other details
	private Boolean isIrrigated;
	private Boolean isWeedControl ;
	private String soilColor;
	private String moisture;
	private String observationComment;
	
	private JSONObject otherDeatails;

	public String getOtherSurveyorName() {
		return otherSurveyorName;
	}

	public void setOtherSurveyorName(String otherSurveyorName) {
		this.otherSurveyorName = otherSurveyorName;
	}

	public String getKebeleName() {
		return kebeleName;
	}

	public void setKebeleName(String kebeleName) {
		this.kebeleName = kebeleName;
	}

	public void setIsDecimalOrDegree(Boolean isDecimalOrDegree) {
		this.isDecimalOrDegree = isDecimalOrDegree;
	}

	public Boolean getIsDecimalOrDegree() {
		return isDecimalOrDegree;
	}

	public void setIsdecimalOrDegree(Boolean isDecimalOrDegree) {
		this.isDecimalOrDegree = isDecimalOrDegree;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getWoredaName() {
		return woredaName;
	}

	

	public void setWoredaName(String woredaName) {
		this.woredaName = woredaName;
	}

	public String getSeasionName() {
		return seasionName;
	}

	public JSONArray[] getSiteInformation() {
		return SiteInformation;
	}

	public void setSiteInformation(JSONArray[] siteInformation) {
		SiteInformation = siteInformation;
	}

	public JSONObject getOtherDeatails() {
		return otherDeatails;
	}

	public void setOtherDeatails(JSONObject otherDeatails) {
		this.otherDeatails = otherDeatails;
	}

	public void setSeasionName(String seasionName) {
		this.seasionName = seasionName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Boolean getIsFarmerContacted() {
		return isFarmerContacted;
	}

	public void setIsFarmerContacted(Boolean isFarmerContacted) {
		this.isFarmerContacted = isFarmerContacted;
	}

	public String getSurveySiteName() {
		return surveySiteName;
	}

	public void setSurveySiteName(String surveySiteName) {
		this.surveySiteName = surveySiteName;
	}

	public String getWheatTypeName() {
		return wheatTypeName;
	}

	public void setWheatTypeName(String wheatTypeName) {
		this.wheatTypeName = wheatTypeName;
	}

	public String getVarityName() {
		return varityName;
	}

	public void setVarityName(String varityName) {
		this.varityName = varityName;
	}

	public String getOtherVarietyName() {
		return otherVarietyName;
	}

	public void setOtherVarietyName(String otherVarietyName) {
		this.otherVarietyName = otherVarietyName;
	}

	public String getOtherWheatName() {
		return otherWheatName;
	}

	public void setOtherWheatName(String otherWheatName) {
		this.otherWheatName = otherWheatName;
	}

	public String getGrowthStageName() {
		return growthStageName;
	}

	public void setGrowthStageName(String growthStageName) {
		this.growthStageName = growthStageName;
	}

	public String getSiteArea() {
		return siteArea;
	}

	public void setSiteArea(String siteArea) {
		this.siteArea = siteArea;
	}

	public Boolean getIsRustAffected() {
		return isRustAffected;
	}

	public void setIsRustAffected(Boolean isRustAffected) {
		this.isRustAffected = isRustAffected;
	}

	

	public Boolean getIsImageCaptured() {
		return isImageCaptured;
	}

	public void setIsImageCaptured(Boolean isImageCaptured) {
		this.isImageCaptured = isImageCaptured;
	}

	public Boolean getIsSampleCollected() {
		return isSampleCollected;
	}

	public void setIsSampleCollected(Boolean isSampleCollected) {
		this.isSampleCollected = isSampleCollected;
	}

	

	

	public Boolean getIsFungisideApplied() {
		return isFungisideApplied;
	}

	public void setIsFungisideApplied(Boolean isFungisideApplied) {
		this.isFungisideApplied = isFungisideApplied;
	}

	public String getEffectiveNessName() {
		return effectiveNessName;
	}

	public void setEffectiveNessName(String effectiveNessName) {
		this.effectiveNessName = effectiveNessName;
	}

	public String getFungicideName() {
		return fungicideName;
	}

	public void setFungicideName(String fungicideName) {
		this.fungicideName = fungicideName;
	}

	public String getOtherFungiName() {
		return otherFungiName;
	}

	public void setOtherFungiName(String otherFungiName) {
		this.otherFungiName = otherFungiName;
	}

	public String getSprayDate() {
		return sprayDate;
	}

	public void setSprayDate(String sprayDate) {
		this.sprayDate = sprayDate;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	

	public Boolean getIsIrrigated() {
		return isIrrigated;
	}

	public void setIsIrrigated(Boolean isIrrigated) {
		this.isIrrigated = isIrrigated;
	}

	public Boolean getIsWeedControl() {
		return isWeedControl;
	}

	public void setIsWeedControl(Boolean isWeedControl) {
		this.isWeedControl = isWeedControl;
	}

	public String getSoilColor() {
		return soilColor;
	}

	public void setSoilColor(String soilColor) {
		this.soilColor = soilColor;
	}

	public String getMoisture() {
		return moisture;
	}

	public void setMoisture(String moisture) {
		this.moisture = moisture;
	}

	public String getObservationComment() {
		return observationComment;
	}

	public void setObservationComment(String observationComment) {
		this.observationComment = observationComment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


	
}
