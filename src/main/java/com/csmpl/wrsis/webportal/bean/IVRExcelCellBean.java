package com.csmpl.wrsis.webportal.bean;

import java.util.List;

public class IVRExcelCellBean {

	private String phone;
	private String zone;
	private String woreda;
	private String language;
	private String qustion_1;
	private String qustion_2;
	private String qustion_3;
	private String fileName;
	private String pageQuestion;
	private Object[] pageQuestions;

	private List<String> qustions;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getQustion_1() {
		return qustion_1;
	}

	public void setQustion_1(String qustion_1) {
		this.qustion_1 = qustion_1;
	}

	public String getQustion_2() {
		return qustion_2;
	}

	public void setQustion_2(String qustion_2) {
		this.qustion_2 = qustion_2;
	}

	public String getQustion_3() {
		return qustion_3;
	}

	public void setQustion_3(String qustion_3) {
		this.qustion_3 = qustion_3;
	}

	public List<String> getQustions() {

		return qustions;
	}

	public void setQustions(List<String> qustions) {

		this.qustions = qustions;
	}

	public String getPageQuestion() {
		return pageQuestion;
	}

	public void setPageQuestion(String pageQuestion) {
		this.pageQuestion = pageQuestion;
	}

	public Object[] getPageQuestions() {
		return pageQuestions;
	}

	public void setPageQuestions(Object[] pageQuestions) {
		this.pageQuestions = pageQuestions;
	}

}
