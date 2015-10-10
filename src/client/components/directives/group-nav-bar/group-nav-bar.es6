'use strict';

app.directive('groupNavBar', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'group-nav-bar.html',
        scope: {},

        link(scope, element, attrs) {

            var screen = 0;

            var setScreen = (index) => {
                screen = index;
            };

            var getScreen = () => {
                return screen;
            };

            var init = () => {

            };

            init();

            scope.setScreen = setScreen;
            scope.getScreen = getScreen;
            scope.isCurrentGroupNav = State.isCurrentGroupNav;
            scope.getCurrentGroupNav = State.getCurrentGroupNav;
            scope.setCurrentGroupNav = State.setCurrentGroupNav;
        }
    }
});
