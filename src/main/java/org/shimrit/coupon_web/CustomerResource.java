package org.shimrit.coupon_web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import client.ClientType;
import client.CompanyFacade;
import client.CustomerFacade;
import dataObjects.Coupon;
import dataObjects.CouponType;
import exceptions.CouponAmountIsEmptyException;
import exceptions.CouponExpiredException;
import exceptions.CouponWasPurchasedException;
import exceptions.CustomSqlSyntaxException;
import exceptions.LoginFailedException;

@Path("customer_resource")
public class CustomerResource {
	
	@POST
	@Path("/coupon/purchase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Coupon coupon){
		String feedback = "";	
		CustomerFacade custFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		custFacade = (CustomerFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (custFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		System.out.println("debug: coupon end date: " + coupon.getEndDate().toString());
		
		// input fields validation
		if(coupon == null 
				|| coupon.getId()==0 
				|| coupon.getEndDate()==null ){
	        return Response.serverError().entity("Coupon object fields cant be null").build();
	    }	
				
		try {
			custFacade.purchaseCoupon(coupon);
			return Response.status(200).entity("Coupon sucessfully purchased").build();
		} catch (Exception e) {
			feedback=e.getMessage();
			
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else if (e instanceof CouponAmountIsEmptyException) {
				feedback = e.getMessage();
			} else if (e instanceof CouponExpiredException) {
				feedback = e.getMessage();
			} else if (e instanceof CouponWasPurchasedException) {
				feedback = e.getMessage();
			}
			else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){
		String feedback = "";
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedCouponslist = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();

		
		custFacade = (CustomerFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (custFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		try {
			purchasedCouponslist = custFacade.getAllPurchasedCoupons();
			if(purchasedCouponslist == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no purchased coupons was found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Coupon>>(purchasedCouponslist) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback=e.getMessage();
				return Response.status(500).entity(feedback).build();
			} else {
				feedback=e.getMessage();
				return Response.status(500).entity(feedback).build();
			}
		}		
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCouponsByType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("type") @DefaultValue("empty-input") String couponType){
		String feedback = "";		
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedcouponListByType = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		CouponType enumCouponType = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		custFacade = (CustomerFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (custFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		if (couponType.equals("empty-input")) {
			return Response.serverError().entity("Type cannot be blank").build();
		}
		// turning couponType that we got from client into enum (REMEMBER: enum in our case works with big letters)
		try{
			enumCouponType = CouponType.valueOf(couponType.toUpperCase()); 
		} catch (Exception e){
			return Response.status(500).entity("no valid format of coupon type").build();
		}
		
		try {
			purchasedcouponListByType = custFacade.getAllPurchasedCouponsByType(enumCouponType);
			
			if(purchasedcouponListByType == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no coupons of the requested type was found").build();
			}
			
			genericEntity = new GenericEntity<ArrayList<Coupon>>(purchasedcouponListByType) {};
			return Response.status(200).entity(genericEntity).build();
			
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				System.out.println(e.getMessage());
				return Response.status(500).entity(feedback).build();			
			} else {
				System.out.println(e.getMessage());
				return Response.status(500).entity(feedback).build();
			}
		}
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCouponsByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("price") @DefaultValue("-1.2")Double price){
		String feedback = "";		
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedcouponListByPrice = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		custFacade = (CustomerFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (custFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		if (price == -1.2) {
			  return Response.serverError().entity("Price cannot be blank").build();
		}
		
		try {
			purchasedcouponListByPrice = custFacade.getAllPurchasedCouponsByPrice(price);
			if(purchasedcouponListByPrice == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no coupons of the requested price was found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Coupon>>(purchasedcouponListByPrice) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}

}
