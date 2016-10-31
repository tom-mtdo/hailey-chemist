package com.mtdo.haileychemist.rest;

import com.mtdo.haileychemist.model.Category;

// for insert a new category
// always insert to first left for now
public class CategoryPostRequest {
	private String newCategoryName;
	private String newCategoryDescription;
	private int parentId; 
//	categoryPostRequest.newCategoryName="Other"
//	categoryPostRequest.newCategoryDescription="Other products"
//	categoryPostRequest.parentId=1;
	public CategoryPostRequest() {
	}

	
	public String getNewCategoryName() {
		return newCategoryName;
	}
	public void setNewCategoryName(String newCategoryName) {
		this.newCategoryName = newCategoryName;
	}
	public String getNewCategoryDescription() {
		return newCategoryDescription;
	}
	public void setNewCategoryDescription(String newCategoryDescription) {
		this.newCategoryDescription = newCategoryDescription;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	
}
