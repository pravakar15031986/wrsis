package com.csmpl.wrsis.datatable;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.RecommendationBean;

public class MonitorRecommendationDataTable {

	private List<RecommendationBean> data;

	private Integer recordsTotal;
	private Integer recordsFiltered;
	private Integer draw;
	private Boolean searchable = true;
	private Boolean orderable = true;

	public List<RecommendationBean> getData() {
		return data;
	}

	public void setData(List<RecommendationBean> data) {
		this.data = data;
	}

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

}
