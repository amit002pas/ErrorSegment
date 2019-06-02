/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.jnanalysis.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yodlee.health.errorsegment.datatypes.Bug;
import com.yodlee.health.errorsegment.datatypes.BugFetchResponse;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;

@Named
public class BugDetailsCollectorStep extends ServiceIO {

	Logger logger = LoggerFactory.getLogger(BugDetailsCollectorStep.class);

	@Inject
	private BugzillaGateway bugzillaGateway;

	private String bugId;

	private List<Bug> bugs;

	

	@Override
	public void accept(Object arg0) {

		this.bugId = (String) arg0;
		bugs = new ArrayList<Bug>();

	}

	@Override
	public Object get() {
		return this.bugs;
	}

	@Override
	public void executeImpl() {

		BugFetchResponse bugResponse = new BugFetchResponse();

		bugResponse = bugzillaGateway.getBugDetails("" + bugId + "");
		if (bugResponse != null && bugResponse.getBug() != null) {
			bugs = new ArrayList<Bug>(Arrays.asList(bugResponse.getBug()));
			System.out.println("BugDetailsCollector retrieved: " + bugs.size());
		} else {
			System.out.println("bugResponse NULL in BugDetailsCollector");
		}

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
