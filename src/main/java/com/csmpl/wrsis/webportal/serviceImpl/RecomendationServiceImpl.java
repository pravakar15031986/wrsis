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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
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
import com.csmpl.wrsis.webportal.bean.RecommendationBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.RecommendationEntiry;
import com.csmpl.wrsis.webportal.entity.RecommendationForwardLogEntity;
import com.csmpl.wrsis.webportal.entity.RecommendationLocationEntity;
import com.csmpl.wrsis.webportal.repository.AdvisoryRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.FungicideDetailsRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationForwardLogRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationLocationRepository;
import com.csmpl.wrsis.webportal.repository.RecommendationRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.service.RecomendationService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class RecomendationServiceImpl implements RecomendationService {

	public static final Logger LOG = LoggerFactory.getLogger(RecomendationServiceImpl.class);
	@Autowired
	RecommendationRepository recommendationRepository;
	@Autowired
	RecommendationLocationRepository recommendationLocationRepository;
	@Autowired
	AdvisoryRepository advisoryRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	FungicideDetailsRepository fungicideDetailsRepository;
	@Autowired
	SeasionMasterRepository seasionMasterRepository;
	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;
	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RecommendationForwardLogRepository recommendationForwardLogRepository;
	@Override
	public String addRecommmendation(RecommendationBean recom) {
		String msg = "";
		try {

			Integer recommendProcessId = 0;
			Integer pendingAt = 0;
			Integer stageNo = 0;

			LOG.info(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID);

			List<Object[]> obj = approvalAuthorityEntityRepository.fetchApproveDetails(
					Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID));
			if (obj != null && !obj.isEmpty()) {
				recommendProcessId = Integer
						.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID);
				pendingAt = (Integer) obj.get(0)[2];
				stageNo = (Integer) obj.get(0)[1];

			} else {
				return msg = "config";
			}

			if (recom.getRecomendId() == 0) {
				// For Save Data

				RecommendationEntiry recoEntity = new RecommendationEntiry();

				AdvisoryEntiry ad = advisoryRepository.getOne(recom.getAdvisoryIdBean());
				recoEntity.setAdvisoryId(ad);
				recoEntity.setAdvisoryNo(ad.getAdvisoryNo());
				recoEntity.setRecomendNo("");
				recoEntity.setRecomendType(recom.getRecomendTypeBean());

				recoEntity.setRecomendSummary(recom.getRecomendSummaryBean());
				recoEntity.setStatus(recom.isStatusBean());
				recoEntity.setCreatedBy(recom.getCreatedByBean());
				recoEntity.setCreatedOn(new Date());

				recoEntity.setSmsContent(recom.getSmsContent());
				if (recom.getSmsContent().length() > 0) {
					recoEntity.setIsSmsRequired(true);
				}

				RecommendationEntiry recommFileAndNo = recommendationRepository.saveAndFlush(recoEntity);

				String recommendFileName = null;
				recommendFileName = createRecommendFileName(recom.getRecomendFileNameBean(),
						recommFileAndNo.getRecomendId());

				if (recom.getRecomendFileNameBean() != null && !recom.getRecomendFileNameBean().isEmpty()) {
					File file = new File(
							WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH);
					if (!file.exists()) {
						file.mkdirs();
					}
					if (!file.exists()) {
						Path path = Paths
								.get(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(recom.getRecomendFileNameBean().getBytes(),
							new File(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH
									+ File.separator + recommendFileName));
				}

				recommFileAndNo.setRecomendFileName(recommendFileName);
				recommFileAndNo.setRecomendNo(createAdvisoryNo(recommFileAndNo.getRecomendId()));

				RecommendationEntiry recoFinal = recommendationRepository.saveAndFlush(recommFileAndNo);
				if (recoFinal != null) {
					msg = WrsisPortalConstant.SAVE;
				}

				// Save ApprovalAuthority
				ApprovalAuthorityEntity authorityEntity = new ApprovalAuthorityEntity();
				authorityEntity.setAprovalPId(recommendProcessId);
				authorityEntity.setStageNo(stageNo);
				authorityEntity.setSurveyId(recoFinal.getRecomendId());
				authorityEntity.setPendingAt(pendingAt);
				authorityEntity.setSentFrom(recoFinal.getCreatedBy());
				authorityEntity.setStatus(false);
				authorityEntity.setCreadtedBy(recom.getCreatedByBean());
				authorityEntity.setCreatedOn(new Date());
				approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

				// Save ApprovalAuthorityHistory
				ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
				history.setAprovalPId(authorityEntity.getAprovalPId());
				history.setStageNo(authorityEntity.getStageNo());
				history.setSurveyId(authorityEntity.getSurveyId());
				history.setPendingAt(authorityEntity.getPendingAt());
				history.setSentFrom(authorityEntity.getSentFrom());
				history.setStatus(authorityEntity.isStatus());
				history.setCreadtedBy(recom.getCreatedByBean());
				history.setCreatedOn(new Date());

				approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
				if (recom.getRecomendTypeBean() == 2) {
					saveAndUpdateRecommendationLocationDetails(recommFileAndNo.getRecomendId(),
							recommFileAndNo.getRecomendNo(), recom.getWoredaNameIdArray(), recom.getKebeleNameIdArray(),
							recommFileAndNo.getCreatedBy());
				}

			} else {
				// for Update Data

				RecommendationEntiry recommEdit = recommendationRepository.getOne(recom.getRecomendId());

				recommEdit.setRecomendType(recom.getRecomendTypeBean());
				LOG.info("recom.getForwardRemarkBean()==" + recom.getRecomendSummaryBean());
				recommEdit.setRecomendSummary(recom.getRecomendSummaryBean());
				recommEdit.setStatus(recom.isStatusBean());
				recommEdit.setCreatedBy(recommEdit.getCreatedBy());
				recommEdit.setCreatedOn(recommEdit.getCreatedOn());
				recommEdit.setUpdatedBy(recom.getCreatedByBean());
				recommEdit.setUpdatedOn(new Date());
				recommEdit.setIsSmsRequired(recom.getRequiredSms());
				recommEdit.setSmsContent(recom.getSmsContent());

				RecommendationEntiry recommFileAndNo = recommendationRepository.saveAndFlush(recommEdit);
				if (recommFileAndNo != null) {
					msg = WrsisPortalConstant.UPDATE;
				}
				if (recom.getRecomendFileNameBean() != null && !recom.getRecomendFileNameBean().isEmpty()) {
					String recommendFileName = null;
					recommendFileName = createRecommendFileName(recom.getRecomendFileNameBean(),
							recommFileAndNo.getRecomendId());
					File file = new File(
							WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH);
					if (!file.exists()) {
						file.mkdirs();
					}
					if (!file.exists()) {
						Path path = Paths
								.get(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH);
						Files.createDirectories(path);
					}
					FileCopyUtils.copy(recom.getRecomendFileNameBean().getBytes(),
							new File(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH
									+ File.separator + recommendFileName));

					recommFileAndNo.setRecomendFileName(recommendFileName);
					RecommendationEntiry recoFinal = recommendationRepository.saveAndFlush(recommFileAndNo);
					if (recoFinal != null) {
						msg = WrsisPortalConstant.UPDATE;
					}
				}

				// Save Data for Region Wise At Edit
				if (recom.getRecomendTypeBean() == 1) {
					List<RecommendationLocationEntity> statusList = recommendationLocationRepository
							.viewRecommendLocationByRecommendId(recommFileAndNo.getRecomendId());
					if (statusList.size() > 0) {
						for (RecommendationLocationEntity stausTest : statusList) {
							RecommendationLocationEntity del = recommendationLocationRepository
									.getOne(stausTest.getReomendLocationId());
							del.setStatus(true);

							del.setUpdatedBy(recommFileAndNo.getCreatedBy());
							del.setUpdatedOn(new Date());
							recommendationLocationRepository.saveAndFlush(del);
						}
					}
				} else {
					if (recommFileAndNo.isStatus()) {
						List<RecommendationLocationEntity> statusList = recommendationLocationRepository
								.viewRecommendLocationByRecommendId(recommFileAndNo.getRecomendId());
						for (RecommendationLocationEntity stausTest : statusList) {
							RecommendationLocationEntity del = recommendationLocationRepository
									.getOne(stausTest.getReomendLocationId());
							del.setStatus(true);
							del.setCreatedBy(del.getCreatedBy());
							del.setCreatedOn(del.getCreatedOn());
							del.setUpdatedBy(recommFileAndNo.getCreatedBy());
							del.setUpdatedOn(new Date());
							recommendationLocationRepository.saveAndFlush(del);
						}

					} else {

						ArrayList<Integer> tableWoreda = new ArrayList<>();
						ArrayList<Integer> tableKebele = new ArrayList<>();
						ArrayList<Integer> tableID = new ArrayList<>();

						List<RecommendationLocationEntity> locationList = recommendationLocationRepository
								.viewRecommendLocationByRecommendId(recommFileAndNo.getRecomendId());

						if (locationList.size() == 0 && !recommFileAndNo.isStatus()) {
							saveAndUpdateRecommendationLocationDetails(recommFileAndNo.getRecomendId(),
									recommFileAndNo.getRecomendNo(), recom.getWoredaNameIdArray(),
									recom.getKebeleNameIdArray(), recommFileAndNo.getCreatedBy());
						} else {

							ArrayList<Integer> pageKebeleOne = new ArrayList<>();
							ArrayList<Integer> pageKebeleTwo = new ArrayList<>();

							for (RecommendationLocationEntity editLoca : locationList) {
								tableID.add(editLoca.getReomendLocationId());
								tableWoreda.add(editLoca.getWoredaId());
								if (editLoca.getKebeleId() != 0) {
									tableKebele.add(editLoca.getKebeleId());
								}
							}
							//
							if (recom.getKebeleNameIdArray().length != 0) {

								for (int i = 0; i < recom.getKebeleNameIdArray().length; i++) {
									pageKebeleOne.add(Integer.parseInt(recom.getKebeleNameIdArray()[i]));
									pageKebeleTwo.add(Integer.parseInt(recom.getKebeleNameIdArray()[i]));
								}

								pageKebeleOne.removeAll(tableKebele);
								// Here Kebele related Operation will be done
								RecommendationLocationEntity rLEK = null;
								if (!pageKebeleOne.isEmpty()) {
									// Here new kebele request Will be Added in to table Start
									for (Integer kebele : pageKebeleOne) {
										rLEK = new RecommendationLocationEntity();

										RecommendationEntiry rec = new RecommendationEntiry();

										rec.setRecomendId(recommFileAndNo.getRecomendId());
										rLEK.setRecomendId(rec);
										rLEK.setRecomendNo(recommFileAndNo.getRecomendNo());
										DemographicEntity kebel = demographicRepository.getOne(kebele);
										rLEK.setKebeleId(kebel.getDemographyId());

										DemographicEntity woreda = demographicRepository.getOne(kebel.getParentId());
										rLEK.setWoredaId(woreda.getDemographyId());

										DemographicEntity zone = demographicRepository.getOne(woreda.getParentId());
										rLEK.setZoneId(zone.getDemographyId());

										DemographicEntity region = demographicRepository.getOne(zone.getParentId());
										rLEK.setRegionId(region.getDemographyId());

										rLEK.setStatus(recommFileAndNo.isStatus());

										rLEK.setUpdatedBy(recommFileAndNo.getCreatedBy());
										rLEK.setUpdatedOn(new Date());

										recommendationLocationRepository.saveAndFlush(rLEK);

									}
									// Here new kebele request Will be Added in to table End
								} else {
									tableKebele.removeAll(pageKebeleTwo);
									// Here request from remove kebele from table Start
									for (Integer kebele : tableKebele) {

										for (Integer recLocId : tableID) {
											RecommendationLocationEntity tableId = recommendationLocationRepository
													.getOne(recLocId);
											if (tableId.getKebeleId() == kebele) {
												RecommendationLocationEntity deleteKebele = recommendationLocationRepository
														.findLocationDetailsByLocationIdAndKebeleId(
																tableId.getReomendLocationId(), kebele);

												deleteKebele.setStatus(true);

												deleteKebele.setUpdatedBy(recommFileAndNo.getCreatedBy());
												deleteKebele.setUpdatedOn(new Date());
												recommendationLocationRepository.saveAndFlush(deleteKebele);
											}
										}
										// HHere request from remove kebele from table End
									}
								}
							} else {
								List<RecommendationLocationEntity> kebelReqNotpage = recommendationLocationRepository
										.viewRecommendLocationByRecommendId(recommFileAndNo.getRecomendId());
								if (kebelReqNotpage.size() > 0) {
									// Here request from remove kebele from table Start
									for (Integer kebele : tableKebele) {

										for (Integer recLocId : tableID) {
											RecommendationLocationEntity tableId = recommendationLocationRepository
													.getOne(recLocId);
											if (tableId.getKebeleId() == kebele) {
												RecommendationLocationEntity deleteKebele = recommendationLocationRepository
														.findLocationDetailsByLocationIdAndKebeleId(
																tableId.getReomendLocationId(), kebele);

												deleteKebele.setStatus(true);

												deleteKebele.setUpdatedBy(recommFileAndNo.getCreatedBy());
												deleteKebele.setUpdatedOn(new Date());
												recommendationLocationRepository.saveAndFlush(deleteKebele);
											}
										}
										// HHere request from remove kebele from table End
									}
								}
							}

							ArrayList<Integer> pageWoredaOne = new ArrayList<>();
							ArrayList<Integer> pageWoredaTwo = new ArrayList<>();

							for (int i = 0; i < recom.getWoredaNameIdArray().length; i++) {
								pageWoredaOne.add(Integer.parseInt(recom.getWoredaNameIdArray()[i]));
								pageWoredaTwo.add(Integer.parseInt(recom.getWoredaNameIdArray()[i]));
							}
							LOG.info("pageWoredaOne==" + pageWoredaOne);
							LOG.info("tableWoreda==" + tableWoreda);
							pageWoredaOne.removeAll(tableWoreda);
							LOG.info("Afeter remove==page woreda==" + pageWoredaOne);

							RecommendationLocationEntity rLEW = null;
							if (!pageWoredaOne.isEmpty()) {
								// Here new Woreda request Will be Added in to table Start

								for (Integer woreda : pageWoredaOne) {
									RecommendationLocationEntity wori = recommendationLocationRepository
											.checkWoredaExist(recommFileAndNo.getRecomendId(), woreda);
									if (wori == null) {
										rLEW = new RecommendationLocationEntity();

										RecommendationEntiry rec = new RecommendationEntiry();

										rec.setRecomendId(recommFileAndNo.getRecomendId());
										rLEW.setRecomendId(rec);
										rLEW.setRecomendNo(recommFileAndNo.getRecomendNo());

										DemographicEntity wored = demographicRepository.getOne(woreda);
										rLEW.setWoredaId(wored.getDemographyId());

										DemographicEntity zone = demographicRepository.getOne(wored.getParentId());
										rLEW.setZoneId(zone.getDemographyId());

										DemographicEntity region = demographicRepository.getOne(zone.getParentId());
										rLEW.setRegionId(region.getDemographyId());

										rLEW.setStatus(recommFileAndNo.isStatus());
										rLEW.setUpdatedBy(recommFileAndNo.getCreatedBy());
										rLEW.setUpdatedOn(new Date());

										recommendationLocationRepository.saveAndFlush(rLEW);

									}
								}
								// Here new Woreda request Will be Added in to table End
							} else {
								tableWoreda.removeAll(pageWoredaTwo);
								// Here new kebele request Will be remove from table Start
								for (Integer woreda : tableWoreda) {
									for (Integer recLocId : tableID) {
										RecommendationLocationEntity tableId = recommendationLocationRepository
												.getOne(recLocId);
										if (tableId.getWoredaId() == woreda) {
											RecommendationLocationEntity deleteWoreda = recommendationLocationRepository
													.findLocationDetailsByLocationIdAndWoredaId(
															tableId.getReomendLocationId(), woreda);
											deleteWoreda.setStatus(true);
											deleteWoreda.setUpdatedBy(recommFileAndNo.getCreatedBy());
											deleteWoreda.setUpdatedOn(new Date());
											recommendationLocationRepository.saveAndFlush(deleteWoreda);
										}
									}
								}
								// Here new Woreda request Will be remove from table End
							}

						} // End Of Status Flase
					} // End Of Location Table
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::addRecommmendation():" + e);
		}
		return msg;
	}

	private String createRecommendFileName(MultipartFile recommendFile, int id) {
		String recommedFileName = null;
		String advNo = String.format("RM%04d", id);
		String extnName = "." + FilenameUtils.getExtension(recommendFile.getOriginalFilename());
		recommedFileName = advNo + extnName;
		return recommedFileName;
	}

	public String createAdvisoryNo(int recId) {
		return String.format("RM%04d", recId);
	}

	@Override
	public List<RecommendationBean> viewAllRecommendation() {

		List<RecommendationBean> recommendList = new ArrayList<>();
		try {

			List<RecommendationEntiry> obj = recommendationRepository.viewAllRecommecdation();
			for (RecommendationEntiry reco : obj) {
				RecommendationBean bean = new RecommendationBean();
				bean.setRecomendId(reco.getRecomendId());
				bean.setRecomendNoBean(reco.getRecomendNo());
				bean.setCreatedOnBean(reco.getCreatedOn());
				bean.setRecomendTypeBean(reco.getRecomendType());
				bean.setRecomendStatusBean(reco.getRecomendStatus());
				bean.setPublishedDateBean(reco.getPublishedDate());
				bean.setAdvisoryNoBean(reco.getAdvisoryNo());
				bean.setRecFileName(reco.getRecomendFileName());
				bean.setAdvisoryDateBean(
						advisoryRepository.fetchAdvisoryByAdvisoryNumber(reco.getAdvisoryNo()).getPublishDate());

				List<RecommendationLocationEntity> locationList = recommendationLocationRepository
						.viewRecommendLocationByRecommendId(reco.getRecomendId());

				List<String> kebeleList = new ArrayList<>();
				List<String> woredaList = new ArrayList<>();
				List<String> zoneList = new ArrayList<>();
				List<String> regionList = new ArrayList<>();
				for (RecommendationLocationEntity location : locationList) {

					if (location.getKebeleId() != 0) {
						DemographicEntity kebele = demographicRepository.getOne(location.getKebeleId());
						kebeleList.add(kebele.getDemographyName());
					}
					if (location.getWoredaId() != 0) {
						DemographicEntity woreda = demographicRepository.getOne(location.getWoredaId());
						woredaList.add(woreda.getDemographyName());
					}
					if (location.getZoneId() != 0) {
						DemographicEntity zone = demographicRepository.getOne(location.getZoneId());
						zoneList.add(zone.getDemographyName());
					}
					if (location.getRegionId() != 0) {
						DemographicEntity region = demographicRepository.getOne(location.getRegionId());
						regionList.add(region.getDemographyName());
					}
				}

				List<String> KList = kebeleList.stream().distinct().collect(Collectors.toList());
				bean.setKebeList(KList);

				List<String> WList = woredaList.stream().distinct().collect(Collectors.toList());

				bean.setWoreList(WList);
				List<String> zList = zoneList.stream().distinct().collect(Collectors.toList());

				bean.setZonList(zList);
				List<String> rList = regionList.stream().distinct().collect(Collectors.toList());

				bean.setRegiList(rList);

				recommendList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewAllRecommendation():" + e);
		}

		return recommendList;
	}

	@Override
	public String recommendFileExits(Integer fileId) {
		JSONObject jObject = new JSONObject();
		try {
			RecommendationEntiry obj = recommendationRepository.getOne(fileId);
			if (obj.getRecomendFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getRecomendFileName());
				if (Files.exists(path)) {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "Yes");
				} else {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "No");
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::recommendFileExits():" + e);
		}
		return jObject.toString();
	}

	@Override
	public void downloadRecommendFile(HttpServletResponse response, Integer fileId) {
		try {
			RecommendationEntiry obj = recommendationRepository.getOne(fileId);
			if (obj.getRecomendFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getRecomendFileName());
				if (Files.exists(path)) {
					response.setContentType("application/pdf");

					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
							"attachment; filename=" + obj.getRecomendFileName());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("RecomendationServiceImpl::downloadRecommendFile():" + ex);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::downloadRecommendFile():" + e);

		}

	}

	@Override
	public String publishRecommendtion(Integer recommendId, Integer userId) {
		JSONObject jsonObject = new JSONObject();
		try {

			RecommendationEntiry obj = recommendationRepository.getOne(recommendId);
			obj.setPublishedBy(userId);
			obj.setPublishedDate(new Date());
			obj.setRecomendStatus(1);

			RecommendationEntiry objs = recommendationRepository.saveAndFlush(obj);
			if (objs != null) {
				Integer recommendProcessId = 0;
				Integer pendingAt = 0;
				Integer stageNo = 0;
				LOG.info(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID);

				List<Object[]> approval = approvalAuthorityEntityRepository.fetchApproveDetails(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID));

				if (obj != null && !approval.isEmpty()) {
					recommendProcessId = Integer
							.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_PROCESS_ID);
					pendingAt = (Integer) approval.get(0)[2];
					stageNo = (Integer) approval.get(0)[1];

					// Here Approval Table Will be updated
					ApprovalAuthorityEntity authorityEntity = approvalAuthorityEntityRepository
							.getDataByAppIdAndProcessId(objs.getRecomendId(), recommendProcessId);
					authorityEntity.setSentFrom(objs.getCreatedBy());
					authorityEntity.setUpdatedBy(objs.getPublishedBy());
					authorityEntity.setUpdatedOn(new Date());
					approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

					// Save ApprovalAuthorityHistory
					ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
					history.setAprovalPId(authorityEntity.getAprovalPId());
					history.setStageNo(authorityEntity.getStageNo());
					history.setSurveyId(authorityEntity.getSurveyId());
					history.setPendingAt(authorityEntity.getPendingAt());
					history.setSentFrom(authorityEntity.getSentFrom());
					history.setStatus(authorityEntity.isStatus());
					history.setCreadtedBy(objs.getPublishedBy());
					history.setCreatedOn(new Date());

					approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
				}

				jsonObject.put(WrsisPortalConstant.SUCCESS_MSG, "yes");
			} else {
				jsonObject.put(WrsisPortalConstant.SUCCESS_MSG, "no");
			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::publishRecommendation():" + e);

		}
		return jsonObject.toString();
	}

	@Override
	public String deleteRecommendtion(Integer recommendId, Integer userId) {

		String result = WrsisPortalConstant.FAILURE;
		try {

			RecommendationEntiry obj = recommendationRepository.getOne(recommendId);
			obj.setCreatedBy(obj.getCreatedBy());
			obj.setCreatedOn(obj.getCreatedOn());
			obj.setUpdatedBy(userId);
			obj.setUpdatedOn(new Date());
			obj.setStatus(true);
			RecommendationEntiry objs = recommendationRepository.saveAndFlush(obj);

			List<RecommendationLocationEntity> recLocation = recommendationLocationRepository
					.viewRecommendLocationByRecommendId(objs.getRecomendId());
			if (recLocation.size() > 0) {
				for (RecommendationLocationEntity locaEntity : recLocation) {
					RecommendationLocationEntity locDel = recommendationLocationRepository
							.getOne(locaEntity.getReomendLocationId());
					locDel.setUpdatedBy(userId);
					locDel.setUpdatedOn(new Date());
					locDel.setStatus(true);
					recommendationLocationRepository.saveAndFlush(locDel);
				}
			}

			result = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::deleteRecommendation():" + e);

		}
		return result;
	}

	@Override
	public List<RecommendationBean> searchRecommendation(String sDate, String eDate, String recpmmendNummber,
			Integer recomType) {
		List<RecommendationBean> recommendList = new ArrayList<>();
		try {
			List<RecommendationEntiry> obj = null;

			if (recomType == 0 && "".equalsIgnoreCase(recpmmendNummber) && !"".equalsIgnoreCase(sDate)
					&& !"".equalsIgnoreCase(eDate)) {
				Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(sDate);
				Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(eDate);

				obj = recommendationRepository.searchByDate(date1, date2);
			}
			if (recomType == 0 && !"".equalsIgnoreCase(recpmmendNummber)) {
				obj = recommendationRepository.searchByRecommendNumber(recpmmendNummber.trim());

				if (!"".equalsIgnoreCase(sDate) && !"".equalsIgnoreCase(eDate)
						&& !"".equalsIgnoreCase(recpmmendNummber)) {
					Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(sDate);
					Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(eDate);
					obj = recommendationRepository.searchByDateAndRecommendNumber(date1, date2,
							recpmmendNummber.trim());
				}
			}
			if (recomType != 0) {
				obj = recommendationRepository.searchByRecommendType(recomType);
				if (!"".equalsIgnoreCase(recpmmendNummber)) {
					obj = recommendationRepository.searchByRecomTypeAndRecNo(recomType, recpmmendNummber.trim());
				}
				if (!"".equalsIgnoreCase(sDate) && !"".equalsIgnoreCase(eDate)) {
					Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(sDate);
					Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(eDate);
					obj = recommendationRepository.searchByRecTypeAndDate(recomType, date1, date2);
				}
				if (!"".equalsIgnoreCase(sDate) && !"".equalsIgnoreCase(eDate)
						&& !"".equalsIgnoreCase(recpmmendNummber)) {
					Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(sDate);
					Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(eDate);
					obj = recommendationRepository.searchByRecTypeAndDateAndRecNo(recomType, date1, date2,
							recpmmendNummber.trim());
				}

			}
			if (recomType == 0 && "".equalsIgnoreCase(sDate) && "".equalsIgnoreCase(eDate)
					&& "".equalsIgnoreCase(recpmmendNummber)) {
				obj = recommendationRepository.viewAllRecommecdation();
			}

			for (RecommendationEntiry reco : obj) {
				RecommendationBean bean = new RecommendationBean();
				bean.setRecomendId(reco.getRecomendId());
				bean.setRecomendNoBean(reco.getRecomendNo());
				bean.setCreatedOnBean(reco.getCreatedOn());
				bean.setRecomendTypeBean(reco.getRecomendType());
				bean.setRecomendStatusBean(reco.getRecomendStatus());
				bean.setPublishedDateBean(reco.getPublishedDate());
				bean.setAdvisoryNoBean(reco.getAdvisoryNo());
				bean.setRecFileName(reco.getRecomendFileName());
				bean.setAdvisoryDateBean(
						advisoryRepository.fetchAdvisoryByAdvisoryNumber(reco.getAdvisoryNo()).getPublishDate());

				List<RecommendationLocationEntity> locationList = recommendationLocationRepository
						.viewRecommendLocationByRecommendId(reco.getRecomendId());

				List<String> kebeleList = new ArrayList<>();
				List<String> woredaList = new ArrayList<>();
				List<String> zoneList = new ArrayList<>();
				List<String> regionList = new ArrayList<>();
				for (RecommendationLocationEntity location : locationList) {

					if (location.getKebeleId() != 0) {
						DemographicEntity kebele = demographicRepository.getOne(location.getKebeleId());
						LOG.info("Kebele name==" + kebele.getDemographyName());
						kebeleList.add(kebele.getDemographyName());
					}
					if (location.getWoredaId() != 0) {
						DemographicEntity woreda = demographicRepository.getOne(location.getWoredaId());
						LOG.info("woreda name==" + woreda.getDemographyName());
						woredaList.add(woreda.getDemographyName());
					}
					if (location.getZoneId() != 0) {
						DemographicEntity zone = demographicRepository.getOne(location.getZoneId());
						LOG.info("zone name==" + zone.getDemographyName());
						zoneList.add(zone.getDemographyName());
					}
					if (location.getRegionId() != 0) {
						DemographicEntity region = demographicRepository.getOne(location.getRegionId());
						LOG.info("region name==" + region.getDemographyName());
						regionList.add(region.getDemographyName());
					}
				}

				List<String> KList = kebeleList.stream().distinct().collect(Collectors.toList());
				bean.setKebeList(KList);

				List<String> WList = woredaList.stream().distinct().collect(Collectors.toList());

				bean.setWoreList(WList);
				List<String> zList = zoneList.stream().distinct().collect(Collectors.toList());

				bean.setZonList(zList);
				List<String> rList = regionList.stream().distinct().collect(Collectors.toList());

				bean.setRegiList(rList);

				recommendList.add(bean);

			}
		} catch (Exception e) {

			LOG.error("RecomendationServiceImpl::searchRecommendation():" + e);

		}

		return recommendList;
	}

	@Override
	public RecommendationBean findByRecommendId(Integer recommendId) {
		RecommendationBean recBean = new RecommendationBean();
		try {

			RecommendationEntiry rec = recommendationRepository.getOne(recommendId);

			recBean.setAdvisoryIdBean(rec.getAdvisoryId().getAdvisoryId());
			recBean.setAdvisoryNoBean(rec.getAdvisoryNo());
			recBean.setRecomendId(rec.getRecomendId());
			recBean.setRecomendNoBean(rec.getRecomendNo());
			recBean.setRecomendTypeBean(rec.getRecomendType());
			recBean.setRecFileName(rec.getRecomendFileName());
			recBean.setRecomendSummaryBean(rec.getRecomendSummary());
			recBean.setRequiredSms(rec.getIsSmsRequired());
			recBean.setSmsContent(rec.getSmsContent());
			recBean.setViewAdvisoryDate(DateUtil.DateToString(rec.getPublishedDate(), "dd-MMM-yyyy"));
			recBean.setRecDate(DateUtil.DateToString(rec.getCreatedOn(), "dd-MMM-yyyy"));
			
			
			// set region zone woreda kebele 
			List<Object[]> locDeatils = recommendationRepository.getRecLocDetailsByRecId(recommendId);
			JSONArray jarr = new JSONArray();
			JSONObject jobj = null; 
			for (Object[] dObject : locDeatils) {

				
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.REGION1, ((Short) dObject[1] > 0) ? String.valueOf(dObject[2]) : "All");
				jobj.put("zone", ((Short) dObject[3] > 0) ? String.valueOf(dObject[4]) : "All");
				jobj.put(WrsisPortalConstant.WOREDA1, ((Short) dObject[5] > 0) ? String.valueOf(dObject[6]) : "All");
				jobj.put(WrsisPortalConstant.KEBELE1,
						((!String.valueOf(dObject[7]).equals("0") && !String.valueOf(dObject[8]).equals("null"))
								? String.valueOf(dObject[8])
								: "All"));
				jarr.put(jobj);
			}
			recBean.setDemographyDetails(jarr.toString());
			Integer locationId = 0;
			ArrayList<Integer> region = new ArrayList<>();
			ArrayList<Integer> zone = new ArrayList<>();
			ArrayList<Integer> woreda = new ArrayList<>();
			ArrayList<Integer> kebele = new ArrayList<>();
			List<RecommendationLocationEntity> locationList = recommendationLocationRepository
					.viewRecommendLocationByRecommendId(rec.getRecomendId());

			for (RecommendationLocationEntity LocaEntity : locationList) {
				locationId = LocaEntity.getReomendLocationId();
				region.add(LocaEntity.getRegionId());
				zone.add(LocaEntity.getZoneId());
				woreda.add(LocaEntity.getWoredaId());
				kebele.add(LocaEntity.getKebeleId());
			}
			List<Integer> rList = region.stream().distinct().collect(Collectors.toList());

			List<Integer> zList = zone.stream().distinct().collect(Collectors.toList());

			recBean.setRegiLi(rList);
			recBean.setZonLi(zList);

			List<Integer> wList = woreda.stream().distinct().collect(Collectors.toList());

			List<Integer> kList = kebele.stream().distinct().collect(Collectors.toList());

			recBean.setWoreLis(wList);
			recBean.setKebeLis(kList);

		} catch (Exception e) {

			LOG.error("RecomendationServiceImpl::findByRecommendId():" + e);
		}
		return recBean;
	}

	public String saveAndUpdateRecommendationLocationDetails(Integer recommendId, String recommendNumber,
			String[] woredaNameIdArray, String[] kebeleNameIdArray, Integer createdBy) {
		String msg = null;

		RecommendationLocationEntity recLocation = null;
		// Here Kebele wise Data Will be Save
		if (kebeleNameIdArray.length != 0) {
			RecommendationLocationEntity rLEK = null;
			for (int i = 0; i < kebeleNameIdArray.length; i++) {
				recLocation = new RecommendationLocationEntity();

				RecommendationEntiry rec = new RecommendationEntiry();
				rec.setRecomendId(recommendId);
				recLocation.setRecomendId(rec);
				recLocation.setRecomendNo(recommendNumber);

				DemographicEntity kebele = demographicRepository.getOne(Integer.parseInt(kebeleNameIdArray[i]));
				recLocation.setKebeleId(kebele.getDemographyId());

				DemographicEntity woreda = demographicRepository.getOne(kebele.getParentId());
				recLocation.setWoredaId(woreda.getDemographyId());

				DemographicEntity zone = demographicRepository.getOne(woreda.getParentId());
				recLocation.setZoneId(zone.getDemographyId());

				DemographicEntity region = demographicRepository.getOne(zone.getParentId());
				recLocation.setRegionId(region.getDemographyId());

				recLocation.setStatus(false);
				recLocation.setCreatedBy(createdBy);
				recLocation.setCreatedOn(new Date());

				rLEK = recommendationLocationRepository.saveAndFlush(recLocation);
			}
			if (rLEK != null) {
				msg = WrsisPortalConstant.SAVE;
			}
		}
		// Here Woreda wise data save

		RecommendationLocationEntity rLEW = null;
		for (int i = 0; i < woredaNameIdArray.length; i++) {

			recLocation = new RecommendationLocationEntity();
			RecommendationEntiry rec = new RecommendationEntiry();
			rec.setRecomendId(recommendId);
			recLocation.setRecomendId(rec);
			recLocation.setRecomendNo(recommendNumber);

			List<RecommendationLocationEntity> wori = recommendationLocationRepository
					.checkWoredaExistByWoreda(recommendId, Integer.parseInt(woredaNameIdArray[i]));

			if (wori.size() == 0) {
				DemographicEntity woreda = demographicRepository.getOne(Integer.parseInt(woredaNameIdArray[i]));
				recLocation.setWoredaId(woreda.getDemographyId());

				DemographicEntity zone = demographicRepository.getOne(woreda.getParentId());
				recLocation.setZoneId(zone.getDemographyId());

				DemographicEntity region = demographicRepository.getOne(zone.getParentId());
				recLocation.setRegionId(region.getDemographyId());

				recLocation.setStatus(false);
				recLocation.setCreatedBy(createdBy);
				recLocation.setCreatedOn(new Date());
				rLEW = recommendationLocationRepository.saveAndFlush(recLocation);
			}
		}

		if (rLEW != null) {
			msg = WrsisPortalConstant.SAVE;
		}

		return msg;
	}

	@Override
	public List<RecommendationBean> getRecommendation(SearchVo searchVo) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> rec = null;
		try {
			Date startDate = null;
			Date endDate = null;
			if (searchVo.getStartDate() != "")
				startDate = DateUtil.StringMMMToDate(searchVo.getStartDate());
			if (searchVo.getEndDate() != "")
				endDate = DateUtil.StringMMMToDate(searchVo.getEndDate());
			if (searchVo.getRecNo() == null)
				searchVo.setRecNo("");
			if (searchVo.getStartDate() == null)
				searchVo.setStartDate("");
			if (searchVo.getEndDate() == null)
				searchVo.setEndDate("");
			String recNo = searchVo.getRecNo().trim().toUpperCase();
			if (searchVo.getRecomType() == 2) {
				rec = recommendationRepository.getRecommendationRegionSpecific(recNo, searchVo.getRegionId(),
						searchVo.getZoneId(), searchVo.getWoredaId(), searchVo.getKebeleId(), searchVo.getStartDate(),
						searchVo.getEndDate());
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId(Integer.valueOf(String.valueOf(objs[0])));
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(String.valueOf(objs[2]));
						recObj.setRecomendTypeBean(Short.valueOf(String.valueOf(objs[3])));
						recList.add(recObj);
					}
				}
			} else if (searchVo.getRecomType() == 1) {
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationForGeneral();
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationForGeneralByRecNo(recNo);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForGeneralByRecDate(startDate, endDate);
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForGeneralByRecNoRecDate(recNo, startDate, endDate);
				}
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId((int) objs[0]);
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(
								DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setRecomendTypeBean((Short) objs[3]);
						recList.add(recObj);
					}
				}
			} else if (searchVo.getRecomType() == 0) {
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationByRecNo(recNo);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationByRecDate(startDate, endDate);
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationByRecNoRecDate(recNo, startDate, endDate);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendation();
				}
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId((int) objs[0]);
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(
								DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setRecomendTypeBean((Short) objs[3]);
						recList.add(recObj);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getrecommendation():" + e);
		}
		return recList;
	}

	@Override
	public String recommendationFileExists(Integer downId) {
		JSONObject jObject = new JSONObject();
		try {
			RecommendationEntiry obj = recommendationRepository.getOne(downId);
			if (obj.getRecomendFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getRecomendFileName());
				if (Files.exists(path)) {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "Yes");
				} else {
					LOG.info("wrong File Name");
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "No");
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::recommendationFileExist():" + e);
		}
		return jObject.toString();
	}

	@Override
	public void downloadRecommendationFile(HttpServletResponse response, Integer fileId) {
		try {
			RecommendationEntiry obj = recommendationRepository.getOne(fileId);
			if (obj.getRecomendFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getRecomendFileName());
				if (Files.exists(path)) {

					response.setContentType("application/pdf");

					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION,
							"attachment; filename=" + obj.getRecomendFileName());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("RecomendationServiceImpl::downloadrecommendationFile():" + ex);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::downloadrecommendationFile():" + e);
		}

	}

	@Override
	public RecommendationBean recDetails(int recomendId) {
		RecommendationBean recObj = new RecommendationBean();
		try {
			List<Object[]> rec = recommendationRepository.getRecommendationById(recomendId);
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					recObj.setRecomendId((int) objs[0]);
					recObj.setRecomendNoBean(String.valueOf(objs[1]));
					recObj.setRecDate(
							DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					recObj.setRecomendTypeBean((Short) objs[3]);
					recObj.setRecFileName(String.valueOf(objs[4]));
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::recDetails():" + e);
		}
		return recObj;
	}

	@Override
	public String updateRecForwardByBoa(RecommendationBean recDetails) {
		String sts = "";
		try {
			RecommendationEntiry rec = recommendationRepository.findByRecomendId(recDetails.getRecomendId());
			RecommendationForwardLogEntity recF;
			if(rec != null)
			{
				List<Short> boaTaggedRegIdList=userRepository.getRegionIdByUserId(recDetails.getLoginUserId());
				if(recDetails.getRecomendTypeBean()==2)
				{
					List<Short> recRegIdList=recommendationLocationRepository.getRegionIdByRecomendIdAndStatus(recDetails.getRecomendId());
					for (Short regIdBoa : boaTaggedRegIdList) {
						if(recRegIdList.contains(regIdBoa))
						{
							recF=new RecommendationForwardLogEntity();
							recF.setRecId((short) recDetails.getRecomendId());
							recF.setRegionId(regIdBoa);
							recF.setForwardBy(recDetails.getLoginUserId().shortValue());
							recF.setForward(new Date());
							recF.setForwardRemarks(recDetails.getForwardRemarkBean());
							recF.setStatus(false);
							recommendationForwardLogRepository.save(recF);
						}
					}
					sts = WrsisPortalConstant.SUCCESS;
				}else if(recDetails.getRecomendTypeBean()==1)
				{
					for (Short regIdBoa : boaTaggedRegIdList) {
						recF=new RecommendationForwardLogEntity();
						recF.setRecId((short) recDetails.getRecomendId());
						recF.setRegionId(regIdBoa);
						recF.setForwardBy(recDetails.getLoginUserId().shortValue());
						recF.setForward(new Date());
						recF.setForwardRemarks(recDetails.getForwardRemarkBean());
						recF.setStatus(false);
						recommendationForwardLogRepository.save(recF);
					}
					sts = WrsisPortalConstant.SUCCESS;
				}
			}
			
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::updateRecForwardByBoa():" + e);
			sts = WrsisPortalConstant.FAILURE;
		}
		return sts;
	}

	@Override
	public List<RecommendationBean> getRecommendationsForwardedByBoa(SearchVo searchVo) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> rec = null;
		try {
			Date startDate = null;
			Date endDate = null;
			if (searchVo.getStartDate() != "")
				startDate = DateUtil.StringMMMToDate(searchVo.getStartDate());
			if (searchVo.getEndDate() != "")
				endDate = DateUtil.StringMMMToDate(searchVo.getEndDate());
			if (searchVo.getRecNo() == null)
				searchVo.setRecNo("");
			if (searchVo.getStartDate() == null)
				searchVo.setStartDate("");
			if (searchVo.getEndDate() == null)
				searchVo.setEndDate("");
			String recNo = searchVo.getRecNo().trim().toUpperCase();
			if (searchVo.getRecomType() == 2) {
				rec = recommendationRepository.getRecommendationForwardedRegionSpecific(recNo, searchVo.getRegionId(),
						searchVo.getZoneId(), searchVo.getWoredaId(), searchVo.getKebeleId(), searchVo.getStartDate(),
						searchVo.getEndDate());
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId(Integer.valueOf(String.valueOf(objs[0])));
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(String.valueOf(objs[2]));
						recObj.setForwardDate(String.valueOf(objs[3]));
						recObj.setRecomendTypeBean(Short.valueOf(String.valueOf(objs[4])));
						recObj.setForwardRemarkBean(String.valueOf(objs[5]));
						recList.add(recObj);
					}
				}
			} else if (searchVo.getRecomType() == 1) {
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationForwordedForGeneral();
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationForwordedForGeneralByRecNo(recNo);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForwordedForGeneralByRecDate(startDate, endDate);
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForwordedForGeneralByRecNoRecDate(recNo, startDate,
							endDate);
				}
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId((int) objs[0]);
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(
								DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setForwardDate(
								DateUtil.DateToString((Date) (objs[3]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setRecomendTypeBean((Short) objs[4]);
						recObj.setForwardRemarkBean(String.valueOf(objs[5]));
						recList.add(recObj);
					}
				}
			} else if (searchVo.getRecomType() == 0) {
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationForwordedByRecNo(recNo);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForwordedByRecDate(startDate, endDate);
				}
				if (searchVo.getRecNo() != "" && searchVo.getStartDate() != "" && searchVo.getEndDate() != "") {
					rec = recommendationRepository.getRecommendationForwordedByRecNoRecDate(recNo, startDate, endDate);
				}
				if (searchVo.getRecNo() == "" && searchVo.getStartDate() == "" && searchVo.getEndDate() == "") {
					rec = recommendationRepository.getRecommendationsForwardedByBoa();
				}
				if (!rec.isEmpty()) {
					for (Object[] objs : rec) {
						recObj = new RecommendationBean();
						recObj.setRecomendId((int) objs[0]);
						recObj.setRecomendNoBean(String.valueOf(objs[1]));
						recObj.setRecDate(
								DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setForwardDate(
								DateUtil.DateToString((Date) (objs[3]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
						recObj.setRecomendTypeBean((Short) objs[4]);
						recObj.setForwardRemarkBean(String.valueOf(objs[5]));
						recList.add(recObj);
					}
				}
			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getRecommendationsForwardedByBoa():" + e);
		}
		return recList;
	}

	@Override
	public List<RecommendationBean> getMoaRecommendations(SearchVo searchVo) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> rec = null;
		String recNo = null;
		Date recDtFrm = null;
		Date recDtTo = null;
		if (searchVo.getRecNo() == null)
			searchVo.setRecNo("");
		if (searchVo.getRecDtFrom() == null)
			searchVo.setRecDtFrom("");
		if (searchVo.getRecDtTo() == null)
			searchVo.setRecDtTo("");
		if (searchVo.getRecNo() != "")
			recNo = searchVo.getRecNo().trim().replaceAll("\\s+", "").toUpperCase();
		if (searchVo.getRecDtFrom() != "")
			recDtFrm = DateUtil.StringMMMToDate(searchVo.getRecDtFrom());
		if (searchVo.getRecDtTo() != "")
			recDtTo = DateUtil.StringMMMToDate(searchVo.getRecDtTo());
		try {
			if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() == "" && searchVo.getRecDtTo() == "") {
				rec = recommendationRepository.getMoaRecommendationsByRecNo(recNo);
			} else if (searchVo.getRecNo() == "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsByRecDate(recDtFrm, recDtTo);
			} else if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsByRecNoAndDate(recNo, recDtFrm, recDtTo);
			} else {
				rec = recommendationRepository.getMoaRecommendations();
			}
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					recObj = new RecommendationBean();
					recObj.setRecomendId((int) objs[0]);
					recObj.setRecomendNoBean(String.valueOf(objs[1]));
					recObj.setRecDate(
							DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					recList.add(recObj);
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getMoaRecommendations():" + e);

		}
		return recList;
	}

	@Override
	public String getFungicideDetails() {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Object[]> fungicideList = fungicideDetailsRepository.getFungicideDetails();

			for (Object[] dObject : fungicideList) {
				jobj = new JSONObject();
				jobj.put("fungicide", String.valueOf(dObject[0]));
				jobj.put("quantity", String.valueOf(dObject[1]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getFungicideDetails():" + e);

		}
		return jarr.toString();
	}

	@Override
	public List<RecommendationBean> getMoaRecommendationsforWoredaExperts(SearchVo searchVo) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> rec = null;
		String recNo = null;
		Date recDtFrm = null;
		Date recDtTo = null;
		if (searchVo.getRecNo() == null)
			searchVo.setRecNo("");
		if (searchVo.getRecDtFrom() == null)
			searchVo.setRecDtFrom("");
		if (searchVo.getRecDtTo() == null)
			searchVo.setRecDtTo("");
		if (searchVo.getRecNo() != "")
			recNo = searchVo.getRecNo().trim().replaceAll("\\s+", "").toUpperCase();
		if (searchVo.getRecDtFrom() != "")
			recDtFrm = DateUtil.StringMMMToDate(searchVo.getRecDtFrom());
		if (searchVo.getRecDtTo() != "")
			recDtTo = DateUtil.StringMMMToDate(searchVo.getRecDtTo());
		try {
			if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() == "" && searchVo.getRecDtTo() == "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecNo(recNo);
			} else if (searchVo.getRecNo() == "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecDate(recDtFrm, recDtTo);
			} else if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecNoAndDate(recNo, recDtFrm, recDtTo);
			} else {
				rec = recommendationRepository.getMoaRecommendationsForWExperts();
			}
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					recObj = new RecommendationBean();
					recObj.setRecomendId((int) objs[0]);
					recObj.setRecomendNoBean(String.valueOf(objs[1]));
					recObj.setRecDate(
							DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					recList.add(recObj);
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getMoaRecommendationsForWoredaExperts():" + e);

		}
		return recList;
	}
	
	@Override
	public String getRecLocDetailsByRecId(int recId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Object[]> reclocList = recommendationRepository.getRecLocDetailsByRecId(recId);
			for (Object[] dObject : reclocList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.REGION1, ((Short) dObject[1] > 0) ? String.valueOf(dObject[2]) : "All");
				jobj.put("zone", ((Short) dObject[3] > 0) ? String.valueOf(dObject[4]) : "All");
				jobj.put(WrsisPortalConstant.WOREDA1, ((Short) dObject[5] > 0) ? String.valueOf(dObject[6]) : "All");
				jobj.put(WrsisPortalConstant.KEBELE1,
						((!("0").equals(String.valueOf(dObject[7])) && !("null").equalsIgnoreCase(String.valueOf(dObject[8])))
								? String.valueOf(dObject[8])
								: "All"));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getRecLocDetailsByRecId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String getRecLocDetailsByUserIdAndRecId(int userId,int recId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Short> boaTaggedRegIdList=userRepository.getRegionIdByUserId(userId);
			for (Short boaRegId : boaTaggedRegIdList) {
				List<Object[]> reclocList=recommendationRepository.getRecLocDetailsByRecIdAndRegId(recId,boaRegId.intValue());
				for (Object[] dObject : reclocList) {
					jobj = new JSONObject();
					jobj.put(WrsisPortalConstant.REGION1, ((Short) dObject[1] > 0) ? String.valueOf(dObject[2]) : "All");
					jobj.put("zone", ((Short) dObject[3] > 0) ? String.valueOf(dObject[4]) : "All");
					jobj.put(WrsisPortalConstant.WOREDA1, ((Short) dObject[5] > 0) ? String.valueOf(dObject[6]) : "All");
					jobj.put(WrsisPortalConstant.KEBELE1,
							((!String.valueOf(dObject[7]).equals("0") && !String.valueOf(dObject[8]).equals("null"))
									? String.valueOf(dObject[8])
									: "All"));
					jarr.put(jobj);
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getRecLocDetailsByUserIdAndRecId():" + e);

		}
		return jarr.toString();
	}
	
	@Override
	public String getForwardedRecLocDetailsByRecId(int recId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Object[]> reclocList = recommendationRepository.getForwardedRecLocDetailsByRecId(recId);
			for (Object[] dObject : reclocList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.REGION1, ((Short) dObject[1] > 0) ? String.valueOf(dObject[2]) : "All");
				jobj.put("zone", ((Short) dObject[3] > 0) ? String.valueOf(dObject[4]) : "All");
				jobj.put(WrsisPortalConstant.WOREDA1, ((Short) dObject[5] > 0) ? String.valueOf(dObject[6]) : "All");
				jobj.put(WrsisPortalConstant.KEBELE1,
						((!String.valueOf(dObject[7]).equals("0") && !String.valueOf(dObject[8]).equals("null"))
								? String.valueOf(dObject[8])
								: "All"));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getForwardedRecLocDetailsByRecId():" + e);

		}
		return jarr.toString();
	}
	

	@Override
	public List<RecommendationBean> getRecLocationDetailsByRecId(int recomendId,int userId) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> locDetailsList = null;
		
		try {
			List<Short> boaTaggedRegIdList=userRepository.getRegionIdByUserId(userId);
			for (Short regId : boaTaggedRegIdList) {
				locDetailsList = recommendationRepository.getRecLocDetailsByRecIdAndRegId(recomendId, regId.intValue());
				if (!locDetailsList.isEmpty()) {
					for (Object[] objs : locDetailsList) {
						recObj = new RecommendationBean();
						recObj.setRegionName(String.valueOf(objs[2]));
						recObj.setZoneName(String.valueOf(objs[4]));
						recObj.setWoredaName(String.valueOf(objs[6]));
						recObj.setKebeleName(!("0").equals(String.valueOf(objs[7])) && !("null").equalsIgnoreCase(String.valueOf(objs[8]))
								? String.valueOf(objs[8])
								: "All");
						recList.add(recObj);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getRecLocationDetailsByRecId():" + e);

		}
		return recList;
	}

	@Override
	public List<RecommendationBean> searchRecommendationForRegionSpesific(String sDate, String eDate, Integer regionId,
			Integer zoneId, Integer woredaId, Integer kebeleId, String recpmmendNummber) {

		List<RecommendationBean> recommendList = new ArrayList<>();
		try {
			List<Object[]> obj = null;

			if (!"".equalsIgnoreCase(sDate) && !"".equalsIgnoreCase(eDate)) {
				Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(sDate);
				Date date2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(eDate);
				if (regionId != null) {
					obj = recommendationLocationRepository.searchRecommendLocationByRegionIdWithDate(regionId, date1,
							date2);
					if (regionId != null && zoneId != null) {
						obj = recommendationLocationRepository
								.searchRecommendLocationByRegionIdAndZoneIDWithDate(regionId, zoneId, date1, date2);
						if (regionId != null && zoneId != null && woredaId != null) {
							obj = recommendationLocationRepository
									.searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdDate(regionId, zoneId,
											woredaId, date1, date2);
							if (regionId != null && zoneId != null && woredaId != null && kebeleId != null) {
								obj = recommendationLocationRepository
										.searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdAndKebeleIdDate(regionId,
												zoneId, woredaId, kebeleId, date1, date2);
							}
						}
					}
				}
			}

			if ("".equalsIgnoreCase(sDate) && "".equalsIgnoreCase(eDate)) {
				if (regionId != null) {
					obj = recommendationLocationRepository.searchRecommendLocationByRegionId(regionId);
					if (regionId != null && zoneId != null) {
						obj = recommendationLocationRepository.searchRecommendLocationByRegionIdAndZoneID(regionId,
								zoneId);
						if (regionId != null && zoneId != null && woredaId != null && woredaId != 0) {
							obj = recommendationLocationRepository
									.searchRecommendLocationByRegionIdAndZoneIDAndWoredaId(regionId, zoneId, woredaId);
							if (regionId != null && zoneId != null && woredaId != null && kebeleId != null) {
								obj = recommendationLocationRepository
										.searchRecommendLocationByRegionIdAndZoneIDAndWoredaIdAndKebeleId(regionId,
												zoneId, woredaId, kebeleId);
							}
						}
					}
				}
			}

			for (Object[] region : obj) {
				RecommendationBean bean = new RecommendationBean();
				bean.setRecomendId(Integer.parseInt(region[0].toString()));
				RecommendationEntiry rc = recommendationRepository.getOne(Integer.parseInt(region[0].toString()));
				bean.setRecFileName(rc.getRecomendFileName());
				bean.setRecomendNoBean(region[1].toString());

				String createdDate = region[16].toString();
				Date createdOn = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD).parse(createdDate);
				bean.setCreatedOnBean(createdOn);

				bean.setRecomendTypeBean(Integer.parseInt(region[4].toString()));
				bean.setRecomendStatusBean(Integer.parseInt(region[7].toString()));
				LOG.info("published Date==" + region[9]);
				if (region[9] != null) {
					String publishedDate = region[9].toString();
					Date publishedOn = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD)
							.parse(publishedDate);
					bean.setPublishedDateBean(publishedOn);
				}

				bean.setAdvisoryNoBean(region[3].toString());
				bean.setAdvisoryDateBean(
						advisoryRepository.fetchAdvisoryByAdvisoryNumber(region[3].toString()).getPublishDate());

				List<String> kebeleList = new ArrayList<>();
				List<String> woredaList = new ArrayList<>();
				List<String> zoneList = new ArrayList<>();
				List<String> regionList = new ArrayList<>();

				if (region[20] != null) {
					Object regionRes = region[20];
					String[] regiList = regionRes.toString().split(",");
					for (int i = 0; i < regiList.length; i++) {
						DemographicEntity regionName = demographicRepository.getOne(Integer.parseInt(regiList[i]));

						regionList.add(regionName.getDemographyName());
					}
				}
				if (region[21] != null) {
					Object zoneRes = region[21];
					String[] zoniList = zoneRes.toString().split(",");
					for (int i = 0; i < zoniList.length; i++) {
						DemographicEntity zoneName = demographicRepository.getOne(Integer.parseInt(zoniList[i]));

						zoneList.add(zoneName.getDemographyName());
					}
				}

				if (region[22] != null) {
					Object woredaRes = region[22];
					String[] woreiList = woredaRes.toString().split(",");
					for (int i = 0; i < woreiList.length; i++) {
						DemographicEntity woredaName = demographicRepository.getOne(Integer.parseInt(woreiList[i]));

						woredaList.add(woredaName.getDemographyName());
					}
				}

				if (region[23] != null) {
					Object kebeleRes = region[23];
					String[] kebeiList = kebeleRes.toString().split(",");
					for (int i = 0; i < kebeiList.length; i++) {
						if (Integer.parseInt(kebeiList[i]) != 0) {
							DemographicEntity kebeleName = demographicRepository.getOne(Integer.parseInt(kebeiList[i]));
							kebeleList.add(kebeleName.getDemographyName());
						}
					}
				}

				List<String> KList = kebeleList.stream().distinct().collect(Collectors.toList());
				bean.setKebeList(KList);

				List<String> WList = woredaList.stream().distinct().collect(Collectors.toList());

				bean.setWoreList(WList);
				List<String> zList = zoneList.stream().distinct().collect(Collectors.toList());

				bean.setZonList(zList);
				List<String> rList = regionList.stream().distinct().collect(Collectors.toList());

				bean.setRegiList(rList);

				recommendList.add(bean);

			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::searchRecommendationForRegionSpesific():" + e);

		}

		return recommendList;

	}

	@Override
	public Object getMoaRecommendationsforWoredaExpertsImplementation(SearchVo searchVo) {
		List<RecommendationBean> recList = new ArrayList<>();
		RecommendationBean recObj = null;
		List<Object[]> rec = null;
		String recNo = null;
		Date recDtFrm = null;
		Date recDtTo = null;
		if (searchVo.getRecNo() == null)
			searchVo.setRecNo("");
		if (searchVo.getRecDtFrom() == null)
			searchVo.setRecDtFrom("");
		if (searchVo.getRecDtTo() == null)
			searchVo.setRecDtTo("");
		if (searchVo.getRecNo() != "")
			recNo = searchVo.getRecNo().trim().replaceAll("\\s+", "").toUpperCase();
		if (searchVo.getRecDtFrom() != "")
			recDtFrm = DateUtil.StringMMMToDate(searchVo.getRecDtFrom());
		if (searchVo.getRecDtTo() != "")
			recDtTo = DateUtil.StringMMMToDate(searchVo.getRecDtTo());
		try {
			if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() == "" && searchVo.getRecDtTo() == "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecNoImplementation(recNo,
						searchVo.getUserId());
			} else if (searchVo.getRecNo() == "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecDateImplementation(recDtFrm,
						recDtTo, searchVo.getUserId());
			} else if (searchVo.getRecNo() != "" && searchVo.getRecDtFrom() != "" && searchVo.getRecDtTo() != "") {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsByRecNoAndDateImplementation(recNo,
						recDtFrm, recDtTo, searchVo.getUserId());
			} else {
				rec = recommendationRepository.getMoaRecommendationsForWExpertsImplementation(searchVo.getUserId());
			}
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					recObj = new RecommendationBean();
					recObj.setRecomendId((int) objs[0]);
					recObj.setRecomendNoBean(String.valueOf(objs[1]));
					recObj.setRecDate(
							DateUtil.DateToString((Date) (objs[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					recList.add(recObj);
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getMoaRecommendationsforWoredaExpertsImplementation():" + e);
		}
		return recList;
	}

	@Override
	public List<DemographicEntity> fetchAllUserWiseImplementionRegion(int userId) {
		List<DemographicEntity> list = new ArrayList<>();
		List<Object[]> obj = seasionMasterRepository.fetchAllUserWiseImplementionRegion(userId);
		DemographicEntity vo = null;
		for (Object[] objs : obj) {
			vo = new DemographicEntity();
			vo.setDemographyId((Short) objs[0]);
			vo.setDemographyName(String.valueOf(objs[1]));
			list.add(vo);
		}
		return list;
	}

	@Override
	public List<RecommendationBean> viewAllRecommendationByPage(Integer recommendType, String recommendNo, String sDate,
			String eDate, Integer userId, Integer processId, Integer actionStartus, Integer pstart, Integer pLength) {

		List<RecommendationBean> beans = new ArrayList<>();
		try {

			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			List<Object[]> dataList = recommendationRepository.viewAllRecommendationByPage(recommendType,
					recommendNo.trim().toString(), startDate, endDate, userId, processId, actionStartus, pstart,
					pLength);
			Integer count = 0;
			for (Object[] obj : dataList) {
				RecommendationBean rec = new RecommendationBean();
				rec.setsNo((pstart) + (++count));
				rec.setRecomendId(Integer.valueOf(String.valueOf(obj[0])));
				rec.setRecomendNoBean(
						"<a href='' title='' data-placement='top' data-toggle='modal' data-target='#recommendationModal'  onclick=\"viewRecommendation('"
								+ rec.getRecomendId() + "')\">" + obj[1] + "</a>");
				rec.setRecDate((String) obj[2]);
				rec.setAdvisoryNoBean((String) obj[3]);

				if (Integer.parseInt(String.valueOf(obj[4])) == 1) {
					rec.setRecomendViewTypeBean(WrsisPortalConstant.GENERAL);
				} else if (Integer.parseInt(String.valueOf(obj[4])) == 2) {
					rec.setRecomendViewTypeBean(
							"Region Specific <a title='' data-placement='top' data-toggle='modal' data-target='#locationModal' onclick='viewLocation("
									+ rec.getRecomendId()
									+ ")'><i class='fa fa-info-circle' aria-hidden='true' style='color: #83a750'></i></a>");
				}
				Object fObj = obj[5];
				String fileName = fObj.toString();

				if (fileName.contains(".pdf")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-pdf-o fa-1x' aria-hidden='true'></i></center></a>");

				} else if (fileName.contains(".doc")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-pdf-o fa-1x' aria-hidden='true'></i></center></a>");

				} else if (fileName.contains(".docx")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-pdf-o fa-1x' aria-hidden='true'></i></center></a>");

				}
				rec.setPublishedRecDate((String) obj[6]);
				if (Integer.parseInt(String.valueOf(obj[7])) == 0) {
					rec.setEditLink(
							"<a class=\"btn btn-info btn-sm editClass\" data-toggle=\"tooltip\" title=\"\" id=\"btnModifyId\" data-original-title=\"Edit\" onclick=\"editRecommend("
									+ rec.getRecomendId() + ")\"><i class=\"icon-edit1\"></i></a>"
									+ "<a class=\"btn btn-primary btn-sm publish\" data-toggle=\"tooltip\" title=\"\" id=\"\" data-original-title=\"\" onclick=\"publishRecommend("
									+ rec.getRecomendId() + ")\" data-original-title=\"Publish\">Publish</a>");

				}

				beans.add(rec);
			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewAllRecommendationByPage():" + e);
		}
		return beans;
	}

	@Override
	public List<RecommendationBean> viewAllRecommendationPage(Integer recommendType, String recommendNo, String sDate,
			String eDate, Integer pstart, Integer pLength) {

		List<RecommendationBean> beans = new ArrayList<>();
		try {

			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			List<Object[]> dataList = recommendationRepository.viewRecommendationPage(recommendType, recommendNo,
					startDate, endDate, pstart, pLength);
			Integer count = 0;
			for (Object[] obj : dataList) {
				RecommendationBean rec = new RecommendationBean();
				rec.setsNo((pstart) + (++count));
				rec.setRecomendId(Integer.valueOf(String.valueOf(obj[0])));
				rec.setRecomendNoBean(
						"<a href='' title='' data-placement='top' data-toggle='modal' data-target='#recommendationModal'  onclick=\"viewRecommendation('"
								+ rec.getRecomendId() + "')\">" + obj[1] + "</a>");

				rec.setRecDate((String) obj[2]);
				rec.setAdvisoryNoBean((String) obj[3]);

				if (Integer.parseInt(String.valueOf(obj[4])) == 1) {
					rec.setRecomendViewTypeBean(WrsisPortalConstant.GENERAL);
				} else if (Integer.parseInt(String.valueOf(obj[4])) == 2) {
					rec.setRecomendViewTypeBean(
							"Region Specific <a title='' data-placement='top' data-toggle='modal' data-target='#locationModal' onclick='viewLocation("
									+ rec.getRecomendId()
									+ ")'><i class='fa fa-info-circle' aria-hidden='true' style='color: #83a750'></i></a>");
				}
				Object fObj = obj[5];
				String fileName = fObj.toString();

				if (fileName.contains(".pdf")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-pdf-o fa-1x' aria-hidden='true'></i></center></a>");

				} else if (fileName.contains(".doc")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-word-o fa-1x' aria-hidden='true'></i></center></a>");

				} else if (fileName.contains(".docx")) {
					rec.setRecFileName("<a href='javascript:void(0)' onclick='recommendFileDownload("
							+ rec.getRecomendId()
							+ ")' ><center><i class='fa fa-file-word-o fa-1x' aria-hidden='true'></i></center></a>");

				}
				rec.setPublishedRecDate((String) obj[6]);
				if (Integer.parseInt(String.valueOf(obj[7])) == 0) {
					rec.setEditLink(
							"<a class=\"btn btn-info btn-sm editClass\" data-toggle=\"tooltip\" title=\"\" id=\"btnModifyId\" data-original-title=\"Edit\" onclick=\"editRecommend("
									+ rec.getRecomendId() + ")\"><i class=\"icon-edit1\"></i></a>"
									+ "<a class=\"btn btn-danger btn-sm deleteClass\" data-toggle=\"tooltip\" title=\"\" id=\"btnDeleteId\" data-original-title=\"Delete\" onclick=\"deleteRecommend("
									+ rec.getRecomendId() + ")\"><i class=\"icon-trash-21\"></i></a>");

				}
				rec.setViewAdvisoryDate((String) obj[8]);

				beans.add(rec);
			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewAllRecommendationPage():" + e);
		}
		return beans;

	}

	@Override
	public List<RecommendationBean> viewAddMonitorRecommendation(String recDtFrom, String recDtTo, String recNo,
			Integer userId, Integer pstart, Integer pLength) {
		List<Object[]> dataList = null;
		RecommendationBean recObj = null;
		Integer count = 0;
		List<RecommendationBean> recList = new ArrayList<>();
		try {

			dataList = recommendationRepository.viewAddMonitorRecommendation(recNo, recDtFrom, recDtTo, userId, pstart,
					pLength);

			if (!dataList.isEmpty()) {
				for (Object[] objs : dataList) {
					recObj = new RecommendationBean();
					recObj.setsNo((pstart) + (++count));
					recObj.setRecomendId((Integer.parseInt(String.valueOf(objs[0]))));
					recObj.setRecomendNoBean((objs[1] != null) ? (String) objs[1] : "");
					recObj.setRecDate((objs[2] != null) ? (String) objs[2] : "");
					recObj.setViewDocument("<a title=\"\"\r\n" + " href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ " data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ " data-original-title=\"Download\"\r\n" + " onclick=\"downloadFile("
							+ recObj.getRecomendId() + ")\"><center><i\r\n"
							+ " class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center> </a> ");
					recObj.setAddLink(
							" <a title=\"\" href=\"#\" data-toggle=\"\" data-placement=\"top\" onclick=\"addImplementation('"
									+ recObj.getRecomendId() + "','" + recObj.getRecomendNoBean()
									+ "')\"  data-original-title=\"Add\"><center><i class=\"fa fa-plus-square fa-2x\" aria-hidden=\"true\"></i></center></a> ");
					recList.add(recObj);
				}
			}
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewAddMonitorRecommendation():" + e);
		}
		return recList;
	}

	@Override
	public Integer viewAddMonitorRecommendationCount(String recDtFrom, String recDtTo, String recNo, Integer userId,
			Integer pstart, Integer pLength) {
		Integer totalCount = 0;
		try {
			totalCount = recommendationRepository.viewAddMonitorRecommendationCount(recNo, recDtFrom, recDtTo, userId,
					pstart, pLength);
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewAddMonitorRecommendationCount():" + e);
		}
		return totalCount;
	}

	@Override
	public List<RecommendationBean> viewRecommendationByBOA(Integer recomType, String recommendNo,
			String sDate, String eDate, Integer pstart, Integer pLength,Integer userId) {

		List<RecommendationBean> recList = new ArrayList<>();
		List<Object[]> rec = null;
		try {
			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			rec = recommendationRepository.getRecommendationByBOA(recomType, recommendNo, startDate,
					endDate, pstart, pLength,userId);
			Integer count = 0;
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					RecommendationBean recObj = new RecommendationBean();
					recObj.setsNo((pstart) + (++count));
					recObj.setRecomendId(Integer.valueOf(String.valueOf(objs[0])));
					recObj.setRecomendNoBean(
							"<a href='' title='' data-placement='top' data-toggle='modal' data-target='#recommendationModal'  onclick=\"viewRecommendation('"
									+ recObj.getRecomendId() + "')\">" + objs[1] + "</a>");
					recObj.setRecDate(String.valueOf(objs[2]));

					if (Integer.parseInt(String.valueOf(objs[3])) == 1) {
						recObj.setRecomendViewTypeBean(WrsisPortalConstant.GENERAL);
					} else if (Integer.parseInt(String.valueOf(objs[3])) == 2) {
						recObj.setRecomendViewTypeBean(
								"Region Specific <a title=\"\" data-placement=\"top\" data-toggle=\"modal\"\r\n"
										+ "									data-target=\"#locationModal\" onclick=\"viewLocation("
										+ recObj.getRecomendId() + ")\"><i class=\"fa fa-info-circle\"\r\n"
										+ "									aria-hidden=\"true\" style=\"color: #83a750\"></i></a>");
					}

					recObj.setRecFileName("<a title=\"\"\r\n"
							+ "							href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "							data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "							data-original-title=\"Download\"\r\n"
							+ "							onclick=\"downloadFile(" + recObj.getRecomendId()
							+ ")\"><center> <i\r\n"
							+ "							class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center> </a>");

					recObj.setEditLink("<a\r\n"
							+ "							class=\"btn btn-primary btn-sm publish\" data-toggle=\"tooltip\"\r\n"
							+ "							title=\"\" id=\"\" data-original-title=\"\"\r\n"
							+ "							onclick=\"forward(" + recObj.getRecomendId()
							+ ")\"> <center> <i\r\n"
							+ "								class=\"fa fa-share\" aria-hidden=\"true\"></i></center>  </a>");

					recList.add(recObj);
				}

			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewRecommendationByBOA():" + e);
		}
		return recList;

	}

	@Override
	public Integer viewRecommendationByBOACount(Integer recomType, String recommendNo,
			String sDate, String eDate, Integer pstart, Integer pLength,Integer userId) {

		Integer couont = 0;
		try {
			couont = recommendationRepository.getRecommendationByBOACount(recomType, recommendNo, sDate,
					eDate, pstart, pLength, userId);
		} catch (Exception e) {

			LOG.error("RecommendationServiceImpl::viewRecommendationByBOACount():" + e);
		}
		return couont;
	}

	@Override
	public List<RecommendationBean> viewForwardedByRecommendationByBOA(Integer recomType, String recommendNo,
			String sDate, String eDate, Integer pstart, Integer pLength,Integer userId) {
		List<RecommendationBean> recList = new ArrayList<>();
		List<Object[]> rec = null;
		try {
			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			rec = recommendationRepository.getRecommendationForwardedByBOA(recomType, recommendNo, startDate,
					endDate, pstart, pLength,userId);
			Integer count = 0;
			if (!rec.isEmpty()) {
				for (Object[] objs : rec) {
					RecommendationBean recObj = new RecommendationBean();
					recObj.setsNo((pstart) + (++count));
					recObj.setRecomendId(Integer.valueOf(String.valueOf(objs[0])));
					recObj.setRecomendNoBean(String.valueOf(objs[1]));
					recObj.setRecDate(String.valueOf(objs[2]));

					if (Integer.parseInt(String.valueOf(objs[3])) == 1) {
						recObj.setRecomendViewTypeBean(WrsisPortalConstant.GENERAL);
					} else if (Integer.parseInt(String.valueOf(objs[3])) == 2) {
						recObj.setRecomendViewTypeBean(
								"Region Specific <a title=\"\" data-placement=\"top\" data-toggle=\"modal\"\r\n"
										+ "									data-target=\"#locationModal\" onclick=\"viewLocation("
										+ recObj.getRecomendId() + ")\"><i class=\"fa fa-info-circle\"\r\n"
										+ "									aria-hidden=\"true\" style=\"color: #83a750\"></i></a>");
					}
					recObj.setForwardDate((String) objs[4]);
					recObj.setForwardRemarkBean((String) objs[5]);
					recObj.setRecFileName("<a title=\"\"\r\n"
							+ "							href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "							data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "							data-original-title=\"Download\"\r\n"
							+ "							onclick=\"downloadFile(" + recObj.getRecomendId()
							+ ")\"><center> <i\r\n"
							+ "							class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center> </a>");

					recList.add(recObj);
				}

			}

		} catch (Exception e) {

			LOG.error("RecommendationServiceImpl::viewForwardedByRecommendationByBOA():" + e);
		}
		return recList;
	}

	@Override
	public Integer viewForwardedByRecommendationByBOACount(Integer recomType, String recommendNo,
			String startDate, String endDate, Integer pstart, Integer pLength,Integer userId) {
		Integer couont = 0;
		try {
			couont = recommendationRepository.getCountRecommendationForwardedByBOA(recomType, recommendNo,
					startDate, endDate, pstart, pLength,userId);
		} catch (Exception e) {

			LOG.error("RecommendationServiceImpl::viewForwardedByRecommendationByBOACount():" + e);
		}
		return couont;
	}

	@Override
	public List<RecommendationBean> viewMOARecommendationPage(String sDate, String eDate, String recommendNo,
			Integer pstart, Integer pLength) {

		List<RecommendationBean> beans = new ArrayList<>();
		try {

			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			List<Object[]> dataList = recommendationRepository.viewMOARecommendationPage(startDate, endDate,
					recommendNo.trim().toUpperCase(), pstart, pLength);
			Integer count = 0;
			for (Object[] obj : dataList) {
				RecommendationBean rec = new RecommendationBean();
				rec.setsNo((pstart) + (++count));
				rec.setRecomendId(Integer.valueOf(String.valueOf(obj[0])));
				rec.setRecomendNoBean(
						"<a href='' title='' data-placement='top' data-toggle='modal' data-target='#recommendationModal'  onclick=\"viewRecommendation('"
								+ rec.getRecomendId() + "')\"> <center>" + obj[1] + "</center> </a>");
			

				Object fObj = obj[2];
				String fileName = fObj.toString();

				if (fileName.contains(".pdf")) {
					rec.setRecFileName("<a title=\"\"\r\n"
							+ "												href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "												data-original-title=\"Download\"\r\n"
							+ "												onclick=\"downloadFile("
							+ rec.getRecomendId() + ")\"><center><i\r\n"
							+ "													class=\"fa fa-file-pdf-o \" aria-hidden=\"true\"></i></center>");

				} else if (fileName.contains(".doc") || fileName.contains(".docx")) {
					rec.setRecFileName("<a title=\"\"\r\n"
							+ "												href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "												data-original-title=\"Download\"\r\n"
							+ "												onclick=\"downloadFile("
							+ rec.getRecomendId() + ")\"><center><i\r\n"
							+ "													class=\"fa fa-file-word-o \" aria-hidden=\"true\"></i></center>");

				}
				rec.setPublishedRecDate("<center>" + (String) obj[3] + "</center>");

				beans.add(rec);
			}

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewMOARecommendationPage():" + e);

		}
		return beans;

	}

	@Override
	public Integer viewMOARecommendationPageCount(String sDate, String eDate, String recommendNo, Integer pstart,
			Integer pLength) {
		Integer count = 0;
		try {

			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				startDate = dt2.format(dt1.parse(sDate));

			} else
				startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				endDate = dt4.format(dt3.parse(eDate));

			} else
				endDate = eDate;

			count = recommendationRepository.viewMOARecommendationPageCount(startDate, endDate,
					recommendNo.trim().toUpperCase(), pstart, pLength);

		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::viewMOARecommendationPageCount():" + e);

		}
		return count;

	}
	
	
	//This method will return email id and mobile no.  for sending notification to boa

	@Override
	public List<UserBean> getBoaDetails(Integer recommendId) {
		List<UserBean> boaList=new ArrayList<>();

		try {
		RecommendationEntiry recObj=recommendationRepository.findByRecomendId(recommendId);
		List<Object[]> boaDetails;
		UserBean boa=null;
		int roleId=9;
		  if(recObj.getRecomendType()==1) {
		  boaDetails=userRepository.getUserByRoleId(roleId); 
		  for (Object[] obj : boaDetails) {
			boa=new UserBean();
			boa.setUserId(Integer.valueOf(String.valueOf(obj[0])));
			boa.setFullName((String.valueOf(obj[1])));
			boa.setEmail((String.valueOf(obj[2])));
			boa.setMobile((String.valueOf(obj[3])));
			
			boaList.add(boa);
		}
		
		  }else { 
			 
			  Integer[] regionIdArr=recommendationRepository.getRegionIdByRecId(recommendId);
			 regionIdArr=removeDuplicateElements(regionIdArr,regionIdArr.length);  
			  for (Integer regId : regionIdArr) {
					if (regId != null) {
						boaDetails = userRepository.getUserByRegionId(regId);

						for (Object[] obj : boaDetails) {
							boa = new UserBean();
							boa.setUserId(Integer.valueOf(String.valueOf(obj[0])));
							boa.setFullName((String.valueOf(obj[1])));
							boa.setEmail((String.valueOf(obj[2])));
							boa.setMobile((String.valueOf(obj[3])));

							boaList.add(boa);
						}
					}
			}
		 
	}
		}catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getBoaDetails():" + e);
		}
		  return boaList;
	}

	public Integer[] removeDuplicateElements(Integer arr[], int n){  
        if (n==0 || n==1){  
            return arr;  
        }  
        Integer[] temp = new Integer[n];  
        int j = 0;  
        for (int i=0; i<n-1; i++){  
            if (!(arr[i].equals(arr[i+1]))){  
                temp[j++] = arr[i];  
            }  
         }  
        temp[j++] = arr[n-1];     
        // Changing original array  
        for (int i=0; i<j; i++){  
            arr[i] = temp[i];  
        }  
        return temp;
    }  
	
	//This method will return recommendation name w.r.t  recommend id
	@Override
	public String getRecommendNoById(Integer recommendId) {
		 RecommendationEntiry recomendId = recommendationRepository.findByRecomendId(recommendId);
		 
		return recomendId.getRecomendNo();
	}


	
	
	
	//This method will return email id and mobile no.  for sending notification to Woreda Experts
	@Override
	public List<UserBean> getWoredaDetailsByRecommendationId(int recomendId) {
		List<UserBean> wExprtList=new ArrayList<>();
		try {
			
			RecommendationEntiry recObj=recommendationRepository.findByRecomendId(recomendId);
			List<Object[]> wExpertDetails;
			UserBean woredaExpert=null;
			int roleId=10;
			 if(recObj.getRecomendType()==1) {
				  wExpertDetails=userRepository.getUserByRoleId(roleId); 
				  for (Object[] obj : wExpertDetails) {
					woredaExpert=new UserBean();
					woredaExpert.setUserId(Integer.valueOf(String.valueOf(obj[0])));
					woredaExpert.setFullName((String.valueOf(obj[1])));
					woredaExpert.setEmail((String.valueOf(obj[2])));
					woredaExpert.setMobile((String.valueOf(obj[3])));
					
					wExprtList.add(woredaExpert);
				}
				
				  }else {
					  
					  Integer[] woredaIdArr=recommendationRepository.getWoredaIdByRecId(recomendId);
						 woredaIdArr=removeDuplicateElements(woredaIdArr,woredaIdArr.length);  
						  for (Integer woredaId : woredaIdArr) {
								if (woredaId != null) {
									wExpertDetails = userRepository.getUserByWoredaId(woredaId);
									if (wExpertDetails != null && !wExpertDetails.isEmpty()) {
										for (Object[] obj : wExpertDetails) {
											woredaExpert = new UserBean();
											woredaExpert.setUserId(Integer.valueOf(String.valueOf(obj[0])));
											woredaExpert.setFullName((String.valueOf(obj[1])));
											woredaExpert.setEmail((String.valueOf(obj[2])));
											woredaExpert.setMobile((String.valueOf(obj[3])));

											wExprtList.add(woredaExpert);
										}
									}
								}
						}
					  
					  
					  
					  
				  }

			
		} catch (Exception e) {
			LOG.error("RecomendationServiceImpl::getWoredaDetailsByRecommendationId():" + e);

		}
		return wExprtList;
	}

	@Override
	public String getSmsContentByById(int recomendId) {
		 RecommendationEntiry rId = recommendationRepository.findByRecomendId(recomendId);

		return rId.getSmsContent();
	}
	
	
}
