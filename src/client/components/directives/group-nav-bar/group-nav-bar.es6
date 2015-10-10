'use strict';

app.directive('groupNavBar', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'group-nav-bar.html',
        scope: {},

        link(scope, element, attrs) {

            var init = () => {

            };

            init();

            scope.isCurrentGroupNav = State.isCurrentGroupNav;
            scope.getCurrentGroupNav = State.getCurrentGroupNav;
            scope.setCurrentGroupNav = State.setCurrentGroupNav;
        }
    }
});
