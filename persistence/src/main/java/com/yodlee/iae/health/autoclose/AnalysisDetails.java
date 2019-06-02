/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.autoclose;

import java.util.Date;

import lombok.Data;

/**
 * @author vchhetri and mkumar10
 *
 */
public @Data class AnalysisDetails {
	private String analysisId;
	private int responseErrorCode;
	private String responseMessage;
	private Date analysisStartTime;
}
