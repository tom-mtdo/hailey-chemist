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
			this.listenTo(eventBus, 'listenMe', this.catch_event);
		},
		
    	events: {
//    		"click button[name='checkout']" : "save"
    	},
    	
        render:function () {
        	utilities.applyTemplate($(this.el), embeddedCartTemplate,{});
        },
        
        catch_event:function(){
        	alert("Cart-embedded caught event listenMe!");
        }
    });
    
    return EmbeddedCartView 
})