package com.mtdo.haileychemist.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

// parameters: categoryId, keyWord
// if category id < 0 -> mean all category
@Path("/search")
@Stateless
public class SearchService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String searchProducts(@Context UriInfo uriInfo) {
//	public SearchResult searchProducts(@Context UriInfo uriInfo) {
		String result = "";
//		Get parameter
//		http://localhost:8080/hailey-chemist/rest/search?categoryId=2&keyWord=abc
		MultivaluedMap<String, String> paras = uriInfo.getQueryParameters();
		result = result + "Paras: categoryId=" + paras.getFirst("categoryId") + 
				", keyWord=" + paras.getFirst("keyWord");
		
		return result;
	}
}