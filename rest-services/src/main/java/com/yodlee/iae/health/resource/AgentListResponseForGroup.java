package com.yodlee.iae.health.resource;

import lombok.Data;

import com.yodlee.iae.health.datatypes.uigroup.AgentListForGroup;

public @Data class AgentListResponseForGroup {
	
	String status;
	String message;
	AgentListForGroup agentListForGroup;

}
