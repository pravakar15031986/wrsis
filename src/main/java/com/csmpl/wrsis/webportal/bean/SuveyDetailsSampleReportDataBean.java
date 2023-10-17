package com.csmpl.wrsis.webportal.bean;

import java.util.ArrayList;

import org.json.JSONObject;

public class SuveyDetailsSampleReportDataBean {

	private Integer surveyId;
	private String surveyNo;
	private String country;
	private String surveyourName;
	private String rcName;
	private String season;
	private String region;
	private String zone;
	private String woreda;
	private String kebele;
	private String altitude;
	private String longitude;
	private String latitude;
	private String surveyDate;
	private String plantingDate;
	private String firstRustObservationDate;
	private String contactedFarmer;
	private String remarks;
	// Site Information
	private String surveySite;
	private String wheatType;
	private String variety;
	private String growthStage;
	private String area;
	private String rustAffected;
	// Rust Details
	private ArrayList<String> rustNameType;
	private JSONObject mapData;

	// SampleDetails
	private JSONObject mapSampleDetails;

	// Fungicide Details
	private String fungicideApplied;
	private String fungicideName;
	private String fungicideDose;
	private String fungicideSprayDate;
	private String fungicideEffectiveNessName;

	private JSONObject mapOtherDisease;

	private JSONObject anyOtherDisease;

	private String irrigated;
	private String weedControl;
	private String soilColor;
	private String moisture;
	private ArrayList<String> tan_spot;
	private String address;

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSurveyourName() {
		return surveyourName;
	}

	public void setSurveyourName(String surveyourName) {
		this.surveyourName = surveyourName;
	}

	public String getRcName() {
		return rcName;
	}

	public void setRcName(String rcName) {
		this.rcName = rcName;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getWoreda() {
		return woreda;
	}

	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}

	public String getKebele() {
		return kebele;
	}

	public void setKebele(String kebele) {
		this.kebele = kebele;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
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

	public String getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(String surveyDate) {
		this.surveyDate = surveyDate;
	}

	public String getPlantingDate() {
		return plantingDate;
	}

	public void setPlantingDate(String plantingDate) {
		this.plantingDate = plantingDate;
	}

	public String getFirstRustObservationDate() {
		return firstRustObservationDate;
	}

	public void setFirstRustObservationDate(String firstRustObservationDate) {
		this.firstRustObservationDate = firstRustObservationDate;
	}

	public String getContactedFarmer() {
		return contactedFarmer;
	}

	public void setContactedFarmer(String contactedFarmer) {
		this.contactedFarmer = contactedFarmer;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSurveySite() {
		return surveySite;
	}

	public void setSurveySite(String surveySite) {
		this.surveySite = surveySite;
	}

	public String getWheatType() {
		return wheatType;
	}

	public void setWheatType(String wheatType) {
		this.wheatType = wheatType;
	}

	public String getVariety() {
		return variety;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}

	public String getGrowthStage() {
		return growthStage;
	}

	public void setGrowthStage(String growthStage) {
		this.growthStage = growthStage;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRustAffected() {
		return rustAffected;
	}

	public void setRustAffected(String rustAffected) {
		this.rustAffected = rustAffected;
	}

	public ArrayList<String> getRustNameType() {
		return rustNameType;
	}

	public void setRustNameType(ArrayList<String> rustNameType) {
		this.rustNameType = rustNameType;
	}

	public JSONObject getMapData() {
		return mapData;
	}

	public void setMapData(JSONObject mapData) {
		this.mapData = mapData;
	}

	public JSONObject getMapSampleDetails() {
		return mapSampleDetails;
	}

	public void setMapSampleDetails(JSONObject mapSampleDetails) {
		this.mapSampleDetails = mapSampleDetails;
	}

	public String getFungicideApplied() {
		return fungicideApplied;
	}

	public void setFungicideApplied(String fungicideApplied) {
		this.fungicideApplied = fungicideApplied;
	}

	public String getFungicideName() {
		return fungicideName;
	}

	public void setFungicideName(String fungicideName) {
		this.fungicideName = fungicideName;
	}

	public String getFungicideDose() {
		return fungicideDose;
	}

	public void setFungicideDose(String fungicideDose) {
		this.fungicideDose = fungicideDose;
	}

	public String getFungicideSprayDate() {
		return fungicideSprayDate;
	}

	public void setFungicideSprayDate(String fungicideSprayDate) {
		this.fungicideSprayDate = fungicideSprayDate;
	}

	public String getFungicideEffectiveNessName() {
		return fungicideEffectiveNessName;
	}

	public void setFungicideEffectiveNessName(String fungicideEffectiveNessName) {
		this.fungicideEffectiveNessName = fungicideEffectiveNessName;
	}

	public JSONObject getMapOtherDisease() {
		return mapOtherDisease;
	}

	public void setMapOtherDisease(JSONObject mapOtherDisease) {
		this.mapOtherDisease = mapOtherDisease;
	}

	public String getIrrigated() {
		return irrigated;
	}

	public void setIrrigated(String irrigated) {
		this.irrigated = irrigated;
	}

	public String getWeedControl() {
		return weedControl;
	}

	public void setWeedControl(String weedControl) {
		this.weedControl = weedControl;
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

	public ArrayList<String> getTan_spot() {
		return tan_spot;
	}

	public void setTan_spot(ArrayList<String> tan_spot) {
		this.tan_spot = tan_spot;
	}

	public JSONObject getAnyOtherDisease() {
		return anyOtherDisease;
	}

	public void setAnyOtherDisease(JSONObject anyOtherDisease) {
		this.anyOtherDisease = anyOtherDisease;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
