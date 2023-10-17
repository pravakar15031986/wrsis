package com.csmpl.wrsis.webportal.serviceImpl;

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
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.WRSurveyOtherDiseaseEntity;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.WRSurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.service.DiseaseMasterService;

@Service
public class DiseaseMasterServiceImpl implements DiseaseMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(DiseaseMasterServiceImpl.class);
	@Autowired
	DiseaseMasterRepository diseaseRepository;
	@Autowired
	WRSurveyOtherDiseaseRepository wRSurveyOtherDiseaseRepository;

	@Override
	public String saveAndUpdate(DiseaseMaster diseaseMaster) {
		String sts = "";
		try {
			if (diseaseMaster.getDiseaseId() == 0) {
				List<Object[]> disName = diseaseRepository.getDisName(diseaseMaster.getDiseaseName(),
						diseaseMaster.isOtherDetails());
				if (!disName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				diseaseRepository.save(diseaseMaster);
				sts = WrsisPortalConstant.SUCCESS;
			} else {
				List<Object[]> disName = diseaseRepository.getDisName(diseaseMaster.getDiseaseId(),
						diseaseMaster.getDiseaseName(), diseaseMaster.isOtherDetails());
				List<WRSurveyOtherDiseaseEntity> list = wRSurveyOtherDiseaseRepository
						.findDiseaseIdByDiseaseId(diseaseMaster.getDiseaseId());
				if (!disName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				if (!diseaseMaster.isStatus() ) {
					diseaseRepository.save(diseaseMaster);
					sts = WrsisPortalConstant.UPDATE;
				} else {
					if (list.isEmpty()) {
						diseaseRepository.save(diseaseMaster);
						sts = WrsisPortalConstant.UPDATE;
					} else {
						return WrsisPortalConstant.DEPENDENT;
					}
				}
			}
		} catch (Exception e) {
			LOG.error("DiseaseMasterServiceImpl::saveAndUpdate():" + e);
			sts = WrsisPortalConstant.FAILURE;
		}

		
		return sts;
	}

	@Override
	public DiseaseMaster getDiseaseMasterById(Integer diseaseId) {
		return diseaseRepository.getDeseasById(diseaseId);
	}

	@Override
	public String viewDiseaseMasterByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<DiseaseMaster> page = diseaseRepository
					.viewAllDeseasPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (DiseaseMaster diseaseMaster : page) {
					jsonObject = new JSONObject();
					if (diseaseMaster != null) {
						jsonObject.put("id", diseaseMaster.getDiseaseId() != 0 ? diseaseMaster.getDiseaseId() : "-NA-");
						jsonObject.put("name",
								diseaseMaster.getDiseaseName() != null ? diseaseMaster.getDiseaseName() : "-NA-");
						jsonObject.put("aliseName",
								diseaseMaster.getDiseaseAliasName() != null ? diseaseMaster.getDiseaseAliasName()
										: "-NA-");
						if (diseaseMaster.isOtherDetails() )
							jsonObject.put(WrsisPortalConstant.OTHER, "Yes");
						else
							jsonObject.put(WrsisPortalConstant.OTHER, "No");
						jsonObject.put("descr",
								diseaseMaster.getDescription() != null ? diseaseMaster.getDescription() : "-NA-");
						if (!(diseaseMaster.isStatus() ))
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
						if (diseaseMaster.getSeverityRequired() ) {
							jsonObject.put(WrsisPortalConstant.SEVERITY_REQ, "Yes");
							jsonObject.put(WrsisPortalConstant.MIN_SEVERITY, diseaseMaster.getSeverityMin());
							jsonObject.put(WrsisPortalConstant.MAX_SEVERITY, diseaseMaster.getSeverityMax());
						} else {
							jsonObject.put(WrsisPortalConstant.SEVERITY_REQ, "No");
							jsonObject.put(WrsisPortalConstant.MIN_SEVERITY, "-NA-");
							jsonObject.put(WrsisPortalConstant.MAX_SEVERITY, "-NA-");
						}

					}
					array.put(jsonObject);
				}
				jObject.put("diseaseList", array);

			}
		} catch (Exception e) {

			LOG.error("DiseaseMasterServiceImpl::viewDiseaseMasterByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchDiseaseMasterByPage(String fName, String sName, String status, Integer pageSize,
			Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<DiseaseMaster> page = null;
		try {
		
			if (!fName.equals("") && !status.equals(WrsisPortalConstant.SELECT)) {
				page = diseaseRepository.findByDiseaseNameContainsIgnoreCaseAndStatus(fName,
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (page == null) {
				if (!sName.equals("") && !status.equals(WrsisPortalConstant.SELECT)) {
					page = diseaseRepository.findByDiseaseAliasNameContainsIgnoreCaseAndStatus(sName,
							Boolean.parseBoolean(status),
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
				if (page == null) {
					if (!status.equals(WrsisPortalConstant.SELECT)) {
						page = diseaseRepository.findByStatus(Boolean.parseBoolean(status),
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					}
					if (!fName.equals("")) {
						page = diseaseRepository.findByDiseaseNameContainsIgnoreCase(fName,
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					}
					if (!sName.equals("")) {
						page = diseaseRepository.findByDiseaseAliasNameContainsIgnoreCase(sName,
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					}

				}
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
				for (DiseaseMaster diseaseMaster : page) {
					jsonObject = new JSONObject();
					if (diseaseMaster != null) {
						jsonObject.put("id", diseaseMaster.getDiseaseId() != 0 ? diseaseMaster.getDiseaseId() : "-NA-");
						jsonObject.put("name",
								diseaseMaster.getDiseaseName() != null ? diseaseMaster.getDiseaseName() : "-NA-");
						jsonObject.put("aliseName",
								diseaseMaster.getDiseaseAliasName() != null ? diseaseMaster.getDiseaseAliasName()
										: "-NA-");
						if (diseaseMaster.isOtherDetails() )
							jsonObject.put(WrsisPortalConstant.OTHER, "Yes");
						else
							jsonObject.put(WrsisPortalConstant.OTHER, "No");
						jsonObject.put("descr",
								diseaseMaster.getDescription() != null ? diseaseMaster.getDescription() : "-NA-");
						if (!diseaseMaster.isStatus() )
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
						if (diseaseMaster.getSeverityRequired() ) {
							jsonObject.put(WrsisPortalConstant.SEVERITY_REQ, "Yes");
							jsonObject.put(WrsisPortalConstant.MIN_SEVERITY, diseaseMaster.getSeverityMin());
							jsonObject.put(WrsisPortalConstant.MAX_SEVERITY, diseaseMaster.getSeverityMax());
						} else {
							jsonObject.put(WrsisPortalConstant.SEVERITY_REQ, "No");
							jsonObject.put(WrsisPortalConstant.MIN_SEVERITY, "-NA-");
							jsonObject.put(WrsisPortalConstant.MAX_SEVERITY, "-NA-");
						}
					}
					array.put(jsonObject);
				}
				jObject.put("diseaseList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("DiseaseMasterServiceImpl::searchDiseaseMasterByPage():" + e);
		}
		return jObject.toString();
	}

}
