package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.entity.RaceScoringMaster;

public interface RaceScoringMasterService {

	String addAndUpdateRaceScroing(RaceScoringMaster resc);

	List<RaceScoringMaster> viewAllScor();

	RaceScoringMaster getRaceScorById(Integer raceScoreId);

	boolean infectionTypeExits(String infectionType, Integer raceScoreId, Integer rustType);

	List<RaceScoringMaster> searchViewAllScor(Integer rustTypeId, String hlId, String status);

}
