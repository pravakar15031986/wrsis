package com.csmpl.wrsis.webportal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.RaceChartHelperBean;
import com.csmpl.wrsis.webportal.entity.RaceIdentificationChart;

public interface RaceIdentificationChartService {

	String saveAndUpdateRaceChart(RaceIdentificationChart rChart);

	RaceIdentificationChart getByRaceChartId(Integer raceChartId);

	String viewRaceIdentificationChartByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	List<RaceIdentificationChart> viewAllRaceChart();

	ArrayList<RaceChartHelperBean> sequenceISExit(RaceIdentificationChart rChart);

	String searchviewRaceIdentificationChart(String stageName, String status, Integer pageSize, Integer pageNumber,
			Pageable pageable);

	boolean chartNameISExit(String chartName);

}
