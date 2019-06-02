/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorbucket;

import java.util.List;
import java.util.Map;

import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.iae.common.services.exception.ServiceException;
import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.datatypes.refresh.RefreshStats;

public interface IErrorBucketService {

	public RefreshStats getErrorBucketDataFromSplunk(List<String> agentList, String groupName)
			throws Exception;

	public Map<String, List<Bucket>> getHealth(List<String> agentList, String groupName) throws Exception;
	
	public void forecastErrorBucket(Map<String, List<Bucket>> agentDataMap)throws ServiceException;
	
	public void bugImpactUpdation();

}
