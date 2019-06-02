/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.j2bugzilla.base.BugzillaException;
import com.yodlee.health.errorsegment.datatypes.CreateBugResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.datatypes.forecast.TopFailure;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.util.BugzillaConstants;
import com.yodlee.iae.health.util.ForecastConstants;

@RunWith(SpringJUnit4ClassRunner.class)
public class BugCreatorTest {

	@InjectMocks
	BugCreator bugCreator;

	@Mock
	private BugzillaGateway bugzillaGateway;

	List<CacheItem> itemList = new ArrayList<>();
	SegmentedBucket segmentedBucket = new SegmentedBucket();
	Integer currentRefreshCount = 1000;

	@Before
	public void setup() {

		segmentedBucket.setItemList(itemList);
		segmentedBucket.setAgentName("Chase");
		segmentedBucket.setBucketId(1234);
		segmentedBucket.setErrorCode("403");
		segmentedBucket.setErrorGroup("g1");
		segmentedBucket.setErrorType("Agent");
		segmentedBucket.setPredictedFailure(500);
		Map<String, Integer> segmentWisePrediction = new HashMap<>();
		segmentWisePrediction.put("10001100", 1010);
		segmentedBucket.setSegmentWisePrediction(segmentWisePrediction);
		TopFailure topFailure = new TopFailure();
		topFailure.setCacheItemId("121212");
		topFailure.setCobrandId("11111");
		segmentedBucket.setTopFailure(topFailure);
		Map<String, List<CacheItem>> segmentListImpacted = new HashMap<>();
		segmentListImpacted.put("10001100", itemList);
		segmentedBucket.setSegmentListImpacted(segmentListImpacted);
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		itemList.add(cacheItem);
		itemList.add(cacheItem1);
		itemList.add(cacheItem2);
		ReflectionTestUtils.setField(bugCreator, "infraErrorCodes", "404");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() throws BugzillaException{

		CreateBugResponse generatedBug = new CreateBugResponse();
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
		double currentFailure = segmentedBucket.getItemList().size();
		double percentage = (currentFailure / currentRefreshCount) * 100;

		percentage = percentage * 100;

		percentage = Math.round(percentage);
		percentage = percentage / 100;
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String currentTimeStr = DATE_FORMAT.format(new Date());

		bugDetails.put(BugzillaConstants.BG_IMPACT_FIELD,
				"Refresh Count: " + 0 + ";" + "Failure Count: " + segmentedBucket.getItemList().size() + ";"
						+ "Predicted Failure: " + segmentedBucket.getPredictedFailure() + ";" + "Impact Percentage: " + percentage + ";" + "Updated At: "
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

		bugDetails.put(BugzillaConstants.REVIEW_LABEL,
				"Stack Trace:" + segmentedBucket.getStacktrace() + "||" + jsonString);

		Mockito.when(bugzillaGateway.createBug(bugDetails)).thenReturn(generatedBug);
		bugCreator.setInput(segmentedBucket, currentRefreshCount);
		bugCreator.execute();
		bugCreator.getOutput();

	}

	@Test
	public void test1() throws BugzillaException{
		segmentedBucket.setErrorCode("404");
		CreateBugResponse generatedBug = new CreateBugResponse();
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
		double currentFailure = segmentedBucket.getItemList().size();
		double percentage = (currentFailure / currentRefreshCount) * 100;
		percentage = percentage * 100;
		percentage = Math.round(percentage);
		percentage = percentage / 100;
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String currentTimeStr = DATE_FORMAT.format(new Date());

		bugDetails.put(BugzillaConstants.BG_IMPACT_FIELD,
				"Refresh Count: " + 0 + ";" + "Failure Count: " + segmentedBucket.getItemList().size() + ";"
						+ "Predicted Failure: " + segmentedBucket.getPredictedFailure() + ";" + "Impact Percentage: " + percentage + ";" + "Updated At: "
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
		bugDetails.put(BugzillaConstants.REVIEW_LABEL,
				"Stack Trace:" + segmentedBucket.getStacktrace() + "||" + jsonString);

		Mockito.when(bugzillaGateway.createBug(bugDetails)).thenReturn(generatedBug);
		bugCreator.setInput(segmentedBucket, currentRefreshCount);
		bugCreator.execute();
		bugCreator.getOutput();

	}
	@Test
	public void test1empty() throws BugzillaException{
		segmentedBucket.setErrorCode("404");
		
		Map<String, List<CacheItem>> segmentListImpacted = new HashMap<>();
		segmentedBucket.setSegmentListImpacted(segmentListImpacted);
		CreateBugResponse generatedBug = new CreateBugResponse();
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
		double currentFailure = segmentedBucket.getItemList().size();
		double percentage = (currentFailure / currentRefreshCount) * 100;
		percentage = percentage * 100;
		percentage = Math.round(percentage);
		percentage = percentage / 100;
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String currentTimeStr = DATE_FORMAT.format(new Date());

		bugDetails.put(BugzillaConstants.BG_IMPACT_FIELD,
				"Refresh Count: " + 0 + ";" + "Failure Count: " + segmentedBucket.getItemList().size() + ";"
						+ "Predicted Failure: " + segmentedBucket.getPredictedFailure() + ";" + "Impact Percentage: " + percentage + ";" + "Updated At: "
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
		bugDetails.put(BugzillaConstants.REVIEW_LABEL,
				"Stack Trace:" + segmentedBucket.getStacktrace() + "||" + jsonString);

		Mockito.when(bugzillaGateway.createBug(bugDetails)).thenReturn(generatedBug);
		bugCreator.setInput(segmentedBucket, currentRefreshCount);
		bugCreator.execute();
		bugCreator.getOutput();

	}

	private String populateDetails() {


		try {
			String bugComments = null;
			bugComments = "Proactive Monitoring " + new Date() + "\n"
					+"IsMSA Failure"+segmentedBucket.isMSAFailure()+"\n"+ "Received response from Yuva: " + true + "\n" + "AgentName: " + segmentedBucket.getAgentName()
					+ "\n" + "Top failure Details: " + segmentedBucket.getTopFailure().toString() + "\n" + "ErrorCode: "
					+ segmentedBucket.getErrorCode() + "\n" + "ExceptionStacktrace: " + segmentedBucket.getStacktrace()
					+ "\n" + "ErrorGroup: " + segmentedBucket.getErrorGroup() + "\n" + "ErrorType: "
					+ segmentedBucket.getErrorType() + "\n" + "\n" + "Predicted Failure Count: " + segmentedBucket.getPredictedFailure() + "\n" + "Current Failure Count: "
					+ segmentedBucket.getItemList().size() + "\n" + "Sample User List:\n"
					+ ((segmentedBucket.getItemList().size() > 5) ? segmentedBucket.getItemList().subList(0, 5)
							: segmentedBucket.getItemList())
							+ "";
			return bugComments;
		}
		catch(Exception e) {
			return "";
		}
	}
}

