/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.repository.jn.bugreanalysis;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yodlee.health.errorsegment.resources.jn.bugreanalysis.JNChironBugTracker;

/**
 * @author vchhetri
 *
 */
@Named
public class JNChironBugTrackerRepository {
	@Inject
	private MongoOperations mongoOperations;

	private static final String BUG_ZILLA_BUGID = "juggernautbugZillaBugId";
	private static final String RESPONSE_MSG= "responseMessage";
	private static final String SENT_TO_JN_DATE="sentToJNDate";

	/**
	 * This method is to save JNChironBugTracker
	 * @param jnChironBugTrackers is a list of JNChironBugTracker class
	 */
	public void saveJNChironBugTracker(List<JNChironBugTracker> jnChironBugTrackers) {
		jnChironBugTrackers.forEach(bugs -> {			
				mongoOperations.save(bugs);			
		});		
	}
	/**
	 * This method do a find query based on bugids passed as parameter
	 * @param bugIds List of bugid
	 * @return List<JNChironBugTracker>
	 */
	public List<JNChironBugTracker> getBugsTrackerFromBugzillaIds(List<Integer> bugIds) {
		Query query = new Query();
		query.addCriteria(Criteria.where(BUG_ZILLA_BUGID).in(bugIds));
		return mongoOperations.find(query, JNChironBugTracker.class);
	}
	/**
	 * Tis method is to find all documents from database where responseMsg is empty.
	 * @return List<JNChironBugTracker>
	 */
	public List<JNChironBugTracker> getBugsBasedOnStatus() {
		Query query = new Query();
		query.addCriteria(Criteria.where(RESPONSE_MSG).is(""));		
		return mongoOperations.find(query, JNChironBugTracker.class);		
	}
	/**
	 * Method to update responseMessage of document based on bug id.
	 * @param bugIdsForJN
	 * @param responseMessage
	 */
	public void updateSentToJNStatus(String bugIdsForJN,String responseMessage) {
		Query query = new Query();
		Update update = new Update();
		query.addCriteria(Criteria.where(BUG_ZILLA_BUGID).in(Integer.parseInt(bugIdsForJN)));
		update.set(RESPONSE_MSG, responseMessage);
		update.set(SENT_TO_JN_DATE, new Date());
		mongoOperations.updateMulti(query, update, JNChironBugTracker.class);		
	}
	

}
