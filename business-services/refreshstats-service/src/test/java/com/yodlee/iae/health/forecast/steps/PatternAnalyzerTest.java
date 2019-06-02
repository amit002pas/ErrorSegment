/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.forecast.steps;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.health.forecast.ForecastAudit;

@RunWith(SpringJUnit4ClassRunner.class)
public class PatternAnalyzerTest {

	@InjectMocks
	PatternAnalyzer patternAnalyzer;
	
	@Mock
	ForecastRepository forecastRepository;
	
	@Test
	public void test(){
		
		ForecastAudit forecastAudit = new ForecastAudit();
		String nullString = null;
		
		patternAnalyzer.setInput(forecastAudit, nullString);
		patternAnalyzer.execute();
		patternAnalyzer.getOutput();
	}
}
