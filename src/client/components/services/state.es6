'use strict';

app.factory('State', function ($rootScope, $http, $state) {

    var state = {
        currentNav: "groups",
        loggedIn: false,
        menuVisible: false
    };

    var logOut = () => {
        state.loggedIn = false;
        $state.go('splash');
    };

    var isCurrentNav = (nav) => {
        return state.currentNav == nav;
    };

    var getStateAttr = (attr) => {
        return () => {
            return state[attr];
        };
    };

    var setStateAttr = (attr) => {
        return (value) => {
            state[attr] = value;
            console.log(`set state[${attr}] = ${value}`);
        }
    };

    return {
        isCurrentNav: isCurrentNav,
        isLoggedIn: getStateAttr('loggedIn'),
        logOut: logOut,
        getCurrentNav: getStateAttr('currentNav'),
        setCurrentNav: setStateAttr('currentNav'),
        isMenuVisible: getStateAttr('menuVisible'),
        setMenuVisible: setStateAttr('menuVisible'),
        setLoggedIn: setStateAttr('loggedIn')
    }
});