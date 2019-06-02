package com.yodlee.health.errorsegment.datatypes;
import lombok.Data;

public @Data class SyntheticFields {

	private String portfolio;

	private Integer bugzillaBugId;
	private boolean isBugzillaBugCreated;
	private SyntheticBugStatus syntheticBugStatus;
	private String failureMessage;

	private String segmentId;
	private String action;

	private String cusip;
	private String symbol;

	private String apiKey;

}
