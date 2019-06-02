/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorbucket.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.common.services.exception.ServiceException;
import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.datatypes.refresh.RefreshStats;
import com.yodlee.iae.health.errorbucket.IErrorBucketService;
import com.yodlee.iae.health.errorbucket.steps.IssueAggregatorStep;
import com.yodlee.iae.health.errorbucket.steps.IssueAnalyzerStep;
import com.yodlee.iae.health.errorsegment.ErrorStatsAudit;
import com.yodlee.iae.health.forecast.BugTracker;
import com.yodlee.iae.health.forecast.steps.ForecasterStep;
import com.yodlee.iae.health.gateway.splunk.SplunkServiceImpl;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;

@Named
public class ErrorBucketServiceImpl implements IErrorBucketService {

	@Inject
	private SplunkServiceImpl splunkServiceImpl;

	@Inject
	private ProcessingTimeAuditRepository timeTrackerRepo;

	@Inject
	private IErrorBucketService errorBucketServiceObj;
	@Inject
	private IssueAggregatorStep issueAggregator;
	@Inject
	private IssueAnalyzerStep issueAnalyzerStep;
	@Inject
	private ForecastRepository forecastRepository;
	
	@Inject
	ForecasterStep forecasterStep;

	@Override
	public RefreshStats getErrorBucketDataFromSplunk(List<String> agentList, String groupName)
			throws Exception {
		RefreshStats refreshStatsSplunkData=new RefreshStats();
		List<ItemResponseSplunk> refreshStats = null;
		ErrorStatsAudit auditTracker = (ErrorStatsAudit) timeTrackerRepo.getInstanceLastAuditData(groupName,
				ErrorStatsAudit.class);
		Date startTime = auditTracker.getEndTime();
		Date endTime = new Date(System.currentTimeMillis()-1800*1000);
		ErrorStatsAudit auditStatsTracker = new ErrorStatsAudit();
		auditStatsTracker.setStartTime(startTime);
		auditStatsTracker.setEndTime(endTime);
		auditStatsTracker.setGroupName(groupName);
		String listOfAgents = String.join(",", agentList);
		refreshStatsSplunkData.setStartTime(startTime);
		refreshStatsSplunkData.setEndTime(endTime);

		try {
			refreshStats = splunkServiceImpl.getCIIAndMSAStatsForAgents(listOfAgents, startTime, endTime);
		} catch (Exception e) {
		}
		if (null != refreshStats) {
			auditStatsTracker.setCreatedAt(new Date());
			timeTrackerRepo.insertLatestAudit(auditStatsTracker);
			refreshStatsSplunkData.setItemLevelData(refreshStats);
		}
		System.out.println("SplunkSize:"+refreshStats.size());
		return refreshStatsSplunkData;
	}

	public Map<String, List<Bucket>> getHealth(List<String> agentList, String groupName) throws Exception {
		System.out.println(new Date()+" Started Processing for "+groupName);
		Map<String, List<Bucket>> agentDataMap = null;
		RefreshStats refreshData = errorBucketServiceObj.getErrorBucketDataFromSplunk(agentList, groupName);
		Object segmentizeBucket = issueAggregator.process(refreshData);
		agentDataMap=(Map<String, List<Bucket>>) issueAnalyzerStep.process(segmentizeBucket);
		System.out.println(new Date()+" Done Bucket Processing for "+groupName);
		return agentDataMap;
	}

	@Override
	public void forecastErrorBucket(Map<String, List<Bucket>> agentDataMap) throws ServiceException {
		System.out.println("Total Agent Refreshed:"+agentDataMap.size());
		for(String agentName:agentDataMap.keySet()) {
			System.out.println("AgentName:"+agentName+" "+agentDataMap.get(agentName).size());
			if(agentDataMap.get(agentName).size()>0)
				forecasterStep.process(agentDataMap.get(agentName));
			
		}
		
	}

	@Override
	public void bugImpactUpdation() {
		
		List<BugTracker> bug=forecastRepository.getListOfAnalyserID();
		
		
	}

}
