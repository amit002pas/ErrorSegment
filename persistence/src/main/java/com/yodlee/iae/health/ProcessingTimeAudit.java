/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection=PersistanceConstants.COLLECTION_TIME_TRACKER)
public @Data class ProcessingTimeAudit {

	private String applicationStartDateTime;
	private long splunkTimeMs;
	private long bucketizerTimeMs;
	private long forecastTimeMs;
	private long projectCompletionTimeMs;
	private String applicationEndDateTime;
}

