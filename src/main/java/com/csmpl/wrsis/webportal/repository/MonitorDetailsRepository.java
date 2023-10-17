package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;

public interface MonitorDetailsRepository extends JpaRepository<MonitorDetailsEntity, Integer> {

	@Query(value = "select * from wrsis.sp_wr_view_monitor_implementation(?1,?2,?3,?4,?5)", nativeQuery = true)
	List<Object[]> viewSurveyImplementation(Integer regionId, Integer zoneId, String monitorNo, String createdDate,
			Integer userId);

	@Query("Select max(monitorid) from MonitorDetailsEntity ")
	String getMaxSlNo();

	@Query(value = "select * from wrsis.sp_wr_view_details_monitor_implementation(?1)", nativeQuery = true)
	List<Object[]> viewDetailsImplementation(Integer monitorId);

	@Query(nativeQuery = true, value = " select distinct (dgrpy3.int_demography_id) as region_id,(dgrpy3.vch_demography_name) as region_name                                               \r\n"
			+ "from wrsis.t_wr_user_location_tag ltag                                               \r\n"
			+ "inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id                                               \r\n"
			+ "inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id                                               \r\n"
			+ "inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id                                               \r\n"
			+ "inner join wrsis.m_wr_demography dgrpy2 on dgrpy1.int_parent_id=dgrpy2.int_demography_id                                               \r\n"
			+ "inner join wrsis.m_wr_demography dgrpy3 on dgrpy2.int_parent_id=dgrpy3.int_demography_id                                               \r\n"
			+ "where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false and dgrpy1.bitstatus=false and                                               \r\n"
			+ "dgrpy2.bitstatus=false and dgrpy3.bitstatus=false and ltag.int_user_id=:userId ")
	List<Object[]> fetchAllActiveDemographs(Integer userId);

	@Query(value = "select * from wrsis.wr_view_monitor_implementation_summary(?1,?2,?3,?4)", nativeQuery = true)
	String viewImplementationSummaryReport(Integer regionId, Integer year, Integer season, Integer recomendationId);

	@Query(value = "select * from wrsis.wr_view_monitor_implementation_report(?1,?2,?3,?4)", nativeQuery = true)
	String viewImplementationReport(Integer regionId, Integer season, Integer year, Integer recomendation);

	@Query(nativeQuery = true, value = "select distinct int_recom_id, vch_recom_no from wrsis.t_wr_monitor_details  where int_monitor_status =1 and \r\n"
			+ "extract(month FROM dtmcreatedon\\:\\:timestamp) in (select int_month_id from wrsis.m_wr_season_month where int_season_id = \r\n"
			+ "(select distinct int_season_id from wrsis.m_wr_season_month where bitstatus=false and  int_month_id = (SELECT extract(month FROM CURRENT_DATE\\:\\:timestamp)))) and \r\n"
			+ "extract(year FROM dtmcreatedon\\:\\:timestamp) =  extract(year FROM CURRENT_DATE\\:\\:timestamp)\r\n"
			+ "\r\n" + "")
	List<Object[]> fetchRecomendationNosCurrentSeasonYear();

	@Query(nativeQuery = true, value = "select distinct int_recom_id, vch_recom_no from wrsis.t_wr_monitor_details  where int_monitor_status = 1 and  \r\n"
			+ "extract(month FROM dtmcreatedon\\:\\:timestamp) in (select int_month_id from wrsis.m_wr_season_month where int_season_id = \r\n"
			+ "?2) and \r\n" + "extract(year FROM dtmcreatedon\\:\\:timestamp) =  ?1\r\n" + "\r\n" + "")
	List<Object[]> fetchRecomendationNosSeasonYear(int intYear, int intSeasonid);

	@Query(value = "select * from wrsis.sp_wr_view_monitor_implementation_pagination(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
	List<Object[]> viewImplementationDetailsData(Integer regionId, Integer zoneId, String monitorNo, String monitordate,
			Integer userId, Integer pstart, Integer plength);

	@Query(value = "select count(*) from wrsis.sp_wr_view_monitor_implementation_count(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
	Integer viewImplementationDetailsDataCount(Integer regionId, Integer zoneId, String monitorNo, String monitordate,
			Integer userId, Integer pstart, Integer plength);

	@Transactional
	@Modifying
	@Query("update MonitorDetailsEntity set bitStatus=true where monitorid = :monitorId")
	void deleteMonitorDetails(@Param("monitorId") int monitorId);

	@Query("FROM MonitorDetailsEntity where bitStatus = false and monitorid=:monitorId")
	MonitorDetailsEntity findByMonitorid(@Param("monitorId") Integer monitorId);

	List<MonitorDetailsEntity> findByRecomidAndWoredaidAndBitStatus(Integer recId, Integer woredaId, boolean b);

}
