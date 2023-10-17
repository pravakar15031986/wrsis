package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.GisFileLogBean;
import com.csmpl.wrsis.webportal.bean.LogBean;
import com.csmpl.wrsis.webportal.entity.GisFileLogEntity;
import com.csmpl.wrsis.webportal.repository.GisFileLogRepository;
import com.csmpl.wrsis.webportal.service.GisFileLogService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Service
public class GisFileLogServiceImpl implements com.csmpl.wrsis.webportal.service.GisFileLogService {

	public static final Logger LOG = LoggerFactory.getLogger(GisFileLogServiceImpl.class);

	@Autowired
	GisFileLogRepository gisFileLogRepository;

	@Autowired
	GisFileLogService gisFileLogService;

	// Before Search
	public List<GisFileLogBean> gisFileLogMethod()// SearchVo searchVo
	{
		List<GisFileLogBean> list = new ArrayList<>();
		GisFileLogBean vo = null;
		try {

			List<Object[]> objList = gisFileLogRepository.getDataList();

			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new GisFileLogBean();
					vo.setFileName(String.valueOf(obj[0]));
					vo.setUpdateDate(String.valueOf(obj[1]));
					list.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("GisFileLogServiceImpl::gisFileLogMethod():" + e);
		}

		return list;
	}

//After search 
	@Override
	public List<GisFileLogBean> gisFileLogMethod(Date uploadstartdate, Date uploadenddate) {

		List<GisFileLogBean> list = new ArrayList<>();
		GisFileLogBean vo = null;
		try {

			List<Object[]> objList = gisFileLogRepository.getDataList(uploadstartdate, uploadenddate);

			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new GisFileLogBean();
					vo.setFileName(String.valueOf(obj[0]));
					vo.setUpdateDate(String.valueOf(obj[1]));
					vo.setFileId(Integer.parseInt(String.valueOf(obj[2])));
					vo.setFileUploadedOn(String.valueOf(obj[3]));
					Boolean status =Boolean.valueOf((boolean)obj[4]);
					if(status == false) {
					vo.setStatus("Active");	
					}else {
					vo.setStatus("Inactive");	
					}
					
					list.add(vo);

				}
			}
		} catch (Exception e) {
			LOG.error("GisFileLogServiceImpl::gisFileLogMethod():" + e);
		}

		return list;
	}

	// for file download
	@Override
	public void downloadGisFile(HttpServletResponse response, Integer fileId, ModelMap model) {

		try {
			GisFileLogEntity obj = gisFileLogRepository.getOne(fileId);
			if (obj.getFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH;
				if (obj.getFileLoc().contains(obj.getFileName())) {
					dataDirectory = obj.getFileLoc().replace(obj.getFileName(), "");
					dataDirectory = dataDirectory.substring(0, dataDirectory.length() - 1);
				}
				Path path = Paths.get(dataDirectory, obj.getFileName());
				if (Files.exists(path)) {
					response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + obj.getFileName());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("GisFileLogServiceImpl::downloadGisFile():" + ex);
					}
				} else {

					model.addAttribute("msg", "File not found");
					
				}
			}
		} catch (Exception e) {
			LOG.error("GisFileLogServiceImpl::downloadGisFile():" + e);
		}

	}

	// for file exist or not
	@Override
	public String gisFileExits(Integer fileId) {
		JSONObject jObject = new JSONObject();
		try {
			GisFileLogEntity obj = gisFileLogRepository.getOne(fileId);
			if (obj.getFileName() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH;
				if (obj.getFileLoc().contains(obj.getFileName())) {
					dataDirectory = obj.getFileLoc().replace(obj.getFileName(), "");
					dataDirectory = dataDirectory.substring(0, dataDirectory.length() - 1);
				}

				Path path = Paths.get(dataDirectory, obj.getFileName());
				if (Files.exists(path)) {
					jObject.put("msg", "Yes");
				} else {

					jObject.put("msg", "No");
				}
			}
		} catch (Exception e) {
			
			LOG.error("GisFileLogServiceImpl::gisFileExists():" + e);
		}
		return jObject.toString();
	}

	// For View Log File
	@Override
	public List<LogBean> viewLogFile(String directory, Date uploadstartdate, Date uploadenddate) {
		List<LogBean> list = new ArrayList<>();
		LogBean obj = null;
		Date fileDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		SimpleDateFormat sdf1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYYHHMMA);

		try {
			File f = new File(directory);
			String[] s = f.list();

			Arrays.sort(s);
			Collections.reverse(Arrays.asList(s));

			for (String s1 : s) {
				obj = new LogBean();
				File file = new File(directory + s1);
				fileDate = DateUtil.StringMMMToDate(sdf.format(file.lastModified()));
				if(fileDate.compareTo(uploadstartdate)>=0 && fileDate.compareTo(uploadenddate)<=0) {
				obj.setFileName(s1);
				obj.setFileDate(sdf1.format(file.lastModified()));
				list.add(obj);
				}
			}
		} catch (Exception e) {

			LOG.error("GisFileLogServiceImpl::viewLogFile():" + e);
		}
		return list;
	}

}
