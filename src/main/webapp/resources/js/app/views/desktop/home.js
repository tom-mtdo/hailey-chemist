/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'text!../../../../templates/desktop/home.html'
], function (utilities,
		jquery,
		bootstrap,
		homeTemplate) {

    var HomeView = Backbone.View.extend({
        render:function () {
            utilities.applyTemplate($(this.el),homeTemplate,{});
            $('.carousel').carousel();
            return this;
        }
    });

    return HomeView;
});