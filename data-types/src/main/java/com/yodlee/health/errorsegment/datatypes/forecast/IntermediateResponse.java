package com.yodlee.health.errorsegment.datatypes.forecast;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

public @Data class IntermediateResponse {
	
	List<SegmentedBucket> segmentedBucketList;
	HashMap<String,Integer> yuvaSegmentCiiCountMap;
	HashMap<String,Integer> healthSegmentCiiCountMap;
}
