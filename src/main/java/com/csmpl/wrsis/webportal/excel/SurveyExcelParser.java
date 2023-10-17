package com.csmpl.wrsis.webportal.excel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.FungicideEffectTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;
import com.csmpl.wrsis.webportal.entity.MoistureMasterEntity;
import com.csmpl.wrsis.webportal.entity.ReactionMasterEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;
import com.csmpl.wrsis.webportal.entity.SiteTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SoilColorMasterEntity;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.WheatTypeMasterEntity;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;
import com.csmpl.wrsis.webportal.service.CommonService;

public class SurveyExcelParser {

	public static final Logger LOG = LoggerFactory.getLogger(SurveyExcelParser.class);

	public static final int CELL_TYPE_NUMERIC = 0;
	public static final int CELL_TYPE_STRING = 1;
	public static final int CELL_TYPE_FORMULA = 2;
	public static final int CELL_TYPE_BLANK = 3;
	public static final int CELL_TYPE_BOOLEAN = 4;
	public static final int CELL_TYPE_ERROR = 5;
	private Connection connection;

	public JSONObject getAllFieldLocations(Row row) throws JSONException {
		JSONArray json = new JSONArray();

		JSONArray jsa = new JSONArray();
		for (int j = 0; j < row.getLastCellNum(); j++) {

			String cellData = getCellData(row, j).trim();
			if (!cellData.equals("")) {
				LOG.info(cellData + "   ,   " + j);
				jsa = new JSONArray();
				jsa.put(cellData);
				jsa.put(j);
				json.put(jsa);

			}

		}

		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < json.length() - 1; i++) {
			arr = new JSONArray();
			String cellData = json.getJSONArray(i).getString(0).trim().replaceAll(" ", "");
			int start = json.getJSONArray(i).getInt(1);
			int end = json.getJSONArray(i + 1).getInt(1);
			arr.put(start);
			arr.put(end);
			obj.put(cellData, arr);
		}
		arr = new JSONArray();
		int start = json.getJSONArray(json.length() - 1).getInt(1);
		int end = row.getLastCellNum();
		String cellData = json.getJSONArray(json.length() - 1).getString(0).trim().replaceAll(" ", "");
		arr.put(start);
		arr.put(end);
		obj.put(cellData, arr);

		return obj;
	}

	public JSONObject getRowWiseData(Sheet sheet, Row row, JSONObject locations) throws JSONException, SQLException {
		JSONObject json = new JSONObject();

		String sl = getCellData(row, 0);
		int slNo = 0;
		if (sl == null || sl.isEmpty()) {

			json.put(WrsisPortalConstant.IS_ROW_END, true);
			return json;
		}
		try {
			slNo = Integer.valueOf(sl.substring(0, sl.lastIndexOf(".")));
		} catch (Exception e) {
			LOG.error("SurveyExcelParser::getRowWiseData():" + e.getMessage());
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Sl No Should be numeric.");
			return json;

		}

		String otherSurveyor = getCellData(row, 1);
		if (!(otherSurveyor == null || otherSurveyor.trim().isEmpty())) {
//			if (otherSurveyor.contains("."))
//				otherSurveyor = otherSurveyor.substring(0, otherSurveyor.lastIndexOf("."));
			if (!otherSurveyor.matches("^[-0-9a-zA-Z,.\\s]+$")) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Other Surveyor's.(Accept only alphanumeric and comma(,).-)");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum());
				return json;
			}

		}

		// Survey Details
		JSONObject sDetails = getSurveyDetails(row, locations.getJSONArray("SurveyDetails").getInt(0),
				locations.getJSONArray("SurveyDetails").getInt(1));
		JSONObject instituteDetails = getInstituteDetails(row, locations.getJSONArray("InstituteName").getInt(0),
				locations.getJSONArray("InstituteName").getInt(1));
		if (sDetails.has(WrsisPortalConstant.IS_ROW_END) && sDetails.getBoolean(WrsisPortalConstant.IS_ROW_END)) {
			json.put(WrsisPortalConstant.IS_ROW_END, true);
			return json;
		}
		
		if (sDetails.has(WrsisPortalConstant.ERR) && sDetails.getBoolean(WrsisPortalConstant.ERR)) {
			sDetails.put("SlNo", slNo);
			sDetails.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return sDetails;
		}
		if (instituteDetails.has(WrsisPortalConstant.IS_ROW_END) && instituteDetails.getBoolean(WrsisPortalConstant.IS_ROW_END)) {
			json.put(WrsisPortalConstant.IS_ROW_END, true);
			return json;
		}
		if (instituteDetails.has(WrsisPortalConstant.ERR) && instituteDetails.getBoolean(WrsisPortalConstant.ERR)) {
			instituteDetails.put("SlNo", slNo);
			instituteDetails.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return instituteDetails;
		}
		sDetails.put("othersurveyor", otherSurveyor);
		String surveyDate = sDetails.getString("surveyDateId");
		String observationDate = sDetails.getString("observationId");
		String plantingDate = sDetails.getString("plantingDate");

		json = sDetails;

		JSONArray surveyorJsa = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		
		List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
