package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import com.mtdo.haileychemist.model.Product;

@Path("/products")
@Stateless
public class ProductService extends BaseEntityService<Product>{

	public ProductService(){
		super(Product.class);
	}

	//	@Override
	//	protected Predicate extractPredicate(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Product> product) {
	//		Predicate predicate = null;
	//        if (queryParameters.containsKey("keyWord")) {
	//        	System.out.println( "Key word: " + queryParameters.getFirst("keyWord") );
	//        	predicate = criteriaBuilder.like( product.<String>get("name"), "%" + queryParameters.getFirst("keyWord") + "%" );
	//        }
	//        System.out.println( "Predicate: " + predicate );
	//		return predicate;
	//    }

	@Override
	protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Product> product) {
		List<Predicate> predicates =  new ArrayList<Predicate>();
		
		//		http://localhost:8080/hailey-chemist/rest/products?keyWord=oi
		if (queryParameters.containsKey("keyWord")) {
			System.out.println( "Key word: " + queryParameters.getFirst("keyWord") );
			Predicate predicate = criteriaBuilder.like( product.<String>get("name"), "%" + queryParameters.getFirst("keyWord") + "%" );
			predicates.add(predicate);        	
		}

		//		get product belong to a category
		//		http://localhost:8080/hailey-chemist/rest/products?categoryId=4
		if ( queryParameters.containsKey("categoryId") ) {
			System.out.println( "CategoryId: " + queryParameters.getFirst("categoryId") );
			Predicate predicate = criteriaBuilder.equal( product.get("category").get("id"), queryParameters.getFirst("categoryId") );
			predicates.add(predicate);        	
		}

		Predicate[] result = ((List<Predicate>)predicates).toArray(new Predicate[predicates.size()]);
		return result;
	}
	
//	orders =  new Order[]{criteriaBuilder.asc(product.get("category").get("id")), criteriaBuilder.asc(product.get("id"))};
	@Override
	protected Order[] createOrderBy(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Product> product) {
		Order[] orders = new Order[]{};
		Order order = null;
		String strColumn = "";
		String strOrderType = "";
		
		if ( queryParameters.containsKey("orderBy") ) {
			System.out.println("found ordebBy para");
			strColumn = queryParameters.getFirst("orderBy");
			if ( queryParameters.containsKey("orderType") ) {strOrderType = queryParameters.getFirst("orderType");}
//			if order by categoryid - this is odd, need to redesign
			if ( strColumn.trim().equalsIgnoreCase("categoryId") ){
				if ( (strOrderType.trim().length()>0) && (strOrderType.trim().equalsIgnoreCase("Desc")) ) {
					order = criteriaBuilder.desc(product.get("category").get("id"));
				} else {
					order = criteriaBuilder.asc(product.get("category").get("id"));
				}
				orders = new Order[]{order, criteriaBuilder.asc(product.get("id"))};
			}
		}
		
		return orders;
	}



}
