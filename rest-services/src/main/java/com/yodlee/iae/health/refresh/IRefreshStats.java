package com.yodlee.iae.health.refresh;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IRefreshStats {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/refreshstats")
	public Response getRefreshStats(String refreshStatsRequest);

}
