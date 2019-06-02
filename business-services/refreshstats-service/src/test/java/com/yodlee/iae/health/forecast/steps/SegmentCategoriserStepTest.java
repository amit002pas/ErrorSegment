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
import com.yodlee.health.errorsegment.datatypes.forecast.CacheItemDB;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.util.ForecastConstants;

@RunWith(SpringJUnit4ClassRunner.class)
public class SegmentCategoriserStepTest {

	@InjectMocks
	SegmentCategoriserStep segmentCategoriserStep;
	
	@Test
	public void test(){
		Map<String, Object> obj = new HashMap<>();
		
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
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
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
		List<Segment> segments = new ArrayList<>();
		Segment segment = new Segment();
		segment.setSegmentId("121212111");
		List<CacheItemDB> ciiDb = new ArrayList<>();
		CacheItemDB cacheItemDB = new CacheItemDB();
		cacheItemDB.setCacheItemId("12345");
		cacheItemDB.setCobrandId("11121212");
		cacheItemDB.setDataBase("2222");
		CacheItemDB cacheItemDB1 = new CacheItemDB();
		cacheItemDB1.setCacheItemId("12345");
		cacheItemDB1.setCobrandId("11121212");
		cacheItemDB1.setDataBase("2222");
		ciiDb.add(cacheItemDB);
		ciiDb.add(cacheItemDB1);
		segment.setCiiDb(ciiDb);
		segments.add(segment);
		obj.put(ForecastConstants.YUVA_SEGMENT_CONSTANT, segments);
		obj.put(ForecastConstants.ERROR_BUCKET_CONSTANT, errorBuckets);
		segmentCategoriserStep.process(obj);
	}
}
