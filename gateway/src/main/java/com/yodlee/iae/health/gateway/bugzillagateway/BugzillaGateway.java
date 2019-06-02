/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.bugzillagateway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.j2bugzilla.base.BugzillaException;
import com.yodlee.health.errorsegment.datatypes.BugFetchResponse;
import com.yodlee.health.errorsegment.datatypes.BugResponse;
import com.yodlee.health.errorsegment.datatypes.CreateBugResponse;
import com.yodlee.health.errorsegment.datatypes.SearchBugResponse;
import com.yodlee.health.errorsegment.datatypes.ToolsResponseHandler;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.Attachment;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.RequestBugUpdate;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;

/**
 * 
 * @author srai
 *
 */
@Named
public class BugzillaGateway {

	@Inject
	RestTemplate restTemplate;

	@Value("${presruser_pass}")
	private String presrEncrPwd;

	@Value("${mysql_pass}")
	private String sqlPassword;

	static Logger logger = LoggerFactory.getLogger(BugzillaGateway.class);

	public boolean updateBug(Map<String, String> bugDetails) {
		

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = GatewayConstants.BUGR_UPDATE_BUG_URL;
		url = url.replace(GatewayConstants.INPUT_BUGID,
				bugDetails.get(GatewayConstants.BUG_ID));
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(
				bugDetails, headers);
		System.out.println("updateBug Request " + request);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,
				request, String.class);

		String input = responseEntity.getBody();
		Gson gson = new Gson();
		BugResponse bugResponse = gson.fromJson(input, BugResponse.class);
		if (bugResponse != null && bugResponse.getBugs() != null
				&& bugResponse.getBugs().size() > 0)
			return true;

