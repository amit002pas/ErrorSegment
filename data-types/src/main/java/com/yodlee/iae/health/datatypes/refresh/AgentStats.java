package com.yodlee.iae.health.datatypes.refresh;
import lombok.Data;

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