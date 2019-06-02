/*
 * Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms.
 */ 
package com.yodlee.health.errorsegment.schedular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


public class HealthSchedularTest {
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetDiffBuckets() {
		
		try {
			HealthSchedular.getDiffBuckets("");
		}catch(Exception ex) {
			Assert.assertEquals("java.net.ConnectException: Connection refused: connect", ex.getMessage());
		}
		
	}
}
