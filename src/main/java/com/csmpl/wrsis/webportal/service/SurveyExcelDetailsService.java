package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyExcelFilesBean;
import com.csmpl.wrsis.webportal.bean.SuveyDetailsSampleReportDataBean;
import com.csmpl.wrsis.webportal.bean.SuveyOtherDiseaseReportDataBean;

public interface SurveyExcelDetailsService {

	HSSFWorkbook createExcelSurvey(HSSFWorkbook workBook, List<SurveyDetailsBean> bean);

	HSSFWorkbook createExcelGlobalRust(HSSFWorkbook workBook, List<SurveyDetailsBean> bean);

	List<SuveyDetailsSampleReportDataBean> createGRTableSurvey(List<SurveyDetailsBean> bean);

	List<SuveyDetailsSampleReportDataBean> createRustSurveyOtherDiseaseTable(List<SurveyDetailsBean> bean);

	HSSFWorkbook createExcelRustSurveyOtherDisease(HSSFWorkbook workBook, List<SurveyDetailsBean> bean);

	List<SuveyOtherDiseaseReportDataBean> createCsvRustSurveyOtherDiseaseTable(List<SurveyDetailsBean> bean);

	HSSFWorkbook createCsvRustSurveyOtherDisease(HSSFWorkbook workBook, List<SurveyDetailsBean> bean);

	HSSFWorkbook createCsvGlobalRustSurvey(HSSFWorkbook workBook, List<SurveyDetailsBean> bean);

	List<SurveyExcelFilesBean> viewAllSurveyFileDetails(Integer userId, String startDate, String endDate,
			Integer pagesize, Integer pageLength);

	Integer viewAllSurveyFileDetailsCount(Integer userId, String startDate, String endDate, Integer pagesize,
			Integer pageLength);
}
