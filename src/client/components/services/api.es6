'use strict';

app.factory('API', function ($rootScope, $http) {

    var API_URL = "/api/";

    var login = (object) => {
        return $http.get(API_URL + "login/", {params : object}).then((response) => {
            console.log(response.data);
            return response.data;
        });
    };

    var getGroups = (object) => {
        return $http.get(API_URL + "groups/", {params : object}).then((response) => {
            console.log(response.data);
            return response.data;
        });
    };

    var getGroupById = (_id) => {
        return $http.get(`${API_URL}groups/_id/${_id}`, {}).then((response) => {
            console.log(response.data);
            return response.data;
        });
    };

    return {
        login: login,
        getGroups: getGroups,
        getGroupById: getGroupById
    }
});