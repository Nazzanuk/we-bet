'use strict';

app.directive('feed', ($timeout, API, $state) => {
    return {
        templateUrl: 'feed.html',
        scope: {},

        link(scope, element, attrs) {

            var feedHeight = () => {
                return ($(window).height() - 50) + 'px';
            };

            var init = () => {

            };

            init();

            scope.feedHeight = feedHeight;
        }
    }
});
