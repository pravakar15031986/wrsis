package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.DiseaseMaster;

@Repository
public interface DiseaseMasterRepository extends JpaRepository<DiseaseMaster, Integer> {

	@Query("FROM DiseaseMaster where diseaseId=:id")
	DiseaseMaster getDeseasById(@Param("id") Integer diseaseId);

	@Query("FROM  DiseaseMaster d ORDER By d.diseaseId desc")
	Page<DiseaseMaster> viewAllDeseasPage(Pageable pageable);

	Page<DiseaseMaster> findByDiseaseNameContainsIgnoreCaseAndStatus(String diseaseName, boolean status,
			Pageable pageable);

	Page<DiseaseMaster> findByDiseaseAliasNameContainsIgnoreCaseAndStatus(String diseaseAliasName, boolean status,
			Pageable pageable);

	Page<DiseaseMaster> findByStatus(boolean status, Pageable pageable);

	@Query("FROM DiseaseMaster where bitstatus=false and otherDetails=:check ORDER BY diseaseName")
	List<DiseaseMaster> fetchAllActiveDisease(@Param("check") boolean check);

	@Query(nativeQuery = true, value = " SELECT dise.int_disease_id,dise.vch_disease,dise.int_severity_min,dise.int_severity_max,dise.bit_severity_required FROM wrsis.m_wr_disease dise where bitstatus = false and bit_other_dtls_required = true ORDER BY dise.vch_disease ")
	public List<Object[]> fetchAllDisease();

	@Query(nativeQuery = true, value = " SELECT dise.int_disease_id,dise.vch_disease,dise.int_severity_min,dise.int_severity_max,dise.bit_severity_required FROM wrsis.m_wr_disease dise where bitstatus = false and bit_other_dtls_required = false ORDER BY dise.vch_disease ")
	List<Object[]> fetchAllAnyOthDisease();

	Page<DiseaseMaster> findByDiseaseNameContainsIgnoreCase(String diseaseName, Pageable pageable);

	Page<DiseaseMaster> findByDiseaseAliasNameContainsIgnoreCase(String diseaseName, Pageable pageable);

	@Query("select dis.diseaseName from DiseaseMaster dis where LOWER(dis.diseaseName) LIKE LOWER(?1) and dis.otherDetails=?2 ")
	List<Object[]> getDisName(String diseaseName, boolean b);

	@Query("select dis.diseaseName from DiseaseMaster dis where dis.diseaseId!=?1 and LOWER(dis.diseaseName) LIKE LOWER(?2) and dis.otherDetails=?3")
	List<Object[]> getDisName(int diseaseId, String diseaseName, boolean b);
}
