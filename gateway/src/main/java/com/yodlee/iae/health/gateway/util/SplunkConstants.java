/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.util;



	public class SplunkConstants {
	       
	       public static final String USERNAME_CONSTANT="username";
	       public static final String PASSWORD_CONSTANT="password";
	       
	       
	      // /opt/splunk/etc/apps/yodlee_iae/lookups/sdbcab02_holding_data.csv
	       public static final String REST_URL = "https://splunkapi.yodlee.com/";
	       public static final String SAVED_SEARCH_URL = "servicesNS/admin/search/saved/searches/";
	       public static final String DISPATCH_URL = "/dispatch";
	       public static final String SEARCH_URL = "services/search/jobs?output_mode=json";
	       public static final String PRE_SID_URL = "/services/search/jobs/";
	       public static final String AUTH_URL = "services/auth/login";
	       public static final String PRE_SID_URL1 = "/search/inputlookup/sdbcab02_holding_data.csv/";
	       public static final String SEARCH_KEYWORD = "search";
	       public static final String AUTHORIZATION_KEYWORD = "Authorization";
	       public static final String SPLUNK_KEYWORD = "Splunk";
	       public static final String RESULT_KEYWORD = "results";
	       
	       public static final String POST_SID_URL = "/results?output_mode=json&offset="; 

	}



