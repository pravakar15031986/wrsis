package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.MapEntity;

public interface MapRepository extends JpaRepository<MapEntity, Integer> {

	@Query("FROM MapEntity where status = false ORDER BY mapName")
	List<MapEntity> fetchAllActiveMapType();

}
