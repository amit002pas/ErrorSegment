package com.yodlee.health.errorsegment.gateway.authenticate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.yodlee.health.errorsegment.gateway.util.IntegrationConstant;

@Named("ProdLDAP")
public class LDAPAuthenticationClientImpl implements LDAPAuthenticationClient{

private String FIREMEM_URL = IntegrationConstant.FIREMEM_URL;
	
	private String QC_URL = IntegrationConstant.QC_URL;
	
	private String AUTHENTICATION_URL = IntegrationConstant.AUTHENTICATION_URL;
	
	private String AUTHORIZATION_URL = IntegrationConstant.AUTHORIZATION_URL;
	
	private List<String> groups = Arrays.asList(IntegrationConstant.TOOLS_FM_PROD);
	

	public Response authenticateuser(String authenticationrequest, boolean isFiremem) throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = null;
		if(null == QC_URL) {
			QC_URL = IntegrationConstant.REST_QC_AUTHENTICATION_URL;
		}
		if(null == FIREMEM_URL) {
			FIREMEM_URL = IntegrationConstant.REST_FIREMEM_AUTHENTICATION_URL;
		}
		if(isFiremem)
			target = client.target(FIREMEM_URL);
		else
			target = client.target(QC_URL);
		
		return target.request().post(Entity.json(authenticationrequest));
	}

	@Override
	public Response authenticateRequest(String authenticationrequest)
			throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(AUTHENTICATION_URL);
		
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);

		return target.request().headers(multivaluedMap).post(Entity.json(authenticationrequest));
	}

	@Override
	public Response validateToken(String token) throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(AUTHORIZATION_URL);
		
		MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap<String, Object>();
		multivaluedMap.add(IntegrationConstant.HEADER_ACCEPT, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_CONTENT_TYPE, IntegrationConstant.HEADER_JSON_VALUE);
		multivaluedMap.add(IntegrationConstant.HEADER_AUTHORIZATION, "Bearer" + " " + token);

		return target.request().headers(multivaluedMap).get();
	}

	@Override
	public Boolean authorizeUser (Set<String> userRoles) throws Exception {
		boolean isAllowed = false;
		if(userRoles == null || userRoles.isEmpty()) {
			return isAllowed;
		}
		
		if(groups == null || groups.isEmpty()) {
			return isAllowed;
		}
		
		for(String userRole: userRoles) {
			for(String group:groups) {
				String role = userRole.replace("ROLE_", "");
				if(group.equals(role)) {
					isAllowed = true;
					break;
				}
			}
			if(isAllowed) {
				break;
			}
		}
		return isAllowed;
	}
}
