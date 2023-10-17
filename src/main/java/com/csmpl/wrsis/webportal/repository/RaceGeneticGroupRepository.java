package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.RaceGeneticGroupEntity;

@Repository
public interface RaceGeneticGroupRepository extends JpaRepository<RaceGeneticGroupEntity, Integer> {

	List<RaceGeneticGroupEntity> findByStatusOrderByGeneticGroupId(boolean b);

}
