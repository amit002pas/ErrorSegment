/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.IntermediateResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.datatypes.forecast.TopFailure;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;

@RunWith(SpringJUnit4ClassRunner.class)
public class PredictionCalculatorStepTest {

	@InjectMocks
	PredictionCalculatorStep predictionCalculatorStep;
	
	@Test
	public void test(){
		IntermediateResponse intermediateResponse = new IntermediateResponse();
		List<CacheItem> itemList = new ArrayList<>();
		
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
		List<SegmentedBucket> segmentedBucketList = new ArrayList<>();
		SegmentedBucket segmentedBucket = new SegmentedBucket();
		itemList.add(cacheItem);
		itemList.add(cacheItem1);
		itemList.add(cacheItem2);
		
		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("403");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Genuine");
		segmentedBucket.setPredictedFailure(500);
		Map<String, Integer> segmentWisePrediction = new HashMap<>();
		segmentWisePrediction.put("10001100", 1010);
		segmentedBucket.setSegmentWisePrediction(segmentWisePrediction);
		TopFailure topFailure = new TopFailure();
		topFailure.setCacheItemId("121212");
		topFailure.setCobrandId("11111");
		segmentedBucket.setTopFailure(topFailure);
		Map<String, List<CacheItem>> segmentListImpacted = new HashMap<>();
		segmentListImpacted.put("10001100", itemList);
		segmentedBucket.setSegmentListImpacted(segmentListImpacted);
		
		segmentedBucketList.add(segmentedBucket);
		
		HashMap<String,Integer> yuvaSegmentCiiCountMap = new HashMap<>();
		yuvaSegmentCiiCountMap.put("10001100", 1);
		HashMap<String,Integer> healthSegmentCiiCountMap = new HashMap<>();
		healthSegmentCiiCountMap.put("10001100", 1);
		intermediateResponse.setSegmentedBucketList(segmentedBucketList);
		intermediateResponse.setHealthSegmentCiiCountMap(healthSegmentCiiCountMap);
		intermediateResponse.setYuvaSegmentCiiCountMap(yuvaSegmentCiiCountMap);
		predictionCalculatorStep.process(intermediateResponse);
		
	}
}
