package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.Month;

@Repository
public interface MonthRepository extends JpaRepository<Month, Integer> {

	@Query("From Month m order by m.monthId")
	List<Month> viewMonth();

	Month findByMonthId(Integer monthId);
}
