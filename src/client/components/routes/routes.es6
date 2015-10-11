app.config(($stateProvider, $urlRouterProvider, $locationProvider) => {

    var resolve = {
        timeout($timeout) {
            $('[screen]').removeClass('active');
            //$('.loading-logo').addClass('active');
            return $timeout(300);
        }
    };

    // For any unmatched url, redirect to /
    $urlRouterProvider.otherwise("/");

    // Now set up the states
    $stateProvider
        .state('splash', {
            url: "/",
            templateUrl: "splash-screen.html",
            controller: "ScreenCtrl",
            resolve: resolve
        })
        .state('home', {
            url: "/home",
            templateUrl: "home-screen.html",
            controller: "HomeCtrl",
            resolve: resolve
        })
        .state('group', {
            url: "/group/:_id",
            templateUrl: "group-screen.html",
            controller: "GroupCtrl",
            resolve: resolve
        });

    //$locationProvider.html5Mode(true);
});