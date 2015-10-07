app.config(($stateProvider, $urlRouterProvider, $locationProvider) => {

    var resolve = {
        timeout ($timeout) {
            $('[part]').addClass('inactive');
            $('.loading-logo').addClass('active');
            return $timeout(800);
        }
    };

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
        });

    //$locationProvider.html5Mode(true);
});