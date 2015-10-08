'use strict';

var app = angular.module('app', ['ui.router']);

app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind('keydown keypress', function (event) {
            if (event.which !== 13) return;
            scope.$apply(function () {
                return scope.$eval(attrs.ngEnter, { $event: event });
            });
            event.preventDefault();
        });
    };
});
app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

    //var resolve = {
    //    timeout ($timeout) {
    //        $('[part]').addClass('inactive');
    //        $('.loading-logo').addClass('active');
    //        return $timeout(800);
    //    }
    //};

    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/home");

    // Now set up the states
    $stateProvider.state('home', {
        url: "/home",
        templateUrl: "home.html",
        controller: "HomeCtrl",
        //resolve: resolve,
        params: {}
    });

    //$locationProvider.html5Mode(true);
});
app.directive('login', function ($sce, $timeout) {
    return {
        templateUrl: 'login.html',
        scope: {},
        link: function link(scope, element, attrs) {
            console.log('login');

            var init = function init() {};

            init();
        }
    };
});

app.controller('HomeCtrl', function () {

    var init = function init() {};

    init();
});