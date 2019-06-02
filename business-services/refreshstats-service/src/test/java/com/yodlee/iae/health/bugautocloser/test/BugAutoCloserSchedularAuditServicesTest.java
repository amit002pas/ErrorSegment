/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautFinalResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.Outcome;
import com.yodlee.health.errorsegment.gateway.jn.JNTokenGenerator;
import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;
import com.yodlee.iae.health.autoclose.RequestResponseDetails;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserSchedularAuditServices;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;
import com.yodlee.iae.health.util.BugAutoCloserConfigModel;
import com.yodlee.iae.health.util.ReadAutoCloseConfig;

/**
 * @author vchhetri
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class BugAutoCloserSchedularAuditServicesTest {
	@Mock
	private BugAutoCloserAuditRepo bugAutoCloserAuditRepo;
	@Mock
	private JNTokenGenerator tokenGenerator;
	@Mock
	private ReadAutoCloseConfig readAutoCloseConfig;
	@Mock
	private JuggernautGateway juggernautGateway;	
	@Mock
	private AutoCloseRepository autoCloseRepository;
	@InjectMocks
	private BugAutoCloserSchedularAuditServices bugAutoCloserSchedularAuditServices;
	
	private String tokenSample = "sampletest-token";
	private String syntheticBugId = "syn-id-test";
	private int error_code = 409;
	private String analysisId = "analysis-id-test";
	private String agentName = "agent-name-test";
	private String Update_SR = "Update_SR";
	private String Bug_Created = "Bug_Created";
	private String SUCCESS = "SUCCESS";
	private String INPROGRESS = "INPROGRESS";
	Date currentTime = new Date();
	
	@Test
	public void testForFailureCaseNoJNResponse(){
		when(autoCloseRepository.getById(any(String.class))).thenReturn(getBugAutoCloserSchedularJob());
		when(tokenGenerator.generateToken(any(String.class), any(String.class), any(String.class))).thenReturn(tokenSample);
		when(readAutoCloseConfig.getBugAutoCloserConfigModel(any(Integer.class))).thenReturn(getBugAutoCloserConfigModel(3));
		bugAutoCloserSchedularAuditServices.process(getBugAutoCloserAudit());
	}
	@Test
	public void testForFailureCaseWithJNResponse(){
		when(autoCloseRepository.getById(any(String.class))).thenReturn(getBugAutoCloserSchedularJob());
		when(tokenGenerator.generateToken(any(String.class), any(String.class), any(String.class))).thenReturn(tokenSample);
		when(readAutoCloseConfig.getBugAutoCloserConfigModel(any(Integer.class))).thenReturn(getBugAutoCloserConfigModel(3));
		when(juggernautGateway.fetchIATResponseList(any(String.class), any(String.class))).thenReturn(getJuggerNautFinalResponseList(Update_SR,SUCCESS));
		bugAutoCloserSchedularAuditServices.process(getBugAutoCloserAudit());
	}
	@Test
	public void testForInprogressCaseWithJNResponse(){
		when(autoCloseRepository.getById(any(String.class))).thenReturn(getBugAutoCloserSchedularJob());
		when(tokenGenerator.generateToken(any(String.class), any(String.class), any(String.class))).thenReturn(tokenSample);
		when(readAutoCloseConfig.getBugAutoCloserConfigModel(any(Integer.class))).thenReturn(getBugAutoCloserConfigModel(4));
		when(juggernautGateway.fetchIATResponseList(any(String.class), any(String.class))).thenReturn(getJuggerNautFinalResponseList(Bug_Created,INPROGRESS));
		bugAutoCloserSchedularAuditServices.process(getBugAutoCloserAudit());
	}
	@Test
	public void saveBugAutoCloserAuditTest(){
		when(bugAutoCloserAuditRepo.isExistCheckById(any(String.class))).thenReturn(false);
		when(readAutoCloseConfig.getBugAutoCloserConfigModel(any(Integer.class))).thenReturn(getBugAutoCloserConfigModel(3));
		Mockito.doNothing().when(bugAutoCloserAuditRepo).saveBugAutoCloserAudit(any(BugAutoCloserAudit.class));
		List<BugAutoCloserSchedularJob> bugAutoCloserSchedularJobList = new ArrayList<>(); 
		bugAutoCloserSchedularJobList.add(getBugAutoCloserSchedularJob());
		bugAutoCloserSchedularAuditServices.saveBugAutoCloserAudit(bugAutoCloserSchedularJobList);
		
	}
	
	public List<JuggerNautFinalResponse> getJuggerNautFinalResponseList(String action , String status){
		List<JuggerNautFinalResponse> jnResponseList = new ArrayList<>();
		com.yodlee.health.errorsegment.datatypes.jnanalysis.AnalysisDetails analysisDetails = new com.yodlee.health.errorsegment.datatypes.jnanalysis.AnalysisDetails();
		analysisDetails.setAnalysis("analysis");
		List<com.yodlee.health.errorsegment.datatypes.jnanalysis.AnalysisDetails> analysisDetailsList = new ArrayList<>();
		analysisDetailsList.add(analysisDetails);
		Outcome outcome = new Outcome();
		outcome.setAction(action);
		JuggerNautFinalResponse juggerNautFinalResponse = new JuggerNautFinalResponse();
		juggerNautFinalResponse.setStatus(status);
		juggerNautFinalResponse.setOutcome(outcome);
		juggerNautFinalResponse.setAnalysisDetails(analysisDetailsList);
		jnResponseList.add(juggerNautFinalResponse);
		return jnResponseList;
	}
	
	public BugAutoCloserSchedularJob getBugAutoCloserSchedularJob(){
		com.yodlee.iae.health.autoclose.AnalysisDetails analysisDetails = new com.yodlee.iae.health.autoclose.AnalysisDetails();
		analysisDetails.setAnalysisId(analysisId);
		analysisDetails.setAnalysisStartTime(currentTime);
		
		List<com.yodlee.iae.health.autoclose.AnalysisDetails> analysisDetailsList = new ArrayList<>();
		analysisDetailsList.add(analysisDetails);
		BugAutoCloserSchedularJob bugAutoCloserSchedularJob = new BugAutoCloserSchedularJob();
		bugAutoCloserSchedularJob.setSyntheticBugId(syntheticBugId);
		bugAutoCloserSchedularJob.setErrorCode(error_code);
		bugAutoCloserSchedularJob.setRetryCount(0);
		bugAutoCloserSchedularJob.setStatus(JobStatus.INPROGRESS);
		bugAutoCloserSchedularJob.setAnalysisDetails(analysisDetailsList);
		bugAutoCloserSchedularJob.setAgentName(agentName);
		bugAutoCloserSchedularJob.setNextScheduledTime(DateUtils.addHours(currentTime, 1));	
		bugAutoCloserSchedularJob.setCreatedDate(currentTime);
		bugAutoCloserSchedularJob.setLastUpdDate(currentTime);
		
		return bugAutoCloserSchedularJob;
	}
		
	
	public BugAutoCloserAudit getBugAutoCloserAudit(){
		RequestResponseDetails requestResponseDetails = new RequestResponseDetails();
		requestResponseDetails.setReqSentTime(new Date());
		requestResponseDetails.setResRecTime(new Date());
		requestResponseDetails.setResponseMessage("Response-message");
		requestResponseDetails.setResponseAction("Update_SR");
		requestResponseDetails.setStatus("COMPLETE");
		
		List<RequestResponseDetails> requestResponseList = new ArrayList<>();
		RequestResponseDetails requestResponseDetails1 = new RequestResponseDetails();
		requestResponseList.add(requestResponseDetails1);
		requestResponseList.add(requestResponseDetails);
		
		BugAutoCloserAudit bugAutoCloserAudit = new BugAutoCloserAudit();
		bugAutoCloserAudit.setAnalysisId("analysis-id");
		bugAutoCloserAudit.setSyntheticBugId("sysnthetic bug-id");
		bugAutoCloserAudit.setRequestResponseDetails(requestResponseList);
		bugAutoCloserAudit.setNextPickTime(new Date());
		bugAutoCloserAudit.setReqSentTime(new Date());
		bugAutoCloserAudit.setStatus(JobStatus.SUCCESS);
		bugAutoCloserAudit.setClosedByChiron(false);
		
		return bugAutoCloserAudit;
	}
	public BugAutoCloserConfigModel getBugAutoCloserConfigModel(int retryCount){
		BugAutoCloserConfigModel config = new BugAutoCloserConfigModel();
		config.setRetryCount(retryCount);
		config.setRetryInterval(180);
		return config;
	}

}
