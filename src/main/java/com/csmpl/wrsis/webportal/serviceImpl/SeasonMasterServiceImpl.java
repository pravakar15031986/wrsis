package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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
import com.csmpl.wrsis.webportal.bean.SeasonMonthBean;
import com.csmpl.wrsis.webportal.entity.Month;
import com.csmpl.wrsis.webportal.entity.SeasonMaster;
import com.csmpl.wrsis.webportal.entity.SeasonMonth;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.repository.SeasonMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMonthRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.service.MonthService;
import com.csmpl.wrsis.webportal.service.SeasonMasterService;

@Service
public class SeasonMasterServiceImpl implements SeasonMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(SeasonMasterServiceImpl.class);
	@Autowired
	MonthService monthService;
	@Autowired
	SeasonMasterRepository seasonMasterRepository;
	@Autowired
	SeasonMonthRepository seasonMonthRepository;
	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;

	@Transactional
	@Override
	public String saveAndUpdateSeasonMaster(SeasonMonthBean seasonMonthBean) {
		String res = "";
		try {
			SeasonMaster seaMaster = new SeasonMaster();
			seaMaster.setSeasonName(seasonMonthBean.getSeasonName());
			seaMaster.setCreatedBy(seasonMonthBean.getCreatedBy());
			seaMaster.setCreatedOn(new Date());
			seaMaster.setDesc(seasonMonthBean.getDesc());
			seaMaster.setStatus(seasonMonthBean.isStatus());
			if (seasonMonthBean.getSeasonIdBean() == 0) {
				SeasonMaster sm = seasonMasterRepository.saveAndFlush(seaMaster);
				int seasonMasterId = sm.getSeasonId();

				SeasonMonth seaMonth = null;

				// Insert for Season_Month Table
				for (int i = 0; i < seasonMonthBean.getMonthId().length; i++) {
					seaMonth = new SeasonMonth();

					Month month = monthService.getMonthById(Integer.valueOf(seasonMonthBean.getMonthId()[i]));
					if (month != null) {
						seaMonth.setSeasonId(seasonMasterId);
						seaMonth.setMonthId(month.getMonthId());
						seaMonth.setMonthname(month.getMonthName());
						seaMonth.setStatus(seasonMonthBean.isStatus());
						seaMonth.setCreatedBy(seasonMonthBean.getCreatedBy());
						seaMonth.setCreatedOn(new Date());
						seasonMonthRepository.save(seaMonth);
					}

				}

				if (sm != null) {
					res = WrsisPortalConstant.SAVE;
				} else
					res = WrsisPortalConstant.FAILURE;

			} else {
				// in Case of Edit

				List<SurveyDetails> list = surveyDetailsRepository
						.findSeasonIdBySeasonId(seasonMonthBean.getSeasonIdBean());
				if (!seasonMonthBean.isStatus()) {
					seaMaster.setSeasonId(seasonMonthBean.getSeasonIdBean());
					SeasonMaster seMaster = seasonMasterRepository.findBySeasonId(seasonMonthBean.getSeasonIdBean());
					seaMaster.setUpdateBy(seasonMonthBean.getCreatedBy());
					seaMaster.setUpdatOn(new Date());
					seaMaster.setCreatedBy(seMaster.getCreatedBy());
					seaMaster.setCreatedOn(seMaster.getCreatedOn());
					// here Save parent data
					SeasonMaster sm = seasonMasterRepository.save(seaMaster);
					SeasonMonth seaMonth = null;

					List<SeasonMonth> sMList = seasonMonthRepository.findBySeasonId(seasonMonthBean.getSeasonIdBean());

					// Here request from Page from month start
					ArrayList<String> pageList = new ArrayList<>();
					ArrayList<String> pageList_2 = new ArrayList<>();
					for (int i = 0; i < seasonMonthBean.getMonthId().length; i++) {

						pageList.add(seasonMonthBean.getMonthId()[i]);
						pageList_2.add(seasonMonthBean.getMonthId()[i]);
					}
					// Here request from Page from month End

					// Here get all month from seasonMonth Table start

					ArrayList<String> sMTableList = new ArrayList<>();
					for (SeasonMonth seasonMonth : sMList) {
						if (!seasonMonth.isStatus()) {
							sMTableList.add(String.valueOf(seasonMonth.getMonthId()));
						}
					}
					// Here get all month from seasonMonth Table End

					pageList.removeAll(sMTableList);

					LOG.info("Common==" + pageList);

					if (!pageList.isEmpty()) {
						for (String string : pageList) {

							seaMonth = new SeasonMonth();

							Month month = monthService.getMonthById(Integer.valueOf(string));
							if (month != null) {

								seaMonth.setSeasonId(seasonMonthBean.getSeasonIdBean());
								seaMonth.setMonthId(month.getMonthId());
								seaMonth.setMonthname(month.getMonthName());
								seaMonth.setStatus(sm.isStatus());
								seaMonth.setCreatedBy(seasonMonthBean.getCreatedBy());
								seaMonth.setCreatedOn(new Date());
								

								seasonMonthRepository.save(seaMonth);
							}
						}

					} else {

						sMTableList.removeAll(pageList_2);
						LOG.info("remove 2nd ==" + sMTableList);
						for (String string : sMTableList) {

							List<SeasonMonth> sM = seasonMonthRepository
									.findByMonthId(seasonMonthBean.getSeasonIdBean(), Integer.parseInt(string));

							for (SeasonMonth seasonMonth : sM) {
								seaMonth = new SeasonMonth();
								if (seasonMonth != null) {
									seaMonth.setStatus(true);
									seaMonth.setMonthId(Integer.parseInt(string));
									seaMonth.setSeasonId(seasonMonthBean.getSeasonIdBean());
									seaMonth.setUpdateBy(seasonMonthBean.getCreatedBy());
									seaMonth.setUpdateOn(new Date());
									seaMonth.setStatus(true);
									seaMonth.setSeasonMonthId(seasonMonth.getSeasonMonthId());
									seaMonth.setCreatedBy(seasonMonth.getCreatedBy());
									seaMonth.setCreatedOn(seasonMonth.getCreatedOn());
									seaMonth.setMonthname(seasonMonth.getMonthname());

									seasonMonthRepository.save(seaMonth);
								}
							}

						}
						// Here no changes from child table then it will saved
						if (sMTableList.isEmpty()) {
							List<SeasonMonth> sameSeasonMonthList = seasonMonthRepository
									.findBySeasonId(seasonMonthBean.getSeasonIdBean());
							for (SeasonMonth seasonMonth : sameSeasonMonthList) {
								seasonMonth.setSeasonMonthId(seasonMonth.getSeasonMonthId());
								seasonMonth.setUpdateBy(seasonMonthBean.getCreatedBy());
								seasonMonth.setStatus(seasonMonthBean.isStatus());
								seasonMonth.setUpdateOn(new Date());
								seasonMonthRepository.save(seasonMonth);

							}

						}
					}
					if (sm != null) {
						res = WrsisPortalConstant.UPDATE;
					} else
						res = WrsisPortalConstant.FAILURE;
				} else {
					if (list.isEmpty()) {
						seaMaster.setSeasonId(seasonMonthBean.getSeasonIdBean());
						SeasonMaster seMaster = seasonMasterRepository
								.findBySeasonId(seasonMonthBean.getSeasonIdBean());
						seaMaster.setUpdateBy(seasonMonthBean.getCreatedBy());
						seaMaster.setUpdatOn(new Date());
						seaMaster.setCreatedBy(seMaster.getCreatedBy());
						seaMaster.setCreatedOn(seMaster.getCreatedOn());
						// here Save parent data
						SeasonMaster sm = seasonMasterRepository.save(seaMaster);
						SeasonMonth seaMonth = null;

						List<SeasonMonth> sMList = seasonMonthRepository
								.findBySeasonId(seasonMonthBean.getSeasonIdBean());

						// Here request from Page from month start
						ArrayList<String> pageList = new ArrayList<>();
						ArrayList<String> pageList_2 = new ArrayList<>();
						for (int i = 0; i < seasonMonthBean.getMonthId().length; i++) {

							pageList.add(seasonMonthBean.getMonthId()[i]);
							pageList_2.add(seasonMonthBean.getMonthId()[i]);
						}
						// Here request from Page from month End

						// Here get all month from seasonMonth Table start

						ArrayList<String> sMTableList = new ArrayList<>();
						for (SeasonMonth seasonMonth : sMList) {
							if (!seasonMonth.isStatus()) {
								sMTableList.add(String.valueOf(seasonMonth.getMonthId()));
							}
						}
						// Here get all month from seasonMonth Table End

						pageList.removeAll(sMTableList);

						LOG.info("Common==" + pageList);

						if (!pageList.isEmpty()) {
							for (String string : pageList) {

								seaMonth = new SeasonMonth();

								Month month = monthService.getMonthById(Integer.valueOf(string));
								if (month != null) {

									seaMonth.setSeasonId(seasonMonthBean.getSeasonIdBean());
									seaMonth.setMonthId(month.getMonthId());
									seaMonth.setMonthname(month.getMonthName());
									seaMonth.setStatus(sm.isStatus());
									seaMonth.setCreatedBy(seasonMonthBean.getCreatedBy());
									seaMonth.setCreatedOn(new Date());
									

									seasonMonthRepository.save(seaMonth);
								}
							}

						} else {

							sMTableList.removeAll(pageList_2);
							LOG.info("remove 2nd ==" + sMTableList);
							for (String string : sMTableList) {

								List<SeasonMonth> sM = seasonMonthRepository
										.findByMonthId(seasonMonthBean.getSeasonIdBean(), Integer.parseInt(string));

								for (SeasonMonth seasonMonth : sM) {
									seaMonth = new SeasonMonth();
									if (seasonMonth != null) {
										seaMonth.setStatus(true);
										seaMonth.setMonthId(Integer.parseInt(string));
										seaMonth.setSeasonId(seasonMonthBean.getSeasonIdBean());
										seaMonth.setUpdateBy(seasonMonthBean.getCreatedBy());
										seaMonth.setUpdateOn(new Date());
										seaMonth.setStatus(true);
										seaMonth.setSeasonMonthId(seasonMonth.getSeasonMonthId());
										seaMonth.setCreatedBy(seasonMonth.getCreatedBy());
										seaMonth.setCreatedOn(seasonMonth.getCreatedOn());
										seaMonth.setMonthname(seasonMonth.getMonthname());

										seasonMonthRepository.save(seaMonth);
									}
								}

							}
							// Here no changes from child table then it will saved
							if (sMTableList.isEmpty()) {
								List<SeasonMonth> sameSeasonMonthList = seasonMonthRepository
										.findBySeasonId(seasonMonthBean.getSeasonIdBean());
								for (SeasonMonth seasonMonth : sameSeasonMonthList) {
									seasonMonth.setSeasonMonthId(seasonMonth.getSeasonMonthId());
									seasonMonth.setUpdateBy(seasonMonthBean.getCreatedBy());
									seasonMonth.setStatus(seasonMonthBean.isStatus());
									seasonMonth.setUpdateOn(new Date());
									seasonMonthRepository.save(seasonMonth);
								}
							}
						}
						if (sm != null) {
							res = WrsisPortalConstant.UPDATE;
						} else
							res = WrsisPortalConstant.FAILURE;
					} else {
						return WrsisPortalConstant.DEPENDENT;
					}
				}

				

			}

		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::saveAndUpdateseasonMaster():" + e);
		}

		return res;
	}

	@Override
	public int getmaxSeasonId() {
		Integer seasonId = null;
		try {
			seasonId = seasonMasterRepository.findMaxSeasonId();
			if (seasonId == null) {
				seasonId = 1;
			} else {
				seasonId = seasonId + 1;
				
			}
		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::getmaxSeasonId():" + e);
		}
		return seasonId;
	}

	@Override
	public List<SeasonMonthBean> viewAllSeason() {
		List<SeasonMonthBean> listAr = new ArrayList<>();
		SeasonMonthBean bean = null;
		try {

			
			List<SeasonMaster> list = seasonMasterRepository.viewAllSeason();
			for (SeasonMaster seasonMaster : list) {
				bean = new SeasonMonthBean();

				bean.setSeasonIdBean(seasonMaster.getSeasonId());
				bean.setSeasonName(seasonMaster.getSeasonName());
				bean.setDesc(seasonMaster.getDesc());
				bean.setStatus(seasonMaster.isStatus());

				List<SeasonMonth> monthList = seasonMonthRepository.findBySeasonId(seasonMaster.getSeasonId());
				ArrayList<String> month = new ArrayList<>();
				for (SeasonMonth seasonMonth : monthList) {
					month.add(seasonMonth.getMonthname());

				}
				bean.setDispMonthName(month);
				listAr.add(bean);

			}
		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::viewAllSeason():" + e);
		}
		return listAr;
	}

	@Override
	public SeasonMonthBean getSeasonById(Integer seasonIdBean) {

		SeasonMonthBean s = new SeasonMonthBean();
		SeasonMaster sMaster = seasonMasterRepository.findBySeasonId(seasonIdBean);
		s.setCreatedBy(sMaster.getCreatedBy());
		s.setDesc(sMaster.getDesc());
		s.setSeasonIdBean(sMaster.getSeasonId());
		s.setSeasonName(sMaster.getSeasonName());
		s.setStatus(sMaster.isStatus());
		s.setCreatedOnBean(sMaster.getCreatedOn());
		return s;
	}

	@Override
	public List<SeasonMonthBean> getMonthBySeasonId(Integer seasonIdBean) {
		List<SeasonMonthBean> listAr = new ArrayList<>();
		SeasonMonthBean bean = new SeasonMonthBean();
		List<SeasonMonth> monthList = seasonMonthRepository.findBySeasonId(seasonIdBean);
		ArrayList<Integer> month = new ArrayList<>();
		for (SeasonMonth seasonMonth : monthList) {
			
			
			month.add(seasonMonth.getMonthId());
			
		}
		bean.setMonthBeanId(month);
		listAr.add(bean);
		return listAr;
	}

	@Override
	public String viewSeasonByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {

			Page<SeasonMaster> page = seasonMasterRepository
					.viewAllSeasonPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (SeasonMaster sMaster : page) {
					jsonObject = new JSONObject();
					if (sMaster != null) {
						jsonObject.put("id", sMaster.getSeasonId() != 0 ? sMaster.getSeasonId() : "-NA-");
						LOG.info("Season Id In Season Table===" + sMaster.getSeasonId());
						jsonObject.put("name", sMaster.getSeasonName() != null ? sMaster.getSeasonName() : "-NA-");
						List<SeasonMonth> monthList = seasonMonthRepository.findBySeasonId(sMaster.getSeasonId());
						monthArray = new JSONArray();
						for (SeasonMonth seasonMonth : monthList) {

							
							monthArray.put(seasonMonth.getMonthname());
							
						}

						jsonObject.put(WrsisPortalConstant.MONTH, monthArray != null ? monthArray : "-NA-");

						jsonObject.put(WrsisPortalConstant.DESCR, sMaster.getDesc() != null ? sMaster.getDesc() : "-NA-");
						if (!sMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
						else
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);
					}
					array.put(jsonObject);

				}
				jObject.put(WrsisPortalConstant.SEASONLIST, array);
			}
		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::viewSeasonByPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchseasonPage(String name, String monthId, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<SeasonMaster> page = null;
		try {
			if (!status.equals("select")) {
				page = seasonMasterRepository.findByStatus(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (!name.equals("") && !status.equals("select")) {
				page = seasonMasterRepository.findByseasonNameContainingIgnoreCaseAndStatus(name,
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (!name.equals("")) {
				page = seasonMasterRepository.findByseasonNameContainingIgnoreCase(name,
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
				JSONArray monthArray = null;
				for (SeasonMaster sMaster : page) {
					jsonObject = new JSONObject();
					if (sMaster != null) {
						jsonObject.put("id", sMaster.getSeasonId() != 0 ? sMaster.getSeasonId() : "-NA-");
						LOG.info("Season Id In Season Table===" + sMaster.getSeasonId());
						jsonObject.put("name", sMaster.getSeasonName() != null ? sMaster.getSeasonName() : "-NA-");
						List<SeasonMonth> monthList = seasonMonthRepository.findBySeasonId(sMaster.getSeasonId());
						monthArray = new JSONArray();
						for (SeasonMonth seasonMonth : monthList) {
							
							monthArray.put(seasonMonth.getMonthname());
							
							
						}
						LOG.info("month Array Length==" + monthArray);
						jsonObject.put(WrsisPortalConstant.MONTH, monthArray != null ? monthArray : "-NA-");
						jsonObject.put(WrsisPortalConstant.DESCR, sMaster.getDesc() != null ? sMaster.getDesc() : "-NA-");
						if (!sMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
						else
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);
					}
					array.put(jsonObject);

				}
				jObject.put(WrsisPortalConstant.SEASONLIST, array);
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::searchseasonPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public String searchSeasonByMonthIdPage(String monthId, Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<Object[]> page = null;
		try {

			page = seasonMasterRepository.searchBymonthId(Integer.parseInt(monthId),
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
			
				for (Object[] sMaster : page) {
					jsonObject = new JSONObject();
					if (sMaster != null) {
						jsonObject.put("id", sMaster[0]);
						jsonObject.put("name", sMaster[1]);
						Month month = monthService.getMonthById(Integer.valueOf(sMaster[5].toString()));
						jsonObject.put(WrsisPortalConstant.MONTH, month.getMonthName() != null ? month.getMonthName() : "-NA-");
						jsonObject.put(WrsisPortalConstant.DESCR, sMaster[2].toString() != null ? sMaster[2].toString() : "-NA-");
						String s = sMaster[3].toString();
						if (!Boolean.parseBoolean(s))
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.ACTIVE);
						else
							jsonObject.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.IN_ACTIVE);

					}
					array.put(jsonObject);

				}
				jObject.put(WrsisPortalConstant.SEASONLIST, array);
			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::searchSeasonByMonthIdPage():" + e);
		}
		return jObject.toString();
	}

	@Override
	public boolean isExistSeasoName(Integer seasionId, String seasonName) {
		boolean res = false;
		
		try {
			SeasonMaster sm = seasonMasterRepository.findByseasonNameIgnoreCaseAndStatus(seasonName, false);
			if (sm != null) {
				res = true;
				if (seasionId != null) {
					if (sm.getSeasonId() == seasionId) {
						res = false;
					} else if (sm.getSeasonId() != seasionId && sm.isStatus()) {
						res = false;
					}
				}

			} else {
				res = false;
			}

		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::isExistSeasonName():" + e);
		}
		return res;
	}

	@Override
	public boolean duplicateExistSeasoMonth(Integer seasionId, String[] monthId) {
		boolean res = false;
		try {
			for (int i = 0; i < monthId.length; i++) {
				List<SeasonMonth> monthList = seasonMonthRepository.findByMonthId(Integer.parseInt(monthId[i]));
				if (monthList != null && monthList.size() > 0) {
					if (seasionId != null) {
						for (SeasonMonth seasMonth : monthList) {
							if (seasionId == seasMonth.getSeasonId()) {
								res = false;
							} else if (seasMonth.isStatus()) {
								res = false;
							} else {
								res = true;
								break;
							}
						}
					} else {
						res = true;
						break;
					}
				} else {
					res = false;
				}

			}

		} catch (Exception e) {
			LOG.error("SeasonMasterServiceImpl::duplicateExistSeasoMonth():" + e);

		}
		return res;
	}
}
