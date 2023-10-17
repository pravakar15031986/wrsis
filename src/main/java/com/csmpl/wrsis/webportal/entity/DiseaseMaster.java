package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_disease")
public class DiseaseMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_disease_id") 
	private int diseaseId;
	
    @Column(name="vch_disease")
    private String diseaseName;
    
    @Column(name="vch_disease_alias")
    private String diseaseAliasName;
    
    @Column(name="vch_desc")
    private String description;
    
    @Column(name="bitstatus")
    private boolean status;
    
    @Column(name="intcreatedby")
    private Integer createdBy;
    
    @Column(name="dtmcreatedon")
    private Date createdOn;
    
    @Column(name="intupdatedby")
    private Integer updatedBy;
    
    @Column(name="dtmupdatedon")
    private Date updateOn;

    @Column(name = "bit_other_dtls_required")
	private boolean otherDetails;
    
    @Column(name="int_severity_min")
    private Short severityMin;
    
    @Column(name="int_severity_max")
    private Short severityMax;
    
    @Column(name="bit_severity_required")
    private Boolean severityRequired;
    
    
    

	public Boolean getSeverityRequired() {
		return severityRequired;
	}

	public void setSeverityRequired(Boolean severityRequired) {
		this.severityRequired = severityRequired;
	}

	public Short getSeverityMin() {
		return severityMin;
	}

	public void setSeverityMin(Short severityMin) {
		this.severityMin = severityMin;
	}

	public Short getSeverityMax() {
		return severityMax;
	}

	public void setSeverityMax(Short severityMax) {
		this.severityMax = severityMax;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getDiseaseAliasName() {
		return diseaseAliasName;
	}

	public void setDiseaseAliasName(String diseaseAliasName) {
		this.diseaseAliasName = diseaseAliasName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public int getDiseaseId() {
		return diseaseId;
	}

	public void setDiseaseId(int diseaseId) {
		this.diseaseId = diseaseId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(boolean otherDetails) {
		this.otherDetails = otherDetails;
	}
    
    
}
