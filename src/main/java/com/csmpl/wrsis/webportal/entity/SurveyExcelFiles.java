package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_wr_import_survey_files")
public class SurveyExcelFiles {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_import_survey_id")
    private int fileSurveyId;
   
	@Column(name="vch_survey_file_name")
    private String fileName;
	 
	@Column(name="int_survey_record_count")
	private Integer count;
	
    @Column(name="bitstatus")
    private boolean status;
    
    @Column(name="intcreatedby")
    private Integer createdBy;
    @Column(name="dtmcreatedon")
    private Date createdOn;
    
    @Column(name="intupdatedby")
    private Integer updatedBy;
    @Column(name="dtmupdatedon")
    private Date updatedOn;
    
    
    public String getFileDecodeName() {
		return fileDecodeName;
	}
	public void setFileDecodeName(String fileDecodeName) {
		this.fileDecodeName = fileDecodeName;
	}
	@Transient
	private String  fileDecodeName;
	 
	public int getFileSurveyId() {
		return fileSurveyId;
	}
	public void setFileSurveyId(int fileSurveyId) {
		this.fileSurveyId = fileSurveyId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
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
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
    
    
}
