'use strict';

app.directive('groupHero', ($timeout, API, $state, State) => {
    return {
        templateUrl: 'group-hero.html',
        scope: {
            image:'=',
            title:'='
        },

        link(scope, element, attrs) {

            var init = () => {

            };

            init();
        }
    }
});
