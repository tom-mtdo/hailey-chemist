/**
 * A module for the router of the desktop application
 */
define("router", [
    'jquery',
    'jquerycookie',
    'underscore',
    'configuration',
    'utilities',
    'app/models/product',
    'app/collections/products',
    'app/views/desktop/home',
    'app/views/desktop/product-details',
    'app/views/desktop/product-grid',
    'app/views/desktop/cart',
    'text!../templates/desktop/main.html'
],function ($,
			jquerycookie,
            _,
            config,
            utilities,
            Product,
            Products,
            HomeView,
            ProductDetailView,
            ProductGridView,
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
            "products/:id":"productDetail",
            "products/:pageNo/:pageSize":"productPagination",
            "productsGrid":"productGrid",
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
        
        productDetail:function (id) {
//        	var product={"id":id,"description":"Black more oidless fish oil, 400 caples, 1000mg",
//	    		"name":"Fish oil","productNo":"PRD001","rrp":29.99,
//	    		"medias":[{"id":1,"itemSerial":null,"type":"image","url":"./resources/img/product/fish-oil.png"}],
//	    		"sales":[{"id":1,"endDate":1472029200000,"itemSerialNo":null,"price":19.99,"startDate":1469350800000}]};
//        	utilities.viewManager.showView(new ProductDetailView( {model:product, el:$("#content")} ));
        	
        	this.product = new Product({ id:id });
 			this.product.fetch();
	        this.product.on("change", 
	        		function () {
	        			utilities.viewManager.showView(new ProductDetailView( {model:this.product, el:$("#content")} ));
	        		}, 
	        		this);
        },
        
        cart:function () {
        	utilities.viewManager.showView( new CartView( {el:$("#content")} ));
        },
        
        productPagination:function(pageNo, pageSize){
//        	http://localhost:8080/hailey-chemist/rest/products?first=2&maxResults=2
        	var first = utilities.pagination.getPageFirstItem(pageNo, pageSize);
        	var maxResult = pageSize;
        	var count = 0;
        	var pagination = {"pageNo":pageNo, "pageSize":pageSize};
        	var productGridModel = {};
        	productGridModel.pagination = pagination;
        	
        	var gotCount = false;
        	var gotProducts = false;

//        	get count from DB, http://localhost:8080/hailey-chemist/rest/products/count
        	var strUrl = config.baseUrl + "rest/products/count";
//	======================================================================
//        	code in getJSON, fetch will be perform later when the data arrive
//        	======================================================================        	
        	$.getJSON(strUrl, function(result){
        		$.each( result, function( key, val ) {
        			if(key == "count"){
        				productGridModel.pagination.count = val;
        				gotCount = true;
                    	if(gotProducts && gotCount){
                    		utilities.viewManager.showView(new ProductGridView( {model:productGridModel, el:$("#content")} ));
                    	}        				
        			}
        		});
        	});
        	
//        	return page no & page size
        	
        	var strUrl = config.baseUrl + "rest/products?first=" + first + "&maxResults=" + maxResult;
            $.getJSON(strUrl, function(products){
            	productGridModel.products = products;
            	gotProducts = true;
            	if(gotProducts && gotCount){
            		utilities.viewManager.showView(new ProductGridView( {model:productGridModel, el:$("#content")} ));
            	}
            });
        },
        
        productGrid:function(){
        	this.productPagination(1,3);
        }
    });
    
	// Create a router instance
	var router = new Router();
	
	return router;

});