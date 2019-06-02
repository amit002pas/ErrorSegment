package com.yodlee.iae.health.uigroupimpl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import com.yodlee.iae.health.datatypes.kafkagroup.GroupCategory;
import com.yodlee.iae.health.datatypes.uigroup.AgentListForGroup;
import com.yodlee.iae.health.datatypes.uigroup.GroupListResponse;
import com.yodlee.iae.health.repository.uigroup.UIGroupRepository;
import com.yodlee.iae.health.resource.AgentListResponseForGroup;
import com.yodlee.iae.health.uigroup.ICategoryFilter;

@Named
@Path("/group")
public class CategoryFilterImpl implements ICategoryFilter{

	@Inject
	UIGroupRepository uiGroupRepository;
	
	@Override
	public GroupListResponse getGroups(GroupCategory groupCategory) {
		return uiGroupRepository.getGroupsNameByCategory(groupCategory);
	}

	@Override
	public AgentListResponseForGroup getAgentListForGroup(GroupCategory groupCategory, String groupName) {
		AgentListResponseForGroup response = new AgentListResponseForGroup();
		AgentListForGroup agentListForGroup = uiGroupRepository.getAgentListForGroup(groupCategory, groupName);
		response.setStatus("Success");
		if(!agentListForGroup.getAgentList().isEmpty()){
			response.setMessage(agentListForGroup.getAgentList().size()+" Agent(s) retrieved");
			response.setAgentListForGroup(agentListForGroup);
			return response;
		}else{
			response.setMessage("No Agent found");
			response.setAgentListForGroup(agentListForGroup);
			return response;
		}		
	}
}
