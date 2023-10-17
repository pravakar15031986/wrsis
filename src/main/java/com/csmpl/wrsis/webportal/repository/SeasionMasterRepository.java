package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;

public interface SeasionMasterRepository extends JpaRepository<SeasionMasterEntity, Integer>{

	@Query("FROM SeasionMasterEntity where bitStatus = false ORDER BY vchSeason")
	List<SeasionMasterEntity> fetchAllActiveSeasion();

	
	@Query(nativeQuery = true,value=" select sea.int_season_id,sea.vch_season from wrsis.m_wr_season sea where sea.bitstatus='false' ")
	List<Object[]> fetchAllSeasions();

	@Query(nativeQuery = true,value=" select sea.int_season_id,sea.vch_season " + 
			" from wrsis.m_wr_season sea " + 
			" where sea.bitstatus='false' ")
	List<Object[]> fetchAllSeasionsWiseMonth();

	@Query(nativeQuery = true,value=" select ltag.int_level_id " +
			" from wrsis.t_wr_user_location_tag ltag " + 
			" where ltag.bitstatus=false and ltag.int_user_id=:userId ")
	List<Short> fetchAllUserWiseDemography(@Param("userId") int userId);

	@Query(nativeQuery = true,value=" select demog.int_demography_id as kebel_id ,demog.vch_demography_name as kebel_name,demog.int_parent_id as woreda_id from wrsis.m_wr_demography demog where demog.int_parent_id in (select distinct dgrpy.int_parent_id                \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag                       \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id                       \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id  \r\n" + 
			"--inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id            \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false  and ltag.int_user_id=:userId);")
	List<Object[]> fetchAllUserWiseKebel(@Param("userId") int userId);
     
	@Query(nativeQuery = true,value=" select distinct (dgrpy1.int_demography_id) as woreda_id,(dgrpy1.vch_demography_name) as woreda_name,                                               \r\n" + 
			"(dgrpy2.int_demography_id) as zone_id \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag  \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy2 on dgrpy1.int_parent_id=dgrpy2.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy3 on dgrpy2.int_parent_id=dgrpy3.int_demography_id \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false and dgrpy1.bitstatus=false and  \r\n" + 
			"dgrpy2.bitstatus=false and dgrpy3.bitstatus=false and ltag.int_user_id=:userId")
	List<Object[]> fetchAllUserWiseWoreda(@Param("userId") int userId);

	@Query(nativeQuery = true,value=" select distinct (dgrpy2.int_demography_id) as zone_id,(dgrpy2.vch_demography_name) as zone_name, \r\n" + 
			"(dgrpy3.int_demography_id) as region_id \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag  \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy2 on dgrpy1.int_parent_id=dgrpy2.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy3 on dgrpy2.int_parent_id=dgrpy3.int_demography_id \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false and dgrpy1.bitstatus=false and \r\n" + 
			"dgrpy2.bitstatus=false and dgrpy3.bitstatus=false and ltag.int_user_id=:userId ")
	List<Object[]> fetchAllUserWiseZone(@Param("userId") int userId);
	
	@Query(nativeQuery = true,value=" select distinct\r\n" + 
			"(case when ltag.int_level_id = 4 then \r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id )))\r\n" + 
			"when ltag.int_level_id = 5 THEN\r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id =  \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id  ))))\r\n" + 
			"end) regionid,\r\n" + 
			"(case when ltag.int_level_id = 4 then \r\n" + 
			"(select vch_demography_name from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id )))\r\n" + 
			"when ltag.int_level_id = 5 THEN\r\n" + 
			"(select  vch_demography_name from wrsis.m_wr_demography where int_demography_id =  \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id  ))))\r\n" + 
			"end) regionname\r\n" + 
			"from wrsis.t_wr_user_location_tag ltag                                               \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id                                              \r\n" + 
			"where  ltag.int_user_id=:userId and ltag.bitstatus=false and ldtl.bitstatus=false\r\n" + 
			"\r\n" + 
			" ")
	List<Object[]> fetchAllUserWiseRegion(@Param("userId") int userId);
	
	
	@Query(nativeQuery = true,value=" select demog.int_demography_id as kebel_id ,demog.vch_demography_name as kebel_name,demog.int_parent_id as woreda_id from wrsis.m_wr_demography demog where demog.int_parent_id in (select distinct dgrpy.int_parent_id                \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag                       \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id                       \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id  \r\n" + 
			"--inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id            \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false  and ltag.int_user_id=:userId);")
	List<Object[]> fetchAllUserWiseImplementionKebel(@Param("userId") int userId);
	
