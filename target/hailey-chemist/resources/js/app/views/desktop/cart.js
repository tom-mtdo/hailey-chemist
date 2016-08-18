/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/cart.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		cartTemplate) {

    var CartView = Backbone.View.extend({
    	events: {
    		"click button[name='checkout']" : "save"
    	},
    	
        render:function () {
            var strCart = utilities.getCookie("cart");
            var aCart = JSON.parse(strCart);
        	utilities.applyTemplate($(this.el), cartTemplate,{cart:aCart});

//          var product={"id":1,"description":"Black more oidless fish oil, 400 caples, 1000mg",
//    		"name":"Fish oil","productNo":"PRD001","rrp":29.99,
//    		"medias":[{"id":1,"itemSerial":null,"type":"image","url":"./resources/img/product/fish-oil.png"}],
//    		"sales":[{"id":1,"endDate":1472029200000,"itemSerialNo":null,"price":19.99,"startDate":1469350800000}]};

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
            
            return this;
        },
    	
    	save: function(){
    		var purchaseRequest = {orderDetailRequests:[]};
    		purchaseRequest.orderDetailRequests.push({productId:1, quantity: 3});
    		purchaseRequest.orderDetailRequests.push({productId:3, quantity: 2});
    		purchaseRequest.email = "john.smith@gmail.com";
    		
            $.ajax({url: (config.baseUrl + "rest/purchases"),
                data:JSON.stringify(purchaseRequest),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success:function (purchase) {
                }}).error(function (error) {
                })
    	}
    });

    return CartView;
});