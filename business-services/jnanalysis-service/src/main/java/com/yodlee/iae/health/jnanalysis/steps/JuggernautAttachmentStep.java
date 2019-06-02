/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.jnanalysis.steps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.yodlee.health.errorsegment.datatypes.jnanalysis.Attachment;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JNAnalysisTriggeredItem;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautFinalResponse;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.RequestBugUpdate;
import com.yodlee.iae.health.gateway.bugzillagateway.BugzillaGateway;
import com.yodlee.iae.health.gateway.jnanalysis.JuggernautGateway;
import com.yodlee.iae.health.jnanalysis.util.JuggernautAnalysisConstants;
import com.yodlee.iae.health.repository.juggernaut.JuggernautRepository;

@Named
@Scope("prototype")
public class JuggernautAttachmentStep extends ServiceIO {

	@Inject
	JuggernautGateway juggerNautGateway;

	@Inject
	private BugzillaGateway bugzillaGateway;

	@Inject
	private JuggernautRepository jnRepository;

	private List<JNAnalysisTriggeredItem> triggeredItemsList = null;
	
	Logger logger = LoggerFactory.getLogger(JuggernautAttachmentStep.class);


	public void fetchLatestBugs() {

		List<JNAnalysisTriggeredItem> allTriggeredItemsList = jnRepository.getTriggeredBugs();
		this.triggeredItemsList = new ArrayList<JNAnalysisTriggeredItem>();
		this.triggeredItemsList = allTriggeredItemsList;
		System.out.println("Size Retrieved from :"+this.triggeredItemsList.size());

	}

	@Override
	public void accept(Object arg0) {

	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void executeImpl() {
		fetchLatestBugs();
		pollingJNTrigerredData();

	}

	public void pollingJNTrigerredData() {

		if (!triggeredItemsList.isEmpty()) {
			Iterator<JNAnalysisTriggeredItem> iterator = null;
			
				String token = juggerNautGateway.getSecurityToken();
				for ( iterator = triggeredItemsList.iterator(); iterator.hasNext();) {
					
					try {
					JNAnalysisTriggeredItem juggerNautAnalysis = iterator.next();
					System.out.println("For JN:"+juggerNautAnalysis.toString());
					JuggerNautFinalResponse iatGetReponse = juggerNautGateway
							.fetchIATResponse(juggerNautAnalysis.getAnalysisRequestId(), token);
					String bugId = juggerNautAnalysis.getBugId();
					if ((iatGetReponse.getStatus().equals(JuggernautAnalysisConstants.COMPLETED))
							&& (null != iatGetReponse.getPdfURI() || iatGetReponse.getPdfURI().isEmpty())) {
						String url = iatGetReponse.getPdfURI();
						byte[] base64bytes = Base64.encodeBase64(juggerNautGateway.fetchByteArray(url));
						Attachment attach = new Attachment();
						attach.setAttachmentName(JuggernautAnalysisConstants.JN_PDF);
						attach.setAttachment(base64bytes);
						bugzillaGateway.updateAttachmentJN(attach, bugId);
						RequestBugUpdate requestBugUpdate = new RequestBugUpdate();
						requestBugUpdate.setComment(JuggernautAnalysisConstants.JN_PDF_Message + url);
						bugzillaGateway.updateBugField(bugId, requestBugUpdate);
						jnRepository.updateJNStatus(bugId);
						iterator.remove();
					}

					} catch (Exception e) {
						iterator.remove();
						//logger.info("Exception in polling:"+e);
						System.out.println("Exception in polling:");
						e.printStackTrace();
					}
				}

			

		}
	}

	@Override
	public void mapInput() {

	}

	@Override
	public void mapOutput() {

	}

	@Override
	public void validate() {

	}

}
