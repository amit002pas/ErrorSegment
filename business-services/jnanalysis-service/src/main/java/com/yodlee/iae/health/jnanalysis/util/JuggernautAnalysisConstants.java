/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 **/
package com.yodlee.iae.health.jnanalysis.util;

public interface JuggernautAnalysisConstants {
	public static final String BUG= "Bug";
	public static final String REQUEST_TYPE="RequestType";
	public static final String INVALID_REQUEST ="Invalid Request";
	public static final String TRIGGERED="Triggered";
	public static final String IN_PROGRESS ="In Progress";
	public static final String BATCH="Batch";
	public static final String SUCCESS="Success";
	public static final String FAILURE="Failure";
	
	public static final String BUG_ID="bugId";
	public static final String STATUS="status";
	public static final String SINGLE="Single";
	public static final String COBRAND_ID="COBRAND ID:";
	public static final String FOUR="4";
	public static final String FIVE="5";
	public static final String NOT_VALID="Not Valid";
	public static final String STARTED="Started";
	public static final String COMPLETED="COMPLETED";
    public static final String JN_PDF="JNAnalysis.pdf";
    public static final String JN_PDF_Message="JN analysis Url";
    
    public static final String CHIRON_SIMILAR_BUG_URL_DEV = "http://192.168.113.224:8080/jn/getJnSimilarBugs";
	public static final String CHIRON_SIMILAR_BUG_URL_PROD = "https://bugr.orphic.yodlee.com/jn/getJnSimilarClosedBugs";
	public static final String COMM_TO_JN_FOR_REANALYSIS = "Communicated to Juggernaut for re-analysis of the bug";

	
	
}
