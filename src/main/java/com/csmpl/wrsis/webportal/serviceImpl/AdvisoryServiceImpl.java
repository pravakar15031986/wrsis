package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.AdvisoryBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.repository.AdvisoryRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.service.AdvisoryService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class AdvisoryServiceImpl implements AdvisoryService {
	private static final Logger LOG = LoggerFactory.getLogger(AdvisoryServiceImpl.class);
	@Autowired
	AdvisoryRepository advisoryRepository;
	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;
	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public String addAdvisoryDetails(AdvisoryBean advDetails) {
		String result = "";
		AdvisoryEntiry aobj = new AdvisoryEntiry();
		String advisoryFileName = null;
		try {
			if (advDetails.getAdvisoryId() == 0) {
				Date date = new Date();
				AdvisoryEntiry ad = advisoryRepository.save(aobj);
				if (advDetails.getAdvisory() != null && !advDetails.getAdvisory().isEmpty()) {
					advisoryFileName = createAdvisoryFileName(ad.getAdvisoryId(), advDetails.getAdvisory());
					File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH);
					if (!file.exists()) {
						file.mkdirs();
					}
					if (!file.exists()) {
						Path path = Paths
								.get(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(advDetails.getAdvisory().getBytes(),
							new File(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH
									+ File.separator + advisoryFileName));
					aobj.setAdvFileName(advisoryFileName);
				}
				aobj.setSummaryFromDate(DateUtil.StringMMMToDate(advDetails.getSummaryFromDate()));
				aobj.setSummaryToDate(DateUtil.StringMMMToDate(advDetails.getSummaryToDate()));
				aobj.setAdvRemark(advDetails.getAdvRemark().trim().replaceAll("\\s+", " "));
				aobj.setBitStatus(false);
				aobj.setCreatedBy(advDetails.getCreatedBy());
				aobj.setCreatedOn(date);
				aobj.setAdvisoryNo(createAdvisoryNo(ad.getAdvisoryId()));
				advisoryRepository.save(aobj);

				List<Object[]> obj = approvalAuthorityEntityRepository.fetchApproveDetails(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_PROCESS_ID));

				Integer advisoryProcessId = Integer
						.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_PROCESS_ID);
				Integer pendingAt = (Integer) obj.get(0)[2];
				Integer stageNo = (Integer) obj.get(0)[1];
				// Save ApprovalAuthority
				ApprovalAuthorityEntity authorityEntity = new ApprovalAuthorityEntity();
				authorityEntity.setAprovalPId(advisoryProcessId);
				authorityEntity.setStageNo(stageNo);
				authorityEntity.setSurveyId(aobj.getAdvisoryId());
				authorityEntity.setPendingAt(pendingAt);
				authorityEntity.setSentFrom(advDetails.getCreatedBy());
				authorityEntity.setStatus(false);
				authorityEntity.setCreadtedBy(advDetails.getCreatedBy());
				authorityEntity.setCreatedOn(date);
				approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

				// Save ApprovalAuthorityHistory
				ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
				history.setAprovalPId(authorityEntity.getAprovalPId());
				history.setStageNo(authorityEntity.getStageNo());
				history.setSurveyId(authorityEntity.getSurveyId());
				history.setPendingAt(authorityEntity.getPendingAt());
				history.setSentFrom(authorityEntity.getSentFrom());
				history.setStatus(authorityEntity.isStatus());
				history.setCreadtedBy(aobj.getCreatedBy());
				history.setCreatedOn(date);

				approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
				result = WrsisPortalConstant.SUCCESS;
			} else {
				AdvisoryEntiry adobj = advisoryRepository.getOne(advDetails.getAdvisoryId());
				if (advDetails.getAdvisory() != null && !advDetails.getAdvisory().isEmpty()) {
					advisoryFileName = createAdvisoryFileName(advDetails.getAdvisoryId(), advDetails.getAdvisory());
					File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH);
					if (!file.exists()) {
						file.mkdirs();
					}
					if (!file.exists()) {
						Path path = Paths
								.get(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(advDetails.getAdvisory().getBytes(),
							new File(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH
									+ File.separator + advisoryFileName));
					adobj.setAdvFileName(advisoryFileName);
				}
				adobj.setSummaryFromDate(DateUtil.StringMMMToDate(advDetails.getSummaryFromDate()));
				adobj.setSummaryToDate(DateUtil.StringMMMToDate(advDetails.getSummaryToDate()));
				adobj.setAdvRemark(advDetails.getAdvRemark().trim().replaceAll("\\s+", " "));
				adobj.setBitStatus(false);
				adobj.setUpdatedBy(advDetails.getUpdatedBy());
				adobj.setUpdatedOn(new Date());
				advisoryRepository.save(adobj);

				result = WrsisPortalConstant.UPDATE;
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::addAdvisoryDetails():" + e);
			result = WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	private String createAdvisoryFileName(int advisoryId, MultipartFile advisoryFile) {
		String advFileName = null;
		String advNo = String.format("AD%04d", advisoryId);
		String extnName = "." + FilenameUtils.getExtension(advisoryFile.getOriginalFilename());
		advFileName = advNo + extnName;
		return advFileName;
	}

	public String createAdvisoryNo(int advId) {
	
		return String.format("AD%04d", advId);
	}

	@Override
	public List<AdvisoryBean> viewAdvisory(SearchVo searchvo) {
		List<AdvisoryBean> advList = new ArrayList<>();
		AdvisoryBean advobj = null;
		List<Object[]> advObjList = null;
		String advNo = null;
		Date upldDtFrm = null;
		Date upldDtTo = null;
		if (searchvo.getAdvNo() == null)
			searchvo.setAdvNo("");
		if (searchvo.getAdvUploadDtFrom() == null)
			searchvo.setAdvUploadDtFrom("");
		if (searchvo.getAdvUploadDtTo() == null)
			searchvo.setAdvUploadDtTo("");
		if (searchvo.getAdvNo() != "")
			advNo = searchvo.getAdvNo().trim().replaceAll("\\s+", "").toUpperCase();
		if (searchvo.getAdvUploadDtFrom() != "")
			upldDtFrm = DateUtil.StringMMMToDate(searchvo.getAdvUploadDtFrom());
		if (searchvo.getAdvUploadDtTo() != "")
			upldDtTo = DateUtil.StringMMMToDate(searchvo.getAdvUploadDtTo());
		try {
			if (searchvo.getAdvNo() != "" && searchvo.getAdvUploadDtFrom() == "" && searchvo.getAdvUploadDtTo() == "") {
				advObjList = advisoryRepository.getAdvListByAdvNo(advNo);
			} else if (searchvo.getAdvNo() == "" && searchvo.getAdvUploadDtFrom() != ""
					&& searchvo.getAdvUploadDtTo() != "") {
				advObjList = advisoryRepository.getAdvListBySummaryDate(upldDtFrm, upldDtTo);
			} else if (searchvo.getAdvNo() != "" && searchvo.getAdvUploadDtFrom() != ""
					&& searchvo.getAdvUploadDtTo() != "") {
				advObjList = advisoryRepository.getAdvListByAdvNoAndSummaryDate(advNo, upldDtFrm, upldDtTo);
			} else {
				advObjList = advisoryRepository.getAdvList();
			}
			if (!advObjList.isEmpty()) {
				for (Object[] objs : advObjList) {
					advobj = new AdvisoryBean();
					advobj.setAdvisoryId((Integer) objs[0]);
					advobj.setAdvisoryNo((String) objs[1]);
					advobj.setCreatedOn(DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					advobj.setAdvFileName((String) objs[3]);
					advobj.setAdvRemark((String) objs[4]);
					advobj.setPublishDate(DateUtil.DateToString((Date) (objs[5]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					advobj.setAdvStatus((Short) objs[6]);
					advList.add(advobj);
				}
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::viewAdvisory():" + e);
		}
		return advList;
	}

	@Override
	public List<AdvisoryEntiry> viewAllPublishedAdvisory() {
		return advisoryRepository.vlewAllPublishedAdvisoryByStatus();
	}

	@Override
	public List<AdvisoryEntiry> viewAllPublishedAdvisory(Date startDate, Date endDate, String advNo) {
		List<AdvisoryEntiry> list = null;
		if (advNo == null)
			advNo = "";
		String adviNo = advNo.trim().toUpperCase();
		try {
			if (startDate != null && endDate != null && "".equals(advNo)) {
				list = advisoryRepository.searchAllPublishedAdvisoryByStatus(startDate, endDate);
			}
			if (!("".equals(advNo)) && startDate == null && endDate == null) {
				list = advisoryRepository.searchAllPublishedAdvisoryByNoStatus(adviNo);
			}
			if (startDate != null && endDate != null && !("".equals(advNo)) ) {
				list = advisoryRepository.vlewAllPublishedAdvisoryByStatus(startDate, endDate, adviNo);
			}
			if (startDate == null && endDate == null && "".equals(advNo)) {
				list = advisoryRepository.vlewAllPublishedAdvisoryByStatus();
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::viewAllPublishedAdvisory():" + e);
		}
		return list;
	}

	@Override
	public String advisoryFileExits(Integer fileId) {

		JSONObject jObject = new JSONObject();
		try {
			AdvisoryEntiry obj = advisoryRepository.getOne(fileId);
			if (obj.getAdvFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getAdvFileName());
				if (Files.exists(path)) {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "Yes");
				} else {

					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "No");
				}
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::advisoryFileExits():" + e);
		}
		return jObject.toString();
	}

	@Override
	public void downloadAdvisoryFile(HttpServletResponse response, Integer fileId) {
		try {
			AdvisoryEntiry obj = advisoryRepository.getOne(fileId);
			if (obj.getAdvFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getAdvFileName());
				if (Files.exists(path)) {
					
					response.setContentType("application/pdf");

					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + obj.getAdvFileName());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("AdvisoryServiceImpl::downloadAdvisory():" + ex);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::downloadAdvisoryFile():" + e);
		}

	}

	@Override
	public String deleteAdvisory(HttpServletRequest request, int advisoryId) {
		String result = null;
		HttpSession session = request.getSession();
		try {
			AdvisoryEntiry ad = advisoryRepository.getOne(advisoryId);
			if (ad != null) {
				ad.setBitStatus(true);
				ad.setUpdatedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
				ad.setUpdatedOn(new Date());
				advisoryRepository.save(ad);
				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::downloadAdvisoryFile():" + e);
			result = WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public String publishAdvisory(HttpServletRequest request, int advisoryId) {
		String result = null;
		HttpSession session = request.getSession();
		try {
			AdvisoryEntiry ad = advisoryRepository.getOne(advisoryId);
			if (ad != null) {
				Date date = new Date();
				ad.setAdvStatus(1);
				ad.setPublishedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
				ad.setPublishDate(new Date());
				ad.setUpdatedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
				ad.setUpdatedOn(date);
				advisoryRepository.save(ad);

				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::publishAdvisory():" + e);
			result = WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public AdvisoryBean advDetails(int advisoryId) {
		AdvisoryEntiry ad = advisoryRepository.getOne(advisoryId);
		AdvisoryBean adv = new AdvisoryBean();
		adv.setAdvisoryId(advisoryId);
		adv.setSummaryFromDate(DateUtil.DateToString(ad.getSummaryFromDate(), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
		adv.setSummaryToDate(DateUtil.DateToString(ad.getSummaryToDate(), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
		adv.setAdvFileName(ad.getAdvFileName());
		adv.setAdvRemark(ad.getAdvRemark());
		return adv;
	}

	@Override
	public List<AdvisoryBean> viewPublishedAdvisory(SearchVo searchVo) {
		List<AdvisoryBean> advList = new ArrayList<>();
		AdvisoryBean advobj = null;
		List<Object[]> advObjList = null;
		String advNo = null;
		Date advDtFrm = null;
		Date advDtTo = null;
		if (searchVo.getAdvNo() == null)
			searchVo.setAdvNo("");
		if (searchVo.getAdvDtFrom() == null)
			searchVo.setAdvDtFrom("");
		if (searchVo.getAdvDtTo() == null)
			searchVo.setAdvDtTo("");
		if (searchVo.getAdvNo() != "")
			advNo = searchVo.getAdvNo().trim().replaceAll("\\s+", "").toUpperCase();
		if (searchVo.getAdvDtFrom() != "")
			advDtFrm = DateUtil.StringMMMToDate(searchVo.getAdvDtFrom());
		if (searchVo.getAdvDtTo() != "")
			advDtTo = DateUtil.StringMMMToDate(searchVo.getAdvDtTo());
		try {
			if (searchVo.getAdvNo() != "" && searchVo.getAdvDtFrom() == "" && searchVo.getAdvDtTo() == "") {
				advObjList = advisoryRepository.getPubAdvListByAdvNo(advNo);
			} else if (searchVo.getAdvNo() == "" && searchVo.getAdvDtFrom() != "" && searchVo.getAdvDtTo() != "") {
				advObjList = advisoryRepository.getPubAdvListByAdvDt(advDtFrm, advDtTo);
			} else if (searchVo.getAdvNo() != "" && searchVo.getAdvDtFrom() != "" && searchVo.getAdvDtTo() != "") {
				advObjList = advisoryRepository.getPubAdvListByAdvNoAndAdvDate(advNo, advDtFrm, advDtTo);
			} else {
				advObjList = advisoryRepository.getPubAdvList();
			}
			if (!advObjList.isEmpty()) {
				for (Object[] objs : advObjList) {
					advobj = new AdvisoryBean();
					advobj.setAdvisoryId((Integer) objs[0]);
					advobj.setAdvisoryNo((String) objs[1]);
					advobj.setPublishDate(DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					advobj.setAdvFileName((String) objs[3]);
					advobj.setAdvRemark((String) objs[4]);
					advList.add(advobj);
				}
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::viewPublishedAdvisory():" + e);
		}
		return advList;
	}

	@Override
	public String advisoryFileExists(Integer downId) {
		JSONObject jObject = new JSONObject();
		try {
			AdvisoryEntiry obj = advisoryRepository.getOne(downId);
			if (obj.getAdvFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getAdvFileName());
				if (Files.exists(path)) {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "Yes");
				} else {
					LOG.info("wrong File Name");
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "No");
				}
			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::advisoryFileExists():" + e);
		}
		return jObject.toString();
	}

	@Override
	public List<AdvisoryBean> viewAdvisoryPageWise(String startDate, String endDate, String advisoryNo, Integer pStart,
			Integer pLength) {
		List<AdvisoryBean> dataList = new ArrayList<>();

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

			Integer count = 0;
			List<Object[]> advList = advisoryRepository.viewAdvisoryPageWise(sDate, eDate,
					advisoryNo.trim().toUpperCase(), pStart, pLength);
			for (Object[] obj : advList) {
				AdvisoryBean adv = new AdvisoryBean();
				adv.setsNo((pStart) + (++count));
				adv.setAdvisoryId(Integer.valueOf((String) obj[0]));
				adv.setAdvisoryNo((String) obj[1]);
				adv.setCreatedOn(obj[2] != null ? (String) obj[2] : "");
				adv.setAdvFileName("<a title=\"\" href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
						+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
						+ "												data-original-title=\"Download\"\r\n"
						+ "												onclick=\"downloadFile(" + adv.getAdvisoryId()
						+ ")\"><i\r\n"
						+ "													class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></a>");
				adv.setAdvRemark(obj[4] != null ? (String) obj[4] : "");
				adv.setPublishDate(obj[5] != null ? (String) obj[5] : "");
				adv.setAdvStatus(Integer.valueOf((obj[6].toString())));
				if (adv.getAdvStatus() == 0) {
					adv.setAction("<a class=\"btn btn-info btn-sm editClass\"\r\n" + "data-toggle=\"tooltip\"\r\n"
							+ "onclick=\"editAdvisory(" + adv.getAdvisoryId() + ")\" title=\"\"\r\n"
							+ "id=\"btnModifyId${advisory.advisoryId}\"\r\n"
							+ "data-original-title=\"Edit\"><i class=\"icon-edit1\"></i></a>"
							+ "<a class=\"btn btn-danger btn-sm deleteClass\"\r\n"
							+ "data-toggle=\"tooltip\" onclick=\"deleteAdvisory(" + adv.getAdvisoryId()
							+ ")\" title=\"\"\r\n"
							+ "id=\"btnDeleteId${advisory.advisoryId}\" data-original-title=\"Delete\"><i class=\"icon-trash-21\"></i></a>");
				}
				dataList.add(adv);

			}
		} catch (Exception e) {

			LOG.error("AdvisoryServiceImpl::viewAdvisoryPageWise():" + e);
		}
		return dataList;
	}

	@Override
	public List<AdvisoryBean> viewAdvisoryPageWiseApp(String startDate, String endDate, String advisoryNo,
			Integer actionStatus, Integer pStart, Integer pLength) {
		List<AdvisoryBean> dataList = new ArrayList<>();

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

			Integer count = 0;
			List<Object[]> advList = advisoryRepository.viewAdvisoryPageWiseApp(sDate, eDate,
					advisoryNo.trim().toUpperCase(), actionStatus, pStart, pLength);
			for (Object[] obj : advList) {
				AdvisoryBean adv = new AdvisoryBean();
				adv.setsNo(pStart + (++count));
				adv.setAdvisoryId(Integer.valueOf((String) obj[0]));
				adv.setAdvisoryNo((String) obj[1]);
				adv.setCreatedOn(obj[2] != null ? (String) obj[2] : "");
				adv.setAdvFileName("<a title=\"\" href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
						+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
						+ "												data-original-title=\"Download\"\r\n"
						+ "												onclick=\"downloadFile(" + adv.getAdvisoryId()
						+ ")\"><center><i\r\n"
						+ "													class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center></a>");
				adv.setAdvRemark(obj[4] != null ? (String) obj[4] : "");
				adv.setPublishDate(obj[5] != null ? (String) obj[5] : "");
				adv.setAdvStatus(Integer.valueOf((obj[6].toString())));
				if (adv.getAdvStatus() == 0) {
					adv.setAction("<a class=\"btn btn-info btn-sm editClass\"\r\n" + "data-toggle=\"tooltip\"\r\n"
							+ "onclick=\"editAdvisory(" + adv.getAdvisoryId() + ")\" title=\"\"\r\n"
							+ "id=\"btnModifyId${advisory.advisoryId}\"\r\n"
							+ "data-original-title=\"Edit\"><i class=\"icon-edit1\"></i></a>"
							+ "<a class=\"btn btn-primary btn-sm publish\" data-toggle=\"tooltip\" title=\"\" id=\"publish"
							+ adv.getAdvisoryId() + "\"	onclick=\"publishAdvisory(" + adv.getAdvisoryId()
							+ ")\" data-original-title=\"Publish\">Publish</a>");
				}
				dataList.add(adv);

			}
		} catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::viewAdvisoryPageWiseApp():" + e);
		}
		return dataList;
	}

	@Override
	public List<AdvisoryBean> viewPublishedAdvisoryPage(String startDate, String endDate, String advisoryNo,
			Integer pStart, Integer pLength) {

		List<AdvisoryBean> dataList = new ArrayList<>();

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

			Integer count = 0;
			List<Object[]> advList = advisoryRepository.viewAdvisoryPublishedPageWise(sDate, eDate,
					advisoryNo.trim().toUpperCase(), pStart, pLength);
			for (Object[] obj : advList) {
				AdvisoryBean adv = new AdvisoryBean();
				adv.setsNo(pStart + (++count));
				adv.setAdvisoryId(Integer.valueOf((String) obj[0]));
				adv.setAdvisoryNo((String) obj[1]);
				adv.setCreatedOn(obj[2] != null ? (String) obj[2] : "");

				String fileNmae = (String) obj[3];
				if (fileNmae.contains(".pdf")) {
					adv.setAdvFileName("<a title=\"\" href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "												data-original-title=\"Download\"\r\n"
							+ "												onclick=\"downloadFile("
							+ adv.getAdvisoryId() + ")\"><center><i\r\n"
							+ "													class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center></a>");
				} else if (fileNmae.contains(".doc") || fileNmae.contains(".docx")) {
					adv.setAdvFileName("<a title=\"\" href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "												data-original-title=\"Download\"\r\n"
							+ "												onclick=\"downloadFile("
							+ adv.getAdvisoryId() + ")\"><center><i\r\n"
							+ "													class=\"fa fa-file-word-o \" aria-hidden=\"true\"></i></center></a>");

				}

				adv.setAdvRemark(obj[4] != null ? (String) obj[4] : "");
				adv.setPublishDate(obj[5] != null ? (String) obj[5] : "");
				adv.setAdvStatus(Integer.valueOf((obj[6].toString())));
				dataList.add(adv);

			}
		} catch (Exception e) {

			LOG.error("AdvisoryServiceImpl::viewPublishedAdvisoryPage():" + e);
		}
		return dataList;

	}

	@Override
	public Integer viewPublishedAdvisoryPageCount(String startDate, String endDate, String advisoryNo, Integer pStart,
			Integer pLength) {
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

			count = advisoryRepository.viewAdvisoryPublishedPageWiseCount(sDate, eDate, advisoryNo.trim().toUpperCase(),
					pStart, pLength);

		} catch (Exception e) {

			LOG.error("AdvisoryServiceImpl::viewPublishedAdvisoryPageCount():" + e);
		}
		return count;
	}

	@Override
	public AdvisoryEntiry findByAdvisoryId(int advisoryId) {
		// TODO Auto-generated method stub
		return advisoryRepository.findByAdvisoryId(advisoryId);
	}

	@Override
	public List<UserBean> getUserDetails(int userId) {
		   List<UserBean> userList=new ArrayList<>();
		   List<Object[]> userDetails = null;
		   UserBean boa=null;
		try {
			
			  userDetails=userRepository.getUserDetails(userId); 
			  for (Object[] obj : userDetails) {
				boa=new UserBean();
				boa.setUserId(Integer.valueOf(String.valueOf(obj[0])));
				boa.setFullName((String.valueOf(obj[1])));
				boa.setEmail((String.valueOf(obj[2])));
				boa.setMobile((String.valueOf(obj[3])));
				
				userList.add(boa);
			}
				
		}catch (Exception e) {
			LOG.error("AdvisoryServiceImpl::getUserDetails():" + e);
		}
		  return userList;
		}

}
