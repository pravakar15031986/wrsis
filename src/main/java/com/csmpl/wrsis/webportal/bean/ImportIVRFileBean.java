package com.csmpl.wrsis.webportal.bean;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ImportIVRFileBean {

	private int imprtIVRIdBean;

	private MultipartFile fileNameBEan;

	private int recordCountBean;

	private boolean statusBean;

	private int createdByBean;

	private List<IVRExcelCellBean> cellBeanList;

	private String excelBeanListString;

	private String file_Name;

	private List<Integer> excelQuestionList;

	private String mobile;
	private String callDate;
	private String regionName;
	private String zoneName;
	private String woredaName;
	private String pageQustion;
	private String ivrQustion;
	private List<String> ivrQuestionsData;
	private String questionOption;

	public int getImprtIVRIdBean() {
		return imprtIVRIdBean;
	}

	public void setImprtIVRIdBean(int imprtIVRIdBean) {
		this.imprtIVRIdBean = imprtIVRIdBean;
	}

	public MultipartFile getFileNameBEan() {
		return fileNameBEan;
	}

	public void setFileNameBEan(MultipartFile fileNameBEan) {
		this.fileNameBEan = fileNameBEan;
	}

	public int getRecordCountBean() {
		return recordCountBean;
	}

	public void setRecordCountBean(int recordCountBean) {
		this.recordCountBean = recordCountBean;
	}

	public boolean isStatusBean() {
		return statusBean;
	}

	public void setStatusBean(boolean statusBean) {
		this.statusBean = statusBean;
	}

	public int getCreatedByBean() {
		return createdByBean;
	}

	public void setCreatedByBean(int createdByBean) {
		this.createdByBean = createdByBean;
	}

	public List<IVRExcelCellBean> getCellBeanList() {
		return cellBeanList;
	}

	public void setCellBeanList(List<IVRExcelCellBean> cellBeanList) {
		this.cellBeanList = cellBeanList;
	}

	public String getExcelBeanListString() {
		return excelBeanListString;
	}

	public void setExcelBeanListString(String excelBeanListString) {
		this.excelBeanListString = excelBeanListString;
	}

	public String getFile_Name() {
		return file_Name;
	}

	public void setFile_Name(String file_Name) {
		this.file_Name = file_Name;
	}

	public List<Integer> getExcelQuestionList() {
		return excelQuestionList;
	}

	public void setExcelQuestionList(List<Integer> excelQuestionList) {
		this.excelQuestionList = excelQuestionList;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
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

	public String getPageQustion() {
		return pageQustion;
	}

	public void setPageQustion(String pageQustion) {
		this.pageQustion = pageQustion;
	}

	public String getIvrQustion() {
		return ivrQustion;
	}

	public void setIvrQustion(String ivrQustion) {
		this.ivrQustion = ivrQustion;
	}

	public List<String> getIvrQuestionsData() {
		return ivrQuestionsData;
	}

	public void setIvrQuestionsData(List<String> ivrQuestionsData) {
		this.ivrQuestionsData = ivrQuestionsData;
	}

	public String getQuestionOption() {
		return questionOption;
	}

	public void setQuestionOption(String questionOption) {
		this.questionOption = questionOption;
	}

}
