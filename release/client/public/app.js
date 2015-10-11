'use strict';

var app = angular.module('app', ['ui.router']).run(function () {
    FastClick.attach(document.body);
});;

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
        controller: "HomeCtrl",
        resolve: resolve
    }).state('group', {
        url: "/group/:_id",
        templateUrl: "group-screen.html",
        controller: "GroupCtrl",
        resolve: resolve
    });

    //$locationProvider.html5Mode(true);
});
app.controller('ScreenCtrl', function ($element, $timeout, State, $state) {

    var init = function init() {
        $timeout(function () {
            return $element.find('[screen]').addClass('active');
        }, 50);
    };

    init();
});

'use strict';

app.factory('Alert', function ($timeout, $rootScope) {

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

    $rootScope.Alert = {
        showMessage: showMessage,
        showError: showError
    };

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

    var getGroups = function getGroups(object) {
        return $http.get(API_URL + "groups/", { params: object }).then(function (response) {
            console.log(response.data);
            return response.data;
        });
    };

    var getGroupById = function getGroupById(_id) {
        return $http.get(API_URL + 'groups/_id/' + _id, {}).then(function (response) {
            console.log(response.data);
            return response.data;
        });
    };

    return {
        login: login,
        getGroups: getGroups,
        getGroupById: getGroupById
    };
});
'use strict';

app.factory('State', function ($rootScope, $http, $state) {

    var state = {
        currentNav: "groups",
        currentGroupNav: "leaderboard",
        loggedIn: false,
        menuVisible: false,
        currentGroup: ""
    };

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {

        if (state.loggedIn && toState.name == 'splash') {
            event.preventDefault();
        }

        if (!state.loggedIn && toState.name != 'splash') {
            event.preventDefault();
            $state.go('splash');
        }
    });

    var logOut = function logOut() {
        state.loggedIn = false;
        $state.go('splash');
    };

    var isCurrentNav = function isCurrentNav(name) {
        return state.currentNav == name;
    };

    var isCurrentGroupNav = function isCurrentGroupNav(name) {
        return state.currentGroupNav == name;
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
        isCurrentGroupNav: isCurrentGroupNav,
        isLoggedIn: getStateAttr('loggedIn'),
        logOut: logOut,

        getCurrentNav: getStateAttr('currentNav'),
        setCurrentNav: setStateAttr('currentNav'),

        getCurrentGroupNav: getStateAttr('currentGroupNav'),
        setCurrentGroupNav: setStateAttr('currentGroupNav'),

        setCurrentGroup: setStateAttr('currentGroup'),

        isMenuVisible: getStateAttr('menuVisible'),
        setMenuVisible: setStateAttr('menuVisible'),

        setLoggedIn: setStateAttr('loggedIn')
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

app.directive('groupHero', function ($timeout, API, $state, State) {
    return {
        templateUrl: 'group-hero.html',
        scope: {
            image: '=',
            title: '='
        },

        link: function link(scope, element, attrs) {

            var init = function init() {};

            init();
        }
    };
});

'use strict';

app.directive('feed', function ($timeout, API, $state, State) {
    return {
        templateUrl: 'feed.html',
        scope: {
            groups: '='
        },

        link: function link(scope, element, attrs) {

            var feedHeight = function feedHeight() {
                return $(window).height() - 80 + 'px';
            };

            (function () {})();

            scope.feedHeight = feedHeight;
        }
    };
});

'use strict';

app.directive('groupNavBar', function ($timeout, API, $state, State) {
    return {
        templateUrl: 'group-nav-bar.html',
        transclude: true,
        scope: {
            screens: '='
        },

        link: function link(scope, element, attrs) {

            var screens = scope.screens;
            var currentScreen = 0;

            var setScreenIndex = function setScreenIndex(index) {
                currentScreen = index;
            };

            var getScreenIndex = function getScreenIndex() {
                return currentScreen;
            };

            var getScreens = function getScreens() {
                return screens;
            };

            var isCurrentScreen = function isCurrentScreen(index) {
                return index == currentScreen;
            };

            var contentHeight = function contentHeight() {
                return $(window).height() - 80 + 'px';
            };

            var init = function init() {};

            init();

            scope.setScreenIndex = setScreenIndex;
            scope.getScreenIndex = getScreenIndex;
            scope.getScreens = getScreens;
            scope.isCurrentScreen = isCurrentScreen;
            scope.contentHeight = contentHeight;
        }
    };
});

'use strict';

app.directive('login', function ($timeout, API, $state, Alert, State) {
    return {
        templateUrl: 'login.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var login = function login(username, password) {
                API.login({ username: username, password: password }).then(function (response) {
                    if (response) {
                        State.setLoggedIn(true);
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

app.directive('menuBar', function (State) {
    return {
        templateUrl: 'menu-bar.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var init = function init() {};

            init();

            scope.logOut = State.logOut;
            scope.isMenuVisible = State.isMenuVisible;
            scope.setMenuVisible = State.setMenuVisible;
        }
    };
});

'use strict';

app.directive('topBar', function ($timeout, API, $state, State) {
    return {
        templateUrl: 'top-bar.html',
        scope: {},

        link: function link(scope, element, attrs) {

            var getCurrentPage = function getCurrentPage() {
                return $state.current.name;
            };

            var init = function init() {};

            init();

            scope.getCurrentPage = getCurrentPage;
            scope.isLoggedIn = State.isLoggedIn;
            scope.isMenuVisible = State.isMenuVisible;
            scope.setMenuVisible = State.setMenuVisible;
        }
    };
});

app.controller('GroupCtrl', function ($element, $timeout, API, $scope, State, $stateParams) {

    var group = {};

    var getGroup = function getGroup() {
        return group;
    };

    var loadGroup = function loadGroup() {
        API.getGroupById($stateParams._id).then(function (response) {
            group = response[0];
            $element.find('[screen]').addClass('active');
        });
    };

    var init = function init() {
        console.log($stateParams);
        loadGroup();
    };

    init();

    $scope.getGroup = getGroup;
});

app.controller('HomeCtrl', function ($element, $timeout, API, $scope, State, $state) {

    var groups = [];

    var getGroups = function getGroups() {
        return groups;
    };

    var loadGroups = function loadGroups() {
        API.getGroups({}).then(function (response) {
            groups = response;
            $element.find('[screen]').addClass('active');
        });
    };

    var init = function init() {
        loadGroups();
    };

    init();

    $scope.getGroups = getGroups;
});