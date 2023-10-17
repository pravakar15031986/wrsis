package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;

@Repository
public interface MonitorRecommendationRepository extends JpaRepository<MonitorDetailsEntity, Integer> {

	@Query(value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,rec.vch_recom_file_name from wrsis.t_wr_recommendation  rec where rec.bitstatus=false  and rec.int_recom_status=3 and rec.int_recom_id in (select distinct rec.int_recom_id from wrsis.t_wr_recommendation  rec inner join wrsis.t_wr_monitor_details mon on rec.int_recom_id = mon.int_recom_id where mon.int_monitor_status=0 and mon.bitstatus=false and rec.bitstatus=false)", nativeQuery = true)
	List<Object[]> viewAllRecommendationForMonitor();

	@Query(value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,rec.vch_recom_file_name from wrsis.t_wr_recommendation  rec \r\n"
			+ " where rec.bitstatus=false and rec.int_recom_status=3 \r\n"
			+ " AND   DATE_TRUNC('day', rec.dt_publish_on) >=?1  AND DATE_TRUNC('day',rec .dt_publish_on)<=?2\r\n"
			+ "  and rec.int_recom_id in\r\n"
			+ " (select distinct rec.int_recom_id from wrsis.t_wr_recommendation  rec \r\n"
			+ "  inner join wrsis.t_wr_monitor_details mon on rec.int_recom_id = mon.int_recom_id where mon.int_monitor_status=0 and mon.bitstatus=false and rec.bitstatus=false)", nativeQuery = true)
	List<Object[]> viewAllRecommendationForMonitorByDate(@Param("sDate") Date sDate, @Param("eDate") Date eDate);

	@Query(value = "select rec.int_recom_id,rec.vch_recom_no,rec.dt_publish_on,rec.vch_recom_file_name from wrsis.t_wr_recommendation  rec \r\n"
			+ " where rec.bitstatus=false and rec.int_recom_status=3 \r\n" + " AND  rec.vch_recom_no=?1\r\n"
			+ "  and rec.int_recom_id in\r\n"
			+ " (select distinct rec.int_recom_id from wrsis.t_wr_recommendation  rec \r\n"
			+ "  inner join wrsis.t_wr_monitor_details mon on rec.int_recom_id = mon.int_recom_id where mon.int_monitor_status=0 and mon.bitstatus=false and rec.bitstatus=false)", nativeQuery = true)
	List<Object[]> viewAllRecommendationForMonitorByRCNumber(@Param("rcNumber") String rcnumber);

	@Query(value = "select * from wrsis.sp_wr_monitor_action(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
	List<Object[]> viewAllMonitorByRecommendNo(Integer regionId, Integer zoneId, Integer woredaId, String monitorNo,
			String startDate, String endDate, String recommendNumber);

	@Query(value = "select * from wrsis.sp_wr_monitor_publish_discard(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	List<Object[]> viewAllMonitorByPublishedAndDiscard(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String startDate, String endDate, Integer status, Integer pstart, Integer plength);

	@Query(value = "select * from wrsis.sp_wr_monitor_published(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
	List<Object[]> viewAllMonitorByPublished(int regionId, int zoneId, int woredaId, String monitorNo, String startDate,
			String endDate, Integer status);

	@Query(value = "SELECT * FROM wrsis.sp_wr_monitor_publish_discard_by_user( ?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
	List<Object[]> viewAllPublishedDiscardedByUserId(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorRefNumber, String startDate, String endDate, Integer status, Integer userId, Integer pstart,
			Integer plength);

	@Query(value = "select * from wrsis.sp_wr_monitor_action_pagnation(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)", nativeQuery = true)
	List<Object[]> monitorRecommendationPublishedData(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String recDtFrom, String recDtTo, String rcNumber, Integer userId, Integer processId,
			Integer pstart, Integer plength);
//commented by Raman Shrestha
//	@Query(value = "select count(*) from wrsis.sp_wr_monitor_action_pagnation_count(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)", nativeQuery = true)
	@Query(value = "select count(*) from wrsis.sp_wr_monitor_action_pagnation(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11)", nativeQuery = true)
	Integer viewImplementationDetailsDataCount(Integer regionId, Integer zoneId, Integer woredaId, String monitorNo,
			String recDtFrom, String recDtTo, String rcNumber, Integer userId, Integer processId, Integer pstart,
			Integer plength);

	@Query(value = "select count(*) from wrsis.sp_wr_monitor_publish_discard_count(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	Integer viewMoniterPublishedAndDiscardSearchCount(Integer regionId, Integer zoneId, Integer woredaId,
			String monitorNo, String startDate, String endDate, Integer status, Integer pstart, Integer plength);

	@Query(value = "select * from wrsis.sp_wr_monitor_published_page(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	List<Object[]> viewAllMonitorByPublishedPageWise(int regionId, int zoneId, int woredaId, String monitorNo,
			String startDate, String endDate, Integer status, Integer pstart, Integer plength);

	@Query(value = "select count(*) from wrsis.sp_wr_monitor_published_page(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	Integer countAllMonitorByPublishedPageWise(int regionId, int zoneId, int woredaId, String monitorNo,
			String startDate, String endDate, Integer status, Integer pstart, Integer plength);

	@Query(value = "SELECT count(*) FROM wrsis.sp_wr_monitor_publish_discard_by_user_count( ?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
	Integer viewAllPublishedDiscardedByUserIdCount(Integer regionId, Integer zoneId, Integer woredaId, String monitorNo,
			String fromDate, String toDate, Integer status, Integer userId, Integer pstart, Integer plength);

	@Query(value = "select * from wrsis.sp_wr_new_monitor_implementation(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	List<Object[]> viewAllRecommendationForMonitorPage(String recommNo, Integer userId, String sDate, String eDate, Integer pStart,
			Integer pLength);

	@Query(value = "select count(*) from wrsis.sp_wr_new_monitor_implementation(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	Integer viewAllRecommendationForMonitorPageCount(String recommNo, Integer userId, String sDate, String eDate, Integer pStart,
			Integer pLength);
}
