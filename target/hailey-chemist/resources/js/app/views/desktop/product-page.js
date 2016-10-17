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

//	input model: products
    var ProductPageView = Backbone.View.extend({
    	events:{
    		"click button[name='addToCart']":"addToCart"
    	},
    	
    	initialize: function(){
    	},
    	    	
        render:function () {
        	var self = this;
			utilities.applyTemplate( $(self.el), productPageTemplate,
					{products:self.model} );
        },
    	addToCart:function(event){
			var self = this;
			var $target = $(event.currentTarget);
			var productId = $target.data("product-id");
			var productName = $target.data("product-name");
			var price = $target.data("product-price");

    		// a line in cart
    		var quantity = $("#quantity"+ productId).val();   
    		
    		var cartLine = {"productId":productId, "productName":productName, "quantity":quantity, "price":price};
    		utilities.addToCart(cartLine);    		

        	var event_bus = utilities.getEventBus();
        	event_bus.trigger('cartAdded');
        	
        	$("#quantity"+ productId).val('');
        	
    		alert(productName + ' added to cart!');
    	}

    });

    return ProductPageView;
});