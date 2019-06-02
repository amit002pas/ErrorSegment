package com.yodlee.health.errorsegment.authentication.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Generating {@link UserDetailsService} by extending {@link org.springframework.security.core.userdetails.User} 
 * with additional setting JWT token.
 *
 * @author mboraiah
 *
 */
public class User extends org.springframework.security.core.userdetails.User {

	/**
	*
	*/
	private static final long serialVersionUID = -4021745941970689574L;

	private String token;

	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 */
	public User() {
		super("xxx", "xxx", Collections.emptySortedSet());
	}

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link org.springframework.security.core.userdetails.User}.
	 *
	 * @param username the username presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param authorities the authorities that should be granted to the caller if they
	 * presented the correct username and password and the user is enabled. Not null.
	 *
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 * a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	public User(final String username, final String password,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, "xxx", Collections.emptySortedSet());
	}

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link org.springframework.security.core.userdetails.User}.
	 *
	 * @param username the username presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param authorities the authorities that should be granted to the caller if they
	 * presented the correct username and password and the user is enabled. Not null.
	 * @param value for get specified user
	 * 
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 * a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */

	public User(final String username, final String password, final Collection<? extends GrantedAuthority> authorities,
			final boolean value) {
		super(username, password, authorities);
	}

	/**
	 * Get generated token by token name.
	 *
	 * @return the token
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Set token value to storage.
	 * 
	 * @param token
	 *            the token to set
	 */
	public void setToken(final String token) {
		this.token = token;
	}

}
