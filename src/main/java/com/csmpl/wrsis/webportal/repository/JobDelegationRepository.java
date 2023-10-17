package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.JobDelegationEntity;

public interface JobDelegationRepository extends JpaRepository<JobDelegationEntity, Integer> {

	@Query(nativeQuery = true, value = " SELECT DISTINCT  apppro.int_approval_process_id,apppro.vch_process FROM wrsis.m_wr_approval_authority  appaut inner join wrsis.m_wr_approval_process apppro on appaut.int_approval_process_id = apppro.int_approval_process_id "
			+ " where appaut.bitstatus = 'false' and apppro.bitstatus = 'false' and appaut.int_primary_auth_id=:userId ")

	List<Object[]> findProcessByUserId(@Param("userId") int userId);

	@Query(nativeQuery = true, value = "SELECT appaut.int_aprov_auth_id,appaut.int_stage_no  "
			+ " FROM wrsis.m_wr_approval_authority  appaut where appaut.bitstatus = 'false'  "
			+ " and appaut.int_approval_process_id=:processId " + " and appaut.int_primary_auth_id=:userId "
			+ " and appaut.int_aprov_auth_id not in (select job.int_aprov_auth_id from wrsis.t_wr_delegation job "
			+ " where ((job.bit_active_status is null) or (job.bit_active_status=true) ) and job.bitstatus=false) ")
	List<Object[]> getAuthorityStatus(@Param("processId") Integer processId, @Param("userId") Integer userId);

	@Query(nativeQuery = true, value = " select process.vch_process,auth.int_stage_no,dele.dtm_from_date,dele.dtm_to_date,"
			+ " usr.vchuserfullname as approvalBy,ur.vchuserfullname as approvalTo,dele.vch_remarks,dele.bitstatus,"
			+ " dele.int_delegation_id,auth.int_aprov_auth_id,dele.bit_active_status "
			+ " from wrsis.t_wr_delegation dele inner join wrsis.m_wr_approval_authority auth on dele.int_aprov_auth_id = auth.int_aprov_auth_id "
			+ " inner join wrsis.m_wr_approval_process process on auth.int_approval_process_id = process.int_approval_process_id "
			+ " inner  join wrsis.t_user usr on dele.intcreatedby = usr.intuserid "
			+ " inner  join wrsis.t_user ur on dele.int_delegate_to = ur.intuserid  "
			+ " where dele.bitstatus = false and dele.intcreatedby =:userId  order by dele.dtm_from_date desc")
	List<Object[]> viewJobDeligation(@Param("userId") int userId);

	@Query(nativeQuery = true, value = "  select dele.int_delegation_id ,auth.int_stage_no,dele.dtm_from_date,dele.dtm_to_date,dele.int_delegate_to,dele.vch_remarks,dele.bitstatus,process.int_approval_process_id  "
			+ " from wrsis.t_wr_delegation dele inner join wrsis.m_wr_approval_authority auth on dele.int_aprov_auth_id = auth.int_aprov_auth_id  "
			+ " inner join wrsis.m_wr_approval_process process on auth.int_approval_process_id = process.int_approval_process_id  "
			+ " where dele.bitstatus = 'false' and dele.int_delegation_id =:delegationId and  auth.int_aprov_auth_id=:multiapprovalId ")
	List<Object[]> editJobDeligation(@Param("delegationId") int delegationId,
			@Param("multiapprovalId") int multiapprovalId);

	JobDelegationEntity findByDelegationId(int delegationId);

	List<JobDelegationEntity> findByActiveStatus(boolean activeStatus);

	List<JobDelegationEntity> findByActiveStatusAndBitStatus(Object nullValue, boolean deleteStatus);

	@Query(nativeQuery = true, value = "SELECT appaut.int_aprov_auth_id,appaut.int_stage_no   FROM wrsis.m_wr_approval_authority  appaut where appaut.bitstatus = 'false'  and appaut.int_approval_process_id=:processId "
			+ " and appaut.int_primary_auth_id=:userId ")
	List<Object[]> getAuthorityStatusEdit(@Param("processId") Integer processId, @Param("userId") Integer userId);

	
	@Query(nativeQuery = true, value = "SELECT appaut.int_aprov_auth_id,appaut.int_stage_no  "
			+ " FROM wrsis.m_wr_approval_authority  appaut where appaut.bitstatus = 'false'  "
			+ " and appaut.int_approval_process_id=:processId " + " and appaut.int_primary_auth_id=:userId "
			+ " and appaut.int_aprov_auth_id in (select job.int_aprov_auth_id from wrsis.t_wr_delegation job "
			+ " where ((job.bit_active_status is null) or (job.bit_active_status=true) ) and job.bitstatus=false) ")
	List<Object[]> isJobDelegationForProcess(@Param("processId") Integer processId, @Param("userId") Integer userId);


}