//		obj.clear();
			if(obj==null || obj.isEmpty() )
			{
				jsonObj.put("InstitutionName", instituteDetails.get("instituteName"));
				jsonObj.put("researchId", instituteDetails.get("instituteId"));
				jsonObj.put("country", 1);
				jsonObj.put("surveyorName", otherSurveyor);
				jsonObj.put("userId", userId);
			}else {
				Short id = (Short) obj.get(0)[0];
				String name = (String) obj.get(0)[1];
				jsonObj.put("InstitutionName", name);
				jsonObj.put("researchId", id);
				jsonObj.put("country", 1);
				List<Object[]> surveyors = commonService.getAllSurveyor(Integer.valueOf(Short.toString(id)));

				Short surveyorId = (Short) surveyors.get(0)[0];
				String surveyorName = (String) surveyors.get(0)[1];

				jsonObj.put("surveyorName", surveyorName);
				jsonObj.put("userId", surveyorId);
			}
			
		surveyorJsa.put(jsonObj);
		json.put("surveyorJsa", surveyorJsa);

		json.put("SlNo", slNo);
		// Site Information
		JSONObject siteInformation = getSiteInformation(row, locations.getJSONArray("SiteInformation").getInt(0),
				locations.getJSONArray("SiteInformation").getInt(1));

		if (siteInformation.has(WrsisPortalConstant.ERR) && siteInformation.getBoolean(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, siteInformation.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put("siteInformation", siteInformation);
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		json.put("siteInformation", siteInformation);

		// Rust Affected
		// rust details
		JSONObject rustAffected = getResuAffectedData(sheet, row, locations.getJSONArray("RustDetails").getInt(0),
				locations.getJSONArray("RustDetails").getInt(1));

		if (rustAffected.has(WrsisPortalConstant.ERR) && rustAffected.getBoolean(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, rustAffected.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		json.put("rustAffectedId", rustAffected.getString("rustAffectedId"));
		json.put("rustDetails", rustAffected.getJSONArray("rustDetails"));
		json.put("capturedImageId", false);
		if (rustAffected.has(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, rustAffected.getBoolean(WrsisPortalConstant.ERR));
			json.put(WrsisPortalConstant.MESSAGE_JSON, rustAffected.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		if (rustAffected.getBoolean("rustAffectedId")) {
			if (rustAffected.getJSONArray("rustDetails").length() == 0) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide atleast one rust details.");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			}
		}

		// Sample Details
		JSONObject sampleDetails = getSampleDetailsData(sheet, row, locations.getJSONArray("SampleDetails").getInt(0),
				locations.getJSONArray("SampleDetails").getInt(1), rustAffected.getBoolean("rustAffectedId"));

		if (sampleDetails.has(WrsisPortalConstant.ERR) && sampleDetails.getBoolean(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, sampleDetails.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		json.put(WrsisPortalConstant.SAMPLE_COLLECTED_ID, sampleDetails.getString(WrsisPortalConstant.SAMPLE_COLLECTED_ID));
		json.put(WrsisPortalConstant.SAMPLE_DETAILS, sampleDetails.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS));

		if (sampleDetails.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID) && rustAffected.getBoolean("rustAffectedId")) {
			if (sampleDetails.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS).length() == 0) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide atleast one sample details.");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			}
		}

		if (sampleDetails.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID) && rustAffected.getBoolean("rustAffectedId")) {
			JSONArray rust = rustAffected.getJSONArray("rustDetails");
			JSONArray sample = sampleDetails.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);

			JSONObject checkRustSampleCombinationJson = new JSONObject();
			checkRustSampleCombinationJson = checkRustSampleCombination(rust, sample);

			if (checkRustSampleCombinationJson.has(WrsisPortalConstant.ERR) && checkRustSampleCombinationJson.getBoolean(WrsisPortalConstant.ERR)) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, checkRustSampleCombinationJson.getString(WrsisPortalConstant.MESSAGE_JSON));
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			}

		}

		// Fungicide details

		int fungStart = locations.getJSONArray("FungicideDetails").getInt(0);

		if (getCellData(row, fungStart) == null || getCellData(row, fungStart).trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Fungicide Applied should not be blank.");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}
		if (!(getCellData(row, fungStart).trim().equalsIgnoreCase("Yes")
				|| getCellData(row, fungStart).trim().equalsIgnoreCase("No")
				|| getCellData(row, fungStart).trim().equalsIgnoreCase("i don't know"))) {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Fungicide Applied.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}
		json.put("fungisideId", (getCellData(row, fungStart).trim().equalsIgnoreCase("Yes")) ? true
				: ((getCellData(row, fungStart).trim().equalsIgnoreCase("I don't know")) ? "null" : false));
		String sprayDate = null;
		if (getCellData(row, fungStart).trim().equalsIgnoreCase("Yes") && rustAffected.getBoolean("rustAffectedId")) {
			JSONObject fungicides = new JSONObject();
			fungStart++;
			String used = getCellData(row, fungStart);
			if (!(used == null || used.isEmpty())) {
				List<FungicideMaster> fungiList = fungicideMasterRepository.fetchAllActiveFungicides();
				Optional<FungicideMaster> ent1 = fungiList.stream().filter(p -> p.getFungicideName().equals(used))
						.findFirst();
				if (ent1.isPresent()) {
					// id
					fungicides.put("fungiUsed", ent1.isPresent() ? ent1.get().getFungicideId() : -1);
					fungicides.put("FungicideName", used);

				} else {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Fungicide Used.");
					json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
					return json;
				}
			}

			fungStart++;

			// Other fungi
			String otherFungi = getCellData(row, fungStart);
			if ((otherFungi == null || otherFungi.trim().isEmpty()) && used.trim().equalsIgnoreCase("other")) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide other Fungicide.");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			} else {
				fungicides.put("OtherFungi", otherFungi);
			}

			fungStart++;

			String dose = getCellData(row, fungStart);
			if (!(dose == null || dose.isEmpty())) {
				if (dose.contains("."))
					dose = dose.substring(0, getCellData(row, fungStart).lastIndexOf("."));
				try {
					int d = Integer.parseInt(dose);
					if (!(d >= 0 && d < 9999)) {
						json.put(WrsisPortalConstant.ERR, true);
						json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Dose.(Accept range from 0 to 9999)");
						json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
						return json;
					}
				} catch (Exception e) {
					LOG.error("SurveyExcelParser::getRowWiseData():" + e.getMessage());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Dose should be numeric.(Accept range from 0 to 9999)");
					json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
					return json;
				}
				fungicides.put("dose", dose);
			}

			fungStart++;
			sprayDate = getCellData(row, fungStart);
			if (!(sprayDate == null || sprayDate.isEmpty())) {
				Date dtSprayDt = convertStringToDate(sprayDate);
				if (dtSprayDt == null) {
					dtSprayDt = convertStringToDateForExcel(sprayDate);
				}
				SimpleDateFormat sdf = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				sprayDate = sdf.format(dtSprayDt);
				if (dtSprayDt == null) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid spray date.(Accept only dd/MM/YYYY)");
					json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
					return json;
				}

				boolean check = checkIfMoreToCurrentDate(dtSprayDt, new Date(), 1);
				if (!check) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Spray Date should not exceed from current date.");
					json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
					return json;
				}
			}

			fungicides.put("sprayDate", sprayDate);

			fungStart++;
			String effectiveness = getCellData(row, fungStart);

			if (!(effectiveness == null || effectiveness.isEmpty())) {
				List<FungicideEffectTypeMasterEntity> fungieffect = fungiEffectTypeRepository
						.fetchAllActiveFungiEffectTypes();
				String effect = getCellData(row, fungStart);
				Optional<FungicideEffectTypeMasterEntity> ent2 = fungieffect.stream()
						.filter(p -> p.getVchDesc().equals(effect)).findFirst();
				if (ent2.isPresent()) {
					fungicides.put(WrsisPortalConstant.EFFECTIVENESS_NAME, effectiveness);
					fungicides.put("effectiveness", ent2.isPresent() ? ent2.get().getIntEffectId() : -1);
				} else {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Effectiveness.");
					json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
					return json;
				}
			}

			json.put("fungicideJson", fungicides);
		}

		// Other details

		int oDStart = locations.getJSONArray("OtherDetails").getInt(0);
		int oDEnd = locations.getJSONArray("OtherDetails").getInt(1);
		JSONObject otherDetails = new JSONObject();

		String irrigated = getCellData(row, oDStart);

		if (irrigated == null || irrigated.isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Irrigated should not be blank.");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}
		if (!(irrigated.trim().equalsIgnoreCase("Yes") || irrigated.trim().equalsIgnoreCase("No"))) {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Irrigated.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		otherDetails.put("irrigated", (irrigated.trim().equalsIgnoreCase("yes")) ? true : false);
		oDStart++;

		String weedControl = getCellData(row, oDStart);

		if (weedControl == null || weedControl.isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Weed control should not be blank.");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}
		if (!(weedControl.trim().equalsIgnoreCase("Yes") || weedControl.trim().equalsIgnoreCase("No"))) {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Weed control.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		otherDetails.put(WrsisPortalConstant.WEED_CONTROL1, (weedControl.trim().equalsIgnoreCase("yes")) ? true : false);
		oDStart++;
		String soilColor = getCellData(row, oDStart);

		if (!(soilColor == null || soilColor.isEmpty())) {
			List<SoilColorMasterEntity> sampleTypeList = soilColorRepository.fetchAllSoilActiveColors();
			Optional<SoilColorMasterEntity> ent = sampleTypeList.stream()
					.filter(p -> p.getVchSoilColor().equals(soilColor)).findFirst();
			if (ent.isPresent()) {
				Integer sId = ent.get().getIntSoilColorId();
				otherDetails.put("soilColor", sId);
				otherDetails.put("SoilName", sId);

			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Soil color.");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			}
		}

		oDStart++;
		String moisture = getCellData(row, oDStart);

		if (!(moisture == null || moisture.isEmpty())) {
			List<MoistureMasterEntity> sampleTypeList1 = moistureRepository.fetchAllActiveMoistures();
			Optional<MoistureMasterEntity> ent3 = sampleTypeList1.stream()
					.filter(p -> p.getVchMoisture().equals(moisture)).findFirst();
			if (ent3.isPresent()) {
				Integer mId = ent3.get().getIntMoistureId();
				otherDetails.put("moisture", mId);
				otherDetails.put("MoistureName", moisture);
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Moisture.");
				json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
				return json;
			}

		}

		// Other Disease
		JSONObject otherDisease = getOtherDisease(sheet, row, locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(0),
				locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(1));
		if (otherDisease.has(WrsisPortalConstant.ERR) && otherDisease.getBoolean(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, otherDisease.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		JSONArray otherDiseaseArray = otherDisease.getJSONArray("Arr");

		json.put("otherDisease", otherDiseaseArray);

		// Any Other disease

		JSONArray anyOtherDiseaseJsa = new JSONArray();
		json.put("anyOtherDiseaseJsa", anyOtherDiseaseJsa);

		// remark field data fecth

		String remark = getCellData(row, oDEnd);
		// ^[0-9a-zA-Z,\\. ]+$
		if ( !remark.isEmpty() && !remark.matches("^[0-9a-zA-Z,\\. ]+$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Additional comments or Observation.");
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}
		json.put("remark", remark);

		// date validations

		JSONObject dateJson = new JSONObject();
		dateJson = checkDateValidations(surveyDate, observationDate, plantingDate, sprayDate,
				json.getString("tilleringdate"));

		if (dateJson.has(WrsisPortalConstant.ERR) && dateJson.getBoolean(WrsisPortalConstant.ERR)) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, dateJson.getString(WrsisPortalConstant.MESSAGE_JSON));
			json.put(WrsisPortalConstant.ROW_NO, row.getRowNum() + 1);
			return json;
		}

		json.put("otherDetails", otherDetails);

		return json;
	}

	private JSONObject getInstituteDetails(Row row, int start, int end) throws JSONException  {
		JSONObject json = new JSONObject();
		
			String instituteName = getCellData(row, start);

			if (instituteName.trim().equals("")) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Select Institute Name.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
			// row end alert

			List<ResearchCenter> rcList = researchCenterRepository.findActiveResearchCenter();
			Optional<ResearchCenter> ent1 = rcList.stream().filter(p -> p.getResearchCenterName().equals(instituteName))
					.findFirst();
			Integer rcId;

			if (ent1.isPresent()) {
				rcId = (Integer) ent1.get().getResearchCenterId();
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Institute Name.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
			json.put("instituteName", instituteName);
			json.put("instituteId", rcId);
		
		return json;
	}

	private JSONObject checkRustSampleCombination(JSONArray rust, JSONArray sample) throws JSONException {

		JSONObject json = new JSONObject();

		main: for (int i = 0; i < sample.length(); i++) {
			JSONObject sampleJ = sample.getJSONObject(i);// sampleTypeId
			boolean b = false;
			SampleTypeMaster mas = sampleTypeRepository.findBySampleId(sampleJ.getInt("sampleTypeId"));
			inner: for (int j = 0; j < rust.length(); j++) {
				JSONObject rustJ = rust.getJSONObject(j);// rustTypeId
				if ((mas.getRustTypeId() == rustJ.getInt("rustTypeId"))) {
					b = true;
				}
				if (b) {
					continue main;
				}
				if (!(mas.getRustTypeId() == rustJ.getInt("rustTypeId"))) {
					continue inner;
				}

			}
			if (!b) {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "You can't add " + sampleJ.getString(WrsisPortalConstant.SAMPLE_TYPE_NAME) + " details.");

				return json;
			}
		}

		return json;
	}

	private JSONObject checkDateValidations(String surveyDate, String observationDate, String plantingDate,
			String sprayDate, String tilleringdate) throws JSONException {

		JSONObject json = new JSONObject();

		
		Date dtSurveyDate = convertStringToDate(surveyDate);
		Date dtObservationDate = convertStringToDate(observationDate);
		Date dtPlantingDate = convertStringToDate(plantingDate);
		Date dtSprayDate = convertStringToDate(sprayDate);
		Date dttilleringdate = convertStringToDate(tilleringdate);

		// planting date < survey date
		boolean check = checkIfMoreToCurrentDate(dtPlantingDate, dtSurveyDate, 1);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Planting  Date should be less  from survey date.");
			return json;
		}

		check = checkIfMoreToCurrentDate(dtPlantingDate, dtObservationDate, 1);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Planting  Date should be less  from Date of first Rust Observation.");
			return json;
		}

		check = checkIfMoreToCurrentDate(dtPlantingDate, dttilleringdate, 1);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Tillering  Date should be less  from Planting Date.");
			return json;
		}

		check = checkIfMoreToCurrentDate(dtObservationDate, dtSurveyDate, 0);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Date of first Rust Observation  should be less or equal from survey date .");
			return json;
		}

		check = checkIfMoreToCurrentDate(dtSprayDate, dtSurveyDate, 0);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Spray Date  should be less or equal from survey date .");
			return json;
		}

		check = checkIfMoreToCurrentDate(dtPlantingDate, dtSprayDate, 1);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Planting Date  should be less  from spray date .");
			return json;
		}

		//

		return json;
	}

	private JSONObject getOtherDisease(Sheet sheet, Row row, int start, int end) throws JSONException {

		JSONObject finalJson = new JSONObject();
		JSONArray jsa = new JSONArray();
		int diseaseNameCount = (end - start) / 2;
		JSONObject obj = new JSONObject();
		for (int i = 0; i < diseaseNameCount; i++) {

			obj = new JSONObject();

			Row midRow = sheet.getRow(1);
			String diseaseName = getCellData(midRow, start);
			obj.put("DiseaseName", diseaseName);

			List<DiseaseMaster> dM = diseaseMasterRepository.fetchAllActiveDisease(true);
			Optional<DiseaseMaster> ent1 = dM.stream().filter(p -> p.getDiseaseName().equals(diseaseName)).findFirst();
			obj.put("diseaseTypeId", ent1.get().getDiseaseId());

			String othIncidentVal = getCellData(row, start);
			start++;
			String othSeverityVal = getCellData(row, start);
			start++;

			if ((othIncidentVal == null || othIncidentVal.isEmpty())
					&& (othSeverityVal == null || othSeverityVal.isEmpty())) {
				continue;
			} else {
				if ((othIncidentVal == null || othIncidentVal.isEmpty())) {
					finalJson.put("Arr", new JSONArray());
					finalJson.put(WrsisPortalConstant.ERR, true);
					finalJson.put(WrsisPortalConstant.MESSAGE_JSON, diseaseName + "  incident should not be blank.");
					finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return finalJson;
				}
				if (Integer.valueOf(othIncidentVal.substring(0, String.valueOf(Double.parseDouble(othIncidentVal)).lastIndexOf("."))) < 0) {
					finalJson.put("Arr", new JSONArray());
					finalJson.put(WrsisPortalConstant.ERR, true);
					finalJson.put(WrsisPortalConstant.MESSAGE_JSON, diseaseName + "  incident should not be less than 0.");
					finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return finalJson;
				}
				if (Integer.valueOf(othIncidentVal.substring(0, String.valueOf(Double.parseDouble(othIncidentVal)).lastIndexOf("."))) > 100) {
					finalJson.put("Arr", new JSONArray());
					finalJson.put(WrsisPortalConstant.ERR, true);
					finalJson.put(WrsisPortalConstant.MESSAGE_JSON, diseaseName + "  incident should not exceed 100.");
					finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return finalJson;
				}
				obj.put("othIncidentVal", (othIncidentVal.trim().isEmpty()) ? "--"
						: Integer.valueOf(othIncidentVal.substring(0, String.valueOf(Double.parseDouble(othIncidentVal)).lastIndexOf("."))));

				if ((othSeverityVal == null || othSeverityVal.isEmpty())
						&& ent1.get().getSeverityRequired()) {
					finalJson.put("Arr", new JSONArray());
					finalJson.put(WrsisPortalConstant.ERR, true);
					finalJson.put(WrsisPortalConstant.MESSAGE_JSON, diseaseName + "  severity should not be blank.");
					finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return finalJson;
				}

				if (ent1.get().getSeverityRequired()) {

					if (Integer.valueOf(othSeverityVal.substring(0, othSeverityVal.lastIndexOf("."))) < ent1.get()
							.getSeverityMin()) {
						finalJson.put("Arr", new JSONArray());
						finalJson.put(WrsisPortalConstant.ERR, true);
						finalJson.put(WrsisPortalConstant.MESSAGE_JSON, diseaseName + "  incident should not be less than "
								+ ent1.get().getSeverityMin() + ".");
						finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
						return finalJson;
					}
					if (Integer.valueOf(othSeverityVal.substring(0, othSeverityVal.lastIndexOf("."))) > ent1.get()
							.getSeverityMax()) {
						finalJson.put("Arr", new JSONArray());
						finalJson.put(WrsisPortalConstant.ERR, true);
						finalJson.put(WrsisPortalConstant.MESSAGE_JSON,
								diseaseName + "  incident should not exceed " + ent1.get().getSeverityMax() + ".");
						finalJson.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
						return finalJson;
					}

				}

				if (ent1.get().getDiseaseName().trim().equalsIgnoreCase("septoria")) {
					othSeverityVal = (othSeverityVal.trim().length() == 1) ? "0" + othSeverityVal : othSeverityVal;
				}

				obj.put("othSeverityVal", (othSeverityVal.trim().isEmpty()) ? "--"
						: othSeverityVal.substring(0, othSeverityVal.lastIndexOf(".")));

			}

			jsa.put(obj);
		}
		finalJson.put("Arr", jsa);

		return finalJson;
	}

	private JSONObject getSampleDetailsData(Sheet sheet, Row row, int start, int end, boolean b)
			throws JSONException, SQLException {

		JSONObject json = new JSONObject();
		String sampleCollectedId = getCellData(row, start);
		start++;

		if (sampleCollectedId == null || sampleCollectedId.trim().isEmpty()) {
			json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Sample collected should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		if (!(sampleCollectedId.trim().equalsIgnoreCase("Yes") || sampleCollectedId.trim().equalsIgnoreCase("No"))) {
			json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Sample collected.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put(WrsisPortalConstant.SAMPLE_COLLECTED_ID, (sampleCollectedId.trim().equalsIgnoreCase("yes")) ? true : false);
		if (sampleCollectedId.trim().equalsIgnoreCase("No") || !b ) {
			json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());

			return json;
		}

		int sampleTypeCount = (end - start) / 3;
		JSONArray jsa = new JSONArray();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < sampleTypeCount; i++) {

			obj = new JSONObject();

			Row midRow = sheet.getRow(1);
			String sampleType = getCellData(midRow, start);
			obj.put(WrsisPortalConstant.SAMPLE_TYPE_NAME, sampleType);

			List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();
			Optional<SampleTypeMaster> ent1 = sampleTypeList.stream().filter(p -> p.getSampleName().equals(sampleType))
					.findFirst();
			obj.put("sampleTypeId", ent1.get().getSampleId());

			String sampleCountId = getCellData(row, start);
			start++;
			String sampleIds = getCellData(row, start);
			start++;
			String remarks = getCellData(row, start);
			start++;

			if ((sampleCountId == null || sampleCountId.isEmpty()) && (sampleIds == null || sampleIds.isEmpty())
					&& (remarks == null || remarks.isEmpty())) {
				continue;
			} else {
				if ((sampleCountId == null || sampleCountId.isEmpty())) {
					json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "  No. of samples should not be blank(" + sampleType + ").");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				if ((sampleIds == null || sampleIds.isEmpty())) {
					json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "  Sample id's should not be blank(" + sampleType + ").");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
			}

			try {
				int no = (int) Math.round(Double.valueOf(sampleCountId));
				if (!(no > 0 && no < 10)) {
					json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON,
							"(" + sampleType + ")" + "No. of samples accepts only numeric.(Accept range from 1 to 9)");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
			} catch (Exception e) {
				LOG.error("SurveyExcelParser::getSampleDetailsData():" + e.getMessage());
				json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON,
						"(" + sampleType + ")" + "No. of samples accepts only numeric.(Accept range from 1 to 9)");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
			if (sampleIds.contains(".")) {
				sampleIds = sampleIds.substring(0, sampleIds.lastIndexOf("."));
			}
			if (!sampleIds.matches("^[0-9a-zA-Z,]+$")) {
				json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON,
						"(" + sampleType + ")" + "Invalid sample ids.(Accept alphanumeric separated with ',' ).");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
			String[] spl = sampleIds.trim().split(",");
			if (spl.length != (int) Math.round(Double.valueOf(sampleCountId))) {
				json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "(" + sampleType + ")" + "No of samples should be "
						+ (int) Math.round(Double.valueOf(sampleCountId)) + ".");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			if (!remarks.isEmpty() && !remarks.matches("^[0-9a-zA-Z,\\. ]+$")) {
				json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "(" + sampleType + ")" + "Sample remarks accept alphabets,numbers, ','.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			obj.put("sampleCountId", Math.round(Double.valueOf(sampleCountId)));
			obj.put("sampleIds", sampleIds);

			// check sample id duplicate check
			String[] splids = sampleIds.trim().split(",");
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {

				for (int c = 0; c < splids.length; c++) {
					String sampleId = splids[c];

					String query = "select * from wrsis.t_wr_survey_sample_sampleids where vch_sampleid_value=? ";
					pstmt = connection.prepareStatement(query);
					pstmt.setString(1, sampleId);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						json.put(WrsisPortalConstant.SAMPLE_DETAILS, new JSONArray());
						json.put(WrsisPortalConstant.ERR, true);
						json.put(WrsisPortalConstant.MESSAGE_JSON, "Duplicate sample id's detected.(" + sampleId + ")");
						json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
						return json;
					}
				}
			} catch (SQLException e) {
				LOG.error("SurveyExcelParser::getSampleDetailsData():" + e.getMessage());
			} finally {

				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (Exception e) {
					LOG.error("SurveyExcelParser::getSampleDetailsData():" + e.getMessage());
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (Exception e) {
					LOG.error("SurveyExcelParser::getSampleDetailsData():" + e.getMessage());
				}
			}

			obj.put("sampleRemarks", remarks);

			jsa.put(obj);
		}

		// loop
		json.put(WrsisPortalConstant.SAMPLE_DETAILS, jsa);

		return json;

	}

	@Autowired
	SampleSurveyDetailsRepository sampleSurveyDetailsRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	private JSONObject getResuAffectedData(Sheet sheet, Row row, int start, int end) throws JSONException {

		JSONObject json = new JSONObject();
		String rustAffectedId = getCellData(row, start);
		start++;

		if (rustAffectedId == null || rustAffectedId.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Rust affected should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		if (!(rustAffectedId.trim().equalsIgnoreCase("Yes") || rustAffectedId.trim().equalsIgnoreCase("No"))) {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Rust affected.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		json.put("rustAffectedId", (rustAffectedId.trim().equalsIgnoreCase("yes")) ? true : false);
		if (json.getBoolean("rustAffectedId") == false) {
			json.put("rustDetails", new JSONArray());
			return json;
		}
		int rustTypeCount = (end - start) / 3;
		JSONArray jsa = new JSONArray();
		JSONObject obj = new JSONObject();
		String headIncident = "";
		String headSeverity = "";
		for (int i = 0; i < rustTypeCount; i++) {

			obj = new JSONObject();

			Row midRow = sheet.getRow(1);
			String rustType = getCellData(midRow, start);
			obj.put("RustTypeName", rustType);

			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
			Optional<RustTypeMasterEntity> ent1 = rustTypeList.stream().filter(p -> p.getVchRustType().equals(rustType))
					.findFirst();
			obj.put("rustTypeId", ent1.get().getIntRustTypeId());

			String incident = getCellData(row, start);
			start++;
			String severity = getCellData(row, start);
			start++;
			String reaction = getCellData(row, start);
			start++;
			
			if(ent1.get().getIntRustTypeId() ==3) { //modification Yellow rust for head incident and head severity
				
			headIncident = getCellData(row, start);
			start++;
			headSeverity = getCellData(row, start);
			start++;
				
			}

			if ((incident == null || incident.isEmpty()) && (severity == null || severity.isEmpty())
					&& (reaction == null || reaction.isEmpty())) {
				continue;
			} else {
				if ((incident == null || incident.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  incident should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				if ((severity == null || severity.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  severity should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				if ((reaction == null || reaction.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  reaction should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
				
				if(ent1.get().getIntRustTypeId() ==3) { //modification Yellow rust for head incident and head severity
					
				if ((headIncident == null || headIncident.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head incident should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				if ((headSeverity == null || headSeverity.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head severity should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
					
				}

			}

			if ((incident == null || incident.isEmpty())) {
				json.put("rustDetails", new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  incident should not be blank.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			try {
				double d = Double.parseDouble(incident);
				if (!(d >= 0 && d <= 100)) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " incident accept only numeric.(Accept  range from 0 to 100 )");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
			} catch (Exception e) {
				json.put("rustDetails", new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " incident accept only numeric.(Accept  range from 0 to 100 )");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

//			obj.put("incidentId", incident.substring(0, incident.lastIndexOf(".")));
			obj.put("incidentId", incident.substring(0, String.valueOf(Double.parseDouble(incident)).lastIndexOf(".")));
			
			if ((severity == null || severity.isEmpty())) {
				json.put("rustDetails", new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  severity should not be blank.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			try {
				double d = Double.parseDouble(severity);
				if (!(d >= 0 && d <= 100)) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " severity accept only numeric.(Accept  range from 0 to 100 )");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
			} catch (Exception e) {
				LOG.error("SurveyExcelParser::getResuAffectedData():" + e.getMessage());
				json.put("rustDetails", new JSONArray());
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " severity accept only numeric.(Accept  range from 0 to 100 )");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			obj.put("severityId", severity.substring(0, String.valueOf(Double.parseDouble(severity)).lastIndexOf(".")));
			
			if(ent1.get().getIntRustTypeId() !=3) { //modification Yellow rust for head incident and head severity
				
				obj.put("headIncidentId", "--");
				obj.put("headSeverityId", "--");
				
			}
			else{  

				if ((headIncident == null || headIncident.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head incident should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				try {
					double d = Double.parseDouble(headIncident);
					if (!(d >= 0 && d <= 100)) {
						json.put("rustDetails", new JSONArray());
						json.put(WrsisPortalConstant.ERR, true);
						json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head incident accept only numeric.(Accept  range from 0 to 100 )");
						json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
						return json;
					}
				} catch (Exception e) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head incident accept only numeric.(Accept  range from 0 to 100 )");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				obj.put("headIncidentId", headIncident.substring(0, String.valueOf(Double.parseDouble(headIncident)).lastIndexOf(".")));
				
				if ((headSeverity == null || headSeverity.isEmpty())) {
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + "  head severity should not be blank.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				try {
					double d = Double.parseDouble(headSeverity);
					if (!(d >= 0 && d <= 5)) {
						json.put("rustDetails", new JSONArray());
						json.put(WrsisPortalConstant.ERR, true);
						json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " head severity accept only numeric.(Accept  range from 0 to 5 )");
						json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
						return json;
					}
				} catch (Exception e) {
					LOG.error("SurveyExcelParser::getResuAffectedData():" + e.getMessage());
					json.put("rustDetails", new JSONArray());
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, rustType + " severity accept only numeric.(Accept  range from 0 to 5 )");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

				obj.put("headSeverityId", headSeverity.substring(0, String.valueOf(Double.parseDouble(headSeverity)).lastIndexOf(".")));
	
				
			}

			obj.put("ReactionName", reaction);
			List<com.csmpl.wrsis.webportal.entity.ReactionMasterEntity> reactionList = reactionTypeRepository
					.fetchAllActiveReactionTypes();
			Optional<ReactionMasterEntity> ent3 = reactionList.stream().filter(p -> p.getVchDesc().equals(reaction))
					.findFirst();
			Integer reactionId;
			if (ent3.isPresent()) {
				reactionId = (Integer) ent3.get().getIntRustReactionId();
				obj.put("reaction", reactionId);
			} else {
				json.put("rustDetails", jsa);
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Reaction(" + rustType + ").");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

			jsa.put(obj);
		}

		// loop
		json.put("rustDetails", jsa);

		return json;
	}

	private JSONObject getSiteInformation(Row row, int start, int end) throws JSONException {
		JSONObject json = new JSONObject();

		String siteType = getCellData(row, start);
		Integer siteId = null;
		start++;
		if (!(siteType == null || siteType.trim().isEmpty())) {
			List<SiteTypeMasterEntity> siteEnt = siteTypeRepository.fetchAllActiveSitetypes();
			Optional<SiteTypeMasterEntity> ent1 = siteEnt.stream().filter(p -> p.getVchSiteName().equals(siteType))
					.findFirst();
			if (ent1.isPresent()) {
				siteId = (Integer) ent1.get().getIntSiteTypeId();
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid SiteType.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
		}
		String wheatType = getCellData(row, start);
		json.put("WheatTypeName", wheatType);
		start++;
		Integer wheatId = null;
		if (!(wheatType == null || wheatType.trim().isEmpty())) {
			List<WheatTypeMasterEntity> wheatEnt = wheatTypeRepository.fetchAllActiveWheattypes();
			Optional<WheatTypeMasterEntity> ent2 = wheatEnt.stream().filter(p -> p.getVchDesc().equals(wheatType))
					.findFirst();
			if (ent2.isPresent()) {
				wheatId = (Integer) ent2.get().getIntWheatTypeId();
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid WheatType.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
		}
		String variety = getCellData(row, start);
		start++;
		Integer varietyId = null;
		if ((variety == null || variety.trim().isEmpty())) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Variety should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (!(variety == null || variety.trim().isEmpty())) {
			List<VarietyTypeMasterEntity> varietyEnt = varietyTypeRepository.fetchActiveVarietyType();
			Optional<VarietyTypeMasterEntity> ent3 = varietyEnt.stream().filter(p -> p.getVchDesc().equals(variety))
					.findFirst();
			if (ent3.isPresent()) {
				varietyId = (Integer) ent3.get().getVarietyTypeId();
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Variety.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
		}

		String varietyOther = getCellData(row, start);

		if ((varietyOther == null || varietyOther.trim().isEmpty()) && variety.trim().equalsIgnoreCase("other")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide Other Variety.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		} else {
			json.put("OtherVariety", varietyOther);
		}

		start++;

		String growth = getCellData(row, start);
		start++;
		Integer growthId = null;
		if (!(growth == null || growth.trim().isEmpty())) {
			List<GrowthMasterEntity> growthEnt = growthRepository.fetchAllActiveGrowth();
			Optional<GrowthMasterEntity> ent4 = growthEnt.stream().filter(p -> p.getVchStage().equals(growth))
					.findFirst();
			if (ent4.isPresent()) {
				growthId = (Integer) ent4.get().getIntCrGrStageId();
			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Growth Stage.");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}
		}

		String area = getCellData(row, start);
		if (!area.matches("^(([1-9]*)|(([0-9]*)\\.([0-9]*)))$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Area should be in numeric format and should be +ve.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put("siteArea", area);
		json.put("surveySite", siteId);
		json.put("SurveySiteName", siteType);
		json.put("growthStage", growthId);

		json.put("GrowthStageName", growth);
		json.put("VarityName", variety);
		json.put("wheatType", wheatId);
		json.put("varity", varietyId);

		return json;
	}

	private CommonService commonService;
	private Integer userId;
	private DemographyRepository demographyRepository;
	private SeasionMasterRepository seasionMasterRepository;
	private SiteTypeRepository siteTypeRepository;
	private WheatTypeRepository wheatTypeRepository;
	private VarietyTypeRepository varietyTypeRepository;
	private GrowthRepository growthRepository;
	private ReactionTypeRepository reactionTypeRepository;
	private RustTypeRepository rustTypeRepository;
	private SampleTypeRepository sampleTypeRepository;
	private FungicideMasterRepository fungicideMasterRepository;
	private FungiEffectTypeRepository fungiEffectTypeRepository;
	private SoilColorRepository soilColorRepository;
	private MoistureRepository moistureRepository;
	private DiseaseMasterRepository diseaseMasterRepository;
	private ResearchCenterRepository researchCenterRepository;
	
	public SurveyExcelParser(CommonService commonService, Integer userId, DemographyRepository demographyRepository,
			SeasionMasterRepository seasionMasterRepository, SiteTypeRepository siteTypeRepository,
			WheatTypeRepository wheatTypeRepository, VarietyTypeRepository varietyTypeRepository,
			GrowthRepository growthRepository, ReactionTypeRepository reactionTypeRepository,
			RustTypeRepository rustTypeRepository, SampleTypeRepository sampleTypeRepository,
			FungicideMasterRepository fungicideMasterRepository, FungiEffectTypeRepository fungiEffectTypeRepository,
			SoilColorRepository soilColorRepository, MoistureRepository moistureRepository,
			DiseaseMasterRepository diseaseMasterRepository,ResearchCenterRepository researchCenterRepository, Connection connection) {
		super();
		this.commonService = commonService;
		this.userId = userId;
		this.demographyRepository = demographyRepository;
		this.seasionMasterRepository = seasionMasterRepository;
		this.siteTypeRepository = siteTypeRepository;
		this.wheatTypeRepository = wheatTypeRepository;
		this.varietyTypeRepository = varietyTypeRepository;
		this.growthRepository = growthRepository;
		this.reactionTypeRepository = reactionTypeRepository;
		this.rustTypeRepository = rustTypeRepository;
		this.sampleTypeRepository = sampleTypeRepository;
		this.fungicideMasterRepository = fungicideMasterRepository;
		this.fungiEffectTypeRepository = fungiEffectTypeRepository;
		this.moistureRepository = moistureRepository;
		this.soilColorRepository = soilColorRepository;
		this.diseaseMasterRepository = diseaseMasterRepository;
		this.researchCenterRepository = researchCenterRepository;
		this.connection = connection;
	}

	public JSONObject getSurveyDetails(Row row, int start, int end) throws JSONException {

		JSONObject json = new JSONObject();
		String season = getCellData(row, start);

		if (season.trim().equals("")) {
			json.put(WrsisPortalConstant.IS_ROW_END, true);
			return json;
		}
		// row end alert

		List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
		Optional<SeasionMasterEntity> ent1 = seasionList.stream().filter(p -> p.getVchSeason().equals(season))
				.findFirst();
		Integer seasonId = null;

		if (ent1.isPresent()) {
			seasonId = (Integer) ent1.get().getIntSeasoonId();
		} else {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Season.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put("SeasionName", season);
		start++;

		List<Object[]> obj = commonService.getSelectedInstitutionNam(userId);
		List<Object[]> demographList = new ArrayList<>();
		if(obj == null || obj.isEmpty()) {
			demographList=demographyRepository.getActiveRegions();
		}else {
			 demographList = demographyRepository
					.fetchAllActiveDemographs(Integer.valueOf(String.valueOf(obj.get(0)[0])));
		}
		 // as Eutopia id is 1

		String region = getCellData(row, start);
		if (region == null || region.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Region should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		Optional<Object[]> matchingObject = demographList.stream().filter(p -> p[1].equals(region)).findFirst();
		Short regionId;
		if (matchingObject.isPresent()) {
			regionId = (Short) matchingObject.get()[0];
		} else {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid Region .");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put("RegionName", region);

		start++;
		String zone = getCellData(row, start);

		if (zone == null || zone.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Zone should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

//		List<Object[]> reactionList = demographyRepository.fetchAllActiveDemographsZone(regionId);
		List<Object[]> reactionList = new ArrayList<>();
		if(obj == null || obj.isEmpty()) {
			reactionList=demographyRepository.getActiveZones();
		}else {
			reactionList = demographyRepository.fetchAllActiveDemographsZone(regionId);
		}
		matchingObject = reactionList.stream().filter(p -> p[1].equals(zone)).findFirst();
		Short zoneId;
		if (matchingObject.isPresent()) {
			zoneId = (Short) matchingObject.get()[0];
		} else {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Zone (" + zone + ") is not Mapped with " + region);
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		json.put("ZoneName", zone);

		start++;
		String woreda = getCellData(row, start);
		if (woreda == null || woreda.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Woreda should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
//		List<Object[]> reactionList = new ArrayList<>();
		if(obj == null || obj.isEmpty()) {
			reactionList=demographyRepository.getActiveWoredas();
		}else {
			reactionList = demographyRepository.fetchAllActiveDemographsWoreda(regionId, zoneId);
		}
//		reactionList = demographyRepository.fetchAllActiveDemographsWoreda(regionId, zoneId);
		Short woredaId;

		matchingObject = reactionList.stream().filter(p -> p[1].equals(woreda)).findFirst();
		if (matchingObject.isPresent()) {
			woredaId = (Short) matchingObject.get()[0];
		} else {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Woreda  (" + woreda + ") is not Mapped with " + zone);
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put("WoredaName", woreda);
		start++;
		String kebele = getCellData(row, start);
		if (kebele == null || kebele.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Kebele should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		Integer kebeleId;
		List<DemographicEntity> reactionList1 = demographyRepository.fetchAllActiveDemographsKebele(woredaId);

		Optional<DemographicEntity> ent = reactionList1.stream().filter(p -> p.getDemographyName().equals(kebele))
				.findFirst();
		if (ent.isPresent()) {
			kebeleId = (Integer) ent.get().getDemographyId();
		} else {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Kebele  (" + kebele + ") is not Mapped with " + woreda);
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		json.put("KebeleName", kebele);

		start++;
		String locationDetails = getCellData(row, start);

		if (kebele.trim().equalsIgnoreCase("other") || woreda.trim().equalsIgnoreCase("other")) {

			if (!(locationDetails == null || locationDetails.trim().isEmpty())) {
				if (locationDetails.contains("."))
					locationDetails = locationDetails.substring(0, locationDetails.lastIndexOf("."));
				if (!locationDetails.matches("^[0-9a-zA-Z]+$")) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid location details .(Accept only alphanumeric.)");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

			} else {
				json.put(WrsisPortalConstant.ERR, true);
				json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide location details");
				json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
				return json;
			}

		}

		start++;
		String altitude = getCellData(row, start);
		if (altitude == null || altitude.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Altitude should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (!altitude.trim().matches("^\\d{0,4}(?:\\.\\d{0,1}){0,1}$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Altitude accepts only decimal values.Accept 1 digit after decimal.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (Double.valueOf(altitude.trim()) < 0.0) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Altitude should not be negative.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		start++;

		String latlongtype = getCellData(row, start);

		if (latlongtype == null || latlongtype.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Lat & Long type should not be blank.It should be either decimal or degree.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		boolean decimaldegree = latlongtype.trim().equalsIgnoreCase("decimal");

		start++;
		String longitude = getCellData(row, start);
		if (longitude == null || longitude.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Longitude should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		// longitide decimal or degree validation

		if (latlongtype.trim().equalsIgnoreCase("decimal")
				&& !longitude.matches("^[-+]?[0-9]{1,4}(\\.[0-9]{6,15})?$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide valid longitude(decimal).");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (latlongtype.trim().equalsIgnoreCase("degree")
				&& !longitude.matches("^[-+]?[0-9]{2,3}(\\.[0-9]{2,2}\\.[0-9]{2,2})?$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide valid longitude(degree).");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		start++;
		String latitude = getCellData(row, start);
		if (latitude == null || latitude.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Latitude should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (latlongtype.trim().equalsIgnoreCase("decimal")
				&& !latitude.matches("^[-+]?[0-9]{1,4}(\\.[0-9]{6,15})?$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide valid latitude(decimal).");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		if (latlongtype.trim().equalsIgnoreCase("degree")
				&& !latitude.matches("^[-+]?[0-9]{2,3}(\\.[0-9]{2,2}\\.[0-9]{2,2})?$")) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Please provide valid latitude(degree).");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		start++;
		String surveyDate = getCellData(row, start);
		if (surveyDate == null || surveyDate.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Survey Date should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		Date dtSuveyDate = convertStringToDate(surveyDate);
		if (dtSuveyDate == null) {
			dtSuveyDate = convertStringToDateForExcel(surveyDate);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		String dateStr = sdf.format(dtSuveyDate);
		surveyDate = dateStr;
		json.put("surveyDateId", surveyDate);
		if (dtSuveyDate == null) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid survey date.(Accept only dd/MM/YYYY)");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		boolean check = checkIfMoreToCurrentDate(dtSuveyDate, new Date(), 0);
		if (!check) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Survey Date should not exceed from current date.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		// season and survey date validation
		LocalDate localDate = dtSuveyDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		Integer seasonCheckCount = seasionMasterRepository.getSurveyAndSeasonMapCount(seasonId, month);
		if (seasonCheckCount == 0) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Survey Date(" + surveyDate + ") is not comming under given season(" + season + ").");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}

		start++;
		String contactedFarmer = getCellData(row, start);
		if (contactedFarmer == null || contactedFarmer.trim().isEmpty()) {
			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Contacted farmer should not be blank.");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		if (!(contactedFarmer.trim().equalsIgnoreCase("Yes") || contactedFarmer.trim().equalsIgnoreCase("No"))) {

			json.put(WrsisPortalConstant.ERR, true);
			json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid contacted farmer.(Accept only Yes/No)");
			json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
			return json;
		}
		String plantingDate="";
		String tilleringDate="";
		String observationDate="";
		if(contactedFarmer.trim().equalsIgnoreCase("Yes"))
		{
			start++;
			plantingDate = getCellData(row, start);
			if (!(plantingDate == null || plantingDate.trim().isEmpty())) {
				Date dtplantingDate = convertStringToDate(plantingDate);
				if (dtplantingDate == null) {
					dtplantingDate = convertStringToDateForExcel(plantingDate);
				}
				plantingDate = sdf.format(dtplantingDate);
				if (dtplantingDate == null) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid planting date.(Accept only dd/MM/YYYY)");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
				check = checkIfMoreToCurrentDate(dtplantingDate, new Date(), 0);
				if (!check) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Planting  Date should not exceed from current date.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

			}

			start++;
			tilleringDate = getCellData(row, start);
			if (!(tilleringDate == null || tilleringDate.trim().isEmpty())) {
				Date dttilleringDate = convertStringToDate(tilleringDate);
				if (dttilleringDate == null) {
					dttilleringDate = convertStringToDateForExcel(tilleringDate);
				}

				tilleringDate = sdf.format(dttilleringDate);
				if (dttilleringDate == null) {
					dttilleringDate = convertStringToDateForExcel(tilleringDate);
				}
				if (dttilleringDate == null) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid tillering date.(Accept only dd/MM/YYYY)");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
				check = checkIfMoreToCurrentDate(dttilleringDate, new Date(), 0);
				if (!check) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Tillering  Date should not exceed from current date.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

			}

			start++;
			observationDate = getCellData(row, start);

			if (!(observationDate == null || observationDate.trim().isEmpty())) {
				Date dtobservationDate = convertStringToDate(observationDate);
				if (dtobservationDate == null) {
					dtobservationDate = convertStringToDateForExcel(observationDate);
				}
				observationDate = sdf.format(dtobservationDate);
				if (dtobservationDate == null) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "Invalid first rust observation  date.(Accept only dd/MM/YYYY)");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}
				check = checkIfMoreToCurrentDate(dtobservationDate, new Date(), 0);
				if (!check) {
					json.put(WrsisPortalConstant.ERR, true);
					json.put(WrsisPortalConstant.MESSAGE_JSON, "First rust observation date should not exceed from current date.");
					json.put(WrsisPortalConstant.ROW_N_NO, row.getRowNum());
					return json;
				}

			}
			// date validations
		}
		
		// Rust Affected

		json.put("tilleringdate", tilleringDate);
		json.put("plantingDate", plantingDate);
		json.put("woredaId", woredaId);
		json.put("kebelworedaaddr", locationDetails);
		json.put("decimaldegree", decimaldegree);

		json.put("contactedFarmerId", (contactedFarmer.trim().equalsIgnoreCase("yes")) ? true : false);

		json.put("observationId", observationDate);
		json.put("latitudeId", latitude);
		json.put("seasonId", seasonId);

		json.put("zoneId", zoneId);
		json.put("altitudeId", altitude);

		json.put(WrsisPortalConstant.REGION_ID, regionId);
		json.put("longitudeId", longitude);
		json.put("kebeleId", kebeleId);

		return json;
	}

	public boolean checkIfMoreToCurrentDate(Date startDate, Date endDate, int days) {
		boolean check = true;
		try {

			// in milliseconds
			long diff = endDate.getTime() - startDate.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000) - days;

			if (diffSeconds < 0 || diffMinutes < 0 || diffHours < 0 || diffDays < 0) {
				check = false;
			}

		} catch (Exception e) {
			LOG.error("SurveyExcelParser::checkIfMoreToCurrentDate():" + e.getMessage());
			check = true;
		}
		return check;
	}

	public static Date convertStringToDate(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("SurveyExcelParser::convertStringToDate():" + ex.getMessage());
		}
		return date;
	}

	public static Date convertStringToDateForExcel(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("SurveyExcelParser::convertStringToDateForExcel():" + ex.getMessage());
		}
		return date;
	}

	public String getCellData(Row row, int columnIndex) {

		int cellType;
		try {
			cellType = row.getCell(columnIndex).getCellType();
		} catch (Exception e) {
			LOG.error("SurveyExcelParser::getCellData():" + e.getMessage());
			return "";// if no value selected
		}

		if (cellType == CELL_TYPE_STRING) {
			return row.getCell(columnIndex).getStringCellValue();
		}
		if (HSSFDateUtil.isCellDateFormatted(row.getCell(columnIndex))) {
			DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			return (row.getCell(columnIndex).getDateCellValue() != null)
					? dateFormat.format(row.getCell(columnIndex).getDateCellValue())
					: " ";
		}

		if (cellType == CELL_TYPE_NUMERIC) {
			return String.valueOf(row.getCell(columnIndex).getNumericCellValue());
		}

		if (cellType == CELL_TYPE_BLANK) {
			return "";
		}
		return "";
	}
}
