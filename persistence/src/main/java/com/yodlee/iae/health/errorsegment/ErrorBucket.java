/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorsegment;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.health.errorsegment.datatypes.forecast.JuggernautDetails;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.PersistanceConstants;

import lombok.Data;

@Document(collection = PersistanceConstants.COLLECTION_ERROR_BUCKET)
public @Data class ErrorBucket {

	private int errorBucketId;
	private String errorCode;
	private String agentName;
	private String stacktrace;
	private String errorType;
	private String errorGroup;
	private boolean isMSAFailure;
	private List<CacheItem> cacheItemIds;
	private int countOfUsers;
	private Date startTime;
	private JuggernautDetails juggernautDetails;
	private Date endTime;
}
