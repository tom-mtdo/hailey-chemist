/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-grid',
    'text!../../../../templates/desktop/home.html',
    'text!../../../../templates/desktop/product-grid.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		ProductGridView,
		homeTemplate,
		productGridTemplate) {

    var HomeView = Backbone.View.extend({
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
        	var pagination = {"pageNo":0, "pageSize":4};
        	var productGridModel = {};
        	var products = {};
        	productGridModel.pagination = pagination;
        	productGridModel.products = products;
        	var productGridView = new ProductGridView( {model:productGridModel, el:$("#featuredProducts")} );
        	productGridView.render();

            return self;
        }
    });

    return HomeView;
});