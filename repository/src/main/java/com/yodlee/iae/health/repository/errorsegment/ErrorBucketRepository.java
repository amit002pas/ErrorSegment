/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository.errorsegment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.iae.health.PersistanceConstants;
import com.yodlee.iae.health.errorsegment.ErrorBucket;

/***
 * 
 * @author akumar23 / svatts
 *
 */

@Named
public class ErrorBucketRepository {

	@Inject
	MongoOperations mongoOps;

	

	public void saveErrorBucket(ErrorBucket errorBucket) {
		mongoOps.save(errorBucket, PersistanceConstants.COLLECTION_ERROR_BUCKET);

	}
	
	public List<ErrorBucket> getBucketIfPresent(Bucket bucket) {
		
		List<ErrorBucket> listOfBucket=new ArrayList<>();
		Query query=new Query(Criteria.where("juggernautDetails").exists(true).and("errorCode").is(bucket.getErrorCode()).and("agentName").is(bucket.getAgentName()).and("startTime").gt(new Date(System.currentTimeMillis()-1000*60*60*24)));
		listOfBucket=mongoOps.find(query, ErrorBucket.class);
		return listOfBucket;
		
	}
	public List<ErrorBucket> getBucketLessThan10(Bucket bucket){
		
		List<ErrorBucket> listOfBucket=new ArrayList<>();
		Query query=new Query(Criteria.where("juggernautDetails").exists(true).and("errorCode").is(bucket.getErrorCode()).and("agentName").is(bucket.getAgentName()).and("startTime").gt(new Date(System.currentTimeMillis()-1000*60*60*24)));
		listOfBucket=mongoOps.find(query, ErrorBucket.class);
		return listOfBucket;
	}

}