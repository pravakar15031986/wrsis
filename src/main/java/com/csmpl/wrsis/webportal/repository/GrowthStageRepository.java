package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.GrowthStage;

@Repository
public interface GrowthStageRepository extends JpaRepository<GrowthStage, Integer> {

	@Query("From GrowthStage where stageId=:id")
	GrowthStage getGrothStageById(@Param("id") Integer stageId);

	@Query("FROM GrowthStage g ORDER BY g.stageId desc")
	List<GrowthStage> viewAllGrothStage();

	@Query("FROM  GrowthStage d ORDER By d.stageId DESC")
	Page<GrowthStage> viewAllGrothStagePage(Pageable pageable);

	Page<GrowthStage> findByStageNameIgnoreCaseAndStatus(String stageName, boolean status, Pageable pageable);

	Page<GrowthStage> findByStageNameContainingIgnoreCase(String stageName, Pageable pageable);

	Page<GrowthStage> findByStatusOrderByStageIdDesc(boolean status, Pageable pageable);

	@Query("FROM GrowthStage where status = false ORDER BY stageName")
	List<GrowthStage> fetchAllActiveGrowth();

	@Query(nativeQuery = true, value = " SELECT grStage.int_cr_gr_stage_id,grStage.vch_stage FROM wrsis.m_wr_cr_growth_stage grStage where bitstatus = false ORDER BY grStage.vch_stage ")
	public List<Object[]> fetchAllGrowth();

	@Query("select gstage.stageName from GrowthStage gstage where gstage.stageId!=?1 and LOWER(gstage.stageName) LIKE LOWER(?2)")
	List<Object[]> getGrowthStage(int stageId, String stageName);

	@Query("select gstage.stageName from GrowthStage gstage where LOWER(gstage.stageName) LIKE LOWER(?1)")
	List<Object[]> getGrowthStage(String stageName);
}
