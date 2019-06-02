/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.health.errorsegment.authentication.security.handler.InternalErrorHandler;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationFilterTest {
	
	@InjectMocks
	AuthenticationFilter authenticationFilter;
	
	@Mock 
	AuthenticationManager authenticationManager;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	ObjectMapper objectMapper;
	
	@Mock
	InternalErrorHandler internalErrorHandler;
	
	@Mock
	Exception exception;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
	}
	
	@Test
	public void testAuthenticationManager(){
		authenticationFilter.setAuthenticationManager(authenticationManager);
	} 

	@Test
	public void testAttemptAuthenticationLoginRequestNull() throws IOException, Exception{
		when(request.getMethod()).thenReturn("POST");
		Mockito.doNothing().when(internalErrorHandler).write(request, response, exception);
		authenticationFilter.attemptAuthentication(request, response);
		assertNotNull(authenticationFilter.toString());
	}
	
	@Test
	public void testAttemptAuthenticationException() throws Exception {
		try{
			when(request.getMethod()).thenReturn("GET");
			authenticationFilter.attemptAuthentication(request, response);
		   }catch(AuthenticationServiceException e){
			e.printStackTrace();
		}
		assertNotNull(authenticationFilter.toString());
	}
	
	@Test
	public void testAttemptAuthentication() throws IOException {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		when(request.getMethod()).thenReturn("POST");
		Reader reader = new StringReader("apple\norange\nbanana");
		BufferedReader br = new BufferedReader(reader);
	    when(request.getReader()).thenReturn(br);
		//when(objectMapper.readValue(any(BufferedReader.class), eq(AuthenticationRequest.class))).thenReturn(authenticationRequest);
		authenticationFilter.attemptAuthentication(request, response);
	}
	
}
