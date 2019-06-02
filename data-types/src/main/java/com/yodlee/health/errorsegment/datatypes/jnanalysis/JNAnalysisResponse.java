/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import lombok.Data;

public @Data class JNAnalysisResponse {
	
	private String status;
	private JNAnalysisResponseEnum analysisResponse;
	private String jnAnalysisResponseId;

}
