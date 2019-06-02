/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */


package com.yodlee.iae.health.forecast.steps;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;


/**
 * @author srai
 * This is Threshold steps.
 * It calls threshold service for each buckets and checks with thresholdCoefficient which is the threshold value.
 */
@Named
public class BucketThreshold implements ForecastStepsBase<SegmentedBucket, Integer, Boolean> {

	@Value("${locale.uk.thresholdCoefficient}")
	private float thresholdCoefficient;

	@Value("${uar.error.codes}")
	private String uarErrorCodes;
	
	@Value("${infra.error.codes}")
	private String infraErrorCodes;

	@Value("${uar.threshold.percentage}")
	private float uarThresholdPercentage;

	/*@Inject
	private Threshold threshold;*/

	private float currentThresholdCoefficient;

	private boolean isAboveThreshold;

	private SegmentedBucket segmentedBucket;

	private int currentRefreshCount;

	Logger logger=LoggerFactory.getLogger(BucketThreshold.class);

	@Override
	public void setInput(SegmentedBucket segmentedBucket, Integer currentRefreshCount) {
		
		this.segmentedBucket=segmentedBucket;
		this.currentRefreshCount=currentRefreshCount;
		isAboveThreshold=false;
		currentThresholdCoefficient=0;
		
	}

	@Override
	public void execute() {
		logger.info("Inside threshold execute");
		List<String> uarErrorCodesList = Arrays.asList(uarErrorCodes.split(","));
		if(uarErrorCodesList.contains(segmentedBucket.getErrorCode())&&currentRefreshCount!=0) {
			logger.info("UAR error found with code: "+segmentedBucket.getErrorCode());
			float failurePercentage = ((float)segmentedBucket.getItemList().size()/(float)currentRefreshCount)*100;
			logger.info("Current failure for UAR is: "+failurePercentage);
			if(failurePercentage >= uarThresholdPercentage
					&& !(segmentedBucket.getAgentName().toLowerCase().contains("bill") && segmentedBucket.getErrorCode().equals("414"))) {	
				isAboveThreshold=true;
				logger.info("THRESHOLD BREACHED DUE TO HIGH UAR FAILURE RATE");
	}else
				isAboveThreshold=false;
		} else if(segmentedBucket.getErrorCode().contains(infraErrorCodes) && currentRefreshCount!=0){

			float failurePercentage = ((float)segmentedBucket.getItemList().size()/(float)currentRefreshCount)*100;
			if(failurePercentage>10.0){
				logger.info("THRESHOLD BREACHED DUE TO HIGH INFRA FAILURE RATE");
				isAboveThreshold=true;
			}
			
		}else if(segmentedBucket.getItemList().size()>=1 || segmentedBucket.getPredictedFailure()>1000){
			logger.info("Overriding threshold Algorith as current failure size is: "+segmentedBucket.getItemList().size());
			isAboveThreshold=true;
			logger.info("THRESHOLD BREACHED DUE TO HIGH PREDICTION/FAILURE COUNT");
		}else {
			logger.info("Last else: {}",segmentedBucket.getItemList().size());
			isAboveThreshold=false;
		}
	}
	@Override
	public Boolean getOutput() {
		return isAboveThreshold;
	}
	public boolean isAboveThreshold(){
		return isAboveThreshold;
	}
	public float getCurrentThresholdCoefficient(){
		return currentThresholdCoefficient;
	}

	
}
