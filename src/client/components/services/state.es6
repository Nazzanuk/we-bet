'use strict';

app.factory('State', function ($rootScope, $http) {

    var state = {
        currentNav:"groups"
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
        getCurrentNav : getStateAttr('currentNav'),
        setCurrentNav : setStateAttr('currentNav')
    }
});