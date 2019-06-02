/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="forecastbucket")
public @Data class BugRepoAttribute {

	String type=PersistenceConstants.BUG_DETAILS_TYPE;
	String bugId;
	int stackTraceHashCode;
	String agentName;
	String errorCode;
	long Impact;
	String timeStamp;
}
