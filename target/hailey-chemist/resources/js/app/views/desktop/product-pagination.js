/**
 * browse product - grid view
 * 
 */
define([
    'utilities',
    'jquery',
    'jquerycookie',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-page',
    'text!../../../../templates/desktop/product-pagination.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		ProductPageView,
		productPaginationTemplate) {

//	input model has: pageNo, pageSize
    var ProductPagination = Backbone.View.extend({
    	
    	events:{
    		"click #previous":"goPrevious",
    		"click #next":"goNext",
    		"change #inpPageSize":"updatePageSize"
    	},
    	
    	initialize: function(){
    	},
    	    	
        render:function () {
        	var self = this;
//  ==========================================================================
//        	update pagination navigation
//        	count the first product of a page
        	self.model.first = utilities.pagination.getPageFirstItem(
        		self.model.pageNo, self.model.pageSize);
//        	check if there is previous page
        	self.model.hasPreviousPage = utilities.pagination.
        		isHasPreviousPage(self.model.pageNo);        	
//        	count total products
        	var strUrl = self.model.dataSourceCount; //config.baseUrl + "rest/products/count";
        	$.getJSON(strUrl, function(result){
        		$.each( result, function( key, val ) {
        			if(key == "count"){
        				self.model.count = val;
//        				count the last product of a page 
        				self.model.last = utilities.pagination.getPageLastItem(
        					self.model.pageNo, self.model.pageSize, self.model.count);
//        				check if has next page
        				self.model.hasNextPage = utilities.pagination.isHasNextPage(
        					self.model.pageNo, self.model.pageSize, self.model.count);
        				
//        				render
        	        	utilities.applyTemplate( $(self.el), productPaginationTemplate, 
        	        		{pagination:self.model} );
//    		        	check enable navigate buttons
        	        	if ( !self.model.hasPreviousPage ) {
    						$("#previous").attr('disabled', true);
    					} else {
    						$("#previous").removeAttr('disabled');
    					}

    					if ( !self.model.hasNextPage ) {
    						$("#next").attr('disabled', true);
    					} else {
    						$("#next").removeAttr('disabled');
    					}        	        					        					        				
        			}
        		});
        	});
//  ==========================================================================
//        	update product page
//        	this path can use mode/collection/fetch(with option) instead of following method
//        	http://localhost:8080/hailey-chemist/rest/products?first=2&maxResults=2
//        	var strUrl = config.baseUrl + "rest/products?first=" + self.model.first +
        	var strUrl = self.model.dataSource + "?first=" + self.model.first + 
        		"&maxResults=" + self.model.pageSize;
            $.getJSON(strUrl, function(products){
            	var productPageView = new ProductPageView( {el:$("#divProductPage"), model:products} );            			
            	productPageView.render();
            });
        	
//  ==========================================================================
//        	code in getJSON, fetch will be perform later when the data arrive
//  ==========================================================================        	
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
        
        goPrevious:function(){
        	var self = this;
        	self.model.pageNo = parseInt(self.model.pageNo) - 1;
        	self.render();
        },

        goNext:function(){
        	var self = this;
        	self.model.pageNo = parseInt(self.model.pageNo) + 1;
        	self.render();
        },
        
        updatePageSize:function(){
        	var i =$("#inpPageSize").val();
        	if ( i < 1){
        		this.model.pageSize = 1
        	} else if ( i > 300 ) {
        		this.model.pageSize = 300;
        	} else {
        		this.model.pageSize = i;
        	}
        	this.render();
        }
        
    });

    return ProductPagination;
});