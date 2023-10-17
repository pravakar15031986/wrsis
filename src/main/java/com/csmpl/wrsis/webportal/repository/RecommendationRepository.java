package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.RecommendationEntiry;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntiry, Integer> {

	@Query("from RecommendationEntiry where status='false' ORDER BY createdOn DESC")
	List<RecommendationEntiry> viewAllRecommecdation();

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where r.bitstatus=false AND   DATE_TRUNC('day', r.dtmcreatedon) >= ?1  AND DATE_TRUNC('day', r.dtmcreatedon)<=?2 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByDate(@Param("sDate") Date sDate, @Param("eDate") Date eDate);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where   r.bitstatus=false AND DATE_TRUNC('day', r.dtmcreatedon ) >= ?1  AND DATE_TRUNC('day', r.dtmcreatedon)  <=?2 and r.vch_recom_no=?3 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByDateAndRecommendNumber(@Param("sDate") Date sDate, @Param("eDate") Date eDate,
			String recmmendNo);

	@Query("From RecommendationEntiry where status='false' and recomendNo=:recmmendNo ")
	List<RecommendationEntiry> searchByRecommendNumber(String recmmendNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + " where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 order by rec.int_recom_id desc")
	List<Object[]> getRecommendation();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type,rec.vch_recom_file_name\r\n" + "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  rec.int_recom_id=?1 and  \r\n"
			+ "rec.bitstatus =false order by rec.int_recom_id desc")
	List<Object[]> getRecommendationById(int recomendId);

	RecommendationEntiry findByRecomendId(int recomendId);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on, \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec  \r\n" + " where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationsForwardedByBoa();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and (rec.int_recom_status=1 or rec.int_recom_status=3) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendations();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  rec.vch_recom_no=?1 and\r\n"
			+ "rec.bitstatus =false and (rec.int_recom_status=1 or rec.int_recom_status=3) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  DATE_TRUNC('day', rec.dt_publish_on ) >=  ?1  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?2 and\r\n"
			+ "rec.bitstatus =false and (rec.int_recom_status=1 or rec.int_recom_status=3) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsByRecDate(Date recDtFrm, Date recDtFrm2);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  rec.vch_recom_no=?1 and  DATE_TRUNC('day', rec.dt_publish_on ) >=  ?2  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?3 and\r\n"
			+ "rec.bitstatus =false and (rec.int_recom_status=1 or rec.int_recom_status=3) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsByRecNoAndDate(String recNo, Date recDtFrm, Date recDtFrm2);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExperts();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  rec.vch_recom_no=?1 and  DATE_TRUNC('day', rec.dt_publish_on ) >=  ?2  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?3 and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecNoAndDate(String recNo, Date recDtFrm, Date recDtTo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where DATE_TRUNC('day', rec.dt_publish_on ) >=  ?1  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?2 and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecDate(Date recDtFrm, Date recDtTo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "where  rec.vch_recom_no=?1 and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select recloc.int_recom_id,recloc.int_region_id,\r\n"
			+ "demo1.vch_demography_name as region_name,recloc.int_zone_id,demo2.vch_demography_name as zone_name,\r\n"
			+ "recloc.int_woreda_id,demo3.vch_demography_name as woreda_name,\r\n"
			+ "STRING_AGG(CAST(recloc.int_kebel_id AS varchar),',') as kebele_id,\r\n"
			+ "STRING_AGG(CAST(demo4.vch_demography_name AS varchar),',') as kebele_name\r\n"
			+ "from wrsis.t_wr_recommendation_loc recloc \r\n"
			+ "left join wrsis.m_wr_demography demo1 on recloc.int_region_id=demo1.int_demography_id \r\n"
			+ "left join wrsis.m_wr_demography demo2 on recloc.int_zone_id=demo2.int_demography_id \r\n"
			+ "left join wrsis.m_wr_demography demo3 on recloc.int_woreda_id=demo3.int_demography_id\r\n"
			+ "left join wrsis.m_wr_demography demo4 on recloc.int_kebel_id=demo4.int_demography_id\r\n"
			+ "where recloc.int_recom_id=?1 and recloc.bitstatus=false\r\n"
			+ "group by recloc.int_recom_id,recloc.int_region_id,demo1.vch_demography_name,recloc.int_zone_id,\r\n"
			+ "demo2.vch_demography_name  ,recloc.int_woreda_id,demo3.vch_demography_name ")
	List<Object[]> getRecLocDetailsByRecId(int recId);

	/*@Query(nativeQuery = true, value = " select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,rec.vch_recom_file_name\r\n"
			+ "from wrsis.t_wr_recommendation rec\r\n"
			+ "left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id\r\n"
			+ "where rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details where intcreatedby=:userId) order by rec.int_recom_id desc ")
	List<Object[]> viewRecommendationData(@Param("userId") Integer userId);*/
	
	
	@Query(nativeQuery = true, value = " SELECT  cast(rec.int_recom_id as text),\r\n" + 
			"cast(rec.vch_recom_no as text),\r\n" + 
			"cast(TO_CHAR(rec.dt_publish_on, 'dd-Mon-YYYY')  as text),\r\n" + 
			"cast(rec.vch_recom_file_name as text)\r\n" + 
			"from wrsis.t_wr_recommendation rec  \r\n" + 
			"inner join wrsis.t_wr_recom_forward_log recforw on rec.int_recom_id=recforw.int_recom_id\r\n" + 
			"--left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id \r\n" + 
			"left join wrsis.t_wr_recommendation_loc recloc on rec.int_recom_id = recloc.int_recom_id\r\n" + 
			"where rec.bitstatus =false and \r\n" + 
			"recforw.int_region_id in (select reg.int_demography_id \r\n" + 
			"                         from wrsis.m_wr_demography reg\r\n" + 
			"                         inner join wrsis.m_wr_demography zone on zone.int_parent_id=reg.int_demography_id\r\n" + 
			"                         inner join wrsis.m_wr_demography woreda on woreda.int_parent_id=zone.int_demography_id\r\n" + 
			"                         where reg.bitstatus=false and zone.bitstatus=false and woreda.bitstatus=false and \r\n" + 
			"                         woreda.int_demography_id in  (select int_demography_id from wrsis.t_wr_user_location_details\r\n" + 
			"                                                      where bitstatus=false  and int_user_loc_tag_id = (select int_user_loc_tag_id \r\n" + 
			"                                                                                                       from wrsis.t_wr_user_location_tag \r\n" + 
			"                                                                                                       where int_user_id=:userId and bitstatus=false)))\r\n" + 
			"                                                        \r\n" + 
			"and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details \r\n" + 
			"                             where int_woreda_id in (select int_demography_id from wrsis.t_wr_user_location_details\r\n" + 
			"                                                     where bitstatus=false  and int_user_loc_tag_id = (select int_user_loc_tag_id \r\n" + 
			"                                                                                                      from wrsis.t_wr_user_location_tag \r\n" + 
			"                                                                                                      where int_user_id=:userId and bitstatus=false))) \r\n" + 
			"                                                                                                      \r\n" + 
			"GROUP BY rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on order by rec.int_recom_id desc  ")
	List<Object[]> viewRecommendationData(@Param("userId") Integer userId);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id\r\n"
			+ "where rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details where intcreatedby=:userId) order by rec.int_recom_id desc ")
	List<Object[]> getMoaRecommendationsForWExpertsImplementation(@Param("userId") Integer userId);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id\r\n"
			+ "where  rec.vch_recom_no=:recNo and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details where intcreatedby=:userId) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecNoImplementation(@Param("recNo") String recNo,
			@Param("userId") Integer userId);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id\r\n"
			+ "where DATE_TRUNC('day', rec.dt_publish_on ) >=  ?1  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?2 and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details where intcreatedby=?3) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecDateImplementation(Date recDtFrm, Date recDtTo, Integer userId);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on \r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n"
			+ "left join wrsis.t_wr_monitor_details mdetails on rec.int_recom_id = mdetails.int_recom_id\r\n"
			+ "where  rec.vch_recom_no=?1 and  DATE_TRUNC('day', rec.dt_publish_on ) >=  ?2  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?3 and\r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_id not in (select int_recom_id from wrsis.t_wr_monitor_details where intcreatedby=?4) order by rec.int_recom_id desc")
	List<Object[]> getMoaRecommendationsForWExpertsByRecNoAndDateImplementation(String recNo, Date recDtFrm,
			Date recDtTo, Integer userId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_region_specific_recommendation_by_boa(?1,?2,?3,?4,?5,?6,?7)")
	List<Object[]> getRecommendationRegionSpecific(String recNo, int regionId, int zoneId, int woredaId, int kebeleId,
			String startDate, String endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type \r\n" + "from wrsis.t_wr_recommendation rec \r\n" + " where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_type=1 and rec.int_recom_status=1 and (rec.vch_recom_no LIKE %:recNo%) order by rec.int_recom_id desc")
	List<Object[]> getRecommendationForGeneralByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec  \r\n" + " where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_type=1 and rec.int_recom_status=1 and DATE_TRUNC('day', rec.dt_publish_on ) >=  ?1  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  ?2  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationForGeneralByRecDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 and rec.int_recom_type=1 and (rec.vch_recom_no LIKE %:recNo%) and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationForGeneralByRecNoRecDate(String recNo, Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 and rec.int_recom_type=1  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationForGeneral();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 and (rec.vch_recom_no LIKE %:recNo%)  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationByRecDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.int_recom_type\r\n" + "from wrsis.t_wr_recommendation rec \r\n" + " where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=1 and (rec.vch_recom_no LIKE %:recNo%) and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate  order by rec.int_recom_id desc")
	List<Object[]> getRecommendationByRecNoRecDate(String recNo, Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_region_specific_recommendation_forwarded_by_boa(?1,?2,?3,?4,?5,?6,?7);")
	List<Object[]> getRecommendationForwardedRegionSpecific(String recNo, int regionId, int zoneId, int woredaId,
			int kebeleId, String startDate, String endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_type=1 order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedForGeneral();

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_type=1 and (rec.vch_recom_no LIKE %:recNo%) order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedForGeneralByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and rec.int_recom_type=1 and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedForGeneralByRecDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and (rec.vch_recom_no LIKE %:recNo%) and rec.int_recom_type=1 and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedForGeneralByRecNoRecDate(String recNo, Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec   \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and (rec.vch_recom_no LIKE %:recNo%) order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedByRecNo(String recNo);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedByRecDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,   \r\n"
			+ "rec.dt_forward_on,rec.int_recom_type,rec.vch_forward_remarks\r\n"
			+ "from wrsis.t_wr_recommendation rec \r\n" + "where \r\n"
			+ "rec.bitstatus =false and rec.int_recom_status=3 and (rec.vch_recom_no LIKE %:recNo%) and DATE_TRUNC('day', rec.dt_publish_on ) >=  :startDate  AND DATE_TRUNC('day', rec.dt_publish_on )  <=  :endDate order by rec.dt_forward_on desc")
	List<Object[]> getRecommendationForwordedByRecNoRecDate(String recNo, Date startDate, Date endDate);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where   r.bitstatus=false AND r.int_recom_type=?1 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByRecommendType(Integer recomType);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where   r.bitstatus=false AND r.int_recom_type=?1 AND r.vch_recom_no=?2 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByRecomTypeAndRecNo(Integer recomType, String recNo);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where   r.bitstatus=false AND r.int_recom_type=?1 AND r.vch_recom_no=?4 AND DATE_TRUNC('day', r.dtmcreatedon ) >= ?2  AND DATE_TRUNC('day', r.dtmcreatedon)  <=?3 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByRecTypeAndDateAndRecNo(Integer recomType, Date sdate, Date edate, String recNo);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, r.vch_recom_file_name, r.vch_recom_summary, \r\n"
			+ "r.int_recom_status, r.int_publish_by, r.dt_publish_on, r.vch_publish_remarks, r.int_forward_by, r.dt_forward_on, r.vch_forward_remarks, r.bitstatus, \r\n"
			+ "r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation r where   r.bitstatus=false AND r.int_recom_type=?1 AND DATE_TRUNC('day', r.dtmcreatedon ) >= ?2  AND DATE_TRUNC('day', r.dtmcreatedon)  <=?3 order by r.dtmcreatedon DESC", nativeQuery = true)
	List<RecommendationEntiry> searchByRecTypeAndDate(Integer recomType, Date date1, Date date2);

	@Query(value = "select * from wrsis.sp_wr_recommend_pending_published_details_byuser(:recommendType,:recoMendNo,:startDate,:endDate,:userId,:processId,:actionStartus,:start,:length)", nativeQuery = true)
	List<Object[]> viewAllRecommendationByPage(@Param("recommendType") Integer recommendType,
			@Param("recoMendNo") String recoMendNo, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("userId") Integer userId, @Param("processId") Integer processId,
			@Param("actionStartus") Integer actionStartus, @Param(WrsisPortalConstant.START) Integer start,
			@Param("length") Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_recommend_pending_published_details_byuser(:recommendType,:recoMendNo,:startDate, :endDate,:userId,:processId,:actionStartus, :start,:length)", nativeQuery = true)
	Integer viewAllCountRecommendationByPage(@Param("recommendType") Integer recomType,
			@Param("recoMendNo") String recoMendNo, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("userId") Integer userId, @Param("processId") Integer processId,
			@Param("actionStartus") Integer actionStartus, @Param(WrsisPortalConstant.START) Integer start,
			@Param("length") Integer length);

	@Query(value = "select * from wrsis.sp_wr_recommend_details(:recommendType,:recoMendNo,:startDate,:endDate,:start,:length)", nativeQuery = true)
	List<Object[]> viewRecommendationPage(@Param("recommendType") Integer recommendType,
			@Param("recoMendNo") String recoMendNo, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param(WrsisPortalConstant.START) Integer start, @Param("length") Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_recommend_details(:recommendType,:recoMendNo,:startDate, :endDate,:start,:length)", nativeQuery = true)
	Integer viewCountRecommendationPage(@Param("recommendType") Integer recommendType,
			@Param("recoMendNo") String recoMendNo, @Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param(WrsisPortalConstant.START) Integer start, @Param("length") Integer length);

	@Query(value = "select * from wrsis.sp_wr_add_monitor_recommendation1(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	List<Object[]> viewAddMonitorRecommendation(String recNo, String recDtFrom, String recDtTo, Integer userId,
			Integer pstart, Integer pLength);

	@Query(value = "select count(*) from wrsis.sp_wr_add_monitor_recommendation1(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	Integer viewAddMonitorRecommendationCount(String recDtFrom, String recDtTo, String recNo, Integer userId,
			Integer pstart, Integer pLength);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_view_publish_recommendation_by_boa(?1,?2,?3,?4, ?5,?6,?7)")
	List<Object[]> getRecommendationByBOA(Integer recomType, String recommendNo, String startDate,
			String endDate, Integer pstart, Integer pLength, Integer userId);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_view_publish_recommendation_by_boa(?1,?2,?3,?4, ?5,?6,?7)")
	Integer getRecommendationByBOACount(Integer recomType, String recommendNo, String startDate,
			String endDate, Integer pstart, Integer pLength, Integer userId);

	@Query(value = "select * from wrsis.sp_wr_moa_recommend_details(:startDate,:endDate,:recoMendNo,:start,:length)", nativeQuery = true)
	List<Object[]> viewMOARecommendationPage(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("recoMendNo") String recoMendNo, @Param(WrsisPortalConstant.START) Integer start, @Param("length") Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_moa_recommend_details(:startDate,:endDate,:recoMendNo,:start,:length)", nativeQuery = true)
	Integer viewMOARecommendationPageCount(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("recoMendNo") String recoMendNo, @Param(WrsisPortalConstant.START) Integer start, @Param("length") Integer length);

	@Query(nativeQuery=true,value=" select rec.int_recom_type from wrsis.t_wr_recommendation rec\r\n"
			+ "                inner join wrsis.t_wr_recom_forward_log recforw on rec.int_recom_id=recforw.int_recom_id\r\n"
			+ "                where  rec.bitstatus=false and rec.int_recom_id=?1\r\n"
			+ "                group by rec.int_recom_type")
	Integer getRecomendTypeByRecomId(int recomId);

	/**
	 * @param recommendId
	 * @return
	 * Object []
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  00-00-2020
	 */
	@Query(nativeQuery=true,value="SELECT int_region_id from wrsis.t_wr_recommendation_loc where bitstatus=false AND int_recom_id=?1")
	Integer[] getRegionIdByRecId(Integer recommendId);
	
	/**
	 * @param recomendId
	 * @return
	 * Integer []
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  00-00-2020
	 */
	
	@Query(nativeQuery=true,value="SELECT int_woreda_id from wrsis.t_wr_recommendation_loc where bitstatus=false AND int_recom_id=?1")
	Integer[] getWoredaIdByRecId(Integer recomendId);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_view_forwarded_recommendation_by_boa(?1,?2,?3,?4, ?5,?6,?7)")
	Integer getCountRecommendationForwardedByBOA(Integer recomType, String recommendNo, String startDate,
			String endDate, Integer pstart, Integer pLength, Integer userId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_view_forwarded_recommendation_by_boa(?1,?2,?3,?4, ?5,?6,?7)")
	List<Object[]> getRecommendationForwardedByBOA(Integer recomType, String recommendNo, String startDate,
			String endDate, Integer pstart, Integer pLength, Integer userId);

	@Query(nativeQuery = true, value = "select recloc.int_recom_id,recloc.int_region_id, \r\n"
			+ "demo1.vch_demography_name as region_name,recloc.int_zone_id,demo2.vch_demography_name as zone_name, \r\n"
			+ "recloc.int_woreda_id,demo3.vch_demography_name as woreda_name, \r\n"
			+ "STRING_AGG(CAST(recloc.int_kebel_id AS varchar),',') as kebele_id, \r\n"
			+ "STRING_AGG(CAST(demo4.vch_demography_name AS varchar),',') as kebele_name \r\n"
			+ "from wrsis.t_wr_recommendation_loc recloc \r\n"
			+ "left join wrsis.m_wr_demography demo1 on recloc.int_region_id=demo1.int_demography_id \r\n"
			+ "left join wrsis.m_wr_demography demo2 on recloc.int_zone_id=demo2.int_demography_id \r\n"
			+ "left join wrsis.m_wr_demography demo3 on recloc.int_woreda_id=demo3.int_demography_id \r\n"
			+ "left join wrsis.m_wr_demography demo4 on recloc.int_kebel_id=demo4.int_demography_id \r\n"
			+ "where recloc.int_recom_id=?1 and recloc.bitstatus=false and recloc.int_region_id=?2 \r\n"
			+ "group by recloc.int_recom_id,recloc.int_region_id,demo1.vch_demography_name,recloc.int_zone_id, \r\n"
			+ "demo2.vch_demography_name  ,recloc.int_woreda_id,demo3.vch_demography_name")
	List<Object[]> getRecLocDetailsByRecIdAndRegId(Integer recId, Integer boaRegId);
	
	 @Query(nativeQuery = true, value = "select recloc.int_recom_id,recloc.int_region_id, \r\n"
	 		+ "demo1.vch_demography_name as region_name,recloc.int_zone_id,demo2.vch_demography_name as zone_name, \r\n"
	 		+ "recloc.int_woreda_id,demo3.vch_demography_name as woreda_name, \r\n"
	 		+ "STRING_AGG(CAST(recloc.int_kebel_id AS varchar),',') as kebele_id, \r\n"
	 		+ "STRING_AGG(CAST(demo4.vch_demography_name AS varchar),',') as kebele_name \r\n"
	 		+ "from wrsis.t_wr_recommendation_loc recloc \r\n"
	 		+ "inner join wrsis.t_wr_recom_forward_log recfor on recloc.int_recom_id=recfor.int_recom_id \r\n"
	 		+ "left join wrsis.m_wr_demography demo1 on recloc.int_region_id=demo1.int_demography_id \r\n"
	 		+ "left join wrsis.m_wr_demography demo2 on recloc.int_zone_id=demo2.int_demography_id \r\n"
	 		+ "left join wrsis.m_wr_demography demo3 on recloc.int_woreda_id=demo3.int_demography_id \r\n"
	 		+ "left join wrsis.m_wr_demography demo4 on recloc.int_kebel_id=demo4.int_demography_id \r\n"
	 		+ "where recloc.int_recom_id=140 and recloc.bitstatus=false and recfor.int_region_id=recloc.int_region_id \r\n"
	 		+ "group by recloc.int_recom_id,recloc.int_region_id,demo1.vch_demography_name,recloc.int_zone_id,  \r\n"
	 		+ "demo2.vch_demography_name  ,recloc.int_woreda_id,demo3.vch_demography_name")
	List<Object[]> getForwardedRecLocDetailsByRecId(Integer recId);

	
	

	
}
