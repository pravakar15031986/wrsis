package com.csmpl.wrsis.webportal.bean;

public class MobileLogViewBean {

	private Integer sNo;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	private Integer intMobSerLogId;

	private String ipAddress;

	private String deviceId;

	private Integer userId;

	private String serviceName;

	private String reqDetails;

	private String reqTime;

	private String resDetails;

	private String resTime;

	private String organisation;

	private String fullName;

	private String designation;

	private String roll;

	private boolean bitStatus;

	private String userName;
	private String modalLink;
	private String modalLinkRes;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String string) {
		this.reqTime = string;
	}

	public String getResDetails() {
		return resDetails;
	}

	public void setResDetails(String resDetails) {
		this.resDetails = resDetails;
	}

	public String getResTime() {
		return resTime;
	}

	public void setResTime(String string) {
		this.resTime = string;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getRoll() {
		return roll;
	}

	public void setRoll(String roll) {
		this.roll = roll;
	}

	public String getModalLink() {
		return modalLink;
	}

	public void setModalLink(String modalLink) {
		this.modalLink = modalLink;
	}

	public String getModalLinkRes() {
		return modalLinkRes;
	}

	public void setModalLinkRes(String modalLinkRes) {
		this.modalLinkRes = modalLinkRes;
	}

	@Override
	public String toString() {
		return "MobileLogViewBean [intMobSerLogId=" + intMobSerLogId + ", ipAddress=" + ipAddress + ", deviceId="
				+ deviceId + ", userId=" + userId + ", serviceName=" + serviceName + ", reqDetails=" + reqDetails
				+ ", reqTime=" + reqTime + ", resDetails=" + resDetails + ", resTime=" + resTime + ", organisation="
				+ organisation + ", fullName=" + fullName + ", designation=" + designation + ", roll=" + roll
				+ ", bitStatus=" + bitStatus + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
