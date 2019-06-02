/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security.service;

import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClient;
import com.yodlee.health.errorsegment.gateway.util.IntegrationConstant;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserDetailsServiceImplTest {

	@InjectMocks
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	LDAPAuthenticationClient lDAPAuthenticationClient;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
	}
	 
	@Test 
	public void testLoadUserByUsername() throws Exception{
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHENTICATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(authenticationRequest));
		when(lDAPAuthenticationClient.authenticateuser(authenticationRequest.toJSON(), false)).thenReturn(response);
		
		userDetailsServiceImpl.loadUserByUsername(authenticationRequest.toJSON());		
	}
	
	@Test 
	public void testLoadUserByUsernameException() throws Exception{
		try{
			userDetailsServiceImpl.loadUserByUsername(null);
		}catch(UsernameNotFoundException unfe){
			unfe.printStackTrace();
		}	
	}
	
	@Test 
	public void testException() throws Exception{
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		try{
			userDetailsServiceImpl.loadUserByUsername(authenticationRequest.toJSON());
		}catch(UsernameNotFoundException unfe){
			unfe.printStackTrace();
		}				
	}
	
	@Test
	public void testTokenIsNull() throws Exception {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("abc");
		authenticationRequest.setPassword("Welcome@321");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(IntegrationConstant.AUTHENTICATION_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(authenticationRequest));
		when(lDAPAuthenticationClient.authenticateuser(authenticationRequest.toJSON(), false)).thenReturn(response);
	    
	    userDetailsServiceImpl.loadUserByUsername(authenticationRequest.toJSON());
	}
	
}
