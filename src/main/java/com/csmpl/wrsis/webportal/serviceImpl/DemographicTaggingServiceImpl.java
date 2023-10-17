package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.entity.AdmLevelDetails;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.repository.AdminLevelRepository;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DemographicLocationTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.DemographicLocationTagEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenterLocation;
import com.csmpl.wrsis.webportal.entity.UserLocationDetails;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographicTaggingRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterLocationRepository;
import com.csmpl.wrsis.webportal.repository.UserLocationDetailsRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.service.DemographicTaggingService;

@Service
public class DemographicTaggingServiceImpl implements DemographicTaggingService {

	private static final Logger LOG = LoggerFactory.getLogger(DemographicTaggingServiceImpl.class);

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserResearchCenterMappingRepository userResearchCenterMappingRepository;
	@Autowired
	ResearchCenterLocationRepository researchCenterLocationRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	DemographicTaggingRepository demographicTaggingRepository;
	@Autowired
	DemographicRepository demographyRepository;
	@Autowired
	AdminLevelRepository adminLevelRepository;
	@Autowired
	UserRoleRepository userRoleRepository;
	@Autowired
	UserLocationDetailsRepository userLocationDetailsRepository;

	@Override
	public String getUserNameByRoleIdAndOrganizationId(Integer roleId, Integer orgId) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Object[]> userList = userRepository.findUserNameByRoleIdAndOrganizationId(roleId, orgId);

