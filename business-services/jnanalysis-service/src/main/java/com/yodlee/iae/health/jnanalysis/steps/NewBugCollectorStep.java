/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.jnanalysis.steps;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.health.errorsegment.datatypes.Bug;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;

@Named
public class NewBugCollectorStep extends ServiceIO {

	Logger logger = LoggerFactory.getLogger(NewBugCollectorStep.class);

	@Inject
	private BugzillaGateway bugzillaGateway;

	private List<Bug> bugs;

	@Override
	public void accept(Object arg0) {
		// TODO Auto-generated method stub
		bugs = new ArrayList<Bug>();

	}

	@Override
	public Object get() {

		return this.bugs;
	}

	@Override
	public void executeImpl() {

		bugs = bugzillaGateway.getBugDetailsForHour().getBugs();

	}

	@Override
	public void mapInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapOutput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
