/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.yuva;

import java.util.List;

import com.yodlee.health.errorsegment.datatypes.forecast.Segment;

public interface IYUVAGateway {

	
	public List<Segment> getYUVASegmentForAgent(String agentName) throws Exception;
}
