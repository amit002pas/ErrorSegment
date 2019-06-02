/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.repository.forecast;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//import com.yodlee.health.errorsegment.datatypes.forecast.CacheItem;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.datatypes.forecast.TopFailure;
import com.yodlee.iae.health.forecast.BugFailure;
import com.yodlee.iae.health.forecast.BugRepoAttribute;
import com.yodlee.iae.health.forecast.BugTracker;
import com.yodlee.iae.health.forecast.ForecastAudit;
import com.yodlee.iae.health.forecast.ForecastedBucket;
import com.yodlee.iae.health.forecast.PersistenceConstants;


@RunWith(SpringJUnit4ClassRunner.class)
public class ForecastRepositoryTest {
	
	@InjectMocks
	private ForecastRepository forecastRepository;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testForecastSegmentRepositoryTest() {
		
		ForecastedBucket forecastBucket = new ForecastedBucket();
		forecastBucket.setAgentName("USAA");
		forecastBucket.setTime(new Date());
		forecastBucket.setType("forecast");
		
		ForecastedBucket forecastBucket1 = new ForecastedBucket();
		forecastBucket1.setAgentName("ABC");
		forecastBucket1.setTime(new Date(-1));
		
		ForecastedBucket forecastBucket2 = new ForecastedBucket();
		forecastBucket2.setAgentName("XYZ");
		forecastBucket2.setTime(new Date());
		
		ForecastedBucket forecastBucket3 = new ForecastedBucket();
		forecastBucket3.setAgentName("PQR");
		forecastBucket3.setTime(new Date());
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		segmentedBucket.setAgentName("USSA");
		segmentedBucket.setBucketId(1);
		segmentedBucket.setErrorCode("0");
		segmentedBucket.setErrorGroup("IAE");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(5);
		segmentedBucket.setStacktrace("");
		
		Map<Integer,Integer> segmentListImpacted = new HashMap<Integer,Integer>();
		segmentListImpacted.put(1,10);
		segmentListImpacted.put(2,10);
		segmentListImpacted.put(3,10);
		segmentListImpacted.put(4,10);
//		
//		CacheItem cacheItem = new CacheItem();
//		List<CacheItem> itemList = new ArrayList<CacheItem>();
//		cacheItem.setCacheItemId("100000");
//		cacheItem.setCobrandId("200000");
//		cacheItem.setDbId("sdbace01");
//		itemList.add(cacheItem);
		
		List<SegmentedBucket> segmentedBucketList = new ArrayList<SegmentedBucket>(); 
		segmentedBucketList.add(segmentedBucket);
		forecastBucket.setSegmentedBucketList(segmentedBucketList);
		
		List<ForecastedBucket> forecastBucketList=new ArrayList<ForecastedBucket>();
		forecastBucketList.add(forecastBucket);
		forecastBucketList.add(forecastBucket1);
		forecastBucketList.add(forecastBucket2);
		forecastBucketList.add(forecastBucket3);
		
		Query query = new Query().addCriteria(Criteria.where(PersistenceConstants.DOCUMENT_TYPE).
				is(PersistenceConstants.PATTERN_ANALYSIS_TYPE).and("agentName").is(forecastBucket.getAgentName()));		
		
		when(mongoOperations.find(query,ForecastedBucket.class)).thenReturn(forecastBucketList);
		
		forecastRepository.saveSegmentedBucketforPattenAnalysis(forecastBucket);
		assertNotNull(forecastRepository.toString());
		
		ForecastAudit forecastAudit = new ForecastAudit();
		forecastRepository.saveForecastAudit(forecastAudit );
		assertNotNull(forecastRepository.toString());
		
		BugRepoAttribute bugRepoAttribute = new BugRepoAttribute();
		forecastRepository.saveBugAttributes(bugRepoAttribute );
		assertNotNull(forecastRepository.toString());
	}
	
	@Test
	public void testForecastSegmentRepositoryTest_1() {
		ForecastedBucket forecastBucket = new ForecastedBucket();
		forecastRepository.saveSegmentedBucketforPattenAnalysis(forecastBucket);
	}
	
	@Test
	public void testForecastSegmentRepositoryTest_2() {
		ForecastedBucket forecastBucket = new ForecastedBucket();
		Query query = new Query().addCriteria(Criteria.where(PersistenceConstants.DOCUMENT_TYPE).
				is(PersistenceConstants.PATTERN_ANALYSIS_TYPE).and("agentName").is(forecastBucket.getAgentName()));		
		
		when(mongoOperations.find(query,ForecastedBucket.class)).thenReturn(null);
		forecastRepository.saveSegmentedBucketforPattenAnalysis(forecastBucket);
	}
	
	@Test
	public void testGetListOfAnalyserId(){
		forecastRepository.getListOfAnalyserID();
		BugTracker bugTracker= new BugTracker();
		SegmentedBucket seg=new SegmentedBucket();
		TopFailure topFailure=new TopFailure();
		topFailure.setCobrandId("10002812");
		topFailure.setCounter(12);
		seg.setTopFailure(topFailure);
		Date date= new Date();
		bugTracker.setAgentName("USAA");
		bugTracker.setAnalyzerId("abc");
		bugTracker.setCreationDate(date);
		bugTracker.setStackTrace("null pointer");
		bugTracker.setStatusCounter(2);
		forecastRepository.saveAnalyzerId(bugTracker,seg);
		forecastRepository.statusCounterUpdation(bugTracker);
		BugFailure bugFailure = new BugFailure();
		forecastRepository.saveFailureBug(bugFailure);
	}
}

