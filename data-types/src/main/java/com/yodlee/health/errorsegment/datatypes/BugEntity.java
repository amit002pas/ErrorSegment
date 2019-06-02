
package com.yodlee.health.errorsegment.datatypes;

import java.util.List;

import lombok.Data;

public @Data class BugEntity {


	private String version;
	private String operatingSystem;
	private String resolution;
	private String status;
	private String summary;
	private String product;
	private String component;
	private String alias;
	private BugResponseMap parameterMap;
	private String platform;
	private List<Boolean> flags;
	private String severity;
	private String priority;
	private String id;

} 
