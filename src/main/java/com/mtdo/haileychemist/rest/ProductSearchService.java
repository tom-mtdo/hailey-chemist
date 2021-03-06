package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

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
//	@Inject
//	private CategoryService categoryService;

//	=============================================================================================================================
//	works
//	=============================================================================================================================	
	//	get products belong to a category
	//	if categoryId = -1 the all category
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
	
	//	count product found: used for pagination, search page
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
	public Set<Integer> productFilter( MultivaluedMap<String, String> queryParameters  ){
		Set<Integer> result = new HashSet<Integer>();		

		List<Tuple> lstTupleProductId = new ArrayList<Tuple>();
		int numberOfAttribute = 0;
		int attrId = -1;
		List<String> attrValues = new ArrayList<String>();
		Iterator<String> it = queryParameters.keySet().iterator();
		while ( it.hasNext() ){
			String theKey = (String)it.next();
			if( (theKey.length()>4) && (theKey.substring(0,4).contentEquals("attr")) ){
				numberOfAttribute++;
				//				which attribute
				attrId = Integer.parseInt(theKey.substring(4));
				attrValues = queryParameters.get(theKey);
				List<Tuple> lstTupleTmp = filterSingleAttribute(queryParameters, attrId, attrValues);
				lstTupleProductId.addAll(lstTupleTmp);
			}
		}

		if ( numberOfAttribute > 0 ){
			//	convert List<Tuple> to List<Integer> of productId
			if( lstTupleProductId.size()>0 ){
				int productId = -1;
				List<Integer> lstAllProductIdFound = new ArrayList<Integer>();
				for (Tuple tuple: lstTupleProductId) {
					productId = tuple.get(0, Integer.class);
					lstAllProductIdFound.add(productId);
				}
				//	find product has all attributes match
				Set<Integer> setUniqueProductIdFound = new HashSet<Integer>(lstAllProductIdFound);	
				for(Integer pId: setUniqueProductIdFound){
					if ( Collections.frequency(lstAllProductIdFound,pId) == numberOfAttribute ){
						result.add(pId);
					}
				}
			}
		}

		//	if no attribute, then query with other parameter (attributeId, keyWord .etc)		
		if ( numberOfAttribute <= 0 ){
			List<Tuple> lstTupleTmp = filterNoAttribute(queryParameters);
			if( lstTupleTmp.size() > 0 ){
				int productId = -1;
				for (Tuple tuple: lstTupleTmp) {
					productId = tuple.get(0, Integer.class);
					result.add(productId);
				}
			}
		}

		return result;
	}
	
	//	Filter product by single attribute
	//	input: predicates which contains: categoryId, keyWord
	//		 : attributeId
	//		 : set of values
	public List<Tuple> filterSingleAttribute(MultivaluedMap<String, String> queryParameters, int attributeId, List<String> attributeValues ){
		List<Tuple> result = new ArrayList<Tuple>();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//		FROM
		Root<Product> product = cq.from(Product.class);
		Join<Product, ProductAttribute> productAttribute = product.join("productAttributes");
		//		SELECT
		cq.multiselect( product.get("id") );
		cq.groupBy( product.get("id") );
		// 		ORDER BY
		Order order = cb.asc( product.get("id") );
		cq.orderBy( order );
		//	WHERE		
		Predicate[] predicates = ProductService.extractPredicatesImpl(queryParameters, cb, product);		
		//	query by attributes	which are from url parameters
		List<Predicate> lstPredicate = new ArrayList<Predicate>(Arrays.asList(predicates));

		Predicate predicateAttrId = cb.equal( productAttribute.get("attribute").get("id"), attributeId );
		Predicate predicateAttrValue = productAttribute.get("attribute_value").in(attributeValues);
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
		Predicate[] newPredicates = new Predicate[lstPredicate.size()];
		lstPredicate.toArray(newPredicates);
		cq.where(newPredicates);

		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
		result = tq.getResultList();

		return result;
	}	

	//	query product with categoryId, keyWord (and maybe other criteria) which are stored in queryParameters
	//	 but no attributes
	public List<Tuple> filterNoAttribute(MultivaluedMap<String, String> queryParameters ){
		List<Tuple> result = new ArrayList<Tuple>();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//		FROM
		Root<Product> product = cq.from(Product.class);
		//		SELECT
		cq.multiselect( product.get("id") );
		cq.groupBy( product.get("id") );
		// 		ORDER BY
		Order order = cb.asc( product.get("id") );
		cq.orderBy( order );
		//	WHERE		
		Predicate[] predicates = ProductService.extractPredicatesImpl(queryParameters, cb, product);		
		cq.where(predicates);

		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
		result = tq.getResultList();

		return result;		
	}	
	
