package com.csmpl.wrsis.webportal.report.domains;

import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "t_wr_sample_lab_tag_details")
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "FetchRaceReportResult", procedureName = "wrsis.sp_wr_race_reports", resultClasses = RaceReportDomain.class, parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "vchAction", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "userid", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "rustTypeId", type = Integer.class)

		}) })

public class RaceReportDomain {

	public static final Logger LOG = LoggerFactory.getLogger(RaceReportDomain.class);

	@Transient
	private String userId;
	@Id
	private String jsonData;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	@Override
	public String toString() {
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try {
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex) {

			jsonStr = ex.toString();
			LOG.error("RaceReportDomain::toString():" + ex.getMessage());
		}
		return jsonStr;
	}

}
