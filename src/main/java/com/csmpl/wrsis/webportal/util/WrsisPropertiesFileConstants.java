package com.csmpl.wrsis.webportal.util;

import java.util.ResourceBundle;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;

public final class WrsisPropertiesFileConstants {

	public static WrsisPropertiesFileConstants theObject;

	public synchronized static WrsisPropertiesFileConstants createWrsisPropertiesFileConstants() {
		if (theObject == null)
			theObject = new WrsisPropertiesFileConstants();
		return theObject;
	}

	public final String USER_PROFILE_IMG_UPLOAD_PATH;

	public final String IVR_EXCEL_UPLOAD_PATH;
	public final String WR_SURVEY_IMAGES_PATH;
	public final String WR_SURVEY_EXCEL_PATH;

	public final String WR_NO_OF_QUESTIONS;
	public final String WR_NO_OF_OPTIONS;
	public final String APPROVAL_PROCESS_ID;
	public final String DIFFERENTIAL_SET_MAX;

	public final String ADVISORY_FILE_UPLOAD_PATH;

	public final String RECOMMENDATION_FILE_UPLOAD_PATH;

	private final ResourceBundle wrsisResourcesBundel;

	public final String WR_RECOMMENDATION_AND_ADVISORY_EXCEL_PATH;
	public final String WR_RACE_ANALYSIS_PUBLISH;
	public final String WR_DEFAULT_START_YEAR;

	public final String GIS_FILE_UPLOAD_ZIP_PATH;
	public final String GIS_FILE_UPLOAD_YEAR_MONTH_PATH;
	public final String GIS_FILE_UPLOAD_UNZIP_PATH;
	
	public final String APK_FILE_UPLOAD_ZIP_PATH;
	public final String APK_FILE_UPLOAD_YEAR_MONTH_PATH;
	public final String APK_FILE_UPLOAD_OLD_PATH;

	public final String TAGSAMPLE_PROCESS_ID;
	public final String RECOMMENDATION_PROCESS_ID;
	public final String ADVISORY_PROCESS_ID;
	public final String IMPLEMENTATION_PROCESS_ID;

	private WrsisPropertiesFileConstants() {

		wrsisResourcesBundel = ResourceBundle.getBundle("wrsisResources");

		USER_PROFILE_IMG_UPLOAD_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.USER_PROFILE_IMG_UPLOAD_PATH)
				.trim();
		IVR_EXCEL_UPLOAD_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.IVR_EXCEL_UPLOAD_PATH).trim();
		WR_SURVEY_IMAGES_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_SURVEY_IMAGES_PATH.trim());
		WR_SURVEY_EXCEL_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_SURVEY_EXCEL_PATH.trim());

		WR_NO_OF_QUESTIONS = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_NO_OF_QUESTIONS.trim());
		WR_NO_OF_OPTIONS = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_NO_OF_OPTIONS.trim());
		APPROVAL_PROCESS_ID = wrsisResourcesBundel.getString(WrsisPortalConstant.APPROVAL_PROCESS_ID.trim());
		DIFFERENTIAL_SET_MAX = wrsisResourcesBundel.getString(WrsisPortalConstant.DIFFERENTIAL_SET_MAX.trim());

		ADVISORY_FILE_UPLOAD_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.ADVISORY_FILE_UPLOAD_PATH.trim());

		RECOMMENDATION_FILE_UPLOAD_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.RECOMMENDATION_FILE_UPLOAD_PATH.trim());
		WR_RECOMMENDATION_AND_ADVISORY_EXCEL_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.WR_RECOMMENDATION_AND_ADVISORY_EXCEL_PATH.trim());
		WR_RACE_ANALYSIS_PUBLISH = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_RACE_ANALYSIS_PUBLISH.trim());
		WR_DEFAULT_START_YEAR = wrsisResourcesBundel.getString(WrsisPortalConstant.WR_DEFAULT_START_YEAR.trim());

		GIS_FILE_UPLOAD_ZIP_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.GIS_FILE_UPLOAD_ZIP_PATH.trim());
		APK_FILE_UPLOAD_ZIP_PATH = wrsisResourcesBundel.getString(WrsisPortalConstant.APK_FILE_UPLOAD_ZIP_PATH.trim());

		GIS_FILE_UPLOAD_YEAR_MONTH_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.GIS_FILE_UPLOAD_YEAR_MONTH_PATH.trim());
		APK_FILE_UPLOAD_YEAR_MONTH_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.APK_FILE_UPLOAD_YEAR_MONTH_PATH.trim());
		GIS_FILE_UPLOAD_UNZIP_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.GIS_FILE_UPLOAD_UNZIP_PATH.trim());
		
		APK_FILE_UPLOAD_OLD_PATH = wrsisResourcesBundel
				.getString(WrsisPortalConstant.APK_FILE_UPLOAD_OLD_PATH.trim());

		TAGSAMPLE_PROCESS_ID = wrsisResourcesBundel.getString(WrsisPortalConstant.TAGSAMPLE_PROCESS_ID.trim());
		RECOMMENDATION_PROCESS_ID = wrsisResourcesBundel
				.getString(WrsisPortalConstant.RECOMMENDATION_PROCESS_ID.trim());
		ADVISORY_PROCESS_ID = wrsisResourcesBundel.getString(WrsisPortalConstant.ADVISORY_PROCESS_ID.trim());
		IMPLEMENTATION_PROCESS_ID = wrsisResourcesBundel
				.getString(WrsisPortalConstant.IMPLEMENTATION_PROCESS_ID.trim());
	}
}
