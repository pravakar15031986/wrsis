package com.csmpl.wrsis.webportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.RaceGroupEntity;

@Repository
public interface RaceGroupRepository extends JpaRepository<RaceGroupEntity, Integer> {

	@Query("select race.maxValue from RaceGroupEntity race where race.rustTypeId=?1 and race.status=false")
	Integer getRustMaxValue(Integer rustTypeId);

}
