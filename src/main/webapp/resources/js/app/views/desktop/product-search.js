/**
 * input: productSearchModel{categoryId,  and query para e.g. keyword}
 * maybe: pageNo, pageSize,
 */

define([
        'utilities',
        'jquery',
        'bootstrap',
        'configuration',
        'app/views/desktop/product-page',
        'app/views/desktop/product-pagination',
        'text!../../../../templates/desktop/product-search.html',
        'text!../../../../templates/desktop/product-count-by-category.html',

        ], function (utilities,
        		jquery,
        		bootstrap,
        		config,
        		ProductPageView,
        		ProductPaginationView,
        		productSearchTemplate,
        		productCountByCategoryTemplate) {

	var ProductSearchView = Backbone.View.extend({
    	initialize: function(){
    		this.model = {};
    		this.model.keyWord = "";
    		this.model.categoryId = "-1";
//    		this.model.on('change', this.render, this);
    	},
    	    			
    	events:{
    		"keypress #txtSearchKeyWord":"updateOnEnter",
//    		"blur  #txtSearchKeyWord":"updateModel",
    	},
		
		render:function(){
			var self = this;
			var strUrl="";
				
			if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
				strUrl="http://localhost:8080/hailey-chemist/rest/product-search/-1/pathCount" + "?keyWord=" + self.model.keyWord.trim();
			} else {
				strUrl="http://localhost:8080/hailey-chemist/rest/product-search/-1/pathCount";
			}
			$.getJSON(strUrl, function( productCountByCategories ){
//				show categories and number of its products
				utilities.applyTemplate( $(self.el), productSearchTemplate, {} );

//				category with path and count product
				self.productCountByCategoryView = new ProductCountByCategoryView({model:productCountByCategories, el:$("#divProductCountByCategory") });
				self.productCountByCategoryView.render();

				//				Show list of product as search result
				var paginationModel={};
				paginationModel.pageNo = 0;
				paginationModel.pageSize = 3;
//				get all product for now
				if ( self.model.keyWord && (self.model.keyWord.trim().length>0) ) {
					paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/-1?keyWord=" + self.model.keyWord.trim();
//					alert("Data source: " + paginationModel.dataSource);
					paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/-1/count?keyWord=" + self.model.keyWord.trim();
				} else {
					paginationModel.dataSource="http://localhost:8080/hailey-chemist/rest/product-search/-1";
					paginationModel.dataSourceCount="http://localhost:8080/hailey-chemist/rest/product-search/-1/count";
				}
				self.productPaginationView = new ProductPaginationView( {model:paginationModel, el:$("#divSearchProductPagination")} );	        	

				self.productPaginationView.render();
			});

			return self;
		},
		
//		working on this +++++++++++++++
		updateModel:function(){
			var value = this.$("#txtSearchKeyWord").val().trim();
	        if(value) {
	          this.model.keyWord = value;
//	          alert("KeyWord: " + value);
//	          need to change to use event listener to render
	          this.render();
	        }
		},
		
		updateOnEnter:function(event){
			if ( event.which == 13 ) {
				this.updateModel();
			}
		}
		
	});

	var ProductCountByCategoryView = Backbone.View.extend({
		render:function(){
			var self = this;
			utilities.applyTemplate( $(self.el), productCountByCategoryTemplate, {productCountByCategories: self.model} );
			return self;
		}
	});


	return ProductSearchView;

});