package com.yodlee.health.errorsegment.authentication.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.health.errorsegment.authentication.security.handler.InternalErrorHandler;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	@Autowired
	private InternalErrorHandler internalErrorHandler;
	
	/**
	 * Constructs a <code>AuthenticationFilter</code> with no parameter.
	 * @return 
	 *
	 */
	
	public AuthenticationFilter() {
	}

	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(SecurityConstants.AUTHENTICATION_NOT_SUPPORTED + request.getMethod());
		}

		try {
			final AuthenticationRequest loginRequest = this.getLoginRequest(request);
			if (loginRequest == null) {
				throw new AuthenticationServiceException(SecurityConstants.AUTHENTICATION_MISSING);
			}

			final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(), loginRequest.getPassword(), new ArrayList<>());

			authRequest.setDetails(loginRequest);
			return this.authenticationManager.authenticate(authRequest);
		} catch (Exception e) {
			try {
				internalErrorHandler.write(request, response, e);
			} catch (Exception e1) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Creating instance for {@link AuthenticationRequest} by getting the
	 * parameters username & password from the login request.
	 *
	 * @param request
	 * @return {@linkplain AuthenticationRequest}
	 */
	private AuthenticationRequest getLoginRequest(final HttpServletRequest request) throws Exception {
		final BufferedReader reader = null;
		AuthenticationRequest loginRequest = null;
		try {
			loginRequest = new ObjectMapper().readValue(request.getReader(), AuthenticationRequest.class);
		} catch (final IOException ex) {
			throw new AuthenticationServiceException(SecurityConstants.AUTHENTICATION_MISSING, ex);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		return loginRequest;
	}
	
}