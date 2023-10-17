package com.csmpl.wrsis.webportal.excel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.FungicideEffectTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.entity.GrowthMasterEntity;
import com.csmpl.wrsis.webportal.entity.MoistureMasterEntity;
import com.csmpl.wrsis.webportal.entity.ReactionMasterEntity;
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
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;

public class DownloadSampleExcel {
	public static final Logger LOG = LoggerFactory.getLogger(DownloadSampleExcel.class);

	public HSSFWorkbook buildSuveyDetailsSampleExcel(RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository,Integer researchCenterId,List<Object[]> obj) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			
			// sheet creation
			Sheet sheet = workbook.createSheet("Actual");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			CellStyle textCellStyle = workbook.createCellStyle();
			textCellStyle.setDataFormat(fmt.getFormat("@"));

			// Create a Row
			// main header
			Row headerRow = sheet.createRow(0);

			Cell mainCell = headerRow.createCell(0);
			mainCell.setCellValue(WrsisPortalConstant.SL_NO);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 16));

			mainCell = headerRow.createCell(1);
			mainCell.setCellValue("Surveyor's Details");
			mainCell.setCellStyle(headerCellStyle);

			mainCell = headerRow.createCell(2);
			mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			// Create cells
			// sub headers
			Row row = sheet.createRow(1);

			// sub header headers
			Cell subCell = row.createCell(0);

			// Survey details sub rows.
			subCell = row.createCell(1);

			subCell = row.createCell(2);
			subCell.setCellValue(WrsisPortalConstant.SEASON_C);
			subCell.setCellStyle(headerCellStyle);

			CellRangeAddressList addressList = new CellRangeAddressList(3, 200, 2, 2);
			List<SeasionMasterEntity> seasionList = seasionMasterRepository.fetchAllActiveSeasion();
			String[] seasionArray = new String[seasionList.size()];
			int index = 0;
			for (SeasionMasterEntity sm : seasionList) {
				seasionArray[index] = sm.getVchSeason();
				index++;
			}

			DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(seasionArray);
			DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);
			sheet.addValidationData(dataValidation);

			subCell = row.createCell(3);
			subCell.setCellValue(WrsisPortalConstant.REGION);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 3, 3);
			// Region
			List<Object[]> dent = new ArrayList<>();

			if(researchCenterId == null) {
				dent = demographyRepository.getActiveRegions();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseRegion(researchCenterId);
			}
			String[] regionArray = new String[dent.size()];
			index = 0;
			for (Object[] sm : dent) {
				regionArray[index] = (String) sm[1];
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(regionArray);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Region.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(4);
			subCell.setCellValue(WrsisPortalConstant.ZONE);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 4, 4);
			// Zone
			Sheet hidden3 = workbook.createSheet("hidden3");
			if(researchCenterId == null) {
				dent = demographyRepository.getActiveZones();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseZone(researchCenterId);
			}
//			dent = demographyRepository.findByLevelIdByResearchCenterWiseZone(researchCenterId);

			String[] zoneArray = new String[dent.size()];
			index = 0;
			for (Object[] sm : dent) {
				zoneArray[index] = (String) sm[1];
				 Row row1 = hidden3.createRow(index);
				   Cell cell1 = row1.createCell(0);
				   cell1.setCellValue((String) sm[1]);
				index++;
			}

			 Name namedCell3 = workbook.createName();
			 namedCell3.setNameName("hidden3");//Sets the name of the named range 
			 namedCell3.setRefersToFormula("hidden3!$A$1:$A$" + zoneArray.length);//Sets the formula that the name is defined to refer to.
			 dvConstraint = DVConstraint.createFormulaListConstraint("hidden3");//referred to the name of the cell
			 dataValidation = new HSSFDataValidation(addressList, dvConstraint);// created list of institute names
			 workbook.setSheetHidden(1, true);
			 
			//dvConstraint = DVConstraint.createExplicitListConstraint(zoneArray);
			//dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Zone.");
			sheet.addValidationData(dataValidation);

			subCell = row.createCell(5);
			subCell.setCellValue(WrsisPortalConstant.WOREDA);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 5, 5);
			// Woreda
			Sheet hidden2 = workbook.createSheet("hidden2");
			if(researchCenterId == null) {
				dent = demographyRepository.getActiveWoredas();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseWoreda(researchCenterId);
			}
//			dent = demographyRepository.findByLevelIdByResearchCenterWiseWoreda(researchCenterId);

			String[] woredaArray = new String[dent.size()];
			index = 0;
			for (Object[] sm : dent) {
				woredaArray[index] = (String) sm[1];
				
				 Row row1 = hidden2.createRow(index);
				   Cell cell1 = row1.createCell(0);
				   cell1.setCellValue((String) sm[1]);
				   
				index++;
			}

			 Name namedCell2 = workbook.createName();
			 namedCell2.setNameName("hidden2");//Sets the name of the named range 
			 namedCell2.setRefersToFormula("hidden2!$A$1:$A$" + woredaArray.length);//Sets the formula that the name is defined to refer to.
			 dvConstraint = DVConstraint.createFormulaListConstraint("hidden2");//referred to the name of the cell
			 dataValidation = new HSSFDataValidation(addressList, dvConstraint);// created list of institute names
			 workbook.setSheetHidden(2, true);
			 
			//dvConstraint = DVConstraint.createExplicitListConstraint(woredaArray);
			//dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Woreda.");
			sheet.addValidationData(dataValidation);

			subCell = row.createCell(6);
			subCell.setCellValue(WrsisPortalConstant.KEBELE);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 6, 6);
			// Kebele
			Sheet hidden1 = workbook.createSheet("hidden1");
			if(researchCenterId == null) {
				dent = demographyRepository.getActiveKebeles();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseKebele(researchCenterId);
			}
