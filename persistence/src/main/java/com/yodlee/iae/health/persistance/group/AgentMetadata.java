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
import com.yodlee.iae.health.datatypes.kafkagroup.AgentProcessor;
import com.yodlee.iae.health.datatypes.kafkagroup.AgentType;
import com.yodlee.iae.health.datatypes.kafkagroup.Region;

import lombok.Data;

@Document(collection = PersistanceConstants.COLLECTION_AGENT_METADATA)
public @Data class AgentMetadata {

	int _id;
	String agentName;
	AgentType agentType;
	List<AgentProcessor> agentProcessor;
	List<Region> region;

}
