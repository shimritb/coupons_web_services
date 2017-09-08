package org.shimrit.coupon_web;

import java.util.ArrayList;


import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import client.ClientType;
import client.CompanyFacade;
import dataObjects.Coupon;
import dataObjects.CouponType;
import exceptions.CouponNameAlreadyExistsException;
import exceptions.CouponNameNotFoundException;
import exceptions.CouponNotBelongToCompanyException;
import exceptions.CustomSqlSyntaxException;
import exceptions.LoginFailedException;

@Path("company_resource")
public class CompanyResource {	
	@POST
	@Path("/coupon/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	// the token must come through http header
	public Response createCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Coupon coupon) {//UserCredentialData userCredentials) {
		if (authorizationHeaderToken != null) {
			String feedback = "";		
			CompanyFacade compFacade = null;
			FacadeManager facadeManager = FacadeManager.getInstance();
				
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			// input fields validation
			if(coupon == null 
					|| coupon.getId()==0 
					|| coupon.getTitle()==null 
					|| coupon.getStartDate()==null 
					|| coupon.getEndDate()==null 
					|| coupon.getMessage()==null 
			 		|| coupon.getPrice()==0.0 
					|| coupon.getImage()==null) {
		        return Response.serverError().entity("Coupon object fields cant be null").build();
		    }
						
			try {
				compFacade.createCoupon(coupon);
				return Response.status(200).entity("Coupon sucessfully created").build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNameAlreadyExistsException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}
		}
		else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}

	@POST
	@Path("/coupon/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Coupon coupon) {
		if (authorizationHeaderToken != null) {
			String feedback="";
			CompanyFacade compFacade = null;
			FacadeManager facadeManager = FacadeManager.getInstance();
			
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			// input fields validation
			if(coupon == null 
					|| coupon.getId()==0 
					|| coupon.getTitle()==null ) {
		        return Response.serverError().entity("Coupon id and title fields cant be null").build();
		    }	
					
			try {
				compFacade.removeCoupon(coupon);
				return Response.status(200).entity("Coupon sucessfully deleted").build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNameNotFoundException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNameNotFoundException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNotBelongToCompanyException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}	
		} 
		else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}
	
	@POST
	@Path("/coupon/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Coupon coupon) {
		if (authorizationHeaderToken != null) {
			String feedback="";
			CompanyFacade compFacade = null;
			FacadeManager facadeManager = FacadeManager.getInstance();
			
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			// input fields validation
			if(coupon == null 
					|| coupon.getId()==0 
					|| coupon.getTitle()==null 
					|| coupon.getEndDate()==null 
					|| coupon.getPrice()==0.0 ) {
		        return Response.serverError().entity("Coupon object fields cant be null").build();
		    }	
			
			try {
				compFacade.updateCoupon(coupon);
				return Response.status(200).entity("Coupon sucessfully updated").build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNameNotFoundException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				
				return Response.status(500).entity(feedback).build();
			}
		}
		else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}
	
