/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.jnanalysis.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="Juggernaut Attachment documentation by Orphic ")
@Path("/jnanalysis")
public interface IJNAnalysisServiceRest {
	
	@POST
	@Path("/trigger/bug")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add Juggernaut Analysis in given Bug")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Return Success and add Juggernaut Analysis in the Bug as Attachment"))	
	public Response bugAnalysis(String jnAnalysisRequestStr);
	
}
