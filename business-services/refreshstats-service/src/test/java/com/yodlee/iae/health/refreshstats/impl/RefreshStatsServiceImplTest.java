/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.refreshstats.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.datatypes.refresh.AgentStats;
import com.yodlee.iae.health.gateway.splunk.SplunkServiceImpl;
import com.yodlee.iae.health.refresh.RefreshStatsAudit;
import com.yodlee.iae.health.repository.ProcessingTimeAuditRepository;
import com.yodlee.iae.health.repository.refresh.RefreshDataRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class RefreshStatsServiceImplTest {

	@InjectMocks
	RefreshStatsServiceImpl refreshStatsServiceImpl;
	
	@Mock
	SplunkServiceImpl splunkServiceImpl;
	@Mock
	ProcessingTimeAuditRepository timeTrackerRepo;
	@Mock
	RefreshDataRepository refreshDataRepository;

	@Test
	public void test() throws Exception{
		List<String> agentList = new ArrayList<>();
		agentList.add("Chase");
		agentList.add("Wells");
		String groupName = "g1";
		RefreshStatsAudit auditTracker = new RefreshStatsAudit();
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(new Date());
		calEnd.add(Calendar.MINUTE, -120);
		auditTracker.setEndTime(calEnd.getTime());
		Mockito.when(timeTrackerRepo.getInstanceLastAuditData("g1", RefreshStatsAudit.class)).thenReturn(auditTracker);
		List<AgentStats> itemResponseSplunkRefresh = new ArrayList<>();
		String listOfAgents = String.join(",", agentList);
		
		Mockito.when(splunkServiceImpl.getRefreshStatsForAgents(listOfAgents,new Date(),new Date())).thenReturn(itemResponseSplunkRefresh);
		refreshStatsServiceImpl.refreshStats(agentList, groupName);
	}
}
