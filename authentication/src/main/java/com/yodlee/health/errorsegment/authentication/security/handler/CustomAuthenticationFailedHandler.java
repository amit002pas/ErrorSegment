package com.yodlee.health.errorsegment.authentication.security.handler;

import static com.yodlee.health.errorsegment.authentication.security.SecurityConstants.AUTHENTICATION_RESTRICTED;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.yodlee.health.errorsegment.authentication.security.SecurityConstants;
import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

/**
 * Rest Authentication entry class
 * 
 * Strategy used to handle a failed authentication attempt.
 * <p>
 * Typical behaviour might be to redirect the user to the authentication page (in the case
 * of a form login) to allow them to try again. More sophisticated logic might be
 * implemented depending on the type of the exception. For example, a
 * {@link CredentialsExpiredException} might cause a redirect to a web controller which
 * allowed the user to change their password.
 *
 * 
 * @author mboraiah
 *
 */
@Component
public class CustomAuthenticationFailedHandler implements AuthenticationFailureHandler, AccessDeniedHandler {

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException {
		this.write(request, response, SC_UNAUTHORIZED, SecurityConstants.AUTHENTICATION_FAILED, exception.getLocalizedMessage());
	}

	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException ex) throws IOException, ServletException {
		this.write(request, response, SC_UNAUTHORIZED, AUTHENTICATION_RESTRICTED, ex.getLocalizedMessage());
	}

	
	/**
	 * @param response
	 * @param request
	 * @throws IOException
	 *
	 */
	private void write(final HttpServletRequest request, final HttpServletResponse response, final int code, final String message, final String reason) 
			throws IOException {
		response.setStatus(code);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setFailureReason(reason);
		
		final PrintWriter out = response.getWriter();
		out.print(authenticationResponse.toJSON());
		out.flush();
		out.close();
	}
}
