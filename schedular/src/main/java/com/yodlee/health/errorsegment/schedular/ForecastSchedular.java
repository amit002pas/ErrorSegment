package com.yodlee.health.errorsegment.schedular;

import java.util.List;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.yodlee.health.errorsegment.schedular.util.SchedularConstants;

@Named
@EnableAutoConfiguration
public class ForecastSchedular {

	@Value("${yuva.agents}")
	String configuredYuvaAgents;

	Logger logger = LoggerFactory.getLogger(ForecastSchedular.class);

	public void scheduleRunForecaster(String token,List<String> agentList) {

		for (String agentName : agentList) {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(SchedularConstants.FORECAST_URL + agentName);
			MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
			multivaluedMap.add(SchedularConstants.HEADER_ACCEPT, SchedularConstants.HEADER_JSON_VALUE);
			multivaluedMap.add(SchedularConstants.HEADER_CONTENT_TYPE, SchedularConstants.HEADER_JSON_VALUE);
			multivaluedMap.add(SchedularConstants.AUTHOURIZATION, token);
			target.request().headers(multivaluedMap).get();
		}

	}
}