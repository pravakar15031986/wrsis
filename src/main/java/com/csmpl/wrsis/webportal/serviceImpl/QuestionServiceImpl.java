package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.OptionBean;
import com.csmpl.wrsis.webportal.bean.QuestionnaireBean;
import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentDetailsRepository;
import com.csmpl.wrsis.webportal.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

	private static final Logger LOG = LoggerFactory.getLogger(QuestionServiceImpl.class);

	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	QuestionOptionRepository questionOptionRepository;
	@Autowired
	IVRDataQuestionRepository iVRDataQuestionRepository;
	@Autowired
	RustIncidentDetailsRepository rustIncidentDetailsRepository;

	@Override
	public String saveAndUpdateQustionAndQustionOption(QuestionnaireBean qBean) {
		String res = "";
		try {
			if (qBean.getQustionBeanId() == 0) {
				Question q = new Question();
				q.setQustion(qBean.getQustionBean());
				q.setQustionType(qBean.getQustionTypeBean());
				q.setQustionOrder(qBean.getQustionOrderBean());
				q.setQustionOptionCount(qBean.getQustionOptionCountBean());
				q.setCreatedBy(qBean.getCreatedByBean());
				q.setStatus(qBean.isStatusBean());
				q.setCreatedOn(new Date());

				Question qst = questionRepository.save(q);
				int orderSeq = 0;
				QuestionOption opObject = null;
				for (OptionBean dObj : qBean.getOptionBeanList()) {
					orderSeq = orderSeq + 1;
					opObject = new QuestionOption();
					LOG.info("Option Number==" + orderSeq);
					
					opObject.setOptionNumber(orderSeq);
					opObject.setOptionValue(dObj.getqValue());
					Question q1 = new Question();
					q1.setQustionId(qst.getQustionId());
					opObject.setQustionId(q1);

					opObject.setOptionInfo(dObj.getqInfo());
					opObject.setCreatedBy(qBean.getCreatedByBean());
					opObject.setCreatedOn(new Date());
					questionOptionRepository.saveAndFlush(opObject);
				}

				if (qst != null)
					res = WrsisPortalConstant.SAVE;
				else
					res = WrsisPortalConstant.FAILURE;
			} else {
				List<IVRDataQuestionEntity> list1 = iVRDataQuestionRepository
						.findQustionIdByQustionId(qBean.getQustionBeanId());

				List<Integer> list2 = rustIncidentDetailsRepository.findQuesIdByQuesId(qBean.getQustionBeanId());

				if (!qBean.isStatusBean() ) {
					// Check order number before update if order number is active
					boolean orderNumberStatus = checkOrderNumberIsExists(qBean.getQustionOrderBean(),
							qBean.getQustionTypeBean(), qBean.getQustionBeanId());
					
					if (orderNumberStatus  && !qBean.isStatusBean()) {
						res = "orderNuumberFail";
					} else {
						// Update Question And Option
						Question qstion = null;
						Question qst = questionRepository.getOne(qBean.getQustionBeanId());

						int x = qst.getQustionOptionCount();
						if (qst != null) {
							Question q = new Question();
							q.setQustion(qBean.getQustionBean());
							q.setQustionType(qBean.getQustionTypeBean());
							q.setQustionOrder(qBean.getQustionOrderBean());
							q.setQustionOptionCount(qBean.getQustionOptionCountBean());
							q.setUpdatedBy(qBean.getCreatedByBean());
							q.setStatus(qBean.isStatusBean());
							q.setUpdatedOn(new Date());
							q.setCreatedBy(qst.getCreatedBy());
							q.setCreatedOn(qst.getCreatedOn());
							q.setQustionId(qst.getQustionId());
							qstion = questionRepository.save(q);

							
							if (qBean.getOptionBeanList().size() != x) {
								// Option Number Not Match with Coming Request Then it status is true

								QuestionOption qOption = null;
								List<QuestionOption> optionList = questionOptionRepository
										.findByQustionId(qst.getQustionId());
								for (QuestionOption quOption : optionList) {
									qOption = new QuestionOption();
									QuestionOption op = questionOptionRepository.getOne(quOption.getOptionId());
									qOption.setCreatedBy(op.getCreatedBy());
									qOption.setUpdatedBy(qBean.getCreatedByBean());
									qOption.setCreatedOn(op.getCreatedOn());
									qOption.setUpdatedOn(new Date());
									qOption.setQustionId(op.getQustionId());
									qOption.setOptionNumber(op.getOptionNumber());
									qOption.setOptionValue(op.getOptionValue());
									qOption.setOptionInfo(op.getOptionInfo());
									qOption.setOptionId(op.getOptionId());
									qOption.setStatus(true);

									questionOptionRepository.save(qOption);
								}

								// Option Number Not Match with Coming Request Then Again Save New Option
								int orderSeq = 0;
								QuestionOption opObject = null;
								for (OptionBean dObj : qBean.getOptionBeanList()) {
									orderSeq = orderSeq + 1;
									opObject = new QuestionOption();
									
									opObject.setOptionNumber(orderSeq);
									opObject.setOptionValue(dObj.getqValue());
									Question q1 = new Question();
									q1.setQustionId(qst.getQustionId());
									opObject.setQustionId(q1);
									opObject.setOptionInfo(dObj.getqInfo());
									opObject.setCreatedBy(qBean.getCreatedByBean());
									opObject.setCreatedOn(new Date());
									questionOptionRepository.saveAndFlush(opObject);

									Question questionMain = questionRepository.getOne(qst.getQustionId());
									questionMain.setQustionOptionCount(orderSeq);
									questionMain.setQustionId(qst.getQustionId());
									questionRepository.save(questionMain);

								}

							} else {
								// If Option Number As Table and coming request then it only update
								// corresponding Record.

								for (OptionBean dObj : qBean.getOptionBeanList()) {
									QuestionOption matchOptio = questionOptionRepository.getOne(dObj.getOptinId());
									if (matchOptio != null) {
										matchOptio.setOptionValue(dObj.getqValue());
										matchOptio.setOptionInfo(dObj.getqInfo());
										matchOptio.setUpdatedOn(new Date());
										matchOptio.setUpdatedBy(qBean.getCreatedByBean());
										questionOptionRepository.saveAndFlush(matchOptio);
									}

								}
							}

							if (qstion != null)
								res = WrsisPortalConstant.UPDATE;
							else
								res = WrsisPortalConstant.FAILURE;
						}
					}
				} else {
					if (list1.isEmpty() && list2.isEmpty()) {
						// Check order number before update if order number is active
						boolean orderNumberStatus = checkOrderNumberIsExists(qBean.getQustionOrderBean(),
								qBean.getQustionTypeBean(), qBean.getQustionBeanId());
						
						if (orderNumberStatus  && !qBean.isStatusBean()) {
							res = "orderNuumberFail";
						} else {
							// Update Question And Option
							Question qstion = null;
							Question qst = questionRepository.getOne(qBean.getQustionBeanId());
							LOG.info("Q Table Option==" + qst.getQustionOptionCount());

							int x = qst.getQustionOptionCount();
							if (qst != null) {
								Question q = new Question();
								q.setQustion(qBean.getQustionBean());
								q.setQustionType(qBean.getQustionTypeBean());
								q.setQustionOrder(qBean.getQustionOrderBean());
								q.setQustionOptionCount(qBean.getQustionOptionCountBean());
								q.setUpdatedBy(qBean.getCreatedByBean());
								q.setStatus(qBean.isStatusBean());
								q.setUpdatedOn(new Date());
								q.setCreatedBy(qst.getCreatedBy());
								q.setCreatedOn(qst.getCreatedOn());
								q.setQustionId(qst.getQustionId());
								qstion = questionRepository.save(q);

								LOG.info("Page Optin==" + qBean.getOptionBeanList().size());

								
								if (qBean.getOptionBeanList().size() != x) {
									// Option Number Not Match with Coming Request Then it status is true

									QuestionOption qOption = null;
									List<QuestionOption> optionList = questionOptionRepository
											.findByQustionId(qst.getQustionId());
									for (QuestionOption quOption : optionList) {
										qOption = new QuestionOption();
										QuestionOption op = questionOptionRepository.getOne(quOption.getOptionId());
										qOption.setCreatedBy(op.getCreatedBy());
										qOption.setUpdatedBy(qBean.getCreatedByBean());
										qOption.setCreatedOn(op.getCreatedOn());
										qOption.setUpdatedOn(new Date());
										qOption.setQustionId(op.getQustionId());
										qOption.setOptionNumber(op.getOptionNumber());
										qOption.setOptionValue(op.getOptionValue());
										qOption.setOptionInfo(op.getOptionInfo());
										qOption.setOptionId(op.getOptionId());
										qOption.setStatus(true);

										questionOptionRepository.save(qOption);
									}

									// Option Number Not Match with Coming Request Then Again Save New Option
									int orderSeq = 0;
									QuestionOption opObject = null;
									for (OptionBean dObj : qBean.getOptionBeanList()) {
										orderSeq = orderSeq + 1;
										opObject = new QuestionOption();
										
										opObject.setOptionNumber(orderSeq);
										opObject.setOptionValue(dObj.getqValue());
										Question q1 = new Question();
										q1.setQustionId(qst.getQustionId());
										opObject.setQustionId(q1);
										opObject.setOptionInfo(dObj.getqInfo());
										opObject.setCreatedBy(qBean.getCreatedByBean());
										opObject.setCreatedOn(new Date());
										questionOptionRepository.saveAndFlush(opObject);

										Question questionMain = questionRepository.getOne(qst.getQustionId());
										questionMain.setQustionOptionCount(orderSeq);
										questionMain.setQustionId(qst.getQustionId());
										questionRepository.save(questionMain);

									}

								} else {
									// If Option Number As Table and coming request then it only update
									// corresponding Record.

									for (OptionBean dObj : qBean.getOptionBeanList()) {
										QuestionOption matchOptio = questionOptionRepository.getOne(dObj.getOptinId());
										if (matchOptio != null) {
											matchOptio.setOptionValue(dObj.getqValue());
											matchOptio.setOptionInfo(dObj.getqInfo());
											matchOptio.setUpdatedOn(new Date());
											matchOptio.setUpdatedBy(qBean.getCreatedByBean());
											questionOptionRepository.saveAndFlush(matchOptio);
										}

									}
								}

								if (qstion != null)
									res = WrsisPortalConstant.UPDATE;
								else
									res = WrsisPortalConstant.FAILURE;
							}
						}
					} else {
						return WrsisPortalConstant.DEPENDENT;
					}
				}
			}
		} catch (Exception e) {
			LOG.error("QuestionServiceImpl::saveAndUpdateQustionAndQustionOption():" + e);
			res = WrsisPortalConstant.FAILURE;
		}
		return res;
	}

	private Integer getQustionMaxId() {
		Integer id = 0;
		try {
			id = questionRepository.findMaxQustionId();
			if (id == null) {
				id = 1;
			} else {
				id = id + 1;
			}
		} catch (Exception e) {
			LOG.error("QuestionServiceImpl::getQuestionMaxId():" + e);

		}
		return id;
	}

	@Override
	public String viewQustionByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Question> page = questionRepository
					.viewAllQustionPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put("currentPage", currentPage);
				jObject.put("startPage", begingPage);
				jObject.put("endPage", endPage);
				jObject.put("pageNo", page.getNumber());
				jObject.put("showRowNo", page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;
				
				for (Question question : page) {
					jsonObject = new JSONObject();
					if (question != null) {
						jsonObject.put("id", question.getQustionId() != 0 ? question.getQustionId() : "-NA-");
						jsonObject.put("name", question.getQustion() != null ? question.getQustion() : "-NA-");
						if (question.getQustionType() == 1) {
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "IVR");
						} else if(question.getQustionType() == 2) {
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "Mobile");
						}else if(question.getQustionType() == 3) {
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "IVR API");
						}
						jsonObject.put("orderNumber", question.getQustionOrder());
						jsonObject.put("optionNumber", question.getQustionOptionCount());
						jsonObject.put("questionType", question.getQustionType());

						if (!question.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("qustionList", array);

			}

		} catch (Exception e) {
			LOG.error("QuestionServiceImpl::viewQustionByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public String searchQustionByPage(String qustionType, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<Question> page = null;
		try {
			if (!status.equals(WrsisPortalConstant.SELECT) && !qustionType.equals("0")) {
				page = questionRepository.findByQustionTypeAndStatus(Integer.parseInt(qustionType),
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (page == null) {
				if (!status.equals(WrsisPortalConstant.SELECT)) {
					page = questionRepository.findBystatus(Boolean.parseBoolean(status),
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
				if (!qustionType.equals("0")) {
					page = questionRepository.findByqustionType(Integer.parseInt(qustionType),
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
			}
			if (status.equals(WrsisPortalConstant.SELECT) && qustionType.equals("0")) {
				page = questionRepository
						.viewAllQustionPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put("currentPage", currentPage);
				jObject.put("startPage", begingPage);
				jObject.put("endPage", endPage);
				jObject.put("pageNo", page.getNumber());
				jObject.put("showRowNo", page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;
				for (Question question : page) {
					jsonObject = new JSONObject();
					if (question != null) {
						jsonObject.put("id", question.getQustionId() != 0 ? question.getQustionId() : "-NA-");
						jsonObject.put("name", question.getQustion() != null ? question.getQustion() : "-NA-");
						if (question.getQustionType() == 1) {
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "IVR");
						} else if(question.getQustionType() == 2){
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "Mobile");
						} else if(question.getQustionType() == 3){
							jsonObject.put(WrsisPortalConstant.QUESTION_TYPE, "IVR API");
						}
						jsonObject.put("orderNumber", question.getQustionOrder());
						jsonObject.put("optionNumber", question.getQustionOptionCount());
						jsonObject.put("questionType", question.getQustionType());
						if (!question.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("qustionList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}

		} catch (Exception e) {
			LOG.error("QuestionServiceImpl::searchQustionByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public QuestionnaireBean getQustionById(Integer qustionId) {

		QuestionnaireBean qBean = null;
		try {
			Question qustion = questionRepository.getOne(qustionId);
			if (qustion != null) {
				qBean = new QuestionnaireBean();
				qBean.setQustionBeanId(qustion.getQustionId());
				qBean.setQustionBean(qustion.getQustion());
				qBean.setQustionOrderBean(qustion.getQustionOrder());
				qBean.setQustionOptionCountBean(qustion.getQustionOptionCount());
				qBean.setStatusBean(qustion.isStatus());
				qBean.setQustionTypeBean(qustion.getQustionType());
			}
		} catch (Exception e) {
			LOG.error("QuestionServiceImpl::getQuestionById():" + e);

		}
		return qBean;
	}

	@Override
	public String checkDuplicateOrderNumberByQustionType(Integer orderId, Integer questionType) {

		JSONObject joObject = new JSONObject();
		
		try {
			Question questionList = questionRepository.validateQustionOrderAndqustionType(orderId, questionType);
			if (questionList != null) {
				joObject.put("isExistOrder", "exist");
			}
			
		} catch (Exception e) {

			LOG.error("QuestionServiceImpl::checkDuplicateOrderNumberByQustionType():" + e);

		}
		return joObject.toString();
	}

	@Override
	public boolean checkOrderNumberIsExists(Integer orderNumber, Integer questionType, Integer questionId) {
		boolean status = false;
		LOG.info("From Page==" + questionId);
		
		Question questionList = null;
		try {
			questionList = questionRepository.validateQustionOrderAndqustionType(orderNumber, questionType);
			
			if (questionList != null && questionList.getQustionId() != questionId) {
				LOG.info("Status==" + questionList.isStatus() + "\t" + questionList.getQustionId());
				status = true;
			} else {
				LOG.info("Status==" + questionList.isStatus() + "\t" + questionList.getQustionId());
				status = false;
			}

			
		} catch (Exception e) {

			LOG.error("QuestionServiceImpl::checkOrderNumberIsExists():" + e);
		}
		return status;
	}

	@Override
	public List<Question> viewAllWebQuestion() {

		return questionRepository.viewAllQustionExcel();
	}

	@Override
	public List<Question> viewAllAPIQuestion() {
		return questionRepository.viewAllAPIQuestion();
	}
}
