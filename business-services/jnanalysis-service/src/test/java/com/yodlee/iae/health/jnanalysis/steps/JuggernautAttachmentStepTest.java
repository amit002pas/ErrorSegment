/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 **/
package com.yodlee.iae.health.jnanalysis.steps;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautFinalResponse;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class JuggernautAttachmentStepTest {

	@InjectMocks
	JuggernautAttachmentStep juggernautAttachmentStep;
	@Mock
	JuggernautRepository juggernautRepository;
	
	@Mock
	JuggernautGateway juggerNautGateway;
	
	@Mock
	RestTemplate restTemplate;

	
	List<JNAnalysisTriggeredItem> triggeredItemsList = null;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		triggeredItemsList=new ArrayList<JNAnalysisTriggeredItem>();
		JNAnalysisTriggeredItem triggeredItem_1=new JNAnalysisTriggeredItem();
		triggeredItem_1.setAnalysisRequestId("d204b5ec-5fc9-11e9-8ca0-0d3ddb5cac1311773");
		triggeredItem_1.setBugId("5cb4cc5b472f8b2630e91cca");
		triggeredItem_1.setStatus("In Progress");
		
		JNAnalysisTriggeredItem triggeredItem_2=new JNAnalysisTriggeredItem();
		triggeredItem_2.setAnalysisRequestId("d3ff7483-5fc9-11e9-8ca0-673ef352fafb11780");
		triggeredItem_2.setBugId("5cb4d10b0aa0bb2dad9b120c");
		triggeredItem_2.setStatus("In Progress");
		
		triggeredItemsList.add(triggeredItem_1);
		triggeredItemsList.add(triggeredItem_2);
		
		}
	
	@Test
	public void test1() {
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("sampleBodyString", HttpStatus.ACCEPTED);
		
		Mockito.when(juggernautRepository.getTriggeredBugs()).thenReturn(triggeredItemsList);
		Mockito.when(juggerNautGateway.getSecurityToken()).thenReturn("vevrg45t55");
		Mockito.when(restTemplate.exchange(Mockito.anyString(), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(), ArgumentMatchers.<Class<String>>any()))
		.thenReturn(responseEntity);
		JuggerNautFinalResponse juggerNautFinalResponse=new JuggerNautFinalResponse();
		juggerNautFinalResponse.setItemType("4");
		juggerNautFinalResponse.setStatus("Completed");
		Mockito.when(juggerNautGateway.fetchIATResponse(Mockito.anyString(), Mockito.anyString())).thenReturn(juggerNautFinalResponse);
		juggernautAttachmentStep.fetchLatestBugs();
		juggernautAttachmentStep.pollingJNTrigerredData();
	}
	
}
