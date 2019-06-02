/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.jn;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.datatypes.jnanalysis.JuggerNautInputRequest;
import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;

/**
 * This class is to send all similar bugs id to Jaggernaut team for re-analysis.
 * @author vchhetri
 *
 */
@Named
public class JNBugRRestImpl implements IJNBugRRest{

	@Value("${user-username}")
	private String jnUserName;

	@Value("${password-password}")
	private String jnPassword;

	@Inject
	private JNTokenGenerator tokenGenerator;
	
	@Override
	public Response commJnForBugReanalysis(Integer bugIdsForJN) {
		Gson gson = new Gson();//GatewayConstants.IATAuthenticationURL
		String token = tokenGenerator.generateToken(GatewayConstants.IATAuthenticationURL, jnUserName,
				jnPassword);
		JuggerNautInputRequest juggerNautInputRequest = new JuggerNautInputRequest(bugIdsForJN.toString(), "2", "",false);
		String json = gson.toJson(juggerNautInputRequest);
		String target_url = GatewayConstants.IATAnalysisURL;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(target_url);
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add("Accept", "application/json");
		multivaluedMap.add("Content-type", "application/json");
		multivaluedMap.add("Authorization", "Bearer" + " " + token);
		System.out.println("@@@@@@Request:"+multivaluedMap);
		Response response = target.request().headers(multivaluedMap).post(Entity.json(json));
		System.out.println("@@@@@@Response:"+response.getStatus()+" ");
		return response;
	}

}
