/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/cart.html',
    'text!../../../../templates/desktop/purchase-confirmation.html'
], function (utilities,
		jquery,
		bootstrap,
		config,
		cartTemplate,
		purchaseConfirmationTemplate) {

    var CartView = Backbone.View.extend({
    	events: {
    		"click button[name='checkout']" : "save"
    	},
    	
        render:function () {
            var strCart = utilities.getCookie("cart");
            if (strCart && (strCart.trim().length>0) ){
                var aCart = JSON.parse(strCart);            	
            } else{
            	var aCart={};
            }
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
//			===============================================================
//			store current context so can change page content using self.el
    		var self = this;
//			===============================================================
    		
//          get cart from cookie
    		var strCart = utilities.getCookie("cart");
            var aCart = JSON.parse(strCart);
    		
//            put each line into request
    		var purchaseRequest = {orderDetailRequests:[]};
    		_.each(aCart, function(line) {
    			purchaseRequest.orderDetailRequests.push(
    					{productId: line.productId, quantity: line.quantity});
    		});    		

//    		purchaseRequest.orderDetailRequests.push({productId: 1, quantity: 3});
//			User email
    		purchaseRequest.email = $("#email").val();
    		
            $.ajax({url: (config.baseUrl + "rest/purchases"),
                data:JSON.stringify(purchaseRequest),
                type:"POST",
                dataType:"json",
                contentType:"application/json",
                success:function (purchase) {
//                	prompt succeed
//                	clear cart in cookie       
                	utilities.clearCookie("cart");
                	utilities.applyTemplate($(self.el), purchaseConfirmationTemplate, {purchase:purchase});
                	alert("checkout success!");
                }}).error(function (error) {
                	alert("checkout error");
                })
    	}
    });

    return CartView;
});