/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security.handler;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.authentication.security.User;
import com.yodlee.health.errorsegment.authentication.security.token.handler.TokenHandler;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomAuthenticationSuccessHandlerTest {

	@InjectMocks
	CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Mock
	HttpServletRequest request; 
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	Authentication authentication;
	
	@Mock
	TokenHandler tokenHandler;
	
	@Mock 
	User user;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);   
	}
	
	@Test
	public void testOnAuthenticationSuccess() throws IOException, ServletException{
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmVzcnVzZXIiLCJpc3MiOiJoYW1tZXIiLCJpYXQiOjE1NDY5NDk5MDAsImV4cCI6MTU0Njk1MTcwMH0.s_fr5qrP9_PO_TI_hS2fup55RdBjHmsTkPCOjaV3-d2fDKndUSZ5EGxeDEc5eVt4GYij6SsoNoWb0uPdOFDxmw";
		when(tokenHandler.generateToken(any(User.class), any(HttpServletRequest.class))).thenReturn(token);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		when(authentication.getDetails()).thenReturn(authenticationRequest);
		String str = "print";
		PrintWriter printWriter = new PrintWriter(str);
		when(response.getWriter()).thenReturn(printWriter); 
		when(authentication.getPrincipal()).thenReturn(user);
		
		customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		assertNotNull(customAuthenticationSuccessHandler.toString());
	}
	
	@Test
	public void testOnAuthenticationFailure() throws IOException, ServletException{
		when(tokenHandler.generateToken(any(User.class), any(HttpServletRequest.class))).thenReturn(null);
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername("presruser");
		authenticationRequest.setPassword("Welcome@321");
		when(authentication.getDetails()).thenReturn(authenticationRequest);
		String str = "print";
		PrintWriter printWriter = new PrintWriter(str);
		when(response.getWriter()).thenReturn(printWriter); 
		when(authentication.getPrincipal()).thenReturn(user);
		
		customAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		assertNotNull(customAuthenticationSuccessHandler.toString());
	}
	
}
