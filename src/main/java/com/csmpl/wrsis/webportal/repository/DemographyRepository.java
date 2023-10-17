package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;

@Repository
public interface DemographyRepository extends JpaRepository<DemographicEntity, Integer> {

	@Query(value = "select demo.int_demography_id,demo.vch_demography_name from "
			+ "wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_demography_id in (select loc.int_region_id from  wrsis.m_wr_research_center_location loc where  loc.bitstatus=false and loc.int_research_center_id=:id)", nativeQuery = true)
	List<Object[]> fetchAllActiveDemographs(@Param("id") Integer id);

	@Query(value = "select demo.int_demography_id,demo.vch_demography_name from "
			+ "wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_demography_id in (select loc.int_zone_id from  wrsis.m_wr_research_center_location loc where loc.bitstatus=false and loc.int_region_id=:id) order by demo.vch_demography_name", nativeQuery = true)
	List<Object[]> fetchAllActiveDemographsZone(@Param("id") int intRustTypeId);

	@Query(value = "select demo.int_demography_id,demo.vch_demography_name from "
			+ "wrsis.m_wr_demography demo where (demo.int_level_id=4 and demo.int_parent_id=0) or  demo.bitstatus = false and demo.int_demography_id in (select loc.int_woreda_id from  wrsis.m_wr_research_center_location loc where loc.bitstatus=false and loc.int_region_id=:regionId and loc.int_zone_id=:intZoneId) order by demo.vch_demography_name", nativeQuery = true)
	List<Object[]> fetchAllActiveDemographsWoreda(@Param(WrsisPortalConstant.REGION_ID) int regionId, @Param("intZoneId") int intZoneId);

	@Query("FROM DemographicEntity where bitstatus=false and (parentId=:woredaId or (parentId=0 and levelId=5)) order by demographyName")
	List<DemographicEntity> fetchAllActiveDemographsKebele(@Param("woredaId") int woredaId);

	@Query("FROM DemographicEntity where status = false and parentId=:id ORDER BY demographyName")
	List<DemographicEntity> fetchAllActiveDemographs1(@Param("id") Integer id);

	@Query(nativeQuery = true, value = " SELECT distinct(loc.int_demography_id) as region_id,loc.vch_demography_name as region_name,loc.int_parent_id as country_id "
			+ "FROM wrsis.m_wr_research_center_location rcloc "
			+ "inner join wrsis.m_wr_demography loc on rcloc.int_region_id=loc.int_demography_id "
			+ "where rcloc.int_research_center_id=:researchCenterId and loc.int_level_id=2 and rcloc.bitstatus=false and loc.bitstatus=false")
	List<Object[]> fetchAllRegions(@Param("researchCenterId") Integer researchCenterId);

	@Query(nativeQuery = true, value = " SELECT distinct(loc.int_demography_id) as zone_id,loc.vch_demography_name as zone_name,loc.int_parent_id as region_id "
			+ "FROM wrsis.m_wr_research_center_location rcloc "
			+ "inner join wrsis.m_wr_demography loc on rcloc.int_zone_id=loc.int_demography_id "
			+ "where rcloc.int_research_center_id=:researchCenterId and loc.int_level_id=3 and rcloc.bitstatus=false and loc.bitstatus=false")
	List<Object[]> fetchAllZones(@Param("researchCenterId") Integer researchCenterId);

	@Query(nativeQuery = true, value = " (SELECT distinct(loc.int_demography_id) as woreda_id,loc.vch_demography_name as woreda_name,loc.int_parent_id as zone_id "
			+ "FROM wrsis.m_wr_research_center_location rcloc "
			+ "inner join wrsis.m_wr_demography loc on rcloc.int_woreda_id=loc.int_demography_id "
			+ "where rcloc.int_research_center_id=:researchCenterId and loc.int_level_id=4 and rcloc.bitstatus=false and loc.bitstatus=false)"
			+ "union (select demo.int_demography_id,demo.vch_demography_name,0 from wrsis.m_wr_demography demo where demo.int_parent_id=0 and  demo.int_level_id=4)")
	List<Object[]> fetchAllWoredas(@Param("researchCenterId") Integer researchCenterId);

	@Query(nativeQuery = true, value = " (SELECT (loc.int_demography_id) as kebel_id,loc.vch_demography_name as kebel_name,loc.int_parent_id as woreda_id "
			+ "FROM wrsis.m_wr_demography loc "
			+ "where loc.int_level_id=5 and loc.int_parent_id IN(select distinct(int_woreda_id) from wrsis.m_wr_research_center_location where int_research_center_id=:researchCenterId and bitstatus=false) and loc.bitstatus=false)"
			+ " union (select demo.int_demography_id,demo.vch_demography_name,0 from wrsis.m_wr_demography demo where demo.int_parent_id=0 and  demo.int_level_id=5 and demo.bitstatus=false) ")
	List<Object[]> fetchAllKebels(@Param("researchCenterId") Integer researchCenterId);

	@Query("FROM DemographicEntity where status = false and levelId=:levelId ORDER BY demographyName")
	List<DemographicEntity> findByLevelId(@Param("levelId") int i);

