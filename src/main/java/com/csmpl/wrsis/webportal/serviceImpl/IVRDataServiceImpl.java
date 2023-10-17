package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
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
import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.service.IVRDataService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class IVRDataServiceImpl implements IVRDataService {
	private static final Logger LOG = LoggerFactory.getLogger(IVRDataServiceImpl.class);
	@Autowired
	IVRDataRepository iVRDataRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	QuestionOptionRepository questionOptionRepository;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	IVRDataQuestionRepository iVRDataQuestionRepository;

	@Override
	public IVRDataEntity saveAndUpdateIvrData(IVRDataEntity ivr) {
		IVRDataEntity d = null;
		try {
			ivr.setCreatedOn(new Date());
			ivr.setStatus(false);
			d = iVRDataRepository.saveAndFlush(ivr);
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::saveAndupdateIvrData():" + e);
		}

		return d;
	}

	@Override
	public String viewIVRFileDataByPage(Integer pageSize, Integer pageNumber, Pageable pageable, Integer ivrFileId) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Object[]> page = iVRDataRepository.viewIVRQustionDetailsByPage(
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), ivrFileId);

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

				for (Object[] ivr : page) {
					jsonObject = new JSONObject();
					if (ivr != null) {

						jsonObject.put("mobile", ivr[0]);
						if (ivr[1] != null) {
							SimpleDateFormat formatter = new SimpleDateFormat(
									WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
							jsonObject.put(WrsisPortalConstant.CALL_DATE, formatter.format(ivr[1]));
						} else {
							jsonObject.put(WrsisPortalConstant.CALL_DATE, "-NA-");
						}

						Object regID = ivr[2];
						if (!regID.toString().equals("0")) {
							DemographicEntity region = demographicRepository.getOne(Integer.parseInt(regID.toString()));
							if (region != null) {
								jsonObject.put(WrsisPortalConstant.REGION_NAME, region.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.REGION_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.REGION_NAME, "-NA-");
						}
						Object zoneId = ivr[3];

						if (!zoneId.toString().equals("0")) {
							DemographicEntity zone = demographicRepository.getOne(Integer.parseInt(zoneId.toString()));
							if (zone != null) {
								jsonObject.put(WrsisPortalConstant.ZONE_NAME, zone.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.ZONE_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.ZONE_NAME, "-NA-");
						}
						Object woredaID = ivr[4];

						if (!woredaID.toString().equals("0")) {
							DemographicEntity woreda = demographicRepository
									.getOne(Integer.parseInt(woredaID.toString()));
							if (woreda != null) {
								jsonObject.put(WrsisPortalConstant.WOREDA_NAME, woreda.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.WOREDA_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.WOREDA_NAME, "-NA-");
						}

						Object qust = ivr[5];

						String[] pageQuestion = qust.toString().split(",");

						Object data_ques = ivr[6];

						String[] d_question = data_ques.toString().split(",");

						// Here will be change according to the question
						JSONArray pageQList = new JSONArray();
						for (int i = 0; i < pageQuestion.length; i++) {
							Question hQuestion = questionRepository.getOne(Integer.parseInt(pageQuestion[i]));
							if (hQuestion != null) {
								pageQList.put(hQuestion.getQustion());
							} else {
								pageQList.put("-NA-");
							}
						}
						jObject.put("PageQuestions", pageQList);

						// Here will be change according to the question End

						JSONArray jsa = new JSONArray();
						for (int j = 0; j < d_question.length; j++) {

							IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
									.getOne(Integer.parseInt(d_question[j]));
							String question_option = null;
							List<QuestionOption> optionList = questionOptionRepository
									.findByQustionId(ivrQuestionEntity.getQustionId());
							for (QuestionOption qOption : optionList) {
								if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
									question_option = qOption.getOptionInfo();
								}
							}
							if (question_option != null) {
								jsa.put(question_option);
							} else {
								jsa.put(ivrQuestionEntity.getQustionResponse());
							}
						}
						jsonObject.put(WrsisPortalConstant.QUESTIONS, jsa);
						array.put(jsonObject);
					}
					jObject.put("ivrQuestionList", array);

				}
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, 0);
			}
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::viewIVRFileDataByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searachIVRFileDataByPage(Integer pageSize, Integer pageNumber, Pageable pageable, Integer ivrFileId,
			String phoneNumber, Integer regionId, Integer zonee, Integer woredaId) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<Object[]> page = null;
		try {
			if (!"".equalsIgnoreCase(phoneNumber) && phoneNumber != null) {
				page = iVRDataRepository.searchIVRQustionDetailsByMobileNumberPage(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), ivrFileId,
						phoneNumber.trim());
			}
			if (regionId != null && regionId != 0) {
				page = iVRDataRepository.searchIVRQustionDetailsByRegionIdPage(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), ivrFileId, regionId);
			}
			if (regionId != null && regionId != 0 && zonee != null && zonee != 0) {
				page = iVRDataRepository.searchIVRQustionDetailsByZoneIdPage(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), ivrFileId, regionId, zonee);
			}
			if (regionId != null && zonee != null && woredaId != null) {
				page = iVRDataRepository.searchIVRQustionDetailsByWoredaIdPage(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), ivrFileId, regionId, zonee,
						woredaId);
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

				for (Object[] ivr : page) {
					jsonObject = new JSONObject();
					if (ivr != null) {

						jsonObject.put("mobile", ivr[0]);
						if (ivr[1] != null) {
							SimpleDateFormat formatter = new SimpleDateFormat(
									WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
							jsonObject.put("callDate", formatter.format(ivr[1]));
						} else {
							jsonObject.put("callDate", "-NA-");
						}

						Object regID = ivr[2];
						if (!regID.toString().equals("0")) {
							DemographicEntity region = demographicRepository.getOne(Integer.parseInt(regID.toString()));
							if (region != null) {
								jsonObject.put(WrsisPortalConstant.REGION_NAME, region.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.REGION_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.REGION_NAME, "-NA-");
						}
						Object zoneId = ivr[3];
						if (!zoneId.toString().equals("0")) {
							DemographicEntity zone = demographicRepository.getOne(Integer.parseInt(zoneId.toString()));
							if (zone != null) {
								jsonObject.put(WrsisPortalConstant.ZONE_NAME, zone.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.ZONE_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.ZONE_NAME, "-NA-");
						}
						Object woredaID = ivr[4];
						if (!woredaID.toString().equals("0")) {
							DemographicEntity woreda = demographicRepository
									.getOne(Integer.parseInt(woredaID.toString()));
							if (woreda != null) {
								jsonObject.put(WrsisPortalConstant.WOREDA_NAME, woreda.getDemographyName());
							} else {
								jsonObject.put(WrsisPortalConstant.WOREDA_NAME, "-NA-");
							}
						} else {
							jsonObject.put(WrsisPortalConstant.WOREDA_NAME, "-NA-");
						}

						Object qust = ivr[5];
						String[] pageQuestion = qust.toString().split(",");

						Object data_ques = ivr[6];

						String[] d_question = data_ques.toString().split(",");

						// Here will be change according to the question
						JSONArray pageQList = new JSONArray();
						for (int i = 0; i < pageQuestion.length; i++) {
							Question hQuestion = questionRepository.getOne(Integer.parseInt(pageQuestion[i]));
							if (hQuestion != null) {
								pageQList.put(hQuestion.getQustion());
							} else {
								pageQList.put("-NA-");
							}
						}
						jObject.put("PageQuestions", pageQList);

						// Here will be change according to the question End

						JSONArray jsa = new JSONArray();
						for (int j = 0; j < d_question.length; j++) {

							IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
									.getOne(Integer.parseInt(d_question[j]));

							String question_option = null;
							List<QuestionOption> optionList = questionOptionRepository
									.findByQustionId(ivrQuestionEntity.getQustionId());
							for (QuestionOption qOption : optionList) {
								if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
									question_option = qOption.getOptionInfo();
								}
							}
							if (question_option != null) {
								jsa.put(question_option);
							} else {
								jsa.put(ivrQuestionEntity.getQustionResponse());
							}
						}
						jsonObject.put(WrsisPortalConstant.QUESTIONS, jsa);
						array.put(jsonObject);

					}
					jObject.put("ivrQuestionList", array);

				}
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, 0);
			}

		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::searchIVRFileDataByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public List<IvrDataReportBean> viewIvrSurveydata(int start, int len) {
		List<IvrDataReportBean> list = new ArrayList<>();

		try {
			//Either remove or fill this block of code
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::viewIvrSurveyData():" + e);
		}
		return list;

	}

	@Override
	public Integer countIvrData() {

		return iVRDataRepository.countIvrSurveyData();
	}

	@Override
	public List<IvrDataReportBean> getIvrData() {

		return Collections.emptyList();
	}

	@Override
	public List<IvrDataReportBean> viewIvrSurveyReportData(String mobile, Integer region, Integer zone, Integer woreda,
			String startDate, String endDate, Integer start, Integer length) {

		List<IvrDataReportBean> ivrList = new ArrayList<>();
		List<Question> s = questionRepository.viewAllQustionExcel();
		try {
			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {

				sDate = dt2.format(dt1.parse(startDate));

			} else
					sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {

				eDate = dt2.format(dt1.parse(endDate));

			} else
					eDate = endDate;
			List<Object[]> page = iVRDataRepository.viewAllIVRDetailsByPage(mobile, region, zone, woreda, sDate, eDate,
					start, length);
			Integer count = 0;
			for (Object[] ivr : page) {
				IvrDataReportBean bean = new IvrDataReportBean();
				bean.setSlNo((start) + (++count));
				bean.setPhnNo((String) ivr[2]);
				bean.setCallDateView((String) ivr[3]);
				bean.setRegion((String) ivr[4]);
				bean.setZone((String) ivr[5]);
				bean.setWoreda((String) ivr[6]);
				bean.setIvrQustion((String) ivr[7]);
				bean.setPageQustion((String) ivr[8]);
				bean.setUploadedDate((String) ivr[9]);
				bean.setQuestionOption((String) ivr[10]);
				ivrList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::viewIvrSurveyReportData():" + e);

		}try {
			for (int i = 0; i < ivrList.size(); i++) {
				IvrDataReportBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getQuestionOption());

				} catch (Exception e) {
					continue;
				}
				ArrayList<String> ivrQustionValue = new ArrayList<>();

				for (int j = 0; j < jsa.length(); j++) {

					JSONObject objects = jsa.getJSONObject(j);
					String question_option = objects.getString("options");
					String incd_option = objects.getString("incdoption");
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(incd_option);
					}
					
					/*
					Optional<QuestionOption>  data = optionList.stream().filter(opt->opt.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())).findFirst();
					if(data.isPresent())
					{
						question_option = data.get().getOptionInfo();
					}
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(ivrQuestionEntity.getQustionResponse());
					}
                    */
				}

				Integer pageQustionSize = s.size();
				Integer retriveQustionSize = ivrQustionValue.size();
				if (pageQustionSize > retriveQustionSize) {
					Integer addTd = 0;
					addTd = pageQustionSize - retriveQustionSize;
					for (int m = 0; m < addTd; m++) {
						ivrQustionValue.add("");
					}
				}
				String[] array = new String[s.size()];

				for (int n = 0; n < array.length; n++) {
					array[n] = ivrQustionValue.get(n);
				}
				List<String> integerList = new ArrayList<>(Arrays.asList(array));

				qust.setIvrQuestionsData(integerList);
				ivrList.set(i, qust);
			}
		} catch (Exception e) {

			LOG.error("IVRDataServiceImpl::viewIvrSurveyReportData():" + e);

		}
		return ivrList;
	}

	@Override
	public List<IvrDataReportBean> viewIvrSurveySearchReportData(Integer regionId, Integer zoneId, Integer woredaId,
			String mob) {
		List<IvrDataReportBean> beanList = new ArrayList<>();
		List<Object[]> obj = null;
		try {
			if (regionId != 0) {
				obj = iVRDataRepository.viewAllIVRReportByRegionData(regionId);
			}
			if (zoneId != 0) {
				obj = iVRDataRepository.viewAllIVRReportByZoneData(zoneId);
			}
			if (woredaId != 0) {
				obj = iVRDataRepository.viewAllIVRReportByWoredaData(woredaId);
			}
			if (mob != null && !"".equalsIgnoreCase(mob)) {
				obj = iVRDataRepository.viewAllIVRReportByPhoneData(mob.trim());
			}
			if (regionId == 0 && zoneId == 0 && woredaId == 0 && "".equalsIgnoreCase(mob)) {
				obj = iVRDataRepository.viewAllIVRReportDataReport();
			}
			for (Object[] ivr : obj) {

				IvrDataReportBean bean = new IvrDataReportBean();
				bean.setPhnNo(ivr[0].toString());

				Object regID = ivr[2];
				if (!regID.toString().equals("0")) {
					DemographicEntity region = demographicRepository.getOne(Integer.parseInt(regID.toString()));
					if (region != null) {
						bean.setRegion(region.getDemographyName());
					} else {
						bean.setRegion("-NA-");
					}
				} else {
					bean.setRegion("-NA-");
				}

				Object zoneID = ivr[3];
				if (!zoneID.toString().equals("0")) {
					DemographicEntity zone = demographicRepository.getOne(Integer.parseInt(zoneID.toString()));
					if (zone != null) {
						bean.setZone(zone.getDemographyName());
					} else {
						bean.setZone("-NA-");
					}
				} else {
					bean.setZone("-NA-");
				}

				Object woredaID = ivr[4];
				if (!woredaID.toString().equals("0")) {
					DemographicEntity woreda = demographicRepository.getOne(Integer.parseInt(woredaID.toString()));
					if (woreda != null) {
						bean.setWoreda(woreda.getDemographyName());
					} else {
						bean.setWoreda("-NA-");
					}
				} else {
					bean.setWoreda("-NA-");
				}

				String[] d_question = null;
				if (ivr[5].toString() != null) {
					Object data_ques = ivr[5];
					d_question = data_ques.toString().split(",");
					bean.setViewQust(d_question.length);
				}
				ArrayList<String> qustList = new ArrayList<>();
				for (int i = 0; i < d_question.length; i++) {
					Question question = questionRepository.getOne(Integer.parseInt(d_question[i]));
					qustList.add(question.getQustion());

				}
				bean.setQuestionList(qustList);
				ArrayList<String> qResList = new ArrayList<>();
				Object data_ques = ivr[6];

				String[] qustionOptionList = data_ques.toString().split(",");
				for (int j = 0; j < qustionOptionList.length; j++) {

					IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
							.getOne(Integer.parseInt(qustionOptionList[j]));
					String question_option = null;
					List<QuestionOption> optionList = questionOptionRepository
							.findByQustionId(ivrQuestionEntity.getQustionId());
					for (QuestionOption qOption : optionList) {
						if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
							question_option = qOption.getOptionInfo();
						}
					}
					if (question_option != null) {
						qResList.add(question_option);
					} else {
						qResList.add(ivrQuestionEntity.getQustionResponse());
					}
				}

				bean.setqResList(qResList);
				beanList.add(bean);

			}
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::viewIvrSurveySearchReportData():" + e);

		}

		return beanList;
	}

	@Override
	public void ivrExcelFileDownload(HttpServletResponse response) {
		try {
			// Excel Code Start for IVR Data sheet / sheet (0)
			HSSFWorkbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet("IVR Data Format");

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

			Font newFont = workbook.createFont();
			newFont.setBold(true);
			newFont.setColor(IndexedColors.WHITE.getIndex());
			newFont.setFontHeightInPoints((short) 11);
			newFont.setItalic(false);

			headerCellStyle.setFont(newFont);

			// Dynamically question creation
			ArrayList<String> questionData = new ArrayList<>();
			int q = 1;
			List<Question> questionList = questionRepository.viewAllQustionExcel();
			for (Question question : questionList) {
				questionData.add("Question " + q++);
			}
			Row headRow = sheet.createRow(0);

			ArrayList<String> staticData = new ArrayList<>();
			staticData.add("Phone #");
			staticData.add(WrsisPortalConstant.ZONE);
			staticData.add(WrsisPortalConstant.WOREDA);
			staticData.add("Language");
			staticData.addAll(questionData);

			int optionListSize = 0;
			for (Question sm : questionList) {

				List<QuestionOption> optionList = questionOptionRepository.findByQustionId(sm.getQustionId());
				optionListSize = optionList.size();
			}

			for (int i = 0; i < staticData.size(); i++) {
				Cell cell = headRow.createCell(i);

				int index = 0;
				if (i == 1) {
					int j = 0;
					CellRangeAddressList addressList = new CellRangeAddressList(1, 100, i, i);

					List<String> optionList = demographicRepository.getZoneNames();
					String[] optionArray = new String[optionList.size()];
					for (String qOption : optionList) {

						optionArray[index] = qOption;

						index++;
					}

					DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(optionArray);
					DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
					dataValidation.setSuppressDropDownArrow(false);
					sheet.addValidationData(dataValidation);

					j++;

				}
				if (i > 3) {

					int j = 0;
					CellRangeAddressList addressList = new CellRangeAddressList(1, 100, i, i);
					Integer qustionId = questionList.get(j).getQustionId();

					List<QuestionOption> optionList = questionOptionRepository.findByQustionId(qustionId);
					String[] optionArray = new String[optionList.size() + 2];
					for (QuestionOption qOption : optionList) {

						optionArray[index] = qOption.getOptionValue();

						index++;
					}

					optionArray[index] = "#";
					index = index + 1;
					optionArray[index] = "null";

					DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(optionArray);
					DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
					dataValidation.setSuppressDropDownArrow(false);
					sheet.addValidationData(dataValidation);

					j++;
				}
				cell.setCellValue(staticData.get(i));
				cell.setCellStyle(headerCellStyle);
			}

			for (int i = 0; i < staticData.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			// Excel Code Start for Sample Data sheet / sheet (1)

			Sheet sheetSample = workbook.createSheet("Sample Data Format");
			headRow = sheetSample.createRow(0);
			String[] columns = { "Phone #", WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA, "Language", "Q1", "Q2", "Q3" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNo = 1;
			int count = 1;

			// Demo data Start
			Row row1 = sheetSample.createRow(rowNo++);
			row1.createCell(0).setCellValue("90401555236");
			row1.createCell(1).setCellValue("Zone Name");
			row1.createCell(2).setCellValue("Woreda Name");
			row1.createCell(3).setCellValue(WrsisPortalConstant.ENGLISH);
			row1.createCell(4).setCellValue("yes");
			row1.createCell(5).setCellValue("no");
			row1.createCell(6).setCellValue("null");

			Row row2 = sheetSample.createRow(rowNo++);
			row2.createCell(0).setCellValue("90401225236");
			row2.createCell(1).setCellValue("Zone Name ");
			row2.createCell(2).setCellValue("Woreda Name");
			row2.createCell(3).setCellValue(WrsisPortalConstant.ENGLISH);
			row2.createCell(4).setCellValue("yes");
			row2.createCell(5).setCellValue("both");
			row2.createCell(6).setCellValue("#");

			Row row3 = sheetSample.createRow(rowNo++);
			row3.createCell(0).setCellValue("90401245236");
			row3.createCell(1).setCellValue("Zone Name ");
			row3.createCell(2).setCellValue("Woreda Name");
			row3.createCell(3).setCellValue(WrsisPortalConstant.ENGLISH);
			row3.createCell(4).setCellValue("#");
			row3.createCell(5).setCellValue("both");
			row3.createCell(6).setCellValue("null");

			// Demo data End

			// Table Question for dispaly
			int qustRowNo = 7;
			Row Qrow = sheetSample.createRow(qustRowNo);
			Qrow.createCell(0).setCellValue("Sl No.");
			Qrow.getCell(0).setCellStyle(headerCellStyle);
			Qrow.createCell(1).setCellValue(WrsisPortalConstant.QUESTIONS);
			Qrow.getCell(1).setCellStyle(headerCellStyle);

			// questionList
			int qustRowTableNo = 8;
			int countQ = 1;
			for (Question qst : questionList) {
				Row rowTableQu = sheetSample.createRow(qustRowTableNo++);
				rowTableQu.createCell(0).setCellValue(Integer.toString(countQ++));
				rowTableQu.createCell(1).setCellValue(qst.getQustion());

			}
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			String FILENAME = "IVR-Formt.xls";
			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			try {
				workbook.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				workbook.close();
			} catch (IOException e) {

				LOG.error("IVRDataServiceImpl::ivrExcelFileDownload():" + e);

			}
			// Excel Code End

		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::ivrExcelFileDownload():" + e);

		}

	}

	@Override
	public List<ImportIVRFileBean> viewIVRFileDataByPageTest(Integer fileId) {

		List<ImportIVRFileBean> dataList = new ArrayList<>();
		try {
			String mobile = "";
			Integer region = 0;
			Integer zone = 0;
			Integer woreda = 0;
			List<Object[]> page = iVRDataRepository.viewIVRQustionDetailsByPageViewDetails(mobile, region, zone, woreda,
					fileId);

			for (Object[] ivr : page) {
				ImportIVRFileBean bean = new ImportIVRFileBean();
				bean.setMobile((String) ivr[2]);
				bean.setCallDate((String) ivr[3]);
				bean.setRegionName((String) ivr[4]);
				bean.setZoneName((String) ivr[5]);
				bean.setWoredaName((String) ivr[6]);
				bean.setIvrQustion((String) ivr[7]);
				bean.setPageQustion((String) ivr[8]);
				bean.setQuestionOption((String) ivr[9]);
				if(( bean.getRegionName() == null || "".equalsIgnoreCase( bean.getRegionName()))) {
					bean.setRegionName((String) ivr[10]);
				}
				if(( bean.getZoneName() == null || "".equalsIgnoreCase( bean.getZoneName()))) {
					bean.setZoneName((String) ivr[11]);
				}
				if(( bean.getWoredaName() == null || "".equalsIgnoreCase( bean.getWoredaName()))) {
					bean.setWoredaName((String) ivr[12]);
				}
				dataList.add(bean);
			}
		} catch (Exception e) {

			LOG.error("IVRDataServiceImpl::viewIVRFileDataByPageTest():" + e);

		}
		return dataList;

	}

	@Override
	public List<ImportIVRFileBean> searachIVRFileDataByPageTest(Integer fileId, String phoneNumber, Integer regionId,
			Integer zoneId, Integer woredaId) {

		List<ImportIVRFileBean> dataList = new ArrayList<>();
		try {

			List<Object[]> page = iVRDataRepository.viewIVRQustionDetailsByPageViewDetails(phoneNumber, regionId,
					zoneId, woredaId, fileId);

			for (Object[] ivr : page) {
				ImportIVRFileBean bean = new ImportIVRFileBean();
				bean.setMobile((String) ivr[2]);
				bean.setRegionName((String) ivr[4]);
				bean.setZoneName((String) ivr[5]);
				bean.setWoredaName((String) ivr[6]);
				bean.setIvrQustion((String) ivr[7]);
				bean.setPageQustion((String) ivr[8]);
				dataList.add(bean);
			}
		} catch (Exception e) {

			LOG.error("IVRDataServiceImpl::searachIVRFileDataByPageTest():" + e);
		}
		return dataList;

	}

	@Override
	public Integer viewIvrSurveyReportDataCount(String mobNo, Integer region, Integer zone, Integer woreda,
			Integer pageStart, Integer pageLength) {

		return null;
	}

	@Override
	public List<IvrDataReportBean> ivrSurveyAPIData(String mobile, Integer region, Integer zone, Integer woreda,
			String startDate, String endDate, Integer start, Integer length) {
		List<IvrDataReportBean> ivrList = new ArrayList<>();
		List<Question> s = questionRepository.viewAllAPIQuestion();
		Date currentDate = new Date();
		try {
			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {

				sDate = dt2.format(dt1.parse(startDate));

			} else
					sDate = startDate;
				//sDate = DateUtil.DateToString(currentDate, WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {

				eDate = dt2.format(dt1.parse(endDate));

			} else
				
					eDate = endDate;
			//eDate = DateUtil.DateToString(currentDate, WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			
			List<Object[]> page = iVRDataRepository.ivrSurveyAPIData(mobile, region, zone, woreda, sDate, eDate,start, length);
			Integer count = 0;
			for (Object[] ivr : page) {
				IvrDataReportBean bean = new IvrDataReportBean();
				bean.setSlNo((start) + (++count));
				bean.setPhnNo((String) ivr[2]);
				bean.setCallDateView((String) ivr[3]);
				bean.setRegion((String) ivr[4]);
				bean.setZone((String) ivr[5]);
				bean.setWoreda((String) ivr[6]);
				bean.setIvrQustion((String) ivr[7]);
				bean.setPageQustion((String) ivr[8]);
				bean.setUploadedDate((String) ivr[9]);
				bean.setQuestionOption((String) ivr[10]);
				if(bean.getRegion() == null || "".equalsIgnoreCase( bean.getRegion())) {
					bean.setRegion((String) ivr[11]);
				}
				if(bean.getZone() == null || "".equalsIgnoreCase( bean.getZone())) {
					bean.setZone((String) ivr[12]);
				}
				if(bean.getWoreda() == null || "".equalsIgnoreCase( bean.getWoreda())) {
					bean.setWoreda((String) ivr[13]);
				}
				bean.setResponded((String) ivr[14]);
				
				ivrList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("IVRDataServiceImpl::ivrSurveyAPIData():" + e);

		}try {
			for (int i = 0; i < ivrList.size(); i++) {
				IvrDataReportBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getQuestionOption());

				} catch (Exception e) {
					continue;
				}
				ArrayList<String> ivrQustionValue = new ArrayList<>();

				for (int j = 0; j < jsa.length(); j++) {

					JSONObject objects = jsa.getJSONObject(j);
					String question_option = objects.getString("options");
					String incd_option = objects.getString("incdoption");
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(incd_option);
					}
					
					/*
					Optional<QuestionOption>  data = optionList.stream().filter(opt->opt.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())).findFirst();
					if(data.isPresent())
					{
						question_option = data.get().getOptionInfo();
					}
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(ivrQuestionEntity.getQustionResponse());
					}
                    */
				}

				Integer pageQustionSize = s.size();
				Integer retriveQustionSize = ivrQustionValue.size();
				if (pageQustionSize > retriveQustionSize) {
					Integer addTd = 0;
					addTd = pageQustionSize - retriveQustionSize;
					for (int m = 0; m < addTd; m++) {
						ivrQustionValue.add("");
					}
				}
				String[] array = new String[s.size()];

				for (int n = 0; n < array.length; n++) {
					array[n] = ivrQustionValue.get(n);
				}
				List<String> integerList = new ArrayList<>(Arrays.asList(array));

				qust.setIvrQuestionsData(integerList);
				ivrList.set(i, qust);
			}
		} catch (Exception e) {

			LOG.error("IVRDataServiceImpl::ivrSurveyAPIData():" + e);

		}
		return ivrList;
	}

}
