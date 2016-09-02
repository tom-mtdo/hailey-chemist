package com.mtdo.haileychemist.rest;

import java.util.List;

import com.mtdo.haileychemist.model.Product;

public class ProductSearchResult {
	private List<Product> products;
	private List<ProductCountByCategory> pCountByCategory;
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<ProductCountByCategory> getCounts() {
		return pCountByCategory;
	}
	public void setCounts(List<ProductCountByCategory> counts) {
		this.pCountByCategory = counts;
	}
	
}
