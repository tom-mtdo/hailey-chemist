/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'text!../../../../templates/desktop/home.html'
], function (utilities, 
		homeTemplate) {

    var HomeView = Backbone.View.extend({
        render:function () {
            utilities.applyTemplate($(this.el),homeTemplate,{});
            return this;
        }
    });

    return HomeView;
});