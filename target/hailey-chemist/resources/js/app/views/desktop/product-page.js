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

    		// a line in cart
    		var quantity = $("#quantity"+ productId).val();   
    		
    		alert("Quantity: " + quantity);
    		var cartLine = {"productId":productId, "productName":productName, "quantity":quantity};
    		utilities.addToCart(cartLine);    		

        	var event_bus = utilities.getEventBus();
        	event_bus.trigger('cartAdded');
    		
    		alert(productName + ' added to cart!');
    	}

    });

    return ProductPageView;
});