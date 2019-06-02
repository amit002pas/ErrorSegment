/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.refreshstats.schedular;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MEComponentTest {

	@InjectMocks
	MEComponent mEComponent;
	
	@Test
	public void test() throws Exception{
	
		mEComponent.designatedMaster();
		mEComponent.designatedSlave();
		mEComponent.getMasterStatus();
		
	}
}
