package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.SeasonMaster;

@Repository
public interface SeasonMasterRepository extends JpaRepository<SeasonMaster, Integer> {

	@Query("select MAX(seasonId) from SeasonMaster")
	Integer findMaxSeasonId();

	SeasonMaster findBySeasonId(Integer seasonId);

	@Query("From SeasonMaster s order by s.seasonId")
	List<SeasonMaster> viewAllSeason();

	@Query("From SeasonMaster s order by s.seasonId")
	Page<SeasonMaster> viewAllSeasonPage(Pageable pageable);

	Page<SeasonMaster> findByStatus(boolean status, Pageable pageable);

	Page<SeasonMaster> findByseasonNameContainingIgnoreCaseAndStatus(String seasonName, boolean status,
			Pageable pageable);

	Page<SeasonMaster> findByseasonNameContainingIgnoreCase(String seasonName, Pageable pageable);

	@Query(value = "select sm.int_season_id as seasonId, sm.vch_season as seasonName,sm.vch_desc as Description,sm.bitstatus,"
			+ " STRING_AGG(CAST(q.int_season_month_id AS varchar),',') as seasonMonthId,STRING_AGG(CAST(q.int_month_id AS varchar),',') as monthId\r\n"
			+ " from wrsis.m_wr_season sm inner join wrsis.m_wr_season_month q on sm.int_season_id=q.int_season_id "
			+ " where q.int_month_id=?1 and sm.bitstatus='false' and q.bitstatus='false' group by sm.int_season_id,sm.vch_season,sm.vch_desc,sm.bitstatus", nativeQuery = true)
	Page<Object[]> searchBymonthId(Integer monthId, Pageable pageable);

	@Query("From SeasonMaster  s  where s.status='false' order by s.seasonName")
	List<SeasonMaster> viewAllSeasonByStatus();

	SeasonMaster findByseasonNameIgnoreCaseAndStatus(String sName, boolean status);
}
