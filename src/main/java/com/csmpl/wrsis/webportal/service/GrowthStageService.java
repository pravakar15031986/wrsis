package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.entity.GrowthStage;

public interface GrowthStageService {

	String saveAndUpdateGrothStage(GrowthStage growthStage);
	GrowthStage getGrothStageById(Integer stageId);
	List<GrowthStage> viewAllGrothStage();
	String searchGrothStage(String stageName,String status,Integer pageSize,Integer pageNumber,Pageable pageable);
	String viewAllGrothByPage(Integer pageSize,Integer pageNumber,Pageable pageable);
}
