/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
public class GatewayUtils {

	
	@Inject 
	private KeystoreGateway keystoreGateway ;
	
	@Value("${presruser_pass}")
	private String presrEncrPwd ;
	
	static Logger logger = LoggerFactory.getLogger(GatewayUtils.class);

	public JsonNode generateToken(boolean ishammerAuthentication) throws JsonParseException, IOException {
		logger.info("Inside generate token");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ToolsResponsehandler());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> map = new HashMap<String, String>();
		map.put(GatewayConstants.USERNAME_CONSTANT, GatewayConstants.USERNAME_VALUE);
		map.put(GatewayConstants.PASSWORD_CONSTANT, keystoreGateway.getAESDecryptedValue(presrEncrPwd));
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
		logger.debug(">>>request: {}", request.getBody());
		String response = null;
		if (ishammerAuthentication) {
			response = restTemplate.postForObject(GatewayConstants.HAMMER_AUTHENTICATION_URL, request, String.class);
		} else {
			response = restTemplate.postForObject(GatewayConstants.AUTHENTICATION_URL, request, String.class);
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		logger.debug(">>>response: {}", actualObj);
		return actualObj;
	}
}
