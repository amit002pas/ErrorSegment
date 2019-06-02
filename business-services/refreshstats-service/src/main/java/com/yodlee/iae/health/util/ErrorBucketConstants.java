/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.util;

public interface ErrorBucketConstants {

	
	final static String ERROR_TYPE_INFRA = "infra";
	final static String ERROR_TYPE_APP = "app";
	final static String ERROR_TYPE_IAE = "iae";
	final static double THRESHOLD_SIMILARITY = 0.97;
	final static String CACHE_ITEM="4";
	final static String MEM_SITE_ACC_ID="5";
	final static String MSA_FAILURE="-1";
	
	
	int INITIAL_RETRY_COUNT=0;
	int ITEM_STATUS_SUCCESS = 0;
	int ERROR_TYPE_INTERMITTENT = 1;
	int ERROR_TYPE_GENUINE = 2;
	int ERROR_TYPE_INTERMITTENT_FM_ELIGIBLE = 3;
	int QUERY_CENTER_INITIAL = -2;
	int ALREADY_SUCCESS = -1;
	int INTERMITTENT_THRESHOLD = 15 ;
	
	String SERVERSTATS_HEADER_ERRORCODE = "ERROR_CODE";
	String SERVERSTATS_HEADER_SCRIPT_VERSION = "SCRIPT_VERSION";

	String HEADER_ACCEPT = "Accept";
	String HEADER_CONTENT_TYPE = "Content-type";
	String HEADER_AUTHORIZATION = "Authorization";
	String HEADER_JSON_VALUE = "application/json";

	String AUTHENTICATION_KEY = "authentication";
	String BEARER = "Bearer ";
	String Authentication_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";

	String QueryCenter_URL = "https://firemem.tools.yodlee.com/hammer/R/QC/EQ";
	String Firemem_Trigger_URL = "https://firemem.tools.yodlee.com/hammer/R/F/NR";
	String Firemem_Status_URL = "https://firemem.tools.yodlee.com/hammer/R/F/RS";

}
