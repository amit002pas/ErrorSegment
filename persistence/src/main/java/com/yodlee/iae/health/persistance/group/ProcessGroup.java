/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.persistance.group;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.iae.health.PersistanceConstants;

import lombok.Data;

@Document(collection=PersistanceConstants.COLLECTION_PROCESS_GROUP)
public @Data class ProcessGroup {

	int _id;
	private String groupName;
	private List<Integer> agentIds;
	
}
