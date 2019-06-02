/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */


package com.yodlee.iae.health.datatypes.refresh;

import java.util.Date;

import lombok.Data;

public @Data class ItemResponseSplunk {

	private String agentName;
	private int errorType;
	private String cacheItemId;
	private String dbId;
	private String cobrandId;
	private String gathererIp;
	private String timeStamp;
	private String stackTrace;
	private String sumInfo;
	private String msaId;
	private String siteId;
	private String refreshType;
	private String locale;
	private Date refreshTime;
	private String route;
		
	
} 
 
