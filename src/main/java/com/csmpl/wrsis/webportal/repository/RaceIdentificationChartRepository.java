package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.RaceIdentificationChart;

@Repository
public interface RaceIdentificationChartRepository extends JpaRepository<RaceIdentificationChart, Integer> {

	RaceIdentificationChart findByRaceChartId(Integer raceChartId);

	@Query("From RaceIdentificationChart r order by r.raceChartId")
	List<RaceIdentificationChart> viewAllRaceChart();

	@Query("From RaceIdentificationChart r where r.status=false  order by r.raceChartId")//Modify by debendra date-30-07-2021 view all active status
	Page<RaceIdentificationChart> viewAllRaceChartByPage(Pageable pageable);

	Page<RaceIdentificationChart> findByStatus(boolean status, Pageable pageable);

	Page<RaceIdentificationChart> findByNameResultContainingIgnoreCase(String nameResult, Pageable pageable);

	Page<RaceIdentificationChart> findByNameResultContainingIgnoreCaseAndStatus(String nameResult, boolean status,
			Pageable pageable);

	RaceIdentificationChart findBynameResultContainingIgnoreCase(String chartName);
}
