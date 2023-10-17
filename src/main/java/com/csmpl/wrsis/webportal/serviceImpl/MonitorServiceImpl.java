package com.csmpl.wrsis.webportal.serviceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.MonitorBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;
import com.csmpl.wrsis.webportal.entity.MonitorDiscardLogEntity;
import com.csmpl.wrsis.webportal.entity.MonitorFungicideEntity;
import com.csmpl.wrsis.webportal.entity.MonitorGrowthStageEntity;
import com.csmpl.wrsis.webportal.entity.MonitorLocationEntity;
import com.csmpl.wrsis.webportal.entity.MonitorRustEntity;
import com.csmpl.wrsis.webportal.entity.MonitorVarietyEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MonitorDiscardLogRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.repository.MonitorGrowthStageRepository;
import com.csmpl.wrsis.webportal.repository.MonitorLocationRepository;
import com.csmpl.wrsis.webportal.repository.MonitorRecommendationRepository;
import com.csmpl.wrsis.webportal.repository.MonitorRustRepository;
import com.csmpl.wrsis.webportal.repository.MonitorVarietyRepository;
import com.csmpl.wrsis.webportal.service.MonitorService;
import com.csmpl.wrsis.webportal.util.EmailUtil;

@Service
public class MonitorServiceImpl implements MonitorService {
	private static final Logger LOG = LoggerFactory.getLogger(MonitorServiceImpl.class);

	@Autowired
	MonitorRecommendationRepository monitorRecommendationRepository;
	@Autowired
	MonitorDetailsRepository monitorDetailsRepository;
	@Autowired
	MonitorDiscardLogRepository monitorDiscardLogRepository;

	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;

	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;

	@Autowired
	MonitorRustRepository monitorRustRepository;

	@Autowired
	MonitorVarietyRepository monitorVarietyRepository;

	@Autowired
	MonitorGrowthStageRepository monitorGrowthStageRepository;

	@Autowired
	MonitorLocationRepository monitorLocationRepository;

	@Autowired
	MonitorFungicideRepository monitorFungicideRepository;

	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Override
	public List<MonitorBean> viewAllRecommendForMonitor() {
		List<MonitorBean> recommendList = new ArrayList<>();
		try {
			List<Object[]> obList = monitorRecommendationRepository.viewAllRecommendationForMonitor();
			for (Object[] reco : obList) {
				MonitorBean bean = new MonitorBean();
				if (reco[0].toString() != null)
					bean.setRecommendBeanNumber(reco[1].toString());
				else
					bean.setRecommendBeanNumber("-NA-");

				if (reco[0].toString() != null)
					bean.setRecommendBeanId(Integer.parseInt(reco[0].toString()));
				if (reco[3].toString() != null)
					bean.setRecommendBeanFileName(reco[3].toString());
				if (reco[2].toString() != null) {
					Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD).parse(reco[2].toString());
					bean.setRecommendBeandate(date);
				}

				recommendList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllRecommendationForMonitor():" + e);
		}
		return recommendList;
	}

	@Override
	public List<MonitorBean> searchAllRecommendForMonitor(SearchVo search) {
		List<MonitorBean> recommendList = new ArrayList<>();
		try {
			List<Object[]> obList = null;
			if (!search.getRecDtFrom().equals("") && !search.getRecDtTo().equals("")) {
				Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(search.getRecDtFrom());
				Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(search.getRecDtTo());
				obList = monitorRecommendationRepository.viewAllRecommendationForMonitorByDate(date1, date2);
			}
			if (!search.getRecNo().equals("")) {
				obList = monitorRecommendationRepository
						.viewAllRecommendationForMonitorByRCNumber(search.getRecNo().trim());
			}
			for (Object[] reco : obList) {
				MonitorBean bean = new MonitorBean();
				if (reco[0].toString() != null)
					bean.setRecommendBeanNumber(reco[1].toString());
				else
					bean.setRecommendBeanNumber("-NA-");

				if (reco[0].toString() != null)
					bean.setRecommendBeanId(Integer.parseInt(reco[0].toString()));
				if (reco[3].toString() != null)
					bean.setRecommendBeanFileName(reco[3].toString());
				if (reco[2].toString() != null) {
					Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD).parse(reco[2].toString());
					bean.setRecommendBeandate(date);
				}

				recommendList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::searchAllRecommendationForMonitor():" + e);

		}
		return recommendList;
	}

