/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.refresh;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.iae.health.PersistanceConstants;

import lombok.Data;


@Document(collection=PersistanceConstants.COLLECTION_REFRESH_STATS_AUDIT)
public @Data class RefreshStatsAudit {

	private Date startTime;
	private Date endTime;
	private Date createdAt;
	private String groupName;
	
}
