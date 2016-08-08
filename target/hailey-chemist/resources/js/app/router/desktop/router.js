/**
 * A module for the router of the desktop application
 */
define("router", [
    'jquery',
    'underscore',
    'configuration',
    'utilities',
    'app/views/desktop/home',
    'app/views/desktop/product-details',
    'text!../templates/desktop/main.html'
],function ($,
            _,
            config,
            utilities,
            HomeView,
            ProductDetailView,
            MainTemplate) {

    $(document).ready(new function() {
       utilities.applyTemplate($('body'), MainTemplate)
    })
    
    var Router = Backbone.Router.extend({
        initialize: function() {
            //Begin dispatching routes
            Backbone.history.start();
        },
        routes:{
            "":"home",
            "productDetails":"productDetails"
        },

        home:function () {
//	            var products=[{name:"Fish oil"},
//	                          {name:"Aloe"},
//	                          {name:"Multiple vitamins"},
//	                          {name:"Ensure Milk"}];
//
//                utilities.viewManager.showView(new HomeView({el:$("#content"), model:products}));
                utilities.viewManager.showView(new HomeView({el:$("#content")}));                
        },
        
        productDetails:function () {
        	utilities.viewManager.showView(new ProductDetailView( {el:$("#content")} ));
        }

    });
    
	// Create a router instance
	var router = new Router();
	
	return router;

});