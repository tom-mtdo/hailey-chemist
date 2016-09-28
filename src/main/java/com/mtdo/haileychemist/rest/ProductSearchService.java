package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

import com.mtdo.haileychemist.model.Category;
import com.mtdo.haileychemist.model.Product;
import com.mtdo.haileychemist.model.ProductAttribute;

// parameters: categoryId, keyWord
// if category id < 0 -> mean all category
@Path("/product-search")
@Stateless
public class ProductSearchService {
	@Inject
	private EntityManager entityManager;
	
	//	get products belong to a category
	//	http://localhost:8080/hailey-chemist/rest/product-search/-1/pathCount
	@Path("/{categoryId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> productByCategory(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo ){
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		List<Product> result = searchProductByCategory( categoryId, queryParameters);
		return result;
	}

	//	return product count by category: 
	//	a list of categoryId, categoryName, categoryPath, productCount 
	@Path("/{categoryId}/pathCount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductCountByCategory> getCategoryPathAndProductCount(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo ){
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
		result = searchCategoryPathCount( categoryId, queryParameters );
		return result;
	}

	//	//	get products belong to all categories
	////	 not in use
	//	@GET
	//	@Produces(MediaType.APPLICATION_JSON)
	//	public ProductSearchResult searchProducts() {
	//		ProductSearchResult result = searchProductByCategory( -1 );
	//		return result;
	//	}

	//	get products belong to a category or all category if input categoryId = -1
	//	for pagination
	@Path("/{categoryId}/count")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Long> productByCategoryCount(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo  ){
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		Map<String, Long> result = searchProductByCategoryCount( categoryId, queryParameters );
		return result;
	}
	

//	Get all value of each attribute of all products found
//	URL parameters: url?keyWork=""&categoryId=""&attr[id1]="value1"&attr[id1]="value2"&attr[id2]="value21"
//	Return a Map<attributeId, List<attributeValue>> for all products found 
//		WORKDING HERE
	@Path("/{categoryId}/attribute")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, List<String>> productSearchAttribute(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo  ){
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();		
		
		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Product> product = cq.from(Product.class);
		Join<Product, ProductAttribute> productAttribute = product.join("productAttributes", JoinType.LEFT);
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);
		Predicate[] predicates = ProductService.extractPredicatesImpl(queryParameters, cb, product);
		
//		Predicate predicates = cb.like(product.<String>get("name"), "%i%");
		cq.where(predicates);
		
		Order order = cb.asc(productAttribute.get("attribute").get("id"));
		cq.orderBy(order);
		
		cq.multiselect(
				productAttribute.get("attribute").get("id"),
				productAttribute.get("attribute").get("name"), 
				productAttribute.get("attribute_value"),
				product.get("name")
				);
		
		cq.groupBy(				
				productAttribute.get("attribute").get("id"), 
				productAttribute.get("attribute_value")
				);

//		cq.multiselect(
//				productAttribute.get("attribute").get("id"),
//				productAttribute.get("attribute_value")
//				).distinct(true);

		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
		List<Tuple> lstTuple = tq.getResultList();

