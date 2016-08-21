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
            utilities.applyTemplate($(this.el),homeTemplate,{});
            $('.carousel').carousel();
// ********************************************************************************************************            
//            this.model is a parameter, router, or anything else init this view, will need put in
//            a view have 2 default parameter: el & model
//            This is one way router pass data to a view
//            utilities.applyTemplate($('#featuredProducts'), productGrid, {products:this.model})
//            see router to see more details
// ********************************************************************************************************            

            // pagination
            // http://localhost:8080/hailey-chemist/rest/products?first=2&maxResults=2
            // should convert to             
            // http://localhost:8080/hailey-chemist/rest/products/:first/:maxResults
        	var pagination = {"pageNo":0, "pageSize":4, "count":5};
        	var productGridModel = {};
        	productGridModel.pagination = pagination;
//        	var first = 1;
//        	var maxResult = 3;
        	var first = utilities.pagination.getPageFirstItem(pagination.pageNo, pagination.pageSize);
        	var maxResult = pagination.pageSize;
            
        	var strUrl = config.baseUrl + "rest/products?first=" + first + "&maxResults=" + maxResult;
            $.getJSON(strUrl, function(products){
            	productGridModel.products = products;
            	self.productGridView = new ProductGridView({ el:$('#featuredProducts'), model:productGridModel });
            	self.productGridView.render();
//            	utilities.applyTemplate($('#featuredProducts'), productGridTemplate, {products:products})
            })
            
            return this;
        }
    });

    return HomeView;
});