package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import lombok.Data;

public @Data class SiteStatsData {

	private String total_request;
	private String success;
	private String succ_perc;
	private String error_code;

	private String error_count;
	private String error_count_perc;
	private String agent_errors;
	private String agent_perc;

	private String site_errors;
	private String site_perc;
	private String uar_errors;
	private String uar_perc;

	private String infra_error;
	private String infra_perc;
	
}
