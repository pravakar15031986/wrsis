package com.csmpl.wrsis.webportal.serviceImpl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
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
import com.csmpl.wrsis.webportal.bean.NotificationBean;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.entity.NotificationEntity;
import com.csmpl.wrsis.webportal.entity.NotificationUserDetailsEntity;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.MobileLoginTrackingRepository;
import com.csmpl.wrsis.webportal.repository.NotificationRepository;
import com.csmpl.wrsis.webportal.repository.NotificationUserDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMonthRepository;
import com.csmpl.wrsis.webportal.service.NotificationService;
import com.csmpl.wrsis.webportal.util.DateUtil;
import com.csmpl.wrsis.webportal.util.EmailUtil;
import com.csmpl.wrsis.webportal.util.PushNotification;
import com.csmpl.wrsis.webportal.util.SMSUtil;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	NotificationUserDetailsRepository notificationUserDetailsRepository;
	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;
	@Autowired
	SeasonMonthRepository seasonMonthRepository;

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@Autowired
	MobileLoginTrackingRepository mobileLoginTrackingRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PushNotification mobileNotification;

	@Autowired
	SMSUtil smsUtil;

	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Value("${notification.indicator}")
	private String notificationFlag;

	@Override
	public String addNotification(NotificationBean notificationDetailsBean) {
		String sts = WrsisPortalConstant.FAILURE;
		NotificationEntity notificationDetails = new NotificationEntity();
		NotificationUserDetailsEntity notificationUsers = null;
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		int seasonId = seasonMonthRepository.getSeasonIdByMonthId(month);
		try {
			List<Short> users = notificationDetailsBean.getSelectedUsers();
			if (users != null) {
				notificationDetails.setOrganizationId(notificationDetailsBean.getOrganizationId());
				notificationDetails.setRcId(notificationDetailsBean.getRcId());
				notificationDetails.setNotificationMsg(notificationDetailsBean.getNotificationMsg());
				notificationDetails.setStatus(false);
				notificationDetails.setCreatedBy((int) notificationDetailsBean.getUserId());
				notificationDetails.setCreatedOn(new Date());
				notificationDetails.setSeasonId((short) seasonId);
				notificationDetails.setYear((short) calendar.get(Calendar.YEAR));

				notificationDetails.setSendStatus(Boolean.valueOf(notificationDetailsBean.getSendStatus()));

				notificationDetails.setNotificationDate(DateUtil.StringMMMToDate(notificationDetailsBean.getNtfDate()));
				NotificationEntity x = notificationRepository.save(notificationDetails);

				for (int i = 0; i < users.size(); i++) {
					notificationUsers = new NotificationUserDetailsEntity();
					notificationUsers.setNotifyId(x.getNotifyId());
					notificationUsers.setUserId(users.get(i));
					notificationUsers.setStatus(false);
					notificationUserDetailsRepository.saveAndFlush(notificationUsers);

				}
				sts = WrsisPortalConstant.SUCCESS;
			} else if (users == null) {
				sts = WrsisPortalConstant.NO_USERS;
				return sts;
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::addNotification():" + e);
		}

		return sts;
	}

	@Override
	public List<NotificationBean> getNotificationDetails() {
		List<NotificationBean> list = new ArrayList<>();
		NotificationBean nobj = null;
		try {
			List<Object[]> notificationList = notificationRepository.getNotificationDetails();
			for (Object[] obj : notificationList) {
				nobj = new NotificationBean();
				nobj.setNotifyId((BigInteger) obj[0]);
				nobj.setCreatedOn(DateUtil.DateToString((Date) (obj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				nobj.setNotificationMsg(String.valueOf(obj[2]));

				nobj.setRcName("null".equals(String.valueOf(obj[4])) ? "NA" : String.valueOf(obj[4]));

				nobj.setNtfDate("null".equals(String.valueOf(obj[6])) ? "NA"
						: DateUtil.DateToString((Date) (obj[6]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				list.add(nobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getNotificationDetails():" + e);
		}

		return list;
	}

	@Override
	public List<NotificationBean> getArchiveNotificationDetails() {
		List<NotificationBean> list = new ArrayList<>();
		NotificationBean nobj = null;
		try {
			List<Object[]> notificationList = notificationRepository.getArchiveNotificationDetails();
			for (Object[] obj : notificationList) {
				nobj = new NotificationBean();
				nobj.setNotifyId((BigInteger) obj[0]);
				nobj.setCreatedOn(DateUtil.DateToString((Date) (obj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				nobj.setNotificationMsg(String.valueOf(obj[2]));

				nobj.setRcName("null".equals(String.valueOf(obj[4])) ? "NA" : String.valueOf(obj[4]));
				nobj.setSendStatus((boolean) obj[5] ? "Sent" : "Not Sent");
				nobj.setSendDate(String.valueOf(obj[6]).equalsIgnoreCase("null") ? "NA"
						: DateUtil.DateToString((Date) (obj[6]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				list.add(nobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getArchieveNotificationDetails():" + e);

		}
		return list;
	}

	@Override
	public String getMemberDetailsByNotifyId(int notifyId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Object[]> memberList = notificationUserDetailsRepository.getMemberDetailsByNotifyId(notifyId);
			for (Object[] dObject : memberList) {
				jobj = new JSONObject();
				jobj.put("organization", String.valueOf(dObject[0]));
				jobj.put("fullName", String.valueOf(dObject[1]));
				jobj.put("role", String.valueOf(dObject[2]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getMemberDetailsByNotifyId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public NotificationBean editNotification(BigInteger notifyId) {
		NotificationBean nObj = null;
		try {
			List<Object[]> olist = notificationRepository.getNotificationDetailsByNotifyId(notifyId);
			for (Object[] obj : olist) {
				nObj = new NotificationBean();
				nObj.setNotifyId((BigInteger) obj[0]);
				nObj.setOrganizationId((Short) obj[1]);
				nObj.setRcId((Short) obj[2]);
				nObj.setNotificationMsg(String.valueOf(obj[3]));
				nObj.setSendStatus(String.valueOf(obj[4]));
				nObj.setNtfDate(DateUtil.DateToString((Date) obj[5], WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::editNotification():" + e);

		}
		return nObj;
	}

	@Override
	public List<NotificationBean> getAllMemberIds(BigInteger notifyId) {
		List<NotificationBean> list = new ArrayList<>();
		NotificationBean vo = null;
		try {
			List<Object[]> members = notificationUserDetailsRepository.getMembersByNotifyId(notifyId.intValue());
			if (!members.isEmpty()) {
				for (Object[] obj : members) {
					vo = new NotificationBean();
					vo.setUserId((Short) obj[0]);
					vo.setFullName(String.valueOf(obj[1]));
					vo.setUserName(String.valueOf(obj[2]));
					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getAllMemberIds():" + e);

		}
		return list;
	}

	@Override
	public String updateNotificationDetails(NotificationBean notificationDetails) {
		String retStatus = WrsisPortalConstant.FAILURE;
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		int seasonId = seasonMonthRepository.getSeasonIdByMonthId(month);
		try {
			List<Short> users = notificationDetails.getSelectedUsers();
			notificationUserDetailsRepository.findByNotifyId(notificationDetails.getNotifyId());

			if (users == null || users.isEmpty()) {
				return WrsisPortalConstant.NO_USERS;
			} else if (users != null) {

				// member mapping start
				List<Short> newUsers = notificationDetails.getSelectedUsers();
				List<NotificationUserDetailsEntity> existingUsers = notificationUserDetailsRepository
						.findByNotifyId(notificationDetails.getNotifyId());

				boolean flag = false;
				for (NotificationUserDetailsEntity existuser : existingUsers) {
					flag = false;
					for (Short newUserId : newUsers) {
						if (existuser.getUserId() == newUserId) {
							if (existuser.getStatus()) {
								List<NotificationUserDetailsEntity> nUsr = notificationUserDetailsRepository
										.findByUserIdAndNotifyId(existuser.getUserId(),
												notificationDetails.getNotifyId());
								if (!nUsr.isEmpty()) {
									NotificationUserDetailsEntity usr = nUsr.get(0);
									usr.setStatus(false);
									notificationUserDetailsRepository.saveAndFlush(usr);
								}
							}

							flag = true;
						}
					}
					if (!flag) {
						// delete existMemberId
						List<NotificationUserDetailsEntity> nUsr = notificationUserDetailsRepository
								.findByUserIdAndNotifyId(existuser.getUserId(), notificationDetails.getNotifyId());
						if (!nUsr.isEmpty()) {
							NotificationUserDetailsEntity usr = nUsr.get(0);
							usr.setStatus(true);
							notificationUserDetailsRepository.saveAndFlush(usr);
						}
					}
				}

				for (Short newUserId : newUsers) {
					flag = false;
					for (NotificationUserDetailsEntity existuser : existingUsers) {
						if (existuser.getUserId() == newUserId) {
							flag = true;
						}
					}
					if (!flag) {
						// insert newMemberId
						NotificationUserDetailsEntity nUsr = new NotificationUserDetailsEntity();
						nUsr.setNotifyId(notificationDetails.getNotifyId());
						nUsr.setUserId(newUserId);
						nUsr.setStatus(false);
						notificationUserDetailsRepository.saveAndFlush(nUsr);
					}
				}
				// Member mapping end

				NotificationEntity dto = notificationRepository.findByNotifyId(notificationDetails.getNotifyId());
				dto.setOrganizationId(notificationDetails.getOrganizationId());
				dto.setRcId(notificationDetails.getRcId());
				dto.setNotificationMsg(notificationDetails.getNotificationMsg());
				dto.setStatus(false);

				dto.setSendStatus(Boolean.valueOf(notificationDetails.getSendStatus()));

				dto.setNotificationDate(DateUtil.StringMMMToDate(notificationDetails.getNtfDate()));
				dto.setUpdatedBy(notificationDetails.getUserId());
				dto.setUpdatedOn(new Date());
				dto.setSeasonId((short) seasonId);
				dto.setYear((short) calendar.get(Calendar.YEAR));
				notificationRepository.save(dto);
			}
			retStatus = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::updateNotificationDetails():" + e);

			retStatus = WrsisPortalConstant.FAILURE;
		}
		return retStatus;
	}

	@Override
	public String sendNotification(NotificationBean notificationDetailsBean) {
		String sts = WrsisPortalConstant.FAILURE;
		String notiftitle = "Survey Notification";
		String notification = "";
		String emailTxt = "";
		NotificationEntity notificationDetails = new NotificationEntity();
		NotificationUserDetailsEntity notificationUsers = null;
		DashboardNotificationEntity dn = null;
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		int seasonId = seasonMonthRepository.getSeasonIdByMonthId(month);
		try {
			List<Short> users = notificationDetailsBean.getSelectedUsers();
			if (users != null) {
				notificationDetails.setOrganizationId(notificationDetailsBean.getOrganizationId());
				notificationDetails.setRcId(notificationDetailsBean.getRcId());
				notificationDetails.setNotificationMsg(notificationDetailsBean.getNotificationMsg());
				notificationDetails.setStatus(false);
				notificationDetails.setCreatedBy((int) notificationDetailsBean.getUserId());
				notificationDetails.setCreatedOn(new Date());
				notificationDetails.setSendDate(new Date());
				notificationDetails.setSeasonId((short) seasonId);
				notificationDetails.setSendStatus(Boolean.valueOf(notificationDetailsBean.getSendStatus()));
				notificationDetails.setNotificationDate(new Date());
				notificationDetails.setYear((short) calendar.get(Calendar.YEAR));
				NotificationEntity x = notificationRepository.save(notificationDetails);

				for (int i = 0; i < users.size(); i++) {
					notificationUsers = new NotificationUserDetailsEntity();
					dn = new DashboardNotificationEntity();
					notificationUsers.setNotifyId(x.getNotifyId());
					notificationUsers.setUserId(users.get(i));
					notificationUsers.setStatus(false);
					dn.setNotificationMsg(notificationDetailsBean.getNotificationMsg());
					dn.setStatus(false);
					dn.setReadStatus(false);
					dn.setToUserId(users.get(i));
					dn.setCreatedBy(notificationDetailsBean.getUserId());
					dn.setCreatedOn(new Date());
					dn.setUpdatedBy(notificationDetailsBean.getUserId());
					dn.setUpdatedOn(new Date());
					notificationUserDetailsRepository.save(notificationUsers);
					dashboardNotificationRepository.save(dn);

					User userBean = userRepository.findByUserId(dn.getToUserId());

					// For sending sms

					LOG.info("NotificationServiceImpl::sendNotificationSchedular():smsFlag-" + smsFlag);
					if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
						LOG.info(
								"NotificationServiceImpl::sendNotificationSchedular(): Now sms going to send to this no.-"
										+ userBean.getMobile());
						if (userBean.getMobile() != null && !"".equalsIgnoreCase(userBean.getMobile())) {
							try {
								final String newSMSTxt = dn.getNotificationMsg();
								Thread t = new Thread() {
									@Override
									public void run() {
										LOG.info(
												"NotificationServiceImpl::sendNotificationSchedular(): Now SMSUtil->sendSms() call");
										smsUtil.sendSms(userBean.getMobile(), newSMSTxt);
									}
								};
								t.start();
							} catch (Exception e) {
								LOG.error("NotificationServiceImpl::sendNotificationSchedular():error on sending sms"
										+ e);
							}
						}
					}

					// For Sending Email

					if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
						final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(6);
						if (userBean.getEmail() != null && !"".equalsIgnoreCase(userBean.getEmail())) {
							try {

								emailTxt = mailsms.getMailcontent();

								final String newEmailTxt = emailTxt.replace("@notificationmsg",
										dn.getNotificationMsg());

								Thread t = new Thread() {
									@Override
									public void run() {

										EmailUtil.sendAppcMail(userBean.getEmail(), newEmailTxt,
												mailsms.getMailsubject());
									}
								};
								t.start();
							} catch (Exception e) {
								LOG.error("NotificationServiceImpl::sendNotification():" + e);

							}
						}
					}

					// push notification

					if (WrsisPortalConstant.YES.equalsIgnoreCase(notificationFlag)) {

						if (dn.getToUserId() != null && dn.getToUserId() != 0 && dn.getNotificationMsg() != null
								&& !"".equalsIgnoreCase(dn.getNotificationMsg())) {

							notification = mobileNotification.sendNotificationParameters(dn.getToUserId(),
									dn.getNotificationMsg(), notiftitle);
							LOG.info(notification + " status ");

						}

					}

				}
				sts = "sendsuccess";
			} else if (users == null) {
				sts = WrsisPortalConstant.NO_USERS;
				return sts;
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::sendNotification():" + e);

		}

		return sts;
	}

	@Override
	public String updateAndSendNotificationDetails(NotificationBean notificationDetails) {
		String retStatus = WrsisPortalConstant.FAILURE;
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH);
		int seasonId = seasonMonthRepository.getSeasonIdByMonthId(month);
		try {
			DashboardNotificationEntity dn = null;
			List<Short> users = notificationDetails.getSelectedUsers();

			notificationUserDetailsRepository.findByNotifyId(notificationDetails.getNotifyId());

			if (users == null || users.isEmpty()) {
				return WrsisPortalConstant.NO_USERS;
			} else if (users != null) {

				// member mapping start
				List<Short> newUsers = notificationDetails.getSelectedUsers();
				List<NotificationUserDetailsEntity> existingUsers = notificationUserDetailsRepository
						.findByNotifyId(notificationDetails.getNotifyId());

				boolean flag = false;
				for (NotificationUserDetailsEntity existuser : existingUsers) {
					flag = false;
					for (Short newUserId : newUsers) {
						if (existuser.getUserId() == newUserId) {
							LOG.info(existuser.getUserId() + "  " + newUserId);
							if (existuser.getStatus()) {
								List<NotificationUserDetailsEntity> nUsr = notificationUserDetailsRepository
										.findByUserIdAndNotifyId(existuser.getUserId(),
												notificationDetails.getNotifyId());
								if (!nUsr.isEmpty()) {
									NotificationUserDetailsEntity usr = nUsr.get(0);
									usr.setStatus(false);
									notificationUserDetailsRepository.saveAndFlush(usr);
								}
							}

							flag = true;
						}
					}
					if (!flag) {
						// delete existMemberId
						List<NotificationUserDetailsEntity> nUsr = notificationUserDetailsRepository
								.findByUserIdAndNotifyId(existuser.getUserId(), notificationDetails.getNotifyId());
						if (!nUsr.isEmpty()) {
							NotificationUserDetailsEntity usr = nUsr.get(0);
							usr.setStatus(true);
							notificationUserDetailsRepository.saveAndFlush(usr);
						}
					}
				}

				for (Short newUserId : newUsers) {
					flag = false;
					for (NotificationUserDetailsEntity existuser : existingUsers) {
						if (existuser.getUserId() == newUserId) {
							flag = true;
						}
					}
					if (!flag) {
						// insert newMemberId
						NotificationUserDetailsEntity nUsr = new NotificationUserDetailsEntity();
						nUsr.setNotifyId(notificationDetails.getNotifyId());
						nUsr.setUserId(newUserId);
						nUsr.setStatus(false);
						notificationUserDetailsRepository.saveAndFlush(nUsr);
					}
				}
				// Member mapping end

				NotificationEntity dto = notificationRepository.findByNotifyId(notificationDetails.getNotifyId());
				dto.setOrganizationId(notificationDetails.getOrganizationId());
				dto.setRcId(notificationDetails.getRcId());
				dto.setNotificationMsg(notificationDetails.getNotificationMsg());
				dto.setStatus(false);
				dto.setUpdatedBy(notificationDetails.getUserId());
				dto.setUpdatedOn(new Date());
				dto.setSendDate(new Date());
				dto.setNotificationDate(new Date());
				dto.setSendStatus(Boolean.valueOf(notificationDetails.getSendStatus()));
				dto.setSeasonId((short) seasonId);
				dto.setYear((short) calendar.get(Calendar.YEAR));
				notificationRepository.save(dto);
			}

			List<Short> dashboardNotificationUsers = notificationUserDetailsRepository
					.getUserIdByNotifyIdAndStatus(notificationDetails.getNotifyId(), false);
			for (int i = 0; i < dashboardNotificationUsers.size(); i++) {
				dn = new DashboardNotificationEntity();
				dn.setNotificationMsg(notificationDetails.getNotificationMsg());
				dn.setStatus(false);
				dn.setToUserId(dashboardNotificationUsers.get(i));
				dn.setReadStatus(false);
				dn.setCreatedBy(notificationDetails.getUserId());
				dn.setCreatedOn(new Date());
				dashboardNotificationRepository.save(dn);
			}
			retStatus = "sendsuccess";

		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::updateAndSendNotificationDetails():" + e);

			retStatus = WrsisPortalConstant.FAILURE;
		}
		return retStatus;
	}

	@Override
	public String getDesktopNotificationsByUserId(int userId, Integer offset) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		String ntfDate = null;
		try {
			List<Object[]> notificationList = dashboardNotificationRepository
					.getDesktopNotificationsByUserIdandStatus(userId, offset);
			LOG.info(WrsisPortalConstant.NOTIFICATION_LIST + notificationList.size());
			for (Object[] dObject : notificationList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.NTF_ID, String.valueOf(dObject[0]));
				jobj.put(WrsisPortalConstant.NTF_MSG, String.valueOf(dObject[1]));
				ntfDate = DateUtil.DateToString((Date) (dObject[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYYHMMSSA);
				jobj.put(WrsisPortalConstant.NTF_DATE, ntfDate);
				jobj.put(WrsisPortalConstant.NTF_READ_STATUS, String.valueOf(dObject[3]));
				jobj.put(WrsisPortalConstant.FULL_NAME, String.valueOf(dObject[4]));
				jobj.put(WrsisPortalConstant.USERNAME, String.valueOf(dObject[5]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getDesktopNotificationsByUserId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String getDesktopNotificationsByUserId(int userId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		String ntfDate = null;
		try {
			List<Object[]> notificationList = dashboardNotificationRepository
					.getDesktopNotificationsByUserIdandStatus(userId);
			LOG.info(WrsisPortalConstant.NOTIFICATION_LIST + notificationList.size());
			for (Object[] dObject : notificationList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.NTF_ID, String.valueOf(dObject[0]));
				jobj.put(WrsisPortalConstant.NTF_MSG, String.valueOf(dObject[1]));
				ntfDate = DateUtil.DateToString((Date) (dObject[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYYHMMSSA);
				jobj.put(WrsisPortalConstant.NTF_DATE, ntfDate);
				jobj.put(WrsisPortalConstant.NTF_READ_STATUS, String.valueOf(dObject[3]));
				jobj.put(WrsisPortalConstant.FULL_NAME, String.valueOf(dObject[4]));
				jobj.put(WrsisPortalConstant.USERNAME, String.valueOf(dObject[5]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getDesktopNotificationsByUserId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String getUnreadDesktopNotificationsByUserId(int userId) {
		JSONObject jobj = new JSONObject();
		try {
			List<Object[]> unreadNotificationList = dashboardNotificationRepository
					.getUnreadDesktopNotificationsByUserIdandStatus(userId);
			Integer totalNot = dashboardNotificationRepository.getDesktopNotificationsByUserId(userId);
			LOG.info("unreadNotificationList " + unreadNotificationList.size());
			jobj.put("unreadNtfCount", String.valueOf(unreadNotificationList.size()));
			jobj.put("TotalNot", totalNot);
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getUnreadDesktopNotificationsByUserId():" + e);

		}
		return jobj.toString();
	}

	@Override
	public String updateNtfReadStatusByNotifyId(int userId, int notifyId) {
		JSONObject jobj = new JSONObject();
		BigInteger dashboardNtfId = BigInteger.valueOf(notifyId);
		Short user = Short.valueOf(String.valueOf(userId));
		try {
			DashboardNotificationEntity dn = dashboardNotificationRepository.getOne(dashboardNtfId);
			dn.setReadStatus(true);
			dn.setUpdatedBy(user);
			dn.setUpdatedOn(new Date());
			dashboardNotificationRepository.save(dn);
			List<Object[]> unreadNotificationList = dashboardNotificationRepository
					.getUnreadDesktopNotificationsByUserIdandStatus(userId);
			LOG.info("unreadNotificationList " + unreadNotificationList.size());
			jobj.put("unreadNtfCount", String.valueOf(unreadNotificationList.size()));
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::updateNtfReadStatusByNotifyId():" + e);

		}
		return jobj.toString();
	}

	@Override
	public String sendNotificationFromView(BigInteger notifyId, int userId) {
		String sts = "";
		try {
			NotificationEntity ntfObj = notificationRepository.getOne(notifyId);
			DashboardNotificationEntity dn = null;
			ntfObj.setSendDate(new Date());
			ntfObj.setSendStatus(true);
			ntfObj.setUpdatedBy((short) userId);
			ntfObj.setUpdatedOn(new Date());
			notificationRepository.save(ntfObj);
			String ntfMsg = String.valueOf(notificationRepository.getNtfMsgByNotifyId(notifyId));
			List<Object[]> usersObj = notificationUserDetailsRepository.getMembersByNotifyId(notifyId.intValue());
			for (Object[] objects : usersObj) {
				dn = new DashboardNotificationEntity();
				dn.setCreatedBy((short) userId);
				dn.setCreatedOn(new Date());
				dn.setNotificationMsg(ntfMsg);
				dn.setReadStatus(false);
				dn.setStatus(false);
				dn.setToUserId((Short) objects[0]);
				dashboardNotificationRepository.save(dn);
			}
			sts = WrsisPortalConstant.SUCCESS;
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::sendNotificationFromView():" + e);

			sts = "fail";
		}
		return sts;
	}

	@Override
	public List<NotificationBean> searchNotificationDetails(SearchVo searchVo) {
		List<NotificationBean> list = new ArrayList<>();
		NotificationBean nobj = null;
		List<Object[]> notificationList = null;
		try {
			if (searchVo.getStartDate() == null || searchVo.getStartDate() == "")
				searchVo.setStartDate(null);
			if (searchVo.getEndDate() == null || searchVo.getEndDate() == "")
				searchVo.setEndDate(null);
			if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
				notificationList = notificationRepository.getNotificationDetailsByNtfDate(
						DateUtil.StringMMMToDate(searchVo.getStartDate()),
						DateUtil.StringMMMToDate(searchVo.getEndDate()));
			} else {
				notificationList = notificationRepository.getNotificationDetails();
			}

			for (Object[] obj : notificationList) {
				nobj = new NotificationBean();
				nobj.setNotifyId((BigInteger) obj[0]);
				nobj.setCreatedOn(DateUtil.DateToString((Date) (obj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				nobj.setNotificationMsg(String.valueOf(obj[2]));
				nobj.setRcName(String.valueOf(obj[4]).equalsIgnoreCase("null") ? "NA" : String.valueOf(obj[4]));
				list.add(nobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::searchNotificationDetails():" + e);

		}
		return list;
	}

	@Override
	public List<NotificationBean> searchArchiveNotificationDetails(SearchVo searchVo) {
		List<NotificationBean> list = new ArrayList<>();
		NotificationBean nobj = null;
		List<Object[]> notificationList = null;
		try {
			Date ntfDateFrom = null, ntfDateTo = null, ntfCreatedFrom = null, ntfCreatedTo = null;
			if (searchVo.getNtfDateFrom() == null || searchVo.getNtfDateFrom() == "")
				searchVo.setNtfDateFrom(null);
			if (searchVo.getNtfDateTo() == null || searchVo.getNtfDateTo() == "")
				searchVo.setNtfDateTo(null);
			if (searchVo.getCreatedDateFrom() == null || searchVo.getCreatedDateFrom() == "")
				searchVo.setCreatedDateFrom(null);
			if (searchVo.getCreatedDateTo() == null || searchVo.getCreatedDateTo() == "")
				searchVo.setCreatedDateTo(null);
			if (searchVo.getNtfDateFrom() != null || searchVo.getNtfDateFrom() != "")
				ntfDateFrom = DateUtil.StringMMMToDate(searchVo.getNtfDateFrom());
			if (searchVo.getNtfDateTo() != null || searchVo.getNtfDateTo() != "")
				ntfDateTo = DateUtil.StringMMMToDate(searchVo.getNtfDateTo());
			if (searchVo.getCreatedDateFrom() != null || searchVo.getCreatedDateFrom() != "")
				ntfCreatedFrom = DateUtil.StringMMMToDate(searchVo.getCreatedDateFrom());
			if (searchVo.getCreatedDateTo() != null || searchVo.getCreatedDateTo() != "")
				ntfCreatedTo = DateUtil.StringMMMToDate(searchVo.getCreatedDateTo());

			if (searchVo.getNtfDateFrom() != null && searchVo.getNtfDateTo() != null
					&& searchVo.getCreatedDateFrom() == null && searchVo.getCreatedDateTo() == null) {
				notificationList = notificationRepository.getArchiveNotificationDetails(ntfDateFrom, ntfDateTo);
			} else if (searchVo.getNtfDateFrom() == null && searchVo.getNtfDateTo() == null
					&& searchVo.getCreatedDateFrom() != null && searchVo.getCreatedDateTo() != null) {
				notificationList = notificationRepository.getArchiveNotificationDetailsByCreatedDate(ntfCreatedFrom,
						ntfCreatedTo);
			} else if (searchVo.getNtfDateFrom() != null && searchVo.getNtfDateTo() != null
					&& searchVo.getCreatedDateFrom() != null && searchVo.getCreatedDateTo() != null) {
				notificationList = notificationRepository.getArchiveNotificationDetails(ntfDateFrom, ntfDateTo,
						ntfCreatedFrom, ntfCreatedTo);
			} else {
				notificationList = notificationRepository.getArchiveNotificationDetails();
			}

			for (Object[] obj : notificationList) {
				nobj = new NotificationBean();
				nobj.setNotifyId((BigInteger) obj[0]);
				nobj.setCreatedOn(DateUtil.DateToString((Date) (obj[1]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				nobj.setNotificationMsg(String.valueOf(obj[2]));

				nobj.setRcName("null".equals(String.valueOf(obj[4])) ? "NA" : String.valueOf(obj[4]));
				nobj.setSendStatus((boolean) obj[5] ? "Sent" : "Not Sent");
				nobj.setSendDate("null".equals(String.valueOf(obj[6])) ? "NA"
						: DateUtil.DateToString((Date) (obj[6]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				list.add(nobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::searchArchieveNotificationDetails():" + e);

		}
		return list;
	}

	@Transactional
	@Override
	public int sendNotificationScheduler(Date thisDate) {
		String emailTxt = "";
		String notification = "";
		String notiftitle = "Survey Notification";
		int retVal = 0;
		List<NotificationEntity> scheduledNtfList = notificationRepository
				.findByNotificationDateAndSendStatusAndStatus(thisDate, false, false);
		List<NotificationUserDetailsEntity> users = null;
		DashboardNotificationEntity dn = null;
		for (NotificationEntity obj : scheduledNtfList) {
			users = notificationUserDetailsRepository.findUserIdByNotifyIdAndStatus(obj.getNotifyId(), false);
			for (NotificationUserDetailsEntity ob : users) {
				dn = new DashboardNotificationEntity();
				dn.setCreatedBy(obj.getCreatedBy().shortValue());
				dn.setCreatedOn(thisDate);
				dn.setNotificationMsg(obj.getNotificationMsg());
				dn.setReadStatus(false);
				dn.setStatus(false);
				dn.setToUserId(ob.getUserId());

				dn.setUpdatedOn(thisDate);
				dashboardNotificationRepository.save(dn);
			}
			obj.setSendDate(thisDate);
			obj.setSendStatus(true);
			notificationRepository.save(obj);

			User userBean = userRepository.findByUserId(dn.getToUserId());

			// For sending sms
			LOG.info("NotificationServiceImpl::sendNotificationSchedular():smsFlag-" + smsFlag);
			if (WrsisPortalConstant.YES.equalsIgnoreCase(smsFlag)) {
				LOG.info("NotificationServiceImpl::sendNotificationSchedular(): Now sms going to send to this no.-"
						+ userBean.getMobile());
				if (userBean.getMobile() != null && !"".equalsIgnoreCase(userBean.getMobile())) {
					try {

						final String newSMSTxt = dn.getNotificationMsg();

						Thread t = new Thread() {
							@Override
							public void run() {
								LOG.info(
										"NotificationServiceImpl::sendNotificationSchedular(): Now SMSUtil->sendSms() call");
								smsUtil.sendSms(userBean.getMobile(), newSMSTxt);

							}
						};
						t.start();
					} catch (Exception e) {
						LOG.error("NotificationServiceImpl::sendNotificationSchedular():error on sending sms" + e);
					}
				}
			}

			// For Sending Email

			if (WrsisPortalConstant.YES.equalsIgnoreCase(mailFlag)) {
				final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(6);
				if (userBean.getEmail() != null && !"".equalsIgnoreCase(userBean.getEmail())) {
					try {

						emailTxt = mailsms.getMailcontent();

						final String newEmailTxt = emailTxt.replace("@notificationmsg", dn.getNotificationMsg());

						Thread t = new Thread() {
							@Override
							public void run() {

								EmailUtil.sendAppcMail(userBean.getEmail(), newEmailTxt, mailsms.getMailsubject());
							}
						};
						t.start();
					} catch (Exception e) {
						LOG.error("NotificationServiceImpl::sendNotificationSchedular():" + e);

					}
				}
			}

			// push notification

			if (WrsisPortalConstant.YES.equalsIgnoreCase(notificationFlag)) {

				if (dn.getToUserId() != null && dn.getToUserId() != 0 && dn.getNotificationMsg() != null
						&& !"".equalsIgnoreCase(dn.getNotificationMsg())) {

					notification = mobileNotification.sendNotificationParameters(dn.getToUserId(),
							dn.getNotificationMsg(), notiftitle);
					LOG.info(notification + " status ");

				}

			}

			retVal++;
		}
		return retVal;
	}

	@Override
	public String loadDesktopNotificationsByUserId(int userId, Integer limit) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		String ntfDate = null;
		try {
			List<Object[]> notificationList = dashboardNotificationRepository
					.loadDesktopNotificationsByUserIdandStatus(userId, limit);
			LOG.info(WrsisPortalConstant.NOTIFICATION_LIST + notificationList.size());
			for (Object[] dObject : notificationList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.NTF_ID, String.valueOf(dObject[0]));
				jobj.put(WrsisPortalConstant.NTF_MSG, String.valueOf(dObject[1]));
				ntfDate = DateUtil.DateToString((Date) (dObject[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYYHMMSSA);
				jobj.put(WrsisPortalConstant.NTF_DATE, ntfDate);
				jobj.put(WrsisPortalConstant.NTF_READ_STATUS, String.valueOf(dObject[3]));
				jobj.put(WrsisPortalConstant.FULL_NAME, String.valueOf(dObject[4]));
				jobj.put(WrsisPortalConstant.USERNAME, String.valueOf(dObject[5]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::loadDesktopNotificationsByUserId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String getTotalNtfOfUserId(int userId) {
		JSONObject jobj = new JSONObject();
		try {
			List<Object[]> notificationList = dashboardNotificationRepository
					.getTotalDesktopNotificationsByUserIdandStatus(userId);
			jobj.put("ntflist", notificationList.size());
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getTotalNtfOfUserId():" + e);

		}
		return jobj.toString();
	}

	@Override
	public List<NotificationBean> getNotificationDetailsByPage(String startDate, String endDate, Integer startPage,
			Integer pageLength) {

		List<NotificationBean> list = new ArrayList<>();

		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			// List<Object[]>

			List<Object[]> notificationList = notificationRepository.getNotificationDetailsByPage(sDate, eDate,
					startPage, pageLength);
			Integer count = 0;
			for (Object[] obj : notificationList) {
				NotificationBean nobj = new NotificationBean();
				nobj.setsNo(startPage + (++count));

				nobj.setNotifyId(BigInteger.valueOf(Long.valueOf((String) obj[0])));

				nobj.setCreatedOn(String.valueOf(obj[1]));

				nobj.setNotificationMsg(String.valueOf(obj[2]));

				nobj.setRcName(String.valueOf(obj[4]).equalsIgnoreCase("null") ? "NA" : String.valueOf(obj[4]));

				nobj.setNtfDate(String.valueOf(obj[6]));

				nobj.setUserInfo(
						"<a data-placement=\"top\" data-toggle=\"modal\" title=\"User Info\" data-target=\"#userModal\" onclick=\"userInfo("
								+ nobj.getNotifyId() + ")\">\r\n"
								+ "             	<button class=\"btn btn-primary btn-xs\" data-title=\"Users\">\r\n"
								+ "             	<i class=\"fa fa-info-circle\"></i>\r\n"
								+ "             	</button>\r\n" + "             	</a>");
				nobj.setEditLink(
						"<a data-placement=\"top\" data-toggle=\"tooltip\" title=\"Edit\" id=\"edit${nList.notifyId}\" onclick=\"editNotification("
								+ nobj.getNotifyId() + ");\">\r\n"
								+ "             	<button class=\"btn btn-primary btn-xs\" data-title=\"Edit\">\r\n"
								+ "             	<i class=\"icon-edit1\"></i>\r\n" + "             	</button>\r\n"
								+ "             	</a>");
				list.add(nobj);
			}
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getNotificationDetailsByPage():" + e);

		}

		return list;

	}

	@Override
	public Integer getNotificationDetailsByPageCount(String startDate, String endDate, Integer startPage,
			Integer pageLength) {
		Integer count = 0;
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			count = notificationRepository.getNotificationDetailsByPageCount(sDate, eDate, startPage, pageLength);
		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getNotificationDetailsByPageCount():" + e);

		}

		return count;

	}

	@Override
	public List<NotificationBean> getNotificationArchiveDetailsByPage(String sendStartDate, String sendEndDate,
			String startDate, String endDate, Integer startPage, Integer pageLength) {
		List<NotificationBean> list = new ArrayList<>();
		try {

			String sendSDate = null;
			if (sendStartDate != null && !"".equalsIgnoreCase(sendStartDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sendSDate = dt2.format(dt1.parse(sendStartDate));

			} else
				sendSDate = sendStartDate;

			String sendEDate = null;
			if (sendEndDate != null && !"".equalsIgnoreCase(sendEndDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sendEDate = dt4.format(dt3.parse(sendEndDate));

			} else
				sendEDate = sendEndDate;

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;

			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			Integer count = 0;
			List<Object[]> notificationList = notificationRepository.getNotificationArchiveDetailsByPage(sendSDate,
					sendEDate, sDate, eDate, startPage, pageLength);
			for (Object[] obj : notificationList) {
				NotificationBean nobj = new NotificationBean();
				nobj.setsNo(startPage + (++count));
				nobj.setNotifyId(BigInteger.valueOf(Long.valueOf((String) obj[0])));
				nobj.setCreatedOn((String) (obj[1]));
				nobj.setNotificationMsg(String.valueOf(obj[2]));
				nobj.setRcName("null".equals(String.valueOf(obj[4])) ? "NA" : String.valueOf(obj[4]));

				nobj.setSendDate((String) (obj[6]));
				nobj.setUserInfo(
						"<a data-placement=\"top\" data-toggle=\"modal\" title=\"User Info\" data-target=\"#userModal\" onclick=\"userInfo("
								+ nobj.getNotifyId() + ")\">\r\n"
								+ "         	<button class=\"btn btn-primary btn-xs\" data-title=\"Users\">\r\n"
								+ "         	<i class=\"fa fa-info-circle\"></i>\r\n" + "         	</button>\r\n"
								+ "         	</a>");
				list.add(nobj);
			}

		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getNotificationArchieveDetailsByPage():" + e);

		}
		return list;
	}

	@Override
	public Integer getNotificationArchiveDetailsCount(String sendStartDate, String sendEndDate, String startDate,
			String endDate, Integer startPage, Integer pageLength) {
		Integer count = 0;
		try {
			String sendSDate = null;
			if (sendStartDate != null && !"".equalsIgnoreCase(sendStartDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sendSDate = dt2.format(dt1.parse(sendStartDate));

			} else
				sendSDate = sendStartDate;

			String sendEDate = null;
			if (sendEndDate != null && !"".equalsIgnoreCase(sendEndDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sendEDate = dt4.format(dt3.parse(sendEndDate));

			} else
				sendEDate = sendEndDate;

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;

			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			count = notificationRepository.getNotificationDetailsArchiveByPageCount(sendSDate, sendEDate, sDate, eDate,
					startPage, pageLength);

		} catch (Exception e) {
			LOG.error("NotificationServiceImpl::getNotificationArchieveDetailsByPageCount():" + e);
		}
		return count;
	}
}
