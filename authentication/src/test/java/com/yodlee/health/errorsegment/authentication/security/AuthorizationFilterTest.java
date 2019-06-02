/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.yodlee.health.errorsegment.authentication.security.token.handler.FailureHandler;
import com.yodlee.health.errorsegment.datatypes.ToolsResponseHandler;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClientImpl;
import com.yodlee.health.errorsegment.gateway.util.IntegrationConstant;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorizationFilterTest {
	
	@InjectMocks
	AuthorizationFilter authorizationFilter;
	
	@Mock
	AuthenticationManager authenticationManager;
	
	@Mock 
	LDAPAuthenticationClientImpl lDAPAuthenticationClientImpl;
	
	@Mock
	FailureHandler failureHandler;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	FilterChain filterChain;
	
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
    		System.out.println("error while parsing token response"+ex);
    	}
       	return tokenGenerated;
    }
	
	@Test
	public void testAuthorizationFilter(){
		authorizationFilter.setTokenHandler(lDAPAuthenticationClientImpl);
		authorizationFilter.setFailureHandler(failureHandler);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDoFilterInternal() throws Exception{
		
		String token = "Bearer" + " " + AuthorizationFilterTest.generateToken();  
		String tokenValue = token.replace("Bearer", "").trim();
		when(request.getServletPath()).thenReturn("R/A/L");  
		when(request.getPathInfo()).thenReturn(null);
		when(request.getHeader("Authorization")).thenReturn(token); 
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHORIZATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, token);
		Response response2 = target.request().headers(multivaluedMap).get();
		when(lDAPAuthenticationClientImpl.validateToken(tokenValue)).thenReturn(response2);
		
		when(lDAPAuthenticationClientImpl.authorizeUser(any(HashSet.class))).thenReturn(true);
		
		authorizationFilter.doFilterInternal(request, response, filterChain);
		assertNotNull(authorizationFilter.toString());
	}
	
	@Test
	public void testDoFilterInternalNegative() throws Exception{
		
		when(request.getServletPath()).thenReturn("R");
		when(request.getPathInfo()).thenReturn(null);
		authorizationFilter.doFilterInternal(request, response, filterChain);
		
		when(request.getServletPath()).thenReturn("/R/A/L"); 
		when(request.getPathInfo()).thenReturn(null);
		authorizationFilter.doFilterInternal(request, response, filterChain);
		
		when(request.getServletPath()).thenReturn("R/A/L");  
		when(request.getPathInfo()).thenReturn(null);
		when(request.getHeader("Authorization")).thenReturn(null);		
		authorizationFilter.doFilterInternal(request, response, filterChain);
		
		when(request.getServletPath()).thenReturn("R/A/L");  
		when(request.getPathInfo()).thenReturn(null);
		when(request.getHeader("Authorization")).thenReturn("");
		authorizationFilter.doFilterInternal(request, response, filterChain);
		
		assertNotNull(authorizationFilter.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUserIsNotAuthorized() throws Exception{
		
		String token = "Bearer" + " " + AuthorizationFilterTest.generateToken();  
		String tokenValue = token.replace("Bearer", "").trim();
		when(request.getServletPath()).thenReturn("R/A/L");  
		when(request.getPathInfo()).thenReturn(null);
		when(request.getHeader("Authorization")).thenReturn(token);
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHORIZATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, token);
		Response response2 = target.request().headers(multivaluedMap).get();
		when(lDAPAuthenticationClientImpl.validateToken(tokenValue)).thenReturn(response2);
		
		when(lDAPAuthenticationClientImpl.authorizeUser(any(HashSet.class))).thenReturn(false);
		
		authorizationFilter.doFilterInternal(request, response, filterChain);
		assertNotNull(authorizationFilter.toString());
	}
	
	@Test
	public void testAuthorizationResponseIsNotValid() throws Exception{
		
		String token = "Bearer" + " " + "dummy token";  
		String tokenValue = token.replace("Bearer", "").trim();
		when(request.getServletPath()).thenReturn("R/A/L");  
		when(request.getPathInfo()).thenReturn(null);
		when(request.getHeader("Authorization")).thenReturn(token);
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHORIZATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, token);
		Response response2 = target.request().headers(multivaluedMap).get();
		when(lDAPAuthenticationClientImpl.validateToken(tokenValue)).thenReturn(response2);
		
		authorizationFilter.doFilterInternal(request, response, filterChain);
		assertNotNull(authorizationFilter.toString());
	}
	
}
