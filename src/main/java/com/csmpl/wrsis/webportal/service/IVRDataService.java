package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.IVRDataEntity;

public interface IVRDataService {

	IVRDataEntity saveAndUpdateIvrData(IVRDataEntity ivr);
	String viewIVRFileDataByPage(Integer pageSize,Integer pageNumber,Pageable pageable,Integer fileId);
	String searachIVRFileDataByPage(Integer pageSize,Integer pageNumber,Pageable pageable,Integer fileId,String phoneNumber,Integer regionId,Integer zoneId,Integer woredaId);
	List<IvrDataReportBean> getIvrData();
	List<IvrDataReportBean> viewIvrSurveydata(int start,int len);
	Integer countIvrData();
	List<IvrDataReportBean> viewIvrSurveyReportData(String mobNo,Integer region,Integer zone,Integer woreda,String startDate,String endDate,Integer pageStart,Integer pageLength);
	List<IvrDataReportBean> viewIvrSurveySearchReportData(Integer region,Integer zone,Integer woreda,String mob);
	void ivrExcelFileDownload(HttpServletResponse response);
	
	List<ImportIVRFileBean> viewIVRFileDataByPageTest(Integer fileId);
	
	List<ImportIVRFileBean> searachIVRFileDataByPageTest(Integer fileId,String phoneNumber,Integer regionId,Integer zoneId,Integer woredaId);
	Integer viewIvrSurveyReportDataCount(String mobNo,Integer region,Integer zone,Integer woreda,Integer pageStart,Integer pageLength);
	List<IvrDataReportBean> ivrSurveyAPIData(String mobNo,Integer region,Integer zone,Integer woreda,String startDate,String endDate,Integer pageStart,Integer pageLength);
}
