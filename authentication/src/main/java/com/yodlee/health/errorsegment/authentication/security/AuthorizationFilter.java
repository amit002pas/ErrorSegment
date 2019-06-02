package com.yodlee.health.errorsegment.authentication.security;

import static com.yodlee.health.errorsegment.authentication.security.SecurityConstants.HEADER_STRING;
import static com.yodlee.health.errorsegment.authentication.security.SecurityConstants.TOKEN_PREFIX;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_EXPECTATION_FAILED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.yodlee.health.errorsegment.authentication.security.token.handler.FailureHandler;
import com.yodlee.health.errorsegment.authentication.security.token.handler.TokenException;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClientImpl;
import com.yodlee.iae.commons.authentication.model.AuthorizationResponse;

/**
 * Authorizing rest-api resource through class {@link BasicAuthenticationFilter}
 * by provided JWT token.
 *
 * @author mboraiah
 *
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

	protected FailureHandler failureHandler;

	protected LDAPAuthenticationClientImpl lDAPAuthenticationClient;

	/**
	 * Constructs a <code>AuthorizationFilter</code>.
	 *
	 * @param authManager
	 */
	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	public void setTokenHandler(
			final LDAPAuthenticationClientImpl lDAPAuthenticationClientImpl) {
		this.lDAPAuthenticationClient = lDAPAuthenticationClientImpl;
	}

	public void setFailureHandler(final FailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		final String route = req.getServletPath();
		final String pathInfo = req.getPathInfo();
		final boolean validRoute = route.contains(SecurityConstants.rootPath) ? true : false;
		if (!validRoute) {
			chain.doFilter(req, res);
			return;
		}
		try {
			final boolean validLoginRoute = route.equals(SecurityConstants.loginrootPath) ? true : false;
			UsernamePasswordAuthenticationToken authentication = null;
			if (!validLoginRoute) {
				String username = null;
				try {
					username = this.authourizeRequest(req, res, chain, route, pathInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				authentication = this.getAuthentication(req, username);
			} else {
				authentication = new UsernamePasswordAuthenticationToken(
						StringUtils.EMPTY, null, new ArrayList<>());
			}
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (final TokenException ex) {
			this.failureHandler.onInvalidToken(req, res, ex);
		}
	}

	/**
	 * For getting the requested token.
	 * 
	 * @param request
	 * @param username 
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(
			HttpServletRequest request, String username) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String user = username;
			if (user != null) {
				UsernamePasswordAuthenticationToken users = new UsernamePasswordAuthenticationToken(
						user, null, new ArrayList<>());
				return users;
			}
		}
		return null;
	}

	/**
	 * 
	 * For validating requested token.
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @param route
	 * @return 
	 * @return
	 * @throws Exception
	 */
	private String authourizeRequest(final HttpServletRequest request,
			final HttpServletResponse response, final FilterChain filterChain,
			final String route, final String pathInfo) throws Exception {

		final boolean validRoute = route.contains(SecurityConstants.rootPath) ? true : false;
		if (!validRoute) {
			throw new TokenException(SC_BAD_REQUEST,
					TokenException.Reason.RESOURCE_PATH_ERROR);
		}

		String header = request.getHeader(HEADER_STRING);	
		
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			throw new TokenException(SC_BAD_REQUEST,
					TokenException.Reason.AUTHOURIZATION_MISSING);
		}

		String authorizationHeader = request
				.getHeader(SecurityConstants.AUTHORIZATION);
		final String authenticationHeader = request
				.getHeader(SecurityConstants.AUTHENTICATION);
		String tokenHeader = null;
		if (StringUtils.isBlank(authorizationHeader)) {
			tokenHeader = (StringUtils.isBlank(authenticationHeader)) ? null
					: authenticationHeader;
		} else {
			tokenHeader = authorizationHeader;
		}
		if (StringUtils.isBlank(tokenHeader)) {
			throw new TokenException(SC_BAD_REQUEST,
					TokenException.Reason.TOKEN_MISSING);
		}

		if (!tokenHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			throw new TokenException(SC_EXPECTATION_FAILED,
					TokenException.Reason.BEARER_MISSING);
		}

		final String token = tokenHeader.replace(TOKEN_PREFIX, "");

		Response authRresponse;

		authRresponse = lDAPAuthenticationClient.validateToken(token);
		AuthorizationResponse authorizationResponse = authRresponse
				.readEntity(AuthorizationResponse.class);
		
		String username = authorizationResponse.getUsername();

		if (authorizationResponse.isTokenValid()
				&& !authorizationResponse.isTokenExpired()) {

			Set<String> userGroups = authorizationResponse.getGroups();
			boolean isAuthorized = false;
			isAuthorized = lDAPAuthenticationClient.authorizeUser(userGroups);
			if (isAuthorized) {
				return username;
			}
		} else {
			throw new TokenException(SC_EXPECTATION_FAILED,
					TokenException.Reason.BEARER_MISSING);
		}
		return null;
	}
}