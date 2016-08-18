package com.mtdo.haileychemist.rest;

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
////		load customer
//		Customer customer = getCustomerByEmail(purchaseRequest.getEmail());
		Customer customer = getEntityManager().find(Customer.class, 2);
		
		// init purchase with customer
		Purchase purchase = new Purchase();
		purchase.setCustomer(customer);

//		// fill in order detail		
//		for ( OrderDetailRequest orderDetailRequest : purchaseRequest.getPurchaseDetails() ){
//			OrderDetail orderDetail = new OrderDetail();
//			Product product = getEntityManager().find(Product.class, orderDetailRequest.getProductId());
//			orderDetail.setProduct(product);
//			orderDetail.setQuantity(orderDetailRequest.getQuantity());
//
//			// add to purchase
//			purchase.getOrderDetails().add(orderDetail);
//		}
//		
////		persistent
		getEntityManager().persist(purchase);
				
		return Response.ok().entity(purchase).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	
	// need fix: if found customer return id
	// if not found create a customer, return id
	public Customer getCustomerByEmail(String email){
        Customer customer = (Customer) getEntityManager()
                .createQuery("select c from Customer c where c.email = :email", Customer.class)
                .setParameter("email", email).getSingleResult();
        return customer;
	}
}
