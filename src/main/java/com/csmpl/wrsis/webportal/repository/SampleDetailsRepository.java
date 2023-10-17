package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SampleDetailsEntity;

public interface SampleDetailsRepository extends JpaRepository<SampleDetailsEntity, Integer>{

	@Query("FROM SampleDetailsEntity where surveyId =:surveyId")
	List<SampleDetailsEntity> findBySurveyId(@Param("surveyId") int surveyId);

 
	@Transactional
	@Modifying
	@Query("DELETE FROM SampleDetailsEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);


	List<SampleDetailsEntity> findSampleTypeIdBySampleTypeId(int sampleId);
}
