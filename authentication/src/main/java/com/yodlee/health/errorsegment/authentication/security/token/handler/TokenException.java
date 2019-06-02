package com.yodlee.health.errorsegment.authentication.security.token.handler;

/**
 * Used by {@link RuntimeException} to handle an
 * <code>TokenException</code>.
 *
 * @author mboraiah
 */
public class TokenException extends RuntimeException {

	private static final long serialVersionUID = -3944177256965227963L;

	private final int code;

	public enum Reason {
		TOKEN_MISSING("Token does not exists or missing"),
		BEARER_MISSING("Bearer does not exists or missing"),
		INVALID_TOKEN("Token not valid"),
		TOKEN_EXPIRED("Token expired. Please renew it"),
		TOKEN_NOT_SHARABLE("Tokens cannot be shared. Please create a new token"),
		AUTHOURIZATION_MISSING("Authourization header is missed. Please create a new token"),
		AUTHOURIZATION_VERSION_MISSING("Authourization version header is missed. try again"),	
		RESOURCE_PATH_ERROR("Requested path error. Verify it.")

		;
		private String message;

		/**
		 *
		 */
		private Reason(final String message) {
			this.message = message;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return this.message;
		}
	}

	/**
	 * Constructor to generate exception.
	 *
	 * @param code (Http status code) from <code>javax.servlet.http.HttpServletResponse</code>
	 * @param reason from <code>com.yodlee.iae.iat.security.token.handler.TokenException.Reason</code>
	 *
	 */
	public TokenException(final int code, final Reason reason) {
		super(reason.getMessage());
		this.code = code;
	}

	/**
	 * Get a error codes.
	 *
	 *	@return error code 
	 */
	public int getCode() {
		return this.code;
	}
}