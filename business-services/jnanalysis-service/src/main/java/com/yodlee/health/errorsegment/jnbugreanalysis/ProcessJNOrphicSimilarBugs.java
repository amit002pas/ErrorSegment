/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.jnbugreanalysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.RequestBugUpdate;
import com.yodlee.health.errorsegment.gateway.jn.IJNBugRRest;
import com.yodlee.health.errorsegment.repository.jn.bugreanalysis.JNChironBugTrackerRepository;
import com.yodlee.health.errorsegment.resources.jn.bugreanalysis.JNChironBugTracker;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;

/**
 * This class consists a set of method for processing Jaggernaut and Orphics
 * similar bugs. All the similar bugs which is closed from Orphics ends will be
 * communicated to Jaggernaut team for re-analysis to close the bugs.
 * 
 * @author vchhetri
 *
 */
@Named
public class ProcessJNOrphicSimilarBugs {

	@Inject
	private JNChironBugTrackerRepository jnChironBugTrackerRepository;
	@Inject
	private IJNBugRRest jnBugRRest;
	@Inject
	private BugzillaGateway bugzillaGatewayImpl;

	/**
	 * This method is to complete a process of sending a similar bugids to JN for
	 * re-analysis, it has 4 sub-process. 1. To save all similar bugs to MongoDB. 2.
	 * To pick all bugs from DB which is not yet sent to JN. 3. Filter all bugids.
	 * 4. Send List of bugid to JN.
	 * 
	 * @param similarJnBugs
	 *            It holds list of Map<k,v> object. k-> synthetic bug , v-> set of
	 *            bugid
	 */
	public void sendSimilarsBugsToJN(Map<String, Set<Integer>> similarJnBugs) {
		saveJNSimilarBugs(similarJnBugs);
		List<JNChironBugTracker> bugsForJN = getBugsFromDB();
		List<Integer> bugIdsForJN = filterBugId(bugsForJN);
		sendBugIdsToJNService(bugIdsForJN);
	}

	/**
	 * Method to save similar bugs in MongoDB after filtering non existing
	 * documents.
	 * 
	 * @param similarJnBugs
	 *            It holds list of Map<k,v> object. k-> synthetic bug , v-> set of
	 *            bugid
	 */
	private void saveJNSimilarBugs(Map<String, Set<Integer>> similarJnBugs) {
		List<JNChironBugTracker> jnChironBugTrackerList = new ArrayList<>();
		similarJnBugs.forEach((k, v) -> {
			v.forEach(bugZillaId -> {
				JNChironBugTracker jnChironBugTracker = new JNChironBugTracker();
				jnChironBugTracker.setJuggernautbugZillaBugId(bugZillaId);
				jnChironBugTracker.setOrphicsyntheticBugid(k);
				jnChironBugTracker.setCreatedDate(new Date());
				jnChironBugTracker.setResponseMessage("");

				jnChironBugTrackerList.add(jnChironBugTracker);
			});
		});
		List<JNChironBugTracker> filteredJNChironBugTrackerList = filterDuplicateAndClosedBugs(jnChironBugTrackerList);
		jnChironBugTrackerRepository.saveJNChironBugTracker(filteredJNChironBugTrackerList);
	}

	/**
	 * Method to filter duplicate bug.
	 * 
	 * @param jnChironBugTrackerList
	 *            Expecting list of JNChironBugTracker pojo.
	 * @return List<JNChironBugTracker> This returns list of closed and non exist
	 *         bugid.
	 */
	private List<JNChironBugTracker> filterDuplicateAndClosedBugs(List<JNChironBugTracker> jnChironBugTrackerList) {
		List<JNChironBugTracker> filteredJNChironBugTrackerList = new ArrayList<>();
		List<Integer> bugZillaIdList = filterBugId(jnChironBugTrackerList);
		if (!bugZillaIdList.isEmpty()) {
			List<JNChironBugTracker> availJNChironBugTrackers = jnChironBugTrackerRepository
					.getBugsTrackerFromBugzillaIds(bugZillaIdList);
			bugZillaIdList.clear();
			availJNChironBugTrackers.forEach(bugs -> {
				bugZillaIdList.add(bugs.getJuggernautbugZillaBugId());
			});
			jnChironBugTrackerList.forEach(bugs -> {
				if (!bugZillaIdList.contains(bugs.getJuggernautbugZillaBugId())) {
					filteredJNChironBugTrackerList.add(bugs);
				}
			});
		}
		return filteredJNChironBugTrackerList;
	}

	/**
	 * This method will return list of bugids from list of JNChironBugTracker pojo.
	 * 
	 * @param jnChironBugTrackerList
	 *            Expecting List of JNChironBugTracker pojo
	 * @return bugZillaIdList This return list of bugzilla bugid
	 */
	private List<Integer> filterBugId(List<JNChironBugTracker> jnChironBugTrackerList) {
		List<Integer> bugZillaIdList = new ArrayList<>();
		jnChironBugTrackerList.forEach(bug -> {
			bugZillaIdList.add(bug.getJuggernautbugZillaBugId());
		});
		return bugZillaIdList;
	}

	/**
	 * Method to pick bugs from MongoDB which are not sent to JN for re-analysis.
	 * 
	 * @return This return list of JNChironBugTracker pojo.
	 */
	public List<JNChironBugTracker> getBugsFromDB() {
		return jnChironBugTrackerRepository.getBugsBasedOnStatus();
	}

	/**
	 * Method to send bugids one by one to JN for initiating re-analysis.
	 * 
	 * @param bugIdsForJN
	 *            This expect List of bugid which will be sent to JN for re-analysis
	 */
	public void sendBugIdsToJNService(List<Integer> bugIdsForJN) {
		if (null != bugIdsForJN && !bugIdsForJN.isEmpty()) {
			bugIdsForJN.forEach(bug -> {
				Response response = jnBugRRest.commJnForBugReanalysis(bug);
				if (response.getStatus() == 200) {
					String analysisRequestID = null;
					try {
						JSONObject json = new JSONObject(response.readEntity(String.class));
						analysisRequestID = json.get("analysisRequestID").toString();
					} catch (JSONException e) {
						updateSentToJNStatusService(bug.toString(), e.getMessage());
					}
					/*
					 * bugzillaGatewayImpl.addComment(bug.toString(),
					 * JnConstant.COMM_TO_JN_FOR_REANALYSIS);
					 */
					RequestBugUpdate requestBugUpdate = new RequestBugUpdate();
					requestBugUpdate.setComment(JuggernautAnalysisConstants.COMM_TO_JN_FOR_REANALYSIS);
					bugzillaGatewayImpl.updateBugField(bug.toString(), requestBugUpdate);
					updateSentToJNStatusService(bug.toString(), analysisRequestID.toString());
				} else {
					String responseMessage = response.readEntity(String.class);
					updateSentToJNStatusService(bug.toString(), responseMessage);
				}
			});
		}
	}

	/**
	 * Method to update document updating AnalysisRequestID as acknowledgement.
	 * 
	 * @param bugIdsForJN
	 * @param analysisRequestID
	 */
	private void updateSentToJNStatusService(String bugIdsForJN, String responseMessage) {
		jnChironBugTrackerRepository.updateSentToJNStatus(bugIdsForJN, responseMessage);
	}
}
