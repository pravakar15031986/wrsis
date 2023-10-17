package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.SurveyDetails;

@Repository("subReportRepository")
public interface SubReportRepository extends JpaRepository<SurveyDetails, Integer> {

	@Query(value = "select * from wrsis.sp_wr_rust_deshboard_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16);", nativeQuery = true)
	List<Object[]> getRustReportList(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			String startDate, String endDate, Integer seasonId, Integer researchCenterId, Integer rustTypeId,
			String surveyNo, Integer dataCollectModeId, Integer fileId, Integer userId, Integer year, Integer start, Integer length);

	@Query(value = "select * from wrsis.sp_wr_race_deshboard_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12);", nativeQuery = true)
	List<Object[]> getRaceReportList(String surv, String sampl, String strtD, String endD, int resonId, int rustTyp,
			int resecntr, int lab, String raceFromDate, String raceToDate, Integer start, Integer length);

	@Query(value = "select * from wrsis.sp_wr_dashboard_monitor_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11);", nativeQuery = true)
	List<Object[]> getMonitorData(int regionId, int zoneId, String monitorno, String recomno, Integer userId,
			String fromDate, String toDate, Integer year, Integer seasonid, Integer start, Integer length);

	@Query(value = "select * from wrsis.sp_wr_rust_incidend_deshboard_report(?1,?2,?3,?4,?5,?6);", nativeQuery = true)
	List<Object[]> getIncidendReportList(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			Integer yearId, Integer seasonTypeId);

	@Query(value = "select * from wrsis.sp_wr_rust_incidend_deshboard_report_dtls(?1,?2,?3,?4,?5,?6);", nativeQuery = true)
	List<Object[]> getIncidendReportListDetails(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			Integer yearId, Integer seasonTypeId);

	@Query(value = "select count(*) from wrsis.sp_wr_race_deshboard_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12);", nativeQuery = true)
	Integer getRaceReportListCount(String surveyId, String sampleId, String surveyDateFrom, String surveyDateTo,
			Integer regionId, Integer rustId, Integer rcId, Integer labId, String recDtFrom, String recDtTo, int start,
			int length);
	@Query(value = "select count(*) from wrsis.sp_wr_rust_deshboard_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16);", nativeQuery = true)
	Integer getRustReportListCount(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId, String sDate,
			String eDate, Integer seasonId, Integer rcId, Integer rustId, String upperCase, Integer dataCollectModeId,
			int parseInt, int parseInt2, Integer year, int start, int length);

	@Query(value = "select count(*) from wrsis.sp_wr_dashboard_monitor_report(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11);", nativeQuery = true)
	Integer getMonitorDataCount(Integer regionId, Integer zoneId, String monitorNo, String recommendationId,
			Integer userId, String monitorFromDate, String monitorToDate, Integer year, Integer seasonId, int start,
			int length);

}
