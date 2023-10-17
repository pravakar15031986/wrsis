package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.RustIncidentDetailsEntity;

public interface RustIncidentDetailsRepository extends JpaRepository<RustIncidentDetailsEntity, Integer>{

		
	@Query(value=" SELECT incd.int_incident_dtl_id, incd.int_incident_id, incd.int_question_id,\r\n" + 
			" incd.vch_option_value, incd.bitstatus ,quesop.vch_option_info,q.vch_question\r\n" + 
			" FROM wrsis.t_wr_rust_incident_details incd\r\n" + 
			" inner join  wrsis.m_wr_question q on  incd.int_question_id=q.int_question_id\r\n" + 
			" inner join wrsis.m_wr_question_option quesop on incd.int_question_id=quesop.int_question_id\r\n" + 
			" where incd.vch_option_value=quesop.vch_option_value and incd.int_incident_id=?1\r\n" + 
			" and incd.bitstatus='false' and quesop.bitstatus='false'",nativeQuery=true)
	List<Object[]> viewIncidentDetailsByIncidentId(@Param("incidentId")Integer incidentId);
    
	@Query(value="select * from wrsis.sp_wr_view_rustincident(?1,?2,?3,?4)",nativeQuery=true)
	List<Object[]> viewRustIncident(Integer seasonId,Integer userId,Integer demographyId,String year);

	@Query(nativeQuery=true,value="select incidtl.int_incident_dtl_id,incidtl.int_question_id,incidtl.vch_option_value,question.vch_question,question.int_ques_order  from wrsis.t_wr_rust_incident_details incidtl "
			+ " left join wrsis.m_wr_question  question  on question.int_question_id = incidtl.int_question_id "
			+ "where incidtl.int_incident_id=:incidentId and incidtl.bitstatus='false' and question.bitstatus='false' order by question.int_ques_order ")//rust incident modify order by int_ques_order
	List<Object[]> findOptionByIncidentId(@Param("incidentId")int incidentId);

	@Query(" from RustIncidentDetailsEntity incidentdtl where incidentdtl.incidentDtlId=:incidtlid")
	RustIncidentDetailsEntity findByIncidentDtlId(@Param("incidtlid")int incidtlid);
	
	@Query(nativeQuery=true,value="select queoption.int_option_no,queoption.vch_option_value,queoption.vch_option_info  from wrsis.m_wr_question_option queoption\r\n" + 
			"where queoption.int_question_id=:questionId and queoption.bitstatus='false'  ")
	List<Object[]> findOptionByQuestionId(@Param("questionId")Integer questionId);

	@Query(nativeQuery=true,value=" select distinct question.int_question_id,question.vch_question,question.int_ques_order\r\n" + 
			"from wrsis.m_wr_question question \r\n" + 
			"inner join wrsis.m_wr_question_option quesoption on question.int_question_id = quesoption.int_question_id\r\n" + 
			"where question.bitstatus=false and int_ques_type=2 and quesoption.bitstatus=false and question.int_question_id  not in(select incidentdtl.int_question_id from wrsis.t_wr_rust_incident_details  incidentdtl\r\n" + 
			"where incidentdtl.bitstatus=false) ")
	List<Object[]> findOptionByNotIncidentId();

	@Query(value="select * from wrsis.sp_wr_gis_rust_incident_mobile(?1,?2)",nativeQuery=true)
	List<Object[]> viewRustIncidentForGis(Integer seasonId,String year);

	@Query(nativeQuery=true,value=" SELECT  cast(rustincidt.int_incident_id as text),cast(TO_CHAR(rustincidt.dt_incident_date, 'dd-Mon-YYYY')  as text)," + 
			" cast(sea.vch_season as text),cast(rustincidt.vch_longitude as text),cast(rustincidt.vch_latitude as text),cast(demo.vch_demography_name as text) " + 
			" FROM  wrsis.t_wr_rust_incident rustincidt inner join wrsis.t_wr_rust_incident_details  incidentdtl  on rustincidt.int_incident_id = incidentdtl.int_incident_id " + 
			" inner join wrsis.m_wr_season sea  on rustincidt.int_season_id =sea.int_season_id " + 
			" inner join wrsis.m_wr_demography demo on rustincidt.int_demography_id = demo.int_demography_id " + 
			" where rustincidt.bitstatus=false and incidentdtl.bitstatus=false and rustincidt.int_incident_id  =?1 "
			+ " GROUP BY rustincidt.int_incident_id,rustincidt.dt_incident_date,sea.vch_season,rustincidt.vch_longitude,rustincidt.vch_latitude,demo.vch_demography_name ")
	List<Object[]> viewRustIncidentByRustIncident(Integer rustincidentId);

	@Query(nativeQuery=true,value="select questionoptn.int_option_no,questionoptn.vch_option_value,questionoptn.vch_option_info " + 
			"from wrsis.t_wr_rust_incident_details incidentdtl " + 
			"inner join wrsis.m_wr_question_option  questionoptn on incidentdtl.int_question_id = questionoptn.int_question_id " + 
			"where incidentdtl.bitstatus=false and questionoptn.bitstatus=false and questionoptn.int_question_id=?1 " + 
			"and questionoptn.vch_option_value=?2   group by questionoptn.int_option_no,questionoptn.vch_option_value,questionoptn.vch_option_info" )
	List<Object[]> findOptionByQuestionIdAndOption(Integer questionId,String questionOption);

	@Query(nativeQuery=true,value="select int_question_id from wrsis.t_wr_rust_incident_details where int_question_id=?1")
	List<Integer> findQuesIdByQuesId(int qustionBeanId);

	List<RustIncidentDetailsEntity> findByIncidentId(Integer incidentId);
}
