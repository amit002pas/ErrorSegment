/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.repository.bugautocloser;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;

@RunWith(SpringJUnit4ClassRunner.class)
public class BugAutoCloserAuditRepoTest {
	@InjectMocks
	private BugAutoCloserAuditRepo bugAutoCloserAuditRepo;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test(){
		
		bugAutoCloserAuditRepo.getBugAutoCloserAuditById("1234");
		
		bugAutoCloserAuditRepo.getSuccessAndNotClosedByChironBugs();
		
		bugAutoCloserAuditRepo.getWaitingStatusAuditDoc();
		
		bugAutoCloserAuditRepo.isExistCheckById("124");
		
		BugAutoCloserAudit bugAutoCloserAudit = new BugAutoCloserAudit();
		bugAutoCloserAuditRepo.saveBugAutoCloserAudit(bugAutoCloserAudit);
		
	}
}
