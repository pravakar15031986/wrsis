package com.csmpl.wrsis.webportal.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.NotificationUserDetailsEntity;

@Repository
public interface NotificationUserDetailsRepository extends JpaRepository<NotificationUserDetailsEntity, BigInteger> {

	@Query(nativeQuery = true, value = "select org.nvchlevelname,usr.vchuserfullname, STRING_AGG (rol.alias_name,',') \r\n"
			+ "from wrsis.t_wr_notification_user_details n  \r\n"
			+ "inner join wrsis.t_user usr on  n.int_user_id=usr.intuserid \r\n"
			+ "inner join wrsis.m_adm_user_role usrol on usr.intuserid=usrol.int_user_id \r\n"
			+ "inner join wrsis.m_adm_role rol on usrol.int_role_id=rol.role_id \r\n"
			+ "inner join wrsis.m_adm_leveldetails org on usr.intleveldetailid=org.intleveldetailid \r\n"
			+ "where n.int_notify_id =?1 and n.bitstatus=false \r\n"
			+ "group by org.nvchlevelname,usr.vchuserfullname,usr.intuserid;")
	List<Object[]> getMemberDetailsByNotifyId(int notifyId);

	@Query(nativeQuery = true, value = "select n.int_user_id,usr.vchuserfullname ,usr.vchusername \r\n"
			+ "from wrsis.t_wr_notification_user_details n  \r\n"
			+ "inner join wrsis.t_user usr on  n.int_user_id=usr.intuserid \r\n"
			+ "where n.int_notify_id =?1 and n.bitstatus=false")
	List<Object[]> getMembersByNotifyId(int notifyId);

	List<NotificationUserDetailsEntity> findByNotifyId(BigInteger notifyId);

	List<NotificationUserDetailsEntity> findByUserIdAndNotifyId(Short userId, BigInteger notifyId);

	@Query("select userId from NotificationUserDetailsEntity where notifyId=?1 and status=false ")
	List<Short> getUserIdByNotifyIdAndStatus(BigInteger notifyId, boolean b);

	List<NotificationUserDetailsEntity> findUserIdByNotifyIdAndStatus(BigInteger notifyId, boolean b);

}
