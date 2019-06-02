package com.yodlee.iae.health.jnanalysis.steps;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.datatypes.Bug;
import com.yodlee.health.errorsegment.datatypes.BugFields;
import com.yodlee.health.errorsegment.datatypes.SyntheticFields;

@RunWith(SpringJUnit4ClassRunner.class)
public class JuggernautAnalyserStepTest {

	@InjectMocks
	JuggernautAnalyserStep juggernautAnalyserStep;
	
	private List<Bug> bugList;
	
	
	
	@Before
	void setup() {
		MockitoAnnotations.initMocks(this);
		Bug bug1=new Bug();
		bug1.setCreatedDate(10000000);
		bug1.setSyntheticBugid("vrjr-dvswa-vrts");
		SyntheticFields synFields=new SyntheticFields();
		BugFields bugFields=new BugFields();
		bugFields.setAgentName("Wellsfargo");
		bugFields.setAgentStatus("full");
		bugFields.setSummary("Proactive Monitoring Bug for");
		bug1.setBugFields(bugFields);
		bugList.add(bug1);
		
	}
	
	@Test
	void executeTest() {
		juggernautAnalyserStep.setInput(bugList, "Batch");
		juggernautAnalyserStep.executeImpl();
	}
	
}
