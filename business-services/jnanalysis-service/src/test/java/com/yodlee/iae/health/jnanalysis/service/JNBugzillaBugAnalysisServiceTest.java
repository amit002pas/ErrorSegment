/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.iae.health.jnanalysis.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.jnanalysis.service.JNBugzillaBugAnalysisService;

@RunWith(SpringJUnit4ClassRunner.class)
public class JNBugzillaBugAnalysisServiceTest {
	
	@InjectMocks
	JNBugzillaBugAnalysisService jnBugzillaBugAnalysisService;
	
	@Mock
	MongoOperations mongoOps;
	
	@Mock
	JuggernautGateway juggernautGateway;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testExecuteImplInProgressReq(){
		String bugId = "123456"; 
		jnBugzillaBugAnalysisService.accept(bugId);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("bugId").is(bugId));
		List<JNAnalysisTriggeredItem> list = new ArrayList<>();
		JNAnalysisTriggeredItem jnAnalysisTriggeredItem = new JNAnalysisTriggeredItem();
		jnAnalysisTriggeredItem.setStatus("In Progress");
		list.add(jnAnalysisTriggeredItem);				
		when(mongoOps.find(query, JNAnalysisTriggeredItem.class,"JNAnalysisTriggerData")).thenReturn(list);
		
		jnBugzillaBugAnalysisService.executeImpl();
		assertEquals(jnBugzillaBugAnalysisService.get(), "In Progress");		
	}
	
	@Test
	public void testExecuteImplValidTriggeredReq(){
		String bugId = "123456";
		jnBugzillaBugAnalysisService.accept(bugId);
		
		JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
		juggerNautrequest.setItemID(bugId);
		juggerNautrequest.setItemType("2");
		juggerNautrequest.setCobrandID("");
		juggerNautrequest.setCreateBug(false);
		
		when(juggernautGateway.getSecurityToken()).thenReturn("DummyToken");
		when(juggernautGateway.getAnalysisRequestId("DummyToken", juggerNautrequest)).thenReturn("Valid");
		
		jnBugzillaBugAnalysisService.executeImpl();
		jnBugzillaBugAnalysisService.mapInput();
		jnBugzillaBugAnalysisService.mapOutput();
		jnBugzillaBugAnalysisService.validate();
		assertEquals(jnBugzillaBugAnalysisService.get(), "Triggered");		
	}
	
	@Test
	public void testExecuteImplInvalidReq(){
		String bugId = "123456";
		jnBugzillaBugAnalysisService.accept(bugId);
		
		JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
		juggerNautrequest.setItemID(bugId);
		juggerNautrequest.setItemType("2");
		juggerNautrequest.setCobrandID("");
		juggerNautrequest.setCreateBug(false);
		
		when(juggernautGateway.getSecurityToken()).thenReturn("DummyToken");
		when(juggernautGateway.getAnalysisRequestId("DummyToken", juggerNautrequest)).thenReturn("Not Valid");
		
		jnBugzillaBugAnalysisService.executeImpl();
		assertEquals(jnBugzillaBugAnalysisService.get(), "Invalid Request");		
	}
	
	@Test
	public void testExecuteImplUpdateExistingItem(){ 
		String bugId = "123456";
		jnBugzillaBugAnalysisService.accept(bugId);
		JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
		juggerNautrequest.setItemID(bugId);
		juggerNautrequest.setItemType("2");
		juggerNautrequest.setCobrandID("");
		juggerNautrequest.setCreateBug(false);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("bugId").is(bugId));
		JNAnalysisTriggeredItem jnAnalysisTriggeredItem = new JNAnalysisTriggeredItem();
		jnAnalysisTriggeredItem.setAnalysisRequestId("abc123xyz"); 
		
		when(juggernautGateway.getSecurityToken()).thenReturn("DummyToken");
		when(juggernautGateway.getAnalysisRequestId("DummyToken", juggerNautrequest)).thenReturn("Valid");
		when(mongoOps.findOne(query, JNAnalysisTriggeredItem.class)).thenReturn(jnAnalysisTriggeredItem);
		
		jnBugzillaBugAnalysisService.executeImpl();
		assertEquals(jnBugzillaBugAnalysisService.get(), "Triggered");		
	}	

}