//	=============================================================================================================================
//	end of works
//	=============================================================================================================================	

	//	return product count by category: 
	//	a list of categoryId, categoryName, categoryPath, productCount 
	@Path("/{categoryId}/pathCount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductCountByCategory> getCategoryPathAndProductCount(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo ){
		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
		queryParameters.add("categoryId", "" + categoryId);

		List<ProductCountByCategory> result = searchCategoryPathCount( queryParameters );
		return result;
	}
	
	public List<ProductCountByCategory> searchCategoryPathCount( MultivaluedMap<String, String> queryParameters ){
		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
		Set<Integer> setProductId = productFilter(queryParameters);

//		if( (setProductId==null) || (setProductId.size()<1) ){
//			return result;
//		}

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
				product.get("category").get("name"),
				cb.count(product)
				);
		//	WHERE
		Predicate predicate = product.get("id").in(setProductId);
		cq.where(predicate);

		//	perform query		
		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
		List<Tuple> lstTuple = tq.getResultList();

		int intCategoryId = -1;
		String strCategoryName = "";
		int intPCount = 0;
		List<String> lstPath = new ArrayList<String>();
		
		ProductCountByCategory pCount = null;
		for (Tuple tuple: lstTuple){
			intCategoryId = tuple.get(0, Integer.class);
			strCategoryName = tuple.get(1, String.class);
			intPCount = tuple.get(2, Long.class).intValue();
			lstPath = getCategoryPath( intCategoryId );

			pCount = new ProductCountByCategory();
			pCount.setCategoryId( intCategoryId );
			pCount.setCategoryName( strCategoryName );
			pCount.setCategoryPath( lstPath );
			pCount.setProductCount( intPCount );
			result.add(pCount);
		}

//	
//		//		convert to return format
//		int intCategoryId = -1;
//		ProductCountByCategory pCount = null;
//		for (Tuple tuple: lstTuple){
//			intCategoryId = tuple.get(0, Integer.class);
//
//			pCount = new ProductCountByCategory();
//			pCount.setCategoryId( intCategoryId );
//			pCount.setCategoryName( getCategoryName( intCategoryId ) );
//			pCount.setCategoryPath( getCategoryPath( intCategoryId ) );
//			pCount.setProductCount( tuple.get(1, Long.class).intValue() );
//			result.add(pCount);
//		}
		
		return result;
	}
	
	
//	@Path("/{categoryId}/pathCount")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<ProductCountByCategory> getCategoryPathAndProductCount(  @PathParam("categoryId") int categoryId, @Context UriInfo uriInfo ){
//		MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
//		queryParameters.add("categoryId", "" + categoryId);
//
//		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
//		result = searchCategoryPathCount( queryParameters );
//		return result;
//	}
	

//	public List<ProductCountByCategory> searchCategoryPathCount( MultivaluedMap<String, String> queryParameters ){
//		List<ProductCountByCategory> result = new ArrayList<ProductCountByCategory>();
//		Set<Integer> setProductId = productFilter(queryParameters);
//		if( (setProductId==null) || (setProductId.size()<1) ){
//			return result;
//		}
//
//		//	get categoryId and count
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//		//	FROM
//		Root<Product> product = cq.from(Product.class);
//		// 	ORDER BY
//		Order order = cb.asc(product.get("category").get("id"));
//		cq.orderBy(order);
//		// GROUP BY		
//		cq.groupBy( product.get("category").get("id") );
//		//	SELECT
//		//		cb.count(product);
//		cq.multiselect(
//				product.get("category").get("id"),
//				cb.count(product)
//				);
//		//	WHERE
//		Predicate predicate = product.get("id").in(setProductId);
//		cq.where(predicate);
//
//		//	perform query		
//		TypedQuery<Tuple> tq = entityManager.createQuery(cq);
//		List<Tuple> lstTuple = tq.getResultList();
//
//		//		convert to return format
//		int intCategoryId = -1;
//		ProductCountByCategory pCount = null;
//		for (Tuple tuple: lstTuple){
//			intCategoryId = tuple.get(0, Integer.class);
//
//			pCount = new ProductCountByCategory();
//			pCount.setCategoryId( intCategoryId );
//			pCount.setCategoryName( getCategoryName( intCategoryId ) );
//			pCount.setCategoryPath( getCategoryPath( intCategoryId ) );
//			pCount.setProductCount( tuple.get(1, Long.class).intValue() );
//			result.add(pCount);
//		}
//		return result;
//	}
	
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


	//	public String buildUriParameter( MultivaluedMap<String, String> queryParameters ) {
	//		String result = "";
	//
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
	//
	//		result = parameters;
	//		return result;
	//	}

	//	get path from service:
	//	http://localhost:8080/hailey-chemist/rest/categories/path/3
	
	public List<String> getCategoryPath( int categoryId ){
		List<String> result = new ArrayList<String>();
		
		Map<Integer, List<String>> cPath = new HashMap<Integer, List<String>>();
		if (categoryId > 0) {
			cPath = CategoryService.getCategoryPath(categoryId, entityManager);
			if ( cPath.containsKey(categoryId) ){
				result = cPath.get(categoryId);
			}
		} 
		return result;
	}
	
//	public List<String> getCategoryPath( int categoryId ){
//		List<String> result = new ArrayList<>();
//
//		Client client = ClientBuilder.newClient();
//		//		client.property doesnt work
//		String strUrl = "";
//		if (categoryId > 0) {
//			strUrl = "http://localhost:8080/hailey-chemist/rest/categories/path/" + categoryId;
//			Map<Integer, List<String>> cPath =
//					client.target(strUrl)
//					.request(MediaType.APPLICATION_JSON)
//					.get(new GenericType<Map<Integer, List<String>>>() {
//					});
//			if ( cPath.containsKey(categoryId) ){
//				result = cPath.get(categoryId);
//			}
//		} 
//		return result;
//	}

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