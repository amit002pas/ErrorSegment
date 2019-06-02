/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.repository.juggernaut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;

@RunWith(SpringJUnit4ClassRunner.class)
public class JuggernautRepositoryTest {

	@InjectMocks
	private JuggernautRepository juggernautRepository;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test(){
		
		juggernautRepository.updateJNStatus("1244");
		
		JNAnalysisTriggeredItem jnAnalysisTriggeredItem = new JNAnalysisTriggeredItem();
		//juggernautRepository.updateBugStatus(jnAnalysisTriggeredItem);
		
		juggernautRepository.saveAnalyzerId(jnAnalysisTriggeredItem);
		
		juggernautRepository.getTriggeredBugs();
		
		juggernautRepository.getJNAnalysisTriggeredItem("1234");
		
		
	}
}
