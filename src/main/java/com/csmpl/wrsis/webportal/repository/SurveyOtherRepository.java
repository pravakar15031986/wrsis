package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveyOtherEntity;

public interface SurveyOtherRepository extends JpaRepository<SurveyOtherEntity, Integer>{

	@Query("FROM SurveyOtherEntity where surveyId=:surveyId")
	SurveyOtherEntity findBySurveyIdOne(@Param("surveyId")int surveyId);

	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyOtherEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);

}
