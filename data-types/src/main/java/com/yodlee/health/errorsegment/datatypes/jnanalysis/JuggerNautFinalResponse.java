/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import java.util.List;

import lombok.Data;


public @Data  class JuggerNautFinalResponse {
	
	private Integer itemId;
	private Long cobrandId;
	private String itemType;
	private String productionStatus;
	private String status;
	private String pdfURI;
	private AdditionalDetail additionalDetail;
	private Outcome outcome;
	private List<AnalysisDetails> analysisDetails;

	
} 