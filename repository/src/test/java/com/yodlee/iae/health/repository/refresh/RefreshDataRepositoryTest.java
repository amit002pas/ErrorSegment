/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.repository.refresh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.refresh.RefreshStatsCollection;

@RunWith(SpringJUnit4ClassRunner.class)
public class RefreshDataRepositoryTest {


	@InjectMocks
	private RefreshDataRepository refreshDataRepository;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test(){

		RefreshStatsCollection refreshStatsAudit = new RefreshStatsCollection();
		refreshDataRepository.insertAuditRefreshData(refreshStatsAudit);
	}
}
