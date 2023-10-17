package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.FungicideEffectTypeMasterEntity;

public interface FungiEffectTypeRepository extends JpaRepository<FungicideEffectTypeMasterEntity, Integer> {

	@Query("FROM FungicideEffectTypeMasterEntity where bitStatus = false ORDER BY vchDesc")
	List<FungicideEffectTypeMasterEntity> fetchAllActiveFungiEffectTypes();

	@Query(nativeQuery = true, value = " SELECT fngEff.int_fung_effect_id,fngEff.vch_effect_desc FROM wrsis.m_wr_fungicide_effect fngEff where bitstatus = false ORDER BY fngEff.vch_effect_desc ")
	public List<Object[]> fetchAllFungiEffectTypes();

}
