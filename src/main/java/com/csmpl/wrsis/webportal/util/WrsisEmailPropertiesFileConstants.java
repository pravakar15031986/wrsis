package com.csmpl.wrsis.webportal.util;

import java.util.ResourceBundle;

import com.csmpl.adminconsole.webportal.util.WrsisEmailPortalConstant;

public final class WrsisEmailPropertiesFileConstants {

	public static WrsisEmailPropertiesFileConstants theObject;

	public synchronized static WrsisEmailPropertiesFileConstants createWrsisEmailPropertiesFileConstants() {
		if (theObject == null)
			theObject = new WrsisEmailPropertiesFileConstants();
		return theObject;
	}

	private final ResourceBundle wrsisResourcesBundel;
	// mail integration

	public final String EMAIL_SMTPAUTH;
	public final String EMAIL_SMTPSTARTTLSENABLE;
	public final String EMAIL_SMTPHOST;
	public final String EMAIL_SMTPPORT;
	public final String EMAIL_IP;
	public final String EMAIL_PORT;
	public final String EMAIL_FROM;
	public final String EMAIL_PASSWORD;
	public final String EMAIL_SUBJECT_WRSIS;

	private WrsisEmailPropertiesFileConstants() {

		wrsisResourcesBundel = ResourceBundle.getBundle("email");

		// mail integration

		EMAIL_SMTPAUTH = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_SMTPAUTH).trim();
		EMAIL_SMTPSTARTTLSENABLE = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_SMTPSTARTTLSENABLE)
				.trim();
		EMAIL_SMTPHOST = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_SMTPHOST).trim();
		EMAIL_SMTPPORT = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_SMTPPORT).trim();
		EMAIL_IP = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_IP).trim();
		EMAIL_PORT = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_PORT).trim();
		EMAIL_FROM = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_FROM).trim();
		EMAIL_PASSWORD = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_PSWD).trim();
		EMAIL_SUBJECT_WRSIS = wrsisResourcesBundel.getString(WrsisEmailPortalConstant.EMAIL_SUBJECT_WRSIS).trim();

	}

}
