package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_dashboard_notification")
public class DashboardNotificationEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_dashboard_notify_id")
	private BigInteger dashboardNotifyId;
	
	@Column(name="int_to_user_id")
	private Short toUserId;
	
	@Column(name="vch_notification_msg")
	private String notificationMsg;
	
	@Column(name="bit_read_status")
	private Boolean readStatus;
	
	@Column(name="bitstatus")
	private Boolean status;
	
	@Column(name="intcreatedby")
	private Short createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private Short updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;

	public BigInteger getDashboardNotifyId() {
		return dashboardNotifyId;
	}

	public Short getToUserId() {
		return toUserId;
	}

	public String getNotificationMsg() {
		return notificationMsg;
	}

	public Boolean getReadStatus() {
		return readStatus;
	}

	public Boolean getStatus() {
		return status;
	}

	public Short getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public Short getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setDashboardNotifyId(BigInteger dashboardNotifyId) {
		this.dashboardNotifyId = dashboardNotifyId;
	}

	public void setToUserId(Short toUserId) {
		this.toUserId = toUserId;
	}

	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}

	public void setReadStatus(Boolean readStatus) {
		this.readStatus = readStatus;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setCreatedBy(Short createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedBy(Short updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
