package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ResearchCenter;

@Repository
public interface ResearchCenterRepository extends JpaRepository<ResearchCenter, Integer> {
	@Query("select MAX(researchCenterId) from ResearchCenter")
	Integer findMaxResearchCenterId();

	@Query(value = "select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,r.vch_desc,r.bitstatus,STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId,STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId from  wrsis.m_wr_research_center r Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id where loc.bitstatus='false' group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> viewAllResearchCenterPage(Pageable pageable);

	@Query(value = "select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ "r.vch_desc,r.bitstatus,\r\n"
			+ "STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ ",STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId,\r\n"
			+ "STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ "STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ "STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ "from  wrsis.m_wr_research_center r\r\n"
			+ "Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ "where r.bitstatus=:status  and loc.bitstatus='false' group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> findBystatus(boolean status, Pageable pageable);

	Page<ResearchCenter> findByResearchCenterId(int researchCenterId, Pageable pageable);

	@Query(value = "select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,"
			+ "r.vch_desc,r.bitstatus,\r\n"
			+ "STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ ",STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId,\r\n"
			+ "STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ "STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ "STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ "from  wrsis.m_wr_research_center r\r\n"
			+ "Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ "where r.vch_research_center_name=:researchCenterName  group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> findByResearchCenterNameIgnoreCaseContaining(String researchCenterName, Pageable pageable);

	Page<ResearchCenter> findByResearchCenterNameIgnoreCaseContainingAndStatus(String researchCenterName,
			boolean status, Pageable pageable);

	@Query(value = "SELECT  rc.int_research_center_id,rc.vch_research_center_name,rc.int_is_lab,rc.bitstatus,dg.int_demography_id,dg.vch_demography_name,rcl.int_research_center_loc_id,rcl.int_region_id,rcl.int_zone_id,rcl.int_woreda_id from wrsis.m_wr_research_center rc INNER JOIN wrsis.m_wr_research_center_location rcl ON rc.int_research_center_id =rcl.int_research_center_id INNER JOIN wrsis.m_wr_demography dg ON rcl.int_region_id=dg.int_demography_id where dg.int_demography_id =:regionId", nativeQuery = true)
	Page<Object[]> searchByRegionId(Integer regionId, Pageable pageable);

	@Query(nativeQuery = true, value = " SELECT rescen.int_research_center_id,rescen.vch_research_center_name FROM wrsis.m_wr_user_rc_mapping rcmapp inner join wrsis.m_wr_research_center rescen on rcmapp.int_research_center_id =rescen.int_research_center_id "
			+ " where rcmapp.int_user_id =:userId ")
	List<Object[]> fetchResearchCenterByUserId(int userId);

	@Query(value = " select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ " r.vch_desc,r.bitstatus,\r\n"
			+ " STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ " ,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId, \r\n"
			+ " STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ " STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ " STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ " from  wrsis.m_wr_research_center r \r\n"
			+ " Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ " where loc.int_region_id=?1 and loc.bitstatus='false'  group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> searchByRegionIdInResearchCenter(Integer regionId, Pageable pageable);

	@Query(value = " select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ " r.vch_desc,r.bitstatus,\r\n"
			+ " STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ " ,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId, \r\n"
			+ " STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ " STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ " STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ " from  wrsis.m_wr_research_center r \r\n"
			+ " Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ " where loc.int_zone_id =?1 and loc.bitstatus='false' group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id ", nativeQuery = true)
	Page<Object[]> searchByZoneIdInResearchCenter(Integer zoneId, Pageable pageable);

	@Query(value = " select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ " r.vch_desc,r.bitstatus,\r\n"
			+ " STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ " ,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId, \r\n"
			+ " STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ " STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ " STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ " from  wrsis.m_wr_research_center r \r\n"
			+ " Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ " where r.vch_research_center_name =?1 AND r.bitstatus=?2 group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc ", nativeQuery = true)
	Page<Object[]> searchByNameAndStatus(String name, Boolean status, Pageable pageable);

	@Query(nativeQuery = true, value = "select rc.int_research_center_id,rc.vch_research_center_name from wrsis.m_wr_research_center rc \r\n"
			+ "inner join wrsis.m_wr_research_center_spelization rcs on rc.int_research_center_id=rcs.int_research_center_id \r\n"
			+ "where rcs.int_spelization_id=?1 and rc.bitstatus=false and rcs.bitstatus=false")
	List<Object[]> retriveResearchCenterListByRustType(Integer rustTypeId);

	@Query(value = "select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ "	r.vch_desc,r.bitstatus,\r\n"
			+ "	STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ "	,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId,\r\n"
			+ "	STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId,\r\n"
			+ "	STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ "	STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId\r\n"
			+ "	from  wrsis.m_wr_research_center r\r\n"
			+ "	Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ "	where r.bitstatus=?1 and loc.int_zone_id =?2 and loc.bitstatus='false' \r\n"
			+ " group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> searchByStatusNadZoneIdInResearchCenter(Boolean status, Integer zoneId, Pageable pageable);

	@Query(value = " select r.int_research_center_id, r.vch_research_center_name,r.int_is_lab,\r\n"
			+ "	r.vch_desc,r.bitstatus,\r\n"
			+ "	STRING_AGG(CAST(loc.int_research_center_id As VARCHAR),',') as RecearchId\r\n"
			+ "	,STRING_AGG(CAST(loc.int_research_center_loc_id AS VARCHAR),',') as LocationId,\r\n"
			+ "	STRING_AGG(CAST(loc.int_region_id AS VARCHAR),',') as RegionId, \r\n"
			+ "	STRING_AGG(CAST(loc.int_zone_id AS VARCHAR),',') as ZoneId,\r\n"
			+ "	STRING_AGG(CAST(loc.int_woreda_id AS VARCHAR),',') as WoredaId \r\n"
			+ "	from  wrsis.m_wr_research_center r \r\n"
			+ "	Inner join wrsis.m_wr_research_center_location loc on r.int_research_center_id=loc.int_research_center_id\r\n"
			+ "	where r.bitstatus=?1  and loc.int_region_id=?2 and loc.bitstatus='false'  group by r.int_research_center_id,r.vch_research_center_name,r.int_is_lab,r.vch_desc order by r.int_research_center_id", nativeQuery = true)
	Page<Object[]> searchByStatusAndRegionIdInResearchCenter(Boolean status, Integer regionId, Pageable pageable);

	@Query("from ResearchCenter where researchCenterName=:name")
	List<ResearchCenter> viewAllRCName(@Param("name") String name);

	List<ResearchCenter> findByLabStatusAndStatus(boolean b, boolean c);

	List<ResearchCenter> findByStatusOrderByResearchCenterName(boolean b);

	@Query("from ResearchCenter where status=false order by researchCenterName")
	List<ResearchCenter> findActiveResearchCenter();
	
	@Query("select researchCenterId,researchCenterName from ResearchCenter where status=false order by researchCenterName")
	List<Object[]> findActiveResearchCenters();
}
