package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.YellowRacePhenotypeBean;
import com.csmpl.wrsis.webportal.entity.RaceGeneticGroupEntity;
import com.csmpl.wrsis.webportal.entity.YellowRacePhenotypeEntity;

public interface YellowRacePhenotypeService {

	List<RaceGeneticGroupEntity> getGroupList();

	List<YellowRacePhenotypeBean> viewYellowRacePhenotype(SearchVo searchvo);

	String saveYellowracePhenotype(YellowRacePhenotypeEntity yellowRacePhenotype);

	YellowRacePhenotypeEntity getRacePhenotypeId(Integer racePhenotypeId);
}