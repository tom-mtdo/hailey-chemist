package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.mtdo.haileychemist.model.Attribute;
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
		List<Product> result = new ArrayList<Product>();
		
		//	filter products
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);
		Set<Integer> setProductId = productFilter(queryParameters);
		
		if( (setProductId==null) || (setProductId.size()<1) ){
			return result;
		}
		
		// extract product list to return	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);
		Root<Product> product = cq.from(Product.class);

		Predicate predicate = product.get("id").in(setProductId);    
		cq.select(product).where(predicate);
		
		TypedQuery<Product> tq = entityManager.createQuery(cq);
		if (queryParameters.containsKey("first")) {
			// original
			// Integer firstRecord = Integer.parseInt(queryParameters.getFirst("first"))-1;
			Integer firstRecord = Integer.parseInt(queryParameters.getFirst("first"));
			tq.setFirstResult(firstRecord);
		}
		if (queryParameters.containsKey("maxResults")) {
			Integer maxResults = Integer.parseInt(queryParameters.getFirst("maxResults"));
			tq.setMaxResults(maxResults);
		}
		result = tq.getResultList();

		return result;
	}

	//	return product count by category: 
	//	a list of categoryId, categoryName, categoryPath, productCount 
	@Path("/{categoryId}/pathCount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductCountByCategory> getCategoryPathAndProductCount(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo ){
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);
	
		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
		result = searchCategoryPathCount( queryParameters );
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
		Map<String, Long> result = new HashMap<String, Long>();
		
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);
		Set<Integer> setProductId = productFilter(queryParameters);
		
		if( (setProductId==null) || (setProductId.size()<1) ){
			result.put("count", 0L);
		} else {
			result.put("count", new Long(setProductId.size()) );
		}
