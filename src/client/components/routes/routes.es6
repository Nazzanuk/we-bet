app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

    var resolve = {
        timeout: function ($timeout) {
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
    $stateProvider
        .state('home', {
            url: "/home",
            templateUrl: "home.html",
            controller: "HomeCtrl",
            resolve: resolve,
            params:  {
                section: null
            }
        })
        .state('blog', {
            url: "/explore",
            templateUrl: "blog.html",
            controller: "BlogCtrl",
            resolve: resolve
        })
        .state('single', {
            url: "/explore/:id/:slug",
            templateUrl: "single.html",
            controller: "SingleCtrl",
            resolve: resolve
        });

    $locationProvider.html5Mode(true);
});