//			dent = demographyRepository.findByLevelIdByResearchCenterWiseKebele(researchCenterId);

			String[] kebeleArray = new String[dent.size()];
			index = 0;
			for (Object[] sm : dent) {
				kebeleArray[index] = (String) sm[1];
				
				
				   Row row1 = hidden1.createRow(index);
				   Cell cell1 = row1.createCell(0);
				   cell1.setCellValue((String) sm[1]);
				   
				   index++;
			}
			
			 Name namedCell1 = workbook.createName();
			 namedCell1.setNameName("hidden1");//Sets the name of the named range 
			 namedCell1.setRefersToFormula("hidden1!$A$1:$A$" + kebeleArray.length);//Sets the formula that the name is defined to refer to.
			 dvConstraint = DVConstraint.createFormulaListConstraint("hidden1");//referred to the name of the cell
			 dataValidation = new HSSFDataValidation(addressList, dvConstraint);// created list of institute names
			 workbook.setSheetHidden(3, true);

			//dvConstraint = DVConstraint.createExplicitListConstraint(kebeleArray);
			//dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Kebele.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(7);
			subCell.setCellValue("Location Details");
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(8);
			subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(9);
			subCell.setCellValue("Lat &  Long Type");
			subCell.setCellStyle(headerCellStyle);

			String[] radio1 = { "Decimal", "Degree" };
			addressList = new CellRangeAddressList(3, 200, 9, 9);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio1);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Lat & Long Type should be either decimal or degree.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(10);
			subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(11);
			subCell.setCellValue(WrsisPortalConstant.LATITUDE);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(12);
			subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yy"));

			for (int i = 3; i < 200; i++) {
				Row r = null;
				Cell c = null;
				try {
					r = sheet.getRow(i);
					if (r == null) {
						r = sheet.createRow(i);
					}
					c = r.getCell(12);
					if (c == null) {
						c = r.createCell(12);
					}
					c.setCellStyle(textCellStyle);

				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e);
				}
			}
			
			subCell = row.createCell(13);
			subCell.setCellValue(WrsisPortalConstant.CONTACTED_FARMER);
			subCell.setCellStyle(headerCellStyle);

			String[] radio = { "Yes", "No" };
			addressList = new CellRangeAddressList(3, 200, 13, 13);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid contacted farmer.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(14);
			subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 3; i < 200; i++) {
				Row r = null;
				Cell c = null;
				try {
					r = sheet.getRow(i);
					if (r == null) {
						r = sheet.createRow(i);
					}
					c = r.getCell(14);
					if (c == null) {
						c = r.createCell(14);
					}
					c.setCellStyle(textCellStyle);

				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e.getMessage());
				}
			}

			subCell = row.createCell(15);
			subCell.setCellValue(" Date of Tillering(dd/mm/yyyy)");
			subCell.setCellStyle(headerCellStyle);

			for (int i = 3; i < 200; i++) {
				Row r = null;
				Cell c = null;
				try {
					r = sheet.getRow(i);
					if (r == null) {
						r = sheet.createRow(i);
					}
					c = r.getCell(15);
					if (c == null) {
						c = r.createCell(15);
					}
					c.setCellStyle(textCellStyle);

				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e);
				}
			}

			subCell = row.createCell(16);
			subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 3; i < 200; i++) {
				Row r = null;
				Cell c = null;
				try {
					r = sheet.getRow(i);
					if (r == null) {
						r = sheet.createRow(i);
					}
					c = r.getCell(16);
					if (c == null) {
						c = r.createCell(16);
					}
					c.setCellStyle(textCellStyle);

				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e.getMessage());
				}
			}

			// Site Information

			subCell = row.createCell(17);
			subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
			subCell.setCellStyle(headerCellStyle);

			List<SiteTypeMasterEntity> siteType = siteTypeRepository.fetchAllActiveSitetypes();

			addressList = new CellRangeAddressList(3, 200, 17, 17);
			// Regio

			String[] siteTypeA = new String[siteType.size()];
			index = 0;
			for (SiteTypeMasterEntity sm : siteType) {
				siteTypeA[index] = sm.getVchSiteName();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(siteTypeA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Survey Site.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(18);
			subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
			subCell.setCellStyle(headerCellStyle);
			List<WheatTypeMasterEntity> wheatType = wheatTypeRepository.fetchAllActiveWheattypes();
			addressList = new CellRangeAddressList(3, 200, 18, 18);
			// Region

			String[] wheatTypeA = new String[wheatType.size()];
			index = 0;
			for (WheatTypeMasterEntity sm : wheatType) {
				wheatTypeA[index] = sm.getVchDesc();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(wheatTypeA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Wheat Type.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(19);
			subCell.setCellValue("Variety");
			subCell.setCellStyle(headerCellStyle);

			List<VarietyTypeMasterEntity> variety = varietyTypeRepository.fetchActiveVarietyType();

			addressList = new CellRangeAddressList(3, 200, 19, 19);
			// Region

			String[] varietyA = new String[variety.size()];
			index = 0;
			for (VarietyTypeMasterEntity sm : variety) {
				varietyA[index] = sm.getVchDesc();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(varietyA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Variety.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(20);
			subCell.setCellValue("Other Variety");
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(21);
			subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 21, 21);
			// Region
			List<GrowthMasterEntity> growth = growthRepository.fetchAllActiveGrowth();
			String[] growthStageA = new String[growth.size()];
			index = 0;
			for (GrowthMasterEntity sm : growth) {
				growthStageA[index] = sm.getVchStage();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(growthStageA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Growth Stage.");

			sheet.addValidationData(dataValidation);

			subCell = row.createCell(22);
			subCell.setCellValue("Area");
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 22));

			mainCell = headerRow.createCell(17);
			mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
			mainCell.setCellStyle(headerCellStyle);

			// Rust Details

			subCell = row.createCell(23);
			subCell.setCellValue(WrsisPortalConstant.RUST_AFFECTED);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, 23, 23);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Rust Affected.");

			sheet.addValidationData(dataValidation);

			int runningCol = 23;
			int prevCol = runningCol;

			// fetch all active Rust types
			List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
			Row thirdRow = sheet.createRow(2);
			List<ReactionMasterEntity> reactions = reactionTypeRepository.fetchAllActiveReactionTypesWithoutRust();
			String[] reactionA = new String[reactions.size()];
			index = 0;
			for (ReactionMasterEntity sm : reactions) {
				reactionA[index] = sm.getVchDesc();
				index++;
			}

			for (int rust = 0; rust < rustTypeList.size(); rust++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(rustTypeList.get(rust).getVchRustType());
				subCell.setCellStyle(headerCellStyle);

				
				
				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 3);
				thirdCell.setCellValue(WrsisPortalConstant.REACTION);
				thirdCell.setCellStyle(headerCellStyle);
				
				addressList = new CellRangeAddressList(3, 200, runningCol + 3, runningCol + 3);
				dvConstraint = DVConstraint.createExplicitListConstraint(reactionA);
				dataValidation = new HSSFDataValidation(addressList, dvConstraint);
				dataValidation.setSuppressDropDownArrow(false);

				dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
				dataValidation.setEmptyCellAllowed(false);
				dataValidation.setShowPromptBox(true);
				dataValidation.setShowErrorBox(true);
				dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Reaction.");

				if (rustTypeList.get(rust).getIntRustTypeId() != 3) {

					sheet.addValidationData(dataValidation);

					sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
					runningCol = runningCol + 3;
				}
                 else
				{
					thirdCell = thirdRow.createCell(runningCol + 4);
					thirdCell.setCellValue("Head Incident");
					thirdCell.setCellStyle(headerCellStyle);

					thirdCell = thirdRow.createCell(runningCol + 5);
					thirdCell.setCellValue("Head Severity");
					thirdCell.setCellStyle(headerCellStyle);

					sheet.addValidationData(dataValidation);

					sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 1, runningCol + 5));
					runningCol = runningCol + 5;

				}
				
				
			}

			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.RUST_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			// Sample Details

			prevCol = runningCol + 1;

			runningCol++;
			subCell = row.createCell(prevCol);
			subCell.setCellValue(WrsisPortalConstant.SAMPLE_COLLECTED);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, prevCol, prevCol);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Rust Affected.");
			sheet.addValidationData(dataValidation);

			List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();

			for (int sample = 0; sample < sampleTypeList.size(); sample++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(sampleTypeList.get(sample).getSampleName());
				subCell.setCellStyle(headerCellStyle);

				sheet.addValidationData(dataValidation);

				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.NO_OF_SAMPLE);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellValue(WrsisPortalConstant.SAMPLE_ID1);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 3);
				thirdCell.setCellValue(WrsisPortalConstant.REMARKS);
				thirdCell.setCellStyle(headerCellStyle);

				sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
				runningCol = runningCol + 3;
			}

			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.SAMPLE_DETAILS2);
			mainCell.setCellStyle(headerCellStyle);

			// Fungicide Details
			prevCol = runningCol + 1;

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);
			subCell.setCellStyle(headerCellStyle);

			String[] fungArr = { "Yes", "No", "I Don't Know" };

			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			dvConstraint = DVConstraint.createExplicitListConstraint(fungArr);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Fungicide Applied.");
			sheet.addValidationData(dataValidation);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_USED);
			subCell.setCellStyle(headerCellStyle);

			List<FungicideMaster> fungi = fungicideMasterRepository.fetchAllActiveFungicides();

			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			// Region
			String[] fungiA = new String[fungi.size()];
			index = 0;
			for (FungicideMaster sm : fungi) {
				fungiA[index] = sm.getFungicideName();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(fungiA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Fungicide Used.");

			sheet.addValidationData(dataValidation);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue("Other Fungicide");
			subCell.setCellStyle(headerCellStyle);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue("Dose");
			subCell.setCellStyle(headerCellStyle);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.SPRAY_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 3; i < 200; i++) {
				Row r = null;
				Cell c = null;
				try {
					r = sheet.getRow(i);
					if (r == null) {
						r = sheet.createRow(i);
					}
					c = r.getCell(runningCol);
					if (c == null) {
						c = r.createCell(runningCol);
					}
					c.setCellStyle(textCellStyle);

				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e);
				}
			}

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.EFFECTIVENESS);
			subCell.setCellStyle(headerCellStyle);

			List<FungicideEffectTypeMasterEntity> fungieffect = fungiEffectTypeRepository.fetchAllActiveFungiEffectTypes();
			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			// Region
			String[] effectA = new String[fungieffect.size()];
			index = 0;
			for (FungicideEffectTypeMasterEntity sm : fungieffect) {
				effectA[index] = sm.getVchDesc();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(effectA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Effectiveness.");
			sheet.addValidationData(dataValidation);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.FUNGICIDE_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			prevCol = runningCol + 1;

			// Other Disease

			
			
			CellStyle unlockedCellStyle = workbook.createCellStyle();
			unlockedCellStyle.setLocked(false);

			List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
			for (int disease = 0; disease < diseaseList.size(); disease++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
				subCell.setCellStyle(headerCellStyle);

				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellStyle(headerCellStyle);

				boolean isSevReq = diseaseList.get(disease).getSeverityRequired();

				if (isSevReq) {
					addressList = new CellRangeAddressList(3, 200, runningCol + 2, runningCol + 2);
					if (diseaseList.get(disease).getDiseaseName().trim().equalsIgnoreCase("septoria")) {
						thirdCell.setCellValue("Severity(00-" + diseaseList.get(disease).getSeverityMax() + ")");
					} else {
						thirdCell.setCellValue("Severity(" + diseaseList.get(disease).getSeverityMin() + "-"
								+ diseaseList.get(disease).getSeverityMax() + ")");
					}
				} else {
					thirdCell.setCellValue("");
					sheet.setColumnHidden(runningCol + 2, true);

				}

				// severity cols disable

				sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
				runningCol = runningCol + 2;
			}

			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
			mainCell.setCellStyle(headerCellStyle);

			// Other Details
			prevCol = runningCol + 1;
			runningCol += 1;
			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.IRRIGATED);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Irrigated.");
			sheet.addValidationData(dataValidation);

			runningCol++;
			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.WEED_CONTROL);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			dvConstraint = DVConstraint.createExplicitListConstraint(radio);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Weed Control.");
			sheet.addValidationData(dataValidation);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.SOIL_COLOR);
			subCell.setCellStyle(headerCellStyle);

			List<SoilColorMasterEntity> color = colorRepository.fetchAllSoilActiveColors();
			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			// Region
			String[] soilA = new String[color.size()];
			index = 0;
			for (SoilColorMasterEntity sm : color) {
				soilA[index] = sm.getVchSoilColor();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(soilA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Soil Color.");

			sheet.addValidationData(dataValidation);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.MOISTURE);
			subCell.setCellStyle(headerCellStyle);

			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			// Region
			List<MoistureMasterEntity> misture = moistureRepository.fetchAllActiveMoistures();
			String[] moistureA = new String[misture.size()];
			index = 0;
			for (MoistureMasterEntity sm : misture) {
				moistureA[index] = sm.getVchMoisture();
				index++;
			}

			dvConstraint = DVConstraint.createExplicitListConstraint(moistureA);
			dataValidation = new HSSFDataValidation(addressList, dvConstraint);
			dataValidation.setSuppressDropDownArrow(false);

			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setEmptyCellAllowed(false);
			dataValidation.setShowPromptBox(true);
			dataValidation.setShowErrorBox(true);
			dataValidation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Moisture.");

			sheet.addValidationData(dataValidation);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.OTHER_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			mainCell = headerRow.createCell(runningCol + 1);
			mainCell.setCellValue("Additional comments or Observation");
			mainCell.setCellStyle(headerCellStyle);
			
			//Institute Name
			
			prevCol = runningCol + 1;
			runningCol += 1;
			
			mainCell = headerRow.createCell(runningCol+1);
			mainCell.setCellValue("Institute Name");
			mainCell.setCellStyle(headerCellStyle);
			
			Cell thirdCell1 = thirdRow.createCell(runningCol + 1);
			thirdCell1.setCellValue("");
			thirdCell1.setCellStyle(headerCellStyle);
			
			Cell thirdCell2 = thirdRow.createCell(runningCol + 1);
			thirdCell2.setCellValue("");
			thirdCell2.setCellStyle(headerCellStyle);
			runningCol++;

			
			addressList = new CellRangeAddressList(3, 200, runningCol, runningCol);
			Sheet hidden = workbook.createSheet("hidden");//created hidden sheet
			
			String[] instituteNames = new String[obj.size()];
			index = 0;
			for (Object[] objects : obj) {//added research center names from obj to instituteNames array
				instituteNames[index] = (String) objects[1];
				index++;
			}
			for (int i = 0; i < instituteNames.length; i++) {//set institute names to cells in hidden sheet
				   String name = instituteNames[i];
				   Row row1 = hidden.createRow(i);
				   Cell cell1 = row1.createCell(0);
				   cell1.setCellValue(name);
				 }
			
			 Name namedCell = workbook.createName();
			 namedCell.setNameName("hidden");//Sets the name of the named range 
			 namedCell.setRefersToFormula("hidden!$A$1:$A$" + instituteNames.length);//Sets the formula that the name is defined to refer to.
			 DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");//referred to the name of the cell
			 HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);// created list of institute names
			 workbook.setSheetHidden(4, true);
			 validation.setSuppressDropDownArrow(false);

			 validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			 validation.setEmptyCellAllowed(false);
			 validation.setShowPromptBox(true);
			 validation.setShowErrorBox(true);
			 validation.createErrorBox(WrsisPortalConstant.INVALID_DATA, "Please provide valid Institution.");

			sheet.addValidationData(validation);
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
			// ****************************************************************************

			// create a sample sheet

			// ****************************************************************************

			Sheet sampleSheet = workbook.createSheet("Sample Sheet");

			// Create a Row
			// main header
			headerRow = sampleSheet.createRow(0);
			Row dataRow = sampleSheet.createRow(3);

			mainCell = headerRow.createCell(0);
			mainCell.setCellValue(WrsisPortalConstant.SL_NO);
			mainCell.setCellStyle(headerCellStyle);

			Cell dataCel = dataRow.createCell(0);
			dataCel.setCellValue(1);

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 16));

			mainCell = headerRow.createCell(1);
			mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			mainCell = headerRow.createCell(2);
			mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			// Create cells
			// sub headers
			row = sampleSheet.createRow(1);

			// sub header headers

			// Survey details sub rows.

			subCell = row.createCell(2);
			subCell.setCellValue(WrsisPortalConstant.SEASON_C);
			subCell.setCellStyle(headerCellStyle);

			// plot season master
			// season

			for (int i = 0; i < seasionList.size(); i++) {
				if (i == 0) {
					SeasionMasterEntity entity = seasionList.get(i);
					dataCel = dataRow.createCell(2);
					dataCel.setCellValue(entity.getVchSeason());
				} else {
					SeasionMasterEntity entity = seasionList.get(i);
					Row dataRow1 = sampleSheet.createRow(3 + i);
					dataCel = dataRow1.createCell(2);
					dataCel.setCellValue(entity.getVchSeason());
				}

			}
			// Region

			for (int i = 0; i < regionArray.length; i++) {
				if (i == 0) {
					dataCel = dataRow.createCell(3);
					dataCel.setCellValue(regionArray[i]);
				} else {

					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(3);
					dataCel.setCellValue(regionArray[i]);
				}

			}

			// zone

			if(researchCenterId==null) {
				dent = demographyRepository.getActiveZones();
			}else {
			dent = demographyRepository.findByLevelIdByResearchCenterWiseRegion(researchCenterId);
			}
			
			for (int i = 0; i < dent.size(); i++) {
				if (i == 0) {
					Object[] entity = dent.get(i);
					dataCel = dataRow.createCell(4);
					dataCel.setCellValue((String) entity[1]);
				} else {
					Object[] entity = dent.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(4);
					dataCel.setCellValue((String) entity[1]);
				}

			}

			// woreda
			if(researchCenterId==null) {
				dent = demographyRepository.getActiveZones();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseWoreda(researchCenterId);
			}
			

			for (int i = 0; i < dent.size(); i++) {
				if (i == 0) {
					Object[] entity = dent.get(i);
					dataCel = dataRow.createCell(5);
					dataCel.setCellValue((String) entity[1]);
				} else {
					Object[] entity = dent.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(5);
					dataCel.setCellValue((String) entity[1]);
				}

			}

			// kebele
			if(researchCenterId==null) {
				dent = demographyRepository.getActiveZones();
			}else {
				dent = demographyRepository.findByLevelIdByResearchCenterWiseKebele(researchCenterId);
			}
			

			for (int i = 0; i < dent.size(); i++) {
				if (i == 0) {
					Object[] entity = dent.get(i);
					dataCel = dataRow.createCell(6);
					dataCel.setCellValue((String) entity[1]);
				} else {
					Object[] entity = dent.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(6);
					dataCel.setCellValue((String) entity[1]);
				}

			}

			subCell = row.createCell(3);
			subCell.setCellValue(WrsisPortalConstant.REGION);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(4);
			subCell.setCellValue(WrsisPortalConstant.ZONE);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(5);
			subCell.setCellValue(WrsisPortalConstant.WOREDA);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(6);
			subCell.setCellValue(WrsisPortalConstant.KEBELE);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(7);
			subCell.setCellValue("Location Details");
			subCell.setCellStyle(headerCellStyle);
			dataCel = dataRow.createCell(7);
			dataCel.setCellValue("Bhubaneswar,Rajsunakhala");

			subCell = row.createCell(8);
			subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(1);
			dataCel.setCellValue("Rakesh,Jhon,Amstrong");

			dataCel = dataRow.createCell(8);
			dataCel.setCellValue("20.20");

			subCell = row.createCell(9);
			subCell.setCellValue("Lat & Long Type");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(9);
			dataCel.setCellValue("decimal(12.21)/degree(12.32.12)");

			subCell = row.createCell(10);
			subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(10);
			dataCel.setCellValue("20.20");

			subCell = row.createCell(11);
			subCell.setCellValue(WrsisPortalConstant.LATITUDE);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(11);
			dataCel.setCellValue("20.20");

			subCell = row.createCell(12);
			subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(12);
			dataCel.setCellValue(WrsisPortalConstant.DATE_12_01_2020);
			
			subCell = row.createCell(13);
			subCell.setCellValue(WrsisPortalConstant.CONTACTED_FARMER);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(13);
			dataCel.setCellValue(WrsisPortalConstant.YES_NO);

			subCell = row.createCell(14);
			subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(14);
			dataCel.setCellValue(WrsisPortalConstant.DATE_12_01_2020);

			subCell = row.createCell(15);
			subCell.setCellValue("Date Of Tillering(dd/mm/yyyy)");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(15);
			dataCel.setCellValue(WrsisPortalConstant.DATE_12_01_2020);

			subCell = row.createCell(16);
			subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(16);
			dataCel.setCellValue(WrsisPortalConstant.DATE_12_01_2020);

			// Site Information

			subCell = row.createCell(17);
			subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < siteType.size(); i++) {
				if (i == 0) {
					SiteTypeMasterEntity entity = siteType.get(i);
					dataCel = dataRow.createCell(17);
					dataCel.setCellValue(entity.getVchSiteName());
				} else {
					SiteTypeMasterEntity entity = siteType.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(17);
					dataCel.setCellValue(entity.getVchSiteName());
				}

			}

			subCell = row.createCell(18);
			subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < wheatType.size(); i++) {
				if (i == 0) {
					WheatTypeMasterEntity entity = wheatType.get(i);
					dataCel = dataRow.createCell(18);
					dataCel.setCellValue(entity.getVchDesc());
				} else {
					WheatTypeMasterEntity entity = wheatType.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(18);
					dataCel.setCellValue(entity.getVchDesc());
				}

			}

			subCell = row.createCell(19);
			subCell.setCellValue("Variety");
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < variety.size(); i++) {
				if (i == 0) {
					VarietyTypeMasterEntity entity = variety.get(i);
					dataCel = dataRow.createCell(19);
					dataCel.setCellValue(entity.getVchDesc());
				} else {
					VarietyTypeMasterEntity entity = variety.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(19);
					dataCel.setCellValue(entity.getVchDesc());
				}

			}

			subCell = row.createCell(20);
			subCell.setCellValue("Other Variety");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(20);
			dataCel.setCellValue("Sample Other Variety");

			subCell = row.createCell(21);
			subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < growth.size(); i++) {
				if (i == 0) {
					GrowthMasterEntity entity = growth.get(i);
					dataCel = dataRow.createCell(21);
					dataCel.setCellValue(entity.getVchStage());
				} else {
					GrowthMasterEntity entity = growth.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(3 + i);
					}
					dataCel = dataRow1.createCell(21);
					dataCel.setCellValue(entity.getVchStage());
				}

			}

			subCell = row.createCell(22);
			subCell.setCellValue("Area");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(22);
			dataCel.setCellValue("20.63");

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 22));

			mainCell = headerRow.createCell(17);
			mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
			mainCell.setCellStyle(headerCellStyle);

			// Rust Details

			subCell = row.createCell(23);
			subCell.setCellValue(WrsisPortalConstant.RUST_AFFECTED);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(23);
			dataCel.setCellValue("Yes/NO");

			runningCol = 23;
			prevCol = runningCol;

			// fetch all active Rust types
			thirdRow = sampleSheet.createRow(2);
			for (int rust = 0; rust < rustTypeList.size(); rust++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(rustTypeList.get(rust).getVchRustType());
				subCell.setCellStyle(headerCellStyle);

				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 1);
				dataCel.setCellValue("10");

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 2);
				dataCel.setCellValue("15");

				thirdCell = thirdRow.createCell(runningCol + 3);
				thirdCell.setCellValue(WrsisPortalConstant.REACTION);
				thirdCell.setCellStyle(headerCellStyle);

				for (int i = 0; i < reactions.size(); i++) {
					if (i == 0) {
						ReactionMasterEntity entity = reactions.get(i);
						dataCel = dataRow.createCell(runningCol + 3);
						dataCel.setCellValue(entity.getVchDesc());
					} else {
						ReactionMasterEntity entity = reactions.get(i);
						Row dataRow1 = sampleSheet.getRow(3 + i);
						if (dataRow1 == null) {
							dataRow1 = sampleSheet.createRow(3 + i);
						}
						dataCel = dataRow1.createCell(runningCol + 3);
						dataCel.setCellValue(entity.getVchDesc());
					}

				}
				
				
				if (rustTypeList.get(rust).getIntRustTypeId() != 3) {

					sampleSheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
					runningCol = runningCol + 3;
				} else
				{
					thirdCell = thirdRow.createCell(runningCol + 4);
					thirdCell.setCellValue("Head Incident");
					thirdCell.setCellStyle(headerCellStyle);
					
					dataCel = dataRow.createCell(runningCol + 4);
					dataCel.setCellValue("75");

					thirdCell = thirdRow.createCell(runningCol + 5);
					thirdCell.setCellValue("Head Severity");
					thirdCell.setCellStyle(headerCellStyle);
					
					dataCel = dataRow.createCell(runningCol + 5);
					dataCel.setCellValue("4");

					sampleSheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 1, runningCol + 5));
					runningCol = runningCol + 5;

				}

				
			}

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.RUST_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			// Sample Details

			prevCol = runningCol + 1;

			runningCol++;
			subCell = row.createCell(prevCol);
			subCell.setCellValue(WrsisPortalConstant.SAMPLE_COLLECTED);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(prevCol);
			dataCel.setCellValue(WrsisPortalConstant.YES_NO);

			for (int sample = 0; sample < sampleTypeList.size(); sample++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(sampleTypeList.get(sample).getSampleName());
				subCell.setCellStyle(headerCellStyle);

				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.NO_OF_SAMPLE);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 1);
				dataCel.setCellValue("2");

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellValue(WrsisPortalConstant.SAMPLE_ID1);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 2);
				dataCel.setCellValue("2,5");

				thirdCell = thirdRow.createCell(runningCol + 3);
				thirdCell.setCellValue(WrsisPortalConstant.REMARKS);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 3);
				dataCel.setCellValue("Sample Remark");

				sampleSheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
				runningCol = runningCol + 3;
			}

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.SAMPLE_DETAILS2);
			mainCell.setCellStyle(headerCellStyle);

			// Fungicide Details
			prevCol = runningCol + 1;

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue("Yes/No/I Don't Know");

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_USED);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < fungi.size(); i++) {
				if (i == 0) {
					FungicideMaster entity = fungi.get(i);
					dataCel = dataRow.createCell(runningCol);
					dataCel.setCellValue(entity.getFungicideName());
				} else {
					FungicideMaster entity = fungi.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(runningCol);
					}
					dataCel = dataRow1.createCell(runningCol);
					dataCel.setCellValue(entity.getFungicideName());
				}

			}

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue("Other Fungicide");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue("Sample Other Fungicide");

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue("Dose");
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue("23");

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.SPRAY_DATE_DD_MM_YYYY);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue("12/02/2020");

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.EFFECTIVENESS);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < fungieffect.size(); i++) {
				if (i == 0) {
					FungicideEffectTypeMasterEntity entity = fungieffect.get(i);
					dataCel = dataRow.createCell(runningCol);
					dataCel.setCellValue(entity.getVchDesc());
				} else {
					FungicideEffectTypeMasterEntity entity = fungieffect.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(runningCol);
					}
					dataCel = dataRow1.createCell(runningCol);
					dataCel.setCellValue(entity.getVchDesc());
				}

			}

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.FUNGICIDE_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			prevCol = runningCol + 1;

			// Other Disease

			for (int disease = 0; disease < diseaseList.size(); disease++) {
				subCell = row.createCell(runningCol + 1);
				subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
				subCell.setCellStyle(headerCellStyle);

				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
				thirdCell.setCellStyle(headerCellStyle);

				dataCel = dataRow.createCell(runningCol + 1);
				dataCel.setCellValue("21");

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellStyle(headerCellStyle);

				boolean isSevReq = diseaseList.get(disease).getSeverityRequired();

				if (isSevReq) {
					addressList = new CellRangeAddressList(3, 200, runningCol + 2, runningCol + 2);
					if (diseaseList.get(disease).getDiseaseName().trim().equalsIgnoreCase("septoria")) {
						thirdCell.setCellValue("Severity(00-" + diseaseList.get(disease).getSeverityMax() + ")");
					} else {
						thirdCell.setCellValue("Severity(" + diseaseList.get(disease).getSeverityMin() + "-"
								+ diseaseList.get(disease).getSeverityMax() + ")");
					}

					dataCel = dataRow.createCell(runningCol + 1);
					dataCel.setCellValue(String.valueOf(diseaseList.get(disease).getSeverityMin() + 1));
				} else {
					thirdCell.setCellValue("");
					sampleSheet.setColumnHidden(runningCol + 2, true);

				}

				sampleSheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
				runningCol = runningCol + 2;
			}

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
			mainCell.setCellStyle(headerCellStyle);

			// Other Details
			prevCol = runningCol + 1;
			runningCol += 1;
			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.IRRIGATED);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue(WrsisPortalConstant.YES_NO);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.WEED_CONTROL);
			subCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol);
			dataCel.setCellValue(WrsisPortalConstant.YES_NO);

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.SOIL_COLOR);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < color.size(); i++) {
				if (i == 0) {
					SoilColorMasterEntity entity = color.get(i);
					dataCel = dataRow.createCell(runningCol);
					dataCel.setCellValue(entity.getVchSoilColor());
				} else {
					SoilColorMasterEntity entity = color.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(runningCol);
					}
					dataCel = dataRow1.createCell(runningCol);
					dataCel.setCellValue(entity.getVchSoilColor());
				}

			}

			runningCol++;

			subCell = row.createCell(runningCol);
			subCell.setCellValue(WrsisPortalConstant.MOISTURE);
			subCell.setCellStyle(headerCellStyle);

			for (int i = 0; i < misture.size(); i++) {
				if (i == 0) {
					MoistureMasterEntity entity = misture.get(i);
					dataCel = dataRow.createCell(runningCol);
					dataCel.setCellValue(entity.getVchMoisture());
				} else {
					MoistureMasterEntity entity = misture.get(i);
					Row dataRow1 = sampleSheet.getRow(3 + i);
					if (dataRow1 == null) {
						dataRow1 = sampleSheet.createRow(runningCol);
					}
					dataCel = dataRow1.createCell(runningCol);
					dataCel.setCellValue(entity.getVchMoisture());
				}

			}

			sampleSheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

			mainCell = headerRow.createCell(prevCol);
			mainCell.setCellValue(WrsisPortalConstant.OTHER_DETAILS);
			mainCell.setCellStyle(headerCellStyle);

			mainCell = headerRow.createCell(runningCol + 1);
			mainCell.setCellValue("Additional comments or Observation");
			mainCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol + 1);
			dataCel.setCellValue("Sample Remark");

			mainCell = headerRow.createCell(runningCol + 1);
			mainCell.setCellValue("Institute Name");
			mainCell.setCellStyle(headerCellStyle);

			dataCel = dataRow.createCell(runningCol + 1);
			dataCel.setCellValue("Ambo Agriculture Research center");
			runningCol++;

			// 50 columns set auto size
			for (int i = 0; i < 200; i++) {
				sampleSheet.autoSizeColumn(i);
			}

			// 50 columns set auto size
			for (int i = 0; i < 200; i++) {
				sheet.autoSizeColumn(i);
			}

			for (int i = 0; i <= runningCol; i++) {
				for (int j = 0; j < 3; j++)// row
				{
					Row r = sampleSheet.getRow(j);
					Cell c = r.getCell(i);
					Row r1 = sheet.getRow(j);
					Cell c1 = r1.getCell(i);
					try {
						c.setCellStyle(headerCellStyle);
					} catch (Exception e) {
						c = r.createCell(i);
						c.setCellStyle(headerCellStyle);
						 
					}
					try {
						c1.setCellStyle(headerCellStyle);
					} catch (Exception e) {
						 
						c1 = r1.createCell(i);
						c1.setCellStyle(headerCellStyle);
					}
				}
			}

			// sub of rust details
