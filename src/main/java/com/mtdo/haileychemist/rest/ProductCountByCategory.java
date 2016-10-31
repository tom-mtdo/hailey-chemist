package com.mtdo.haileychemist.rest;

import java.util.ArrayList;
import java.util.List;

public class ProductCountByCategory {

	
	private int categoryId;
	private String categoryName;
	//		path from top category
	private List<String> categoryPath = new ArrayList<String>();
	//	number of product in this category
	private int productCount;
	
	public ProductCountByCategory() {
	}

	public ProductCountByCategory(int categoryId, String categoryName, List<String> categoryPath, int productCount) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryPath = categoryPath;
		this.productCount = productCount;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public List<String> getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(List<String> categoryPath) {
		this.categoryPath = categoryPath;
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
