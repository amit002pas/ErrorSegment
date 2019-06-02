/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.util;

public final class IntegrationConstant {

	private IntegrationConstant() {
	}

    //Firemem RestUrl
	public static final String REST_QC_AUTHENTICATION_URL = "https://authentication-service.tools.yodlee.com/authentication/login";
    public static final String REST_FIREMEM_AUTHENTICATION_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";
    public static final String BETA_REST_AUTHENTICATION_URL ="https://beta-hammer.tools.yodlee.com/hammer/R/A/L";

    //Firemem RestHeaders
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_CONTENT_TYPE = "Content-type";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_JSON_VALUE = "application/json";
	
	//LDAP Authentication url
	public static final String FIREMEM_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";
	public static final String QC_URL = "https://authentication-service.tools.yodlee.com/authentication/login";
	public static final String AUTHENTICATION_URL = "https://authentication-service.tools.yodlee.com/authentication/login";
	public static final String AUTHORIZATION_URL = "https://authentication-service.tools.yodlee.com/authentication/token/validate/groups";
	public static final String TOOLS_FM_PROD = "Tools-Firemem-Prod";
	
}