		List<String> lstValue = null;
		int attributeId = 0;
		for (Tuple tuple: lstTuple) {
			attributeId = tuple.get(0, Integer.class);
//			if key in list already then add value to value list of the key
//			else add key to key list and value to value list of the key
			if ( result.containsKey(attributeId) ){
				result.get(attributeId).add( tuple.get(2, String.class) );
			} else {
				lstValue = new ArrayList<String>();
				lstValue.add(tuple.get(1, String.class));
				lstValue.add(tuple.get(2, String.class));
				result.put(attributeId, lstValue);
			}
		}

//		Map<String, Long> result = searchProductByCategoryCount( categoryId, queryParameters );
		return result;
	}


	// SHOULD USE SQL GROUP BY AND COUNT TO COUNT
	public List<ProductCountByCategory> searchCategoryPathCount( int categoryId, MultivaluedMap<String, String> queryParameters ){
		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
		//		consumes products rest service
		Client client = ClientBuilder.newClient();
		String uriParameters = buildUriParameter( queryParameters );
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?categoryId=" + categoryId;
		} else {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?orderBy=categoryId";
		}
		
		strUrl = strUrl + "&" + uriParameters;
		List<Product> products =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});

		//		Count product for each category
		if ( products.size() > 0 ) {
			//	init result					
			ProductCountByCategory pCount = new ProductCountByCategory();
			//		set first categoryId to be current
			//		start from category of the first product
			int currentCategoryId = products.get(0).getCategory().getId();
			pCount.setCategoryId(currentCategoryId);
			pCount.setCategoryName( getCategoryName(currentCategoryId) );
			pCount.setCategoryPath( getCategoryPath( currentCategoryId ) );
			pCount.setProductCount( 0 );
			result.add(pCount);
			//		pCount and pCount in result point to the same object
			//		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			//		SHOULD USE GROUP BY & COUNT OF SQL TO COUNT
			//		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			for ( Product product: products ) {
				//			store category id
				//			if same category then increase count
				if ( currentCategoryId == product.getCategory().getId() ){
					pCount.setProductCount( pCount.getProductCount() + 1 );
				} else { // else store count for current category then reset to count for new category
					pCount = new ProductCountByCategory();
					currentCategoryId = product.getCategory().getId();
					pCount.setCategoryId( currentCategoryId );
					pCount.setCategoryName( getCategoryName( currentCategoryId ) );
					pCount.setCategoryPath( getCategoryPath( currentCategoryId ) );
					pCount.setProductCount( 1 );
					//	store count and reset current category
					result.add(pCount);
				}
			}
		}

		return result;
	}

	public Map<String, Long> searchProductByCategoryCount( int categoryId, MultivaluedMap<String, String> queryParameters ){
		//		consumes products rest service
		Client client = ClientBuilder.newClient();
		String uriParameters = buildUriParameter( queryParameters );
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products/count?categoryId=" + categoryId + "&" + uriParameters;
		} else {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products/count" + "?" + uriParameters;
		}
		Map<String, Long> result =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<Map<String, Long>>() {
				});		

		return result;
	}

	//	input: categoryId
	//	if category < 0 -> all categories
	//	product list must be sorted by categoryId
	//	can be extended to deal with query parameters.
	//	http://localhost:8080/hailey-chemist/rest/products?categoryId=4
	public List<Product> searchProductByCategory( int categoryId, MultivaluedMap<String, String> queryParameters ){

		String parameters = buildUriParameter( queryParameters );
//		String parameters = "";
//		Iterator<String> it = queryParameters.keySet().iterator();
//		while(it.hasNext()){
//			String theKey = (String)it.next();
//			String theValue = queryParameters.getFirst(theKey);
//			if ( parameters.trim().length() < 1 ){
//				parameters = theKey + "=" + theValue;
//			} else {
//				parameters = parameters + "&" + theKey + "=" + theValue;
//			}          
//		}

		//		consumes products rest service
		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?categoryId=" + categoryId;
		} else {
			strUrl = "http://localhost:8080/hailey-chemist/rest/products?orderBy=categoryId";
		}
		
		strUrl = strUrl + "&" + parameters;
		List<Product> products =
				client.target(strUrl)
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Product>>() {
				});

		return products;
	}
	
	public String buildUriParameter( MultivaluedMap<String, String> queryParameters ) {
		String result = "";

		String parameters = "";
		Iterator<String> it = queryParameters.keySet().iterator();
		while(it.hasNext()){
			String theKey = (String)it.next();
			String theValue = queryParameters.getFirst(theKey);
			if ( parameters.trim().length() < 1 ){
				parameters = theKey + "=" + theValue;
			} else {
				parameters = parameters + "&" + theKey + "=" + theValue;
			}          
		}
		
		result = parameters;
		return result;
	}

	//	get path from service:
	//	http://localhost:8080/hailey-chemist/rest/categories/path/3
	public List<String> getCategoryPath( int categoryId ){
		List<String> result = new ArrayList<>();

		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/categories/path/" + categoryId;
			Map<Integer, List<String>> cPath =
					client.target(strUrl)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<Map<Integer, List<String>>>() {
					});
			if ( cPath.containsKey(categoryId) ){
				result = cPath.get(categoryId);
			}
		} 

		return result;
	}

	public String getCategoryName( int categoryId ){
		String result = "";

		Client client = ClientBuilder.newClient();
		//		client.property doesnt work
		String strUrl = "";
		if (categoryId > 0) {
			strUrl = "http://localhost:8080/hailey-chemist/rest/categories/" + categoryId;
			Category category =
					client.target(strUrl)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<Category>() {
					});
			if ( category != null ){
				result = category.getName();
			}
		} 

		return result;
	}


}