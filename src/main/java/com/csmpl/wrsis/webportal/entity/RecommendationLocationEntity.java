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

@Entity
@Table(name="t_wr_recommendation_loc")
public class RecommendationLocationEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_recom_loc_id")
	private Integer reomendLocationId;
	
	@ManyToOne
    @JoinColumn(name="int_recom_id")
    private RecommendationEntiry recomendId;
	
    @Column(name="vch_recom_no")
    private String recomendNo;
    
    @Column(name="int_region_id")
    private int regionId;
    
    @Column(name="int_zone_id")
    private int zoneId;
    
    @Column(name="int_woreda_id")
    private int woredaId;
    
    @Column(name="int_kebel_id")
    private int kebeleId;
    
    @Column(name="bitstatus")
    private boolean status;
    
    @Column(name="intcreatedby")
    private int createdBy;
    
    @Column(name="dtmcreatedon")
    private Date createdOn;
    @Column(name="intupdatedby")
    private int updatedBy;
    @Column(name="dtmupdatedon")
    private Date updatedOn;
	public Integer getReomendLocationId() {
		return reomendLocationId;
	}
	public void setReomendLocationId(Integer reomendLocationId) {
		this.reomendLocationId = reomendLocationId;
	}
	public RecommendationEntiry getRecomendId() {
		return recomendId;
	}
	public void setRecomendId(RecommendationEntiry recomendId) {
		this.recomendId = recomendId;
	}
	public String getRecomendNo() {
		return recomendNo;
	}
	public void setRecomendNo(String recomendNo) {
		this.recomendNo = recomendNo;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public int getWoredaId() {
		return woredaId;
	}
	public void setWoredaId(int woredaId) {
		this.woredaId = woredaId;
	}
	public int getKebeleId() {
		return kebeleId;
	}
	public void setKebeleId(int kebeleId) {
		this.kebeleId = kebeleId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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
    
}
