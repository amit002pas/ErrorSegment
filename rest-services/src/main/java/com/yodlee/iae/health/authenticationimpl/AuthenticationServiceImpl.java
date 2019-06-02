package com.yodlee.iae.health.authenticationimpl;

import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.authentication.response.CustomResponse;
import com.yodlee.health.errorsegment.authentication.security.SecurityConstants;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClient;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;
import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;
import com.yodlee.iae.commons.authentication.model.AuthorizationResponse;
import com.yodlee.iae.framework.model.communication.CommunicationStatus;
import com.yodlee.iae.health.authentication.IAuthenticationService;

@Service
@Component
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Inject
	@Qualifier("ProdLDAP")
	private LDAPAuthenticationClient lDAPAuthenticationClient;

	@Override
	public Response login(HttpServletRequest request, String authentication) throws Exception {
		Gson gson = new Gson();
		AuthenticationRequest authenticationRequest = gson.fromJson(authentication, AuthenticationRequest.class); 
		
		AuthenticationResponse finalresponse = new AuthenticationResponse();
		AuthenticationResponse resp;
		AuthorizationResponse authorizationResponse;
		Set<String> userGroups = null;
		if (null == authenticationRequest) {
			finalresponse.setSuccess(false);
			finalresponse.setCommunicationStatus(CommunicationStatus.FAILURE);
			finalresponse.setUserMessage(SecurityConstants.AUTHENTICATION_FAILED);
			finalresponse.setFailureReason(SecurityConstants.CREDENTIALS_MISSING);
			return Response.status(Response.Status.UNAUTHORIZED).entity(finalresponse)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Max-Age", "3600")
					.header("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token")
					.header("Access-Control-Expose-Headers", "xsrf-token")
					.build();
		} else {
			Response response = lDAPAuthenticationClient.authenticateRequest(authenticationRequest.toJSON());
			resp = response.readEntity(AuthenticationResponse.class);
			boolean isAuthenticated = false;
			boolean isAuthorized = false;
			if (resp.getToken() != null) {
				// Authorizing user
				Response authResponse = lDAPAuthenticationClient.validateToken(resp.getToken());
				authorizationResponse = authResponse.readEntity(AuthorizationResponse.class);

				if (authorizationResponse.isTokenValid() && !authorizationResponse.isTokenExpired()) {
					isAuthenticated = true;
					userGroups = authorizationResponse.getGroups();
					// Security Group Check
					isAuthorized = lDAPAuthenticationClient.authorizeUser(userGroups);
					if (isAuthorized) {
						finalresponse.setSuccess(true);
						finalresponse.setToken(resp.getToken());
						finalresponse.setDisplayName(authenticationRequest.getUsername());
					}
				}
			}

			if (!isAuthenticated) {
				finalresponse.setUserMessage(SecurityConstants.AUTHENTICATION_FAILED);
				finalresponse.setFailureReason(SecurityConstants.AUTHENTICATION_RESTRICTED);
				return Response.status(Response.Status.UNAUTHORIZED).entity(finalresponse)
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Max-Age", "3600")
						.header("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token")
						.header("Access-Control-Expose-Headers", "xsrf-token").build();
			}

			if (!isAuthorized) {
				finalresponse.setUserMessage(SecurityConstants.AUTHENTICATION_FAILED);
				finalresponse.setFailureReason(SecurityConstants.AUTHORIZATION_RESTRICTED);
				return Response.status(Response.Status.UNAUTHORIZED).entity(finalresponse)
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
						.header("Access-Control-Max-Age", "3600")
						.header("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token")
						.header("Access-Control-Expose-Headers", "xsrf-token").build();
			}
		}
		
		CustomResponse customResponse = new CustomResponse();
		customResponse.setHttpStatus(resp.getHttpStatus());
		customResponse.setCommunicationStatus(resp.getCommunicationStatus());
		customResponse.setSuccess(true);
		customResponse.setUserMessage(resp.getUserMessage());
		customResponse.setFailureReason(resp.getFailureReason());
		customResponse.setDisplayName(resp.getDisplayName());
		customResponse.setUserName(authenticationRequest.getUsername());
		customResponse.setToken(resp.getToken());
		customResponse.setTokenValid(true);;
		customResponse.setTokenExpired(false);
		customResponse.setGroups(userGroups);
		
		return Response.ok().entity(customResponse).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Max-Age", "3600")
				.header("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token")
				.header("Access-Control-Expose-Headers", "xsrf-token").build();
	}

}
