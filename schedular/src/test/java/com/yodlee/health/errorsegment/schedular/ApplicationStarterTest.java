/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */ 
package com.yodlee.health.errorsegment.schedular;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClient;
import com.yodlee.health.errorsegment.gateway.util.KeystoreGateway;
import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.health.errorbucket.IErrorBucketService;
import com.yodlee.iae.health.errorbucket.impl.ErrorBucketServiceImpl;
import com.yodlee.iae.health.jnanalysis.service.JNSyntheticBugAnalysisServiceImpl;
import com.yodlee.iae.health.jnanalysis.steps.JuggernautAttachmentStep;
import com.yodlee.iae.health.refreshstats.schedular.MEComponent;
import com.yodlee.iae.health.repository.ProcessGroupRepository;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;
import com.yodlee.iae.vitarana.Task;
import com.yodlee.iae.vitarana.exception.TaskSubmitException;
import com.yodlee.iae.vitarana.producers.TaskProducerPro;

public class ApplicationStarterTest {

	@InjectMocks
	private ApplicationStarter spluckStatsSchedular;

	@Mock
	Task task;

	@Mock
	private ErrorBucketServiceImpl errorBucketServiceImpl;

	@Mock
	private ProcessingTimeAuditRepository timeTrackerRepo;

	@Mock
	private JuggernautAttachmentStep jnAnalysisAttachmentService;

	@Mock
	private ForecastRepository forecastRepository;

	@Mock
	private JNSyntheticBugAnalysisServiceImpl jnAnalysisService;

	@Mock
	private LDAPAuthenticationClient lDAPAuthenticationClient;

	@Mock
	private TaskProducerPro taskProducerPro;

	@Mock
	private KeystoreGateway keystoreGateway;

	@Mock
	private ForecastSchedular forecast;

	@Mock
	private IErrorBucketService errorBucketService;

	@Mock
	private MEComponent masterElection;

	@Mock
	private ProcessGroupRepository processGroupRepo;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDoProcessing() throws Exception {

		List<String> agentList = new ArrayList<String>();
		agentList.add("CharlesSchwabStocks");
		agentList.add("ETradeInvestments");
		spluckStatsSchedular.doProcessing(agentList, "G1");

	}

	@Test
	public void testStartJuggernautAnalysis() throws Exception {

		spluckStatsSchedular.startJuggernautAnalysis();

	}

	@Test
	public void testSendAgentsForAnalysis() throws IOException, TaskSubmitException{

		List<String> listOfgroup = new ArrayList<>();
		listOfgroup.add("sadsadasd");
		listOfgroup.add("aaa");
		Mockito.when(masterElection.isMaster()).thenReturn(true);
		Mockito.when(processGroupRepo.getAllGroupNames()).thenReturn(listOfgroup);
		spluckStatsSchedular.sendAgentsForAnalysis();

	}

	@Test
	public void testSendAgentsForAnalysisFalse() throws IOException, TaskSubmitException{

		Mockito.when(masterElection.isMaster()).thenReturn(false);
		spluckStatsSchedular.sendAgentsForAnalysis();

	}

	@Test
	public void testSetAgents(){
		String agent = "Chase";
		spluckStatsSchedular.setAgent(agent);
	}

	@Test
	public void testForTopic(){
		spluckStatsSchedular.forTopic();
	}

	@Test
	public void testProcess(){
		byte b[] = {12,22};
		Mockito.when(task.getPayload()).thenReturn(b);
		List<String> listOfAgent = new ArrayList<>();
		listOfAgent.add("weadsad");
		listOfAgent.add("asdasdads");
		Mockito.when(processGroupRepo.searchAgentListByGroup(any(String.class))).thenReturn(listOfAgent);
		spluckStatsSchedular.process(task);
	}
	
}
