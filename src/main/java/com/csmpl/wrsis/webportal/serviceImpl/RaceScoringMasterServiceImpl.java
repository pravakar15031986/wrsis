package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.RaceScoringMaster;
import com.csmpl.wrsis.webportal.repository.RaceScoringMasterRepository;
import com.csmpl.wrsis.webportal.service.RaceScoringMasterService;

@Service
public class RaceScoringMasterServiceImpl implements RaceScoringMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(RaceScoringMasterServiceImpl.class);
	@Autowired
	RaceScoringMasterRepository raceScoringRepository;

	@Override
	public String addAndUpdateRaceScroing(RaceScoringMaster resc) {
		String msg = "";

		try {
			if (resc.getRaceScoreId() == 0) {
				resc.setCreatedDate(new Date());
				RaceScoringMaster r = raceScoringRepository.save(resc);
				if (r != null) {
					msg = WrsisPortalConstant.SAVE;
				}
			} else {
				RaceScoringMaster race = raceScoringRepository.getOne(resc.getRaceScoreId());
				resc.setCreatedBy(race.getCreatedBy());
				resc.setCreatedDate(race.getCreatedDate());
				resc.setUpdatedBy(resc.getCreatedBy());
				resc.setUpdatedOn(new Date());
				RaceScoringMaster r = raceScoringRepository.save(resc);
				if (r != null) {
					msg = WrsisPortalConstant.UPDATE;
				}
			}
		} catch (Exception e) {
			LOG.error("RaceScoringMasterServiceImpl::addAndUpdateRaceScoring():" + e);
		}
		return msg;
	}

	@Override
	public List<RaceScoringMaster> viewAllScor() {

		List<RaceScoringMaster> list = new ArrayList<>();
		try {

			List<RaceScoringMaster> race = raceScoringRepository.viewAllRaceScor();
			RaceScoringMaster master = null;
			for (RaceScoringMaster raceMaster : race) {
				master = new RaceScoringMaster();
				master.setRaceScoreId(raceMaster.getRaceScoreId());
				master.setCreatedBy(raceMaster.getCreatedBy());
				master.setCreatedDate(raceMaster.getCreatedDate());
				master.setHlValue(raceMaster.getHlValue().toUpperCase());
				master.setInfectionType(raceMaster.getInfectionType());
				master.setRustId(raceMaster.getRustId());
				master.setStatus(raceMaster.isStatus());
				master.setUpdatedBy(raceMaster.getUpdatedBy());
				master.setUpdatedOn(raceMaster.getUpdatedOn());
				list.add(master);

			}

		} catch (Exception e) {
			LOG.error("RaceScoringMasterServiceImpl::viewallScor():" + e);
		}

		return list;
	}

	@Override
	public RaceScoringMaster getRaceScorById(Integer raceScoreId) {
		RaceScoringMaster res = null;
		try {
			res = raceScoringRepository.findByraceScoreId(raceScoreId);
		} catch (Exception e) {
			LOG.error("RaceScoringMasterServiceImpl::getRaceScorById():" + e);
		}

		return res;
	}

	@Override
	public boolean infectionTypeExits(String infectionType, Integer raceScoreId, Integer rustType) {
		boolean res = false;
		try {

			RaceScoringMaster infeList = raceScoringRepository.validateInfectionType(rustType, infectionType);
			if (raceScoreId != 0) {
				if (raceScoreId == infeList.getRaceScoreId()) {
					res = false;     // Applicable for Update data
				} else {
					res = true;     // Duplicate data
				}
			} else {
				if (infeList != null) {
					res = true;      // Duplicate Data
				} else {
					res = false;
				}
			}

		} catch (Exception e) {
			LOG.error("RaceScoringMasterServiceImpl::infectionTypeExist():" + e);
		}

		return res;
	}

	@Override

	public List<RaceScoringMaster> searchViewAllScor(Integer rustTypeId, String hlId, String status) {

		List<RaceScoringMaster> list = new ArrayList<>();

		List<RaceScoringMaster> raceList = null;
		try {

			if (rustTypeId != null) {
				raceList = raceScoringRepository.searchRaceScorByRustId(rustTypeId);
			}
			if (hlId != null && !"".equalsIgnoreCase(hlId)) {
				raceList = raceScoringRepository.searchRaceScorByHLLevel(hlId);
			}
			if (status != null && !"".equalsIgnoreCase(status)) {
				raceList = raceScoringRepository.searchRaceScorByStatus(Boolean.parseBoolean(status));
			}
			if (rustTypeId != null && !"".equalsIgnoreCase(hlId) && !"".equalsIgnoreCase(status)) {
				raceList = raceScoringRepository.searchRaceScorByRustIdHLLevelStatus(rustTypeId, hlId,
						Boolean.parseBoolean(status));
			}
			if (rustTypeId == null && "".equals(hlId) && "".equals(status)) {
				raceList = raceScoringRepository.viewAllRaceScor();
			}

			RaceScoringMaster master = null;
			for (RaceScoringMaster raceMaster : raceList) {
				master = new RaceScoringMaster();
				master.setRaceScoreId(raceMaster.getRaceScoreId());
				master.setCreatedBy(raceMaster.getCreatedBy());
				master.setCreatedDate(raceMaster.getCreatedDate());
				master.setHlValue(raceMaster.getHlValue().toUpperCase());
				master.setInfectionType(raceMaster.getInfectionType());
				master.setRustId(raceMaster.getRustId());
				master.setStatus(raceMaster.isStatus());
				master.setUpdatedBy(raceMaster.getUpdatedBy());
				master.setUpdatedOn(raceMaster.getUpdatedOn());
				list.add(master);

			}

		} catch (Exception e) {
			LOG.error("RaceScoringMasterServiceImpl::searchViewAllScor():" + e);
		}
		return list;
	}

}
