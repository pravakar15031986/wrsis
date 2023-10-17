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
import com.csmpl.wrsis.webportal.entity.FungicideDetailsEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.MonitorFungicideEntity;
import com.csmpl.wrsis.webportal.repository.FungicideDetailsRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.MonitorFungicideRepository;
import com.csmpl.wrsis.webportal.service.FungicideMasterService;

@Service
public class FungicideMasterServiceImpl implements FungicideMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(FungicideMasterServiceImpl.class);
	@Autowired
	FungicideMasterRepository fungicideRepository;
	@Autowired
	FungicideDetailsRepository fungicideDetailsRepository;
	@Autowired
	MonitorFungicideRepository monitorFungicideRepository;

	@Override
	public String saveAndUpdate(FungicideMaster fungicideMaster) {
		String str = "";
		try {
			if (fungicideMaster.getFungicideId() == 0) {
				List<Object[]> fungName = fungicideRepository.getFungNames(fungicideMaster.getFungicideName());
				if (!fungName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				fungicideMaster.setCreatedOn(new Date());
				FungicideMaster fungiObj = fungicideRepository.save(fungicideMaster);
				if (fungiObj != null)
					str = WrsisPortalConstant.SAVE;
				else
					str = WrsisPortalConstant.FAILURE;
			} else {
				List<Object[]> fungName = fungicideRepository.getFungNames(fungicideMaster.getFungicideId(),
						fungicideMaster.getFungicideName());
				if (!fungName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				FungicideMaster fMaster = fungicideRepository.getFungicideMasterById(fungicideMaster.getFungicideId());
				if (fMaster != null) {
					List<FungicideDetailsEntity> list1 = fungicideDetailsRepository
							.findOtherFungiByOtherFungi(Integer.toString(fungicideMaster.getFungicideId()));
					List<MonitorFungicideEntity> list2 = monitorFungicideRepository
							.findFungicideidByFungicideid(fungicideMaster.getFungicideId());
					if (!(fungicideMaster.isStatus() )) {
						fMaster.setUpdatedBy(fungicideMaster.getCreatedBy());
						fMaster.setUpdatedOn(new Date());
						fMaster.setFungicideName(fungicideMaster.getFungicideName());
						fMaster.setFungicideAliasName(fungicideMaster.getFungicideAliasName());
						fMaster.setQuantity(fungicideMaster.getQuantity());
						fMaster.setDescription(fungicideMaster.getDescription());
						fMaster.setStatus(fungicideMaster.isStatus());
						FungicideMaster fungiObj = fungicideRepository.save(fMaster);
						if (fungiObj != null)
							str = WrsisPortalConstant.UPDATE;
						else
							str = WrsisPortalConstant.FAILURE;
					} else {
						if (list1.isEmpty() && list2.isEmpty()) {
							fMaster.setUpdatedBy(fungicideMaster.getCreatedBy());
							fMaster.setUpdatedOn(new Date());
							fMaster.setFungicideName(fungicideMaster.getFungicideName());
							fMaster.setFungicideAliasName(fungicideMaster.getFungicideAliasName());
							fMaster.setQuantity(fungicideMaster.getQuantity());
							fMaster.setDescription(fungicideMaster.getDescription());
							fMaster.setStatus(fungicideMaster.isStatus());
							FungicideMaster fungiObj = fungicideRepository.save(fMaster);
							if (fungiObj != null)
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
			LOG.error("FungicideMasterServiceImpl::saveAndUpdate():" + e);
			str = WrsisPortalConstant.FAILURE;
		}

		return str;
	}

	@Override
	public FungicideMaster getFungicideById(Integer fungicideId) {
		return fungicideRepository.getFungicideMasterById(fungicideId);
	}

	@Override
	public String viewFungicideByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<FungicideMaster> page = fungicideRepository
					.viewAllFungicidePage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (FungicideMaster fMaster : page) {
					jsonObject = new JSONObject();
					if (fMaster != null) {
						jsonObject.put("id", fMaster.getFungicideId() != 0 ? fMaster.getFungicideId() : "-NA-");
						jsonObject.put("name",
								fMaster.getFungicideName() != null ? fMaster.getFungicideName() : "-NA-");
						jsonObject.put("aliseName",
								fMaster.getFungicideAliasName() != null ? fMaster.getFungicideAliasName() : "-NA-");
						jsonObject.put("quantity", fMaster.getQuantity() != null ? fMaster.getQuantity() : "-NA-");
						jsonObject.put("descr", fMaster.getDescription() != null ? fMaster.getDescription() : "-NA-");
						if (!(fMaster.isStatus() ))
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("fungiList", array);

			}
		} catch (Exception e) {
			LOG.error("FungicideMasterServiceImpl::viewFungicideByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public List<FungicideMaster> viewAllFungicide() {
		return fungicideRepository.viewAllFungicide();
	}

	@Override
	public String searchFungicide(String fungicideName, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<FungicideMaster> page = null;
		try {
			if (!fungicideName.equals("") && !status.equals("select")) {
				page = fungicideRepository.findByFungicideNameIgnoreCaseAndStatus(fungicideName,
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (!fungicideName.equals("")) {
				page = fungicideRepository.findByFungicideNameContainingIgnoreCase(fungicideName,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (!status.equals("select")) {
				page = fungicideRepository.findByStatusOrderByFungicideIdDesc(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else {
				page = fungicideRepository
						.viewAllFungicidePage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (FungicideMaster fMaster : page) {
					jsonObject = new JSONObject();
					if (fMaster != null) {
						jsonObject.put("id", fMaster.getFungicideId() != 0 ? fMaster.getFungicideId() : "-NA-");
						jsonObject.put("name",
								fMaster.getFungicideName() != null ? fMaster.getFungicideName() : "-NA-");
						jsonObject.put("aliseName",
								fMaster.getFungicideAliasName() != null ? fMaster.getFungicideAliasName() : "-NA-");
						jsonObject.put("quantity", fMaster.getQuantity() != null ? fMaster.getQuantity() : "-NA-");
						jsonObject.put("descr", fMaster.getDescription() != null ? fMaster.getDescription() : "-NA-");
						if (!(fMaster.isStatus() ))
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("fungiList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("FungicideMasterServiceImpl::searchFungicide():" + e);
		}
		return jObject.toString();
	}

}
