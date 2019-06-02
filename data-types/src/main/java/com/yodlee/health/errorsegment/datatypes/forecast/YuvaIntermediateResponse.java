package com.yodlee.health.errorsegment.datatypes.forecast;

import java.util.List;

import lombok.Data;

public @Data class YuvaIntermediateResponse {
	
	String name;
	List<String> segmentIds;

}