	@Override
	public List<SurveyImplementationBean> viewMoniterDataByRecommendNumber(String rcNumber) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		try {
			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByRecommendNo(0, 0, 0,
					"", "", "", rcNumber);

			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewMoniterDataByRecommendNumbe():" + e);

		}
		return implementationlist;
	}

	@Override
	public List<SurveyImplementationBean> viewMoniterDataByRecommendNumberSearch(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String startDate, String endDate, String rcNumber) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else {
				sDate = startDate;
			}
			String eDate = null;
			if (!endDate.isEmpty() && !"".equalsIgnoreCase(endDate) ) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else {
				eDate = endDate;
			}

			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByRecommendNo(regionId,
					zoneId, woredaId, monitorNo, sDate, eDate, rcNumber);

			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewMoniterDataByRecommendNumberSearch():" + e);

		}
		return implementationlist;
	}

	@Override
	public String publishedAndDiscardMonitorData(String jsonString, Integer userId) {// Integer status,
		String msg = null;
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.IMPLEMENTATION_PROCESS_ID;
		try {
			MonitorDetailsEntity mDetailsEntity = null;
			MonitorDiscardLogEntity monidiscard = null;
			ApprovalAuthorityHistoryEntity history = null;
			ApprovalAuthorityEntity approval = null;
			if (jsonString != null) {
				JSONObject jsonObj = new JSONObject(jsonString);
				JSONArray jsa = jsonObj.getJSONArray("RowsData");
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject innerJson = jsa.getJSONObject(i);
					String monitorId = innerJson.getString("monitorId");
					String remarks = innerJson.getString("rejectremark");
					String status = innerJson.getString("actionId");
					MonitorDetailsEntity monitor = monitorDetailsRepository.getOne(Integer.parseInt(monitorId));
					monitor.setMonitorid(monitor.getMonitorid());
					monitor.setUpdatedBy(userId);
					Date date = new Date();
					Timestamp ts = new Timestamp(date.getTime());
					monitor.setUpdatedOn(ts);
					monitor.setMonitorStatus(Integer.parseInt(status));
					mDetailsEntity = monitorDetailsRepository.saveAndFlush(monitor);

					// insert data t_wr_monitor_discard_log table
					if (Integer.parseInt(status) == 2 && mDetailsEntity != null) {
						monidiscard = new MonitorDiscardLogEntity();
						monidiscard.setMonitorid(monitor.getMonitorid());
						monidiscard.setMonitorno(monitor.getMonitorno());
						monidiscard.setRecomid(monitor.getRecomid());
						monidiscard.setRecomno(monitor.getRecomno());
						monidiscard.setRejectRemarks(remarks);
						monidiscard.setDiscardBy(userId);
						monidiscard.setDiscardOn(new Timestamp(new Date().getTime()));
						monidiscard.setBitStatus(false);
						monitorDiscardLogRepository.saveAndFlush(monidiscard);

						// Here Save Data into DhashBoard Notification for discarded Data
						discardedDashBoardNotification(monitor.getCreatedBy(), monidiscard, userId);
						// Here Send Mail to Corresponding Monitor discarded Data
						discardedDashBoardSendMail(monitor.getCreatedBy(), monidiscard);

						// Its Update ApprovalAuthorityEntity
						List<ApprovalAuthorityEntity> appAuthorityEntity = approvalAuthorityEntityRepository
								.findApprovalAuthorityDetailsBySurveyId(monitor.getMonitorid(),
										Integer.parseInt(processId));
						if (appAuthorityEntity != null) {
							for (ApprovalAuthorityEntity approvalauthority : appAuthorityEntity) {
								approval = new ApprovalAuthorityEntity();
								// Here Update ApprovalAuthorityEntity Table
								approval.setAprovalPId(approvalauthority.getAprovalPId());
								approval.setStageNo(approvalauthority.getStageNo());
								approval.setSurveyId(approvalauthority.getSurveyId());
								approval.setPendingAt(0);
								approval.setSentFrom(userId);
								approval.setUpdatedBy(userId);
								approval.setUpdatedOn(new Timestamp(new Date().getTime()));

								approvalAuthorityEntityRepository.saveAndFlush(approval);

								// Hear Save In to ApprovalAuthorityHistoryEntity table
								history = new ApprovalAuthorityHistoryEntity();
								history.setAprovalPId(approvalauthority.getAprovalPId());
								history.setStageNo(approvalauthority.getStageNo());
								history.setSurveyId(approvalauthority.getSurveyId());
								history.setPendingAt(0);
								history.setSentFrom(userId);
								history.setUpdatedBy(userId);
								history.setUpdatedOn(new Timestamp(new Date().getTime()));
								
								
								
								approvalAuthorityHistoryEntityRepository.saveAndFlush(history);

							}
						}

					} else {

						// Here Save Data into DhashBoard Notification for publish Data
						publishDashBoardNotification(mDetailsEntity, userId);
						// Here Send Mail to Corresponding Monitor publish Data
						publishDashBoardSendMail(mDetailsEntity);

						// Its Update ApprovalAuthorityEntity
						List<ApprovalAuthorityEntity> appAuthorityEntity = approvalAuthorityEntityRepository
								.findApprovalAuthorityDetailsBySurveyId(mDetailsEntity.getMonitorid(),
										Integer.parseInt(processId));
						if (appAuthorityEntity != null) {
							for (ApprovalAuthorityEntity approvalauthority : appAuthorityEntity) {
								approval = new ApprovalAuthorityEntity();
								// Here Update ApprovalAuthorityEntity Table
								approval.setAprovalPId(approvalauthority.getAprovalPId());
								approval.setStageNo(approvalauthority.getStageNo());
								approval.setSurveyId(approvalauthority.getSurveyId());
								approval.setPendingAt(0);
								approval.setSentFrom(userId);
								approval.setUpdatedBy(userId);
								approval.setUpdatedOn(new Timestamp(new Date().getTime()));

								approvalAuthorityEntityRepository.saveAndFlush(approval);

								// Hear Save In to ApprovalAuthorityHistoryEntity table
								history = new ApprovalAuthorityHistoryEntity();
								history.setAprovalPId(approvalauthority.getAprovalPId());
								history.setStageNo(approvalauthority.getStageNo());
								history.setSurveyId(approvalauthority.getSurveyId());
								history.setPendingAt(0);
								history.setSentFrom(userId);
								history.setUpdatedBy(userId);
								history.setUpdatedOn(new Timestamp(new Date().getTime()));
								
								
								
								approvalAuthorityHistoryEntityRepository.saveAndFlush(history);

							}
						}
					}

				}
			}
			if (mDetailsEntity.getMonitorStatus() == 1 && mDetailsEntity != null) {
				msg = "Data published  successfully";
			}
			if (mDetailsEntity.getMonitorStatus() == 2 && mDetailsEntity != null) {
				msg = "Data discarded  successfully";
			}

		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::publishedAndDiscardMonitorData():" + e);

		}
		return msg;
	}

	// some changes due to pagination

	

	@Override
	public List<SurveyImplementationBean> viewMoniterPublishedAndDiscardSearch(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String startDate, String endDate, Integer status, Integer pstart,
			Integer plength) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		Integer count = 0;
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else {
				sDate = startDate;
			}
			String eDate = null;
			if (!endDate.isEmpty() && !"".equalsIgnoreCase(endDate) && endDate != null) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else {
				eDate = endDate;
			}

			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByPublishedAndDiscard(
					regionId, zoneId, woredaId, monitorNo, sDate, eDate, status, pstart, plength);

			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setsNo(pstart + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitorno("<a href=\"javascript:void(0);\"  onclick=\"edit('"
						+ implementation.getMonitorid() + "')\">" + implementation.getMonitorno() + "</a>");
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewMoniterPublishedAndDiscardSearch():" + e);

		}
		return implementationlist;

	}

	@Override
	public List<SurveyImplementationBean> viewAllPublished(SearchVo searchVo) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		try {
			if (searchVo.getMonitorRefNumber() == null)
				searchVo.setMonitorRefNumber("");
			if (searchVo.getStartDate() == null)
				searchVo.setStartDate("");
			if (searchVo.getEndDate() == null)
				searchVo.setEndDate("");
			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByPublished(
					searchVo.getRegionId(), searchVo.getZoneId(), searchVo.getWoredaId(),
					searchVo.getMonitorRefNumber(), searchVo.getStartDate(), searchVo.getEndDate(), 1);

			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublished():" + e);

		}
		return implementationlist;
	}

	@Override
	public List<SurveyImplementationBean> viewAllPublishedDiscardedByUserId(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String fromDate, String toDate, Integer status, Integer userId,
			Integer pstart, Integer plength) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		SurveyImplementationBean implementation = null;
		Integer count = 0;
		try {
			List<Object[]> implementationDetails = null;

			implementationDetails = monitorRecommendationRepository.viewAllPublishedDiscardedByUserId(regionId, zoneId,
					woredaId, monitorNo, fromDate, toDate, status, userId, pstart, plength);
			for (Object[] obj : implementationDetails) {

				implementation = new SurveyImplementationBean();
				implementation.setsNo(pstart + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitorno("<a href=\"javascript:void(0);\"  onclick=\"edit('"
						+ implementation.getMonitorid() + "')\">" + implementation.getMonitorno() + "</a>");
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedDiscardedByUserId():" + e);

		}
		return implementationlist;
	}

	@Override
	public List<SurveyImplementationBean> viewImplementationDetailsData(Integer regionId, Integer zoneId,
			String monitorNo, String monitordate, Integer userId, Integer pstart, Integer plength) {
		List<Object[]> implementationDetails = null;
		SurveyImplementationBean implementation = null;
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		Integer count = 0;
		try {
			implementationDetails = monitorDetailsRepository.viewImplementationDetailsData(regionId, zoneId, monitorNo,
					monitordate, userId, pstart, plength);

			for (Object[] obj : implementationDetails) {
				implementation = new SurveyImplementationBean();
				implementation.setsNo(pstart + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitorno("<a href=\"javascript:void(0);\"  onclick=\"viewImplementation('"
						+ implementation.getMonitorid() + "')\">" + implementation.getMonitorno() + "</a>");
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));
				implementation.setCollectMode(String.valueOf(obj[16]));
				implementation.setAction(
						"<a class=\"btn btn-info btn-sm editClass\" data-toggle=\"tooltip editClass\" title=\"\" id=\" \" data-original-title=\"Edit\" monitor-id=\""
								+ implementation.getMonitorid() + "\"><i class=\"icon-edit1\"></i></a>\r\n"
								+ " <a class=\"btn btn-danger btn-sm deleteClass\" data-toggle=\"tooltip\" title=\"\" id=\"btnDeleteId\" data-original-title=\"Delete\" monitor-id=\""
								+ implementation.getMonitorid() + "\"><i class=\"icon-trash-21\"></i></a>\r\n ");

				implementationlist.add(implementation);
			}

		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewImplementationDetailsData():" + e);

		}
		return implementationlist;
	}

	@Override
	public Integer viewImplementationDetailsDataCount(Integer regionId, Integer zoneId, String monitorNo,
			String monitordate, Integer userId, Integer pstart, Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = monitorDetailsRepository.viewImplementationDetailsDataCount(regionId, zoneId, monitorNo,
					monitordate, userId, pstart, plength);
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewImplementationDetailsDataCount():" + e);

		}

		return totalCount;
	}

	@Override
	public List<SurveyImplementationBean> monitorRecommendationPublishedData(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String recDtFrom, String recDtTo, String rcNumber, Integer userId,
			Integer processId, Integer pstart, Integer plength) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		SurveyImplementationBean implementation = null;
		Integer count = 0;
		try {
			List<Object[]> implementationDetails = monitorRecommendationRepository.monitorRecommendationPublishedData(
					regionId, zoneId, woredaId, monitorNo, recDtFrom, recDtTo, rcNumber, userId, processId, pstart,
					plength);

			for (Object[] obj : implementationDetails) {
				implementation = new SurveyImplementationBean();
				implementation.setsNo(pstart + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorCheckBox("<input\r\n" + " id=\"demo-form-inline-checkbox"
						+ implementation.getMonitorid() + "\"\r\n" + " class=\"magic-checkbox\" type=\"checkbox\"\r\n"
						+ " name=\"moniterDetailsId\" value=" + implementation.getMonitorid() + ">\r\n"
						+ " <label for=\"demo-form-inline-checkbox" + implementation.getMonitorid() + "\"></label> ");
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitorno("<a href=\"javascript:void(0);\"  onclick=\"edit('"
						+ implementation.getMonitorid() + "')\">" + implementation.getMonitorno() + "</a>"
						+ "<input type=\"hidden\" id=\"monitorNo\"  value=" + implementation.getMonitorno() + ">");
				implementation.setMonitordate(String.valueOf(obj[2]));
				String hiddenRecomno = obj[3].toString() + "<input type=\"hidden\" id=\"tabRecId\" value="
						+ obj[3].toString() + ">" + "<input type=\"hidden\" id=\"recomId\" value=" + obj[16].toString()
						+ ">";
				implementation.setRecomno(hiddenRecomno);
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::monitorRecommendationPublishedData():" + e);

		}
		return implementationlist;
	}

	@Override
	public Integer monitorRecommendationPublishedDataCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String recDtFrom, String recDtTo, String rcNumber, Integer userId, Integer processId,
			Integer pstart, Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = monitorRecommendationRepository.viewImplementationDetailsDataCount(regionId, zoneId, woredaId,
					monitorNo, recDtFrom, recDtTo, rcNumber, userId, processId, pstart, plength);
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::monitorRecommendationPublishedDataCount():" + e);

		}

		return totalCount;
	}

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;
	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;

	@Autowired
	UserRepository userRepository;

	// Save Discarded Survey Data Into DashBoard Notification
	private void discardedDashBoardNotification(Integer userTo, MonitorDiscardLogEntity monidiscard, Integer userFrom) {

		Integer intv = new Integer(userFrom);
		Short mValueCreatedBy = intv.shortValue();

		Integer intSCv = new Integer(userTo);
		Short monitorCreadtedBy = intSCv.shortValue();
		MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(8);

		String dashBoardTxt = mailsms.getNotification();

		final String newDashBoardTxt = dashBoardTxt.replaceAll("@monitorno ", monidiscard.getMonitorno())
				.replaceAll("@reason", monidiscard.getRejectRemarks());

		DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
		dashBoard.setToUserId(monitorCreadtedBy);
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(mValueCreatedBy);
		dashBoard.setCreatedOn(new Date());
		dashboardNotificationRepository.saveAndFlush(dashBoard);

	}

	// Discarded Survey Data mail
	private void discardedDashBoardSendMail(Integer userTo, MonitorDiscardLogEntity monidiscard) {
		if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
			final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(8);
			User user = userRepository.getOne(userTo);
			if (user.getEmail() != null && !"".equalsIgnoreCase(user.getEmail())) {
				try {

					String emailTxt = mailsms.getMailcontent();
					final String newEmailTxt = emailTxt.replaceAll("@monitorno", monidiscard.getMonitorno())
							.replaceAll(" @reason", monidiscard.getRejectRemarks());
					
					
					
					
					Thread t = new Thread() {
						@Override
						public void run() {

							EmailUtil.sendAppcMail(user.getEmail(), newEmailTxt, mailsms.getMailsubject());
						}
					};
					t.start();
				} catch (Exception e) {
					LOG.error("MonitorServiceImpl::discardedDashboardSendMail():" + e);

				}
			}
		}
	}

	// Save publish Survey Data Into DashBoard Notification
	private void publishDashBoardNotification(MonitorDetailsEntity monipublish, Integer userFrom) {

		Integer intv = new Integer(userFrom);
		Short mValueCreatedBy = intv.shortValue();

		Integer intSCv = new Integer(monipublish.getCreatedBy());
		Short monitorCreadtedBy = intSCv.shortValue();
		MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(9);

		String dashBoardTxt = mailsms.getNotification();

		final String newDashBoardTxt = dashBoardTxt.replaceAll("@monitorno ", monipublish.getMonitorno());

		DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
		dashBoard.setToUserId(monitorCreadtedBy);
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(mValueCreatedBy);
		dashBoard.setCreatedOn(new Date());
		dashboardNotificationRepository.saveAndFlush(dashBoard);

	}

	// publish Survey Data mail
	private void publishDashBoardSendMail(MonitorDetailsEntity monipublish) {
		if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
			final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(9);
			User user = userRepository.getOne(monipublish.getCreatedBy());
			if (user.getEmail() != null && !"".equalsIgnoreCase(user.getEmail())) {
				try {

					String emailTxt = mailsms.getMailcontent();
					final String newEmailTxt = emailTxt.replaceAll("@monitorno", monipublish.getMonitorno());
					
					
					
					
					
					
					Thread t = new Thread() {
						@Override
						public void run() {

							EmailUtil.sendAppcMail(user.getEmail(), newEmailTxt, mailsms.getMailsubject());
						}
					};
					t.start();
				} catch (Exception e) {
					LOG.error("MonitorServiceImpl::publishDashBoardSendMail():" + e);

				}
			}
		}
	}

	@Override
	public Integer viewMoniterPublishedAndDiscardSearchCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String startDate, String endDate, Integer status, Integer pstart, Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = monitorRecommendationRepository.viewMoniterPublishedAndDiscardSearchCount(regionId, zoneId,
					woredaId, monitorNo, startDate, endDate, status, pstart, plength);
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewMoniterPublishedAndDiscardSearchCount():" + e);

		}

		return totalCount;
	}

	@Override
	public List<SurveyImplementationBean> viewAllPublishedByPage(SearchVo searchVo) {

		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		try {
			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByPublishedPageWise(
					searchVo.getRegionId(), searchVo.getZoneId(), searchVo.getWoredaId(),
					searchVo.getMonitorRefNumber(), searchVo.getStartDate(), searchVo.getEndDate(), 1,
					searchVo.getPageSize(), searchVo.getPageLength());
			Integer count = 0;
			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setsNo((searchVo.getPageSize()) + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno("<a href=\"javascript:void(0)\" onclick=\"edit("
						+ implementation.getMonitorid() + ")\">" + String.valueOf(obj[1]) + "</a>");
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);

			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedByPage():" + e);

		}
		return implementationlist;

	}

	@Override
	public Integer viewCountAllPublishedByPage(SearchVo searchVo) {
		Integer count = 0;
		try {
			count = monitorRecommendationRepository.countAllMonitorByPublishedPageWise(searchVo.getRegionId(),
					searchVo.getZoneId(), searchVo.getWoredaId(), searchVo.getMonitorRefNumber(),
					searchVo.getStartDate(), searchVo.getEndDate(), 1, searchVo.getPageSize(),
					searchVo.getPageLength());

		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedByPage():" + e);

		}
		return count;
	}

	@Override
	public Integer viewAllPublishedDiscardedByUserIdCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String fromDate, String toDate, Integer status, Integer userId, Integer pstart,
			Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = monitorRecommendationRepository.viewAllPublishedDiscardedByUserIdCount(regionId, zoneId,
					woredaId, monitorNo, fromDate, toDate, status, userId, pstart, plength);
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedDiscardedByUserIdCount():" + e);

		}

		return totalCount;
	}

	@Override
	public List<MonitorBean> viewAllForRecommendForMonitorPage(String recommNo, Integer userId, String startDate, String endDate,
			Integer pStart, Integer pLength) {

		List<MonitorBean> recommendList = new ArrayList<>();
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else {
				sDate = startDate;
			}
			String eDate = null;
			if (!endDate.isEmpty() && !"".equalsIgnoreCase(endDate) ) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else {
				eDate = endDate;
			}
			List<Object[]> obList = monitorRecommendationRepository.viewAllRecommendationForMonitorPage(recommNo, userId, sDate,
					eDate, pStart, pLength);
			Integer count = 0;
			for (Object[] reco : obList) {
				MonitorBean bean = new MonitorBean();
				bean.setsNo((pStart) + (++count));
				bean.setRecommendBeanId(Integer.valueOf(reco[0].toString()));
				bean.setRecommendBeanNumber(reco[1].toString());
				bean.setViewRCDate((String) reco[2]);
				String fileName = reco[3].toString();
				if (fileName.contains(".pdf")) {
					bean.setRecommendBeanFileName("<a href='javascript:void(0)' onclick=\"recommendFileDownload("
							+ bean.getRecommendBeanId()
							+ ")\" ><center><i class=\"fa fa-file-pdf-o fa-1x\" aria-hidden=\"true\"></i></center></a>");
				} else if (fileName.contains(".doc")) {
					bean.setRecommendBeanFileName("<a href='javascript:void(0)' onclick=\"recommendFileDownload("
							+ bean.getRecommendBeanId()
							+ ")\" ><center><i class=\"fa fa-file-word-o fa-1x\" aria-hidden=\"true\"></i></center></a>");
				}
				bean.setActionLisnk(
						"<a class=\"btn btn-info btn-sm\"  title=\"\" data-original-title=\"View Details\" href='javascript:void(0)' onclick=\"viewPublishedMoniter('"
								+ bean.getRecommendBeanNumber()
								+ "')\"><i class=\"fa fa-eye\" aria-hidden=\"true\"></i></a>"
								+ "<a class=\"btn btn-primary btn-sm publish\" href='javascript:void(0)' onclick=\"publishedMoniter('"
								+ bean.getRecommendBeanNumber() + "')\">Publish</a>");
				recommendList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllForRecommendForMonitorPage():" + e);

		}
		return recommendList;

	}

	@Override
	public Integer viewAllForRecommendForMonitorPageCount(String recommNo,Integer userId, String startDate, String endDate,
			Integer pStart, Integer pLength) {
		Integer count = 0;
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else {
				sDate = startDate;
			}
			String eDate = null;
			if (!endDate.isEmpty() && !"".equalsIgnoreCase(endDate) ) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else {
				eDate = endDate;
			}
			count = monitorRecommendationRepository
					.viewAllRecommendationForMonitorPageCount(recommNo.trim().toUpperCase(),userId, sDate, eDate, -1, -1);

		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllForRecommendForMonitorPageCount():" + e);

		}
		return count;
	}

	@Override
	public String deleteMonitorDetails(int monitorId) {

		try {
			monitorDetailsRepository.deleteMonitorDetails(monitorId);
			return "Deleted successfully.";
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::deleteMonitorDetails():" + e);

			return "Unable to delete due to some internal error.";
		}
	}

	@Override
	public JSONObject getFormmatedJsonByMonitorId(Integer monitorId) throws JSONException {

		JSONObject json = new JSONObject();

		Optional<MonitorDetailsEntity> monitorDetails = monitorDetailsRepository.findById(monitorId);

		if (monitorDetails.get() != null) {
			json.put("monitorId", monitorDetails.get().getMonitorid());
			json.put("monitorNo", monitorDetails.get().getMonitorno());
			json.put("recoId", monitorDetails.get().getRecomid());
			json.put("recoNo", monitorDetails.get().getRecomno());

			json.put("incidencespc", monitorDetails.get().getIncidencespc());
			json.put("severitypc", monitorDetails.get().getSeveritypc());
			json.put("sowingland", monitorDetails.get().getSowingland());
			json.put("infectedland", monitorDetails.get().getInfectedland());

			json.put("controlledland", monitorDetails.get().getControlledland());
			json.put("malefarmer", monitorDetails.get().getMalefarmer());
			json.put("femalefarmer", monitorDetails.get().getFemalefarmer());
			json.put("totalfarmer", monitorDetails.get().getTotalfarmer());

			// surveyorJsa
			JSONArray jsa = new JSONArray();
			try {
				List<MonitorRustEntity> rustDetails = monitorRustRepository.findByMonitorid(monitorId);
				for (MonitorRustEntity rust : rustDetails) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put(WrsisPortalConstant.RUSTID, (rust.getRusttypeid()));
					jsa.put(jsonObj);
				}

			} catch (Exception e) {
				LOG.error("MonitorServiceImpl::getFormmatedJsonByMonitorId():" + e);

			}
			json.put("rustJsa", jsa);

			// varietyDetails
			List<MonitorVarietyEntity> varietyDetails = monitorVarietyRepository.findByMonitorid(monitorId);
			jsa = new JSONArray();
			for (MonitorVarietyEntity variety : varietyDetails) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("varietyId", variety.getVarietyid());
				jsa.put(jsonObj);
			}

			json.put("varietyjsa", jsa);

			// grothDetails
			List<MonitorGrowthStageEntity> grothDetails = monitorGrowthStageRepository.findByMonitorid(monitorId);
			jsa = new JSONArray();
			for (MonitorGrowthStageEntity groth : grothDetails) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("grothId", groth.getGrowthstageid());
				jsa.put(jsonObj);
			}

			json.put("grothStagejsa", jsa);

			// location
			List<MonitorLocationEntity> locDetails = monitorLocationRepository.findByMonitorid(monitorId);
			jsa = new JSONArray();
			for (MonitorLocationEntity loc : locDetails) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put(WrsisPortalConstant.REGION_ID, loc.getRegionid());
				jsonObj.put("zoneId", loc.getZoneid());
				jsonObj.put("woredaId", loc.getWoredaid());
				jsonObj.put("kebeleId", loc.getKebeleid());
				jsa.put(jsonObj);
			}

			json.put("locjsa", jsa);

			// fungicide used
			List<MonitorFungicideEntity> fungiDetails = monitorFungicideRepository.findByMonitorid(monitorId);
			jsa = new JSONArray();
			for (MonitorFungicideEntity fungi : fungiDetails) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("fungiId", fungi.getFungicideid());
				jsonObj.put("fungiUsed", fungi.getFungicideused());

				jsa.put(jsonObj);
			}

			json.put("fungijsa", jsa);

		}
		return json;
	}

	@Override
	public List<SurveyImplementationBean> viewAllPublishedByPageReport(SearchVo searchVo) {

		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		try {
			List<Object[]> implementationDetails = monitorRecommendationRepository.viewAllMonitorByPublishedPageWise(
					searchVo.getRegionId(), searchVo.getZoneId(), searchVo.getWoredaId(),
					searchVo.getMonitorRefNumber(), searchVo.getStartDate(), searchVo.getEndDate(), 1,
					searchVo.getPageSize(), searchVo.getPageLength());
			Integer count = 0;
			for (Object[] obj : implementationDetails) {
				SurveyImplementationBean implementation = new SurveyImplementationBean();
				implementation.setsNo((searchVo.getPageSize()) + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno((String.valueOf(obj[1])));
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);

			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedByPagereport():" + e);

		}
		return implementationlist;

	}

	@Override
	public Integer viewCountAllPublishedByPageReport(SearchVo searchVo) {
		Integer count = 0;
		try {
			count = monitorRecommendationRepository.countAllMonitorByPublishedPageWise(searchVo.getRegionId(),
					searchVo.getZoneId(), searchVo.getWoredaId(), searchVo.getMonitorRefNumber(),
					searchVo.getStartDate(), searchVo.getEndDate(), 1, searchVo.getPageSize(),
					searchVo.getPageLength());

		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewCountAllPublishedByPageReport():" + e);

		}
		return count;
	}

	@Override
	public List<SurveyImplementationBean> viewAllPublishedDiscardedReportByUserId(Integer regionId, Integer zoneId,
			Integer woredaId, String monitorNo, String fromDate, String toDate, Integer status, Integer userId,
			Integer pstart, Integer plength) {
		List<SurveyImplementationBean> implementationlist = new ArrayList<>();
		SurveyImplementationBean implementation = null;
		Integer count = 0;
		try {
			List<Object[]> implementationDetails = null;

			implementationDetails = monitorRecommendationRepository.viewAllPublishedDiscardedByUserId(regionId, zoneId,
					woredaId, monitorNo, fromDate, toDate, status, userId, pstart, plength);
			for (Object[] obj : implementationDetails) {

				implementation = new SurveyImplementationBean();
				implementation.setsNo(pstart + (++count));
				implementation.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				implementation.setMonitorno(String.valueOf(obj[1]));
				implementation.setMonitorno(implementation.getMonitorno());
				implementation.setMonitordate(String.valueOf(obj[2]));
				implementation.setRecomno(String.valueOf(obj[3]));
				implementation.setRegion(String.valueOf(obj[4]));
				implementation.setZone(String.valueOf(obj[5]));
				implementation.setWoreda(String.valueOf(obj[6]));
				implementation.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				implementation.setTotalAreaInfested(String.valueOf(obj[8]));
				implementation.setTotalControlledha(String.valueOf(obj[9]));
				implementation.setTotalControlledpercent(String.valueOf(obj[10]));
				implementation.setTotalFungicidesUsed(String.valueOf(obj[15]));
				implementation.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				implementation.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				implementation.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				implementationlist.add(implementation);
			}
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedDiscardedReportByUserId():" + e);

		}
		return implementationlist;
	}

	@Override
	public Integer viewAllPublishedDiscardedReportByUserIdCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String fromDate, String toDate, Integer status, Integer userId, Integer pstart,
			Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = monitorRecommendationRepository.viewAllPublishedDiscardedByUserIdCount(regionId, zoneId,
					woredaId, monitorNo, fromDate, toDate, status, userId, pstart, plength);
		} catch (Exception e) {
			LOG.error("MonitorServiceImpl::viewAllPublishedDiscardedReportByUserIdCount():" + e);

		}

		return totalCount;
	}

}