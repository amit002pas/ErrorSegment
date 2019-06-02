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

import lombok.Data;

@Document(collection="forecastaudit")
public @Data class ForecastAudit {
	String type=PersistenceConstants.FORECAST_AUDIT_TYPE;
	int totalCurrentRefreshCount;
	String agentName;
	Date time;
	boolean isYuvaWorking;
	List<ForecastAttribute> forecastAttributesList;
	String impact;
	long yuvaTimeMs;
}
