package com.csmpl.adminconsole.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.service.RaceReportService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.report.domains.RaceReportDomain;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;

@Service("racereport")
public class RaceReportServiceImpl implements RaceReportService {

	public static final Logger LOG = LoggerFactory.getLogger(RaceReportServiceImpl.class);

	@Autowired
	private EntityManager em;

	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;

	@Autowired
	RaceAnalysisRepository raceAnalysisRepository;

	@Override
	public JSONObject fetchRaceReportResultWise(Integer userId, Integer rustTypeId) throws JSONException {
		JSONObject response = new JSONObject();
		try {
			
			@SuppressWarnings("unchecked")
			List<RaceReportDomain> records = em.createNamedStoredProcedureQuery("FetchRaceReportResult")
					.setParameter("vchAction", "fetchRaceResults").setParameter("userid", userId)
					.setParameter("rustTypeId", rustTypeId).getResultList();
			RaceReportDomain report = records.get(0);

			JSONArray dataJsa = new JSONArray(report.getJsonData());

			JSONArray columns = new JSONArray();
			JSONArray finalData = new JSONArray();

			if (dataJsa.length() > 0) {
				JSONArray jsa = dataJsa.getJSONObject(0).getJSONArray("raceresults");
				for (int i = 0; i < jsa.length(); i++) {
					columns.put(jsa.getJSONObject(i).getString("result"));
				}

				for (int i = 0; i < dataJsa.length(); i++) {
					JSONObject json = new JSONObject();
					JSONArray finalJson = new JSONArray();
					json = dataJsa.getJSONObject(i);
					
					finalJson.put(json.getString(WrsisPortalConstant.REGION1));
					finalJson.put(json.getInt("regioncount"));

					for (int j = 0; j < columns.length(); j++) {
						finalJson.put(json.getJSONArray("raceresults").getJSONObject(j).getString("count"));
					}
					finalJson.put(json.getString("regionid"));
					finalData.put(finalJson);

				}
			}

			response.put("Columns", columns);
			response.put("TableData", finalData);

			return response;
		} catch (Exception e) {
			LOG.error("RaceReportServiceImpl::fetchRaceReportResultwise():" + e);
			response.put("Columns", new JSONArray());
			response.put("TableData", new JSONArray());
			return response;

		}
	}

	@Override
	public List<SampleTagBean> getDetails(SearchVo searchVo) {
		if (searchVo.getRaceResult() == null)
			searchVo.setRaceResult("");
		if (searchVo.getSurveyNo() == null)
			searchVo.setSurveyNo("");
		if (searchVo.getSampleId() == null)
			searchVo.setSampleId("");
		if (searchVo.getSurveyDateFrom() == null)
			searchVo.setSurveyDateFrom("");
		if (searchVo.getSurveyDateTo() == null)
			searchVo.setSurveyDateTo("");
		if (searchVo.getRustId() == null)
			searchVo.setRustId(0);
		if (searchVo.getLabId() == null)
			searchVo.setLabId(0);

		List<Object[]> sampleTagDetails = null;
		List<SampleTagBean> samples = new ArrayList<>();
		try {
			sampleTagDetails = sampleLabTagDetailsRepository.viewDetails(searchVo.getSurveyNo(), searchVo.getSampleId(),
					searchVo.getSurveyDateFrom(), searchVo.getSurveyDateTo(), searchVo.getRaceResult(),
					searchVo.getRegionId(), searchVo.getRustId(), searchVo.getLabId());
			for (Object[] obj : sampleTagDetails) {
				SampleTagBean sample = new SampleTagBean();
				sample.setSurveyNo(String.valueOf(obj[0]));
				sample.setSampleValue(String.valueOf(obj[1]));
				sample.setRustType(String.valueOf(obj[2]));
				sample.setSurveyDate(String.valueOf(obj[3]));
				sample.setInoculationDate((obj[4] != null) ? String.valueOf(obj[4]) : "NA");
				sample.setRegion(String.valueOf(obj[5]));
				sample.setZone(String.valueOf(obj[6]));
				sample.setWoreda(String.valueOf(obj[7]));
				sample.setKebel(String.valueOf(obj[8]));
				sample.setSurveyId(Integer.valueOf(String.valueOf(obj[9])));
				sample.setResearchCenter((obj[12] != null) ? String.valueOf(obj[12]) : "NA");
				sample.setStatus(String.valueOf(obj[14]));
				sample.setRaceResult((obj[13] != null) ? String.valueOf(obj[13]) : "NA");
				sample.setRacePublishDate((obj[15] != null) ? String.valueOf(obj[15]) : "NA");
				sample.setRegionId(Integer.valueOf(String.valueOf(obj[17])));
				samples.add(sample);
			}
		} catch (Exception e) {
			LOG.error("RaceReportServiceImpl::getDetails():" + e);
		}
		return samples;
	}

	@Override
	public String fetchVirulenceData(Integer rustType) {
		return raceAnalysisRepository.fetchVirulenceData(rustType);
	}

	@Override
	public String fetchVarietyReportDataSearch(Integer rustId) {
		return raceAnalysisRepository.fetchVarietyReportDataSearch(rustId);
	}

	@Override
	public String fetchVarietyReportDataSearch(Integer rustId, Integer regionId) {
		return raceAnalysisRepository.fetchVarietyReportDataSearch(rustId, regionId);
	}

	@Override
	public List<SampleTagBean> reportExcel(String surveyNo, String sampleId, String surveyDateFrom, String surveyDateTo,
			String raceResult, Integer regionId, Integer rustId, Integer labId) {

		if (surveyNo == null)
			surveyNo = "";
		if (sampleId == null)
			sampleId = "";
		if (surveyDateFrom == null)
			surveyDateFrom = "";
		if (surveyDateTo == null)
			surveyDateTo = "";

		List<Object[]> sampleTagDetails = null;
		List<SampleTagBean> samples = new ArrayList<>();
		try {
			sampleTagDetails = sampleLabTagDetailsRepository.viewDetails(surveyNo, sampleId, surveyDateFrom,
					surveyDateTo, raceResult, regionId, rustId, labId);
			for (Object[] obj : sampleTagDetails) {
				SampleTagBean sample = new SampleTagBean();
				sample.setSurveyNo(String.valueOf(obj[0]));
				sample.setSampleValue(String.valueOf(obj[1]));
				sample.setRustType(String.valueOf(obj[2]));
				sample.setSurveyDate(String.valueOf(obj[3]));
				sample.setInoculationDate((obj[4] != null) ? String.valueOf(obj[4]) : "NA");
				sample.setRegion(String.valueOf(obj[5]));
				sample.setZone(String.valueOf(obj[6]));
				sample.setWoreda(String.valueOf(obj[7]));
				sample.setKebel(String.valueOf(obj[8]));
				sample.setSurveyId(Integer.valueOf(String.valueOf(obj[9])));
				sample.setResearchCenter((obj[12] != null) ? String.valueOf(obj[12]) : "NA");
				sample.setStatus(String.valueOf(obj[14]));
				sample.setRaceResult((obj[13] != null) ? String.valueOf(obj[13]) : "NA");
				sample.setRacePublishDate((obj[15] != null) ? String.valueOf(obj[15]) : "NA");
				sample.setRegionId(Integer.valueOf(String.valueOf(obj[17])));
				samples.add(sample);
			}
		} catch (Exception e) {
			LOG.error("RaceReportServiceImpl::reportExcel():" + e);
		}
		return samples;
	}

}
