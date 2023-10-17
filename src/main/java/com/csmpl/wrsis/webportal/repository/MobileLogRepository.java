package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.MobileLogEntity;

public interface MobileLogRepository extends JpaRepository<MobileLogEntity, Integer> {
	@Query(nativeQuery = true, value = "select mobilelog.vch_service_name,mobilelog.vch_req_details,cast(TO_CHAR(mobilelog.dtm_req_time, 'dd-Mon-YYYY hh:mm:ss AM')  as text) as reqdate,mobilelog.vch_res_details,cast(TO_CHAR(mobilelog.dtm_res_time, 'dd-Mon-YYYY')  as text) as resdate, \r\n"
			+ "users.vchuserfullname,users.vchusername,deg.nvchdesigname, \r\n" + " rl.role_name,\r\n"
			+ "lvl.nvchlevelname \r\n" + " from wrsis.t_wr_mobile_service_log  mobilelog \r\n"
			+ "inner join wrsis.t_user users on mobilelog.int_user_id=users.intuserid \r\n"
			+ "inner join wrsis.m_adm_designation deg on deg.intdesigid=users.intdesigid \r\n"
			+ "inner join wrsis.m_adm_user_role a on users.intuserid=a.int_user_id \r\n"
			+ "inner join wrsis.m_adm_role rl on rl.role_id=a.int_role_id \r\n"
			+ "inner join wrsis.m_adm_user_role ad  on ad.int_role_id=rl.role_id \r\n"
			+ "inner join wrsis.t_user userss  on userss.intuserid=a.int_user_id \r\n"
			+ "inner join wrsis.m_adm_leveldetails lvl  on users.intleveldetailid=lvl.intleveldetailid \r\n"
			+ "where  DATE_TRUNC('day', mobilelog.dtm_req_time )>=:startDate AND DATE_TRUNC('day', mobilelog.dtm_req_time)<=:endDate and mobilelog.bit_status=false  ")

	List<Object[]> getDataList(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_view_mobile_log_details_test(?1,?2,?3,?4,?5)")
	List<Object[]> getDataList(String startDate, String endDate, Integer start, Integer length, Integer logId);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_view_mobile_log_details_test(?1,?2,?3,?4,?5)")
	int getDataListCount(String startDate, String endDate, Integer pstart, Integer plength, Integer logId);

	@Query(nativeQuery = true, value = "select mobilelog.int_mob_ser_log_id,\r\n" + "mobilelog.vch_service_name,\r\n"
			+ "mobilelog.vch_req_details,\r\n"
			+ "cast(TO_CHAR(mobilelog.dtm_req_time, 'dd-Mon-YYYY hh:mm:ss AM')  as text) as reqdate,\r\n"
			+ "mobilelog.vch_res_details,\r\n" + "cast(TO_CHAR(mobilelog.dtm_res_time, 'dd-Mon-YYYY')  as text) as resdate \r\n"

			+ "from wrsis.t_wr_mobile_service_log  mobilelog \r\n"

			+ "where  DATE_TRUNC('day', mobilelog.dtm_req_time )>=:date AND DATE_TRUNC('day', mobilelog.dtm_req_time)<=:date2 and mobilelog.bit_status=false  ")

	List<Object[]> uKMetAndSurvey(Date date, Date date2);

}