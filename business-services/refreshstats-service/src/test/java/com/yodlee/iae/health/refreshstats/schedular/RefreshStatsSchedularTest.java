/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.refreshstats.schedular;


import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.refreshstats.impl.RefreshStatsServiceImpl;
import com.yodlee.iae.health.repository.ProcessGroupRepository;
import com.yodlee.iae.vitarana.Task;
import com.yodlee.iae.vitarana.producers.TaskProducerPro;

@RunWith(SpringJUnit4ClassRunner.class)
public class RefreshStatsSchedularTest {

	@InjectMocks
	RefreshStatsSchedular refreshStatsSchedular;
	
	@Mock
	private TaskProducerPro taskProducerPro;

	@Mock
	Task task;

	@Mock
	ProcessGroupRepository processGroupRepository;

	@Mock
	private RefreshStatsServiceImpl refreshStatsServiceImpl;
	@Mock
	private MEComponent masterElection;

	@Test
	public void test() throws Exception{
	
		byte b[] = {12,22};
		String groupName = new String(b);
		Mockito.when(task.getPayload()).thenReturn(b);
		List<String> listOfAgent = new ArrayList<>();
		listOfAgent.add("weadsad");
		listOfAgent.add("asdasdads");
		Mockito.when(processGroupRepository.searchAgentListByGroup(groupName)).thenReturn(listOfAgent);
		Mockito.when(masterElection.getMasterStatus()).thenReturn(true);
		Mockito.when(processGroupRepository.getAllGroupNames()).thenReturn(listOfAgent);
		refreshStatsSchedular.forTopic();
		refreshStatsSchedular.process(task);
		refreshStatsSchedular.sendAgentsForStats();
		
	}
}
