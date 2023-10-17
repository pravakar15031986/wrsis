package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.RecommendationLocationEntity;

@Repository
public interface RecommendationLocationRepository extends JpaRepository<RecommendationLocationEntity, Integer> {

	@Query(value = "SELECT int_recom_loc_id, int_recom_id, vch_recom_no, int_region_id, int_zone_id, int_woreda_id, int_kebel_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation_loc where int_recom_id=?1 and int_woreda_id=?2 and  bitstatus=false", nativeQuery = true)
	RecommendationLocationEntity checkWoredaExist(Integer recommnId, Integer woredaId);

	@Query(value = "SELECT int_recom_loc_id, int_recom_id, vch_recom_no, int_region_id, int_zone_id, int_woreda_id, int_kebel_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation_loc where int_recom_id=?1 and  bitstatus=false", nativeQuery = true)
	List<RecommendationLocationEntity> viewRecommendLocationByRecommendId(Integer recommnId);

	@Query(value = "SELECT int_recom_loc_id, int_recom_id, vch_recom_no, int_region_id, int_zone_id, int_woreda_id, int_kebel_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation_loc where int_recom_loc_id=?1 and int_kebel_id=?2 and bitstatus='false';", nativeQuery = true)
	RecommendationLocationEntity findLocationDetailsByLocationIdAndKebeleId(Integer locationId, Integer kebeleId);

	@Query(value = "SELECT int_recom_loc_id, int_recom_id, vch_recom_no, int_region_id, int_zone_id, int_woreda_id, int_kebel_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ "FROM wrsis.t_wr_recommendation_loc where int_recom_loc_id=?1 and int_woreda_id=?2 and bitstatus='false';", nativeQuery = true)
	RecommendationLocationEntity findLocationDetailsByLocationIdAndWoredaId(Integer locationId, Integer woredaId);

	@Query(value = "SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionId(@Param(WrsisPortalConstant.REGION_ID) Integer regionId);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneID(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("zoneId") Integer zoneId);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2 and l.int_woreda_id=?3 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneIDAndWoredaId(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("zoneId") Integer zoneId, @Param("woredaId") Integer woredaId);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2 and l.int_woreda_id=?3 and l.int_kebel_id=?4 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdAndKebeleId(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("zoneId") Integer zoneId, @Param("woredaId") Integer woredaId, @Param("kebeleId") Integer kebeleId);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1 and l.bitstatus=false\r\n"
			+ " and  DATE_TRUNC('day', r.dtmcreatedon ) >=  ?2  AND DATE_TRUNC('day', r.dtmcreatedon )  <=  ?3\r\n"
			+ " \r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdWithDate(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("sDate") Date sDate, @Param("eDate") Date eDate);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2"
			+ " and  DATE_TRUNC('day', r.dtmcreatedon ) >=  ?3  AND DATE_TRUNC('day', r.dtmcreatedon )  <=  ?4 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneIDWithDate(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("zoneId") Integer zoneId, @Param("sDate") Date sDate, @Param("eDate") Date eDate);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2 and l.int_woreda_id=?3 "
			+ " and  DATE_TRUNC('day', r.dtmcreatedon ) >=  ?4  AND DATE_TRUNC('day', r.dtmcreatedon )  <=  ?5  and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdDate(@Param(WrsisPortalConstant.REGION_ID) Integer regionId,
			@Param("zoneId") Integer zoneId, @Param("woredaId") Integer woredaId, @Param("sDate") Date sDate,
			@Param("eDate") Date eDate);

	@Query(value = " SELECT r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby, r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon,\r\n"
			+ " STRING_AGG(CAST(l.int_recom_loc_id AS varchar),',') as locationId,STRING_AGG(CAST(l.int_region_id AS varchar),',') as Region,\r\n"
			+ " STRING_AGG(CAST(l.int_zone_id AS varchar),',') as Zone,\r\n"
			+ " STRING_AGG(CAST(l.int_woreda_id AS varchar),',') as Woreda,\r\n"
			+ " STRING_AGG(CAST(l.int_kebel_id AS varchar),',') as kebele\r\n"
			+ " FROM wrsis.t_wr_recommendation r\r\n"
			+ " inner join wrsis.t_wr_recommendation_loc l on l.int_recom_id=r.int_recom_id\r\n"
			+ " where r.bitstatus=false and l.int_region_id=?1  and l.int_zone_id=?2 and l.int_woreda_id=?3 and l.int_kebel_id=?4 "
			+ " and  DATE_TRUNC('day', r.dtmcreatedon ) >=  ?5  AND DATE_TRUNC('day', r.dtmcreatedon )  <=  ?6 and l.bitstatus=false\r\n"
			+ " group by r.int_recom_id, r.vch_recom_no, r.int_advisory_id, r.vch_advisory_no, r.int_recom_type, \r\n"
			+ " r.vch_recom_file_name, r.vch_recom_summary, r.int_recom_status, r.int_publish_by, r.dt_publish_on, \r\n"
			+ " r.vch_publish_remarks, r.int_forward_by, dt_forward_on, r.vch_forward_remarks, r.bitstatus, r.intcreatedby,\r\n"
			+ " r.dtmcreatedon, r.intupdatedby, r.dtmupdatedon order by r.dtmcreatedon DESC", nativeQuery = true)
	List<Object[]> searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdAndKebeleIdDate(
			@Param(WrsisPortalConstant.REGION_ID) Integer regionId, @Param("zoneId") Integer zoneId, @Param("woredaId") Integer woredaId,
			@Param("kebeleId") Integer kebeleId, @Param("sDate") Date sDate, @Param("eDate") Date eDate);

	@Query(value = "SELECT int_recom_loc_id, int_recom_id, vch_recom_no, int_region_id, int_zone_id, int_woreda_id, int_kebel_id, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon\r\n"
			+ " FROM wrsis.t_wr_recommendation_loc where int_recom_id=?1 and int_woreda_id=?2 and  bitstatus=false", nativeQuery = true)
	List<RecommendationLocationEntity> checkWoredaExistByWoreda(Integer recommnId, Integer woredaId);

	@Query(nativeQuery = true, value = "select distinct int_region_id from wrsis.t_wr_recommendation_loc "
			+ "where int_recom_id=?1 and bitstatus=false")
	List<Short> getRegionIdByRecomendIdAndStatus(int recomId);

	@Query(nativeQuery = true, value = "select int_zone_id from wrsis.t_wr_recommendation_loc "
			+ "where int_recom_id=?1 and bitstatus=false")
	List<Short> getZoneIdByRecomendIdAndStatus(int recomId);

	@Query(nativeQuery = true, value = "select int_woreda_id from wrsis.t_wr_recommendation_loc "
			+ "where int_recom_id=?1 and bitstatus=false")
	List<Short> getWoredaIdByRecomendIdAndStatus(int recomId);
	
	@Query(nativeQuery=true,value="select demo.int_demography_id,demo.vch_demography_name from \r\n" + 
			" wrsis.m_wr_demography demo where demo.bitstatus = false \r\n" + 
			" and demo.int_demography_id in \r\n" + 
			" (select loc.int_kebel_id from  wrsis.t_wr_recommendation_loc loc where loc.bitstatus=false and\r\n" + 
			" loc.int_region_id=?1 and loc.int_zone_id=?2 and loc.int_woreda_id=?3 and loc.int_recom_id=?4) \r\n" + 
			" or (demo.int_parent_id=0 and demo.int_level_id=5)")
	List<Object[]> fetchKebeleIdinRecom(int regionId, int zoneId, int woredaId, int recomId);
}
