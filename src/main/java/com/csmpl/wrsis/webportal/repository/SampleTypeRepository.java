package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
@Repository
public interface SampleTypeRepository extends JpaRepository<SampleTypeMaster, Integer>{

	@Query("From SampleTypeMaster s order by s.sampleId")
	List<SampleTypeMaster> viewAllSample();
	
	SampleTypeMaster findBySampleId(Integer sampleId);
	
	@Query(nativeQuery=true,value="select sample.int_sample_type_id,sample.int_rust_type_id,rust.vch_rust_type,sample.vch_sample_type,sample.vch_desc,sample.bitstatus " + 
			"from wrsis.m_wr_sample_type sample  " + 
			"inner join wrsis.m_wr_rust_type rust on sample.int_rust_type_id=rust.int_rust_type_id order by sample.int_sample_type_id desc")
	Page<Object[]> viewAllSampleTypePage(Pageable pageable);
	
	@Query(nativeQuery=true,value="select sample.int_sample_type_id,sample.int_rust_type_id,rust.vch_rust_type,sample.vch_sample_type,sample.vch_desc,sample.bitstatus " + 
			"from wrsis.m_wr_sample_type sample  " + 
			"inner join wrsis.m_wr_rust_type rust on sample.int_rust_type_id=rust.int_rust_type_id " + 
			"where sample.int_rust_type_id=?1 and sample.bitstatus=?2 order by sample.int_sample_type_id desc")
	Page<Object[]> findByRustTypeIdAndStatus(int rustTypeId,boolean status,Pageable pageable);
	
	@Query(nativeQuery=true,value="select sample.int_sample_type_id,sample.int_rust_type_id,rust.vch_rust_type,sample.vch_sample_type,sample.vch_desc,sample.bitstatus " + 
			"from wrsis.m_wr_sample_type sample  " + 
			"inner join wrsis.m_wr_rust_type rust on sample.int_rust_type_id=rust.int_rust_type_id " + 
			"where sample.int_rust_type_id=?1 order by sample.int_sample_type_id desc")
	Page<Object[]> findByRustTypeId(int rustTypeId,Pageable pageable);
	
	@Query(nativeQuery=true,value="select sample.int_sample_type_id,sample.int_rust_type_id,rust.vch_rust_type,sample.vch_sample_type,sample.vch_desc,sample.bitstatus " + 
			"from wrsis.m_wr_sample_type sample  " + 
			"inner join wrsis.m_wr_rust_type rust on sample.int_rust_type_id=rust.int_rust_type_id " + 
			"where sample.bitstatus=?1 order by sample.int_sample_type_id desc")
	Page<Object[]> findByStatus(boolean status,Pageable pageable);
	Page<SampleTypeMaster> findBySampleNameContainingAndStatus(String sampleName,boolean status,Pageable pageable);

	@Query("FROM SampleTypeMaster where status = false ORDER BY sampleName")
	List<SampleTypeMaster> fetchAllActiveSampletypes();
	
	@Query(nativeQuery = true,value =" SELECT samtype.int_sample_type_id,samtype.vch_sample_type,samtype.int_rust_type_id FROM wrsis.m_wr_sample_type samtype where bitstatus = false ORDER BY samtype.vch_sample_type " )
	public List<Object[]> fetchAllSampletypes();

	List<SampleTypeMaster> findBySampleNameIgnoreCase(String sampleName);

	@Query("select sample.sampleName from SampleTypeMaster sample where LOWER(sample.sampleName) LIKE LOWER(?1) and sample.sampleId!=?2 ")
	List<SampleTypeMaster> getSampleType(String sampleName, int sampleId);


	@Query(nativeQuery=true,value="select sample.int_sample_type_id,sample.int_rust_type_id,rust.vch_rust_type,sample.vch_sample_type,sample.vch_desc,sample.bitstatus " + 
			"from wrsis.m_wr_sample_type sample  " + 
			"inner join wrsis.m_wr_rust_type rust on sample.int_rust_type_id=rust.int_rust_type_id order by sample.int_sample_type_id desc")
	Page<Object[]> getAllSampleTypes(Pageable pageable);

	@Query("FROM SampleTypeMaster where bitstatus = false and rustTypeId=?1")
	List<SampleTypeMaster> findByRustTypeId(int int1);
}
