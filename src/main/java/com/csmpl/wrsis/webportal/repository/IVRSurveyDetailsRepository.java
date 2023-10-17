package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.IVRSurveyDetails;

@Repository	
public interface IVRSurveyDetailsRepository extends JpaRepository<IVRSurveyDetails, Integer>{

	@Query(nativeQuery = true, value = " SELECT * FROM wrsis.t_wr_ivr_survey_details where bitstatus =false ORDER BY int_ivr_survey_id DESC LIMIT 1 ")
	IVRSurveyDetails findLatestRecord();

}
