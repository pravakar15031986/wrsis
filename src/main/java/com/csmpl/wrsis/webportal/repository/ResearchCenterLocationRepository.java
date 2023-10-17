package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ResearchCenterLocation;

@Repository
public interface ResearchCenterLocationRepository extends JpaRepository<ResearchCenterLocation, Integer> {

	@Query(value = "SELECT * FROM wrsis.m_wr_research_center_location where int_research_center_id=:researchCenterId and bitstatus='false'", nativeQuery = true)
	List<ResearchCenterLocation> viewAllRCLocationByresearchCenterIdAndStatus(Integer researchCenterId);

	List<ResearchCenterLocation> findByRegionId(Integer regionId);

	@Query(value = "select * FROM wrsis.m_wr_research_center_location where int_research_center_id=:researchCenterId AND int_woreda_id=:woredaId AND bitstatus='false'", nativeQuery = true)
	ResearchCenterLocation getRCLocationByResearchCenterIdAndWoredaId(
			@Param("researchCenterId") Integer researchCenterId, @Param("woredaId") Integer woredaId);

	@Query(value = "select * FROM wrsis.m_wr_research_center_location where int_research_center_id=:researchCenterId AND bitstatus='false'", nativeQuery = true)
	List<ResearchCenterLocation> findByResearchCenterId(Integer researchCenterId);

	@Query(nativeQuery = true, value = "SELECT int_research_center_loc_id, int_research_center_id, int_region_id, int_zone_id, int_woreda_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon FROM wrsis.m_wr_research_center_location  where bitstatus=false")
	List<ResearchCenterLocation> getAllResearchCenterLocationByRegion();
}
