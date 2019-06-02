package com.yodlee.health.errorsegment.authentication.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

/**
 * @author mboraiah
 *
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		this.write(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", authException.getLocalizedMessage());
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
