/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="bugtracker")
public @Data class BugTracker {
	
	String analyzerId;
	String stackTrace;
	int countOfUsers;
	String agentName;
	Date creationDate;
	int statusCounter;
	

}
