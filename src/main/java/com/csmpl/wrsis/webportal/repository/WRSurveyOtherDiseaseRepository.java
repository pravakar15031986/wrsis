package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.WRSurveyOtherDiseaseEntity;

public interface WRSurveyOtherDiseaseRepository extends JpaRepository<WRSurveyOtherDiseaseEntity, Integer>{

	@Query("FROM WRSurveyOtherDiseaseEntity where surveyId=:surveyId")
	List<WRSurveyOtherDiseaseEntity> findBySurveyId(@Param("surveyId")int surveyId);

 
	@Transactional
	@Modifying
	@Query("DELETE FROM WRSurveyOtherDiseaseEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);


	List<WRSurveyOtherDiseaseEntity> findDiseaseIdByDiseaseId(int diseaseId);
}
