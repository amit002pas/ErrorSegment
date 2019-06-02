/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.schedular.util;

public interface SchedularConstants {
	String HEADER_ACCEPT = "Accept";
	String HEADER_CONTENT_TYPE = "Content-type";
	String HEADER_JSON_VALUE = "application/json";
	String healthURL="http://localhost:9006/health/agent/error-segment";
	String FORECAST_URL="http://localhost:9006/health/error-segment/forecast/";
	String INCORRECT_IMPACT_ANALYSIS="http://localhost:9006/health/stats/firememResponse";
	String AGENT_SEPERATOR_DELIMETER=",";
	String JN_ANALYSIS_URL="http://localhost:9006/jnanalysis/trigger/all";
	String AUTHOURIZATION="Authorization";
	String USERNAME="username";
	String PASSWORD="password";
	
	
	
}
