package org.shimrit.coupon_web;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import client.ClientType;
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
	public Response purchaseCoupon(Coupon coupon){
		String feedback = "";	
		CustomerFacade custFacade = null;
		
		try {
			custFacade = (CustomerFacade) CouponSystemInst.couponSystem.login("raviv", "8889", ClientType.valueOf("CUSTOMER"));	
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
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
			custFacade.purchaseCoupon(coupon);
			return Response.status(200).entity("Coupon sucessfully purchased").build();
		} catch (CustomSqlSyntaxException e) {
			feedback=e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CouponAmountIsEmptyException e) {
			feedback=e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CouponExpiredException e) {
			feedback=e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CouponWasPurchasedException e) {
			feedback=e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons(){
		String feedback = "";
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedCouponslist = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		
		try {
			custFacade = (CustomerFacade) CouponSystemInst.couponSystem.login("raviv", "8889", ClientType.valueOf("CUSTOMER"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			purchasedCouponslist = custFacade.getAllPurchasedCoupons();
			if(purchasedCouponslist == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no purchased coupons was found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Coupon>>(purchasedCouponslist) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (CustomSqlSyntaxException e) {
			feedback=e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
		
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCouponsByType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(@QueryParam("type") @DefaultValue("empty-inputt") String couponType){
		String feedback = "";		
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedcouponListByType = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		CouponType enumCouponType = null;
		
		try {
			custFacade = (CustomerFacade) CouponSystemInst.couponSystem.login("raviv", "8889", ClientType.valueOf("CUSTOMER"));
			
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		if (couponType.equals("empty-inputt")) {
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
			
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/coupon/getAllPurchasedCouponsByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(@QueryParam("price") @DefaultValue("-1.2")Double price){
		String feedback = "";		
		CustomerFacade custFacade = null;
		ArrayList<Coupon> purchasedcouponListByPrice = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		
		try {
			custFacade = (CustomerFacade) CouponSystemInst.couponSystem.login("raviv", "8889", ClientType.valueOf("CUSTOMER"));
			
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
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
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
	}

}
