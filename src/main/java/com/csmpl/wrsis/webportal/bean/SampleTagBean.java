package com.csmpl.wrsis.webportal.bean;

import java.util.List;

public class SampleTagBean {

	private String surveyNo;
	private String sampleValue;
	private String allocationDate;
	private String rustType;
	private Integer rustTypeId;
	private Integer surveyId;
	private String sampleCollectedOn;
	private Integer zoneId;
	private Integer regionId;
	private Integer woredaId;
	private Integer kebelId;

	private String zone;
	private String region;
	private String woreda;
	private String kebel;

	private String allocationStartDate;
	private String allocationEndDate;

	private Integer sampleTagId;
	private String inoculationDate;
	private Integer analysisId;
	private String repeatation;
	private Integer raceStatus;
	private String researchCenter;
	private String surveyDate;
	private String surveyPublishDate;
	private Integer samplesId;
	private Integer researchCenterId;
	private Integer rustId;
	private int userId;
	private Integer sampleId;
	private String status;
	private String raceResult;
	private String racePublishDate;
	private String raceflag;
	private Integer sNo;
	private String surveyCheckBox;
	private String rcDropDown;
	private String sampleNo;
	private String startDate;
	private String endDate;
	private String surveyDateFrom;
	private String surveyDateTo;
	private String raceDocument;

	public String getRaceDocument() {
		return raceDocument;
	}

	public void setRaceDocument(String raceDocument) {
		this.raceDocument = raceDocument;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public String getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(String allocationDate) {
		this.allocationDate = allocationDate;
	}

	public String getRustType() {
		return rustType;
	}

	public void setRustType(String rustType) {
		this.rustType = rustType;
	}

	public Integer getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(Integer rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getSampleCollectedOn() {
		return sampleCollectedOn;
	}

	public void setSampleCollectedOn(String sampleCollectedOn) {
		this.sampleCollectedOn = sampleCollectedOn;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getWoredaId() {
		return woredaId;
	}

	public void setWoredaId(Integer woredaId) {
		this.woredaId = woredaId;
	}

	public Integer getKebelId() {
		return kebelId;
	}

	public void setKebelId(Integer kebelId) {
		this.kebelId = kebelId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getWoreda() {
		return woreda;
	}

	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}

	public String getKebel() {
		return kebel;
	}

	public String getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(String surveyDate) {
		this.surveyDate = surveyDate;
	}

	public String getSurveyPublishDate() {
		return surveyPublishDate;
	}

	public void setSurveyPublishDate(String surveyPublishDate) {
		this.surveyPublishDate = surveyPublishDate;
	}

	public void setKebel(String kebel) {
		this.kebel = kebel;
	}

	public String getAllocationStartDate() {
		return allocationStartDate;
	}

	public void setAllocationStartDate(String allocationStartDate) {
		this.allocationStartDate = allocationStartDate;
	}

	public String getAllocationEndDate() {
		return allocationEndDate;
	}

	public void setAllocationEndDate(String allocationEndDate) {
		this.allocationEndDate = allocationEndDate;
	}

	public Integer getSampleTagId() {
		return sampleTagId;
	}

	public void setSampleTagId(Integer sampleTagId) {
		this.sampleTagId = sampleTagId;
	}

	public String getInoculationDate() {
		return inoculationDate;
	}

	public void setInoculationDate(String inoculationDate) {
		this.inoculationDate = inoculationDate;
	}

	public Integer getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(Integer analysisId) {
		this.analysisId = analysisId;
	}

	public String getRepeatation() {
		return repeatation;
	}

	public void setRepeatation(String repeatation) {
		this.repeatation = repeatation;
	}

	public Integer getRaceStatus() {
		return raceStatus;
	}

	public void setRaceStatus(Integer raceStatus) {
		this.raceStatus = raceStatus;
	}

	public String getResearchCenter() {
		return researchCenter;
	}

	public void setResearchCenter(String researchCenter) {
		this.researchCenter = researchCenter;
	}

	public Integer getSamplesId() {
		return samplesId;
	}

	public void setSamplesId(Integer samplesId) {
		this.samplesId = samplesId;
	}

	public Integer getResearchCenterId() {
		return researchCenterId;
	}

	public void setResearchCenterId(Integer researchCenterId) {
		this.researchCenterId = researchCenterId;
	}

	public Integer getRustId() {
		return rustId;
	}

	public void setRustId(Integer rustId) {
		this.rustId = rustId;
	}

	private List<ResearchCenterBean> researchCenterDetails;

	public List<ResearchCenterBean> getResearchCenterDetails() {
		return researchCenterDetails;
	}

	public void setResearchCenterDetails(List<ResearchCenterBean> researchCenterDetails) {
		this.researchCenterDetails = researchCenterDetails;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRaceResult() {
		return raceResult;
	}

	public void setRaceResult(String raceResult) {
		this.raceResult = raceResult;
	}

	public String getRacePublishDate() {
		return racePublishDate;
	}

	public void setRacePublishDate(String racePublishDate) {
		this.racePublishDate = racePublishDate;
	}

	public String getRaceflag() {
		return raceflag;
	}

	public void setRaceflag(String raceflag) {
		this.raceflag = raceflag;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getSurveyCheckBox() {
		return surveyCheckBox;
	}

	public void setSurveyCheckBox(String surveyCheckBox) {
		this.surveyCheckBox = surveyCheckBox;
	}

	public String getRcDropDown() {
		return rcDropDown;
	}

	public void setRcDropDown(String rcDropDown) {
		this.rcDropDown = rcDropDown;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSurveyDateFrom() {
		return surveyDateFrom;
	}

	public void setSurveyDateFrom(String surveyDateFrom) {
		this.surveyDateFrom = surveyDateFrom;
	}

	public String getSurveyDateTo() {
		return surveyDateTo;
	}

	public void setSurveyDateTo(String surveyDateTo) {
		this.surveyDateTo = surveyDateTo;
	}

}