	@Query(nativeQuery = true,value=" select distinct (dgrpy1.int_demography_id) as woreda_id,(dgrpy1.vch_demography_name) as woreda_name,                                               \r\n" + 
			"(dgrpy2.int_demography_id) as zone_id \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag  \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy2 on dgrpy1.int_parent_id=dgrpy2.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy3 on dgrpy2.int_parent_id=dgrpy3.int_demography_id \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false and dgrpy1.bitstatus=false and  \r\n" + 
			"dgrpy2.bitstatus=false and dgrpy3.bitstatus=false and ltag.int_user_id=:userId")
	List<Object[]> fetchAllUserWiseImplementionWoreda(@Param("userId") int userId);
	
	@Query(nativeQuery = true,value=" select distinct (dgrpy2.int_demography_id) as zone_id,(dgrpy2.vch_demography_name) as zone_name, \r\n" + 
			"(dgrpy3.int_demography_id) as region_id \r\n" + 
			"from wrsis.t_wr_user_location_tag ltag  \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy on ldtl.int_demography_id=dgrpy.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy1 on dgrpy.int_parent_id=dgrpy1.int_demography_id  \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy2 on dgrpy1.int_parent_id=dgrpy2.int_demography_id \r\n" + 
			"inner join wrsis.m_wr_demography dgrpy3 on dgrpy2.int_parent_id=dgrpy3.int_demography_id \r\n" + 
			"where ltag.bitstatus=false and ldtl.bitstatus=false and dgrpy.bitstatus=false and dgrpy1.bitstatus=false and \r\n" + 
			"dgrpy2.bitstatus=false and dgrpy3.bitstatus=false and ltag.int_user_id=:userId ")
	List<Object[]> fetchAllUserWiseImplementionZone(@Param("userId") int userId);
	
	@Query(nativeQuery = true,value=" select distinct\r\n" + 
			"(case when ltag.int_level_id = 4 then \r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id )))\r\n" + 
			"when ltag.int_level_id = 5 THEN\r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id =  \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id  ))))\r\n" + 
			"end) regionid,\r\n" + 
			"(case when ltag.int_level_id = 4 then \r\n" + 
			"(select vch_demography_name from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id )))\r\n" + 
			"when ltag.int_level_id = 5 THEN\r\n" + 
			"(select  vch_demography_name from wrsis.m_wr_demography where int_demography_id =  \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = ldtl.int_demography_id  ))))\r\n" + 
			"end) regionname\r\n" + 
			"from wrsis.t_wr_user_location_tag ltag                                               \r\n" + 
			"inner join wrsis.t_wr_user_location_details ldtl on ltag.int_user_loc_tag_id=ldtl.int_user_loc_tag_id                                              \r\n" + 
			"where  ltag.int_user_id=:userId and ltag.bitstatus=false and ldtl.bitstatus=false\r\n" + 
			"\r\n" + 
			" ")
	List<Object[]> fetchAllUserWiseImplementionRegion(@Param("userId") int userId);

	@Query("from SeasionMasterEntity where bitstatus=false ORDER BY vchSeason")
	List<SeasionMasterEntity> getActiveSeasonList();


