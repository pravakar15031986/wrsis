package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.DemographicLocationTagEntity;

@Repository
public interface DemographicTaggingRepository extends JpaRepository<DemographicLocationTagEntity, Integer> {

	@Query(value = "select tag.int_user_loc_tag_id as tagId,tag.int_user_id,tag.int_level_id as LevelId,tag.bitstatus,"
			+ " STRING_AGG(CAST(ul.int_loc_id AS VARCHAR),',') as userLocationId,STRING_AGG(CAST(ul.int_demography_id AS VARCHAR),',') as demographyId"
			+ " from wrsis.t_wr_user_location_tag tag"
			+ " INNER JOIN wrsis.t_wr_user_location_details ul on tag.int_user_loc_tag_id=ul.int_user_loc_tag_id"
			+ " group by tag.int_user_loc_tag_id,tag.int_user_id,tag.int_level_id order by tag.dtmcreatedon ", nativeQuery = true)
	Page<Object[]> viewAllLocationTag(Pageable pageable);

	@Query(value = "select tag.int_user_loc_tag_id as tagId,tag.int_user_id,tag.int_level_id as LevelId,tag.bitstatus,\r\n"
			+ " STRING_AGG(CAST(ul.int_loc_id AS VARCHAR),',') as userLocationId,STRING_AGG(CAST(ul.int_demography_id AS VARCHAR),',') as demographyId  \r\n"
			+ " from wrsis.t_wr_user_location_tag tag\r\n"
			+ " INNER JOIN wrsis.t_wr_user_location_details ul on tag.int_user_loc_tag_id=ul.int_user_loc_tag_id \r\n"
			+ " where  tag.int_user_id=?1  and ul.bitstatus='false'\r\n"
			+ " group by tag.int_user_loc_tag_id,tag.int_user_id,tag.int_level_id order by tag.dtmcreatedon", nativeQuery = true)
	Page<Object[]> searchAllLocationTagByOrganizationName(Pageable pageable, Integer orgId);

	@Query(value = "select tag.int_user_loc_tag_id as tagId,tag.int_user_id,tag.int_level_id as LevelId,tag.bitstatus,\r\n"
			+ " STRING_AGG(CAST(ul.int_loc_id AS VARCHAR),',') as userLocationId,STRING_AGG(CAST(ul.int_demography_id AS VARCHAR),',') as demographyId  \r\n"
			+ " from wrsis.t_wr_user_location_tag tag\r\n"
			+ " INNER JOIN wrsis.t_wr_user_location_details ul on tag.int_user_loc_tag_id=ul.int_user_loc_tag_id \r\n"
			+ " where  tag.bitstatus=?1  and ul.bitstatus='false'\r\n"
			+ " group by tag.int_user_loc_tag_id,tag.int_user_id,tag.int_level_id order by tag.dtmcreatedon", nativeQuery = true)
	Page<Object[]> searchAllLocationTagByStatus(Pageable pageable, boolean status);

	@Query(value = "select tag.int_user_loc_tag_id as tagId,tag.int_user_id,tag.int_level_id as LevelId,tag.bitstatus,\r\n"
			+ " STRING_AGG(CAST(ul.int_loc_id AS VARCHAR),',') as userLocationId,STRING_AGG(CAST(ul.int_demography_id AS VARCHAR),',') as demographyId  \r\n"
			+ " from wrsis.t_wr_user_location_tag tag\r\n"
			+ " INNER JOIN wrsis.t_wr_user_location_details ul on tag.int_user_loc_tag_id=ul.int_user_loc_tag_id \r\n"
			+ " where tag.int_user_id=?1 AND tag.bitstatus=?2  AND ul.bitstatus='false'\r\n"
			+ " group by tag.int_user_loc_tag_id,tag.int_user_id,tag.int_level_id order by tag.dtmcreatedon", nativeQuery = true)
	Page<Object[]> searchAllLocationTagByOrganizationNameAndStatus(Pageable pageable, Integer orgId, boolean status);

	@Query(value = "SELECT * FROM wrsis.t_wr_user_location_tag where int_user_id=:userId", nativeQuery = true)
	List<DemographicLocationTagEntity> findAllLocationTagDetailsByUserId(Integer userId);

	@Query(nativeQuery=true, value = "select tag.int_user_loc_tag_id as tagId,tag.int_user_id,tag.int_level_id as LevelId,tag.bitstatus, \r\n" + 
			" STRING_AGG(CAST(ul.int_loc_id AS VARCHAR),',') as userLocationId,STRING_AGG(CAST(ul.int_demography_id AS VARCHAR),',') as demographyId \r\n" + 
			" from wrsis.t_wr_user_location_tag tag \r\n" + 
			" INNER JOIN wrsis.t_wr_user_location_details ul on tag.int_user_loc_tag_id=ul.int_user_loc_tag_id \r\n" + 
			" where tag.int_user_id=:userId \r\n" + 
			" group by tag.int_user_loc_tag_id,tag.int_user_id,tag.int_level_id,tag.bitstatus")
	List<Object[]> getRegionTaggedByUserId(Integer userId);

}
