/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 *//*
package com.yodlee.health.errorsegment.jnanalysis.rest.impl;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisRequest;
import com.yodlee.health.errorsegment.jnanalysis.rest.impl.JNAnalysisServiceRestImpl;
import com.yodlee.iae.health.jnanalysis.service.JNBugzillaBugAnalysisService;
import com.yodlee.iae.health.jnanalysis.service.JNSyntheticBugAnalysisServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
public class JNAnalysisServiceRestImplTest {
	
	@InjectMocks
	private JNAnalysisServiceRestImpl jnAnalysisServiceRestImpl;
	
	@Mock
	private JNBugzillaBugAnalysisService jnBugAnalysisService;
	
	@Mock
	private JNSyntheticBugAnalysisServiceImpl jnSyntheticBugAnalysisServiceImpl;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testJNAnalysisForbugIAT(){
		JNAnalysisRequest jnAnalysisRequest = new JNAnalysisRequest();
		jnAnalysisRequest.setBugID("1234567");
		jnAnalysisRequest.setReportedBy("AIM");
		//Response res = jnAnalysisServiceRestImpl.bugAnalysis(jnAnalysisRequest);
		//assertEquals(res.getStatus(), 200);
	}
	
	@Test
	public void testJNAnalysisForbugPreSR(){
		JNAnalysisRequest jnAnalysisRequest = new JNAnalysisRequest();
		jnAnalysisRequest.setBugID("5ca2e609472f8b2630e900da123");
		//jnAnalysisRequest.setSyntheticBug(true);
		//Response res = jnAnalysisServiceRestImpl.bugAnalysis(jnAnalysisRequest);
		//assertEquals(res.getStatus(), 200);
	}
}
*/