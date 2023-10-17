package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;

@Repository
public interface DemographicRepository extends JpaRepository<DemographicEntity, Integer> {

	@Query("from DemographicEntity where levelId=:id and status=false order by demographyName")
	List<DemographicEntity> findByLevelId(@Param("id") Integer levelId);

	@Query("From DemographicEntity")
	List<DemographicEntity> viewAllDemography();

	DemographicEntity findByparentId(Integer parentId);

	List<DemographicEntity> findByParentId(int parentId);

	DemographicEntity findByDemographyId(@Param("demographyId") int demographyId);

	List<DemographicEntity> findByParentIdAndStatus(int parentId, boolean b);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "region.bitstatus as delete_flag,region.vch_alias, region.vch_desc\r\n"
			+ "from wrsis.m_wr_demography region\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where region.int_level_id=?1 and country.bitstatus=false order by region.vch_demography_name\r\n")
	List<Object[]> retriveRegionList(int levelId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n" + " from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=?1 and region.bitstatus=false and country.bitstatus=false order by zone.vch_demography_name\r\n")
	List<Object[]> retriveZoneList(int levelId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc\r\n"
			+ "from wrsis.m_wr_demography woreda\r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where woreda.int_level_id=?1 and zone.bitstatus=false and region.bitstatus=false and country.bitstatus=false order by woreda.vch_demography_name")
	List<Object[]> retriveWoredaList(int levelId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id\r\n,"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc\r\n"
			+ "from wrsis.m_wr_demography kebele\r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where kebele.int_level_id=?1 and woreda.bitstatus=false and zone.bitstatus=false and region.bitstatus=false and country.bitstatus=false order by kebele.vch_demography_name")
	List<Object[]> retriveKebeleList(int levelId);

	

	@Query("From DemographicEntity t where t.levelId='2' And parentId='1' And status='false' ORDER BY t.demographyId")
	List<DemographicEntity> viewAllRegionByStatus();

	@Query("From DemographicEntity t where t.levelId=:levelId And t.parentId=:parentId And status='false' ORDER BY t.demographyName")
	List<DemographicEntity> viewAllZoneByRegionIdAndStatus(Integer levelId, Integer parentId);

	@Query("From DemographicEntity t where status='false' and (t.parentId=:parentId or (t.levelId=:levelId And t.parentId=0)) ORDER BY t.demographyName")
	List<DemographicEntity> viewAllWoredaByZoneIdAndStatus(Integer levelId, Integer parentId);

	@Query("from DemographicEntity t where t.demographyId=:demographyId ")
	DemographicEntity getRegionDetialsByDemographyId(Integer demographyId);

	@Query("from DemographicEntity t where t.demographyId=:demographyId")
	DemographicEntity getZoneDetialsByDemographyId(Integer demographyId);

	@Query("from DemographicEntity t where t.demographyId=:demographyId")
	DemographicEntity getWoredaDetialsByDemographyId(Integer demographyId);

	@Query("from DemographicEntity t where t.levelId='4' And parentId=:parentId And status='false'")
	List<DemographicEntity> viewAllWoredaDetialsByZoneId(Integer parentId);

	DemographicEntity findByDemographyNameIgnoreCase(String demographyName);

	
	@Query("From DemographicEntity t where status='false' and (t.parentId=:parentId or (t.levelId=:levelId And t.parentId=0)) ORDER BY t.demographyName")
	List<DemographicEntity> viewAllKebeleByWoredaIdAndStatus(Integer levelId, Integer parentId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "region.bitstatus as delete_flag,region.vch_alias, region.vch_desc\r\n"
			+ "from wrsis.m_wr_demography region\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where region.int_level_id=2\r\n" + " and country.bitstatus=false\r\n"
			+ "and (region.vch_demography_name LIKE %:regionName%)\r\n"
			+ "and region.bitstatus=:condition" + " order by region.int_demography_id")
	List<Object[]> viewRegion(@Param("condition") boolean condition, @Param("regionName") String regionName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "region.bitstatus as delete_flag,region.vch_alias, region.vch_desc\r\n"
			+ "from wrsis.m_wr_demography region\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where region.int_level_id=2\r\n" + " and country.bitstatus=false\r\n"
			+ "and (region.vch_demography_name LIKE %:regionName%)\r\n"
			+ "order by region.int_demography_id")
	List<Object[]> viewRegion(@Param("regionName") String regionName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3 \r\n" 
			+ "and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false\r\n" + "and region.int_demography_id=:regionId\r\n"
			+ "order by zone.int_demography_id")
	List<Object[]> viewZone(@Param(WrsisPortalConstant.REGION_ID) int regionId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3\r\n" + " and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false\r\n" + " and region.int_demography_id=:regionId\r\n"
			+ "and (zone.vch_demography_name LIKE %:zoneName%)\r\n"
			+ "order by zone.int_demography_id")
	List<Object[]> viewZone(@Param(WrsisPortalConstant.REGION_ID) int regionId, @Param("zoneName") String zoneName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3\r\n" + " and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false\r\n" + " and region.int_demography_id=:regionId \r\n"
			+ "and (zone.vch_demography_name LIKE %:zoneName%) \r\n"
			+ "and zone.bitstatus=:condition\r\n" + " order by zone.int_demography_id ")
	List<Object[]> viewZone(@Param(WrsisPortalConstant.REGION_ID) int regionId, @Param("zoneName") String zoneName,
			@Param("condition") boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3\r\n" + " and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false \r\n"
			+ "and (zone.vch_demography_name LIKE %:zoneName%) \r\n"
			+ "order by zone.int_demography_id \r\n")
	List<Object[]> viewZone(@Param("zoneName") String zoneName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3 \r\n" + " and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false \r\n"
			+ "and (zone.vch_demography_name LIKE %:zoneName%) \r\n"
			+ "and zone.bitstatus=:condition" + " order by zone.int_demography_id \r\n")
	List<Object[]> viewZone(@Param("zoneName") String zoneName, @Param("condition") boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3 \r\n"
			+ "and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false  \r\n"
			+ "and zone.bitstatus=:condition\r\n"
			+ "order by zone.int_demography_id \r\n")
	List<Object[]> viewZone(@Param("condition") boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3 \r\n" + " and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false\r\n" + " and region.int_demography_id=:regionId\r\n"
			+ "and region.bitstatus=:condition\r\n" + " order by zone.int_demography_id")
	List<Object[]> viewZoneByRegionIdAndStatus(int regionId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "zone.bitstatus as delete_flag,zone.vch_alias, zone.vch_desc\r\n"
			+ "from wrsis.m_wr_demography zone\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where zone.int_level_id=3 \r\n"
			+ "and region.bitstatus=false \r\n"
			+ "and country.bitstatus=false  \r\n"
			+ "order by zone.int_demography_id")
	List<Object[]> viewZone();

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and\r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId\r\n" + " order by woreda.int_demography_id\r\n")
	List<Object[]> viewWoredaByRegionId(@Param(WrsisPortalConstant.REGION_ID) int regionId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId and\r\n" + " zone.int_demography_id=:zoneId \r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdandZoneId(@Param(WrsisPortalConstant.REGION_ID) int regionId, @Param("zoneId") int zoneId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and\r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false\r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoreda();

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%)\r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdZoneIdAndWoredaName(int regionId, int zoneId, String woredaName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%) \r\n"
			+ "and woreda.bitstatus=:condition\r\n" + " order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdZoneIdWoredaNameAndStatus(int regionId, int zoneId, String woredaName,
			boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  "
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "zone.int_demography_id=:zoneId \r\n" + " order by woreda.int_demography_id")
	List<Object[]> viewWoredaByZoneId(int zoneId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%)\r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByZoneIdAndWoredaName(int zoneId, String woredaName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%)\r\n"
			+ "and woreda.bitstatus=:condition\r\n" + " order by woreda.int_demography_id")
	List<Object[]> viewWoredaByZoneIdWoredaNameAndStatus(int zoneId, String woredaName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda  "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and\r\n " + " country.bitstatus=false and \r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%)\r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByWoredaName(String woredaName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography woreda  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%) and\r\n"
			+ "woreda.bitstatus=:condition\r\n " + " order by woreda.int_demography_id")
	List<Object[]> viewWoredaByWoredaNameAndStatus(String woredaName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and\r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "woreda.bitstatus=:condition\r\n " + " order by woreda.int_demography_id")
	List<Object[]> viewWoredaByStatus(boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id\r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id\r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId and\r\n"
			+ "(woreda.vch_demography_name LIKE %:woredaName%)\r\n"
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdandWoredaName(int regionId, String woredaName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "region.int_demography_id=:regionId and\r\n" + " woreda.bitstatus=:condition\r\n "
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdandStatus(int regionId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n "
			+ "from wrsis.m_wr_demography woreda \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where woreda.int_level_id=4 and \r\n" + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and " + " country.bitstatus=false and"
			+ "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n" + " woreda.bitstatus=:condition \r\n "
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByRegionIdZoneIdAndStatus(int regionId, int zoneId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "woreda.bitstatus as delete_flag,woreda.vch_alias, woreda.vch_desc \r\n"
			+ "from wrsis.m_wr_demography woreda \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where woreda.int_level_id=4 and\r\n " + " zone.bitstatus=false and \r\n"
			+ "region.bitstatus=false and \r\n" + " country.bitstatus=false and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n" + " woreda.bitstatus=:condition\r\n "
			+ "order by woreda.int_demography_id")
	List<Object[]> viewWoredaByZoneIdAndStatus(int zoneId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id"
			+ "where kebele.int_level_id=5 and\r\n" + " woreda.bitstatus=false and\r\n"
			+ "zone.bitstatus=false and \r\n" + " region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + " region.int_demography_id=:regionId\r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionId(int regionId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where kebele.int_level_id=5 and\r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and\r\n"
			+ "country.bitstatus=false\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebele();

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n  "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + " woreda.bitstatus=false and\r\n"
			+ "zone.bitstatus=false and \r\n" + " region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + " region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId \r\n" + " order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdandZoneId(int regionId, int zoneId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where kebele.int_level_id=5 and\r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "woreda.int_demography_id=:woredaId \r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdandWoredaId(int regionId, int woredaId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n "
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele \r\n "
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n " + " woreda.bitstatus=false and\r\n "
			+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and \r\n "
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) \r\n "
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdandKebeleName(int regionId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n "
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele \r\n "
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n " + " woreda.bitstatus=false and \r\n "
			+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and \r\n "
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "kebele.bitstatus=:condition\r\n " + " order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdandStatus(int regionId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele \r\n "
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n " + " woreda.bitstatus=false and \r\n "
			+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and\r\n "
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "zone.int_demography_id=:zoneId and\r\n " + " woreda.int_demography_id=:woredaId \r\n "
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdAndWoredaId(int regionId, int zoneId, int woredaId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n" + " woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and\r\n " + " region.bitstatus=false and\r\n"
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "woreda.int_demography_id=:woredaId and \r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%)\r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdWoredaIdandKebeleName(int regionId, int woredaId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n "
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele \r\n "
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n " + " woreda.bitstatus=false and \r\n "
			+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n "
			+ "kebele.bitstatus=:condition " + " order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdKebeleNameandStatus(int regionId, String kebeleName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n "
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and\r\n " + " woreda.bitstatus=false and \r\n "
	 		+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and\r\n "
			+ "country.bitstatus=false and\r\n " + " region.int_demography_id=:regionId and\r\n "
			+ "zone.int_demography_id=:zoneId and\r\n " + " woreda.int_demography_id=:woredaId and\r\n "
			+ "(kebele.vch_demography_name LIKE %:kebeleName%)\r\n "
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdWoredaIdandKebeleName(int regionId, int zoneId, int woredaId,
			String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n "
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n "
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n "
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n "
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n "
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc \r\n "
			+ "from wrsis.m_wr_demography kebele \r\n "
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n "
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n "
			+ "where kebele.int_level_id=5 and \r\n " + " woreda.bitstatus=false and \r\n "
			+ "zone.bitstatus=false and \r\n " + " region.bitstatus=false and \r\n "
			+ "country.bitstatus=false and\r\n" + " region.int_demography_id=:regionId and\r\n "
			+ "woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n"
			+ "kebele.bitstatus=:condition\r\n " + " order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdWoredaIdKebeleNameandStatus(int regionId, int woredaId, String kebeleName,
			boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id, \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id, \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id , \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id ,\r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id ,\r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc\r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and\r\n"
			+ "zone.bitstatus=false and\r\n" + "region.bitstatus=false and\r\n  "
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n" + "woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n"
			+ "kebele.bitstatus=:condition\r\n " + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdWoredaIdKebeleNameandStatus(int regionId, int zoneId, int woredaId,
			String kebeleName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneId(int zoneId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId and\r\n"
			+ "woreda.int_demography_id=:woredaId and\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdAndWoredaId(int zoneId, int woredaId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdAndKebeleName(int zoneId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId and\r\n"
			+ "kebele.bitstatus=:condition\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdAndStatus(int zoneId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId and\r\n"
			+ "woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdWoredaIdAndKebeleName(int zoneId, int woredaId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + " woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + " region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + " zone.int_demography_id=:zoneId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n"
			+ "kebele.bitstatus=:condition\r\n" + " order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdAndKebeleName(int zoneId, String kebeleName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "zone.int_demography_id=:zoneId and"
			+ "woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n"
			+ "kebele.bitstatus=:condition\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByZoneIdWoredaIdKebeleNameAndStatus(int zoneId, int woredaId, String kebeleName,
			boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "woreda.int_demography_id=:woredaId \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByWoredaId(int woredaId);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + " woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByWoredaIdAndKebeleName(int woredaId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "woreda.int_demography_id=:woredaId and\r\n"
			+ "kebele.bitstatus=:condition\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByWoredaIdAndStatus(int woredaId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "woreda.int_demography_id=:woredaId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and \r\n"
			+ "kebele.bitstatus=:condition\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByWoredaIdKebeleNameAndStatus(int woredaId, String kebeleName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByKebeleName(String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and \r\n"
			+ "kebele.bitstatus=:condition" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByKebeleNameAndStatus(String kebeleName, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "kebele.bitstatus=:condition\r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByStatus(boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n" + "woreda.int_demography_id=:woredaId and\r\n"
			+ "kebele.bitstatus=:condition\r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdWoredaIdandStatus(int regionId, int zoneId, int woredaId,
			boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,\r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n" + "kebele.bitstatus=:condition\r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdAndStatus(int regionId, int zoneId, boolean condition);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,\r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,\r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) \r\n"
			+ "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdandKebeleName(int regionId, int zoneId, String kebeleName);

	@Query(nativeQuery = true, value = "select country.vch_demography_name as country_name, country.int_demography_id as country_id,  \r\n"
			+ "region.vch_demography_name as region_name,region.int_demography_id as region_id,  \r\n"
			+ "zone.vch_demography_name as zone_name, zone.int_demography_id as zone_id ,  \r\n"
			+ "woreda.vch_demography_name as woreda_name, woreda.int_demography_id as woreda_id , \r\n"
			+ "kebele.vch_demography_name as kebele_name, kebele.int_demography_id as kebele_id , \r\n"
			+ "kebele.bitstatus as delete_flag,kebele.vch_alias, kebele.vch_desc  \r\n"
			+ "from wrsis.m_wr_demography kebele  \r\n"
			+ "inner join wrsis.m_wr_demography woreda on kebele.int_parent_id=woreda.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography zone on woreda.int_parent_id=zone.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography region on zone.int_parent_id=region.int_demography_id  \r\n"
			+ "inner join wrsis.m_wr_demography country on region.int_parent_id=country.int_demography_id  \r\n"
			+ "where kebele.int_level_id=5 and \r\n" + "woreda.bitstatus=false and \r\n"
			+ "zone.bitstatus=false and \r\n" + "region.bitstatus=false and \r\n"
			+ "country.bitstatus=false and\r\n" + "region.int_demography_id=:regionId and\r\n"
			+ "zone.int_demography_id=:zoneId and\r\n"
			+ "(kebele.vch_demography_name LIKE %:kebeleName%) and\r\n"
			+ "zone.bitstatus=:condition \r\n" + "order by kebele.int_demography_id")
	List<Object[]> viewKebeleByRegionIdZoneIdKebeleNameandStatus(int regionId, int zoneId, String kebeleName,
			boolean condition);

	@Query(value = "SELECT int_demography_id, vch_demography_name, int_level_id, int_parent_id, vch_alias, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon, vch_desc FROM wrsis.m_wr_demography where int_level_id=2 and bitstatus='false' order by vch_demography_name", nativeQuery = true)
	List<DemographicEntity> viewAllZoneByLevelAndStatus();

	@Query(value = "SELECT int_demography_id, vch_demography_name, int_level_id, int_parent_id, vch_alias, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon, vch_desc\r\n"
			+ "	FROM wrsis.m_wr_demography where int_demography_id=?1 and bitstatus='false';", nativeQuery = true)
	DemographicEntity findByparentIdAndStatu(Integer parentId);

	@Query("select demographyName from DemographicEntity where LOWER(demographyName) LIKE LOWER(:regionName)")
	List<DemographicEntity> getExistRegion(@Param("regionName") String regionName);

	@Query("select demo from DemographicEntity demo where LOWER(demo.demographyName) LIKE LOWER(?1) and demo.demographyId!=?2")
	List<DemographicEntity> getExistRegion(String regionName, int demographyId);

	@Query("select demographyName from DemographicEntity where LOWER(demographyName) LIKE LOWER(?2) and demographyId!=?1")
	List<DemographicEntity> getExistZone(int zoneId, String demographyName);

	@Query("select demographyName from DemographicEntity where LOWER(demographyName) LIKE LOWER(?1)")
	List<DemographicEntity> getExistZone(String demographyName);

	@Query("from DemographicEntity where levelId=2 and status=false")
	List<DemographicEntity> retriveRegions();

	@Query(nativeQuery = true, value = "select  distinct rcloc.int_region_id, \r\n"
			+ "rcloc.int_research_center_id,\r\n" + "demo1.vch_demography_name\r\n"
			+ "from wrsis.m_wr_research_center_location rcloc \r\n"
			+ "inner join wrsis.m_wr_demography demo1 on rcloc.int_region_id=demo1.int_demography_id\r\n"
			+ "where rcloc.int_research_center_id=?1 and rcloc.bitstatus=false and demo1.bitstatus=false\r\n"
			+ "group by rcloc.int_region_id,rcloc.int_research_center_id,demo1.vch_demography_name")
	List<Object[]> retriveRegionsByResearchcenter(int rcId);

	@Query(nativeQuery = true, value = "select rcsp.int_spelization_id, \r\n" + "rcsp.int_research_center_id,\r\n"
			+ "rust.vch_rust_type\r\n" + "from wrsis.m_wr_research_center_spelization rcsp \r\n"
			+ "inner join wrsis.m_wr_rust_type rust on rcsp.int_spelization_id=rust.int_rust_type_id\r\n"
			+ "where rcsp.int_research_center_id=?1 and rcsp.bitstatus=false and rust.bitstatus=false")
	List<Object[]> retriveRustByResearchcenter(int rcId);

	@Query("from DemographicEntity t where t.levelId='4' And parentId=:parentId And status='false'")
	List<DemographicEntity> viewAllWoredaDetialsByZoneId(int parentId);

	@Query("from DemographicEntity t where t.levelId='5' And parentId=:parentId And status='false'")
	List<DemographicEntity> viewAllKebeleDetialsByWoredaId(int parentId);

	@Query("select alias from DemographicEntity where LOWER(alias) LIKE LOWER(?1)")
	List<DemographicEntity> getExistAlias(String demographyName);

	@Query("select alias from DemographicEntity where demographyId!=?2 AND LOWER(alias) LIKE LOWER(?1)")
	List<DemographicEntity> getExistAlias(String alias, int demographyId);

	@Query(nativeQuery=true,value = "select vch_demography_name From wrsis.m_wr_demography "
			+ "where bitstatus='false' and int_level_id=3 "
			+ "ORDER BY vch_demography_name")
	List<String> getZoneNames();

	@Query(nativeQuery= true, value = "select int_demography_id From wrsis.m_wr_demography where "
			+ "Lower(vch_demography_name) Like Lower(?1) and int_level_id=?2 and bitstatus=false")
	int getDemoIdByDemoNameAndLevelId(String demoName, int levelId);

	@Query(nativeQuery= true, value = "select int_parent_id From wrsis.m_wr_demography where int_demography_id=?1 "
			+ "and bitstatus=false")
	int getParentIdByDemogId(int woredaId);

	@Query(nativeQuery= true, value = "select vch_demography_name From wrsis.m_wr_demography where int_level_id=4 and "
			+ "Lower(vch_demography_name) Like Lower(?1) and bitstatus=false")
	String checkWoredaNameByWoredaName(String valueOf);

	@Query("From DemographicEntity t where status='false' and t.levelId=:levelId and t.parentId=:parentId ORDER BY t.demographyName")
	List<DemographicEntity> viewAllDemographyExcludingOther(Integer levelId, int parentId);

	List<DemographicEntity> findByLevelIdAndDemographyNameIgnoreCaseAndStatus(int levelId, String zone, boolean bitStaus);

	List<DemographicEntity> findByDemographyNameIgnoreCaseAndStatus(String zoneName, boolean status);

}
