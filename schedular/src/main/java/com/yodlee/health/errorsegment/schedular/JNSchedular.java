/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.schedular;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.health.errorsegment.jnbugreanalysis.DateFilter;
import com.yodlee.health.errorsegment.jnbugreanalysis.ProcessJNOrphicSimilarBugs;
import com.yodlee.health.errorsegment.resources.jn.bugreanalysis.JNChironBugTracker;
import com.yodlee.health.errorsegment.schedular.util.SchedularConstants;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;

@Named
@EnableAutoConfiguration
@EnableScheduling
public class JNSchedular {
	@Inject
	private ProcessJNOrphicSimilarBugs processJNOrphicSimilarBugs;

	@Inject
	private MongoOperations mongoOperations;
	
	Logger logger = LoggerFactory.getLogger(JNSchedular.class);


	public static void startJuggerNautAnalysis() {

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(SchedularConstants.JN_ANALYSIS_URL);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(SchedularConstants.HEADER_ACCEPT, SchedularConstants.HEADER_JSON_VALUE);
		multivaluedMap.add(SchedularConstants.HEADER_CONTENT_TYPE, SchedularConstants.HEADER_JSON_VALUE);
		target.request().headers(multivaluedMap).get();
	}

	@Scheduled(fixedDelay = 2 * 60 * 1000)
	public void sendSimilarBugsToJN() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(JuggernautAnalysisConstants.CHIRON_SIMILAR_BUG_URL_PROD);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(SchedularConstants.HEADER_ACCEPT, SchedularConstants.HEADER_JSON_VALUE);
		multivaluedMap.add(SchedularConstants.HEADER_CONTENT_TYPE, SchedularConstants.HEADER_JSON_VALUE);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(auditBasedOnCreatedDate()));
		Map<String, Set<Integer>> map = response.readEntity(new GenericType<HashMap<String, Set<Integer>>>() {
		});
		logger.info("sendSimilarBugsToJN response : "+map);
		if (!map.isEmpty())
			processJNOrphicSimilarBugs.sendSimilarsBugsToJN(map);
	}

	public DateFilter auditBasedOnCreatedDate() {
		DateFilter dateFilter = new DateFilter();
		Query query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));
		JNChironBugTracker audit = mongoOperations.findOne(query, JNChironBugTracker.class);

		Date date;
		if (audit == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -7);
			date = cal.getTime();
		} else {
			date = audit.getCreatedDate();
		}
		dateFilter.setStartTime(date);
		dateFilter.setEndTime(new Date());
		return dateFilter;
	}
}
