/**
 * A module for the router of the desktop application
 */
define("router", [
    'jquery',
    'underscore',
    'configuration',
    'utilities',
    'app/views/desktop/home',
    'text!../templates/desktop/main.html'
],function ($,
            _,
            config,
            utilities,
            HomeView,
            MainTemplate) {

    $(document).ready(new function() {
       utilities.applyTemplate($('body'), MainTemplate)
    })
    
//    var Router = Backbone.Router.extend({
//        initialize: function() {
//            //Begin dispatching routes
//            Backbone.history.start();
//        },
//        routes:{
//            "":"home",
//        },
//        
//        home:function () {
//                utilities.viewManager.showView(new HomeView({el:$("#content")}));
//        }
//
//    });
//    
//	// Create a router instance
//	var router = new Router();
//	
//	return router;

});