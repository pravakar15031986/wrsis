package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;

@Repository
public interface ApprovalAuthorityEntityRepository extends JpaRepository<ApprovalAuthorityEntity, Integer> {

	@Query(nativeQuery = true, value = "select cast(apauth.int_aprov_auth_id as INTEGER),cast(apauth.int_stage_no as INTEGER),\r\n"
			+ "CASE\r\n" + "      WHEN apauth.int_delegate_status = true  THEN\r\n"
			+ "      cast((SELECT a.int_delegate_to  from wrsis.t_wr_delegation as  a where a.int_aprov_auth_id=apauth.int_aprov_auth_id and a.bit_active_status =true) as INTEGER)\r\n"
			+ "      ELSE\r\n" + "    cast(apauth.int_primary_auth_id as INTEGER)\r\n" + "END\r\n" + "\r\n" + "\r\n"
			+ "from wrsis.m_wr_approval_authority apauth\r\n"
			+ "where apauth.int_approval_process_id = :id and apauth.int_stage_no =1 and apauth.bitstatus = false;")
	List<Object[]> fetchApproveDetails(@Param("id") int i);

	@Query(value = "select * from wrsis.t_wr_approval where int_application_id=?1 and int_approval_process_id=?2 and bitstatus='false'", nativeQuery = true)
	List<ApprovalAuthorityEntity> findApprovalAuthorityDetailsBySurveyId(Integer surveyId, Integer processId);

	@Query(value = "select * from wrsis.t_wr_approval a where a.int_application_id=?1 and a.int_approval_process_id=?2 and a.bitstatus='false'", nativeQuery = true)
	ApprovalAuthorityEntity getDataByAppIdAndProcessId(Integer appId, Integer processId);
}
