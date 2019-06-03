/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.schedular;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserSchedularAuditServices;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserServiceImpl;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;

/**
 * @author vchhetri and mkumar10
 *
 */
@Named
@EnableAutoConfiguration
@EnableScheduling
public class BugAutoCloserSchedular {

	@Inject
	private BugAutoCloserAuditRepo bugAutoCloserAuditRepo;
	@Inject
	private AutoCloseRepository autoCloseRepository;
	@Inject
	private BugAutoCloserSchedularAuditServices bugAutoCloserSchedularAuditServices;
	@Inject
	private BugAutoCloserServiceImpl bugAutoCloserService;

	Logger logger = LoggerFactory.getLogger(BugAutoCloserSchedular.class);

	/**
	 * scheduler to analyze all ready status bug.
	 */
	@Scheduled(fixedDelay = 30 * 60 * 1000)
	public void scheduleAutoCloserBugs() {
		logger.info("^^^Inside scheduleAutoCloserBugs");
		ExecutorService executor = Executors.newFixedThreadPool(4);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				bugAutoCloserService.process(autoCloseRepository.getBugList());
			}
		});
	}

	/**
	 * Scheduler to audit and synch/tracking of synthetic bugs.
	 */
	@Scheduled(fixedDelay = 30 * 60 * 1000)
	public void BugAutoCloserAudit() {
		logger.info("^^^Inside BugAutoCloserAudit");
		List<BugAutoCloserSchedularJob> mainBugList = autoCloseRepository.getJobsForAuditing();
		bugAutoCloserSchedularAuditServices.saveBugAutoCloserAudit(mainBugList);
		List<BugAutoCloserAudit> bugAutoCloserAuditList = bugAutoCloserAuditRepo.getWaitingStatusAuditDoc();

		ExecutorService executor = Executors.newFixedThreadPool(4);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				bugAutoCloserAuditList.forEach(bugAudit -> {
					bugAutoCloserSchedularAuditServices.process(bugAudit);
				});
			}
		});
		List<BugAutoCloserAudit> auditBugList = bugAutoCloserAuditRepo.getSuccessAndNotClosedByChironBugs();
		auditBugList.forEach(BugAutoCloserAudit -> {
			bugAutoCloserSchedularAuditServices.updateChiron(BugAutoCloserAudit.getSyntheticBugId(),
					BugAutoCloserAudit.getAnalysisId(), BugAutoCloserAudit.getStatus());
		});
	}
}
