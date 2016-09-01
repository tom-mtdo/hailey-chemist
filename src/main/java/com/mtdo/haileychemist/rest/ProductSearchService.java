package com.mtdo.haileychemist.rest;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/product-search")
@Stateless
public class ProductSearchService {

	//	get products belong to a category
	//	http://localhost:8080/hailey-chemist/rest/products?categoryId=4
	@Path("/{categoryId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> productByCategory(  @PathParam("categoryId") int categoryId ){
		//		consumes products rest service
		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "http://localhost:8080/hailey-chemist/rest/products?categoryId=" + categoryId;
		List<Product> products =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});
		System.out.println( "Number found: " + products.size() );
		
		return products;
	}

	//	get products belong to all categories
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> searchProducts(@Context UriInfo uriInfo) {
		//	public SearchResult searchProducts(@Context UriInfo uriInfo) {
		//		String result = "";
		//		Get parameter
		MultivaluedMap<String, String> paras = uriInfo.getQueryParameters();
		//		result = result + "Paras: categoryId=" + paras.getFirst("categoryId") + 
		//				", keyWord=" + paras.getFirst("keyWord");
		// ------------------------------------------------
		//		use this to use service search products by keyword
		//		http://docs.oracle.com/javaee/7/tutorial/jaxrs-client002.htm

		//		http://localhost:8080/hailey-chemist/rest/search?categoryId=2&keyWord=abc
		Client client = ClientBuilder.newClient();
		//		http://localhost:8080/hailey-chemist/rest/products?keyWord=oi

		String strUrl = "http://localhost:8080/hailey-chemist/rest/products?keyWord=" + "oi" ;
		List<Product> products =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});

		Map<Integer, String> catPath =
				client.target("http://localhost:8080/hailey-chemist/rest/categories/path")
				.path("3")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Map<Integer, String>>() {
				});


		// ------------------------------------------------
		//		then use category service to get path
		//		then create result to return
		// ------------------------------------------------


		return products;
	}


}