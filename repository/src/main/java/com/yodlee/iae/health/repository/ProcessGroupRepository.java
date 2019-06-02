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
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.yodlee.iae.health.persistance.group.ProcessGroup;

import lombok.Data;

/***
 * This class will process the group that is required for kafka to work
 * @author akumar23
 *
 */
@Named
public class ProcessGroupRepository {

	@Inject
	private MongoOperations mongoOps;
	@Inject
	private AgentMetadataRepository agentMetadataRepository;

	
	/**
	 * 
	 * @return the list of groups stored in DB
	 */
	public List<String> getAllGroupNames() {
		ProjectionOperation operation = Aggregation.project(RepositoryConstants.GROUP_NAME).andExclude(RepositoryConstants.ID);
		Aggregation aggregation = Aggregation.newAggregation(operation);
		List<String> groupNames = new ArrayList<>();
		AggregationResults<Result> aggregationResults = mongoOps.aggregate(aggregation, ProcessGroup.class,
				Result.class);

		aggregationResults.getMappedResults().forEach(s -> groupNames.add(s.getGroupName()));
		return groupNames;
	}

	public List<Integer> getAgentIdsForGroup(String groupName) {
		Query query = new Query(Criteria.where(RepositoryConstants.GROUP_NAME).is(groupName));
		ProcessGroup group = mongoOps.findOne(query, ProcessGroup.class);
		return group.getAgentIds();
	}
	
	public List<String> searchAgentListByGroup(String groupName){
		
		return agentMetadataRepository.getAgentListWithId(getAgentIdsForGroup(groupName));
		
		
	}
	

	@Data
	class Result {
		private String groupName;
	}

}
