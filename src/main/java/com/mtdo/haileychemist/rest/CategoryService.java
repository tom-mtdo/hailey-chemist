package com.mtdo.haileychemist.rest;
import java.util.ArrayList;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.mtdo.haileychemist.model.Category;

@Path("/categories")
@Stateless
public class CategoryService extends BaseEntityService<Category>{
	public CategoryService() {
		super(Category.class);
	}

	@Override
	protected Predicate[] extractPredicates(MultivaluedMap<String, String> queryParameters,
			CriteriaBuilder criteriaBuilder, Root<Category> category) {
		Predicate[] predicates = new Predicate[]{};
		
		return predicates;
	}

	//	return path to a category (secified by Id)
	//	if id = null then return 
	@Path("/path/{categoryId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//	public Map<Integer, String> getPath(@PathParam("categoryId") int categoryId){
	public Map<Integer, List<String>> getPath(@PathParam("categoryId") int categoryId){
		EntityManager em = getEntityManager();
		Map<Integer, List<String>> result = getCategoryPath(categoryId, em);
		return result;
	}
	
	public static Map<Integer, List<String>> getCategoryPath(int categoryId, EntityManager em){

		//		List<Predicate> lstPredicates = new ArrayList<Predicate>();

		//		get entity manager
//		EntityManager em = getEntityManager();
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

		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		//		convert tuples to a desired type
		List<String> path = new ArrayList<String>();
		int count = 1;
		for (Tuple tuple: qResult){
			// exclude the first current category out of the path			
			if ( (count<qResult.size()) && (count>1)) {
				path.add( tuple.get(1, String.class) );
			}
			count++;
//				//			int id = tuple.get(0, Integer.class);
//				//			if first then no need comma
//				if ( path.trim().length() < 1){
//					path = tuple.get(1, String.class);
//				} else {
//					path = path + ">" + tuple.get(1, String.class);	
//				}
//				
//			}
		}
		result.put(categoryId, path);

		return result;
	}
 

	//	need test
	//	input: newCategoryName,newCategoryDescription,parentCategoryId
	//	return: a new Category object
	//	insert a category to be first child of its parent
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postCategory(CategoryPostRequest catRequest) {
		//		LOCK TABLE nested_category WRITE; doesn't work on H2
		//		check if parent is null
		//		get parent left
		int myLeft = 0;
		if (catRequest.getParentId() > 0){
			myLeft = getMyleft( catRequest.getParentId() );			
		}

		//		update all node on the right
		getEntityManager()
		.createQuery("UPDATE Category c SET c.rgt = c.rgt+2 WHERE c.rgt > :myLeft")
		.setParameter("myLeft", myLeft)
		.executeUpdate();

		getEntityManager()
		.createQuery("UPDATE Category c SET c.lft = c.lft+2 WHERE c.lft > :myLeft")
		.setParameter("myLeft", myLeft)
		.executeUpdate();

		Category category = new Category();
		category.setName(catRequest.getNewCategoryName());
		category.setDescription(catRequest.getNewCategoryDescription());
		category.setLft( myLeft + 1 );
		category.setRgt( myLeft + 2 );

		getEntityManager().persist(category);
		getEntityManager().flush();
		//		insert new category
		//		UNLOCK TABLES;

		return Response.ok().entity(catRequest).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	public int getMyleft(int parentId){
		int result = 0;	
		Category category = getEntityManager().find(Category.class, parentId);
		if (category != null) {
			result = category.getLft();
		}
		return result;
	}

	//	delete leaf node only, otherwise error
	@DELETE 
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteCategory(@PathParam("id") int categoryId) {
		Category category = getEntityManager().find(Category.class, categoryId);
		if (category == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		int myLeft = category.getLft();
		int myRight = category.getRgt();
		int myWidth = myRight - myLeft + 1;

		//		LOCK TABLE nested_category WRITE;
		getEntityManager()
		.createQuery("DELETE FROM Category c WHERE c.id = :id")
		.setParameter("id",category.getId())
		.executeUpdate();

		//		update all node on the right
		getEntityManager()
		.createQuery("UPDATE Category c SET c.rgt = c.rgt - :myWidth WHERE c.rgt > :myRight")
		.setParameter("myWidth", myWidth)
		.setParameter("myRight", myRight)
		.executeUpdate();

		getEntityManager()
		.createQuery("UPDATE Category c SET c.lft = c.lft - :myWidth WHERE c.lft > :myRight")
		.setParameter("myWidth", myWidth)
		.setParameter("myRight", myRight)
		.executeUpdate();
		//		UNLOCK TABLES;

		return Response.noContent().build();
	}
	
}
