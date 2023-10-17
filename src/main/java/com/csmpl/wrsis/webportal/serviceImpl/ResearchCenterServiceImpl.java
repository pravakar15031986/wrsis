package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.ResearchCenterHelperBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.ResearchCenterLocation;
import com.csmpl.wrsis.webportal.entity.ResearchCenterSpelization;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterLocationRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterSpelizationRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;

@Service
public class ResearchCenterServiceImpl implements ResearchCenterService {

	private static final Logger LOG = LoggerFactory.getLogger(ResearchCenterServiceImpl.class);

	@Autowired
	ResearchCenterRepository researchCenterRepository;
	@Autowired
	ResearchCenterLocationRepository researchCenterLocationRepository;
	@Autowired
	ResearchCenterSpelizationRepository researchCenterSpelizationRepository;
	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@Autowired
	UserResearchCenterMappingRepository userResearchCenterMappingRepository;

	@Override
	public String saveAndUpdate(ResearchCenterHelperBean centerBean) {
		String res = "";
		try {
			if (centerBean.getResearchCenterBeanId() == 0) {
				ResearchCenter center = new ResearchCenter();
				center.setResearchCenterName(centerBean.getResearchCenterBeanName());
				center.setStatus(centerBean.isStatusBean());
				center.setDesc(centerBean.getDescriptionBean());
				center.setLabStatus(centerBean.isLabStatusBean());
				center.setCreatedBy(centerBean.getCreatedByBean());
				center.setCreatedOn(new Date());
				int researchCenterId = getmaxSeasonId();
				center.setResearchCenterId(researchCenterId);
				ResearchCenter rCenter = researchCenterRepository.save(center);
				ResearchCenterLocation location = null;
				// insert in Location Table
				for (int i = 0; i < centerBean.getWoredaBeanId().length; i++) {
					location = new ResearchCenterLocation();
					LOG.info("Worida Id==>" + centerBean.getWoredaBeanId()[i]);

					location.setResearchCenterId(center);
					DemographicEntity zone = demographicRepository
							.getWoredaDetialsByDemographyId(Integer.parseInt(centerBean.getWoredaBeanId()[i]));

					DemographicEntity region = demographicRepository.getWoredaDetialsByDemographyId(zone.getParentId());
					location.setRegionId(region.getParentId());
					location.setZoneId(zone.getParentId());
					location.setWoredaId(Integer.parseInt(centerBean.getWoredaBeanId()[i]));
					location.setStatus(false);
					location.setCreatedBy(centerBean.getCreatedByBean());
					location.setCreatedOn(new Date());
					researchCenterLocationRepository.save(location);
				}
				// Insert for Specialization Table
				if (centerBean.isLabStatusBean())
					if (centerBean.getRustBeanId() != null) {
						ResearchCenterSpelization rCSpelization = null;
						TypeOfRust rust = null;

						for (int i = 0; i < centerBean.getRustBeanId().length; i++) {
							rCSpelization = new ResearchCenterSpelization();

							rCSpelization.setResearchCenterId(center);
							rust = new TypeOfRust();
							rust.setRustId(Integer.parseInt(centerBean.getRustBeanId()[i]));
							rCSpelization.setSpelizationId(rust);
							rCSpelization.setStatus(false);
							rCSpelization.setCreaatedBy(centerBean.getCreatedByBean());
							rCSpelization.setCreatedOn(new Date());
							researchCenterSpelizationRepository.save(rCSpelization);
						}
					}

				if (rCenter != null)
					res = WrsisPortalConstant.SAVE;
				else
					res = WrsisPortalConstant.FAILURE;
			} else {
				// In Edit Case

				List<UserResearchCenterMapping> list = userResearchCenterMappingRepository
						.findRecearchCenterIdByRecearchCenterId(centerBean.getResearchCenterBeanId());
				if (!centerBean.isStatusBean()) {
					ResearchCenter rCenter = null;
					ResearchCenter center = new ResearchCenter();
					ResearchCenter rc = researchCenterRepository.getOne(centerBean.getResearchCenterBeanId());
					if (rc != null) {

						center.setResearchCenterName(centerBean.getResearchCenterBeanName());

						center.setStatus(centerBean.isStatusBean());

						center.setDesc(centerBean.getDescriptionBean());

						center.setLabStatus(centerBean.isLabStatusBean());

						center.setCreatedBy(rc.getCreatedBy());

						center.setCreatedOn(rc.getCreatedOn());
						center.setUpdatedBy(centerBean.getCreatedByBean());
						center.setResearchCenterId(rc.getResearchCenterId());
						center.setUpdatedOn(new Date());

						rCenter = researchCenterRepository.save(center);

					}
					// this is use for Research Center Location data Save

					ArrayList<String> pageWoridaList = new ArrayList<>();
					ArrayList<String> pageWoridaList_2 = new ArrayList<>();

					
					for (int i = 0; i < centerBean.getWoredaBeanId().length; i++) {
						pageWoridaList.add(centerBean.getWoredaBeanId()[i]);
						pageWoridaList_2.add(centerBean.getWoredaBeanId()[i]);
					}

					List<ResearchCenterLocation> rCLocation = researchCenterLocationRepository
							.viewAllRCLocationByresearchCenterIdAndStatus(rc.getResearchCenterId());
					ArrayList<String> rCLocationTableList = new ArrayList<>();
					for (ResearchCenterLocation Clocatiion : rCLocation) {

						rCLocationTableList.add(String.valueOf(Clocatiion.getWoredaId()));
					}

					pageWoridaList.removeAll(rCLocationTableList);

					ResearchCenterLocation location = null;
					if (!pageWoridaList.isEmpty()) {
						// In this section New Woreda save if the request is coming form page
						for (String woreda : pageWoridaList) {
							LOG.info("Location New Add");
							location = new ResearchCenterLocation();
							location.setResearchCenterId(center);
							DemographicEntity zone = demographicRepository
									.getWoredaDetialsByDemographyId(Integer.parseInt(woreda));
							DemographicEntity region = demographicRepository
									.getWoredaDetialsByDemographyId(zone.getParentId());
							location.setRegionId(region.getParentId());
							location.setZoneId(zone.getParentId());
							location.setWoredaId(Integer.parseInt(woreda));
							location.setStatus(false);
							location.setCreatedBy(centerBean.getCreatedByBean());
							location.setCreatedOn(new Date());
							researchCenterLocationRepository.save(location);
						}

					} else {

						// In this section woreda will be deleted if request coming from the page
						rCLocationTableList.removeAll(pageWoridaList_2);
						for (String worida_id : rCLocationTableList) {

					
							ResearchCenterLocation rCentLocation = researchCenterLocationRepository
									.getRCLocationByResearchCenterIdAndWoredaId(rc.getResearchCenterId(),
											Integer.parseInt(worida_id));
							if (rCentLocation != null) {
								LOG.info("Location Updated");
								location = new ResearchCenterLocation();
								location.setResearchCenterId(rCentLocation.getResearchCenterId());

								location.setRegionId(rCentLocation.getRegionId());
								location.setZoneId(rCentLocation.getZoneId());
								location.setWoredaId(rCentLocation.getWoredaId());

								location.setStatus(true);
								location.setCreatedBy(rCentLocation.getCreatedBy());
								location.setCreatedOn(rCentLocation.getCreatedOn());

								location.setUpdatedBy(centerBean.getCreatedByBean());
								location.setUpdatedOn(new Date());
								location.setResearCenterLocationId(rCentLocation.getResearCenterLocationId());
								researchCenterLocationRepository.save(location);
							}
						}
					}

					// Insert Specialization Table
					if (!centerBean.isLabStatusBean()) {
						List<ResearchCenterSpelization> specialization = researchCenterSpelizationRepository
								.viewAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId());
						for (ResearchCenterSpelization rustSpecial : specialization) {
							rustSpecial.setStatus(true);
							researchCenterSpelizationRepository.saveAndFlush(rustSpecial);
						}

					} else {
						if (centerBean.getRustBeanId() != null) {
							ResearchCenterSpelization rCSpelization = null;
							TypeOfRust rust = null;
							ArrayList<String> pageRuastList = new ArrayList<>();
							ArrayList<String> pageRustList_2 = new ArrayList<>();

							for (int i = 0; i < centerBean.getRustBeanId().length; i++) {

								pageRuastList.add(centerBean.getRustBeanId()[i]);
								pageRustList_2.add(centerBean.getRustBeanId()[i]);
							}
							LOG.info("pageRuastList===" + pageRuastList);

							List<ResearchCenterSpelization> specialization = researchCenterSpelizationRepository
									.viewAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId());

							ArrayList<String> specializationTableList = new ArrayList<>();

							for (ResearchCenterSpelization rustSpecial : specialization) {
								
								specializationTableList.add(String.valueOf(rustSpecial.getSpelizationId().getRustId()));
							}

							pageRuastList.removeAll(specializationTableList);

							// In this section If new Specialization request coming from the page then it
							// will add it
							if (!pageRuastList.isEmpty()) {
								for (String string : pageRuastList) {

									rCSpelization = new ResearchCenterSpelization();
									rCSpelization.setResearchCenterId(center);
									rust = new TypeOfRust();
									rust.setRustId(Integer.parseInt(string));
									rCSpelization.setSpelizationId(rust);
									rCSpelization.setStatus(false);
									rCSpelization.setCreaatedBy(centerBean.getCreatedByBean());
									rCSpelization.setCreatedOn(new Date());

									researchCenterSpelizationRepository.save(rCSpelization);
								}
							} else {

								// In this stage If request coming for unchecked the specialization then it
								// delete the that Specialization
								specializationTableList.removeAll(pageRustList_2);
								LOG.info("After remove Table Rust==" + specializationTableList);
								for (String rust_id : specializationTableList) {
									ResearchCenterSpelization specializationRust = researchCenterSpelizationRepository
											.getAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId(),
													Integer.parseInt(rust_id));
									if (specializationRust != null) {
										rCSpelization = new ResearchCenterSpelization();
										rCSpelization.setResearchCenterId(center);
										
										rCSpelization.setSpelizationId(specializationRust.getSpelizationId());
										rCSpelization.setStatus(true);
										rCSpelization.setCreaatedBy(specializationRust.getCreaatedBy());
										rCSpelization.setCreatedOn(specializationRust.getCreatedOn());
										rCSpelization.setUpdatedOn(new Date());
										rCSpelization.setUpdatedBy(centerBean.getCreatedByBean());
										rCSpelization.setCenterSpelizeId(specializationRust.getCenterSpelizeId());
										researchCenterSpelizationRepository.save(rCSpelization);
									}
								}

							}
						}

					}

					if (rCenter != null)
						res = WrsisPortalConstant.UPDATE;
					else
						res = WrsisPortalConstant.FAILURE;

				} else {
					if (list.isEmpty()) {
						ResearchCenter rCenter = null;
						ResearchCenter center = new ResearchCenter();
						ResearchCenter rc = researchCenterRepository.getOne(centerBean.getResearchCenterBeanId());
						if (rc != null) {

							center.setResearchCenterName(centerBean.getResearchCenterBeanName());

							center.setStatus(centerBean.isStatusBean());

							center.setDesc(centerBean.getDescriptionBean());

							center.setLabStatus(centerBean.isLabStatusBean());

							center.setCreatedBy(rc.getCreatedBy());

							center.setCreatedOn(rc.getCreatedOn());
							center.setUpdatedBy(centerBean.getCreatedByBean());
							center.setResearchCenterId(rc.getResearchCenterId());
							center.setUpdatedOn(new Date());

							rCenter = researchCenterRepository.save(center);

						}
						// this is use for Research Center Location data Save

						ArrayList<String> pageWoridaList = new ArrayList<>();
						ArrayList<String> pageWoridaList_2 = new ArrayList<>();

						
						for (int i = 0; i < centerBean.getWoredaBeanId().length; i++) {
							pageWoridaList.add(centerBean.getWoredaBeanId()[i]);
							pageWoridaList_2.add(centerBean.getWoredaBeanId()[i]);
						}

						List<ResearchCenterLocation> rCLocation = researchCenterLocationRepository
								.viewAllRCLocationByresearchCenterIdAndStatus(rc.getResearchCenterId());
						ArrayList<String> rCLocationTableList = new ArrayList<>();
						for (ResearchCenterLocation Clocatiion : rCLocation) {

							rCLocationTableList.add(String.valueOf(Clocatiion.getWoredaId()));
						}

						pageWoridaList.removeAll(rCLocationTableList);

						ResearchCenterLocation location = null;
						if (!pageWoridaList.isEmpty()) {
							// In this section New Woreda save if the request is coming form page
							for (String woreda : pageWoridaList) {
								location = new ResearchCenterLocation();
								location.setResearchCenterId(center);
								DemographicEntity zone = demographicRepository
										.getWoredaDetialsByDemographyId(Integer.parseInt(woreda));
								DemographicEntity region = demographicRepository
										.getWoredaDetialsByDemographyId(zone.getParentId());
								location.setRegionId(region.getParentId());
								location.setZoneId(zone.getParentId());
								location.setWoredaId(Integer.parseInt(woreda));
								location.setStatus(false);
								location.setCreatedBy(centerBean.getCreatedByBean());
								location.setCreatedOn(new Date());
								researchCenterLocationRepository.save(location);
							}

						} else {

							// In this section woreda will be deleted if request coming from the page
							rCLocationTableList.removeAll(pageWoridaList_2);
							for (String worida_id : rCLocationTableList) {

								
								ResearchCenterLocation rCentLocation = researchCenterLocationRepository
										.getRCLocationByResearchCenterIdAndWoredaId(rc.getResearchCenterId(),
												Integer.parseInt(worida_id));
								if (rCentLocation != null) {
									location = new ResearchCenterLocation();
									location.setResearchCenterId(rCentLocation.getResearchCenterId());

									location.setRegionId(rCentLocation.getRegionId());
									location.setZoneId(rCentLocation.getZoneId());
									location.setWoredaId(rCentLocation.getWoredaId());

									location.setStatus(true);
									location.setCreatedBy(rCentLocation.getCreatedBy());
									location.setCreatedOn(rCentLocation.getCreatedOn());

									location.setUpdatedBy(centerBean.getCreatedByBean());
									location.setUpdatedOn(new Date());
									location.setResearCenterLocationId(rCentLocation.getResearCenterLocationId());
									researchCenterLocationRepository.save(location);
								}
							}
						}

						// Insert Specialization Table
						if (!centerBean.isLabStatusBean()) {
							List<ResearchCenterSpelization> specialization = researchCenterSpelizationRepository
									.viewAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId());
							for (ResearchCenterSpelization rustSpecial : specialization) {
								rustSpecial.setStatus(true);
								researchCenterSpelizationRepository.saveAndFlush(rustSpecial);
							}

						} else {
							if (centerBean.getRustBeanId() != null) {
								ResearchCenterSpelization rCSpelization = null;
								TypeOfRust rust = null;
								ArrayList<String> pageRuastList = new ArrayList<>();
								ArrayList<String> pageRustList_2 = new ArrayList<>();

								for (int i = 0; i < centerBean.getRustBeanId().length; i++) {

									pageRuastList.add(centerBean.getRustBeanId()[i]);
									pageRustList_2.add(centerBean.getRustBeanId()[i]);
								}
								LOG.info("pageRuastList===" + pageRuastList);

								List<ResearchCenterSpelization> specialization = researchCenterSpelizationRepository
										.viewAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId());

								ArrayList<String> specializationTableList = new ArrayList<>();

								for (ResearchCenterSpelization rustSpecial : specialization) {
									
									specializationTableList
											.add(String.valueOf(rustSpecial.getSpelizationId().getRustId()));
								}

								pageRuastList.removeAll(specializationTableList);

								// In this section If new Specialization request coming from the page then it
								// will add it
								if (!pageRuastList.isEmpty()) {
									for (String string : pageRuastList) {

										rCSpelization = new ResearchCenterSpelization();
										rCSpelization.setResearchCenterId(center);
										rust = new TypeOfRust();
										rust.setRustId(Integer.parseInt(string));
										rCSpelization.setSpelizationId(rust);
										rCSpelization.setStatus(false);
										rCSpelization.setCreaatedBy(centerBean.getCreatedByBean());
										rCSpelization.setCreatedOn(new Date());

										researchCenterSpelizationRepository.save(rCSpelization);
									}
								} else {

									// In this stage If request coming for unchecked the specialization then it
									// delete the that Specialization
									specializationTableList.removeAll(pageRustList_2);
									LOG.info("After remove Table Rust==" + specializationTableList);
									for (String rust_id : specializationTableList) {
										ResearchCenterSpelization specializationRust = researchCenterSpelizationRepository
												.getAllRCSpelizationByresearchCenterIdAndStatus(
														rc.getResearchCenterId(), Integer.parseInt(rust_id));
										if (specializationRust != null) {
											rCSpelization = new ResearchCenterSpelization();
											rCSpelization.setResearchCenterId(center);
											
											rCSpelization.setSpelizationId(specializationRust.getSpelizationId());
											rCSpelization.setStatus(true);
											rCSpelization.setCreaatedBy(specializationRust.getCreaatedBy());
											rCSpelization.setCreatedOn(specializationRust.getCreatedOn());
											rCSpelization.setUpdatedOn(new Date());
											rCSpelization.setUpdatedBy(centerBean.getCreatedByBean());
											rCSpelization.setCenterSpelizeId(specializationRust.getCenterSpelizeId());
											researchCenterSpelizationRepository.save(rCSpelization);
										}
									}

								}
							}

						}

