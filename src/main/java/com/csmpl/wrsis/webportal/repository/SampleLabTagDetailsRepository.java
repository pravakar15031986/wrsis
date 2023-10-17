package com.csmpl.wrsis.webportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SampleSurveyDetailsEntity;

public interface SampleLabTagDetailsRepository extends JpaRepository<SampleLabTagDetails, Integer> {

	@Query(value = "select * from wrsis.sp_wr_pending_sample(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	List<Object[]> fetchActiveSampleTagDetails(String surveyNo, String sampleId, String allocationStartDate,
			String allocationEndDate, Integer regionId, Integer userId, Integer start, Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_pending_sample(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	Integer fetchActiveSampleTagDetailsCount(String surveyNo, String sampleId, String allocationStartDate,
			String allocationEndDate, Integer regionId, Integer userId, Integer start, Integer length);

	@Query(value = "select * from wrsis.sp_wr_inoculated_sample(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	List<Object[]> fetchActiveInoculatedPending(String string, String string2, String string3, String string4, int i,
			Integer userId, Integer start, Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_inoculated_sample(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	Integer fetchActiveInoculatedPendingCount(String string, String string2, String string3, String string4, int i,
			Integer userId, Integer start, Integer length);

	@Query(value = "SELECT dline.vch_dif_line,src.vch_seed_src,dline.vch_gene,dline.vch_expect_lowit,dline.int_diff_set_no,dline.int_wheat_dif_line_id,dline.int_seq_no FROM wrsis.m_wr_wheat_differ_line dline \r\n"
			+ "	left join wrsis.m_wr_seed_source src on src.int_seed_src_id=dline.int_seed_src_id  \r\n"
			+ "	where  dline.int_rust_type_id=?1 and dline.bitstatus=false  and (dline.bit_first_line = 0 or dline.bit_first_line = 2) order by dline.int_seq_no", nativeQuery = true)
	List<Object[]> fetchRaceStatusWiseRepeatationDetails(Integer rustTypeId);

	@Query(value = "SELECT concat(vch_first_seq,vch_second_seq,vch_third_seq,vch_fourth_seq),vch_result_name FROM wrsis.m_wr_race_identification_chart where bitstatus = false", nativeQuery = true)
	List<Object[]> fetchChartDetails();

	@Query(value = "select * from wrsis.sp_wr_tag_sample(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
	List<Object[]> fetchActiveTagSamples(String surveyNo, String sampleId, String startdate, String enddate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer pstart, Integer plength);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_tagged_samples(?1, ?2, ?3, ?4, ?5, ?6,?7,?8)") // ,?7,?8
	List<Object[]> viewTaggedSamples(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, Integer regionId, Integer rustTypeId, Integer pstart, Integer plength);// , Integer

	@Query(nativeQuery = true, value = "select ts.int_sample_lab_tag_id,ts.vch_sampleid_value,ts.vch_survey_number,ts.dt_survey_date, "
			+ "ts.int_region_id, " + "demo.vch_demography_name, " + "ts.int_rust_type_id, " + "rust.vch_rust_type, "
			+ "ts.int_research_center_id, " + "rc.vch_research_center_name, " + "ts.bit_external_lab "
			+ "from wrsis.t_wr_sample_lab_tag_details ts "
			+ "inner join wrsis.t_wr_survey_details survey on ts.int_survey_id=survey.int_survey_id "
			+ "inner join wrsis.m_wr_demography demo on ts.int_region_id=demo.int_demography_id "
			+ "inner join wrsis.m_wr_rust_type rust on ts.int_rust_type_id=rust.int_rust_type_id "
			+ "left join wrsis.m_wr_research_center rc on ts.int_research_center_id=rc.int_research_center_id "
			+ "where ts.int_sample_lab_tag_id=?1")
	List<Object[]> getTagDetails(int sampleLabTagId);

	SampleLabTagDetails findBySampleLabTagId(Integer sampleLabTagId);

	@Query(value = "select * from wrsis.sp_wr_view_race_analysis(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
	List<Object[]> viewRaceAnalysis(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer researchCenterId, Integer start,
			Integer length);

	@Query(value = "select * from wrsis.sp_wr_view_race_analysis_mobile(?1,?2)", nativeQuery = true)
	List<Object[]> viewRaceAnalysisMolile(Integer userId, Integer recid);

	@Query("FROM SampleSurveyDetailsEntity where bitstatus=false and bitTagStatus=false and surveySampleId=:sampleId")
	SampleSurveyDetailsEntity getDataBySurveySampleId(@Param("sampleId") Integer sampleId);

	@Query(value = "select * from wrsis.sp_wr_view_recommendation(?1,?2,?3)", nativeQuery = true)
	List<Object[]> viewRecommendation(String string, String string2, String string3);

	@Query(value = "select * from wrsis.sp_wr_view_advisory(?1,?2,?3)", nativeQuery = true)
	List<Object[]> viewAdvisory(String string, String string2, String string3);

	@Query(value = "SELECT dline.vch_dif_line,src.vch_seed_src,dline.vch_gene,dline.vch_expect_lowit,dline.int_diff_set_no,dline.int_wheat_dif_line_id,dline.int_seq_no,dline.vch_race_phenotype FROM wrsis.m_wr_wheat_differ_line dline \r\n"
			+ "	left join wrsis.m_wr_seed_source src on src.int_seed_src_id=dline.int_seed_src_id  \r\n"
			+ "	where  dline.int_rust_type_id=?1 and dline.bitstatus=false and (dline.bit_first_line = 0 or dline.bit_first_line = 2) order by dline.int_seq_no", nativeQuery = true)
	List<Object[]> fetchRaceStatusWiseRepeatationDetailsYellow(Integer rustTypeId);

	@Query(value = "select * from wrsis.sp_wr_race_result(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	List<Object[]> fetchActiveRaceResult(String string, String string2, String string3, String string4, int i,
			Integer userId, Integer start, Integer length);

	@Query(value = "select count(*) from wrsis.sp_wr_race_result(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	Integer fetchActiveRaceResultCount(String string, String string2, String string3, String string4, int i,
			Integer userId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_tagged_samples(?1,?2,?3,?4,?5,?6)")
	List<Object[]> viewExtTaggedSamples(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_tagged_samples_race_result(?1,?2,?3,?4,?5,?6)")
	List<Object[]> viewExtRaceResult(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_dumped_samples_race_result(?1,?2,?3,?4,?5,?6,?7)")
	List<Object[]> viewDumpedSamples(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			int regionId, int rustTypeId, int rcId);

	@Query(value = " select  distinct survey.vch_survey_number,survey.dt_survey_date,demo.vch_demography_name as region,demo1.vch_demography_name as zone,demo2.vch_demography_name as woreda,demo3.vch_demography_name as kebele,\r\n"
			+ "survey.vch_latitude,survey.vch_longitude,survey.vch_altitude,samplesampleids.vch_sampleid_value,rustmaster.vch_rust_type,usr.vchuserfullname as reciveby,recenter.vch_research_center_name,\r\n"
			+ "variety.vch_variety_name,usr1.vchuserfullname as surveyor,analysis.dt_incolutation_date,analysis.vch_race_doc,analysis.vch_race_result,differline.vch_dif_line,analysis.dtmcreatedon,survey.int_survey_id\r\n"
			+ " from wrsis.t_wr_race_analysis analysis\r\n"
			+ " inner join wrsis.t_wr_sample_lab_tag_details tagsample on analysis.int_sample_lab_tag_id =tagsample.int_sample_lab_tag_id\r\n"
			+ " inner join  wrsis.t_wr_survey_details survey on tagsample.int_survey_id = survey.int_survey_id\r\n"
			+ " inner join wrsis.m_wr_demography demo on demo.int_demography_id=survey.int_region_id\r\n"
			+ " inner join  wrsis.m_wr_demography demo1 on demo1.int_demography_id=survey.int_zone_id \r\n"
			+ " inner join  wrsis.m_wr_demography demo2 on demo2.int_demography_id=survey.int_woreda_id\r\n"
			+ " inner join  wrsis.m_wr_demography demo3 on demo3.int_demography_id=survey.int_kebel_id\r\n"
			+ " --inner join wrsis.t_wr_survey_rust_details rustdtls on survey.int_survey_id = rustdtls.int_survey_id \r\n"
			+ " --inner join wrsis.m_wr_rust_type rustmaster on rustmaster.int_rust_type_id = rustdtls.int_rust_type_id\r\n"
			+ " --inner join wrsis.t_wr_survey_sample_sampleids  samplesampleids on survey.int_survey_id=samplesampleids.int_survey_id\r\n"
			+ " inner join wrsis.t_user usr on analysis.intcreatedby=usr.intuserid\r\n"
			+ " --inner join wrsis.m_wr_user_rc_mapping rcmapping on usr.intuserid=rcmapping.int_user_id\r\n"
			+ " --inner join wrsis.m_wr_research_center recenter on rcmapping.int_research_center_id=recenter.int_research_center_id\r\n"
			+ " inner join wrsis.t_wr_survey_site_details wheattype on survey.int_survey_id = wheattype.int_survey_id \r\n"
			+ " inner join wrsis.m_wr_variety_type variety on  cast( wheattype.vch_variety as integer) = variety.int_variety_type_id \r\n"
			+ " inner join wrsis.t_user usr1 on survey.intcreatedby=usr1.intuserid\r\n"
			+ " inner join wrsis.t_wr_survey_sample_sampleids  samplesampleids on tagsample.int_survey_sample_sampleid=samplesampleids.int_survey_sample_sampleid\r\n"
			+ " inner join wrsis.m_wr_rust_type rustmaster on rustmaster.int_rust_type_id = tagsample.int_rust_type_id\r\n"
			+ " left join wrsis.m_wr_research_center recenter on recenter.int_research_center_id=tagsample.int_research_center_id\r\n"
			+ " left join wrsis.t_wr_race_analysis_incolution  incolution on analysis.int_race_analysis_id = incolution.int_race_analysis_id\r\n"
			+ " inner join wrsis.m_wr_wheat_differ_line  differline on differline.int_wheat_dif_line_id = analysis.int_first_wheat_line\r\n"
			+ " \r\n"
			+ "where survey.bitstatus='false' and demo.bitstatus='false'and demo1.bitstatus='false'  and demo2.bitstatus='false'  and demo3.bitstatus='false' \r\n"
			+ "and rustmaster.bitstatus='false' and samplesampleids.bitstatus='false' and usr.tintdeletedstatus='false'  \r\n"
			+ "and recenter.bitstatus='false' and wheattype.bitstatus='false' and variety.bitstatus='false'  and analysis.bitstatus='false' and differline.bitstatus='false' and analysis.int_race_analysis_id=:raceAnalysisId", nativeQuery = true)
	List<Object[]> getSurveyDetatailsBySurveyId(@Param("raceAnalysisId") Integer raceAnalysisId);

	@Query(value = "select incolution.dt_incolutation_date,incolution.dt_recording_date,incolution.vch_incolution_result,incolution.int_repeation_no\r\n"
			+ "from wrsis.t_wr_race_analysis_incolution incolution\r\n"
			+ "inner join wrsis.t_wr_race_analysis analysis on incolution.int_race_analysis_id = analysis.int_race_analysis_id\r\n"
			+ "where incolution.bitstatus='false' and analysis.bitstatus='false' and incolution.int_race_analysis_id =:raceAnalysisId ORDER BY  incolution.dtmupdatedon ASC", nativeQuery = true)
	List<Object[]> getIncolutionDataByRaceAnalysisId(@Param("raceAnalysisId") Integer raceAnalysisId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_view_stem_rust_race_details(?1,?2,?3,?4,?5,?6,?7,?8)")
	List<Object[]> viewDetails(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			String raceResult, Integer regionId, Integer rustTypeId, Integer labId);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_view_race_analysis_report(?1,?2,?3,?4,?5,?6,?7,?8,?9)")
	List<Object[]> viewRaceAnalysisReport(String surveyNo, String sampleId, String startDate, String endDate,
			int regionId, int zoneId, String year, int seasonId, int rustTypeId);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_tag_sample_count(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)")
	Integer searchTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select count(1) from wrsis.t_wr_survey_sample_sampleids where int_survey_id=?1 and \r\n"
			+ "int_survey_sample_sampleid not in ( select int_survey_sample_sampleid from  wrsis.t_wr_sample_lab_tag_details \r\n"
			+ "where int_survey_id=?1 and bitstatus=false ) and bitstatus=false")
	Integer getSampleCountBySurveyId(Integer surveyId);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_view_tag_sample_count(?1,?2,?3,?4,?5,?6,?7,?8)")
	Integer viewTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_view_race_analysis_count(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)")
	Integer viewRaceAnalysisCount(String surveyNo, String sampleId, String startDate, String endDate, Integer regionId,
			Integer rustTypeId, Integer userId, Integer researchCenterId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_dumped_samples_race_result_view(?1,?2,?3,?4,?5,?6,?7,?8,?9)")
	List<Object[]> viewDumpedSamplesPage(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			int regionId, int rustTypeId, int rcId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_ext_dumped_samples_race_result_view(?1,?2,?3,?4,?5,?6,?7,?8,?9)")
	Integer viewDumpedSamplesPageCount(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			int regionId, int rustTypeId, int rcId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_tagged_samples_view(?1,?2,?3,?4,?5,?6,?7,?8)")
	List<Object[]> viewExtTaggedSamplesPage(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_ext_tagged_samples_view(?1,?2,?3,?4,?5,?6,?7,?8)")
	Integer viewExtTaggedSamplesPageCont(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select * from wrsis.sp_wr_ext_tagged_samples_race_result_view(?1,?2,?3,?4,?5,?6,?7,?8)")
	List<Object[]> viewExtRaceResultPage(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId, Integer start, Integer length);

	@Query(nativeQuery = true, value = "select count(*) from wrsis.sp_wr_ext_tagged_samples_race_result_view(?1,?2,?3,?4,?5,?6,?7,?8)")
	Integer viewExtRaceResultPageCount(String surveyNo, String sampleIdValue, String surveyStartDate,
			String surveyEndDate, int regionId, int rustTypeId, Integer start, Integer length);

}
