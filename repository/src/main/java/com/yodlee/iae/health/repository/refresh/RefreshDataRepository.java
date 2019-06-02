/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository.refresh;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;

import com.yodlee.iae.health.refresh.RefreshStatsCollection;

@Named
public class RefreshDataRepository {

	
	@Inject
	MongoOperations mongoOperations;

	public void insertAuditRefreshData(RefreshStatsCollection refreshStatsAudit) {
		mongoOperations.save(refreshStatsAudit);
	}
}
