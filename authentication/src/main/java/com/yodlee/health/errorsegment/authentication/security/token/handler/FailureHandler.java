package com.yodlee.health.errorsegment.authentication.security.token.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * Used by {@link ExceptionTranslationFilter} to handle an
 * <code>FailureHandler</code>.
 *
 * @author mboraiah
 */
public interface FailureHandler {

	/**
	 * Handles a token invalid failure.
	 *
	 * @param request that resulted in an <code>AccessDeniedException</code>
	 * @param response so that the user agent can be advised of the failure
	 * @param tokenException that caused the invocation
	 *
	 * @throws IOException in the event of an IOException
	 * @throws ServletException in the event of a ServletException
	 */
	public void onInvalidToken(HttpServletRequest request, HttpServletResponse response, TokenException tokenException)
			throws IOException, ServletException;

}
