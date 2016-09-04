/**
 * input: productSearchModel{categoryId, pageNo, pageSize}
 */

define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-page',
    'text!../../../../templates/desktop/product-search.html',
    'text!../../../../templates/desktop/product-count-by-category.html',
    
], function (utilities,
		jquery,
		bootstrap,
		config,
		ProductPageView,
		productSearchTemplate,
		productCountByCategoryTemplate) {
	
	var ProductSearchView = Backbone.View.extend({
		render:function(){
			var self = this;
//			get data
			var strUrl="http://localhost:8080/hailey-chemist/rest/product-search";
			$.getJSON(strUrl, function( productSearchResult ){
				
				utilities.applyTemplate( $(self.el), productSearchTemplate, {} );
				self.productCountByCategoryView = new ProductCountByCategoryView({model:productSearchResult.counts, el:$("#divProductCountByCategory") });
				self.productCountByCategoryView.render();
// working on this +++++++++++++++++++
				
	        	self.productPageView = new ProductPageView( {model:productSearchResult.products, el:$("#divSearchProductPagination")} );
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