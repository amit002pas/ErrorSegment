/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.IntermediateResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.gateway.yuva.IYUVAGateway;
import com.yodlee.iae.health.util.ForecastConstants;

@Named
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ForecasterStep extends ServiceIO {

	@Inject
	IYUVAGateway iyuvaGatewayObj;

	@Inject
	SegmentCategoriserStep segmentCategoriserStep;

	@Inject
	PredictionCalculatorStep predictionCalculatorStep;

	@Inject
	BugCreatorStep bugCreatorStep;

	private List<Bucket> agentBucketData = null;
	private long yuvaFetchStartTime;
	private long yuvaFetchEndTime;

	@Override
	public void accept(Object input) {
		agentBucketData = (List<Bucket>) input;

	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void executeImpl() {

		IntermediateResponse intermediateResponse = new IntermediateResponse();
		boolean isYuvaWorking = true;
		yuvaFetchStartTime = System.currentTimeMillis();

		if (null != agentBucketData && !agentBucketData.isEmpty()) {
			String agentName = agentBucketData.get(0).getAgentName();
			List<Segment> yuvaSegmentCiiList = null;
			try {
				yuvaSegmentCiiList = iyuvaGatewayObj.getYUVASegmentForAgent(agentName);
			} catch (Exception e) {
				isYuvaWorking = false;
			}
			Map<String, Object> inputData = new HashMap<>();
			inputData.put(ForecastConstants.ERROR_BUCKET_CONSTANT, agentBucketData);
			if(isYuvaWorking) {
			inputData.put(ForecastConstants.YUVA_SEGMENT_CONSTANT, yuvaSegmentCiiList);

			intermediateResponse = (IntermediateResponse) segmentCategoriserStep.process(inputData);
			intermediateResponse = (IntermediateResponse) predictionCalculatorStep.process(intermediateResponse);
			}
			inputData.put(ForecastConstants.INTERMEDIATE, intermediateResponse);
			inputData.put(ForecastConstants.ISYUVAWORKING, isYuvaWorking);
			bugCreatorStep.process(inputData);
			yuvaFetchEndTime = System.currentTimeMillis();
		}
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
