/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.util;

import javax.inject.Named;

import lombok.Data;

/**
 * @author vchhetri
 *
 */
@Named
public @Data class BugAutoCloserConfigModel {
	private int retryCount;
	private int retryInterval;
}
