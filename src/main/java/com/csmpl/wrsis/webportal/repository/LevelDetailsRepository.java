package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.adminconsole.webportal.entity.LevelDetails;

public interface LevelDetailsRepository extends JpaRepository<LevelDetails, Integer> {

	@Query(nativeQuery = true, value = "select rcen.int_research_center_id,rcen.vch_research_center_name from wrsis.m_wr_user_rc_mapping rcmap inner join wrsis.m_wr_research_center rcen on rcmap.int_research_center_id = rcen.int_research_center_id where rcmap.int_user_id = :userId")
	List<Object[]> getInstituonName(@Param("userId") Integer userId);

	@Query(value = "select rle.int_user_id,(select vchuserfullname from wrsis.t_user where intuserid=rle.int_user_id ) from\r\n"
			+ "wrsis.m_wr_user_rc_mapping rcmap inner join wrsis.m_adm_user_role rle on   rcmap.int_user_id=rle.int_user_id \r\n"
			+ "where int_research_center_id =:researchCenterId and rle.int_role_id=8", nativeQuery = true)
	List<Object[]> getAllSurveyor(@Param("researchCenterId") Integer researchCenterId);

	@Query(nativeQuery = true, value = "select rcen.int_research_center_id,rcen.vch_research_center_name from wrsis.m_wr_user_rc_mapping rcmap inner join wrsis.m_wr_research_center rcen on rcmap.int_research_center_id = rcen.int_research_center_id")
	List<Object[]> getInstituonName();
}
