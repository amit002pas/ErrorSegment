/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.errorbucket.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.errorbucket.IErrorBucketService;
import com.yodlee.iae.health.errorbucket.steps.IssueAggregatorStep;
import com.yodlee.iae.health.errorbucket.steps.IssueAnalyzerStep;
import com.yodlee.iae.health.errorsegment.ErrorStatsAudit;
import com.yodlee.iae.health.forecast.BugTracker;
import com.yodlee.iae.health.forecast.steps.ForecasterStep;
import com.yodlee.iae.health.gateway.splunk.SplunkServiceImpl;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ErrorBucketServiceImplTest {

	@InjectMocks
	ErrorBucketServiceImpl errorBucketServiceImpl;
	@Mock
	private SplunkServiceImpl splunkServiceImpl;
	@Mock
	private ProcessingTimeAuditRepository timeTrackerRepo;
	@Mock
	private IErrorBucketService errorBucketServiceObj;
	@Mock
	private IssueAggregatorStep issueAggregator;
	@Mock
	private IssueAnalyzerStep issueAnalyzerStep;
	@Mock
	private ForecastRepository forecastRepository;
	@Mock
	ForecasterStep forecasterStep;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testBugImpactUpdation(){
		List<BugTracker> bug= null;
		Mockito.when(forecastRepository.getListOfAnalyserID()).thenReturn(bug);
		errorBucketServiceImpl.bugImpactUpdation();
	}
	@Test
	public void testForecastErrorBucket(){

		Map<String, List<Bucket>> agentDataMap = new HashMap<>();
		List<Bucket> value = new ArrayList<Bucket>();
		Bucket bucket = new Bucket();
		value.add(bucket);
		agentDataMap.put("Chase", value);
		errorBucketServiceImpl.forecastErrorBucket(agentDataMap);
		Map<String, List<Bucket>> agentDataMap1 = new HashMap<>();
		List<Bucket> value1 = new ArrayList<Bucket>();
		agentDataMap1.put("Chase", value1);
		errorBucketServiceImpl.forecastErrorBucket(agentDataMap1);
	}
	
	@Test
	public void testGetHealth() throws Exception{

		List<String> agentList = new ArrayList<>();
		String groupName ="g1";
		
		List<ItemResponseSplunk> item = new ArrayList<>();
		Mockito.when(errorBucketServiceObj.getErrorBucketDataFromSplunk(agentList,groupName)).thenReturn(item);
		Object segmentizeBucket  =  new Object();
		Mockito.when(issueAggregator.process(item)).thenReturn(segmentizeBucket);
		
		errorBucketServiceImpl.getHealth(agentList, groupName);
		
	}
	
	@Test
	public void testGetErrorBucketDataFromSplunk() throws Exception{
		List<String> agentList = new ArrayList<>();
		agentList.add("wellsfargo");
		agentList.add("Cahse");
		String groupName ="g1";
		ErrorStatsAudit auditTracker = new ErrorStatsAudit();
		auditTracker.setEndTime(new Date());
		Mockito.when(timeTrackerRepo.getInstanceLastAuditData(groupName, ErrorStatsAudit.class)).thenReturn(auditTracker);
		List<ItemResponseSplunk> refreshStats = new ArrayList<>();
		String listOfAgents = String.join(",", agentList);

		Mockito.when(splunkServiceImpl.getCIIAndMSAStatsForAgents(listOfAgents, new Date(), new Date())).thenReturn(refreshStats);
		errorBucketServiceImpl.getErrorBucketDataFromSplunk(agentList, groupName);
	}
	
	@Test
	public void testGetErrorBucketDataFromSplunkNul() throws Exception {
		List<String> agentList = new ArrayList<>();
		agentList.add("wellsfargo");
		agentList.add("Cahse");
		String groupName ="g1";
		ErrorStatsAudit auditTracker = new ErrorStatsAudit();
		auditTracker.setEndTime(new Date());
		Mockito.when(timeTrackerRepo.getInstanceLastAuditData(groupName, ErrorStatsAudit.class)).thenReturn(auditTracker);
		List<ItemResponseSplunk> refreshStats = null;
		String listOfAgents = String.join(",", agentList);

		Mockito.when(splunkServiceImpl.getCIIAndMSAStatsForAgents(listOfAgents, new Date(), new Date())).thenReturn(refreshStats);
		errorBucketServiceImpl.getErrorBucketDataFromSplunk(agentList, groupName);
	}
}
