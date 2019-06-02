/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes;

import lombok.Data;

public @Data class Bug {

	private String syntheticBugid;
	private long createdDate;
	private SyntheticFields syntheticFields;
	private BugFields bugFields;
	private MiscellaneousFields miscellaneousFields;
	// private List<AttachmentRequest> attachments;

}
