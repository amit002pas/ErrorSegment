/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;

import lombok.Data;

@Document(collection="tempbugfailure")
public @Data class BugFailure {
	
	String message;
	String error;
	SegmentedBucket segmentedBucket;
	int countOfTotalRefresh;
	Date createdDate;
}
