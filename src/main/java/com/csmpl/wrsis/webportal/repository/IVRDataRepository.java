package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.IVRDataEntity;

@Repository
public interface IVRDataRepository extends JpaRepository<IVRDataEntity, Integer> {

	@Query(value = "select * from wrsis.t_wr_ivr_data where int_import_ivr_id=:ivrFileId", nativeQuery = true)
	List<IVRDataEntity> findByIvrFileId(Integer ivrFileId);

	@Query(value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "where d.int_import_ivr_id=?1 group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id", nativeQuery = true)
	Page<Object[]> viewIVRQustionDetailsByPage(Pageable pageable, Integer ivrFileId);

	@Query(value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "where d.int_import_ivr_id=?1 and d.vch_mobileno=?2 group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id", nativeQuery = true)
	Page<Object[]> searchIVRQustionDetailsByMobileNumberPage(Pageable pageable, Integer ivrFileId, String phone);

	

	@Query(nativeQuery = true, value = "select count(*) from wrsis.t_wr_ivr_data")
	Integer countIvrSurveyData();

	
	@Query(value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "where d.int_import_ivr_id=?1 and d.int_region_id=?2 group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id", nativeQuery = true)
	Page<Object[]> searchIVRQustionDetailsByRegionIdPage(Pageable pageable, Integer ivrFileId, Integer regionId);

	@Query(value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "where d.int_import_ivr_id=?1 and d.int_region_id=?2 and d.int_zone_id=?3 group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id", nativeQuery = true)
	Page<Object[]> searchIVRQustionDetailsByZoneIdPage(Pageable pageable, Integer ivrFileId, Integer regionId,
			Integer zoneId);

	@Query(value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "where d.int_import_ivr_id=?1 and d.int_region_id=?2 and d.int_zone_id=?3 and d.int_woreda_id=?4 group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id", nativeQuery = true)
	Page<Object[]> searchIVRQustionDetailsByWoredaIdPage(Pageable pageable, Integer ivrFileId, Integer regionId,
			Integer zoneId, Integer woredaId);

	@Query(nativeQuery = true, value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id\r\n"
			+ "group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id")
	List<Object[]> viewAllIVRReportDataReport();

	@Query(nativeQuery = true, value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data\r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id where d.int_region_id=?\r\n"
			+ "group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id")
	List<Object[]> viewAllIVRReportByRegionData(Integer region);

	@Query(nativeQuery = true, value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data \r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id where d.int_zone_id=?1\r\n"
			+ "group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id")
	List<Object[]> viewAllIVRReportByZoneData(Integer zone);

	@Query(nativeQuery = true, value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data \r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id where d.int_woreda_id=?1\r\n"
			+ "group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id")
	List<Object[]> viewAllIVRReportByWoredaData(Integer woreda);

	@Query(nativeQuery = true, value = "select d.vch_mobileno as mobile,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id,\r\n"
			+ "STRING_AGG(CAST(q.int_question_id AS varchar),',') as QustionId,STRING_AGG(CAST(q.int_ivr_ques_id AS varchar),',') from wrsis.t_wr_ivr_data \r\n"
			+ "d inner join wrsis.t_wr_ivr_data_ques q on d.int_ivr_data_id=q.int_ivr_data_id where d.vch_mobileno=?1\r\n"
			+ "group by d.vch_mobileno,d.dtm_call_data_time,d.int_region_id,d.int_zone_id,d.int_woreda_id")
	List<Object[]> viewAllIVRReportByPhoneData(String mobile);

	@Query(value = "select * from wrsis.sp_wr_ivr_details(?1,?2,?3,?4,?5);", nativeQuery = true)
	List<Object[]> viewIVRQustionDetailsByPageViewDetails(String mobile, Integer region, Integer zone, Integer woreda,
			Integer fileId);

	

	@Query(value = "select * from wrsis.sp_wr_view_all_ivr_details(?1,?2,?3,?4,?5,?6,?7,?8);", nativeQuery = true)
	List<Object[]> viewAllIVRDetailsByPage(String mobile, Integer region, Integer zone, Integer woreda, String sDate,
			String eDate, Integer start, Integer length);

	@Query(value = "select * from wrsis.sp_wr_view_ivr_page(?1,?2,?3,?4);", nativeQuery = true)
	List<Object[]> viewIvrDataPage(String startDate, String endDate, Integer pstart, Integer pLength);
	
	@Query(value = "select count(*) from wrsis.sp_wr_view_ivr_page(?1,?2,?3,?4);", nativeQuery = true)
	Integer countIvrDataPage(String startDate, String endDate, int pstart, int pLength);
	
	@Query(value = "select * from wrsis.sp_wr_view_api_ivr_details(?1,?2,?3,?4,?5,?6,?7,?8);", nativeQuery = true)
	List<Object[]> ivrSurveyAPIData(String mobile, Integer region, Integer zone, Integer woreda, String sDate,
			String eDate, Integer start, Integer length);

}
