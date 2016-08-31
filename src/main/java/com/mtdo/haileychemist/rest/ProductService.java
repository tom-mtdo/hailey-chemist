package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.impl.io.SocketOutputBuffer;

import com.mtdo.haileychemist.model.Product;

@Path("/products")
@Stateless
public class ProductService extends BaseEntityService<Product>{

	public ProductService(){
		super(Product.class);
	}

	@Override
	protected Predicate extractPredicate(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Product> product) {
		Predicate predicate = null;
        if (queryParameters.containsKey("keyWord")) {
        	System.out.println( "Key word: " + queryParameters.getFirst("keyWord") );
        	predicate = criteriaBuilder.like( product.<String>get("name"), "%" + queryParameters.getFirst("keyWord") + "%" );
        }
        System.out.println( "Predicate: " + predicate );
		return predicate;
    }

//	@Override
//	protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters, CriteriaBuilder criteriaBuilder, Root<Product> product) {
//		List<Predicate> predicates =  new ArrayList<Predicate>();
//        if (queryParameters.containsKey("keyWord")) {
//        	System.out.println( "Key word: " + queryParameters.getFirst("keyWord") );
//        	Predicate predicate = criteriaBuilder.like( product.<String>get("name"), "*" + queryParameters.getFirst("keyWord") + "*" );
//        	predicates.add(predicate);
//        	
//        }
//
//		Predicate[] result = ((List<Predicate>)predicates).toArray(new Predicate[predicates.size()]);
//		return result;
//    }
	
}
