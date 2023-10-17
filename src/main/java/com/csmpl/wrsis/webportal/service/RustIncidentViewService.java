package com.csmpl.wrsis.webportal.service;

import java.util.Date;
import java.util.List;

import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;

public interface RustIncidentViewService {
	List<RustIncidentEntityBean> viewALLRustIncident();

	String viewIncidentDetailsById(Integer incidentId);

	List<RustIncidentEntityBean> viewALLRustIncident(Date startDate, Date endDate);

	List<RustIncidentEntityBean> viewALLRustIncidentByUserId(String startDate, String endDate, int userId);

	List<RustIncidentEntityBean> viewRustIncidentDetails(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, Integer yearId, Integer seasionId, String startDate, String endDate, Integer pstart,
			Integer pLength);
}
