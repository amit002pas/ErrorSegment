/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */ 
package com.yodlee.health.errorsegment.schedular;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.support.RequestContext;

@RunWith(SpringJUnit4ClassRunner.class)
public class ForecastSchedularTest {

	@InjectMocks
	private ForecastSchedular forecastSchedular;

	@Mock
	@Context
	UriInfo info;

	@Mock
	private HttpServletRequest request;

	@Mock
	private WebTarget webTarget;

	@Mock
	private Client client;

	@Mock
	private ClientBuilder clientBuilder;

	@Mock
	private ResponseBuilder responseBuilder;
	@Mock
	private RequestContext requestContext;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() {
		List<String> agentList = new ArrayList<String>();
		agentList.add("CharlesSchwabStocks");
		agentList.add("ETradeInvestments");
		try{
			forecastSchedular.scheduleRunForecaster("",agentList);
		} catch(Exception ex){
			Assert.assertEquals("java.net.ConnectException: Connection refused: connect", ex.getMessage());
		}
	}

}
