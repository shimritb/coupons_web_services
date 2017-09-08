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

import client.AdminFacade;
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
	public Response createCompany(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Company company) {
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}	
		
	    // input fields validation
		if(company == null 
				|| company.getCompName()==null 
				|| company.getEmail()==null
				|| company.getPassword()==null) {
			return Response.serverError().entity("Company object fields cant be null").build();
	    }
		try {
			adminFacade.createCompany(company);
			return Response.status(200).entity("Company sucessfully created").build();
		} catch (Exception e) {
			if (e instanceof CompanyNameAlreadyExistsException) {
				feedback = e.getMessage();
			} else if (e instanceof CustomSqlSyntaxException){
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@POST
	@Path("/company/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCompany(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Company company){
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}	
		
    	// input fields validation
		if(company == null 
				|| company.getId()==0 
				|| company.getCompName()==null) {
	        return Response.serverError().entity("Company object fields cant be null").build();
    }
		try {
			adminFacade.removeCompany(company);
			return Response.status(200).entity("Company sucessfully removed").build();
		} catch (Exception e) {
			if (e instanceof CompanyNameDoesNotExistsException) {
				feedback = e.getMessage();
			} else if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@POST
	@Path("/company/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCompany(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Company company){
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

    	// input fields validation
		if(company == null 
				|| company.getId()==0 
//				|| company.getCompName()==null 
				|| ( company.getEmail()==null
				&& company.getPassword()==null))  {
	        return Response.serverError().entity("Company object fields cant be null").build();
    }
		try {
			adminFacade.updateCompany(company);
			return Response.status(200).entity("Company sucessfully updated").build();
		
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else if (e instanceof CompanyNameDoesNotExistsException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			
			return Response.status(500).entity(feedback).build(); 
		}
	}
	
	@GET
	@Path("/company/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompany(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("id")@DefaultValue("-1")Long id){
		String feedback="";
		AdminFacade adminFacade = null;
		Company company = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		if(id == -1) {
	        return Response.serverError().entity("ID cannot be blank").build();
		}
				
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		try {
			company = adminFacade.getCompany(id);
		
			if(company == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("Company not found for ID: " + id).build();
			}
		     return Response.status(200).entity(company).build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else if (e instanceof CompanyIdNotFoundException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();			
		}
	}
	
	@GET
	@Path("/company/getAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Company> companyList = null;
		GenericEntity<ArrayList<Company>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		try {			
			companyList = adminFacade.getAllCompanies();		
			if(companyList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no companies found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Company>>(companyList) {};
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
	
	@POST
	@Path("/customer/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		if(customer == null
					|| customer.getCustName()==null
					|| customer.getPassword()==null) {
	        return Response.serverError().entity("Customer object cant be null").build();
	    }
		
		try {
			adminFacade.createCustomer(customer);
			return Response.status(200).entity("Customer sucessfully created").build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage();
			} else if (e instanceof CustomerNameAlreadyExsitsException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@POST
	@Path("/customer/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCustomer(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

		if(customer == null
				|| customer.getId()==0
				|| customer.getCustName()==null
		){
			return Response.serverError().entity("Customer object cant be null").build();
		}
	
		try {
			adminFacade.removeCustomer(customer);
			return Response.status(200).entity("Customer sucessfully created").build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage(); 
			} else if (e instanceof CustomerNameNotFound) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@POST
	@Path("/customer/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, Customer customer){
		String feedback = "";
		AdminFacade adminFacade = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		if(customer == null
				|| customer.getId()==0
				|| customer.getCustName()==null
				|| customer.getPassword()==null) {
        return Response.serverError().entity("Customer object cant be null").build();
    }
	
		try {
			adminFacade.updateCustomer(customer);
			return Response.status(200).entity("Customer sucessfully updated").build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage(); 
			} else if (e instanceof CustomerNameNotFound) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}
	}
	
	@GET
	@Path("/customer/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken, @QueryParam("id")@DefaultValue("-1")Long id){
		String feedback="";
		AdminFacade adminFacade = null;
		Customer customer = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		if(id == -1) {
	        return Response.serverError().entity("ID cannot be blank").build();
		}

		try {
			customer = adminFacade.getCustomer(id);
		
			if(customer == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("Customer not found for ID: " + id).build();
			}
		     return Response.status(200).entity(customer).build();
		} catch (Exception e) {
			if (e instanceof CustomSqlSyntaxException) {
				feedback = e.getMessage(); 
			} else if (e instanceof CustomerIdNotFoundException) {
				feedback = e.getMessage();
			} else {
				feedback = e.getMessage();
			}
			return Response.status(500).entity(feedback).build();
		}	
	}
	@GET
	@Path("/customer/getAllCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomer(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Customer> customerList = null;
		GenericEntity<ArrayList<Customer>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		try {			
			customerList = adminFacade.getAllCustomer();		
			if(customerList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no customers found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Customer>>(customerList) {};
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
	
	@GET
	@Path("/getAllCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupon(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeaderToken){
		String feedback="";
		AdminFacade adminFacade = null;
		ArrayList<Coupon> couponList = null;
		GenericEntity<ArrayList<Coupon>> genericEntity = null;
		FacadeManager facadeManager = FacadeManager.getInstance();
		
		adminFacade = (AdminFacade) facadeManager.getFacade(authorizationHeaderToken);
		if (adminFacade == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		try {			
			couponList = adminFacade.getAllCoupon();		
			if(couponList == null) {	
		        return Response.status(Response.Status.NOT_FOUND).entity("no customers found").build();
			}
			genericEntity = new GenericEntity<ArrayList<Coupon>>(couponList) {};
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
