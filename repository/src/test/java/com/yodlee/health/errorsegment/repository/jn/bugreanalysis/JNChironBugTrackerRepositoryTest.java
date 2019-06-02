/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc.Â 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.repository.jn.bugreanalysis;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.resources.jn.bugreanalysis.JNChironBugTracker;


@RunWith(SpringJUnit4ClassRunner.class)
public class JNChironBugTrackerRepositoryTest {

	private static final String BUG_ZILLA_BUGID = "juggernautbugZillaBugId";
	private static final String RESPONSE_MSG= "responseMessage";

	@InjectMocks
	private JNChironBugTrackerRepository jNChironBugTrackerRepository;
	
	@Mock
	private MongoOperations mongoOperations;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() {
		List<JNChironBugTracker> jnChironBugTrackers = new ArrayList<>();
		JNChironBugTracker jNChironBugTracker = new JNChironBugTracker();
		jnChironBugTrackers.add(jNChironBugTracker);
		jNChironBugTrackerRepository.saveJNChironBugTracker(jnChironBugTrackers);
		
		List<Integer> bugIds = new ArrayList<>();
		bugIds.add(12345);
		bugIds.add(123456);
		Query query = new Query().addCriteria(Criteria.where(BUG_ZILLA_BUGID).in(bugIds));		
		when(mongoOperations.find(query,JNChironBugTracker.class)).thenReturn(jnChironBugTrackers);
		jNChironBugTrackerRepository.getBugsTrackerFromBugzillaIds(bugIds);
		
		Query query1 = new Query().addCriteria(Criteria.where(RESPONSE_MSG).in(""));		
		when(mongoOperations.find(query1,JNChironBugTracker.class)).thenReturn(jnChironBugTrackers);
		
		jNChironBugTrackerRepository.getBugsBasedOnStatus();
		String bugIdsForJN = "1234";
		String responseMessage = "message";
		jNChironBugTrackerRepository.updateSentToJNStatus(bugIdsForJN, responseMessage);
		
	}
}
