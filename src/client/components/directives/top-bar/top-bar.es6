'use strict';

app.directive('topBar', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'top-bar.html',
        scope: {},

        link(scope, element, attrs) {

            var init = () => {

            };

            init();

            scope.isLoggedIn = State.isLoggedIn;
            scope.isMenuVisible = State.isMenuVisible;
            scope.setMenuVisible = State.setMenuVisible;
        }
    }
});
