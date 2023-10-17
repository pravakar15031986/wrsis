package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.WheatDifferentialLineBean;
import com.csmpl.wrsis.webportal.entity.SeedSourceEntity;
import com.csmpl.wrsis.webportal.entity.WheatDifferentialLineEntity;
import com.csmpl.wrsis.webportal.repository.RaceGroupRepository;
import com.csmpl.wrsis.webportal.repository.SeedSourceRepository;
import com.csmpl.wrsis.webportal.repository.WheatDifferentialLineRepository;
import com.csmpl.wrsis.webportal.service.WheatDifferentialLineService;

@Service
public class WheatDifferentialLineServiceImpl implements WheatDifferentialLineService {

	public static final Logger LOG = LoggerFactory.getLogger(WheatDifferentialLineServiceImpl.class);

	@Autowired
	SeedSourceRepository seedSourceRepository;

	@Autowired
	WheatDifferentialLineRepository wheatDifferentialLineRepository;

	@Autowired
	RaceGroupRepository raceGroupRepository;

	@Override
	public List<SeedSourceEntity> retriveSeedSource() {
		return seedSourceRepository.retriveAllSeedSrcByStatus();
	}

	@Override
	public String addUpdateWheatDiffLine(WheatDifferentialLineEntity wheatDiffLineDetails) {
		String sts = "";
		try {
			int rustmaxvalue = raceGroupRepository.getRustMaxValue(wheatDiffLineDetails.getRustTypeId());
			if (wheatDiffLineDetails.getWheatDifLineId() == 0) {
				WheatDifferentialLineEntity wObj = new WheatDifferentialLineEntity();
				List<Object[]> wheatDiffLineExistName = wheatDifferentialLineRepository
						.retriveDiffLineExistName(wheatDiffLineDetails.getDifLine());
				if (!wheatDiffLineExistName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				if (wheatDiffLineDetails.getSeqNo() != 0) {
					List<Object[]> rustSeq = wheatDifferentialLineRepository
							.retriveRustSeq(wheatDiffLineDetails.getRustTypeId(), wheatDiffLineDetails.getSeqNo());
					if (!rustSeq.isEmpty()) {
						return "seqexists";
					}
				}

				if (wheatDiffLineDetails.getFirstLine() != 1 && !wheatDiffLineDetails.getStatus()
						&& wheatDiffLineDetails.getRustTypeId() != 3) {
					List<Object[]> wheatDiffLine = wheatDifferentialLineRepository
							.retrieveWheatDiffLineForRustTypeInSet(wheatDiffLineDetails.getRustTypeId(),
									wheatDiffLineDetails.getDiffSetNo());
					if (wheatDiffLine.size() < rustmaxvalue) {
						wObj.setRustTypeId(wheatDiffLineDetails.getRustTypeId());
						wObj.setFirstLine(wheatDiffLineDetails.getFirstLine());
						wObj.setDifLine(wheatDiffLineDetails.getDifLine());
						wObj.setDiffSetNo(wheatDiffLineDetails.getDiffSetNo());
						wObj.setSeqNo(wheatDiffLineDetails.getSeqNo());
						wObj.setSeedSrcId(wheatDiffLineDetails.getSeedSrcId());
						wObj.setGene(wheatDiffLineDetails.getGene());
						wObj.setExpectLowIt(wheatDiffLineDetails.getExpectLowIt());
						wObj.setRacePhenotype(wheatDiffLineDetails.getRacePhenotype());
						wObj.setDesc(wheatDiffLineDetails.getDesc());
						wObj.setStatus(wheatDiffLineDetails.getStatus());
						wObj.setCreatedBy(wheatDiffLineDetails.getCreatedBy());
						wObj.setCreatedOn(new Date());
						wheatDifferentialLineRepository.save(wObj);
						sts = WrsisPortalConstant.SUCCESS;
					} else {
						return "diffExists";
					}
				} else {
					wObj.setRustTypeId(wheatDiffLineDetails.getRustTypeId());
					wObj.setFirstLine(wheatDiffLineDetails.getFirstLine());
					wObj.setDifLine(wheatDiffLineDetails.getDifLine());
					wObj.setDiffSetNo(wheatDiffLineDetails.getDiffSetNo());
					wObj.setSeqNo(wheatDiffLineDetails.getSeqNo());
					wObj.setSeedSrcId(wheatDiffLineDetails.getSeedSrcId());
					wObj.setGene(wheatDiffLineDetails.getGene());
					wObj.setExpectLowIt(wheatDiffLineDetails.getExpectLowIt());
					wObj.setRacePhenotype(wheatDiffLineDetails.getRacePhenotype());
					wObj.setDesc(wheatDiffLineDetails.getDesc());
					wObj.setStatus(wheatDiffLineDetails.getStatus());
					wObj.setCreatedBy(wheatDiffLineDetails.getCreatedBy());
					wObj.setCreatedOn(new Date());
					wheatDifferentialLineRepository.save(wObj);
					sts = WrsisPortalConstant.SUCCESS;
				}
			} else {
				WheatDifferentialLineEntity wObj = wheatDifferentialLineRepository
						.findByWheatDifLineId(wheatDiffLineDetails.getWheatDifLineId());
				List<Object[]> wheatDiffLineExistName = wheatDifferentialLineRepository.retriveDiffLineExistName(
						wheatDiffLineDetails.getWheatDifLineId(), wheatDiffLineDetails.getDifLine());
				if (!wheatDiffLineExistName.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				if (wheatDiffLineDetails.getSeqNo() != 0) {
					List<Object[]> rustSeq = wheatDifferentialLineRepository.retriveRustSeq(
							wheatDiffLineDetails.getWheatDifLineId(), wheatDiffLineDetails.getRustTypeId(),
							wheatDiffLineDetails.getSeqNo());
					if (!rustSeq.isEmpty()) {
						return "seqexists";
					}
				}
				if (wheatDiffLineDetails.getFirstLine() != 1 && !wheatDiffLineDetails.getStatus()
						&& wheatDiffLineDetails.getRustTypeId() != 3) {
					List<Object[]> wheatDiffLine = wheatDifferentialLineRepository
							.retrieveWheatDiffLineForRustTypeInSet(wheatDiffLineDetails.getRustTypeId(),
									wheatDiffLineDetails.getDiffSetNo(), wheatDiffLineDetails.getWheatDifLineId());
					if (wheatDiffLine.size() < rustmaxvalue) {
						wObj.setRustTypeId(wheatDiffLineDetails.getRustTypeId());
						wObj.setFirstLine(wheatDiffLineDetails.getFirstLine());
						wObj.setDifLine(wheatDiffLineDetails.getDifLine());
						wObj.setDiffSetNo(wheatDiffLineDetails.getDiffSetNo());
						wObj.setSeqNo(wheatDiffLineDetails.getSeqNo());
						wObj.setSeedSrcId(wheatDiffLineDetails.getSeedSrcId());
						wObj.setGene(wheatDiffLineDetails.getGene());
						wObj.setExpectLowIt(wheatDiffLineDetails.getExpectLowIt());
						wObj.setRacePhenotype(wheatDiffLineDetails.getRacePhenotype());
						wObj.setDesc(wheatDiffLineDetails.getDesc());
						wObj.setStatus(wheatDiffLineDetails.getStatus());
						wObj.setUpdatedBy(wheatDiffLineDetails.getUpdatedBy());
						wObj.setUpdatedOn(new Date());
						wheatDifferentialLineRepository.save(wObj);
						sts = WrsisPortalConstant.UPDATE;
					} else {
						return "diffExists";
					}
				} else {
					wObj.setRustTypeId(wheatDiffLineDetails.getRustTypeId());
					wObj.setFirstLine(wheatDiffLineDetails.getFirstLine());
					wObj.setDifLine(wheatDiffLineDetails.getDifLine());
					wObj.setDiffSetNo(wheatDiffLineDetails.getDiffSetNo());
					wObj.setSeqNo(wheatDiffLineDetails.getSeqNo());
					wObj.setSeedSrcId(wheatDiffLineDetails.getSeedSrcId());
					wObj.setGene(wheatDiffLineDetails.getGene());
					wObj.setExpectLowIt(wheatDiffLineDetails.getExpectLowIt());
					wObj.setRacePhenotype(wheatDiffLineDetails.getRacePhenotype());
					wObj.setDesc(wheatDiffLineDetails.getDesc());
					wObj.setStatus(wheatDiffLineDetails.getStatus());
					wObj.setUpdatedBy(wheatDiffLineDetails.getUpdatedBy());
					wObj.setUpdatedOn(new Date());
					wheatDifferentialLineRepository.save(wObj);
					sts = WrsisPortalConstant.UPDATE;
				}
			}
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineServiceImpl::addUpdateWheatDiffLine():" + e);
			sts = WrsisPortalConstant.FAILURE;
		}
		return sts;
	}

	@Override
	public List<WheatDifferentialLineBean> viewDifferentialLine(SearchVo searchVo) {
		List<WheatDifferentialLineBean> wheatDL = new ArrayList<>();
		WheatDifferentialLineBean wobj = null;
		try {

			List<Object[]> wheatDLList = wheatDifferentialLineRepository.retriveWheatDLDetails(searchVo.getRustTypeId(),
					searchVo.getDiffSetNo(), searchVo.getIsFirstDiffLine(), searchVo.getDeleteFlag());
			for (Object[] objs : wheatDLList) {
				wobj = new WheatDifferentialLineBean();
				String wheatDiffLineId = (String) objs[0];
				wobj.setWheatDifLineId(Short.parseShort(wheatDiffLineId));
				String rustTypeid = (String) objs[1];
				wobj.setRustTypeId(Short.parseShort(rustTypeid));
				wobj.setRustTypeName(String.valueOf(objs[2]));
				wobj.setDifLine(String.valueOf(objs[3]));
				String diffSetNo = (String) objs[4];
				wobj.setDiffSetNo(Short.parseShort(diffSetNo));
				if (objs[5] != null)
					wobj.setSeedSourceName(String.valueOf(objs[5]));
				wobj.setGene(String.valueOf(objs[6]));
				wobj.setExpectLowIt(String.valueOf(objs[7]));
				if (objs[8] != null)
					wobj.setDesc(String.valueOf(objs[8]));
				String firstDiffLine = (String) objs[9];
				wobj.setFirstLine(Short.parseShort(firstDiffLine));
				
				wobj.setStatus((Boolean) objs[10]);
				wobj.setSeqNo(String.valueOf(objs[11]));
				if (objs[12] != null)
					wobj.setRacephenotype(String.valueOf(objs[12]));
				wheatDL.add(wobj);
			}
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineServiceImpl::viewDifferentialLine():" + e);

		}
		return wheatDL;
	}

	@Override
	public WheatDifferentialLineEntity getWheatDiffLine(int wheatDifLineId) {
		return wheatDifferentialLineRepository.findByWheatDifLineId(wheatDifLineId);
	}

	@Override
	public String getMaxSeqNo(int rustid) {
		JSONObject jObject = new JSONObject();
		try {
			int maxno = wheatDifferentialLineRepository.getMaxSeqNoByRustTypeId(rustid);
			jObject.put("no", String.valueOf(maxno + 1));
		} catch (Exception e) {
			LOG.error("WheatDifferentialLineServiceImpl::getWheatDiffLine():" + e);

		}
		return jObject.toString();
	}

}
