package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.csmpl.adminconsole.webportal.entity.User;

@Entity
@Table(name = "t_wr_user_location_tag")
public class DemographicLocationTagEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_user_loc_tag_id")
	private int locationTagId;
	
	@ManyToOne
    @JoinColumn(name ="int_user_id")
    private User userId;
	
	
    @Column(name = "int_level_id")
    private int levelId;
	
    
	@Column(name = "bitstatus")
    private boolean status;
    @Column(name = "intcreatedby")
    private int creadtedBy;
    @Column(name = "dtmcreatedon")
    private Date createdOn;
    @Column(name = "intupdatedby")
    private int updatedBy;
    @Column(name = "dtmupdatedon")
    private Date updatedOn;
	
    public int getLocationTagId() {
		return locationTagId;
	}
	public void setLocationTagId(int locationTagId) {
		this.locationTagId = locationTagId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCreadtedBy() {
		return creadtedBy;
	}
	public void setCreadtedBy(int creadtedBy) {
		this.creadtedBy = creadtedBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	
    
    
    
    
    
}
