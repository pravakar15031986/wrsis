package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.wrsis.webportal.entity.Month;
import com.csmpl.wrsis.webportal.repository.MonthRepository;
import com.csmpl.wrsis.webportal.service.MonthService;

@Service
public class MonthServiceImpl implements MonthService {

	private static final Logger LOG = LoggerFactory.getLogger(MonthServiceImpl.class);
	@Autowired
	MonthRepository monthRepository;

	@Override
	public List<Month> viewAllMonth() {
		List<Month> monthList = null;
		try {
			monthList = monthRepository.viewMonth();
		} catch (Exception e) {
			LOG.error("MonthServiceImpl::viewAllMonth():" + e);
		}
		return monthList;
	}

	@Override
	public Month getMonthById(Integer monthId) {
		Month month = null;
		try {
			month = monthRepository.findByMonthId(monthId);
		} catch (Exception e) {
			LOG.error("MonthServiceImpl::getMonthById():" + e);
		}
		return month;
	}

}
