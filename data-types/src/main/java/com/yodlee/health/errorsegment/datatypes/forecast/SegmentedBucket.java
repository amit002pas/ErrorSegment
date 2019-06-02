/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.forecast;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;

import lombok.Data;

public @Data class SegmentedBucket {

	int bucketId;
	String errorCode;
	String agentName;
	String stacktrace;
	String errorType;
	String errorGroup;
	String siteId;
	String sumInfo;
	String locale;
	Date startTime;
	Date endTime;
	boolean isMSAFailure;
	JuggernautDetails juggernautDetails;
	Map<String, List<CacheItem>> segmentListImpacted;
	int predictedFailure;
	List<CacheItem> itemList;
	private Map<String, Integer> segmentWisePrediction;
	private TopFailure topFailure;
	String route;
	public void setLocale(String locale2) {
		
	}

}
