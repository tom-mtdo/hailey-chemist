/**
 * input: productSearchModel{categoryId,  and query paras: keyword, categoryId, resultCount}
 * This view will add to model: 
 * resultCount: Number of product found
 * categories: list of all category to fill in the category select for search
 * attributeValues: list all values of product found group by each attribute, for advanced search 
 * Main view: product-search.html
 * 
 * 
 */

define([
        'utilities',
        'jquery',
        'bootstrap',
        'configuration',
        'app/views/desktop/product-page',
        'app/views/desktop/product-pagination',
        'text!../../../../templates/desktop/product-search.html',
//        'text!../../../../templates/desktop/product-search-attribute.html',
        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		ProductPageView,
        		ProductPaginationView,
        		productSearchTemplate
//        		attributeTemplate
        		) {

//	var AttributeView = Backbone.View.extend({
//		initialize: function(){
//		},
//
//		events:{
////			either of following two line works
////			"change input[class='checkboxAttributeValue']":"updateFilter"
//			"change :checkbox[class='checkboxAttributeValue']":"updateFilter"
//		},
//
//		render:function(){
//			var self = this;
//			$.getJSON(self.model.strUrlAttr, function( listAttributeValues ){
//				self.model.listAttributeValues = [];
//				$.each( listAttributeValues, function( attributeId, values ){
//					var attributeValues = {"id":attributeId, "name":values[0], "values":[]};
////					get all attribute values except the first one as it is attribute name
//					var i
//					for ( i=1; i<values.length; i++) {
//						attributeValues.values.push(values[i]);
//					}
//					self.model.listAttributeValues.push(attributeValues);
//				});
//				utilities.applyTemplate( $(self.el), attributeTemplate, {model:self.model} );
//			});
//		},
//
//		updateFilter:function( event ){
//			var self=this;
//			var $target = $(event.currentTarget)
//			var attrId = $target.data("attribute-id");
//			var attrValue = $target.val();
//			var checked = $target.is(':checked');
//
//			if ( checked ){
//				alert("Add filter: attributeId: " + attrId + ", value: " + attrValue);
//			} else {
//				alert("Remove filter: attributeId: " + attrId + ", value: " + attrValue);				
//			}
////			self.model.categoryId = catId;
//		}
//
//	});

	var ProductSearchView = Backbone.View.extend({
		initialize: function(){
			this.model.resultCount = 0;
		},

		events:{
			"keypress #txtSearchKeyWord":"updateOnEnter",
			"click a[class='categoryPath']":"showProductByCategory",
			"click #aClearCategory":"clearCategory",
			"click #aClearKeyWord":"clearKeyWord",
	//		either of following two line works
	//		"change input[class='checkboxAttributeValue']":"updateFilter"
			"change :checkbox[class='checkboxAttributeValue']":"updateFilter",
			"click #btnApplyFilter":"applyFilter"
		},

		render:function(){
			var self = this;
			self.strUrl="";
			self.strUrlAttr = "";
			self.paginationModel={};
			self.paginationModel.pageNo = 0;
			self.paginationModel.pageSize = 3;
			self.attributeModel={};
			self.gotCount = false;
			self.gotCategoryCount = false;
			self.gotCategories = false;
			self.gotAttributes = false;

			if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
				self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount" + "?keyWord=" + self.model.keyWord.trim();
				self.attributeModel.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute" + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?keyWord=" + self.model.keyWord.trim();
			} else {
				self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount";
				self.attributeModel.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute";
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId;
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count";
			}
			
//			get all categories, always to choose to search, currently not in use
			var strCatUrl = "http://localhost:8080/hailey-chemist/rest/categories";
			$.getJSON( strCatUrl, function(categories){
				self.gotCategories = true;
				self.model.categories = categories;
//				alert("No of category: " + self.model.categories.length);
				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes ) {
					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories} );
				}
			});

			//			update count number of product found
			//        	var strUrl = self.model.dataSourceCount; //config.baseUrl + "rest/products/count";
			$.getJSON(self.paginationModel.dataSourceCount, function(result){
				self.gotCount = true;
				$.each( result, function( key, val ) {
					if(key == "count"){
						self.model.resultCount = val;
//						if got all data then render        				
						if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes ) {
							utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories, } );
						}
					}
				});
			});

//			count product found by category
			$.getJSON(self.strUrl, function( productCountByCategories ){
//				show categories and number of its products
				self.gotCategoryCount = true;

//				if got all data then render
				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes ) {
					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories} );
				}
//				pagination
				self.productPaginationView = new ProductPaginationView( {model:self.paginationModel, el:$("#divSearchProductPagination")} );	        	
				self.productPaginationView.render();
			});

//			get attribute of product found
			$.getJSON(self.attributeModel.strUrlAttr, function( listAttributeValues ){
				self.gotAttributes = true;
//				self.model.listAttributeValues = [
//  					{"id":1, "name":"Total Weight", "type":"int", "values":["500","1000"] },
//  					{"id":2, "name":"Content Weight", "type":"int", "values":["1000","500"] },
//  					];
				
				self.model.listAttributeValues = [];
				$.each( listAttributeValues, function( attributeId, values ){
					var attributeValues = {"id":attributeId, "name":values[0], "values":[]};
//					get all attribute values except the first one as it is attribute name
					var i
					for ( i=1; i<values.length; i++) {
						attributeValues.values.push(values[i]);
					}
					self.model.listAttributeValues.push(attributeValues);
				});
				
//				if got all data then render
				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes ) {
					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories} );
				}
			});
			
			return self;
		},

		updateModel:function(){
			var value = this.$("#txtSearchKeyWord").val().trim();
			if(value) {
				this.model.keyWord = value;
//				need to change to use event listener to render
				this.render();
			}
		},

		updateOnEnter:function(event){
			if ( event.which == 13 ) {
				this.updateModel();
			}
		},

		showProductByCategory:function(event){
			var self = this;
			var catId = $(event.currentTarget).data("category-id");
			self.model.categoryId = catId;
			self.render();
		},

		clearCategory:function(){
			this.model.categoryId = -1;
			this.render();
		},

		clearKeyWord:function(){
			this.model.keyWord = "";
			this.render();
		},
		
		updateFilter:function( event ){
			var self=this;
			var $target = $(event.currentTarget)
			var attrId = $target.data("attribute-id");
			var attrValue = $target.val();
			var checked = $target.is(':checked');
	
			if ( checked ){
				alert("Add filter: attributeId: " + attrId + ", value: " + attrValue);
			} else {
				alert("Remove filter: attributeId: " + attrId + ", value: " + attrValue);				
			}
	//		self.model.categoryId = catId;
		},

		applyFilter:function(){
			alert("Apply filter!!");
		}
		
	});

	return ProductSearchView;

});