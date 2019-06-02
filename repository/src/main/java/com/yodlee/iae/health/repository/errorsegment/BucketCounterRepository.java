/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository.errorsegment;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yodlee.iae.health.errorsegment.BucketCounter;

@Named
public class BucketCounterRepository {

	@Inject
	MongoOperations mongoOps;

	public int incrementAndGetAuditCounter() {

		Query query = new Query(Criteria.where("type").is("segmentCounter"));
		Update update = new Update().inc("mSegmentCounter", 1);
		BucketCounter escd = mongoOps.findAndModify(query, update, BucketCounter.class);
		return escd.getMSegmentCounter();
	}

}
