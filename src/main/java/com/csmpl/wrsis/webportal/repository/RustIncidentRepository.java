package com.csmpl.wrsis.webportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.RustIncidentEntity;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;

public interface RustIncidentRepository extends JpaRepository<RustIncidentEntity, Integer>{
    
	@Query("Select max(incidentId) from RustIncidentEntity ")
	String getMaxSlNo();
	
	@Query("from RustIncidentEntity t order by incidentId")
	List<RustIncidentEntity> viewAllIncident();
	
	@Query("from RustIncidentEntity where rustDate between :startDate AND :endDate") 
	List<RustIncidentEntity> findByRustDate(Date startDate,Date endDate);
 
	@Query(value="SELECT int_incident_id, int_user_id, dt_incident_date, bitstatus, intcreatedby, dtmcreatedon, intupdatedby, dtmupdatedon, int_demography_id, int_season_id, vch_year, vch_longitude, vch_latitude\r\n" + 
			"	FROM wrsis.t_wr_rust_incident WHERE int_season_id=?1",nativeQuery=true)
	List<RustIncidentEntity> searchIncidentBySeason(Integer seasonId);

	@Query(" from RustIncidentEntity incident where incident.incidentId=:incidentId")
	RustIncidentEntity findByIncidentId(@Param("incidentId") Integer incidentId);

	@Query(nativeQuery=true,value = "SELECT * FROM wrsis.sp_wr_rust_incident_by_user( ?1,?2,?3)")
	List<Object[]> findByRustDateAndUserId(String startDate, String endDate, int userId);

	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="update wrsis.t_wr_rust_incident set geom_lat_long=st_SetSrid(st_MakePoint(cast(vch_longitude as float), cast(vch_latitude as float)), 4326) where  int_incident_id= ?1")
	void updateGeoLocation(Integer incidentId);
	
	
	@Query(value="select * from wrsis.sp_wr_view_all_rust_incidend(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10);",nativeQuery=true)
	List<Object[]> getRustIncidendDetails(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			Integer yearId, Integer seasonTypeId,String startDate,String endDate,Integer start,Integer length);
	
	
	@Query(value="select count(*) from wrsis.sp_wr_view_all_rust_incidend(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10);",nativeQuery=true)
	Integer getRustIncidendDetailsCount(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			Integer yearId, Integer seasonTypeId,String startDate,String endDate,Integer start,Integer length);


	@Query(value= " select count(*) from wrsis.t_wr_rust_incident where int_user_id =:userId and int_demography_id=:demographyId and int_season_id =:seasonId and vch_year=:year and bitStatus=false ",nativeQuery=true)
	Integer getRustIncidentCount(@Param("userId") int userId,@Param("demographyId") Integer demographyId,@Param("seasonId") Integer seasonId,@Param("year") String year);

	List<RustIncidentEntity> findByYearAndSeasonIdAndDemographyIdAndUserId(String yearFromDate, SeasionMasterEntity s1,
			DemographicEntity d1, int userId);
	
	
	
	
}
