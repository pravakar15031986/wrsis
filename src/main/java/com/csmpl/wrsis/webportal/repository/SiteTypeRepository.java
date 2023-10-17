package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.SiteTypeMasterEntity;

public interface SiteTypeRepository extends JpaRepository<SiteTypeMasterEntity, Integer>{

	@Query("FROM SiteTypeMasterEntity where bitStatus = false ORDER BY vchSiteName")
	List<SiteTypeMasterEntity> fetchAllActiveSitetypes();
	
	
	@Query(nativeQuery = true,value =" SELECT sursit.int_site_type_id,sursit.vch_site_name FROM wrsis.m_wr_survey_site_type sursit where bitStatus = false ORDER BY sursit.vch_site_name " )
	public List<Object[]> fetchAllSitetypes();

}