	@GET
	@Path("/coupon/getCouponById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("id")@DefaultValue("-1")Long id){		
		if(authorizationHeaderToken != null){
			String feedback="";
			CompanyFacade compFacade = null;
			Coupon coupon = null;
			FacadeManager facadeManager = FacadeManager.getInstance();
			
			if(id==-1) {
		        return Response.serverError().entity("ID cannot be blank").build();
		    }
			
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
							
			try {
				coupon = compFacade.getCoupon(id);

				if(coupon == null) {	
			        return Response.status(Response.Status.NOT_FOUND).entity("Coupon not found for ID: " + id).build();
			    }

				return Response.status(200).entity(coupon).build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else if (e instanceof CouponNotBelongToCompanyException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}
	
	@GET
	@Path("/coupon/getCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoupons(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){
		String feedback="";
		CompanyFacade compFacade = null;
		ArrayList<Coupon> coupons = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		if(authorizationHeaderToken != null) {
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			try {			
				coupons = compFacade.getCoupons();
				
				if(coupons == null) {	
			        return Response.status(Response.Status.NOT_FOUND).entity("no coupons found").build();
				}
				
				genericEntity = new GenericEntity<ArrayList<Coupon>>(coupons) {};
				return Response.status(200).entity(genericEntity).build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}			
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}	
	}
	
	@GET
	@Path("/coupon/getCouponByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouponByPrice(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("price") @DefaultValue("-1.1")Double price) {
		String feedback="";
		CompanyFacade compFacade = null;
		ArrayList<Coupon> couponListByPrice = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		if(authorizationHeaderToken != null) {
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
					
			if(price==(-1.1)){
				return Response.serverError().entity("Price cannot be blank").build();
			}
			
			try {
				couponListByPrice = compFacade.getCouponByPrice(price);
				if(couponListByPrice == null) {	
			        return Response.status(Response.Status.NOT_FOUND).entity("no coupons of the requested price was found").build();
				}
				
				genericEntity = new GenericEntity<ArrayList<Coupon>>(couponListByPrice) {};
				return Response.status(200).entity(genericEntity).build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				} else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}

	@GET
	@Path("/coupon/getCouponByType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouponByType(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("type") @DefaultValue("empty-input") String couponType) {
		String feedback="";
		CompanyFacade compFacade = null;
		ArrayList<Coupon> couponListByType = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		CouponType enumCouponType = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		if(authorizationHeaderToken != null) {
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			
			if (couponType.equals("empty-input")) {
				return Response.serverError().entity("empty input, please insert input").build();
			}
			
			// turning couponType that we got from client into enum (REMEMBER: enum in our case works with big letters)
			try {
				enumCouponType = CouponType.valueOf(couponType.toUpperCase()); 
			} catch (Exception e) {
				return Response.status(500).entity("no valid format of coupon type").build();
			}
			
			try {
				couponListByType = compFacade.getCouponByType(enumCouponType);
					
				if(couponListByType == null) {	
			        return Response.status(Response.Status.NOT_FOUND).entity("no coupons of the requested type was found").build();
				}
				
				genericEntity = new GenericEntity<ArrayList<Coupon>>(couponListByType) {};
				return Response.status(200).entity(genericEntity).build();
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException)
					feedback = e.getMessage();
				else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();
			}
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}		
	}
	
	@GET
	@Path("/coupon/getCouponByDate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouponByDate(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("endDate") @DefaultValue("empty-input")String endDate){
		if (authorizationHeaderToken != null) {
			String feedback="";
			CompanyFacade compFacade = null;
			ArrayList<Coupon> couponListByDate = null;
			GenericEntity<ArrayList<Coupon>> genericEntity = null;
			FacadeManager facadeManager = FacadeManager.getInstance();
					
			compFacade = (CompanyFacade) facadeManager.getFacade(authorizationHeaderToken);//userCredentials.getToken());
			if (compFacade == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}	
						
			if(endDate.equals("empty-input")){
				return Response.serverError().entity("Date cannot be blank").build();
			}
	//		if (!java.sql.Date endDate == java.sql.Date.valueOf( "2013-12-31" ); //java.sql.Date startDate = java.sql.Date.valueOf( "2013-12-31" );
			if (!endDate.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
				return Response.serverError().entity("date format is invalid, please insert date according to the format yyyy-mm-dd").build();
			}
			
			try {
				java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);// converting back from string to date format
				couponListByDate = compFacade.getCouponByDate(sqlEndDate);
				if(couponListByDate == null) {	
			        return Response.status(Response.Status.NOT_FOUND).entity("no coupons of the requested date was found").build();
				}
				
				genericEntity = new GenericEntity<ArrayList<Coupon>>(couponListByDate) {};
				return Response.status(200).entity(genericEntity).build();			
			} catch (Exception e) {
				if (e instanceof CustomSqlSyntaxException) {
					feedback = e.getMessage();
				}
				else {
					return Response.status(500).build();
				}
				return Response.status(500).entity(feedback).build();			
			}
		}
		else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please perform re-login").build();
		}
	}
}

