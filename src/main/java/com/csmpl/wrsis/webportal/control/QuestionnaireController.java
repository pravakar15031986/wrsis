package com.csmpl.wrsis.webportal.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.OptionBean;
import com.csmpl.wrsis.webportal.bean.QuestionnaireBean;
import com.csmpl.wrsis.webportal.service.QuestionService;
import com.csmpl.wrsis.webportal.service.QustionOptionService;
import com.csmpl.wrsis.webportal.util.Validation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class QuestionnaireController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireController.class);
	@Autowired
	QuestionService questionService;
	@Autowired
	QustionOptionService qustionOptionService;

	@RequestMapping(value = "/questionnaireMaster", method = RequestMethod.GET)
	public String questionnaireMaster(Model model, @ModelAttribute(WrsisPortalConstant.QUESTION) QuestionnaireBean qustion) {
		model.addAttribute(WrsisPortalConstant.QUESTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_QUESTIONS);
		model.addAttribute(WrsisPortalConstant.OPTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_OPTIONS);

		return "questionnaireMaster";
	}

	@PostMapping("save-Qustion")
	public String saveAandUpdate(Model model, @ModelAttribute(WrsisPortalConstant.QUESTION) QuestionnaireBean qustionBean,
			HttpServletRequest request, RedirectAttributes redirect) {
		String returnPage = "questionnaireMaster";
		model.addAttribute(WrsisPortalConstant.QUESTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_QUESTIONS);
		model.addAttribute(WrsisPortalConstant.OPTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_OPTIONS);

		if (qustionBean.getQustionBeanId() != 0) {
			returnPage = "questionnaireMasterEdit";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			if (!qustionBean.getQustionBean().isEmpty()) {

				qustionBean.setOptionBeanList(extractReqDetailsFromJson(qustionBean.getOptionBeanListString()));

				if (!Validation.validateQuestion(qustionBean.getQustionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Question accept only alphabets and numbers.");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUESTION);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(qustionBean.getQustionBean())
						|| !Validation.validateLastBlankSpace(qustionBean.getQustionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in question");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUESTION);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(qustionBean.getQustionBean())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Question should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.QUESTION);
					return returnPage;
				}
			}

			qustionBean.setCreatedByBean(userId);

			String msg = questionService.saveAndUpdateQustionAndQustionOption(qustionBean);
			if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.QUESTION, new QuestionnaireBean());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Saved Successfully");
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"This Questionnaire cannot be deactive. Already in use somewhere.");
			}
			if (WrsisPortalConstant.UPDATE.equals(msg)) {
				
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				returnPage = "redirect:questionnaireMasterView";
			}
			if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}
			if ("orderNuumberFail".equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Order number already exists");
			}

		} catch (Exception e) {
			LOG.error("QuestionnaireController::saveAandUpdate():" + e);
		}

		return returnPage;
	}

	@RequestMapping(value = "/questionnaireMasterView", method = RequestMethod.GET)
	public String questionnaireMasterView(Model model) {

		return "questionnaireMasterView";
	}

	@ResponseBody
	@GetMapping("/viewAllQustionPage")
	public String viewDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return questionService.viewQustionByPage(pageSize, pageNumber, pageable);
	}

	@ResponseBody
	@GetMapping("/searchAllQustionPage")
	public String searchDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "typeOfQustion", required = false) String typeOfQustion,
			@RequestParam(value = "status", required = false) String status) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("diseaseId"));
		return questionService.searchQustionByPage(typeOfQustion, status, pageSize, pageNumber, pageable);
	}

	@PostMapping("/qustion-edit")
	public String questionnaireMasterEdit(Model model, @RequestParam("qustionId") Integer qustionId) {

		try {
			model.addAttribute(WrsisPortalConstant.QUESTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_QUESTIONS);
			model.addAttribute(WrsisPortalConstant.OPTION_COUNTS, WrsisPortalConstant.wrsisPropertiesFileConstants.WR_NO_OF_OPTIONS);

			QuestionnaireBean q = questionService.getQustionById(qustionId);
			
			model.addAttribute("optionNumber", q.getQustionOptionCountBean());
			model.addAttribute(WrsisPortalConstant.QUESTION, q);
		} catch (Exception e) {
			LOG.error("QuestionnaireController::questionnaireMasterEdit():" + e);
		}
		return "questionnaireMasterEdit";
	}

	@ResponseBody
	@RequestMapping(value = "/viewQustionOptionByQustionId")
	public String getOptionsByQustionId(@Param("qustionId") Integer qustionId, Model model) {

		return qustionOptionService.getOptionByQustionId(qustionId);
	}

	private List<OptionBean> extractReqDetailsFromJson(String optionBeanListString) {
		ObjectMapper mapper = new ObjectMapper();
		List<OptionBean> list = null;
		try {
			list = mapper.readValue(optionBeanListString, new TypeReference<List<OptionBean>>() {
			});

		} catch (JsonParseException e) {
			LOG.error("QuestionnaireController::extractReqDetailsFromJson():" + e);
		} catch (JsonMappingException e) {
			LOG.error("QuestionnaireController::extractReqDetailsFromJson():" + e);
		} catch (IOException e) {
			LOG.error("QuestionnaireController::extractReqDetailsFromJson():" + e);
		}

		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteOptionByOptionId")
	public String deleteOptionByOptionId(@RequestParam("optionId") String optionId, Model model,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);

		return qustionOptionService.removeOptionByOptionId(optionId, userId);
	}

	@ResponseBody
	@GetMapping("/duplicateOrderNumberByQuestionType")
	public String duplicateOrderNumberByQustionType(@RequestParam("orderId") Integer orderId,
			@RequestParam("questionType") Integer questionType) {
		return questionService.checkDuplicateOrderNumberByQustionType(orderId, questionType);
	}
}
