package com.csmpl.wrsis.webportal.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, BigInteger> {

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dt_notification_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=false order by notification.int_notify_id desc;")
	List<Object[]> getNotificationDetails();

	@Query(nativeQuery = true, value = "select int_notify_id,int_org_id,int_rc_id,vch_notification_msg,bit_send_status,dt_notification_date from wrsis.t_wr_notification where int_notify_id=?1")
	List<Object[]> getNotificationDetailsByNotifyId(BigInteger notifyId);

	NotificationEntity findByNotifyId(BigInteger notifyId);

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dtm_send_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=true order by notification.int_notify_id desc;")
	List<Object[]> getArchiveNotificationDetails();

	@Query(nativeQuery = true, value = "select vch_notification_msg\r\n" + "from wrsis.t_wr_notification\r\n"
			+ "where int_notify_id=?1 and bitstatus=false")
	Object getNtfMsgByNotifyId(BigInteger notifyId);

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dtm_send_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=false and DATE_TRUNC('day', notification.dtmcreatedon ) >=  ?1  AND DATE_TRUNC('day', notification.dtmcreatedon )  <=  ?2 order by notification.int_notify_id desc;")
	List<Object[]> getNotificationDetailsByNtfDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dtm_send_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=true and DATE_TRUNC('day', notification.dtm_send_date ) >=  ?1  AND DATE_TRUNC('day', notification.dtm_send_date )  <=  ?2 order by notification.int_notify_id desc;")
	List<Object[]> getArchiveNotificationDetails(Date ntfDateFrom, Date ntfDateTo);

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dtm_send_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=true and DATE_TRUNC('day', notification.dtm_send_date ) >=  ?1  AND DATE_TRUNC('day', notification.dtm_send_date )  <=  ?2 and DATE_TRUNC('day', notification.dtmcreatedon ) >=  ?3  AND DATE_TRUNC('day', notification.dtmcreatedon )  <=  ?4 order by notification.int_notify_id desc;")
	List<Object[]> getArchiveNotificationDetails(Date ntfDateFrom, Date ntfDateTo, Date ntfCreatedFrom,
			Date ntfCreatedTo);

	@Query(nativeQuery = true, value = "select notification.int_notify_id,notification.dtmcreatedon,\r\n"
			+ "notification.vch_notification_msg,\r\n"
			+ "(select nvchlevelname from wrsis.m_adm_leveldetails where intleveldetailid=notification.int_org_id),\r\n"
			+ "(select vch_research_center_name from  wrsis.m_wr_research_center where int_research_center_id=notification.int_rc_id),\r\n"
			+ "notification.bit_send_status,notification.dtm_send_date "
			+ "from wrsis.t_wr_notification notification\r\n"
			+ "where notification.bitstatus=false and notification.bit_send_status=true and DATE_TRUNC('day', notification.dtmcreatedon ) >=  ?1  AND DATE_TRUNC('day', notification.dtmcreatedon )  <=  ?2 order by notification.int_notify_id desc;")
	List<Object[]> getArchiveNotificationDetailsByCreatedDate(Date ntfCreatedFrom, Date ntfCreatedTo);

	List<NotificationEntity> findByNotificationDateAndSendStatusAndStatus(Date thisDate, boolean b, boolean c);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_notification_details(?1,?2,?3,?4)")
	List<Object[]> getNotificationDetailsByPage(String sDate, String eDate, Integer startPage, Integer pageLength);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_notification_details(?1,?2,?3,?4)")
	Integer getNotificationDetailsByPageCount(String sDate, String eDate, Integer startPage, Integer pageLength);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_notification_archive_details(?1,?2,?3,?4,?5,?6)")
	List<Object[]> getNotificationArchiveDetailsByPage(String sendSDate, String sendEDate, String sDate, String eDate,
			Integer startPage, Integer pageLength);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_notification_archive_details(?1,?2,?3,?4,?5,?6)")
	Integer getNotificationDetailsArchiveByPageCount(String sendSDate, String sendEDate, String sDate, String eDate,
			Integer startPage, Integer pageLength);

}
