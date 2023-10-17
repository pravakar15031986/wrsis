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

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.RaceChartHelperBean;
import com.csmpl.wrsis.webportal.entity.RaceIdentificationChart;
import com.csmpl.wrsis.webportal.repository.RaceIdentificationChartRepository;
import com.csmpl.wrsis.webportal.service.RaceIdentificationChartService;

@Service
public class RaceIdentificationChartServiceImpl implements RaceIdentificationChartService {

	private final static Logger LOG = LoggerFactory.getLogger(RaceIdentificationChartServiceImpl.class);
	@Autowired
	RaceIdentificationChartRepository raceChartRepository;

	@Override
	public String saveAndUpdateRaceChart(RaceIdentificationChart rChart) {
		String res = "";
		try {
			if (rChart.getRaceChartId() == 0) {
				rChart.setCreatedOn(new Date());
				RaceIdentificationChart rC = raceChartRepository.save(rChart);
				if (rC != null) {
					res = WrsisPortalConstant.SAVE;
				} else
					res = WrsisPortalConstant.FAILURE;
			} else {
				RaceIdentificationChart raceChart = raceChartRepository.findByRaceChartId(rChart.getRaceChartId());
				if (raceChart != null) {
					rChart.setUpdatedBy(rChart.getCreatedBy());
					rChart.setUpdateOn(new Date());
					rChart.setCreatedOn(raceChart.getCreatedOn());
					RaceIdentificationChart rC = raceChartRepository.save(rChart);
					if (rC != null) {
						res = WrsisPortalConstant.UPDATE;
					} else
						res = WrsisPortalConstant.FAILURE;
				}
			}
		} catch (Exception e) {
			LOG.error("RaceIdentificationChartServiceImpl::saveAndUpdateRaceChart():" + e);

		}
		return res;
	}

	@Override
	public RaceIdentificationChart getByRaceChartId(Integer raceChartId) {
		return raceChartRepository.findByRaceChartId(raceChartId);
	}

