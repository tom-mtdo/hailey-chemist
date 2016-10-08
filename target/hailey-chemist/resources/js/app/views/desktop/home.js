/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'app/views/desktop/product-pagination',
    'app/views/desktop/cart-embedded',
    'text!../../../../templates/desktop/home.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		ProductPaginationView,
		EmbeddedCartView,
		homeTemplate		) {

//	<script type="text/javascript">
// 	//Create an object 'myVal' and 'myVal1' and extend them using Backbone.Events method
//    var myVal = _.extend({name:'Hello..'}, Backbone.Events);
//    var myVal1 = _.extend({name:'Welcome to TutorialsPoint!!!'}, Backbone.Events);
//
//   //create the 'listenMe' callback function and invoke when one object listens to particular event on another object
//    var listenMe = function(){
//       document.write("The value is: ");
//       document.write(this.name);
//    };
//  //The object 'myVal1' listens once for the 'listenMe' event triggered on object 'myVal'
//   myVal1.listenTo(myVal, 'listenMe', listenMe);
//
//   //The 'myVal' has no listenMe event and displays the value of 'myVal1'
//   myVal.trigger('listenMe');
// </script>
// 
//	var event_bus = _({}).extend(Backbone.Events);
	
	
	
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
            // embedded cart
            var embeddedCart = new EmbeddedCartView({ el:$("#divEmbeddedCart") });
            embeddedCart.render();
            
            // pagination            
            // http://localhost:8080/hailey-chemist/rest/products/:first/:maxResults            
        	var paginationModel = {"pageNo":0, "pageSize":4};
        	paginationModel.dataSource=config.baseUrl + "rest/products/";
        	paginationModel.dataSourceCount=config.baseUrl + "rest/products/count";
        	var productPaginationView = new ProductPaginationView( {model:paginationModel, el:$("#featuredProducts")} );
        	productPaginationView.render();
        	
            return self;
        },
        
        searchProducts: function () {
//        	var event_bus = utilities.getEventBus();
//        	event_bus.trigger('listenMe');
        	
        	var value = this.$("#quickSearchInputHome").val().trim();
//        	working on this , navigate to search page
//        	require("router").navigate('/book/' + $("#venueSelector option:selected").val() + '/' + $("#performanceTimes").val(), true)
        	require("router").navigate('/product-search/-1/' + value, true);
        }
    });

    return HomeView;
});