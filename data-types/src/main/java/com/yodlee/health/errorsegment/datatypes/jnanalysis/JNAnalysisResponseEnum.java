/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.datatypes.jnanalysis;

public enum JNAnalysisResponseEnum {
	
	Triggered(0), InProgress(1), InvalidRequest(2);
    private int id;
    
    private JNAnalysisResponseEnum(int id){
          this.id = id;
    }
    public int getId(){
          return this.id;
    }
}
