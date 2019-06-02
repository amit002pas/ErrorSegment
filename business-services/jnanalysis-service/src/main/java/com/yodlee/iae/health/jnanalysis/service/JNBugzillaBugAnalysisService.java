/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.iae.health.jnanalysis.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.yodlee.health.errorsegment.datatypes.BugFetchResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponseEnum;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.RequestBugUpdate;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.jnanalysis.steps.ServiceIO;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;

@Named
public class JNBugzillaBugAnalysisService extends ServiceIO {

	@Inject
	JuggernautGateway juggernautGateway;

	@Inject
	JuggernautRepository juggernautRepository;
	
	@Inject
	BugzillaGateway bugzillaGateway;

	private String bugId;
	private String requestSource = JuggernautAnalysisConstants.SINGLE;
	private String requestStatus;

    @Override
	public void accept(Object arg0) {
		this.bugId = (String) arg0;		
	}
	
	@Override
	public void executeImpl() {
		if (!isJNAlreadyRunningForBug(bugId)) {
			JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
			juggerNautrequest.setItemID(bugId);
			juggerNautrequest.setItemType("2");
			juggerNautrequest.setCobrandID("");
			juggerNautrequest.setCreateBug(false);

			String securityToken = juggernautGateway.getSecurityToken();
			String analysisRequestId = juggernautGateway.getAnalysisRequestId(securityToken, juggerNautrequest);
			if (analysisRequestId.equals(JuggernautAnalysisConstants.NOT_VALID))
				this.requestStatus = JuggernautAnalysisConstants.INVALID_REQUEST;
			else{
				this.requestStatus = JuggernautAnalysisConstants.TRIGGERED;				
				RequestBugUpdate requestBugUpdate = new RequestBugUpdate();
				requestBugUpdate.setJnAnalysisId(analysisRequestId);
				requestBugUpdate.setComment("Updating JN Analysis Id");
				bugzillaGateway.updateBugField(bugId, requestBugUpdate);
			}
			JNAnalysisTriggeredItem jnAnalysisTriggeredItem = new JNAnalysisTriggeredItem();
			jnAnalysisTriggeredItem.setBugId(bugId);
			jnAnalysisTriggeredItem.setStatus(JuggernautAnalysisConstants.IN_PROGRESS);
			jnAnalysisTriggeredItem.setAnalysisRequestId(analysisRequestId);
			jnAnalysisTriggeredItem.setRequestSource(this.requestSource);
			jnAnalysisTriggeredItem.setDate(new Date());
			
			juggernautRepository.updateBugStatus(jnAnalysisTriggeredItem);
		} 
		else
			this.requestStatus = JuggernautAnalysisConstants.IN_PROGRESS;
	}

	
	private boolean isJNAlreadyRunningForBug(String bugId) {
		boolean isJNRunning;
		List<JNAnalysisTriggeredItem> triggeredItemsList = juggernautRepository
				.getJNAnalysisTriggeredItem(bugId);
		if (triggeredItemsList != null && !triggeredItemsList.isEmpty()	
				&& triggeredItemsList.get(0).getStatus().equals(JuggernautAnalysisConstants.IN_PROGRESS))
			isJNRunning = true;
		else
			isJNRunning = false;
		return isJNRunning;
	}
	
	@Override
	public Object get() {
		JNAnalysisResponse jnAnalysisResponse = new JNAnalysisResponse();
		if (requestStatus.equals(JuggernautAnalysisConstants.TRIGGERED)){
			jnAnalysisResponse.setAnalysisResponse(JNAnalysisResponseEnum.Triggered);
			jnAnalysisResponse.setStatus(JuggernautAnalysisConstants.SUCCESS);
		} else if(requestStatus.equals(JuggernautAnalysisConstants.IN_PROGRESS)) {
			jnAnalysisResponse.setAnalysisResponse(JNAnalysisResponseEnum.InProgress);
			jnAnalysisResponse.setStatus(JuggernautAnalysisConstants.SUCCESS);
		} else{
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