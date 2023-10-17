package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.RecommendationForwardLogEntity;

@Repository
public interface RecommendationForwardLogRepository extends JpaRepository<RecommendationForwardLogEntity, Short> {

	
}
