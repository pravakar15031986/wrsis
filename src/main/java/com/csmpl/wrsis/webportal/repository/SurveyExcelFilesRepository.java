package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.SurveyExcelFiles;

public interface SurveyExcelFilesRepository extends JpaRepository<SurveyExcelFiles, Integer>{

	@Query("FROM SurveyExcelFiles where status=false and createdBy=?1 ORDER BY fileSurveyId DESC")
	List<SurveyExcelFiles> fetchAllActiveExcelFiles(Integer userId);
	
	@Query("FROM SurveyExcelFiles where status=false and createdBy=:userId and createdOn between :startDate and :endDate  ORDER BY fileSurveyId DESC")
	List<SurveyExcelFiles> fetchAllActiveExcelFilesSearch(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("userId")Integer userId);

	@Query(value="select * from wrsis.sp_wr_view_survey_file(?1,?2,?3,?4,?5)",nativeQuery=true)
	List<Object[]> viewAllActiveExcelFile(Integer userId,String startDate,String endDate,Integer start,Integer length);
	
	@Query(value="select count(*) from wrsis.sp_wr_view_survey_file(?1,?2,?3,?4,?5)",nativeQuery=true)
	Integer viewAllActiveExcelFileCount(Integer userId,String startDate,String endDate,Integer start,Integer length);
	
}
	 	