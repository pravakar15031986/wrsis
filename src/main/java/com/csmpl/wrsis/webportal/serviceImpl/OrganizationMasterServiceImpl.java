package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.OrganizationMasterBean;
import com.csmpl.wrsis.webportal.entity.OrganizationMasterEntity;
import com.csmpl.wrsis.webportal.repository.OrganizationMasterRepository;
import com.csmpl.wrsis.webportal.service.OrganizationMasterService;

@Service
public class OrganizationMasterServiceImpl implements OrganizationMasterService {

	private static final Logger LOG = LoggerFactory.getLogger(OrganizationMasterServiceImpl.class);
	@Autowired
	OrganizationMasterRepository organizationMasterRepository;

	@Override
	public String saveAndUpdate(OrganizationMasterBean orgDetails) {
		String str = WrsisPortalConstant.FAILURE;
		OrganizationMasterEntity orgObj = new OrganizationMasterEntity();
		try {

			if (orgDetails.getIntleveldetailid() == 0) {
				List<Object[]> orgsNames = organizationMasterRepository.getOrgNames(orgDetails.getNvchlevelname());
				if (!orgsNames.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				orgObj.setNvchlevelname(orgDetails.getNvchlevelname().toUpperCase());
				orgObj.setVchalias(orgDetails.getVchalias());
				orgObj.setDtmcreatedon(new Date());
				orgObj.setIntcreatedby(orgDetails.getIntcreatedby());
				orgObj.setIntparentid(orgDetails.getMinistryId());
				orgObj.setIntlevelid(3);
				orgObj.setInthierarchyid(0);
				orgObj.setBitstatus(Boolean.valueOf(orgDetails.getBitstatus()));
				organizationMasterRepository.save(orgObj);
				str = WrsisPortalConstant.SAVE;
			} else {
				List<Object[]> orgsNames = organizationMasterRepository.getOrgNames(orgDetails.getIntleveldetailid(),
						orgDetails.getNvchlevelname());
				if (!orgsNames.isEmpty()) {
					return WrsisPortalConstant.EXIST;
				}
				orgObj = organizationMasterRepository.findByIntleveldetailid(orgDetails.getIntleveldetailid());
				if (orgObj != null) {
					orgObj.setNvchlevelname(orgDetails.getNvchlevelname().toUpperCase());
					orgObj.setVchalias(orgDetails.getVchalias());
					orgObj.setDtmupdatedon(new Date());
					orgObj.setIntupdatedby(orgDetails.getIntcreatedby());
					orgObj.setIntparentid(orgDetails.getMinistryId());
					orgObj.setIntlevelid(3);
					orgObj.setInthierarchyid(0);
					orgObj.setBitstatus(Boolean.valueOf(orgDetails.getBitstatus()));
					organizationMasterRepository.save(orgObj);
					str = WrsisPortalConstant.UPDATE;
				}
			}
		} catch (Exception e) {
			LOG.error("OrganizationMasterServiceImpl::saveAndUpdate():" + e);
		}

		return str;
	}

	@Override
	public List<OrganizationMasterBean> viewAllOrganisation() {
		List<OrganizationMasterBean> list = new ArrayList<>();
		OrganizationMasterBean vo = null;
		try {
			List<Object[]> objList = organizationMasterRepository.getOrganisationList();
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new OrganizationMasterBean();
					vo.setCouyntry(String.valueOf(obj[0]));
					vo.setMinistry(String.valueOf(obj[1]));
					vo.setOrganisation(String.valueOf(obj[2]));
					vo.setVchalias(String.valueOf(obj[3]));
					vo.setStatus(String.valueOf(obj[4]));
					vo.setIntleveldetailid((Short) obj[5]);
					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("OrganizationMasterServiceImpl::viewAllOrganisation():" + e);
		}

		return list;
	}

	@Override
	public OrganizationMasterBean editOrganization(OrganizationMasterBean organization) {
		OrganizationMasterBean vo = null;
		try {
			List<Object[]> objList = organizationMasterRepository.editOrganization(organization.getIntleveldetailid());
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new OrganizationMasterBean();
					vo.setCountryId((Short) (obj[0]));
					
					vo.setNvchlevelname((String.valueOf(obj[1])));
					vo.setVchalias(String.valueOf(obj[2]));
					vo.setBitstatus(String.valueOf(obj[3]));
					vo.setIntleveldetailid((Short) (obj[4]));

				}
			}
		} catch (Exception e) {
			LOG.error("OrganizationMasterServiceImpl::editOrganisation():" + e);
		}
		return vo;
	}

	

}
