/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.refreshstats;

import java.util.List;


public interface IRefreshStatsService {
	/**
	 * 
	 * @param agentList
	 * @param groupName
	 * @return Refresh stats after hitting splunk
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public void refreshStats(List<String> agentList, String groupName) throws NullPointerException, Exception;

}
