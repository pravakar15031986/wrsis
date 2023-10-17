package com.csmpl.wrsis.webportal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csmpl.wrsis.webportal.repository.MobileLoginTrackingRepository;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class PushNotification {

	private static final Logger LOG = LoggerFactory.getLogger(PushNotification.class);

	@Autowired
	MobileLoginTrackingRepository mobileLoginTrackingRepository;

	@Value("${push.notification.url}")
	private String notificationurl;

	@Value("${push.notification.key}")
	private String notificationkey;

	public String sendNotificationParameters(Short userId, String notifMsg, String notiftitle) {

		String fcmidResult = "fcmid found";
		try {

			String fcmId = mobileLoginTrackingRepository.getFcmIdByUserId(userId);

			if (fcmId != null && !fcmId.equals("")) {

				pushNotificationSend("\\" + "\"" + fcmId + "\\" + "\"", "\\" + "\"" + notifMsg + "\\" + "\"",
						"\\" + "\"" + notiftitle + "\\" + "\"");
			}

		} catch (Exception e) {
			fcmidResult = "fcmid not found";
			LOG.error("PushNotification::sendNotificationParameters():" + e.getMessage());
		}

		return fcmidResult;
	}

	public void pushNotificationSend(String fcmId, String notifMsg, String notiftitle) {

		try {

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json,application/json");

			RequestBody body = RequestBody.create(mediaType, "{\r\n  \"notification\":{\r\n    \"title\":" + notiftitle
					+ ",\r\n    \"body\":" + notifMsg
					+ ",\r\n    \"sound\":\"default\",\r\n    \"click_action\":\"FCM_PLUGIN_ACTIVITY\",\r\n    \"icon\":\"fcm_push_icon\"\r\n  },\r\n  \"data\":{\r\n    \"title\":"
					+ notiftitle + ",\r\n    \"body\":" + notifMsg
					+ ",\r\n    \"sound\":\"default\",\r\n    \"click_action\":\"FCM_PLUGIN_ACTIVITY\",\r\n    \"icon\":\"fcm_push_icon\"\r\n  },\r\n  \"to\":"
					+ fcmId + ",\r\n  \"priority\":\"high\"\r\n}");

			Request request = new Request.Builder().url(notificationurl).method("POST", body)
					.addHeader("Content-Type", "application/json").addHeader("Authorization", notificationkey)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			response.close();
			LOG.error("PushNotification::pushNotificationSend(): push notification status " + response);
		} catch (Exception e) {
			LOG.error("PushNotification::pushNotificationSend():" + e.getMessage());
		}
	}

}
