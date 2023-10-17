package com.csmpl.wrsis.webportal.bean;

import java.util.Date;

public class MonitorBean {

	private int moniterBeanId;
	private String moniterBeanMoniterNumber;
	private int recommendBeanId;
	private String recommendBeanNumber;
	private Date recommendBeandate;
	private String recommendBeanFileName;
	private Integer sNo;
	private String actionLisnk;
	private String viewRCDate;

	public int getMoniterBeanId() {
		return moniterBeanId;
	}

	public void setMoniterBeanId(int moniterBeanId) {
		this.moniterBeanId = moniterBeanId;
	}

	public String getMoniterBeanMoniterNumber() {
		return moniterBeanMoniterNumber;
	}

	public void setMoniterBeanMoniterNumber(String moniterBeanMoniterNumber) {
		this.moniterBeanMoniterNumber = moniterBeanMoniterNumber;
	}

	public int getRecommendBeanId() {
		return recommendBeanId;
	}

	public void setRecommendBeanId(int recommendBeanId) {
		this.recommendBeanId = recommendBeanId;
	}

	public String getRecommendBeanNumber() {
		return recommendBeanNumber;
	}

	public void setRecommendBeanNumber(String recommendBeanNumber) {
		this.recommendBeanNumber = recommendBeanNumber;
	}

	public Date getRecommendBeandate() {
		return recommendBeandate;
	}

	public void setRecommendBeandate(Date recommendBeandate) {
		this.recommendBeandate = recommendBeandate;
	}

	public String getRecommendBeanFileName() {
		return recommendBeanFileName;
	}

	public void setRecommendBeanFileName(String recommendBeanFileName) {
		this.recommendBeanFileName = recommendBeanFileName;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getActionLisnk() {
		return actionLisnk;
	}

	public void setActionLisnk(String actionLisnk) {
		this.actionLisnk = actionLisnk;
	}

	public String getViewRCDate() {
		return viewRCDate;
	}

	public void setViewRCDate(String viewRCDate) {
		this.viewRCDate = viewRCDate;
	}

}
