/**
 * 
 */

define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/product-search.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		productSearchTemplate) {
	
	var ProductSearchView = Backbone.View.extend({
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), productSearchTemplate, {} );
			return self;
		}
	});
	
	return ProductSearchView;
	
});