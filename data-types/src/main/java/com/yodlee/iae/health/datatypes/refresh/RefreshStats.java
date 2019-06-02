package com.yodlee.iae.health.datatypes.refresh;

import java.util.Date;
import java.util.List;

import lombok.Data;

public @Data  class RefreshStats {
	
	private Date startTime;
	private Date endTime;
	private List<ItemResponseSplunk> itemLevelData;
	

}
