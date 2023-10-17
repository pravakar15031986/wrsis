package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ResearchCenterSpelization;

@Repository
public interface ResearchCenterSpelizationRepository extends JpaRepository<ResearchCenterSpelization, Integer> {

	@Query(value = "SELECT * FROM wrsis.m_wr_research_center_spelization where int_research_center_id=:researchCenterId and bitstatus='false'", nativeQuery = true)
	List<ResearchCenterSpelization> viewAllRCSpelizationByresearchCenterIdAndStatus(Integer researchCenterId);

	@Query(value = "SELECT * FROM wrsis.m_wr_research_center_spelization  where int_research_center_id=:researchCenterId and int_spelization_id=:spelizationId And bitstatus='false'", nativeQuery = true)
	ResearchCenterSpelization getAllRCSpelizationByresearchCenterIdAndStatus(
			@Param("researchCenterId") Integer researchCenterId, @Param("spelizationId") Integer spelizationId);

	@Query(value = "select reacenter.int_research_center_id,reacenter.vch_research_center_name \r\n"
			+ "from wrsis.m_wr_research_center_spelization  resspl \r\n"
			+ "inner join wrsis.m_wr_research_center reacenter on resspl.int_research_center_id = reacenter.int_research_center_id\r\n"
			+ // resspl.int_spelization_id,
			"where  resspl.bitstatus=false and reacenter.bitstatus=false and resspl.int_spelization_id=:rustId ", nativeQuery = true)
	List<Object[]> findResearchCenteByRustId(@Param("rustId") int rustId);
}
