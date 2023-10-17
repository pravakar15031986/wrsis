package com.csmpl.wrsis.webportal.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.RustIncidentEntity;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentDetailsRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentRepository;
import com.csmpl.wrsis.webportal.service.RustIncidentViewService;

@Service
public class RustIncidentViewServiceImp implements RustIncidentViewService {

	private static final Logger LOG = LoggerFactory.getLogger(RustIncidentViewServiceImp.class);
	@Autowired
	RustIncidentRepository rustIncidentRepository;
	@Autowired
	RustIncidentDetailsRepository rustIncidentDetailsRepository;
	@Autowired
	DemographicRepository demographicRepository;

	@Override
	public List<RustIncidentEntityBean> viewALLRustIncident() {
		List<RustIncidentEntityBean> beanList = new ArrayList<>();
		List<RustIncidentEntity> inciList = null;
		try {
			RustIncidentEntityBean bean = null;
			inciList = rustIncidentRepository.viewAllIncident();
			for (RustIncidentEntity incident : inciList) {
				bean = new RustIncidentEntityBean();
				DemographicEntity demo = demographicRepository
						.findByparentIdAndStatu(incident.getDemographyId().getDemographyId());
				LOG.info(demo.getDemographyId() + "\t" + demo.getDemographyName());
				if (demo.getLevelId() == 5) {
					bean.setKebeleName(demo.getDemographyName());
					DemographicEntity woreda = demographicRepository.findByparentIdAndStatu(demo.getParentId());
					LOG.info(woreda.getDemographyId() + "\t" + woreda.getDemographyName());
					bean.setWoredaName(woreda.getDemographyName());
				} else {
					bean.setWoredaName(demo.getDemographyName());
					bean.setKebeleName("-NA-");
				}
				bean.setIncidentId(incident.getIncidentId());
				bean.setRustDate(incident.getRustDate());
				bean.setSeasonId(incident.getSeasonId());
				bean.setLongitude(incident.getLongitude());
				bean.setLatitude(incident.getLatitude());
				bean.setYear(incident.getYear());
				beanList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("RustIncidentViewServiceImp::viewAllRustincident():" + e);
		}
		return beanList;
	}

	@Override
	public String viewIncidentDetailsById(Integer incidentId) {
		JSONObject jsonObject = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			List<Object[]> detailsList = rustIncidentDetailsRepository.viewIncidentDetailsByIncidentId(incidentId);
			if (detailsList != null) {
				JSONObject job = null;
				for (Object[] detailsEntity : detailsList) {
					job = new JSONObject();
					job.put("quust_val", detailsEntity[6].toString());
					job.put("option_val", detailsEntity[5].toString());
					arr.put(job);
				}

			}
			jsonObject.put("dtlArr", arr);
		} catch (Exception e) {
			LOG.error("RustIncidentViewServiceImp::viewIncidentDetailsById():" + e);
		}
		return jsonObject.toString();
	}

	@Override
	
	public List<RustIncidentEntityBean> viewALLRustIncident(Date startDate, Date endDate) {
		List<RustIncidentEntityBean> beanList = new ArrayList<>();
		List<RustIncidentEntity> inciList = null;
		try {
			RustIncidentEntityBean bean = null;

			
			if (startDate != null && endDate != null) {
				
				
				inciList = rustIncidentRepository.findByRustDate(startDate, endDate);
			}

			for (RustIncidentEntity incident : inciList) {
				bean = new RustIncidentEntityBean();
				// DemographicEntity
				
				DemographicEntity demo = demographicRepository
						.findByparentIdAndStatu(incident.getDemographyId().getDemographyId());
				LOG.info(demo.getDemographyId() + "\t" + demo.getDemographyName());
				if (demo.getLevelId() == 5) {
					bean.setKebeleName(demo.getDemographyName());
					DemographicEntity woreda = demographicRepository.findByparentIdAndStatu(demo.getParentId());
					LOG.info(woreda.getDemographyId() + "\t" + woreda.getDemographyName());
					bean.setWoredaName(woreda.getDemographyName());
				} else {
					bean.setWoredaName(demo.getDemographyName());
					bean.setKebeleName("-NA-");
				}
				bean.setIncidentId(incident.getIncidentId());

				bean.setRustDate(incident.getRustDate());
				
				bean.setSeasonId(incident.getSeasonId());
				bean.setLongitude(incident.getLongitude());
				bean.setLatitude(incident.getLatitude());
				bean.setYear(incident.getYear());
				beanList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("RustIncidentViewServiceImp::viewALLRustIncident():" + e);
		}
		return beanList;
	}

	@Override
	public List<RustIncidentEntityBean> viewALLRustIncidentByUserId(String startDate, String endDate, int userId) {
		List<RustIncidentEntityBean> beanList = new ArrayList<>();
		List<Object[]> inciList = null;
		if (startDate == null)
			startDate = "";
		if (endDate == null)
			endDate = "";
		try {
			RustIncidentEntityBean bean = null;
			inciList = rustIncidentRepository.findByRustDateAndUserId(startDate, endDate, userId);
			for (Object[] incident : inciList) {
				bean = new RustIncidentEntityBean();
				bean.setIncidentId(Integer.parseInt(String.valueOf(incident[0])));
				bean.setIncidentDate(String.valueOf(incident[1]));
				bean.setWoredaName(String.valueOf(incident[11]));
				bean.setKebeleName(String.valueOf(incident[3]));
				bean.setSeasonName(String.valueOf(incident[8]));
				bean.setLongitude(String.valueOf(incident[9]));
				bean.setLatitude(String.valueOf(incident[10]));
				beanList.add(bean);
			}
		} catch (Exception e) {
			LOG.error("RustIncidentViewServiceImp::viewALLRustIncidentByUserId():" + e);

		}
		return beanList;
	}

	@Override
	public List<RustIncidentEntityBean> viewRustIncidentDetails(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, Integer yearId, Integer seasionId, String startDate, String endDate, Integer pstart,
			Integer pLength) {
		List<RustIncidentEntityBean> beanList = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> inciList = rustIncidentRepository.getRustIncidendDetails(regionId, zoneId, woredaId,
					kebeleId, yearId, seasionId, sDate, eDate, pstart, pLength);
			Integer count = 0;
			for (Object[] obj : inciList) {
				RustIncidentEntityBean objReport = new RustIncidentEntityBean();
				objReport.setsNo((pstart) + (++count));
				objReport.setIncidendDate(String.valueOf(obj[0]));
				objReport.setRegionName(String.valueOf(obj[1]));
				objReport.setZoneName(String.valueOf(obj[2]));
				objReport.setWoredaName(String.valueOf(obj[3]));
				objReport.setKebeleName(String.valueOf(obj[4]));
				objReport.setYear(String.valueOf(obj[5]));
				objReport.setSeasonName(String.valueOf(obj[6]));
				objReport.setLongitude(String.valueOf(obj[7]));
				objReport.setLatitude(String.valueOf(obj[8]));
				objReport.setQueostions(String.valueOf(obj[9]));
				objReport.setUserFullName(String.valueOf(obj[10]));
				objReport.setIncidentId(Integer.parseInt(String.valueOf(obj[11])));
				objReport.setModalView(
						" <a href data-toggle='modal' data-target='#myModal' class=\"fa fa-fw fa-eye-slash pass-i-tooltip\"\r\n"
								+ "onclick=\"optionValue(" + objReport.getIncidentId() + ")\"></a>");
				beanList.add(objReport);
			}

		} catch (Exception e) {
			LOG.error("RustIncidentViewServiceImp::viewRustIncidentDetails():" + e);

		}
		return beanList;
	}
}
