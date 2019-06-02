package com.yodlee.health.errorsegment.authentication.security.token.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.yodlee.health.errorsegment.authentication.security.SecurityConstants;
import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

/**
 * @author mboraiah
 *
 */
@Named
public class InvalidTokenHandler implements FailureHandler {

	@Override
	public void onInvalidToken(final HttpServletRequest request, final HttpServletResponse response,
			final TokenException ex) throws IOException, ServletException {
		this.write(request, response, ex.getCode(), SecurityConstants.TOKEN_ISSUE, ex.getLocalizedMessage());
	}

	/**
	 * @param response
	 * @param request
	 * @throws IOException
	 *
	 */
	private void write(final HttpServletRequest request, final HttpServletResponse response, final int code,
			final String message, final String reason) throws IOException {
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
