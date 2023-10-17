package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.csmpl.wrsis.webportal.entity.WheatDifferentialLineEntity;

@Repository
public interface WheatDifferentialLineRepository extends JpaRepository<WheatDifferentialLineEntity, Integer> {

	@Query("select wdl.difLine from WheatDifferentialLineEntity wdl where LOWER(wdl.difLine) LIKE LOWER(?1)")
	List<Object[]> retriveDiffLineExistName(String difLine);

	@Query("select wdl.difLine from WheatDifferentialLineEntity wdl where wdl.wheatDifLineId!=?1 and LOWER(wdl.difLine) LIKE LOWER(?2)")
	List<Object[]> retriveDiffLineExistName(Integer wheatDifLineId, String difLine);

	@Query("from WheatDifferentialLineEntity wdl where wdl.wheatDifLineId=?1")
	WheatDifferentialLineEntity findByWheatDifLineId(Integer wheatDifLineId);

	@Query("select wdl.difLine from WheatDifferentialLineEntity wdl where wdl.rustTypeId=?1 and wdl.diffSetNo=?2 and wdl.status=false and (wdl.firstLine=0 or wdl.firstLine=2)")
	List<Object[]> retrieveWheatDiffLineForRustTypeInSet(Integer rustTypeId, Integer diffSetNo);

	@Query(nativeQuery = true, value = "select dif.int_wheat_dif_line_id,dif.int_rust_type_id,rust.vch_rust_type,dif.bit_first_line,dif.vch_dif_line, "
			+ "dif.int_diff_set_no,dif.int_seed_src_id,ss.vch_seed_src,dif.vch_gene,dif.vch_expect_lowit,dif.vch_desc,dif.bitstatus "
			+ "from wrsis.m_wr_wheat_differ_line dif "
			+ "inner join wrsis.m_wr_rust_type rust on dif.int_rust_type_id=rust.int_rust_type_id "
			+ "left join wrsis.m_wr_seed_source ss on dif.int_seed_src_id=ss.int_seed_src_id order by rust.vch_rust_type,dif.int_diff_set_no")
	List<Object[]> retriveWheatDLDetails();

	@Query(nativeQuery = true, value ="select * from wrsis.sp_wr_view_wheat_diff_line(?1,?2,?3,?4)")
	List<Object[]> retriveWheatDLDetails(int rustTypeId, int diffSetNo, int isFirstDiffLine, int status);

	@Query("select wdl.difLine from WheatDifferentialLineEntity wdl where wdl.wheatDifLineId!=?3 and wdl.rustTypeId=?1 and wdl.diffSetNo=?2 and wdl.status=false and (wdl.firstLine=0 or wdl.firstLine=2)")
	List<Object[]> retrieveWheatDiffLineForRustTypeInSet(Integer rustTypeId, Integer diffSetNo, Integer wheatDifLineId);

	@Query(nativeQuery=true,value="select max(int_seq_no) from wrsis.m_wr_wheat_differ_line where int_rust_type_id=?1 and bitstatus=false")
	int getMaxSeqNoByRustTypeId(int rustid);

	@Query("select seqNo from WheatDifferentialLineEntity where rustTypeId=?1 and seqNo=?2")
	List<Object[]> retriveRustSeq(Integer rustTypeId, int seqNo);

	@Query("select seqNo from WheatDifferentialLineEntity where wheatDifLineId!=?1 and rustTypeId=?2 and seqNo=?3")
	List<Object[]> retriveRustSeq(Integer wheatDifLineId, Integer rustTypeId, int seqNo);

}
