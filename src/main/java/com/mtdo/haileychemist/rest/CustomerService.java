package com.mtdo.haileychemist.rest;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.mtdo.haileychemist.model.Customer;

@Path("/customers")
@Stateless
public class CustomerService extends BaseEntityService<Customer>{

	public CustomerService(){
		super(Customer.class);
	}
	
}
