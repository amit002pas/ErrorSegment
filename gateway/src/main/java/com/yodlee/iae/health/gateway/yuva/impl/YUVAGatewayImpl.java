/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.yuva.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.health.errorsegment.datatypes.forecast.YuvaResp;
import com.yodlee.health.errorsegment.datatypes.forecast.YuvaSegmentListResponse;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.health.errorsegment.gateway.util.GatewayUtils;
import com.yodlee.iae.health.gateway.yuva.IYUVAGateway;

@Named
public class YUVAGatewayImpl implements IYUVAGateway {

	@Inject
	GatewayUtils gatewayUtils;

	@Inject
	YuvaGetCiiForSegment yuvaGetCiiForSegment;

	private static JSONObject jsonToken;

	@CacheEvict(value = "yuvacache")
	//@Scheduled(fixedDelay = 86400 * 1000)
	public void yuvaCacheEvict() {
	}

	@Override
	//@Cacheable(value = "yuvacache")
	public List<Segment> getYUVASegmentForAgent(String agentName) throws JSONException, IOException {

		initializeToken();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<>(getHeader(jsonToken.getString("token")));
		ResponseEntity<String> response = null;
		String url = GatewayConstants.SEGMENT_LIST_URL;
		url = url + agentName.toLowerCase();
		try {
			response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		} catch (Exception e) {
			if (response == null)
				return null;
		}

		String input = response.getBody();
		Gson gson = new Gson();
		YuvaResp res[] = gson.fromJson(input, YuvaResp[].class);
		List<YuvaResp> dataOutputList = Arrays.asList(res);
		List<YuvaSegmentListResponse> segment = dataOutputList.get(0).getSegments();
		List<String> segmentList = segment.stream().map(m -> m.getSegmentId()).collect(Collectors.toList());
		System.out.println("Size returend:" + segmentList.size());
		if (segmentList != null) {
			CompletableFuture<?>[] responseList = new CompletableFuture<?>[segmentList.size()];

			for (int i = 0; i < segmentList.size(); i++) {
				try {

					responseList[i] = yuvaGetCiiForSegment.getSegmentCiiList(segmentList.get(i), jsonToken);

				} catch (Exception e) {

				}
			}
			CompletableFuture.allOf(responseList).join();

			List<Segment> finalList = new ArrayList<>();
			for (CompletableFuture<?> com : responseList) {
				try {
					finalList.add((Segment) com.get());
				} catch (Exception e) {
					System.out.println("Inside Future get Exception");
				}
			}

			return finalList;
		}
		return new ArrayList<Segment>();

	}

	private static HttpHeaders getHeader(String accessToken) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set(GatewayConstants.AUTHENTICATION_KEY, accessToken);
		return header;
	}

	private void initializeToken() throws JSONException, IOException {
		jsonToken = new JSONObject(gatewayUtils.generateToken(false) + "");
	}

}
