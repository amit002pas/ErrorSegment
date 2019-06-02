/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.CreateBugResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;
import com.yodlee.iae.health.util.BugzillaConstants;
import com.yodlee.iae.health.util.ForecastConstants;

/**
 * @author srai This is steps for bug creation and filling fields with
 *         appropriate details
 */
@Named
public class BugCreator implements ForecastStepsBase<SegmentedBucket, Integer, CreateBugResponse> {

	@Inject
	private BugzillaGateway bugzillaGateway;

	private CreateBugResponse generatedBug;

	private int countOfTotalRefresh;

	private SegmentedBucket segmentedBucket;

	@Inject
	private JuggernautRepository juggernautRepository;

	Logger logger = LoggerFactory.getLogger(BugCreator.class);

	@Value("${infra.error.codes}")
	private String infraErrorCodes;

	public static final String NOT_VALID = "Not Valid";

	@Override
	public void setInput(SegmentedBucket segmentedBucket, Integer countOfTotalRefresh) {
		this.segmentedBucket = segmentedBucket;
		this.countOfTotalRefresh = countOfTotalRefresh;
		this.generatedBug = new CreateBugResponse();
	}

	@Override
	public void execute() {

		Map<String, String> bugDetails = initializeBugDetails();
		JNAnalysisTriggeredItem jnTriggeredItem = new JNAnalysisTriggeredItem();
		try {
			this.generatedBug = bugzillaGateway.createBug(bugDetails);
			if(null!=segmentedBucket.getJuggernautDetails()) {
			System.out.println("Bug Created with jn id and route info : "
					+ segmentedBucket.getJuggernautDetails().getAnalysisId() + " : " + segmentedBucket.getRoute());
			jnTriggeredItem.setAnalysisRequestId(segmentedBucket.getJuggernautDetails().getAnalysisId());

			logger.info("Inside Step create Bug:{}", generatedBug);
			jnTriggeredItem.setBugId(generatedBug.getSyntheticBugId());
			jnTriggeredItem.setDate(new Date());
			jnTriggeredItem.setStatus("In Progress");
			juggernautRepository.saveAnalyzerId(jnTriggeredItem);
			}
		} catch (Exception e) {
			logger.info("ConnectionException >>>>" + e);
		}

	}

