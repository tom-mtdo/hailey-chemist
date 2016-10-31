package com.mtdo.haileychemist.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mtdo.haileychemist.model.Customer;
import com.mtdo.haileychemist.model.OrderDetail;
import com.mtdo.haileychemist.model.Product;
import com.mtdo.haileychemist.model.Purchase;

@Path("/purchases")
@Stateless
public class PurchaseService extends BaseEntityService<Purchase>{

	public PurchaseService(){
		super(Purchase.class);
	}

	// Data to Rest service use PurchaseRequest to simplify communication with REST clients
	// this function will convert PurchaseRequest to Purchase entity to store in DB
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPurchase(PurchaseRequest purchaseRequest){
		//		load customer
		Customer customer = getCustomerByEmail(purchaseRequest.getEmail());

		// init purchase with customer
		Purchase purchase = new Purchase();
		purchase.setCustomer(customer);

		//		OrderDetail orderDetail = new OrderDetail();
		//		Product product = getEntityManager().find(Product.class, 1 );
		//		orderDetail.setProduct(product);
		//		orderDetail.setQuantity(1.0f);
		//		orderDetail.setPurchase(purchase);
		//		purchase.getOrderDetails().add(orderDetail);

		// fill in order detail		
		for ( OrderDetailRequest orderDetailRequest : purchaseRequest.getOrderDetailRequests() ){
			OrderDetail orderDetail = new OrderDetail();
			Product product = getEntityManager().find(Product.class, orderDetailRequest.getProductId());
			orderDetail.setProduct(product);
			orderDetail.setQuantity(orderDetailRequest.getQuantity());
			orderDetail.setPurchase(purchase);
			orderDetail.setPricePerUnit(product.getSale().getPrice());

			// add to purchase
			purchase.getOrderDetails().add(orderDetail);
		}

		//		persistent
		getEntityManager().persist(purchase);

		return Response.ok().entity(purchase).type(MediaType.APPLICATION_JSON_TYPE).build();
	}


	public Customer getCustomerByEmail(String email){
		Customer customer = null;
		// check if customer exist
		List<Customer> customers = (List<Customer>) getEntityManager()
				.createQuery("select c from Customer c where c.email = :email", Customer.class)
				.setParameter("email", email).setMaxResults(7).setFirstResult(7).getResultList();

		//		if customer not exist then create new customer
		if ( customers == null || customers.isEmpty() ) {
			customer = new Customer();
			customer.setEmail(email);
			getEntityManager().persist(customer);
			// to get make sure get the auto generated id
			getEntityManager().flush();
		} else {
			customer = customers.get(0);
		}

		return customer;
	}
}
