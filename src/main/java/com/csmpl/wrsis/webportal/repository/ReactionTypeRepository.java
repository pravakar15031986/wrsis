package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmpl.wrsis.webportal.entity.ReactionMasterEntity;

public interface ReactionTypeRepository extends JpaRepository<ReactionMasterEntity, Integer> {

	@Query("FROM ReactionMasterEntity  where  bitStatus = false  ORDER BY vchDesc")
	List<ReactionMasterEntity> fetchAllActiveReactionTypes();

	@Query("FROM ReactionMasterEntity  where bitStatus = false  ORDER BY vchDesc")
	List<ReactionMasterEntity> fetchAllActiveReactionTypesWithoutRust();

	@Query(nativeQuery = true, value = " SELECT reaction.int_rust_reaction_id,reaction.vch_rust_reaction_name FROM wrsis.m_wr_rust_reaction reaction where bitstatus = false ORDER BY reaction.vch_rust_reaction_name ")
	public List<Object[]> fetchAllReactionTypes();

}
