/**
 * Module for the Products collection
 */
define([
    // The collection element type and configuration are dependencies
    'app/models/product',
    'configuration',
    'backbone'
], function (Product, config) {
    /**
     *  Here we define the Bookings collection
     *  We will use it for CRUD operations on Bookings
     */
    var Products = Backbone.Collection.extend({
    	model: Product,
    	url:'http://localhost:8080/hailey-chemist/rest/products'
//    	url: config.baseUrl + "rest/products", // the URL for performing CRUD operations
//        id:"id" // the 'id' property of the model is the identifier
    }); 
    return Products;
});