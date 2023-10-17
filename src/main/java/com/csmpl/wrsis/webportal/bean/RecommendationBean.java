package com.csmpl.wrsis.webportal.bean;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.csmpl.wrsis.webportal.entity.RecommendationEntiry;

public class RecommendationBean {

	private int recomendId;
	private Integer loginUserId;
	private String recomendNoBean;
	private Integer advisoryIdBean;
	private String advisoryNoBean;
	private int recomendTypeBean;
	private MultipartFile recomendFileNameBean;

	private String recomendSummaryBean;

	private int recomendStatusBean;
	private int publishedByBean;
	private Date publishedDateBean;
	private String publishedRemarkBean;
	private int forwordByBean;
	private Date forwardDateBean;
	private String forwardRemarkBean;
	private boolean statusBean;
	private int createdByBean;
	private Date createdOnBean;
	private int updatedByBean;
	private Date updatedOnBean;
	private Integer reomendLocationIdBean;
	private RecommendationEntiry recomendIdBean;
	private int regionIdBean;
	private int zoneIdBean;
	private int woredaIdBean;
	private int kebeleIdBean;

	private String[] WoredaNameIdArray;
	private String[] kebeleNameIdArray;

	private List<String> kebeList;
	private List<String> woreList;
	private List<String> zonList;
	private List<String> regiList;

	private List<Integer> kebeLis;
	private List<Integer> woreLis;
	private List<Integer> zonLi;
	private List<Integer> regiLi;

	// created by Raman
	private String recDate;
	private String forwardDate;
	private String regionName;
	private String zoneName;
	private String woredaName;
	private String kebeleName;
	private String recFileName;
	private String demographyDetails;
	//
	private Date advisoryDateBean;

	private Integer sNo;
	private String publishedRecDate;
	private String recomendViewTypeBean;
	private String editLink;
	private String publishLink;
	private String viewAdvisoryDate;
	private String addLink;
	private String viewDocument;

	// created by Shaktimaan

	private Boolean requiredSms;

	public Boolean getRequiredSms() {
		return requiredSms;
	}

	public void setRequiredSms(Boolean requiredSms) {
		this.requiredSms = requiredSms;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	private String smsContent;

	public String getEditLink() {
		return editLink;
	}

	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}

	public String getPublishLink() {
		return publishLink;
	}

	public void setPublishLink(String publishLink) {
		this.publishLink = publishLink;
	}

	public String getRecomendViewTypeBean() {
		return recomendViewTypeBean;
	}

	public void setRecomendViewTypeBean(String recomendViewTypeBean) {
		this.recomendViewTypeBean = recomendViewTypeBean;
	}

	public String getPublishedRecDate() {
		return publishedRecDate;
	}

