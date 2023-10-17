package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.UserLocationDetails;
@Repository
public interface UserLocationDetailsRepository extends JpaRepository<UserLocationDetails, Integer>{

	@Query(value="select * from wrsis.t_wr_user_location_details ul where  ul.int_user_loc_tag_id=?1 and bitstatus='false'",nativeQuery=true)
	List<UserLocationDetails> findBylocationTagId(Integer locationTagId);
	
	
	@Query(value="select * from wrsis.t_wr_user_location_details ul where  ul.int_user_loc_tag_id=?1 and int_demography_id=?2 and bitstatus='false'",nativeQuery=true)
	UserLocationDetails findBylocationTagIdAndDemograpgyId(Integer locationTagId,Integer demographyId);

	@Query(value="select * from wrsis.t_wr_user_location_details ul where  ul.int_user_loc_tag_id=?2 and int_demography_id=?1",nativeQuery=true)
	Object[] findByDemographyIdAndlocationTagId(Integer regionId, Integer locationTagId);
	
	@Query(value="select * from wrsis.t_wr_user_location_details ul where  ul.int_user_loc_tag_id=?2 and int_demography_id=?1",nativeQuery=true)
	UserLocationDetails findByDemographyIdAndLocationTagId(Integer regionId, Integer locationTagId);
}
