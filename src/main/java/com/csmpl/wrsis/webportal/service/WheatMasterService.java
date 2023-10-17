package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.entity.WheatMaster;

public interface WheatMasterService {

	String saveAndUpdate(WheatMaster wMaster);

	String viewWheatMasterByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	List<WheatMaster> viewAllWheatType();

	WheatMaster getWheatById(Integer wheatTypeId);

	String searchWheatByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String wheatName, String status);

}