	@Override
	public String viewRaceIdentificationChartByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<RaceIdentificationChart> page = raceChartRepository
					.viewAllRaceChartByPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (RaceIdentificationChart raceChartMaster : page) {
					jsonObject = new JSONObject();
					if (raceChartMaster != null) {
						jsonObject.put("id",
								raceChartMaster.getRaceChartId() != 0 ? raceChartMaster.getRaceChartId() : "-NA-");

						if (raceChartMaster.getFisrtSeq() != null) {
							if (raceChartMaster.getFisrtSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_1, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_1, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_1, "-NA-");
						}
						if (raceChartMaster.getSecondSeq() != null) {
							if (raceChartMaster.getSecondSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_2, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_2, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_2, "-NA-");
						}
						if (raceChartMaster.getThirdSeq() != null) {
							if (raceChartMaster.getThirdSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_3, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_3, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_3, "-NA-");
						}
						if (raceChartMaster.getFourthSeq() != null) {
							if (raceChartMaster.getFourthSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_4, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_4, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_4, "-NA-");
						}
						jsonObject.put("name",
								raceChartMaster.getNameResult() != null ? raceChartMaster.getNameResult() : "-NA-");
						jsonObject.put("descr",
								raceChartMaster.getDescription() != null ? raceChartMaster.getDescription() : "-NA-");
						if (!raceChartMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("raceChartList", array);

			}
		} catch (Exception e) {
			LOG.error("RaceIdentificationChartServiceImpl::viewRaceIdentificationChartByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public List<RaceIdentificationChart> viewAllRaceChart() {
		return raceChartRepository.viewAllRaceChart();
	}

	@Override
	public ArrayList<RaceChartHelperBean> sequenceISExit(RaceIdentificationChart rChart) {
		ArrayList<RaceChartHelperBean> sequenceExit = new ArrayList<>();
		RaceChartHelperBean helper = new RaceChartHelperBean();
		try {

			ArrayList<String> givenSequence = new ArrayList<>();
			givenSequence.add(rChart.getFisrtSeq());
			givenSequence.add(rChart.getSecondSeq());
			givenSequence.add(rChart.getThirdSeq());
			givenSequence.add(rChart.getFourthSeq());


			List<RaceIdentificationChart> raceList = raceChartRepository.findAll();
			ArrayList<String> getSequence = null;
			for (RaceIdentificationChart raceChart : raceList) {

				getSequence = new ArrayList<>();
				getSequence.add(raceChart.getFisrtSeq());
				getSequence.add(raceChart.getSecondSeq());
				getSequence.add(raceChart.getThirdSeq());
				getSequence.add(raceChart.getFourthSeq());
				

				boolean sequenceExitResult = givenSequence.equals(getSequence); // false
				if (sequenceExitResult) {
					helper.setRaceHelperId(raceChart.getRaceChartId());
					helper.setRaceStatusBean(raceChart.isStatus());
					helper.setResult(WrsisPortalConstant.EXIST);
					sequenceExit.add(helper);
					break;

				}
			}
			
		} catch (Exception e) {
			LOG.error("RaceIdentificationChartServiceImpl::sequenceISExist():" + e);

		}
		return sequenceExit;
	}

	@Override
	public String searchviewRaceIdentificationChart(String name, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		Page<RaceIdentificationChart> page = null;
		try {
			if (!status.equals("select")) {
				page = raceChartRepository.findByStatus(Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (!name.equals("")) {
				page = raceChartRepository.findByNameResultContainingIgnoreCase(name,
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			}
			if (!name.equals("") && !status.equals("select")) {
				page = raceChartRepository.findByNameResultContainingIgnoreCaseAndStatus(name,
						Boolean.parseBoolean(status),
						new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
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
				for (RaceIdentificationChart raceChartMaster : page) {
					jsonObject = new JSONObject();
					if (raceChartMaster != null) {
						jsonObject.put("id",
								raceChartMaster.getRaceChartId() != 0 ? raceChartMaster.getRaceChartId() : "-NA-");

						if (raceChartMaster.getFisrtSeq() != null) {
							if (raceChartMaster.getFisrtSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_1, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_1, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_1, "-NA-");
						}
						if (raceChartMaster.getSecondSeq() != null) {
							if (raceChartMaster.getSecondSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_2, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_2, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_2, "-NA-");
						}
						if (raceChartMaster.getThirdSeq() != null) {
							if (raceChartMaster.getThirdSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_3, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_3, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_3, "-NA-");
						}
						if (raceChartMaster.getFourthSeq() != null) {
							if (raceChartMaster.getFourthSeq().equals("l"))
								jsonObject.put(WrsisPortalConstant.SEQ_4, "Low");
							else
								jsonObject.put(WrsisPortalConstant.SEQ_4, "High");
						} else {
							jsonObject.put(WrsisPortalConstant.SEQ_4, "-NA-");
						}
						jsonObject.put("name",
								raceChartMaster.getNameResult() != null ? raceChartMaster.getNameResult() : "-NA-");
						jsonObject.put("descr",
								raceChartMaster.getDescription() != null ? raceChartMaster.getDescription() : "-NA-");
						if (!raceChartMaster.isStatus())
							jsonObject.put(WrsisPortalConstant.STATUS, "Active");
						else
							jsonObject.put(WrsisPortalConstant.STATUS, "Inactive");
					}
					array.put(jsonObject);
				}
				jObject.put("raceChartList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, "0");
			}
		} catch (Exception e) {
			LOG.error("RaceIdentificationChartServiceImpl::viewRaceIdentificationChartByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public boolean chartNameISExit(String chartName) {
		boolean res = false;
		try {
			RaceIdentificationChart race = raceChartRepository.findBynameResultContainingIgnoreCase(chartName);
			if (race != null) {
				res = true;
			}
		} catch (Exception e) {
			LOG.error("RaceIdentificationChartServiceImpl::chartNameISExit():" + e);

		}

		return res;
	}
}
