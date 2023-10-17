package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.WheatMaster;

@Repository
public interface WheatMasterRepository extends JpaRepository<WheatMaster, Integer> {

	@Query("From WheatMaster where wheatTypeId=:id")
	WheatMaster getWheatMasterById(@Param("id") Integer wheatTypeId);

	@Query("From WheatMaster f ORDER BY f.wheatTypeId")
	List<WheatMaster> viewAllWheatType();

	@Query("From WheatMaster w order by w.wheatTypeId desc")
	Page<WheatMaster> viewAllWheatPage(Pageable pageable);

	Page<WheatMaster> findByWheatNameIgnoreCaseAndStatus(String wheatName, boolean status, Pageable pageable);

	Page<WheatMaster> findByWheatNameIgnoreCaseContaining(String wheatName, Pageable pageable);

	Page<WheatMaster> findByStatusOrderByWheatTypeIdDesc(boolean status, Pageable pageable);

	Page<WheatMaster> findByWheatNameIgnoreCaseContainingAndStatus(String wheatName, boolean status, Pageable pageable);

	@Query("FROM WheatMaster where status = false ORDER BY description")
	List<WheatMaster> fetchAllActiveWheattypes();

	@Query(nativeQuery = true, value = " SELECT wtype.int_wheat_type_id,wtype.vch_wheat_type FROM wrsis.m_wr_wheat_type wtype where bitstatus = false ORDER BY wtype.vch_wheat_type ")
	public List<Object[]> fetchAllWheattypes();

	@Query("select wheat.wheatName from WheatMaster wheat where wheat.wheatTypeId!=?1 and LOWER(wheat.wheatName) LIKE LOWER(?2)")
	List<Object[]> getWheatTypes(int wheatTypeId, String wheatName);

	@Query("select wheat.wheatName from WheatMaster wheat where LOWER(wheat.wheatName) LIKE LOWER(?1)")
	List<Object[]> getWheatTypes(String wheatName);
}
