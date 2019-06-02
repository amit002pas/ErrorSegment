/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security.token.handler;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class InvalidTokenHandlerTest {
	
	@InjectMocks
	InvalidTokenHandler invalidTokenHandler;
	
	@Mock
	HttpServletRequest request; 
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	TokenException ex; 
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
	}
	
	@Test
	public void testOnInvalidToken() throws IOException, ServletException{
		String str = "print";
		PrintWriter printWriter = new PrintWriter(str);
		when(ex.getCode()).thenReturn(1);
		when(ex.getLocalizedMessage()).thenReturn("No Reason"); 
		when(response.getWriter()).thenReturn(printWriter);
		invalidTokenHandler.onInvalidToken(request, response, ex);	
		assertNotNull(invalidTokenHandler.toString());
	}

}
