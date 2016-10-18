define(['underscore', 'backbone'], function (_, Backbone) {

    
	var dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    var monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"];
    var event_bus = _.extend(Backbone.Events);

    Date.prototype.toPrettyString = function () {
        return dayNames[this.getDay()] + " " +
            this.getDate() + " " +
            monthNames[this.getMonth()] + " " +
            this.getFullYear() + " at " +
            this.getHours().toZeroPaddedString(2) + ":" +
            this.getMinutes().toZeroPaddedString(2);
    };

    Date.prototype.toPrettyStringWithoutTime = function () {
        return dayNames[this.getDay()] + " " +
            this.getDate() + " " +
            monthNames[this.getMonth()] + " " +
            this.getFullYear();
    };

    Date.prototype.toYMD = function () {
        return this.getFullYear() + '-' + (this.getMonth() + 1).toZeroPaddedString(2) + '-' + this.getDate().toZeroPaddedString(2);
    };

    Date.prototype.toCalendarDate = function () {
        return { 'day':this.getDate(), 'month':this.getMonth(), 'year':this.getFullYear()};
    };

    Date.prototype.withoutTimeOfDay = function () {
        return new Date(this.getFullYear(), this.getMonth(), this.getDate(), 0, 0, 0, 0);
    };

    Date.prototype.asArray = function () {
        return [this.getFullYear(), this.getMonth(), this.getDate()];
    };


    Date.prototype.toTimeOfDay = function () {
        return { 'hours':this.getHours(), 'minutes':this.getMinutes(),
            'seconds':this.getSeconds(), 'milliseconds':this.getMilliseconds()};
    };

    Date.prototype.diff = function (other) {
        return parseInt((this.withoutTimeOfDay().getTime() - other.withoutTimeOfDay().getTime()) / (1000.0 * 60 * 60 * 24));
    };

    Number.prototype.toZeroPaddedString = function (digits) {
        val = this + "";
        while (val.length < digits) val = "0" + val;
        return val;
    };

    Backbone.View.prototype.close = function(){
        $(this.el).empty();
        this.undelegateEvents();
        if (this.onClose){
            this.onClose();
        }
    };

    // utility functions for rendering templates
    var utilities = {
        viewManager:{
            currentView:null,

            showView:function (view) {
                if (this.currentView != null) {
                    this.currentView.close();
                }
                this.currentView = view;
                return this.currentView.render();
            }
        },
        renderTemplate:function (template, data) {
            return _.template(template, (data == undefined) ? {} : data);
        },
        applyTemplate:function (target, template, data) {
            return target.empty().append(this.renderTemplate(template, data));
        },
        displayAlert: function(msg) {
            if(navigator.notification) {
                navigator.notification.alert(msg);
            } else {
                alert(msg);
            }
        },
        
        setCookie:function (cname,cvalue,exdays) {
            var d = new Date();
            d.setTime(d.getTime() + (exdays*24*60*60*1000));
            var expires = "expires=" + d.toGMTString();
            document.cookie = cname+"="+cvalue+"; "+expires;
        },

        getCookie:function (cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for(var i=0; i<ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0)==' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        },

        clearCookie:function (cname) {
            document.cookie = cname + "={};expires=Thu, 01 Jan 1970 00:00:01 GMT;";
        },
        
        addToCart:function(cartLine){
//        	get cart from cookie
        	var strCart = this.getCookie("cart");
        	var cart = [];
        	
//        	if cart is null or empty then init cart with cartLine (input)
            if(!strCart){
            	cart = [cartLine];
            } else{
//            	convert to array of objects
                cart = JSON.parse(strCart);                
//        	if cart is not empty then            
//        		check if product exit in cart
                var found = false;
            	_.each(cart, function(line){
//        		if exist then increase/add quantity
            		if ( line.productId == cartLine.productId ){
            			line.quantity = Number(line.quantity) + Number(cartLine.quantity);
            			found = true;
            		}
            	});            		
//        		if not exist then add line to cart
            	if (!found) {
            		cart.push(cartLine);
            	}
            }
              
//    		convert cart to string to put to cookie
            strCart = JSON.stringify(cart);
//        	alert("new cart:" + strCart);
    		this.setCookie( "cart", strCart, 10);
        },
        
        removeFromCart:function(productId){
//        	get cart from cookie
        	var strCart = this.getCookie("cart");
        	var aCart = [];
        	var newCart = []; 
        	
//        	if cart is not empty then
            if(strCart && (strCart.trim().length>0) ){
//            	convert to array of objects
                aCart = JSON.parse(strCart);
//                remove item from cart
                newCart = aCart.filter(function(element){
                	return element.productId !== productId;
                });
//              store update cart to cookie:
//        		convert cart to string to put to cookie
                strCart = JSON.stringify(newCart);
//            	alert("new cart:" + strCart);
        		this.setCookie( "cart", strCart, 10);
            }
        },
        
        updateCart:function(cartLine){
//        	get cart from cookie
        	var strCart = this.getCookie("cart");
        	var aCart = [];
        	
//        	if cart is not empty then
            if(strCart && (strCart.trim().length>0) ){
//            	convert to array of objects
                aCart = JSON.parse(strCart);                
            
//        		check if product exit in cart
            	_.each(aCart, function(line){
//        		if exist then update quantity
            		if ( line.productId == cartLine.productId ){
            			line.quantity = cartLine.quantity;
            		}
            	});            		
//        		convert cart to string to put to cookie
                strCart = JSON.stringify(aCart);
//            	alert("new cart:" + strCart);
        		this.setCookie( "cart", strCart, 10);        	

            }
        },
        
        pagination:{
            getPageFirstItem:function (pageNo, pageSize) {
            	return pageNo*pageSize;
            },
            
            getPageLastItem:function (pageNo, pageSize, count) {
            	var i = parseInt(this.getPageFirstItem(pageNo,pageSize)) + parseInt(pageSize) - 1;
                var count = count - 1;
                if (i > count) {
                    i = count;
                }
                if (i < 0) {
                    i = 0;
                }

                return i;            	
            },
            
            isHasNextPage:function(pageNo, pageSize, count){
            	return parseInt( (pageNo+1)*pageSize ) + 1 <= parseInt(count);
            },
            
            isHasPreviousPage:function(pageNo){
            	return parseInt( pageNo ) > 0;
            }
        },
        
        getEventBus:function(){
        	return event_bus;
        }

    };

    return utilities;

});