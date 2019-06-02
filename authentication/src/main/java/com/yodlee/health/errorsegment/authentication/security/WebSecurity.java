package com.yodlee.health.errorsegment.authentication.security;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yodlee.health.errorsegment.authentication.security.token.handler.FailureHandler;
import com.yodlee.health.errorsegment.gateway.authenticate.LDAPAuthenticationClientImpl;

/**
 * @author mboraiah
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AuthenticationFailureHandler authenticationHandler;

	@Autowired
	private AccessDeniedHandler authenticationDeniedHandler;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private FailureHandler failureHandler;

	@Autowired
	private LDAPAuthenticationClientImpl lDAPAuthenticationClientImpl;

	private final String[] antMatches = new String[] { "/", "index",
			"/index.html", "/login.html", "/dashboard.html", "/login", "/home",
			"/iat-error.html", "/static/**", "/js/**", "/css/**", "/styles/**",
			"/webjars/**", "/fonts/**", "/images/**", "/scripts/**", "/modules/**", 
			"/views/**", "/templates/**", "/public/**" };

	/**
	 * Constructs a <code>WebSecurity</code> with the specified
	 * userDetailsService and bCryptPasswordEncoder.
	 *
	 * @param userDetailsService
	 * @param bCryptPasswordEncoder
	 */
	public WebSecurity(UserDetailsService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(
				bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().authorizeRequests().antMatchers(this.antMatches).permitAll().and()
				.authorizeRequests().anyRequest().fullyAuthenticated().and()
				.headers().frameOptions().disable().and().formLogin()
				.loginPage(SecurityConstants.LOGIN_PAGE)
				.loginProcessingUrl(SecurityConstants.LOGIN)
				.successHandler(this.authenticationSuccessHandler)
				.failureHandler(this.authenticationHandler).and()
				.addFilter(this.authenticationFilter())
				.addFilter(this.authorizationFilter()).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.exceptionHandling()
				.accessDeniedHandler(this.authenticationDeniedHandler)
				.authenticationEntryPoint(this.authenticationEntryPoint).and()
				.csrf().disable();
	}

	/**
	 * Creating instance for {@link AuthenticationFilter} by setting the
	 * parameter name which will be used to obtain the username from the login
	 * request.
	 *
	 * @return {@linkplain Filter}
	 * @throws Exception
	 */
	private Filter authenticationFilter() throws Exception {
		final AuthenticationFilter jwtAuthenticationFilter = new AuthenticationFilter();
		jwtAuthenticationFilter.setAuthenticationManager(this
				.authenticationManager());
		jwtAuthenticationFilter
				.setAuthenticationFailureHandler(this.authenticationHandler);
		jwtAuthenticationFilter
				.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
		return jwtAuthenticationFilter;
	}

	/**
	 * Creating instance for {@link AuthorizationFilter} by setting the
	 * parameter name which will be used to obtain the username from the login
	 * request.
	 *
	 * @return {@linkplain Filter}
	 * @throws Exception
	 */
	private Filter authorizationFilter() throws Exception {
		final AuthorizationFilter jwtAuthorizationFilter = new AuthorizationFilter(
				this.authenticationManager());
		jwtAuthorizationFilter
				.setTokenHandler(this.lDAPAuthenticationClientImpl);
		jwtAuthorizationFilter.setFailureHandler(this.failureHandler);
		return jwtAuthorizationFilter;
	}
	
	/**
	 * @author RRaj
	 * Bean for Cors configuration.
	 * To allow Authorization header to be passed 
	 * @return
	 */
	
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowCredentials(true);
	        configuration.setAllowedHeaders(Arrays.asList("Authorization"));
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedMethods(Arrays.asList("*"));
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
}