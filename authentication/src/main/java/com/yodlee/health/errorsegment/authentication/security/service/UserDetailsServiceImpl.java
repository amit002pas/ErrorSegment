package com.yodlee.health.errorsegment.authentication.security.service;

import static java.util.Collections.emptyList;

import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClient;
import com.yodlee.iae.commons.authentication.model.AuthenticationRequest;
import com.yodlee.iae.commons.authentication.model.AuthenticationResponse;

/**
 * @author mboraiah
 *
 */
@Named
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Qualifier("ProdLDAP")
	private LDAPAuthenticationClient lDAPAuthenticationClient;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Gson gson = new Gson(); 
		AuthenticationRequest applicationUser = gson.fromJson(username, AuthenticationRequest.class);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}

		try {
			Response response = lDAPAuthenticationClient.authenticateuser(applicationUser.toJSON(), false);
			AuthenticationResponse _response = response.readEntity(AuthenticationResponse.class);
			if (_response.getToken() == null && _response.getFailureReason() != null) {
				applicationUser.setPassword(StringUtils.EMPTY);
			} else {
				applicationUser.setPassword(new BCryptPasswordEncoder().encode(applicationUser.getPassword()));
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(username);
		}
		final UserDetails user = new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
		this.detailsChecker.check(user);
		return user;
	}
}