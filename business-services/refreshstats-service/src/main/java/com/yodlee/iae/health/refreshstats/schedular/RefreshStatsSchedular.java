/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.refreshstats.schedular;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.iae.health.refreshstats.impl.RefreshStatsServiceImpl;
import com.yodlee.iae.health.repository.ProcessGroupRepository;
import com.yodlee.iae.vitarana.ITaskConsumer;
import com.yodlee.iae.vitarana.Level;
import com.yodlee.iae.vitarana.Task;
import com.yodlee.iae.vitarana.exception.TaskSubmitException;
import com.yodlee.iae.vitarana.producers.TaskProducerPro;

/***
 * This class will be used as schedular to send and fetch data from kafka
 * 
 * @author akumar23
 *
 */
@Named
@Primary
public class RefreshStatsSchedular implements ITaskConsumer {

	@Inject
	private TaskProducerPro taskProducerPro;

	@Inject
	ProcessGroupRepository processGroupRepository;

	@Inject
	private RefreshStatsServiceImpl refreshStatsServiceImpl;
	@Inject
	private MEComponent masterElection;

	@Override
	public Level forTopic() {
		return Level.TWO;
	}

	/**
	 * It will get the message from kafka ,after that it will fetch agent name for
	 * that groups and do the processing
	 */
	@Override
	public void process(Task arg0) {
		byte b[] = arg0.getPayload();
		String groupName = new String(b);
		System.out.println(
				String.format("#### STARTED -----> LEVEL: %s  *****   GROUP: %s", Level.TWO.name(), groupName));
		List<String> listOfAgent = processGroupRepository.searchAgentListByGroup(groupName);
		if (null != listOfAgent) {
			try {
				refreshStatsServiceImpl.refreshStats(listOfAgent, groupName);
			} catch (Exception e) {
			}

		}
		System.out.println(
				String.format("#### COMPLETED -----> LEVEL: %s  *****   GROUP: %s", Level.TWO.name(), groupName));
	}

	/**
	 * It will send groupname to kafka every 30 min
	 * 
	 * @throws TaskSubmitException
	 */
	@Scheduled(fixedRate =  1800*1000, initialDelay = 2 * 60 * 1000)
	public void sendAgentsForStats() throws TaskSubmitException {
		if (masterElection.getMasterStatus()) {
			List<String> listOfgroup = processGroupRepository.getAllGroupNames();
			System.out.println(new Date() + " Second Topic Sending Data for " + listOfgroup.size());

			for (String groupName : listOfgroup) {
				String mpm = "refreshstats" + Level.TWO.name() + groupName;
				taskProducerPro.send(groupName.getBytes(), mpm, Level.TWO);
				System.out.println(String.format("#### SENT MESSAGE TO KAFKA -> LEVEL: %s  *****   GROUP: %s",
						Level.TWO.name(), groupName));

			}
		}
	}

}
