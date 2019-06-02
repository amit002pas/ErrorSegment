package com.yodlee.health.errorsegment.authentication.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.yodlee.health.errorsegment.authentication.security.SecurityConstants;
import com.yodlee.health.errorsegment.authentication.security.token.handler.TokenHandler;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;
import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

/**
 * <tt>AuthenticationSuccessHandler</tt> which can be configured with a default
 * URL which users should be sent to upon successful authentication.
 * <p>
 * The logic used is that of the {@link SimpleUrlAuthenticationSuccessHandler
 * parent class}.
 *
 * @author mboraiah
 *
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


	@Autowired
	TokenHandler tokenHandler;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		this.doSucess(request, response, authentication);
	}

	/**
	 * @param request
	 * @param response
	 * @param authentication
	 * @throws IOException
	 */
	private void doSucess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException {

		final AuthenticationRequest loginRequets = (AuthenticationRequest) authentication.getDetails();
		final com.yodlee.health.errorsegment.authentication.security.User user = new com.yodlee.health.errorsegment.authentication.security.User(loginRequets.getUsername(),
				loginRequets.getPassword(), authentication.getAuthorities(), true);

		final String token = tokenHandler.generateToken(user, request);

		response.addHeader(SecurityConstants.authHeader, token);

		user.setToken(token);

		this.write(response, token, authentication);
		this.clearAuthenticationAttributes(request);
	}

	/**
	 * @param response
	 * @param token
	 * @param authentication
	 * @throws IOException
	 */
	private void write(final HttpServletResponse response, final String token, final Authentication authentication)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		
		if (token != null) {
			authenticationResponse.setToken(SecurityConstants.TOKEN_PREFIX + token);
			authenticationResponse.setDisplayName(((User) authentication.getPrincipal()).getUsername());
		}
		final PrintWriter out = response.getWriter();
		out.print(authenticationResponse.toJSON());
		out.flush();
		out.close();
	}
}