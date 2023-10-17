package com.csmpl.adminconsole.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;

@Repository
public interface VarietyRepository extends JpaRepository<VarietyTypeMasterEntity, Integer> {

	@Query(" from VarietyTypeMasterEntity v where v.id=:id")
	VarietyTypeMasterEntity findByVarietyId(@Param("id") int id);

}
