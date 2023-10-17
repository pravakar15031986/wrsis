package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.entity.FungicideMaster;

public interface FungicideMasterService {

	String saveAndUpdate(FungicideMaster fungicideMaster);

	FungicideMaster getFungicideById(Integer fungicideId);

	String viewFungicideByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	List<FungicideMaster> viewAllFungicide();

	String searchFungicide(String fungicideName, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable);

}
