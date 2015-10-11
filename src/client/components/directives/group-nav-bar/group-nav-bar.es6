'use strict';

app.directive('groupNavBar', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'group-nav-bar.html',
        transclude: true,
        scope: {
            screens: '='
        },

        link(scope, element, attrs) {

            var screens = scope.screens;
            var currentScreen = 0;

            var setScreenIndex = (index) => {
                currentScreen = index;
            };

            var getScreenIndex = () => {
                return currentScreen;
            };

            var getScreens = () => {
                return screens;
            };

            var isCurrentScreen = (index) => {
                return index == currentScreen;
            };

            var contentHeight = () => {
                return ($(window).height() - 80) + 'px';
            };

            var init = () => {

            };

            init();

            scope.setScreenIndex = setScreenIndex;
            scope.getScreenIndex = getScreenIndex;
            scope.getScreens = getScreens;
            scope.isCurrentScreen = isCurrentScreen;
            scope.contentHeight = contentHeight;
        }
    }
});
