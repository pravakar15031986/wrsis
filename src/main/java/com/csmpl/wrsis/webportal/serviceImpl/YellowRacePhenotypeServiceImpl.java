package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.YellowRacePhenotypeBean;
import com.csmpl.wrsis.webportal.entity.RaceGeneticGroupEntity;
import com.csmpl.wrsis.webportal.entity.YellowRacePhenotypeEntity;
import com.csmpl.wrsis.webportal.repository.RaceGeneticGroupRepository;
import com.csmpl.wrsis.webportal.repository.YellowRacePhenotypeRepository;
import com.csmpl.wrsis.webportal.service.YellowRacePhenotypeService;

@Service
public class YellowRacePhenotypeServiceImpl implements YellowRacePhenotypeService {

	private static final Logger LOG = LoggerFactory.getLogger(YellowRacePhenotypeServiceImpl.class);
	@Autowired
	RaceGeneticGroupRepository raceGeneticGroupRepository;
	@Autowired
	YellowRacePhenotypeRepository yellowRacePhenotypeRepository;

	@Override
	public List<RaceGeneticGroupEntity> getGroupList() {
		
		return raceGeneticGroupRepository.findByStatusOrderByGeneticGroupId(false);
	}

	@Override
	public List<YellowRacePhenotypeBean> viewYellowRacePhenotype(SearchVo searchVo) {
		if (searchVo.getRaceName() == null)
			searchVo.setRaceName("");
		if (searchVo.getStatus() == null)
			searchVo.setStatus("select");
		List<Object[]> yellowRPobj = yellowRacePhenotypeRepository.getYelloeracePhenotype(searchVo.getGenGroupId(),
				searchVo.getRaceName(), searchVo.getStatus());
		List<YellowRacePhenotypeBean> yellowRacePhenotypeDetails = new ArrayList<>();
		YellowRacePhenotypeBean obj = null;
		for (Object[] objs : yellowRPobj) {
			obj = new YellowRacePhenotypeBean();
			obj.setRacePhenotypeId(Short.parseShort(String.valueOf(objs[0])));
			obj.setGeneticGroupId(Short.parseShort(String.valueOf(objs[1])));
			obj.setGeneticGroupName(String.valueOf(objs[2]));
			obj.setRaceName(String.valueOf(objs[3]));
			obj.setVirulencePhenotype(String.valueOf(objs[4]));
			obj.setStatus(Boolean.parseBoolean(String.valueOf(objs[5])));
			yellowRacePhenotypeDetails.add(obj);
		}
		return yellowRacePhenotypeDetails;
	}

	@Override
	public String saveYellowracePhenotype(YellowRacePhenotypeEntity yellowRacePhenotype) {
		String sts = "";
		try {
			if (yellowRacePhenotype.getRacePhenotypeId() == null) {
				List<YellowRacePhenotypeEntity> race = yellowRacePhenotypeRepository
						.findByRaceNameIgnoreCase(yellowRacePhenotype.getRaceName());
				if (!race.isEmpty())
					return "raceexist";
				List<YellowRacePhenotypeEntity> vphen = yellowRacePhenotypeRepository
						.findByVirulencePhenotypeIgnoreCase(yellowRacePhenotype.getVirulencePhenotype());
				if (!vphen.isEmpty())
					return "vphenotypeexists";
				yellowRacePhenotype.setCreatedOn(new Date());
				yellowRacePhenotypeRepository.save(yellowRacePhenotype);
				sts = WrsisPortalConstant.SUCCESS;
			} else {
				List<YellowRacePhenotypeEntity> race = yellowRacePhenotypeRepository
						.getRaceName(yellowRacePhenotype.getRaceName(), yellowRacePhenotype.getRacePhenotypeId());
				if (!race.isEmpty())
					return "raceexist";
				List<YellowRacePhenotypeEntity> vphen = yellowRacePhenotypeRepository.getVPhenotype(
						yellowRacePhenotype.getVirulencePhenotype(), yellowRacePhenotype.getRacePhenotypeId());
				if (!vphen.isEmpty())
					return "vphenotypeexists";
				YellowRacePhenotypeEntity obj = yellowRacePhenotypeRepository
						.getOne(yellowRacePhenotype.getRacePhenotypeId());
				obj.setGeneticGroupId(yellowRacePhenotype.getGeneticGroupId());
				obj.setRaceName(yellowRacePhenotype.getRaceName());
				obj.setVirulencePhenotype(yellowRacePhenotype.getVirulencePhenotype());
				obj.setStatus(yellowRacePhenotype.getStatus());
				obj.setUpdatedBy(yellowRacePhenotype.getUpdatedBy());
				obj.setUpatedOn(new Date());
				yellowRacePhenotypeRepository.save(obj);
				sts = WrsisPortalConstant.UPDATE;
			}
		} catch (Exception e) {

			LOG.error("YellowRacePhenotypeServiceImpl::saveYellowracePhenotype():" + e);
			sts = WrsisPortalConstant.FAILURE;
		}
		return sts;
	}

	@Override
	public YellowRacePhenotypeEntity getRacePhenotypeId(Integer racePhenotypeId) {
		return yellowRacePhenotypeRepository.getOne(racePhenotypeId);
	}

}