runningCol++;
			for (int i = 0; i <= runningCol; i++) {
				try {
					sampleSheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
				} catch (Exception e) {
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e);
					continue;

				}
			}
		} catch (Exception e) {
			LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcel():" + e);
		}
		return workbook;

	}

	// Plot data

	public HSSFWorkbook buildSuveyDetailsSampleExcelPlotData(HttpSession session, RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository, Integer researchCenterId) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		// sheet creation
		Sheet sheet = workbook.createSheet(WrsisPortalConstant.SURVEY_DATA);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbook.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		// Create a Row
		// main header
		Row headerRow = sheet.createRow(0);

		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellValue(WrsisPortalConstant.SL_NO);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));

		mainCell = headerRow.createCell(1);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEY_NUMBER);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
		mainCell = headerRow.createCell(2);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEYOR_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		// Create cells
		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		// Surveyor details sub rows.

		subCell = row.createCell(2);
		subCell.setCellValue(WrsisPortalConstant.SURVEYOR_NAME);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(3);
		subCell.setCellValue(WrsisPortalConstant.RESEARCH_CENTER);
		subCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 16));
		mainCell = headerRow.createCell(4);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Survey details sub rows.

		subCell = row.createCell(4);
		subCell.setCellValue(WrsisPortalConstant.SEASON_C);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(5);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);
		
		subCell = row.createCell(9);//modify new add subcell and its ploting value
		subCell.setCellValue("Location Name");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(10);
		subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(11);
		subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(12);
		subCell.setCellValue(WrsisPortalConstant.LATITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(13);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(14);
		subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(15);
		subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(16);
		subCell.setCellValue(WrsisPortalConstant.CONTACTED_FARMER);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(17);
		subCell.setCellValue(WrsisPortalConstant.REMARKS);
		subCell.setCellStyle(headerCellStyle);

		// Site Information Header
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 22));
		mainCell = headerRow.createCell(18);
		mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
		mainCell.setCellStyle(headerCellStyle);

		// Site Information Sub Header
		subCell = row.createCell(18);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(19);
		subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(20);
		subCell.setCellValue("Variety");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(21);
		subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(22);
		subCell.setCellValue("Area");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(23);
		subCell.setCellValue(WrsisPortalConstant.RUST_AFFECTED);
		subCell.setCellStyle(headerCellStyle);

		int runningCol = 23;
		int prevCol = runningCol;

		// fetch all active Rust types
		List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();

		Row thirdRow = sheet.createRow(2);

		for (int rust = 0; rust < rustTypeList.size(); rust++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(rustTypeList.get(rust).getVchRustType());
			subCell.setCellStyle(headerCellStyle);

			if(rustTypeList.get(rust).getIntRustTypeId() == 1 || rustTypeList.get(rust).getIntRustTypeId() == 2) {
			
			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue(WrsisPortalConstant.REACTION);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;
			
			}else if(rustTypeList.get(rust).getIntRustTypeId() == 3) {
				
				Cell thirdCell = thirdRow.createCell(runningCol + 1);
				thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 2);
				thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 3);
				thirdCell.setCellValue(WrsisPortalConstant.REACTION);
				thirdCell.setCellStyle(headerCellStyle);
				
				thirdCell = thirdRow.createCell(runningCol + 4);
				thirdCell.setCellValue("Head Incident");
				thirdCell.setCellStyle(headerCellStyle);

				thirdCell = thirdRow.createCell(runningCol + 5);
				thirdCell.setCellValue("Head Severity");
				thirdCell.setCellStyle(headerCellStyle);

				sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 5 - 4, runningCol + 5));
				runningCol = runningCol + 5;
				
			}
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol + 1, runningCol));
		mainCell = headerRow.createCell(prevCol + 1);
		mainCell.setCellValue(WrsisPortalConstant.RUST_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Sample Details

		prevCol = runningCol + 1;

		runningCol++;
		subCell = row.createCell(prevCol);
		subCell.setCellValue(WrsisPortalConstant.SAMPLE_COLLECTED);
		subCell.setCellStyle(headerCellStyle);

		List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();

		for (int sample = 0; sample < sampleTypeList.size(); sample++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(sampleTypeList.get(sample).getSampleName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.NO_OF_SAMPLE);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SAMPLE_ID1);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue(WrsisPortalConstant.REMARKS);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.SAMPLE_DETAILS2);
		mainCell.setCellStyle(headerCellStyle);

		// Fungicide Details
		prevCol = runningCol + 1;
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);
		subCell.setCellStyle(headerCellStyle);

		runningCol++;

		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_USED);
		subCell.setCellStyle(headerCellStyle);

		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue("Dose");
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SPRAY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.EFFECTIVENESS);
		subCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.FUNGICIDE_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		prevCol = runningCol + 1;

		// Other Disease
		List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
		for (int disease = 0; disease < diseaseList.size(); disease++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
			runningCol = runningCol + 2;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
		mainCell.setCellStyle(headerCellStyle);

		// Other Details
		prevCol = runningCol + 1;
		runningCol += 1;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.IRRIGATED);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.WEED_CONTROL);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SOIL_COLOR);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.MOISTURE);
		subCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		prevCol = runningCol + 1;

		// 50 columns set auto size
		for (int i = 0; i < 200; i++) {
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i <= runningCol; i++) {
			for (int j = 0; j < 3; j++)// row
			{
				Row r1 = sheet.getRow(j);
				Cell c1 = r1.getCell(i);
				try {
					c1.setCellStyle(headerCellStyle);
				} catch (Exception e) {
					c1 = r1.createCell(i);
					c1.setCellStyle(headerCellStyle);
					LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcelPlotData():" + e);
				}
			}
		}

		// sub of rust details

		for (int i = 0; i <= runningCol; i++) {
			try {
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
			} catch (Exception e) {
				LOG.error("DownloadSampleExcel::buildSuveyDetailsSampleExcelPlotData():" + e);
				continue;

			}
		}

		return workbook;

	}

	// Plot Data Global Rust Report Header Excel

	public HSSFWorkbook buildGlobalRustExcelPlotData(HttpSession session, RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository, Integer researchCenterId) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		// sheet creation
		Sheet sheet = workbook.createSheet(WrsisPortalConstant.SURVEY_DATA);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbook.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		// Create a Row
		// main header
		Row headerRow = sheet.createRow(0);

		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellValue(WrsisPortalConstant.SL_NO);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));

		mainCell = headerRow.createCell(1);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEY_NUMBER);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
		mainCell = headerRow.createCell(2);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEYOR_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		// Create cells
		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		// Surveyor details sub rows.

		subCell = row.createCell(2);
		subCell.setCellValue(WrsisPortalConstant.COUNTRY1);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(3);
		subCell.setCellValue(WrsisPortalConstant.SURVEYOR_NAME);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(4);
		subCell.setCellValue(WrsisPortalConstant.RESEARCH_CENTER);
		subCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 18));
		mainCell = headerRow.createCell(5);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Survey details sub rows.

		subCell = row.createCell(5);
		subCell.setCellValue(WrsisPortalConstant.SEASON_C);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(9);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(10);
		subCell.setCellValue("Location Name");
		subCell.setCellStyle(headerCellStyle);
		
		subCell = row.createCell(11);
		subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(12);
		subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(13);
		subCell.setCellValue(WrsisPortalConstant.LATITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(14);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(15);
		subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(16);
		subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(17);
		subCell.setCellValue(WrsisPortalConstant.CONTACTED_FARMER);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(18);
		subCell.setCellValue(WrsisPortalConstant.REMARKS);
		subCell.setCellStyle(headerCellStyle);

		// Site Information Header
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 19, 23));
		mainCell = headerRow.createCell(19);
		mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
		mainCell.setCellStyle(headerCellStyle);

		// Site Information Sub Header
		subCell = row.createCell(19);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(20);
		subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(21);
		subCell.setCellValue("Variety");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(22);
		subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(23);
		subCell.setCellValue("Area");
		subCell.setCellStyle(headerCellStyle);

		int runningCol = 23;
		int prevCol = runningCol;

		// fetch all active Rust types
		List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();

		Row thirdRow = sheet.createRow(2);

		for (int rust = 0; rust < rustTypeList.size(); rust++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(rustTypeList.get(rust).getVchRustType());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue(WrsisPortalConstant.REACTION);
			thirdCell.setCellStyle(headerCellStyle);

			if(rustTypeList.get(rust).getIntRustTypeId() !=3) {
			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;	
			}else {
				
			thirdCell = thirdRow.createCell(runningCol + 4);
			thirdCell.setCellValue("Head Incident");
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 5);
			thirdCell.setCellValue("Head Severity");
			thirdCell.setCellStyle(headerCellStyle);	
			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 5 - 4, runningCol + 5));
			runningCol = runningCol + 5;	
			}
			
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol + 1, runningCol));
		mainCell = headerRow.createCell(prevCol + 1);
		mainCell.setCellValue(WrsisPortalConstant.RUST_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Sample Details

		prevCol = runningCol + 1;

		runningCol++;
		subCell = row.createCell(prevCol);
		subCell.setCellValue(WrsisPortalConstant.SAMPLE_COLLECTED);
		subCell.setCellStyle(headerCellStyle);

		List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();

		for (int sample = 0; sample < sampleTypeList.size(); sample++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(sampleTypeList.get(sample).getSampleName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.NO_OF_SAMPLE);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SAMPLE_ID1);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue(WrsisPortalConstant.REMARKS);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.SAMPLE_DETAILS2);
		mainCell.setCellStyle(headerCellStyle);

		// Fungicide Details
		prevCol = runningCol + 1;
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);
		subCell.setCellStyle(headerCellStyle);

		runningCol++;

		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_USED);
		subCell.setCellStyle(headerCellStyle);

		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue("Dose");
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SPRAY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.EFFECTIVENESS);
		subCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.FUNGICIDE_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		prevCol = runningCol + 1;

		// Other Disease
		List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
		for (int disease = 0; disease < diseaseList.size(); disease++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
			runningCol = runningCol + 2;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
		mainCell.setCellStyle(headerCellStyle);

		// Other Details
		prevCol = runningCol + 1;
		runningCol += 1;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.IRRIGATED);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.WEED_CONTROL);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SOIL_COLOR);
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.MOISTURE);
		subCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		prevCol = runningCol + 1;

		// 50 columns set auto size
		for (int i = 0; i < 200; i++) {
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i <= runningCol; i++) {
			for (int j = 0; j < 3; j++)// row
			{
				Row r1 = sheet.getRow(j);
				Cell c1 = r1.getCell(i);
				try {
					c1.setCellStyle(headerCellStyle);
				} catch (Exception e) {
					c1 = r1.createCell(i);
					c1.setCellStyle(headerCellStyle);
					LOG.error("DownloadSampleExcel::buildGlobalRustExcelPlotData():" + e);
				}
			}
		}

		// sub of rust details

		for (int i = 0; i <= runningCol; i++) {
			try {
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
			} catch (Exception e) {
				LOG.error("DownloadSampleExcel::buildGlobalRustExcelPlotData():" + e);
				continue;

			}
		}

		return workbook;

	}

	public HSSFWorkbook surveyDiscardedReportDownload(HttpServletResponse response, List<SurveyDetailsBean> beans) {
		PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

		// build

		return publishSurveyExcel.buildDiscardedSurveyExcel(beans);
	}

	/* -------------------sucess weblogin details */
	public HSSFWorkbook sucessWebloginReportDownload( List<IPTrackBean> beans) {
		
		PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

		// build

		return publishSurveyExcel.sucessWebloginExcel(beans);
	}

	/* -------------------recomendation survey data report */
	public HSSFWorkbook recomendationSurveyReportDownload(HttpServletResponse response,
			List<SurveyImplementationBean> beans) {
		PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

		// build

		return publishSurveyExcel.recomendationSurveyExcel(beans);
	}

	/* -------------------recomendation published survey data report */
	public HSSFWorkbook recomendationPublishedSurveyReportDownload(HttpServletResponse response,
			List<SurveyImplementationBean> beans) {
		PublishSurveyExcel publishSurveyExcel = new PublishSurveyExcel();

		// build

		return publishSurveyExcel.recomendationPublishedSurveyExcel(beans);
	}

	// Plot Header for Other Disease Excel Report

	public HSSFWorkbook buildOtherDiseaseExcelPlotData( RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository, Integer researchCenterId) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		// sheet creation
		Sheet sheet = workbook.createSheet("Other Diseases");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbook.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		// Create a Row
		// main header
		Row headerRow = sheet.createRow(0);

		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellValue(WrsisPortalConstant.SL_NO);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));

		mainCell = headerRow.createCell(1);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEY_NUMBER);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
		mainCell = headerRow.createCell(2);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEYOR_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		// Create cells
		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		// Surveyor details sub rows.

		subCell = row.createCell(2);
		subCell.setCellValue(WrsisPortalConstant.COUNTRY1);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(3);
		subCell.setCellValue(WrsisPortalConstant.SURVEYOR_NAME);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(4);
		subCell.setCellValue(WrsisPortalConstant.RESEARCH_CENTER);
		subCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 15));
		mainCell = headerRow.createCell(5);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Survey details sub rows.

		subCell = row.createCell(5);
		subCell.setCellValue(WrsisPortalConstant.SEASON_C);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(9);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(10);
		subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(11);
		subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(12);
		subCell.setCellValue(WrsisPortalConstant.LATITUDE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(13);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(14);
		subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(15);
		subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		// Site Information Header
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 20));
		mainCell = headerRow.createCell(16);
		mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
		mainCell.setCellStyle(headerCellStyle);

		// Site Information Sub Header
		subCell = row.createCell(16);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(17);
		subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(18);
		subCell.setCellValue("Variety");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(19);
		subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(20);
		subCell.setCellValue("Area");
		subCell.setCellStyle(headerCellStyle);

		int runningCol = 20;
		int prevCol = runningCol;

		Row thirdRow = sheet.createRow(2);

		// Other Disease

		List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
		for (int disease = 0; disease < diseaseList.size(); disease++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
			runningCol = runningCol + 2;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol + 1, runningCol));

		mainCell = headerRow.createCell(prevCol + 1);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
		mainCell.setCellStyle(headerCellStyle);

		// 50 columns set auto size
		for (int i = 0; i < 200; i++) {
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i <= runningCol; i++) {
			for (int j = 0; j < 3; j++)// row
			{
				Row r1 = sheet.getRow(j);
				Cell c1 = r1.getCell(i);
				try {
					c1.setCellStyle(headerCellStyle);
				} catch (Exception e) {
					c1 = r1.createCell(i);
					c1.setCellStyle(headerCellStyle);
					LOG.error("DownloadSampleExcel::buildOtherDiseaseExcelPlotData():" + e);
				}
			}
		}

		// sub of rust details

		for (int i = 0; i <= runningCol; i++) {
			try {
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
			} catch (Exception e) {
				LOG.error("DownloadSampleExcel::buildOtherDiseaseExcelPlotData():" + e);
				continue;
			}
		}

		return workbook;

	}

	// Plot CSV header for Other disease

	public HSSFWorkbook buildOtherDiseaseCSVHeader( RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository, Integer researchCenterId) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		// sheet creation
		Sheet sheet = workbook.createSheet("Other Diseases");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbook.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		// Create a Row
		// main header
		Row headerRow = sheet.createRow(0);

		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellValue(WrsisPortalConstant.SL_NO);
		mainCell.setCellStyle(headerCellStyle);

		mainCell = headerRow.createCell(1);
		mainCell.setCellValue("Survey Details : Survey No.");
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
		mainCell = headerRow.createCell(2);
		mainCell.setCellValue("Surveyor Details.");
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		// Create cells
		// sub headers

		// sub header headers
		Cell subCell = headerRow.createCell(0);
		subCell.setCellValue("Sl No");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(1);
		subCell.setCellValue("Survey No");
		subCell.setCellStyle(headerCellStyle);

		// Surveyor details sub rows.

		subCell = headerRow.createCell(2);
		subCell.setCellValue("Surveyor Details : Country");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(3);
		subCell.setCellValue("Surveyor Details : Surveyor Name");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(4);
		subCell.setCellValue("Surveyor Details : Research Center");
		subCell.setCellStyle(headerCellStyle);

		// Survey details sub rows.

		subCell = headerRow.createCell(5);
		subCell.setCellValue("Survey Details : Season");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(6);
		subCell.setCellValue("Survey Details : Region");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(7);
		subCell.setCellValue("Survey Details : Zone");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(8);
		subCell.setCellValue("Survey Details : Woreda");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(9);
		subCell.setCellValue("Survey Details : Kebele");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(10);
		subCell.setCellValue("Survey Details : Altitude");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(11);
		subCell.setCellValue("Survey Details : Longitude");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(12);
		subCell.setCellValue("Survey Details : Latitude");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(13);
		subCell.setCellValue("Survey Details : Survey Date(dd/mm/yyyy)");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(14);
		subCell.setCellValue("Survey Details : Planting Date(dd/mm/yyyy)");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(15);
		subCell.setCellValue("Survey Details : First Rust Observation Date(dd/mm/yyyy)");
		subCell.setCellStyle(headerCellStyle);

		// Site Information Header

		// Site Information Sub Header
		subCell = headerRow.createCell(16);
		subCell.setCellValue("Site Information : Survey Site");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(17);
		subCell.setCellValue("Site Information : Wheat Type");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(18);
		subCell.setCellValue("Site Information : Variety");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(19);
		subCell.setCellValue("Site Information : Growth Stage");
		subCell.setCellStyle(headerCellStyle);

		subCell = headerRow.createCell(20);
		subCell.setCellValue("Site Information : Area");
		subCell.setCellStyle(headerCellStyle);

		int runningCol = 20;

		// Other Disease

		List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
		for (int disease = 0; disease < diseaseList.size(); disease++) {

			Cell thirdCell = headerRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE_SEMCOLN + diseaseList.get(disease).getDiseaseName() + " :Incident");
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = headerRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE_SEMCOLN + diseaseList.get(disease).getDiseaseName() + " :Severity");
			thirdCell.setCellStyle(headerCellStyle);

			runningCol = runningCol + 2;
		}

		// 50 columns set auto size
		for (int i = 0; i < 200; i++) {
			sheet.autoSizeColumn(i);
		}

		return workbook;

	}

	// Global rust Survey CSV Header generator

	public HSSFWorkbook buildGlobalRusCSVHeaderPlot(RustTypeRepository rustTypeRepository,
			SampleTypeRepository sampleTypeRepository, DiseaseMasterRepository diseaseMasterRepository,
			SeasionMasterRepository seasionMasterRepository, DemographyRepository demographyRepository,
			SiteTypeRepository siteTypeRepository, WheatTypeRepository wheatTypeRepository,
			VarietyTypeRepository varietyTypeRepository, GrowthRepository growthRepository,
			ReactionTypeRepository reactionTypeRepository, FungicideMasterRepository fungicideMasterRepository,
			FungiEffectTypeRepository fungiEffectTypeRepository, SoilColorRepository colorRepository,
			MoistureRepository moistureRepository, Integer researchCenterId) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		// sheet creation
		Sheet sheet = workbook.createSheet(WrsisPortalConstant.SURVEY_DATA);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbook.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		// Create a Row
		// main header
		Row headerRow = sheet.createRow(0);
		Row heading = sheet.createRow(3);

		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellValue(WrsisPortalConstant.SL_NO);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));

		Cell headCell = heading.createCell(0);
		headCell.setCellValue(WrsisPortalConstant.SLNO);

		mainCell = headerRow.createCell(1);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEY_NUMBER);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));

		headCell = heading.createCell(1);
		headCell.setCellValue("Survey No.");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
		mainCell = headerRow.createCell(2);
		mainCell.setCellValue(WrsisPortalConstant.HASH_SURVEYOR_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		// Create cells
		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		// Surveyor details sub rows.

		subCell = row.createCell(2);
		subCell.setCellValue(WrsisPortalConstant.COUNTRY1);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(2);
		headCell.setCellValue("Surveyor Details : Country");

		subCell = row.createCell(3);
		subCell.setCellValue(WrsisPortalConstant.SURVEYOR_NAME);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(3);
		headCell.setCellValue("Surveyor Details : Surveyor Name");

		subCell = row.createCell(4);
		subCell.setCellValue(WrsisPortalConstant.RESEARCH_CENTER);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(4);
		headCell.setCellValue("Surveyor Details : Research Center");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 17));
		mainCell = headerRow.createCell(5);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Survey details sub rows.

		subCell = row.createCell(5);
		subCell.setCellValue(WrsisPortalConstant.SEASON_C);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(5);
		headCell.setCellValue("Survey Details : Season");

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(6);
		headCell.setCellValue("Survey Details : Region");

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(7);
		headCell.setCellValue("Survey Details : Zone");

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(8);
		headCell.setCellValue("Survey Details : Woreda");

		subCell = row.createCell(9);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(9);
		headCell.setCellValue("Survey Details : Kebele");

		subCell = row.createCell(10);
		subCell.setCellValue(WrsisPortalConstant.ALTITUDE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(10);
		headCell.setCellValue("Survey Details : Altitude");

		subCell = row.createCell(11);
		subCell.setCellValue(WrsisPortalConstant.LONGITUDE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(11);
		headCell.setCellValue("Survey Details : Longitude");

		subCell = row.createCell(12);
		subCell.setCellValue(WrsisPortalConstant.LATITUDE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(12);
		headCell.setCellValue("Survey Details : Latitude");

		subCell = row.createCell(13);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(13);
		headCell.setCellValue("Survey Details : Survey Date(dd/mm/yyyy)");

		subCell = row.createCell(14);
		subCell.setCellValue(WrsisPortalConstant.PLANTING_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(14);
		headCell.setCellValue("Survey Details : Planting Date(dd/mm/yyyy)");

		subCell = row.createCell(15);
		subCell.setCellValue(WrsisPortalConstant.FIRST_RUST_OBSERVATION_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(15);
		headCell.setCellValue("Survey Details : First Rust Observation Date(dd/mm/yyyy)");

		subCell = row.createCell(16);
		subCell.setCellValue(WrsisPortalConstant.CONTACTED_FARMER);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(16);
		headCell.setCellValue("Survey Details : Contacted Farmer");

		subCell = row.createCell(17);
		subCell.setCellValue(WrsisPortalConstant.REMARKS);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(17);
		headCell.setCellValue("Survey Details : Remarks");

		// Site Information Header
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 22));
		mainCell = headerRow.createCell(18);
		mainCell.setCellValue(WrsisPortalConstant.SITE_INFORMATION);
		mainCell.setCellStyle(headerCellStyle);

		// Site Information Sub Header
		subCell = row.createCell(18);
		subCell.setCellValue(WrsisPortalConstant.SURVEY_SITE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(18);
		headCell.setCellValue("Site Information : Survey Site");

		subCell = row.createCell(19);
		subCell.setCellValue(WrsisPortalConstant.WHEAT_TYPE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(19);
		headCell.setCellValue("Site Information : Wheat Type");

		subCell = row.createCell(20);
		subCell.setCellValue("Variety");
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(20);
		headCell.setCellValue("Site Information : Variety");

		subCell = row.createCell(21);
		subCell.setCellValue(WrsisPortalConstant.GROWTH_STAGE);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(21);
		headCell.setCellValue("Site Information : Growth Stage");

		subCell = row.createCell(22);
		subCell.setCellValue("Area");
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(22);
		headCell.setCellValue("Site Information : Area");

		int runningCol = 22;
		int prevCol = runningCol;

		// fetch all active Rust types
		List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();

		Row thirdRow = sheet.createRow(2);

		for (int rust = 0; rust < rustTypeList.size(); rust++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(rustTypeList.get(rust).getVchRustType());
			subCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 1);
			headCell.setCellValue("Rust  Details : " + rustTypeList.get(rust).getVchRustType() + " : Incident");

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);
			headCell = heading.createCell(runningCol + 2);
			headCell.setCellValue("Rust  Details : " + rustTypeList.get(rust).getVchRustType() + " : Severity");

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue(WrsisPortalConstant.REACTION);
			thirdCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 3);
			headCell.setCellValue("Rust  Details : " + rustTypeList.get(rust).getVchRustType() + " : Reaction");

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol + 1, runningCol));
		mainCell = headerRow.createCell(prevCol + 1);
		mainCell.setCellValue(WrsisPortalConstant.RUST_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		// Sample Details

		prevCol = runningCol + 1;

		runningCol++;
		subCell = row.createCell(prevCol);
		subCell.setCellValue(WrsisPortalConstant.SAMPLE_COLLECTED);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(prevCol);
		headCell.setCellValue("Sample Collected ");

		List<SampleTypeMaster> sampleTypeList = sampleTypeRepository.fetchAllActiveSampletypes();

		for (int sample = 0; sample < sampleTypeList.size(); sample++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(sampleTypeList.get(sample).getSampleName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.NO_OF_SAMPLE);
			thirdCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 1);
			headCell.setCellValue("Sample Details :" + sampleTypeList.get(sample).getSampleName() + ": No. Of Sample");

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SAMPLE_ID1);
			thirdCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 2);
			headCell.setCellValue("Sample Details :" + sampleTypeList.get(sample).getSampleName() + ": Sample ID");

			thirdCell = thirdRow.createCell(runningCol + 3);
			thirdCell.setCellValue("Remarks");
			thirdCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 3);
			headCell.setCellValue("Sample Details :" + sampleTypeList.get(sample).getSampleName() + ": Remarks");

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 3 - 2, runningCol + 3));
			runningCol = runningCol + 3;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.SAMPLE_DETAILS2);
		mainCell.setCellStyle(headerCellStyle);

		// Fungicide Details
		prevCol = runningCol + 1;
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(runningCol);
		headCell.setCellValue(WrsisPortalConstant.FUNGICIDE_APPLIED);

		runningCol++;

		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.FUNGICIDE_USED);
		subCell.setCellStyle(headerCellStyle);

		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Fungicide Details : Fungicide Used");

		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue("Dose");
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Fungicide Details : Dose");
		subCell.setCellStyle(headerCellStyle);
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SPRAY_DATE_DD_MM_YYYY);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Fungicide Details : Spray Date(dd/mm/yyyy)");
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue("Fungicide Details : Effectiveness");
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Fungicide Details : Effectiveness");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));
		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.FUNGICIDE_DETAILS);
		mainCell.setCellStyle(headerCellStyle);

		prevCol = runningCol + 1;

		// Other Disease
		List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
		for (int disease = 0; disease < diseaseList.size(); disease++) {
			subCell = row.createCell(runningCol + 1);
			subCell.setCellValue(diseaseList.get(disease).getDiseaseName());
			subCell.setCellStyle(headerCellStyle);

			Cell thirdCell = thirdRow.createCell(runningCol + 1);
			thirdCell.setCellValue(WrsisPortalConstant.INCIDENT);
			thirdCell.setCellStyle(headerCellStyle);

			headCell = heading.createCell(runningCol + 1);
			headCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE_SEMCOLN + diseaseList.get(disease).getDiseaseName() + " : Incident");

			thirdCell = thirdRow.createCell(runningCol + 2);
			thirdCell.setCellValue(WrsisPortalConstant.SEVERITY);
			thirdCell.setCellStyle(headerCellStyle);
			headCell = heading.createCell(runningCol + 2);
			headCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE_SEMCOLN + diseaseList.get(disease).getDiseaseName() + " : Severity");

			sheet.addMergedRegion(new CellRangeAddress(1, 1, runningCol + 2 - 1, runningCol + 2));
			runningCol = runningCol + 2;
		}

		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DISEASE);
		mainCell.setCellStyle(headerCellStyle);

		// Other Details
		prevCol = runningCol + 1;
		runningCol += 1;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.IRRIGATED);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Other Details : Irrigated");
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.WEED_CONTROL);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Other Details : Weed Control");
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.SOIL_COLOR);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Other Details : Soil Color");
		runningCol++;
		subCell = row.createCell(runningCol);
		subCell.setCellValue(WrsisPortalConstant.MOISTURE);
		subCell.setCellStyle(headerCellStyle);
		headCell = heading.createCell(runningCol);
		headCell.setCellValue("Other Details : Moisture");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, prevCol, runningCol));

		mainCell = headerRow.createCell(prevCol);
		mainCell.setCellValue(WrsisPortalConstant.OTHER_DETAILS);
		mainCell.setCellStyle(headerCellStyle);
		prevCol = runningCol + 1;

		// Any Other Disease Plot

		// 50 columns set auto size
		for (int i = 0; i < 200; i++) {
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i <= runningCol; i++) {
			for (int j = 0; j < 3; j++)// row
			{
				Row r1 = sheet.getRow(j);
				Cell c1 = r1.getCell(i);
				try {
					c1.setCellStyle(headerCellStyle);
				} catch (Exception e) {
					c1 = r1.createCell(i);
					c1.setCellStyle(headerCellStyle);
				}
			}
		}

		// sub of rust details

		for (int i = 0; i <= runningCol; i++) {
			try {
				sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
			} catch (Exception e) {
				LOG.error("DownloadSampleExcel::buildGlobalRusCSVHeaderPlot():" + e);
				continue;
			}
		}

		return workbook;
	}


}
