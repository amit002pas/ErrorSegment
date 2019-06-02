/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.util;

import java.util.ArrayList;
import java.util.List;

public class ErrorSegmentationUtil {

	private static List<String> intermittentStacktraceList=new ArrayList<>();
	
	private static int[] uarErrors=new int[]{
			402,405,406,407,414,428,429,508,518,522,523,518
	};
	
	static{
		intermittentStacktraceList.add("UnreachableBrowserException");
		intermittentStacktraceList.add(".PDFExtracter.convertPDFToBase64(PDFExtracter.java");
		intermittentStacktraceList.add("selenium.TimeoutException: timeout");
		}
	
	public static int isStacktraceIntermittent(String stackTrace){
		
		for(String stacktraceChecker:intermittentStacktraceList){
			if(stackTrace.contains(stacktraceChecker))
				return 1;
		}
		return 0;
	}
	
	public static boolean isUarError(int errorCode){
		
		for(int errorFromList : uarErrors){
			if(errorFromList==errorCode){
				return true ;
			}
		}
		
		return false ;
	}
	
	public static boolean isSuccessError(int error) {
		return (error == 0) ? true : false;
	}

		
}
