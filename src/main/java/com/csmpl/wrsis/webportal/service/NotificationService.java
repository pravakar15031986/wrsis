package com.csmpl.wrsis.webportal.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.NotificationBean;

public interface NotificationService {

	String addNotification(NotificationBean notificationDetails);

	List<NotificationBean> getNotificationDetails();

	String getMemberDetailsByNotifyId(int notifyId);

	NotificationBean editNotification(BigInteger notifyId);

	List<NotificationBean> getAllMemberIds(BigInteger notifyId);

	String updateNotificationDetails(NotificationBean notificationDetails);

	String sendNotification(NotificationBean notificationDetails);

	String updateAndSendNotificationDetails(NotificationBean notificationDetails);

	List<NotificationBean> getArchiveNotificationDetails();

	String getDesktopNotificationsByUserId(int userId, Integer offset);

	String getDesktopNotificationsByUserId(int userId);

	String getUnreadDesktopNotificationsByUserId(int userId);

	String updateNtfReadStatusByNotifyId(int userId, int notifyId);

	String sendNotificationFromView(BigInteger notifyId, int userId);

	List<NotificationBean> searchNotificationDetails(SearchVo searchVo);

	List<NotificationBean> searchArchiveNotificationDetails(SearchVo searchVo);

	int sendNotificationScheduler(Date date);

	String loadDesktopNotificationsByUserId(int userId, Integer limit);

	String getTotalNtfOfUserId(int userId);

	List<NotificationBean> getNotificationDetailsByPage(String startDate, String endDate, Integer startPage,
			Integer pageLength);

	Integer getNotificationDetailsByPageCount(String startDate, String endDate, Integer startPage, Integer pageLength);

	List<NotificationBean> getNotificationArchiveDetailsByPage(String sendStartDate, String sendEndDate,
			String startDate, String endDate, Integer startPage, Integer pageLength);

	Integer getNotificationArchiveDetailsCount(String sendStartDate, String sendEndDate, String startDate,
			String endDate, Integer startPage, Integer pageLength);

}
