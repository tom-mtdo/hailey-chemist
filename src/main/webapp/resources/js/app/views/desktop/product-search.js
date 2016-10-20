/**
 * input: productSearchModel{categoryId,  and query paras: keyword, categoryId, resultCount, productCountByCategories}
 * 		filters = [];	// filter by attribute value
 * This view will add to model: 
 * resultCount: Number of product found
 * categories: list of all category to fill in the category select for search
 * attributeValues: list all values of product found group by each attribute, for advanced search 
 * Main view: product-search.html
 * 
 * categoryId: always has value: -1 means all
 * keyWord: always has value: empty string "" means no keyword
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
        'app/views/desktop/cart-embedded',
        'text!../../../../templates/desktop/product-search.html',
        'text!../../../../templates/desktop/category-embedded.html'
        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		ProductPageView,
        		ProductPaginationView,
        		EmbeddedCart,
        		productSearchTemplate,
        		categoryEmbeddedTemplate
        		) {

	var ProductSearchView = Backbone.View.extend({
		initialize: function(){
			var self = this;
			self.model.resultCount = 0;
//			for clear filter of 1 value of 1 attribute
//			self.model.filters=[]; //[{ attributeId: attrId, attributeName: attrName, attributeValue:attrValue }]
//			for clear filter of all value of 1 attribute
			self.model.filterAttributes=[]; // [{attributeId, attributeName}] no double value
		},

		events:{
			"click #btnSearch":"searchKeyword",
			"keypress #txtSearchKeyWord":"updateOnEnter",
			"click a[class='categoryPath']":"showProductByCategory",
			"click #aClearCategory":"clearCategory",
			"click #aClearKeyWord":"clearKeyWord",
	//		either of following two line works
	//		"change input[class='checkboxAttributeValue']":"updateFilter"
			"change :checkbox[class='checkboxAttributeValue']":"updateFilter",
			"click #btnApplyFilter":"applyFilter",
			"click #btnClearAllFilters":"clearAllFilters",
			"click #btnResetSearch":"resetSearch",
			"click a[class='clearFilter']":"clearFilter",
			"click a[class='clearFilters']":"clearFilters"
//			"click #btnCheckout":"showCart"
		},

		render:function(){
			var self = this;
			self.strUrl="";
			self.strUrlAttr = "";
			self.paginationModel={};
			self.paginationModel.pageNo = 0;
			self.paginationModel.pageSize = 4;
			self.gotCount = false;
			self.gotCategoryCount = false;
			self.gotCategories = true;
			self.gotCategoryName = false;
//			self.gotCategories = false;
			self.gotAttributes = false;
			
//			if want to make sure then uncomment following line
//			$(self.el).empty();

			if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
				self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount" + "?keyWord=" + self.model.keyWord.trim();
				self.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute" + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?keyWord=" + self.model.keyWord.trim();
			} else {
				self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount";
				self.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute";
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId;
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count";
			}
			
			// add other filter: like attribute filters, which are inform of "attr" + attributeId = attributeValue
			if ( self.model.filters.length > 0 ) {
//				convert filters to string to add to url
				var strFilter = "";
				var i = 0;
				$.each(self.model.filters, function(index, flt){
					if (i==0){	// if first element then no &
						strFilter = strFilter + "attr" + flt.attributeId + "=" + flt.attributeValue;
					} else {
						strFilter = strFilter + "&attr" + flt.attributeId + "=" + flt.attributeValue;	
					}					
					i++;
				});
				
//				$.each(self.model.filters, function(index, flt){
//					strFilter = strFilter + flt + "&";
//				});
				
//				add to url
				$.each(self.model.filters, function(index, flt){
					if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
						self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount" + "?keyWord=" + self.model.keyWord.trim() + "&" + strFilter;
						self.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute" + "?keyWord=" + self.model.keyWord.trim() + "&" + strFilter;
						self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?keyWord=" + self.model.keyWord.trim() + "&" + strFilter;
						self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?keyWord=" + self.model.keyWord.trim() + "&" + strFilter;
					} else {
						self.strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount?" + strFilter;
						self.strUrlAttr="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/attribute?" + strFilter;
						self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?" + strFilter;
						self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?" + strFilter;
					}
				});
			}

//		get category name from id, used when navigate to from home page
			if ( self.model.categoryId > -1 ){
				var catUrl = "http://localhost:8080/hailey-chemist/rest/categories/" + self.model.categoryId;
				$.getJSON(catUrl, function(category){
					self.model.categoryName = category.name;
					self.gotCategoryName = true;
					if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes && self.gotCategoryName) {
						self.showViews(self);							
					}
				});
			} else {
				self.gotCategoryName = true;
			}
			
////			get all categories, always to choose to search, currently not in use
//			var strCatUrl = "http://localhost:8080/hailey-chemist/rest/categories";
//			$.getJSON( strCatUrl, function(categories){
//				self.model.categories = categories;
////				alert("No of category: " + self.model.categories.length);
//				self.gotCategories = true;
//				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes ) {
//					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model} );
//					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:self.model.productCountByCategories} );
//				}
//			});

			//			update count number of product found
			//        	var strUrl = self.model.dataSourceCount; //config.baseUrl + "rest/products/count";
			$.getJSON(self.paginationModel.dataSourceCount, function(result){
				$.each( result, function( key, val ) {
					if(key == "count"){
						self.model.resultCount = val;
//						if got all data then render
						self.gotCount = true;
						if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes  && self.gotCategoryName) {
							self.showViews(self);							
						}
					}
				});
			});

//			count product found by category
			$.getJSON(self.strUrl, function( productCountByCategories ){
//				show categories and number of its products
//				if got all data then render
				self.model.productCountByCategories = productCountByCategories;
				self.gotCategoryCount = true;
				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes  && self.gotCategoryName) {
					self.showViews(self);
				}				
			});

//			get attribute of product found
			$.getJSON(self.strUrlAttr, function( listAttributeValues ){
//				self.model.listAttributeValues = [
//  					{"id":1, "name":"Total Weight", "type":"int", "values":["500","1000"] },
//  					{"id":2, "name":"Content Weight", "type":"int", "values":["1000","500"] },
//  					];
				
				self.model.listAttributeValues = [];
				$.each( listAttributeValues, function( attributeId, values ){
//					convert to desired format for display template
//					get all attribute values except the first one as it is attribute name

					//	if this attributed was used to filtered data then display clear & clear all buttons
					//	else display check-box
					var filterValues = _.filter(self.model.filters, function(filter){ 
						return filter.attributeId == attributeId; 
					});
					
					if ( filterValues && (filterValues.length>0) ){
						var attributeValues = { "id":attributeId, "name":values[0], "values":[], "filtered":true };
						_.each(filterValues, function(filter){
							attributeValues.values.push(filter.attributeValue);
						});
						self.model.listAttributeValues.push(attributeValues);
					} else {
						var attributeValues = { "id":attributeId, "name":values[0], "values":[], "filtered":false };						
						var i;
						for ( i=1; i<values.length; i++) {
							attributeValues.values.push(values[i]);
						}
						self.model.listAttributeValues.push(attributeValues);
					}
					
					
				});
				
//				if got all data then render
				self.gotAttributes = true;
				if ( self.gotCount && self.gotCategoryCount && self.gotCategories && self.gotAttributes  && self.gotCategoryName) {
					self.showViews(self);
				}
			});
			
			return self;
		},

		showViews:function(self){
//			var self = this;
			utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model} );
			
//			pagination
			self.productPaginationView = new ProductPaginationView( {model:self.paginationModel, el:$("#divSearchProductPagination")} );	        	
			self.productPaginationView.render();
			
//			embedded cart
			self.embeddedCart = new EmbeddedCart( {el:$("#divEmbeddedCart")} );	        	
			self.embeddedCart.render();
			
//			products by category
			self.catModel={};
			var parents = _.uniq(
		            _.map(self.model.productCountByCategories, function(productCountByCategory){
		            	var parentIndex = (productCountByCategory.categoryPath.length-1);
		                return productCountByCategory.categoryPath[parentIndex];
		            }), false);
			
			self.catModel.categoryParents = parents;
			self.catModel.productCountByCategories = self.model.productCountByCategories;
			utilities.applyTemplate( $("#divCategoryEmbedded"), categoryEmbeddedTemplate, {model:self.catModel} );
		},
		
		showProductByCategory:function(event){
			var self = this;
			var catId = $(event.currentTarget).data("category-id");
			var catName = $(event.currentTarget).data("category-name");
			self.model.categoryId = catId;
			self.model.categoryName = catName;
			self.render();
		},

		clearCategory:function(){
			this.model.categoryId = -1;
			this.model.categoryName = "";
			this.render();
		},

		clearKeyWord:function(){
			this.model.keyWord = "";
			this.render();
		},
		
		updateFilter:function( event ){
			var self=this;
			var $target = $(event.currentTarget);
			var attrId = $target.data("attribute-id");
			var attrName = $target.data("attribute-name");
			var attrValue = $target.val();
			var checked = $target.is(':checked');
			
//			alert("id:" + attrId + ", name:" + attrName + ", value:" + attrValue);
			var checkFilter = { attributeId: attrId, attributeName: attrName, attributeValue:attrValue };
			var attribute = {attributeId: attrId, attributeName: attrName};
			
			if ( checked ){
//				var newFilter = "attr" + attrId + "=" + attrValue;
//				{attributeId:[attributeName,attributeValue]}
//				var newFilter = { attributeId: attrId, attributeName: attrName, attrValue:attrValue };
//				var index = self.model.filters.indexOf(newFilter);
				if ( !_.contains( self.model.filters, checkFilter )){
					self.model.filters.push(checkFilter);	
				}
			} else {
//				var oldFilter = "attr" + attrId + "=" + attrValue;
//				var oldFilter = { attributeId: attrId, attributeName: attrName, attrValue:attrValue };
//				var index = self.model.filters.indexOf(oldFilter);
//				self.model.filters.splice(index, 1);
				self.model.filters = _.reject(self.model.filters, function(filter){ 
					return (filter.attributeId == attrId) && (filter.attributeValue==attrValue); 
				});
			}
		},
		
		applyFilter:function(){
			var self=this;
			
//			var strFilter = "";
//			var i = 0;
//			$.each(self.model.filters, function(index, flt){
//				if (i==0){	// if first element then no &
//					strFilter = strFilter + "attr" + flt.attributeId + "=" + flt.attributeValue;
//				} else {
//					strFilter = strFilter + "&attr" + flt.attributeId + "=" + flt.attributeValue;	
//				}					
//				i++;
//			});
//			alert("Filter: " + strFilter);
			
			self.render();
		},
		
		resetModel:function(){
			this.model.filterAttributes=[];
			this.model.filters=[];
			this.model.categoryId = -1;
			this.model.categoryName = "";
			this.model.keyWord = "";			
		},
		
		clearFilter:function(event){
			var self = this;
			var $target = $(event.currentTarget);
			var attrId = $target.data("attribute-id");
			var attrValue = $target.data("attribute-value");
			
			var tmp = _.reject(self.model.filters, function(filter){ 
				return ( (filter.attributeId==attrId)  && (filter.attributeValue==attrValue) ); 
			});
			
			self.model.filters = tmp;
			 
//			var index = self.model.filters.indexOf(filter);
//			self.model.filters.splice(index, 1);
			self.render();
		},
		
		clearFilters:function(event){
			var self = this;
			var $target = $(event.currentTarget);
			var attrId = $target.data("attribute-id");
			
			self.model.filters = _.reject(self.model.filters, function(filter){ 
				return ( (filter.attributeId==attrId) ); 
			});
			
//			var index = self.model.filters.indexOf(filter);
//			self.model.filters.splice(index, 1);
			self.render();
		},
		
		clearAllFilters:function(){
			this.model.filterAttributes=[];
			this.model.filters=[];
//			this.model.categoryId = -1;
//			this.model.keyWord = "";
			this.render();
		},
		
		resetSearch:function(){
			this.model.filterAttributes=[];
			this.model.filters=[];
			this.model.categoryId = -1;
			this.model.categoryName = "";
			this.model.keyWord = "";
			this.render();
		},
		
		searchKeyword:function(){
//			this.resetModel();
			var self = this;
//			if want to make sure then uncomment following lines
//			self.model.filterAttributes=[];
//			self.model.filters=[];
//			self.model.categoryId = -1;
//			self.model.categoryName = "";
//			self.model.keyWord = "";			
			
			var value = self.$("#txtSearchKeyWord").val().trim();
			if(value) {
				self.model.keyWord = value;
//				need to change to use event listener to render
				self.render();
			}
		},

		updateOnEnter:function(event){
			if ( event.which == 13 ) {
				this.searchKeyword();
			}
		},
		
//		showCart:function(){
//			require("router").navigate('/cart', true);
//		}
	
	});

	return ProductSearchView;

});