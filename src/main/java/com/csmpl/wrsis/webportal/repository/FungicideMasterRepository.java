package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.FungicideMaster;

@Repository
public interface FungicideMasterRepository extends JpaRepository<FungicideMaster, Integer> {

	@Query("From FungicideMaster where fungicideId=:id")
	FungicideMaster getFungicideMasterById(@Param("id") Integer fungicideId);

	@Query("From FungicideMaster f ORDER BY f.fungicideId desc")
	List<FungicideMaster> viewAllFungicide();

	@Query("FROM  FungicideMaster d ORDER By d.fungicideId Desc")
	Page<FungicideMaster> viewAllFungicidePage(Pageable pageable);

	Page<FungicideMaster> findByFungicideNameIgnoreCaseAndStatus(String fungicideName, boolean status,
			Pageable pageable);

	Page<FungicideMaster> findByFungicideNameContainingIgnoreCase(String fungicideName, Pageable pageable);

	Page<FungicideMaster> findByStatusOrderByFungicideIdDesc(boolean status, Pageable pageable);

	@Query("From FungicideMaster where status=false ORDER BY fungicideName")
	List<FungicideMaster> fetchAllActiveFungicides();

	@Query(nativeQuery = true, value = " SELECT fungi.int_fungicide_type_id,fungi.vch_fungicide FROM wrsis.m_wr_fungicide_type fungi where bitstatus = false ORDER BY fungi.vch_fungicide ")
	public List<Object[]> fetchAllFungicides();

	@Query("select fung.fungicideName from FungicideMaster fung where LOWER(fung.fungicideName) LIKE LOWER(?1)")
	List<Object[]> getFungNames(String fungicideName);

	@Query("select fung.fungicideName from FungicideMaster fung where fung.fungicideId!=?1 and LOWER(fung.fungicideName) LIKE LOWER(?2)")
	List<Object[]> getFungNames(int fungicideId, String fungicideName);

}
