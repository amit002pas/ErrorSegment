/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import lombok.Data;

public @Data class RequestBugUpdate {

	private String id;
	private String product;
	private String component;
	private String summary;
	private String version;
	private String action="update";
	private String cf_comments_release_notes;
	private String comment;
	private String jnAnalysisId;	
}
