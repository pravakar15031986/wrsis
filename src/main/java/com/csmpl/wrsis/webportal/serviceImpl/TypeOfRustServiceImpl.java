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
import com.csmpl.wrsis.webportal.entity.MonitorRustEntity;
import com.csmpl.wrsis.webportal.entity.SurveyRustDetailsEntity;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.MonitorRustRepository;
import com.csmpl.wrsis.webportal.repository.SurveyRustDetailsRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;

@Service
public class TypeOfRustServiceImpl implements TypeOfRustService {

	private static final Logger LOG = LoggerFactory.getLogger(TypeOfRustServiceImpl.class);
	@Autowired
	TypeOfRustRepository typeOfRustRepository;
	@Autowired
	SurveyRustDetailsRepository surveyRustDetailsRepository;
	@Autowired
	MonitorRustRepository monitorRustRepository;

	@Override
	public String saveAndUpdateTypeOfRust(TypeOfRust rust) {
		String sts = "";
		try {
			if (rust.getRustId() == 0) {
				List<Object[]> rustTypes = typeOfRustRepository.getRustTypes(rust.getTypeOfRust());
				if (!rustTypes.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				typeOfRustRepository.save(rust);
				sts = WrsisPortalConstant.SUCCESS;
			} else {
				List<Object[]> rustTypes = typeOfRustRepository.getRustTypes(rust.getRustId(), rust.getTypeOfRust());
				if (!rustTypes.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				List<SurveyRustDetailsEntity> list1 = surveyRustDetailsRepository
						.findRustTypeIdByRustTypeId(rust.getRustId());
				List<MonitorRustEntity> list2 = monitorRustRepository.findRusttypeidByRusttypeid(rust.getRustId());
				if (!rust.getIsDelete()) {
					typeOfRustRepository.save(rust);
					sts = WrsisPortalConstant.UPDATE;
				} else {
					if (list1.isEmpty() && list2.isEmpty()) {
						typeOfRustRepository.save(rust);
						sts = WrsisPortalConstant.UPDATE;
					} else {
						return WrsisPortalConstant.DEPENDENT;
					}
				}
			}
		} catch (Exception e) {
			LOG.error("TypeOfRustServiceImpl::saveAndupdateTypeOfRust():" + e);
			sts = WrsisPortalConstant.FAILURE;
		}
		
		return sts;
	}

	@Override
	public TypeOfRust getTypeOfRustById(Integer rustId) {
		return typeOfRustRepository.getTypeIfRustById(rustId);
	}

	@Override
	public List<TypeOfRust> vewAllTypeOFRust() {

		return typeOfRustRepository.vewAllTypeOFRustByStatus();
	}

	@Override
	public String TypeOfRustByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jobjb = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<TypeOfRust> page = typeOfRustRepository
					.viewAllTypeOfRustPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jobjb.put("currentPage", currentPage);
				jobjb.put("startPage", begingPage);
				jobjb.put("endPage", endPage);
				jobjb.put("pageNo", page.getNumber());
				jobjb.put("showRowNo", page.getSize());
				jobjb.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObj = null;
				for (TypeOfRust typeOfRust : page) {
					jsonObj = new JSONObject();
					if (typeOfRust != null) {
						jsonObj.put(WrsisPortalConstant.RUSTID, typeOfRust.getRustId() != 0 ? typeOfRust.getRustId() : "-Na-");
						jsonObj.put(WrsisPortalConstant.RUSTNAME,
								typeOfRust.getTypeOfRust() != null ? typeOfRust.getTypeOfRust() : "-NA-");
						jsonObj.put("shortName",
								typeOfRust.getShortName() != null ? typeOfRust.getShortName() : "-NA-");
						jsonObj.put("description",
								typeOfRust.getRustDescription() != null ? typeOfRust.getRustDescription() : "-NA-");
						if (!typeOfRust.getIsDelete())
							jsonObj.put("status", "Active");
						else
							jsonObj.put("status", "Inactive");
					}
					array.put(jsonObj);
				}
				jobjb.put("rustList", array);

			} else {
				jobjb.put("rustList", array);
			}
		} catch (Exception e) {
			LOG.error("TypeOfRustServiceImpl::TypeOfRustByPage():" + e);
		}
		return jobjb.toString();
	}

	@Override
	public String searchTypeOfRustByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String fName,
			String sName, String status) {
		JSONObject jobjb = new JSONObject();
		JSONArray array = new JSONArray();
		Page<TypeOfRust> page = null;
		try {

			if (!fName.equals("") && !status.equals("select")) {
				page = typeOfRustRepository.searchByTypeOfRustContainsIgnoreCaseAndIsDelete(fName,
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (page == null) {
				if (!sName.equals("") && !status.equals("select")) {
					page = typeOfRustRepository.searchByShortNameContainsIgnoreCaseAndIsDelete(sName,
							Boolean.parseBoolean(status),
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
				if (page == null) {
					if (!fName.equals("")) {
						page = typeOfRustRepository.findByTypeOfRustContainingIgnoreCase(fName,
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					} else if (!sName.equals("")) {
						page = typeOfRustRepository.findByShortNameContainingIgnoreCase(sName,
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					}

					else if (!status.equals("select")) {
						page = typeOfRustRepository.findByisDeleteOrderByRustIdDesc(Boolean.parseBoolean(status),
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					} else {
						page = typeOfRustRepository.viewAllTypeOfRustPage(
								new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
					}

				}
			}
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jobjb.put("currentPage", currentPage);
				jobjb.put("startPage", begingPage);
				jobjb.put("endPage", endPage);
				jobjb.put("pageNo", page.getNumber());
				jobjb.put("showRowNo", page.getSize());
				jobjb.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObj = null;
				for (TypeOfRust typeOfRust : page) {
					jsonObj = new JSONObject();
					if (typeOfRust != null) {
						jsonObj.put(WrsisPortalConstant.RUSTID, typeOfRust.getRustId() != 0 ? typeOfRust.getRustId() : "-Na-");
						jsonObj.put(WrsisPortalConstant.RUSTNAME,
								typeOfRust.getTypeOfRust() != null ? typeOfRust.getTypeOfRust() : "-NA-");
						jsonObj.put("shortName",
								typeOfRust.getShortName() != null ? typeOfRust.getShortName() : "-NA-");
						jsonObj.put("description",
								typeOfRust.getRustDescription() != null ? typeOfRust.getRustDescription() : "-NA-");
						if (!typeOfRust.getIsDelete())
							jsonObj.put("status", "Active");
						else
							jsonObj.put("status", "Inactive");
					}
					array.put(jsonObj);
				}
				jobjb.put("rustList", array);

			} else {
				jobjb.put(WrsisPortalConstant.TOTAL_ROW_NO, 0);
			}

		} catch (Exception e) {
			LOG.error("TypeOfRustServiceImpl::searchtypeOfRustByPage():" + e);
		}
		return jobjb.toString();
	}

}
