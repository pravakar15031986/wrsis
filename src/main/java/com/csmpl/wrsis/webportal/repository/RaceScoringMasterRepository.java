package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.RaceScoringMaster;

@Repository
public interface RaceScoringMasterRepository extends JpaRepository<RaceScoringMaster, Integer> {

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where int_race_score_id=?1", nativeQuery = true)
	RaceScoringMaster findByraceScoreId(Integer raceScoreId);

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where int_rust_type_id=?1 and vch_infection_type_value=?2", nativeQuery = true)
	RaceScoringMaster validateInfectionType(Integer rustType, String infectionType);

	@Query("from RaceScoringMaster t order by t.raceScoreId")
	List<RaceScoringMaster> viewAllRaceScor();

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where int_rust_type_id=?1 order by int_race_score_id", nativeQuery = true)
	List<RaceScoringMaster> searchRaceScorByRustId(Integer rustId);

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where vch_l_h_value=?1 order by int_race_score_id", nativeQuery = true)
	List<RaceScoringMaster> searchRaceScorByHLLevel(String hlId);

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where bitstatus=?1 order by int_race_score_id", nativeQuery = true)
	List<RaceScoringMaster> searchRaceScorByStatus(Boolean status);

	@Query(value = "SELECT int_race_score_id, int_rust_type_id, vch_infection_type_value, vch_l_h_value, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.m_wr_race_score_master where int_rust_type_id=?1 and vch_l_h_value=?2 and bitstatus=?3 order by int_race_score_id", nativeQuery = true)
	List<RaceScoringMaster> searchRaceScorByRustIdHLLevelStatus(Integer rustId, String hlId, Boolean status);

	@Query("FROM RaceScoringMaster where bitstatus = false and rustId.rustId = ?1")
	List<RaceScoringMaster> findByRustId(Integer rustTypeId);
}
