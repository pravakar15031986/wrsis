package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ApprovalProcessEntity;

@Repository("ApprovalProcessRepository")
public interface ApprovalProcessRepository extends JpaRepository<ApprovalProcessEntity, Integer> {

	@Query("select CV FROM ApprovalProcessEntity CV where intDeletedFlag=0 order by vchProcessName")
	List<ApprovalProcessEntity> getApprovalProcess();

	@Query("select usr from ApprovalProcessEntity usr where usr.intProcessId=:processid")
	ApprovalProcessEntity editApproval(@Param("processid") int intProcessId);

	@Query("select usr from ApprovalProcessEntity usr where usr.vchProcessName=?1")
	List<Object[]> isProcessNameDataExist(String processName);

	@Query("select usr from ApprovalProcessEntity usr where usr.intProcessId !=?1 and usr.vchProcessName =?2")
	List<Object[]> isProcessNameDataExistOnupdate(int intProcessId, String vchProcessName);

	@Query("from ApprovalProcessEntity")
	Page<ApprovalProcessEntity> findAllProcess(Pageable pageable);

	@Query(nativeQuery = true, value = "select app.int_approval_process_id,app.vch_process,app.vch_desc,app.bitstatus \r\n"
			+ "from wrsis.m_wr_approval_process app where \r\n" + " app.bitstatus=:condition and \r\n"
			+ "(app.vch_process LIKE %:appProcessName%) \r\n"
			+ "ORDER BY app.int_approval_process_id DESC")
	List<Object[]> viewApprovalProcessList(@Param("condition") boolean condition,
			@Param("appProcessName") String appProcessName);

	@Query(nativeQuery = true, value = "select app.int_approval_process_id,app.vch_process,app.vch_desc,app.bitstatus \r\n"
			+ "from wrsis.m_wr_approval_process app where \r\n"
			+ "(app.vch_process LIKE %:appProcessName%) \r\n"
			+ "ORDER BY app.int_approval_process_id DESC")
	List<Object[]> viewApprovalProcessList(@Param("appProcessName") String appProcessName);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.m_wr_approval_process process \r\n"
			+ "inner join wrsis.m_wr_approval_authority authority on process.int_approval_process_id = authority.int_approval_process_id \r\n"
			+ "where authority.int_approval_process_id=:intProcessId and authority.int_delegate_status=false and authority.bitstatus=false and process.bitstatus=false ")
	Integer isProcessNameConfigureToUser(@Param("intProcessId") int intProcessId);

}
