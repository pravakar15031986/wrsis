package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveyOtherDiseaseEntity;

public interface SurveyOtherDiseaseRepository extends JpaRepository<SurveyOtherDiseaseEntity, Integer>{

	
	@Query("FROM SurveyOtherDiseaseEntity where surveyId =:surveyId")
	List<SurveyOtherDiseaseEntity> findBySurveyId(@Param("surveyId") int surveyId);

	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyOtherDiseaseEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);

}
