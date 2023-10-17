package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.SeedSourceEntity;

@Repository
public interface SeedSourceRepository extends JpaRepository<SeedSourceEntity, Integer>{

	@Query("from SeedSourceEntity ss where ss.status=false order by seedSrc")
	List<SeedSourceEntity> retriveAllSeedSrcByStatus();

	
}
