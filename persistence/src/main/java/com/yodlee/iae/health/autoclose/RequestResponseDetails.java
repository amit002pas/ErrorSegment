/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.autoclose;

import java.util.Date;

import javax.inject.Named;

import lombok.Data;

/**
 * @author vchhetri and mkumar10
 *
 */
@Named
public @Data class RequestResponseDetails {
	private Date reqSentTime;
	private Date resRecTime;
	private String responseMessage;
	private String responseAction;
	private String status;
	private String analysisReportUrl;
	
}
