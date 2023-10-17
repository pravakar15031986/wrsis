package com.csmpl.wrsis.webportal.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;

@Repository
public interface DashboardNotificationRepository extends JpaRepository<DashboardNotificationEntity, BigInteger> {

	@Query(nativeQuery = true, value = "SELECT dn.int_dashboard_notify_id,dn.vch_notification_msg,dn.dtmcreatedon,dn.bit_read_status,usr.vchuserfullname,usr.vchusername\r\n"
			+ "FROM wrsis.t_wr_dashboard_notification dn\r\n"
			+ "INNER JOIN wrsis.t_user usr on dn.intcreatedby=usr.intuserid\r\n"
			+ "WHERE dn.int_to_user_id=?1 AND dn.bitstatus=false\r\n"
			+ "ORDER by dtmcreatedon DESC LIMIT 10 OFFSET ?2") // LIMIT 8
	List<Object[]> getDesktopNotificationsByUserIdandStatus(int userId, Integer offset);

	@Query(nativeQuery = true, value = "SELECT dn.int_dashboard_notify_id,dn.vch_notification_msg,dn.dtmcreatedon,dn.bit_read_status,usr.vchuserfullname,usr.vchusername\r\n"
			+ "FROM wrsis.t_wr_dashboard_notification dn\r\n"
			+ "INNER JOIN wrsis.t_user usr on dn.intcreatedby=usr.intuserid\r\n"
			+ "WHERE dn.int_to_user_id=?1 AND dn.bitstatus=false\r\n"
			+ "ORDER by dtmcreatedon DESC LIMIT 10 ") // LIMIT 8
	List<Object[]> getDesktopNotificationsByUserIdandStatus(int userId);

	@Query(nativeQuery = true, value = "SELECT int_dashboard_notify_id,vch_notification_msg,dtmcreatedon\r\n"
			+ "FROM wrsis.t_wr_dashboard_notification\r\n"
			+ "WHERE int_to_user_id=?1 AND bit_read_status=false AND bitstatus=false\r\n"
			+ "ORDER by dtmcreatedon DESC")
	List<Object[]> getUnreadDesktopNotificationsByUserIdandStatus(int userId);

	@Query("select notif from DashboardNotificationEntity notif where notif.dashboardNotifyId =?1 and notif.toUserId =?2")
	DashboardNotificationEntity findByDashboardNotifyIdAndToUserId(BigInteger bigInteger, Short userId);

	@Query(nativeQuery = true, value = "SELECT dn.int_dashboard_notify_id,dn.vch_notification_msg,dn.dtmcreatedon,dn.bit_read_status,usr.vchuserfullname,usr.vchusername\r\n"
			+ "FROM wrsis.t_wr_dashboard_notification dn\r\n"
			+ "INNER JOIN wrsis.t_user usr on dn.intcreatedby=usr.intuserid\r\n"
			+ "WHERE dn.int_to_user_id=?1 AND dn.bitstatus=false\r\n"
			+ "ORDER by dtmcreatedon DESC LIMIT ?2")
	List<Object[]> loadDesktopNotificationsByUserIdandStatus(int userId, Integer limit);

	@Query(nativeQuery = true, value = "SELECT dn.int_dashboard_notify_id,dn.vch_notification_msg,dn.dtmcreatedon,dn.bit_read_status,usr.vchuserfullname,usr.vchusername\r\n"
			+ "FROM wrsis.t_wr_dashboard_notification dn\r\n"
			+ "INNER JOIN wrsis.t_user usr on dn.intcreatedby=usr.intuserid\r\n"
			+ "WHERE dn.int_to_user_id=?1 AND dn.bitstatus=false\r\n" + " ORDER by dtmcreatedon DESC")
	List<Object[]> getTotalDesktopNotificationsByUserIdandStatus(int userId);

	@Query(nativeQuery = true, value = "SELECT count(*) \r\n" + "FROM wrsis.t_wr_dashboard_notification\r\n"
			+ "WHERE int_to_user_id=?1  AND bitstatus=false\r\n" + "")
	Integer getDesktopNotificationsByUserId(int userId);

}
