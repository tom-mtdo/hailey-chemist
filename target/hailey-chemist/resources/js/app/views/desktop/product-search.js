/**
 * 
 */

define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/product-search.html',
    'text!../../../../templates/desktop/product-count-by-category.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		productSearchTemplate,
		productCountByCategoryTemplate) {
	
	var ProductSearchView = Backbone.View.extend({
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), productSearchTemplate, {} );
			self.productCountByCategoryView = new ProductCountByCategoryView({model:{}, el:$("#divProductCountByCategory") });
			self.productCountByCategoryView.render();
			return self;
		}
	});

	var ProductCountByCategoryView = Backbone.View.extend({
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), productCountByCategoryTemplate, {} );
			return self;
		}
	});

	
	return ProductSearchView;
	
});