//		Map<String, Long> result = searchProductByCategoryCount( queryParameters );
		return result;
	}


	//	Get all value of each attribute of all products found
	//	URL parameters: url?keyWork=""&attr[id1]="value1"&attr[id1]="value2"&attr[id2]="value21"
	//	Return a Map<attributeId, List<attributeValue>> for all products found
	//	 the first element in List<attributeValue> is attribute name.
	@Path("/{categoryId}/attribute")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, List<String>> productSearchAttribute(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo  ){
		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();		

		//	get all query parameters: categoryId, keyWord, a
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);
		//	filter products
		Set<Integer> setProductId = productFilter(queryParameters);
		if( (setProductId==null) || (setProductId.size()<1) ){
			return result;
		}

		//	get all other attributes
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//	FROM
		Root<Product> product = cq.from(Product.class);
		Join<Product, ProductAttribute> productAttribute = product.join("productAttributes", JoinType.LEFT);
		Join<ProductAttribute, Attribute> attribute = productAttribute.join("attribute");
		// 	ORDER BY
		Order order = cb.asc(attribute.get("id"));
		cq.orderBy(order);

		TypedQuery<Tuple> tq = null;
		//	all pair attribute-value found
		Set<Tuple> setTupleAttributeValue = new HashSet<Tuple>();
		//	product found by each attribute
		List<Tuple> lstTupleTmp  = new ArrayList<Tuple>();

		//	SELECT
		//	get all (other) attributes of the found products		
		cq.multiselect(
				attribute.get("id"),
				attribute.get("name"),
				productAttribute.get("attribute_value")
				);
		cq.groupBy(				
				attribute.get("id"),
				productAttribute.get("attribute_value")
				);

		//	WHERE
		Predicate predicateProductId = product.get("id").in(setProductId);
		cq.where(predicateProductId);
		
		//	perform query		
		tq = entityManager.createQuery(cq);
		lstTupleTmp = tq.getResultList();
		//to make sure no duplicate value
		setTupleAttributeValue.addAll(lstTupleTmp);

		//	put into data format to return
		List<String> lstValue = null;
		//		int attributeId = 0;
		int attrId = -1;
		for (Tuple tuple: setTupleAttributeValue) {
			attrId = tuple.get(0, Integer.class);
			//			if key in list already then add value to value list of the key
			//			else add key to key list and value to value list of the key
			if ( result.containsKey(attrId) ){
				result.get(attrId).add( tuple.get(2, String.class) );
			} else {
				lstValue = new ArrayList<String>();
				lstValue.add(tuple.get(1, String.class));
				lstValue.add(tuple.get(2, String.class));
				result.put(attrId, lstValue);
			}
		}
		return result;
	}

	/*
	 * Main filter function
	 * input: all query and filter parameters: 
	 * 		: MultivaluedMap<String, String> queryParameters including:
	 * 		: - categoryId (-1 means all category), 
	 * 		: - keyWord="",
	 * 		: - list<attr[attributeId]=value>
	 * output:
	 * 		: list<productId> found
	 */
	public Set<Integer> productFilter( MultivaluedMap<String, String> inQueryParameters  ){
		Set<Integer> result = new HashSet<Integer>();		
		//		copy parameter, just in case
		MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>(inQueryParameters);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();

		//		FROM
		Root<Product> product = cq.from(Product.class);
//		Join<Product, ProductAttribute> productAttribute = product.join("productAttributes", JoinType.LEFT);
//		Join<ProductAttribute, Attribute> attribute = productAttribute.join("attribute");

		//		WHERE
		//		query by category and keyWord
		//		parameters.add("categoryId", "" + categoryId);
		Predicate[] predicates = ProductService.extractPredicatesImpl(parameters, cb, product);

		//		SELECT
		cq.multiselect( product.get("id") );
		cq.groupBy( product.get("id") );
		// 		ORDER BY
		Order order = cb.asc( product.get("id") );
		cq.orderBy( order );

		//		query by attributes	which are from url parameters
		List<Predicate> lstPredicate = new ArrayList<Predicate>(Arrays.asList(predicates));

		int attrId = -1;
		List<String> theValues = new ArrayList<String>();
//		Predicate predicateAttrId = null;
//		Predicate predicateAttrValue  = null;

		TypedQuery<Tuple> tq = null;
		//		set of id of product found
		Set<Tuple> setTupleProductId = new HashSet<Tuple>();
		//		product found by each attribute
		List<Tuple> lstTupleTmp  = new ArrayList<Tuple>();

		// 		find product match attribute filter
		//		find attribute parameter in url
		Boolean wasFilteredByAttribute = false;
//		Boolean wasInit = false;
		Iterator<String> it = parameters.keySet().iterator();
		while ( it.hasNext() ){
			String theKey = (String)it.next();
			if( (theKey.length()>4) && (theKey.substring(0,4).contentEquals("attr")) ){
				//	JOIN attribute and value tables
				Join<Product, ProductAttribute> productAttribute = product.join("productAttributes");
				Join<ProductAttribute, Attribute> attribute = productAttribute.join("attribute");

//				if ( !wasInit ) {
//					//		SELECT
//					cq.multiselect(
//							product.get("id"),
//							attribute.get("id")
//							);
//					cq.groupBy(	attribute.get("id") );
//					// 		ORDER BY
//					Order order = cb.asc(attribute.get("id"));
//					cq.orderBy(order);
//					wasInit = true;
//				}
				//				which attribute
				attrId = Integer.parseInt(theKey.substring(4));
				theValues = parameters.get(theKey);
				Predicate predicateAttrId = cb.equal( attribute.get("id"), attrId );
				Predicate predicateAttrValue = productAttribute.get("attribute_value").in(theValues);
				//				remove old attribute predicate
				if (lstPredicate.contains(predicateAttrId)) {
					lstPredicate.remove(predicateAttrId);
				}
				if (lstPredicate.contains(predicateAttrValue)) {
					lstPredicate.remove(predicateAttrValue);
				}

				lstPredicate.add(predicateAttrId);
				lstPredicate.add(predicateAttrValue);

				// convert to array to put in where clause
				predicates = new Predicate[lstPredicate.size()];
				lstPredicate.toArray(predicates);
				cq.where(predicates);

				tq = entityManager.createQuery(cq);
				lstTupleTmp = tq.getResultList();
				//				union all productId found for each attribute 
				setTupleProductId.addAll(lstTupleTmp);
				wasFilteredByAttribute = true;
			}
		}

		if (!wasFilteredByAttribute){
//			//		SELECT
//			cq.multiselect( product.get("id") );
//			cq.groupBy( product.get("id") );
//			// 		ORDER BY
//			Order order = cb.asc( product.get("id") );
//			cq.orderBy( order );
			//		Then query 
			cq.where(predicates);
			tq = entityManager.createQuery(cq);
			lstTupleTmp = tq.getResultList();
			//to make sure no duplicate value
			setTupleProductId.addAll(lstTupleTmp);			
		}

		//		extract productId
		if( setTupleProductId.size()>0 ){
			int productId = -1;
			for (Tuple tuple: setTupleProductId) {
				productId = tuple.get(0, Integer.class);
				result.add(productId);
			}
		}
		return result;
	}

	public List<ProductCountByCategory> searchCategoryPathCount( MultivaluedMap<String, String> queryParameters ){
		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
		Set<Integer> setProductId = productFilter(queryParameters);
		if( (setProductId==null) || (setProductId.size()<1) ){
			return result;
		}
		
		//	get categoryId and count
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//	FROM
		Root<Product> product = cq.from(Product.class);
		// 	ORDER BY
		Order order = cb.asc(product.get("category").get("id"));
		cq.orderBy(order);
		// GROUP BY		
		cq.groupBy( product.get("category").get("id") );
		//	SELECT
//		cb.count(product);
		cq.multiselect(
				product.get("category").get("id"),
				cb.count(product)
				);
		//	WHERE
		Predicate predicate = product.get("id").in(setProductId);
		cq.where(predicate);
		
		//	perform query		
		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
		List<Tuple> lstTuple = tq.getResultList();
		
//		convert to return format
//		NEED check if categoryId = -1: all
		int intCategoryId = -1;
		ProductCountByCategory pCount = null;
		for (Tuple tuple: lstTuple){
			intCategoryId = tuple.get(0, Integer.class);

			pCount = new ProductCountByCategory();
			pCount.setCategoryId( intCategoryId );
			pCount.setCategoryName( getCategoryName( intCategoryId ) );
			pCount.setCategoryPath( getCategoryPath( intCategoryId ) );
			pCount.setProductCount( tuple.get(1, Long.class).intValue() );
			//	store count and reset current category
			result.add(pCount);
		}
		
//		//		Count product for each category
//		if ( products.size() > 0 ) {
//			//	init result					
//			ProductCountByCategory pCount = new ProductCountByCategory();
//			//		set first categoryId to be current
//			//		start from category of the first product
//			int currentCategoryId = products.get(0).getCategory().getId();
//			pCount.setCategoryId(currentCategoryId);
//			pCount.setCategoryName( getCategoryName(currentCategoryId) );
//			pCount.setCategoryPath( getCategoryPath( currentCategoryId ) );
//			pCount.setProductCount( 0 );
//			result.add(pCount);
//			//		pCount and pCount in result point to the same object
//			//		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//			//		SHOULD USE GROUP BY & COUNT OF SQL TO COUNT
//			//		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//			for ( Product product: products ) {
//				//			store category id
//				//			if same category then increase count
//				if ( currentCategoryId == product.getCategory().getId() ){
//					pCount.setProductCount( pCount.getProductCount() + 1 );
//				} else { // else store count for current category then reset to count for new category
//					pCount = new ProductCountByCategory();
//					currentCategoryId = product.getCategory().getId();
//					pCount.setCategoryId( currentCategoryId );
//					pCount.setCategoryName( getCategoryName( currentCategoryId ) );
//					pCount.setCategoryPath( getCategoryPath( currentCategoryId ) );
//					pCount.setProductCount( 1 );
//					//	store count and reset current category
//					result.add(pCount);
//				}
//			}
//		}

		return result;
	}

