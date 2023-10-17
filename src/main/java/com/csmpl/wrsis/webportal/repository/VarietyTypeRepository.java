package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;

public interface VarietyTypeRepository extends JpaRepository<VarietyTypeMasterEntity, Integer>{

	@Query(" from VarietyTypeMasterEntity v where v.id=:id")
	VarietyTypeMasterEntity findByVarietyId(@Param("id") int id);
 
	
	@Query("FROM VarietyTypeMasterEntity where bitStatus=false order by vchDesc")
	List<VarietyTypeMasterEntity> fetchActiveVarietyType();


	@Query(nativeQuery=true,value="select var.int_variety_type_id,var.vch_variety_name,var.bitstatus " + 
			"from wrsis.m_wr_variety_type var where var.bitstatus=:condition ORDER BY var.int_variety_type_id DESC")
	List<Object[]> viewVarietyList(boolean condition);

	@Query(nativeQuery=true,value="select var.int_variety_type_id,var.vch_variety_name,var.bitstatus " + 
			"from wrsis.m_wr_variety_type var ORDER BY var.int_variety_type_id DESC")
	List<Object[]> viewVarietyList();
	
	 @Query(nativeQuery = true,value =" SELECT vari.int_variety_type_id,vari.vch_variety_name FROM wrsis.m_wr_variety_type vari where bitstatus = false ORDER BY vari.vch_variety_name   " )
	 List<Object[]> fetchAllVarietyType();

	@Query("select variety.vchDesc from VarietyTypeMasterEntity variety where variety.varietyTypeId!=?1 and LOWER(variety.vchDesc) LIKE LOWER(?2)")
	List<Object[]> getVariety(int varietyTypeId, String vchDesc);

	@Query("select variety.vchDesc from VarietyTypeMasterEntity variety where LOWER(variety.vchDesc) LIKE LOWER(?1)")
	List<Object[]> getVariety(String vchDesc);

}
