package com.mtdo.haileychemist.rest;

import com.mtdo.haileychemist.model.Category;

// for insert a new category
// always insert to first left for now
public class CategoryPostRequest {
	private Category category;
	private Category parent;
	
	public CategoryPostRequest() {
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	
}
