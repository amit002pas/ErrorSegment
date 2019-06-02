/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.gateway.authenticate;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
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
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;


@RunWith(SpringJUnit4ClassRunner.class)
public class LDAPAuthenticationClientImplTest { 

    @InjectMocks
    LDAPAuthenticationClientImpl lDAPAuthenticationClientImpl;
    
	@Mock
	HttpServletRequest request;
	
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
    	String Authentication_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";
    	String response = restTemplate.postForObject(Authentication_URL, request, String.class);
    	String tokenGenerated = null;
    	try{
    		JSONObject jsonObject = new JSONObject(response);
    		tokenGenerated = jsonObject.get("token").toString();
    	} catch(Exception ex){
    		System.out.println("error while parsing token response"); 
    	}
       	return tokenGenerated;
    }

	@Test
	public void testAuthenticateUser() throws Exception{
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		boolean isFiremem = true;
		Response result = lDAPAuthenticationClientImpl.authenticateuser(authenticationRequest.toJSON(), isFiremem);
		Response result2 = lDAPAuthenticationClientImpl.authenticateuser(authenticationRequest.toJSON(), false);
		assertNotNull(result.toString());
		assertNotNull(result2.toString());
	}
	
	@Test
	public void testAuthenticateRequest() throws Exception{
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		Response result2 = lDAPAuthenticationClientImpl.authenticateRequest(authenticationRequest.toJSON());
		assertNotNull(result2.toString());
	}
	
	@Test
	public void testValidateToken() throws Exception{
		String token = LDAPAuthenticationClientImplTest.generateToken();
		Response result3 = lDAPAuthenticationClientImpl.validateToken(token);
		assertNotNull(result3.toString());
	}
	
	@Test
	public void testAuthorizeUser() throws Exception{
		Set<String> userGroupsNull = null;
		lDAPAuthenticationClientImpl.authorizeUser(userGroupsNull);
		
		Set<String> userGroupsEmpty = new HashSet<String>();;
		userGroupsEmpty.add("");
		lDAPAuthenticationClientImpl.authorizeUser(userGroupsEmpty);
		
		Set<String> userGroups = new HashSet<String>(); 
		userGroups.add("Tools-Firemem-Prod");  
		userGroups.add("ROLE_#All-India");
		lDAPAuthenticationClientImpl.authorizeUser(userGroups);	
	}
	
	
}

