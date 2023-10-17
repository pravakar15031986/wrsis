package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.FungicideDetailsEntity;

public interface FungicideDetailsRepository extends JpaRepository<FungicideDetailsEntity, Integer> {

	@Query("FROM FungicideDetailsEntity where surveyId =:surveyId")
	FungicideDetailsEntity findBySurveyId(@Param("surveyId") int surveyId);

	@Transactional
	@Modifying
	@Query("DELETE FROM FungicideDetailsEntity where surveyId=:surveyId")
	void deleteBySurveyId(@Param("surveyId") int surveyId);

	@Query(nativeQuery = true, value = "select fun.vch_fungicide,fun.vch_quantity as fungicide from wrsis.m_wr_fungicide_type fun where fun.bitstatus=false")
	List<Object[]> getFungicideDetails();

	List<FungicideDetailsEntity> findOtherFungiByOtherFungi(String fungicideId);

}
