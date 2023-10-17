package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.OrganizationMasterEntity;

@Repository
public interface OrganizationMasterRepository extends JpaRepository<OrganizationMasterEntity, Integer> {

	@Query("FROM OrganizationMasterEntity where bitstatus = false and intlevelid=1 ")
	List<OrganizationMasterEntity> fetchAllCountry();

	@Query("FROM OrganizationMasterEntity where bitstatus = false and intparentid=:countryId ")
	List<OrganizationMasterEntity> getCountryByMinistry(@Param("countryId") Integer countryId);

	@Query(nativeQuery = true, value = " select country.nvchlevelname as country_name, \r\n"
			+ "ministry.nvchlevelname as ministry_name,\r\n"
			+ "org.nvchlevelname as org_name,org.vchalias,org.bitstatus,org.intleveldetailid\r\n"
			+ "from wrsis.m_adm_leveldetails org \r\n"
			+ "inner join wrsis.m_adm_leveldetails ministry on org.intparentid=ministry.intleveldetailid\r\n"
			+ "inner join wrsis.m_adm_leveldetails country on ministry.intparentid=country.intleveldetailid\r\n"
			+ "where org.intlevelid=3 and ministry.bitstatus=false \r\n"
			+ "and country.bitstatus=false order by org.nvchlevelname")
	List<Object[]> getOrganisationList();

	@Query(nativeQuery = true, value = " select country.intleveldetailid as country_id, \r\n" +
	// "ministry.intleveldetailid as ministry_id,\r\n" +
			"org.nvchlevelname as org_name,org.vchalias,org.bitstatus,org.intleveldetailid \r\n"
			+ "from wrsis.m_adm_leveldetails org \r\n"
			+ "inner join wrsis.m_adm_leveldetails ministry on org.intparentid=ministry.intleveldetailid\r\n"
			+ "inner join wrsis.m_adm_leveldetails country on ministry.intparentid=country.intleveldetailid\r\n"
			+ "where org.intlevelid=3  and ministry.bitstatus=false \r\n" + // and org.bitstatus=false
			"and country.bitstatus=false and org.intleveldetailid=:intleveldetailid ")
	List<Object[]> editOrganization(@Param("intleveldetailid") int intleveldetailid);

	OrganizationMasterEntity findByIntleveldetailid(int intleveldetailid);

	@Query("select org.nvchlevelname from OrganizationMasterEntity org where org.intleveldetailid!=?1 and LOWER(org.nvchlevelname) LIKE LOWER(?2)")
	List<Object[]> getOrgNames(int intleveldetailid, String nvchlevelname);

	@Query("select org.nvchlevelname from OrganizationMasterEntity org where LOWER(org.nvchlevelname) LIKE LOWER(?1)")
	List<Object[]> getOrgNames(String nvchlevelname);

}
