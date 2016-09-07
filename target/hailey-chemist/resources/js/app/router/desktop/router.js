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
    'app/views/desktop/product-pagination',
    'app/views/desktop/cart',
    'app/views/desktop/search',
    'app/views/desktop/product-search',
    'app/views/desktop/category-admin',
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
            ProductPaginationView,
            CartView,
            SearchView,
            ProductSearchView,
            CategoryAdminView,
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
            "products":"products",
            "products/:id":"productDetail",
            "products/:pageNo/:pageSize":"productPagination",
            "product-search":"productSearch",
            "product-search/:categoryId/:keyWord":"productSearchKeyWord",
            "product-search/:categoryId":"productSearchByCategory", // not in use?
            "cart":"cart",
            "search/:keyWord":"search", // not in use ?
            "category-admin":"categoryAdmin",
            
        },
//        cart
        home:function () {
//	            var products=[{name:"Fish oil"},
//	                          {name:"Aloe"},
//	                          {name:"Multiple vitamins"},
//	                          {name:"Ensure Milk"}];
                utilities.viewManager.showView(new HomeView({el:$("#content")}));                
        },
        
        productDetail:function (id) {
//        	var product={"id":id,"description":"Black more oidless fish oil, 400 caples, 1000mg",
//	    		"name":"Fish oil","productNo":"PRD001","rrp":29.99,
//	    		"medias":[{"id":1,"itemSerial":null,"type":"image","url":"./resources/img/product/fish-oil.png"}],
//	    		"sales":[{"id":1,"endDate":1472029200000,"itemSerialNo":null,"price":19.99,"startDate":1469350800000}]};
        	
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
        	var paginationModel={};
        	paginationModel.pageNo = pageNo;
        	paginationModel.pageSize = pageSize;
        	paginationModel.dataSource=config.baseUrl + "rest/products";
        	paginationModel.dataSourceCount=config.baseUrl + "rest/products/count";
        	
        	utilities.viewManager.showView( new ProductPaginationView( {model:paginationModel, el:$("#content")} ));     	
        },
        
        products:function(){
        	this.productPagination(0,3);
        },
        
//        will be not in used
        search:function (keyWord) {
        	var searchModel = {};
        	searchModel.keyWord = keyWord;
        	searchView = new SearchView( {model:searchModel, el:$("#content")} )
        	utilities.viewManager.showView( searchView );
        },
        
        productSearch:function (  ){
        	var searchModel = {};
        	searchModel.keyWord = "";
        	searchModel.categoryId = "-1";
        	
        	var productSearchView = new ProductSearchView({ model:searchModel, el:$("#content") });
        	utilities.viewManager.showView( productSearchView );
        },

        productSearchKeyWord:function ( categoryId, keyWord ){
        	var searchModel = {};
        	searchModel.keyWord = keyWord;
        	searchModel.categoryId = categoryId;
        	
        	var productSearchView = new ProductSearchView({ model:searchModel, el:$("#content") });
        	utilities.viewManager.showView( productSearchView );
        },
        
        productSearchByCategory:function(){
        	
        },
        
        categoryAdmin:function(){
        	var categoryAdminView = new CategoryAdminView({ el:$("#content") });
        	utilities.viewManager.showView( categoryAdminView );
        }
        
    });
    
	// Create a router instance
	var router = new Router();
	
	return router;

});