/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.yodlee.iae.health.persistance.group.AgentMetadata;

import lombok.Data;

/**
 * This class will give agent info for the Ids provided
 * @author akumar23
 *
 */
@Named
public class AgentMetadataRepository {

	@Inject
	MongoOperations mongoOps;

	/**
	 * 
	 * @param ids  list of agent Ids
	 * @return     list of agent name
	 */

	public List<String> getAgentListWithId(List<Integer> ids) {
		MatchOperation match = Aggregation.match(Criteria.where(RepositoryConstants.ID).in(ids));
		ProjectionOperation project = Aggregation.project(RepositoryConstants.AGENT_NAME).andExclude(RepositoryConstants.ID);
		Aggregation aggregation = Aggregation.newAggregation(match, project);
		AggregationResults<AgentName> agents = mongoOps.aggregate(aggregation, AgentMetadata.class, AgentName.class);
		List<String> agentNames = new ArrayList<>();
		agents.getMappedResults().forEach(s -> agentNames.add(s.getAgentName()));
		return agentNames;
	}


	@Data
	class AgentName {
		private String agentName;
	}

}
