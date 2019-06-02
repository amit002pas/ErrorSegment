package com.yodlee.iae.health.refresh;

import lombok.Data;

public @Data class RefreshStats {
	
	private String _id; //agentName
	private int totalRefresh;
	private int success;
	private int failure;
	private int uarcount;
	private int sitecount;
	private int agentcount;
	private int infracount;


}
