package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.WheatTypeMasterEntity;

public interface WheatTypeRepository extends JpaRepository<WheatTypeMasterEntity, Integer>{

	@Query("FROM WheatTypeMasterEntity where bitStatus = false ORDER BY vchDesc")
	List<WheatTypeMasterEntity> fetchAllActiveWheattypes();

}
