package com.csmpl.wrsis.webportal.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_details")
public class SurveyDetails {
	
	@Id
	@Column(name = "int_survey_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer surveyId;
	
	
	@Column(name = "vch_survey_number")
	private String surveyNumber;
	@Column(name = "dt_survey_date")
	private Date surveyDate;
	@Column(name = "int_season_id")
	private Integer seasonId;
	@Column(name = "int_zone_id")
	private Integer zoneId;
	
	
	@Column(name = "int_woreda_id")
	private Integer wordeaId;
	
	
	@Column(name = "int_kebel_id")
	private Integer kebelId;
	@Column(name = "vch_longitude")	
	private String longitude;
	@Column(name = "vch_latitude")
	private String latitude;
	@Column(name = "vch_altitude")
	private String altitude;
	@Column(name = "dt_planting_date")
	private Date planningDate;
	@Column(name = "dt_first_rust_observe_date")
	private Date observeDate;
	@Column(name = "int_is_farmer_contacted")
	private boolean farmerContacted;
	@Column(name = "int_is_rust_affected")
	private boolean rustAffected;
	@Column(name = "int_is_sample_collected")
	private boolean sampleCollected;
	@Column(name = "int_is_image_captured")
	private boolean imageCaptured;
	@Column(name = "int_is_fungicide_applied")
	private Boolean fungicideApplied;
	@Column(name = "vch_remarks")	
	private String remarks;
	@Column(name = "bitstatus")
	private boolean bitstatus;
	public Boolean getFungicideApplied() {
		return fungicideApplied;
	}

	@Column(name = "intcreatedby")
	private Integer createdBy;
	@Column(name = "dtmcreatedon")
	private Date createdOn;
	@Column(name = "intupdatedby")
	private Integer updatedBy;
	@Column(name = "dtmupdatedon")
	private Date updatedOn;
	@Column(name = "int_survey_status")	
	private Integer surveyStatus;
	@Column(name = "int_action_taken_by")
	private Integer actionBy;
	@Column(name = "dt_action_taken_on")	
	private Date actionOn;
	@Column(name = "vch_action_remarks")
	private String actionRemark;
	@Column(name = "int_data_collect_mode")
	private Integer collecMode;
	@Column(name="int_region_id")
	private Integer regionId;
	
	@Column(name="vch_location_details")
	private String locationDetails;
	
	@Column(name="dt_tillering_date")
	private Date tilleringDate;
	
	@Column(name="bit_lat_long_type")
	private Boolean decimaldegree;
	
	
	public Boolean getDecimaldegree() {
		return decimaldegree;
	}
	public void setDecimaldegree(Boolean decimaldegree) {
		this.decimaldegree = decimaldegree;
	}
	public Date getTilleringDate() {
		return tilleringDate;
	}
	public void setTilleringDate(Date tilleringDate) {
		this.tilleringDate = tilleringDate;
	}
	public String getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}

	@Column(name="vch_other_surveyors")
	private String otherSurveyor;
	
	public String getOtherSurveyor() {
		return otherSurveyor;
	}
	public void setOtherSurveyor(String otherSurveyor) {
		this.otherSurveyor = otherSurveyor;
	}
	
	@Column(name="vch_latitude_degree")
	private String degreeLatitude;
	
	@Column(name="vch_longitude_degree")
	private String degreeLongitude;
	
	

	public String getDegreeLatitude() {
		return degreeLatitude;
	}
	public void setDegreeLatitude(String degreeLatitude) {
		this.degreeLatitude = degreeLatitude;
	}
	public String getDegreeLongitude() {
		return degreeLongitude;
	}
	public void setDegreeLongitude(String degreeLongitude) {
		this.degreeLongitude = degreeLongitude;
	}

