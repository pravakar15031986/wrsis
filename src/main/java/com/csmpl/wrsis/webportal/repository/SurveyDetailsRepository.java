package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;

public interface SurveyDetailsRepository extends JpaRepository<SurveyDetails, Integer>{

	@Query("FROM SurveyDetails where bitstatus = false order by surveyId")
	List<SurveyDetails> findActiveSurveyDetailsAll();

	@Transactional
	@Modifying
	@Query("update SurveyDetails set bitstatus=true where surveyId = :surveyId")
	void deleteSurveyDetails(@Param("surveyId")int surveyId);
	
	@Query("FROM SurveyDetails where bitstatus = false and surveyId=:surveyId")
	SurveyDetails findBySurveyId(@Param("surveyId")int surveyId);

	@Query(value="select * from wrsis.sp_wr_survey_details(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:start,:length);",nativeQuery=true)
	List<Object[]> searchSurveyDetails(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);

	@Query(value="select count(*) from wrsis.sp_wr_survey_details(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:start,:length);",nativeQuery=true)
	Integer searchSurveyDetailsDataTableCount(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId
			,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length
			);

	@Query(value="select * from wrsis.sp_wr_survey_details(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:start,:length);",nativeQuery=true)
	List<Object[]> searchSurveyDetailsDataTable(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length
			);
	
	@Query(nativeQuery = true,value =" select sd.vch_latitude,sd.vch_longitude,rt.vch_rust_type,gs.vch_stage,srd.vch_severity,"
			+ "\r\n" + 
			"(select sev1.vch_severity_type from wrsis.m_wr_severity as sev1 where " + 
			"CAST (sev1.vch_severity_value AS NUMERIC)  >= CAST (srd.vch_severity AS NUMERIC)  ORDER by " + 
			"CAST (sev1.vch_severity_value AS NUMERIC) ASC  limit 1)  from wrsis.t_wr_survey_details sd " + 
			"inner join  wrsis.t_wr_survey_rust_details srd on sd.int_survey_id = srd.int_survey_id " + 
			"inner join  wrsis.m_wr_rust_type rt on srd.int_rust_type_id = rt.int_rust_type_id " + 
			"inner join  wrsis.t_wr_survey_site_details ssd on sd.int_survey_id = ssd.int_survey_id " + 
			"inner join  wrsis.m_wr_cr_growth_stage gs on ssd.int_cr_gr_stage_id = gs.int_cr_gr_stage_id " + 
			"where sd.int_survey_status =1 and sd.bitstatus = false and gs.int_cr_gr_stage_id =:stageId " ) 
	List<Object[]> fetchAllsurDataforGISByStage(@Param("stageId")Integer stageId);
    
	@Query(nativeQuery = true,value =" select sd.vch_latitude,sd.vch_longitude,rt.vch_rust_type,gs.vch_stage,srd.vch_severity,"
			+ " \r\n" + 
			"(select sev1.vch_severity_type from wrsis.m_wr_severity as sev1 where \r\n" + 
			"CAST (sev1.vch_severity_value AS NUMERIC)  >= CAST (srd.vch_severity AS NUMERIC)  ORDER by \r\n" + 
			"CAST (sev1.vch_severity_value AS NUMERIC) ASC  limit 1)  from wrsis.t_wr_survey_details sd " + 
			"inner join  wrsis.t_wr_survey_rust_details srd on sd.int_survey_id = srd.int_survey_id " + 
			"inner join  wrsis.m_wr_rust_type rt on srd.int_rust_type_id = rt.int_rust_type_id " + 
			"inner join  wrsis.t_wr_survey_site_details ssd on sd.int_survey_id = ssd.int_survey_id " + 
			"inner join  wrsis.m_wr_cr_growth_stage gs on ssd.int_cr_gr_stage_id = gs.int_cr_gr_stage_id " + 
			"where sd.int_survey_status =1 and sd.bitstatus = false and srd.int_rust_type_id=:disease " )  
	List<Object[]> fetchAllsurDataforGISByDisease(@Param("disease")Integer disease);

