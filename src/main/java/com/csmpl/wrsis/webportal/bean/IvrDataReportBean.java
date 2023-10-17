package com.csmpl.wrsis.webportal.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IvrDataReportBean {

	private int ivrDataId;
	private String phnNo;
	private Timestamp dtmCallDataTime;
	private int regionId;
	private int ZoneId;
	private int woredaId;
	private String region;
	private String zone;
	private String woreda;
	private String responseValue;
	private int sl;
	private ArrayList<String> questionList;
	private ArrayList<String> qResList;
	private Integer viewQust;
	private String ivrQustion;
	private String pageQustion;
	private List<String> ivrQuestionsData;
	private Integer slNo;
	private String callDateView;
	private String uploadedDateView;
	private String fileName;
	private String ivrDataCount;
	private String questionOption;
	private String responded;
	
	public int getIvrFileId() {
		return ivrFileId;
	}

	public void setIvrFileId(int ivrFileId) {
		this.ivrFileId = ivrFileId;
	}

	private int ivrFileId;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String uploadDate;
	private Integer noOfIvrData;

	

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Integer getNoOfIvrData() {
		return noOfIvrData;
	}

	public void setNoOfIvrData(Integer noOfIvrData) {
		this.noOfIvrData = noOfIvrData;
	}
	
	public int getIvrDataId() {
		return ivrDataId;
	}

	public void setIvrDataId(int ivrDataId) {
		this.ivrDataId = ivrDataId;
	}

	public String getPhnNo() {
		return phnNo;
	}

	public void setPhnNo(String phnNo) {
		this.phnNo = phnNo;
	}

	public Timestamp getDtmCallDataTime() {
		return dtmCallDataTime;
	}

	public void setDtmCallDataTime(Timestamp dtmCallDataTime) {
		this.dtmCallDataTime = dtmCallDataTime;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public int getZoneId() {
		return ZoneId;
	}

	public void setZoneId(int zoneId) {
		ZoneId = zoneId;
	}

	public int getWoredaId() {
		return woredaId;
	}

	public void setWoredaId(int woredaId) {
		this.woredaId = woredaId;
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

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	public int getSl() {
		return sl;
	}

	public void setSl(int sl) {
		this.sl = sl;
	}

	public ArrayList<String> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(ArrayList<String> questionList) {
		this.questionList = questionList;
	}

	public ArrayList<String> getqResList() {
		return qResList;
	}

	public void setqResList(ArrayList<String> qResList) {
		this.qResList = qResList;
	}

	public Integer getViewQust() {
		return viewQust;
	}

	public void setViewQust(Integer viewQust) {
		this.viewQust = viewQust;
	}

	public String getIvrQustion() {
		return ivrQustion;
	}

	public void setIvrQustion(String ivrQustion) {
		this.ivrQustion = ivrQustion;
	}

	public String getPageQustion() {
		return pageQustion;
	}

	public void setPageQustion(String pageQustion) {
		this.pageQustion = pageQustion;
	}

	public List<String> getIvrQuestionsData() {
		return ivrQuestionsData;
	}

	public void setIvrQuestionsData(List<String> ivrQuestionsData) {
		this.ivrQuestionsData = ivrQuestionsData;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public String getCallDateView() {
		return callDateView;
	}

	public void setCallDateView(String callDateView) {
		this.callDateView = callDateView;
	}

	public String getUploadedDateView() {
		return uploadedDateView;
	}

	public void setUploadedDate(String uploadedDateView) {
		this.uploadedDateView = uploadedDateView;
	}

	public String getIvrDataCount() {
		return ivrDataCount;
	}

	public void setIvrDataCount(String ivrDataCount) {
		this.ivrDataCount = ivrDataCount;
	}

	public String getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(String questionOption) {
		this.questionOption = questionOption;
	}

	public String getResponded() {
		return responded;
	}

	public void setResponded(String responded) {
		this.responded = responded;
	}
	

}
