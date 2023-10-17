package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;

@Repository
public interface AdvisoryRepository extends JpaRepository<AdvisoryEntiry, Integer> {

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtmcreatedon,adv.vch_advisory_file_name,adv.vch_advisory_remarks,adv.dtm_publish_date,adv.int_advisory_status "
			+ "from wrsis.t_wr_advisory_details adv "
			+ "where adv.vch_advisory_no=?1 and adv.bitstatus=false order by adv.int_advisory_id desc")
	List<Object[]> getAdvListByAdvNo(String advNo);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtmcreatedon,adv.vch_advisory_file_name,adv.vch_advisory_remarks,adv.dtm_publish_date,adv.int_advisory_status "
			+ "from wrsis.t_wr_advisory_details adv "
			+ "where DATE_TRUNC('day', adv.dtmcreatedon ) >=  ?1  AND DATE_TRUNC('day', adv.dtmcreatedon )  <=  ?2 and adv.bitstatus=false order by adv.int_advisory_id desc")
	List<Object[]> getAdvListBySummaryDate(Date uploadDtfrom, Date uploadDtTo);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtmcreatedon,adv.vch_advisory_file_name,adv.vch_advisory_remarks,adv.dtm_publish_date,adv.int_advisory_status "
			+ "from wrsis.t_wr_advisory_details adv "
			+ "where adv.vch_advisory_no=?1 and DATE_TRUNC('day', adv.dtmcreatedon ) >=  ?2  AND DATE_TRUNC('day', adv.dtmcreatedon )  <=  ?3 and adv.bitstatus=false order by adv.int_advisory_id desc")
	List<Object[]> getAdvListByAdvNoAndSummaryDate(String advNo, Date summaryFromDate, Date summaryToDate);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtmcreatedon,adv.vch_advisory_file_name,adv.vch_advisory_remarks,adv.dtm_publish_date,adv.int_advisory_status "
			+ "from wrsis.t_wr_advisory_details adv where adv.bitstatus=false order by adv.int_advisory_id desc")
	List<Object[]> getAdvList();

	@Query(value = "SELECT int_advisory_id, vch_advisory_no, dt_summary_from_date, dt_summary_to_date, vch_advisory_file_name, vch_advisory_remarks, dtm_publish_date, int_published_by, int_advisory_status, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ " FROM wrsis.t_wr_advisory_details where int_advisory_status=1 and bitstatus=false and int_advisory_id not in(select int_advisory_id from wrsis.t_wr_recommendation where bitstatus=false) order by dtmcreatedon desc;", nativeQuery = true)
	List<AdvisoryEntiry> vlewAllPublishedAdvisoryByStatus();

	@Query(value = "SELECT int_advisory_id, vch_advisory_no, dt_summary_from_date, dt_summary_to_date, vch_advisory_file_name, vch_advisory_remarks, dtm_publish_date, int_published_by, int_advisory_status, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ " FROM wrsis.t_wr_advisory_details where int_advisory_status=1 and DATE_TRUNC('day', dtm_publish_date ) >=  ?1  AND DATE_TRUNC('day', dtm_publish_date )  <=  ?2 and bitstatus=false and int_advisory_id not in(select int_advisory_id from wrsis.t_wr_recommendation where bitstatus=false);", nativeQuery = true)
	List<AdvisoryEntiry> searchAllPublishedAdvisoryByStatus(Date startdate, Date endDate);

	@Query(value = "SELECT int_advisory_id, vch_advisory_no, dt_summary_from_date, dt_summary_to_date, vch_advisory_file_name, vch_advisory_remarks, dtm_publish_date, int_published_by, int_advisory_status, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "	FROM wrsis.t_wr_advisory_details where int_advisory_status=1 and (vch_advisory_no LIKE %:advisoryNo%) and bitstatus=false and int_advisory_id not in(select int_advisory_id from wrsis.t_wr_recommendation where bitstatus=false);", nativeQuery = true)
	List<AdvisoryEntiry> searchAllPublishedAdvisoryByNoStatus(String advisoryNo);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtm_publish_date,adv.vch_advisory_file_name,adv.vch_advisory_remarks "
			+ " from wrsis.t_wr_advisory_details adv where adv.bitstatus=false and adv.int_advisory_status=1 order by adv.int_advisory_id desc")
	List<Object[]> getPubAdvList();

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtm_publish_date,adv.vch_advisory_file_name,adv.vch_advisory_remarks "
			+ " from wrsis.t_wr_advisory_details adv where adv.vch_advisory_no=?1 and adv.bitstatus=false and adv.int_advisory_status=1 order by adv.int_advisory_id desc")
	List<Object[]> getPubAdvListByAdvNo(String advNo);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtm_publish_date,adv.vch_advisory_file_name,adv.vch_advisory_remarks "
			+ " from wrsis.t_wr_advisory_details adv where DATE_TRUNC('day', adv.dtm_publish_date ) >=  ?1  AND DATE_TRUNC('day', adv.dtm_publish_date )  <=  ?2 and adv.bitstatus=false "
			+ " and adv.int_advisory_status=1 order by adv.int_advisory_id desc")
	List<Object[]> getPubAdvListByAdvDt(Date advDtFrm, Date advDtTo);

	@Query(nativeQuery = true, value = "select adv.int_advisory_id,adv.vch_advisory_no,adv.dtm_publish_date,adv.vch_advisory_file_name,adv.vch_advisory_remarks "
			+ " from wrsis.t_wr_advisory_details adv where adv.vch_advisory_no=?1 and DATE_TRUNC('day', adv.dtm_publish_date ) >=  ?2  AND DATE_TRUNC('day', adv.dtm_publish_date )  <=  ?3 and adv.bitstatus=false "
			+ " and adv.int_advisory_status=1 order by adv.int_advisory_id desc")
	List<Object[]> getPubAdvListByAdvNoAndAdvDate(String advNo, Date advDtFrm, Date advDtTo);

	@Query(value = "SELECT int_advisory_id, vch_advisory_no, dt_summary_from_date, dt_summary_to_date, vch_advisory_file_name, vch_advisory_remarks, dtm_publish_date, int_published_by, int_advisory_status, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon	FROM wrsis.t_wr_advisory_details where vch_advisory_no=?1", nativeQuery = true)
	AdvisoryEntiry fetchAdvisoryByAdvisoryNumber(String advNo);

	@Query(value = "SELECT int_advisory_id, vch_advisory_no, dt_summary_from_date, dt_summary_to_date, vch_advisory_file_name, vch_advisory_remarks, dtm_publish_date, int_published_by, int_advisory_status, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "	FROM wrsis.t_wr_advisory_details where int_advisory_status=1 and DATE_TRUNC('day', dtm_publish_date ) >=  ?1  AND DATE_TRUNC('day', dtm_publish_date )  <=  ?2 and (vch_advisory_no LIKE %?3%) and bitstatus=false and int_advisory_id not in(select int_advisory_id from wrsis.t_wr_recommendation where bitstatus=false);", nativeQuery = true)
	List<AdvisoryEntiry> vlewAllPublishedAdvisoryByStatus(Date startDate, Date endDate, String adviNo);

	@Query(value = "select * from  wrsis.sp_wr_advisory_view(:sDate, :eDate,:advisoryNo,:pStart,:pLength)", nativeQuery = true)
	List<Object[]> viewAdvisoryPageWise(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("pStart") Integer pStart, @Param("pLength") Integer pLength);

	@Query(value = "select count(*) from  wrsis.sp_wr_advisory_view(:sDate, :eDate,:advisoryNo,:pStart,:pLength)", nativeQuery = true)
	Integer viewAdvisoryPageCount(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("pStart") Integer pStart, @Param("pLength") Integer pLength);

	@Query(value = "select * from  wrsis.sp_wr_advisory_pending_publish(:sDate, :eDate,:advisoryNo,:actionStatus,:pStart,:pLength)", nativeQuery = true)
	List<Object[]> viewAdvisoryPageWiseApp(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("actionStatus") Integer actionStatus,
			@Param("pStart") Integer pStart, @Param("pLength") Integer pLength);

	@Query(value = "select count(*) from  wrsis.sp_wr_advisory_pending_publish(:sDate, :eDate,:advisoryNo,:actionStatus,:pStart,:pLength)", nativeQuery = true)
	Integer viewAdvisoryPageCountApp(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("actionStatus") Integer actionStatus,
			@Param("pStart") Integer pStart, @Param("pLength") Integer pLength);

	@Query(value = "select * from  wrsis.sp_wr_advisory_publish_view(:sDate, :eDate,:advisoryNo,:pStart,:pLength)", nativeQuery = true)
	List<Object[]> viewAdvisoryPublishedPageWise(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("pStart") Integer pStart, @Param("pLength") Integer pLength);

	@Query(value = "select count(*) from  wrsis.sp_wr_advisory_publish_view(:sDate, :eDate,:advisoryNo,:pStart,:pLength)", nativeQuery = true)
	Integer viewAdvisoryPublishedPageWiseCount(@Param("sDate") String sDate, @Param("eDate") String eDate,
			@Param("advisoryNo") String advisoryNo, @Param("pStart") Integer pStart, @Param("pLength") Integer pLength);
	
	@Query("FROM AdvisoryEntiry where bitStatus = false and advisoryId=:advisoryId")
	AdvisoryEntiry findByAdvisoryId(@Param("advisoryId")int advisoryId);

}
