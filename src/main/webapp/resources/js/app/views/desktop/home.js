/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/home.html',
    'text!../../../../templates/desktop/product-grid.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		homeTemplate,
		productGrid) {

    var HomeView = Backbone.View.extend({
        render:function () {
        	
            utilities.applyTemplate($(this.el),homeTemplate,{});
            $('.carousel').carousel();
// ********************************************************************************************************            
//            this.model is a parameter, router, or anything else init this view, will need put in
//            a view have 2 default parameter: el & model
//            This is one way router pass data to a view
//            utilities.applyTemplate($('#featuredProducts'), productGrid, {products:this.model})
//            see router to see more details
// ********************************************************************************************************            
            
            $.getJSON(config.baseUrl + "rest/products", function(products){
            	utilities.applyTemplate($('#featuredProducts'), productGrid, {products:products})
            })
            
            return this;
        }
    });

    return HomeView;
});