package com.csmpl.wrsis.webportal.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
 
	 *  This Utill class is design for  getting the altitude value 
	 *  from lat & long through API call.
	 *  
	 * 
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since   11-8-2020
	 * */
@Component
public class LatLongUtill {

	public static final Logger LOG = LoggerFactory.getLogger(LatLongUtill.class);


	@Value("${altitude.api.uri}")
	private  String altitudeURI;

	@Value("${altitude.api.token}")
	private  String altitudeToken;

	public  String getAltitudeFromAPI(String latitude, String longitude) {
		String response = null;
		try {

			String uri = altitudeURI + "?key=" + altitudeToken + "&shapeFormat=raw&latLngCollection=" + latitude + ","
					+ longitude; 
			LOG.info("LatLongUtill::getAltitudeFromAPI():Altidue API URL-"+uri);
			RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
			String result = restTemplate.getForObject(uri, String.class);
			LOG.info("LatLongUtill::getAltitudeFromAPI():Altidue API result-"+result);
			if(result!=null) {
				JSONObject resultJson = new JSONObject(result);
				JSONArray jsonArray = resultJson.getJSONArray("elevationProfile");
				LOG.info("LatLongUtill::getAltitudeFromAPI():jsonArray-"+jsonArray);
				if(jsonArray!=null){
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject json = jsonArray.getJSONObject(i);
						response = json.get("height").toString();
						LOG.info("LatLongUtill::getAltitudeFromAPI():height-"+response);
					}//end of for
				}//end of jsonArray
			}//end of result

		} catch (Exception e) {
			LOG.error("LatLongUtill::getAltitudeFromAPI():Error on Altidue API calling:-" + e);
		}
		return response;

	}
	
	//Override timeouts in request factory
	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() 
	{
	    SimpleClientHttpRequestFactory clientHttpRequestFactory
	                      = new SimpleClientHttpRequestFactory();
	    //Connect timeout
	    clientHttpRequestFactory.setConnectTimeout(10_000);
	     
	    //Read timeout
	    clientHttpRequestFactory.setReadTimeout(10_000);
	    return clientHttpRequestFactory;
	}

}