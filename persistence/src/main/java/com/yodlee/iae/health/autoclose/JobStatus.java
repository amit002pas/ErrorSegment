/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.autoclose;

/**
 * @author vchhetri and mkumar10
 *
 */
public enum JobStatus {
	
	READY(0,"READY"),INPROGRESS(1,"INPROGRESS"),SUCCESS(3,"SUCCESS"),FAILURE(4,"FAILURE");
	private int id;
	private String name;
	
	private JobStatus(int id,String name){
		this.id=id;
		this.name=name;
	}
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}

}
