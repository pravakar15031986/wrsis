package com.csmpl.wrsis.webportal.service;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.entity.DiseaseMaster;

public interface DiseaseMasterService {

	String saveAndUpdate(DiseaseMaster diseaseMaster);
	DiseaseMaster getDiseaseMasterById(Integer diseaseId);
	String viewDiseaseMasterByPage(Integer pageSize,Integer pageNumber,Pageable pageable);
	String searchDiseaseMasterByPage(String fName,String sName,String status,Integer pageSize,Integer pageNumber,Pageable pageable);
}