	@Query(nativeQuery = true,value =" select sd.vch_latitude,sd.vch_longitude,rt.vch_rust_type,gs.vch_stage,srd.vch_severity,\r\n" + 
			"(select sev1.vch_severity_type from wrsis.m_wr_severity as sev1 where \r\n" + 
			"CAST (sev1.vch_severity_value AS NUMERIC)  >= CAST (srd.vch_severity AS NUMERIC)  ORDER by \r\n" + 
			"CAST (sev1.vch_severity_value AS NUMERIC) ASC  limit 1)  from wrsis.t_wr_survey_details sd " + 
			"inner join  wrsis.t_wr_survey_rust_details srd on sd.int_survey_id = srd.int_survey_id " + 
			"inner join  wrsis.m_wr_rust_type rt on srd.int_rust_type_id = rt.int_rust_type_id " + 
			"inner join  wrsis.t_wr_survey_site_details ssd on sd.int_survey_id = ssd.int_survey_id " + 
			"inner join  wrsis.m_wr_cr_growth_stage gs on ssd.int_cr_gr_stage_id = gs.int_cr_gr_stage_id " + 
			"where sd.int_survey_status =1 and sd.bitstatus = false and extract(year from sd.dt_survey_date)=:yearPara " )
	List<Object[]> fetchAllsurDataforGISByYear(@Param("yearPara")Double yearPara);
	
	@Query(value="select * from wrsis.sp_wr_view_suvey_mobile(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId);",nativeQuery=true)
	List<Object[]> searchSurveyDataforMobile(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,
			@Param("userId")Integer userId);
	                            
	@Query(value="select * from wrsis.sp_wr_survey_pending_published_discard_details(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId);",nativeQuery=true)
	List<Object[]> searchSurveyPendingPublishedDiscardDetails(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId);

	
	@Query(value="select * from  wrsis.sp_wr_survey_publish(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId);",nativeQuery=true)
	List<Object[]> searchSurveyPendingPublishedDiscardDetailsByResearchCenterTag(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId);

	@Query(nativeQuery=true,value="select distinct int_season_id from wrsis.m_wr_season_month where bitstatus=false and int_month_id = (SELECT extract(month FROM CURRENT_DATE\\:\\:timestamp))")
	Integer getCurrentSeasonId();
	
	@Query(value="select * from  wrsis.sp_wr_survey_published_discard_details_byuser(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:status);",nativeQuery=true)
	List<Object[]> searchSurveyPendingPublishedDiscardDetailsByUser(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,@Param("status")Integer status);

	@Query(value="select * from wrsis.sp_wr_search_survey_gis_map(?1,?2,?3,?4,?5)",nativeQuery=true)
	List<Object[]> getSurveyDataforGIS(String year,Integer rustId,Integer growthId,String severity,Integer seasonId);
	
	
	@Query(value="select * from wrsis.sp_wr_global_rust(:regionId,:startDate,:endDate,:surveyNo,:status,:seasonId,:researchCenterId,:userId,:year,:start,:length)",nativeQuery=true)
	List<Object[]> getGlobalRustReport(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("status")Integer status,@Param("seasonId")Integer seasonId,@Param("researchCenterId")Integer researchCenterId, @Param("userId")Integer userId,@Param("year")String year,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);

	@Query(nativeQuery=true,value=" select season.int_season_id,season.vch_season\r\n" + 
			"            from wrsis.m_wr_season season  \r\n" + 
			"            where  season.int_season_id =(select distinct int_season_id from wrsis.m_wr_season_month where bitstatus=false \r\n" + 
			"            and  int_month_id = (SELECT extract(month FROM CURRENT_DATE\\:\\:timestamp))) \r\n" + 
			"			")
	List<Object[]> fetchCurrentYearWiseSeason();
	
	@Query(value="select count(*) from wrsis.sp_wr_global_rust(:regionId,:startDate,:endDate,:surveyNo,:status,:seasonId,:researchCenterId,:userId,:year,:start,:length)",nativeQuery=true)
	Integer countTotalGlobalRustReportTable(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("status")Integer status,@Param("seasonId")Integer seasonId,@Param("researchCenterId")Integer researchCenterId, @Param("userId")Integer userId,@Param("year")String year,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);
	 
	
	@Query(value="select * from wrsis.sp_wr_survey_pending_published_discard_details_bystatus(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:actionStatus,:start,:length);",nativeQuery=true)
	List<Object[]> searchSurveyPendingPublishedDiscardDetailsByActionStatus(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param("actionStatus")Integer actionStatus,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);
	
	@Query(value="select count(*) from wrsis.sp_wr_survey_pending_published_discard_details_bystatus(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:actionStatus,:start,:length);",nativeQuery=true)
	Integer searchSurveyPendingPublishedDiscardDetailsByActionStatusDataCount(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param("actionStatus")Integer actionStatus,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);
	
	@Query(value="select * from wrsis.sp_wr_rust_survey_other_disease(:regionId,:startDate,:endDate,:surveyNo,:status,:seasonId,:researchCenterId,:userId,:year,:start,:length)",nativeQuery=true)
	List<Object[]> getRustSurveyOtherDiseaseReport(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("status")Integer status,@Param("seasonId")Integer seasonId,@Param("researchCenterId")Integer researchCenterId, @Param("userId")Integer userId,@Param("year")String year,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);

