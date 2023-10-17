package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
@Repository
public interface UserResearchCenterMappingRepository extends JpaRepository<UserResearchCenterMapping, Integer>{

	@Query(value = "select * FROM wrsis.m_wr_user_rc_mapping  t where t.int_user_id=?1 AND t.bitStatus='false'",nativeQuery = true)
	UserResearchCenterMapping findByUserId(Integer userId);

	List<UserResearchCenterMapping> findRecearchCenterIdByRecearchCenterId(int researchCenterBeanId);
}
