package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_recom_forward_log")
public class RecommendationForwardLogEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="int_recom_forward_log_id")
	private short recForwardLogId;
	
	@Column(name="int_recom_id")
	private short recId;
	
	@Column(name="int_region_id")
	private short regionId;
	
	@Column(name="int_forward_by")
	private short forwardBy;
	
	@Column(name="dtm_forward_on")
	private Date forward;
	
	@Column(name="vch_forward_remarks")
	private String forwardRemarks;
	
	@Column(name="bitstatus")
	private boolean status;

	public short getRecForwardLogId() {
		return recForwardLogId;
	}

	public void setRecForwardLogId(short recForwardLogId) {
		this.recForwardLogId = recForwardLogId;
	}

	public short getRecId() {
		return recId;
	}

	public void setRecId(short recId) {
		this.recId = recId;
	}

	public short getRegionId() {
		return regionId;
	}

	public void setRegionId(short regionId) {
		this.regionId = regionId;
	}

	public short getForwardBy() {
		return forwardBy;
	}

	public void setForwardBy(short forwardBy) {
		this.forwardBy = forwardBy;
	}

	public Date getForward() {
		return forward;
	}

	public void setForward(Date forward) {
		this.forward = forward;
	}

	public String getForwardRemarks() {
		return forwardRemarks;
	}

	public void setForwardRemarks(String forwardRemarks) {
		this.forwardRemarks = forwardRemarks;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
