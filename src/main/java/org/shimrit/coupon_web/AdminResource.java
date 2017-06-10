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

import client.AdminFacade;
import client.ClientType;
import client.CompanyFacade;
import dataObjects.Company;
import dataObjects.Coupon;
import dataObjects.Customer;
import exceptions.CompanyIdNotFoundException;
import exceptions.CompanyNameAlreadyExistsException;
import exceptions.CompanyNameDoesNotExistsException;
import exceptions.CustomSqlSyntaxException;
import exceptions.CustomerIdNotFoundException;
import exceptions.CustomerNameAlreadyExsitsException;
import exceptions.CustomerNameNotFound;
import exceptions.LoginFailedException;

@Path("admin_resource")
public class AdminResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	@POST
	@Path("/company/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCompany(Company company) {
		String feedback = "";
		AdminFacade adminFacade = null;
		 
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
	    	// input fields validation
		if(company == null 
				|| company.getId()==0 
				|| company.getCompName()==null 
				|| company.getEmail()==null
				|| company.getPassword()==null) {
			return Response.serverError().entity("Company object fields cant be null").build();
	    }
		try {
			adminFacade.createCompany(company);
			return Response.status(200).entity("Company sucessfully created").build();
		} catch (CompanyNameAlreadyExistsException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} 

	}
	
	@POST
	@Path("/company/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCompany(Company company){
		String feedback = "";
		AdminFacade adminFacade = null;
		 
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
    	// input fields validation
		if(company == null 
				|| company.getId()==0 
				|| company.getCompName()==null 
				|| company.getEmail()==null
				|| company.getPassword()==null)  {
	        return Response.serverError().entity("Company object fields cant be null").build();
    }
		try {
			adminFacade.removeCompany(company);
			return Response.status(200).entity("Company sucessfully removed").build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CompanyNameDoesNotExistsException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
			}
	}
	
	@POST
	@Path("/company/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company){
		String feedback = "";
		AdminFacade adminFacade = null;


		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
    	// input fields validation
		if(company == null 
				|| company.getId()==0 
				|| company.getCompName()==null 
				|| company.getEmail()==null
				|| company.getPassword()==null)  {
	        return Response.serverError().entity("Company object fields cant be null").build();
    }
		try {
			adminFacade.updateCompany(company);
			return Response.status(200).entity("Company sucessfully updated").build();
		
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CompanyNameDoesNotExistsException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/company/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompany(@QueryParam("id")@DefaultValue("-1")Long id){
		String feedback="";
		AdminFacade adminFacade = null;
		Company company = null;
		
		if(id == -1) {
	        return Response.serverError().entity("ID cannot be blank").build();//MAIL TO ITAY
		}
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			company = adminFacade.getCompany(id);
		
			if(company == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("Company not found for ID: " + id).build();
			}
		     return Response.status(200).entity(company).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CompanyIdNotFoundException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}	
	}
	@GET
	@Path("/company/getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies(){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Company> companyList = null;
		GenericEntity<ArrayList<Company>> genericEntity = null;

		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		try {			
			companyList = adminFacade.getAllCompanies();		
			if(companyList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no companies found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Company>>(companyList) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}

	}
	
	@POST
	@Path("/customer/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		 
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		if(customer == null
					|| customer.getId()==0
					|| customer.getCustName()==null
					|| customer.getPassword()==null) {
	        return Response.serverError().entity("Customer object cant be null").build();
	    }
		
		try {
		adminFacade.createCustomer(customer);
		return Response.status(200).entity("Customer sucessfully created").build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CustomerNameAlreadyExsitsException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@POST
	@Path("/customer/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCustomer(Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		if(customer == null
				|| customer.getId()==0
				|| customer.getCustName()==null
				|| customer.getPassword()==null) {
        return Response.serverError().entity("Customer object cant be null").build();
    }
	
		try {
			adminFacade.removeCustomer(customer);
			return Response.status(200).entity("Customer sucessfully created").build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CustomerNameNotFound e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}	
	}
	
	@POST
	@Path("/customer/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		if(customer == null
				|| customer.getId()==0
				|| customer.getCustName()==null
				|| customer.getPassword()==null) {
        return Response.serverError().entity("Customer object cant be null").build();
    }
	
		try {
			adminFacade.removeCustomer(customer);
			return Response.status(200).entity("Customer sucessfully updated").build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CustomerNameNotFound e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/customer/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@QueryParam("id")@DefaultValue("-2")Long id){
		String feedback="";
		AdminFacade adminFacade = null;
		Customer customer = null;
		
		if(id == 2) {
	        return Response.serverError().entity("ID cannot be blank").build();
		}
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		try {
			customer = adminFacade.getCustomer(id);
		
			if(customer == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("Customer not found for ID: " + id).build();
			}
		     return Response.status(200).entity(customer).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		} catch (CustomerIdNotFoundException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
		
	}
	@GET
	@Path("/customer/getAllCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomer(){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Customer> customerList = null;
		GenericEntity<ArrayList<Customer>> genericEntity = null;
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		try {			
			customerList = adminFacade.getAllCustomer();		
			if(customerList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no customers found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Customer>>(customerList) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}

	}
	@GET
	@Path("/getAllCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupon(){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Coupon> couponList = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		
		try {
			adminFacade = (AdminFacade) CouponSystemInst.couponSystem.login("admin", "1234", ClientType.valueOf("ADMIN"));		
		} catch (CustomSqlSyntaxException e) {
			System.out.println(e.getMessage());
		} catch (LoginFailedException e) {
			System.out.println(e.getMessage());
		}
		
		try {			
			couponList = adminFacade.getAllCoupon();		
			if(couponList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no customers found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Coupon>>(couponList) {};
			return Response.status(200).entity(genericEntity).build();
		} catch (CustomSqlSyntaxException e) {
			feedback = e.getMessage();
			return Response.status(500).entity(feedback).build();
		}
		
	}
}
