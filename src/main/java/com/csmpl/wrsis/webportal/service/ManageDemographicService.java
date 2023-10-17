package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.validation.Valid;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.DemographicBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;

public interface ManageDemographicService {

	List<DemographicEntity> viewCountry();

	String saveRegionName(DemographicBean region);

	List<DemographicBean> viewRegion(SearchVo searchVo);

	DemographicEntity getCountryByParentId(Integer parentId);

	DemographicEntity editRegion(int demographyId);

	String updateRegion(@Valid DemographicBean demoBean);

	List<DemographicEntity> regionList();

	List<DemographicEntity> kebeleList();

	String saveZoneName(DemographicBean zone);

	List<DemographicEntity> zoneList();

	String saveWoredaName(DemographicBean woreda);

	List<DemographicEntity> woredaList();

	String saveKebeleName(DemographicBean kebele);

	List<DemographicBean> viewZone(SearchVo searchVo);

	List<DemographicEntity> getRegionByParentId(int parentId);

	DemographicEntity editZone(int demographyId);

	String updateZone(@Valid DemographicBean demoBean);

	List<DemographicBean> viewWoreda(SearchVo searchVo);

	List<DemographicBean> viewKebele(SearchVo searchVo);

	String updateWoreda(@Valid DemographicBean woreda);

	DemographicEntity editWoreda(int demographyId);

	DemographicEntity editKebele(int demographyId);

	String updateKebele(@Valid DemographicBean kebele);

	List<DemographicEntity> getDemographyListByParentId(int parentId);

	List<DemographicEntity> viewAllRegionByStatus();

	String viewAllZoneByRegionIdAndStatus(Integer levelId, Integer parentId);

	String viewAllWoredaByZoneIdAndStatus(Integer levelId, Integer parentId);

	DemographicEntity searchAllWoredaByZoneIdAndStatus(Integer levelId, Integer parentId);

	String findMulultipleZoneByRegionId(Integer levelId, String regionId);

	String findMulultipleWoredaByZoneId(Integer levelId, String zoneId);

	String findMulultipleKebeleByWoredaId(Integer levelId, String woredaId);

	List<DemographicEntity> viewRegionList();

}
