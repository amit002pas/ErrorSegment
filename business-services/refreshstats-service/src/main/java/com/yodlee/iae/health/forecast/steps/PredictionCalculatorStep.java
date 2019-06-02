/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.yodlee.health.errorsegment.datatypes.forecast.IntermediateResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.errorbucket.ServiceIO;

@Named
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PredictionCalculatorStep extends ServiceIO{
	
	
	private IntermediateResponse intermediateResponse;

	@Override
	public void accept(Object t) {
		intermediateResponse=new IntermediateResponse();
		intermediateResponse=(IntermediateResponse) t;
		
	}

	@Override
	public Object get() {
		return this.intermediateResponse;
	}

	@Override
	public void executeImpl() {
		
		
		List<SegmentedBucket> predictedBucketList=new ArrayList<SegmentedBucket>();
		for(SegmentedBucket segBucket:intermediateResponse.getSegmentedBucketList()){
			Double predictionCount = 0.0;
			Map<String,Integer> segmentWisePredictionMap=new HashMap<String,Integer>();
			for (Map.Entry<String,List<CacheItem>> entry : segBucket.getSegmentListImpacted().entrySet()) {
				String segID=entry.getKey();
				double numerator=entry.getValue().size();
				double denomerator=intermediateResponse.getHealthSegmentCiiCountMap().get(segID);
				double factor=intermediateResponse.getYuvaSegmentCiiCountMap().get(segID);
				predictionCount=predictionCount+((numerator/denomerator)*factor);
				segmentWisePredictionMap.put(segID,(int)((numerator/denomerator)*factor));
			}
			segBucket.setPredictedFailure(predictionCount.intValue());
			segBucket.setSegmentWisePrediction(segmentWisePredictionMap);
			predictedBucketList.add(segBucket);
		}
		intermediateResponse.setSegmentedBucketList(predictedBucketList);
		
	}

	@Override
	public void mapInput() {
		
	}

	@Override
	public void mapOutput() {
		
	}

	@Override
	public void validate() {
		
	}

}
