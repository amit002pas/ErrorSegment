/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.splunk;

import java.util.Date;
import java.util.List;

import com.yodlee.iae.health.datatypes.refresh.AgentStats;
import com.yodlee.iae.health.gateway.exception.ParsingException;

public interface ISplunkService {
	/**
	 * 
	 * Returns the refresh statistics for the given agents in mentioned timeline.
	 * 
	 * @param agentNames
	 *            List of Agents for which data need to be fetched
	 * @param startTime
	 *            Start Time
	 * @param endTime
	 *            End Time
	 * @return List of Statistics for each agent from the Splunk
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public List<AgentStats> getRefreshStatsForAgents(String agentNames, Date startTime, Date endTime)
			throws ParsingException,Exception;

	// public List<ItemResponseSplunk> getSplunkRawData(String agentName,String
	// startTime,String endTime );

}
