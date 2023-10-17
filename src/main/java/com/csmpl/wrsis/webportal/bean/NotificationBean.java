package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class NotificationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger notifyId;

	private Short organizationId;

	private Short rcId;

	private String notificationMsg;

	private Boolean status;

	private Integer createdbY;

	private String createdOn;

	private Integer updatedy;

	private Date updatedOn;

	private Integer notifyUserId;

	private Short userId;

	private int roleId;

	private String orgName;

	private String rcName;

	private String userName;

	private String fullName;

	private String sendStatus;

	private String sendDate;

	private String ntfDate;

	private Integer sNo;
	private String userInfo;
	private String editLink;

	public String getSendStatus() {
		return sendStatus;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getUserName() {
		return userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getRcName() {
		return rcName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setRcName(String rcName) {
		this.rcName = rcName;
	}

	public int getRoleId() {
		return roleId;
	}

	public List<Short> getSelectedUsers() {
		return selectedUsers;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setSelectedUsers(List<Short> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	private List<Short> selectedUsers;

	public BigInteger getNotifyId() {
		return notifyId;
	}

	public Short getOrganizationId() {
		return organizationId;
	}

	public Short getRcId() {
		return rcId;
	}

	public String getNotificationMsg() {
		return notificationMsg;
	}

	public Boolean getStatus() {
		return status;
	}

	public Integer getCreatedbY() {
		return createdbY;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public Integer getUpdatedy() {
		return updatedy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public Integer getNotifyUserId() {
		return notifyUserId;
	}

	public Short getUserId() {
		return userId;
	}

	public void setNotifyId(BigInteger notifyId) {
		this.notifyId = notifyId;
	}

	public void setOrganizationId(Short organizationId) {
		this.organizationId = organizationId;
	}

	public void setRcId(Short rcId) {
		this.rcId = rcId;
	}

	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setCreatedbY(Integer createdbY) {
		this.createdbY = createdbY;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedy(Integer updatedy) {
		this.updatedy = updatedy;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public void setNotifyUserId(Integer notifyUserId) {
		this.notifyUserId = notifyUserId;
	}

	public void setUserId(Short userId) {
		this.userId = userId;
	}

	public String getNtfDate() {
		return ntfDate;
	}

	public void setNtfDate(String ntfDate) {
		this.ntfDate = ntfDate;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getEditLink() {
		return editLink;
	}

	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}

}
