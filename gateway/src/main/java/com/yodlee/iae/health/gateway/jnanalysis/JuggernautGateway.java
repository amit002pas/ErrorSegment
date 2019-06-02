/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.gateway.jnanalysis;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.AnalysisDetails;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautFinalResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.Outcome;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.health.errorsegment.gateway.util.JuggerNautConstants;
import com.yodlee.health.errorsegment.gateway.util.ToolsResponsehandler;

@Named
public class JuggernautGateway {

	@Value("${user-username}")
	private String presrUserName;

	@Value("${password-password}")
	private String presrPasword;

	@Inject
	RestTemplate restTemplate;

	public JsonNode generateToken() throws JsonParseException, IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> map = new HashMap<>();
		map.put("username", "presruser");
		map.put("password", "Welcome@321");
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
		// System.out.println("Rquest:"+request);
		String response = restTemplate.postForObject(GatewayConstants.IATAuthenticationURL, request, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		return actualObj;
	}

	public JsonNode fetchIATAnalysisRequestId(String accessToken, JuggerNautInputRequest jnInputRequest)
			throws JsonParseException, IOException {
		Gson gson = new Gson();

		HttpHeaders headers = getHeader(accessToken);
		String requestJson = gson.toJson(jnInputRequest);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestJson, headers);
		System.out.println("Request for getting Request Id: " + requestEntity);
		String response = restTemplate.postForObject(GatewayConstants.IATAnalysisURL, requestEntity, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		return actualObj;

	}

	private HttpHeaders getHeader(String accessToken) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set(JuggerNautConstants.AUTHORIZATION, accessToken);
		return header;
	}

	public String getSecurityToken() {

		String securityToken = "";
		try {
			JsonNode response = generateToken();
			if (response.get(JuggerNautConstants.AUTHENTICATION_KEY).asText().equals(JuggerNautConstants.SUCCESS)) {
				securityToken = response.get(JuggerNautConstants.SECURITY_TOKEN_KEY).asText();
			}
		} catch (JsonParseException e) {
			System.out.println("JsonParseException" + e);
		} catch (Exception e) {
			System.out.println("Token Exception" + e);
		}
		// System.out.println("token for JN analysis ...>> " + securityToken);
		return securityToken;
	}

	public String getAnalysisRequestId(String token, JuggerNautInputRequest juggerNautInputRequest) {
		String analysisReqId = "";
		try {
			JsonNode response = fetchIATAnalysisRequestId(token, juggerNautInputRequest);
			analysisReqId = response.get(JuggerNautConstants.ANALYSISREQUESTID).asText();

		} catch (Exception e) {
			System.out.println("Exception " + e);
			e.printStackTrace();
			return "Not Valid";
		}
		System.out.println("analysisReqId ...>> " + analysisReqId);
		return analysisReqId;
	}

	public JuggerNautFinalResponse fetchIATResponse(String appRequestId, String accessToken) {
		System.out.println("Checking Response for ID:"+appRequestId);
		Gson gson = new Gson();
		JuggerNautFinalResponse appResponse[] = null;
		HttpHeaders headers = getHeader(accessToken);
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

		String response = null;
		try {

			ResponseEntity<String> response1 = restTemplate.exchange(GatewayConstants.IATResponseURL + appRequestId,
					HttpMethod.GET, requestEntity, String.class);
			response = response1.getBody();

			appResponse = gson.fromJson(response, JuggerNautFinalResponse[].class);

		} catch (Exception ex) {
			System.out.println("Exception in fetchIAT Response:"+ex);
		}

		return appResponse[0];

	}

	/**
	 * method to fetch JN response with analysisId
	 * 
	 * @param appRequestId
	 * @param accessToken
	 * @return
	 */
	public List<JuggerNautFinalResponse> fetchIATResponseList(String appRequestId, String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		Gson gson = new Gson();
		HttpHeaders headers = getHeader(accessToken);
		List<JuggerNautFinalResponse> appResponseList = new ArrayList<>();
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		String response = null;
		try {
			ResponseEntity<String> response1 = restTemplate.exchange(GatewayConstants.IATResponseURL + appRequestId,
					HttpMethod.GET, requestEntity, String.class);
			response = response1.getBody();
			Type listType = new TypeToken<ArrayList<JuggerNautFinalResponse>>() {
			}.getType();
			appResponseList = gson.fromJson(response, listType);
		} catch (HttpClientErrorException httpEx) {
			AnalysisDetails analysisDetails = new AnalysisDetails();
			analysisDetails.setAnalysis(httpEx.getMessage());
			Outcome outCome = new Outcome();
			outCome.setAction(JuggerNautConstants.BUG_CREATED);
			JuggerNautFinalResponse juggerNautFinalResponse = new JuggerNautFinalResponse();
			juggerNautFinalResponse.setAnalysisDetails(Arrays.asList(analysisDetails));
			juggerNautFinalResponse.setOutcome(outCome);
			appResponseList.add(juggerNautFinalResponse);
			System.out.println("HttpClientErrorException : " + httpEx.getMessage());
		} catch (JsonSyntaxException jsonEx) {
			System.out.println("parshing exception : " + jsonEx.getMessage());
		}
		return appResponseList;
	}

	public byte[] fetchByteArray(String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ToolsResponsehandler());
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		HttpHeaders headers = new HttpHeaders();

		headers.set(JuggerNautConstants.AUTHORIZATION, getSecurityToken());
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class, "1");

		byte byteArrayofPdf[] = response.getBody();
		return byteArrayofPdf;
	}

}
