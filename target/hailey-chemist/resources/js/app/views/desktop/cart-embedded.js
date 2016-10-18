/**
 * 
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/cart-embedded.html',
], function (utilities,
		jquery,
		bootstrap,
		config,
		embeddedCartTemplate
		) {

    var EmbeddedCartView = Backbone.View.extend({
		initialize: function(){
			var eventBus=utilities.getEventBus();
			this.listenTo(eventBus, 'cartAdded', this.response_event);
		},
		
    	events: {
//    		"click button[name='checkout']" : "save"
    	},
    	
        render:function () {
            var strCart = utilities.getCookie("cart");
            if (strCart && (strCart.trim().length>0) ){
                var aCart = JSON.parse(strCart);            	
            } else{
            	var aCart={};
            }
            
        	utilities.applyTemplate($(this.el), embeddedCartTemplate,{cart:aCart});
        	
        	if ( aCart && (aCart.length > 0)) {
        		$("#btnCheckout").removeAttr('disabled');
			} else {
				$("#btnCheckout").attr('disabled', true);
			}
        },
        
        response_event:function(){
        	this.render();
//        	alert("Cart-embedded caught event listenMe!");
        }
    });
    
    return EmbeddedCartView 
})