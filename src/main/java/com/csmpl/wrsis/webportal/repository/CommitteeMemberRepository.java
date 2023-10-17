package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.CommitteeMemberEntity;

public interface CommitteeMemberRepository extends JpaRepository<CommitteeMemberEntity, Integer> {

	@Query("select com from CommitteeConfigurationEntity com where com.committeeId !=?1 and com.committeName =?2")
	List<Object[]> doesCommitteeNameDataExist(int committeeId, String committeeName);

	List<CommitteeMemberEntity> findByCommitteeId(int committeeId);

	@Query(nativeQuery = true, value = "select comm.int_user_id from wrsis.m_wr_committee_member comm "
			+ "inner join wrsis.t_user usr on  comm.int_user_id=usr.intuserid " + "where comm.int_committee_id =?1")
	List<Integer> getMembersIdbyCommitteeId(int committeeId);

	CommitteeMemberEntity findByUserId(int i);

	@Query(nativeQuery = true, value = "select comm.int_user_id,usr.vchuserfullname ,usr.vchusername "
			+ "from wrsis.m_wr_committee_member comm  "
			+ "inner join wrsis.t_user usr on  comm.int_user_id=usr.intuserid "
			+ "where comm.int_committee_id =?1 and comm.bitstatus=false")
	List<Object[]> getAllMemberIds(@Param("committeeId") int committeeId);

	List<CommitteeMemberEntity> findByUserIdAndCommitteeId(int userId, int committeeId);

}
