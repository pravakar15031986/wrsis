package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.MobileLoginTracking;

@Repository("mobileLoginTrackingRepository")
public interface MobileLoginTrackingRepository extends JpaRepository<MobileLoginTracking, Integer> {

	@Query(nativeQuery = true, value = "select track.vch_fcm_id from wrsis.t_wr_app_tracking track\r\n"
			+ "where track.int_created_by=?1 and track.bit_login_status=true and track.bit_status=false ORDER BY track.int_app_track_id DESC limit 1")
	String getFcmIdByUserId(Short toUserId);

	@Query(nativeQuery = true, value = "select track.vch_fcm_id from wrsis.t_wr_app_tracking track\r\n"
			+ "where track.int_created_by=?1 and track.bit_login_status=true and track.bit_status=false ORDER BY track.int_app_track_id DESC limit 1")
	String getfcmiddetails(Short toUserId);

	@Query(nativeQuery = true, value = " SELECT cast(int_app_track_id as text),cast(int_created_by as text) FROM wrsis.t_wr_app_tracking where int_created_by =:userId "
			+ " and vch_fcm_id=:fcmId and bit_login_status=true and bit_status = false ")
	List<Object[]> mobileUserLogout(@Param("userId") Integer userId, @Param("fcmId") String fcmId);

	MobileLoginTracking findByTrackId(Integer trackId);

}
