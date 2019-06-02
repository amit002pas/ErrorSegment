/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast;

import java.util.List;

import lombok.Data;

public @Data class ForecastAttribute {

	int segmentedBucketId;
	int currentFailure;
	int predictedFailure;
	boolean isAboveThreshold;
	float thresholdCoefficient;
	boolean isBugFilingRequired;
	String bugID;
	float localeCoefficient;
	float volumeCoefficent;
	float failurePercentageCoefficient;
	List<Integer>segmentsImpacted;
}
