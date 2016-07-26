package com.mtdo.haileychemist.rest;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

import com.mtdo.haileychemist.model.Purchase;

@Path("/purchases")
@Stateless
public class PurchaseService extends BaseEntityService<Purchase>{

	public PurchaseService(){
		super(Purchase.class);
	}
	
}
