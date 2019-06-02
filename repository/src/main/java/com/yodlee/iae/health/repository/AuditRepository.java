/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.yodlee.iae.health.PersistanceConstants;
import com.yodlee.iae.health.errorsegment.ErrorStatsAudit;
import com.yodlee.iae.health.refresh.RefreshStatsAudit;


/***
 * This class will be used as audit to monitor the last fetched details
 * @author akumar23
 *
 */
@Named
public class AuditRepository {

	@Inject
	MongoOperations mongoOps;

	
	/**
	 * Inserts the current audit info for tracking purpose
	 * @param auditTracker
	 */
	public void insertLatestAudit(Object auditTracker) {
		if (auditTracker instanceof ErrorStatsAudit)
			mongoOps.save(auditTracker, PersistanceConstants.COLLECTION_ERROR_STATS_AUDIT);
		else
			mongoOps.save(auditTracker, PersistanceConstants.COLLECTION_REFRESH_STATS_AUDIT);
	}

	/**
	 * 
	 * @param groupName
	 * @param classname
	 * @return latest audit info refresh stats
	 */
	public Object getGroupLastAudit(String groupName, Class classname) {
		MatchOperation match = Aggregation.match(Criteria.where(RepositoryConstants.GROUP_NAME).is(groupName));
		SortOperation sort = Aggregation.sort(new Sort(Sort.Direction.DESC, RepositoryConstants.ENDTIME));
		LimitOperation limit = Aggregation.limit(1);
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);
		if (classname.getName().equals(ErrorStatsAudit.class.getName()))
			return mongoOps.aggregate(aggregation, ErrorStatsAudit.class, ErrorStatsAudit.class).getMappedResults().get(0);
		else
			return mongoOps.aggregate(aggregation, RefreshStatsAudit.class, RefreshStatsAudit.class).getMappedResults()
					.get(0);

	}

}
