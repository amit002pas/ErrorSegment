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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author vchhetri and mkumar10
 *
 */
@Named
@Document(collection = "BugAutoCloserAudit")
public @Data class BugAutoCloserAudit{		
	@Id
	private String analysisId;
	private String syntheticBugId;
	private List<RequestResponseDetails> requestResponseDetails;	
	private Date nextPickTime;
	private Date reqSentTime;
	private JobStatus status;
	private boolean isClosedByChiron;
}
