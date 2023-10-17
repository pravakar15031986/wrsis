package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.MoistureMasterEntity;

public interface MoistureRepository extends JpaRepository<MoistureMasterEntity, Integer> {

	@Query("FROM MoistureMasterEntity where bitStatus = false ORDER BY vchMoisture")
	List<MoistureMasterEntity> fetchAllActiveMoistures();

	@Query(nativeQuery = true, value = " SELECT mois.int_moisture_id,mois.vch_moisture FROM wrsis.m_wr_moisture mois where bitstatus = false ORDER BY mois.vch_moisture ")
	public List<Object[]> fetchAllMoistures();

}
