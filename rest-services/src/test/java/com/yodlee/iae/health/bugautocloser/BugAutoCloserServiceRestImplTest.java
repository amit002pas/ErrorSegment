/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser;


import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.autoclose.AnalysisDetails;
import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;


@RunWith(SpringJUnit4ClassRunner.class)
public class BugAutoCloserServiceRestImplTest {

	@InjectMocks
	BugAutoCloserServiceRestImpl bugAutoCloserServiceRestImpl;
	
	@Mock
	BugAutoCloserAuditRepo bugAutoCloserAuditRepo;

	@Mock
	AutoCloseRepository autoCloseRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAuditDetailsForSyntheticBug(){
	
		BugAutoCloserSchedularJob bugAutoCloserSchedularJob = new BugAutoCloserSchedularJob();
		List<AnalysisDetails> analysisDetails = new ArrayList<>();
		AnalysisDetails analysisDetailsObj = new AnalysisDetails();
		analysisDetailsObj.setAnalysisId("1111");
		analysisDetails.add(analysisDetailsObj);
		bugAutoCloserSchedularJob.setAnalysisDetails(analysisDetails);
		BugAutoCloserAudit bugAutoCloserAudit = new BugAutoCloserAudit();
		
		when(autoCloseRepository.getById("1234")).thenReturn(bugAutoCloserSchedularJob);
		
		when(bugAutoCloserAuditRepo.getBugAutoCloserAuditById("1111")).thenReturn(bugAutoCloserAudit);
		bugAutoCloserServiceRestImpl.getAuditDetailsForSyntheticBug("1234");
	}
	@Test
	public void testGetAuditDetailsForSyntheticBugNull(){
	
		BugAutoCloserSchedularJob bugAutoCloserSchedularJob = null;
		
		when(autoCloseRepository.getById("1234")).thenReturn(bugAutoCloserSchedularJob);
		
		bugAutoCloserServiceRestImpl.getAuditDetailsForSyntheticBug("1234");
	}
}

