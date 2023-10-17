package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_rust_incident_details")
public class RustIncidentDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_incident_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer incidentDtlId;

	@Column(name = "int_incident_id")
	private Integer incidentId;
	
	@ManyToOne
	@JoinColumn(name = "int_question_id")
	private Question questionId;

	@Column(name = "vch_option_value")
	private String optionValue;

	@Column(name = "bitstatus")
	private Boolean bitStatus;

	public Integer getIncidentDtlId() {
		return incidentDtlId;
	}

	public void setIncidentDtlId(Integer incidentDtlId) {
		this.incidentDtlId = incidentDtlId;
	}

	public Integer getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(Integer incidentId) {
		this.incidentId = incidentId;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public Question getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Question questionId) {
		this.questionId = questionId;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

}
