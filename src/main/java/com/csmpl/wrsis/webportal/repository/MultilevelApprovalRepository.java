package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.MultilevelApprovalEntity;

public interface MultilevelApprovalRepository extends JpaRepository<MultilevelApprovalEntity, Integer> {

	@Query(nativeQuery = true, value = " select appauth.int_role_id,appauth.int_primary_auth_id,appauth.int_time_line,appauth.vch_auth_action_ids from wrsis.m_wr_approval_authority appauth "
			+ " where appauth.bitstatus = 'false' and appauth.int_approval_process_id =:processId ")
	List<Object[]> viewApprovalAuthorityDetails(@Param("processId") Integer processId);

	MultilevelApprovalEntity findByMultiapprovalIdAndBitStatus(int multiapprovalId, boolean b);

	@Transactional
	@Modifying
	@Query("DELETE FROM MultilevelApprovalEntity where processId = ?1")
	void removeByProcessId(int parseInt);

	MultilevelApprovalEntity findByProcessIdAndBitStatus(Integer processId, boolean b);

}
