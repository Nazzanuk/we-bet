'use strict';

app.directive('login', ($sce, API) => {
    return {
        templateUrl: 'login.html',
        scope: {},

        link(scope, element, attrs) {

            var login = (username, password) => {
                API.login({username: username, password: password}).then(response => console.log(response.data))
            };

            var init = () => {

            };

            init();

            scope.login = login;
        }
    }
});
