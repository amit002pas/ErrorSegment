/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.refreshstats.schedular;

import javax.inject.Named;

import com.yodlee.mel.MasterElectionCallable;

@Named
public class MEComponent extends MasterElectionCallable {

	private Boolean isMaster;

	@Override
	public void designatedMaster() {
		isMaster = true;
		System.out.println("########### ELECTED AS MASTER!!");
	}

	@Override
	public void designatedSlave() {
		isMaster = false;
		System.out.println("########### ELECTED AS SLAVE!!");
	}

	public Boolean getMasterStatus() {
		return isMaster;
	}

}