	@Query(nativeQuery = true, value = "select demo.int_demography_id,demo.vch_demography_name from \r\n"
			+ "	wrsis.m_wr_demography demo where demo.bitstatus = false and \r\n"
			+ " demo.int_demography_id in (select loc.int_zone_id from  wrsis.m_wr_research_center_location loc where  loc.bitstatus=false and loc.int_research_center_id=?1) ")
	List<Object[]> findByLevelIdByResearchCenterWiseZone(Integer researchCenterId);

	@Query(nativeQuery = true, value = "select demo.int_demography_id,demo.vch_demography_name from \r\n"
			+ "	wrsis.m_wr_demography demo where demo.bitstatus = false and \r\n"
			+ " demo.int_demography_id in (select loc.int_region_id from  wrsis.m_wr_research_center_location loc where  loc.bitstatus=false and loc.int_research_center_id=?1) ")
	List<Object[]> findByLevelIdByResearchCenterWiseRegion(Integer researchCenterId);

	@Query(nativeQuery = true, value = "select demo.int_demography_id,demo.vch_demography_name from \r\n"
			+ "	wrsis.m_wr_demography demo where (demo.int_level_id=4 and demo.int_parent_id=0) or demo.bitstatus = false and \r\n"
			+ " demo.int_demography_id in (select loc.int_woreda_id from  wrsis.m_wr_research_center_location loc where  loc.bitstatus=false and loc.int_research_center_id=?1) ")
	List<Object[]> findByLevelIdByResearchCenterWiseWoreda(Integer researchCenterId);

	@Query(nativeQuery = true, value = "select demo1.int_demography_id,demo1.vch_demography_name from \r\n"
			+ "	wrsis.m_wr_demography demo1 where (demo1.int_level_id=5 and demo1.int_parent_id=0)  or demo1.bitstatus = false and \r\n"
			+ " demo1.int_parent_id in (select demo.int_demography_id from \r\n"
			+ "	wrsis.m_wr_demography demo where (demo.int_level_id=5 and demo.int_parent_id=0) or demo.bitstatus = false and \r\n"
			+ " demo.int_demography_id in (select loc.int_woreda_id from  wrsis.m_wr_research_center_location loc where  loc.bitstatus=false and loc.int_research_center_id=?1))")
	List<Object[]> findByLevelIdByResearchCenterWiseKebele(Integer researchCenterId);
	
	@Query(value = "select demo.int_demography_id,demo.vch_demography_name from "
			+ "wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_parent_id =?1 order by demo.vch_demography_name ", nativeQuery = true)
	
	List<Object[]> fetchAllActiveDemographsByParentId(int paerntId);

	
	@Query(nativeQuery=true,value="select vch_demography_name from wrsis.m_wr_demography where int_demography_id=?1 and bitstatus=false")
	String getDemographyNameByDemographyId(Integer regionId);

	@Query(nativeQuery=true,value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_demography_id in \r\n" + 
			" (select loc.int_zone_id from  wrsis.t_wr_recommendation_loc loc where loc.bitstatus=false "
			+ " and loc.int_region_id=?1 and loc.int_recom_id=?2)")
	List<Object[]> fetchAllZoneInRecom(int regionId, int recomId);

	@Query(value = "select demo.int_demography_id,demo.vch_demography_name from "
			+ " wrsis.m_wr_demography demo where (demo.int_level_id=4 and demo.int_parent_id=0) "
			+ " or  demo.bitstatus = false and demo.int_demography_id in (select loc.int_woreda_id "
			+ " from  wrsis.t_wr_recommendation_loc loc where loc.bitstatus=false and loc.int_region_id=?1 "
			+ " and loc.int_zone_id=?2 and loc.int_recom_id=?3)", nativeQuery = true)
	List<Object[]> fetchAllWoredaInRecom(int region, int zone, int recomId);

	@Query(nativeQuery=true,value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and \r\n" + 
			" (demo.int_parent_id= ?1 or (demo.int_parent_id=0 and demo.int_level_id=5))")
	List<Object[]> fetchAllActiveKebele(int woredaId);

	@Query(nativeQuery=true,value="select demo.int_demography_id, demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo \r\n" + 
			" left join wrsis.t_wr_user_location_details uld on demo.int_demography_id = uld.int_demography_id\r\n" + 
			" where (demo.int_level_id=4 and demo.int_parent_id=0)     \r\n" + 
			" or uld.bitstatus=false  and uld.int_user_loc_tag_id = (select int_user_loc_tag_id \r\n" + 
			" from wrsis.t_wr_user_location_tag \r\n" + 
			" where int_user_id=:userId and bitstatus=false)")
	List<Object[]> fetchAllWoredaTaggedToUser(int userId);

	@Query(nativeQuery = true , value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_level_id=2")
	List<Object[]> getActiveRegions();
	@Query(nativeQuery = true , value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_level_id=3")
	List<Object[]> getActiveZones();
	@Query(nativeQuery = true , value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_level_id=4")
	List<Object[]> getActiveWoredas();
	@Query(nativeQuery = true , value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false and demo.int_level_id=5")
	List<Object[]> getActiveKebeles(); 
	
}
