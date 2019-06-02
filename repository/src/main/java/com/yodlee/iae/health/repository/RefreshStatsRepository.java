/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.yodlee.iae.health.refresh.RefreshStats;
import com.yodlee.iae.health.repository.exception.MyException;

/***
 * This class returns the refresh stats for the given time interval from DB
 * 
 * @author akumar23
 *
 */
@Named
public class RefreshStatsRepository {

	@Inject
	private MongoOperations mongoOps;

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param agentList
	 * @return
	 */
	public List<RefreshStats> findRefreshStatsByTimeandAgents(Date startTime, Date endTime, List<String> agentList)
			throws NullPointerException, MyException {

		MatchOperation match = null;
		
		UnwindOperation unwind = Aggregation.unwind("agentStatsList");
		if(!agentList.isEmpty())
		 match = Aggregation.match(Criteria.where("agentStatsList.agentName").in(agentList)
				.and("startTime").gte(new Date(startTime.getTime())).lte(new Date(endTime.getTime())));

		else {
			match = Aggregation.match(Criteria.where("startTime").gte(new Date(startTime.getTime())).lte(new Date(endTime.getTime())));
		}
		
		GroupOperation group = Aggregation.group("agentStatsList.agentName").sum("agentStatsList.success").as("success")
				.sum("agentStatsList.failure").as("failure").sum("agentStatsList.totalRefresh").as("totalRefresh")
				.sum("agentStatsList.uarcount").as("uarcount").sum("agentStatsList.infracount").as("infracount")
				.sum("agentStatsList.sitecount").as("sitecount").sum("agentStatsList.agentcount").as("agentcount");
		Aggregation aggregation = Aggregation.newAggregation(unwind, match, group).withOptions(new AggregationOptions(true, false, null));

		AggregationResults<RefreshStats> resultSet = mongoOps.aggregate(aggregation, "refreshstats",
				RefreshStats.class);
		if (null != resultSet.getMappedResults() && !resultSet.getMappedResults().isEmpty())
			return resultSet.getMappedResults();
		else if (null == resultSet.getMappedResults())
			// return new ArrayList<RefreshStats>();
			throw new NullPointerException("Error while getting the data");
		else if (resultSet.getMappedResults().isEmpty())
			throw new MyException("No Data present for the given interval");
		else
			return resultSet.getMappedResults();
	}

}
