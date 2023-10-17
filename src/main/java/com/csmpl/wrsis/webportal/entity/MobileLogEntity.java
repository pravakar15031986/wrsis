package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_mobile_service_log")
public class MobileLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_mob_ser_log_id")
	private Integer intMobSerLogId;

	@Column(name = "vch_ip_address")
	private String ipAddress;

	@Column(name = "vch_device_id")
	private String deviceId;

	@Column(name = "int_user_id")
	private Integer userId;

	@Column(name = "vch_service_name")
	private String serviceName;

	@Column(name = "vch_req_details")
	private String reqDetails;

	@Column(name = "dtm_req_time")
	private Date reqTime;

	@Column(name = "vch_res_details")
	private String resDetails;

	@Column(name = "dtm_res_time")
	private Date resTime;

	@Column(name = "bit_status")
	private boolean bitStatus;

	public Integer getIntMobSerLogId() {
		return intMobSerLogId;
	}

	public void setIntMobSerLogId(Integer intMobSerLogId) {
		this.intMobSerLogId = intMobSerLogId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getReqDetails() {
		return reqDetails;
	}

	public void setReqDetails(String reqDetails) {
		this.reqDetails = reqDetails;
	}

	public Date getReqTime() {
		return reqTime;
	}

	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}

	public String getResDetails() {
		return resDetails;
	}

	public void setResDetails(String resDetails) {
		this.resDetails = resDetails;
	}

	public Date getResTime() {
		return resTime;
	}

	public void setResTime(Date resTime) {
		this.resTime = resTime;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	@Override
	public String toString() {
		return "MobileLogEntity [intMobSerLogId=" + intMobSerLogId + ", ipAddress=" + ipAddress + ", deviceId="
				+ deviceId + ", userId=" + userId + ", serviceName=" + serviceName + ", reqDetails=" + reqDetails
				+ ", reqTime=" + reqTime + ", resDetails=" + resDetails + ", resTime=" + resTime + ", bitStatus="
				+ bitStatus + "]";
	}

}
