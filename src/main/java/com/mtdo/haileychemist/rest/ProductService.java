package com.mtdo.haileychemist.rest;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.mtdo.haileychemist.model.Product;

@Path("/products")
@Stateless
public class ProductService extends BaseEntityService<Product>{

	public ProductService(){
		super(Product.class);
	}
	
}
