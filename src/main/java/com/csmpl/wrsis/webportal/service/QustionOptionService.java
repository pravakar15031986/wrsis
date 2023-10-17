package com.csmpl.wrsis.webportal.service;

public interface QustionOptionService {

	String getOptionByQustionId(Integer qustionId);

	String removeOptionByOptionId(String optionId, Integer userId);
}
