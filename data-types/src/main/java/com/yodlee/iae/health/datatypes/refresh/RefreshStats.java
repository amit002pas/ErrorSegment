package com.yodlee.iae.health.datatypes.refresh;

/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
import java.util.Date;
import java.util.List;

import lombok.Data;

public @Data  class RefreshStats {
	
	private Date startTime;
	private Date endTime;
	private List<ItemResponseSplunk> itemLevelData;
	

}
