
/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;

import lombok.Data;

@Document(collection="rtdaAudit")
public @Data class ForecastedBucket {
	
	String type=PersistenceConstants.FORECAST_AUDIT_TYPE;
	String agentName;
	Date time;
	List<SegmentedBucket> segmentedBucketList;
}
