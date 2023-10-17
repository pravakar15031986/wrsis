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
import com.csmpl.wrsis.webportal.entity.GrowthStage;
import com.csmpl.wrsis.webportal.entity.MonitorGrowthStageEntity;
import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;
import com.csmpl.wrsis.webportal.repository.GrowthStageRepository;
import com.csmpl.wrsis.webportal.repository.MonitorGrowthStageRepository;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.service.GrowthStageService;

@Service
public class GrowthStageServiceImpl implements GrowthStageService {

	private static final Logger LOG = LoggerFactory.getLogger(GrowthStageServiceImpl.class);

	@Autowired
	GrowthStageRepository growthRepository;
	@Autowired
	SurveySiteRepository surveySiteRepository;
	@Autowired
	MonitorGrowthStageRepository monitorGrowthStageRepository;

	@Override
	public String saveAndUpdateGrothStage(GrowthStage gStage) {
		String res = null;
		try {
			if (gStage.getStageId() == 0) {
				List<Object[]> growthStage = growthRepository.getGrowthStage(gStage.getStageName());
				if (!growthStage.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				gStage.setCreatedOn(new Date());
				GrowthStage gS = growthRepository.save(gStage);
				if (gS != null) {
					res = WrsisPortalConstant.SAVE;
				} else
						res = WrsisPortalConstant.FAILURE;
			} else {
				List<Object[]> growthStage = growthRepository.getGrowthStage(gStage.getStageId(),
						gStage.getStageName());
				if (!growthStage.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				GrowthStage grothS = growthRepository.getGrothStageById(gStage.getStageId());
				if (grothS != null) {
					List<SurveySiteEntity> list1 = surveySiteRepository.findGrowthIdByGrowthId(gStage.getStageId());
					List<MonitorGrowthStageEntity> list2 = monitorGrowthStageRepository
							.findGrowthstageidByGrowthstageid(gStage.getStageId());
					if (!gStage.isStatus()) {
						grothS.setUpdateBy(gStage.getCreatedBy());
						grothS.setUpdateOn(new Date());
						grothS.setStageName(gStage.getStageName());
						grothS.setDescription(gStage.getDescription());
						grothS.setStatus(gStage.isStatus());
						GrowthStage gS = growthRepository.save(grothS);
						if (gS != null) {
							res = WrsisPortalConstant.UPDATE;
						} else
								res = WrsisPortalConstant.FAILURE;
					} else {
						if (list1.isEmpty() && list2.isEmpty()) {
							grothS.setUpdateBy(gStage.getCreatedBy());
							grothS.setUpdateOn(new Date());
							grothS.setStageName(gStage.getStageName());
							grothS.setDescription(gStage.getDescription());
							grothS.setStatus(gStage.isStatus());
							GrowthStage gS = growthRepository.save(grothS);
							if (gS != null) {
								res = WrsisPortalConstant.UPDATE;
							} else
									res = WrsisPortalConstant.FAILURE;
						} else {
							return WrsisPortalConstant.DEPENDENT;
						}
					}

				}

			}
		} catch (Exception e) {
			LOG.error("GrowthStageServiceImpl::saveAndUpdateGrowthStage():" + e);
		}
		return res;
	}

	@Override
	public GrowthStage getGrothStageById(Integer stageId) {
		GrowthStage grothS = null;
		try {
			grothS = growthRepository.getGrothStageById(stageId);
		} catch (Exception e) {
			LOG.error("GrowthStageServiceImpl::getGrothStageById():" + e);
		}
		return grothS;
	}

	@Override
	public List<GrowthStage> viewAllGrothStage() {
		List<GrowthStage> list = null;
		try {
			list = growthRepository.viewAllGrothStage();
		} catch (Exception e) {
			LOG.error("GrowthStageServiceImpl::viewAllGrothStage():" + e);
		}
		return list;
	}

	@Override
	public String viewAllGrothByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<GrowthStage> page = growthRepository
					.viewAllGrothStagePage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (GrowthStage gMaster : page) {
					jsonObject = new JSONObject();
					if (gMaster != null) {
						jsonObject.put("id", gMaster.getStageId() != 0 ? gMaster.getStageId() : "-NA-");
						jsonObject.put("name", gMaster.getStageName() != null ? gMaster.getStageName() : "-NA-");
						
						jsonObject.put("descr", gMaster.getDescription() != null ? gMaster.getDescription() : "-NA-");
						if ( !gMaster.isStatus() )
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("grothList", array);

			}
		} catch (Exception e) {
			LOG.error("GrowthStageServiceImpl::viewAllGrothByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public String searchGrothStage(String stageName, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<GrowthStage> page = null;
		try {
			if (!stageName.equals("") && !status.equals("select")) {
				page = growthRepository.findByStageNameIgnoreCaseAndStatus(stageName, Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (!stageName.equals("")) {
				page = growthRepository.findByStageNameContainingIgnoreCase(stageName,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (!status.equals("select")) {
				page = growthRepository.findByStatusOrderByStageIdDesc(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else {
				page = growthRepository
						.viewAllGrothStagePage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (GrowthStage gMaster : page) {
					jsonObject = new JSONObject();
					if (gMaster != null) {
						jsonObject.put("id", gMaster.getStageId() != 0 ? gMaster.getStageId() : "-NA-");
						jsonObject.put("name", gMaster.getStageName() != null ? gMaster.getStageName() : "-NA-");
						
						jsonObject.put("descr", gMaster.getDescription() != null ? gMaster.getDescription() : "-NA-");
						if (!(gMaster.isStatus() ))
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("grothList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("GrowthStageServiceImpl::searchGrothStage():" + e);

		}
		return jObject.toString();

	}

}
