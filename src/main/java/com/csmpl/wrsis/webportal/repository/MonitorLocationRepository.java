package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.MonitorLocationEntity;

public interface MonitorLocationRepository extends JpaRepository<MonitorLocationEntity, Integer> {

	List<MonitorLocationEntity> findByMonitorid(Integer monitorId);

	@Transactional
	@Modifying
	@Query("DELETE FROM MonitorLocationEntity where monitorid=:monitorid")
	void deleteByMonitorid(@Param("monitorid") Integer monitorid);

}
