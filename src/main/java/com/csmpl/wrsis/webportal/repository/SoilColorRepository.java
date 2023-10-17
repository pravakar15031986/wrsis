package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.SoilColorMasterEntity;

public interface SoilColorRepository extends JpaRepository<SoilColorMasterEntity, Integer> {

	@Query("FROM SoilColorMasterEntity where bitStatus = false ORDER BY vchSoilColor")
	List<SoilColorMasterEntity> fetchAllSoilActiveColors();

	@Query(nativeQuery = true, value = " SELECT soicol.int_soil_color_id,soicol.vch_soil_color FROM wrsis.m_wr_soil_color soicol where bitstatus = false ORDER BY soicol.vch_soil_color ")
	public List<Object[]> fetchAllSoilColors();

}
