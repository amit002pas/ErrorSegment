/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.splunk;

public interface ISplunkConnect {
	/**
	 * The interface executes the mentioned Splunk query and returns result in JSON
	 * format.
	 * 
	 * @param queryString
	 * @return result of the query in Json Format
	 * @throws Exception
	 */
	public String executeSplunkServices(String queryString) throws Exception;

}
