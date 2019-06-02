package com.yodlee.iae.health.resource;

import java.util.List;

import lombok.Data;

public @Data class RefreshStatsRequest {

	private String startTime;
	private String endTime;
	private List<String> agentList;

}
