package com.csmpl.wrsis.datatable;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.RaceAnalysisPendingSamples;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;

public class RaceAnalysisDataTable {

	private List<RaceAnalysisPendingSamples> data;
	private List<SampleTagBean> list;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private Integer draw;
	private Boolean searchable = true;
	private Boolean orderable = true;

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}

	public Boolean getOrderable() {
		return orderable;
	}

	public void setOrderable(Boolean orderable) {
		this.orderable = orderable;
	}

	public List<RaceAnalysisPendingSamples> getData() {
		return data;
	}

	public void setData(List<RaceAnalysisPendingSamples> data) {
		this.data = data;
	}

	public List<SampleTagBean> getList() {
		return list;
	}

	public void setList(List<SampleTagBean> list) {
		this.list = list;
	}

}
