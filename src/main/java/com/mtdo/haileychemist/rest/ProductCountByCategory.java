package com.mtdo.haileychemist.rest;

public class ProductCountByCategory {

	
	private int categoryId;
	private String categoryName;
	//		path from top category
	private String path;
	//	number of product in this category
	private int productCount;
	
	public ProductCountByCategory() {
	}
	public ProductCountByCategory(int categoryId, String path) {
		super();
		this.categoryId = categoryId;
		this.path = path;
	}
	public ProductCountByCategory(int categoryId, String path, int productCount) {
		this.categoryId = categoryId;
		this.path = path;
		this.productCount = productCount;
	}
	
	public ProductCountByCategory(int categoryId, String categoryName, String path, int productCount) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.path = path;
		this.productCount = productCount;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
