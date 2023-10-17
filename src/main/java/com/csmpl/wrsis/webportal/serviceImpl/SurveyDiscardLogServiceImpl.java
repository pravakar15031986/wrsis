package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.wrsis.webportal.entity.SurveyDiscardLogEntity;
import com.csmpl.wrsis.webportal.repository.SurveyDiscardLogRepository;
import com.csmpl.wrsis.webportal.service.SurveyDiscardLogService;

@Service
public class SurveyDiscardLogServiceImpl implements SurveyDiscardLogService {

	public static final Logger LOG = LoggerFactory.getLogger(SurveyDiscardLogServiceImpl.class);

	@Autowired
	SurveyDiscardLogRepository surveyDiscardLogRepository;

	@Override
	public SurveyDiscardLogEntity saveSurveyDiscardLog(SurveyDiscardLogEntity s) {
		try {
			s.setStatus(false);
			s.setDiscardedOn(new Date());
			surveyDiscardLogRepository.save(s);
		} catch (Exception e) {
			LOG.error("SurveyDiscardLogServiceImpl::saveSurveyDiscardLog():" + e);
		}
		return null;
	}

}
