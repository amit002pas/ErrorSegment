/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.yuva.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.forecast.CacheItemDB;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.health.errorsegment.gateway.util.ToolsResponsehandler;

@Named
public class YuvaGetCiiForSegment {
	
	
	
	@Async
	public CompletableFuture<Segment> getSegmentCiiList(String segID,JSONObject jsonToken) throws Exception {

		if (jsonToken.get("authentication").equals("success")) {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(new ToolsResponsehandler());

			HttpEntity<String> requestEntity = new HttpEntity<>(getHeader(jsonToken.getString("token")));
			String CII_LIST_URL = GatewayConstants.GET_CACHEITEM_URL;
			CII_LIST_URL = CII_LIST_URL.replace("INPUT_SEGID", segID);
			ResponseEntity<String> response = restTemplate.exchange(CII_LIST_URL, HttpMethod.GET, requestEntity,
					String.class);

			JSONArray jsonArray = new JSONArray(response.getBody());
			Gson gson = new Gson();
			CacheItemDB cacheItemDB[] = gson.fromJson(jsonArray.toString(), CacheItemDB[].class);
			List<CacheItemDB> cItemDb = Arrays.asList(cacheItemDB);
			Segment segment = new Segment();
			segment.setSegmentId(segID);
			segment.setCiiDb(cItemDb);

			return CompletableFuture.completedFuture(segment);
		}
		return null;
	}
	
	private static HttpHeaders getHeader(String accessToken) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set(GatewayConstants.AUTHENTICATION_KEY, accessToken);
		return header;
	}



}
