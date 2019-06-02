package com.yodlee.health.errorsegment.authentication.security.token.handler;

import static com.yodlee.health.errorsegment.authentication.security.SecurityConstants.SECRET;
import static java.util.concurrent.TimeUnit.MINUTES;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import net.jodah.expiringmap.ExpiringMap;

import org.apache.commons.lang3.StringUtils;

import com.yodlee.health.errorsegment.authentication.security.SecurityConstants;
import com.yodlee.health.errorsegment.authentication.security.User;

/**
 * 
 * 
 * @author mboraiah
 *
 */
@Named
public class TokenHandlerImpl implements TokenHandler {


	private final Map<String, User> userInfos = ExpiringMap.builder().maxSize(5000)
			.expirationPolicy(net.jodah.expiringmap.ExpirationPolicy.CREATED).expiration(SecurityConstants.expiration, MINUTES).build();

	@Override
	public Claims parseToken(String token) throws Exception{
		return Jwts.parser().setSigningKey(SecurityConstants.SECRET.getBytes()).parseClaimsJws(token).getBody();
	}

	@Override
	public String parseUsername(final String token) {
		final String username = Jwts.parser().setSigningKey(SecurityConstants.SECRET.getBytes()).parseClaimsJws(token)
				.getBody().getSubject();
		return username;
	}

	@Override
	public String generateToken(final User user, final HttpServletRequest request) {
		String ip = this.getRemoteAddr(request);
		final String token = Jwts.builder().setSubject(user.getUsername()).setExpiration(this.generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes()).claim(SecurityConstants.IP, ip).compact();
		this.userInfos.put(token, user);
		return token;
	}

	@Override
	public boolean isExpired(final Claims claims) {
		return claims.getExpiration().before(this.getCurrentDate());
	}

	@Override
	public User getUser(final String token) {
		return this.getUser(token, User.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getUser(final String token, final Class<T> type) {
		return (T) this.userInfos.get(token);
	}

	@Override
	public String getRemoteAddr(HttpServletRequest request) {
		String ret = request.getHeader(SecurityConstants.CIP);
		if (StringUtils.isBlank(ret)) {
			ret = request.getHeader(SecurityConstants.RIP);
			if (StringUtils.isBlank(ret)) {
				ret = request.getRemoteAddr();
			}
		}
		return ret;
	}

	@Override
	public String gettokenvalue(String token, String removeChar) {
		return token.replace(removeChar, "");
	}

	@Override
	public Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	@Override
	public Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + (SecurityConstants.expiration * 60 * 1000));
	}

}
