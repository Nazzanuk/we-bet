'use strict';

app.directive('login', ($timeout, API, $state) => {
    return {
        templateUrl: 'login.html',
        scope: {},

        link(scope, element, attrs) {

            var login = (username, password) => {
                API.login({username: username, password: password}).then(response => {
                    if (response) $state.go('home')
                });
            };

            var init = () => {
            };

            init();

            scope.login = login;
        }
    }
});
