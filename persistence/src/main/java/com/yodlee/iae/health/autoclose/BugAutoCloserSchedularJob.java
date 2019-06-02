/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.autoclose;

import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author vchhetri and mkumar10
 *
 */
@Named
@Document(collection="BugAutoCloserSchedularJob")
public @Data class BugAutoCloserSchedularJob {
	@Id
	private String syntheticBugId;
	private int errorCode;
	private int retryCount;
	private JobStatus status;	
	private List<AnalysisDetails> analysisDetails;	
	private String agentName;
	private Date nextScheduledTime;
	private Date createdDate;
	private Date lastUpdDate;	
}
