/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.resources.errorsegment;

import lombok.Data;

public @Data class CacheItem {

	private String msaId;
	private String cacheItemId ;
	private String dbId ;
	private String cobrandId ;
	
}
