package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.YellowRacePhenotypeEntity;

@Repository
public interface YellowRacePhenotypeRepository extends JpaRepository<YellowRacePhenotypeEntity, Integer> {

	/*
	 * @Query(nativeQuery=true,value =
	 * "select yrp.int_race_phenotype_id,yrp.int_genetic_group_id,rgen.vch_group_name,yrp.vch_race_name,yrp.vch_virulence_phenotype,yrp.bitstatus\r\n"
	 * + "from wrsis.m_wr_yellow_race_phenotype yrp\r\n" +
	 * "inner join wrsis.m_wr_race_genetic_group rgen on yrp.int_genetic_group_id=rgen.int_genetic_group_id order by yrp.int_race_phenotype_id desc"
	 * )
	 */

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_yellow_race_phenotype(?1,?2,?3)")
	List<Object[]> getYelloeracePhenotype(int gengrpId, String race, String status);

	List<YellowRacePhenotypeEntity> findByRaceNameIgnoreCase(String raceName);

	@Query("select yrp.raceName from YellowRacePhenotypeEntity yrp where LOWER(yrp.raceName) LIKE LOWER(?1) and yrp.racePhenotypeId!=?2 ")
	List<YellowRacePhenotypeEntity> getRaceName(String raceName, int racePhenotypeId);

	List<YellowRacePhenotypeEntity> findByVirulencePhenotypeIgnoreCase(String virulencePhenotype);

	@Query("select yrp.virulencePhenotype from YellowRacePhenotypeEntity yrp where LOWER(yrp.virulencePhenotype) LIKE LOWER(?1) and yrp.racePhenotypeId!=?2 ")
	List<YellowRacePhenotypeEntity> getVPhenotype(String virulencePhenotype, Integer racePhenotypeId);

}
