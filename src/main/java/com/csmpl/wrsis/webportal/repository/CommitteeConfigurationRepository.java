package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.CommitteeConfigurationEntity;

public interface CommitteeConfigurationRepository extends JpaRepository<CommitteeConfigurationEntity, Integer> {

	@Query(nativeQuery = true, value = "select usr.intuserid,usr.vchusername,usr.vchuserfullname from wrsis.t_user usr"
			+ "inner join wrsis.m_adm_user_role usrol on usr.intuserid=usrol.int_user_id"
			+ "where usrol.int_role_id =?1 and usr.intleveldetailid =?2 and usr.tintdeletedstatus=false")
	List<Object[]> getUserByRoleAndOrgId(int roleId, int orgId);

	@Query(nativeQuery = true, value = "select comm.int_committee_id,comm.vch_committee_name,comm.int_role_id,role.role_name,comm.vch_desc "
			+ "from wrsis.m_adm_role role " + "inner join wrsis.m_wr_committee comm on role.role_id=comm.int_role_id")
	List<Object[]> getCommitteeDetails();

	@Query(nativeQuery = true, value = "select org.nvchlevelname,usr.vchuserfullname, STRING_AGG (rol.alias_name,',') "
			+ "from wrsis.m_wr_committee_member comm  "
			+ "inner join wrsis.t_user usr on  comm.int_user_id=usr.intuserid "
			+ "inner join wrsis.m_adm_user_role usrol on usr.intuserid=usrol.int_user_id "
			+ "inner join wrsis.m_adm_role rol on usrol.int_role_id=rol.role_id "
			+ "inner join wrsis.m_adm_leveldetails org on usr.intleveldetailid=org.intleveldetailid "
			+ "where comm.int_committee_id =?1 and comm.bitstatus=false "
			+ "group by org.nvchlevelname,usr.vchuserfullname,usr.intuserid")
	List<Object[]> getMemberDetailsByComId(int committeeId);

	@Query(nativeQuery = true, value = "select comm.vch_committee_name,comm.int_role_id,role.role_name,comm.vch_desc,comm.int_committee_id "
			+ "from wrsis.m_adm_role role " + "inner join wrsis.m_wr_committee comm on role.role_id=comm.int_role_id "
			+ "where comm.int_committee_id=?1")
	List<Object[]> getCommitteeDetailsByComId(int committeId);

	@Query(nativeQuery = true, value = "select comm.int_user_id,usr.vchuserfullname as Member_Name ,usr.vchusername "
			+ "from wrsis.m_wr_committee_member comm  "
			+ "inner join wrsis.t_user usr on  comm.int_user_id=usr.intuserid "
			+ "where comm.int_committee_id =?1 and comm.bitstatus=false")
	List<Object[]> getMembersByComId(@Param("committeeId") Integer committeeId);

	@Query(nativeQuery = true, value = "select comm.int_user_id " + "from wrsis.m_wr_committee_member comm  "
			+ "inner join wrsis.t_user usr on  comm.int_user_id=usr.intuserid "
			+ "where comm.int_committee_id =?1 and comm.bitstatus=false")
	List<Integer> getMembersByCID(int committeeId);
}
