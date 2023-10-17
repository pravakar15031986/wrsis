package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.SeasonMonthBean;

public interface SeasonMasterService {

	String saveAndUpdateSeasonMaster(SeasonMonthBean seasonMonthBean);

	int getmaxSeasonId();

	List<SeasonMonthBean> viewAllSeason();

	SeasonMonthBean getSeasonById(Integer seasonIdBean);

	List<SeasonMonthBean> getMonthBySeasonId(Integer seasonIdBean);

	String viewSeasonByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	String searchseasonPage(String name, String monthId, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable);

	String searchSeasonByMonthIdPage(String monthId, Integer pageSize, Integer pageNumber, Pageable pageable);

	boolean isExistSeasoName(Integer seasionId, String seasonname);

	boolean duplicateExistSeasoMonth(Integer seasionId, String[] monthId);
}
