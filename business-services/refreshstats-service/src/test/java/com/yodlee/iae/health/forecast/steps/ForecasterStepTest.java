/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.gateway.yuva.IYUVAGateway;

@RunWith(SpringJUnit4ClassRunner.class)
public class ForecasterStepTest {

	@InjectMocks
	ForecasterStep forecasterStep;
	
	@Mock
	IYUVAGateway iyuvaGatewayObj;

	@Mock
	SegmentCategoriserStep segmentCategoriserStep;

	@Mock
	PredictionCalculatorStep predictionCalculatorStep;

	@Mock
	BugCreatorStep bugCreatorStep;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() throws Exception{
		List<Bucket> errorBuckets = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Chase");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("Genuine");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("5");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		errorBuckets.add(bucket);
		List<Segment> yuvaSegmentCiiList = null;
		Mockito.when(iyuvaGatewayObj.getYUVASegmentForAgent("Chase")).thenReturn(yuvaSegmentCiiList);
		
		forecasterStep.process(errorBuckets);
		
		Mockito.when(iyuvaGatewayObj.getYUVASegmentForAgent("Chase")).thenThrow(new Exception());
		
		forecasterStep.process(errorBuckets);
		
	}
}
