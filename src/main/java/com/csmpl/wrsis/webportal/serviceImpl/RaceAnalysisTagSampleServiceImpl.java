package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.ResearchCenterBean;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SampleSurveyDetailsEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterSpelizationRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.service.RaceAnalysisTagSampleService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Component
public class RaceAnalysisTagSampleServiceImpl implements RaceAnalysisTagSampleService {

	public static final Logger LOG = LoggerFactory.getLogger(RaceAnalysisTagSampleServiceImpl.class);

	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;
	@Autowired
	SampleSurveyDetailsRepository sampleSurveyDetailsRepository;

	@Autowired
	ResearchCenterSpelizationRepository researchCenterSpelizationRepository;

	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;

	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;

	@Override
	public String saveTagSample(SampleTagBean tagBean, String jsonString) {
		String returnType = WrsisPortalConstant.SUCCESS;
		SampleLabTagDetails tagSample = null;
		SampleSurveyDetailsEntity samplIds = new SampleSurveyDetailsEntity();
		ApprovalAuthorityEntity authorityEntity = null;
		ApprovalAuthorityHistoryEntity history = null;
		Integer processId = Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.TAGSAMPLE_PROCESS_ID);
		try {
			if (jsonString != null) {
				
				JSONObject jsonObj = new JSONObject(jsonString);
				
				JSONArray jsa = jsonObj.getJSONArray("RowsData");
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject innerJson = jsa.getJSONObject(i);
					String sampleId = innerJson.getString(WrsisPortalConstant.SAMPLEID);
					String labId = innerJson.getString("labId");
					String regionid = innerJson.getString("regionid");
					String rustId = innerJson.getString("rustid");
					String survydt = innerJson.getString("surveydt");
					String surveyId = innerJson.getString("surveyId");
					String surveyValue = innerJson.getString("surveyValue");
					String samplval = innerJson.getString("samplval");
					String samplids = innerJson.getString("samplids");
					String tagid = innerJson.getString("labstat");

					tagSample = new SampleLabTagDetails();
					tagSample.setSampleId(Integer.parseInt(sampleId));
					if (WrsisPortalConstant.TRUE.equalsIgnoreCase(tagid)) {
						tagSample.setResearchCenterId(0);
					} else {
						tagSample.setResearchCenterId(Integer.parseInt(labId));
					}

					tagSample.setSurveyId(Integer.parseInt(surveyId));
					tagSample.setSampleIds(Integer.parseInt(samplids));
					tagSample.setSurveyNo(surveyValue);
					tagSample.setSampleIdValue(samplval);
					tagSample.setRustTypeId(Integer.parseInt(rustId));
					tagSample.setRegionId(Integer.parseInt(regionid));
					tagSample.setRaceStatus(0);
					tagSample.setSurveyDate(DateUtil.StringMMMToDate(survydt));
					tagSample.setCreatedBy(tagBean.getUserId());
					tagSample.setCreatedOn(new Timestamp(new Date().getTime()));
					tagSample.setBitstatus(false);
					tagSample.setExternallab(Boolean.parseBoolean(tagid));
					sampleLabTagDetailsRepository.saveAndFlush(tagSample);

					samplIds = sampleLabTagDetailsRepository.getDataBySurveySampleId(Integer.parseInt(samplids));
					if (samplIds != null) {
						samplIds.setBitTagStatus(true);
						sampleSurveyDetailsRepository.saveAndFlush(samplIds);
					}

					Integer sampleCount = sampleLabTagDetailsRepository
							.getSampleCountBySurveyId(tagSample.getSurveyId());

					if (sampleCount == 0) {

						ApprovalAuthorityEntity approval = approvalAuthorityEntityRepository
								.getDataByAppIdAndProcessId(tagSample.getSurveyId(), processId);
						if (approval != null) {
							authorityEntity = new ApprovalAuthorityEntity();
							authorityEntity.setAprovalPId(processId);
							authorityEntity.setStageNo(1);
							authorityEntity.setSurveyId(tagSample.getSurveyId());
							authorityEntity.setPendingAt(0);
							authorityEntity.setSentFrom(tagBean.getUserId());
							authorityEntity.setStatus(false);
							authorityEntity.setCreadtedBy(tagBean.getUserId());
							authorityEntity.setCreatedOn(new Timestamp(new Date().getTime()));
							approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

							// Save ApprovalAuthorityHistory history=new
							history = new ApprovalAuthorityHistoryEntity();
							history.setAprovalPId(authorityEntity.getAprovalPId());
							history.setStageNo(authorityEntity.getStageNo());
							history.setSurveyId(authorityEntity.getSurveyId());
							history.setPendingAt(authorityEntity.getPendingAt());
							history.setSentFrom(authorityEntity.getSentFrom());
							history.setStatus(authorityEntity.isStatus());
							history.setCreadtedBy(tagBean.getUserId());
							history.setCreatedOn(new Timestamp(new Date().getTime()));

							approvalAuthorityHistoryEntityRepository.saveAndFlush(history);

						}

					}

				}
			}
			returnType = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::saveTagSample():" + e);
		}
		return returnType;
	}

	@Override
	public void tagsampleExcelDownload(HttpServletResponse response, String sampleIdE, String surveyNoE,
			String fromDateE, String toDateE, int regionIdE, int filterE) {
		try {
			try {
				List<Object[]> dataObj = null;
				if (sampleIdE == null && surveyNoE == null && fromDateE == null && toDateE == null && regionIdE == 0
						&& filterE == 0) {
					dataObj = sampleLabTagDetailsRepository.viewTaggedSamples("", "", "", "", 0, 0, -1, -1);
				} else {
					dataObj = sampleLabTagDetailsRepository.viewTaggedSamples(surveyNoE, sampleIdE, fromDateE, toDateE,
							regionIdE, filterE, -1, -1);
				}
				// Excel Code Start
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Tag Sample Data");

				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
				headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

				Font newFont = workbook.createFont();
				newFont.setBold(true);
				newFont.setColor(IndexedColors.WHITE.getIndex());
				newFont.setFontHeightInPoints((short) 13);
				newFont.setItalic(false);

				headerCellStyle.setFont(newFont);

				Row headRow = sheet.createRow(0);
				String[] columns = { WrsisPortalConstant.SLNO, "Sample ID", "Survey No.", "Survey Date", "Published Date",
						"Sample Tagged On", WrsisPortalConstant.REGION, "Rust Type", "Lab Tagging", "Laboratory Name" };
				for (int i = 0; i < columns.length; i++) {
					Cell cell = headRow.createCell(i);
					cell.setCellValue(columns[i]);
					cell.setCellStyle(headerCellStyle);
				}
				int rowNo = 1;
				int count = 1;
				for (Object[] ar : dataObj) {

					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(2).setCellValue((ar[4] != null) ? (String) ar[4] : "-NA-");
					row.createCell(3).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(4).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(5).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(6).setCellValue((ar[10] != null) ? (String) ar[10] : "-NA-");
					row.createCell(7).setCellValue((ar[12] != null) ? (String) ar[12] : "-NA-");
					if (ar[16].equals(WrsisPortalConstant.TRUE)) {
						row.createCell(8).setCellValue(WrsisPortalConstant.EXTERNAL);
					} else if (ar[16].equals(WrsisPortalConstant.FALSE)) {
						row.createCell(8).setCellValue(WrsisPortalConstant.INTERNAL);
					} else {
						row.createCell(8).setCellValue("-NA-");
					}
					
					row.createCell(9).setCellValue((ar[14] != null) ? (String) ar[14] : "-NA-");

				}
				
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
				String FILENAME = "Tag_Sample-Data.xlsx";
				response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
				try {
					workbook.write(response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
					workbook.close();
				} catch (IOException e) {

					LOG.error("RaceAnalysisTagSampleServiceImpl::tagSampleExcelDownload()IO:" + e);
				}
				// Excel Code End
			} catch (Exception ex) {
				LOG.error("RaceAnalysisTagSampleServiceImpl::tagSampleExcelDownload()ex:" + ex);

			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::tagSampleExcelDownload()e:" + e);

		}

	}

	@Override
	public List<SampleLabTagBean> tagsamplePdfDownload(String sampleIdPDF, String surveyNoPDF, String fromDatePDF,
			String toDatePDF, int regionIdPDF, int filterPDF) {
		List<SampleLabTagBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = null;
			if (sampleIdPDF == null && surveyNoPDF == null && fromDatePDF == null && toDatePDF == null
					&& regionIdPDF == 0 && filterPDF == 0) {
				details = sampleLabTagDetailsRepository.viewTaggedSamples("", "", "", "", 0, 0, -1, -1);
			} else {
				details = sampleLabTagDetailsRepository.viewTaggedSamples(surveyNoPDF, sampleIdPDF, fromDatePDF,
						toDatePDF, regionIdPDF, filterPDF, -1, -1);
			}
			
			
			for (Object[] obj : details) {
				SampleLabTagBean objReport = new SampleLabTagBean();

				objReport.setSurveyNo((obj[4] != null) ? (String) obj[4] : "-NA-");
				objReport.setSampleIdValue((obj[5] != null) ? (String) obj[5] : "-NA-");
				objReport.setSurveyDate((obj[6] != null) ? (String) obj[6] : "-NA-");
				objReport.setSamplePublishedOn((obj[7] != null) ? (String) obj[7] : "-NA-");
				objReport.setCreatedOn((obj[8] != null) ? (String) obj[8] : "-NA-");
				objReport.setRegionName((obj[10] != null) ? (String) obj[10] : "-NA-");
				objReport.setRustType((obj[12] != null) ? (String) obj[12] : "-NA-");
				objReport.setResearchCenterName((obj[14] != null) ? (String) obj[14] : "-NA-");
				if (obj[16].equals(WrsisPortalConstant.TRUE)) {
					objReport.setExternalLab(WrsisPortalConstant.EXTERNAL);
				} else if (obj[16].equals(WrsisPortalConstant.FALSE)) {
					objReport.setExternalLab(WrsisPortalConstant.INTERNAL);
				} else {
					objReport.setExternalLab("-NA-");
				}
				
				beans.add(objReport);
			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::tagSamplePdfDownload():" + e);

		}
		return beans;
	}

	

	public List<ResearchCenterBean> getResearchCenterNameByRustId(String rustId) {
		List<ResearchCenterBean> list = new ArrayList<>();
		if (rustId != null && !"0".equals(rustId)) {

			List<Object[]> researchCenterObj = researchCenterSpelizationRepository
					.findResearchCenteByRustId(Integer.parseInt(rustId));
			for (Object[] obj : researchCenterObj) {
				ResearchCenterBean researchCenter = new ResearchCenterBean();
				researchCenter.setResearchCenterId(Integer.valueOf(String.valueOf(obj[0])));
				researchCenter.setResearchCenter(String.valueOf(obj[1]));
				list.add(researchCenter);
			}

		}
		return list;
	}

	@Override
	public List<SampleTagBean> fetchActiveTagSamples(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer pstart, Integer plength) {
		List<SampleTagBean> samples = new ArrayList<>();
		Integer count = 0;
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;


			

			List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.fetchActiveTagSamples(
					surveyNo.trim().toUpperCase(), sampleId, sDate, eDate, regionId, rustTypeId, userId, processId,
					pstart, plength);

			for (Object[] obj : sampleTagDetails) {
				SampleTagBean sample = new SampleTagBean();
				sample.setsNo((pstart) + (++count));
				sample.setSampleId((obj[10] != null) ? Integer.valueOf(String.valueOf(obj[10])) : 0);
				sample.setSurveyId((obj[0] != null) ? Integer.valueOf(String.valueOf(obj[0])) : 0);
				sample.setSurveyCheckBox("<input id='checkbox_" + sample.getsNo()
						+ "' class='magic-checkbox' type='checkbox' name='surveyDetailsId' value="
						+ sample.getSampleId() + ">" + "<label for='checkbox_" + sample.getsNo() + "'></label>");
				sample.setSurveyNo((obj[1] != null) ? (String) obj[1] : "");

				

				sample.setSurveyPublishDate((obj[4] != null) ? (String) obj[4] : "");

				
				sample.setSamplesId((obj[7] != null) ? Integer.valueOf(String.valueOf(obj[7])) : 0);
				
				
				sample.setRustId((obj[9] != null) ? Integer.valueOf(String.valueOf(obj[9])) : 0);
				if (obj[9] != null)

					sample.setResearchCenterDetails(getResearchCenterNameByRustId(String.valueOf(sample.getRustId())));

				List<ResearchCenterBean> ruList = null;
				ruList = sample.getResearchCenterDetails();
				String s1 = "<select class='form-control' id='select_" + sample.getsNo() + "'>";
				String s2 = null;
				ArrayList<String> t = new ArrayList<>();
				for (ResearchCenterBean rc : ruList) {
					s2 = "<option value=" + rc.getResearchCenterId() + ">" + rc.getResearchCenter() + "</option>";
					t.add(s2);

				}
				String s3 = "</select>";

				sample.setRcDropDown(s1 + t + s3);

				String action = obj[2].toString() + "<input type=\"hidden\" id=\"sample_" + sample.getsNo()
						+ "\"  value=" + obj[2].toString() + ">" + "<input type=\"hidden\" id=\"sampleids_"
						+ sample.getsNo() + "\"  value=" + obj[7].toString() + ">";
				sample.setSampleValue(action);
				String surveyhidden = "<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id="
						+ sample.getSurveyId() + ">" + sample.getSurveyNo() + "</a>"
						+ "<input type=\"hidden\" id=\"surveyid_" + sample.getsNo() + "\"  value="
						+ sample.getSurveyId() + ">" + "<input type=\"hidden\" id=\"surveyval_" + sample.getsNo()
						+ "\"  value=" + sample.getSurveyNo() + ">";
				sample.setSurveyNo(surveyhidden);
				String hiddenDate = obj[3].toString() + "<input type=\"hidden\" id=\"surveydt_" + sample.getsNo()
						+ "\"  value=" + obj[3].toString() + ">";
				sample.setSurveyDate(hiddenDate);
				String hiddenregionid = obj[5].toString() + "<input type=\"hidden\" id=\"regionid_" + sample.getsNo()
						+ "\"  value=" + Integer.valueOf(String.valueOf(obj[8])) + ">";
				sample.setRegion(hiddenregionid);
				String hiddenrustid = obj[6].toString() + "<input type=\"hidden\" id=\"rustid_" + sample.getsNo()
						+ "\"  value=" + Integer.valueOf(String.valueOf(obj[9])) + ">";
				sample.setRustType(hiddenrustid);

				samples.add(sample);
			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::fetchActivaTagSamples():" + e);

		}
		return samples;
	}

	@Override
	public Integer searchTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer userId, Integer processId, Integer start, Integer length) {

		Integer totalCount = 0;
		try {
			totalCount = sampleLabTagDetailsRepository.searchTagSampleDetailsDataCount(surveyNo, sampleId, startDate,
					endDate, regionId, rustTypeId, userId, processId, start, length);
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::searchTagSampleDetailsDataCount():" + e);

		}

		return totalCount;
	}

	@Override
	public List<SampleLabTagBean> viewTaggedSamples(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer pstart, Integer plength) {
		List<SampleLabTagBean> taggedSampleList = new ArrayList<>();
		SampleLabTagBean vo = null;
		Integer count = 0;
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.viewTaggedSamples(
					surveyNo.trim().toUpperCase(), sampleId, sDate, eDate, regionId, rustTypeId, pstart, plength);// ,pstart,plength
			if (!sampleTagDetails.isEmpty()) {

				for (Object[] dto : sampleTagDetails) {
					vo = new SampleLabTagBean();
					vo.setsNo((pstart) + (++count));
					vo.setSampleLabTagId((dto[0] != null) ? Integer.valueOf(String.valueOf(dto[0])) : 0);
					vo.setSurveyId((Integer.parseInt(String.valueOf(dto[3]))));
					vo.setSampleIdValue((dto[5] != null) ? (String) dto[5] : "");
					vo.setSurveyNo((dto[4] != null) ? (String) dto[4] : "");
					vo.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
							+ vo.getSurveyId() + "\">" + vo.getSurveyNo() + "</a>");
					vo.setSurveyDate((dto[6] != null) ? (String) dto[6] : "");
					vo.setSamplePublishedOn((dto[7] != null) ? (String) dto[7] : "");
					vo.setCreatedOn((dto[8] != null) ? (String) dto[8] : "");
					vo.setRegionName((dto[10] != null) ? (String) dto[10] : "");
					vo.setRustType((dto[12] != null) ? (String) dto[12] : "");
					
					if (dto[14] != null) {
						vo.setResearchCenterName(dto[14].toString());
					} else {
						vo.setResearchCenterName("NA");
					}

					
					if (dto[16].toString().equals(WrsisPortalConstant.FALSE)) {
						vo.setExternalLab(WrsisPortalConstant.INTERNAL);
					} else {
						vo.setExternalLab(WrsisPortalConstant.EXTERNAL);
					}
					if ((Integer.parseInt(String.valueOf(dto[15]))) == 0) {
						vo.setEditLink(" <a data-placement=\"top\" data-toggle=\"tooltip\" id=\"edit"
								+ vo.getSampleLabTagId() + "\"  onclick=\"editTaggedSamples(" + vo.getSampleLabTagId()
								+ ");\" title=\"Edit\" >"
								+ "<button class=\"btn btn-primary btn-xs\" data-title=\"Edit\" data-toggle=\"modal\" data-target=\"#edit\" >\r\n"
								+ " <i class=\"icon-edit1\"></i>\r\n" + " </button> </a> ");
					}
					taggedSampleList.add(vo);

				}
			}

		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::viewTaggedSamples():" + e);

		}
		return taggedSampleList;
	}

	@Override
	public Integer viewTagSampleDetailsDataCount(String surveyNo, String sampleId, String startDate, String endDate,
			Integer regionId, Integer rustTypeId, Integer start, Integer length) {

		Integer totalCount = 0;
		try {
			totalCount = sampleLabTagDetailsRepository.viewTagSampleDetailsDataCount(surveyNo, sampleId, startDate,
					endDate, regionId, rustTypeId, start, length);
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleServiceImpl::viewTagSampleDetailsDataCount():" + e);

		}

		return totalCount;
	}

}
