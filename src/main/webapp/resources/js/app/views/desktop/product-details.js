/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'jquerycookie',
    'bootstrap',
    'configuration',
    'text!../../../../templates/desktop/product-details.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		productDetailTemplate) {

    var ProductDetailView = Backbone.View.extend({
    	events:{
    		"click button[name='addToCart']":"addToCart"
    	},
    	
        render:function () {
//            var product={"id":1,"description":"Black more oidless fish oil, 400 caples, 1000mg",
//            		"name":"Fish oil","productNo":"PRD001","rrp":29.99,
//            		"medias":[{"id":1,"itemSerial":null,"type":"image","url":"./resources/img/product/fish-oil.png"}],
//            		"sales":[{"id":1,"endDate":1472029200000,"itemSerialNo":null,"price":19.99,"startDate":1469350800000}]};
            utilities.applyTemplate($(this.el), productDetailTemplate,{product:this.model});
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
    	
    	addToCart:function(){
//    		cart is an array of id, name, quantity    		

//    		var quantity = $("input[name='quantity']").val();
//    		var cart = [[this.model.get("id"), this.model.get("name"), quantity],
//    		            [3, "Multiple Vitamin", 1]];
//    		var strCart = JSON.stringify(cart);
//    		utilities.setCookie( "cart", strCart, 10);

    		// a line in cart
    		var quantity = $("input[name='quantity']").val();    		
    		var cartLine = {"productId":this.model.get("id"), "productName":this.model.get("name"), "quantity":quantity};
    		utilities.addToCart(cartLine);
    		
//    		alert('Added to cart!');
//    		$.cookie.raw = true;
//    		$.cookie( "pId", this.model.get("id"), {expires:10, path: "/"} );
//    		$.cookie( "pquantity", 1, {expires:10, path: "/"} );
    	}
    
    });

    return ProductDetailView;
});