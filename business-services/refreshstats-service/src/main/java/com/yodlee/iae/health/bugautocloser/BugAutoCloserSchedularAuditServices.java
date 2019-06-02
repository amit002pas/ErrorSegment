/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautFinalResponse;
import com.yodlee.health.errorsegment.gateway.jn.JNTokenGenerator;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.iae.health.autoclose.BugAutoCloserAudit;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;
import com.yodlee.iae.health.autoclose.RequestResponseDetails;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;
import com.yodlee.iae.health.util.ReadAutoCloseConfig;

/**
 * @author vchhetri
 *
 */
@Named
@Scope("prototype")
public class BugAutoCloserSchedularAuditServices extends ServiceIO {

	@Inject
	private BugAutoCloserAuditRepo bugAutoCloserAuditRepo;

	@Inject
	private AutoCloseRepository bugAutoCloserRepo;

	@Inject
	private JNTokenGenerator tokenGenerator;

	@Inject
	private ReadAutoCloseConfig readAutoCloseConfig;

	@Inject
	private JuggernautGateway juggerNautGateway;

	@Value("${user-username}")
	private String jnUserName;

	@Value("${password-password}")
	private String jnPassword;

	private static final String SUCCESS = "success";
	private static final String COMPLETED = "COMPLETED";
	private static final int ONE = 1;
	Logger logger = LoggerFactory.getLogger(BugAutoCloserSchedularAuditServices.class);

	/**
	 * Method to save bugs for audit
	 * 
	 * @param jobList
	 */
	private BugAutoCloserAudit bugAutoCloserAudit;
	BugAutoCloserSchedularJob bugAutoCloserSchedularJob;