			JSONObject jsonObject = null;
			if (userList != null && !userList.isEmpty()) {

				for (Object[] objects : userList) {

					// Here Check if the user already exist in the table of UserLocationTag table
					List<DemographicLocationTagEntity> tag = demographicTaggingRepository
							.findAllLocationTagDetailsByUserId(Integer.parseInt(objects[0].toString()));
					if (tag.isEmpty()) {
						jsonObject = new JSONObject();
						jsonObject.put("userId", objects[0]);
						jsonObject.put("userName", objects[1]);
						jsonObject.put("fullName", objects[2]);
						array.put(jsonObject);
					}
				}
			}
			jObject.put("userList", array);

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::getUserNameByRoleIdAndOrganizationId():" + e);

		}
		return jObject.toString();
	}

	@Override
	public String serchLocationDetailsByUserId(Integer userId) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			UserResearchCenterMapping centerMap = userResearchCenterMappingRepository.findByUserId(userId);
			if (centerMap != null) {
				List<ResearchCenterLocation> locationList = researchCenterLocationRepository
						.findByResearchCenterId(centerMap.getRecearchCenterId());

				JSONObject jsonObject = null;
				if (locationList != null && locationList.size() > 0) {

					for (ResearchCenterLocation location : locationList) {
						jsonObject = new JSONObject();
						DemographicEntity region = demographicRepository.getOne(location.getRegionId());
						if (region != null) {

							jsonObject.put("regionName", region.getDemographyName());
							jsonObject.put(WrsisPortalConstant.REGION_ID, region.getDemographyId());
						} else {
							jsonObject.put("regionName", "-NA-");
							jsonObject.put(WrsisPortalConstant.REGION_ID, "0");
						}
						DemographicEntity zone = demographicRepository.getOne(location.getZoneId());
						if (zone != null) {

							jsonObject.put("zoneName", zone.getDemographyName());
							jsonObject.put("zoneId", zone.getDemographyId());

						} else {
							jsonObject.put("zoneName", "-NA-");
							jsonObject.put("zoneId", "0");
						}
						DemographicEntity woreda = demographicRepository.getOne(location.getWoredaId());
						if (woreda != null) {
							jsonObject.put("woredaName", woreda.getDemographyName());
							jsonObject.put("woredaId", woreda.getDemographyId());

						} else {
							jsonObject.put("woredaName", "-NA-");
							jsonObject.put("woredaId", "0");
						}

						array.put(jsonObject);
					}
				}
			}
			jObject.put("locationList", array);

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::serchLocationDetailsByUserId():" + e);

		}
		return jObject.toString();
	}

	@Override
	public String viewDemographyLocationTagByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Object[]> page = demographicTaggingRepository
					.viewAllLocationTag(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (Object[] tagLocation : page) {
					jsonObject = new JSONObject();
					if (tagLocation != null) {

						jsonObject.put("id", tagLocation[0]);
						LOG.info("DemographicTaggingServiceImpl::Tag Id===" + tagLocation[0]);
						LOG.info("DemographicTaggingServiceImpl::Table User Id==" + tagLocation[1]);

						User u = userRepository.getOne(Integer.parseInt(tagLocation[1].toString()));
						jsonObject.put("name", u.getFullName() != null ? u.getFullName() : "-NA-");

						LOG.info("DemographicTaggingServiceImpl::Level Dtl ==" + u.getIntLevelDetailId());

						AdmLevelDetails levelDetails = adminLevelRepository
								.findLevelDetailsByUserId(u.getIntLevelDetailId());
						if (levelDetails != null) {
							LOG.info("DemographicTaggingServiceImpl::Level ID==" + levelDetails.getLevelDetailId());
							LOG.info("DemographicTaggingServiceImpl::Level Name==" + levelDetails.getLevelName());
							jsonObject.put(WrsisPortalConstant.ORG_NAME, levelDetails.getLevelName());
						} else {
							jsonObject.put(WrsisPortalConstant.ORG_NAME, "-NA-");
						}

						LOG.info("DemographicTaggingServiceImpl::User Id==" + u.getUserId());
						List<Object[]> roleList = userRoleRepository.findUserRoleNameByUserId(u.getUserId());
						for (Object[] rName : roleList) {
							jsonObject.put("roleName", rName[1] != null ? rName[1] : "-NA-");
						}

						ArrayList<String> region = new ArrayList<>();
						ArrayList<String> zone = new ArrayList<>();
						ArrayList<String> worida = new ArrayList<>();
						ArrayList<String> kebele = new ArrayList<>();

						String[] userLocationArr = tagLocation[4].toString().split(",");
						for (int k = 0; k < userLocationArr.length; k++) {

							UserLocationDetails locationuser = userLocationDetailsRepository
									.getOne(Integer.parseInt(userLocationArr[k]));
							if (!locationuser.isStatus() ) {
								if (Integer.parseInt(tagLocation[2].toString()) == 4) {
									DemographicEntity woredaId = demographyRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());

									worida.add(woredaId.getDemographyName());
									DemographicEntity zoneId = demographyRepository
											.findByDemographyId(woredaId.getParentId());
									zone.add(zoneId.getDemographyName());
									DemographicEntity regionId = demographyRepository
											.findByDemographyId(zoneId.getParentId());
									region.add(regionId.getDemographyName());
								}else if(Integer.parseInt(tagLocation[2].toString()) == 2) {
									DemographicEntity regionId = demographyRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());

									region.add(regionId.getDemographyName());
									
								}
								else {
									DemographicEntity kebeleId = demographyRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());

									kebele.add(kebeleId.getDemographyName());
									DemographicEntity woredaId = demographyRepository
											.findByDemographyId(kebeleId.getParentId());
									worida.add(woredaId.getDemographyName());
									DemographicEntity zoneId = demographyRepository
											.findByDemographyId(woredaId.getParentId());
									if (zoneId != null) {
										zone.add(zoneId.getDemographyName());
										DemographicEntity regionId = demographyRepository
												.findByDemographyId(zoneId.getParentId());
										if (regionId != null)
											region.add(regionId.getDemographyName());
									}
								}
							}
						}
						ArrayList<String> fworida = removeDuplicates(worida);
						if(fworida!=null && !fworida.isEmpty()) {
							jsonObject.put(WrsisPortalConstant.WOREDA1, fworida);
						}else {
							jsonObject.put(WrsisPortalConstant.WOREDA1, "-NA-");
						}
						if (kebele != null && !kebele.isEmpty()) {
							jsonObject.put(WrsisPortalConstant.KEBELE1, kebele);
						} else {
							jsonObject.put(WrsisPortalConstant.KEBELE1, "-NA-");
						}
						ArrayList<String> fZone = removeDuplicates(zone);
						if(fZone!=null && !fZone.isEmpty()) {
							jsonObject.put("zone", fZone);
						}else {
							jsonObject.put("zone", "-NA-");
						}
						
						ArrayList<String> fregion = removeDuplicates(region);
						jsonObject.put(WrsisPortalConstant.REGION1, fregion);
						if (!Boolean.parseBoolean(tagLocation[3].toString()) )
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("locationTagList", array);
			}

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::viewDemographyLocationTagByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public String saveAndUpdateTaggingLocationWithLevelId(DemographicLocationTagBean tag) {
		String res = "";
		try {
			if (tag.getLocationTagIdBean() == 0) {
				DemographicLocationTagEntity saveRes = null;
				DemographicLocationTagEntity tagLocation = new DemographicLocationTagEntity();

				tagLocation.setCreadtedBy(tag.getCreadtedByBean());
				tagLocation.setUserId(tag.getUserIdBean());
				tagLocation.setStatus(tag.isStatusBean());
				tagLocation.setCreatedOn(new Date());

				if (tag.getKebeleIdBean() != null) {

					DemographicEntity kebeleLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getKebeleIdBean()[0]));

					tagLocation.setLevelId(kebeleLevelId.getLevelId());

					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);

					UserLocationDetails userLocationDetails = null;
					if (saveRes != null) {

						for (int i = 0; i < tag.getKebeleIdBean().length; i++) {
							userLocationDetails = new UserLocationDetails();
							userLocationDetails.setLocationTagId(saveRes);

							DemographicEntity demographyId = demographyRepository
									.getOne(Integer.parseInt(tag.getKebeleIdBean()[i]));
							userLocationDetails.setDemographyId(demographyId);
							userLocationDetails.setStatus(tag.isStatusBean());
							userLocationDetailsRepository.saveAndFlush(userLocationDetails);
						}
					}
				} else if(tag.getWoredaIdBean() != null){

					// Save for Woreda level ID Data

					DemographicEntity woredaLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getWoredaIdBean()[0]));
					tagLocation.setLevelId(woredaLevelId.getLevelId());
					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);
					UserLocationDetails userLocationDetails = null;

					if (saveRes != null) {
						for (int i = 0; i < tag.getWoredaIdBean().length; i++) {
							userLocationDetails = new UserLocationDetails();
							userLocationDetails.setLocationTagId(saveRes);
							DemographicEntity demographyId = demographyRepository
									.getOne(Integer.parseInt(tag.getWoredaIdBean()[i]));
							userLocationDetails.setDemographyId(demographyId);
							userLocationDetails.setStatus(false);
							userLocationDetailsRepository.saveAndFlush(userLocationDetails);

						}
					}
				}
				else if(tag.getRegionIdBean() != null){
					DemographicEntity regionLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getRegionIdBean()[0]));
					tagLocation.setLevelId(regionLevelId.getLevelId());
					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);
					UserLocationDetails userLocationDetails = null;
					if (saveRes != null) {
						for (int i = 0; i < tag.getRegionIdBean().length; i++) {
							userLocationDetails = new UserLocationDetails();
							userLocationDetails.setLocationTagId(saveRes);
							DemographicEntity demographyId = demographyRepository
									.getOne(Integer.parseInt(tag.getRegionIdBean()[i]));
							userLocationDetails.setDemographyId(demographyId);
							userLocationDetails.setStatus(false);
							userLocationDetailsRepository.saveAndFlush(userLocationDetails);
						}
					}
				}
				if (saveRes != null)
					res = "Save";
				else
					res = "fail";

			} else {
				// Update
				DemographicLocationTagEntity saveRes = null;
				DemographicLocationTagEntity tagLocation = demographicTaggingRepository
						.getOne(tag.getLocationTagIdBean());

				if (tag.getUserIdBean().getUserId() == null) {
					tagLocation.setUserId(tagLocation.getUserId());
				} else {
					tagLocation.setUserId(tag.getUserIdBean());
				}
				tagLocation.setStatus(tagLocation.isStatus());
				tagLocation.setCreadtedBy(tagLocation.getCreadtedBy());
				tagLocation.setCreatedOn(tagLocation.getCreatedOn());
				tagLocation.setUpdatedBy(tag.getCreadtedByBean());
				tagLocation.setUpdatedOn(new Date());
				tagLocation.setLocationTagId(tagLocation.getLocationTagId());
				tagLocation.setStatus(tag.isStatusBean());
				if (tag.getKebeleIdBean() != null) {
					// Save for kebele level ID Data

					DemographicEntity kebeleLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getKebeleIdBean()[0]));

					tagLocation.setLevelId(kebeleLevelId.getLevelId());

					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);

					UserLocationDetails userLocationDetails = null;
					if (saveRes != null) {

						ArrayList<Integer> tableKebeleArray = new ArrayList<>();

						List<UserLocationDetails> locationuserList = userLocationDetailsRepository
								.findBylocationTagId(saveRes.getLocationTagId());
						for (UserLocationDetails userLoc : locationuserList) {
							DemographicEntity kebeleId = demographyRepository
									.getOne(userLoc.getDemographyId().getDemographyId());

							if (kebeleId.getLevelId() == 5) {
								tableKebeleArray.add(kebeleId.getDemographyId());
							}
						}
						ArrayList<Integer> pageKebeleArr = new ArrayList<>();
						ArrayList<Integer> pageKebeleArr_2 = new ArrayList<>();

						for (int i = 0; i < tag.getKebeleIdBean().length; i++) {
							pageKebeleArr.add(Integer.parseInt(tag.getKebeleIdBean()[i]));
							pageKebeleArr_2.add(Integer.parseInt(tag.getKebeleIdBean()[i]));
						}

						pageKebeleArr.removeAll(tableKebeleArray);

						if (!(pageKebeleArr.isEmpty())) {
							// In This Section user want to add new Kebele then it will be save
							for (Integer kebeleid : pageKebeleArr) {
								// Here IF already Woreda Is Present then first delete it
								List<UserLocationDetails> userLocList = userLocationDetailsRepository
										.findBylocationTagId(saveRes.getLocationTagId());
								for (UserLocationDetails userLoc : userLocList) {
									DemographicEntity woriId = demographyRepository
											.getOne(userLoc.getDemographyId().getDemographyId());

									if (woriId.getLevelId() == 4) {


										
										UserLocationDetails delWoreda = userLocationDetailsRepository
												.getOne(userLoc.getUserLocationId());
										delWoreda.setStatus(true);
										userLocationDetailsRepository.saveAndFlush(delWoreda);

									}
								}

								// Here after delete of woreda it will save new kebele
								userLocationDetails = new UserLocationDetails();
								userLocationDetails.setLocationTagId(saveRes);

								DemographicEntity demographyId = demographyRepository.getOne(kebeleid);
								userLocationDetails.setDemographyId(demographyId);
								userLocationDetails.setStatus(false);
								userLocationDetailsRepository.saveAndFlush(userLocationDetails);

							}
						} else {
							// In This Section user want to remove Kebele then it will be save
							tableKebeleArray.removeAll(pageKebeleArr_2);
							for (Integer kebelId : tableKebeleArray) {

								UserLocationDetails ulo = userLocationDetailsRepository
										.findBylocationTagIdAndDemograpgyId(saveRes.getLocationTagId(), kebelId);
								ulo.setStatus(true);
								userLocationDetailsRepository.saveAndFlush(ulo);
							}

						}

					}
				}
				
				else if(tag.getWoredaIdBean() != null){
					DemographicEntity woredaLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getWoredaIdBean()[0]));

					tagLocation.setLevelId(woredaLevelId.getLevelId());

					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);
					UserLocationDetails ulDetails;
					if (saveRes != null) {
						ArrayList<Integer> tableWoredaArray = new ArrayList<>();

						List<UserLocationDetails> locationuserList = userLocationDetailsRepository
								.findBylocationTagId(saveRes.getLocationTagId());
						for (UserLocationDetails userLoc : locationuserList) {
							DemographicEntity woredaId = demographyRepository
									.getOne(userLoc.getDemographyId().getDemographyId());
							if (woredaId.getLevelId() == 4) {
								tableWoredaArray.add(woredaId.getDemographyId());
							}

						}
						ArrayList<Integer> pageWoredaArr = new ArrayList<>();
						ArrayList<Integer> pageWoredaArr_2 = new ArrayList<>();

						for (int i = 0; i < tag.getWoredaIdBean().length; i++) {
							pageWoredaArr.add(Integer.parseInt(tag.getWoredaIdBean()[i]));
							pageWoredaArr_2.add(Integer.parseInt(tag.getWoredaIdBean()[i]));
						}
						pageWoredaArr.removeAll(tableWoredaArray);
						if (!(pageWoredaArr.isEmpty())) {
							// In This Section user want to add new Woreda then it will be save
							for (Integer woredaId : pageWoredaArr) {
								// Here If any Kebele present then it will remove first
								List<UserLocationDetails> userLocList = userLocationDetailsRepository
										.findBylocationTagId(saveRes.getLocationTagId());
								for (UserLocationDetails userLoc : userLocList) {
									DemographicEntity kebeId = demographyRepository
											.getOne(userLoc.getDemographyId().getDemographyId());

									if (kebeId.getLevelId() == 5) {


										
										UserLocationDetails delKebele = userLocationDetailsRepository
												.getOne(userLoc.getUserLocationId());
										delKebele.setStatus(true);
										userLocationDetailsRepository.saveAndFlush(delKebele);

									}
								}
								// Here new Woreda Will be Save
								ulDetails = new UserLocationDetails();
								ulDetails.setLocationTagId(saveRes);

								DemographicEntity demographyId = demographyRepository.getOne(woredaId);
								ulDetails.setDemographyId(demographyId);
								ulDetails.setStatus(false);
								userLocationDetailsRepository.saveAndFlush(ulDetails);

							}
						} else {
							// In This Section user want to remove Woreda then it will be save
							tableWoredaArray.removeAll(pageWoredaArr_2);
							for (Integer woredaId : tableWoredaArray) {

								UserLocationDetails ulo = userLocationDetailsRepository
										.findBylocationTagIdAndDemograpgyId(saveRes.getLocationTagId(), woredaId);
								ulo.setStatus(true);
								userLocationDetailsRepository.saveAndFlush(ulo);

							}

						}

					}

				}
				else if(tag.getRegionIdBean() != null){
					DemographicEntity regionLevelId = demographyRepository
							.getOne(Integer.parseInt(tag.getRegionIdBean()[0]));

					tagLocation.setLevelId(regionLevelId.getLevelId());

					saveRes = demographicTaggingRepository.saveAndFlush(tagLocation);
					UserLocationDetails ulD;
					if (saveRes != null) {
						ArrayList<Integer> tableRegionArray = new ArrayList<>();
						List<UserLocationDetails> locationuserList = userLocationDetailsRepository
								.findBylocationTagId(saveRes.getLocationTagId());
						for (UserLocationDetails userLoc : locationuserList) {// Here region present in table is stored in the array 
							DemographicEntity regionId = demographyRepository
									.getOne(userLoc.getDemographyId().getDemographyId());
							if (regionId.getLevelId() == 2) {
								tableRegionArray.add(regionId.getDemographyId());
							}
						}
						// Here If any region present then it will remove first
						for (Integer regionId : tableRegionArray) {

							UserLocationDetails ulo = userLocationDetailsRepository
									.findBylocationTagIdAndDemograpgyId(saveRes.getLocationTagId(), regionId);
							ulo.setStatus(true);
							userLocationDetailsRepository.saveAndFlush(ulo);
						}
						ArrayList<Integer> pageRegionArr = new ArrayList<>();

						for (int i = 0; i < tag.getRegionIdBean().length; i++) {
							pageRegionArr.add(Integer.parseInt(tag.getRegionIdBean()[i]));
						}
						pageRegionArr.removeAll(tableRegionArray);
						if (!(pageRegionArr.isEmpty())) {
							// In This Section user want to add new Region then it will be save
							for (Integer regionId : pageRegionArr) {
								// Here If any region already present then it set status false otherwise new region added
								Object[] userLocObj = userLocationDetailsRepository
										.findByDemographyIdAndlocationTagId(regionId,saveRes.getLocationTagId());
								UserLocationDetails userLoc;
								if(userLocObj.length>0)
								{
									userLoc=userLocationDetailsRepository.findByDemographyIdAndLocationTagId(regionId, saveRes.getLocationTagId());
									if(userLoc.isStatus())
									{
										userLoc.setStatus(false);
									}	
								}else {
									userLoc=new UserLocationDetails();
									userLoc.setStatus(false);
								}
								userLoc.setLocationTagId(saveRes);
								DemographicEntity demographyId = demographyRepository.getOne(regionId);
								userLoc.setDemographyId(demographyId);
								userLocationDetailsRepository.saveAndFlush(userLoc);
							}
						}
					}
				}
				if (saveRes != null)
					res = "update";
				else
					res = "fail";
			}
		} catch (Exception e) {
			LOG.error("DemographicTaggingServiceImpl::saveAndUpdateTaggingLocationWithLevelId():" + e);
		}
		return res;
	}

	// remove duplicate elements from ArrayList
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
		Set<T> set = new LinkedHashSet<>();
		set.addAll(list);
		list.clear();
		list.addAll(set);
		return list;
	}

	@Override
	public DemographicLocationTagBean getTaggingDetailsById(Integer locationTagId) {
		DemographicLocationTagBean bean = new DemographicLocationTagBean();
		try {

			DemographicLocationTagEntity tagLocation = demographicTaggingRepository.getOne(locationTagId);
			bean.setLocationTagIdBean(tagLocation.getLocationTagId());
			bean.setUserIdBean(tagLocation.getUserId());
			bean.setStatusBean(tagLocation.isStatus());

			
			List<UserLocationDetails> userLoc = userLocationDetailsRepository
					.findBylocationTagId(tagLocation.getLocationTagId());

			Map<String, String> woridaTotalMapList = new HashMap<>();
			Map<String, String> woridaMapList = new HashMap<>();
			Map<String, String> kebeleTotalMapList = new HashMap<>();
			Map<String, String> kebeleMapList = new HashMap<>();

			ArrayList<String> demographyWoredaDataArray = new ArrayList<>();
			ArrayList<String> userLocationWoredaDataArray = new ArrayList<>();

			ArrayList<String> demographyKebeleDataArray = new ArrayList<>();
			ArrayList<String> userLocationKebeleDataArray = new ArrayList<>();

			for (UserLocationDetails userLocationDetails : userLoc) {

				userLocationWoredaDataArray
						.add(String.valueOf(userLocationDetails.getDemographyId().getDemographyId()));
				// This Is user Search Level 4 / For Woreda
				if (tagLocation.getLevelId() == 4) {

					List<DemographicEntity> woredaList = demographyRepository
							.findByParentId(userLocationDetails.getDemographyId().getParentId());
					for (DemographicEntity woreda : woredaList) {

						demographyWoredaDataArray.add(String.valueOf(woreda.getDemographyId()));

						// here its display kebeles corresponding woreda
						List<DemographicEntity> kebeleList = demographyRepository
								.findByParentId(woreda.getDemographyId());
						for (DemographicEntity kebel : kebeleList) {
							if (kebel.getLevelId() == 5)
								kebeleTotalMapList.put(String.valueOf(kebel.getDemographyId()),
										kebel.getDemographyName());
						}
					}
					// Here it shows all woreda from the location table
					woridaMapList.put(String.valueOf(userLocationDetails.getDemographyId().getDemographyId()),
							userLocationDetails.getDemographyId().getDemographyName());

				} else {
					// This Is user Search Level 5 / For Kebele

					DemographicEntity kebeleId = demographyRepository
							.getOne(userLocationDetails.getDemographyId().getDemographyId());
					if (kebeleId.getLevelId() == 5) {

						List<DemographicEntity> kebeleList = demographyRepository
								.findByParentId(kebeleId.getParentId());
						for (DemographicEntity kebele : kebeleList) {

							demographyKebeleDataArray.add(String.valueOf(kebele.getDemographyId()));

						}
						kebeleMapList.put(String.valueOf(kebeleId.getDemographyId()), kebeleId.getDemographyName());

						userLocationKebeleDataArray.add(String.valueOf(kebeleId.getDemographyId()));


						DemographicEntity woredaId = demographyRepository.getOne(kebeleId.getParentId());

						woridaMapList.put(String.valueOf(woredaId.getDemographyId()),
								String.valueOf(woredaId.getDemographyName()));
					}
				}
			}

			demographyWoredaDataArray.removeAll(userLocationWoredaDataArray);

			// Here Only Uncommon Woreda Will Displayed in Left Side
			for (String Woreda : demographyWoredaDataArray) {
				DemographicEntity woredaId = demographyRepository.getOne(Integer.parseInt(Woreda));
				woridaTotalMapList.put(String.valueOf(woredaId.getDemographyId()), woredaId.getDemographyName());
			}

			demographyKebeleDataArray.removeAll(userLocationKebeleDataArray);

			// Here Only Uncommon Kebele Will Displayed in Left Side
			for (String kebele : demographyKebeleDataArray) {
				DemographicEntity kebeleId = demographyRepository.getOne(Integer.parseInt(kebele));
				kebeleTotalMapList.put(String.valueOf(kebeleId.getDemographyId()), kebeleId.getDemographyName());

			}

			bean.setTotalWoridaBeanID(woridaTotalMapList);
			bean.setCheckWoridaBeanID(woridaMapList);
			bean.setTotalKebeleBeanID(kebeleTotalMapList);
			bean.setCheckKebeleBeanID(kebeleMapList);

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::getTaggingDetailsById():" + e);

		}
		return bean;
	}

	@Override
	public String searchDemographyLocationTagByPage(Integer pageSize, Integer pageNumber, Pageable pageable,
			Integer userId, String status) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Object[]> page = null;
			if (userId != 0) {
				page = demographicTaggingRepository.searchAllLocationTagByOrganizationName(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), userId);
			}
			if (!status.equals("0")) {
				page = demographicTaggingRepository.searchAllLocationTagByStatus(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()),
						Boolean.parseBoolean(status));
			}
			if (userId != 0 && !status.equals("0")) {
				page = demographicTaggingRepository.searchAllLocationTagByOrganizationNameAndStatus(
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), userId,
						Boolean.parseBoolean(status));
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
				for (Object[] tagLocation : page) {
					jsonObject = new JSONObject();
					if (tagLocation != null) {

						jsonObject.put("id", tagLocation[0]);
						User u = userRepository.getOne(Integer.parseInt(tagLocation[1].toString()));
						jsonObject.put("name", u.getFullName() != null ? u.getFullName() : "-NA-");

						AdmLevelDetails levelDetails = adminLevelRepository
								.findLevelDetailsByUserId(u.getIntLevelDetailId());
						if (levelDetails != null) {
							jsonObject.put(WrsisPortalConstant.ORG_NAME, levelDetails.getLevelName());
						} else {
							jsonObject.put(WrsisPortalConstant.ORG_NAME, "-NA-");
						}

						List<Object[]> roleList = userRoleRepository.findUserRoleNameByUserId(u.getUserId());
						for (Object[] rName : roleList) {
							jsonObject.put("roleName", rName[1] != null ? rName[1] : "-NA-");
						}

						ArrayList<String> region = new ArrayList<>();
						ArrayList<String> zone = new ArrayList<>();
						ArrayList<String> worida = new ArrayList<>();
						ArrayList<String> kebele = new ArrayList<>();

						String[] userLocationArr = tagLocation[4].toString().split(",");
						for (int k = 0; k < userLocationArr.length; k++) {

							UserLocationDetails locationuser = userLocationDetailsRepository
									.getOne(Integer.parseInt(userLocationArr[k]));
							if (locationuser.isStatus() ) {
								if (Integer.parseInt(tagLocation[2].toString()) == 4) {
									DemographicEntity woredaId = demographyRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());

									worida.add(woredaId.getDemographyName());
									DemographicEntity zoneId = demographyRepository
											.findByDemographyId(woredaId.getParentId());
									zone.add(zoneId.getDemographyName());
									DemographicEntity regionId = demographyRepository
											.findByDemographyId(zoneId.getParentId());
									region.add(regionId.getDemographyName());
								} else {
									DemographicEntity kebeleId = demographyRepository
											.findByDemographyId(locationuser.getDemographyId().getDemographyId());

									kebele.add(kebeleId.getDemographyName());
									DemographicEntity woredaId = demographyRepository
											.findByDemographyId(kebeleId.getParentId());
									worida.add(woredaId.getDemographyName());
									DemographicEntity zoneId = demographyRepository
											.findByDemographyId(woredaId.getParentId());
									zone.add(zoneId.getDemographyName());
									DemographicEntity regionId = demographyRepository
											.findByDemographyId(zoneId.getParentId());
									region.add(regionId.getDemographyName());
								}
							}
						}
						ArrayList<String> fworida = removeDuplicates(worida);
						jsonObject.put(WrsisPortalConstant.WOREDA1, fworida);
						if (kebele != null && ! kebele.isEmpty()) {
							jsonObject.put(WrsisPortalConstant.KEBELE1, kebele);
						} else {
							jsonObject.put(WrsisPortalConstant.KEBELE1, "-NA-");
						}
						ArrayList<String> fZone = removeDuplicates(zone);

						jsonObject.put("zone", fZone);
						ArrayList<String> fregion = removeDuplicates(region);
						jsonObject.put(WrsisPortalConstant.REGION1, fregion);
						if (!Boolean.parseBoolean(tagLocation[3].toString()))
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("locationTagList", array);
			}

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::searchDemographyLocationTagByPage():" + e);

		}
		return jObject.toString();

	}

	@Override
	public String getUserNameByRoleIdAndOrganizationIdSearch(Integer roleId, Integer orgId) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Object[]> userList = userRepository.findUserNameByRoleIdAndOrganizationId(roleId, orgId);

			JSONObject jsonObject = null;
			if (userList != null && !userList.isEmpty()) {

				for (Object[] objects : userList) {
					LOG.info("DemographicTaggingServiceImpl::user Id==" + objects[0] + "\t" + objects[1]);
					// Here Check if the user already exist in the table of UserLocationTag table
					
					jsonObject = new JSONObject();
					jsonObject.put("userId", objects[0]);
					jsonObject.put("userName", objects[1]);
					jsonObject.put("fullName", objects[2]);
					array.put(jsonObject);
					
				}
			}
			jObject.put("userList", array);

		} catch (Exception e) {

			LOG.error("DemographicTaggingServiceImpl::getUserNameByRoleIdAndOrganizationIdSearch():" + e);

		}
		return jObject.toString();
	}
}
