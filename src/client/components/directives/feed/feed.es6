'use strict';

app.directive('feed', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'feed.html',
        scope: {
            groups: '='
        },

        link(scope, element, attrs) {

            var feedHeight = () => {
                return ($(window).height() - 80) + 'px';
            };

            (() => {

            })();

            scope.feedHeight = feedHeight;
        }
    }
});
