package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequest {
	private List<OrderDetailRequest> orderDetailRequests = new ArrayList<OrderDetailRequest>();
	private String email;

	public PurchaseRequest() {
	}

	public PurchaseRequest(List<OrderDetailRequest> orderDetailRequests, String email) {
		this.orderDetailRequests = orderDetailRequests;
		this.email = email;
	}

	public List<OrderDetailRequest> getOrderDetailRequests() {
		return orderDetailRequests;
	}

	public void setPurchaseDetails(List<OrderDetailRequest> orderDetailRequests) {
		this.orderDetailRequests = orderDetailRequests;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}