/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.iae.health.uigroupimpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.datatypes.kafkagroup.GroupCategory;
import com.yodlee.iae.health.datatypes.uigroup.GroupListResponse;
import com.yodlee.iae.health.repository.uigroup.UIGroupRepository;
import com.yodlee.iae.health.datatypes.uigroup.AgentListForGroup;
import com.yodlee.iae.health.resource.AgentListResponseForGroup;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryFilterImplTest { 

	@InjectMocks  
	CategoryFilterImpl categoryFilterImpl;
	
	@Mock
	UIGroupRepository uiGroupRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetGroups(){
		GroupCategory groupCategory = GroupCategory.valueOf("AGENT_GROUP");
		GroupListResponse groupListResponse = new GroupListResponse();
		List<String> groupNames = Arrays.asList("AgentSet1","AgentSet2","AgentSet3");
		groupListResponse.setCategory(groupCategory);
		groupListResponse.setGroupNames(groupNames);
		when(uiGroupRepository.getGroupsNameByCategory(groupCategory)).thenReturn(groupListResponse);
		assertEquals(categoryFilterImpl.getGroups(groupCategory), groupListResponse);
	}
	
	@Test
	public void testGetAgentListForGroupAgentFound(){
		GroupCategory groupCategory = GroupCategory.valueOf("AGENT_GROUP");
		String groupName = "AgentSet3";
		List<String> agentList = Arrays.asList("CharlesSchwabStocks","VanguardBalancesInvestments","FidelityInvestments","ADPPublicLogin");
		AgentListForGroup agentListForGroup = new AgentListForGroup();
		agentListForGroup.setCategory(groupCategory);
		agentListForGroup.setGroupName(groupName);
		agentListForGroup.setAgentList(agentList);
		
		AgentListResponseForGroup response = new AgentListResponseForGroup();
		response.setAgentListForGroup(agentListForGroup);
		response.setMessage(agentListForGroup.getAgentList().size()+" Agent(s) retrieved");
		response.setStatus("SUCCESS");
		
		when(uiGroupRepository.getAgentListForGroup(groupCategory, groupName)).thenReturn(agentListForGroup);
		AgentListResponseForGroup responseReceived = categoryFilterImpl.getAgentListForGroup(groupCategory, groupName);
		assertEquals(responseReceived.getAgentListForGroup().getAgentList().size(), 4);		
	}
	
	@Test
	public void testGetAgentListForGroupAgentNotFound(){
		GroupCategory groupCategory = GroupCategory.valueOf("AGENT_GROUP");
		String groupName = "";
		List<String> agentList = new ArrayList<>();
		AgentListForGroup agentListForGroup = new AgentListForGroup();
		agentListForGroup.setCategory(groupCategory);
		agentListForGroup.setGroupName(groupName);
		agentListForGroup.setAgentList(agentList);
		
		AgentListResponseForGroup response = new AgentListResponseForGroup();
		response.setAgentListForGroup(agentListForGroup);
		response.setMessage("No Agent found");
		response.setStatus("SUCCESS");
		
		when(uiGroupRepository.getAgentListForGroup(groupCategory, groupName)).thenReturn(agentListForGroup);
		AgentListResponseForGroup responseReceived = categoryFilterImpl.getAgentListForGroup(groupCategory, groupName);
		assertEquals(responseReceived.getAgentListForGroup().getAgentList().size(), 0);		
	}
	
}