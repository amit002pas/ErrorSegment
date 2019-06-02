package com.yodlee.health.errorsegment.schedular;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import com.yodlee.health.errorsegment.schedular.util.SchedularConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Named
@EnableAutoConfiguration
@EnableScheduling
public class HealthSchedular {

	static Logger logger = LoggerFactory.getLogger(HealthSchedular.class);

	public static void getDiffBuckets(String token) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(SchedularConstants.healthURL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(SchedularConstants.HEADER_ACCEPT, SchedularConstants.HEADER_JSON_VALUE);
		multivaluedMap.add(SchedularConstants.HEADER_CONTENT_TYPE, SchedularConstants.HEADER_JSON_VALUE);
		multivaluedMap.add(SchedularConstants.AUTHOURIZATION, token);
		target.request().headers(multivaluedMap).get();
	}
}
