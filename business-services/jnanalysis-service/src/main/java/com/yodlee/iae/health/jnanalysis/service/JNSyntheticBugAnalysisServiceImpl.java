/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.jnanalysis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.yodlee.health.errorsegment.datatypes.Bug;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponseEnum;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.iae.common.services.ServiceBase;
import com.yodlee.iae.health.jnanalysis.steps.BugDetailsCollectorStep;
import com.yodlee.iae.health.jnanalysis.steps.JuggernautAnalyserStep;
import com.yodlee.iae.health.jnanalysis.steps.NewBugCollectorStep;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;

@Named
public class JNSyntheticBugAnalysisServiceImpl extends ServiceBase {

	@Inject
	NewBugCollectorStep newBugCollector;
	@Inject
	JuggernautAnalyserStep juggerNautAnalyser;

	@Inject
	BugDetailsCollectorStep bugDetailsCollector;

	@Inject
	MongoOperations mongoOps;
	@Inject
	JuggernautRepository jnRepository;

	private String outputMessage;

	private String bugId;

	public void setInput(String bugId) {
		this.bugId = bugId;
	}

	@Override
	public void executeImpl() {
		if (bugId != null) {
			bugDetailsCollector.process(bugId);
			List<Bug> bugDetails = (List<Bug>) bugDetailsCollector.get();
			if (!bugDetails.isEmpty()) {
				if (!isJNAlreadyRunningForBug(bugDetails.get(0).getSyntheticBugid())) {
					Map<String, Object> input = new HashMap<String, Object>();
					input.put(JuggernautAnalysisConstants.BUG, bugDetails);
					input.put(JuggernautAnalysisConstants.REQUEST_TYPE, JuggernautAnalysisConstants.SINGLE);
					juggerNautAnalyser.process(input);
					String requestStatus = (String) juggerNautAnalyser.get();
					if (requestStatus != null && requestStatus.equals(JuggernautAnalysisConstants.INVALID_REQUEST))
						outputMessage = requestStatus;
					else
						outputMessage = JuggernautAnalysisConstants.TRIGGERED;
				} else
					outputMessage = JuggernautAnalysisConstants.IN_PROGRESS;
			}
		} else {
			newBugCollector.process(null);
			List<Bug> bugList = (List<Bug>) newBugCollector.get();
			List<Bug> bugListAfterDedup = new ArrayList<Bug>();

			if (bugList != null && !bugList.isEmpty()) {
				for (Bug bug : bugList) {
					if (!isJNAlreadyRunningForBug(bug.getSyntheticBugid()))
						bugListAfterDedup.add(bug);
				}
			}
			if (!bugListAfterDedup.isEmpty()) {
				juggerNautAnalyser.setInput(bugListAfterDedup, JuggernautAnalysisConstants.BATCH);
				juggerNautAnalyser.execute();
				outputMessage = JuggernautAnalysisConstants.TRIGGERED;
			}
		}

	}

	private boolean isJNAlreadyRunningForBug(String bugId) {
		boolean isJNRunning;
		Query query = new Query();
		query.addCriteria(Criteria.where(JuggernautAnalysisConstants.BUG_ID).is(bugId));
		query.fields().include(JuggernautAnalysisConstants.BUG_ID);
		query.fields().include(JuggernautAnalysisConstants.STATUS);

		List<JNAnalysisTriggeredItem> triggeredItemsList = jnRepository.getJNAnalysisTriggeredItem(bugId);
		if (triggeredItemsList != null && !triggeredItemsList.isEmpty()
				&& triggeredItemsList.get(0).getStatus().equals(JuggernautAnalysisConstants.IN_PROGRESS))
			isJNRunning = true;
		else
			isJNRunning = false;

		return isJNRunning;
	}

	public JNAnalysisResponse getOutput() {
		JNAnalysisResponse jnAnalysisResponse = new JNAnalysisResponse();
		if (outputMessage.equals(JuggernautAnalysisConstants.TRIGGERED)) {
			jnAnalysisResponse.setAnalysisResponse(JNAnalysisResponseEnum.Triggered);
			jnAnalysisResponse.setStatus(JuggernautAnalysisConstants.SUCCESS);
		} else if (outputMessage.equals(JuggernautAnalysisConstants.IN_PROGRESS)) {
			jnAnalysisResponse.setAnalysisResponse(JNAnalysisResponseEnum.InProgress);
			jnAnalysisResponse.setStatus(JuggernautAnalysisConstants.SUCCESS);
		} else {
			jnAnalysisResponse.setAnalysisResponse(JNAnalysisResponseEnum.InvalidRequest);
			jnAnalysisResponse.setStatus(JuggernautAnalysisConstants.FAILURE);
		}
		return jnAnalysisResponse;
	}

	@Override
	public void mapInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapOutput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
