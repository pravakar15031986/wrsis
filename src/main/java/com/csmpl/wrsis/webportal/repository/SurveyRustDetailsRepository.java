package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveyRustDetailsEntity;

public interface SurveyRustDetailsRepository extends JpaRepository<SurveyRustDetailsEntity, Integer>{

	@Query("FROM SurveyRustDetailsEntity where surveyId=:surveyId")
	List<SurveyRustDetailsEntity> findBySurveyId(@Param("surveyId") int surveyId);

	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyRustDetailsEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);

	List<SurveyRustDetailsEntity> findRustTypeIdByRustTypeId(int rustId);

}