	@Override
	public void accept(Object t) {
		bugAutoCloserAudit = (BugAutoCloserAudit) t;
		bugAutoCloserSchedularJob = bugAutoCloserRepo.getById(bugAutoCloserAudit.getSyntheticBugId());
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void executeImpl() {
		logger.info("processing auto close audit : " + bugAutoCloserAudit.getAnalysisId() + " : synid :"
				+ bugAutoCloserAudit.getSyntheticBugId());
		if (null != bugAutoCloserSchedularJob) {
			RequestResponseDetails requestResponseDetails = getStatusWithAnalysisId(bugAutoCloserAudit);
			postProcessing(bugAutoCloserAudit, requestResponseDetails, bugAutoCloserSchedularJob);
		}
	}

	@Override
	public void mapInput() {

	}

	@Override
	public void mapOutput() {

	}

	@Override
	public void validate() {

	}

	public void saveBugAutoCloserAudit(List<BugAutoCloserSchedularJob> jobList) {
		jobList.forEach(job -> {
			if (null != job.getAnalysisDetails() && !job.getAnalysisDetails().isEmpty()) {
				boolean isExist = bugAutoCloserAuditRepo.isExistCheckById(
						job.getAnalysisDetails().get(job.getAnalysisDetails().size() - 1).getAnalysisId());
				if (!isExist) {
					BugAutoCloserAudit bugAutoCloserAudit = new BugAutoCloserAudit();
					bugAutoCloserAudit.setAnalysisId(
							job.getAnalysisDetails().get(job.getAnalysisDetails().size() - 1).getAnalysisId());
					bugAutoCloserAudit.setSyntheticBugId(job.getSyntheticBugId());
					bugAutoCloserAudit.setReqSentTime(
							job.getAnalysisDetails().get(job.getAnalysisDetails().size() - 1).getAnalysisStartTime());
					bugAutoCloserAudit.setNextPickTime(DateUtils.addMinutes(
							job.getAnalysisDetails().get(job.getAnalysisDetails().size() - 1).getAnalysisStartTime(),
							readAutoCloseConfig.getBugAutoCloserConfigModel(job.getErrorCode()).getRetryInterval()));
					bugAutoCloserAudit.setStatus(job.getStatus());
					bugAutoCloserAudit.setClosedByChiron(false);
					bugAutoCloserAuditRepo.saveBugAutoCloserAudit(bugAutoCloserAudit);
				}
			}
		});

	}

	/**
	 * Method to get response from JN with analysis ID
	 * 
	 * @param job
	 * @return RequestResponseDetails to save response from JN
	 */
	public RequestResponseDetails getStatusWithAnalysisId(BugAutoCloserAudit job) {
		String token = tokenGenerator.generateToken(GatewayConstants.IATAuthenticationURL, jnUserName, jnPassword);
		RequestResponseDetails requestResponseDetails = new RequestResponseDetails();
		requestResponseDetails.setReqSentTime(new Date());
		List<JuggerNautFinalResponse> juggerNautFinalResponseList = juggerNautGateway
				.fetchIATResponseList(job.getAnalysisId(), token);
		requestResponseDetails.setResRecTime(new Date());
		if (null != juggerNautFinalResponseList && juggerNautFinalResponseList.size() == 1) {
			JuggerNautFinalResponse juggerNautFinalResponse = juggerNautFinalResponseList
					.get(juggerNautFinalResponseList.size() - 1);
			requestResponseDetails.setStatus(juggerNautFinalResponse.getStatus());
			requestResponseDetails.setResponseAction(juggerNautFinalResponse.getOutcome().getAction());
			requestResponseDetails.setResponseMessage(juggerNautFinalResponse.getAnalysisDetails().toString());
		} else if (null != juggerNautFinalResponseList && juggerNautFinalResponseList.size() > 1) {
			BugAutoCloserSchedularJob bugAutoCloserSchedularJob = bugAutoCloserRepo.getById(job.getSyntheticBugId());
			for (JuggerNautFinalResponse juggerNautFinalResponse : juggerNautFinalResponseList) {
				if (juggerNautFinalResponse.getAdditionalDetail().getAgentName()
						.equalsIgnoreCase(bugAutoCloserSchedularJob.getAgentName())) {
					requestResponseDetails.setStatus(juggerNautFinalResponse.getStatus());
					requestResponseDetails.setResponseAction(juggerNautFinalResponse.getOutcome().getAction());
					requestResponseDetails.setResponseMessage(juggerNautFinalResponse.getAnalysisDetails().toString());
					break;
				}
			}
		}
		return requestResponseDetails;
	}

	/**
	 * Method to process bugs after geting response from JN
	 * 
	 * @param auditBug
	 * @param requestResponseDetails
	 * @param bugAutoCloserSchedularJob
	 */
	public void postProcessing(BugAutoCloserAudit auditBug, RequestResponseDetails requestResponseDetails,
			BugAutoCloserSchedularJob bugAutoCloserSchedularJob) {
		boolean synchFlag = false;
		JobStatus JobStatusForBugAutoCloser = null;
		if (null != auditBug.getRequestResponseDetails()) {
			auditBug.getRequestResponseDetails().add(requestResponseDetails);
		} else {
			auditBug.setRequestResponseDetails(Arrays.asList(requestResponseDetails));
		}
		if (null != requestResponseDetails.getResponseAction()
				&& "Update_SR".equals(requestResponseDetails.getResponseAction())) {
			auditBug.setStatus(JobStatus.SUCCESS);
			JobStatusForBugAutoCloser = JobStatus.SUCCESS;
			synchFlag = true;
		} else {
			if (auditBug.getRequestResponseDetails().size() >= readAutoCloseConfig
					.getBugAutoCloserConfigModel(bugAutoCloserSchedularJob.getErrorCode()).getRetryCount() 
					|| requestResponseDetails.getStatus().equals(COMPLETED)) {
				auditBug.setStatus(JobStatus.FAILURE);
				JobStatusForBugAutoCloser = JobStatus.FAILURE;
				synchFlag = true;
			} else {
				auditBug.setNextPickTime(DateUtils.addMinutes(new Date(), readAutoCloseConfig
						.getBugAutoCloserConfigModel(bugAutoCloserSchedularJob.getErrorCode()).getRetryInterval()));
			}
		}
		bugAutoCloserAuditRepo.saveBugAutoCloserAudit(auditBug);
		if (synchFlag) {
			bugAutoCloserAndAuditSync(auditBug, bugAutoCloserSchedularJob, JobStatusForBugAutoCloser);
		}
	}

	/**
	 * Method to sync status with main scheduler
	 * 
	 * @param auditBug
	 * @param bugAutoCloserSchedularJob
	 * @param status
	 */
	private void bugAutoCloserAndAuditSync(BugAutoCloserAudit auditBug,
			BugAutoCloserSchedularJob bugAutoCloserSchedularJob, JobStatus status) {
		if (null != bugAutoCloserSchedularJob) {
			if (JobStatus.SUCCESS.getName().equals(status.getName())) {
				bugAutoCloserSchedularJob.setStatus(status);
				updateChiron(auditBug.getSyntheticBugId(), auditBug.getAnalysisId(), auditBug.getStatus());
			} else if (JobStatus.FAILURE.getName().equals(status.getName())) {
				if (bugAutoCloserSchedularJob.getRetryCount() >= readAutoCloseConfig
						.getBugAutoCloserConfigModel(bugAutoCloserSchedularJob.getErrorCode()).getRetryCount()) {
					bugAutoCloserSchedularJob.setStatus(JobStatus.FAILURE);
					updateChiron(auditBug.getSyntheticBugId(), auditBug.getAnalysisId(), JobStatus.FAILURE);
				} else {
					bugAutoCloserSchedularJob.setNextScheduledTime(DateUtils.addMinutes(new Date(), readAutoCloseConfig
							.getBugAutoCloserConfigModel(bugAutoCloserSchedularJob.getErrorCode()).getRetryInterval()));
					bugAutoCloserSchedularJob.setStatus(JobStatus.READY);
				}
			}
			bugAutoCloserRepo.saveBugDetail(bugAutoCloserSchedularJob);
		}
	}

	/**
	 * method to update chiron.
	 * 
	 * @param job
	 * @throws JSONException
	 */
	public void updateChiron(String bugId, String analysisId, JobStatus status) {
		JSONObject obj = new JSONObject();
		try {
			if (status.getName().equals(JobStatus.SUCCESS.getName())) {
				obj.put("cf_workflow_status", "Closed");
				obj.put("bug_status", "RESOLVED");
				obj.put("resolution", "INVALID");
				obj.put("comment", "Closing, After orphic analysis");
				obj.put("progressStatus", status.getName());
			} else if (JobStatus.INPROGRESS.getName().equals(status.getName())) {
				obj.put("progressStatus", JobStatus.INPROGRESS.getName());
				obj.put("comment", "Bug is in analysis by Orphic");
			} else if (JobStatus.FAILURE.getName().equals(status.getName())) {
				obj.put("progressStatus", JobStatus.FAILURE.getName());
				obj.put("comment", "Analysis failed by Orphic");
			}
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(obj.toString(), header);
			String url = GatewayConstants.BUGR_UPDATE_BUG_SYNTHETIC_URL;
			url = url.replace("INPUT_BUGID", bugId);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			JSONObject json = new JSONObject(responseBody);
			Object resStatus = json.get("status");
			if (null != resStatus && SUCCESS.equalsIgnoreCase(resStatus.toString())
					&& status.getName().equals(JobStatus.SUCCESS.getName())) {
				logger.info("updateChiron success case");
				BugAutoCloserAudit bugAutoCloserAudit = bugAutoCloserAuditRepo.getBugAutoCloserAuditById(analysisId);
				bugAutoCloserAudit.setClosedByChiron(true);
				bugAutoCloserAuditRepo.saveBugAutoCloserAudit(bugAutoCloserAudit);
			}
		} catch (HttpClientErrorException httpEx) {

		} catch (JSONException e) {

		}
	}

}
