'use strict';

app.directive('navBar', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'nav-bar.html',
        scope: {},

        link(scope, element, attrs) {

            var init = () => {

            };

            init();

            scope.isCurrentNav = State.isCurrentNav;
            scope.getCurrentNav = State.getCurrentNav;
            scope.setCurrentNav = State.setCurrentNav;
        }
    }
});
