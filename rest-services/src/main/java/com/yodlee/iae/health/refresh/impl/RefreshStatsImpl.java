package com.yodlee.iae.health.refresh.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.iae.health.refresh.IRefreshStats;
import com.yodlee.iae.health.refresh.RefreshStats;
import com.yodlee.iae.health.repository.RefreshStatsRepository;
import com.yodlee.iae.health.repository.exception.MyException;
import com.yodlee.iae.health.resource.RefreshStatsRequest;
import com.yodlee.iae.health.resource.RefreshStatsResponse;

@Named
@Path("/health")
public class RefreshStatsImpl implements IRefreshStats {

	@Inject
	RefreshStatsRepository refreshStatsRepository;

	@Override
	public Response getRefreshStats(String request) {

		Gson gson=new Gson();
		RefreshStatsRequest refreshStatsRequest=gson.fromJson(request.toString(), RefreshStatsRequest.class);
		RefreshStatsResponse refreshStatsResponse = new RefreshStatsResponse();
		if (null == refreshStatsRequest || null == refreshStatsRequest.getEndTime()
				|| null == refreshStatsRequest.getStartTime() || null == refreshStatsRequest.getAgentList()) {

			refreshStatsResponse.setStatus(GatewayConstants.FAILURE);
			refreshStatsResponse.setMessage(GatewayConstants.INVALID_REQUEST);
			return Response.ok().entity(refreshStatsResponse).build();
		}

		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.DATE_FORMATTER);
		List<RefreshStats> refreshStats = null;

		try {
			refreshStats = refreshStatsRepository.findRefreshStatsByTimeandAgents(
					sdf.parse(refreshStatsRequest.getStartTime()), sdf.parse(refreshStatsRequest.getEndTime()),
					refreshStatsRequest.getAgentList());
		} catch (NullPointerException e) {
			refreshStatsResponse.setMessage(e.getMessage());
			refreshStatsResponse.setStatus(GatewayConstants.FAILURE);
			return Response.ok().entity(refreshStatsResponse).build();
		} catch (MyException e) {
			refreshStatsResponse.setMessage(e.getMessage());
			refreshStatsResponse.setStatus(GatewayConstants.FAILURE);
			return Response.ok().entity(refreshStatsResponse).build();
		} catch (ParseException p) {
			refreshStatsResponse.setMessage(p.getMessage());
			refreshStatsResponse.setStatus(GatewayConstants.FAILURE);
			return Response.ok().entity(refreshStatsResponse).build();
		}

		RefreshStats consolidated = new RefreshStats();

		Integer sumSuccess = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getSuccess())))
				.reduce((e1, e2) -> e1 + e2).get();

		Integer sumFailure = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getFailure())))
				.reduce((e1, e2) -> e1 + e2).get();

		Integer sumAgent = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getAgentcount())))
				.reduce((e1, e2) -> e1 + e2).get();

		Integer sumSite = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getSitecount())))
				.reduce((e1, e2) -> e1 + e2).get();

		Integer sumUar = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getUarcount())))
				.reduce((e1, e2) -> e1 + e2).get();

		Integer sumInfra = refreshStats.stream().map(i -> Integer.parseInt(String.valueOf(i.getInfracount())))
				.reduce((e1, e2) -> e1 + e2).get();

		consolidated.set_id(GatewayConstants.ALL);
		consolidated.setTotalRefresh(sumSuccess + sumFailure);
		consolidated.setSuccess(sumSuccess);
		consolidated.setFailure(sumFailure);
		consolidated.setAgentcount(sumAgent);
		consolidated.setInfracount(sumInfra);
		consolidated.setSitecount(sumSite);
		consolidated.setUarcount(sumUar);
		refreshStatsResponse.setMessage("");
		refreshStatsResponse.setStatus(GatewayConstants.SUCCESS);
		refreshStatsResponse.setRefreshStats(refreshStats);
		refreshStatsResponse.setConsolidatedRefreshStats(consolidated);

		return Response.ok().entity(refreshStatsResponse).header("Acess-Control-Allow-Origin", "")
				.header("Acess-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
				.header("Acess-Control-Max-Age", "3600")
				.header("Acess-Control-Allow-Headers", "authorization,content-type,xsrf-token")
				.header("Acess-Control-Expose-Headers", "xsrf-token").build();

	}

}
