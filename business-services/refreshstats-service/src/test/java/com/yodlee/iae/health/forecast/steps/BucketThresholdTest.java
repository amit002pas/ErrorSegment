/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;

@RunWith(SpringJUnit4ClassRunner.class)
public class BucketThresholdTest {

	@InjectMocks
	BucketThreshold bucketThreshold;
	
	List<CacheItem> itemList = new ArrayList<>();
	
	@Before
	public void setup() {
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
		itemList.add(cacheItem);
		itemList.add(cacheItem1);
		itemList.add(cacheItem2);
		
		
		
		ReflectionTestUtils.setField(bucketThreshold, "thresholdCoefficient", 0.135f);
		ReflectionTestUtils.setField(bucketThreshold, "uarErrorCodes", "402,407,429,428,414");
		ReflectionTestUtils.setField(bucketThreshold, "infraErrorCodes", "404");
		ReflectionTestUtils.setField(bucketThreshold, "uarThresholdPercentage", 70.0f);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("403");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		
		Integer currentRefreshCount = 1000;
		
		
		
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
	
	@Test
	public void test1(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("402");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		
		Integer currentRefreshCount = 1000;
		
		
		
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
	
	@Test
	public void test11(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("402");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		
		Integer currentRefreshCount = 3;
		
		
		
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
	
	@Test
	public void test2(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("404");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		
		Integer currentRefreshCount = 1000;
		
		
		
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
	@Test
	public void test22(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("404");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		
		Integer currentRefreshCount = 3;
		
		
		
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
	@Test
	public void testElse(){
		
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		List<CacheItem> itemList1 = new ArrayList<>();
		segmentedBucket.setItemList(itemList1);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("error");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		Integer currentRefreshCount = 3;
		bucketThreshold.setInput(segmentedBucket, currentRefreshCount);
		bucketThreshold.isAboveThreshold();
		bucketThreshold.getCurrentThresholdCoefficient();
		bucketThreshold.execute();
		bucketThreshold.getOutput();
		
	}
}
