package com.csmpl.adminconsole.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.adminconsole.webportal.entity.PrimaryLink;

@Repository("primaryLnkRepository")
public interface PrimaryLinkRepository extends JpaRepository<PrimaryLink, Integer> {

	

	@Query(nativeQuery = true, value = " select * from  wrsis.m_adm_primarylink pml,wrsis.m_adm_grouplinkaccess glac, wrsis.m_adm_user_role urol "
			+ " where urol.int_role_id=glac.intgroupid and glac.intplinkid=pml.intplinkid and "
			+ " urol.int_user_id=:userId and glac.bitStatus=false and pml.bitStatus=false ")
	List<PrimaryLink> findUserPrimaryLinkById(@Param("userId") int userId);

	List<PrimaryLink> findGlobalLinkIdByGlobalLinkId(Integer gl_id);

	List<PrimaryLink> findFunctionIdByFunctionId(Integer functn_id);

	

}
