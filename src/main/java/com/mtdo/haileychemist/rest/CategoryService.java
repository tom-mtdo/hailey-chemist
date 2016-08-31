package com.mtdo.haileychemist.rest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
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
	public Map<Integer, String> getPath(@PathParam("categoryId") int categoryId){

		//		List<Predicate> lstPredicates = new ArrayList<Predicate>();

		//		get entity manager
		EntityManager em = getEntityManager();
		//		get criteia builder
		CriteriaBuilder cb = em.getCriteriaBuilder();
		//		create query helper which allow select only few columns of a table - using tuple
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//		from clauses
		Root<Category> node = cq.from(Category.class);
		Root<Category> parent = cq.from(Category.class);
		//		Predicate predicate = cb.equal( node.get("id"), categoryId );

		//		where clauses
		Predicate[] predicates = new Predicate[]{
				cb.between( node.<Integer>get("lft"), parent.<Integer>get("lft"), parent.<Integer>get("rgt")),
				cb.equal( node.get("id"), categoryId )
		};
		cq.where( predicates );
		//		say which columns to select
		cq.multiselect(
				parent.get("id"),
				parent.get("name") );
		//		order by
		cq.orderBy( cb.asc(parent.get("lft")) );

		//		create real query with all settings above
		TypedQuery<Tuple> tq = em.createQuery(cq);
		//		perform query and get result in a list of tuples
		List<Tuple> qResult = tq.getResultList();
		Map<Integer, String> result = new HashMap<Integer, String>();

		//		convert tuples to a desired type
		String path ="";
		for (Tuple tuple: qResult){
			//			int id = tuple.get(0, Integer.class);
			//			if first then no need comma
			if ( path.trim().length() < 1){
				path = tuple.get(1, String.class);
			} else {
				path = path + ">" + tuple.get(1, String.class);	
			}
		}
		result.put(categoryId, path);

		return result;
	}



}
