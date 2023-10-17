package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.MonitorFungicideEntity;

public interface MonitorFungicideRepository extends JpaRepository<MonitorFungicideEntity, Integer> {

	@Query(nativeQuery = true, value = "select fungitype.vch_fungicide,mfungicide.vch_fungicide_used  from wrsis.t_wr_monitor_fungicide mfungicide \r\n"
			+ "inner join wrsis.t_wr_monitor_details mdetails on mfungicide.int_monitor_id = mdetails.int_monitor_id\r\n"
			+ "inner join wrsis.m_wr_fungicide_type fungitype on mfungicide.int_fungicide_type_id = fungitype.int_fungicide_type_id\r\n"
			+ "where mfungicide.int_monitor_id=:monitordtlid and mfungicide.bitstatus='false' and mdetails.bitstatus='false' and fungitype.bitstatus='false' ")
	List<Object[]> findFungicideDetailsByMonitorid(@Param("monitordtlid") Integer monitordtlid);

	@Transactional
	@Modifying
	@Query("DELETE FROM MonitorFungicideEntity where monitorid=:monitorid")
	void deleteByMonitorid(@Param("monitorid") Integer monitorid);

	List<MonitorFungicideEntity> findByMonitorid(Integer monitorId);

	List<MonitorFungicideEntity> findFungicideidByFungicideid(int fungicideId);

}
