package com.yodlee.health.errorsegment.authentication.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

public class InternalErrorHandlerImpl implements InternalErrorHandler {

	/**
	 * @param response
	 * @param request
	 * @throws IOException
	 *
	 */
	@Override
	public void write(final HttpServletRequest request, final HttpServletResponse response, final Exception exception) throws IOException, Exception {
		response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setFailureReason(exception.getLocalizedMessage());

		PrintWriter out;
		out = response.getWriter();
		out.print(authenticationResponse.toJSON());
		out.flush();
		out.close();
	}

}
