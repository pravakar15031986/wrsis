package com.csmpl.wrsis.webportal.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.csmpl.wrsis.webportal.entity.ImportIVRFile;

@Repository
public interface ManageIVRDataRepository extends JpaRepository<ImportIVRFile, Integer> {

	@Query("from ImportIVRFile f ORDER BY f.createdOn DESC")
	Page<ImportIVRFile> viewIVRFileByPage(Pageable pageable);

	ImportIVRFile findByFileNameIgnoreCase(String fileName);

	@Query(value = "SELECT d.int_import_ivr_id, d.vch_ivr_file_name, d.int_ivr_record_count, d.bitstatus, d.intcreatedby, d.dtmcreatedon, d.intupdatedby, d.dtmupdatedon"
			+ " FROM wrsis.t_wr_import_ivr_files  d where  DATE_TRUNC('day', d.dtmcreatedon ) >=  ?1  AND DATE_TRUNC('day', d.dtmcreatedon )  <=?2 ", nativeQuery = true)
	Page<ImportIVRFile> searchIVRFileByPage(Pageable pageable, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}
