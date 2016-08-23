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
    		"click #previous":"goPrevious",
    		"click #next":"goNext"
    	},
    	
    	initialize: function(){
    	},
    	    	
        render:function () {
        	var self = this;
        	var first = utilities.pagination.getPageFirstItem(self.model.pagination.pageNo, self.model.pagination.pageSize);
        	
        	// to check if get result from all services (get count and products)
        	var gotCount = false;
        	var gotProducts = false;
        	
        	self.productGridModel = {};
        	self.productGridModel.first = first;
        	self.productGridModel.hasPreviousPage = utilities.pagination.isHasPreviousPage(self.model.pagination.pageNo);

//  ==========================================================================
//        	code in getJSON, fetch will be perform later when the data arrive
//  ==========================================================================        	
        	var strUrl = config.baseUrl + "rest/products/count";
        	$.getJSON(strUrl, function(result){
        		$.each( result, function( key, val ) {
        			if(key == "count"){
        				self.model.pagination.count = val; 
        				self.productGridModel.last = utilities.pagination.getPageLastItem(self.model.pagination.pageNo, self.model.pagination.pageSize, self.model.pagination.count);        				
        				self.productGridModel.hasNextPage = utilities.pagination.isHasNextPage(self.model.pagination.pageNo, self.model.pagination.pageSize, self.model.pagination.count);
        				self.productGridModel.count = val;
        				gotCount = true;
//        				if got all data then display the page
        				if(gotProducts && gotCount){
        					utilities.applyTemplate($(self.el),productGridTemplate,{productGridModel:self.productGridModel});
        		        	if ( !self.productGridModel.hasPreviousPage ) {
        						$("#previous").attr('disabled', true);
        					} else {
        						$("#previous").removeAttr('disabled');
        					}

        					if ( !self.productGridModel.hasNextPage ) {
        						$("#next").attr('disabled', true);
        					} else {
        						$("#next").removeAttr('disabled');
        					}        	        					        					
//        					self.updateView();
                    	}        				
        			}
        		});
        	});
        	
//        	http://localhost:8080/hailey-chemist/rest/products?first=2&maxResults=2
        	var strUrl = config.baseUrl + "rest/products?first=" + first + "&maxResults=" + self.model.pagination.pageSize;
            $.getJSON(strUrl, function(products){
            	self.model.products = products;
            	self.productGridModel.products = products;            	
            	gotProducts = true;
            	if(gotProducts && gotCount){
        			utilities.applyTemplate($(self.el),productGridTemplate,{productGridModel:self.productGridModel});
                	if ( !self.productGridModel.hasPreviousPage ) {
        				$("#previous").attr('disabled', true);
        			} else {
        				$("#previous").removeAttr('disabled');
        			}

        			if ( !self.productGridModel.hasNextPage ) {
        				$("#next").attr('disabled', true);
        			} else {
        				$("#next").removeAttr('disabled');
        			}        	
//            		self.updateView();
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
        },
        
        updateView:function(){
			utilities.applyTemplate($(self.el),productGridTemplate,{productGridModel:self.productGridModel});
        	if ( !self.productGridModel.hasPreviousPage ) {
				$("#previous").attr('disabled', true);
			} else {
				$("#previous").removeAttr('disabled');
			}

			if ( !self.productGridModel.hasNextPage ) {
				$("#next").attr('disabled', true);
			} else {
				$("#next").removeAttr('disabled');
			}        	
        },
        
        goPrevious:function(){
        	var self = this;

        	self.model.pagination.pageNo = parseInt(self.model.pagination.pageNo) - 1;
        	self.render();
        },

        goNext:function(){
        	var self = this;

        	self.model.pagination.pageNo = parseInt(self.model.pagination.pageNo) + 1;
        	self.render();
        },
        
        
    });

    return ProductGridView;
});