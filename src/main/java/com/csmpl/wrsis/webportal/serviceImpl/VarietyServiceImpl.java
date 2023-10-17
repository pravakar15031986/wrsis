package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.VarietyBean;
import com.csmpl.wrsis.webportal.entity.MonitorVarietyEntity;
import com.csmpl.wrsis.webportal.entity.SurveySiteEntity;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.repository.MonitorVarietyRepository;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.service.VarietyService;

@Service
public class VarietyServiceImpl implements VarietyService {

	public static final Logger LOG = LoggerFactory.getLogger(VarietyServiceImpl.class);

	@Autowired
	VarietyTypeRepository varietyRepository;
	@Autowired
	SurveySiteRepository surveySiteRepository;
	@Autowired
	MonitorVarietyRepository monitorVarietyRepository;

	@Transactional
	@Override
	public String addvariety(VarietyBean variety) {
		String result = "";
		VarietyTypeMasterEntity vObj = new VarietyTypeMasterEntity();
		try {

			if (variety.getVarietyTypeId() == 0) {
				List<Object[]> varietyname = varietyRepository.getVariety(variety.getVchDesc());
				if (!varietyname.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				vObj.setVchDesc(variety.getVchDesc());
				vObj.setBitStatus(variety.isBitStatus());
				vObj.setCreatedBy(variety.getCreatedBy());
				vObj.setCreatedOn(new Date());
				varietyRepository.saveAndFlush(vObj);
				result = WrsisPortalConstant.SUCCESS;
			} else {
				List<Object[]> varietyname = varietyRepository.getVariety(variety.getVarietyTypeId(),
						variety.getVchDesc());
				if (!varietyname.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				VarietyTypeMasterEntity vid = varietyRepository.findByVarietyId(variety.getVarietyTypeId());
				if (vid != null) {
					List<SurveySiteEntity> list1 = surveySiteRepository
							.findOtherVarietyByOtherVariety(Integer.toString(variety.getVarietyTypeId()));
					List<MonitorVarietyEntity> list2 = monitorVarietyRepository
							.findVarietyidByVarietyid(variety.getVarietyTypeId());
					if (!variety.isBitStatus()) {
						vid.setVchDesc(variety.getVchDesc());
						vid.setBitStatus(variety.isBitStatus());
						vid.setUpdatedBy(variety.getUpdatedBy());
						vid.setUpdatedOn(new Date());
						varietyRepository.saveAndFlush(vid);
						result = WrsisPortalConstant.UPDATE;
					} else {
						if (list1.isEmpty() && list2.isEmpty()) {
							vid.setVchDesc(variety.getVchDesc());
							vid.setBitStatus(variety.isBitStatus());
							vid.setUpdatedBy(variety.getUpdatedBy());
							vid.setUpdatedOn(new Date());
							varietyRepository.saveAndFlush(vid);
							result = WrsisPortalConstant.UPDATE;
						} else {
							return WrsisPortalConstant.DEPENDENT;
						}
					}

				}
			}
		} catch (Exception e) {
			LOG.error("VarietyServiceImpl::addvariety():" + e);
			result = WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public VarietyTypeMasterEntity getVarietyById(Integer id) {
		VarietyTypeMasterEntity variety = null;
		try {
			variety = varietyRepository.getOne(id);
			if (variety != null) {
				variety.getVarietyTypeId();
				variety.getVchDesc();
			}
		} catch (Exception e) {
			LOG.error("VarietyServiceImpl::getVarietyById():" + e);
		}
		return variety;
	}

	@Override
	public List<VarietyBean> viewVariety(SearchVo searchVo) {
		List<VarietyBean> varietyList = new ArrayList<>();
		VarietyBean vo = null;
		boolean condition = false;
		List<Object[]> obj = null;
		try {

			if (searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = varietyRepository.viewVarietyList(condition);

			} else {
				obj = varietyRepository.viewVarietyList();
			}
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new VarietyBean();
					vo.setVarietyTypeId((Short) dto[0]);
					vo.setVchDesc(String.valueOf(dto[1]));
					vo.setBitStatus((Boolean) dto[2]);
					varietyList.add(vo);
				}
				LOG.info("view List size of Variety " + varietyList.size());
			}
		} catch (Exception e) {
			LOG.error("VarietyServiceImpl::viewVariety():" + e);
		}
		return varietyList;
	}

}
