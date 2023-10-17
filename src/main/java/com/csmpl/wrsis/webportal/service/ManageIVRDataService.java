package com.csmpl.wrsis.webportal.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;

import com.csmpl.wrsis.webportal.bean.IVRExcelCellBean;
import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.ImportIVRFile;

public interface ManageIVRDataService {

	List<IVRExcelCellBean> readIvrExcel(ImportIVRFileBean fileName);

	String saveIVRFile(ImportIVRFileBean fileName);

	String viewIVRFileDataByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	void downloadIVRFile(HttpServletResponse response, Integer fileId, ModelMap model);

	String searchIVRFileDataByDatePage(Integer pageSize, Integer pageNumber, Pageable pageable, Date fromDate,
			Date toDate);

	ImportIVRFile getQustionDetailsByImprtIVRId(Integer imprtIVRId);

	void iVRFileDataByExcel(HttpServletResponse response, Integer region, Integer zone, Integer woreda, String mob);

	String ivrFileExits(Integer fileId);

	List<IvrDataReportBean> viewIvrDataPage(String startDate, String endDate, Integer valueOf, Integer valueOf2);

	Integer countIvrDataPage(String startDate, String endDate, int i, int j);

	String saveIvrDataByApi(ImportIVRFileBean ivrData);
	
}
