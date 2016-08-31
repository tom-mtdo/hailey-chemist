package com.mtdo.haileychemist.rest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.mtdo.haileychemist.model.Category;

@Path("/categories")
@Stateless
public class CategoryService extends BaseEntityService<Category>{
	public CategoryService() {
		super(Category.class);
	}

	@Override
	protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters,
			CriteriaBuilder criteriaBuilder, Root<Category> root) {
		Predicate[] predicates = new Predicate[]{};
		
		return predicates;
	}
	
//	return path to a category (secified by Id)
//	if id = null then return 
	@Path("/path/{categoryId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
//	public Map<Integer, String> getPath(@PathParam("categoryId") int categoryId){
	public List<Category> getPath(@PathParam("categoryId") int categoryId){
		Map<Integer, String> result = new HashMap<Integer, String>();
		result.put(categoryId, "geting category path");
//
		EntityManager em = getEntityManager();
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();		
		final CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
		Root<Category> node = criteriaQuery.from(Category.class);
//		Root<Category> parent = criteriaQuery.from(Category.class);

		Predicate[] predicates = new Predicate[]{};
		criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);
		
		TypedQuery<Category> query = em.createQuery(criteriaQuery);
		return query.getResultList();
//		
//		return result;
	}
	
	

}
