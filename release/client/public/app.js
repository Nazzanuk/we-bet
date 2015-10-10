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
app.controller('ScreenCtrl', function ($element, $timeout) {

    var init = function init() {
        $timeout(function () {
            return $element.find('[screen]').addClass('active');
        }, 50);
    };

    init();
});

app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

    var resolve = {
        timeout: function timeout($timeout) {
            $('[screen]').removeClass('active');
            //$('.loading-logo').addClass('active');
            return $timeout(300);
        }
    };

    // For any unmatched url, redirect to /
    $urlRouterProvider.otherwise("/");

    // Now set up the states
    $stateProvider.state('splash', {
        url: "/",
        templateUrl: "splash-screen.html",
        controller: "ScreenCtrl",
        resolve: resolve
    }).state('home', {
        url: "/home",
        templateUrl: "home-screen.html",
        controller: "ScreenCtrl",
        resolve: resolve
    });

    //$locationProvider.html5Mode(true);
});
'use strict';

app.factory('Alert', function ($timeout) {

    var active = false,
        message = false,
        colour = "primary",
        timeout;

    var showMessage = function showMessage(msg) {
        showError(msg);
        colour = "primary";
    };

    var showError = function showError(msg) {
        colour = "red";
        setActive(true);
        message = msg;
        $timeout.cancel(timeout);
        timeout = $timeout(function () {
            return setActive(false);
        }, 2000);
    };

    var getColour = function getColour() {
        return colour;
    };

    var getMessage = function getMessage() {
        return message;
    };

    var getActive = function getActive() {
        return active;
    };

    var setActive = function setActive(flag) {
        active = flag;
    };

    var switchActive = function switchActive() {
        active = !active;
    };

    var init = function init() {};

    init();

    return {
        showMessage: showMessage,
        showError: showError,
        getMessage: getMessage,
        getColour: getColour,
        getActive: getActive,
        setActive: setActive,
        switchActive: switchActive
    };
});
'use strict';

app.factory('API', function ($rootScope, $http) {

    var API_URL = "/api/";

    var login = function login(object) {
        return $http.get(API_URL + "login/", { params: object }).then(function (response) {
            console.log(response.data);
            return response.data;
        });
    };

    return {
        login: login
    };
});
'use strict';

app.factory('State', function ($rootScope, $http) {

    var state = {
        currentNav: "groups"
    };

    var isCurrentNav = function isCurrentNav(nav) {
        return state.currentNav == nav;
    };

    var getStateAttr = function getStateAttr(attr) {
        return function () {
            return state[attr];
        };
    };

    var setStateAttr = function setStateAttr(attr) {
        return function (value) {
            state[attr] = value;
            console.log('set state[' + attr + '] = ' + value);
        };
    };

    return {
        isCurrentNav: isCurrentNav,
        getCurrentNav: getStateAttr('currentNav'),
        setCurrentNav: setStateAttr('currentNav')
    };
});
'use strict';

app.directive('alert', function (Alert) {
    return {
        templateUrl: 'alert.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var init = function init() {};

            init();

            scope.getColour = Alert.getColour;
            scope.getMessage = Alert.getMessage;
            scope.getActive = Alert.getActive;
            scope.setActive = Alert.setActive;
            scope.switchActive = Alert.switchActive;
        }
    };
});

'use strict';

app.directive('login', function ($timeout, API, $state, Alert) {
    return {
        templateUrl: 'login.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var login = function login(username, password) {
                API.login({ username: username, password: password }).then(function (response) {
                    if (response) {
                        $state.go('home');
                        Alert.showMessage("Welcome!");
                    } else {
                        Alert.showError("Username and password didn't match");
                    }
                });
            };

            var init = function init() {};

            init();

            scope.login = login;
        }
    };
});

'use strict';

app.directive('feed', function ($timeout, API, $state) {
    return {
        templateUrl: 'feed.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var feedHeight = function feedHeight() {
                return $(window).height() - 50 + 'px';
            };

            var init = function init() {};

            init();

            scope.feedHeight = feedHeight;
        }
    };
});

'use strict';

app.directive('navBar', function ($timeout, API, $state, State) {
    return {
        templateUrl: 'nav-bar.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var init = function init() {};

            init();

            scope.isCurrentNav = State.isCurrentNav;
            scope.getCurrentNav = State.getCurrentNav;
            scope.setCurrentNav = State.setCurrentNav;
        }
    };
});