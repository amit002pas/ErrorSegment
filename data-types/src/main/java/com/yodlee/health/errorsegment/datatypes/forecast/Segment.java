package com.yodlee.health.errorsegment.datatypes.forecast;

import java.util.List;

import lombok.Data;

public @Data class Segment {
//	String averageSuccess;
//	String averageLatency;
//	String cacheCount;
	
	String segmentId;
	List<CacheItemDB> ciiDb;
}
