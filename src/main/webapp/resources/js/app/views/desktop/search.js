define([
    'utilities',
    'jquery',
    'jquerycookie',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/search.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		searchTemplate) {

//	input model has: keyWord
//	Render add products to its model
    var Search = Backbone.View.extend({
        render:function () {
        	var self = this;
        	var strUrl = "rest/products?keyWord=" + self.model.keyWord;
        	$.getJSON(strUrl, function( products ) {
        		self.model.products = products;
                utilities.applyTemplate($(self.el),searchTemplate,{searchModel:self.model});
        	})
        }
    });
    
    return Search;
})