package com.yodlee.iae.health.authentication;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mboraiah
 * URL R/A/L is using for authentication and token generation
 */
@Path("/R")
@Produces(MediaType.APPLICATION_JSON)
public interface IAuthenticationService {

	@POST
	@Path("/A/L")
	public Response login(@Context HttpServletRequest request, String str) throws Exception;

}
