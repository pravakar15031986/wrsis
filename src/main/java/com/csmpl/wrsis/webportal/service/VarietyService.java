package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.VarietyBean;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;

@Service
public interface VarietyService {

	String addvariety(VarietyBean variety);

	VarietyTypeMasterEntity getVarietyById(Integer id);

	List<VarietyBean> viewVariety(SearchVo searchVo);

}