/**
 * 
 */

define([
        'utilities',
        'jquery',
        'bootstrap',
        'configuration',
        'text!../../../../templates/desktop/category-admin.html',

        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		categoryAdminTemplate) {

	var CategoryAdminView = Backbone.View.extend({
    	initialize: function(){
    	},
    	    			
    	events:{
    		"keypress #txtSearchKeyWord":"updateOnEnter",
    	},
		
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), categoryAdminTemplate, {} );
		}
	});
	 
	return CategoryAdminView;  
})