	@Query(value="select count(*) from wrsis.sp_wr_rust_survey_other_disease(:regionId,:startDate,:endDate,:surveyNo,:status,:seasonId,:researchCenterId,:userId,:year,:start,:length)",nativeQuery=true)
	Integer countTotalRustSurveyOtherDiseaseReportTable(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("status")Integer status,@Param("seasonId")Integer seasonId,@Param("researchCenterId")Integer researchCenterId, @Param("userId")Integer userId,@Param("year")String year,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);

	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="update wrsis.t_wr_survey_details set geom_lat_long=st_SetSrid(st_MakePoint(cast(vch_longitude as float), cast(vch_latitude as float)), 4326) where  int_survey_id= ?1")
	void updateGeoLocation(Integer surveyId);
	
	
	@Query(value="select * from wrsis.sp_wr_global_rust_gis(?1,?2)",nativeQuery=true)
	List<Object[]> viewGisSurveyData(String fromDate, String toDate);

	@Query(value="select * from wrsis.sp_wr_survey_pending_published_discard_details_bystatus_user(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:actionStatus,:processId,:start,:length);",nativeQuery=true)
	List<Object[]> searchSurveyPendingPublishedDiscardDetailsByActionStatusByUser(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param("actionStatus")Integer actionStatus,@Param("processId")Integer processId,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);
	
	@Query(value="select count(*) from wrsis.sp_wr_survey_pending_published_discard_details_bystatus_user(:regionId,:zoneId,:woredaId,:kebeleId,:startDate,:endDate,:rustTypeId,:surveyNo,:fileId,:userId,:actionStatus,:processId,:start,:length);",nativeQuery=true)
	Integer searchSurveyPendingPublishedDiscardDetailsByActionStatusByUserDataCount(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId,@Param("kebeleId") Integer kebeleId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO) String surveyNo,@Param("fileId") Integer fileId,@Param("userId")Integer userId,
			@Param("actionStatus")Integer actionStatus,@Param("processId")Integer processId,@Param(WrsisPortalConstant.START) Integer start,@Param("length") Integer length);

	@Query(value="select * from  wrsis.sp_wr_survey_publish_discard_details_user(:regionId, :zoneId, :woredaId, :kebeleId,:startDate, :endDate, :rustTypeId, :surveyNo, :fileId,:userId, :actionStatus,:start,:length);",nativeQuery=true)
	List<Object[]> viewSurveyPendingPublishedDiscardDetailsByUser(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId, @Param("kebeleId")Integer kebeleId,
															      @Param("startDate")String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO)String surveyNo,@Param("fileId") Integer fileId,@Param("userId") Integer userId,@Param("actionStatus") Integer actionStatus,@Param(WrsisPortalConstant.START) Integer start,@Param("length")Integer length);

	@Query(value="select count(*) from  wrsis.sp_wr_survey_publish_discard_details_user(:regionId, :zoneId, :woredaId, :kebeleId,:startDate, :endDate, :rustTypeId, :surveyNo, :fileId,:userId, :actionStatus,:start,:length);",nativeQuery=true)
	Integer viewSurveyPendingPublishedDiscardDetailsByUserCount(@Param(WrsisPortalConstant.REGION_ID)Integer regionId,@Param("zoneId")Integer zoneId,@Param("woredaId") Integer woredaId, @Param("kebeleId")Integer kebeleId,
															      @Param("startDate")String startDate,@Param("endDate") String endDate,@Param("rustTypeId") Integer rustTypeId,@Param(WrsisPortalConstant.SURVEYNO)String surveyNo,@Param("fileId") Integer fileId,@Param("userId") Integer userId,@Param("actionStatus") Integer actionStatus,@Param(WrsisPortalConstant.START) Integer start,@Param("length")Integer length);

	List<SurveyDetails> findSeasonIdBySeasonId(int seasonIdBean);

	@Query(nativeQuery = true,value="select * FROM wrsis.t_wr_survey_details \r\n" + 
			" where vch_latitude=?2 and vch_longitude=?3 and int_season_id=?1")
	List<SurveyDetails> checkSeasonLatLongDuplicate(Integer seasonId, String latitude, String longitude);

	@Query(value="select * from wrsis.sp_wr_raw_global_rust_gis(?1,?2)",nativeQuery=true)
	List<Object[]> viewRawGisSurveyData(String fromDate, String toDate);

}
