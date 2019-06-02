/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public @Data class JNAnalysisRequest {
	
	private String bugID;
	@JsonProperty
	private boolean isSyntheticBug;

}