package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmpl.wrsis.webportal.entity.ProcessEntity;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Integer> {

	List<ProcessEntity> findByBitstatusOrderByProcessNameAsc(boolean b);

}
