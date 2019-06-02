package com.yodlee.health.errorsegment.datatypes;

import lombok.Data;

public @Data class BugCreateUpdateResponse {

	private String status;
	private String message;
	private String bugAnalyserId;
	private String error;
	private String bugID;
}
