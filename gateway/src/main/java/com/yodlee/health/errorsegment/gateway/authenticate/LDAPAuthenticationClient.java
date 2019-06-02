/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.authenticate;

import java.util.Set;

import javax.ws.rs.core.Response;

public interface LDAPAuthenticationClient {

	/**
	 * Authenticating user, using firemem authentication
	 * @param authenticationrequest
	 * @return Response
	 * @throws Exception
	 */
	public Response authenticateuser(String authenticationrequest, boolean isFiremem) throws Exception;
	
	/**
	 * Authenticating user using the authentication-service of tools
	 * @param authenticationrequest
	 * @return Response
	 * @throws Exception
	 */
	public Response authenticateRequest(String authenticationrequest) throws Exception;
	
	/**
	 * Validates the token using the authentication-service of tools
	 * @param authorizationrequest
	 * @return Response
	 * @throws Exception
	 */
	public Response validateToken (String authorizationrequest) throws Exception;
	
	/**
	 * Authorizes user by security group check
	 * @param userRoles
	 * @return true if belongs to security group
	 * @throws Exception
	 */
	public Boolean authorizeUser (Set<String> userRoles) throws Exception;

}
