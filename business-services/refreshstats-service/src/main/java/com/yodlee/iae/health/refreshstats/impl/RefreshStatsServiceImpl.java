/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.refreshstats.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.yodlee.iae.health.datatypes.refresh.AgentStats;
import com.yodlee.iae.health.gateway.splunk.SplunkServiceImpl;
import com.yodlee.iae.health.refresh.RefreshStatsAudit;
import com.yodlee.iae.health.refresh.RefreshStatsCollection;
import com.yodlee.iae.health.refreshstats.IRefreshStatsService;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;
import com.yodlee.iae.health.repository.refresh.RefreshDataRepository;

/***
 * This class will get the time interval for which we need to hit the splunk and
 * post that make changes in DB
 * 
 * @author akumar23
 *
 */
@Named
public class RefreshStatsServiceImpl implements IRefreshStatsService {

	@Inject
	SplunkServiceImpl splunkServiceImpl;
	@Inject
	ProcessingTimeAuditRepository timeTrackerRepo;
	@Inject
	RefreshDataRepository refreshDataRepository;

	@Override
	public void refreshStats(List<String> agentList, String groupName) throws Exception {

		RefreshStatsAudit auditTracker = (RefreshStatsAudit) timeTrackerRepo.getInstanceLastAuditData(groupName,
				RefreshStatsAudit.class);
		Date startTime = auditTracker.getEndTime();
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(auditTracker.getEndTime());
		calEnd.add(Calendar.MINUTE, 30);
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.add(Calendar.MINUTE, -45);
		while (calEnd.compareTo(calCurrent) <= 0) {
			Date endTime = calEnd.getTime();
			RefreshStatsAudit auditStatsTracker = new RefreshStatsAudit();
			auditStatsTracker.setStartTime(startTime);
			auditStatsTracker.setEndTime(endTime);
			auditStatsTracker.setGroupName(groupName);
			RefreshStatsCollection
			refreshStats = new RefreshStatsCollection();
			refreshStats.setStartTime(startTime);
			String listOfAgents = String.join(",", agentList);
			List<AgentStats> itemResponseSplunkRefresh = splunkServiceImpl.getRefreshStatsForAgents(listOfAgents,
					startTime, endTime);
			if (null != itemResponseSplunkRefresh) {
				
				refreshStats.setEndTime(endTime);
				refreshStats.setCreatedDate(new Date());
				refreshStats.setAgentStatsList(itemResponseSplunkRefresh);
				auditStatsTracker.setCreatedAt(new Date());
				refreshDataRepository.insertAuditRefreshData(refreshStats);
				timeTrackerRepo.insertLatestAudit(auditStatsTracker);
			}
			calEnd.add(Calendar.MINUTE, 30);

			startTime = endTime;
		}
		System.out.println("Done with Insertion");
	}

}
