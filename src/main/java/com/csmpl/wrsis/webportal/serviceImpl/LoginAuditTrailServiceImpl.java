package com.csmpl.wrsis.webportal.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.repository.IPTrackingRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.service.LoginAuditTrailService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class LoginAuditTrailServiceImpl implements LoginAuditTrailService {
	private static final Logger LOG = LoggerFactory.getLogger(LoginAuditTrailServiceImpl.class);
	@Autowired
	IPTrackingRepository ipTrackingRepository;

	@Override
	public List<IPTrackBean> viewSucessWebLoginDetails(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName, Integer userId, String logStatus,
			Integer start, Integer length) {
		List<IPTrackBean> beans = new ArrayList<>();

		try {
			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);
				startDate = tdate;

			} else {
				startDate = DateUtil.StringMMMToDate(startDate).toString();
			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);
				endDate = tdate;

			} else {
				endDate = DateUtil.StringMMMToDate(endDate).toString();
			}

			IPTrackBean vo = null;
			Integer count = 0;
			List<Object[]> listDetails = ipTrackingRepository.viewSucessWebLoginDetails(organizationId, roleId,
					researchCenterId, intdesigid, startDate, endDate, fullName, userId, logStatus, start, length);
			if (!listDetails.isEmpty()) {

				for (Object[] dto : listDetails) {
					vo = new IPTrackBean();

					vo.setsNo((start) + (++count));
					vo.setOrganizationName(String.valueOf(dto[0]));
					vo.setUserId(String.valueOf(dto[1]));
					vo.setUserName(String.valueOf(dto[2]));
					vo.setDesignation(String.valueOf(dto[3]));
					vo.setRole(String.valueOf(dto[4]));
					if (dto[5] != null) {
						vo.setResearchcenterName(String.valueOf(dto[5]));
					} else {
						vo.setResearchcenterName(" ");
					}
					vo.setIpAddress(String.valueOf(dto[6]));
					vo.setLoginTime(String.valueOf(dto[7]));
					if ((dto[8]) != null) {
						vo.setLogoutTime(String.valueOf(dto[8]));
					} else {
						vo.setLogoutTime("");
					}
					String status = (String) (dto[9]);
					if ("Y".equals(status)) {
						vo.setStatus("Success");
					} else {
						vo.setStatus("Failure");
					}

					beans.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("LoginAuditTrailServiceImpl::viewSucessWebLoginDetails():" + e);
		}

		return beans;
	}

	@Override
	public Integer viewSucessWebLoginDetailsCount(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName, Integer userId, String logStatus,
			Integer pstart, Integer plength) {

		Integer totalCount = 0;
		try {
			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				startDate = tdate;

			} else {

				startDate = DateUtil.StringMMMToDate(startDate).toString();

			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				endDate = tdate;

			} else {

				endDate = DateUtil.StringMMMToDate(endDate).toString();

			}
			totalCount = ipTrackingRepository.viewSucessWebLoginDetailsCount(organizationId, roleId, researchCenterId,
					intdesigid, startDate, endDate, fullName, userId, logStatus, pstart, plength);
		} catch (Exception e) {
			
			LOG.error("LoginAuditTrailServiceImpl::viewSucessWebLoginDetailsCount():" + e);
		}

		return totalCount;
	}

	@Override
	public List<IPTrackBean> viewSucessMobLoginDetails(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName, Integer userId, String logStatus,
			Integer start, Integer length) {

		List<IPTrackBean> beans = new ArrayList<>();
		try {

			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				startDate = tdate;

			} else {

				startDate = DateUtil.StringMMMToDate(startDate).toString();

			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				endDate = tdate;

			} else {

				endDate = DateUtil.StringMMMToDate(endDate).toString();

			}

			Integer count = 0;

			List<Object[]> listDetails = ipTrackingRepository.viewSucessMobLoginDetails(organizationId, roleId,
					researchCenterId, intdesigid, startDate, endDate, fullName, userId, logStatus, start, length);
			if (!listDetails.isEmpty()) {

				for (Object[] dto : listDetails) {
					IPTrackBean vo = new IPTrackBean();

					vo.setsNo(start + (++count));
					vo.setOrganizationName(String.valueOf(dto[0]));
					vo.setUserId(String.valueOf(dto[1]));
					vo.setUserName(String.valueOf(dto[2]));
					vo.setDesignation(String.valueOf(dto[3]));
					vo.setRole(String.valueOf(dto[4]));
					if (dto[5] != null) {
						vo.setResearchcenterName(String.valueOf(dto[5]));
					} else {
						vo.setResearchcenterName(" ");
					}
					if (dto[6] != null) {
						vo.setIpAddress(String.valueOf(dto[6]));
					} else {
						vo.setIpAddress("");
					}
					if (dto[7] != null) {
						vo.setOsName((String) dto[7]);
					} else {
						vo.setOsName("");
					}
					if (dto[8] != null) {
						vo.setDeviceId((String) dto[8]);
					} else {
						vo.setDeviceId("");
					}
					if (dto[9] != null) {
						vo.setSubscriberId((String) dto[9]);
					} else {
						vo.setSubscriberId("");
					}
					if (dto[10] != null) {
						vo.setImeiNo((String) dto[10]);
					} else {
						vo.setImeiNo("");
					}

					if (dto[11] != null) {
						vo.setLoginTime((String) dto[11]);
					} else {
						vo.setLoginTime("");
					}

					if (dto[12] != null) {
						vo.setLogoutTime((String) dto[12]);
					} else {
						vo.setLogoutTime("");
					}
					String status = (String) dto[13];
					if ("true".equals(status)) {
						vo.setStatus("Success");
					} else {
						vo.setStatus("Failure");
					}

					beans.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("LoginAuditTrailServiceImpl::viewSucessMobLoginDetails():" + e);

		}

		return beans;

	}

	@Override
	public Integer viewSucessMobLoginDetailsCount(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName, Integer userId, String logStatus,
			Integer pstart, Integer plength) {

		Integer totalCount = 0;
		try {
			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				startDate = tdate;

			} else {

				startDate = DateUtil.StringMMMToDate(startDate).toString();

			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				endDate = tdate;

			} else {

				endDate = DateUtil.StringMMMToDate(endDate).toString();

			}
			totalCount = ipTrackingRepository.viewSucessMobLoginDetailsCount(organizationId, roleId, researchCenterId,
					intdesigid, startDate, endDate, fullName, userId, logStatus, pstart, plength);
		} catch (Exception e) {

			LOG.error("LoginAuditTrailServiceImpl::viewSucessMobLoginDetailsCount():" + e);
		}

		return totalCount;

	}

}
