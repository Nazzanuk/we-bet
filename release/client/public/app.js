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

    var resolve = {
        timeout: function timeout($timeout) {
            $('[part]').addClass('inactive');
            $('.loading-logo').addClass('active');
            return $timeout(800);
        }
    };

    //$locationProvider.html5Mode(true)
    //    .hashPrefix('!');

    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/home");

    // Now set up the states
    $stateProvider.state('home', {
        url: "/home",
        templateUrl: "home.html",
        controller: "HomeCtrl",
        resolve: resolve,
        params: {
            section: null
        }
    }).state('blog', {
        url: "/explore",
        templateUrl: "blog.html",
        controller: "BlogCtrl",
        resolve: resolve
    }).state('single', {
        url: "/explore/:id/:slug",
        templateUrl: "single.html",
        controller: "SingleCtrl",
        resolve: resolve
    });

    $locationProvider.html5Mode(true);
});
app.controller('HomeCtrl', function (RestAPI, $scope, $element, $timeout, $stateParams) {

    var init = function init() {};

    init();
});