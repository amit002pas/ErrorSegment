package com.yodlee.health.errorsegment.authentication.security.token.handler;

import io.jsonwebtoken.Claims;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.yodlee.health.errorsegment.authentication.security.User;

@Component
public interface TokenHandler {

	/**
	 * Handles a token parsing from the given token.
	 *
	 * @param token that resulted in an <code>Claims</code>
	 *
	 * @return {@link Claims}
	 */
	public Claims parseToken(final String token) throws Exception;

	/**
	 * Parse the use name a given token.
	 *
	 * @param token that resulted in an <code>String</code>
	 *
	 * @return {@link String}
	 */
	public String parseUsername(final String token);

	/**
	 * Handles a token value.
	 *
	 * @param token that resulted in an <code>String</code>
	 * @param removeChar replace the given string from the token
	 *
	 * @return {@link String}
	 */
	public String gettokenvalue(final String token, final String removeChar);

	/**
	 * Generate the JWT token for provided {@link User}.
	 *
	 * @param user detail for the <code>User</code>
	 * @param request HttpServletRequest for get requested IP Address.
	 *
	 * @return {@link String}
	 */
	public String generateToken(User user, HttpServletRequest request);

	/**
	 * Verify a token is invalid or NOT.
	 *
	 * @param claims that resulted in an <code>Claims</code>
	 *
	 * @return {@link Boolean}
	 */
	public boolean isExpired(Claims claims);


	/**
	 * Get a <code>User</code> from token.
	 *
	 * @param token requested.
	 * 
	 * @return {@link User}
	 */
	public User getUser(String token);

	/**
	 * Get generic type of a token <code>User</code> from token.
	 *
	 * @param token requested.
	 *
	 * @return {@link User}
	 */
	public <T> T getUser(String token, Class<T> type);

	/**
	 * Retrieve remote server Address .
	 *
	 * @param request that resulted in an <code>HttpServletRequest</code>
	 * 
	 * @return {@link String}
	 */
	public String getRemoteAddr(HttpServletRequest request);
	
	/**
	 * Handles a Current timestamp.
	 *
	 *
	 * @return {@link Date}
	 */
	public Date getCurrentDate();
	
	
	/**
	 * Handles a Expiration time stamp for token.
	 *
	 *
	 * @return {@link Date}
	 */
	public Date generateExpirationDate();
}
