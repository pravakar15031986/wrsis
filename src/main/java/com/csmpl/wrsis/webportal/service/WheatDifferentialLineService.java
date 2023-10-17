package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.WheatDifferentialLineBean;
import com.csmpl.wrsis.webportal.entity.SeedSourceEntity;
import com.csmpl.wrsis.webportal.entity.WheatDifferentialLineEntity;

public interface WheatDifferentialLineService {

	List<SeedSourceEntity> retriveSeedSource();

	String addUpdateWheatDiffLine(WheatDifferentialLineEntity wheatDiffLineDetails);

	List<WheatDifferentialLineBean> viewDifferentialLine(SearchVo searchVo);

	WheatDifferentialLineEntity getWheatDiffLine(int wheatDifLineId);

	String getMaxSeqNo(int rustid);

}
