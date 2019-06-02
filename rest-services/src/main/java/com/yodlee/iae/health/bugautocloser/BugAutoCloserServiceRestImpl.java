/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.bugautocloser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import com.yodlee.iae.health.autoclose.AnalysisDetails;
import com.yodlee.iae.health.autoclose.BugAutoCloserSchedularJob;
import com.yodlee.iae.health.repository.bugautocloser.AutoCloseRepository;
import com.yodlee.iae.health.repository.bugautocloser.BugAutoCloserAuditRepo;

/**
 * @author mkumar10, vchhetri
 *
 */
@Named
public class BugAutoCloserServiceRestImpl implements BugAutoCloserServiceRest {

	@Inject
	BugAutoCloserAuditRepo bugAutoCloserAuditRepo;

	@Inject
	AutoCloseRepository autoCloseRepository;


	@Override
	public Response getAuditDetailsForSyntheticBug(String bugId) {
		BugAutoCloserSchedularJob bugAutoCloserSchedularJob =  autoCloseRepository.getById(bugId);
		Map<String, Object> responseMap = new HashMap<>();
		if(bugAutoCloserSchedularJob!=null) {
			List<AnalysisDetails> analysisDetailsList = bugAutoCloserSchedularJob.getAnalysisDetails();
			responseMap.put("synthBug", bugAutoCloserSchedularJob);
			for(AnalysisDetails analysisDetailsObj : analysisDetailsList) {
				responseMap.put(analysisDetailsObj.getAnalysisId(), bugAutoCloserAuditRepo.getBugAutoCloserAuditById(analysisDetailsObj.getAnalysisId()));
			}
		}
		return Response.ok(responseMap)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Max-Age", "3600")
				.header("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token")
				.header("Access-Control-Expose-Headers", "xsrf-token") 
				.build();
		
	}


}

