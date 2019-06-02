package com.yodlee.health.errorsegment.datatypes.forecast;
import java.util.List;

import lombok.Data;

public @Data class YuvaResp {
	
	private String agentName;
	private List<YuvaSegmentListResponse> segments;
	
}
