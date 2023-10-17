package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.wrsis.webportal.entity.RaceAnalysis;

public interface RaceAnalysisRepository extends JpaRepository<RaceAnalysis, Integer> {

	@Query(value = "SELECT int_wheat_dif_line_id,vch_dif_line FROM wrsis.m_wr_wheat_differ_line where bitstatus=false and (bit_first_line=1 or bit_first_line=2) and int_rust_type_id=?1", nativeQuery = true)
	List<Object[]> getFirstWheatLineByRustId(Integer valueOf);

	@Transactional
	@Modifying
	@Query("UPDATE RaceAnalysis set raceStatus=?1 where raceAnalysisId=?2")
	void updateAnalysisCount(Integer analysisCount, Integer analysisId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_race_reports(\"fetchRaceResults\",2)")
	List<Object[]> fetchRaceReportResultWise(int rustTypeId);

	@Query(nativeQuery = true, value = "select cast((SELECT array_to_json(array_agg(row_to_json(data1)))\r\n"
			+ "FROM (\r\n" + "(select analysis.int_race_analysis_id analysisid,analysis.vch_race_result raceresult,\r\n"
			+ "cast((SELECT array_to_json(array_agg(row_to_json(data)))\r\n" + "FROM (\r\n"
			+ "select score.int_wheat_diff_line_id as diffid,dline.vch_dif_line as diffline,score.vch_h_l_val as hlval from wrsis.t_wr_race_analysis_score score \r\n"
			+ "inner join wrsis.m_wr_wheat_differ_line dline on score.int_wheat_diff_line_id = dline.int_wheat_dif_line_id\r\n"
			+ "where score.int_race_incolution_id = \r\n"
			+ "(select int_race_incolution_id from wrsis.t_wr_race_analysis_incolution inoculation \r\n"
			+ "where inoculation.int_race_analysis_id=analysis.int_race_analysis_id and inoculation.vch_incolution_result=analysis.vch_race_result order by inoculation.int_race_incolution_id DESC LIMIT 1 )) as data) as text) as wheatlines \r\n"
			+ "from wrsis.t_wr_race_analysis analysis \r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details stdtls on stdtls.int_sample_lab_tag_id = analysis.int_sample_lab_tag_id\r\n"
			+ "\r\n"
			+ "where analysis.vch_race_result is not null  and analysis.bit_publish_status = true and stdtls.int_rust_type_id=?1 order by analysis.int_race_analysis_id)) as data1) as text) as data")
	String fetchVirulenceData(Integer rustType);

	@Query(nativeQuery = true, value = "SELECT CAST((SELECT array_to_json(array_agg(row_to_json(fdata)))\r\n"
			+ "FROM \r\n" + "(select srdtls.int_region_id,\r\n"
			+ "(select demo.vch_demography_name as regionname from wrsis.m_wr_demography demo where demo.int_demography_id = srdtls.int_region_id),\r\n"
			+ "(SELECT array_to_json(array_agg(row_to_json(zonedetails)))\r\n" + "FROM \r\n"
			+ "(select srdtlsz.int_zone_id,\r\n"
			+ " (select demo.vch_demography_name as zonename from wrsis.m_wr_demography demo where demo.int_demography_id = srdtlsz.int_zone_id),\r\n"
			+ " (SELECT array_to_json(array_agg(row_to_json(raceresults)))\r\n" + "FROM \r\n" + "(\r\n" + "    \r\n"
			+ "    \r\n"
			+ " select tagdtlsrace.vch_race_result AS raceresult, count(*) as racecount,(SELECT count(*) FROM wrsis.t_wr_sample_lab_tag_details samtagdtls \r\n"
			+ "inner join wrsis.t_wr_survey_details srdetails1 on srdetails1.int_survey_id = samtagdtls.int_survey_id\r\n"
			+ "inner join wrsis.t_wr_survey_rust_details rdetails on rdetails.int_survey_id = srdetails1.int_survey_id \r\n"
			+ "where samtagdtls.int_rust_type_id=?1 and  samtagdtls.vch_race_result = tagdtlsrace.vch_race_result and samtagdtls.bitstatus=false and \r\n"
			+ "srdetails1.bitstatus=false and srdetails1.int_region_id= srdtls.int_region_id  and srdetails1.int_zone_id=srdtlsz.int_zone_id and rdetails.bitstatus=false) as samplecollected ,    (SELECT array_to_json(array_agg(row_to_json(varietywisecounts)))\r\n"
			+ "FROM \r\n"
			+ "(select cast(sitedetails.vch_variety as NUMERIC) varietyid,count(*) from wrsis.t_wr_sample_lab_tag_details tagdtls1 \r\n"
			+ " inner join wrsis.t_wr_survey_details srdtls1 on srdtls1.int_survey_id = tagdtls1.int_survey_id\r\n"
			+ " inner join wrsis.t_wr_survey_site_details sitedetails on srdtls1.int_survey_id = sitedetails.int_survey_id \r\n"
			+ " where  tagdtls1.int_rust_type_id=?1 and  tagdtls1.int_region_id = srdtls.int_region_id and srdtls1.int_zone_id = srdtlsz.int_zone_id \r\n"
			+ " and tagdtls1.vch_race_result = tagdtlsrace.vch_race_result group by sitedetails.vch_variety) as varietywisecounts) as resultwisevariety\r\n"
			+ "    \r\n" + "from wrsis.t_wr_survey_details srdtlsrace\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtlsrace on tagdtlsrace.int_survey_id = srdtlsrace.int_survey_id\r\n"
			+ "where  tagdtlsrace.int_rust_type_id=?1 and tagdtlsrace.int_sample_status=2 and tagdtlsrace.int_region_id=srdtls.int_region_id and srdtlsrace.int_zone_id  = srdtlsz.int_zone_id \r\n"
			+ "group by tagdtlsrace.vch_race_result\r\n" + "\r\n" + ")\r\n" + "  \r\n" + "  \r\n"
			+ "  as raceresults) as raceresults\r\n" + " \r\n" + " \r\n" + " \r\n"
			+ " from wrsis.t_wr_survey_details srdtlsz\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtlsz on tagdtlsz.int_survey_id = srdtlsz.int_survey_id\r\n"
			+ "where tagdtlsz.int_rust_type_id=?1 and tagdtlsz.int_sample_status=2 and tagdtlsz.int_region_id = srdtls.int_region_id group by srdtlsz.int_zone_id\r\n"
			+ ") as zonedetails)  as zonedtls\r\n" + "\r\n" + "from wrsis.t_wr_survey_details srdtls\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtls on tagdtls.int_survey_id = srdtls.int_survey_id\r\n"
			+ "where  tagdtls.int_rust_type_id=?1 and tagdtls.int_sample_status=2 group by srdtls.int_region_id) as fdata) as text)")
	String fetchVarietyReportDataSearch(Integer rustId);

	@Query(nativeQuery = true, value = "SELECT CAST((SELECT array_to_json(array_agg(row_to_json(fdata)))\r\n"
			+ "FROM \r\n" + "(select srdtls.int_region_id,\r\n"
			+ "(select demo.vch_demography_name as regionname from wrsis.m_wr_demography demo where demo.int_demography_id = srdtls.int_region_id),\r\n"
			+ "(SELECT array_to_json(array_agg(row_to_json(zonedetails)))\r\n" + "FROM \r\n"
			+ "(select srdtlsz.int_zone_id,\r\n"
			+ " (select demo.vch_demography_name as zonename from wrsis.m_wr_demography demo where demo.int_demography_id = srdtlsz.int_zone_id),\r\n"
			+ " (SELECT array_to_json(array_agg(row_to_json(raceresults)))\r\n" + "FROM \r\n" + "(\r\n" + "    \r\n"
			+ "    \r\n"
			+ " select tagdtlsrace.vch_race_result AS raceresult, count(*) as racecount,(SELECT count(*) FROM wrsis.t_wr_sample_lab_tag_details samtagdtls \r\n"
			+ "inner join wrsis.t_wr_survey_details srdetails1 on srdetails1.int_survey_id = samtagdtls.int_survey_id\r\n"
			+ "inner join wrsis.t_wr_survey_rust_details rdetails on rdetails.int_survey_id = srdetails1.int_survey_id \r\n"
			+ "where samtagdtls.int_rust_type_id=?1 and  samtagdtls.vch_race_result = tagdtlsrace.vch_race_result and samtagdtls.bitstatus=false and \r\n"
			+ "srdetails1.bitstatus=false and srdetails1.int_region_id= srdtls.int_region_id  and srdetails1.int_zone_id=srdtlsz.int_zone_id and rdetails.bitstatus=false) as samplecollected ,    (SELECT array_to_json(array_agg(row_to_json(varietywisecounts)))\r\n"
			+ "FROM \r\n"
			+ "(select cast(sitedetails.vch_variety as NUMERIC) varietyid,count(*) from wrsis.t_wr_sample_lab_tag_details tagdtls1 \r\n"
			+ " inner join wrsis.t_wr_survey_details srdtls1 on srdtls1.int_survey_id = tagdtls1.int_survey_id\r\n"
			+ " inner join wrsis.t_wr_survey_site_details sitedetails on srdtls1.int_survey_id = sitedetails.int_survey_id \r\n"
			+ " where  tagdtls1.int_rust_type_id=?1 and  tagdtls1.int_region_id = srdtls.int_region_id and srdtls1.int_zone_id = srdtlsz.int_zone_id \r\n"
			+ " and tagdtls1.vch_race_result = tagdtlsrace.vch_race_result group by sitedetails.vch_variety) as varietywisecounts) as resultwisevariety\r\n"
			+ "    \r\n" + "from wrsis.t_wr_survey_details srdtlsrace\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtlsrace on tagdtlsrace.int_survey_id = srdtlsrace.int_survey_id\r\n"
			+ "where  tagdtlsrace.int_rust_type_id=?1 and tagdtlsrace.int_sample_status=2 and tagdtlsrace.int_region_id=srdtls.int_region_id and srdtlsrace.int_zone_id  = srdtlsz.int_zone_id \r\n"
			+ "group by tagdtlsrace.vch_race_result\r\n" + "\r\n" + ")\r\n" + "  \r\n" + "  \r\n"
			+ "  as raceresults) as raceresults\r\n" + " \r\n" + " \r\n" + " \r\n"
			+ " from wrsis.t_wr_survey_details srdtlsz\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtlsz on tagdtlsz.int_survey_id = srdtlsz.int_survey_id\r\n"
			+ "where tagdtlsz.int_rust_type_id=?1 and tagdtlsz.int_sample_status=2 and tagdtlsz.int_region_id = srdtls.int_region_id group by srdtlsz.int_zone_id\r\n"
			+ ") as zonedetails)  as zonedtls\r\n" + "\r\n" + "from wrsis.t_wr_survey_details srdtls\r\n"
			+ "inner join wrsis.t_wr_sample_lab_tag_details tagdtls on tagdtls.int_survey_id = srdtls.int_survey_id\r\n"
			+ "where  tagdtls.int_region_id = ?2 and tagdtls.int_rust_type_id=?1 and tagdtls.int_sample_status=2 group by srdtls.int_region_id) as fdata) as text)")
	String fetchVarietyReportDataSearch(Integer rustId, Integer regionId);

	@Query(nativeQuery = true, value = "select tagdtls.int_survey_id from wrsis.t_wr_race_analysis analysis inner join\r\n"
			+ "wrsis.t_wr_sample_lab_tag_details tagdtls on tagdtls.int_sample_lab_tag_id = analysis.int_sample_lab_tag_id\r\n"
			+ "where analysis.int_race_analysis_id = ?1")
	Integer fetchSurveyIdByAnalysisId(Integer analysisId);

}
