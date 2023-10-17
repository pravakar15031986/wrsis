package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.IVRExcelCellBean;
import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataEntity;
import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;
import com.csmpl.wrsis.webportal.entity.ImportIVRFile;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.IVRDataRepository;
import com.csmpl.wrsis.webportal.repository.ManageIVRDataRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.service.ManageIVRDataService;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class ManageIVRDataServiceImpl implements ManageIVRDataService {

	private static final Logger LOG = LoggerFactory.getLogger(ManageIVRDataServiceImpl.class);
	@Autowired
	ManageIVRDataRepository manageIVRDataRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	IVRDataServiceImpl iVRDataServiceImpl;
	@Autowired
	IVRDataQuestionRepository iVRQuestionRepository;
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	IVRDataRepository iVRDataRepository;
	@Autowired
	IVRDataQuestionRepository iVRDataQuestionRepository;
	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@SuppressWarnings("resource")
	@Override
	public List<IVRExcelCellBean> readIvrExcel(ImportIVRFileBean ivr) {
		List<IVRExcelCellBean> excelDataList = null;
		try {
			String ivrDataFileName = null;
			if (ivr.getFileNameBEan() != null && !ivr.getFileNameBEan().isEmpty()) {
				ivrDataFileName = createIVREXCELFileName(ivr);
				File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH);
				if (!file.exists()) {
					file.mkdir();
				}
				if (!file.exists()) {
					Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH);
					Files.createDirectories(path);
				}
				FileCopyUtils.copy(ivr.getFileNameBEan().getBytes(),
						new File(WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH + File.separator
								+ ivrDataFileName));
			} // End Of 81
			if (ivrDataFileName != null) {
				if (ivr.getFileNameBEan() != null && !ivr.getFileNameBEan().isEmpty()) {
					File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH
							+ File.separator + ivr.getFileNameBEan());
					if (file.exists()) {
						if (!file.delete()) {
							// file delete failed take appropriate action
						}
					}
				}
				File file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH
						+ File.separator + ivrDataFileName);
				String fileExtensionName = ivrDataFileName.substring(ivrDataFileName.lastIndexOf("."));
				// Start OF .XLXS File
				if (fileExtensionName.equals(".xlsx")) {

					try (FileInputStream fis = new FileInputStream(file);
							XSSFWorkbook workBook = new XSSFWorkbook(fis);) {

						XSSFSheet sheet = workBook.getSheetAt(0);
						Iterator<Row> itr = sheet.iterator();
						IVRExcelCellBean cellBean = null;
						excelDataList = new ArrayList<>();
						Cell zoneName = null;
						Cell woredaName = null;
						String zName = "";
						String wName = "";
						String woredaExist = "";
						String errorMsg = "";

						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							zoneName = workBook.getSheetAt(0).getRow(i)
									.getCell(CellReference.convertColStringToIndex("B"));
							if (String.valueOf(zoneName) != "" && zoneName != null) {
								woredaName = workBook.getSheetAt(0).getRow(i)
										.getCell(CellReference.convertColStringToIndex("C"));
								zName = String.valueOf(zoneName).trim();
								wName = String.valueOf(woredaName).trim();
								woredaExist = demographicRepository.checkWoredaNameByWoredaName(wName);
								if (woredaExist == null) {
									errorMsg = "Incorrect Woreda name : " + wName;
									cellBean = new IVRExcelCellBean();
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("nameError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
								int zoneId = demographicRepository.getDemoIdByDemoNameAndLevelId(zName, 3);
								int woredaId = demographicRepository.getDemoIdByDemoNameAndLevelId(wName, 4);
								int parentId = demographicRepository.getParentIdByDemogId(woredaId);
								if (parentId != zoneId) {
									cellBean = new IVRExcelCellBean();
									errorMsg = wName + " is not in " + zName;
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("existError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
							} else {
								i = 100;
							}
						}

						while (itr.hasNext()) {
							List<String> excelCelList = new ArrayList<>();
							Row row = itr.next();
							if (row.getRowNum() == 0) {
								continue;
							}
							Iterator<Cell> cell = row.cellIterator();
							while (cell.hasNext()) {
								Cell c = cell.next();
								String cellValue = (c.getCellType() == 0) ? String.valueOf(c.getNumericCellValue())
										: c.getStringCellValue();
								if (c.getCellType() == 0) {
									BigDecimal bd = new BigDecimal(cellValue);
									long lonVal = bd.longValue();
									cellValue = Long.toString(lonVal);
								}

								excelCelList.add(cellValue);
							}
							if (excelCelList != null && !excelCelList.isEmpty()) {
								cellBean = new IVRExcelCellBean();
								cellBean.setPhone(excelCelList.get(0).trim());
								cellBean.setZone(excelCelList.get(1).trim());
								cellBean.setWoreda(excelCelList.get(2).trim());
								cellBean.setLanguage(excelCelList.get(3).trim());
								List<String> second = new ArrayList<>(excelCelList.subList(4, excelCelList.size()));
								ArrayList<String> ques = new ArrayList<>();
								for (String question : second) {
									ques.add(question.trim());
								}
								cellBean.setQustions(ques);
								cellBean.setFileName(ivrDataFileName);
								excelDataList.add(cellBean);
							}
						}
						workBook.close();

					} // End of try with resource
				} // End OF .XLSX File And Start of .XLS File
				if (fileExtensionName.equals(".xls")) {
					try (FileInputStream fis = new FileInputStream(file);
							HSSFWorkbook workBook = new HSSFWorkbook(fis);) {

						HSSFSheet sheet = workBook.getSheetAt(0);
						Iterator<Row> itr = sheet.iterator();
						IVRExcelCellBean cellBean = null;
						excelDataList = new ArrayList<>();
						Cell zoneName = null;
						Cell woredaName = null;
						String zName = "";
						String wName = "";
						String woredaExist = "";
						String errorMsg = "";

						for (int i = 1; i <= sheet.getLastRowNum(); i++) {
							zoneName = workBook.getSheetAt(0).getRow(i)
									.getCell(CellReference.convertColStringToIndex("B"));
							if (String.valueOf(zoneName) != "" && zoneName != null) {
								woredaName = workBook.getSheetAt(0).getRow(i)
										.getCell(CellReference.convertColStringToIndex("C"));
								zName = String.valueOf(zoneName).trim();
								wName = String.valueOf(woredaName).trim();
								woredaExist = demographicRepository.checkWoredaNameByWoredaName(wName);
								if (woredaExist == null) {
									errorMsg = "Incorrect Woreda name : " + wName;
									cellBean = new IVRExcelCellBean();
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("nameError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
								int zoneId = demographicRepository.getDemoIdByDemoNameAndLevelId(zName, 3);
								int woredaId = demographicRepository.getDemoIdByDemoNameAndLevelId(wName, 4);
								int parentId = demographicRepository.getParentIdByDemogId(woredaId);
								if (parentId != zoneId) {
									cellBean = new IVRExcelCellBean();
									errorMsg = wName + " is not in " + zName;
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("existError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
							} else {
								i = 100;
							}
						}

						while (itr.hasNext()) {
							List<String> excelCelList = new ArrayList<>();
							Row row = itr.next();

							if (row.getRowNum() == 0) {
								continue;
							}
							Iterator<Cell> cell = row.cellIterator();
							while (cell.hasNext()) {
								Cell c = cell.next();
								String cellValue = (c.getCellType() == 0) ? String.valueOf(c.getNumericCellValue())
										: c.getStringCellValue();
								if (c.getCellType() == 0) {
									BigDecimal bd = new BigDecimal(cellValue);
									long lonVal = bd.longValue();
									cellValue = Long.toString(lonVal);
								}
								excelCelList.add(cellValue);
							}
							if (excelCelList != null && !excelCelList.isEmpty()) {
								cellBean = new IVRExcelCellBean();
								cellBean.setPhone(excelCelList.get(0).trim());
								cellBean.setZone(excelCelList.get(1).trim());
								cellBean.setWoreda(excelCelList.get(2).trim());
								cellBean.setLanguage(excelCelList.get(3).trim());
								List<String> second = new ArrayList<>(excelCelList.subList(4, excelCelList.size()));
								ArrayList<String> ques = new ArrayList<>();
								for (String question : second) {
									ques.add(question.trim());
								}
								cellBean.setQustions(ques);
								cellBean.setFileName(ivrDataFileName);
								excelDataList.add(cellBean);
							}
						}
						workBook.close();

					} // End of try with resource block

				} // End of if .xls
				if (fileExtensionName.equalsIgnoreCase(".csv")) {
					CSVReader reader = null;
					try {
						reader = new CSVReader(new FileReader(file.getPath()), ',', '"', 1);
						String[] nextLine;

						IVRExcelCellBean cellBean = null;
						excelDataList = new ArrayList<>();
						String zName = "";
						String wName = "";
						String woredaExist = "";
						String errorMsg = "";
						
						// reads one line at a time
						while ((nextLine = reader.readNext()) != null) {
							List<String> excelCelList = new ArrayList<>();
							zName = nextLine[1].trim();
							if (zName != "" && zName != null) {
								wName = nextLine[2].trim();
								woredaExist = demographicRepository.checkWoredaNameByWoredaName(wName);
								if (woredaExist == null) {
									errorMsg = "Incorrect Woreda name : " + wName;
									cellBean = new IVRExcelCellBean();
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("nameError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
								int zoneId = demographicRepository.getDemoIdByDemoNameAndLevelId(zName, 3);
								int woredaId = demographicRepository.getDemoIdByDemoNameAndLevelId(wName, 4);
								int parentId = demographicRepository.getParentIdByDemogId(woredaId);
								if (parentId != zoneId) {
									cellBean = new IVRExcelCellBean();
									errorMsg = wName + " is not in " + zName;
									cellBean.setWoreda(String.valueOf(errorMsg));
									cellBean.setZone("existError");
									excelDataList.add(cellBean);
									return excelDataList;
								}
							}
							for (String cellValue : nextLine) {
								if (cellValue == nextLine[0]) {
									BigDecimal bd = new BigDecimal(nextLine[0]);
									long lonVal = bd.longValue();
									cellValue = Long.toString(lonVal);
								}
								excelCelList.add(cellValue);
							}
							if (excelCelList != null && !excelCelList.isEmpty()) {
								cellBean = new IVRExcelCellBean();
								cellBean.setPhone(excelCelList.get(0).trim());
								cellBean.setZone(excelCelList.get(1).trim());
								cellBean.setWoreda(excelCelList.get(2).trim());
								cellBean.setLanguage(excelCelList.get(3).trim());
								List<String> second = new ArrayList<>(excelCelList.subList(4, excelCelList.size()));
								ArrayList<String> ques = new ArrayList<>();
								for (String question : second) {
									ques.add(question.trim());
								}
								cellBean.setQustions(ques);
								cellBean.setFileName(ivrDataFileName);
								excelDataList.add(cellBean);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					} // End of try catch block

				} // End of if .csv
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::readIvrExcel():" + e);
		}

		List<Question> questionList = questionRepository.viewAllQustionExcel();

		if (excelDataList != null && excelDataList.get(0).getQustions().size() > 0
				&& questionList.size() != excelDataList.get(0).getQustions().size()) {
			excelDataList = null;
		}
		return excelDataList;
	}

	private String createIVREXCELFileName(ImportIVRFileBean ivrFile) {
		String fileName = null;
		try {
			SecureRandom random = new SecureRandom();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
			Date datee = new Date();
			String date = dateFormat.format(datee);
			String month = monthFormat.format(datee);
			String year = String.valueOf(datee.getYear() + 1900);
			int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

			String extensionName = FilenameUtils.getExtension(ivrFile.getFileNameBEan().getOriginalFilename());
			fileName = "IVR_" + randomNo + date + month + year + "." + extensionName;

		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::createIVREXCELFileName():" + e);

		}
		return fileName;
	}

	@Override
	public String saveIVRFile(ImportIVRFileBean ivr) {
		String res = "";
		try {
			if (ivr.getImprtIVRIdBean() == 0) {

				// File Data Saved Here
				ImportIVRFile ivEnt = new ImportIVRFile();
				ivEnt.setCreatedBy(ivr.getCreatedByBean());
				ivEnt.setCreatedOn(new Date());
				ivEnt.setRecordCount(ivr.getRecordCountBean());
				ivEnt.setFileName(ivr.getFile_Name());
				ivEnt.setStatus(false);
				ImportIVRFile ivrFileObj = manageIVRDataRepository.saveAndFlush(ivEnt);
				// File Data Saved Here

				Integer fileId = ivrFileObj.getImprtIVRId();

				IVRDataEntity ivrDataObj = null;
				List<IVRExcelCellBean> excelData = ivr.getCellBeanList();

				if (excelData != null) {
					for (IVRExcelCellBean ivrExCellBean : excelData) {
						// Here IVR Data Save Start
						ivrDataObj = new IVRDataEntity();

						ivrDataObj.setCreatedBy(ivr.getCreatedByBean());

						ivrDataObj.setMobile(ivrExCellBean.getPhone());

						ivrDataObj.setLanguage(ivrExCellBean.getLanguage());

						ImportIVRFile iv = new ImportIVRFile();

						iv.setImprtIVRId(fileId);
						ivrDataObj.setIvrFileId(iv);

						int regionId = 0;
						DemographicEntity zoneId = demographicRepository
								.findByDemographyNameIgnoreCase(ivrExCellBean.getZone());
						if (zoneId != null) {
							regionId = zoneId.getParentId();
							ivrDataObj.setZoneId(zoneId.getDemographyId());
						} else {
							ivrDataObj.setZoneId(0);
						}

						DemographicEntity woredaId = demographicRepository
								.findByDemographyNameIgnoreCase(ivrExCellBean.getWoreda());
						if (woredaId != null) {

							ivrDataObj.setWoredaId(woredaId.getDemographyId());
						} else {
							ivrDataObj.setWoredaId(0);
						}

						ivrDataObj.setRegionId(regionId);

						IVRDataEntity ivrDEnt = iVRDataServiceImpl.saveAndUpdateIvrData(ivrDataObj);
						// Here IVR Data Save Start End

						

						if (ivrDEnt != null) {
							Integer dataId = ivrDEnt.getIvrDataId();

							IVRDataQuestionEntity qustion = null;

							JSONArray arr = new JSONArray(ivrExCellBean.getPageQuestion());
							for (int i = 0; i < arr.length(); i++) {

								
								// here Question Is search by Question Order Id and question type
								Question qList = questionRepository.findByQustionOrderByQuestionType(i + 1, 1);
								qustion = new IVRDataQuestionEntity();
								qustion.setStatus(false);
								qustion.setQustionId(qList.getQustionId());

								qustion.setQustionResponse(arr.getJSONObject(i).getString("Question"));

								IVRDataEntity d = new IVRDataEntity();
								d.setIvrDataId(dataId);
								qustion.setIvrDataId(d);
								qustion.setCreatedBy(ivr.getCreatedByBean());
								qustion.setCreatedOn(new Date());
								iVRQuestionRepository.saveAndFlush(qustion);
							}

						}
					}
					res = WrsisPortalConstant.SAVE;
				}
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::saveIVRFile():" + e);

		}

		return res;
	}

	@Override
	public String viewIVRFileDataByPage(Integer pageSize, Integer pageNumber, Pageable pageable) {
		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Page<ImportIVRFile> page = manageIVRDataRepository
					.viewIVRFileByPage(new PageRequest(pageable.getPageNumber(), pageable.getPageSize()));
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put("currentPage", currentPage);
				jObject.put("startPage", begingPage);
				jObject.put("endPage", endPage);
				jObject.put("pageNo", page.getNumber());
				jObject.put("showRowNo", page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;
				
				for (ImportIVRFile ivr : page) {
					jsonObject = new JSONObject();
					if (ivr != null) {
						jsonObject.put("id", ivr.getImprtIVRId() != 0 ? ivr.getImprtIVRId() : "-NA-");
						jsonObject.put("fileName", ivr.getFileName() != null ? ivr.getFileName() : "-NA-");
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

						jsonObject.put("uploadDate", formatter.format(ivr.getCreatedOn()));
						jsonObject.put("recordCount", ivr.getRecordCount());

					}
					array.put(jsonObject);
				}
				jObject.put("ivrFileList", array);

			}

		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::viewIVRFileDataByPage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public void downloadIVRFile(HttpServletResponse response, Integer fileId, ModelMap model) {

		try {
			ImportIVRFile obj = manageIVRDataRepository.getOne(fileId);
			if (obj.getFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getFileName());
				if (Files.exists(path)) {
					response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + obj.getFileName());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("ManageIVRDataServiceImpl::downloadIVRFile():" + ex);
					}
				} else {
					model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "File not found");
				}
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::downloadIVRFile():" + e);

		}
	}

	@Override
	public String searchIVRFileDataByDatePage(Integer pageSize, Integer pageNumber, Pageable pageable, Date fromDate,
			Date toDate) {

		JSONObject jObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {

			Page<ImportIVRFile> page = manageIVRDataRepository.searchIVRFileByPage(
					new PageRequest(pageable.getPageNumber(), pageable.getPageSize()), fromDate, toDate);
			if (page != null && page.getSize() > 0) {
				int currentPage = page.getNumber();
				int begingPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(begingPage + pageSize, page.getTotalPages());
				jObject.put("currentPage", currentPage);
				jObject.put("startPage", begingPage);
				jObject.put("endPage", endPage);
				jObject.put("pageNo", page.getNumber());
				jObject.put("showRowNo", page.getSize());
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, page.getTotalElements());

				JSONObject jsonObject = null;

				for (ImportIVRFile ivr : page) {
					jsonObject = new JSONObject();
					if (ivr != null) {
						jsonObject.put("id", ivr.getImprtIVRId() != 0 ? ivr.getImprtIVRId() : "-NA-");
						jsonObject.put("fileName", ivr.getFileName() != null ? ivr.getFileName() : "-NA-");
						SimpleDateFormat formatter = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
						jsonObject.put("uploadDate", formatter.format(ivr.getCreatedOn()));
						jsonObject.put("recordCount", ivr.getRecordCount());

					}
					array.put(jsonObject);
				}
				jObject.put("ivrFileList", array);

			} else {
				jObject.put(WrsisPortalConstant.TOTAL_ROW_NO, 0);
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::searchIVRFileDataByDatePage():" + e);

		}
		return jObject.toString();
	}

	@Override
	public ImportIVRFile getQustionDetailsByImprtIVRId(Integer imprtIVRId) {

		return manageIVRDataRepository.getOne(imprtIVRId);
	}

	// this is used for Excel Download
	@Override
	public void iVRFileDataByExcel(HttpServletResponse response, Integer region, Integer zone, Integer woreda,
			String mob) {

		List<Object[]> objBeanList = null;
		try {

			if (region != 0) {
				objBeanList = iVRDataRepository.viewAllIVRReportByRegionData(region);
			}
			if (zone != 0) {
				objBeanList = iVRDataRepository.viewAllIVRReportByZoneData(zone);
			}
			if (woreda != 0) {
				objBeanList = iVRDataRepository.viewAllIVRReportByWoredaData(zone);
			}
			if (mob != null && !"".equalsIgnoreCase(mob)) {
				objBeanList = iVRDataRepository.viewAllIVRReportByPhoneData(mob.trim());
			}
			if (region == 0 && zone == 0 && woreda == 0 && "".equalsIgnoreCase(mob)) {
				objBeanList = iVRDataRepository.viewAllIVRReportDataReport();
			}

			// Excel Code Start
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("IVR Data");

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
			// Dynamically question creation
			ArrayList<String> questionData = new ArrayList<>();
			int q = 1;
			List<Question> questionList = questionRepository.viewAllQustionExcel();
			for (Question question : questionList) {
				questionData.add("Question " + q++);
			}
			ArrayList<String> columns = new ArrayList<>();
			columns.add("Phone #");
			columns.add("Call Date Time");
			columns.add(WrsisPortalConstant.REGION);
			columns.add(WrsisPortalConstant.ZONE);
			columns.add(WrsisPortalConstant.WOREDA);

			columns.addAll(questionData);

			for (int i = 0; i < columns.size(); i++) {
				Cell cell = headRow.createCell(i);
				cell.setCellValue(columns.get(i));
				cell.setCellStyle(headerCellStyle);
			}
			int rowNo = 1;
			for (Object[] ivr : objBeanList) // Bean Object
			{

				Row row = sheet.createRow(rowNo++);
				row.createCell(0).setCellValue(ivr[0].toString());

				row.createCell(1).setCellValue("-NA-");

				Object regID = ivr[2];
				if (!regID.toString().equals("0")) {
					DemographicEntity region1 = demographicRepository.getOne(Integer.parseInt(regID.toString()));
					if (region1 != null) {
						row.createCell(2).setCellValue(region1.getDemographyName());

					} else {
						row.createCell(2).setCellValue("-NA-");
					}
				} else {
					row.createCell(2).setCellValue("-NA-");
				}

				Object zoneID = ivr[3];
				if (!zoneID.toString().equals("0")) {
					DemographicEntity zone1 = demographicRepository.getOne(Integer.parseInt(zoneID.toString()));
					if (zone1 != null) {
						row.createCell(3).setCellValue(zone1.getDemographyName());
					} else {
						row.createCell(3).setCellValue("-NA-");
					}
				} else {
					row.createCell(3).setCellValue("-NA-");
				}

				Object woredaID = ivr[4];
				if (!woredaID.toString().equals("0")) {
					DemographicEntity woreda1 = demographicRepository.getOne(Integer.parseInt(woredaID.toString()));
					if (woreda1 != null) {
						row.createCell(4).setCellValue(woreda1.getDemographyName());

					} else {
						row.createCell(4).setCellValue("-NA-");
					}
				} else {
					row.createCell(4).setCellValue("-NA-");
				}

				int excelCout = 5;

				Object data_ques = ivr[6];

				String[] qustionOptionList = data_ques.toString().split(",");
				for (int j = 0; j < qustionOptionList.length; j++) {

					IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
							.getOne(Integer.parseInt(qustionOptionList[j]));

					String question_option = null;
					List<QuestionOption> optionList = questionOptionRepository
							.findByQustionId(ivrQuestionEntity.getQustionId());
					for (QuestionOption qOption : optionList) {

						if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
							question_option = qOption.getOptionInfo();

						}
					}
					if (question_option != null) {
						row.createCell(excelCout + j).setCellValue(question_option);

					} else {
						row.createCell(excelCout + j).setCellValue(ivrQuestionEntity.getQustionResponse());

					}
				}

			}
			for (int i = 0; i < columns.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			String FILENAME = "IVR-Survey-Data-Report.xlsx";
			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			try {
				workbook.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				workbook.close();
			} catch (IOException e) {

				LOG.error("ManageIVRDataServiceImpl::ivrFileDataByExcel():" + e);
			}
			// Excel Code End

		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::ivrFileDataByExcel():" + e);

		}

	}

	@Override
	public String ivrFileExits(Integer fileId) {
		JSONObject jObject = new JSONObject();
		try {
			ImportIVRFile obj = manageIVRDataRepository.getOne(fileId);
			if (obj.getFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.IVR_EXCEL_UPLOAD_PATH;
				Path path = Paths.get(dataDirectory, obj.getFileName());
				if (Files.exists(path)) {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "Yes");
				} else {
					jObject.put(WrsisPortalConstant.SUCCESS_MSG, "No");
				}
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::ivrFileExist():" + e);

		}
		return jObject.toString();
	}

	@Override
	public List<IvrDataReportBean> viewIvrDataPage(String sDate, String eDate, Integer pstart, Integer pLength) {
		List<IvrDataReportBean> beans = new ArrayList<>();
		SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		String fileName = null;
		try {
			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				startDate = dt2.format(dt1.parse(sDate));
			} else
					startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				endDate = dt2.format(dt1.parse(eDate));
			} else
					endDate = eDate;
			
			
			List<Object[]> dataList = iVRDataRepository.viewIvrDataPage(startDate, endDate, pstart, pLength);
			
			
			Integer count = 0;
			for (Object[] obj : dataList) {
				IvrDataReportBean ivr = new IvrDataReportBean();
				ivr.setSlNo((pstart) + (++count));
				ivr.setIvrFileId(Integer.valueOf(String.valueOf(obj[0])));
				
				Object fObj = obj[1];
				fileName = null;
				if(fObj!=null)
				{
					fileName = fObj.toString();
				}
				
				if ( fileName != null && (fileName.contains(".xls") || fileName.contains(".xlsx")|| fileName.contains(".csv"))) {
					ivr.setFileName("<a href='javascript:void(0)' onclick=\"generateExcel(" + obj[0] + ")\">"
							+ fileName + "</a>");
				}else {
					ivr.setFileName("API");
				}
				ivr.setUploadDate((String) obj[2]);
				ivr.setIvrDataCount("<a href='javascript:void(0)' onclick=\"edit(" + obj[0] + ")\">"
						+ obj[3] + "</a>");
				if(obj[4]!=null && !"".equals(obj[4])) {
					
				ivr.setResponded("<button class=\"btn btn-primary\" onclick=\"viewApiResponse('" + new String(Base64.getEncoder().encode(obj[4].toString().getBytes())) 
							+ "')\"><i class=\"fa fa-info\" aria-hidden=\"true\"></i></button>");
				//new String(Base64.getEncoder().encode(response.toString().getBytes()))
				//ivr.setResponded("<a href='javascript:void(0)' onclick=\"viewApiResponse('" + Base64.getEncoder().encodeToString(obj[4].toString().getBytes()) + "')\">"
						//+ obj[3] + "</a>");
				}
				//ivr.setResponded((String) obj[4]);
				LOG.info("ManageIVRDataServiceImpl::viewIvrDataPage():obj[4]:-" + obj[4]);
				beans.add(ivr);
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::viewIvrDataPage():error in retrive the ivr log data" + e);
		}
		return beans;
	}

	@Override
	public Integer countIvrDataPage(String sDate, String eDate, int pstart,int pLength) {
		Integer count = 0;
		SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		try {

			String startDate = null;
			if (sDate != null && !"".equalsIgnoreCase(sDate)) {
				startDate = dt2.format(dt1.parse(sDate));

			} else
					startDate = sDate;
			String endDate = null;
			if (eDate != null && !"".equalsIgnoreCase(eDate)) {
				endDate = dt2.format(dt1.parse(eDate));

			} else
					endDate = eDate;

			count = iVRDataRepository.countIvrDataPage(startDate, endDate, pstart, pLength);

		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::countIvrDataPage():" + e);

		}
		return count;

	}
	
	// Author : Raman Shrestha
	@Override
	public String saveIvrDataByApi(ImportIVRFileBean ivr) {
		String res = "";
		try {
			if (ivr.getImprtIVRIdBean() == 0) {
				// File Data Saved Here
				ImportIVRFile ivEnt = new ImportIVRFile();
				ivEnt.setCreatedBy(ivr.getCreatedByBean());
				ivEnt.setCreatedOn(new Date());
				ivEnt.setRecordCount(ivr.getRecordCountBean());
				ivEnt.setFileName("API");
				ivEnt.setStatus(false);
				ImportIVRFile ivrFileObj = manageIVRDataRepository.saveAndFlush(ivEnt);
				// File Data Saved Here
				Integer fileId = ivrFileObj.getImprtIVRId();
				IVRDataEntity ivrDataObj = null;
				List<IVRExcelCellBean> ivrData = ivr.getCellBeanList();

				if (ivrData != null) {
					for (IVRExcelCellBean ivrExCellBean : ivrData) {
						// Here IVR Data Save Start
						ivrDataObj = new IVRDataEntity();

						ivrDataObj.setMobile(ivrExCellBean.getPhone().trim());

						ivrDataObj.setLanguage(ivrExCellBean.getLanguage().trim());

						ImportIVRFile iv = new ImportIVRFile();

						iv.setImprtIVRId(fileId);
						ivrDataObj.setIvrFileId(iv);

						int regionId = 0;
						DemographicEntity zoneId = demographicRepository
								.findByDemographyNameIgnoreCase(ivrExCellBean.getZone().trim());
						if (zoneId != null) {
							regionId = zoneId.getParentId();
							ivrDataObj.setZoneId(zoneId.getDemographyId());
						} else {
							ivrDataObj.setZoneId(0);
						}

						DemographicEntity woredaId = demographicRepository
								.findByDemographyNameIgnoreCase(ivrExCellBean.getWoreda().trim());
						if (woredaId != null) {

							ivrDataObj.setWoredaId(woredaId.getDemographyId());
						} else {
							ivrDataObj.setWoredaId(0);
						}

						ivrDataObj.setRegionId(regionId);

						IVRDataEntity ivrDEnt = iVRDataServiceImpl.saveAndUpdateIvrData(ivrDataObj);
						// Here IVR Data Save Start End

						if (ivrDEnt != null) {
							Integer dataId = ivrDEnt.getIvrDataId();

							IVRDataQuestionEntity qustion = null;
							
							Object[] arr = ivrExCellBean.getPageQuestions();
							for (int i = 0; i < arr.length; i++) {

								
								// here Question Is search by Question Order Id and question type
								Question qList = questionRepository.findByQustionOrderByQuestionType(
										Integer.parseInt(((LinkedHashMap<String, String>) arr[i]).get("q_order").trim()), 1);
								qustion = new IVRDataQuestionEntity();
								qustion.setStatus(false);
								qustion.setQustionId(qList.getQustionId());

								qustion.setQustionResponse(
										((LinkedHashMap<String, String>) arr[i]).get("q_val").trim());
								
							

								IVRDataEntity d = new IVRDataEntity();
								d.setIvrDataId(dataId);
								qustion.setIvrDataId(d);
								qustion.setCreatedOn(new Date());
								iVRQuestionRepository.saveAndFlush(qustion);
							}

						} else {
							res = "fail";
							return res;
						}
					}
					res = "Save";
				}
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataServiceImpl::saveIvrDataByApi():" + e);
			res = "fail";
		}

		return res;
	}

}
