package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;

public interface SurveySiteRepository extends JpaRepository<SurveySiteEntity, Integer>{

	@Query("FROM SurveySiteEntity where surveyId=:surveyId")
	SurveySiteEntity findBySurveyIdId(@Param("surveyId")int surveyId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM SurveySiteEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId")int surveyId);

	List<SurveySiteEntity> findWheatTypeIdByWheatTypeId(int wheatTypeId);

	List<SurveySiteEntity> findOtherVarietyByOtherVariety(String varietyId);

	List<SurveySiteEntity> findGrowthIdByGrowthId(int stageId);

}