	@Column(name="int_import_survey_id")
	private Integer fileId;
	
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_region_id", referencedColumnName="int_demography_id")
	private DemographicEntity region;
	 
	
	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_zone_id", referencedColumnName="int_demography_id")
	private DemographicEntity zone;
	 
	
	public DemographicEntity getZone() {
		return zone;
	}
	public void setZone(DemographicEntity zone) {
		this.zone = zone;
	}

	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_season_id", referencedColumnName="int_season_id")
	private SeasionMasterEntity seasion;
	
	 
	public SeasionMasterEntity getSeasion() {
		return seasion;
	}
	public void setSeasion(SeasionMasterEntity seasion) {
		this.seasion = seasion;
	}
	public DemographicEntity getRegion() {
		return region;
	}
	public void setRegion(DemographicEntity region) {
		this.region = region;
	}
	
	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_woreda_id", referencedColumnName="int_demography_id")
	private DemographicEntity woreda;
	
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	
	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_kebel_id", referencedColumnName="int_demography_id")
	private DemographicEntity kebele;
	
	
	public DemographicEntity getWoreda() {
		return woreda;
	}
	public void setWoreda(DemographicEntity woreda) {
		this.woreda = woreda;
	}
	public DemographicEntity getKebele() {
		return kebele;
	}
	public void setKebele(DemographicEntity kebele) {
		this.kebele = kebele;
	}
	
	@OneToMany
	@JoinColumn(updatable=false,insertable=false,name="int_survey_id", referencedColumnName="int_survey_id")
	private List<SurveyRustDetailsEntity> rustDetails;
	
	
	public Integer getCollecMode() {
		return collecMode;
	}
	public void setCollecMode(Integer collecMode) {
		this.collecMode = collecMode;
	}
	public Integer getSurveyId() {
		return surveyId;
	}
	public List<SurveyRustDetailsEntity> getRustDetails() {
		return rustDetails;
	}
	public void setRustDetails(List<SurveyRustDetailsEntity> rustDetails) {
		this.rustDetails = rustDetails;
	}
	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}
	public String getSurveyNumber() {
		return surveyNumber;
	}
	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}
	public Date getSurveyDate() {
		return surveyDate;
	}
	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	public Integer getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}
	public Integer getZoneId() {
		return zoneId;
	}
	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}
	public Integer getWordeaId() {
		return wordeaId;
	}
	public void setWordeaId(Integer wordeaId) {
		this.wordeaId = wordeaId;
	}
	public Integer getKebelId() {
		return kebelId;
	}
	public void setKebelId(Integer kebelId) {
		this.kebelId = kebelId;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public Date getPlanningDate() {
		return planningDate;
	}
	public void setPlanningDate(Date planningDate) {
		this.planningDate = planningDate;
	}
	public Date getObserveDate() {
		return observeDate;
	}
	public void setObserveDate(Date observeDate) {
		this.observeDate = observeDate;
	}
	public boolean isFarmerContacted() {
		return farmerContacted;
	}
	public void setFarmerContacted(boolean farmerContacted) {
		this.farmerContacted = farmerContacted;
	}
	public boolean isRustAffected() {
		return rustAffected;
	}
	public void setRustAffected(boolean rustAffected) {
		this.rustAffected = rustAffected;
	}
	public boolean isSampleCollected() {
		return sampleCollected;
	}
	public void setSampleCollected(boolean sampleCollected) {
		this.sampleCollected = sampleCollected;
	}
	public boolean isImageCaptured() {
		return imageCaptured;
	}
	public void setImageCaptured(boolean imageCaptured) {
		this.imageCaptured = imageCaptured;
	}
	public Boolean isFungicideApplied() {
		return fungicideApplied;
	}
	public void setFungicideApplied(Boolean fungicideApplied) {
		this.fungicideApplied = fungicideApplied;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isBitstatus() {
		return bitstatus;
	}
	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
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
	public Integer getSurveyStatus() {
		return surveyStatus;
	}
	public void setSurveyStatus(Integer surveyStatus) {
		this.surveyStatus = surveyStatus;
	}
	public Integer getActionBy() {
		return actionBy;
	}
	public void setActionBy(Integer actionBy) {
		this.actionBy = actionBy;
	}
	public Date getActionOn() {
		return actionOn;
	}
	public void setActionOn(Date actionOn) {
		this.actionOn = actionOn;
	}
	public String getActionRemark() {
		return actionRemark;
	}
	public void setActionRemark(String actionRemark) {
		this.actionRemark = actionRemark;
	}
	  

	public SurveySiteEntity getSurveySiteEntity() {
		return surveySiteEntity;
	}
	public void setSurveySiteEntity(SurveySiteEntity surveySiteEntity) {
		this.surveySiteEntity = surveySiteEntity;
	}

 
	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_survey_id", referencedColumnName="int_survey_id")
	private SurveySiteEntity surveySiteEntity;
	 

}
