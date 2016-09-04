package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
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
	@Path("/{categoryId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ProductSearchResult productByCategory(  @PathParam("categoryId") int categoryId ){
		ProductSearchResult result = searchProductByCategory( categoryId );
		return result;
	}

	//	get products belong to all categories
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ProductSearchResult searchProducts() {
		ProductSearchResult result = searchProductByCategory( -1 );
		return result;
	}


	//	input: categoryId
	//	if category < 0 -> all categories
	//	product list must be sorted by categoryId
	//	can be extended to deal with query parameters.
	//	http://localhost:8080/hailey-chemist/rest/products?categoryId=4
	public ProductSearchResult searchProductByCategory( int categoryId ){
		//		consumes products rest service
		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?categoryId=" + categoryId;
		} else {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?orderBy=categoryId";
		}
		List<Product> products =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});

		//		Count product for each category
		ProductSearchResult result = new ProductSearchResult();
		if ( products.size() > 0 ) {
			//	init result					
			ProductCountByCategory pCount = new ProductCountByCategory();
			//		set first categoryId to be current
			//		start from category of the first product
			//		pCount.setCategoryId(products.get(0).getCategory().getId());
			result.setProducts(products);
			int currentCategoryId = products.get(0).getCategory().getId();
			pCount.setCategoryId(currentCategoryId);
			pCount.setPath( getCategoryPath( currentCategoryId ) );
			//			int intPCount = 0;
			pCount.setProductCount( 0 );
			result.getCounts().add(pCount);
			//			pCount and pCount in result point to the same object

			//		WORKING HERE
			for ( Product product: products ) {
				//			store category id
				//			if same category then increase count
				if ( currentCategoryId == product.getCategory().getId() ){
					pCount.setProductCount( pCount.getProductCount() + 1 );
					//					intPCount = intPCount + 1;
				} else { // else store count for current category then reset to count for new category
					//					init count for new category
					pCount = new ProductCountByCategory();
					//					pCount.setProductCount(intPCount);
					pCount.setCategoryId( product.getCategory().getId() );
					pCount.setPath( getCategoryPath( product.getCategory().getId() ) );
					pCount.setProductCount( 1 );

					//	store count and reset current category
					result.getCounts().add(pCount);
					currentCategoryId = product.getCategory().getId();
				}
			}
		}

		return result;
	}

	//	get path from service:
	//	http://localhost:8080/hailey-chemist/rest/categories/path/3
	public String getCategoryPath( int categoryId ){
		String result = "";

		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/categories/path/" + categoryId;
			Map<Integer, String> cPath =
					client.target(strUrl)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<Map<Integer, String>>() {
					});
			if ( cPath.containsKey(categoryId) ){
				result = cPath.get(categoryId);
			}
		} 

		return result;
	}

}