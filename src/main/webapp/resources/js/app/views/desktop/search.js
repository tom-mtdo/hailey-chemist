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

//	input model has: pageNo, pageSize
    var Search = Backbone.View.extend({
        render:function () {
        	var self = this;
            utilities.applyTemplate($(self.el),searchTemplate,{searchModel:self.model});
        }
    });
    
    return Search;
})