/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.forecast.CacheItemDB;
import com.yodlee.health.errorsegment.datatypes.forecast.IntermediateResponse;
import com.yodlee.health.errorsegment.datatypes.forecast.Segment;
import com.yodlee.health.errorsegment.datatypes.forecast.SegmentedBucket;
import com.yodlee.health.errorsegment.datatypes.forecast.TopFailure;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.util.ForecastConstants;

@Named
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SegmentCategoriserStep extends ServiceIO {

	private List<Segment> yuvaSegments = new ArrayList<Segment>();
	private List<Bucket> errorBuckets = new ArrayList<Bucket>();

	private IntermediateResponse intermediateResponse;

	@Override
	public void accept(Object input) {
		Map<String, Object> inputMap = (Map<String, Object>) input;
		yuvaSegments = (List<Segment>) inputMap.get(ForecastConstants.YUVA_SEGMENT_CONSTANT);
		errorBuckets = (List<Bucket>) inputMap.get(ForecastConstants.ERROR_BUCKET_CONSTANT);
		intermediateResponse = new IntermediateResponse();

	}

	private HashMap<String, String> mapCIIDBSegmentData() {

		HashMap<String, String> ciiDbidSegmentIdMap = new HashMap<String, String>();
		HashMap<String, Integer> segmentCiiMap = new HashMap<String, Integer>();
		for (Segment segment : yuvaSegments) {
			if (segment.getCiiDb() != null && segment.getSegmentId() != null) {
				segmentCiiMap.put(segment.getSegmentId(), segment.getCiiDb().size());
				for (CacheItemDB ciiDBID : segment.getCiiDb()) {
					if (ciiDBID.getCacheItemId() != null && ciiDBID.getDataBase() != null)
						ciiDbidSegmentIdMap.put(
								ciiDBID.getCacheItemId() + ForecastConstants.DELIMITER + ciiDBID.getDataBase(),
								segment.getSegmentId());
				}
			}
		}

		intermediateResponse.setYuvaSegmentCiiCountMap(segmentCiiMap);

		return ciiDbidSegmentIdMap;
	}

	@Override
	public Object get() {
		return this.intermediateResponse;
	}

	@Override
	public void executeImpl() {
		HashMap<String, String> ciiDbidSegmentIdMap = mapCIIDBSegmentData();
		categorize(ciiDbidSegmentIdMap);

	}

	private void categorize(HashMap<String, String> ciiDbidSegmentIdMap) {

		HashMap<String, Integer> healthSegmentCiiCountMap = new HashMap<String, Integer>();
		List<SegmentedBucket> segmentedBucketList = new ArrayList<>();

		for (Bucket errorBucket : errorBuckets) {
			TopFailure topFailure = new TopFailure();
			SegmentedBucket segmentedBucket = new SegmentedBucket();
			List<CacheItem> ciiList = errorBucket.getCacheItemIds();
			segmentedBucket.setSumInfo(errorBucket.getSumInfo());
			segmentedBucket.setSiteId(errorBucket.getSiteId());
			segmentedBucket.setLocale(errorBucket.getLocale());
			segmentedBucket.setStartTime(errorBucket.getStartTime());
			segmentedBucket.setEndTime(errorBucket.getEndTime());
			segmentedBucket.setAgentName(errorBucket.getAgentName());
			segmentedBucket.setErrorGroup(errorBucket.getErrorGroup());
			segmentedBucket.setErrorType(errorBucket.getErrorType());
			segmentedBucket.setStacktrace(errorBucket.getStacktrace());
			segmentedBucket.setBucketId(errorBucket.getErrorSegmentId());
			segmentedBucket.setErrorCode(errorBucket.getErrorCode());
			segmentedBucket.setItemList(errorBucket.getCacheItemIds());
			segmentedBucket.setJuggernautDetails(errorBucket.getJuggernautDetails());
			segmentedBucket.setRoute(errorBucket.getRoute());
			
			topFailure.setCacheItemId(ciiList.get(0).getCacheItemId());
			topFailure.setDbId(ciiList.get(0).getDbId());
			topFailure.setCobrandId(ciiList.get(0).getCobrandId());
			topFailure.setCounter(0);

			CacheItem topFailureCachItem = null;
			Integer topFailureCount = 0;

			HashMap<String, List<CacheItem>> effectedSegmentMap = new HashMap<String, List<CacheItem>>();
			for (CacheItem ciiItem : ciiList) {
				String cii = ciiItem.getCacheItemId();
				String dbID = ciiItem.getDbId();

				// what if null
				try {
					String segmentID = ciiDbidSegmentIdMap.get(cii + ForecastConstants.DELIMITER + dbID);
					if(segmentID!=null) {
					effectedSegmentMap.computeIfAbsent(segmentID, k -> new ArrayList<CacheItem>()).add(ciiItem);

					// Need to verify

					healthSegmentCiiCountMap.compute(segmentID, (k, v) -> {
						if (v == null)
							return 1;
						else {
							return v + 1;
						}
					});

					if (effectedSegmentMap.get(segmentID).size() > topFailureCount) {
						topFailureCount = effectedSegmentMap.get(segmentID).size();
						topFailureCachItem = ciiItem;
					}
					}
				} catch (Exception e) {
					System.out.println("CacheItemId not present in Yuva");
				}
			}

			// No Need, in above loop we can achive this

			if (null != topFailureCachItem) {
				topFailure.setCacheItemId(topFailureCachItem.getCacheItemId());
				topFailure.setCobrandId(topFailureCachItem.getCobrandId());
				topFailure.setDbId(topFailureCachItem.getDbId());
				topFailure.setCounter(topFailureCount);
			}
			segmentedBucket.setTopFailure(topFailure);
			segmentedBucket.setSegmentListImpacted(effectedSegmentMap);
			segmentedBucketList.add(segmentedBucket);

		}

		// adding all the data for final count
		intermediateResponse.setHealthSegmentCiiCountMap(healthSegmentCiiCountMap);
		intermediateResponse.setSegmentedBucketList(segmentedBucketList);

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
