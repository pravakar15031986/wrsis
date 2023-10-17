package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;

public interface SampleTypeService {

	String saveAndUpdateSampleType(SampleTypeMaster sampleTypeMaster);

	SampleTypeMaster getSampleTypeById(Integer sampleId);

	List<SampleTypeMaster> viewAllSample();

	String viewSampleTypeByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	String searchSampleTypeBySampleName(Integer pageSize, Integer pageNumber, Pageable pageable, String rustType,
			String status);

	List<SampleLabTagBean> viewExtTaggedSamples(SearchVo searchVo);

	SampleLabTagBean getSampleDetails(int sampleLabTagId);

	String updtExtTaggedSample(SampleLabTagDetails extTaggedSampledetails);

	List<SampleLabTagBean> viewExtRaceResult(SearchVo searchVo);

	List<SampleLabTagBean> viewdumpedSamples(SearchVo searchVo);

	List<DemographicEntity> viewRegionList(int rcId);

	List<TypeOfRust> getRustList(int rcId);

	Integer viewdumpedSamplesCount(SearchVo searchVo);

	Integer viewExtTaggedSamplesCount(SearchVo searchVo);

	Integer viewExtRaceResultCount(SearchVo searchVo);
}
