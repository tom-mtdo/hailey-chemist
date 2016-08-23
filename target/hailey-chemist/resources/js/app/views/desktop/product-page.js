/**
 * brow product - grid view
 * input model: page, hasNext, count, products
 */
define([
    'utilities',
    'jquery',
    'jquerycookie',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/product-page.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		productPageTemplate) {

    var ProductPageView = Backbone.View.extend({
    	events:{
    	},
    	
    	initialize: function(){
    	},
    	    	
        render:function () {
        	var self = this;
			utilities.applyTemplate( $(self.el), productPageTemplate,
					{products:self.model} );
        }
    });

    return ProductPageView;
});