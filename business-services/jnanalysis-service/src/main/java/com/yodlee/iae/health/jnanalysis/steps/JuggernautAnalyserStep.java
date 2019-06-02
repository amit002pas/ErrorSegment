/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.jnanalysis.steps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;

import com.yodlee.health.errorsegment.datatypes.Bug;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.RequestBugUpdate;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;

@Named
@Scope("prototype")
public class JuggernautAnalyserStep extends ServiceIO {

	Logger logger = LoggerFactory.getLogger(JuggernautAnalyserStep.class);

	@Inject
	JuggernautGateway juggernautGateway;

	@Inject
	JuggernautRepository juggernautRepository;

	@Inject
	MongoOperations mongoOps;

	@Inject
	BugzillaGateway bBugzillaGateway;

	private List<Bug> bugList;

	private String requestSource;

	private String requestStatus;

	public void setInput(List<Bug> bugList, String requestSource) {
		this.bugList = bugList;
		this.requestSource = requestSource;
	}

	@Override
	public void accept(Object arg0) {
		bugList = new ArrayList<Bug>();
		Map<String, Object> input = (Map<String, Object>) arg0;
		bugList = (List<Bug>) input.get(JuggernautAnalysisConstants.BUG);
		this.requestSource = (String) input.get(JuggernautAnalysisConstants.REQUEST_TYPE);
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return this.requestStatus;
	}

	@Override
	public void executeImpl() {

		for (Bug bugDetails : bugList) {

			String bugId = bugDetails.getSyntheticBugid();
			String summary = bugDetails.getBugFields().getSummary();
			String cobrandId = summary.substring(summary.indexOf(JuggernautAnalysisConstants.COBRAND_ID) + 11);
			String component = bugDetails.getBugFields().getComponent();
			String product = bugDetails.getBugFields().getProduct();
			String version = bugDetails.getBugFields().getVersion();
			String itemId = bugDetails.getBugFields().getMemitem();// getParameterMap().getCf_memitem();
			String itemType = JuggernautAnalysisConstants.FOUR;
			if (itemId.equals("-1")) {
				itemId = bugDetails.getBugFields().getMemSiteAccId();
				itemType = JuggernautAnalysisConstants.FIVE;

			}
			JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
			juggerNautrequest.setCobrandID(cobrandId);
			juggerNautrequest.setCreateBug(false);
			juggerNautrequest.setItemID(itemId);
			juggerNautrequest.setItemType(itemType);

			String securityToken = juggernautGateway.getSecurityToken();

			String analysisRequestId = juggernautGateway.getAnalysisRequestId(securityToken, juggerNautrequest);
			if (analysisRequestId.equals(JuggernautAnalysisConstants.NOT_VALID)) {
				this.requestStatus = JuggernautAnalysisConstants.INVALID_REQUEST;
				continue;
			} else {
				System.out.println("Updating JN Analysis:" + analysisRequestId);
				this.requestStatus = JuggernautAnalysisConstants.STARTED;
				RequestBugUpdate requestBugUpdate = new RequestBugUpdate();
				requestBugUpdate.setJnAnalysisId(analysisRequestId);
				requestBugUpdate.setComment("Updating JN Analysis Id");
				bBugzillaGateway.updateBugField(bugId, requestBugUpdate);
			}

			JNAnalysisTriggeredItem analysisTriggeredItem = new JNAnalysisTriggeredItem();
			analysisTriggeredItem.setBugId(bugId);
			analysisTriggeredItem.setStatus(JuggernautAnalysisConstants.IN_PROGRESS);
			analysisTriggeredItem.setBugSummary(summary);
			analysisTriggeredItem.setAnalysisRequestId(analysisRequestId);
			analysisTriggeredItem.setSecurityToken(securityToken);
			analysisTriggeredItem.setDate(new Date());
			analysisTriggeredItem.setRequestSource(this.requestSource);
			analysisTriggeredItem.setProduct(product);
			analysisTriggeredItem.setComponent(component);
			analysisTriggeredItem.setVersion(version);
			juggernautRepository.saveAnalyzerId(analysisTriggeredItem);

		}

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
