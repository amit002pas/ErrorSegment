/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.persistance.group;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.iae.health.PersistanceConstants;
@Document(collection=PersistanceConstants.COLLECTION_AGENT_GROUP)
public class AgentGroup extends BaseGroup{
	
}
