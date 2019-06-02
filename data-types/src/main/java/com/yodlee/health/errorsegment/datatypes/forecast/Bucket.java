/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.forecast;

import java.util.Date;
import java.util.List;

import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;

import lombok.Data;

public @Data class Bucket {
	
	int errorSegmentId;
	String errorCode;
	String agentName;
    String stacktrace;
    String sumInfo;
    String siteId;
    String errorType;
    String errorGroup;
    String locale;
    Date startTime;
    Date endTime;
    JuggernautDetails juggernautDetails;
    boolean isMSAFailure;
	List<CacheItem> cacheItemIds;
	String route;
	
}

