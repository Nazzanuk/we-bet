'use strict';

app.directive('menuBar', (State) => {
    return {
        templateUrl: 'menu-bar.html',
        scope: {},

        link(scope, element, attrs) {

            var init = () => {

            };

            init();

            scope.logOut = State.logOut;
            scope.isMenuVisible = State.isMenuVisible;
            scope.setMenuVisible = State.setMenuVisible;
        }
    }
});
