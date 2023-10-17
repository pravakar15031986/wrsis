package com.csmpl.wrsis.webportal.bean;

import java.util.Map;

import com.csmpl.adminconsole.webportal.entity.User;

public class DemographicLocationTagBean {

	private int locationTagIdBean;
	private User userIdBean;

	private String[] regionIdBean;

	private String[] zoneIdBean;

	private String[] woredaIdBean;

	private String[] kebeleIdBean;

	private boolean statusBean;
	private int creadtedByBean;

	private Map<String, String> totalWoridaBeanID;
	private Map<String, String> checkWoridaBeanID;
	private Map<String, String> totalKebeleBeanID;
	private Map<String, String> checkKebeleBeanID;

	public int getLocationTagIdBean() {
		return locationTagIdBean;
	}

	public void setLocationTagIdBean(int locationTagIdBean) {
		this.locationTagIdBean = locationTagIdBean;
	}

	public User getUserIdBean() {
		return userIdBean;
	}

	public void setUserIdBean(User userIdBean) {
		this.userIdBean = userIdBean;
	}

	public String[] getRegionIdBean() {
		return regionIdBean;
	}

	public void setRegionIdBean(String[] regionIdBean) {
		this.regionIdBean = regionIdBean;
	}

	public String[] getZoneIdBean() {
		return zoneIdBean;
	}

	public void setZoneIdBean(String[] zoneIdBean) {
		this.zoneIdBean = zoneIdBean;
	}

	public String[] getWoredaIdBean() {
		return woredaIdBean;
	}

	public void setWoredaIdBean(String[] woredaIdBean) {
		this.woredaIdBean = woredaIdBean;
	}

	public String[] getKebeleIdBean() {
		return kebeleIdBean;
	}

	public void setKebeleIdBean(String[] kebeleIdBean) {
		this.kebeleIdBean = kebeleIdBean;
	}

	public boolean isStatusBean() {
		return statusBean;
	}

	public void setStatusBean(boolean statusBean) {
		this.statusBean = statusBean;
	}

	public int getCreadtedByBean() {
		return creadtedByBean;
	}

	public void setCreadtedByBean(int creadtedByBean) {
		this.creadtedByBean = creadtedByBean;
	}

	public Map<String, String> getCheckWoridaBeanID() {
		return checkWoridaBeanID;
	}

	public void setCheckWoridaBeanID(Map<String, String> checkWoridaBeanID) {
		this.checkWoridaBeanID = checkWoridaBeanID;
	}

	public Map<String, String> getCheckKebeleBeanID() {
		return checkKebeleBeanID;
	}

	public void setCheckKebeleBeanID(Map<String, String> checkKebeleBeanID) {
		this.checkKebeleBeanID = checkKebeleBeanID;
	}

	public Map<String, String> getTotalWoridaBeanID() {
		return totalWoridaBeanID;
	}

	public void setTotalWoridaBeanID(Map<String, String> totalWoridaBeanID) {
		this.totalWoridaBeanID = totalWoridaBeanID;
	}

	public Map<String, String> getTotalKebeleBeanID() {
		return totalKebeleBeanID;
	}

	public void setTotalKebeleBeanID(Map<String, String> totalKebeleBeanID) {
		this.totalKebeleBeanID = totalKebeleBeanID;
	}

}
