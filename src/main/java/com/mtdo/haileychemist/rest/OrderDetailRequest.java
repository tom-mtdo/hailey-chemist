package com.mtdo.haileychemist.rest;
public class OrderDetailRequest {
	private int productId;
	private float quantity;
	
	public OrderDetailRequest() {
	}

	public OrderDetailRequest(int productId, float quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
}