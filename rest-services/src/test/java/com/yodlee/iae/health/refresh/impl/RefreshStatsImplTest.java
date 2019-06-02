/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.refresh.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.iae.health.refresh.RefreshStats;
import com.yodlee.iae.health.repository.RefreshStatsRepository;
import com.yodlee.iae.health.repository.exception.MyException;


@RunWith(SpringJUnit4ClassRunner.class)
public class RefreshStatsImplTest {

	@InjectMocks
	RefreshStatsImpl refreshStatsImpl;

	@Mock
	RefreshStatsRepository refreshStatsRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetRefreshStats() throws NullPointerException, MyException, ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.DATE_FORMATTER);
		
		String request = "{\"startTime\":\"2019-04-10 01:01:01\",\"endTime\":\"2019-04-10 01:01:01\",\"agentList\":[\"Chase\"]}";
		List<String> agentList = new ArrayList<>();
		agentList.add("Chase");
		List<RefreshStats> refreshStats = new ArrayList<>();
		RefreshStats refreshStat = new RefreshStats();
		refreshStat.set_id("123");
		refreshStat.setAgentcount(2222);
		refreshStat.setFailure(22);
		refreshStat.setInfracount(11);
		refreshStat.setSitecount(2);
		refreshStat.setSuccess(2000);
		refreshStat.setTotalRefresh(121212);
		refreshStat.setUarcount(12);
		RefreshStats refreshStat1 = new RefreshStats();
		refreshStat1.set_id("1234");
		refreshStat1.setAgentcount(1222);
		refreshStat1.setFailure(12);
		refreshStat1.setInfracount(11);
		refreshStat1.setSitecount(22);
		refreshStat1.setSuccess(1000);
		refreshStat1.setTotalRefresh(221212);
		refreshStat1.setUarcount(22);
		RefreshStats refreshStat2 = new RefreshStats();
		refreshStat2.set_id("1235");
		refreshStat2.setAgentcount(3222);
		refreshStat2.setFailure(32);
		refreshStat2.setInfracount(31);
		refreshStat2.setSitecount(32);
		refreshStat2.setSuccess(3000);
		refreshStat2.setTotalRefresh(321212);
		refreshStat2.setUarcount(312);
		
		refreshStats.add(refreshStat);
		Mockito.when(refreshStatsRepository.findRefreshStatsByTimeandAgents(sdf.parse("2019-04-10 01:01:01"),sdf.parse("2019-04-10 01:01:01"), agentList)).thenReturn(refreshStats);
		refreshStatsImpl.getRefreshStats(request);

	}
	
	@Test
	public void testGetRefreshStatsException() throws NullPointerException, MyException, ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.DATE_FORMATTER);
		
		String request = "{\"startTime\":\"121212\",\"endTime\":\"121212\",\"agentList\":[\"Chase\"]}";
		List<String> agentList = new ArrayList<>();
		agentList.add("Chase");
		List<RefreshStats> refreshStats = null;
		Mockito.when(refreshStatsRepository.findRefreshStatsByTimeandAgents(new Date(),new Date(), agentList)).thenReturn(refreshStats);
		refreshStatsImpl.getRefreshStats(request);

	}
	@Test
	public void testGetRefreshStatsNullException() throws NullPointerException, MyException, ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.DATE_FORMATTER);
		
		String request = "{\"startTime\":\"2019-04-10 01:01:01\",\"endTime\":\"2019-04-10 01:01:01\",\"agentList\":[\"Chase\"]}";
		List<String> agentList = new ArrayList<>();
		agentList.add("Chase");
		Mockito.when(refreshStatsRepository.findRefreshStatsByTimeandAgents(sdf.parse("2019-04-10 01:01:01"),sdf.parse("2019-04-10 01:01:01"), agentList)).thenThrow(new NullPointerException());
		refreshStatsImpl.getRefreshStats(request);

	}

	@Test
	public void testGetRefreshStatsMyexceptionException() throws NullPointerException, MyException, ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(GatewayConstants.DATE_FORMATTER);
		
		String request = "{\"startTime\":\"2019-04-10 01:01:01\",\"endTime\":\"2019-04-10 01:01:01\",\"agentList\":[\"Chase\"]}";
		List<String> agentList = new ArrayList<>();
		agentList.add("Chase");
		Mockito.when(refreshStatsRepository.findRefreshStatsByTimeandAgents(sdf.parse("2019-04-10 01:01:01"),sdf.parse("2019-04-10 01:01:01"), agentList)).thenThrow(new MyException("exception"));
		refreshStatsImpl.getRefreshStats(request);

	}
	
	@Test
	public void testGetRefreshStatsNull() throws NullPointerException, MyException{

		String request = "{\"startTime\":\"2019-04-10 01:01:01\",\"agentList\":[\"\"]}";
		refreshStatsImpl.getRefreshStats(request);
		String request1 = "{}";
		refreshStatsImpl.getRefreshStats(request1);
		String request2 = "{\"endTime\":\"2019-04-10 01:01:01\",\"agentList\":[\"Chase\"]}";
		refreshStatsImpl.getRefreshStats(request2);
		String request3 = "{\"startTime\":\"2019-04-10 01:01:01\",\"endTime\":\"Chase\"}";
		refreshStatsImpl.getRefreshStats(request3);

	}
}
