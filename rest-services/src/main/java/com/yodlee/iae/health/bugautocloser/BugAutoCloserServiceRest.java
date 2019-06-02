/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mkumar10, vchhetri
 *
 */
@Path("/audit")
public interface BugAutoCloserServiceRest {
	
	/**
	 * @param bugId
	 * @return analysis details for a bugId.
	 */
	@GET
	@Path("/bug/{bugId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAuditDetailsForSyntheticBug(@PathParam("bugId") String bugId);


}
