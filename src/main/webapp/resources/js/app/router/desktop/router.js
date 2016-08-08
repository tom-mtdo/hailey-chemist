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
    'app/views/desktop/cart',
    'text!../templates/desktop/main.html'
],function ($,
            _,
            config,
            utilities,
            HomeView,
            ProductDetailView,
            CartView,
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
            "productDetails":"productDetails",
            "cart":"cart"
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
        },
        
        cart:function () {
        	utilities.viewManager.showView( new CartView( {el:$("#content")} ));
        }
    });
    
	// Create a router instance
	var router = new Router();
	
	return router;

});