package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_season_month")
public class SeasonMonth {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_season_month_id")
	private Integer seasonMonthId;
	
	@Column(name="int_season_id")
    private int seasonId;
	
	@Column(name="int_month_id")
	private Integer monthId;
	
	@Column(name="vch_month")
	private String monthname;
	
    @Column(name="bitstatus")
    private boolean status;
    
    @Column(name="intcreatedby")
    private Integer createdBy;
    @Column(name="dtmcreatedon")
    private Date createdOn;
    @Column(name="intupdatedby")
    private Integer updateBy;
    @Column(name="dtmupdatedon")
    private Date updateOn;
	public Integer getSeasonMonthId() {
		return seasonMonthId;
	}
	public void setSeasonMonthId(Integer seasonMonthId) {
		this.seasonMonthId = seasonMonthId;
	}
	public int getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}
	public Integer getMonthId() {
		return monthId;
	}
	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}
	public String getMonthname() {
		return monthname;
	}
	public void setMonthname(String monthname) {
		this.monthname = monthname;
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
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateOn() {
		return updateOn;
	}
	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}
	
        

}
