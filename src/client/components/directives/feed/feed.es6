'use strict';

app.directive('feed', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'feed.html',
        scope: {},

        link(scope, element, attrs) {

            var feedHeight = () => {
                return ($(window).height() - 80) + 'px';
            };

            var setCurrentGroup = (group) => {
                State.setCurrentGroup(group);
                $state.go('group')
            };

            var init = () => {

            };

            init();

            scope.feedHeight = feedHeight;
            scope.setCurrentGroup = setCurrentGroup;
        }
    }
});
