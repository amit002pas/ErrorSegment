/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.persistance.group;

import java.util.List;

import lombok.Data;

public @Data class BaseGroup {
	String groupId;
	String groupName;
	List<Integer> agentIds;
	
}
