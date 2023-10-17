package com.csmpl.wrsis.webportal.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.SeverityEntity;

public interface SeverityRepository extends JpaRepository<SeverityEntity, Integer>{
	
	@Query(nativeQuery = true,value =" select int_severity_id,CONCAT(vch_severity_type,'(',vch_desc,')') as severitytype from wrsis.m_wr_severity  where bitstatus = false ORDER BY vch_severity_type " )
	List<Object[]> fetchAllSeverity();

	@Query(nativeQuery = true,value ="select sev1.vch_severity_type from wrsis.m_wr_severity as sev1 where " + 
			"CAST (sev1.vch_severity_value AS NUMERIC)  >= CAST (:severity AS NUMERIC)  ORDER by " + 
			"CAST (sev1.vch_severity_value AS NUMERIC) ASC  limit 1")
	String getIncidentName(@Param("severity") String severity);

}
