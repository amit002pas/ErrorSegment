/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.authenticationimpl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.yodlee.health.errorsegment.datatypes.ToolsResponseHandler;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClient;
import com.yodlee.health.errorsegment.gateway.util.IntegrationConstant;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationServiceImplTest {
	
	@InjectMocks
	AuthenticationServiceImpl authenticationServiceImpl;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	LDAPAuthenticationClient lDAPAuthenticationClient;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	public static String generateToken() {
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.setErrorHandler(new ToolsResponseHandler());
    	HttpHeaders httpHeaders = new HttpHeaders();
    	httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("username","presruser");
    	map.put("password", "Welcome@321");
    	HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(map,httpHeaders);
    	//String Authentication_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";
    	String response = restTemplate.postForObject(IntegrationConstant.AUTHENTICATION_URL, request, String.class);
    	String tokenGenerated = null;
    	try{
    		JSONObject jsonObject = XML.toJSONObject(response);
    		JSONObject json=jsonObject.getJSONObject("AuthenticationResponse");
    		tokenGenerated = json.get("token").toString();
    	} catch(Exception ex){
    		System.out.println("error while parsing token response");
    	}
       	return tokenGenerated;
    }
		
	@SuppressWarnings("unchecked")
	@Test
	public void testLogin() throws Exception{
		
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		
		AuthenticationServiceImplTest authenticationServiceImplTest = new AuthenticationServiceImplTest();
		Response response = authenticationServiceImplTest.postRequestWithValidUser();
		when(lDAPAuthenticationClient.authenticateRequest(authenticationRequest.toJSON())).thenReturn(response); 
		
		Response response2 = authenticationServiceImplTest.tokenValidation(); 
		when(lDAPAuthenticationClient.validateToken(any(String.class))).thenReturn(response2);
		
		when(lDAPAuthenticationClient.authorizeUser(any(HashSet.class))).thenReturn(true);
		
		Response result = authenticationServiceImpl.login(request, authenticationRequest.toJSON());
		assertNotNull(result.toString());
	}

	@Test
	public void testLoginAuthenticationRequestIsNull() throws Exception{
		
		Response result = authenticationServiceImpl.login(request, null);
		assertNotNull(result.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoginIsNotAuthorized() throws Exception{
		
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");  
		
		AuthenticationServiceImplTest authenticationServiceImplTest = new AuthenticationServiceImplTest();
		Response response = authenticationServiceImplTest.postRequestWithValidUser();
		when(lDAPAuthenticationClient.authenticateRequest(authenticationRequest.toJSON())).thenReturn(response); 
		
		Response response2 = authenticationServiceImplTest.tokenValidation(); 
		when(lDAPAuthenticationClient.validateToken(any(String.class))).thenReturn(response2);
		
		when(lDAPAuthenticationClient.authorizeUser(any(HashSet.class))).thenReturn(false);
		
		Response result = authenticationServiceImpl.login(request, authenticationRequest.toJSON());
		assertNotNull(result.toString()); 
	}
	
	@Test
	public void testLoginIsNotAuthenticated() throws Exception{
		
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("abc");
		authenticationRequest.setPassword("Welcome@321");
		
		AuthenticationServiceImplTest authenticationServiceImplTest = new AuthenticationServiceImplTest();
		Response response = authenticationServiceImplTest.postRequest();
		when(lDAPAuthenticationClient.authenticateRequest(authenticationRequest.toJSON())).thenReturn(response); 
		
		Response result = authenticationServiceImpl.login(request, authenticationRequest.toJSON());
		assertNotNull(result.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAuthorizationResponseInvalid() throws Exception{
		
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		
		AuthenticationServiceImplTest authenticationServiceImplTest = new AuthenticationServiceImplTest();
		Response response = authenticationServiceImplTest.postRequestWithValidUser();
		when(lDAPAuthenticationClient.authenticateRequest(authenticationRequest.toJSON())).thenReturn(response); 
		
		Response response2 = authenticationServiceImplTest.tokenValidationTesting(); 
		when(lDAPAuthenticationClient.validateToken(any(String.class))).thenReturn(response2);
		
		when(lDAPAuthenticationClient.authorizeUser(any(HashSet.class))).thenReturn(true);
		
		Response result = authenticationServiceImpl.login(request, authenticationRequest.toJSON());
		assertNotNull(result.toString());
	}
		
	public Response postRequest(){
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("abc");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHENTICATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(authenticationRequest));
		return response;
	}
	
	public Response postRequestWithValidUser(){
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHENTICATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(authenticationRequest));
		return response;
	}
	
	public Response tokenValidation(){
		String token = AuthenticationServiceImplTest.generateToken();
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHORIZATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>(); 
	    multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
	    multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
	    multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, "Bearer" + " " + token);
	    Response response = target.request().headers(multivaluedMap).get();
	    return response;
    }
	
	public Response tokenValidationTesting(){
		String token = "Dummy Token";
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHORIZATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>(); 
	    multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
	    multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
	    multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, "Bearer" + " " + token);
	    Response response = target.request().headers(multivaluedMap).get();
	    return response;
    }
	
}