	/**
	 * 
	 * @return Map of Bug fields name and corresponding value
	 */
	private Map<String, String> initializeBugDetails() {

		Map<String, String> bugDetails = new HashMap<String, String>();
		bugDetails = new HashMap<String, String>();
		bugDetails.put(BugzillaConstants.BG_WORKFLOW_FIELD, "IAE");
		bugDetails.put(BugzillaConstants.BG_DEPARTMENT_FIELD, "IAE");
		bugDetails.put(BugzillaConstants.BG_ENVIRONMENT_FIELD, "Production");
		bugDetails.put(BugzillaConstants.BG_PLATFORM_FIELD, "All");
		bugDetails.put(BugzillaConstants.BG_COMPONENT_FIELD, "Agent");
		bugDetails.put(BugzillaConstants.BG_PRODUCT_FIELD, "Proactive Monitoring");
		bugDetails.put(BugzillaConstants.BG_VERSION_FIELD, "unspecified");
		bugDetails.put(BugzillaConstants.BG_OP_SYS_FIELD, "Windows");
		bugDetails.put(BugzillaConstants.BG_CF_BUGTYPE_FIELD, "Proactive");
		bugDetails.put(BugzillaConstants.BG_SOURCE_FIELD, "Preventive Fixes");
		bugDetails.put(BugzillaConstants.BG_STATUS_WHITEBOARD_FIELD, BugzillaConstants.PRESR_WHILEBOARD_KEY);
		bugDetails.put(BugzillaConstants.BG_CF_CUSTOMER_FIELD, "All");
		bugDetails.put(BugzillaConstants.BG_CF_PORTFOLIO_FIELD, "Proactive Monitoring");
		bugDetails.put(BugzillaConstants.BG_PRIORITY_FIELD, "P3");
		bugDetails.put(BugzillaConstants.CF_INITIATIVE, "IAE");
		bugDetails.put(BugzillaConstants.BG_CF_CREATE_BUGZILLA_BUG, "false");
		if (null!=segmentedBucket.getJuggernautDetails() && !NOT_VALID.equals(segmentedBucket.getJuggernautDetails().getAnalysisId()))
			bugDetails.put(BugzillaConstants.JN_ANALYSISID, segmentedBucket.getJuggernautDetails().getAnalysisId());

		bugDetails.put(BugzillaConstants.ROUTE, segmentedBucket.getRoute());
		double currentFailure = segmentedBucket.getItemList().size();
		double percentage = (currentFailure / countOfTotalRefresh) * 100;

		percentage = percentage * 100;

		percentage = Math.round(percentage);
		percentage = percentage / 100;
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String currentTimeStr = DATE_FORMAT.format(segmentedBucket.getEndTime());

		String predictedFailureString = null;
		if (segmentedBucket.getErrorCode().contains(infraErrorCodes)) {
			predictedFailureString = "Predicted Failure: " + "NA";
		} else {
			predictedFailureString = "Predicted Failure: " + segmentedBucket.getPredictedFailure();
		}
		int count = 0;

		bugDetails.put(BugzillaConstants.BG_IMPACT_FIELD,
				"Refresh Count: " + count + ";" + "Failure Count: " + segmentedBucket.getItemList().size() + ";"
						+ predictedFailureString + ";" + "Impact Percentage: " + percentage + ";" + "Updated At: "
						+ currentTimeStr);
		bugDetails.put("cf_workflow_status", "NEW");

		bugDetails.put(BugzillaConstants.CF_SUMINFO, segmentedBucket.getSumInfo());
		bugDetails.put(BugzillaConstants.CF_SITEID, segmentedBucket.getSiteId());
		bugDetails.put(BugzillaConstants.BG_AGENT_FIELD, segmentedBucket.getAgentName());
		bugDetails.put(BugzillaConstants.BG_CF_MEMITEM_FIELD, segmentedBucket.getTopFailure().getCacheItemId());
		bugDetails.put(BugzillaConstants.BG_ERRORCODE_FIELD, segmentedBucket.getErrorCode());
		bugDetails.put(BugzillaConstants.BG_SUMMARY_FIELD,
				"Proactive Monitoring Bugs created by PreSR AgentName: " + segmentedBucket.getAgentName()
						+ " ErrorCode:" + segmentedBucket.getErrorCode() + " COBRAND ID:"
						+ segmentedBucket.getTopFailure().getCobrandId());
		bugDetails.put(BugzillaConstants.BG_COMMENTS, populateDetails());

		Gson gson = new Gson();
		String jsonString = "";
		try {
			Map<String, String> segmentWiseMap = new HashMap<String, String>();
			for (String segID : segmentedBucket.getSegmentListImpacted().keySet()) {
				String cFailure = String.valueOf(segmentedBucket.getSegmentListImpacted().get(segID).size());
				String pFailure = String.valueOf(segmentedBucket.getSegmentWisePrediction().get(segID));
				if (cFailure != null && pFailure != null) {
					cFailure = cFailure + ForecastConstants.CURRENT_PREDICTION_SEPERATOR + pFailure;
					segmentWiseMap.put(segID, cFailure);
				}
			}

			jsonString = gson.toJson(segmentWiseMap);
		} catch (Exception e) {
			jsonString = "";
		}
		bugDetails.put(BugzillaConstants.REVIEW_LABEL,
				"Stack Trace:" + segmentedBucket.getStacktrace() + "||" + jsonString);
		return bugDetails;
	}

	private String populateDetails() {

		try {
			boolean yuvaStatus = true;
			if (segmentedBucket.getSegmentListImpacted().size() == 0)
				yuvaStatus = false;

			String predictedFailure = null;
			String bugComments = null;
			String yuvaPath = "";

			if (segmentedBucket.getErrorCode().contains(infraErrorCodes)) {
				predictedFailure = "Predicted Failure Count: " + "NA";
			} else {
				predictedFailure = "Predicted Failure Count: " + segmentedBucket.getPredictedFailure();
			}

			bugComments = "Proactive Monitoring " + new Date() + "\n" + "IsMSA Failure" + segmentedBucket.isMSAFailure()
					+ "\n" + "Received response from Yuva: " + yuvaStatus + "\n" + "AgentName: "
					+ segmentedBucket.getAgentName() + "\n" + "Top failure Details: "
					+ segmentedBucket.getTopFailure().toString() + "\n" + "ErrorCode: " + segmentedBucket.getErrorCode()
					+ "\n" + "ExceptionStacktrace: " + segmentedBucket.getStacktrace() + "\n" + "ErrorGroup: "
					+ segmentedBucket.getErrorGroup() + "\n" + "ErrorType: " + segmentedBucket.getErrorType() + "\n"
					+ "\n" + predictedFailure + "\n" + "Current Failure Count: " + segmentedBucket.getItemList().size()
					+ "\n" + "Sample User List:\n"
					+ ((segmentedBucket.getItemList().size() > 5) ? segmentedBucket.getItemList().subList(0, 5)
							: segmentedBucket.getItemList())
					+"\n"+"Start Time:"+segmentedBucket.getStartTime()+" EndTime:"+segmentedBucket.getEndTime()
					+ "\n" + "Juggernaut Details:" + segmentedBucket.getJuggernautDetails() + yuvaPath;
			logger.info("The bug Comment is {}", bugComments);
			return bugComments;
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public CreateBugResponse getOutput() {
		return generatedBug;
	}
}