	public void setPublishedRecDate(String publishedRecDate) {
		this.publishedRecDate = publishedRecDate;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public int getRecomendId() {
		return recomendId;
	}

	public void setRecomendId(int recomendId) {
		this.recomendId = recomendId;
	}

	public String getRecomendNoBean() {
		return recomendNoBean;
	}

	public void setRecomendNoBean(String recomendNoBean) {
		this.recomendNoBean = recomendNoBean;
	}

	public Integer getAdvisoryIdBean() {
		return advisoryIdBean;
	}

	public void setAdvisoryIdBean(Integer advisoryIdBean) {
		this.advisoryIdBean = advisoryIdBean;
	}

	public String getAdvisoryNoBean() {
		return advisoryNoBean;
	}

	public void setAdvisoryNoBean(String advisoryNoBean) {
		this.advisoryNoBean = advisoryNoBean;
	}

	public int getRecomendTypeBean() {
		return recomendTypeBean;
	}

	public void setRecomendTypeBean(int recomendTypeBean) {
		this.recomendTypeBean = recomendTypeBean;
	}

	public MultipartFile getRecomendFileNameBean() {
		return recomendFileNameBean;
	}

	public void setRecomendFileNameBean(MultipartFile recomendFileNameBean) {
		this.recomendFileNameBean = recomendFileNameBean;
	}

	public String getRecomendSummaryBean() {
		return recomendSummaryBean;
	}

	public void setRecomendSummaryBean(String recomendSummaryBean) {
		this.recomendSummaryBean = recomendSummaryBean;
	}

	public int getRecomendStatusBean() {
		return recomendStatusBean;
	}

	public void setRecomendStatusBean(int recomendStatusBean) {
		this.recomendStatusBean = recomendStatusBean;
	}

	public int getPublishedByBean() {
		return publishedByBean;
	}

	public void setPublishedByBean(int publishedByBean) {
		this.publishedByBean = publishedByBean;
	}

	public Date getPublishedDateBean() {
		return publishedDateBean;
	}

	public void setPublishedDateBean(Date publishedDateBean) {
		this.publishedDateBean = publishedDateBean;
	}

	public String getPublishedRemarkBean() {
		return publishedRemarkBean;
	}

	public void setPublishedRemarkBean(String publishedRemarkBean) {
		this.publishedRemarkBean = publishedRemarkBean;
	}

	public int getForwordByBean() {
		return forwordByBean;
	}

	public void setForwordByBean(int forwordByBean) {
		this.forwordByBean = forwordByBean;
	}

	public Date getForwardDateBean() {
		return forwardDateBean;
	}

	public void setForwardDateBean(Date forwardDateBean) {
		this.forwardDateBean = forwardDateBean;
	}

	public String getForwardRemarkBean() {
		return forwardRemarkBean;
	}

	public void setForwardRemarkBean(String forwardRemarkBean) {
		this.forwardRemarkBean = forwardRemarkBean;
	}

	public boolean isStatusBean() {
		return statusBean;
	}

	public void setStatusBean(boolean statusBean) {
		this.statusBean = statusBean;
	}

	public int getCreatedByBean() {
		return createdByBean;
	}

	public void setCreatedByBean(int createdByBean) {
		this.createdByBean = createdByBean;
	}

	public Date getCreatedOnBean() {
		return createdOnBean;
	}

	public void setCreatedOnBean(Date createdOnBean) {
		this.createdOnBean = createdOnBean;
	}

	public int getUpdatedByBean() {
		return updatedByBean;
	}

	public void setUpdatedByBean(int updatedByBean) {
		this.updatedByBean = updatedByBean;
	}

	public Date getUpdatedOnBean() {
		return updatedOnBean;
	}

	public void setUpdatedOnBean(Date updatedOnBean) {
		this.updatedOnBean = updatedOnBean;
	}

	public Integer getReomendLocationIdBean() {
		return reomendLocationIdBean;
	}

	public void setReomendLocationIdBean(Integer reomendLocationIdBean) {
		this.reomendLocationIdBean = reomendLocationIdBean;
	}

	public RecommendationEntiry getRecomendIdBean() {
		return recomendIdBean;
	}

	public void setRecomendIdBean(RecommendationEntiry recomendIdBean) {
		this.recomendIdBean = recomendIdBean;
	}

	public int getRegionIdBean() {
		return regionIdBean;
	}

	public void setRegionIdBean(int regionIdBean) {
		this.regionIdBean = regionIdBean;
	}

	public int getZoneIdBean() {
		return zoneIdBean;
	}

	public void setZoneIdBean(int zoneIdBean) {
		this.zoneIdBean = zoneIdBean;
	}

	public int getWoredaIdBean() {
		return woredaIdBean;
	}

	public void setWoredaIdBean(int woredaIdBean) {
		this.woredaIdBean = woredaIdBean;
	}

	public int getKebeleIdBean() {
		return kebeleIdBean;
	}

	public void setKebeleIdBean(int kebeleIdBean) {
		this.kebeleIdBean = kebeleIdBean;
	}

	public String[] getWoredaNameIdArray() {
		return WoredaNameIdArray;
	}

	public void setWoredaNameIdArray(String[] woredaNameIdArray) {
		WoredaNameIdArray = woredaNameIdArray;
	}

	public String[] getKebeleNameIdArray() {
		return kebeleNameIdArray;
	}

	public void setKebeleNameIdArray(String[] kebeleNameIdArray) {
		this.kebeleNameIdArray = kebeleNameIdArray;
	}

	public List<String> getKebeList() {
		return kebeList;
	}

	public void setKebeList(List<String> kebeList) {
		this.kebeList = kebeList;
	}

	public List<String> getWoreList() {
		return woreList;
	}

	public void setWoreList(List<String> woreList) {
		this.woreList = woreList;
	}

	public List<String> getZonList() {
		return zonList;
	}

	public void setZonList(List<String> zonList) {
		this.zonList = zonList;
	}

	public List<String> getRegiList() {
		return regiList;
	}

	public void setRegiList(List<String> regiList) {
		this.regiList = regiList;
	}

	public List<Integer> getKebeLis() {
		return kebeLis;
	}

	public void setKebeLis(List<Integer> kebeLis) {
		this.kebeLis = kebeLis;
	}

	public List<Integer> getWoreLis() {
		return woreLis;
	}

	public void setWoreLis(List<Integer> woreLis) {
		this.woreLis = woreLis;
	}

	public List<Integer> getZonLi() {
		return zonLi;
	}

	public void setZonLi(List<Integer> zonLi) {
		this.zonLi = zonLi;
	}

	public List<Integer> getRegiLi() {
		return regiLi;
	}

	public void setRegiLi(List<Integer> regiLi) {
		this.regiLi = regiLi;
	}

	public String getRecDate() {
		return recDate;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public String getWoredaName() {
		return woredaName;
	}

	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public void setWoredaName(String woredaName) {
		this.woredaName = woredaName;
	}

	public String getKebeleName() {
		return kebeleName;
	}

	public void setKebeleName(String kebeleName) {
		this.kebeleName = kebeleName;
	}

	public String getRecFileName() {
		return recFileName;
	}

	public void setRecFileName(String recFileName) {
		this.recFileName = recFileName;
	}

	public String getForwardDate() {
		return forwardDate;
	}

	public void setForwardDate(String forwardDate) {
		this.forwardDate = forwardDate;
	}

	public Date getAdvisoryDateBean() {
		return advisoryDateBean;
	}

	public void setAdvisoryDateBean(Date advisoryDateBean) {
		this.advisoryDateBean = advisoryDateBean;
	}

	public Integer getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(Integer loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getViewAdvisoryDate() {
		return viewAdvisoryDate;
	}

	public void setViewAdvisoryDate(String viewAdvisoryDate) {
		this.viewAdvisoryDate = viewAdvisoryDate;
	}

	public String getAddLink() {
		return addLink;
	}

	public void setAddLink(String addLink) {
		this.addLink = addLink;
	}

	public String getViewDocument() {
		return viewDocument;
	}

	public void setViewDocument(String viewDocument) {
		this.viewDocument = viewDocument;
	}

	public String getDemographyDetails() {
		return demographyDetails;
	}

	public void setDemographyDetails(String demographyDetails) {
		this.demographyDetails = demographyDetails;
	}

}
