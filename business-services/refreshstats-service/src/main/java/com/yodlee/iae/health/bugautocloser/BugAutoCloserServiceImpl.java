/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.BugFetchResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.gateway.jn.JNTokenGenerator;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.iae.health.autoclose.AnalysisDetails;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.forecast.steps.BucketThreshold;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.util.ReadAutoCloseConfig;

/**
 * @author mkumar10
 *
 */
@Named
@Scope("prototype")
public class BugAutoCloserServiceImpl extends ServiceIO {

	@Inject
	private BugzillaGateway bugzillaGateway;

	@Inject
	private AutoCloseRepository autoCloseRepository;

	@Inject
	private JNTokenGenerator jnTokenGenerator;
	@Inject
	private BugAutoCloserSchedularAuditServices bugAutoCloserSchedularAuditServices;
	@Inject
	private ReadAutoCloseConfig readAutoCloseConfig;

	@Value("${user-username}")
	private String jnUserName;

	@Value("${password-password}")
	private String jnPassword;

	public String analysisRequestID = "analysisRequestID";
	public String THREE = "3";
	public String FIVE = "5";
	List<BugAutoCloserSchedularJob> bugList;
	
	Logger logger=LoggerFactory.getLogger(BugAutoCloserServiceImpl.class);


	@SuppressWarnings("unchecked")
	@Override
	public void accept(Object t) {
		bugList = (List<BugAutoCloserSchedularJob>)t;

	}

	@Override
	public void validate() {
		
	}

	@Override
	public void executeImpl() {
		logger.info("Processing auto close bug: "+bugList.size());
		if(bugList == null || bugList.isEmpty())
			return;
		for(BugAutoCloserSchedularJob aucoCloseObj : bugList) {
			BugFetchResponse bugFetchResponse = bugzillaGateway.getBugDetails(aucoCloseObj.getSyntheticBugId());
			BugAutoCloserSchedularJob bugAutoCloserSchedularJob = autoCloseRepository.getById(aucoCloseObj.getSyntheticBugId());
			AnalysisDetails jnAnalysisDetails = new AnalysisDetails();
			List<AnalysisDetails> jnAnalysisList = bugAutoCloserSchedularJob.getAnalysisDetails();

			Response jnResponse = analyseThroughJN(bugFetchResponse);
			if(jnResponse!= null && jnResponse.getStatus() == GatewayConstants.SUCCESS_RESPONSE) {
				JSONObject jsonResponse;
					try {
						jsonResponse = new JSONObject(jnResponse.readEntity(String.class));
						jnAnalysisDetails.setAnalysisId(jsonResponse.get(analysisRequestID).toString());
					} catch (JSONException e) {
					}
				bugAutoCloserSchedularJob.setRetryCount(bugAutoCloserSchedularJob.getRetryCount()+1);
				bugAutoCloserSchedularJob.setStatus(JobStatus.INPROGRESS);				
				jnAnalysisDetails.setAnalysisStartTime(new Date());
				jnAnalysisList.add(jnAnalysisDetails);
				bugAutoCloserSchedularJob.setAnalysisDetails(jnAnalysisList);
				bugAutoCloserSchedularJob.setLastUpdDate(new Date());
				bugAutoCloserSchedularJob.setNextScheduledTime(DateUtils.addMinutes(new Date(), readAutoCloseConfig.getBugAutoCloserConfigModel(bugAutoCloserSchedularJob.getErrorCode()).getRetryInterval()));
				autoCloseRepository.saveBugDetail(bugAutoCloserSchedularJob);
				bugAutoCloserSchedularAuditServices.updateChiron(aucoCloseObj.getSyntheticBugId(), jnAnalysisDetails.getAnalysisId(), JobStatus.INPROGRESS);
			} else {
				logger.info("Failed analysis : "+aucoCloseObj.getSyntheticBugId());
				bugAutoCloserSchedularJob.setStatus(JobStatus.FAILURE);
				jnAnalysisDetails.setResponseMessage(jnResponse.readEntity(String.class));
				jnAnalysisList.add(jnAnalysisDetails);
				bugAutoCloserSchedularJob.setAnalysisDetails(jnAnalysisList);
				autoCloseRepository.saveBugDetail(bugAutoCloserSchedularJob);
				bugAutoCloserSchedularAuditServices.updateChiron(aucoCloseObj.getSyntheticBugId(), null, JobStatus.FAILURE);
			}

		}

	}
	
	/**
	 * Method to initiate analysis in JN
	 * @param bugFetchResponse
	 * @return Response
	 */
	public Response analyseThroughJN(BugFetchResponse bugFetchResponse) {
	       
	       String itemId;
	       String itemType;
	       Gson gson = new Gson();
	       String token = jnTokenGenerator.generateToken(GatewayConstants.IATAuthenticationURL, jnUserName,
	                     jnPassword);
	       logger.info("analyseThroughJN start for : "+bugFetchResponse.getBug().getSyntheticBugid());
	       JuggerNautInputRequest juggerNautInputRequest = null;
	       String summary = bugFetchResponse.getBug().getBugFields().getSummary();
	       String cobrandId = summary.substring(summary.lastIndexOf(":")+1, summary.length());
	       if(bugFetchResponse.getBug().getBugFields().getMemitem()!=null) {
	              itemId = bugFetchResponse.getBug().getBugFields().getMemitem();
	              itemType = THREE;
	       } else {
	              itemId = bugFetchResponse.getBug().getBugFields().getMemSiteAccId();
	              itemType = FIVE;
	       }
	       juggerNautInputRequest = new JuggerNautInputRequest(itemId, itemType, cobrandId,false);
	       String json = gson.toJson(juggerNautInputRequest);
	       String target_url = GatewayConstants.IATAnalysisURL;
	       Client client = ClientBuilder.newClient();
	       WebTarget target = client.target(target_url);
	       MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
	       multivaluedMap.add(GatewayConstants.HEADER_ACCEPT, GatewayConstants.HEADER_JSON_VALUE);
	       multivaluedMap.add(GatewayConstants.HEADER_CONTENT_TYPE, GatewayConstants.HEADER_JSON_VALUE);
	       multivaluedMap.add(GatewayConstants.HEADER_AUTHORIZATION, token);
	       logger.info("analyseThroughJN end for : "+bugFetchResponse.getBug().getSyntheticBugid());
	       return target.request().headers(multivaluedMap).post(Entity.json(json));

	}

	@Override
	public Object get() {
		return null;
	}


	@Override
	public void mapInput() {

	}

	@Override
	public void mapOutput() {

	}


}
