/**
 * input: productSearchModel{categoryId,  and query paras: keyword, categoryId, resultCount}
 * maybe: pageNo, pageSize,
 */

define([
        'utilities',
        'jquery',
        'bootstrap',
        'configuration',
        'app/views/desktop/product-page',
        'app/views/desktop/product-pagination',
        'text!../../../../templates/desktop/product-search.html',
        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		ProductPageView,
        		ProductPaginationView,
        		productSearchTemplate) {

	var ProductSearchView = Backbone.View.extend({
		initialize: function(){
			this.model.resultCount = 0;
//			this.model = {};
//			this.model.keyWord = "";
//			this.model.categoryId = "-1";
////			this.model.on('change', this.render, this);
		},

		events:{
			"keypress #txtSearchKeyWord":"updateOnEnter",
			"click a":"showProductByCategory"
		},

		render:function(){
			var self = this;
			var strUrl="";
			self.paginationModel={};
			self.paginationModel.pageNo = 0;
			self.paginationModel.pageSize = 3;
			self.gotCount = false;
			self.gotCategoryCount = false;


			if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
				strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount" + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?keyWord=" + self.model.keyWord.trim();
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?keyWord=" + self.model.keyWord.trim();
			} else {
				strUrl="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/pathCount";
				self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId;
				self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count";
			}

			//			update count
//        	var strUrl = self.model.dataSourceCount; //config.baseUrl + "rest/products/count";
        	$.getJSON(self.paginationModel.dataSourceCount, function(result){
        		$.each( result, function( key, val ) {
        			if(key == "count"){
        				self.model.resultCount = val;
        				self.gotCount = true;
//        				if got all data then render        				
        				if (self.gotCount && self.gotCategoryCount) {
        					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories} );
        				}
        			}
        		});
        	});

			$.getJSON(strUrl, function( productCountByCategories ){
//				show categories and number of its products
				self.gotCategoryCount = true;
//				if got all data then render
				if (self.gotCount && self.gotCategoryCount) {
					utilities.applyTemplate( $(self.el), productSearchTemplate, {model:self.model, productCountByCategories:productCountByCategories} );
				}
////				category with path and count product
				//				Show list of product as search result
//				if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
//					self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "?keyWord=" + self.model.keyWord.trim();
//					self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count?keyWord=" + self.model.keyWord.trim();
//				} else {
//					self.paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId;
//					self.paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/" + self.model.categoryId + "/count";
//				}
				self.productPaginationView = new ProductPaginationView( {model:self.paginationModel, el:$("#divSearchProductPagination")} );	        	
				self.productPaginationView.render();
			});

			return self;
		},

//		working on this +++++++++++++++
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
 		}

	});

//	var ProductCountByCategoryView = Backbone.View.extend({
//		events:{
//			"click a":"showProductByCategory"
//		},
//		
//		render:function(){
//			var self = this;
//			utilities.applyTemplate( $(self.el), productCountByCategoryTemplate, {productCountByCategories: self.model} );
//			return self;
//		},
//		
//		showProductByCategory:function(event){
//			var self = this;
//			var catId = $(event.currentTarget).data("category-id");
// 		}
//	});


	return ProductSearchView;

});