package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DemographicBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;

@Component
public class ManageDemographicServiceImpl implements ManageDemographicService {

	private static final Logger LOG = LoggerFactory.getLogger(ManageDemographicServiceImpl.class);

	@Autowired
	DemographicRepository demographyRepository;

	@Override
	public List<DemographicEntity> viewCountry() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(1);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewCountry():" + e);
		}
		return obj;
	}

	@Override
	public String saveRegionName(DemographicBean region) {
		DemographicEntity demoEnt = null;
		String sts = "";
		try {
			List<DemographicEntity> existingRegionName = demographyRepository
					.getExistRegion(region.getDemographyName());
			if (!existingRegionName.isEmpty())
				return WrsisPortalConstant.EXIST;
			List<DemographicEntity> existingAlias = demographyRepository.getExistAlias(region.getAlias());
			if (!existingAlias.isEmpty())
				return "aliasexists";
			demoEnt = new DemographicEntity();
			demoEnt.setDemographyName(region.getDemographyName().toUpperCase());
			demoEnt.setAlias(region.getAlias().toUpperCase());
			demoEnt.setDescription(region.getDescription());
			demoEnt.setStatus(region.getStatus());
			demoEnt.setLevelId(2);
			demoEnt.setParentId(region.getCountryId());
			demoEnt.setCreateOn(new Date());
			demoEnt.setCreatedBy(region.getUserId());
			demographyRepository.save(demoEnt);
			sts = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			sts = WrsisPortalConstant.FAILURE;
			LOG.error("ManageDemographicServiceImpl::saveRegionName():" + e);

		}
		return sts;
	}

	@Override
	public DemographicEntity getCountryByParentId(Integer parentId) {

		return demographyRepository.findByparentId(parentId);
	}

	@Override
	public DemographicEntity editRegion(int demographyId) {
		DemographicEntity obj = null;
		try {
			obj = demographyRepository.getOne(demographyId);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::editRegion():" + e);

		}
		return obj;
	}

	@Override
	public String updateRegion(@Valid DemographicBean demoBean) {
		String result = "";
		Date date = new Date();
		try {
			List<DemographicEntity> existingRegionName = demographyRepository
					.getExistRegion(demoBean.getDemographyName(), demoBean.getDemographyId());
			if (!existingRegionName.isEmpty())
				return WrsisPortalConstant.EXIST;
			List<DemographicEntity> existingAlias = demographyRepository.getExistAlias(demoBean.getAlias(),
					demoBean.getDemographyId());
			if (!existingAlias.isEmpty())
				return "aliasexists";
			DemographicEntity demoEnt = demographyRepository.findByDemographyId(demoBean.getDemographyId());
			if (demoEnt != null) {
				demoEnt.setDemographyName(demoBean.getDemographyName().toUpperCase());
				demoEnt.setAlias(demoBean.getAlias().toUpperCase());
				demoEnt.setDescription(demoBean.getDescription());
				demoEnt.setLevelId(2);
				demoEnt.setParentId(demoBean.getCountryId());
				demoEnt.setStatus(demoBean.getStatus());
				demoEnt.setUpdateOn(date);
				demoEnt.setUpdateBy(demoBean.getUserId());
				demographyRepository.save(demoEnt);
				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;

			LOG.error("ManageDemographicServiceImpl::updateRegion():" + e);

		}
		return result;
	}

	@Override
	public List<DemographicEntity> regionList() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(2);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::regionList():" + e);

		}
		return obj;
	}

	@Override
	public String saveZoneName(DemographicBean zone) {
		DemographicEntity demoEnt = null;
		String sts = "";
		try {
			List<DemographicEntity> existZone = demographyRepository.getExistZone(zone.getDemographyName());
			if (!existZone.isEmpty()) {
				return WrsisPortalConstant.EXIST;
			}
			demoEnt = new DemographicEntity();
			demoEnt.setDemographyName(zone.getDemographyName().toUpperCase());
			demoEnt.setAlias(zone.getAlias().toUpperCase());
			demoEnt.setDescription(zone.getDescription());
			demoEnt.setStatus(zone.getStatus());
			demoEnt.setLevelId(3);
			demoEnt.setParentId(zone.getRegionId());
			demoEnt.setCreateOn(new Date());
			demoEnt.setCreatedBy(zone.getUserId());

			demographyRepository.save(demoEnt);
			sts = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			sts = WrsisPortalConstant.FAILURE;
			LOG.error("ManageDemographicServiceImpl::saveZoneNAme():" + e);

		}
		return sts;
	}

	@Override
	public List<DemographicEntity> zoneList() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(3);
			obj.sort(Comparator.comparing(DemographicEntity::getDemographyName));
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::zoneList():" + e);

		}
		return obj;
	}

	@Override
	public String saveWoredaName(DemographicBean woreda) {
		DemographicEntity demoEnt = null;
		String sts = "";
		try {
			demoEnt = new DemographicEntity();
			demoEnt.setDemographyName(woreda.getDemographyName().toUpperCase());
			demoEnt.setAlias(woreda.getAlias().toUpperCase());
			demoEnt.setDescription(woreda.getDescription());
			demoEnt.setStatus(woreda.getStatus());
			demoEnt.setLevelId(4);
			demoEnt.setParentId(woreda.getZoneId());
			demoEnt.setCreateOn(new Date());
			demoEnt.setCreatedBy(woreda.getUserId());

			demographyRepository.save(demoEnt);
			sts = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			sts = WrsisPortalConstant.FAILURE;
			LOG.error("ManageDemographicServiceImpl::saveWoredaName():" + e);

		}
		return sts;
	}

	@Override
	public List<DemographicEntity> woredaList() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(4);
			obj.sort(Comparator.comparing(DemographicEntity::getDemographyName));
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::woredaList():" + e);

		}
		return obj;
	}

	@Override
	public String saveKebeleName(DemographicBean kebele) {
		DemographicEntity demoEnt = null;
		String sts = "";
		try {
			demoEnt = new DemographicEntity();
			demoEnt.setDemographyName(kebele.getDemographyName().toUpperCase());
			demoEnt.setAlias(kebele.getAlias().toUpperCase());
			demoEnt.setDescription(kebele.getDescription());
			demoEnt.setStatus(kebele.getStatus());
			demoEnt.setLevelId(5);
			demoEnt.setParentId(kebele.getWoredaId());
			demoEnt.setCreateOn(new Date());
			demoEnt.setCreatedBy(kebele.getUserId());

			demographyRepository.save(demoEnt);
			sts = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			sts = WrsisPortalConstant.FAILURE;
			LOG.error("ManageDemographicServiceImpl::saveKebeleName():" + e);

		}
		return sts;
	}

	@Override
	public List<DemographicBean> viewZone(SearchVo searchVo) {

		List<DemographicBean> zoneList = new ArrayList<>();
		DemographicBean vo = null;
		boolean condition = false;
		int regionId = searchVo.getRegionId();
		String zoneName = searchVo.getZoneName();
		if (zoneName == null) {
			zoneName = "";
		}
		List<Object[]> obj = null;
		try {

			if (regionId != 0 &&  "".equals(zoneName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewZone(regionId);
			} else if (regionId != 0 && !("".equals(zoneName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewZone(regionId, zoneName);
			} else if (regionId != 0 && "".equals(zoneName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewZoneByRegionIdAndStatus(regionId, condition);
			} else if (regionId != 0 && !("".equals(zoneName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
					obj = demographyRepository.viewZone(regionId, zoneName.toUpperCase(), condition);
				} else if (searchVo.getDataId() == 2) {
					condition = true;
					obj = demographyRepository.viewZone(regionId, zoneName.toUpperCase(), condition);
				}
			} else if (regionId == 0 && !("".equals(zoneName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewZone(zoneName.toUpperCase());
			} else if (regionId == 0 && !("".equals(zoneName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
					obj = demographyRepository.viewZone(zoneName.toUpperCase(), condition);
				} else if (searchVo.getDataId() == 2) {
					condition = true;
					obj = demographyRepository.viewZone(zoneName.toUpperCase(), condition);
				}
			} else if (regionId == 0 && "".equals(zoneName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
					obj = demographyRepository.viewZone(condition);
				} else if (searchVo.getDataId() == 2) {
					condition = true;
					obj = demographyRepository.viewZone(condition);
				}
			} else {
				obj = demographyRepository.viewZone();
			}
			if (obj != null && !obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new DemographicBean();
					vo.setCountryName(String.valueOf(dto[0]));
					vo.setRegionName(String.valueOf(dto[1]));
					vo.setRegionId((Short) dto[2]);
					vo.setZoneName(String.valueOf(dto[3]));
					vo.setZoneId((Short) dto[4]);
					vo.setStatus((Boolean) dto[5]);
					vo.setAlias(String.valueOf(dto[6]));
					vo.setDescription(String.valueOf(dto[7]));
					zoneList.add(vo);
				}
				LOG.info("view List size of Zone " + zoneList.size());
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewZone():" + e);
		}
		return zoneList;
	}

	@Override
	public List<DemographicEntity> getRegionByParentId(int parentId) {

		return demographyRepository.findByParentId(parentId);
	}

	@Override
	public DemographicEntity editZone(int demographyId) {
		DemographicEntity obj = null;
		try {
			obj = demographyRepository.getOne(demographyId);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::editZone():" + e);

		}
		return obj;
	}

	@Override
	public String updateZone(@Valid DemographicBean demoBean) {
		String result = "";
		Date date = new Date();
		try {
			List<DemographicEntity> existZone = demographyRepository.getExistZone(demoBean.getDemographyId(),
					demoBean.getDemographyName());
			if (!existZone.isEmpty()) {
				return WrsisPortalConstant.EXIST;
			}
			DemographicEntity demoEnt = demographyRepository.findByDemographyId(demoBean.getDemographyId());
			if (demoEnt != null) {
				demoEnt.setDemographyName(demoBean.getDemographyName().toUpperCase());
				demoEnt.setAlias(demoBean.getAlias().toUpperCase());
				demoEnt.setDescription(demoBean.getDescription());
				demoEnt.setLevelId(3);
				demoEnt.setStatus(demoBean.getStatus());
				demoEnt.setUpdateOn(date);
				demoEnt.setUpdateBy(demoBean.getUserId());
				demoEnt.setParentId(demoBean.getRegionId());
				demographyRepository.save(demoEnt);
				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;

			LOG.error("ManageDemographicServiceImpl::updateZone():" + e);

		}
		return result;
	}

	@Override
	public List<DemographicBean> viewWoreda(SearchVo searchVo) {
		List<DemographicBean> woredaList = new ArrayList<>();
		DemographicBean vo = null;
		boolean condition = false;
		int regionId = searchVo.getRegionId();
		int zoneId = searchVo.getZoneId();
		String woredaName = searchVo.getWoredaName();
		if (woredaName == null) {
			woredaName = "";
		}
		List<Object[]> obj = null;
		try { // regionid only selected
			if (regionId != 0 && zoneId == 0 && "".equals(woredaName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByRegionId(regionId);
			} // regionid and zoneid is selected
			else if (regionId != 0 && zoneId != 0 && "".equals(woredaName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByRegionIdandZoneId(regionId, zoneId);
			}
			// regionid and woredaname provided
			else if (regionId != 0 && zoneId == 0 && !("".equals(woredaName)) && searchVo.getDataId() != 0) {
				obj = demographyRepository.viewWoredaByRegionIdandWoredaName(regionId, woredaName.toUpperCase());
			}
			// regionid and status provided
			else if (regionId != 0 && zoneId == 0 && "".equals(woredaName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByRegionIdandStatus(regionId, condition);
			}
			// regionid, zoneid and woreda name is given
			else if (regionId != 0 && zoneId != 0 && !("".equals(woredaName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByRegionIdZoneIdAndWoredaName(regionId, zoneId,
						woredaName.toUpperCase());
			} // regionid, zoneid and status is given
			else if (regionId != 0 && zoneId != 0 && "".equals(woredaName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByRegionIdZoneIdAndStatus(regionId, zoneId, condition);
			}
			// regionid, zoneid, woreda name and status are provided
			else if (regionId != 0 && zoneId != 0 && !("".equals(woredaName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByRegionIdZoneIdWoredaNameAndStatus(regionId, zoneId,
						woredaName.toUpperCase(), condition);
			} // zoneid provided
			else if (regionId == 0 && zoneId != 0 && "".equals(woredaName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByZoneId(zoneId);
			}
			// zoneid, woreda name are provided
			else if (regionId == 0 && zoneId != 0 && !("".equals(woredaName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByZoneIdAndWoredaName(zoneId, woredaName.toUpperCase());
			} // zone and status provided
			else if (regionId == 0 && zoneId != 0 && "".equals(woredaName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByZoneIdAndStatus(zoneId, condition);
			}
			// zoneid, woreda name and status are provided
			else if (regionId == 0 && zoneId != 0 && !("".equals(woredaName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByZoneIdWoredaNameAndStatus(zoneId, woredaName.toUpperCase(),
						condition);
			} // woreda name provided
			else if (regionId == 0 && zoneId == 0 && !("".equals(woredaName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewWoredaByWoredaName(woredaName.toUpperCase());
			} // woreda name and status are provided
			else if (regionId == 0 && zoneId == 0 && !("".equals(woredaName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByWoredaNameAndStatus(woredaName.toUpperCase(), condition);
			} // status provided
			else if (regionId == 0 && zoneId == 0 && "".equals(woredaName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewWoredaByStatus(condition);
			} else // nothing provided
			{
				obj = demographyRepository.viewWoreda();
			}
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new DemographicBean();
					vo.setCountryName(String.valueOf(dto[0]));
					vo.setCountryId((Short) dto[1]);
					vo.setRegionName(String.valueOf(dto[2]));
					vo.setRegionId((Short) dto[3]);
					vo.setZoneName(String.valueOf(dto[4]));
					vo.setZoneId((Short) dto[5]);
					vo.setWoredaName(String.valueOf(dto[6]));
					vo.setWoredaId((Short) dto[7]);
					vo.setStatus((Boolean) dto[8]);
					vo.setAlias(String.valueOf(dto[9]));
					vo.setDescription(String.valueOf(dto[10]));
					woredaList.add(vo);
				}
				LOG.info("view List size of Woreda " + woredaList.size());
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewWoreda():" + e);

		}
		return woredaList;
	}

	@Override
	public List<DemographicBean> viewKebele(SearchVo searchVo) {
		List<DemographicBean> kebeleList = new ArrayList<>();
		DemographicBean vo = null;
		boolean condition = false;
		int regionId = searchVo.getRegionId();
		int zoneId = searchVo.getZoneId();
		int woredaId = searchVo.getWoredaId();
		String kebeleName = searchVo.getKebeleName();
		if (kebeleName == null) {
			kebeleName = "";
		}
		List<Object[]> obj = null;
		try { // regionid only selected
			if (regionId != 0 && zoneId == 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionId(regionId);
			} // regionid and zoneId provided
			else if (regionId != 0 && zoneId != 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdandZoneId(regionId, zoneId);
			} // regionid and WoredaId provided
			else if (regionId != 0 && zoneId == 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdandWoredaId(regionId, woredaId);
			} // regionid and kebelename provided
			else if (regionId != 0 && zoneId == 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdandKebeleName(regionId, kebeleName.toUpperCase());
			} // regionid and status provided
			else if (regionId != 0 && zoneId == 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdandStatus(regionId, condition);
			} // regionid , zoneid and woredaid provided
			else if (regionId != 0 && zoneId != 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdZoneIdAndWoredaId(regionId, zoneId, woredaId);
			}
			// regionid , zoneid and status provided
			else if (regionId != 0 && zoneId != 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdZoneIdAndStatus(regionId, zoneId, condition);
			}
			// regionid, zoneid and kebele name provided
			else if (regionId != 0 && zoneId != 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdZoneIdandKebeleName(regionId, zoneId,
						kebeleName.toUpperCase());
			}
			// regionid, woredaid and kebele name provided
			else if (regionId != 0 && zoneId == 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByRegionIdWoredaIdandKebeleName(regionId, woredaId,
						kebeleName.toUpperCase());
			} // regionid,kebele name and status provided
			else if (regionId != 0 && zoneId == 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdKebeleNameandStatus(regionId, kebeleName.toUpperCase(),
						condition);
			} // regionid,zoneid,woredaid and kebele name
			else if (regionId != 0 && zoneId != 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdZoneIdWoredaIdandStatus(regionId, zoneId, woredaId,
						condition);
			} // regionid,zoneid, kebele name and status provided
			else if (regionId != 0 && zoneId != 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdZoneIdKebeleNameandStatus(regionId, zoneId,
						kebeleName.toUpperCase(), condition);
			}
			// regionid,zoneid,woredaid, status provided
			else if (regionId != 0 && zoneId != 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				obj = demographyRepository.viewKebeleByRegionIdZoneIdWoredaIdandKebeleName(regionId, zoneId, woredaId,
						kebeleName.toUpperCase());
			} // regionid, woredaid,kebelename and status provided
			else if (regionId != 0 && zoneId == 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdWoredaIdKebeleNameandStatus(regionId, woredaId,
						kebeleName.toUpperCase(), condition);
			} // regionid,zoneid,woredaid,kebele name, status provided
			else if (regionId != 0 && zoneId != 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByRegionIdZoneIdWoredaIdKebeleNameandStatus(regionId, zoneId,
						woredaId, kebeleName.toUpperCase(), condition);
			} // zone provided
			else if (regionId == 0 && zoneId != 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByZoneId(zoneId);
			} // zoneid and woredaid provided
			else if (regionId == 0 && zoneId != 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByZoneIdAndWoredaId(zoneId, woredaId);
			} // zoneid and kebele name provided
			else if (regionId == 0 && zoneId != 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByZoneIdAndKebeleName(zoneId, kebeleName.toUpperCase());
			} // zoneid and status provided
			else if (regionId == 0 && zoneId != 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByZoneIdAndStatus(zoneId, condition);
			} // zoneId , woredaid,kebele name provided
			else if (regionId == 0 && zoneId != 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByZoneIdWoredaIdAndKebeleName(zoneId, woredaId,
						kebeleName.toUpperCase());
			} // zoneid, kebele name and status provided
			else if (regionId == 0 && zoneId != 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByZoneIdAndKebeleName(zoneId, kebeleName.toUpperCase(), condition);
			} // zoneid,woredaid,kebele name and status provided
			else if (regionId == 0 && zoneId != 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByZoneIdWoredaIdKebeleNameAndStatus(zoneId, woredaId,
						kebeleName.toUpperCase(), condition);
			} // woredaid provided
			else if (regionId == 0 && zoneId == 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByWoredaId(woredaId);
			}
			// woredaid and kebele name provided
			else if (regionId == 0 && zoneId == 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByWoredaIdAndKebeleName(woredaId, kebeleName.toUpperCase());
			}
			// woredaid and status provided
			else if (regionId == 0 && zoneId == 0 && woredaId != 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByWoredaIdAndStatus(woredaId, condition);
			}
			// woredaid ,kebele name and status provided
			else if (regionId == 0 && zoneId == 0 && woredaId != 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByWoredaIdKebeleNameAndStatus(woredaId, kebeleName.toUpperCase(),
						condition);
			} // kebele name provided
			else if (regionId == 0 && zoneId == 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() == 0) {
				obj = demographyRepository.viewKebeleByKebeleName(kebeleName.toUpperCase());
			}
			// kebele name and status provided
			else if (regionId == 0 && zoneId == 0 && woredaId == 0 && !("".equals(kebeleName)) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByKebeleNameAndStatus(kebeleName.toUpperCase(), condition);
			} // status provided
			else if (regionId == 0 && zoneId == 0 && woredaId == 0 && "".equals(kebeleName) && searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewKebeleByStatus(condition);
			} else // nothing provided
			{
				obj = demographyRepository.viewKebele();
			}
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new DemographicBean();
					vo.setCountryName(String.valueOf(dto[0]));
					vo.setCountryId((Short) dto[1]);
					vo.setRegionName(String.valueOf(dto[2]));
					vo.setRegionId((Short) dto[3]);
					vo.setZoneName(String.valueOf(dto[4]));
					vo.setZoneId((Short) dto[5]);
					vo.setWoredaName(String.valueOf(dto[6]));
					vo.setWoredaId((Short) dto[7]);
					vo.setKebeleName(String.valueOf(dto[8]));
					vo.setKebeleId((Short) (dto[9]));
					vo.setStatus((Boolean) dto[10]);
					vo.setAlias(String.valueOf(dto[11]));
					vo.setDescription(String.valueOf(dto[12]));
					kebeleList.add(vo);
				}
				LOG.info("ManageDemographicServiceImpl::view List size of Woreda " + kebeleList.size());
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewKebele():" + e);

		}
		return kebeleList;
	}

	@Override
	public String updateWoreda(@Valid DemographicBean woreda) {
		String result = "";
		Date date = new Date();
		try {

			DemographicEntity demoEnt = demographyRepository.findByDemographyId(woreda.getDemographyId());
			if (demoEnt != null) {
				demoEnt.setDemographyName(woreda.getDemographyName().toUpperCase());
				demoEnt.setAlias(woreda.getAlias().toUpperCase());
				demoEnt.setDescription(woreda.getDescription());
				demoEnt.setLevelId(4);
				demoEnt.setParentId(woreda.getZoneId());
				demoEnt.setStatus(woreda.getStatus());
				demoEnt.setUpdateOn(date);
				demoEnt.setUpdateBy(woreda.getUserId());
				demographyRepository.save(demoEnt);
				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;

			LOG.error("ManageDemographicServiceImpl::updateWoreda():" + e);

		}
		return result;
	}

	@Override
	public DemographicEntity editWoreda(int demographyId) {
		DemographicEntity obj = null;
		try {
			obj = demographyRepository.getOne(demographyId);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::editWoreda():" + e);

		}
		return obj;
	}

	@Override
	public DemographicEntity editKebele(int demographyId) {
		DemographicEntity obj = null;
		try {
			obj = demographyRepository.getOne(demographyId);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::editKebele():" + e);

		}
		return obj;
	}

	@Override
	public String updateKebele(@Valid DemographicBean kebele) {
		String result = "";
		Date date = new Date();
		try {

			DemographicEntity demoEnt = demographyRepository.findByDemographyId(kebele.getDemographyId());
			if (demoEnt != null) {
				demoEnt.setDemographyName(kebele.getDemographyName().toUpperCase());
				demoEnt.setAlias(kebele.getAlias().toUpperCase());
				demoEnt.setDescription(kebele.getDescription());
				demoEnt.setLevelId(5);
				demoEnt.setParentId(kebele.getWoredaId());
				demoEnt.setStatus(kebele.getStatus());
				demoEnt.setUpdateOn(date);
				demoEnt.setUpdateBy(kebele.getUserId());
				demographyRepository.save(demoEnt);
				result = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;

			LOG.error("ManageDemographicServiceImpl::updateKebele():" + e);

		}
		return result;
	}

	@Override
	public List<DemographicEntity> getDemographyListByParentId(int parentId) {
		List<DemographicEntity> list = demographyRepository.findByParentIdAndStatus(parentId, false);
		list.sort(Comparator.comparing(DemographicEntity::getDemographyName));
		return list;
	}

	@Override
	public List<DemographicEntity> viewAllRegionByStatus() {

		return demographyRepository.viewAllRegionByStatus();
	}

	@Override
	public String viewAllZoneByRegionIdAndStatus(Integer levelId, Integer parentId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<DemographicEntity> zoneList = demographyRepository.viewAllZoneByRegionIdAndStatus(levelId, parentId);

			for (DemographicEntity dObject : zoneList) {
				jobj = new JSONObject();
				jobj.put(WrsisPortalConstant.ZONE_ID, dObject.getDemographyId());
				jobj.put("zoneName", dObject.getDemographyName());
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewAllZoneByRegionIdAndStatus():" + e);

		}
		return jarr.toString();

	}

	@Override
	public String viewAllWoredaByZoneIdAndStatus(Integer levelId, Integer parentId) {

		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<DemographicEntity> zoneList = demographyRepository.viewAllDemographyExcludingOther(levelId, parentId);

			for (DemographicEntity dObject : zoneList) {
				jobj = new JSONObject();
				jobj.put("woridaId", dObject.getDemographyId());
				jobj.put("woridaName", dObject.getDemographyName());
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewAllWoredaByZoneIdAndStatus():" + e);
		}
		return jarr.toString();
	}

	@Override
	public DemographicEntity searchAllWoredaByZoneIdAndStatus(Integer levelId, Integer parentId) {

		return null;
	}

	@Override
	public String findMulultipleZoneByRegionId(Integer levelId, String regionId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;

		try {
			String[] s1 = regionId.split(",");

			List<DemographicEntity> zoneList = null;
			for (int i = 0; i < s1.length; i++) {

				zoneList = demographyRepository.viewAllZoneByRegionIdAndStatus(levelId, Integer.parseInt(s1[i]));

				for (DemographicEntity dObject : zoneList) {
					jobj = new JSONObject();

					jobj.put("zoneId", dObject.getDemographyId());

					jobj.put("zoneName", dObject.getDemographyName());
					jarr.put(jobj);
				}
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::findMulultipleZoneByRegionId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String findMulultipleWoredaByZoneId(Integer levelId, String zoneId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;

		try {
			String[] s1 = zoneId.split(",");

			List<DemographicEntity> woridaList = null;
			for (int i = 0; i < s1.length; i++) {

				woridaList = demographyRepository.viewAllDemographyExcludingOther(levelId, Integer.parseInt(s1[i]));

				for (DemographicEntity dObject : woridaList) {
					jobj = new JSONObject();

					jobj.put("woridaId", dObject.getDemographyId());
					jobj.put("woridaName", dObject.getDemographyName());
					jarr.put(jobj);
				}
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::findMulultipleWoredaByZoneId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public String findMulultipleKebeleByWoredaId(Integer levelId, String woredaId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;

		try {
			String[] s1 = woredaId.split(",");

			List<DemographicEntity> woridaList = null;
			for (int i = 0; i < s1.length; i++) {

				woridaList = demographyRepository.viewAllDemographyExcludingOther(levelId, Integer.parseInt(s1[i]));

				for (DemographicEntity dObject : woridaList) {
					jobj = new JSONObject();
					LOG.info("Woreda Id==" + dObject.getDemographyId());
					jobj.put("kebeleId", dObject.getDemographyId());
					jobj.put("kebeleName", dObject.getDemographyName());
					LOG.info("Woreda Name==" + dObject.getDemographyName());

					jarr.put(jobj);
				}
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::findMulultipleKebeleByWoredaId():" + e);

		}
		return jarr.toString();
	}

	@Override
	public List<DemographicBean> viewRegion(SearchVo searchVo) {
		List<DemographicBean> regionList = new ArrayList<>();
		DemographicBean vo = null;
		boolean condition = false;
		String regionName = searchVo.getRegionName();
		if (regionName == null) {
			regionName = "";
		}
		List<Object[]> obj = null;
		try {

			if (searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					//Either remove or fill this block of code.
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = demographyRepository.viewRegion(condition, regionName.toUpperCase());
			} else {
				obj = demographyRepository.viewRegion(regionName.toUpperCase());
			}
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new DemographicBean();
					vo.setCountryName(String.valueOf(dto[0]));
					vo.setCountryId((Short) dto[1]);
					vo.setRegionName(String.valueOf(dto[2]));
					vo.setRegionId((Short) dto[3]);
					vo.setStatus((Boolean) dto[4]);
					vo.setAlias(String.valueOf(dto[5]));
					vo.setDescription(String.valueOf(dto[6]));
					regionList.add(vo);
				}
				LOG.info("view List size of Region " + regionList.size());
			}
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewRegion():" + e);
		}
		return regionList;
	}

	@Override
	public List<DemographicEntity> viewRegionList() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(2);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::viewRegionList():" + e);
		}

		return obj;
	}

	@Override
	public List<DemographicEntity> kebeleList() {
		List<DemographicEntity> obj = null;
		try {
			obj = demographyRepository.findByLevelId(5);
		} catch (Exception e) {
			LOG.error("ManageDemographicServiceImpl::kebeleList():" + e);
		}
		return obj;
	}

}