		return false;

	}

	public CreateBugResponse createBug(Map<String, String> bugDetails)
			throws BugzillaException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ToolsResponseHandler());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = GatewayConstants.BUGR_CREATE_BUG_SYNTHETIC_URL;
		Gson gson = new Gson();

		String bugRequest = gson.toJson(bugDetails);

		HttpEntity<String> requestEntity = new HttpEntity<String>(bugRequest,
				headers);
		logger.info("createBug Request " + requestEntity);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,
				requestEntity, String.class);

		String input = responseEntity.getBody();

		CreateBugResponse bugResponse = gson.fromJson(input,
				CreateBugResponse.class);
		logger.info("++++++++++createBug BugResponse " + bugResponse.getStatus());

		return bugResponse;
	}

	public BugFetchResponse getBugDetails(String bugID) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = GatewayConstants.BUGR_FETCH_BUG_SYNTHETIC_URL;
		url = url.replace("INPUT_BUGID", bugID);
		HttpEntity<String> request = new HttpEntity<String>(null, headers);
		ResponseEntity<BugFetchResponse> response = restTemplate.exchange(url,
				HttpMethod.GET, request, BugFetchResponse.class);
		return response.getBody();
	}

	public Attachment loadFile(String htmlString) throws IOException {

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;
		File htmlTempFile = File.createTempFile("JuggerNaut_Analysis", ".html");
		String htmlFileName = htmlTempFile.toString();
		String fileNameAfterSubstring = htmlFileName.substring(
				htmlFileName.lastIndexOf("JuggerNaut_Analysis"),
				htmlFileName.length());

		BufferedWriter writer = new BufferedWriter(new FileWriter(htmlTempFile));
		writer.write(htmlString);
		writer.close();

		bytesArray = new byte[(int) htmlTempFile.length()];
		fileInputStream = new FileInputStream(htmlTempFile);
		fileInputStream.read(bytesArray);
		fileInputStream.close();

		byte[] base64bytes = Base64.encodeBase64(bytesArray);
		Attachment attachment = new Attachment();
		attachment.setAttachmentName(fileNameAfterSubstring);
		attachment.setAttachment(base64bytes);

		return attachment;
	}

	public static Resource getUserFileResource(String htmlString)
			throws IOException {
		Path tempFile = Files.createTempFile("JuggerNaut_Analysis", ".html");
		Files.write(tempFile, htmlString.getBytes());
		File file = tempFile.toFile();
		return new FileSystemResource(file);
	}

	public JsonNode updateBugAttachment(String htmlString, String bugId)
			throws JsonParseException, IOException {

		Attachment attachment = loadFile(htmlString);
		Gson gson = new Gson();
		String requestJsonInput = gson.toJson(attachment);

		requestJsonInput = "[" + requestJsonInput + "]";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestJsonInput,
				headers);

		String URL = GatewayConstants.BUGR_ADD_ATTACHMENT_SYNTHETIC_URL
				.replace(GatewayConstants.INPUT_BUGID, bugId);
		
		ResponseEntity<String> response = restTemplate.exchange(URL,
				HttpMethod.POST, requestEntity, String.class);
		
		String responseString = response.getBody();

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(responseString);
		JsonNode actualObj = mapper.readTree(parser);

		return actualObj;

	}

	
	public CreateBugResponse updateBugField(String bugId,
			RequestBugUpdate requestBugUpdate) {
		Gson gson = new Gson();
		String requestJsonInput = gson.toJson(requestBugUpdate);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(
				requestJsonInput, header);

		CreateBugResponse response = new CreateBugResponse();
		String url = GatewayConstants.BUGR_UPDATE_BUG_SYNTHETIC_URL.replace(GatewayConstants.INPUT_BUGID, bugId);
		response = restTemplate.postForObject(url, requestEntity,
				CreateBugResponse.class);
	   return response;

	}

	public void updateAttachmentJN(Attachment attach, String bugId) {
		String URL = GatewayConstants.BUGR_ADD_ATTACHMENT_SYNTHETIC_URL.replace(GatewayConstants.INPUT_BUGID, bugId);
		Gson gson = new Gson();
		String body = gson.toJson(attach);
		body = "[" + body + "]";
		HttpHeaders header = new HttpHeaders();
		header.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, header);
		RestTemplate restTemplate1 = new RestTemplate();
		String response=restTemplate1.postForObject(URL, requestEntity, String.class);
		System.out.println("Response for updateAttachmentJN"+response);

	}

	public SearchBugResponse getBugDetailsForHour() {

		restTemplate.setErrorHandler(new ToolsResponseHandler());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String endDate = dateFormat.format(new Date());
		String startDate = dateFormat.format(new Date(System
				.currentTimeMillis() - 2 * 3600 * 1000));
		String body = "{\r\n  \"timeStamp\": {\r\n    \"startTime\": \""
				+ startDate
				+ "\",\r\n    \"endTime\": \""
				+ endDate
				+ "\"\n  },\r\n  \"filters\": {\r\n    \"keyword\": \"\",\r\n    \"whiteboard\": \"\",\r\n    \"summary\": \"\",\r\n    \"siteid\": \"\",\r\n    \"sourceProduct\": \"PRE_SR\",\r\n    \"agentNames\": [],\r\n    \"errorCodes\": []\r\n  },\r\n  \"sort\": {\r\n    \"category\": \"FAILURE\",\r\n    \"order\": \"DESCENDING\"\r\n  },\r\n  \"pageNum\": 1\r\n}";
		String url = GatewayConstants.BUGR_SEARCH_HOURLY_BUG_SYNTHETIC_URL;
		HttpEntity<String> request = new HttpEntity<String>(body, headers);
		// logger.info("Request for bug fetching" + request);
		ResponseEntity<String> response = restTemplate.postForEntity(url,
				request, String.class);
		logger.info("Response" + response.getStatusCodeValue());
		Gson gson = new Gson();
		SearchBugResponse searchBugResponse = gson.fromJson(response.getBody(),
				SearchBugResponse.class);

		return searchBugResponse;
	}
}
