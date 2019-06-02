/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */


package com.yodlee.health.errorsegment.repository.forecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.iae.health.forecast.BugFailure;
import com.yodlee.iae.health.forecast.BugRepoAttribute;
import com.yodlee.iae.health.forecast.BugTracker;
import com.yodlee.iae.health.forecast.ForecastAudit;
import com.yodlee.iae.health.forecast.ForecastedBucket;
import com.yodlee.iae.health.forecast.PersistenceConstants;

/**
 * 
 * @author srai This class is for saving the data in mongodb
 *
 */
@Named
public class ForecastRepository {

	static Logger logger = LoggerFactory.getLogger(ForecastRepository.class);

	@Inject
	private MongoOperations mongoOperations;

	public void saveSegmentedBucketforPattenAnalysis(ForecastedBucket forecastBucket) {

		Query query = new Query().addCriteria(Criteria.where(PersistenceConstants.DOCUMENT_TYPE)
				.is(PersistenceConstants.PATTERN_ANALYSIS_TYPE).and("agentName").is(forecastBucket.getAgentName()));
		List<ForecastedBucket> forecastBucketList = new ArrayList<>();
		Date oldestDate = new Date();
		boolean isDeletionRequired = false;

		forecastBucketList = mongoOperations.find(query, ForecastedBucket.class);

		if (forecastBucketList != null && forecastBucketList.size() == 4) {
			isDeletionRequired = true;
			for (ForecastedBucket forecastBucketTemp : forecastBucketList) {
				if (oldestDate.compareTo(forecastBucketTemp.getTime()) > 0) {
					oldestDate = forecastBucketTemp.getTime();
				}
			}
		}
		if (isDeletionRequired) {
			Query removeQuery = new Query().addCriteria(
					Criteria.where(PersistenceConstants.DOCUMENT_TYPE).is(PersistenceConstants.PATTERN_ANALYSIS_TYPE)
							.and("agentName").is(forecastBucket.getAgentName()).and("time").is(oldestDate));
			mongoOperations.remove(removeQuery, PersistenceConstants.MONGODB_COLLECTION);
		}

		mongoOperations.save(forecastBucket, PersistenceConstants.MONGODB_COLLECTION);
	}

	public void saveForecastAudit(ForecastAudit forecastAudit) {
		mongoOperations.save(forecastAudit);
	}

	public void saveBugAttributes(BugRepoAttribute bugRepoAttribute) {
		mongoOperations.save(bugRepoAttribute, PersistenceConstants.MONGODB_COLLECTION);
	}

	public void saveAnalyzerId(BugTracker bugTracker,SegmentedBucket segmentedBucket) {
		Query query=new Query(Criteria.where("analyzerId").is(bugTracker.getAnalyzerId()));
		Update update=new Update();
		update.set("cobrandId", segmentedBucket.getTopFailure().getCobrandId());
		update.set("cacheItemId", segmentedBucket.getTopFailure().getCacheItemId());
		update.set("agentName",bugTracker.getAgentName());
		update.inc("statusCounter", 1);
		mongoOperations.upsert(query, update, BugTracker.class);
	}

	
	public List<BugTracker> getListOfAnalyserID() {

		Query query = null;
		query = new Query(Criteria.where("statusCounter").lt(2).and("creationDate")
				.gt(new Date(System.currentTimeMillis() - 6 * 7200 * 1000)));

		return mongoOperations.find(query, BugTracker.class);
	}
	

	public void statusCounterUpdation(BugTracker bug) {
		logger.info("Inside Updation in bugtracker");
		Query query = new Query(Criteria.where("analyzerId").is(bug.getAnalyzerId()));
		Update update = new Update();
		update.set("lastModified", new Date());
		update.inc("statusCounter", 1);
		mongoOperations.updateFirst(query, update, BugTracker.class);

	}
	
	public void saveFailureBug(BugFailure bugFailure) {
		mongoOperations.save(bugFailure);
	}
}
