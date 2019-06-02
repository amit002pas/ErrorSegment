/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorbucket.steps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.JuggernautDetails;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.gateway.util.JuggerNautConstants;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.errorsegment.ErrorBucket;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.repository.errorsegment.BucketCounterRepository;
import com.yodlee.iae.health.repository.errorsegment.ErrorBucketRepository;
import com.yodlee.iae.health.util.Cosine;
import com.yodlee.iae.health.util.ErrorBucketConstants;
import com.yodlee.iae.health.util.ErrorSegmentationUtil;

/***
 * 
 * @author akumar23
 *
 */

@Named
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IssueAnalyzerStep extends ServiceIO {

	@Inject
	private ErrorBucketRepository errorSegmentRepository;

	@Inject
	private BucketCounterRepository bucketCounterRepository;

	@Inject
	JuggernautGateway juggernautGateway;
	@Inject
	Cosine cosine;

	@Value("${noOfUsersToProcess}")
	private int noOfUsersToprocess;

	@Value("${bucketSize}")
	private int bucketSize;

	private List<Bucket> inputBucketList = new ArrayList<>();
	public Map<String, List<Bucket>> agentDataMap = null;

	private Bucket[] outputBucket = null;

	private void agentWiseMapping(Bucket[] finalResponse) {

		agentDataMap = new HashMap<String, List<Bucket>>();
		for (Bucket bucket : finalResponse) {
			agentDataMap.computeIfAbsent(bucket.getAgentName(), k -> new ArrayList<Bucket>()).add(bucket);
		}

	}

	private int checkStackTraceForIntermittent(String stackTrace) {
		return ErrorSegmentationUtil.isStacktraceIntermittent(stackTrace);

	}

	public boolean isSuccessError(String error) {
		return (error.equals("0")) ? true : false;
	}

	private int checkSegmentForIntermittentErrorsAndTriggerIAT(Bucket bucket, int retryCounter, String securityToken) {

		CacheItem cii = null;

		try {
			cii = bucket.getCacheItemIds().get(retryCounter);
		} catch (IndexOutOfBoundsException e1) {
			return ErrorBucketConstants.ALREADY_SUCCESS;
		}
		List<ErrorBucket> listOfBuckets = errorSegmentRepository.getBucketIfPresent(bucket);
		if (bucket.getCacheItemIds().size() > bucketSize)
			triggerJuggernaut(bucket, securityToken,listOfBuckets);
		else
		{			
			for(ErrorBucket dbBucket:listOfBuckets) {
				
				if(cosine.similarity(dbBucket.getStacktrace(),bucket.getStacktrace())>ErrorBucketConstants.THRESHOLD_SIMILARITY) {
					System.out.println("^^^Less than:"+dbBucket.getErrorBucketId()+" "+bucket.getErrorSegmentId());
					bucket.setJuggernautDetails(dbBucket.getJuggernautDetails());
					break;
				}
			}
		}
		if (cii.getCacheItemId().equals("-1"))
			bucket.setMSAFailure(true);
		else
			bucket.setMSAFailure(false);

		int qcCheck = ErrorBucketConstants.QUERY_CENTER_INITIAL;

		try {

			qcCheck = checkStackTraceForIntermittent(bucket.getStacktrace());
		} catch (Exception e1) {
			qcCheck = 2;
		}

		switch (qcCheck) {

		case ErrorBucketConstants.ERROR_TYPE_INTERMITTENT:
			return ErrorBucketConstants.ERROR_TYPE_INTERMITTENT;

		default:
			return ErrorBucketConstants.ERROR_TYPE_GENUINE;

		}

	}

	private void triggerJuggernaut(Bucket bucket, String securityToken,List<ErrorBucket> listOfBuckets) {
		JuggerNautInputRequest juggerNautrequest = new JuggerNautInputRequest();
		juggerNautrequest.setCobrandID(bucket.getCacheItemIds().get(0).getCobrandId());
		juggerNautrequest.setCreateBug(false);
		String item=bucket.getCacheItemIds().get(0).getCacheItemId();
		String type=ErrorBucketConstants.CACHE_ITEM;
		if(item.equals(ErrorBucketConstants.MSA_FAILURE)) {
			item=bucket.getCacheItemIds().get(0).getMsaId();
			type=ErrorBucketConstants.MEM_SITE_ACC_ID;
		}
		juggerNautrequest.setItemID(item);
		juggerNautrequest.setItemType(type);
		JuggernautDetails juggernautDetails = new JuggernautDetails();
		
		boolean flag = false;
		for (ErrorBucket similarBucket : listOfBuckets) {

			if (similarBucket.getCacheItemIds().size()>10 && cosine.similarity(bucket.getStacktrace(),
					similarBucket.getStacktrace()) > ErrorBucketConstants.THRESHOLD_SIMILARITY && null!=similarBucket.getJuggernautDetails()) {
				flag = true;
				System.out.println("^^Inside matching JN");
				juggernautDetails = similarBucket.getJuggernautDetails();
				break;
			}
		}

		if (!flag) {
			String analysisId = "";
			try {
				System.out.println("Inside JN Hit");
				analysisId = juggernautGateway.getAnalysisRequestId(securityToken, juggerNautrequest);
			} catch (Exception e) {
				e.printStackTrace();
			}

			juggernautDetails.setAnalysisId(analysisId);
			juggernautDetails.setTime(new Date());
		}
		bucket.setJuggernautDetails(juggernautDetails);

	}

	@Async
	public CompletableFuture<Void> processBucket(Bucket bucket, int index, String securityToken) {

		int segmentId = bucketCounterRepository.incrementAndGetAuditCounter();
		bucket.setErrorSegmentId(segmentId);

		if (bucket.getErrorCode().equals("0")) {
			bucket.setErrorType("Success");
		} else {
			int intermittentCheck = checkSegmentForIntermittentErrorsAndTriggerIAT(bucket,
					ErrorBucketConstants.INITIAL_RETRY_COUNT, securityToken);
			// int intermittentCheck=2;
			switch (intermittentCheck) {

			case ErrorBucketConstants.ERROR_TYPE_INTERMITTENT:
				bucket.setErrorType("Intermittent");
				break;

			default:
				bucket.setErrorType("Genuine");
			}
		}

		outputBucket[index] = bucket;
		ErrorBucket errorBucket = new ErrorBucket();
		errorBucket.setMSAFailure(outputBucket[index].isMSAFailure());
		errorBucket.setAgentName(outputBucket[index].getAgentName());
		errorBucket.setCacheItemIds((outputBucket[index].getCacheItemIds()));
		errorBucket.setErrorBucketId((outputBucket[index].getErrorSegmentId()));
		errorBucket.setErrorCode(outputBucket[index].getErrorCode());
		errorBucket.setErrorGroup(outputBucket[index].getErrorGroup());
		errorBucket.setErrorType(outputBucket[index].getErrorType());
		errorBucket.setStacktrace(outputBucket[index].getStacktrace());
		errorBucket.setCountOfUsers(outputBucket[index].getCacheItemIds().size());
		errorBucket.setJuggernautDetails(outputBucket[index].getJuggernautDetails());
		List<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
		errorBucket.setStartTime(new Date());
		try {
			errorSegmentRepository.saveErrorBucket(errorBucket);
		} catch (Exception e) {
			System.out.println("Mongo Limit Exceeded: " + errorBucket.getErrorCode() + " "
					+ errorBucket.getCountOfUsers() + " " + errorBucket.getAgentName());
		}

		return CompletableFuture.completedFuture(null);
	}

	@Override
	public void accept(Object t) {
		agentDataMap = null;
		inputBucketList = (List<Bucket>) t;
		outputBucket = new Bucket[inputBucketList.size()];
	}

	@Override
	public Object get() {
		return agentDataMap;
	}

	@Override
	public void executeImpl() {
		int index = 0;
		String token = null;
		String securityToken = "";
		try {
			JsonNode response = juggernautGateway.generateToken();
			if (response.get(JuggerNautConstants.AUTHENTICATION_KEY).asText().equals(JuggerNautConstants.SUCCESS)) {
				securityToken = response.get(JuggerNautConstants.SECURITY_TOKEN_KEY).asText();
			}
		} catch (JsonParseException e) {
			System.out.println("JsonParseException" + e);
		} catch (IOException e) {
			System.out.println("IOException" + e);
		}
		CompletableFuture<?>[] taskList = new CompletableFuture<?>[inputBucketList.size()];
		for (Bucket bucket : inputBucketList) {
			CompletableFuture<Void> task = processBucket(bucket, index, securityToken);
			taskList[index] = task;
			index++;
		}
		CompletableFuture.allOf(taskList).join();
		agentWiseMapping(outputBucket);
	}

	@Override
	public void mapInput() {

	}

	@Override
	public void mapOutput() {

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
