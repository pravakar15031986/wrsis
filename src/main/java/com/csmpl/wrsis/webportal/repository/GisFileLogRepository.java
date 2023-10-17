package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.GisFileLogEntity;

public interface GisFileLogRepository extends JpaRepository<GisFileLogEntity, Integer> {

	@Query(nativeQuery = true, value = " select count(*) from wrsis.t_wr_forecast_upload_file_log where DATE_TRUNC('day', dtm_upload_date )=:startDate and bitstatus=false ")
	Integer CheckLogFileExit(@Param("startDate") Date startDate);

	@Query(nativeQuery = true, value = "select vch_file_name as file_name,	cast(TO_CHAR(t_wr_forecast_upload_file_log.dtm_upload_date, 'dd-Mon-YYYY')  as text) as date,int_forecast_upload_file_id,cast(TO_CHAR(t_wr_forecast_upload_file_log.dtm_created_on, 'dd-Mon-YYYY hh:mi:ss AM')  as text) as createdon,bitstatus as status \r\n"
			+ "from wrsis.t_wr_forecast_upload_file_log where bitstatus= false and dtm_upload_date >= CURRENT_DATE")

	List<Object[]> getDataList();

	@Query(nativeQuery = true, value = "select vch_file_name as file_name,	cast(TO_CHAR(t_wr_forecast_upload_file_log.dtm_upload_date, 'dd-Mon-YYYY')  as text) as date,int_forecast_upload_file_id,cast(TO_CHAR(t_wr_forecast_upload_file_log.dtm_created_on, 'dd-Mon-YYYY hh:mi:ss AM')  as text) as createdon,bitstatus as status  \r\n"
			+ " from  wrsis.t_wr_forecast_upload_file_log where  DATE_TRUNC('day',dtm_upload_date) >= ?1 and DATE_TRUNC('day',dtm_upload_date) <= ?2  order by int_forecast_upload_file_id desc")
	List<Object[]> getDataList(@Param("startDate") Date startDate, @Param("endDate") Date endDate);//bitstatus=false and 
	

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2")
	List<Object[]> getGisForcastingFileByDate(Date startDate, Date endDate);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_rust_type_id=?3")
	List<Object[]> getGisForcastingFileByDateAndRustType(Date startDate, Date endDate, Integer rustId);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_map_type_id=?3")

	List<Object[]> getGisForcastingFileByDateAndMapType(Date startDate, Date endDate, Integer mapTypId);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_rust_type_id=?3 and file.int_map_type_id=?4")

	List<Object[]> getGisForcastingFileByDateRustTypeAndMapType(Date startDate, Date endDate, Integer rustId,
			Integer mapTypId);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.bitstatus=?3")
	List<Object[]> getGisForcastingFileByDateAndStatus(Date startDate, Date endDate, Boolean status);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_rust_type_id=?3 and file.bitstatus=?4")
	List<Object[]> getGisForcastingFileByDateRustTypeAndStatus(Date startDate, Date endDate, Integer rustId,
			Boolean status);

	//Gis Forcasting file

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_map_type_id=?3 and file.bitstatus=?4")
	List<Object[]> getGisForcastingFileByDateAndMapTypeAndStatus(Date startDate, Date endDate, Integer mapTypId,
			Boolean status);

	@Query(nativeQuery = true, value = "select map.vch_map_type,\r\n"
			+ "file.vch_file_name,file.vch_store_name,file.vch_layer_name,file.dtmcreatedon,\r\n"
			+ "rust.vch_rust_type ,file.int_forecasting_file_id,file.bitstatus\r\n" + "from wrsis.m_wr_map_type map\r\n"
			+ "inner join wrsis.t_wr_forecasting_file file\r\n" + "on map.int_map_type_id=file.int_map_type_id\r\n"
			+ "inner join wrsis.m_wr_rust_type rust on file.int_rust_type_id=rust.int_rust_type_id\r\n"
			+ "where DATE_TRUNC('day',file.dtmcreatedon ) >=?1 and DATE_TRUNC('day',file.dtmcreatedon ) <=?2 and file.int_rust_type_id=?3 and file.int_map_type_id=?4 and file.bitstatus=?5 ")
	List<Object[]> getGisForcastingFileByDateAndRustTypeMapTypeAndStatus(Date startDate, Date endDate, Integer rustId,
			Integer mapTypId, Boolean status);

	@Query(" from GisFileLogEntity where uploadedDate=:uploadDate and bitStatus=false ")
	List<GisFileLogEntity> getFileLogsByDate(Date uploadDate);


}
