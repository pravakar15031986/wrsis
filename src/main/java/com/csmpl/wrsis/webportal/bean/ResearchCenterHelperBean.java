package com.csmpl.wrsis.webportal.bean;

import java.util.List;
import java.util.Map;

public class ResearchCenterHelperBean {

	private int researchCenterBeanId;
	private String researchCenterBeanName;
	private boolean labStatusBean;
	private String descriptionBean;
	private boolean statusBean;
	private int createdByBean;

	private int researCenterLocationBeanId;
	private int regionBeanId;
	private int zoneBeanId;
	private String zoneNameBean;
	private String[] woredaBeanId;
	private List<String> woridaBeanName;
	private int centerSpelizeBeanId;
	private String[] rustBeanId;
	private List<String> woridaBeanIDEdit;

	private Map<String, String> checkWoridaBeanID;

	private List<String> rustBeanIDEdit;

	private Map<String, String> checkWoridaListBean;

	public int getResearchCenterBeanId() {
		return researchCenterBeanId;
	}

	public void setResearchCenterBeanId(int researchCenterBeanId) {
		this.researchCenterBeanId = researchCenterBeanId;
	}

	public String getResearchCenterBeanName() {
		return researchCenterBeanName;
	}

	public void setResearchCenterBeanName(String researchCenterBeanName) {
		this.researchCenterBeanName = researchCenterBeanName;
	}

	public boolean isLabStatusBean() {
		return labStatusBean;
	}

	public void setLabStatusBean(boolean labStatusBean) {
		this.labStatusBean = labStatusBean;
	}

	public String getDescriptionBean() {
		return descriptionBean;
	}

	public void setDescriptionBean(String descriptionBean) {
		this.descriptionBean = descriptionBean;
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

	public int getResearCenterLocationBeanId() {
		return researCenterLocationBeanId;
	}

	public void setResearCenterLocationBeanId(int researCenterLocationBeanId) {
		this.researCenterLocationBeanId = researCenterLocationBeanId;
	}

	public int getRegionBeanId() {
		return regionBeanId;
	}

	public void setRegionBeanId(int regionBeanId) {
		this.regionBeanId = regionBeanId;
	}

	public int getZoneBeanId() {
		return zoneBeanId;
	}

	public void setZoneBeanId(int zoneBeanId) {
		this.zoneBeanId = zoneBeanId;
	}

	public String[] getWoredaBeanId() {
		return woredaBeanId;
	}

	public void setWoredaBeanId(String[] woredaBeanId) {
		this.woredaBeanId = woredaBeanId;
	}

	public int getCenterSpelizeBeanId() {
		return centerSpelizeBeanId;
	}

	public void setCenterSpelizeBeanId(int centerSpelizeBeanId) {
		this.centerSpelizeBeanId = centerSpelizeBeanId;
	}

	public String[] getRustBeanId() {
		return rustBeanId;
	}

	public void setRustBeanId(String[] rustBeanId) {
		this.rustBeanId = rustBeanId;
	}

	public String getZoneNameBean() {
		return zoneNameBean;
	}

	public void setZoneNameBean(String zoneNameBean) {
		this.zoneNameBean = zoneNameBean;
	}

	public List<String> getWoridaBeanName() {
		return woridaBeanName;
	}

	public void setWoridaBeanName(List<String> woridaBeanName) {
		this.woridaBeanName = woridaBeanName;
	}

	public List<String> getWoridaBeanIDEdit() {
		return woridaBeanIDEdit;
	}

	public void setWoridaBeanIDEdit(List<String> woridaBeanIDEdit) {
		this.woridaBeanIDEdit = woridaBeanIDEdit;
	}

	public List<String> getRustBeanIDEdit() {
		return rustBeanIDEdit;
	}

	public void setRustBeanIDEdit(List<String> rustBeanIDEdit) {
		this.rustBeanIDEdit = rustBeanIDEdit;
	}

	public Map<String, String> getCheckWoridaBeanID() {
		return checkWoridaBeanID;
	}

	public void setCheckWoridaBeanID(Map<String, String> checkWoridaBeanID) {
		this.checkWoridaBeanID = checkWoridaBeanID;
	}

	public Map<String, String> getCheckWoridaListBean() {
		return checkWoridaListBean;
	}

	public void setCheckWoridaListBean(Map<String, String> checkWoridaListBean) {
		this.checkWoridaListBean = checkWoridaListBean;
	}

}
