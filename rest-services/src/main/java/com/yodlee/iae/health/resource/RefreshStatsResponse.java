package com.yodlee.iae.health.resource;

import java.util.List;

import com.yodlee.iae.health.refresh.RefreshStats;

import lombok.Data;

public @Data class RefreshStatsResponse {

	String status;
	String message;
	RefreshStats consolidatedRefreshStats;
	List<RefreshStats> refreshStats;
	// Date startDuration;
	// Date endDuration;

}
