package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.entity.Month;

public interface MonthService {

	List<Month> viewAllMonth();
	Month getMonthById(Integer monthId);
}
