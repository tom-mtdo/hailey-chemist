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
    	events: {
//    		"click button[name='checkout']" : "save"
    	},
    	
        render:function () {
        	utilities.applyTemplate($(this.el), embeddedCartTemplate,{});
        }
    });
    
    return EmbeddedCartView 
})