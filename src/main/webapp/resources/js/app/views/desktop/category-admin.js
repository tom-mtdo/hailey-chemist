/**
 * 
 */

define([
        'utilities',
        'jquery',
        'bootstrap',
        'configuration',
        'text!../../../../templates/desktop/category-admin.html',

        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		categoryAdminTemplate) {

	var CategoryAdminView = Backbone.View.extend({
    	initialize: function(){
    	},
    	    			
    	events:{
    		"click #btnCategorySubmit":"saveCategory",
    		"click #btnCategoryDelete":"deleteCategory",
    		
    	},
		
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), categoryAdminTemplate, {} );
		},
    	
		saveCategory:function(){
			alert("Category saved!");

//			purchaseRequest.email = $("#email").val();
// prepare data
			var categoryPostRequest = {};
			categoryPostRequest.newCategoryName="Other"
			categoryPostRequest.newCategoryDescription="Other products"
			categoryPostRequest.parentId=1;
    		
            $.ajax({
            	url: (config.baseUrl + "rest/categories"),
                data:JSON.stringify(categoryPostRequest),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success:function (category) {
                	alert("Category added!");
                }
            }).error(function (error) {
            	alert("Add category error");
            })

		},
		
		deleteCategory:function(){
			var categoryId = 8;
            $.ajax({
            	url: (config.baseUrl + "rest/categories/" + categoryId),
                type:"DELETE",
                success:function (category) {
                	alert("Category deleted!");
                }
            }).error(function (error) {
            	alert("Delete category error");
            })			
		}
		
	});
	 
	return CategoryAdminView;  
})