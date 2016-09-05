/**
 * input: productSearchModel{categoryId, pageNo, pageSize, and query para e.g. keyword}
 */

define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-page',
    'app/views/desktop/product-pagination',
    'text!../../../../templates/desktop/product-search.html',
    'text!../../../../templates/desktop/product-count-by-category.html',
    
], function (utilities,
		jquery,
		bootstrap,
		config,
		ProductPageView,
		ProductPaginationView,
		productSearchTemplate,
		productCountByCategoryTemplate) {
	
	var ProductSearchView = Backbone.View.extend({
		render:function(){
			var self = this;
//			get data
//			var strUrl="http://localhost:8080/hailey-chemist/rest/product-search";

			//			NEED A SERVICE COUNT PRODUCT BY CATEGORY
			var strUrl="http://localhost:8080/hailey-chemist/rest/product-search/-1";
			$.getJSON(strUrl, function( productSearchResult ){
//				show categories and number of its products
				utilities.applyTemplate( $(self.el), productSearchTemplate, {} );
				self.productCountByCategoryView = new ProductCountByCategoryView({model:productSearchResult.counts, el:$("#divProductCountByCategory") });
				self.productCountByCategoryView.render();
// working on this +++++++++++++++++++
//				Show list of product as search result
//				++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				//				NEED A SERVICE RETURN ONLY A LIST OF PRODUCTS
//				++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	        	var paginationModel={};
	        	paginationModel.pageNo = 0;
	        	paginationModel.pageSize = 2;
//	        	get all product for now
	        	paginationModel.dataSource=config.baseUrl + "http://localhost:8080/hailey-chemist/rest/product-search/-1";
	        	paginationModel.dataSourceCount=config.baseUrl + "http://localhost:8080/hailey-chemist/rest/product-search/-1/count";
//	        	paginationModel.dataSource=config.baseUrl + "rest/products";
//	        	paginationModel.dataSourceCount=config.baseUrl + "rest/products/count";
	        	
	        	self.productPaginationView = new ProductPaginationView( {model:paginationModel, el:$("#divSearchProductPagination")} );	        	
//	        	self.productPageView = new ProductPageView( {model:productSearchResult.products, el:$("#divSearchProductPagination")} );
	        	
	        	self.productPageView.render();

			});
			
			return self;
		}
	});

	var ProductCountByCategoryView = Backbone.View.extend({
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), productCountByCategoryTemplate, {productCountByCategories: self.model} );
			return self;
		}
	});

	
	return ProductSearchView;
	
});