package com.csmpl.wrsis.webportal.bean;

public class GisBean {
	private String mapType;

	private String fileName;

	private String storeName;

	private String layerName;

	private String createdOn;

	private String rustType;

	private Boolean bitStatus;

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
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

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getRustType() {
		return rustType;
	}

	public void setRustType(String rustType) {
		this.rustType = rustType;
	}

	@Override
	public String toString() {
		return "GisBean [mapType=" + mapType + ", fileName=" + fileName + ", storeName=" + storeName + ", layerName="
				+ layerName + ", createdOn=" + createdOn + ", rustType=" + rustType + ", bitStatus=" + bitStatus + "]";
	}

}
