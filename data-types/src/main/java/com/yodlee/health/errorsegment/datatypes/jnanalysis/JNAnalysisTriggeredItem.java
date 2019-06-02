package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/***
 * 
 * @author Sanyam Jain
 *
 */
@Document(collection="JNAnalysisTriggerData")
public @Data class JNAnalysisTriggeredItem {
	
	private String bugId;
	private String status;
	private String bugSummary;
	private String analysisRequestId;
	private String securityToken;
	private String component;
	private String product;
	private String version;
	private String requestSource;
	private Date date;
	
}
