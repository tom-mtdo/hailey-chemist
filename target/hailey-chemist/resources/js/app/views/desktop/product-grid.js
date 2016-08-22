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
    'text!../../../../templates/desktop/product-grid.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		productGridTemplate) {

    var ProductGridView = Backbone.View.extend({
    	
    	events:{
    	},
    	
    	initialize: function(){
    	},
    	
        render:function () {
        	var self = this;
        	var first = utilities.pagination.getPageFirstItem(self.model.pagination.pageNo, self.model.pagination.pageSize);
        	var maxResults = self.model.pagination.pageSize;
        	
        	// to check if get result from all services (get count and products)
        	var gotCount = false;
        	var gotProducts = false;

//			self.model.pagination.count = 5;
        	var strUrl = config.baseUrl + "rest/products/count";
//	======================================================================
//        	code in getJSON, fetch will be perform later when the data arrive
//  ======================================================================        	
        	$.getJSON(strUrl, function(result){
        		$.each( result, function( key, val ) {
        			if(key == "count"){
        				self.model.pagination.count = val;
        				gotCount = true;
//        				if got all data then display the page
        				if(gotProducts && gotCount){
        					utilities.applyTemplate($(self.el),productGridTemplate,{productGridModel:self.model});
                    	}        				
        			}
        		});
        	});
        	
//        	http://localhost:8080/hailey-chemist/rest/products?first=2&maxResults=2
        	var strUrl = config.baseUrl + "rest/products?first=" + first + "&maxResults=" + maxResults;
            $.getJSON(strUrl, function(products){
            	self.model.products = products;
            	gotProducts = true;
            	if(gotProducts && gotCount){
            		utilities.applyTemplate($(self.el),productGridTemplate,{productGridModel:self.model});
            	}
            });
        	
//            utilities.applyTemplate($(this.el), productGridTemplate,{productGridModel:this.model});
// ********************************************************************************************************            
//            this.model is a parameter, router, or anything else init this view, will need put in
//            a view have 2 default parameter: el & model
//            This is one way router pass data to a view
//            utilities.applyTemplate($('#featuredProducts'), productGrid, {products:this.model})
//            see router to see more details
// ********************************************************************************************************            
//            $.getJSON(config.baseUrl + "rest/products", function(products){
//            	utilities.applyTemplate($('#featuredProducts'), productGrid, {products:products})
//            })            
            return self;
        }
        
    });

    return ProductGridView;
});