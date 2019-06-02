/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.jnbugreanalysis;

import java.util.Date;

import lombok.Data;

/**
 * @author vchhetri
 *
 */
public @Data class DateFilter {
	private Date startTime;
	private Date endTime;

}
