/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.splunk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.iae.health.datatypes.refresh.AgentStats;
import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.gateway.exception.ParsingException;
import com.yodlee.iae.health.gateway.util.SplunkQueryReader;

/**
 * This class get the splunk data for the given time range and query provided
 * 
 * @author akumar23
 *
 */

@Named
public class SplunkServiceImpl implements ISplunkService {

	@Inject
	ISplunkConnect splunkConnectObj;

	@Inject
	private SplunkQueryReader splunkQueryReader;

	@Override
	public List<AgentStats> getRefreshStatsForAgents(String agentNames, Date startTime, Date endTime)
			throws ParsingException, Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.FORMATTER);
		String[] args = { agentNames, sdf.format(startTime), sdf.format(endTime) };
		String queryString = splunkQueryReader.getPropertyByKey(GatewayConstants.QUERY_REFRESH_STATS, args);
		String result = splunkConnectObj.executeSplunkServices(queryString);
		Gson gson = new Gson();
		AgentStats[] agentStats = null;
		try {
			agentStats = gson.fromJson(result, AgentStats[].class);
		} catch (JsonSyntaxException ex) {
			throw new ParsingException(ex.getMessage());
		}
		List<AgentStats> agentStatsList = Stream.of(agentStats).collect(Collectors.toList());
		return agentStatsList;
	}

	public List<ItemResponseSplunk> getCIIAndMSAStatsForAgents(String agentNames, Date startTime, Date endTime)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.FORMATTER);
		String[] args = { agentNames, sdf.format(startTime), sdf.format(endTime) };
		String queryString = splunkQueryReader.getPropertyByKey(GatewayConstants.QUERY_RAW_STATS, args);
		String result = splunkConnectObj.executeSplunkServices(queryString);
		Gson gson = new Gson();
		ItemResponseSplunk[] agentStats = gson.fromJson(result, ItemResponseSplunk[].class);

		List<ItemResponseSplunk> agentStatsList = Stream.of(agentStats).collect(Collectors.toList());
		return agentStatsList;

	}

}
