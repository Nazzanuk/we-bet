'use strict';

app.factory('API', function ($rootScope, $http) {

    var API_URL = "/api/";

    var login = (object) => {
        return $http.get(API_URL + "login/", {params : object}).then((response) => {
            console.log(response.data);
            return response.data;
        });
    };

    return {
        login: login
    }
});