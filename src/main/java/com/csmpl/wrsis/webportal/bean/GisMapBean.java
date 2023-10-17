package com.csmpl.wrsis.webportal.bean;

import java.util.Date;

public class GisMapBean {

	private Integer mapTypeId;
	private Integer rustTypeId;
	private String fileName;
	private String storeName;
	private String layerName;
	private Date uploadOn;

	public Integer getMapTypeId() {
		return mapTypeId;
	}

	public void setMapTypeId(Integer mapTypeId) {
		this.mapTypeId = mapTypeId;
	}

	public Integer getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(Integer rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public Date getUploadOn() {
		return uploadOn;
	}

	public void setUploadOn(Date uploadOn) {
		this.uploadOn = uploadOn;
	}
	
	

}
