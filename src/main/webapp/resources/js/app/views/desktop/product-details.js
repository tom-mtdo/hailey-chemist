/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'jquerycookie',
    'bootstrap',
    'configuration',
    'app/views/desktop/cart-embedded',
    'text!../../../../templates/desktop/product-details.html'
], function (utilities,
		jquery,
		jquerycookie,
		bootstrap,
		config,
		EmbeddedCartView,
		productDetailTemplate) {

    var ProductDetailView = Backbone.View.extend({
    	events:{
    		"click button[name='addToCart']":"addToCart",
			"change #txtQtyInDetail":"validate"
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
            var qty = $("#txtQtyInDetail").val();
            
            if ( qty && (Number(qty) > 0) ){
            	$("#btnAddToCart").removeAttr('disabled');	
            } else {
            	$("#btnAddToCart").attr('disabled', true);
            }
            
            // embedded cart
            var embeddedCart = new EmbeddedCartView({ el:$("#divEmbeddedCart") });
            embeddedCart.render();
            
            return this;
        },
    	
    	addToCart:function(){
			var self = this;

			// a line in cart
//    		var quantity = $("input[name='quantity']").val();
    		var quantity = $("#txtQtyInDetail").val();
    		var cartLine = {"productId":self.model.get("id"), "productName":self.model.get("name"), "quantity":quantity, "price":self.model.get("sale").price };
    		utilities.addToCart(cartLine);    		
    		alert('Added to cart!');

        	var event_bus = utilities.getEventBus();
        	event_bus.trigger('cartAdded');
        	
        	$("#txtQtyInDetail").val('');
    	},
    	
    	validate:function(event){
    		var self=this;
    		var $target=$(event.currentTarget);
    		var quantity = $target.val();

    		if ( quantity < 1){
    			quantity = 1;
        	} else if ( quantity > 999999 ) {
        		quantity = 999999;
        	} 
    		
        	if ( quantity > 0) {
        		$("#btnAddToCart").removeAttr('disabled');
			} else {
				$("#btnAddToCart").attr('disabled', true);
			}
        	
        	$target.val(quantity);    		
//    		self.render();

    	}
    
    });

    return ProductDetailView;
});