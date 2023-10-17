
package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_forecasting_file")
public class GisMapEntity {

	@Id
	@Column(name = "int_forecasting_file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer forecastingId;

	@Column(name = "int_map_type_id")
	private Integer mapTypeId;

	@Column(name = "int_rust_type_id")
	private Integer rustTypeId;

	@Column(name = "vch_file_name")
	private String fileName;

	@Column(name = "vch_store_name")
	private String storeName;

	@Column(name = "vch_layer_name")
	private String layerName;

	@Column(name = "bitstatus")
	private boolean bitStatus;

	@Column(name = "dtmcreatedon")
	public Date Createdon;

	@Column(name = "dtmupdatedon")
	public Date Updatedon;
	
	@Column(name = "dtmuploadon")
	public Date uploadOn;
	
	public Integer getForecastingId() {
		return forecastingId;
	}

	public void setForecastingId(Integer forecastingId) {
		this.forecastingId = forecastingId;
	}

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

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Date getCreatedon() {
		return Createdon;
	}

	public void setCreatedon(Date createdon) {
		Createdon = createdon;
	}

	public Date getUpdatedon() {
		return Updatedon;
	}

	public void setUpdatedon(Date updatedon) {
		Updatedon = updatedon;
	}

	public Date getUploadOn() {
		return uploadOn;
	}

	public void setUploadOn(Date uploadOn) {
		this.uploadOn = uploadOn;
	}

	
	
}
