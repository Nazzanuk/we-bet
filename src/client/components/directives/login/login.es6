'use strict';

app.directive('login', ($timeout, API, $state, Alert, State) => {
    return {
        templateUrl: 'login.html',
        scope: {},

        link(scope, element, attrs) {

            var login = (username, password) => {
                API.login({username: username, password: password}).then(response => {
                    if (response) {
                        State.setLoggedIn(true);
                        $state.go('home');
                        Alert.showMessage("Welcome!");
                    } else {
                        Alert.showError("Username and password didn't match");
                    }
                });
            };

            var init = () => {
            };

            init();

            scope.login = login;
        }
    }
});
