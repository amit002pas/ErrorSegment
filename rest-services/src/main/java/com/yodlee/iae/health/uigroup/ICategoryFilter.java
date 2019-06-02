package com.yodlee.iae.health.uigroup;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yodlee.iae.health.datatypes.kafkagroup.GroupCategory;
import com.yodlee.iae.health.datatypes.uigroup.GroupListResponse;
import com.yodlee.iae.health.resource.AgentListResponseForGroup;

public interface ICategoryFilter {

	@GET
	@Path("/allgroups/category={category}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get group names of a category")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Returns List of group of the category"))
	public GroupListResponse getGroups(@PathParam("category") GroupCategory category);
	
	@GET
	@Path("/agentlist/category={category}/group={group}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get list of agents for a particular group of particular category")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Returns list of agents for a particular group of particular category"))
	public AgentListResponseForGroup getAgentListForGroup(@PathParam("category") GroupCategory category, @PathParam("group") String groupName);
	
}
