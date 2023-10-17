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
import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;
import com.csmpl.wrsis.webportal.entity.WheatMaster;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.repository.WheatMasterRepository;
import com.csmpl.wrsis.webportal.service.WheatMasterService;

@Service
public class WheatMasterServiceImpl implements WheatMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(WheatMasterServiceImpl.class);
	@Autowired
	WheatMasterRepository wheatMasterRepository;
	@Autowired
	SurveySiteRepository surveySiteRepository;

	@Override
	public String saveAndUpdate(WheatMaster wMaster) {
		String str = "";
		try {
			if (wMaster.getWheatTypeId() == 0) {
				List<Object[]> wheatTypes = wheatMasterRepository.getWheatTypes(wMaster.getWheatName());
				if (!wheatTypes.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				wMaster.setCreatedOn(new Date());
				WheatMaster wh = wheatMasterRepository.save(wMaster);
				if (wh != null)
					str = WrsisPortalConstant.SAVE;
				else
					str = WrsisPortalConstant.FAILURE;
			} else {
				List<Object[]> wheatTypes = wheatMasterRepository.getWheatTypes(wMaster.getWheatTypeId(),
						wMaster.getWheatName());
				if (!wheatTypes.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				WheatMaster wheatMaster = wheatMasterRepository.getWheatMasterById(wMaster.getWheatTypeId());
				if (wheatMaster != null) {
					List<SurveySiteEntity> list = surveySiteRepository
							.findWheatTypeIdByWheatTypeId(wMaster.getWheatTypeId());
					if (!wMaster.isStatus()) {
						wheatMaster.setUpdatedBy(wMaster.getCreatedBy());
						wheatMaster.setUpdatedOn(new Date());
						wheatMaster.setWheatName(wMaster.getWheatName());
						wheatMaster.setDescription(wMaster.getDescription());
						wheatMaster.setStatus(wMaster.isStatus());
						WheatMaster wh = wheatMasterRepository.save(wheatMaster);
						if (wh != null)
							str = WrsisPortalConstant.UPDATE;
						else
							str = WrsisPortalConstant.FAILURE;
					} else {
						if (list.isEmpty()) {
							wheatMaster.setUpdatedBy(wMaster.getCreatedBy());
							wheatMaster.setUpdatedOn(new Date());
							wheatMaster.setWheatName(wMaster.getWheatName());
							wheatMaster.setDescription(wMaster.getDescription());
							wheatMaster.setStatus(wMaster.isStatus());
							WheatMaster wh = wheatMasterRepository.save(wheatMaster);
							if (wh != null)
								str = WrsisPortalConstant.UPDATE;
							else
								str = WrsisPortalConstant.FAILURE;
						} else {
							return WrsisPortalConstant.DEPENDENT;
						}
					}

				}
			}
		} catch (Exception e) {
			LOG.error("WheatMasterServiceImpl::saveAndUpdate():" + e);
		}

		return str;
	}

	@Override
	public String viewWheatMasterByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<WheatMaster> page = wheatMasterRepository
					.viewAllWheatPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();

				int begingPage = Math.max(1, currentPage - 5);

				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());

				jObject.put("currentPage", currentPage);
				jObject.put("startPage", begingPage);
				jObject.put("endPage", endPage);
				jObject.put("pageNo", page.getNumber());
				jObject.put("showRowNo", page.getSize());
				jObject.put("totalRowNo", page.getTotalElements());

				JSONObject jsonObject = null;
				for (WheatMaster wMaster : page) {
					jsonObject = new JSONObject();
					if (wMaster != null) {
						jsonObject.put("id", wMaster.getWheatTypeId() != 0 ? wMaster.getWheatTypeId() : "-NA-");
						jsonObject.put("name", wMaster.getWheatName() != null ? wMaster.getWheatName() : "-NA-");
						jsonObject.put("desc", wMaster.getDescription() != null ? wMaster.getDescription() : "-NA-");
						if (!wMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");

					}
					array.put(jsonObject);
				}
				jObject.put("wheatList", array);
			}

		} catch (Exception e) {
			LOG.error("WheatMasterServiceImpl::viewWheatMasterByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public List<WheatMaster> viewAllWheatType() {
		return wheatMasterRepository.viewAllWheatType();
	}

	@Override
	public WheatMaster getWheatById(Integer wheatTypeId) {

		return wheatMasterRepository.getWheatMasterById(wheatTypeId);
	}

	@Override
	public String searchWheatByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String wheatName,
			String status) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<WheatMaster> page = null;
		try {
			if (!wheatName.equals("") && !status.equals(WrsisPortalConstant.SELECT)) {
				page = wheatMasterRepository.findByWheatNameIgnoreCaseAndStatus(wheatName, Boolean.parseBoolean(status),
						pageable);
			} else if (!wheatName.equals("")) {
				page = wheatMasterRepository.findByWheatNameIgnoreCaseContaining(wheatName, pageable);
			} else if (!wheatName.equals("") && !status.equals(WrsisPortalConstant.SELECT)) {
				page = wheatMasterRepository.findByWheatNameIgnoreCaseContainingAndStatus(wheatName,
						Boolean.parseBoolean(status), pageable);
			} else if (!status.equals(WrsisPortalConstant.SELECT)) {
				page = wheatMasterRepository.findByStatusOrderByWheatTypeIdDesc(Boolean.parseBoolean(status), pageable);
			} else {
				page = wheatMasterRepository
						.viewAllWheatPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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

				jObject.put("totalRowNo", page.getTotalElements());

				JSONObject jsonObject = null;
				for (WheatMaster wMaster : page) {
					jsonObject = new JSONObject();
					if (wMaster != null) {
						jsonObject.put("id", wMaster.getWheatTypeId() != 0 ? wMaster.getWheatTypeId() : "-NA-");
						jsonObject.put("name", wMaster.getWheatName() != null ? wMaster.getWheatName() : "-NA-");
						jsonObject.put("desc", wMaster.getDescription() != null ? wMaster.getDescription() : "-NA-");
						if (!wMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");

					}
					array.put(jsonObject);
				}
				jObject.put("wheatList", array);
			}

		} catch (Exception e) {
			LOG.error("WheatMasterServiceImpl::searchWheatByPage():" + e);
		}
		return jObject.toString();
	}

}
