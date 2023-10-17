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
@Table(name="t_wr_notification")
public class NotificationEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_notify_id")
	private BigInteger notifyId; 
	
	@Column(name="int_org_id")
	private Short organizationId;
	
	@Column(name="int_rc_id")
	private Short rcId;
	
	@Column(name="vch_notification_msg")
	private String notificationMsg;
	
	@Column(name="bitstatus")
	private Boolean status;
	
	@Column(name="intcreatedby")
	private Integer createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private Short updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;

	@Column(name="int_season_id")
	private Short seasonId;
	
	@Column(name="int_year")
	private Short year;
	
	@Column(name="bit_send_status")
	private Boolean sendStatus;
	
	@Column(name="dtm_send_date")
	private Date sendDate;
	
	@Column(name="dt_notification_date")
	private Date notificationDate;
	
	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public BigInteger  getNotifyId() {
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

	public Integer getCreatedBy() {
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

	public void setNotifyId(BigInteger  notifyId) {
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

	public void setCreatedBy(Integer createdBy) {
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

	public Short getSeasonId() {
		return seasonId;
	}

	public Short getYear() {
		return year;
	}

	public Boolean getSendStatus() {
		return sendStatus;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSeasonId(Short seasonId) {
		this.seasonId = seasonId;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public void setSendStatus(Boolean sendStatus) {
		this.sendStatus = sendStatus;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	
	
}
