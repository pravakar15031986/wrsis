package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.SeasonMonth;
@Repository
public interface SeasonMonthRepository extends JpaRepository<SeasonMonth, Integer>{

	@Query(value="SELECT int_season_month_id, int_season_id, int_month_id, vch_month, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n" + 
			" FROM wrsis.m_wr_season_month where  int_season_id=?1 and bitstatus='false'",nativeQuery=true)
	List<SeasonMonth> findBySeasonId(Integer seasonId);
	
	@Query("From SeasonMonth s where monthId=:monthId And seasonId=:seasonId And status='false' order by s.seasonMonthId")
	List<SeasonMonth> findByMonthId(Integer seasonId,Integer monthId);
	
	@Query("From SeasonMonth s where monthId=:monthId  And status='false'")
	List<SeasonMonth> searchByMonthId(Integer monthId);
	
	@Query("From SeasonMonth s where monthId=:monthId  order by s.monthId")
	List<SeasonMonth> findByMonthId(Integer monthId);

	SeasonMonth findSeasonIdByMonthId(int month);
	
	@Query("select seasonId From SeasonMonth s where monthId=:month and status=false")
	int getSeasonIdByMonthId(int month);
}
