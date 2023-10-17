package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.QuestionnaireBean;
import com.csmpl.wrsis.webportal.entity.Question;

public interface QuestionService {

	String saveAndUpdateQustionAndQustionOption(QuestionnaireBean qBean);

	String viewQustionByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	String searchQustionByPage(String qustionType, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable);

	QuestionnaireBean getQustionById(Integer qustionId);

	String checkDuplicateOrderNumberByQustionType(Integer orderId, Integer questionType);

	boolean checkOrderNumberIsExists(Integer orderNumber, Integer questionType, Integer questionId);

	List<Question> viewAllWebQuestion();

	List<Question> viewAllAPIQuestion();
}
