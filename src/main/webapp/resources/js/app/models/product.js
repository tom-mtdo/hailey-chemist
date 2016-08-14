/**
 * Module for the Product model
 */
define([ 
    'configuration',
    'backbone'
], function (config) {
    /**
     * The Product model class definition
     * Used for CRUD operations against individual events
     */
    var Product = Backbone.Model.extend({
		urlRoot: 'http://localhost:8080/hailey-chemist/rest/products',
		idAttribute: "id"
//        urlRoot: config.baseUrl + 'rest/products' // the URL for performing CRUD operations
    });
    // export the Product class
    return Product;
});