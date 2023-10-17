package com.csmpl.wrsis.webportal.bean;

import java.util.ArrayList;
import java.util.Date;

public class SeasonMonthBean {

	private int seasonIdBean;

	private String seasonName;
	private String[] monthId;
	private String desc;
	private boolean status;
	private Integer createdBy;

	private ArrayList<Integer> monthBeanId;

	private int monthIdBean;

	private ArrayList<String> dispMonthName;

	private Date createdOnBean;

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	private String monthName;

	public int getSeasonIdBean() {
		return seasonIdBean;
	}

	public void setSeasonIdBean(int seasonIdBean) {
		this.seasonIdBean = seasonIdBean;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public String[] getMonthId() {
		return monthId;
	}

	public void setMonthId(String[] monthId) {
		this.monthId = monthId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public ArrayList<Integer> getMonthBeanId() {
		return monthBeanId;
	}

	public void setMonthBeanId(ArrayList<Integer> monthBeanId) {
		this.monthBeanId = monthBeanId;
	}

	public int getMonthIdBean() {
		return monthIdBean;
	}

	public void setMonthIdBean(int monthIdBean) {
		this.monthIdBean = monthIdBean;
	}

	public ArrayList<String> getDispMonthName() {
		return dispMonthName;
	}

	public void setDispMonthName(ArrayList<String> dispMonthName) {
		this.dispMonthName = dispMonthName;
	}

	public Date getCreatedOnBean() {
		return createdOnBean;
	}

	public void setCreatedOnBean(Date createdOnBean) {
		this.createdOnBean = createdOnBean;
	}

}
