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

import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.JobStatus;

/**
 * @author vchhetri
 *
 */
@Named
@Scope("prototype")
public class BugAutoCloserAuditRepo {
	@Inject
	private MongoOperations mongoOperations;
	
	private static final String STATUS = "status";
	private static final String NEXTPICKTIME ="nextPickTime";
	private static final String ISCLOSEDBYCHIRON ="isClosedByChiron";
	
	public List<BugAutoCloserAudit> getWaitingStatusAuditDoc() {
		Query query= new Query();
		Criteria criteria = new Criteria(); 
				criteria.andOperator(Criteria.where(STATUS).is(JobStatus.INPROGRESS), Criteria.where(NEXTPICKTIME).lt(new Date()));
		query.addCriteria(criteria);			
		return mongoOperations.find(query, BugAutoCloserAudit.class);
	}
	
	public BugAutoCloserAudit getBugAutoCloserAuditById(String id){
		return mongoOperations.findById(id, BugAutoCloserAudit.class);
	}

	public boolean isExistCheckById(String analysisId) {
		boolean isExist = false;		
		BugAutoCloserAudit bugAutoCloserAudit= getBugAutoCloserAuditById(analysisId);
		if(null!=bugAutoCloserAudit) isExist=true;		
		return isExist;
	}

	public void saveBugAutoCloserAudit(BugAutoCloserAudit bugAutoCloserAudit) {
		mongoOperations.save(bugAutoCloserAudit);		
	}

	public List<BugAutoCloserAudit> getSuccessAndNotClosedByChironBugs() {
		Query query= new Query();
		Criteria criteria = new Criteria(); 
		criteria.andOperator(Criteria.where(STATUS).is(JobStatus.SUCCESS), Criteria.where(ISCLOSEDBYCHIRON).is(false));
		query.addCriteria(criteria);			
		return mongoOperations.find(query, BugAutoCloserAudit.class);
	}


}
