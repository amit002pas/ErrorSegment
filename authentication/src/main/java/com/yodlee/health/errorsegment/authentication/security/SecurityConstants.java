package com.yodlee.health.errorsegment.authentication.security;

/**
 * @author mboraiah
 *
 */
public class SecurityConstants {

	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final Long expiration = 180l; // 3 Hours
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	public static final String LOGIN = "/iat/login";
	public static final String LOGIN_PAGE = "/login";
	public static final String VERIFICATION = "/Verification";

	public static final String FAILED = "failed";
	public static final String TOKEN = "token";
	public static final String SUCCESS = "success";
	public static final String AUTHENTICATION_FAILED = "Authentication Failed";
	public static final String AUTHENTICATION_SUCCESS = "Authentication Success";
	public static final String AUTHORIZATION_RESTRICTED = "You are not privileged to request this resource. Please check with your manager to get the access for Juggernaut.";
	public static final String AUTHENTICATION_RESTRICTED = "Invalid Credentials";
	public static final String CREDENTIALS_MISSING = "Credentials are Missing";
	public static final String AUTHENTICATION_NOT_SUPPORTED = "Authentication method not supported: ";
	public static final String AUTHENTICATION_MISSING = "Authentication Details are missing";

	public static final String TOKEN_ISSUE = "Token Issue";

	public static final String rootPath = "/";
	public static final String loginrootPath = "/R/A/L";

	public final static String IP = "ip";

	public static final String CIP = "CIP";
	public static final String RIP = "RIP";
	public static String authHeader = "X-AUTH-TOKEN";
	public static final boolean ipMatchDisabled = false;

	protected static final String AUTHORIZATION = "Authorization";
	protected static final String AUTHENTICATION = "Authentication";
	protected static final String CLAIMS = "cliams";

}
