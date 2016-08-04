/**
 * The landing view (or home view) set
 */
define([
    'utilities',
    'jquery',
    'bootstrap',
    'text!../../../../templates/desktop/home.html',
    'text!../../../../templates/desktop/product-icon.html'
], function (utilities,
		jquery,
		bootstrap,
		homeTemplate,
		productIcon) {

    var HomeView = Backbone.View.extend({
        render:function () {
            utilities.applyTemplate($(this.el),homeTemplate,{});
            $('.carousel').carousel();
            utilities.applyTemplate($('#featuredProducts'), productIcon)
            return this;
        }
    });

    return HomeView;
});