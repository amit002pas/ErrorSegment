package com.yodlee.health.errorsegment.resources.errorsegment;

import java.util.Date;

import lombok.Data;

public @Data class ErrorSegment {

	private String errorSegmentId ;
	private int errorCode ;
	private String agentName ;
	private String sumInfo;
	private String siteId;
	private String stacktrace ;
	private String errorType ;
	private String errorGroup;
	private String locale;
	private Date refreshTime;

	private CacheItem[] cacheItemIds ;
	
}