	@Query(nativeQuery = true,value=" SELECT cast(array_to_json(array_agg(row_to_json(finaldata))) as text)\r\n" + 
			"FROM\r\n" + 
			"(\r\n" + 
			"select demoregion.int_demography_id regionid,demoregion.vch_demography_name  regionname, \r\n" + 
			"(SELECT array_to_json(array_agg(row_to_json(zone)))\r\n" + 
			"FROM\r\n" + 
			"(select demozone.int_demography_id zoneid,demozone.vch_demography_name zonename,\r\n" + 
			"( SELECT array_to_json(array_agg(row_to_json(woreda)))\r\n" + 
			"FROM\r\n" + 
			"(select demozone.int_demography_id zoneid,demoworeda.int_demography_id woredaid,demoworeda.vch_demography_name woredaname,\r\n" + 
			"( SELECT array_to_json(array_agg(row_to_json(kebele)))\r\n" + 
			"FROM\r\n" + 
			"(select demoworeda.int_demography_id woredaid,demokebel.int_demography_id kebelid,demokebel.vch_demography_name kebelaname from wrsis.m_wr_demography demokebel\r\n" + 
			"where demokebel.int_parent_id =  demoworeda.int_demography_id) as kebele) as kebele\r\n" + 
			"from wrsis.m_wr_demography demoworeda where demoworeda.int_parent_id =  demozone.int_demography_id) as woreda) as woreda\r\n" + 
			"from wrsis.m_wr_demography demozone where int_parent_id =  demoregion.int_demography_id) as zone) as zone\r\n" + 
			"    \r\n" + 
			"from wrsis.m_wr_demography demoregion where demoregion.int_demography_id in ((select \r\n" + 
			"case when (select demog.int_level_id from wrsis.m_wr_demography demog where demog.int_demography_id=usrloc.int_demography_id) = 4 THEN \r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = usrloc.int_demography_id )))\r\n" + 
			"when (select demog.int_level_id from wrsis.m_wr_demography demog where demog.int_demography_id=usrloc.int_demography_id) = 5 THEN\r\n" + 
			"(select int_demography_id from wrsis.m_wr_demography where int_demography_id =  \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = \r\n" + 
			"(select int_parent_id from wrsis.m_wr_demography where int_demography_id = usrloc.int_demography_id  ))))\r\n" + 
			"end regionid\r\n" + 
			"from wrsis.t_wr_user_location_tag loctag\r\n" + 
			"inner join wrsis.t_wr_user_location_details usrloc on loctag.int_user_loc_tag_id = usrloc.int_user_loc_tag_id\r\n" + 
			"where loctag.bitstatus='false' and usrloc.bitstatus='false' and loctag.int_user_id=:userId))\r\n" + 
			"\r\n" + 
			") as finaldata   ")
	String getdemographydata(@Param("userId") int userId);


	@Query(nativeQuery=true,value="SELECT int_month_id FROM wrsis.m_wr_season_month where int_season_id=?1 and bitstatus=false")
	List<Object> getSeasonMonths(Integer season);

    @Query(nativeQuery=true,value="select seamonth.int_month_id,seamonth.vch_month,sea.int_season_id\r\n" + 
    		"from wrsis.m_wr_season sea \r\n" + 
    		"inner join wrsis.m_wr_season_month seamonth on sea.int_season_id = seamonth.int_season_id \r\n" + 
    		"where sea.bitstatus='false' and seamonth.bitstatus='false' order by seamonth.int_month_id asc")
	List<Object[]> getMonthByseasonId();


	@Query(nativeQuery=true,value="SELECT count(0) FROM wrsis.m_wr_season_month where int_season_id=?1 and int_month_id=?2")
	Integer getSurveyAndSeasonMapCount(Integer seasonId, Integer monthId);

	@Query(nativeQuery=true,value=" select extract(year FROM CURRENT_DATE\\:\\:timestamp) as cur_year,season.int_season_id,season.vch_season\r\n" + 
			"            from wrsis.m_wr_season season  \r\n" + 
			"            where  season.int_season_id =(select distinct int_season_id from wrsis.m_wr_season_month where bitstatus=false \r\n" + 
			"            and  int_month_id = (SELECT extract(month FROM CURRENT_DATE\\:\\:timestamp))) ")
	
	List<Object[]> fetchCurrentYearWiseSeason();


	@Query(nativeQuery=true,value="select cast((select row_to_json(jsond) from  \r\n" + 
			"(\r\n" + 
			"select (select distinct int_season_id from wrsis.m_wr_season_month where bitstatus='false' and int_month_id = (SELECT extract(month FROM CURRENT_DATE\\:\\:timestamp))) as seasonid,\r\n" + 
			"(SELECT extract(year FROM CURRENT_DATE\\:\\:timestamp)) as year\r\n" + 
			"    ) jsond) as text)")
	String getCurrentYearAndSeason();
}
