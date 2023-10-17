package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;

public interface RustTypeRepository extends JpaRepository<RustTypeMasterEntity, Integer>{

	@Query("FROM RustTypeMasterEntity where bitStatus = false ORDER BY vchRustType")
	List<RustTypeMasterEntity> fetchAllActiveRustType();

}
