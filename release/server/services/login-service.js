'use strict';

module.exports = {

    isValidUser: function isValidUser(username, password) {
        var valid = false;

        valid = username == "nathan" && password == "nathan";

        return valid;
    }
};