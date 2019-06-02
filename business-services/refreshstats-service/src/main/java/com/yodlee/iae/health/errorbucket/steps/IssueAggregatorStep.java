/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorbucket.steps;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.datatypes.refresh.RefreshStats;
import com.yodlee.iae.health.errorbucket.ServiceIO;
import com.yodlee.iae.health.exception.JSONParsingException;
import com.yodlee.iae.health.util.Cosine;
import com.yodlee.iae.health.util.ErrorBucketConstants;

@Named
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IssueAggregatorStep extends ServiceIO {

	@Inject
	private Cosine cosine;

	private List<Bucket> mainBucketList = null;

	private RefreshStats refreshStats = null;

	private String DELIMITER = "`";

	private List<Bucket> segmentiseRefreshFromSplunk(RefreshStats stats) throws JSONException {
		mainBucketList = new ArrayList<>();
		HashMap<String, Set<ItemResponseSplunk>> stackTraceItemsMap = new HashMap<>();

		boolean matchFound;
		for (ItemResponseSplunk refreshDetails : stats.getItemLevelData()) {
			matchFound = false;
			String stacktrace = refreshDetails.getStackTrace();
			String newAgent = refreshDetails.getAgentName();
			int errorType = refreshDetails.getErrorType();
			StringBuilder stackTaceBuilder = new StringBuilder().append((null == stacktrace) ? "" : stacktrace)
					.append(DELIMITER).append(errorType).append(DELIMITER).append(newAgent);

			if (null!=stackTraceItemsMap.get(stackTaceBuilder.toString())) {
				stackTraceItemsMap.compute(stackTaceBuilder.toString(), (k, v) -> {
					v.add(refreshDetails);
					return v;
				});

			} else {
				
				for (String key : stackTraceItemsMap.keySet()) {
					String[] traceList = key.split(DELIMITER, 3);
					
					if (traceList[1].equals(String.valueOf(errorType)) && traceList[2].equals(newAgent)) {
						
						double similarity = cosine.similarity(stacktrace, traceList[0]);
						if (similarity > ErrorBucketConstants.THRESHOLD_SIMILARITY) {
							stackTraceItemsMap.compute(key, (k, v) -> {
								v.add(refreshDetails);
								return v;
							});

							matchFound = true;
							break;
						}
					}
				}

				if (!matchFound)
					stackTraceItemsMap
							.computeIfAbsent(stackTaceBuilder.toString(), k -> new HashSet<ItemResponseSplunk>())
							.add(refreshDetails);

			}
		}
		System.out.println("MapSize:"+stackTraceItemsMap.size());
		List<Bucket> listOfBuckets = createBucketForSplunkRefresh(stackTraceItemsMap,stats.getStartTime(),stats.getEndTime());
		System.out.println("BucketSize"+listOfBuckets.size());
		return listOfBuckets;

	}

	private List<Bucket> createBucketForSplunkRefresh(Map<String, Set<ItemResponseSplunk>> splunkResponseItemList,Date startTime,Date endTime) {
		List<Bucket> bucketList = new ArrayList<>();
		CacheItem itemId = null;
		Bucket bucket = null;
		String errorGroup;
		for (String stackTraceKey : splunkResponseItemList.keySet()) {
			bucket = new Bucket();
			Set<ItemResponseSplunk> responseSplunkSet = splunkResponseItemList.get(stackTraceKey);
			ItemResponseSplunk responseSplunk = responseSplunkSet.iterator().next();

			bucket.setAgentName(responseSplunk.getAgentName());
			bucket.setSiteId(responseSplunk.getSiteId());
			bucket.setSumInfo(responseSplunk.getSumInfo());
			bucket.setRoute(responseSplunk.getRoute());
			bucket.setStartTime(startTime);
			bucket.setEndTime(endTime);
			List<CacheItem> cacheItemList = new ArrayList<>();
			for (ItemResponseSplunk item : responseSplunkSet) {
				itemId = new CacheItem();
				itemId.setMsaId(item.getMsaId());
				itemId.setCacheItemId(item.getCacheItemId());
				itemId.setCobrandId(item.getCobrandId());
				itemId.setDbId(item.getDbId());
				cacheItemList.add(itemId);
			}
			bucket.setCacheItemIds(cacheItemList);
			bucket.setLocale(responseSplunk.getLocale());
			bucket.setStacktrace(responseSplunk.getStackTrace());
			int errorCode = responseSplunk.getErrorType();
			String errorType = getErrorTypeForCode(errorCode);
			bucket.setErrorCode(String.valueOf(errorCode));

			switch (errorType) {
			case ErrorBucketConstants.ERROR_TYPE_INFRA:
				errorGroup = ErrorBucketConstants.ERROR_TYPE_INFRA;
				break;
			case ErrorBucketConstants.ERROR_TYPE_APP:
				errorGroup = ErrorBucketConstants.ERROR_TYPE_APP;
				break;
			default:
				errorGroup = ErrorBucketConstants.ERROR_TYPE_IAE;
			}

			bucket.setErrorGroup(errorGroup);
			bucketList.add(bucket);

		}
		return bucketList;

	}

	private String getErrorTypeForCode(int errorCode) {

		switch (errorCode) {
		case 401:
		case 404:
		case 601:
			return ErrorBucketConstants.ERROR_TYPE_INFRA;
		case 801:
		case 802:
			return ErrorBucketConstants.ERROR_TYPE_APP;
		default:
			return ErrorBucketConstants.ERROR_TYPE_IAE;
		}
	}

	@Override
	public void accept(Object t) {
		refreshStats = (RefreshStats) t;

	}

	@Override
	public Object get() {
		Object bucketList = mainBucketList;
		return bucketList;
	}

	@Override
	public void executeImpl() {

		mainBucketList = null;
		try {
			mainBucketList = segmentiseRefreshFromSplunk(refreshStats);
		} catch (JSONException e) {
			throw new JSONParsingException(e.getMessage());
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

}
