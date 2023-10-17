package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
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

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.SampleDetailsEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.SampleDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.service.SampleTypeService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class SampleTypeServiceImpl implements SampleTypeService {

	private static final Logger LOG = LoggerFactory.getLogger(SampleTypeServiceImpl.class);
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;
	@Autowired
	UserResearchCenterMappingRepository userRCMappingRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	TypeOfRustRepository typeOfRustRepository;
	@Autowired
	SampleDetailsRepository sampleDetailsRepository;

	@Override
	public String saveAndUpdateSampleType(SampleTypeMaster sampleTypeMaster) {
		String res = "";
		try {
			if (sampleTypeMaster.getSampleId() == 0) {
				List<SampleTypeMaster> sampleType = sampleTypeRepository
						.findBySampleNameIgnoreCase(sampleTypeMaster.getSampleName());
				if (!sampleType.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				sampleTypeMaster.setCreadtedOn(new Date());
				SampleTypeMaster s = sampleTypeRepository.save(sampleTypeMaster);
				if (s != null)
					res = WrsisPortalConstant.SAVE;
				else
					res = WrsisPortalConstant.FAILURE;
			} else {
				List<SampleTypeMaster> sampleType = sampleTypeRepository.getSampleType(sampleTypeMaster.getSampleName(),
						sampleTypeMaster.getSampleId());
				if (!sampleType.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				SampleTypeMaster sMaster = sampleTypeRepository.findBySampleId(sampleTypeMaster.getSampleId());
				if (sMaster != null) {
					List<SampleDetailsEntity> list = sampleDetailsRepository
							.findSampleTypeIdBySampleTypeId(sampleTypeMaster.getSampleId());
					if (!sampleTypeMaster.isStatus()) {
						sampleTypeMaster.setUpdatedBy(sampleTypeMaster.getCreatedBy());
						sampleTypeMaster.setUpdatedOn(new Date());
						sampleTypeMaster.setCreadtedOn(sMaster.getCreadtedOn());
						SampleTypeMaster s = sampleTypeRepository.save(sampleTypeMaster);
						if (s != null)
							res = WrsisPortalConstant.UPDATE;
						else
							res = WrsisPortalConstant.FAILURE;
					} else {
						if (list.isEmpty()) {
							sampleTypeMaster.setUpdatedBy(sampleTypeMaster.getCreatedBy());
							sampleTypeMaster.setUpdatedOn(new Date());
							sampleTypeMaster.setCreadtedOn(sMaster.getCreadtedOn());
							SampleTypeMaster s = sampleTypeRepository.save(sampleTypeMaster);
							if (s != null)
								res = WrsisPortalConstant.UPDATE;
							else
								res = WrsisPortalConstant.FAILURE;
						} else {
							return WrsisPortalConstant.DEPENDENT;
						}
					}

				}
			}

		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::saveAndUpdateSampleType():" + e);
		}
		return res;
	}

	@Override
	public SampleTypeMaster getSampleTypeById(Integer sampleId) {
		SampleTypeMaster s = null;
		try {
			s = sampleTypeRepository.findBySampleId(sampleId);
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::getSampleTypeById():" + e);
		}
		return s;
	}

	@Override
	public List<SampleTypeMaster> viewAllSample() {
		return sampleTypeRepository.viewAllSample();
	}

	@Override
	public String viewSampleTypeByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Object[]> page = sampleTypeRepository
					.viewAllSampleTypePage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (Object[] sampleType : page) {
					jsonObject = new JSONObject();
					if (sampleType != null) {
						jsonObject.put("id", (Short) sampleType[0] != 0 ? (Short) sampleType[0] : "-NA-");
						jsonObject.put("rustType",
								!(String.valueOf(sampleType[2]).equals("")) ? String.valueOf(sampleType[2]) : "-NA-");
						jsonObject.put("name",
								!(String.valueOf(sampleType[3]).equals("")) ? String.valueOf(sampleType[3]) : "-NA-");
						jsonObject.put("desc",
								!(String.valueOf(sampleType[4]).equals("")) ? String.valueOf(sampleType[4]) : "-NA-");
						if ((boolean) sampleType[5] == false)
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");

					}
					array.put(jsonObject);
				}
				jObject.put("sampleList", array);
			}

		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewsampleTypeByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchSampleTypeBySampleName(Integer pageSize, Integer pageNumber, Pageable pageable, String rustType,
			String status) {

		LOG.info("name==" + rustType);
		LOG.info("status===" + status);

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<Object[]> page = null;
		try {
			int rustTypeId = Integer.parseInt(rustType);
			if (rustTypeId != 0 && status.equals(WrsisPortalConstant.SELECT)) {
				page = sampleTypeRepository.findByRustTypeId(rustTypeId,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (rustTypeId == 0 && !status.equals(WrsisPortalConstant.SELECT)) {
				page = sampleTypeRepository.findByStatus(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else if (rustTypeId != 0 && !status.equals(WrsisPortalConstant.SELECT)) {
				page = sampleTypeRepository.findByRustTypeIdAndStatus(rustTypeId, Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			} else {
				page = sampleTypeRepository
						.getAllSampleTypes(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (Object[] sampleType : page) {
					jsonObject = new JSONObject();
					if (sampleType != null) {
						jsonObject.put("id", (Short) sampleType[0] != 0 ? (Short) sampleType[0] : "-NA-");
						jsonObject.put("rustType",
								!(String.valueOf(sampleType[2]).equals("")) ? String.valueOf(sampleType[2]) : "-NA-");
						jsonObject.put("name",
								!(String.valueOf(sampleType[3]).equals("")) ? String.valueOf(sampleType[3]) : "-NA-");
						jsonObject.put("desc",
								!(String.valueOf(sampleType[4]).equals("")) ? String.valueOf(sampleType[4]) : "-NA-");
						if ((boolean) sampleType[5] == false)
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");

					}
					array.put(jsonObject);
				}
				jObject.put("sampleList", array);
			}

		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::searchSampleTypeBySampleName():" + e);
		}
		return jObject.toString();
	}

	@Override
	public List<SampleLabTagBean> viewExtTaggedSamples(SearchVo searchVo) {
		List<SampleLabTagBean> extTaggedSampleList = new ArrayList<>();
		SampleLabTagBean vo = null;
		List<Object[]> obj = null;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			
			obj = sampleLabTagDetailsRepository.viewExtTaggedSamplesPage(searchVo.getSurveyNo().trim().toUpperCase(),
					searchVo.getSampleId().trim(), searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(),
					searchVo.getRegionId(), searchVo.getRustTypeId(), searchVo.getPageSize(), searchVo.getPageLength());
			Integer count = 0;
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new SampleLabTagBean();
					vo.setsNo((Integer.valueOf(searchVo.getPageSize())) + (++count));
					vo.setSampleLabTagId((Integer.parseInt(String.valueOf(dto[0]))));
					vo.setSampleIdValue(String.valueOf(dto[5]));
					vo.setSurveyId((Integer.parseInt(String.valueOf(dto[3]))));
					vo.setSurveyNoLink("<a href='javascript:void(0);' class='viewsurvey' survey-id='" + vo.getSurveyId()
							+ "'>" + String.valueOf(dto[4]) + "</a>");
					vo.setSurveyNo(String.valueOf(dto[4]));
					vo.setSurveyDate(String.valueOf(dto[6]));
					vo.setCreatedOn(String.valueOf(dto[7]));
					vo.setRegionName(String.valueOf(dto[9]));
					vo.setRustType(String.valueOf(dto[11]));
					vo.setEditLink("<a data-placement=\"top\" data-toggle=\"tooltip\"  id=\"edit"
							+ vo.getSampleLabTagId() + "\" onclick=\"editextTaggedSamples(" + vo.getSampleLabTagId()
							+ ");\" title=\"Update Race Result\">\r\n"
							+ "                      	<button class=\"btn btn-primary btn-xs\" data-title=\"Edit\" data-toggle=\"modal\" data-target=\"#edit\" >\r\n"
							+ "                      	<i class=\"icon-edit1\"></i>\r\n"
							+ "                      	</button>\r\n" + "                      	</a>");
					extTaggedSampleList.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewExtTaggedSamples():" + e);
		}
		return extTaggedSampleList;
	}

	@Override
	public SampleLabTagBean getSampleDetails(int sampleLabTagId) {
		SampleLabTagBean sample = new SampleLabTagBean();
		try {
			List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.getTagDetails(sampleLabTagId);
			for (Object[] objs : sampleTagDetails) {
				sample.setSampleLabTagId((Integer) objs[0]);
				sample.setSampleIdValue(String.valueOf(objs[1]));
				sample.setSurveyNo(String.valueOf(objs[2]));
				sample.setSurveyDate(DateUtil.DateToString((Date) (objs[3]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
				sample.setRegionName(objs[5].toString());
				int rustId = (Short) objs[6];
				sample.setRustTypeId(rustId);
				sample.setRustType(String.valueOf(objs[7]));
				int rcId = (Short) objs[8];
				sample.setResearchCenterId(rcId);
				sample.setResearchCenterName(String.valueOf(objs[9]));
				sample.setExternalLab(String.valueOf(objs[10]));
			}
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::getsampleDetails():" + e);

		}
		return sample;
	}

	@Override
	public String updtExtTaggedSample(SampleLabTagDetails extTaggedSampledetails) {
		String rtn = "";
		try {
			SampleLabTagDetails obj = sampleLabTagDetailsRepository
					.findBySampleLabTagId(extTaggedSampledetails.getSampleLabTagId());
			if (extTaggedSampledetails.getRaceStatus() == 2) {
				obj.setRaceResult(extTaggedSampledetails.getRaceResult());
				obj.setRaceStatus(2);
				rtn = "raceResultSuccess";
			} else if (extTaggedSampledetails.getRaceStatus() == 3) {
				obj.setRejectRemark(extTaggedSampledetails.getRejectRemark());
				obj.setRaceStatus(3);
				rtn = "dumpSuccess";
			}
			obj.setUpdatedBy(extTaggedSampledetails.getUpdatedBy());
			obj.setUpdatedOn(new Date());
			sampleLabTagDetailsRepository.save(obj);
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::updtEXTTaggedSample():" + e);

			rtn = WrsisPortalConstant.FAILURE;
		}
		return rtn;
	}

	@Override
	public List<SampleLabTagBean> viewExtRaceResult(SearchVo searchVo) {
		List<SampleLabTagBean> extTaggedSampleList = new ArrayList<>();
		SampleLabTagBean vo = null;
		List<Object[]> obj = null;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			
			obj = sampleLabTagDetailsRepository.viewExtRaceResultPage(searchVo.getSurveyNo().trim().toUpperCase(),
					searchVo.getSampleId().trim(), searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(),
					searchVo.getRegionId(), searchVo.getRustTypeId(), searchVo.getPageSize(), searchVo.getPageLength());
			Integer count = 0;
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new SampleLabTagBean();
					vo.setsNo((Integer.valueOf(searchVo.getPageSize())) + (++count));
					vo.setSampleLabTagId((Integer.parseInt(String.valueOf(dto[0]))));
					vo.setSampleIdValue(String.valueOf(dto[5]));
					vo.setSurveyId((Integer.parseInt(String.valueOf(dto[3]))));
					vo.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""
							+ vo.getSurveyId() + "\">" + String.valueOf(dto[4]) + "</a>");
					vo.setSurveyDate(String.valueOf(dto[6]));
					vo.setCreatedOn(String.valueOf(dto[7]));
					vo.setRegionName(String.valueOf(dto[9]));
					vo.setRustType(String.valueOf(dto[11]));
					vo.setRaceResult(String.valueOf(dto[13]));
					extTaggedSampleList.add(vo);

				}
				LOG.info("view List size of exttaggedSample " + extTaggedSampleList.size());
			}
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewExtRaceresult():" + e);

		}
		return extTaggedSampleList;
	}

	@Override
	public List<SampleLabTagBean> viewdumpedSamples(SearchVo searchVo) {
		List<SampleLabTagBean> extTaggedSampleList = new ArrayList<>();
		int rcId = 0;
		SampleLabTagBean vo = null;
		List<Object[]> obj = null;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			UserResearchCenterMapping rcObj = userRCMappingRepository.findByUserId(searchVo.getUserId());
			if (rcObj != null) {
				rcId = rcObj.getRecearchCenterId();
			}
			
			Integer count = 0;
			obj = sampleLabTagDetailsRepository.viewDumpedSamplesPage(searchVo.getSurveyNo().trim().toUpperCase(),
					searchVo.getSampleId().trim(), searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(),
					searchVo.getRegionId(), searchVo.getRustTypeId(), rcId, searchVo.getPageSize(),
					searchVo.getPageLength());
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new SampleLabTagBean();
					vo.setsNo((Integer.valueOf(searchVo.getPageSize())) + (++count));

					vo.setSampleLabTagId((Integer.parseInt(String.valueOf(dto[0]))));
					vo.setSampleIdValue(String.valueOf(dto[5]));
					vo.setSurveyId((Integer.parseInt(String.valueOf(dto[3]))));
					vo.setSurveyNo("<a href='javascript:void(0);' class='viewsurvey' survey-id='" + vo.getSurveyId()
							+ "'>" + String.valueOf(dto[4]) + "</a>");
					vo.setSurveyDate(String.valueOf(dto[6]));
					vo.setCreatedOn(String.valueOf(dto[7]));
					vo.setRegionName(String.valueOf(dto[9]));
					vo.setRustType(String.valueOf(dto[11]));
					vo.setRejectRemark(String.valueOf(dto[13]));
					vo.setExternalLab(String.valueOf(dto[14]));
					if (vo.getExternalLab().equals("false")) {
						vo.setExternalLab(String.valueOf(dto[15]));
					} else {
						vo.setExternalLab("External");
					}
					extTaggedSampleList.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewdumppedSamples():" + e);

		}
		return extTaggedSampleList;
	}

	@Override
	public List<DemographicEntity> viewRegionList(int rcId) {
		List<DemographicEntity> regionList = null;
		DemographicEntity obj;
		if (rcId == 0) {
			regionList = demographicRepository.findByLevelId(2);
		} else {
			List<Object[]> regionList1 = demographicRepository.retriveRegionsByResearchcenter(rcId);
			regionList = new ArrayList<>();
			for (Object[] objects : regionList1) {
				obj = new DemographicEntity();
				obj.setDemographyId(Integer.parseInt(objects[0].toString()));
				obj.setDemographyName(String.valueOf(objects[2]));
				regionList.add(obj);
			}
		}
		return regionList;
	}

	@Override
	public List<TypeOfRust> getRustList(int rcId) {
		List<TypeOfRust> rustList = null;
		TypeOfRust obj;
		if (rcId == 0) {
			rustList = typeOfRustRepository.getRustTypes();
		} else {
			List<Object[]> rustList1 = demographicRepository.retriveRustByResearchcenter(rcId);
			rustList = new ArrayList<>();
			for (Object[] objects : rustList1) {
				obj = new TypeOfRust();
				obj.setRustId(Integer.parseInt(objects[0].toString()));
				obj.setTypeOfRust(String.valueOf(objects[2]));
				rustList.add(obj);
			}
		}
		return rustList;
	}

	@Override
	public Integer viewdumpedSamplesCount(SearchVo searchVo) {
		Integer count = 0;
		int rcId = 0;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			UserResearchCenterMapping rcObj = userRCMappingRepository.findByUserId(searchVo.getUserId());
			if (rcObj != null) {
				rcId = rcObj.getRecearchCenterId();
			}
			count = sampleLabTagDetailsRepository.viewDumpedSamplesPageCount(searchVo.getSurveyNo(),
					searchVo.getSampleId(), searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(),
					searchVo.getRegionId(), searchVo.getRustTypeId(), rcId, searchVo.getPageSize(),
					searchVo.getPageLength());

		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewdumpedsamplesCount():" + e);

		}
		return count;

	}

	@Override
	public Integer viewExtTaggedSamplesCount(SearchVo searchVo) {

		Integer count = 0;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			count = sampleLabTagDetailsRepository.viewExtTaggedSamplesPageCont(
					searchVo.getSurveyNo().trim().toUpperCase(), searchVo.getSampleId().trim(),
					searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(), searchVo.getRegionId(),
					searchVo.getRustTypeId(), searchVo.getPageSize(), searchVo.getPageLength());
		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewExtTaggedSamplesCount():" + e);

		}
		return count;

	}

	@Override
	public Integer viewExtRaceResultCount(SearchVo searchVo) {

		Integer count = 0;
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		try {
			count = sampleLabTagDetailsRepository.viewExtRaceResultPageCount(
					searchVo.getSurveyNo().trim().toUpperCase(), searchVo.getSampleId().trim(),
					searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(), searchVo.getRegionId(),
					searchVo.getRustTypeId(), searchVo.getPageSize(), searchVo.getPageLength());

		} catch (Exception e) {
			LOG.error("SampleTypeServiceImpl::viewExtRaceResultCount():" + e);
		}
		return count;

	}
}
