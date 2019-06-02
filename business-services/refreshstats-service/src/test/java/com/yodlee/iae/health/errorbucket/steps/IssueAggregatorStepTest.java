/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.errorbucket.steps;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;
import com.yodlee.iae.health.util.Cosine;

@RunWith(SpringJUnit4ClassRunner.class)
public class IssueAggregatorStepTest {

	@InjectMocks
	IssueAggregatorStep issueAggregatorStep;
	@Mock
	private Cosine cosine;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSegmentiseRefreshFromSplunk(){
		List<ItemResponseSplunk> obj = new ArrayList<>();
		ItemResponseSplunk item = new ItemResponseSplunk();
		item.setAgentName("Chase");
		item.setErrorType(403);
		item.setStackTrace("nullpointerexception");
		ItemResponseSplunk item1 = new ItemResponseSplunk();
		item1.setAgentName("Chase");
		item1.setErrorType(403);
		item1.setStackTrace("nullpointerexception");
		ItemResponseSplunk item2 = new ItemResponseSplunk();
		item2.setAgentName("Chase");
		item2.setErrorType(401);
		item2.setStackTrace("indexexception");
		ItemResponseSplunk item3 = new ItemResponseSplunk();
		item3.setAgentName("Chase");
		item3.setErrorType(404);
		item3.setStackTrace("infraerexception");
		ItemResponseSplunk item4 = new ItemResponseSplunk();
		item4.setAgentName("Chase");
		item4.setErrorType(601);
		item4.setStackTrace("exception");
		ItemResponseSplunk item5 = new ItemResponseSplunk();
		item5.setAgentName("Chase");
		item5.setErrorType(802);
		item5.setStackTrace("invalidexception");
		ItemResponseSplunk item6 = new ItemResponseSplunk();
		item6.setAgentName("Chase");
		item6.setErrorType(801);
		item6.setStackTrace("nullexception");
		ItemResponseSplunk item7 = new ItemResponseSplunk();
		item7.setAgentName("Chase");
		item7.setErrorType(801);
		item7.setStackTrace("nullexceptionns");
		
		obj.add(item);
		obj.add(item1);
		obj.add(item2);
		obj.add(item3);
		obj.add(item4);
		obj.add(item5);
		obj.add(item6);
		obj.add(item7);
		
		
		issueAggregatorStep.process(obj);
	}
}
