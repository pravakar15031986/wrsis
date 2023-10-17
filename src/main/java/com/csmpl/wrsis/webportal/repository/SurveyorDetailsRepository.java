package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveyorDetailsEntity;

public interface SurveyorDetailsRepository extends JpaRepository<SurveyorDetailsEntity, Integer> {

	@Query("FROM SurveyorDetailsEntity where surveyId =:surveyId")
	List<SurveyorDetailsEntity> findBySurveyId(@Param("surveyId") int surveyId);

	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyorDetailsEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId") int surveyId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_prevalence_report(?1,?2,?3,?4)")
	String fetchPrevalenceReport(String string, int i, int j, int k);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_rust_incident_severity(?1,?2,?3,?4)")
	String fetchRustSeverityReport(String string, int int1, int i, int j);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_rust_reaction_report(?1,?2,?3)")
	String fetchReactionReport(String string, int int1, int i);

	@Query(nativeQuery = true, value = "select string_agg(surveyor.vch_surveyor_name, ',')  as surveyorname\r\n"
			+ "      FROM wrsis.t_wr_surveyor_details surveyor \r\n"
			+ "      WHERE surveyor.bitstatus=false and surveyor.int_survey_id=:surveyId")
	String getSurveyorName(@Param("surveyId") Integer surveyId);

}
