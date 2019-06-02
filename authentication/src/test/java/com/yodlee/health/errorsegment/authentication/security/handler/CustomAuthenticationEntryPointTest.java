/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security.handler;

import static org.junit.Assert.assertNotNull;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomAuthenticationEntryPointTest {
	
	@InjectMocks
	CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Mock
	HttpServletRequest request; 
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	AuthenticationException authException;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);  
	}
	
	@Test
	public void testCommence() throws IOException, ServletException{
		String str = "print";
		PrintWriter printWriter = new PrintWriter(str);
		when(response.getWriter()).thenReturn(printWriter);
		when(authException.getLocalizedMessage()).thenReturn("Success");
		customAuthenticationEntryPoint.commence(request, response, authException);
		assertNotNull(customAuthenticationEntryPoint.toString());
	}

}
