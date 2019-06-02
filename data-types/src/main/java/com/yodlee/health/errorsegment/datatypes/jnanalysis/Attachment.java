package com.yodlee.health.errorsegment.datatypes.jnanalysis;

/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

import java.util.Arrays;

import lombok.Data;

public @Data class Attachment {
	private String attachmentName;
	private byte[] attachment;

	@Override
	public String toString() {

		return "attachments{attachmentName=\"" + attachmentName + "\", attachment=\"" + Arrays.toString(attachment).replaceAll("\\[|\\]", "") 
				+ "\"}";

	}
}
