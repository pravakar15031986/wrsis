package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SampleSurveyDetailsEntity;

public interface SampleSurveyDetailsRepository extends JpaRepository<SampleSurveyDetailsEntity, Integer>{

	@Transactional
	@Modifying
	@Query("DELETE FROM SampleSurveyDetailsEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);

	@Query("FROM SampleSurveyDetailsEntity where bitstatus=false and sampleId=:sampleId")
	List<SampleSurveyDetailsEntity> findBySurveySampleId(@Param("sampleId")Integer sampleId);

	 

}
 