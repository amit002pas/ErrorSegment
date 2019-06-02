/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
Â *
Â * This software is the confidential and proprietary information of Yodlee, Inc.Â 
Â * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.jnanalysis.rest.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.BugFetchResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisRequest;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisResponseEnum;
import com.yodlee.health.errorsegment.jnanalysis.rest.IJNAnalysisServiceRest;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.jnanalysis.service.JNBugzillaBugAnalysisService;
import com.yodlee.iae.health.jnanalysis.service.JNSyntheticBugAnalysisServiceImpl;

@Named
public class JNAnalysisServiceRestImpl implements IJNAnalysisServiceRest {

	@Inject
	private JNSyntheticBugAnalysisServiceImpl jnSyntheticBugAnalysisServiceImpl;
	
	@Inject
	private JNBugzillaBugAnalysisService jnBugzillaBugAnalysisService;
	
	@Inject
	private BugzillaGateway bugzillaGateway;
	
	@Override
	public Response bugAnalysis(String jnAnalysisRequestStr) {
		Gson gson=new Gson();
		JNAnalysisRequest jnAnalysisRequest=gson.fromJson(jnAnalysisRequestStr.toString(), JNAnalysisRequest.class);
		if(!jnAnalysisRequest.isSyntheticBug()){	
			jnBugzillaBugAnalysisService.process(jnAnalysisRequest.getBugID());
			JNAnalysisResponse jnAnalysisResponse = (JNAnalysisResponse) jnBugzillaBugAnalysisService.get();			
			return Response.ok(jnAnalysisResponse).build();
		}				
		else{  
			jnSyntheticBugAnalysisServiceImpl.setInput(jnAnalysisRequest.getBugID());
			jnSyntheticBugAnalysisServiceImpl.execute();			
			JNAnalysisResponse jnAnalysisResponse = jnSyntheticBugAnalysisServiceImpl.getOutput();
			if(jnAnalysisResponse.getAnalysisResponse().getId()!=JNAnalysisResponseEnum.InvalidRequest.getId()){
				BugFetchResponse bugFetchResponse = bugzillaGateway.getBugDetails(jnAnalysisRequest.getBugID());
				if(null!=bugFetchResponse && null!=bugFetchResponse.getBug() && null !=bugFetchResponse.getBug().getMiscellaneousFields()){
					jnAnalysisResponse.setJnAnalysisResponseId(bugFetchResponse.getBug().getMiscellaneousFields().getJnAnalysisId());
				}
			}
			System.out.println("Response from JNanalysis(Single):"+jnAnalysisResponse);
		    return Response.ok(jnAnalysisResponse).build();
		}				
	} 
 }