//	public Map<String, Long> searchProductByCategoryCount( MultivaluedMap<String, String> queryParameters ){
//		
//		Map<String, Long> result = new HashMap<String, Long>();		
//		//	get all query parameters: categoryId, keyWord, a
//		Set<Integer> setProductId = productFilter(queryParameters);
//		if( (setProductId==null) || (setProductId.size()<1) ){
//			result.put("count", 0L);
//		} else {
//			result.put("count", new Long(setProductId.size()) );
//		}
//		
////		//		consumes products rest service
////		Client client = ClientBuilder.newClient();
////		String uriParameters = buildUriParameter( queryParameters );
////		//		client.property doesnt work
////		String strUrl = "";
////		if (categoryId > 0) {
////			strUrl = "http://localhost:8080/hailey-chemist/rest/products/count?categoryId=" + categoryId + "&" + uriParameters;
////		} else {
////			strUrl = "http://localhost:8080/hailey-chemist/rest/products/count" + "?" + uriParameters;
////		}
////		Map<String, Long> result =
////				client.target(strUrl)
////				.request(MediaType.APPLICATION_JSON)
////				.get(new GenericType<Map<String, Long>>() {
////				});		
//
//		return result;
//	}

	//	input: categoryId
	//	if category < 0 -> all categories
	//	product list must be sorted by categoryId
	//	can be extended to deal with query parameters.
	//	http://localhost:8080/hailey-chemist/rest/products?categoryId=4
//	public List<Product> searchProductByCategory( int categoryId, MultivaluedMap<String, String> queryParameters ){
//
//		String parameters = buildUriParameter( queryParameters );
//		//		consumes products rest service
//		Client client = ClientBuilder.newClient();
//		//		client.property doesnt work
//		String strUrl = "";
//		if (categoryId > 0) {
//			strUrl = "http://localhost:8080/hailey-chemist/rest/products?categoryId=" + categoryId;
//		} else {
//			strUrl = "http://localhost:8080/hailey-chemist/rest/products?orderBy=categoryId";
//		}
//
//		strUrl = strUrl + "&" + parameters;
//		List<Product> products =
//				client.target(strUrl)
//				.request(MediaType.APPLICATION_JSON)
//				.get(new GenericType<List<Product>>() {
//				});
//
//		return products;
//	}

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