package com.yodlee.health.errorsegment.resources.jn.bugreanalysis;

import java.util.Date;

import javax.inject.Named;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author vchhetri
 *
 */
@Document(collection = "JNChironBugTracker")
@Named
public @Data class JNChironBugTracker {
	private String orphicsyntheticBugid;
	private Integer juggernautbugZillaBugId;
    private String responseMessage;
	private Date createdDate;
	private Date sentToJNDate;

}