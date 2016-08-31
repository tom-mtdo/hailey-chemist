package com.mtdo.haileychemist.rest;

import java.util.List;
import java.util.Map;

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
	public Map<Integer, String> searchProducts(@Context UriInfo uriInfo) {
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

		List<Product> products =
				client.target("http://localhost:8080/hailey-chemist/rest/products")
				.property("keyWord", "oil")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});
		
		//		also work
		//		String strUrl = "http://localhost:8080/hailey-chemist/rest/products?keyWord=" + "oi" ;
		//		List<Product> products =
		//				client.target(strUrl)
		//				.request(MediaType.APPLICATION_JSON)
		//				.get(new GenericType<List<Product>>() {
		//				});

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


		return catPath;
	}


}