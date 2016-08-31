package com.mtdo.haileychemist.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.mtdo.haileychemist.model.Product;

// parameters: categoryId, keyWord
// if category id < 0 -> mean all category
@Path("/search")
@Stateless
public class SearchService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> searchProducts(@Context UriInfo uriInfo) {
		//	public SearchResult searchProducts(@Context UriInfo uriInfo) {
//		String result = "";
		//		Get parameter
		//		http://localhost:8080/hailey-chemist/rest/search?categoryId=2&keyWord=abc
		MultivaluedMap<String, String> paras = uriInfo.getQueryParameters();
//		result = result + "Paras: categoryId=" + paras.getFirst("categoryId") + 
//				", keyWord=" + paras.getFirst("keyWord");
		// ------------------------------------------------
		//		use this to use service search products by keyword
		Client client = ClientBuilder.newClient();
//		String pUrl = "http://localhost:8080/hailey-chemist/rest/products?keyWord=oil";
//		List<Product> products = (List<Product>) client.target(pUrl).request(MediaType.APPLICATION_JSON).get(Product.class);
		
		
//	    List<Customer> customers =
//	            client.target("http://localhost:8080/customer/webapi/Customer")
//	            .path("all")
//	            .request(MediaType.APPLICATION_XML)
//	            .get(new GenericType<List<Customer>>() {
//	            });
//	    return customers;
//		client.target("http://localhost:8080/hailey-chemist/rest/products?keyWord=oil")
	    List<Product> products =
	            client.target("http://localhost:8080/hailey-chemist/rest/products")
	            .property("keyWord", "oil")
	            .request(MediaType.APPLICATION_JSON)
	            .get(new GenericType<List<Product>>() {
	            });
	    
	

		
		//		http://docs.oracle.com/javaee/7/tutorial/jaxrs-client002.htm
		// ------------------------------------------------
		//		then use category service to get path
		//		then create result to return
		// ------------------------------------------------
		return products;
	}


}