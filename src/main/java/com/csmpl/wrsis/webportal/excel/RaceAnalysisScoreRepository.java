package com.csmpl.wrsis.webportal.excel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.RaceAnalysisScore;

public interface RaceAnalysisScoreRepository extends JpaRepository<RaceAnalysisScore, Integer> {

	@Query("FROM RaceAnalysisScore where bitstatus = false and inoculationId=?1 order by createdOn")
	List<RaceAnalysisScore> findByInoculationId(Integer inoculationId);

	@Transactional
	@Modifying
	@Query("DELETE from RaceAnalysisScore where inoculationId=?1")
	void removeByIoculationId(Integer inoculationId);

}
