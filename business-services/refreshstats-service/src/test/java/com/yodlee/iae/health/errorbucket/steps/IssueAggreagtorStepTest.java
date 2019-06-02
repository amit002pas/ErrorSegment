package com.yodlee.iae.health.errorbucket.steps;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.yodlee.iae.health.datatypes.refresh.ItemResponseSplunk;

public class IssueAggreagtorStepTest {

	@InjectMocks
	IssueAggregatorStep issueAggregatorStep;
	
	private List<ItemResponseSplunk> refreshStats = null;	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ItemResponseSplunk item1=new ItemResponseSplunk();
		item1.setAgentName("Wellsfargo");
		item1.setCacheItemId("124224979");
        item1.setCobrandId("100272812");
        item1.setDbId("sdbcae01");
        item1.setErrorType(404);
        item1.setGathererIp("172.13.43.123");
        item1.setLocale("US");
        item1.setMsaId("15325222");
        item1.setRefreshTime(new Date());
        item1.setSiteId("5");
        item1.setStackTrace("Browser timeout exception");
        item1.setSumInfo("5");
        refreshStats=new ArrayList<>();
        refreshStats.add(item1);
        
	}
	@Test
	public void segmentiseRefreshFromSplunk() {
		issueAggregatorStep.executeImpl();
		fail("Not yet implemented");
	}

}
