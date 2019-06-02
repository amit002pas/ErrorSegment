/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */ 
package com.yodlee.health.errorsegment.schedular;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserSchedularAuditServices;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserServiceImpl;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;

public class BugAutoCloserSchedularTest {

	@InjectMocks
	private BugAutoCloserSchedular bugAutoCloserSchedular;

	@Mock
	private BugAutoCloserAuditRepo bugAutoCloserAuditRepo;

	@Mock
	private AutoCloseRepository autoCloseRepository;

	@Mock
	private BugAutoCloserSchedularAuditServices bugAutoCloserSchedularAuditServices;

	@Mock
	private BugAutoCloserServiceImpl bugAutoCloserService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}


    @Test
    public void testScheduleAutoCloserBugs() {
   
    	Object obj = new Object();
    	Mockito.when(bugAutoCloserService.process(obj)).thenReturn(obj);
    	bugAutoCloserSchedular.scheduleAutoCloserBugs();
    	
    }
    
    @Test
    public void testBugAutoCloserAudit() {
   
    	List<BugAutoCloserSchedularJob> mainBugList = new ArrayList<>();
    	List<BugAutoCloserAudit> bugAutoCloserAuditList= new ArrayList<>();
    	BugAutoCloserAudit bugAutoCloserAudit = new BugAutoCloserAudit();
    	bugAutoCloserAudit.setAnalysisId("asdsad");
    	bugAutoCloserAuditList.add(bugAutoCloserAudit);
    	List<BugAutoCloserAudit> auditBugList= new ArrayList<>();
    	BugAutoCloserAudit bugAutoCloserAudit1 = new BugAutoCloserAudit();
    	bugAutoCloserAudit1.setSyntheticBugId("adssadadsasd");
    	bugAutoCloserAudit.setAnalysisId("asdsad");
    	JobStatus status = JobStatus.READY;
    	bugAutoCloserAudit.setStatus(status);
    	auditBugList.add(bugAutoCloserAudit1);
    	Mockito.when(autoCloseRepository.getJobsForAuditing()).thenReturn(mainBugList);
    	Mockito.when(bugAutoCloserAuditRepo.getWaitingStatusAuditDoc()).thenReturn(bugAutoCloserAuditList);
    	Mockito.when(bugAutoCloserAuditRepo.getSuccessAndNotClosedByChironBugs()).thenReturn(auditBugList);
    	
    	bugAutoCloserSchedular.BugAutoCloserAudit();
    }
}
