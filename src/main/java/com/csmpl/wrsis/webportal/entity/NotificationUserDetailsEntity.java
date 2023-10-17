package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_notification_user_details")
public class NotificationUserDetailsEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_notify_user_id")
	private BigInteger notifyUserId;
	
	@Column(name="int_notify_id")
	private BigInteger notifyId;
	
	@Column(name="int_user_id")
	private Short userId;
	
	@Column(name="bitstatus")
	private Boolean status;

	public BigInteger getNotifyUserId() {
		return notifyUserId;
	}

	public BigInteger getNotifyId() {
		return notifyId;
	}

	public Short getUserId() {
		return userId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setNotifyUserId(BigInteger notifyUserId) {
		this.notifyUserId = notifyUserId;
	}

	public void setNotifyId(BigInteger  notifyId) {
		this.notifyId = notifyId;
	}

	public void setUserId(Short userId) {
		this.userId = userId;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	
	
}
