package com.csmpl.wrsis.webportal.datatable;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;

public class IvrSurveyDataTable {

	private Integer start;
	private Integer length;
	private String order;
	private String search;
	List<IvrDataReportBean> data;
	private Integer recordsFiltered;
	private Integer recordsTotal;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<IvrDataReportBean> getData() {
		return data;
	}

	public void setData(List<IvrDataReportBean> data) {
		this.data = data;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

}
