package org.shimrit.coupon_web;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import client.ClientType;
import db_services.CouponClientFacade;
import exceptions.CustomSqlSyntaxException;
import exceptions.LoginFailedException;

/**
 * Root resource (exposed at "login" path)
 */
@Path("login_resource")
public class Login {
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(UserResponseData userCredentials) {
		String feedback = "";
		String userName = userCredentials.getName();
		String password = userCredentials.getPassword();
		String clientType = userCredentials.getClientType().toUpperCase();
		System.out.println(userName);
		System.out.println(password);
		System.out.println(clientType);

		FacadeManager facadeManager = FacadeManager.getInstance();

		try {
			CouponClientFacade facade = CouponSystemInst.couponSystem.login(userName, password,	ClientType.valueOf(clientType));

			if (facade != null) { //login succeed
				String token = issueToken(userName);

				// addin facade to facade collection so we dont have to recreate
				// it everytime
				facadeManager.addFacade(token, facade, userName);

				// taking the user json that we got from angular and adding
				// additional fields to it
				UserResponseData userResponseData = new UserResponseData();//userCredentials;

				switch (clientType.toUpperCase()) {
				case "ADMIN":
					userResponseData.setRedirectUrl("/home/admin");
					break;
				case "COMPANY":
					userResponseData.setRedirectUrl("/home/company");
					break;
				case "CUSTOMER":
					userResponseData.setRedirectUrl("/home/customer");
					break;
				default:
					userResponseData.setRedirectUrl("/home");
					break;
				}

				userResponseData.setToken(token);

				return Response.status(200).header(HttpHeaders.AUTHORIZATION, token).entity(userResponseData).build();
			} else {
				feedback = "Please provide valid credentials";
			}
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
		} catch (LoginFailedException e) {
			feedback = e.getMessage();
		}
		return Response.status(Response.Status.UNAUTHORIZED).entity(feedback).build();
	}

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){ //, UserResponseData userCredentials) {
		if (authorizationHeaderToken != null) {
			FacadeManager facadeManager = FacadeManager.getInstance();
			facadeManager.removeFacade(authorizationHeaderToken);//userCredentials.getToken());

			UserResponseData userResponseData = new UserResponseData(); //userCredentials;
			userResponseData.setRedirectUrl("/home");

			return Response.status(200).entity(userResponseData).build();
		} else {
			return Response.status(500).entity("Internal error").build();
		}
		
		
	}

	private String issueToken(String userName) {
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int repetiotionCount = userName.length()*5;

		StringBuilder builder = new StringBuilder();
		while (repetiotionCount-- != 0) {
			int characterIndex = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(characterIndex));
		}
		return builder.toString();
	}
}
