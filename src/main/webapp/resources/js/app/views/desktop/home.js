/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-pagination',
    'text!../../../../templates/desktop/home.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		ProductPaginationView,
		homeTemplate		) {

    var HomeView = Backbone.View.extend({
    	events: {
    		"click #quickSearchLink":"searchProducts"
    	},
    	
        render:function () {
        	var self = this;
            utilities.applyTemplate($(self.el),homeTemplate,{});
            $('.carousel').carousel();
// ********************************************************************************************************            
//            this.model is a parameter, router, or anything else init this view, will need put in
//            a view have 2 default parameter: el & model
//            This is one way router pass data to a view
//            utilities.applyTemplate($('#featuredProducts'), productGrid, {products:this.model})
//            see router to see more details
// ********************************************************************************************************            
 
            // http://localhost:8080/hailey-chemist/rest/products/:first/:maxResults            
        	var paginationModel = {"pageNo":0, "pageSize":4};
        	paginationModel.dataSource=config.baseUrl + "rest/products/";
        	paginationModel.dataSourceCount=config.baseUrl + "rest/products/count";
        	var productPaginationView = new ProductPaginationView( {model:paginationModel, el:$("#featuredProducts")} );
        	productPaginationView.render();
        	
            return self;
        },
        
        searchProducts: function () {        	
        }
    });

    return HomeView;
});