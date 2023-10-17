
package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.GisMapEntity;
import com.csmpl.wrsis.webportal.entity.MapEntity;

public interface GisMapRepository extends JpaRepository<GisMapEntity, Integer> {

	@Query(value = "select * from wrsis.sp_wr_gis_rust_data(?1,?2)", nativeQuery = true)
	List<Object[]> getLatestGISData(Integer disease, Integer mapTypeId);

	@Query(value = "select * from wrsis.sp_wr_gis_rust_history_data(?1,?2)", nativeQuery = true)
	List<Object[]> getGISHistoryData(Integer disease, Integer mapTypeId);

	@Query(nativeQuery = true, value = " SELECT int_map_type_id,vch_map_type FROM wrsis.m_wr_map_type  where bitstatus = false ORDER BY vch_map_type ")
	List<Object[]> fetchAllMapType();

	// before search
	@Query(nativeQuery = true, value = "select m_wr_map_type.vch_map_type,\r\n"
			+ "t_wr_forecasting_file.vch_file_name,t_wr_forecasting_file.vch_store_name,t_wr_forecasting_file.vch_layer_name,t_wr_forecasting_file.dtmcreatedon,\r\n"
			+ "m_wr_rust_type.vch_rust_type\r\n" + "from wrsis.m_wr_map_type\r\n"
			+ "inner join wrsis.t_wr_forecasting_file\r\n"
			+ "on m_wr_map_type.int_map_type_id=t_wr_forecasting_file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type\r\n"
			+ "on t_wr_forecasting_file.int_rust_type_id=m_wr_rust_type.int_rust_type_id")

	List<Object[]> getDataList();

	@Query(nativeQuery = true, value = "SELECT * FROM wrsis.m_wr_map_type  where bitstatus = false ORDER BY vch_map_type ")
	List<MapEntity> viewAllMapType();

	@Query("from GisMapEntity where DATE_TRUNC('day', uploadOn)<?1 and bitStatus=false")
	List<GisMapEntity> getGISMapRecordBeforeDate(Date date);

	@Query(nativeQuery = true, value = "select styl.int_style_id,styl.vch_style_name,styl.vch_style_range,styl.int_map_type_id,styl.int_rust_type_id " + 
			" from wrsis.t_wr_forecasting_style_details styl where bitstatus=false ")
	List<Object[]> getStyleDetails();

	@Query("from GisMapEntity where DATE_TRUNC('day', uploadOn)=?1 and bitStatus=false")
	List<GisMapEntity> getGISMapRecordByDate(Date date);

	@Query(value = "select * from wrsis.sp_wr_get_gis_ivr_data(?1,?2)", nativeQuery = true)
	List<Object[]> getGISIVRdata(String startDate, String endDate);

	@Query(value = "select * from wrsis.sp_wr_get_gis_ivr_count_data(?1,?2,?3,?4,?5)", nativeQuery = true)
	List<Object[]> getIVRCountDetails(String startDate, String endDate,String regionName,String zoneName,String woredaName);
}
