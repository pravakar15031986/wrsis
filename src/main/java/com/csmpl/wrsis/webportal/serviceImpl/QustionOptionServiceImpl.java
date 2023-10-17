package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.service.QustionOptionService;

@Service
public class QustionOptionServiceImpl implements QustionOptionService {

	private static final Logger LOG = LoggerFactory.getLogger(QustionOptionServiceImpl.class);
	@Autowired
	QuestionOptionRepository questionOptionRepository;
	@Autowired
	QuestionRepository questionRepository;

	@Override
	public String getOptionByQustionId(Integer qustionId) {

		JSONObject jsonObject = new JSONObject();
		try {
			List<QuestionOption> optionList = questionOptionRepository.findByQustionId(qustionId);

			if (optionList != null) {
				ArrayList<String> valueList = new ArrayList<>();
				ArrayList<String> infoList = new ArrayList<>();
				ArrayList<Integer> optionIdList = new ArrayList<>();
				for (QuestionOption option : optionList) {
					valueList.add(option.getOptionValue());
					infoList.add(option.getOptionInfo());
					
					jsonObject.put("optionNumber", option.getQustionId().getQustionOptionCount());
					optionIdList.add(option.getOptionId());

				}

				jsonObject.put("valueL", valueList);
				jsonObject.put("valueLSize", valueList.size());
				jsonObject.put("infoL", infoList);
				jsonObject.put("optionId", optionIdList);

			}

		} catch (Exception e) {
			LOG.error("QustionOptionServiceImpl::getOptionByQuestionId():" + e);
		}
		return jsonObject.toString();
	}

	@Override
	public String removeOptionByOptionId(String optionId, Integer userId) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (!optionId.isEmpty()) {
				int optionNumber = 0;
				QuestionOption option = questionOptionRepository.getOne(Integer.parseInt(optionId));

				if (option != null) {
					option.setOptionNumber(option.getOptionNumber());
					option.setStatus(true);
					option.setUpdatedBy(userId);
					option.setUpdatedOn(new Date());
					questionOptionRepository.save(option);
					Question q = questionRepository.getOne(option.getQustionId().getQustionId());

					if (q != null) {
						optionNumber = q.getQustionOptionCount() - 1;
						q.setQustionOptionCount(optionNumber);
						q.setUpdatedBy(userId);
						q.setUpdatedOn(new Date());
						questionRepository.save(q);
					}
					jsonObject.put("potionNumber", optionNumber);

				}
			}

		} catch (Exception e) {
			LOG.error("QustionOptionServiceImpl::removeOptionByQuestionId():" + e);
		}
		return jsonObject.toString();
	}

}