						if (rCenter != null)
							res = WrsisPortalConstant.UPDATE;
						else
							res = WrsisPortalConstant.FAILURE;

					} else {
						return WrsisPortalConstant.DEPENDENT;
					}
				}

				//
			}

		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::saveandUpdate():" + e);
		}
		return res;
	}

	public int getmaxSeasonId() {
		Integer researchCenterId = null;
		try {

			researchCenterId = researchCenterRepository.findMaxResearchCenterId();
			if (researchCenterId == null) {
				researchCenterId = 1;
			} else {
				researchCenterId = researchCenterId + 1;
			}
			LOG.info("in Max ID==" + researchCenterId);
		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::getmaxSeasonId():" + e);
		}
		return researchCenterId;
	}

	@Override
	public ResearchCenterHelperBean getByResearchCenterId(Integer researchCenterBeanId) {
		ResearchCenterHelperBean rCBean = new ResearchCenterHelperBean();
		try {
			ResearchCenter rc = researchCenterRepository.getOne(researchCenterBeanId);

			rCBean.setResearchCenterBeanId(rc.getResearchCenterId());
			rCBean.setResearchCenterBeanName(rc.getResearchCenterName());
			rCBean.setLabStatusBean(rc.isLabStatus());
			rCBean.setStatusBean(rc.isStatus());
			rCBean.setDescriptionBean(rc.getDesc());
			List<ResearchCenterLocation> locationList = researchCenterLocationRepository
					.viewAllRCLocationByresearchCenterIdAndStatus(rc.getResearchCenterId());

			
			String region = null;
			String zone = null;
			Map<String, String> woridaMapList = new HashMap<>();
			for (ResearchCenterLocation rCenterLocation : locationList) {
				DemographicEntity d = demographicRepository
						.getRegionDetialsByDemographyId(rCenterLocation.getRegionId());
				ResearchCenter r1 = rCenterLocation.getResearchCenterId();
				if (rc.getResearchCenterId() == r1.getResearchCenterId()) {
					
					rCBean.setRegionBeanId(d.getDemographyId());
				} else {
					region = "NA";
				}

				DemographicEntity d1 = demographicRepository.getZoneDetialsByDemographyId(rCenterLocation.getZoneId());
				ResearchCenter r2 = rCenterLocation.getResearchCenterId();
				if (rc.getResearchCenterId() == r2.getResearchCenterId()) {
					// zone=
					rCBean.setZoneBeanId(d.getDemographyId());
					rCBean.setZoneNameBean(d1.getDemographyName());
				} else {
					zone = "NA";
				}

				DemographicEntity d2 = demographicRepository
						.getWoredaDetialsByDemographyId(rCenterLocation.getWoredaId());
				ResearchCenter r3 = rCenterLocation.getResearchCenterId();
				if (rc.getResearchCenterId() == r3.getResearchCenterId()) {

					
					woridaMapList.put(String.valueOf(d2.getDemographyId()), d2.getDemographyName());

				}
			}

			
			rCBean.setCheckWoridaBeanID(woridaMapList);
			// This For Location retrieve End

			ArrayList<String> rustList = new ArrayList<>();
			List<ResearchCenterSpelization> rCSpelization = researchCenterSpelizationRepository
					.viewAllRCSpelizationByresearchCenterIdAndStatus(rc.getResearchCenterId());
			for (ResearchCenterSpelization spelization : rCSpelization) {

				TypeOfRust tRust = spelization.getSpelizationId();

				TypeOfRust rust = typeOfRustRepository.getTypeIfRustById(tRust.getRustId());
				rustList.add(String.valueOf(rust.getRustId()));

			}

			rCBean.setRustBeanIDEdit(rustList);

		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::getByResearchCenterId():" + e);
		}
		return rCBean;
	}

	@Override
	public String viewResearchCenterByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<Object[]> page = researchCenterRepository
					.viewAllResearchCenterPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));

			int currentPage = page.getNumber();
			int begingPage = Math.max(1, currentPage - 5);
			int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
			jObject.put(WrsisPortalConstant.CURRENT_PAGE, currentPage);
			jObject.put(WrsisPortalConstant.START_PAGE, begingPage);
			jObject.put(WrsisPortalConstant.END_PAGE, endPage);
			jObject.put(WrsisPortalConstant.PAGE_NO, page.getNumber());
			jObject.put(WrsisPortalConstant.SHOW_ROW_NO, page.getSize());
			jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

			JSONObject jsonObject = null;
		
			for (Object[] rc : page) {
				jsonObject = new JSONObject();
				if (rc != null) {
					jsonObject.put("id", rc[0]);
					jsonObject.put("name", rc[1].toString() != null ? rc[1] : "-NA-");
					Integer rcId = Integer.parseInt(rc[0].toString());

					/* Today Code Start */

					/* retrieve region Start */

					ArrayList<String> regionlist = new ArrayList<>();
					Object regio = rc[7];
					String[] regioArr = regio.toString().split(",");
					for (int i = 0; i < regioArr.length; i++) {
						DemographicEntity regionOb = demographicRepository
								.getRegionDetialsByDemographyId(Integer.parseInt(regioArr[i]));
						if (regionOb != null) {
							regionlist.add(regionOb.getDemographyName());
						} else {
							regionlist.add("NA");
						}

					}

					/* retrieve region End */

					/* retrieve Zone Start */

					ArrayList<String> zoneelist = new ArrayList<>();
					Object zonio = rc[8];
					String[] zoneArr = zonio.toString().split(",");
					for (int j = 0; j < zoneArr.length; j++) {
						DemographicEntity zoneOb = demographicRepository
								.getRegionDetialsByDemographyId(Integer.parseInt(zoneArr[j]));
						if (zoneOb != null) {
							zoneelist.add(zoneOb.getDemographyName());
						} else {
							zoneelist.add("NA");
						}

					}

					/* retrieve Zone End */

					/* retrieve Woreda Start */

					ArrayList<String> WoridaList = new ArrayList<>();
					Object woreio = rc[9];
					String[] woredaArr = woreio.toString().split(",");
					for (int k = 0; k < woredaArr.length; k++) {
						DemographicEntity woreOb = demographicRepository
								.getRegionDetialsByDemographyId(Integer.parseInt(woredaArr[k]));
						if (woreOb != null) {
							WoridaList.add(woreOb.getDemographyName());
						} else {
							WoridaList.add("NA");
						}

					}

					/* retrieve Woreda End */
					/* Today Code End */
					ArrayList<String> rustList = new ArrayList<>();
					List<ResearchCenterSpelization> rCSpelization = researchCenterSpelizationRepository
							.viewAllRCSpelizationByresearchCenterIdAndStatus(rcId);
					for (ResearchCenterSpelization spelization : rCSpelization) {

						TypeOfRust tRust = spelization.getSpelizationId();

						TypeOfRust rust = typeOfRustRepository.getTypeIfRustById(tRust.getRustId());

						rustList.add(rust.getTypeOfRust());

					}

					List<String> fRegion = regionlist.stream().distinct().collect(Collectors.toList());

					if (fRegion != null) {
						jsonObject.put(WrsisPortalConstant.REGION1, fRegion);
					} else {
						jsonObject.put(WrsisPortalConstant.REGION1, "-NA-");
					}
					List<String> fZone = zoneelist.stream().distinct().collect(Collectors.toList());
					if (fZone != null) {
						jsonObject.put("zone", fZone);
					} else {
						jsonObject.put("zone", "-NA-");
					}
					List<String> fWoreda = WoridaList.stream().distinct().collect(Collectors.toList());
					if (fWoreda != null) {
						jsonObject.put("worida", fWoreda);
					} else {
						jsonObject.put("worida", "-NA-");
					}
					jsonObject.put("rust", rustList);
					if (Boolean.parseBoolean(rc[2].toString()) ) {
						jsonObject.put("lab", WrsisPortalConstant.YES);
					} else {
						jsonObject.put("lab", WrsisPortalConstant.NO);
					}

					if (!Boolean.parseBoolean(rc[4].toString()) )
						jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
					else
						jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);
				}
				array.put(jsonObject);

			}
			jObject.put(WrsisPortalConstant.RESEARCH_CENTER_LIST, array);
		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::viewResearchCenterByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchResearchCenterByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String name,
			String status, Integer regionId, Integer zoneId) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {

			Page<Object[]> page = null;
			if (!status.equals(WrsisPortalConstant.SELECT)) {
				page = researchCenterRepository.findBystatus(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (regionId != 0) {
				page = researchCenterRepository.searchByRegionIdInResearchCenter(regionId,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				if (zoneId != null) {
					page = researchCenterRepository.searchByZoneIdInResearchCenter(zoneId,
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
			}
			if (name != null && !"".equalsIgnoreCase(name)) {
				page = researchCenterRepository.findByResearchCenterNameIgnoreCaseContaining(name,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}

			if (!status.equals(WrsisPortalConstant.SELECT) && regionId != 0) {
				page = researchCenterRepository.searchByStatusAndRegionIdInResearchCenter(Boolean.parseBoolean(status),
						regionId, new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				if (!status.equals(WrsisPortalConstant.SELECT) && zoneId != null) {
					page = researchCenterRepository.searchByStatusNadZoneIdInResearchCenter(
							Boolean.parseBoolean(status), zoneId,
							new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
				}
			}
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put(WrsisPortalConstant.CURRENT_PAGE, currentPage);
				jObject.put(WrsisPortalConstant.START_PAGE, begingPage);
				jObject.put(WrsisPortalConstant.END_PAGE, endPage);
				jObject.put(WrsisPortalConstant.PAGE_NO, page.getNumber());
				jObject.put(WrsisPortalConstant.SHOW_ROW_NO, page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;
				JSONArray monthArray = null;
				for (Object[] rc : page) {
					jsonObject = new JSONObject();
					if (rc != null) {
						jsonObject.put("id", rc[0]);
						jsonObject.put("name", rc[1].toString() != null ? rc[1] : "-NA-");
						Integer rcId = Integer.parseInt(rc[0].toString());

						/* Today Code Start */

						/* retrieve region Start */

						ArrayList<String> regionlist = new ArrayList<>();
						Object regio = rc[7];
						String[] regioArr = regio.toString().split(",");
						for (int i = 0; i < regioArr.length; i++) {
							DemographicEntity regionOb = demographicRepository
									.getRegionDetialsByDemographyId(Integer.parseInt(regioArr[i]));
							if (regionOb != null) {
								regionlist.add(regionOb.getDemographyName());
							} else {
								regionlist.add("NA");
							}

						}

						/* retrieve region End */

						/* retrieve Zone Start */

						ArrayList<String> zoneelist = new ArrayList<>();
						Object zonio = rc[8];
						String[] zoneArr = zonio.toString().split(",");
						for (int j = 0; j < zoneArr.length; j++) {
							DemographicEntity zoneOb = demographicRepository
									.getRegionDetialsByDemographyId(Integer.parseInt(zoneArr[j]));
							if (zoneOb != null) {
								zoneelist.add(zoneOb.getDemographyName());
							} else {
								zoneelist.add("NA");
							}

						}

						/* retrieve Zone End */

						/* retrieve Woreda Start */

						ArrayList<String> WoridaList = new ArrayList<>();
						Object woreio = rc[9];
						String[] woredaArr = woreio.toString().split(",");
						for (int k = 0; k < woredaArr.length; k++) {
							DemographicEntity woreOb = demographicRepository
									.getRegionDetialsByDemographyId(Integer.parseInt(woredaArr[k]));
							if (woreOb != null) {
								WoridaList.add(woreOb.getDemographyName());
							} else {
								WoridaList.add("NA");
							}

						}

						/* retrieve Woreda End */
						/* Today Code End */
						ArrayList<String> rustList = new ArrayList<>();
						List<ResearchCenterSpelization> rCSpelization = researchCenterSpelizationRepository
								.viewAllRCSpelizationByresearchCenterIdAndStatus(rcId);
						for (ResearchCenterSpelization spelization : rCSpelization) {
							TypeOfRust tRust = spelization.getSpelizationId();

							TypeOfRust rust = typeOfRustRepository.getTypeIfRustById(tRust.getRustId());
							rustList.add(rust.getTypeOfRust());

						}

						List<String> fRegion = regionlist.stream().distinct().collect(Collectors.toList());

						if (fRegion != null) {
							jsonObject.put(WrsisPortalConstant.REGION1, fRegion);
						} else {
							jsonObject.put(WrsisPortalConstant.REGION1, "-NA-");
						}
						List<String> fZone = zoneelist.stream().distinct().collect(Collectors.toList());
						if (fZone != null) {
							jsonObject.put("zone", fZone);
						} else {
							jsonObject.put("zone", "-NA-");
						}
						List<String> fWoreda = WoridaList.stream().distinct().collect(Collectors.toList());
						if (fWoreda != null) {
							jsonObject.put("worida", fWoreda);
						} else {
							jsonObject.put("worida", "-NA-");
						}
						jsonObject.put("rust", rustList);
						if (Boolean.parseBoolean(rc[2].toString())) {
							jsonObject.put("lab", WrsisPortalConstant.YES);
						} else {
							jsonObject.put("lab", WrsisPortalConstant.NO);
						}

						if (!Boolean.parseBoolean(rc[4].toString()))
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
						else
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);
					}
					array.put(jsonObject);

				}
				jObject.put(WrsisPortalConstant.RESEARCH_CENTER_LIST, array);
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::searchResearchCenterByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchResearchCenterRegionAndZoneByPage(Integer pageSize, Integer pageNumber, Pageable pageable,
			String regionId, String zoneId) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {

			Page<Object[]> page = null;
			if (!regionId.equals("")) {
				page = researchCenterRepository.searchByRegionId(Integer.parseInt(regionId),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));

			}

			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put(WrsisPortalConstant.CURRENT_PAGE, currentPage);
				jObject.put(WrsisPortalConstant.START_PAGE, begingPage);
				jObject.put(WrsisPortalConstant.END_PAGE, endPage);
				jObject.put(WrsisPortalConstant.PAGE_NO, page.getNumber());
				jObject.put(WrsisPortalConstant.SHOW_ROW_NO, page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;
			
				for (Object[] rc : page) {

					jsonObject = new JSONObject();
					if (rc != null) {
						jsonObject.put("id", rc[0]);
						jsonObject.put("name", rc[1] != null ? rc[1] : "-NA-");
						int rcId = (Short) rc[0];

						// This For Location retrieve Start
						List<ResearchCenterLocation> locationList = researchCenterLocationRepository
								.viewAllRCLocationByresearchCenterIdAndStatus(rcId);

						ArrayList<String> WoridaList = new ArrayList<>();
						String region = null;
						String zone = null;

						for (ResearchCenterLocation rCenterLocation : locationList) {
							LOG.info("Region ID==" + rCenterLocation.getRegionId());
							DemographicEntity d = demographicRepository
									.getRegionDetialsByDemographyId(rCenterLocation.getRegionId());
							ResearchCenter r1 = rCenterLocation.getResearchCenterId();
							if (rcId == r1.getResearchCenterId()) {
								LOG.info("Region Name==" + d.getDemographyName());
								region = d.getDemographyName();
							} else {
								region = "NA";
							}

							DemographicEntity d1 = demographicRepository
									.getZoneDetialsByDemographyId(rCenterLocation.getZoneId());
							ResearchCenter r2 = rCenterLocation.getResearchCenterId();
							if (rcId == r2.getResearchCenterId()) {
								zone = d1.getDemographyName();
							} else {
								zone = "NA";
							}

							DemographicEntity d2 = demographicRepository
									.getWoredaDetialsByDemographyId(rCenterLocation.getWoredaId());
							ResearchCenter r3 = rCenterLocation.getResearchCenterId();
							if (rcId == r3.getResearchCenterId()) {

								WoridaList.add(d2.getDemographyName());
							}
						}
						// This For Location retrieve End

						ArrayList<String> rustList = new ArrayList<>();
						List<ResearchCenterSpelization> rCSpelization = researchCenterSpelizationRepository
								.viewAllRCSpelizationByresearchCenterIdAndStatus(rcId);
						for (ResearchCenterSpelization spelization : rCSpelization) {

							TypeOfRust tRust = spelization.getSpelizationId();

							TypeOfRust rust = typeOfRustRepository.getTypeIfRustById(tRust.getRustId());
							rustList.add(rust.getTypeOfRust());

						}
						if (region != null) {
							jsonObject.put(WrsisPortalConstant.REGION1, region);
						} else {
							jsonObject.put(WrsisPortalConstant.REGION1, "-NA-");
						}
						if (zone != null) {
							jsonObject.put("zone", zone);
						} else {
							jsonObject.put("zone", "-NA-");
						}
						jsonObject.put("worida", WoridaList);
						jsonObject.put("rust", rustList);
						boolean labStatus = (boolean) rc[2];
						if (labStatus) {
							jsonObject.put("lab", WrsisPortalConstant.YES);
						} else {
							jsonObject.put("lab", WrsisPortalConstant.NO);
						}

						boolean status = (boolean) rc[3];
						if (!status)
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
						else
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);
					}
					array.put(jsonObject);

				}
				jObject.put(WrsisPortalConstant.RESEARCH_CENTER_LIST, array);
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::searchResearchCenterRegionAndZoneByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public List<ResearchCenter> getResearchCenterList() {
		
		return researchCenterRepository.findActiveResearchCenter();
	}

	@Override
	public List<ResearchCenter> getResearchCenterListByRustType(Integer rustTypeId) {
		List<Object[]> rcList = researchCenterRepository.retriveResearchCenterListByRustType(rustTypeId);
		List<ResearchCenter> list = new ArrayList<>();
		for (Object[] objs : rcList) {
			ResearchCenter rc = new ResearchCenter();
			int rcId = (Short) objs[0];
			rc.setResearchCenterId(rcId);
			rc.setResearchCenterName(String.valueOf(objs[1]));
			list.add(rc);
		}
		return list;
	}

	@Override
	public boolean checkDuplicateRCName(Integer researchCenterBeanId, String rcName) {
		boolean res = false;
		try {
			List<ResearchCenter> nameList = researchCenterRepository.viewAllRCName(rcName);
			if (nameList.size() > 0 && nameList != null) {

				if (researchCenterBeanId != 0) {
					if (researchCenterBeanId == nameList.get(0).getResearchCenterId()) {
						res = false;
					} else {
						res = true;
					}
				} else {
					res = true;
				}

			} else {
				res = false;
			}
		} catch (Exception e) {
			LOG.error("ResearchCenterServiceImpl::checkDuplicateRCName():" + e);
		}
		return res;
	}

	@Override
	public List<ResearchCenter> getResearchCenterListOrder() {
		return researchCenterRepository.findByStatusOrderByResearchCenterName(false);
	}

}
