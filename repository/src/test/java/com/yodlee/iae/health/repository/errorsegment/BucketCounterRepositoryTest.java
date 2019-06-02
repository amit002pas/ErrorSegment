/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.repository.errorsegment;


import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.errorsegment.BucketCounter;

@RunWith(SpringJUnit4ClassRunner.class)
public class BucketCounterRepositoryTest {

	@InjectMocks
	private BucketCounterRepository bucketCounterRepository;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test(){
		
		
		BucketCounter escd = new BucketCounter();
		escd.setMSegmentCounter(12);
		Query query = new Query().addCriteria(Criteria.where("type").is("segmentCounter"));		
		Update update = new Update().inc("mSegmentCounter", 1);
		when(mongoOperations.findAndModify(query, update, BucketCounter.class)).thenReturn(escd);
		
		bucketCounterRepository.incrementAndGetAuditCounter();
	}
}
