package com.yodlee.iae.health.datatypes.refresh;
import lombok.Data;

/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

public @Data class AgentStats {
	
	private String agentName;
	private int totalRefresh;
	private int success;
	private int failure;
	private int uarcount;
	private int sitecount;
	private int agentcount;
	private int infracount;
	
	
}