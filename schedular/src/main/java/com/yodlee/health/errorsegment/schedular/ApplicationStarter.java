/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.schedular;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.gateway.util.KeystoreGateway;
import com.yodlee.iae.health.ProcessingTimeAudit;
import com.yodlee.iae.health.errorbucket.IErrorBucketService;
import com.yodlee.iae.health.errorbucket.impl.ErrorBucketServiceImpl;
import com.yodlee.iae.health.jnanalysis.steps.JuggernautAttachmentStep;
import com.yodlee.iae.health.refreshstats.schedular.MEComponent;
import com.yodlee.iae.health.repository.ProcessGroupRepository;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;
import com.yodlee.iae.vitarana.ITaskConsumer;
import com.yodlee.iae.vitarana.Level;
import com.yodlee.iae.vitarana.Task;
import com.yodlee.iae.vitarana.exception.TaskSubmitException;
import com.yodlee.iae.vitarana.producers.TaskProducerPro;

/***
 * 
 * @author akumar23
 *
 */
@Named
@EnableAutoConfiguration
public class ApplicationStarter implements ITaskConsumer {

	@Inject
	ErrorBucketServiceImpl errorBucketServiceImpl;
	@Inject
	private ProcessingTimeAuditRepository timeTrackerRepo;
	@Inject
	private JuggernautAttachmentStep jnAnalysisAttachmentService;

	@Inject
	private MEComponent masterElection;

	@Inject
	private TaskProducerPro taskProducerPro;

	@Inject
	private KeystoreGateway keystoreGateway;

	/*
	 * @Inject private BugImpactUpdation bugImpactUpdation;
	 */
	@Inject
	ForecastSchedular forecast;
	@Inject
	IErrorBucketService errorBucketService;

	@Inject
	ProcessGroupRepository processGroupRepo;

	@Value("${yuva.agents}")
	private String agentLists;

	@Value("${user-username}")
	private String username;

	@Value("${presruser_pass}")
	private String password;

	/**
	 * 
	 * @param agentList
	 * @throws Exception
	 */
	//@Scheduled(initialDelay = 5000, fixedRate = 7200000)
	public void doProcessing(List<String> agentList, String groupName) throws Exception {
		//List<String> agentList, String groupName
		//List<String> agentList = Arrays.asList("USBank", "Wellsfargo");
		//String groupName = "G1";
		System.out.println(new Date() + " ^^Inside doProcessing..." + groupName);
		
		ProcessingTimeAudit timeTrackerPerisitence = new ProcessingTimeAudit();
		timeTrackerPerisitence.setApplicationStartDateTime(new Date().toString());
		long splunkStartTime = System.currentTimeMillis();
		long splunkEndTime = System.currentTimeMillis();
		timeTrackerPerisitence.setSplunkTimeMs(splunkEndTime - splunkStartTime);
		Map<String, List<Bucket>> agentDataMap = errorBucketService.getHealth(agentList, groupName);
		long buketizerEndTime = System.currentTimeMillis();
		timeTrackerPerisitence.setBucketizerTimeMs(buketizerEndTime - splunkEndTime);
		errorBucketService.forecastErrorBucket(agentDataMap);
		long forecastEndTime = System.currentTimeMillis();

		timeTrackerPerisitence.setForecastTimeMs(forecastEndTime - buketizerEndTime);
		timeTrackerPerisitence.setProjectCompletionTimeMs(forecastEndTime - splunkStartTime);
		timeTrackerPerisitence.setApplicationEndDateTime(new Date().toString());
		timeTrackerRepo.saveTimeAudit(timeTrackerPerisitence);
		System.out.println(new Date() + " ^^Done Processing..." + groupName);
	}
	@Scheduled(initialDelay = 5000, fixedDelay = 900000)
	public void startJuggernautAnalysis() {
		if (masterElection.getMasterStatus()) {			
			//jnAnalysisAttachmentService.process(new Object());
		}
	}

	/*
	 * @Scheduled(cron = "${hourly-cron}")
	 * 
	 * public void bugImpactUpdation() { List<BugTracker> bugs =
	 * forecastRepository.getListOfAnalyserID(); bugImpactUpdation.setBugs(bugs);
	 * bugImpactUpdation.execute();
	 * 
	 * }
	 */
	@Scheduled(initialDelay = 5000, fixedRate = 7200000)
	public void sendAgentsForAnalysis() throws IOException, TaskSubmitException {
		System.out.println("Inside sendAgentsForAnalysis");
		if (masterElection.isMaster()) {
			List<String> listOfgroup = processGroupRepo.getAllGroupNames();
			System.out.println("Group Size"+listOfgroup.size());

			for (String groupName : listOfgroup) {
				String mpm = "errorstats" + Level.ONE.name() + groupName;
				taskProducerPro.send(groupName.getBytes(), mpm, Level.ONE);
				System.out.println(String.format("#### SENT MESSAGE TO KAFKA -> LEVEL: %s  *****   GROUP: %s",
						Level.ONE.name(), groupName));
			}
		}
	}

	@Override
	public void process(Task arg0) {
		String marker = arg0.getPayloadMagicMarker();
		byte b[] = arg0.getPayload();
		String groupName = new String(b);
		System.out.println(
				String.format("#### STARTED -----> LEVEL: %s  *****   GROUP: %s", Level.ONE.name(), groupName));
		List<String> listOfAgent = processGroupRepo.searchAgentListByGroup(groupName);
		// setAgent(agentList);
		try {
			Executors.newSingleThreadExecutor().submit(() -> {
				try {
				//	doProcessing(listOfAgent, groupName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).get(90L, TimeUnit.MINUTES);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.out.println("Oh No!!!!!!!Not Completed in 2hr..."+e);
		}
		System.out.println(
				String.format("#### COMPLETED -----> LEVEL: %s  *****   GROUP: %s", Level.ONE.name(), groupName));
	}

	@Override
	public Level forTopic() {
		return Level.ONE;
	}

	public void setAgent(String agent) {
		agentLists = agent;
	}

}
