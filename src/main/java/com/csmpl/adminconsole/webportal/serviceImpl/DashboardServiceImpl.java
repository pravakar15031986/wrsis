/**
 * 
 */
package com.csmpl.adminconsole.webportal.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SeasonMonth;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMonthRepository;

@Service("dashboardservice")
public class DashboardServiceImpl implements DashboardService {

	public static final Logger LOG = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SeasionMasterRepository seasionMasterRepository;
	@Autowired
	SeasonMonthRepository seasonMonthRepository;

	@Override
	public JSONObject getDashboardContent(Integer userId) {

		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_dashboard_content(?)";
			query = "select * from wrsis.sp_wr_dashboard_transaction_count(?)"; //Modified as on 04-09-2020
			psmt = con.prepareStatement(query);
			psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			if (rs.next()) {
				response = new JSONObject(rs.getString(WrsisPortalConstant.DASHBOARD_JSON));

			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getDashboardContent():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getDashboardContent():" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardContent():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardContent():" + e);
			}
		}

		return response;
	}

	@Override
	public List<UserBean> fetchSurveyorList(String mobile, String email, String name, String rcId, String desigId,
			Integer start, Integer length) {
		if (rcId == null)
			rcId="";
		if (desigId == null)
			desigId="";
		if (mobile == null)
			mobile="";
		if (email == null)
			email="";
		if (name == null)
			name="";
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchSurveyorList(mobile,email,
					name, rcId, desigId,start,length);
			int count=0;
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setsNo(start + (++count));
				obj.setOrganizationName((objs[0] != null) ? String.valueOf(objs[0]) : "NA");
				obj.setUserName(String.valueOf(objs[10]));
				obj.setFullName(String.valueOf(objs[1]));
				if(Integer.parseInt(String.valueOf(objs[2]))==1)
					obj.setGendr(WrsisPortalConstant.MALE);
				if(Integer.parseInt(String.valueOf(objs[2]))==2)
					obj.setGendr(WrsisPortalConstant.FEMALE);
				if(Integer.parseInt(String.valueOf(objs[2]))==0)
					obj.setGendr("NA");
				obj.setDesignation(String.valueOf(objs[3]));
				obj.setRoleName(String.valueOf(objs[5]));
				obj.setMobile(String.valueOf(objs[7]));
				obj.setEmail(String.valueOf(objs[8]));
				obj.setUserId(Integer.parseInt(objs[4].toString()));
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::fetchSurveyorList():" + e);
		}
		return userList;
	}

	@Override
	public List<UserBean> fetchPathologistList(SearchVo searchVo) {
		if (searchVo.getCenter() == null)
			searchVo.setCenter("");
		if (searchVo.getDesn() == null)
			searchVo.setDesn("");
		if (searchVo.getMobileno() == null)
			searchVo.setMobileno("");
		if (searchVo.getEmail() == null)
			searchVo.setEmail("");
		if (searchVo.getFullname() == null)
			searchVo.setFullname("");
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchPathologistList(searchVo.getMobileno(), searchVo.getEmail(),
					searchVo.getFullname(), searchVo.getCenter(), searchVo.getDesn());
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setOrganizationName((objs[0] != null) ? String.valueOf(objs[0]) : "NA");
				obj.setFullName(String.valueOf(objs[1]));
				obj.setUserName(String.valueOf(objs[10]));
				obj.setGender(Integer.parseInt(objs[2].toString()));
				obj.setDesignation(String.valueOf(objs[3]));
				obj.setUserId(Integer.parseInt(objs[4].toString()));
				obj.setRoleName(String.valueOf(objs[5]));
				obj.setMobile(String.valueOf(objs[7]));
				obj.setEmail(String.valueOf(objs[8]));
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::fetchPathologistList():" + e);
		}
		return userList;
	}

	@Override
	public List<UserBean> fetchWoredaExpertList(SearchVo searchVo) {
		if (searchVo.getCenter() == null)
			searchVo.setCenter("");
		if (searchVo.getDesn() == null)
			searchVo.setDesn("");
		if (searchVo.getMobileno() == null)
			searchVo.setMobileno("");
		if (searchVo.getEmail() == null)
			searchVo.setEmail("");
		if (searchVo.getFullname() == null)
			searchVo.setFullname("");
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchWoredaExpertList(searchVo.getMobileno(), searchVo.getEmail(),
					searchVo.getFullname(), searchVo.getDesn());
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setFullName(String.valueOf(objs[0]));
				obj.setUserName(String.valueOf(objs[9]));
				obj.setGender(Integer.parseInt(objs[1].toString()));
				obj.setDesignation(String.valueOf(objs[2]));
				obj.setUserId(Integer.parseInt(objs[3].toString()));
				obj.setRoleName(String.valueOf(objs[4]));
				obj.setMobile(String.valueOf(objs[6]));
				obj.setEmail(String.valueOf(objs[7]));
				obj.setDemoId(String.valueOf(objs[10]));
				obj.setDemoName(String.valueOf(objs[11]));
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::fetchWoredaExpertList():" + e);
		}
		return userList;
	}

	@Override
	public List<UserBean> fetchDevAgentList(SearchVo searchVo) {
		if (searchVo.getCenter() == null)
			searchVo.setCenter("");
		if (searchVo.getDesn() == null)
			searchVo.setDesn("");
		if (searchVo.getMobileno() == null)
			searchVo.setMobileno("");
		if (searchVo.getEmail() == null)
			searchVo.setEmail("");
		if (searchVo.getFullname() == null)
			searchVo.setFullname("");
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchDevAgentList(searchVo.getMobileno(), searchVo.getEmail(),
					searchVo.getFullname(), searchVo.getDesn());
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setFullName(String.valueOf(objs[0]));
				obj.setUserName(String.valueOf(objs[9]));
				obj.setGender(Integer.parseInt(objs[1].toString()));
				obj.setDesignation(String.valueOf(objs[2]));
				obj.setUserId(Integer.parseInt(objs[3].toString()));
				obj.setRoleName(String.valueOf(objs[4]));
				obj.setMobile(String.valueOf(objs[6]));
				obj.setEmail(String.valueOf(objs[7]));
				obj.setDemoId(String.valueOf(objs[10]));
				obj.setDemoName((objs[11] != null) ? String.valueOf(objs[11]) : "NA");
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::fetchDevAgentList():" + e);
		}
		return userList;
	}

	@Override
	public List<SeasionMasterEntity> getActiveSeasonList() {
		
		return seasionMasterRepository.getActiveSeasonList();
	}

	@Override
	public JSONArray getRustReport(SearchVo searchVo) throws SQLException {
		JSONArray response = new JSONArray();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			if (searchVo.getCurrentYear() == null)
				searchVo.setCurrentYear("current");
			if (searchVo.getYear() == null)
				searchVo.setYear("");
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_rust_type_chart_report(?,?,?)";
			psmt = con.prepareStatement(query);
			psmt.setString(1, searchVo.getCurrentYear());
			psmt.setString(2, searchVo.getYear());
			psmt.setInt(3, searchVo.getSeasonId());
			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(WrsisPortalConstant.RUST_REPORT_JSON) == null)
					return response;
				response = new JSONArray(rs.getString(WrsisPortalConstant.RUST_REPORT_JSON));

			}
			if (searchVo.getYear() == "") {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Integer currentYear = year;
				searchVo.setYear(currentYear.toString());
			}
			if (searchVo.getSeasonId() == 0) {
				Date date = new Date();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int month = localDate.getMonthValue();
				SeasonMonth seasonId = seasonMonthRepository.findSeasonIdByMonthId(month);
				searchVo.setSeasonId(seasonId.getSeasonId());
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustReport():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustReport():" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReport():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReport():" + e);
			}
		}
		return response;
	}

	@Override
	public JSONArray getRustReportByRegion(SearchVo searchVo) throws SQLException {
		JSONArray jsa = new JSONArray();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		JSONArray finalJsonData = new JSONArray();
		try {
			if (searchVo.getCurrentYear() == null)
				searchVo.setCurrentYear("current");
			if (searchVo.getYear() == null)
				searchVo.setYear("");
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_region_vs_rust_chart_report(?,?,?)";
			psmt = con.prepareStatement(query);

			psmt.setString(1, searchVo.getCurrentYear());

			psmt.setString(2, searchVo.getYear());

			psmt.setInt(3, searchVo.getSeasonId());
			rs = psmt.executeQuery();
			if (rs.next()) {
				jsa = new JSONArray(rs.getString(WrsisPortalConstant.RUST_REPORT_JSON));
			}
			
			JSONArray finalJsa = new JSONArray();
			Set<String> rustTypes = new HashSet<>();
			Set<String> regions = new HashSet<>();
			
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject json = new JSONObject();
				json = jsa.getJSONObject(i);
				JSONArray details = new JSONArray();

				if (json.get(WrsisPortalConstant.RUST_WISE_DETAILS) == null || !json.has(WrsisPortalConstant.RUST_WISE_DETAILS)
						|| !(json.get(WrsisPortalConstant.RUST_WISE_DETAILS) instanceof JSONArray)) {
					return finalJsonData;
				}
				details = json.getJSONArray(WrsisPortalConstant.RUST_WISE_DETAILS);
				for (int j = 0; j < details.length(); j++) {
					rustTypes.add(details.getJSONObject(j).getString("rustname"));
				}
			}
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject json = new JSONObject();
				json = jsa.getJSONObject(i);
				JSONArray details = new JSONArray();
				details = json.getJSONArray(WrsisPortalConstant.RUST_WISE_DETAILS);
				JSONArray arr = new JSONArray();
				String regionName = json.getString("regionname");
				regions.add(regionName);
				for (String rustType : rustTypes) {
					boolean check = false;
					for (int j = 0; j < details.length(); j++) {
						if (rustType.equals(details.getJSONObject(j).getString(WrsisPortalConstant.RUST_NAME1))) {
							check = true;
							JSONObject jsonData = new JSONObject();
							jsonData.put(WrsisPortalConstant.LABEL, regionName);
							jsonData.put("y", details.getJSONObject(j).getString("rustcount"));
							jsonData.put(WrsisPortalConstant.RUST_NAME, rustType);
							arr.put(jsonData);
						}

					}
					if (!check ) {
						JSONObject jsonData = new JSONObject();
						jsonData.put(WrsisPortalConstant.LABEL, regionName);
						jsonData.put("y", 0);
						jsonData.put(WrsisPortalConstant.RUST_NAME, rustType);
						arr.put(jsonData);
					}
				}

				finalJsa.put(arr);
			}

			for (String rName : rustTypes) {
				JSONObject fJson = new JSONObject();
				fJson.put("type", "column");
				fJson.put("name", rName);
				fJson.put("showInLegend", true);
				JSONArray dataPoints = new JSONArray();

				main: for (int i = 0; i < finalJsa.length(); i++) {
					JSONArray indexJs = finalJsa.getJSONArray(i);
					for (int j = 0; j < indexJs.length(); j++) {
						JSONObject json_ = indexJs.getJSONObject(j);
						if (json_.getString(WrsisPortalConstant.RUST_NAME).equals(rName)) {
							JSONObject dataPJson = new JSONObject();
							dataPJson.put("y", json_.getInt("y"));
							dataPJson.put(WrsisPortalConstant.LABEL, json_.getString(WrsisPortalConstant.LABEL));
							dataPoints.put(dataPJson);
							continue main;
						}
					}
				}
				fJson.put("dataPoints", dataPoints);
				finalJsonData.put(fJson);
			}

			if (searchVo.getYear() == "") {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Integer currentYear = year;
				searchVo.setYear(currentYear.toString());
			}
			if (searchVo.getSeasonId() == 0) {
				Date date = new Date();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int month = localDate.getMonthValue();
				SeasonMonth seasonId = seasonMonthRepository.findSeasonIdByMonthId(month);
				searchVo.setSeasonId(seasonId.getSeasonId());
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustReportByRegion():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegion():" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegion():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegion():" + e);
			}
		}

		return finalJsonData;
	}

	@Override
	public JSONObject getSurveyIncidentReport(String yearId, Integer seasonId) {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_survey_incident_byregion_report(?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, yearId);
			pst.setInt(2, seasonId);
			res = pst.executeQuery();
			if (res.next()) {
				response = new JSONObject(res.getString(WrsisPortalConstant.DATA_JSON));
			}

		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getSurveyIncidentReport():" + e);
		} finally {
			try {
				if (res != null && !res.isClosed()) {
					res.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReport():" + e);
			}
			try {
				if (pst != null && !pst.isClosed()) {
					pst.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReport():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReport():" + e);
			}
		}
		return response;
	}

	@Override
	public JSONArray getRustReportDateWise(SearchVo searchVo) throws SQLException {

		JSONArray response = new JSONArray();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {

			String x = searchVo.getSurveyDateFrom();
			String sDate = null;

			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			sDate = dt2.format(dt1.parse(x));

			String y = searchVo.getSurveyDateTo();
			String eDate = null;

			SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			eDate = dt4.format(dt3.parse(y)); /* dt3.parse() */
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_rust_type_date_chart_report(?,?)";
			psmt = con.prepareStatement(query);
			psmt.setString(1, sDate);
			psmt.setString(2, eDate);

			rs = psmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(WrsisPortalConstant.RUST_REPORT_JSON) == null)
					return response;
				response = new JSONArray(rs.getString(WrsisPortalConstant.RUST_REPORT_JSON));

			}

		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustReportDateWise():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustReportDateWise():" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportDateWise():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportDateWise():" + e);
			}
		}

		return response;

	}

	@Override
	public JSONArray getRustReportByRegionDateWise(SearchVo searchVo) throws SQLException {

		JSONArray jsa = new JSONArray();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		JSONArray finalJsonData = new JSONArray();
		try {
			String x = searchVo.getSurveyDateFrom();
			String sDate = null;

			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			sDate = dt2.format(dt1.parse(x));

			String y = searchVo.getSurveyDateTo();
			String eDate = null;

			SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			eDate = dt4.format(dt3.parse(y));

			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_region_vs_rust_chart_date_report(?,?)";
			psmt = con.prepareStatement(query);

			psmt.setString(1, sDate);

			psmt.setString(2, eDate);

			rs = psmt.executeQuery();
			if (rs.next()) {
				jsa = new JSONArray(rs.getString(WrsisPortalConstant.RUST_REPORT_JSON));
			}
			
			JSONArray finalJsa = new JSONArray();
			Set<String> rustTypes = new HashSet<>();
			Set<String> regions = new HashSet<>();
			
			
			
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject json = new JSONObject();
				json = jsa.getJSONObject(i);
				JSONArray details = new JSONArray();

				if (json.get(WrsisPortalConstant.RUST_WISE_DETAILS) == null || !json.has(WrsisPortalConstant.RUST_WISE_DETAILS)
						|| !(json.get(WrsisPortalConstant.RUST_WISE_DETAILS) instanceof JSONArray)) {
					return finalJsonData;
				}
				details = json.getJSONArray(WrsisPortalConstant.RUST_WISE_DETAILS);
				for (int j = 0; j < details.length(); j++) {
					rustTypes.add(details.getJSONObject(j).getString(WrsisPortalConstant.RUST_NAME1));
				}
			}
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject json = new JSONObject();
				json = jsa.getJSONObject(i);
				JSONArray details = new JSONArray();
				details = json.getJSONArray(WrsisPortalConstant.RUST_WISE_DETAILS);
		
				JSONArray arr = new JSONArray();
		
				String regionName = json.getString("regionname");
				regions.add(regionName);
				for (String rustType : rustTypes) {
					boolean check = false;
					for (int j = 0; j < details.length(); j++) {
						if (rustType.equals(details.getJSONObject(j).getString(WrsisPortalConstant.RUST_NAME1))) {
							check = true;
							JSONObject jsonData = new JSONObject();
							jsonData.put(WrsisPortalConstant.LABEL, regionName);
							jsonData.put("y", details.getJSONObject(j).getString("rustcount"));
							jsonData.put(WrsisPortalConstant.RUST_NAME, rustType);
							arr.put(jsonData);
						}

					}
					if (!check ) {
						JSONObject jsonData = new JSONObject();
						jsonData.put(WrsisPortalConstant.LABEL, regionName);
						jsonData.put("y", 0);
						jsonData.put(WrsisPortalConstant.RUST_NAME, rustType);
						arr.put(jsonData);
					}
				}

				finalJsa.put(arr);
			}

			for (String rName : rustTypes) {
				JSONObject fJson = new JSONObject();
				fJson.put("type", "column");
				fJson.put("name", rName);
				fJson.put("showInLegend", true);
				JSONArray dataPoints = new JSONArray();

				main: for (int i = 0; i < finalJsa.length(); i++) {
					JSONArray indexJs = finalJsa.getJSONArray(i);
					for (int j = 0; j < indexJs.length(); j++) {
						JSONObject json_ = indexJs.getJSONObject(j);
						if (json_.getString(WrsisPortalConstant.RUST_NAME).equals(rName)) {
							JSONObject dataPJson = new JSONObject();
							dataPJson.put("y", json_.getInt("y"));
							dataPJson.put(WrsisPortalConstant.LABEL, json_.getString(WrsisPortalConstant.LABEL));
							dataPoints.put(dataPJson);
							continue main;
						}
					}
				}
				fJson.put("dataPoints", dataPoints);
				finalJsonData.put(fJson);
			}

			if (searchVo.getYear() == "") {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Integer currentYear = year;
				searchVo.setYear(currentYear.toString());
			}
			if (searchVo.getSeasonId() == 0) {
				Date date = new Date();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int month = localDate.getMonthValue();
				SeasonMonth seasonId = seasonMonthRepository.findSeasonIdByMonthId(month);
				searchVo.setSeasonId(seasonId.getSeasonId());
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustReportByRegionDateWise():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegionDateWise() finally:" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegionDateWise():" + e);
	
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustReportByRegionDateWise():" + e);
			}
		}

		return finalJsonData;

	}

	@Override
	public JSONObject getSurveyIncidentReportDateWise(String fromDate, String toDate) throws SQLException {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		try {

			String sDate = null;

			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			sDate = dt2.format(dt1.parse(fromDate));

			String eDate = null;

			SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			eDate = dt4.format(dt3.parse(toDate)); /* dt3.parse() */

			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_survey_incident_byregion_date_wise_report(?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, sDate);
			pst.setString(2, eDate);
			res = pst.executeQuery();
			if (res.next()) {
				response = new JSONObject(res.getString(WrsisPortalConstant.DATA_JSON));
			}

		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getSurveyIncidentReportDateWise():" + e);
		} finally {
			try {
				if (res != null && !res.isClosed()) {
					res.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReportDateWise():" + e);
			}
			try {
				if (pst != null && !pst.isClosed()) {
					pst.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReportDateWise():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getSurveyIncidentReportDateWise():" + e);
			}
		}
		return response;
	}

	@Override
	public JSONObject getRustSurveyAndRecommendSurvey(String yearId, Integer seasonId) throws SQLException {

		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_survey_rust_and_recommendation(?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, yearId);
			pst.setInt(2, seasonId);
			res = pst.executeQuery();
			if (res.next()) {
				response = new JSONObject(res.getString(WrsisPortalConstant.DATA_JSON));
			}

		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);
		} finally {
			try {
				if (res != null && !res.isClosed()) {
					res.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
			try {
				if (pst != null && !pst.isClosed()) {
					pst.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
		}
		return response;

	}

	@Override
	public JSONObject getRustSurveyAndRecommendSurveyDate(String fromDate, String toDate) throws SQLException {

		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		try {
			String sDate = null;

			SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			sDate = dt2.format(dt1.parse(fromDate));

			String eDate = null;

			SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			eDate = dt4.format(dt3.parse(toDate));

			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_survey_rust_and_recommendation_date(?,?)";
			pst = con.prepareStatement(query);
			pst.setString(1, sDate);
			pst.setString(2, eDate);
			res = pst.executeQuery();
			if (res.next()) {
				response = new JSONObject(res.getString(WrsisPortalConstant.DATA_JSON));
			}

		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

		} finally {
			try {
				if (res != null && !res.isClosed()) {
					res.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
			try {
				if (pst != null && !pst.isClosed()) {
					pst.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRustSurveyAndRecommendSurvey():" + e);

			}
		}
		return response;

	}

	@Override
	public void surveyorExcelDownload(HttpServletResponse response, String mobileE, String emailE, String fullNameE,
			String researchCenterIdE, String intdesigidE) {
		Workbook workbook = new XSSFWorkbook();
		try {
			try {
				List<Object[]> dataObj = null;

				if (mobileE == null && emailE == null && fullNameE == null && researchCenterIdE == null
						&& intdesigidE == null) {
					dataObj = userRepository.fetchSurveyorList("", "", "", "", "",-1,-1);
				} else {
					dataObj = userRepository.fetchSurveyorList(mobileE, emailE, fullNameE, researchCenterIdE,
							intdesigidE,-1,-1);
				}
				// Excel Code Start

				Sheet sheet = workbook.createSheet("Surveyor Details Report");

				CellStyle headerCellStyle = workbook.createCellStyle();
				headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
				headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

				Font newFont = workbook.createFont();
				newFont.setBold(true);
				newFont.setColor(IndexedColors.WHITE.getIndex());
				newFont.setFontHeightInPoints((short) 13);
				newFont.setItalic(false);

				headerCellStyle.setFont(newFont);

				Row headRow = sheet.createRow(0);
				String[] columns = { "Sl No.", "Research Center Name", "Surveyor Name", "Gender", "Designation", "Role",
						"Mobile", "Email" };
				for (int i = 0; i < columns.length; i++) {
					Cell cell = headRow.createCell(i);
					cell.setCellValue(columns[i]);
					cell.setCellStyle(headerCellStyle);
				}
				int rowNo = 1;
				int count = 1;
				for (Object[] ar : dataObj) {

					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[0] != null) ? (String) ar[0] : "-NA-");
					row.createCell(2).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(4).setCellValue((ar[3] != null) ? (String) ar[3] : "-NA-");
					row.createCell(5).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(6).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(7).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					if (ar[2].equals("1")) {
						row.createCell(3).setCellValue(WrsisPortalConstant.MALE);
					} else if (ar[2].equals("2")) {
						row.createCell(3).setCellValue(WrsisPortalConstant.FEMALE);
					} else {
						row.createCell(3).setCellValue("-NA-");
					}
				}
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
				String FILENAME = "Surveyor_Details_Report.xlsx";
				response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);

				// Excel Code End
			} catch (Exception ex) {
				LOG.error("DashboardServiceImpl::surveyorExcelDownload():" + ex.getMessage());

			} finally {
				workbook.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				workbook.close();
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::surveyorExcelDownload():" + e);
		}
	}

	@Override
	public List<UserBean> surveyorPdfDownload(String researchCenterIdPDF, String intdesigidPDF, String mobilePDF,
			String emailPDF, String fullNamePDF) {
		List<UserBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = null;
			if (researchCenterIdPDF == null && intdesigidPDF == null && mobilePDF == null && emailPDF == null
					&& fullNamePDF == null) {
				details = userRepository.fetchSurveyorList("", "", "", "", "",-1,-1);
			} else {
				details = userRepository.fetchSurveyorList(mobilePDF, emailPDF, fullNamePDF, researchCenterIdPDF,
						intdesigidPDF,-1,-1);
			}
			
			for (Object[] obj : details) {
				UserBean objReport = new UserBean();

				objReport.setOrganizationName((obj[0] != null) ? String.valueOf(obj[0]) : "NA");
				objReport.setFullName(String.valueOf(obj[1]));
				objReport.setDesignation(String.valueOf(obj[3]));
				objReport.setRoleName(String.valueOf(obj[5]));
				objReport.setMobile(String.valueOf(obj[7]));
				objReport.setEmail(String.valueOf(obj[8]));
				if (obj[2].equals("1")) {
					objReport.setGenderName(WrsisPortalConstant.MALE);
				} else if (obj[2].equals("2")) {
					objReport.setGenderName(WrsisPortalConstant.FEMALE);
				} else {
					objReport.setGenderName("-NA-");
				}
				beans.add(objReport);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::surveyorPdfDownload():" + e);
		}
		return beans;
	}

	@Override
	public List<UserBean> downloadPathologistReportExcel(String rcId, String desigId, String mobileNo, String email,
			String name) {
		if (rcId == null)
			rcId = "";
		if (desigId == null)
			desigId = "";
		if (mobileNo == null)
			mobileNo = "";
		if (email == null)
			email = "";
		if (name == null)
			name = "";
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchPathologistList(mobileNo, email, name, rcId, desigId);
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setOrganizationName((objs[0] != null) ? String.valueOf(objs[0]) : "NA");
				obj.setFullName(String.valueOf(objs[1]));
				obj.setUserName(String.valueOf(objs[10]));
				if (objs[2].equals("1")) {
					obj.setGenderName(WrsisPortalConstant.MALE);
				} else if (objs[2].equals("2")) {
					obj.setGenderName(WrsisPortalConstant.FEMALE);
				}
				obj.setDesignation(String.valueOf(objs[3]));
				obj.setUserId(Integer.parseInt(objs[4].toString()));
				obj.setRoleName(String.valueOf(objs[5]));
				obj.setMobile(String.valueOf(objs[7]));
				obj.setEmail(String.valueOf(objs[8]));
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::downloadPathologistReportExcel():" + e);
		}
		return userList;
	}

	@Override
	public List<UserBean> downloadWoredaExpertReportExcel(String desigId, String mobileNo, String email, String name) {

		if (desigId == null)
			desigId = "";
		if (mobileNo == null)
			mobileNo = "";
		if (email == null)
			email = "";
		if (name == null)
			name = "";
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchWoredaExpertList(mobileNo, email, name, desigId);
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setFullName(String.valueOf(objs[0]));
				if (objs[1].equals("1")) {
					obj.setGenderName(WrsisPortalConstant.MALE);
				} else if (objs[1].equals("2")) {
					obj.setGenderName(WrsisPortalConstant.FEMALE);
				}
				obj.setDesignation(String.valueOf(objs[2]));
				obj.setRoleName(String.valueOf(objs[4]));
				obj.setMobile(String.valueOf(objs[6]));
				obj.setEmail(String.valueOf(objs[7]));
				obj.setDemoName(String.valueOf(objs[11]));
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::downloadWoredaExpertReportExcel():" + e);
		}
		return userList;
	}

	@Override
	public List<UserBean> downloadDevAgentReportExcel(String desigId, String mobileNo, String email, String name) {
		if (desigId == null)
			desigId = "";
		if (mobileNo == null)
			mobileNo = "";
		if (email == null)
			email = "";
		if (name == null)
			name = "";
		List<UserBean> userList = new ArrayList<>();
		UserBean obj = null;
		try {
			List<Object[]> userObj = userRepository.fetchDevAgentList(mobileNo, email, name, desigId);
			for (Object[] objs : userObj) {
				obj = new UserBean();
				obj.setFullName(String.valueOf(objs[0]));
				if (objs[1].equals("1")) {
					obj.setGenderName(WrsisPortalConstant.MALE);
				} else if (objs[1].equals("2")) {
					obj.setGenderName(WrsisPortalConstant.FEMALE);
				}
				obj.setDesignation(String.valueOf(objs[2]));
				obj.setRoleName(String.valueOf(objs[4]));
				obj.setMobile(String.valueOf(objs[6]));
				obj.setEmail(String.valueOf(objs[7]));
				obj.setDemoName((objs[11] != null) ? String.valueOf(objs[11]) : "NA");
				userList.add(obj);
			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::downloadDevAgentReportExcel():" + e);
		}
		return userList;
	}

	@Override
	public JSONObject getDashboardUsersContent(Integer userId) {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_dashboard_user_count(?)";
			psmt = con.prepareStatement(query);
			psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			if (rs.next()) {
				response = new JSONObject(rs.getString(WrsisPortalConstant.DASHBOARD_JSON));

			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getDashboardUsersContent():" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getDashboardUsersContent():" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardUsersContent():" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardUsersContent():" + e);
			}
		}

		return response;
	}

	@Override
	public JSONObject getDashboardTransContent(Integer userId) {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.sp_wr_dashboard_transaction_count(?)";
			psmt = con.prepareStatement(query);
			psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			if (rs.next()) {
				response = new JSONObject(rs.getString(WrsisPortalConstant.DASHBOARD_JSON));

			}
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getDashboardTransContent()e:" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getDashboardTransContent()SQL:" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardTransContent()psmt:" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getDashboardTransContent()con:" + e);
			}
		}

		return response;
	}

	@Override
	public Integer fetchSurveyorListCount(String mobile, String email, String name, String rcId, String desigId,
			int start, int length) {
		if (rcId == null)
			rcId="";
		if (desigId == null)
			desigId="";
		if (mobile == null)
			mobile="";
		if (email == null)
			email="";
		if (name == null)
			name="";
		Integer count = 0;
		try {
			count = userRepository.fetchSurveyorListCount(mobile,email,
					name, rcId, desigId,start,length);
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::fetchSurveyorListCount():" + e);
		}
		return count;
	}

	@Override
	public JSONObject getAdvisoryLatestRecord() {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.t_wr_advisory_details where int_advisory_status=1 and bitstatus=false  ORDER BY int_advisory_id DESC limit 1";
			psmt = con.prepareStatement(query);
			//psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			if (rs.next()) {
				
				response.put("advisoryId", rs.getInt("int_advisory_id"));
				response.put("advisoryNo", rs.getString("vch_advisory_no"));
				
				String advisoryPublishedDate = rs.getString("dtm_publish_date");
				if (advisoryPublishedDate != null && !advisoryPublishedDate.isEmpty()) {
					advisoryPublishedDate = new SimpleDateFormat("dd-MMM-yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(advisoryPublishedDate));
				} else {
					advisoryPublishedDate = "";
				}
				
				response.put("advPublishDate", advisoryPublishedDate);

			}
			
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getAdvisoryLatestRecord()e:" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getAdvisoryLatestRecord()SQL:" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getAdvisoryLatestRecord()psmt:" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getAdvisoryLatestRecord()con:" + e);
			}
		}

		return response;
	}

	@Override
	public JSONObject getRecomLatestRecord() {
		JSONObject response = new JSONObject();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			
			con = jdbcTemplate.getDataSource().getConnection();
			String query = "select * from wrsis.t_wr_recommendation where int_recom_status=1 and bitstatus=false ORDER BY int_recom_id DESC limit 1";
			psmt = con.prepareStatement(query);
			//psmt.setInt(1, userId);
			rs = psmt.executeQuery();
			if (rs.next()) {
				
				response.put("recomId", rs.getInt("int_recom_id"));
				response.put("recomNo", rs.getString("vch_recom_no"));
				response.put("recomType", rs.getInt("int_recom_type"));
				
				String recomPublishedDate = rs.getString("dt_publish_on");
				if (recomPublishedDate != null && !recomPublishedDate.isEmpty()) {
					recomPublishedDate = new SimpleDateFormat("dd-MMM-yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(recomPublishedDate));
				} else {
					recomPublishedDate = "";
				}
				
				response.put("recomPublishDate", recomPublishedDate);

			}
			
		} catch (Exception e) {
			LOG.error("DashboardServiceImpl::getRecomLatestRecord()e:" + e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				LOG.error("DashboardServiceImpl::getRecomLatestRecord()SQL:" + e);
			}
			try {
				if (psmt != null && !psmt.isClosed()) {
					psmt.close();
				}
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRecomLatestRecord()psmt:" + e);
			}
			try {
				if (con != null && !con.isClosed())
					con.close();
			} catch (Exception e) {
				LOG.error("DashboardServiceImpl::getRecomLatestRecord()con:" + e);
			}
		}

		return response;
	}

}
