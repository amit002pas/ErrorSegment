/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.health.errorsegment.repository.forecast.ForecastRepository;
import com.yodlee.iae.health.forecast.ForecastAudit;


/**
 * @author srai
 *
 */
@Named
public class PatternAnalyzer implements ForecastStepsBase<ForecastAudit, String, Boolean> {

	@Inject
	ForecastRepository forecastRepository;
	
	private ForecastAudit forecastAudit=new ForecastAudit();
	
	Logger logger=LoggerFactory.getLogger(PatternAnalyzer.class);
	
	@Override
	public void setInput(ForecastAudit forecastAudit, String nullString) {
		this.forecastAudit=forecastAudit;
	}

	@Override
	public void execute() {
		forecastRepository.saveForecastAudit(forecastAudit);
	}

	@Override
	public Boolean getOutput() {
		return null;
	}

}
