package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveyCapturedImageEntity;

public interface SurveyCapturedImageRepository extends JpaRepository<SurveyCapturedImageEntity, Integer>{

	@Query("FROM SurveyCapturedImageEntity where surveyId=:surveyId")
	List<SurveyCapturedImageEntity> findBySurveyId(@Param("surveyId") int surveyId);

	@Query("FROM SurveyCapturedImageEntity where imageName=:imageName")
	List<SurveyCapturedImageEntity> checkImageName(@Param("imageName")String name);

	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyCapturedImageEntity where surveyId=:surveyId")
	void deleteBySurveyId(Integer surveyId);

	 

 

}
