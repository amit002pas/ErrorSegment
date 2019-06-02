/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorsegment;

import org.springframework.data.mongodb.core.mapping.Document;

import com.yodlee.iae.health.PersistanceConstants;

import lombok.Data;

@Document(collection=PersistanceConstants.COLLECTION_BUCKET_COUNTER)
public @Data class BucketCounter {
	int mSegmentCounter ;
}
