package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.RaceAnalysisInoculation;

public interface RaceAnalysisInoculationRepository extends JpaRepository<RaceAnalysisInoculation, Integer> {

	@Query("FROM RaceAnalysisInoculation where bitstatus=false and raceAnalysisId=?1 ORDER by raceIncoulationId")
	List<RaceAnalysisInoculation> fetchByAnalysisId(Integer analysisId);

	@Query(nativeQuery = true, value = "SELECT dline.vch_dif_line,src.vch_seed_src,dline.vch_gene,dline.vch_expect_lowit,dline.int_diff_set_no,dline.int_wheat_dif_line_id FROM wrsis.m_wr_wheat_differ_line dline \r\n"
			+ "left join wrsis.m_wr_seed_source src on src.int_seed_src_id=dline.int_seed_src_id where dline.int_wheat_dif_line_id=?1 ")
	List<Object[]> fetchWheatLineDetails(Integer wheatLineId);

	@Query("select COUNT(c) FROM RaceAnalysisInoculation c where c.bitstatus=false and c.raceAnalysisId=?1")
	Integer findByAnalysisId(Integer valueOf);

	@Query(nativeQuery = true, value = "SELECT dline.vch_dif_line,src.vch_seed_src,dline.vch_gene,dline.vch_expect_lowit,dline.int_diff_set_no,dline.int_wheat_dif_line_id FROM wrsis.m_wr_wheat_differ_line dline \r\n"
			+ "left join wrsis.m_wr_seed_source src on src.int_seed_src_id=dline.int_seed_src_id where dline.int_wheat_dif_line_id=?1")
	List<Object[]> fetchWheatLineDetailsYellow(Integer wheatLineId);

	@Query(nativeQuery = true, value = "select \r\n" + "case when  " + "(select  " + "count(*)  "
			+ "from wrsis.m_wr_yellow_race_phenotype where vch_virulence_phenotype=?1) != 0 THEN " + "(select  "
			+ "vch_race_name " + "from wrsis.m_wr_yellow_race_phenotype where vch_virulence_phenotype=?1) " + "else "
			+ "'Other' " + "END ")
	String getRaceName(String result);

}
