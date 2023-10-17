package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.TypeOfRust;


@Repository
public interface TypeOfRustRepository extends JpaRepository<TypeOfRust, Integer>{

	@Query("from TypeOfRust where rustId=:rustId")
	TypeOfRust getTypeIfRustById(@Param("rustId")Integer rustId);
	
	@Query("FROM   TypeOfRust t ORDER BY t.rustId desc") 
	Page<TypeOfRust> viewAllTypeOfRustPage(Pageable pageable); 
	/*@Query(value="select * from wrsis.m_wr_rust_type m where m.vch_rust_type=typeOfRust and vch_rust_alias=shortName and bitstatus=isDelete;",nativeQuery=true)*/
	@Query("from TypeOfRust m where m.typeOfRust=:typeOfRust and isDelete=:isDelete")
	Page<TypeOfRust> searchByTypeOfRustContainsIgnoreCaseAndIsDelete(@Param("typeOfRust")String typeOfRust,@Param("isDelete")Boolean isDelete,Pageable pageable);
	
	@Query("from TypeOfRust m where  m.shortName=:shortName and m.isDelete=:isDelete")
	Page<TypeOfRust> searchByShortNameContainsIgnoreCaseAndIsDelete(@Param("shortName")String shortName,@Param("isDelete")Boolean isDelete,Pageable pageable);
	
	Page<TypeOfRust> findByTypeOfRustContainingIgnoreCase(String typeOfRust,Pageable pageable);
	
	Page<TypeOfRust> findByisDeleteOrderByRustIdDesc(Boolean isDelete,Pageable pageable);
	
	@Query("FROM   TypeOfRust t where t.isDelete='false' ORDER BY t.typeOfRust ASC") 
	List<TypeOfRust> vewAllTypeOFRustByStatus(); 
	
	@Query(nativeQuery = true,value =" SELECT rstype.int_rust_type_id,rstype.vch_rust_type FROM wrsis.m_wr_rust_type rstype where bitstatus = false ORDER BY rstype.vch_rust_type " )
	public List<Object[]> fetchAllRustType();
	
	Page<TypeOfRust> findByShortNameContainingIgnoreCase(String shortName,Pageable pageable);

	@Query(" FROM TypeOfRust rust where rust.isDelete='false' ORDER BY rust.typeOfRust ")
	List<TypeOfRust> getTypeOfRusts();

	@Query("select rust.typeOfRust from TypeOfRust rust where rust.rustId!=?1 and LOWER(rust.typeOfRust) LIKE LOWER(?2)")
	List<Object[]> getRustTypes(int rustId, String typeOfRust);
	
	@Query("select rust.typeOfRust from TypeOfRust rust where LOWER(rust.typeOfRust) LIKE LOWER(?1)")
	List<Object[]> getRustTypes( String typeOfRust);

	@Query("from TypeOfRust rust where rust.isDelete=false order by rust.typeOfRust")
	List<TypeOfRust> getRustTypes();
}
