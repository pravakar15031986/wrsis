package com.csmpl.wrsis.webportal.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_app_tracking")
public class MobileLoginTracking {

	@Id
	@Column(name = "int_app_track_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trackId;

	@Column(name = "vch_ipaddress")
	private String ipaddress;

	@Column(name = "vch_os_name")
	private String osName;

	@Column(name = "vch_device_id")
	private String deviceName;

	@Column(name = "vch_subscriber_id")
	private String subscriberName;

	@Column(name = "vch_imei_no")
	private String imeiNo;

	@Column(name = "vch_fcm_id")
	private String fcmName;

	@Column(name = "bit_login_status")
	private Boolean loginStatus;

	@Column(name = "int_created_by")
	private Integer createdBy;

	@Column(name = "dtm_created_on")
	private Timestamp createdOn;

	@Column(name = "int_updated_by")
	private Integer updatedBy;

	@Column(name = "dtm_updated_on")
	private Timestamp updatedOn;

	@Column(name = "bit_status")
	private Boolean bitStatus;

	public Integer getTrackId() {
		return trackId;
	}

	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getFcmName() {
		return fcmName;
	}

	public void setFcmName(String fcmName) {
		this.fcmName = fcmName;
	}

	public Boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

}
