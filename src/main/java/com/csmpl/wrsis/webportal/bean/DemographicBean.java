package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;

public class DemographicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8604688657386947990L;

	private int demographyId;

	private String demographyName;

	private int levelId;

	private int parentId;

	private String alias;

	private boolean status;

	private int createdBy;

	private Date createOn;

	private int updateBy;

	private Date updateOn;

	private int regionId;
	private String regionName;

	private int countryId;
	private String countryName;

	private int zoneId;
	private String zoneName;

	private int woredaId;
	private String woredaName;

	private int kebeleId;
	private String kebeleName;

	private String description;
	private int userId;

	private String countryNameByParentId;

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public int getWoredaId() {
		return woredaId;
	}

	public void setWoredaId(int woredaId) {
		this.woredaId = woredaId;
	}

	public String getWoredaName() {
		return woredaName;
	}

	public void setWoredaName(String woredaName) {
		this.woredaName = woredaName;
	}

	public int getKebeleId() {
		return kebeleId;
	}

	public void setKebeleId(int kebeleId) {
		this.kebeleId = kebeleId;
	}

	public String getKebeleName() {
		return kebeleName;
	}

	public void setKebeleName(String kebeleName) {
		this.kebeleName = kebeleName;
	}

	public String getCountryNameByParentId() {
		return countryNameByParentId;
	}

	public void setCountryNameByParentId(String countryNameByParentId) {
		this.countryNameByParentId = countryNameByParentId;
	}

	public int getDemographyId() {
		return demographyId;
	}

	public void setDemographyId(int demographyId) {
		this.demographyId = demographyId;
	}

	public String getDemographyName() {
		return demographyName;
	}

	public void setDemographyName(String demographyName) {
		this.demographyName = demographyName;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public int getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
