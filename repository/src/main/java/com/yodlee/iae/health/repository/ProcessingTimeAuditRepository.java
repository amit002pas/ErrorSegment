/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yodlee.iae.health.PersistanceConstants;
import com.yodlee.iae.health.ProcessingTimeAudit;
import com.yodlee.iae.health.errorsegment.ErrorStatsAudit;
import com.yodlee.iae.health.refresh.RefreshStatsAudit;

/**
 * 
 * @author srai
 *
 */

@Named
public class ProcessingTimeAuditRepository {

	@Inject
	MongoOperations mongoOps;

	private static Logger logger = LoggerFactory.getLogger(ProcessingTimeAuditRepository.class);

	public void saveTimeAudit(ProcessingTimeAudit timeTrackerPersistence) {
		mongoOps.save(timeTrackerPersistence, PersistanceConstants.COLLECTION_TIME_TRACKER);
		logger.info("+++ Time audit saved in db +++");

	}

	public Object getInstanceLastAuditData(String groupName,Class classname) {
		MatchOperation match = Aggregation.match(Criteria.where("groupName").is(groupName));
		SortOperation sort = Aggregation.sort(new Sort(Sort.Direction.DESC, "endTime"));
		LimitOperation limit = Aggregation.limit(1);
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);
		if(classname.getName().equals(ErrorStatsAudit.class.getName()))
		{
			return mongoOps.aggregate(aggregation, ErrorStatsAudit.class, ErrorStatsAudit.class).getMappedResults().get(0);}
		else {
			return mongoOps.aggregate(aggregation, RefreshStatsAudit.class, RefreshStatsAudit.class).getMappedResults().get(0);}

	}

	public void updationStartandEndTime(String instance, Date startTime, Date endTime) {
		Update update = new Update();
		update.set("startTime", startTime);
		update.set("endTime", endTime);
		mongoOps.updateFirst(new Query(Criteria.where("instance").is(instance)), update, ErrorStatsAudit.class);
	}

	public void insertLatestAudit(Object auditTracker) {
		if(auditTracker instanceof ErrorStatsAudit)
			mongoOps.save(auditTracker,PersistanceConstants.COLLECTION_ERROR_STATS_AUDIT);
		else
			mongoOps.save(auditTracker,PersistanceConstants.COLLECTION_REFRESH_STATS_AUDIT);
	}

}