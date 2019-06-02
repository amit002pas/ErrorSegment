/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import com.yodlee.health.errorsegment.datatypes.CreateBugResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.IntermediateResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.health.autoclose.AnalysisDetails;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.autoclose.JobStatus;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.forecast.BugFailure;
import com.yodlee.iae.health.forecast.BugTracker;
import com.yodlee.iae.health.forecast.ForecastAttribute;
import com.yodlee.iae.health.forecast.ForecastAudit;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.util.ForecastConstants;

@Named
public class BugCreatorStep extends ServiceIO {

	@Inject
	BucketThreshold bucketThreshold;
	@Inject
	BugCreator bugCreator;
	@Inject
	ForecastRepository forecastRepository;
	@Inject
	PatternAnalyzer patternAnalyzer;

	private IntermediateResponse intermediateResponse;
	private List<Bucket> errorBuckets;
	private boolean isYuvaWorking;

	@Value("${site.error.codes}")
	private String siteErrorCodes;

	@Inject
	private AutoCloseRepository autoCloseRepository;

	private final String synIdPreStr = "BAID-";
	private final String synIdPostStr = "-BGCR";
	private final String AIM_STR="aim"; 
	@Override
	public void accept(Object input) {

		intermediateResponse = new IntermediateResponse();
		Map<String, Object> inputMap = (Map<String, Object>) input;
		intermediateResponse = (IntermediateResponse) inputMap.get(ForecastConstants.INTERMEDIATE);
		errorBuckets = (List<Bucket>) inputMap.get(ForecastConstants.ERROR_BUCKET_CONSTANT);
		isYuvaWorking = (boolean) inputMap.get(ForecastConstants.ISYUVAWORKING);

	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void executeImpl() {

		int countOfTotalRefresh = 0;
		List<ForecastAttribute> forecastAttributesList = new ArrayList<>();
		ForecastAudit forecastAudit = new ForecastAudit();

		if (isYuvaWorking) {
			for (SegmentedBucket segmentedBucket : intermediateResponse.getSegmentedBucketList()) {
				countOfTotalRefresh += segmentedBucket.getItemList().size();
			}
			for (SegmentedBucket segmentedBucket : intermediateResponse.getSegmentedBucketList()) {
				if (segmentedBucket.getErrorType().equals(ForecastConstants.GENUINE_FAILURE_ERROR_TYPE)) {
					forecastAttributesList.add(createForecastAttributesForAudit(segmentedBucket, countOfTotalRefresh));
				}
			}

		} else {
			for (Bucket healthBucket : errorBuckets) {
				if (healthBucket.getErrorCode().equals("0"))
					countOfTotalRefresh += healthBucket.getCacheItemIds().size();
			}
			for (Bucket healthBucket : errorBuckets) {
				if (healthBucket.getErrorType().equals(ForecastConstants.GENUINE_FAILURE_ERROR_TYPE)) {
					SegmentedBucket segmentedBucket = mapHealthBucketToSegmentedBucket(healthBucket);
					forecastAttributesList.add(createForecastAttributesForAudit(segmentedBucket, countOfTotalRefresh));
				}
			}

		}
		forecastAudit.setAgentName(errorBuckets.get(0).getAgentName());
		forecastAudit.setTime(new Date());
		forecastAudit.setYuvaWorking(isYuvaWorking);
		forecastAudit.setTotalCurrentRefreshCount(countOfTotalRefresh);
		forecastAudit.setForecastAttributesList(forecastAttributesList);
		patternAnalyzer.setInput(forecastAudit, null);
		patternAnalyzer.execute();
	}

	private SegmentedBucket mapHealthBucketToSegmentedBucket(Bucket healthBucket) {

		SegmentedBucket segmentedBucket = new SegmentedBucket();
		segmentedBucket.setMSAFailure(healthBucket.isMSAFailure());
		segmentedBucket.setLocale(healthBucket.getLocale());
		segmentedBucket.setStartTime(healthBucket.getStartTime());
		segmentedBucket.setEndTime(healthBucket.getEndTime());
		segmentedBucket.setAgentName(healthBucket.getAgentName());
		segmentedBucket.setBucketId(healthBucket.getErrorSegmentId());
		segmentedBucket.setErrorCode(healthBucket.getErrorCode());
		segmentedBucket.setErrorGroup(healthBucket.getErrorGroup());
		segmentedBucket.setErrorType(healthBucket.getErrorType());
		segmentedBucket.setItemList(healthBucket.getCacheItemIds());
		segmentedBucket.setStacktrace(healthBucket.getStacktrace());
		segmentedBucket.setRoute(healthBucket.getRoute());

		return segmentedBucket;
	}

	private CreateBugResponse createBug(SegmentedBucket segmentedBucket, int countOfTotalRefresh) {
		System.out.println("Inside create bug");
		bugCreator.setInput(segmentedBucket, countOfTotalRefresh);
		bugCreator.execute();

		CreateBugResponse bugResponse = bugCreator.getOutput();
		return bugResponse;

	}

	private ForecastAttribute createForecastAttributesForAudit(SegmentedBucket segmentedBucket,
			int countOfTotalRefresh) {
		ForecastAttribute forecastAttributes = new ForecastAttribute();
		boolean isAboveThreshold = false;
		String bugID = "N/A";
		try {
			isAboveThreshold = checkThreshold(segmentedBucket, countOfTotalRefresh);
			if (isAboveThreshold) {

				CreateBugResponse bugResponse = createBug(segmentedBucket, countOfTotalRefresh);
				System.out.println("BugResponse:"+bugResponse.getStatus()+" "+bugResponse.getMessage());
				if (bugResponse != null && !bugResponse.getStatus().contains("failed")) {
					forecastAttributes.setBugFilingRequired(true);
					forecastAttributes.setBugID(bugResponse.getSyntheticBugId());
					if (segmentedBucket.getItemList().size() > 150) {
						BugTracker bugTracker = new BugTracker();
						bugTracker.setAgentName(segmentedBucket.getAgentName());
						bugTracker.setStackTrace(segmentedBucket.getStacktrace());
						bugTracker.setAnalyzerId(bugResponse.getSyntheticBugId());
						bugTracker.setCreationDate(new Date());
						forecastRepository.saveAnalyzerId(bugTracker, segmentedBucket);
					}

					List<String> siteErrorList = new ArrayList<>();
					siteErrorList.addAll(Arrays.asList(siteErrorCodes.split(",")));
					if (siteErrorList.contains(segmentedBucket.getErrorCode()) && !AIM_STR.equalsIgnoreCase(segmentedBucket.getRoute())) {
						
						BugAutoCloserSchedularJob autoCloseObj = new BugAutoCloserSchedularJob();
						String synthBugId = bugResponse.getSyntheticBugId();
						synthBugId = synthBugId.replace(synIdPreStr, "");
						synthBugId = synthBugId.replace(synIdPostStr, "");
						autoCloseObj.setSyntheticBugId(synthBugId);
						autoCloseObj.setErrorCode(Integer.parseInt(segmentedBucket.getErrorCode()));
						autoCloseObj.setStatus(JobStatus.READY);
						autoCloseObj.setRetryCount(0);
						List<AnalysisDetails> analysisDetailsList = new ArrayList<>();
						autoCloseObj.setAnalysisDetails(analysisDetailsList);
						autoCloseObj.setAgentName(segmentedBucket.getAgentName());
						autoCloseObj.setNextScheduledTime(new Date());
						autoCloseObj.setCreatedDate(new Date());
						autoCloseObj.setLastUpdDate(new Date());
						autoCloseRepository.saveBugDetail(autoCloseObj);
					}

				} else if (bugResponse != null && bugResponse.getStatus().contains("failed")) {
					BugFailure bugFailure = new BugFailure();
					bugFailure.setMessage(bugResponse.getMessage());
					bugFailure.setCreatedDate(new Date());
					bugFailure.setSegmentedBucket(segmentedBucket);
					bugFailure.setCountOfTotalRefresh(countOfTotalRefresh);
					forecastRepository.saveFailureBug(bugFailure);

				}

			}
			forecastAttributes.setAboveThreshold(isAboveThreshold);
			createAudit(forecastAttributes, isAboveThreshold, segmentedBucket, countOfTotalRefresh);
		} catch (Exception e) {
			System.out.println("BugCreatorStep Catch..."+e);
			forecastAttributes.setBugID(bugID);
			forecastAttributes.setBugFilingRequired(isAboveThreshold);
			forecastAttributes.setAboveThreshold(isAboveThreshold);
			createAudit(forecastAttributes, false, segmentedBucket, countOfTotalRefresh);
		}
		return forecastAttributes;
	}

	/**
	 * 
	 * @param segmentedBucket
	 * @param countOfTotalRefresh
	 * @return isAboveThreshold
	 */
	private boolean checkThreshold(SegmentedBucket segmentedBucket, int countOfTotalRefresh) {
		bucketThreshold.setInput(segmentedBucket, countOfTotalRefresh);
		bucketThreshold.execute();
		return bucketThreshold.isAboveThreshold();
	}

	/**
	 * 
	 * @param forecastAttributes
	 * @param isAboveThreshold
	 * @param segmentedBucket
	 * @param countOfTotalRefresh
	 */
	private void createAudit(ForecastAttribute forecastAttributes, Boolean isAboveThreshold,
			SegmentedBucket segmentedBucket, int countOfTotalRefresh) {
		forecastAttributes.setCurrentFailure(segmentedBucket.getItemList().size());
		forecastAttributes.setSegmentedBucketId(segmentedBucket.getBucketId());
		forecastAttributes.setPredictedFailure(segmentedBucket.getPredictedFailure());
		forecastAttributes.setThresholdCoefficient(bucketThreshold.getCurrentThresholdCoefficient());

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

}
