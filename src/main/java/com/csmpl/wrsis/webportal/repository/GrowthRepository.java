package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;

public interface GrowthRepository extends JpaRepository<GrowthMasterEntity, Integer> {

	@Query("FROM GrowthMasterEntity where bitStatus = false ORDER BY vchStage")
	List<GrowthMasterEntity> fetchAllActiveGrowth();

}
