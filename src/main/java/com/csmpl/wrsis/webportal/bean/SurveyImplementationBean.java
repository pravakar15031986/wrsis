package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;

public class SurveyImplementationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private Integer monitorid;
	private String monitorno;
	private Integer recomid;
	private String recomno;
	private Integer woredaid;
	private String severitypc;
	private String incidencespc;
	private String sowingland;
	private String infectedland;
	private String controlledland;
	private Integer malefarmer;
	private Integer femalefarmer;
	private Integer totalfarmer;
	private String occurenceDate;
	private String monitordate;
	private String region;
	private String zone;
	private String woreda;
	private Integer nopaeffected;
	private String totalAreaInfested;
	private String totalControlledha;
	private String totalControlledpercent;
	private String totalFungicidesUsed;

	private int regionId;
	private int zoneId;
	private String collectMode;
	private String rustType;
	private String variety;
	private String growthStage;
	private String fungicideType;
	private Integer woredacount;
	private String monitorFromDate;
	private String monitorToDate;
	private Integer year;
	private Integer seasonId;
	private Integer sNo;
	private String monitorCheckBox;
	private String action;
	
	private Integer searchBySeason;

	public Integer getSearchBySeason() {
		return searchBySeason;
	}

	public void setSearchBySeason(Integer searchBySeason) {
		this.searchBySeason = searchBySeason;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Integer getMonitorid() {
		return monitorid;
	}

	public void setMonitorid(Integer monitorid) {
		this.monitorid = monitorid;
	}

	public String getMonitorno() {
		return monitorno;
	}

	public void setMonitorno(String monitorno) {
		this.monitorno = monitorno;
	}

	public Integer getRecomid() {
		return recomid;
	}

	public void setRecomid(Integer recomid) {
		this.recomid = recomid;
	}

	public String getRecomno() {
		return recomno;
	}

	public void setRecomno(String recomno) {
		this.recomno = recomno;
	}

	public Integer getWoredaid() {
		return woredaid;
	}

	public void setWoredaid(Integer woredaid) {
		this.woredaid = woredaid;
	}

	public String getSeveritypc() {
		return severitypc;
	}

	public void setSeveritypc(String severitypc) {
		this.severitypc = severitypc;
	}

	public String getIncidencespc() {
		return incidencespc;
	}

	public void setIncidencespc(String incidencespc) {
		this.incidencespc = incidencespc;
	}

	public String getSowingland() {
		return sowingland;
	}

	public void setSowingland(String sowingland) {
		this.sowingland = sowingland;
	}

	public String getInfectedland() {
		return infectedland;
	}

	public void setInfectedland(String infectedland) {
		this.infectedland = infectedland;
	}

	public String getControlledland() {
		return controlledland;
	}

	public void setControlledland(String controlledland) {
		this.controlledland = controlledland;
	}

	public Integer getMalefarmer() {
		return malefarmer;
	}

	public void setMalefarmer(Integer malefarmer) {
		this.malefarmer = malefarmer;
	}

	public Integer getFemalefarmer() {
		return femalefarmer;
	}

	public void setFemalefarmer(Integer femalefarmer) {
		this.femalefarmer = femalefarmer;
	}

	public Integer getTotalfarmer() {
		return totalfarmer;
	}

	public void setTotalfarmer(Integer totalfarmer) {
		this.totalfarmer = totalfarmer;
	}

	public String getOccurenceDate() {
		return occurenceDate;
	}

	public void setOccurenceDate(String occurenceDate) {
		this.occurenceDate = occurenceDate;
	}

	public String getMonitordate() {
		return monitordate;
	}

	public void setMonitordate(String monitordate) {
		this.monitordate = monitordate;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getWoreda() {
		return woreda;
	}

	public void setWoreda(String woreda) {
		this.woreda = woreda;
	}

	public Integer getNopaeffected() {
		return nopaeffected;
	}

	public void setNopaeffected(Integer nopaeffected) {
		this.nopaeffected = nopaeffected;
	}

	public String getTotalAreaInfested() {
		return totalAreaInfested;
	}

	public void setTotalAreaInfested(String totalAreaInfested) {
		this.totalAreaInfested = totalAreaInfested;
	}

	public String getTotalControlledha() {
		return totalControlledha;
	}

	public void setTotalControlledha(String totalControlledha) {
		this.totalControlledha = totalControlledha;
	}

	public String getTotalControlledpercent() {
		return totalControlledpercent;
	}

	public void setTotalControlledpercent(String totalControlledpercent) {
		this.totalControlledpercent = totalControlledpercent;
	}

	public String getTotalFungicidesUsed() {
		return totalFungicidesUsed;
	}

	public void setTotalFungicidesUsed(String totalFungicidesUsed) {
		this.totalFungicidesUsed = totalFungicidesUsed;
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

	public String getCollectMode() {
		return collectMode;
	}

	public void setCollectMode(String collectMode) {
		this.collectMode = collectMode;
	}

	public String getRustType() {
		return rustType;
	}

	public void setRustType(String rustType) {
		this.rustType = rustType;
	}

	public String getVariety() {
		return variety;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}

	public String getGrowthStage() {
		return growthStage;
	}

	public void setGrowthStage(String growthStage) {
		this.growthStage = growthStage;
	}

	public String getFungicideType() {
		return fungicideType;
	}

	public void setFungicideType(String fungicideType) {
		this.fungicideType = fungicideType;
	}

	public Integer getWoredacount() {
		return woredacount;
	}

	public void setWoredacount(Integer woredacount) {
		this.woredacount = woredacount;
	}

	public String getMonitorFromDate() {
		return monitorFromDate;
	}

	public void setMonitorFromDate(String monitorFromDate) {
		this.monitorFromDate = monitorFromDate;
	}

	public String getMonitorToDate() {
		return monitorToDate;
	}

	public void setMonitorToDate(String monitorToDate) {
		this.monitorToDate = monitorToDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getMonitorCheckBox() {
		return monitorCheckBox;
	}

	public void setMonitorCheckBox(String monitorCheckBox) {
		this.monitorCheckBox = monitorCheckBox;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
