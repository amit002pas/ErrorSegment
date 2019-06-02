/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.gateway.jn;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author vchhetri
 *
 */
@Named
@Scope("prototype")
public class JNTokenGenerator {

	/**
	 * Method to generate token for authentication.
	 * @param url 
	 * @param userName
	 * @param password
	 * @return token string
	 */
	public String generateToken(String url , String userName , String password) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", userName);
		map.put("password", password);
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, httpHeaders);		
		String response = restTemplate.postForObject(url, request, String.class);
		String tokenGenerated = null;
		try {
			JSONObject json = new JSONObject(response);
			tokenGenerated = json.get("token").toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tokenGenerated;
	}

}
