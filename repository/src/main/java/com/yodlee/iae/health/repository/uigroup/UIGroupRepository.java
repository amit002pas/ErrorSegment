/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository.uigroup;

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

import com.yodlee.iae.health.datatypes.kafkagroup.GroupCategory;
import com.yodlee.iae.health.datatypes.uigroup.AgentListForGroup;
import com.yodlee.iae.health.datatypes.uigroup.GroupListResponse;
import com.yodlee.iae.health.persistance.group.AgentGroup;
import com.yodlee.iae.health.persistance.group.CobrandGroup;
import com.yodlee.iae.health.persistance.group.SiteGroup;
import com.yodlee.iae.health.repository.AgentMetadataRepository;
import lombok.Data;

@Named
public class UIGroupRepository {

	@Inject
	MongoOperations mongoOps;

	@Inject
	AgentMetadataRepository agentMetadataRepository;

	public GroupListResponse getGroupsNameByCategory(GroupCategory groupCategory) {
		GroupListResponse response = new GroupListResponse();
		response.setCategory(groupCategory);
		Class<?> collectionClass = getCollectionClass(groupCategory);
		if (collectionClass == null) {
			response.setGroupNames(new ArrayList<>());
		} else {
			ProjectionOperation project = Aggregation.project("groupName").andExclude("_id");
			Aggregation aggregation = Aggregation.newAggregation(project);
			AggregationResults<GroupName> groups = null;
			List<String> groupNames = new ArrayList<>();
			groups = mongoOps.aggregate(aggregation, collectionClass, GroupName.class);
			groups.getMappedResults().forEach(s -> groupNames.add(s.getGroupName()));
			response.setGroupNames(groupNames);
		}
		return response;
	}

	public AgentListForGroup getAgentListForGroup(GroupCategory groupCategory, String groupName) {
		AgentListForGroup response = new AgentListForGroup();
		response.setCategory(groupCategory);
		response.setGroupName(groupName);
		Class<?> collectionClass = getCollectionClass(groupCategory);
		if (collectionClass == null) {
			response.setAgentList(new ArrayList<>());
		} else {
			MatchOperation match = Aggregation.match(Criteria.where("groupName").is(groupName));
			ProjectionOperation project = Aggregation.project("agentIds").andExclude("_id");
			Aggregation aggregation = Aggregation.newAggregation(match, project);
			AggregationResults<AgentIds> agentIds = mongoOps.aggregate(aggregation, collectionClass, AgentIds.class);
			List<String> agentList = new ArrayList<>();
			if (agentIds.getMappedResults().size() > 0) {
				List<Integer> agentId = agentIds.getMappedResults().get(0).getAgentIds();
				agentList = agentMetadataRepository.getAgentListWithId(agentId);
				response.setAgentList(agentList);
			} else {
				response.setAgentList(new ArrayList<>());
			}
		}
		return response;
	}

	public static final Class<?> getCollectionClass(GroupCategory groupCategory) {
		switch (groupCategory) {
		case AGENT_GROUP: {
			return AgentGroup.class;
		}
		case COBRAND_GROUP: {
			return CobrandGroup.class;
		}
		case SITE_GROUP: {
			return SiteGroup.class;
		}
		default:
			return null;
		}
	}

	public @Data class GroupName {
		String groupName;
	}

	public @Data class AgentIds {
		List<Integer> agentIds;
	}
}
