/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.repository.bugautocloser;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;

/**
 * @author mkumar10
 *
 */
@Named
@Scope("prototype")
public class AutoCloseRepository {
	@Inject
	private MongoOperations mongoOperations;
	
	public String STATUS = "status";
	public String NEXT_SCHEDULED_TIME = "nextScheduledTime";
	public String RETRY_COUNT = "retryCount";

	public void saveBugDetail(BugAutoCloserSchedularJob bugAutoCloserSchedularJob) {
		mongoOperations.save(bugAutoCloserSchedularJob);
	}
	
	public BugAutoCloserSchedularJob getById(String id) {
		return mongoOperations.findById(id, BugAutoCloserSchedularJob.class);
	}
	
	public List<BugAutoCloserSchedularJob> getBugList() {
		Query query= new Query();
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where(STATUS).is(JobStatus.READY), Criteria.where(NEXT_SCHEDULED_TIME).lt(new Date()).and(RETRY_COUNT).lt(3));
		query.addCriteria(criteria);
		return mongoOperations.find(query, BugAutoCloserSchedularJob.class);
	}
	
	public List<BugAutoCloserSchedularJob> getJobsForAuditing() {
		Query query= new Query();
		Criteria criteria = Criteria.where(STATUS).is(JobStatus.INPROGRESS);
		query.addCriteria(criteria);			
		return mongoOperations.find(query, BugAutoCloserSchedularJob.class);
	}
}
