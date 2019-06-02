/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.repository.juggernaut;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.iae.health.repository.constants.ConstantsUtils;

@Named
public class JuggernautRepository {
	static Logger logger = LoggerFactory.getLogger(JuggernautRepository.class);

	@Inject
	private MongoOperations mongoOperations;
	
	public void updateJNStatus(String bugid){
		Query query = new Query();
		query.addCriteria(Criteria.where(ConstantsUtils.BUG_ID).is(bugid)); 
        Update update = new Update();
		update.set(ConstantsUtils.STATUS, ConstantsUtils.COMPLETED);
		mongoOperations.updateMulti(query, update, JNAnalysisTriggeredItem.class,
				ConstantsUtils.JNANALYSISTRIGGERDATA );  
	}
	
	public void updateBugStatus(JNAnalysisTriggeredItem jnAnalysisTriggeredItem) {
		Query query = new Query();
		query.addCriteria(Criteria.where(ConstantsUtils.BUG_ID).is(jnAnalysisTriggeredItem.getBugId()));
		Update update = new Update();
		update.set(ConstantsUtils.STATUS, jnAnalysisTriggeredItem.getStatus());
		update.set(ConstantsUtils.ANALYSISREQUEST_ID, jnAnalysisTriggeredItem.getAnalysisRequestId());
		update.set(ConstantsUtils.REQUEST_SOURCE, jnAnalysisTriggeredItem.getRequestSource());
		update.set(ConstantsUtils.DATE, new Date());
		mongoOperations.upsert(query, update, JNAnalysisTriggeredItem.class);
	}
	
	public void saveAnalyzerId(JNAnalysisTriggeredItem jnAnalysisTriggeredItem){
		/*Query query = new Query();
		query.addCriteria(Criteria.where(ConstantsUtils.BUG_ID).is(jnAnalysisTriggeredItem.getBugId()));

		Update update = new Update();
		update.set(ConstantsUtils.STATUS, ConstantsUtils.IN_PROGRESS);
		update.set(ConstantsUtils.BUG_SUMMARY, jnAnalysisTriggeredItem.getBugSummary());
		update.set(ConstantsUtils.ANALYSISREQUEST_ID, jnAnalysisTriggeredItem.getAnalysisRequestId());
		update.set(ConstantsUtils.SECURITY_TOKEN, jnAnalysisTriggeredItem.getSecurityToken());
		update.set(ConstantsUtils.COMPONENT, jnAnalysisTriggeredItem.getComponent());
		update.set(ConstantsUtils.PRODUCT, jnAnalysisTriggeredItem.getProduct());
		update.set(ConstantsUtils.VERSION, jnAnalysisTriggeredItem.getVersion());
		update.set(ConstantsUtils.REQUEST_SOURCE,jnAnalysisTriggeredItem.getRequestSource());
		update.set(ConstantsUtils.DATE, new Date());*/
		try {
		mongoOperations.save(jnAnalysisTriggeredItem);
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Id Already exist");
		}
	}
	
	  public  List<JNAnalysisTriggeredItem>  getJNAnalysisTriggeredItem(String bugId){
		Query query = new Query();
		query.addCriteria(Criteria.where(ConstantsUtils.BUG_ID).is(bugId));
		query.fields().include(ConstantsUtils.BUG_ID);
		query.fields().include(ConstantsUtils.STATUS);
		List<JNAnalysisTriggeredItem> triggeredItemsList = mongoOperations.find(query, 
				JNAnalysisTriggeredItem.class,ConstantsUtils.JNANALYSISTRIGGERDATA);
		return triggeredItemsList;
	}

	  public List<JNAnalysisTriggeredItem> getTriggeredBugs(){
	  Query query = new Query(Criteria.where(ConstantsUtils.STATUS).is(ConstantsUtils.IN_PROGRESS));
	  List<JNAnalysisTriggeredItem> allTriggeredItemsList = mongoOperations.find(
				query, JNAnalysisTriggeredItem.class);
	  return allTriggeredItemsList;
	  }
}
