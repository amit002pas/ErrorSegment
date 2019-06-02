/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.errorbucket;

import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.FirememRequest;
import com.yodlee.health.errorsegment.datatypes.ToolsResponseHandler;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;


@Named
public class FirememConnect {
	
	
	public static HttpHeaders getHeader(String accessToken) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set(GatewayConstants.AUTHENTICATION, GatewayConstants.BEARER + accessToken);
		return header;
	}
	public int checkCurrentFiremem(String hammerToken, CacheItem cii) throws InterruptedException, JSONException {

		HttpHeaders headers = null;
		String token = hammerToken;
		headers = getHeader(token);
		FirememRequest firememRequest = new FirememRequest();
		firememRequest.setDbName(cii.getDbId());
		firememRequest.setItemId(cii.getCacheItemId());
		Gson gson = new Gson();
		String requestJson = gson.toJson(firememRequest);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestJson, headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ToolsResponseHandler());
		String firememResponse = restTemplate.postForObject(GatewayConstants.Firemem_Trigger_URL, requestEntity,
				String.class);
		JSONObject firemem = new JSONObject(firememResponse.toString());
		String requestId = firemem.getString("appRequestId");
		String requestJson1 = " {\"appRequestId\":\"" + requestId + "\"}";

		HttpEntity<String> requestEntity1 = new HttpEntity<String>(requestJson1, headers);
		int retry = 0;
		int status1 = -1;
		while (retry < 3) {
			Thread.sleep(15000);
			String firememResult = restTemplate.postForObject(GatewayConstants.Firemem_Status_URL, requestEntity1,
					String.class);
			JSONObject firememStatus = new JSONObject(firememResult.toString());
			try {
				JSONArray status = (JSONArray) firememStatus.get("receivedResponses");
				JSONObject js = (JSONObject) status.get(0);
				status1 = js.getInt("fmStatus");
				break;
			} catch (JSONException ex) {
			}
			retry++;
		}
		return status1;
	}


	
	}
