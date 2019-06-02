package com.yodlee.health.errorsegment.authentication.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Used by {@link ExceptionHandler} to handle an
 * <code>InternalErrorHandler</code>.
 *
 * @author mboraiah
 */
public interface InternalErrorHandler {

	public void write(HttpServletRequest request, HttpServletResponse response, Exception Exception) throws IOException, Exception;

}
