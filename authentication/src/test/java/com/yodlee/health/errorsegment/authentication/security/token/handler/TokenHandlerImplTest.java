/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.authentication.security.token.handler;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.authentication.security.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class TokenHandlerImplTest {
	
	@InjectMocks
	TokenHandlerImpl tokenHandlerImpl;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	User user;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
	}
	
	@Test
	public void testUserAndGetTokenValue() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcmVzcnVzZXIiLCJpc3MiOiJoYW1tZXIiLCJpYXQiOjE1NDcwMTk4MzUsImV4cCI6MTU0NzAyMTYzNX0.dWxkwbxBRnoVx0a_hwz3EUkb6ZD1hr5B7q0M0eDIErVu4srWmZCBJ5-Zl9WHUicKef8Skv4lbcMtNeIIiT93Zw";
		String removeChar = "";
		tokenHandlerImpl.getUser(token);
		assertNotNull(tokenHandlerImpl.toString());
		
		String result2 = tokenHandlerImpl.gettokenvalue(token, removeChar);
		assertNotNull(result2.toString());
	}
	
	@Test
	public void testGenerateTokenAndGetCurrentDate() throws Exception{
		String result3 = tokenHandlerImpl.generateToken(user, request);
		assertNotNull(result3.toString());
		
		Date result4 = tokenHandlerImpl.getCurrentDate();
		assertNotNull(result4.toString());
		
	}
		

}
